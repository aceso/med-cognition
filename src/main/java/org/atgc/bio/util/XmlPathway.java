/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import java.net.URISyntaxException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;
import org.neo4j.rest.graphdb.RestGraphDatabase;
//import org.neo4j.graphdb.factory.GraphDatabaseFactory;
//import org.neo4j.rest.graphdb.RestGraphDatabase;

/**
 *
 * @author jtanisha-ee
 */
public class XmlPathway {
/*
    private static JSON xmlUrlToJson(String string) {
        throw new UnsupportedOperationException("Not yet implemented");
    }
     */
    
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
    
    /**
     * Neo4j Example
     * Creates node/property/relationship in Neo4J
     * @param args
     * @throws Exception 
     */
    public static void main(String[] args) throws Exception {
        /*
       XMLToJson json = new XMLToJson();
        json.xmlUrlToJson("http://hotdiary.com/nci/disease/disease1220.xml");
        System.out.println("json = " + json.toString());
        //FileWrite.writeToFile(json.toString(), "test.txt");
        //System.out.println("map = " + testJackson(json).toString());
        BasicDBList basicDbList = jsonToBasicDBObject(json);
        System.out.println("basicDbList = " + jsonToBasicDBObject(json).toString());
        MongoUtil mongoUtil;
        MongoCollection mongoCollection;
        
        try {
            mongoUtil = new MongoUtil("saibaba.local", "27017", "atgc");
            mongoCollection = mongoUtil.getCollection("test");
            //Map map = new HashMap();
            //map.put("D", 1);
            mongoCollection.insert(new BasicDBObject(basicDbList.toMap()));
        } catch (Exception e) {
            e.printStackTrace();
        }
         */
    }
}

