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
import java.util.List;

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
           // getCaseStudy1();
            setDrugGeneRelation();
           // createCaseStudy3();
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
    public static void getCaseStudy1() throws Exception {

        int depth = 4;
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

        List<BioRelTypes> relTypes = new ArrayList<>();
        relTypes.add(BioRelTypes.DRUG_MANUFACTURED_BY);
        relTypes.add(BioRelTypes.DRUG_PACKAGED_BY);


        // depth is 4
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
        saveSubGraph("CaseStudy1");
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

        /*  These did not yield results
        bioMap.put(BioTypes.INTACT_GENE, "tp53i3");
       // bioMap.put(BioTypes.RNA, "546760");
        bioMap.put(BioTypes.ENZYME, "1.1.1.298");
        bioMap.put(BioTypes.HUMAN_DISEASE_ONTOLOGY, "0050014");
        */

        /*
            Not using this
            kras gene = [Gene]412275
            p01116, protein = [Protein]204950
            cetuximabDrug = [Drug]122
            enzyme = [Enzyme]102367
            drug = [Drug]28975

        /*
            Using this
            messages:
            ccnd1-human gene = Gene-NCBITaxon:9606-CCND1  412275
            p01116, protein = Protein-P24385-ccnd1_human  204950
            cetuximabDrug = Drug-Cetuximab  122
            enzyme = Enzyme-1.1.1.298   102367
            drug = Drug-Erlotinib   28975

         */

        // GeneSymbol  compound
        /*
        Gene geneBio = BioEntityTemplate.getBioEntity(BioTypes.GENE, "Gene-NCBITaxon:9606-CCND1");
        Node gene = BioEntityTemplate.getNode(geneBio);
        */
        Node gene = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "595");
        if (gene != null) {
            System.out.println("ccnd1-human gene = " + getLabel(gene) + gene.getId());
        }

        Protein proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P24385");
        Node protein = BioEntityTemplate.getNode(proteinBio);
        if (protein != null) {
            System.out.println("p01116, protein = " + getLabel(protein) + protein.getId());
        }

        // HumanDiseaseOntology
        /*
        Drug cetuBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "Cetuximab");
        Node cetuximabDrug = BioEntityTemplate.getNode(cetuBio);
        if (cetuximabDrug!= null) {
            System.out.println("cetuximabDrug = " + getLabel(cetuximabDrug) + cetuximabDrug.getId());
        }


        Enzyme enzymeBio = BioEntityTemplate.getBioEntity(BioTypes.ENZYME, "1.1.1.298");
        Node enzyme = BioEntityTemplate.getNode(enzymeBio);
        if (enzyme != null) {
            System.out.println("enzyme = " + getLabel(enzyme) + enzyme.getId());
        } */

        Drug drugBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "Erlotinib");
        Node drug= BioEntityTemplate.getNode(drugBio);
        if (drug != null) {
            System.out.println("drug = " + getLabel(drug) + drug.getId());
        }

        List<Node> list = new ArrayList<>();
        list.add(protein);
        list.add(gene);
        list.add(drug);

        List<BioRelTypes> relTypes = new ArrayList<>();
        relTypes.add(BioRelTypes.DRUG_MANUFACTURED_BY);
        relTypes.add(BioRelTypes.DRUG_PACKAGED_BY);
        relTypes.add(BioRelTypes.FOUND_EVIDENCE_IN);
        relTypes.add(BioRelTypes.HAS_A_PROTEIN);
        relTypes.add(BioRelTypes.HAS_AUTHOR);
        relTypes.add(BioRelTypes.REFERENCES_PUBMED);
        getIntelligentPaths(3, list, relTypes);
    }

    /**
     * createCaseStudy3 - glucagon
     * @throws Exception
     */
    public static void createCaseStudy3() throws Exception {

        /*
        A-A gene = Gene-NCBITaxon:9606-HLA-A  937823
            Q9UQU7, protein = Protein-Q9UQU7  935777
            glucagonDrug = Drug-glucagon   377567
        */

        cacheListPath = new HashMap<>();
        //HLA-A
        Node gene = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "3105");
        if (gene != null) {
            System.out.println("HLA-A gene = " + getLabel(gene) + gene.getId());
        }

        //
        Protein proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "Q9UQU7");
        Node protein = BioEntityTemplate.getNode(proteinBio);
        if (protein != null) {
            System.out.println("Q9UQU7, protein = " + getLabel(protein) + protein.getId());
        }

        Drug glucaGonBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "glucagon");
        Node glucaGonDrug = BioEntityTemplate.getNode(glucaGonBio);
        if (glucaGonDrug != null) {
            System.out.println("glucagonDrug = " + getLabel(glucaGonDrug) + glucaGonDrug.getId());
        }

        //drug.setDrugGeneRelation(gene, "" )
        List<Node> list = new ArrayList<>();
        list.add(gene);
        list.add(protein);
        list.add(glucaGonDrug);

        List<BioRelTypes> relTypes = new ArrayList<>();
        relTypes.add(BioRelTypes.DRUG_MANUFACTURED_BY);
        relTypes.add(BioRelTypes.DRUG_PACKAGED_BY);
        relTypes.add(BioRelTypes.FOUND_EVIDENCE_IN);
        relTypes.add(BioRelTypes.HAS_A_PROTEIN);
        relTypes.add(BioRelTypes.HAS_AUTHOR);
        relTypes.add(BioRelTypes.REFERENCES_PUBMED);

        getIntelligentPaths(6, list, relTypes);
    }




    /**
     * getIntelligentPaths
     * @param depth
     * @param list
     * @param excludeRelTypes
     * @throws Exception
     */
    public static void getIntelligentPaths(int depth,
                                           List<Node> list,
                                           List<BioRelTypes> excludeRelTypes) throws Exception {
        for (Node node : list) {
            getGraphPath(list, node, depth, excludeRelTypes);
            log.info("path size " + cacheListPath.size());
           // break;
            if (cacheListPath.size() == list.size()) {
                break;
            }
        }
        setRatio(list);
        log.info("***** path size " + cacheListPath.size());

        for (CacheGraphPath cachePath : cacheListPath.values()) {
            log.info("cachePath =" + "path=" + cachePath.path);
            log.info("ratio = " + cachePath.ratio);
        }
        for (Node node : cacheListPath.keySet()) {
            log.info(node.getLabels().toString() + " =" + + node.getId());
        }
        saveSubGraph("CaseStudy3");
    }


    /**
     * getInteractors
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
        getNewKnowledge(td, node, list, depth);
    }

    private static String getLabel(Node n) {
        Object obj = n.getProperty(BioFields.MESSAGE.toString());
        if (obj != null)
            return obj.toString();
        return null;
    }

    /**
     *
     * @param path
     * @param list
     * @param node
     * @return
     */
    public static void addPath(Path path, List<Node> list, Node node) {
        log.info("\n\n\t addPath(), cacheListPath.size() " + cacheListPath.size());
        for (Node n: path.nodes()) {
            int matches = 0;
            //getCount(path, list);
            for (Node targetNode : list) {
                if (isNodeIncluded(n, targetNode)) {
                    matches++;
                    log.info("addPath(), matches "  + ", targetNodeid" + targetNode.getId() + " in path=" + path);
                    if (cacheListPath.get(targetNode) != null) {
                        log.info("addPath(), targetNode exists" + targetNode.getId());
                        continue;
                    }
                    CacheGraphPath cachePath = new CacheGraphPath();
                    cachePath.path = path;
                    cachePath.startNode = node;
                    cachePath.totalEntities = path.length();
                    cacheListPath.put(targetNode, cachePath);
                }
            }
        }
    }

    private static void getNewKnowledge(TraversalDescription td, Node node, List<Node> list, int depth) {
        log.info("getNewKnowledge() ");
        int numNodes = 0;
        Traverser traverser = td.evaluator(Evaluators.toDepth(depth))
                .traverse(node);
        ResourceIterator<Path> iterator = traverser.iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            log.info("getNewKnowledge(), traverser(), path.length=" + path.length() + ", path=" + path);
            addPath(path, list, node);
        }
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

    private static void setRatio(List<Node> list) {
        double numMatches = 0.0;
        for (CacheGraphPath cachePath : cacheListPath.values()) {
            for (Node n: cachePath.path.nodes()) {
                for (Node k : list) {
                    if (isNodeIncluded(n, k)) {
                        numMatches++;
                    }
                }
                cachePath.ratio = numMatches / (double)cachePath.path.length();
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
        //StringBuffer sb = new StringBuffer();
        for (String s : propertyKeys) {
            cnt++;
            //sb.append(s);
            //sb.append(" , ");
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
            log.info("relationship =" + iterable.iterator().next().getType().toString());
            return true;
        }
        return iterable.iterator().hasNext() ? true : false;
    }

    public static void saveSubGraph(String label) {
       for (CacheGraphPath cachePath : cacheListPath.values()) {
            log.info("nodes tagged with label experiment1 in path " + cachePath.path);
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
    private static void setDrugProteinRelation(Drug drug, Protein protein) {
        drug.setProteinRelation(protein);
    }

    private static void setGeneProteinRelation(Gene gene, Protein protein) {
       // gene.setSequenceIdentificationRelation(protein, hgncId, );
    }

    /*
     * Drug gene role relation
     */
    private static void setDrugGeneRelation(Drug drug, Gene gene, String geneTerm) {

        //for (String geneTerm : geneTerms ) {
            drug.setDrugGeneRoleRelations(gene, geneTerm, null);
        //}
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

        Drug glucaGonBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "glucagon");
        Node glucaGonDrug = BioEntityTemplate.getNode(glucaGonBio);
        if (glucaGonDrug != null) {
            System.out.println("glucagonDrug = " + getLabel(glucaGonDrug) + glucaGonDrug.getId());
        }

        Drug erlotinibBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "erlotinib");
        Node erlotinibDrug = BioEntityTemplate.getNode(erlotinibBio);
        if (erlotinibDrug != null) {
            System.out.println("erlotinibDrug = " + getLabel(erlotinibDrug) + erlotinibDrug.getId());
        }

        Drug cetuximabBio = BioEntityTemplate.getBioEntity(BioTypes.DRUG, "cetuximab");
        Node cetuximabDrug = BioEntityTemplate.getNode(cetuximabBio);
        if (cetuximabDrug != null) {
            System.out.println("cetuximbaDrug = " + getLabel(cetuximabDrug) + cetuximabDrug.getId());
        }

        // CCND1 - cyclin d1
        Node ccnd1Gene = getNode(BioTypes.GENE, BioFields.NCBI_GENE_ID.toString(), )
        Gene ccnd1Bio = BioEntityTemplate.getBioEntity(BioTypes.GENE, "CCND1");
        if (ccnd1Gene != null) {
            System.out.println("ccnd1Gene = " + getLabel(ccnd1Gene) + ccnd1Gene.getId());
        }
        setDrugGeneRelation(glucaGonBio, ccnd1Bio, "cyclin d1");
        setDrugGeneRelation(erlotinibBio, ccnd1Bio, "cyclin d1");
        setDrugGeneRelation(cetuximabBio, ccnd1Bio, "cyclin d1");

        // PTGS2s
        Gene geneBio = BioEntityTemplate.getBioEntity(BioTypes.GENE, "PTGS2");
        Node geneNode = BioEntityTemplate.getNode(geneBio);
        if (geneNode != null) {
            System.out.println("ccnd1Gene = " + getLabel(geneNode) + geneNode.getId());
        }
        // PTGS2
        setDrugGeneRelation(glucaGonBio, geneBio, "cox-2");
        setDrugGeneRelation(erlotinibBio, geneBio, "cox-2");


        //gene KRAS
        //ki-ras
        //Gene geneBios = BioEntityTemplate.getBioEntity(BioTypes.GENE, "3845");
        geneNode = getNode(BioTypes.GENE.toString(), BioFields.NCBI_GENE_ID.toString(), "3845");
        if (geneBio != null) {
            System.out.println("KRAS = " + getLabel(geneNode) + geneNode.getId());
        }

        setDrugGeneRelation(erlotinibBio, geneBio, "kras");
        setDrugGeneRelation(cetuximabBio, geneBio, "kras");

        //ki-ra for glucagon drug
        setDrugGeneRelation(glucaGonBio, geneBio, "ki-ras");


        // geneTerms2.add("erk1");  //  MAPK3
        geneBio = BioEntityTemplate.getBioEntity(BioTypes.GENE, "MAPK3");
        geneNode = BioEntityTemplate.getNode(geneBio);
        if (geneBio != null) {
            System.out.println("MAPK3 = " + getLabel(geneNode) + geneNode.getId());
        }
        setDrugGeneRelation(glucaGonBio, geneBio, "erk1");
        setDrugGeneRelation(erlotinibBio, geneBio, "erk1");
        // protein
        // P01116

        // P35354 (PTGS3) cox-2
        Protein proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P35354");
        if (proteinBio != null) {
            Node proteinNode = BioEntityTemplate.getNode(proteinBio);
            if (proteinBio != null) {
                System.out.println("protein for PTGS3  = " + getLabel(proteinNode) + proteinNode.getId());
            }
            setDrugProteinRelation(cetuximabBio, proteinBio);
            setDrugProteinRelation(erlotinibBio, proteinBio);
            setDrugProteinRelation(glucaGonBio, proteinBio);
        }

        //MAPK3,  P27361 (protein)  pubmed: 15213626
        proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P27361");
        if (proteinBio != null) {
            Node proteinNode = BioEntityTemplate.getNode(proteinBio);
            if (proteinBio != null) {
                System.out.println("protein of MAPK3 = " + getLabel(proteinNode) + proteinNode.getId());
            }
            setDrugProteinRelation(cetuximabBio, proteinBio);
            setDrugProteinRelation(erlotinibBio, proteinBio);
            setDrugProteinRelation(glucaGonBio, proteinBio);
        }


        //KRAS,  pubmed: 16043828,  compound1146.xml
        proteinBio = BioEntityTemplate.getBioEntity(BioTypes.PROTEIN, "P01116");
        if (proteinBio != null) {
            Node proteinNode = BioEntityTemplate.getNode(proteinBio);
            if (proteinBio != null) {
                System.out.println("protein of KRAS = " + getLabel(proteinNode) + proteinNode.getId());
            }
            setDrugProteinRelation(cetuximabBio, proteinBio);
            setDrugProteinRelation(erlotinibBio, proteinBio);
            setDrugProteinRelation(glucaGonBio, proteinBio);
        }
    }

}
