/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.domain.BioTypes;
import org.atgc.http.HttpClient;
import org.atgc.json.XMLToJson;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;

import java.io.IOException;
import java.net.UnknownHostException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpException;

/**
 * > db.pubchemligands.ensureIndex({"PC_Compounds.0.id.id.cid" : 1}, {unique : true})
 * @author jtanisha-ee
 */
@SuppressWarnings("javadoc")
public class PubChemLigandMongoImport {

    protected static Logger log = LogManager.getLogger(PubChemLigandMongoImport.class);

    private static final String url = "http://pubchem.ncbi.nlm.nih.gov/rest/pug/compound/name/";
    private static final String JSON = "/JSON";
    private static final int NUM_RETRIES = 3;
    private static final String CHEMICAL_ID = "chemicalId";

    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }

    /**
     * Look up a field in a DBObject and return a String. If the value
     * is not found or null, then return null. If the class of the value
     * is not a String, then throw an exception.
     *
     * @param dbObject
     * @param field
     * @return
     */
    private static String getString(DBObject dbObject, String field) {
        if (dbObject == null || field == null) {
            return null;
        }
        Object obj = dbObject.get(field);
        if (obj == null) {
            return null;
        }
        if (!obj.getClass().equals(String.class)) {
            log.error("Expected String object but found " + obj.getClass().getName() + ", field = " + field);
            return null;
        }
        //log.info(field.toString() + " = " + obj);
        return (String)obj;
    }

    public static BasicDBObject getPubChemQuery(String chemicalId) {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(CHEMICAL_ID, chemicalId);
        return queryMap;
    }

    public static boolean pubchemExists(ImportCollectionNames coll, String chemicalId) throws UnknownHostException {
        MongoCollection collection = getCollection(coll);

        BasicDBList result = collection.findDB(getPubChemQuery(chemicalId));
        if ((result == null) || (result.isEmpty())) {
            return false;
        }
        if (result.size() != 1) {
            log.error("result size = " + result.size());
            throw new RuntimeException("Multiple pubmedId objects detected for chemicalId: " + chemicalId);
        }
        return true;
    }

    public static void loadPubChemLigands() throws InterruptedException, IOException, HttpException {
        MongoCollection pubchemColl = getCollection(ImportCollectionNames.PUBCHEM_LIGANDS);

        try (DBCursor dbCursor = getCollection(ImportCollectionNames.LIGAND).findDBCursor("{}, {\"@chemicalID\" : 1}")) {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject) dbCursor.next();
                String chemicalId = getString(result, "@chemicalID");
                if (!pubchemExists(ImportCollectionNames.PUBCHEM_LIGANDS, chemicalId)) {
                    String myurl = url + chemicalId + JSON;
                    log.info("myurl = " + myurl);
                    //String content = HttpPostUtil.get(myurl, ContentTypes.JSON, "");
                    String content = null;
                    try {
                        content = HttpClient.get(myurl);
                    } catch (Exception e) {
                        log.error(BioTypes.LIGAND + "Could not fetch url " + myurl, e);
                    }
                    if (content != null) {
                        //log.info(content);
                        BasicDBObject res = XMLToJson.stringToBasicDBObject(content);
                        res.put(CHEMICAL_ID, chemicalId);
                        pubchemColl.insert(res);
                    }
                    Thread.sleep(100);
                }
            }
        }
    }

    public static void main(String[] args) throws InterruptedException, IOException, HttpException {
        loadPubChemLigands();
    }
}
