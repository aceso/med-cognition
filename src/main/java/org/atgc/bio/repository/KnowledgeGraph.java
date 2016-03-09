/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.Drug;
import org.atgc.bio.domain.IndexNames;
import org.atgc.bio.meta.AnnotationTypes;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.init.Config;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;

/**
 * KnowledgeGraph - Traversal
 * @author jtanisha-ee
 *         Use BioFields to fetch BioEntities and Nodes
 *
 *         References:
 *  https://github.com/neo4j/neo4j/blob/2.3.2/community/embedded-examples/src/main/java/org/neo4j/examples/NewMatrix.java
 *  http://www.informit.com/articles/article.aspx?p=2415371#articleDiscussion
 *
 *  copying subgraph or entire db from srcDb to destDb
 *      https://gist.github.com/kenahoo-windlogics/036639a7061877acc520
 *
 *  evaluators examples
 *  https://github.com/maxdemarzi/neo_airlines
 *
 */
@SuppressWarnings("javadoc")
public class KnowledgeGraph {

    /* change this path while running this program */
   // private static final String dbPath = "/Users/smitha/Documents/Neo4j/default.graphdb";
    //private static final String destDbPath = "/Users/smitha/Documents/Neo4j-subgraph/default.graphdb";
    private static GraphDatabaseService graphDb, destGraphDb;
    protected static final Logger log = LogManager.getLogger(KnowledgeGraph.class);
    private static HashMap<Node, CacheGraphPath> cacheListPath;

    /**
     * cacheGraphPath
     */
    public static class CacheGraphPath {
        Node startNode;
        Path path;
        int totalEntities;
        Double numMatches;
        Double ratio;
    }

    /* public KnowledgeGraph() {
        cacheListPath = new ArrayList<CacheGraphPath>();
    } */

    /**
     * setup
     * @throws URISyntaxException
     */
    private static void setup() throws URISyntaxException {


        try {
            // File dbFile = new File(dbPath.toString());
            graphDb = BioEntityTemplate.getGraphDb();
            File destDbFile = new File(Config.DEST_DB_PATH.toString());
            // graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);
             destGraphDb = new GraphDatabaseFactory().newEmbeddedDatabase(destDbFile);
        } catch (Exception ex) {
           log.error("Could not initialize RestGraphDatabase ", ex);
           throw new RuntimeException("uri", ex);
        }
    }


    private static void registerShutdownHook() {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime()
            .addShutdownHook( new Thread() {
            @Override
            public void run() {
                //graphDb.shutdown();
                destGraphDb.shutdown();
           }
        });
    }



    public static void main(String[] args) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException {
        setup();
        Transaction tx = graphDb.beginTx();
        Transaction destTx = destGraphDb.beginTx();
        try {
            // specify depth
            getIntelligentPaths(6);
            tx.success();
            destTx.success();
        } catch(Exception e) {
            tx.failure(); //rollback
            destTx.failure();
            throw new RuntimeException("Something went wrong with access while accessing bioentity. msg=" + e.getMessage(), e);
        } finally {
            tx.close();
            destTx.close();
        }
    }

    public static Node getStartNode(String bioType, String propName, String propValue) {
        Node node = getNode(bioType, propName, propValue);
        return node;
    }

    /**
     *
     message:
     ProteinSequence-P01588-C91F0E4C26A52033-193
     entryVersion:
     143
     molecularWeight:
     21307
     sequenceSum:
     C91F0E4C26A52033
     sequenceLength:
     193
     nodeType:
     ProteinSequence

     Protein information
     <id>:
     2948
     message:
     Protein-P01588
     interactorId:
     360144
     shortLabel:
     epo_human
     ncbiTaxId:
     9606
     intactId:
     360144
     nodeType:
     Protein
     uniprotEntryType:
     Swiss-Prot
     uniprot:
     P01588

     NciBiTaxonomy
     <id>:
     189
     message:
     NcbiTaxonomy-NCBITaxon:9606-Homo sapiens
     name:
     Homo sapiens
     ncbiTaxId:
     NCBITaxon:9606
     xref:
     GC_ID:1
     taxonomyRank:
     species


     * model Etanercept
     * @throws UnsupportedEncodingException
     */
    public static void getIntelligentPaths(int depth) throws Exception {

        Drug bioDrug = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "Epoetin alfa");
        Node drug = BioEntityTemplate.getNode(bioDrug);
        if (drug != null) {
            System.out.println("drug=" + drug.getLabels().toString() + drug.getId());
        }

        cacheListPath = new HashMap<>();
        Node protein = getNode(BioTypes.PROTEIN.toString(), BioFields.UNIPROT_ID.toString(), "P01877");
        if (protein != null) {
            System.out.println("protein=" + protein.getLabels().toString() + protein.getId());
        }

        // nodeid
        Node urokinaseDrug = getNode(BioTypes.DRUG.toString(), BioFields.DRUG_NAME.toString(), "Urokinase");
        if (urokinaseDrug!= null) {
            System.out.println("urokinaseDrug=" + urokinaseDrug.getLabels().toString() + urokinaseDrug.getId());
        }

        // nodeid
        Node ncbiTaxonomy = getNode(BioTypes.NCBI_TAXONOMY.toString(), BioFields.NCBI_TAX_ID.toString(), "NCBITaxon:9606");
        if (ncbiTaxonomy != null) {
            System.out.println("ncbiTaxonomy=" + ncbiTaxonomy.getLabels().toString() + ncbiTaxonomy.getId());
        }

        // Erlonitib
        /*
        Node pubmed = getNode(BioTypes.PUBMED.toString(), IndexNames.PUBMED_ID.toString(), "17041093");
       // Node gene = getNode(BioTypes.GENE.toString(), IndexNames.GENE_ID.toString(), "9606-MAPK1");


        if (pubmed != null) {
            System.out.println("protein" + pubmed.getLabels().toString() + pubmed.getId());
        }

        if (gene != null) {
            System.out.println("gene" + gene.getLabels().toString() + gene.getId());
        }
        */

        List<BioRelTypes> relTypes = new ArrayList<>();
        relTypes.add(BioRelTypes.DRUG_MANUFACTURED_BY);
        relTypes.add(BioRelTypes.DRUG_PACKAGED_BY);
        List<Node> entityList = getEntityInteractors(protein, urokinaseDrug, ncbiTaxonomy);
         for (Node node : entityList)  {
             getGraphPath(entityList, drug, depth, relTypes);
             if (cacheListPath.size() == entityList.size()) {
                 break;
             }
        }
        setRatio(entityList);
        for (CacheGraphPath cachePath : cacheListPath.values()) {
            log.info("cachePath =" + "path=" + cachePath.path);
            log.info("ratio = " + cachePath.ratio);
        }
        for (Node node: cacheListPath.keySet()) {
            log.info("node =" + node.getId());
        }
        saveSubGraph();
    }



    // cetuximab:
    // compound104.xml, compound1146.xml

    // erlotinib:DB00530 (drugbank),
    // compound4176.xml, compound583.xml, compound968.xml
    // compound974.xml, compound750.xml, compound891.xml
    // compound583.xml, compound4716.xml, compound4093.xml
    //
    // hugogene: MAPK1, MAPK3,
    // uniprotid: P28482
    // geneterm: erk (extracellular signal-regulated kinase), erk1
    // pubmedid: 18723475 (drugbank), 17041093, 16937104

    // pubmedid: 16217753 (erk1/MAPK3),15657067
    // Gene: (HugoGeneSymbol)9606-MAPK1
    //
    public static List<Node> getEntityInteractors(Node node1, Node node2, Node node3) {
        List<Node> list = new ArrayList<>();
        try {
            list.add(node1);
            list.add(node2);
            list.add(node3);
        } catch(Exception e) {
            log.error("error retrieving BioEntity protein " + e.getMessage(), e);
        }
        return list;
    }

    public static Node getNode(String bioType, String propName, String propValue) {
        return graphDb.findNode(DynamicLabel.label(bioType), propName, propValue);
    }

    /**
     * getGraphPath
     * uses PathExpander and Evaluator
     */
    public static void getGraphPath(List<Node> list, Node node, int depth,
                                    List<BioRelTypes> excludeRelTypes) throws UnsupportedEncodingException {
        TraversalDescription td = graphDb.traversalDescription()
        //for (Path path : graphDb.traversalDescription()
            // Choose a depth-first search strategy
            .breadthFirst()
            .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
            //.uniqueness(Uniqueness.NODE_GLOBAL)
            .expand( new PathExpander<Object>() {
                 @Override
                 public Iterable<Relationship> expand(Path path, BranchState<Object> objectBranchState) {
                      // Get the depth of this node
                     /*
                    int length = path.length();
                                for (Node node : list) {
                                    if (isNodeIncluded(path.endNode(), node)) {
                                        for (BioRelTypes relType : excludeRelTypes) {
                                            if (isRelationship(path.endNode(), relType)) {
                                                System.out.println("relType =" + relType + " endNodetype" + path.endNode().getId());
                                                //return path.endNode().getRelationships();

                                                return Collections.emptyList();
                                        }
                                    }
                                }
                            }*/

                     return path.endNode().getRelationships();
                 }
                 @Override
                 public PathExpander<Object> reverse() {
                     return null;
                 }
            })
            //.evaluator(Evaluators.excludeStartPosition() )
            //.evaluator(Evaluators.toDepth(depth))
            .evaluator(new KnowledgeEntityEvaluator(list, excludeRelTypes));
        getNewKnowledge(td, node, list);
    }

    private static String getLabel(Node n) {
        Object obj = n.getProperty(BioFields.MESSAGE.toString());
        if (obj != null)
            return obj.toString();
        return null;
    }

    /*
    private static String getLabel(Node n) {
        Iterator<Label> it = n.getLabels().iterator();
        if (it.hasNext())
            return it.next().toString();
        return null;
    }*/

    /**
     *
     * @param path
     * @param list
     * @param node
     * @return
     */
    public static void addPath(Path path, List<Node> list, Node node) {
        log.info("\n\n\t addPath()");
        for (Node n: path.nodes()) {
            int matches = 0;
            getCount(path, list);
            for (Node targetNode : list) {
                String pathNodeLabel = getLabel(n);
                String targetLabel = getLabel(targetNode);
                if (pathNodeLabel.equalsIgnoreCase(targetLabel)) {
                    matches++;
                    log.info("addPath(), matches " + targetLabel + ", targetNodeid" + targetNode.getId() + " in path=" + path);
                    CacheGraphPath cachePath = new CacheGraphPath();
                    cachePath.path = path;
                    cachePath.startNode = node;
                    cachePath.totalEntities = path.length();
                    cacheListPath.put(targetNode, cachePath);
                }
            }
        }
    }

    private static void getNewKnowledge(TraversalDescription td, Node node, List<Node> list) {
        log.info("getNewKnowledge() ");
        int numNodes = 0;
        Traverser traverser = td.evaluator(Evaluators.toDepth(6))
                .traverse(node);
        ResourceIterator<Path> iterator = traverser.iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            log.info("getNewKnowledge(), traverser(), path.length=" + path.length() + ", path=" + path);
            addPath(path, list, node);
        }
    }

    private static double getCount(Path path, List<Node> list) {
        double matches = 0;
        for (Node n: path.nodes()) {
            for (Node k : list) {
                String pathNodeLabel = getLabel(n);
                String kLabel = getLabel(k);
                if (pathNodeLabel.equalsIgnoreCase(kLabel)) {
                    matches++;
                }
            }
        }
        return matches;
    }

    private static void setRatio(List<Node> list) {
        double numMatches = 0.0;
        for (CacheGraphPath cachePath : cacheListPath.values()) {
            for (Node n: cachePath.path.nodes()) {
                for (Node k : list) {
                    String pathNodeLabel = getLabel(n);
                    String kLabel = getLabel(k);
                    if (pathNodeLabel.equalsIgnoreCase(kLabel)) {
                        numMatches++;
                    }
                }
                cachePath.ratio = numMatches / (double)cachePath.path.length();
                cachePath.numMatches = numMatches;
            }
        }
    }

    private static boolean isNodeIncluded(Node node, Node dNode) {
        String srcLabel = getLabel(node);
        String destLabel = getLabel(dNode);
        if (srcLabel.equalsIgnoreCase(destLabel))
            return true;
        return false;
    }

    /**
     * checks if relationship exists
     * @param pathEndNode
     * @param relType
     * @return
     */
    private static boolean isRelationship(Node pathEndNode, BioRelTypes relType) {

        Iterable<Relationship> iterable = pathEndNode.getRelationships(
                DynamicRelationshipType.withName(URLEncoder.encode(
                        relType.toString())));

        while (iterable.iterator().hasNext()) {
            log.info("relationship =" + iterable.iterator().next().getType().toString());
            return true;
        }
        return iterable.iterator().hasNext() ? true : false;
    }

    public static void saveSubGraph() {
       for (CacheGraphPath cachePath : cacheListPath.values()) {
            log.info("nodes tagged with label experiment1 in path " + cachePath.path);
            for (Node node : cachePath.path.nodes()) {
                node.addLabel(DynamicLabel.label("Experiment1"));
                //BioEntity bioEntity = (BioEntity) BioEntityTemplate.getBioEntity(node);
                //Subgraph subgraph = new Subgraph();
                //subgraph.add(bioEntity);
                //BioEntityTemplate.saveSubgraph(subgraph);
            }
        }
    }

    /* save into another db */
    public static void saveSubGraph2(TraversalDescription td, Node node) throws Exception {
        Subgraph subGraph = new Subgraph();
        log.info("************* saveSubGraph()");
        for (Path path : td.traverse(node)) {
            log.info("path.length()" + path.length() + " path=" + path);
                for (Node pNode : path.nodes()) {
                    System.out.println("node =" + pNode.getLabels().toString());
                    Node n = graphDb.getNodeById(pNode.getId());
                    if (n == null) {
                        log.error("node is null, in path.nodes()" + path);
                        break;
                    }
                    Iterator<Label> iterator = n.getLabels().iterator();
                    Label label = iterator.next();
                    log.info("label=" + label.name());

                    Node destNode = destGraphDb.createNode(label);
                    destNode.addLabel(DynamicLabel.label("Innovation1"));
                    setNodeProperties(destNode, n.getAllProperties());
                }

                log.info("begin relationships");
                Iterator<Relationship> iterator = path.relationships().iterator();
                while (iterator.hasNext()) {
                    Relationship r = iterator.next();
                    Node endNode = r.getEndNode();
                    Iterator<Label> it = endNode.getLabels().iterator();
                    Label endNodeLabel = it.next();

                    Node startNode = r.getStartNode();
                    it = startNode.getLabels().iterator();
                    Label startNodeLabel = it.next();

                    log.info("startNode =" + startNodeLabel);
                    log.info("relation =" + r.getType().name());
                    log.info("endNode =" +  endNodeLabel);

                    Node destStartNode = destGraphDb.findNode(startNodeLabel, "name", startNode.getProperty("name"));
                    Node destEndNode = destGraphDb.findNode(endNodeLabel, "name", endNode.getProperty("name"));

                    if (destStartNode != null && destEndNode != null)  {
                        setNodeProperties(destEndNode, endNode.getAllProperties());
                        Relationship destRel = destStartNode.createRelationshipTo(destEndNode, DynamicRelationshipType.withName(r.getType().name()));
                        Map<String, Object> properties = r.getAllProperties();
                        properties.forEach((k, v) -> {
                            destRel.setProperty(k, v);
                        });
                    }
                }
            }
        //}
    }

    private static void setNodeProperties(Node n, Map<String, Object> properties ) {
        properties.forEach( (k,v) -> {
            n.setProperty(k, v);
        });
    }

    /**
     *
     * gene and intact experiments
     *  Participant  (bioentity)
     <id>:
     227908
     message:
     Participant-942176-n/a
     intactParticipantId:
     EBI-4409209


     IntactGene (bioentity)
     <id>:
     216220
     geneSymbol:
     mdm2
     message:
     IntactGene-mdm2-mdm2_human_gene


     ExperimentalRole (bioentity)
     <id>:
     227992
     message:
     ExperimentalRole-942176-prey
     shortLabel:
     prey
     participantId:
     942176

     */

}
