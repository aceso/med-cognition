/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;


import com.sun.tools.javac.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.*;
import org.atgc.bio.meta.AnnotationTypes;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.util.UniprotUtil;
import org.atgc.init.Config;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.kernel.Traversal;
import uk.ac.ebi.uniprot.dataservices.jaxb.rest.WwwEbiAcUk_ToolsServicesRestNcbiblast;

import java.io.File;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;
import java.util.List;

/**
 * KnowledgeGraph - Traversal and create case studies for knowledge creation, new discoveries and paths
 *
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
   // private static final String dbPath = "/Users/tanisha/Documents/Neo4j/default.graphdb";
    private static GraphDatabaseService graphDb;
    protected static final Logger log = LogManager.getLogger(KnowledgeGraph.class);
    private static HashMap<Node, CacheGraphPath> cacheListPath;
    private static List<Path> cacheAllPaths;

    /**
     * cacheGraphPath - used to store the paths after bioassay evaluation is met
     *
     */
    public static class CacheGraphPath {
        Node startNode;
        Path path;
        Double numMatches;
        Double confidenceRatio;
    }

    /**
     * setup
     * @throws URISyntaxException
     */
    private static void setup() throws URISyntaxException {
        try {
            graphDb = BioEntityTemplate.getGraphDb();
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
                //destGraphDb.shutdown();
           }
        });
    }

    public static void main(String[] args) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException {
        setup();
        Transaction tx = graphDb.beginTx();
        try {
            createCaseStudyLapatinib();
            tx.success();
            // specify depth
            // getCaseStudy1();
            // createCaseStudy2();
            // createCaseStudy3();
        } catch(Exception e) {
            tx.failure(); //rollback
            throw new RuntimeException("Something went wrong with access while accessing, msg=" + e.getMessage(), e);
        } finally {
            tx.close();
        }
    }


    /**
     * createCaseStudyLapatinib
     * @throws Exception
     */
    public static void createCaseStudyLapatinib()  throws Exception {

        cacheListPath = new HashMap<>();
        cacheAllPaths = new ArrayList<>();

        Node geneOnto = getNode(BioTypes.GENE_ONTOLOGY.toString(), BioFields.GENE_ONTOLOGY_ID.toString(), "GO:0009931");
        if (geneOnto != null) {
            System.out.println("geneOnto = " + getLabel(geneOnto) + geneOnto.getId());
        }

        // GTPase KRAS (P49137)
        Node protein = getNode(BioTypes.PROTEIN.toString(), BioFields.UNIPROT_ID.toString(), "P49137");
        if (protein != null) {
            System.out.println("p49137, protein = " + getLabel(protein) + protein.getId());
        }

        Node drug= getNode(BioTypes.DRUG.toString(), BioFields.DRUG_NAME.toString(), "Lapatinib");
        if (drug != null) {
            System.out.println("drug = " + getLabel(drug) + drug.getId());
        }

        // enzymes of CYP450
        Node enzyme = getNode(BioTypes.ENZYME.toString(), BioFields.ENZYME_ID.toString(), "5.3.99.4");
        if (enzyme != null) {
            System.out.println("5.3.99.4 = " + getLabel(enzyme) + enzyme.getId());
        }

        // create ingredients of bioassay's
        List<Node> assayList = new ArrayList<>();
        assayList.add(drug);
        assayList.add(protein);
        assayList.add(enzyme);

        // evaluate these relationship, include and prune
        List<BioTypes> bioEvalTypes = getBioEvalTypes();
        getIntelligentPaths(50, assayList, bioEvalTypes, "CaseStudy603D3-14");
    }

    /**
     * gets the node given a biotype (gene/protein etc), property nane and property value
     * eg: biotype (GENE), property name (geneSymbol) property value (KRAS)
     * @param bioType
     * @param propName
     * @param propValue
     * @return
     */
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
    public static void getCaseStudy1() throws Exception {

        int depth = 20;
        cacheListPath = new HashMap<>();
        cacheAllPaths = new ArrayList<>();

        Drug bioDrug = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "Epoetin alfa");
        Node drug = BioEntityTemplate.getNode(bioDrug);
        if (drug != null) {
            System.out.println("drug=" + drug.getLabels().toString() + drug.getId());
        }

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

        List<BioTypes> bioEvalTypes = getBioEvalTypes();

        // call getGraphPath with depth and bioassays
        List<Node> entityList = getEntityInteractors(protein, urokinaseDrug, ncbiTaxonomy);
         for (Node node : entityList)  {
             getGraphPath(entityList, drug, depth, bioEvalTypes);
            // if (cacheListPath.size() == entityList.size()) {
              //   break;
             //}
        }
        setConfidenceRatio(entityList);
        for (CacheGraphPath cachePath : cacheListPath.values()) {
            log.info("cachePath =" + "path=" + cachePath.path);
            log.info("confidenceRatio = " + cachePath.confidenceRatio);
        }
        for (Node node: cacheListPath.keySet()) {
            log.info("node =" + node.getId());
        }
        saveSubGraph("CaseStudy103");
       //log.info("cacheAllPaths =" + cacheAllPaths.size());
        if (cacheAllPaths.size() > 3) {
            log.info("cacheAllPaths last path" + cacheAllPaths.get(cacheAllPaths.size() - 3));
            log.info("cacheAllPaths last path" + cacheAllPaths.get(cacheAllPaths.size() - 2));
            log.info("cacheAllPaths last path" + cacheAllPaths.get(cacheAllPaths.size() - 1));
        }
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



    // capacetabine: compound1325.xml compound1283.xml compound1271.xml
    //
    // cetuximab:
    // compound104.xml compound1146.xml
    // compound1146.xml:  KRAS (gene),  P01116 (uniprot)
    // CCND1 (gene),  P24385 (uniprot),


    // erlotinib:DB00530 (drugbank),
    // KRAS (gene), 16043828 (pubmedid),   P01116 (uniprot)
    // NM_004985 (RefSeqID)
    // Gene-NCBITaxon:9606-CCND1
    // CCND1 (gene), cyclin d1(gene alias), P24385 (uniprot),
    // DrugTerm and Gene are connected by ROLE_OF_GENE

    //
    // compound1146.xml
    // compound4176.xml compound583.xml compound968.xml
    // compound974.xml compound750.xml compound891.xml
    // compound583.xml compound4716.xml compound4093.xml
    //
    // hugogene: MAPK1, MAPK3,
    // uniprotid: P28482
    // geneterm: erk (extracellular signal-regulated kinase), erk1
    // pubmedid: 18723475 (drugbank), 17041093, 16937104
    // 17206887 (pubmedid)

    // pubmedid: 16217753 (erk1/MAPK3),15657067
    // Gene: (HugoGeneSymbol)9606-MAPK1
    //

    // drug: capecitabine :  gene:  TYMS (thymidylate synthase),
    // uniprot:   P04818
    // RefSeqId:  NM_001071
    //

    /*
    unique: RNA_PREFERRED_SYMBOL
    <id>:  248358
    message:  IntactRna--human_18s_rrna
    interactorId: 940992
    shortLabel:  human_18s_rrna
    ncbiTaxId:  9606
    intactId: 940992
    fullName: 18S rRNA
    nodeType: Rna

    <id>: 216219
    geneSymbol:  tp53i3
    message:  IntactGene-tp53i3-tp53i3_human_gene
    interactorId:558898
    shortLabel: tp53i3_human_gene
    ncbiTaxId:9606
    intactId: 558898
    fullName: Human TP53I3 gene
    nodeType: IntactGene


    <id>: 102367
    message: Enzyme-1.1.1.298
    enzymeLastChange: 2012-02-10 23:35:10
    enzymeId: 1.1.1.298
    nodeType: Enzyme


    <id>:
    210562
    message: Dna-EBI-1648030-cdk4_human_dna
    interactorId: 546760
    ebiId: EBI-1648030
    shortLabel:cdk4_human_dna
    ncbiTaxId: 9606
    intactId: 546760
    fullName: Double-stranded oligomer corresponding to MBS4 in human cdk4 promoter
    nodeType: Dna
    */
    public static void createCaseStudy2()  throws Exception {

        cacheListPath = new HashMap<>();
        HashMap<BioTypes, String> bioMap = new HashMap<>();
        cacheAllPaths = new ArrayList<>();
        /*
            Using this
            messages:
            ccnd1-human gene = Gene-NCBITaxon:9606-CCND1  412275
            p01116, protein = Protein-P24385-ccnd1_human  204950
            cetuximabDrug = Drug-Cetuximab  122
            enzyme = Enzyme-1.1.1.298   102367
            drug = Drug-Erlotinib   28975
         */

        // GeneSymbol compound CCND1
        Node gene = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "101943958");
        if (gene != null) {
            System.out.println("ccnd1-human gene = " + getLabel(gene) + gene.getId());
        }

        //Protein proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P24385");
        Node protein = getNode(BioTypes.PROTEIN.toString(), BioFields.UNIPROT_ID.toString(), "P24385");
        if (protein != null) {
            System.out.println("p24385, protein = " + getLabel(protein) + protein.getId());
        }

        Drug drugBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "Erlotinib");
        Node drug= BioEntityTemplate.getNode(drugBio);
        if (drug != null) {
            System.out.println("drug = " + getLabel(drug) + drug.getId());
        }

        //Protein proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P24385");
        Node enzyme = getNode(BioTypes.ENZYME.toString(), BioFields.ENZYME_ID.toString(), "5.3.99.5");
        if (enzyme != null) {
            System.out.println("5.3.99.5 = " + getLabel(enzyme) + enzyme.getId());
        }


        List<Node> list = new ArrayList<>();
        list.add(drug);
        list.add(protein);
        list.add(enzyme);

        List<BioTypes> bioTypes = new ArrayList<>();
        bioTypes.add(BioTypes.DRUG_MANUFACTURER);
        bioTypes.add(BioTypes.DRUG_PACKAGER);
        bioTypes.add(BioTypes.DRUG_PRICE);
        getIntelligentPaths(20, list, bioTypes, "CaseStudy205");
    }


    /**
     * createCaseStudy3 - glucagon
     * @throws Exception
     */
    public static void createCaseStudy3() throws Exception {

        /*
        A-A gene = Gene-NCBITaxon:9606-KRAS,  id=422619
            protein = P01116, id=526842
            glucagonDrug = Drug-glucagon   377567
        */

        cacheListPath = new HashMap<>();
        cacheAllPaths = new ArrayList<>();
        //HLA-A
        Node gene = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "3845");
        if (gene != null) {
            System.out.println("KRAS = " + getLabel(gene) + "id=" + gene.getId());
        }

        //
        Protein proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P01116");
        Node protein = BioEntityTemplate.getNode(proteinBio);
        //protein.setProperty(BioFields.MESSAGE.toString(), "P01116");
        protein.setProperty(BioFields.UNIPROT_ID.toString(), "P01116");
        if (protein != null) {
            System.out.println("P01116, protein = " + getLabel(protein) + "id=" + protein.getId());
        }

        Drug glucaGonBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "glucagon");
        Node glucaGonDrug = BioEntityTemplate.getNode(glucaGonBio);
        if (glucaGonDrug != null) {
            System.out.println("glucagonDrug = " + getLabel(glucaGonDrug) + glucaGonDrug.getId());
        }

        //drug.setDrugGeneRelation(gene, "" )
        List<Node> list = new ArrayList<>();
        list.add(glucaGonDrug);
        list.add(gene);
        list.add(protein);

        List<BioTypes> bioTypes = new ArrayList<>();
        bioTypes.add(BioTypes.DRUG_MANUFACTURER);
        bioTypes.add(BioTypes.DRUG_PACKAGER);
        bioTypes.add(BioTypes.DRUG_PRICE);
        getIntelligentPaths(10, list, bioTypes, "CaseStudy301");
    }


    /**
     * getIntelligentPaths
     * @param depth
     * @param assayList
     * @param excludeBioTypes
     * @throws Exception
     */
    public static void getIntelligentPaths(int depth,
                                           List<Node> assayList,
                                           List<BioTypes> excludeBioTypes,
                                           String label) throws Exception {
        for (Node assayNode : assayList) {
            // get graphPath
            getGraphPath(assayList, assayNode, depth, excludeBioTypes);
            log.info("path size " + cacheListPath.size());
        }
        setConfidenceRatio(assayList);
        saveAllGraphs(label);
        /* for this test */
        //saveSubGraph(label);
    }


    /**
     * getEntityInteractors
     * @param node1
     * @param node2
     * @param node3
     * @return
     */
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
                                    List<BioTypes> evalBioTypes) throws UnsupportedEncodingException {
        TraversalDescription td = graphDb.traversalDescription()
            // Choose a breadth-first search strategy
            .breadthFirst()
            .uniqueness(Uniqueness.RELATIONSHIP_GLOBAL)
            .expand(new PathExpander<Object>() {
                 @Override
                 public Iterable<Relationship> expand(Path path, BranchState<Object> objectBranchState) {
                     log.info("path.length=" + path.length());
                     log.info("path.relationships()" + path.toString());
                    // log.info("path.endNode().getRelationships()" + path.endNode().getRelationships().);
                     //return path.endNode().getRelationships();
                     return allPathNodeRelationships(path, evalBioTypes);
                     //return path.relationships();
                 }
                 @Override
                 public PathExpander<Object> reverse() {
                     return null;
                 }
            })
            .evaluator(new KnowledgeEntityEvaluator(list, evalBioTypes));
        getNewKnowledge(td, node, list, depth);
    }


    private static List<BioTypes> getBioEvalTypes() {
        List<BioTypes> evalBioTypes = new ArrayList<>();
        evalBioTypes.add(BioTypes.DRUG_MANUFACTURER);
        evalBioTypes.add(BioTypes.DRUG_PACKAGER);
        evalBioTypes.add(BioTypes.DRUG_PRICE);
        evalBioTypes.add(BioTypes.DRUG_PATENT);
        return evalBioTypes;
    }

    private static String getLabel(Node n) {
        Object obj = n.getProperty(BioFields.MESSAGE.toString());
        if (obj != null)
            return obj.toString();
        return null;
    }

    private static Iterable<Relationship> allPathNodeRelationships(Path path, List<BioTypes> bioTypes) {
        List<Relationship> relList = new LinkedList<>();
        for (Node node : path.nodes()) {
             if (excludeNodes(node, bioTypes)) {
                 continue;
             }
             Iterable<Relationship> iterable = node.getRelationships();
             Iterator<Relationship> iterator = iterable.iterator();
             while (iterator.hasNext()) {
                 Relationship rel = iterator.next();
                 if (rel != null) {
                     if (rel.getEndNode() != null && rel.getStartNode() != null && rel.getType() != null) {
                         Node eNode = rel.getEndNode();
                         String eNodeType = (String) rel.getEndNode().getProperty(BioFields.NODE_TYPE.toString());
                         String sNodeType = (String) rel.getStartNode().getProperty(BioFields.NODE_TYPE.toString());
                         Node sNode = rel.getStartNode();
                         Node otherEndNode = rel.getOtherNode(eNode);
                         Node otherStartNode = rel.getOtherNode(sNode);
                         log.info("endNode=" + eNodeType);
                         log.info("startNode =" + sNodeType);

                         if (sNodeType.equals(BioTypes.GENE_ONTOLOGY.toString())) {
                             log.info("relationship =" + rel.getType().name() + ", startNode=" + sNode.getProperty(BioFields.GENE_ONTOLOGY_ID.toString()));
                         }
                         if (eNodeType.equals(BioTypes.GENE_ONTOLOGY.toString())) {
                             log.info("endNode=" + eNode.getProperty(BioFields.GENE_ONTOLOGY_ID.toString()));
                         }
                         if (otherEndNode != null) {
                             log.info("Other endNode =" + otherEndNode.getProperty(BioFields.NODE_TYPE.toString()));
                             log.info("other endNode.getId()" + otherEndNode.getId());

                         }
                         if (otherStartNode != null) {
                             log.info("Other startNode =" + otherStartNode.getProperty(BioFields.NODE_TYPE.toString()));
                             log.info("id" + otherStartNode.getId());
                         }
                         relList.add(rel);
                     }
                 }
             }
        }
        log.info("relList for path=" + relList.size());
        return relList;
    }

    private static boolean excludeNodes(Node node, List<BioTypes> bioTypes) {
        String nodeType = (String)node.getProperty(BioFields.NODE_TYPE.toString());
        for (BioTypes bioType : bioTypes) {
            if (nodeType.equals(bioType)) {
                return true;
            }
        }
        return false;
    }

    /**
     *
     * This method adds the paths that meet the criteria of bio assay's
     * These paths are added in cacheListPath a HashMap.
     * These paths are saved with a label for each case study.
     * This shows new knowledge that are interconnected with labels for convenience for retrieval
     * @param path
     * @param assayList
     * @param node
     * @return
     */
    public static void addPath(Path path, List<Node> assayList, Node node) {
        cacheAllPaths.add(path);
        System.out.println("path =" + path.toString());
    }

    /**
     * specify the depth for evaluation and retrieve the paths
     */
    private static void getNewKnowledge(TraversalDescription td, Node node, List<Node> list, int depth) {
        log.info("getNewKnowledge()");
        Traverser traverser =//td.evaluator(Evaluators.fromDepth(depthFrom))
                td.evaluator(Evaluators.toDepth(depth))
                .traverse(node);

        /* do not check for metadata in traverser, it gives wrong result */
        ResourceIterator<Path> iterator = traverser.iterator();
        try {
            while (iterator != null && iterator.hasNext()) {
                Path path = iterator.next();
                addPath(path, list, node);
            }
        } catch(RuntimeException e) {
            log.error("iterator.hasNext() error " + e.getMessage(), e);
        }
    }

    private static void setConfidenceRatio(List<Node> list) {
        double numMatches = 0.0;
        for (CacheGraphPath cachePath : cacheListPath.values()) {
            for (Node n: cachePath.path.nodes()) {
                for (Node k : list) {
                    if (isNodeIncluded(n, k)) {
                        numMatches++;
                    }
                }
                cachePath.confidenceRatio = numMatches / (double)cachePath.path.length();
                cachePath.numMatches = numMatches;
            }
        }
    }

    // As iterable is not in order
    private static boolean isNodeIncluded(Node node, Node dNode) {
        Iterable<String> propertyKeys = node.getPropertyKeys();
        Iterable<String> destProps = dNode.getPropertyKeys();
        int cnt = 0;
        int dCnt = 0;

        for (String s : propertyKeys) {
            cnt++;
            for (String d : destProps) {
                if (s.equals(d)) {
                    dCnt++;
                }
            }
        }
        if (cnt == dCnt)
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
            //log.info("relationship =" + iterable.iterator().next().getType().toString());
            return true;
        }
        return iterable.iterator().hasNext() ? true : false;
    }

    /**
     * all paths are saved
     * @param label
     */
    public static void saveAllGraphs(String label) {
        for ( Path path : cacheAllPaths) {
            log.info(label + " has path.length=" + path.length());
            for (Node node : path.nodes()) {
                node.addLabel(DynamicLabel.label(label));
            }
        }
        log.info("saveAllGraphs()" + label + ", total number of paths" + cacheAllPaths.size());
    }

    public static void saveSubGraph(String label) {
        for (CacheGraphPath cachePath : cacheListPath.values()) {
            // log.info("nodes tagged with label experiment1 in path " + cachePath.path);
            for (Node node : cachePath.path.nodes()) {
                node.addLabel(DynamicLabel.label(label));
            }
        }
    }

    /**
     *
     * set protein and gene role relations
     * @param drug
     * @param protein
     */
    private static void setDrugProteinRelation(Node drug, Node protein) {
        drug.createRelationshipTo(protein, DynamicRelationshipType.withName(URLEncoder.encode(
                BioRelTypes.IDENTIFIED_PROTEIN.toString())));
    }

    private static void setGeneProteinRelation(Gene gene, Protein protein) {
       // gene.setSequenceIdentificationRelation(protein, hgncId, );
    }

    /*
     * Drug gene role relation
     */
    private static Relationship setDrugGeneRelation(Node drug, Node gene, String geneTerm) {
        Relationship rel = drug.createRelationshipTo(gene, DynamicRelationshipType.withName(URLEncoder.encode(
                BioRelTypes.ROLE_OF_GENE.toString())));
        return rel;

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



     message:
     Journal-0009-9104-Clinical and experimental immunology
     title:
     Clinical and experimental immunology
     issn:
     0009-9104
     issnType:
     Print
     nodeType:
     Journal
     isoAbbreviation:
     Clin. Exp. Immunol.
     */

      /*
        Rna rnaBio = BioEntityTemplate.getBioEntity(BioTypes.RNA, "546760");
        Node rna = BioEntityTemplate.getNode(rnaBio);
        if (rna != null) {
            System.out.println("rna= " + rna.getLabels().toString() + rna.getId());
        }
      */

    public static void setDrugGeneRelation() throws Exception {

        Drug glucaGonBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "Glucagon");
        Node glucaGonDrug = BioEntityTemplate.getNode(glucaGonBio);
        if (glucaGonDrug != null) {
            System.out.println("glucagonDrug = " + getLabel(glucaGonDrug) + ", id=" + glucaGonDrug.getId());
        }

        Drug erlotinibBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "Erlotinib");
        Node erlotinibDrug = BioEntityTemplate.getNode(erlotinibBio);
        if (erlotinibDrug != null) {
            System.out.println("erlotinibDrug = " + getLabel(erlotinibDrug) +  ",id="+ erlotinibDrug.getId());
        }

        Drug cetuximabBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "Cetuximab");
        Node cetuximabDrug = BioEntityTemplate.getNode(cetuximabBio);
        if (cetuximabDrug != null) {
            System.out.println("cetuximbaDrug = " + getLabel(cetuximabDrug) + ", id="+cetuximabDrug.getId());
        }

        // compound104.xml   CCND1 - cyclin d1,  proteinid: P24385,  ncbiGeneId: 595,  pubmedid: 15569985
        Node geneNode = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "101943958");
        Relationship rel = setDrugGeneRelation(erlotinibDrug, geneNode, "cyclin d1");
        rel.setProperty("Chemical_or_Drug_Affects_Gene_Product", "cyclin d1");
        rel.setProperty("Chemical_or_Drug_Represses_Gene_Product_Expression", "cyclin d1");

        rel = setDrugGeneRelation(cetuximabDrug, geneNode, "cyclin d1");
        rel.setProperty("Chemical_or_Drug_Affects_Gene_Product", "cyclin d1");
        rel.setProperty("Chemical_or_Drug_Has_Mechanism_Of_Action", "cyclin d1");
        rel.setProperty("Chemical_or_Drug_Decreases_Metabolic_Status", "cyclin d1");
        rel.setProperty("Chemical_or_Drug_Changes_Expression", "cyclin d1");
        rel.setProperty("Gene_Anomaly_May_Effect_Resistance_to_Chemical_or_Drug", "cyclin d1");
        rel.setProperty("Chemical_or_Drug_Has_Study_Therapeutic_Use_For", "cyclin d1");


        // gene: MKI67,  proteinid: P46013, pubmedid: 9112016, ncbiGeneid: 4288, compound891.xml
        geneNode = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "4288");
        Gene geneBio = GraphMapper.getBioEntity(geneNode);
        if (geneNode != null) {
            System.out.println("MKI67 gene = " + getLabel(geneNode) + ",id=" + geneNode.getId());
        }
        // 9112016
        setDrugGeneRelation(glucaGonDrug, geneNode, "ki-67");
        rel = setDrugGeneRelation(erlotinibDrug, geneNode, "ki-67");
        rel.setProperty("Chemical_or_Drug_Affects_Gene_Product_Expression", "ki-67");


        //gene KRAS,  ncbiGeneId: 3845, proteinid: P01116 pubmedid:16110022 (compound1146.xml)
        //ki-ras
        geneNode = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "3845");
        geneBio = GraphMapper.getBioEntity(geneNode);
        if (geneBio != null) {
            System.out.println("KRAS = " + getLabel(geneNode) + ",id=" + geneNode.getId());
        }
        rel = setDrugGeneRelation(erlotinibDrug, geneNode, "kras");
        rel.setProperty("Chemical_or_Drug_Has_Study_Therapeutic_Use_For", "kras");
        rel.setProperty("Gene_Product_Anomaly_Effects_Resistance_to_Chemical_or_Drug", "kras");
        rel.setProperty("Chemical_or_Drug_Has_Mechanism_Of_Action", "kras");

        rel = setDrugGeneRelation(cetuximabDrug, geneNode, "kras");
        rel.setProperty("Chemical_or_Drug_Affects_Gene_Product", "kras");
        rel.setProperty("Gene_Product_Anomaly_Effects_Resistance_to_Chemical_or_Drug", "kras");


        //ki-ra for glucagon drug
        rel = setDrugGeneRelation(glucaGonDrug, geneNode, "ki-ras");
        rel.setProperty("Chemical_or_Drug_Affects_Gene_Product", "ki-ras");
        rel.setProperty("Chemical_or_Drug_Represses_Gene_Product_Expression", "ki-ras");


        // geneTerms2.add("vegf");  //  VEGFA  (compound750.xml), ncbiGeneId: 7422
        // proteinid: Q96L82,  pubmed: 17206887
        geneNode = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "7422");
        geneBio = GraphMapper.getBioEntity(geneNode);
        if (geneBio != null) {
            System.out.println("VEGFA = " + getLabel(geneNode) + ", id=" + geneNode.getId());
        }
        //setDrugGeneRelation(glucaGonDrug, geneNode, "vegf");
        setDrugGeneRelation(erlotinibDrug, geneNode, "vegf");


        // gene: MKI67, proteinid: P46013  geneid: 4288
        Protein proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P46013");
        if (proteinBio != null) {
            Node proteinNode = BioEntityTemplate.getNode(proteinBio);
            if (proteinBio != null) {
                System.out.println("protein for MKI67 = " + getLabel(proteinNode) + ",id=" + proteinNode.getId());
            }
            setDrugProteinRelation(erlotinibDrug, proteinNode);
            setDrugProteinRelation(glucaGonDrug, proteinNode);
        }

        // VEGFA gene, erlotinib drug,  ncbiGeneid: 7422,
        proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "Q96L82");
        if (proteinBio != null) {
            Node proteinNode = BioEntityTemplate.getNode(proteinBio);
            if (proteinBio != null) {
                System.out.println("protein of VEGFA = " + getLabel(proteinNode) + ",id =" + proteinNode.getId());
            }
            setDrugProteinRelation(erlotinibDrug, proteinNode);
        }


        //KRAS (gene),  pubmed: 16043828,  compound1146.xml, geneid: 3845,
        proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P01116");
        if (proteinBio != null) {
            Node proteinNode = BioEntityTemplate.getNode(proteinBio);
            if (proteinBio != null) {
                System.out.println("protein of KRAS = " + getLabel(proteinNode) + " id=" + proteinNode.getId());
            }
            setDrugProteinRelation(cetuximabDrug, proteinNode);
            setDrugProteinRelation(erlotinibDrug, proteinNode);
            setDrugProteinRelation(glucaGonDrug, proteinNode);
        }

        // CCND1 gene, compound104.xml
        proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P24385");
        if (proteinBio != null) {
            Node proteinNode = BioEntityTemplate.getNode(proteinBio);
            if (proteinBio != null) {
                System.out.println("protein of CCND1 = " + getLabel(proteinNode) + " id=" + proteinNode.getId());
            }
            setDrugProteinRelation(cetuximabDrug, proteinNode);
            setDrugProteinRelation(erlotinibDrug, proteinNode);
            System.out.println("create relationship between protein and drug");
        }

/*
        <id>:
        412087
        message:
        Drug-erlotinib
        nciDrugCode:
        C2693
        drugName:
        erlotinib
        */

    }


    public static void getProteins() throws Exception {
        UniprotUtil.getProtein("P01116");
        UniprotUtil.getProtein("P24385");
        UniprotUtil.getProtein("Q96L82");
        UniprotUtil.getProtein("P46013");

    }

    /*
    private static double getCount(Path path, List<Node> list) {
        double matches = 0;
        for (Node n: path.nodes()) {
            for (Node k : list) {
                String pathNodeLabel = getLabel(n);
                String kLabel = getLabel(k);
                if (kLabel != null && pathNodeLabel != null) {
                    if (pathNodeLabel.equalsIgnoreCase(kLabel)) {
                        matches++;
                    }
                }
            }
        }
        return matches;
    }
    */

}
