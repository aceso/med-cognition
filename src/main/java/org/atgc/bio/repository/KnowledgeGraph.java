/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;


import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.factory.GraphDatabaseFactory;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.graphdb.traversal.*;
import org.neo4j.graphdb.traversal.Traverser;
import org.neo4j.io.fs.DefaultFileSystemAbstraction;
import org.neo4j.kernel.StoreLocker;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;

import org.neo4j.graphdb.Label;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

import org.neo4j.rest.graphdb.RestGraphDatabase;


import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;

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
public class KnowledgeGraph {

    protected static final Logger log = LogManager.getLogger(KnowledgeGraph.class);
    private static final String dbPath = "/Users/smitha/Documents/Neo4j-IE/default.graphdb";
    private static GraphDatabaseService graphDb;

    private static void setup() throws URISyntaxException {
        //static {
            try {
                StoreLocker lock = new StoreLocker(new DefaultFileSystemAbstraction());
                lock.checkLock(new File(dbPath));
                try {
                    lock.release();
                    // graph = new GraphDatabaseFactory().newEmbeddedDatabase(DB_Location);
                } catch (IOException e1) {
                    e1.printStackTrace();
                }
                //setup();
                File dbFile = new File(dbPath);
                graphDb = new GraphDatabaseFactory().newEmbeddedDatabase(dbFile);
            } catch (Exception ex) {
              log.error("Could not initialize RestGraphDatabase ", ex);
                throw new RuntimeException("uri", ex);
            }
        //}

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

    public static void main(String[] args) throws IllegalAccessException, URISyntaxException {
        setup();
        Transaction tx = graphDb.beginTx();
        try {
            //createGraph();
            traverseGraph();
            getGraphPath();
            tx.success();
        } catch(Exception e) {
            tx.failure(); //rollback
            throw new RuntimeException("Something went wrong with access while saving bioentity.", e);
        } finally {
            //tx.finish();
           // tx.close();
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


    public static void createGraph() throws IllegalAccessException {
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
        pmid4.setProperty("name", "pmid5");


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
        System.out.println( "articles: " );
        for( Node protein : traverser.nodes() ) {
            System.out.println( "\t" + protein.getProperty( "name" ) );
        }


        // Returns ERBB, BRCA2, IL, P53
        TraversalDescription proteinTd = graphDb.traversalDescription()
                .breadthFirst();
        Traverser proteinTraverser = proteinTd
                .relationships(RelationshipTypes.INTERACTS_WITH, Direction.BOTH)
                .traverse( node );
        System.out.println("Protein Interactions");
        for( Node protein : proteinTraverser.nodes()) {
            System.out.println( "\t" + protein.getProperty( "name" ) );
        }
    }


    /* getProteinArticleTraverser */
    public static Traverser getProteinArticlesTraverser(Node node) {

        // traversal
        TraversalDescription proteinArticles = graphDb.traversalDescription()
                .breadthFirst()
                .evaluator( Evaluators.atDepth(1));

        Traverser traverser = proteinArticles
                .relationships(RelationshipTypes.REFERENCES_PUBMED, Direction.OUTGOING)
                .traverse(node);
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
    public static void getGraphPath() {

        System.out.println("getGraphPath using PathExpander");

        TraversalDescription proteinsThatReferencePubMed = graphDb.traversalDescription()
                // Choose a depth-first search strategy
                .depthFirst()
                // At depth 0 traverse the INTERACTS_WITH relationships,
                // at a depth of 1 traverse the REFERENCES_PUBMED relationship
                .expand( new PathExpander<Object>() {
                    @Override
                    public Iterable<Relationship> expand(Path path, BranchState<Object> objectBranchState) {
                        // Get the depth of this node
                        int depth = path.length();

                        if( depth == 0 ) {
                            // Depth of 0 means the user's node (starting node)
                            System.out.println("interacts_with" + path.endNode().getProperty("name"));
                            return path.endNode().getRelationships(
                                    RelationshipTypes.INTERACTS_WITH );
                        }
                        else {
                            // A depth of 1 would mean that we're at a friend and
                            // should expand his HAS_SEEN relationships
                            System.out.println("references pubmed" + path.endNode().getProperty("name"));
                            return path.endNode().getRelationships(
                                    RelationshipTypes.REFERENCES_PUBMED );
                        }
                    }
                    @Override
                    public PathExpander<Object> reverse() {
                        return null;
                    }
                })

                // Only go down to a depth of 2
                .evaluator( Evaluators.atDepth( 2 ) )

                // Only return PUBMED
                .evaluator( new Evaluator() {
                    @Override
                    public Evaluation evaluate(Path path) {
                        System.out.println("path=" + path);
                        System.out.println("includes pubMed nodes " + path.endNode().getProperty("name"));
                        if( path.endNode().hasLabel(Labels.PUBMED ) ) {
                            return Evaluation.INCLUDE_AND_CONTINUE;
                        }
                        return Evaluation.EXCLUDE_AND_CONTINUE;
                    }
                });

        Node node = getNode(Labels.PROTEIN, "name", "ERBB");
        Traverser traverser = proteinsThatReferencePubMed.traverse(node);
        System.out.println( "articles that refer to other proteins that interact with " + node.getProperty("name"));
        for( Node pubmed : traverser.nodes()) {
            System.out.println( "\t" + pubmed.getProperty("name"));
        }
    }


    /**
     *
     * This is one level depth view
     * articles that refer to other proteins that interact with ERBB
     path=(22)
     includes pubMed nodes ERBB
     interacts_withERBB
     path=(22)<--[INTERACTS_WITH,31]--(18)
     includes pubMed nodes BRCA1
     references pubmedBRCA1
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
