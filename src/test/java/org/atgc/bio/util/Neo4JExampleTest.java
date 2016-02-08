/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;

import javax.validation.constraints.AssertFalse;
import java.net.URISyntaxException;
import java.util.Iterator;
import java.util.UUID;

/**
 *
 * @author jtanisha-ee
 */
public class Neo4JExampleTest {
    private static enum RelType implements RelationshipType {
       INHIBITS
    }

    private static RestGraphDatabase graphDb;

    private static Relationship relationship;
    private static final String DB_PATH= "neo4j-shortest-path";


    @Before
    public void setup() throws URISyntaxException {
        graphDb = new RestGraphDatabase("http://localhost:7474/db/data");
    }

    @After
    public void tearDown() {
        graphDb.shutdown();
    }

    @Test
    public void testSimpleIndex() {
        RestIndex<Node> myIndex;
        String indexName = UUID.randomUUID().toString();
        Transaction tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            tx.success();
        } catch (Exception e) {
            Assert.fail("Could not create index " + e.getMessage());
        } finally {
            tx.close();
        }
        tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            myIndex.delete();
            tx.success();
        } catch (Exception e) {
            Assert.fail("Could not create index " + e.getMessage());
        } finally {
            tx.close();
        }
    }

    @Test
    public void testIndexKey() {
        RestIndex<Node> myIndex;
        String indexName = UUID.randomUUID().toString();
        String key = UUID.randomUUID().toString();
        String value  = UUID.randomUUID().toString();
        String label  = UUID.randomUUID().toString();
        Node firstNode;
        Transaction tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            firstNode = graphDb.createNode();
            firstNode.setProperty(key, value);
            firstNode.addLabel(DynamicLabel.label(label));
            myIndex.add(firstNode, key, value);
            tx.success();
        } catch (Exception e) {
            Assert.fail("Could not test index key " + e.getMessage());
            tx.failure();
        } finally {
            tx.close();
        }

        tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            IndexHits<Node> pNodeHits = myIndex.get(key, value);
            if (pNodeHits.size() > 0) {
                firstNode = pNodeHits.getSingle();
                Assert.assertEquals(firstNode.getProperty(key), value);
            }
            pNodeHits.close();
            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.close();
        }
        tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            myIndex.delete();
            tx.success();
        } catch (Exception e) {
            Assert.fail("Could not create index " + e.getMessage());
        } finally {
            tx.close();
        }
    }

    @Test
    public void testNonIndexKey() {
        RestIndex<Node> myIndex;
        String indexName = UUID.randomUUID().toString();
        String key = UUID.randomUUID().toString();
        String value  = UUID.randomUUID().toString();
        String niKey = UUID.randomUUID().toString();
        String niValue  = UUID.randomUUID().toString();
        Node firstNode;
        Transaction tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            firstNode = graphDb.createNode();
            firstNode.setProperty(key, value);
            firstNode.setProperty(niKey, niValue);
            myIndex.add(firstNode, key, value);
            tx.success();
        } catch (Exception e) {
            Assert.fail("Could not test non index key " + e.getMessage());
            tx.failure();
        } finally {
            tx.close();
        }

        tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            IndexHits<Node> pNodeHits = myIndex.get(key, value);
            if (pNodeHits.size() > 0) {
                firstNode = pNodeHits.getSingle();
                Assert.assertEquals(firstNode.getProperty(niKey), niValue);
            }
            pNodeHits.close();
            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.close();
        }
        tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            myIndex.delete();
            tx.success();
        } catch (Exception e) {
            Assert.fail("Could not create index " + e.getMessage());
        } finally {
            tx.close();
        }
    }

    @Test
    public void testRelation() {
        RestIndex<Node> myIndex;
        String indexName = UUID.randomUUID().toString();
        String key = UUID.randomUUID().toString();
        String value  = UUID.randomUUID().toString();
        String skey = UUID.randomUUID().toString();
        String svalue  = UUID.randomUUID().toString();
        String relation = UUID.randomUUID().toString();
        String rkey = UUID.randomUUID().toString();
        String rvalue  = UUID.randomUUID().toString();
        Node firstNode;
        Node secondNode;
        Relationship relationship;
        Transaction tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            firstNode = graphDb.createNode();
            firstNode.setProperty(key, value);
            secondNode = graphDb.createNode();
            secondNode.setProperty(skey, svalue);
            myIndex.add(firstNode, key, value);
            myIndex.add(secondNode, skey, svalue);
            relationship = firstNode.createRelationshipTo(secondNode, RelType.INHIBITS);
            relationship.setProperty(rkey, rvalue);
            tx.success();
        } catch (Exception e) {
            Assert.fail("Could not test relation " + e.getMessage());
            tx.failure();
        } finally {
            tx.close();
        }

        tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            IndexHits<Node> pNodeHits = myIndex.get(key, value);
            if (pNodeHits.size() > 0) {
                firstNode = pNodeHits.getSingle();
                Iterable<Relationship> relationships = firstNode.getRelationships();
                for (Relationship r : relationships) {
                    Assert.assertEquals(r.getType().toString(), RelType.INHIBITS.toString());
                    break;
                }
            }
            pNodeHits.close();
            pNodeHits = myIndex.get(skey, svalue);
            if (pNodeHits.size() > 0) {
                secondNode = pNodeHits.getSingle();
                Iterable<Relationship> relationships = secondNode.getRelationships();
                for (Relationship r : relationships) {
                    Assert.assertEquals(r.getType().toString(), RelType.INHIBITS.toString());
                    break;
                }
            }
            pNodeHits.close();
            tx.success();
        } catch (Exception e) {
            tx.failure();
        } finally {
            tx.close();
        }
        tx = graphDb.beginTx();
        try {
            myIndex = graphDb.index().forNodes(indexName);
            myIndex.delete();
            tx.success();
        } catch (Exception e) {
            Assert.fail("Could not create index " + e.getMessage());
        } finally {
            tx.close();
        }
    }
}