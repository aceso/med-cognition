/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.meta.BioEntity;
import org.atgc.init.Config;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.BranchState;
import org.neo4j.graphdb.traversal.Evaluators;
import org.neo4j.graphdb.traversal.TraversalDescription;
import org.neo4j.graphdb.traversal.Traverser;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

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
    private static final String dbPath = "/Users/smitha/Documents/Neo4j-IE/default.graphdb";
    private static final String destDbPath = "/Users/smitha/Documents/Neo4j-subgraph/default.graphdb";
    private static GraphDatabaseService graphDb, destGraphDb;
    protected static final Logger log = LogManager.getLogger(KnowledgeGraph.class);


    /**
     * setup
     * @throws URISyntaxException
     */
    private static void setup() throws URISyntaxException {
        try {
             File dbFile = new File(Config.DB_PATH.toString());
             File destDbFile = new File(Config.DEST_DB_PATH.toString());
             graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);
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
                graphDb.shutdown();
                destGraphDb.shutdown();
           }
        });
    }



    public static void main(String[] args) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException {
        setup();
        Transaction tx = graphDb.beginTx();
        Transaction destTx = destGraphDb.beginTx();
        try {
           // createGraph();
           // getDepthFromProtein();
           // traverseGraph();
            // bioType, propertyName, propertyValue
            for (int i = 0; i < 5; i++) {
                getGraphPath(getList(), getStartNode(args[1], args[2], args[3]), i);
            }
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
        Node node = getNode(DynamicLabel.label(bioType), propName, propValue);
        return node;
    }

    public static List<BioEntity> getList() {
        List<BioEntity> list = new ArrayList<>();
        try {
            list.add(PersistenceTemplate.getBioEntity(BioTypes.DRUG, "Lapatinib"));
            list.add(PersistenceTemplate.getBioEntity(BioTypes.GENE_ONTOLOGY, "GO:0000002"));
            // this enzyme deals with breast cancer
            // 3.4.22.43
            list.add(PersistenceTemplate.getBioEntity(BioTypes.ENZYME, "3.4.21.109"));
        } catch(Exception e) {
            log.error("error retrieving BioEntity protein " + e.getMessage(), e);
        }
        return list;
    }

    public static Node getNode(Label label, String propName, String propValue) {
        return graphDb.findNode(label, propName, propValue);
    }

    /**
     * getGraphPath
     * uses PathExpander and Evaluator
     */
    public static void getGraphPath(List<BioEntity> list, Node node, int depth) throws UnsupportedEncodingException {
        System.out.println("getGraphPath using PathExpander");
        TraversalDescription td = graphDb.traversalDescription()
            // Choose a depth-first search strategy
            .breadthFirst()
            .expand( new PathExpander<Object>() {
                 @Override
                 public Iterable<Relationship> expand(Path path, BranchState<Object> objectBranchState) {
                      // Get the depth of this node
                      int depth = path.length();
                      System.out.println("depth =" + depth);
                      return path.endNode().getRelationships();
                 }
                 @Override
                 public PathExpander<Object> reverse() {
                     return null;
                 }
            })
            .evaluator( Evaluators.atDepth( depth ))
            .evaluator(new KnowledgeEntityEvaluator(list));
           /*     @Override
                public Evaluation evaluate(Path path) {
                    System.out.println("evaluate path=" + path);
                    if (isNodeIncluded(path.endNode(), enzyme2)) {
                        System.out.println("enzyme2 exists " + path.endNode().getProperty("name"));
                            // return Evaluation.INCLUDE_AND_CONTINUE;
                    }
                    if (isRelationship(path.endNode(), BioRelTypes.NEGATIVELY_REGULATES)) {
                        System.out.println("negatively regulates relationship exists, node=" +path.endNode().getProperty("name"));
                            return Evaluation.INCLUDE_AND_CONTINUE;
                    }

                return Evaluation.EXCLUDE_AND_CONTINUE;
                }*/
       // });

        getNewKnowledge(td, node);
        //System.out.println("saveSubGraph");
    }

    public static void getNewKnowledge(TraversalDescription td, Node node) {

        System.out.println("\n\n\t getNewKnowledge()");
        Traverser traverser = td.traverse(node);
        //saveSubGraph(td, node);
    }

    private static boolean isNodeIncluded(Node node, Node dNode) {
        System.out.println("nodeId match check" + node.getId() +  "== " + dNode.getId());
        if (node.getId() == dNode.getId() &&
                node.getLabels().toString().equalsIgnoreCase(dNode.getLabels().toString())) {
            return true;
        }
        return false;
    }


    // Node enzyme2 = getNode(DynamicLabel.label(BioTypes.ENZYME.toString()), "name", "Enzyme2");
    // Node enzyme3 = getNode(DynamicLabel.label(BioTypes.ENZYME.toString()), "name", "Enzyme3");
    public enum Labels implements Label {
        PROTEIN,
        PUBMED,
        GENE
    }

    public enum RelationshipTypes implements RelationshipType {
        INTERACTS_WITH,
        REFERENCES_PUBMED;
    }

    public static Relationship setPubMedRelation(Node protein, Node pubmed, int ref) {
        Relationship relationship = protein.createRelationshipTo( pubmed, RelationshipTypes.REFERENCES_PUBMED );
        relationship.setProperty( "primaryRef", ref );
        return relationship;
    }


    /**
     * @throws IllegalAccessException
     */
    public static void createGraphTest() throws IllegalAccessException, UnsupportedEncodingException {
        Node node = graphDb.createNode(Labels.PROTEIN);
        node.setProperty("name", "BRCA1");
        node.addLabel(Labels.PROTEIN);
        System.out.println("node ="  + node.getId());

        Node proteinP53 = graphDb.createNode(Labels.PROTEIN);
        proteinP53.setProperty("name", "P53");

        Node proteinIL = graphDb.createNode(Labels.PROTEIN);
        proteinIL.setProperty("name", "IL");

        Node proteinBRCA2 = graphDb.createNode(Labels.PROTEIN);
        proteinBRCA2.setProperty("name", "BRCA2");

        Node proteinERBB = graphDb.createNode(Labels.PROTEIN);
        proteinERBB.setProperty("name", "ERBB");

        /* add labels */
        proteinERBB.addLabel(Labels.PROTEIN);
        proteinBRCA2.addLabel(Labels.PROTEIN);
        proteinIL.addLabel(Labels.PROTEIN);
        proteinP53.addLabel(Labels.PROTEIN);
        System.out.println("completed protein");

        node.createRelationshipTo(proteinP53, RelationshipTypes.INTERACTS_WITH);
        node.createRelationshipTo(proteinIL, RelationshipTypes.INTERACTS_WITH);
        node.createRelationshipTo(proteinBRCA2, RelationshipTypes.INTERACTS_WITH);
        node.createRelationshipTo(proteinERBB, RelationshipTypes.INTERACTS_WITH);

        System.out.println("completed protein relationships");

        /* add pubmed */

        Node pmid1 = graphDb.createNode(Labels.PUBMED);
        pmid1.setProperty("name", "pmid1");

        Node pmid2 = graphDb.createNode(Labels.PUBMED);
        pmid2.setProperty("name", "pmid2");

        Node pmid3 = graphDb.createNode(Labels.PUBMED);
        pmid3.setProperty("name", "pmid3");

        Node pmid4 = graphDb.createNode(Labels.PUBMED);
        pmid4.setProperty("name", "pmid4");

        Node pmid5= graphDb.createNode(Labels.PUBMED);
        pmid5.setProperty("name", "pmid5");


        // create relationships

        // BRCA1 (node)
        setPubMedRelation(node, pmid1, 5);
        setPubMedRelation(node, pmid2, 5);
        setPubMedRelation(node, pmid3, 4);

        setPubMedRelation(proteinERBB, pmid2, 5);
        setPubMedRelation(proteinERBB, pmid4, 5);

        setPubMedRelation(proteinP53, pmid2, 5);
        setPubMedRelation(proteinP53, pmid3, 3);

        setPubMedRelation(proteinIL, pmid1, 4);
        setPubMedRelation(proteinIL, pmid2, 5);
        setPubMedRelation(proteinIL, pmid5, 5);


        // create genes
        Node her1Gene = graphDb.createNode(DynamicLabel.label(BioTypes.GENE.toString()));
        her1Gene.setProperty("name", "Her1");

        Node her2Gene = graphDb.createNode(DynamicLabel.label(BioTypes.GENE.toString()));
        her2Gene.setProperty("name", "Her2");

        Node her3Gene = graphDb.createNode(DynamicLabel.label(BioTypes.GENE.toString()));
        her3Gene.setProperty("name", "Her3");

        System.out.println("completed genes");

        // gene to protein relationship (IDENTIFIED_IN_SEQUENCE)
       // her1Gene.createRelationshipTo(proteinP53,  DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_IN_SEQUENCE.toString(), "UTF-8")));
        //her2Gene.createRelationshipTo(proteinERBB,  DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_IN_SEQUENCE.toString(), "UTF-8")));
        //her3Gene.createRelationshipTo(proteinBRCA2,  DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_IN_SEQUENCE.toString(), "UTF-8")));

        //gene-to-gene relationship (GENE_ON_RIGHT)
        her2Gene.createRelationshipTo(her1Gene,  DynamicRelationshipType.withName(BioRelTypes.GENE_ON_RIGHT.toString()));
        System.out.println("her2Gene =" + her2Gene.getRelationships().toString());

        her2Gene.createRelationshipTo(her1Gene,  DynamicRelationshipType.withName(BioRelTypes.GENE_ON_RIGHT.toString()));
        her3Gene.createRelationshipTo(her1Gene,  DynamicRelationshipType.withName(BioRelTypes.GENE_ON_RIGHT.toString()));

        // gene-to-pubmed relationship (REFERENCES_PUBMED)
        her1Gene.createRelationshipTo(pmid1, DynamicRelationshipType.withName(BioRelTypes.REFERENCES_PUBMED.toString()));
        her2Gene.createRelationshipTo(pmid2, DynamicRelationshipType.withName(BioRelTypes.REFERENCES_PUBMED.toString()));
        her3Gene.createRelationshipTo(pmid3, DynamicRelationshipType.withName(BioRelTypes.REFERENCES_PUBMED.toString()));


        //create drugs:
        Node drug1 = graphDb.createNode(DynamicLabel.label(BioTypes.DRUG.toString()));
        drug1.setProperty("name", "Drug1");

        Node drug2 = graphDb.createNode(DynamicLabel.label(BioTypes.DRUG.toString()));
        drug2.setProperty("name", "Drug2");

        Node drug3 = graphDb.createNode(DynamicLabel.label(BioTypes.DRUG.toString()));
        drug3.setProperty("name", "Drug3");
        System.out.println("completed creating nodes for drugs");

        // drug-to-protein relationship (IDENTIFIED_PROTEIN)
       // drug1.createRelationshipTho(proteinP53, DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_PROTEIN.toString(), "UTF-8")));
       // drug2.createRelationshipTo(proteinERBB, DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_PROTEIN.toString(), "UTF-8")));
       // drug3.createRelationshipTo(proteinBRCA2, DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_PROTEIN.toString(), "UTF-8")));


        // gene to drug relationship  (ROLE_OF_GENE)
        her1Gene.createRelationshipTo(drug1,  DynamicRelationshipType.withName(BioRelTypes.ROLE_OF_GENE.toString()));
        her2Gene.createRelationshipTo(drug2,  DynamicRelationshipType.withName(BioRelTypes.ROLE_OF_GENE.toString()));
        her3Gene.createRelationshipTo(drug3,  DynamicRelationshipType.withName(BioRelTypes.ROLE_OF_GENE.toString()));

        // drug-to-protein relationship (IDENTIFIED_PROTEIN)
        //drug1.createRelationshipTo(proteinP53, DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_PROTEIN.toString(), "UTF-8")));
        //drug2.createRelationshipTo(proteinERBB, DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_PROTEIN.toString(), "UTF-8")));
        //drug3.createRelationshipTo(proteinBRCA2, DynamicRelationshipType.withName(URLEncoder.encode(BioRelTypes.IDENTIFIED_PROTEIN.toString(), "UTF-8")));


        // enzymes
        Node enzyme1 = graphDb.createNode(DynamicLabel.label(BioTypes.ENZYME.toString()));
        enzyme1.setProperty("name", "Enzyme1");

        Node enzyme2 = graphDb.createNode(DynamicLabel.label(BioTypes.ENZYME.toString()));
        enzyme2.setProperty("name", "Enzyme2");

        Node enzyme3 = graphDb.createNode(DynamicLabel.label(BioTypes.ENZYME.toString()));
        enzyme3.setProperty("name", "Enzyme3");
        System.out.println("completed creating nodes for enzymes");

        // enzyme to drug relationship  (ROLE_OF_ENZYME in metabolism)
        enzyme1.createRelationshipTo(drug1,  DynamicRelationshipType.withName(BioRelTypes.NEGATIVELY_REGULATES.toString()));
        enzyme2.createRelationshipTo(drug2,  DynamicRelationshipType.withName(BioRelTypes.NEGATIVELY_REGULATES.toString()));
        enzyme3.createRelationshipTo(drug3,  DynamicRelationshipType.withName(BioRelTypes.NEGATIVELY_REGULATES.toString()));
    }

    public static void traverseGraph() {
        //Object value = "P53";
        //Node node = graphDb.findNode(Labels.PROTEIN, "name", value);
        Node node = getNode(Labels.PROTEIN, "name", "P53");

        // traversal for BRCA1
        TraversalDescription td = graphDb.traversalDescription()
                .breadthFirst()
                .evaluator( Evaluators.atDepth(1));


        // returns pmid3, pmid2, pmid1
        Traverser traverser = td
                .relationships(RelationshipTypes.REFERENCES_PUBMED, Direction.BOTH)
                .traverse( node );
        System.out.println( "depth 5 traverseGraph: " );
        for( Node protein : traverser.nodes() ) {
            System.out.println( "\t" + protein.getProperty( "name" ) );
        }

        // Returns ERBB, BRCA2, IL, P53
        TraversalDescription proteinTd = graphDb.traversalDescription()
                .breadthFirst();
        Traverser proteinTraverser = proteinTd
                .relationships(RelationshipTypes.INTERACTS_WITH, Direction.BOTH)
                .traverse( node );
        System.out.println("Protein Interactions, depth5");
        for( Node protein : proteinTraverser.nodes()) {
            System.out.println( "\t" + protein.getProperty( "name" ) );
        }
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
        return iterable.iterator().hasNext() ?  true :  false;

    }

    /* save into another db */
    public static void saveSubGraph(TraversalDescription td, Node node) {
        Subgraph subGraph = new Subgraph();
        System.out.println("************* saveSubGraph()");

        for (Path path : td.traverse(node)) {
            System.out.println("path.length()" + path.length() + " path=" + path);
            if (path.length() < 5) {
                continue;
            }
            if (path.length() == 5) {
                System.out.println("********  path.length() is 5");
                for (Node pNode : path.nodes()) {
                    System.out.println("node =" + pNode.getLabels().toString());
                    Node n = graphDb.getNodeById(pNode.getId());
                    System.out.println("node =" + n.getLabels().toString() + ", name=" + n.getProperty("name"));
                    if (n == null) {
                        log.error("node is null, in path.nodes()" + path);
                        break;
                    }
                    Iterator<Label> iterator = n.getLabels().iterator();
                    Label label = iterator.next();
                    System.out.println("label=" + label.name());

                    Node destNode = destGraphDb.createNode(label);
                    destNode.addLabel(DynamicLabel.label("Innovation1"));
                    setNodeProperties(destNode, n.getAllProperties());
                }

                System.out.println("begin relationships");
                Iterator<Relationship> iterator = path.relationships().iterator();
                while (iterator.hasNext()) {

                    Relationship r = iterator.next();

                    Node endNode = r.getEndNode();
                    Iterator<Label> it = endNode.getLabels().iterator();
                    Label endNodeLabel = it.next();

                    Node startNode = r.getStartNode();
                    it = startNode.getLabels().iterator();
                    Label startNodeLabel = it.next();

                    System.out.println("startNode =" + startNodeLabel);
                    System.out.println("relation =" + r.getType().name());
                    System.out.println("endNode =" +  endNodeLabel);

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
        }
    }

    private static void setNodeProperties(Node n, Map<String, Object> properties ) {
        properties.forEach( (k,v) -> {
            n.setProperty(k, v);
        });
    }

    /**
     *
     * This is one level depth view
     * articles that refer to other proteins that interact with ERBB
     *
     * nodeids of protein: 18 - 22
     * nodeids of pubmed:  23 - 27
     * relationship ids: 28-34
     *
     nodeId of ERBB = (22)
     includes pubMed nodes ERBB
     interacts_withERBB

     path=(22)<--[INTERACTS_WITH,31]--(18)

     includes pubMed nodes BRCA1
     references pubmed BRCA1

     path=(22)<--[INTERACTS_WITH,31]--(18)--[REFERENCES_PUBMED,34]-->(25)

     includes pubMed nodes pmid3

     pmid3

     path=(22)<--[INTERACTS_WITH,31]--(18)--[REFERENCES_PUBMED,33]-->(24)
     includes pubMed nodes pmid2

     pmid2

     path=(22)<--[INTERACTS_WITH,31]--(18)--[REFERENCES_PUBMED,32]-->(23)
     includes pubMed nodes pmid1
     pmid1
     */
}
