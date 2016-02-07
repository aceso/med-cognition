/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.*;
import org.atgc.bio.repository.CompoundKey;
import org.atgc.bio.repository.PrimaryKey;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Date;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Update and check the status of Neo4J import. Everytime a BioEntity is
 * completely added to Neo4J, the designer can decide to insert an id into
 * this collection "status{bioType}". The existence of an entry in this
 * collection is an indication that this BioEntity need not be imported
 * again. There is one status collection per new BioType.
 *
 * <p>
 * Some bioentities take quite long to load from mongo into Neo4j.
 * So we want to make sure we save time not to reload it from mongo into
 * neo4j. RedbasinTemplate will make calls to idInsert and other classes
 * can make calls to idExists. The reason we do not want to enforce the
 * loading of ids in RedbasinTemplate by making calls to idExists, is that
 * many bioentities may not be ready to be saved in final version. The
 * applications decide whether or not to include the BioEntity in the subgraph
 * for persistence to Neo4J by optionally checking if the idExists.
 * </p>
 *
 * @author jtanisha-ee
 * @param <T>
 */
public class StatusUtil<T> {
    protected static Logger log = LogManager.getLogger(StatusUtil.class);

    private static BasicDBObject getObjectQuery(String key, String id) {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(key, id);
        return queryMap;
    }

    private static MongoCollection getCollection(String coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll);
    }

    private static DBObject getObject(String coll, String key, String id) throws UnknownHostException, RuntimeException {
        MongoCollection collection = getCollection(coll);

        BasicDBList result = collection.findDB(getObjectQuery(key, id));
        if ((result == null) || (result.isEmpty())) {
            return null;
        }
        if (result.size() != 1) {
            log.error("result size = " + result.size());
            throw new RuntimeException("Multiple pathway objects detected for id: " + id);
        }
        return (DBObject) result.get(0);
    }

    /**
     * Checking if an id exists might result in better performance.
     * But it's optional. The idInsert method does check if it is already there.
     *
     * @param bioType
     * @param key
     * @param id
     * @return
     * @throws UnknownHostException
     */
    public static boolean idExists(String bioType, String key, String id) throws UnknownHostException {
        String coll = StatusFields.STATUS + bioType;
        MongoCollection collection = getCollection(coll);
        long cnt = collection.count(getObjectQuery(key, id));
        return cnt != 0;
    }

    /**
     * Checking if an id exists might result in better performance.
     * But it's optional. The idInsert method does check if it is already there.
     *
     * @param <T>
     * @param bioType
     * @return
     * @throws UnknownHostException
     * @throws java.net.URISyntaxException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> boolean idExists(T bioType) throws UnknownHostException, IllegalArgumentException, IllegalAccessException, URISyntaxException, NoSuchFieldException {
        String key;
        String value;
        CompoundKey compoundKey = CompoundKey.getCompoundKey(bioType);
        if (compoundKey == null) {
            PrimaryKey primaryKey = PrimaryKey.getPrimaryKey(bioType);
            key = primaryKey.getKey();
            value = primaryKey.getValue();
        } else {
            key = compoundKey.getKey();
            value = compoundKey.getValue();
        }
        String coll = StatusFields.STATUS + bioType.getClass().getSimpleName();
        MongoCollection collection = getCollection(coll);
        long cnt = collection.count(getObjectQuery(key, value));
        return cnt != 0;
    }

    /**
     * We check if the id is there, and if not we insert it. We also create
     * an index if it does not already exist.
     *
     * @param bioType
     * @param key
     * @param id
     * @return
     * @throws UnknownHostException
     */
    public static WriteResult idInsert(String bioType, String key, String id) throws UnknownHostException {
        String coll = StatusFields.STATUS + bioType;
        DBObject obj = getObject(coll, key, id);
        if (obj == null) {
            MongoCollection statusCollection = getCollection(coll);
            statusCollection.ensureIndex(key, key, true);
            //DBObject dbObject = XMLToJson.stringToBasicDBObject("{" + key + ":" + "\'"+ id + "\',"   + StatusFields.DATE + ":" + "\'" +  new Date() + "\'" + "}");
            BasicDBObject basicDBObject = (BasicDBObject) new BasicDBObject(key, id);
            basicDBObject.put(StatusFields.DATE.toString(), new Date());
            log.info("bioType = " + bioType + ", key = " + key + ", id = " + id);
            WriteResult writeResult = statusCollection.insert(basicDBObject);
            return writeResult;
        }
        return null;
    }

    /**
     * We check if the id is there, and if not we insert it. We also create
     * an index if it does not already exist.
     *
     * @param <T>
     * @param bioType
     * @return
     * @throws UnknownHostException
     * @throws java.lang.IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> WriteResult idInsert(T bioType) throws UnknownHostException, IllegalArgumentException, IllegalAccessException, URISyntaxException, NoSuchFieldException {
        String key;
        String value;
        CompoundKey compoundKey = CompoundKey.getCompoundKey(bioType);
        if (compoundKey == null) {
            PrimaryKey primaryKey = PrimaryKey.getPrimaryKey(bioType);
            if (primaryKey == null) {
                log.warn("Primary key is null!");
                return null;
            }
            key = primaryKey.getKey();
            value = primaryKey.getValue();
            log.info("primary key obtained");
        } else {
            key = compoundKey.getKey();
            value = compoundKey.getValue();
            log.info("secondary key obtained");
        }
        log.info("bioType = " + bioType.toString());
        if (idExists(bioType)) {
            return null;
        }
        String coll = StatusFields.STATUS + bioType.getClass().getSimpleName();
        DBObject obj = getObject(coll, key, value);
        if (obj == null) {
            MongoCollection statusCollection = getCollection(coll);
            log.info("coll = " + coll + ", key = " + key + ", value = " + value);
            statusCollection.ensureIndex(key, key, true);
            //DBObject dbObject = XMLToJson.stringToBasicDBObject("{" + key + ":" + "\'"+ id + "\',"   + StatusFields.DATE + ":" + "\'" +  new Date() + "\'" + "}");
            BasicDBObject basicDBObject = (BasicDBObject) new BasicDBObject(key, value);
            basicDBObject.put(StatusFields.DATE.toString(), new Date());
            WriteResult writeResult = statusCollection.insert(basicDBObject);
            log.info("coll = " + coll + ", key = " + key + ", value = " + value);
            return writeResult;
        }
        return null;
    }

    public static void main(String[] args) throws UnknownHostException {
        idInsert("protein", "foo", "bar2");
        log.info(idExists("protein", "foo", "bar2"));
    }
}