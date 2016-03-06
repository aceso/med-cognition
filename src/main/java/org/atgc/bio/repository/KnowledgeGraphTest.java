/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.io.fs.DefaultFileSystemAbstraction;
import org.neo4j.kernel.StoreLocker;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;

/**
 * KnowledgeGraph - Traversal
 * @author jtanisha-ee
 *         Use BioFields to fetch BioEntities and Nodes
 *
 *         References:
 *  https://github.com/neo4j/neo4j/blob/2.3.2/community/embedded-examples/src/main/java/org/neo4j/examples/NewMatrix.java
 *  http://www.informit.com/articles/article.aspx?p=2415371#articleDiscussion
 */
@SuppressWarnings("javadoc")
public class KnowledgeGraphTest {

    protected static final Logger log = LogManager.getLogger(KnowledgeGraphTest.class);
    private static final String dbPath = "/Users/smitha/Documents/Neo4j-IE/default.graphdb";
    private static GraphDatabaseService graphDb;

    private static void setup() throws URISyntaxException {
            try {
                StoreLocker lock = new StoreLocker(new DefaultFileSystemAbstraction());
                lock.checkLock(new File(dbPath));
                try {
                    lock.release();
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                File dbFile = new File(dbPath);
                graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);
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
                .addShutdownHook( new Thread()
                {
                    @Override
                    public void run()
                    {
                        graphDb.shutdown();
                    }
                } );
    }



    public enum Labels implements Label {
        PROTEIN,
        PUBMED,
        GENE
    }

    public static void main(String[] args) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException {
        setup();
        Transaction tx = graphDb.beginTx();
        try {
           // createGraph();
           // getDepthFromProtein();
           // traverseGraph();
            getGraphPath();
            tx.success();
        } catch(Exception e) {
            tx.failure(); //rollback
            throw new RuntimeException("Something went wrong with access while accessing bioentity. msg=" + e.getMessage(), e);
        } finally {
            tx.finish();
            tx.close();
        }
        //registerShutdownHook();

    }

    public enum RelationshipTypes implements RelationshipType {
        INTERACTS_WITH,
        REFERENCES_PUBMED;
    }


    public static Relationship setPubMedRelation(Node protein, Node pubmed, int ref)
    {
        Relationship relationship = protein.createRelationshipTo( pubmed, RelationshipTypes.REFERENCES_PUBMED );
        relationship.setProperty( "primaryRef", ref );
        return relationship;
    }




    // node.addLabel(DynamicLabel.label(bioType.toString()));


    /**
     * @throws IllegalAccessException
     */

    public static void createGraph() throws IllegalAccessException, UnsupportedEncodingException{
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



    public static Node getNode(Label label, String propName, String propValue) {
        Node node = graphDb.findNode(label, propName, propValue);

        // BRCA1
        // Long id = 18L;
        //Node node = graphDb.getNodeById(id);
        System.out.println("found =" + label.name() + ",propValue=" + node.getProperty("name") + " ,label" + node.getLabels().toString());
        return node;
    }


    public static void traverseGraph() {
        //Object value = "P53";
        //Node node = graphDb.findNode(Labels.PROTEIN, "name", value);
        Node node = getNode(Labels.PROTEIN, "name", "P53");

        // traversal for BRCA1
        TraversalDescription proteinArticles = graphDb.traversalDescription()
                .breadthFirst()
                .evaluator( Evaluators.atDepth(1));


        // returns pmid3, pmid2, pmid1
        Traverser traverser = proteinArticles
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


    public static void getDepthFromProtein() throws UnsupportedEncodingException {
       getProteinArticlesTraverser(getNode(Labels.PROTEIN, "name", "P53"));
    }

    /* getProteinArticleTraverser */
    /* works for until depth 3 for references pubmed */
    public static Traverser getProteinArticlesTraverser(Node node) throws UnsupportedEncodingException {

        // traversal
        TraversalDescription proteinArticles = graphDb.traversalDescription()
               .breadthFirst()
                //.depthFirst()
                .evaluator( Evaluators.atDepth(4));

        Traverser traverser = proteinArticles

                //.relationships(DynamicRelationshipType.withName(
                      //  URLEncoder.encode(BioRelTypes.REFERENCES_PUBMED.toString(), "UTF-8")),
                      //  Direction.OUTGOING)
                .traverse(node);

        ResourceIterator<Path> iterator = traverser.iterator();
        while (iterator.hasNext()) {
            Path path = iterator.next();
            System.out.println("path =" + path);
            for (Node n : path.nodes()) {
                System.out.println("path node =" + n.getProperty("name"));
            }
        }
        System.out.println( "pubmed articles of " + node.getProperty("name") + " refer to: " );
        for( Node protein : traverser.nodes() ) {
            System.out.println( "\t" + protein.getProperty("name") );
        }
        return traverser;

    }


    /**
     * getGraphPath
     * uses PathExpander and Evaluator
     */
    public static void getGraphPath() throws UnsupportedEncodingException {

        System.out.println("getGraphPath using PathExpander");

        TraversalDescription proteinsThatReferencePubMed = graphDb.traversalDescription()
                // Choose a depth-first search strategy
                .breadthFirst()
                // At depth 0 traverse the INTERACTS_WITH relationships,
                // at a depth of 1 traverse the REFERENCES_PUBMED relationship
                .expand( new PathExpander<Object>() {
                    @Override
                    public Iterable<Relationship> expand(Path path, BranchState<Object> objectBranchState) {
                        // Get the depth of this node
                        int depth = path.length();
                        System.out.println("depth =" + depth);

                        if (depth == 0) {
                            // Depth of 0 means the user's node (starting node)
                            System.out.println("interacts_with" + path.endNode().getProperty("name"));
                            return path.endNode().getRelationships();
                            //return path.endNode().getRelationships(
                                 //   RelationshipTypes.REFERENCES_PUBMED );
                          //  return path.endNode().getRelationships(
                            //        RelationshipTypes.INTERACTS_WITH);
                        } else {
                            System.out.println("depth=" + depth + " path=" + path);
                            System.out.println("depth=4, node=" + path.endNode().getProperty("name"));
                            if (isNodeGene(path.endNode())) {
                                System.out.println("Gene node" + path.endNode().getProperty("name"));
                            }
                            if (isRelationship(path.endNode(), BioRelTypes.NEGATIVELY_REGULATES)) {
                                System.out.println("negatively regulates for enzyme on drug" + path.endNode().getProperty("name"));
                                return path.endNode().getRelationships(
                                        DynamicRelationshipType.withName(URLEncoder.encode(
                                                BioRelTypes.NEGATIVELY_REGULATES.toString())));
                            }
                            //return path.endNode().getRelationships(
                             //       DynamicRelationshipType.withName(URLEncoder.encode(
                              //              BioRelTypes.NEGATIVELY_REGULATES.toString())));
                           // return path.endNode().getRelationships(
                             //     RelationshipTypes.REFERENCES_PUBMED );
                            return path.endNode().getRelationships();
                        }
                    }
                    @Override
                    public PathExpander<Object> reverse() {
                        return null;
                    }
                })

                // Only go down to a depth of 4
                .evaluator( Evaluators.atDepth(4))

                // Only return DRUG
                .evaluator(new Evaluator() {
                    @Override
                    public Evaluation evaluate(Path path) {
                        System.out.println("evaluate path=" + path);
                        if (isRelationship(path.endNode(), BioRelTypes.NEGATIVELY_REGULATES)) {
                            System.out.println("negatively regulates relationship exists, node=" +path.endNode().getProperty("name"));
                        }
                        if (isNodeGene(path.endNode())) {
                            System.out.println("Node is a Gene " + path.endNode().getProperty("name"));
                        }
                        System.out.println("evaluate() path.endNode() " + path.endNode().getProperty("name"));
                        if( path.endNode().hasLabel(DynamicLabel.label(BioTypes.GENE.toString()))) {
                            System.out.println("include and continue, gene" + path.endNode().getProperty("name"));
                            return Evaluation.INCLUDE_AND_CONTINUE;
                        }
                        return Evaluation.EXCLUDE_AND_CONTINUE;
                    }
                });


        Node node = getNode(Labels.PROTEIN, "name", "P53");
        Traverser traverser = proteinsThatReferencePubMed.traverse(node);
        System.out.println( "traversing the protein " + node.getProperty("name"));
        for( Node n : traverser.nodes()) {
            System.out.println( "\t" + n.getProperty("name"));
        }
    }


    /* checks if this node is a gene */
    private static boolean isNodeGene(Node node) {
        return node.hasLabel(DynamicLabel.label(BioTypes.GENE.toString())) ? true : false;
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
