/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import java.net.URISyntaxException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;
//import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//import org.neo4j.rest.graphdb.RestGraphDatabase;

/**
 *
 * @author jtanisha-ee
 */
public class IndexSearchExample {
    private static enum RelTypes implements RelationshipType {
       INHIBITS
    }
    
    private static RestGraphDatabase graphDb;
    private static Node firstNode;
    private static Node secondNode;
    private static Relationship relationship;
    private static final String DB_PATH= "neo4j-shortest-path";
    
    private static void setup() throws URISyntaxException {
        //graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        graphDb = new RestGraphDatabase("http://saibaba.local:7474/db/data");
        registerShutdownHook( graphDb );
    }
    
    private static void registerShutdownHook( RestGraphDatabase graphDb1) {
    // Registers a shutdown hook for the Neo4j instance so that it
    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
    // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread() {
        @Override
        public void run() {
            graphDb.shutdown();
        }
    } );
    }
    
    public static void main(String[] args) throws Exception {
        setup();
        
        Transaction tx = graphDb.beginTx();
        try {
            firstNode = graphDb.createNode();
            //String[] species = {"human", "mouse", "yeast"};
            String species = "human mouse";
            RestIndex<Node> speciesIndex;
            firstNode.setProperty("species", species);
            speciesIndex = graphDb.index().forNodes("aspecies");
            speciesIndex.add(firstNode, "species", species);
            tx.success();

            IndexHits<Node> pNodeHits = speciesIndex.get("species", "mouse");
            if (pNodeHits.size() > 0) { // if molecule node already exists
                for (Node n : pNodeHits) {
                    if (n.hasProperty("species")) {
                        System.out.println(n.getProperty("species"));
                    }
                }
            }
            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            //tx.finish();
            tx.close();
        }
    }
}