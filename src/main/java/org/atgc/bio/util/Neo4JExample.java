/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import java.net.URISyntaxException;
import java.util.Iterator;

import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;

/**
 *
 * @author jtanisha-ee
 */
public class Neo4JExample {
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
        graphDb = new RestGraphDatabase("http://localhost:7474/db/data");
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


        RestIndex<Node> myIndex;
        String indexName = "myindex";
        String key = "message";

        Transaction tx = graphDb.beginTx();


        try {
    // Mutating operations go here
            firstNode = graphDb.createNode();
            //firstNode.setProperty(key, "prime");
            /*
            A label is a grouping facility for Node where all nodes having a label are part of the same group. Labels on nodes are optional
            and any node can have an arbitrary number of labels attached to it. Objects of classes implementing this interface can be used as
            label representations in your code. It's very important to note that a label is uniquely identified by its name, not by any particular
            instance that implements this interface. This means that the proper way to check if two labels are equal is by invoking equals() on
            their names, NOT by using Java's identity operator (==) or equals() on the Label instances. A consequence of this is that you can
            NOT use Label instances in hashed collections such as HashMap and HashSet. However, you usually want to check whether a specific node
            instance has a certain label. That is best achieved with the Node.hasLabel(Label) method. For labels that your application know up
            front you should specify using an enum, and since the name is accessed using the name() method it fits nicely. public enum MyLabels
            implements Label { PERSON, RESTAURANT; } For labels that your application don't know up front you can make use of
            DynamicLabel.label(String), or your own implementation of this interface, as it's just the name that matters.
             */
            firstNode.addLabel(DynamicLabel.label("test"));

            secondNode = graphDb.createNode();
            secondNode.setProperty(key, "galaxy" );
            secondNode.addLabel(DynamicLabel.label("test"));

            relationship = firstNode.createRelationshipTo(secondNode, RelTypes.INHIBITS );
            relationship.setProperty( "message", "brave heart Neo4j ");

            myIndex = graphDb.index().forNodes(indexName);
            myIndex.add(firstNode, key, "prime");
            myIndex.add(secondNode, key, "galaxy");

            tx.success();
            
        } finally {
            //tx.finish();
            tx.close();
        }

        tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            IndexHits<Node> pNodeHits = myIndex.get(key, "prime");
            if (pNodeHits.size() > 0) {
                firstNode = pNodeHits.getSingle();
                Iterator j = firstNode.getLabels().iterator();
                while (j.hasNext()) {
                    String k = ((Label)j.next()).toString();
                    System.out.println("firstNode label = " + k);
                }
                Iterator i = firstNode.getPropertyKeys().iterator();
                while (i.hasNext()) {
                    String k = (String)i.next();
                    System.out.println("firstNode key = " + k + ", value = " + firstNode.getProperty(k));
                }
             }
            pNodeHits = myIndex.get(key, "galaxy");
            if (pNodeHits.size() > 0) {
                secondNode = pNodeHits.getSingle();
                System.out.println("secondNode from index = " + secondNode);
                Iterator j = secondNode.getLabels().iterator();
                while (j.hasNext()) {
                    String k = ((Label)j.next()).toString();
                    System.out.println("secondNode label = " + k);
                }
                Iterator i = secondNode.getPropertyKeys().iterator();
                while (i.hasNext()) {
                    String k = (String)i.next();
                    System.out.println("secondNode key = " + k + ", value = " + secondNode.getProperty(k));
                }
            }
            pNodeHits.close();
            System.out.println("firstNode id = " + firstNode.toString());
            System.out.println("secondNode id = " + firstNode.toString());
        } finally {
            //tx.finish();
            tx.close();
        }
    }
}