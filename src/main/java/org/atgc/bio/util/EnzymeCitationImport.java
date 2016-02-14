package org.atgc.bio.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.apache.http.HttpException;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.*;
import org.atgc.bio.domain.*;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.neo4j.graphdb.NotFoundException;

/**
 * EnzymeCitation refers to pubmed citations for a given citekey.
 * "cite_key" : "beg-zh-1985-1682",
 * "pubmed_id" : "3155737",
 * 
 * enzymecitekey maps:
 * These are mapped to citekey which consists of <author-<key>-<dateofpublication->
 * citekey is mapped to enzymeidnumer in enzymecitekey collection
 * 
 * eg:"cite_key" : "oudot-c-2001-3047",
 *     "ec_num" : "2.7.11.5",
 * 
 * 
 * @author jtanisha-ee
 */
@SuppressWarnings("javadoc")
public class EnzymeCitationImport {
    
    protected static Logger log = LogManager.getLogger(EnzymeCitationImport.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
     
    public static void main(String[] args) throws UnknownHostException {

        try (DBCursor dbCursor = getCollection(ImportCollectionNames.ENZYME).findDBCursor("{}")) {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject) dbCursor.next();
                log.info("result =" + result.toString());
                String citeKey = (String) result.get(EnzymeFields.CITE_KEY.toString());
                log.info("###### citeKey =" + citeKey);
                // until then a quick fix. later remove it.
                try {
                    processEnzymeCiteKey(citeKey);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
                log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
                log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
            }
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
        log.info("Completed successfully!");
    }

    /*
    public static boolean isObj(BasicDBObject obj) {
           if (obj.get(EnzymeFields.TEXT.toString()) != null) {
              // log.info(field.toString() + " value exists");
              return true;
           }
        return false;
    }*/
    
    public static String getValue(BasicDBObject obj) {
        return obj.getString(EnzymeFields.TEXT.toString());
    }
    
    
    public static Enzyme getEnzyme(String ecNum, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException {
        Enzyme enzyme = (Enzyme)subGraph.search(BioTypes.ENZYME, BioFields.ENZYME_ID, ecNum);
        if (enzyme == null) {
            enzyme = EnzymeImport.getEnzyme(ecNum, subGraph);
        }
        return enzyme;
    }
    
    public static String getPubMedId(Map map) {
        return (String)map.get(EnzymeCitationFields.PUBMED_ID.toString());
    }
    
    
    public static void processEnzymeCiteKey(String citeKey) throws IOException, RuntimeException, URISyntaxException, HttpException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, InterruptedException {
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put(EnzymeCitationFields.CITE_KEY.toString(), citeKey);
        try (DBCursor dbCursor = getCollection(ImportCollectionNames.ENZYME_CITATION).findDBCursor(basicDBObject)) {
            if (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject) dbCursor.next();
                log.info("map" + result.toString());
                log.info("citeKey =" + result.get(EnzymeCitationFields.CITE_KEY.toString()));
                log.info("pubMedId =" + result.get(EnzymeCitationFields.PUBMED_ID.toString()));
                //log.info("row =" + map.get(EnzymeFields.ROW.toString()));
                String pubMedId = getPubMedId(result);
                if (pubMedId != null) {
                    Subgraph subGraph = new Subgraph();
                    createCitationRelations(citeKey, pubMedId, subGraph);
                    PersistenceTemplate.saveSubgraph(subGraph);
                }
            }
        }
    }   
    
    
    /**
     * Given a citation key returns the enzymes associated with this citation key
     * @param citeKey
     * @return List<Map>
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static List<Map> getEnzymeIdList(String citeKey) throws UnknownHostException, RuntimeException {
        return EnzymeCiteKeyUtil.getEcNumObjects(citeKey);
        
    }
    
    public static void createPubMedRelation(PubMed pubMed, Enzyme enzyme)  {
        log.info("createPubMedRelation");
          pubMed.setPubMedRelation(enzyme);
    }
    
    public static void createCitationRelations(String citeKey, String pubMedId, Subgraph subGraph) throws IOException, RuntimeException, IllegalAccessException, InterruptedException, HttpException, URISyntaxException, InvocationTargetException, NoSuchFieldException {
        /**
         * Returns ec_num (enzyme numbers) from enzymecitekey collection
         */
        List<Map> ecNumMaps = getEnzymeIdList(citeKey);
        log.info("ecNum List for citeKey  =" + citeKey + " " + ecNumMaps.toString());
        
        /**
         * All BioEntities related with PubMed are added in the SubGraph
         *  {@link JournalIssue} {@link Journal } {@link Author}
         */ 
        PubMed pubMed = getPubMed(pubMedId, subGraph);
        if (pubMed != null) { 
            log.info("pubMed entity is not null");
            createEnzymePubMedRelation(pubMed, ecNumMaps, subGraph);
        } else {
            log.info("pubMed entity is null");
        }
    } 
    
    /**
     * createEnzymePubMedRelation
     * @param pubMed
     * @param ecNumMaps - enzymeNumber
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception
     */
    public static void createEnzymePubMedRelation(PubMed pubMed, List<Map> ecNumMaps, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException {
        for (Map map : ecNumMaps) {
            if (map.size() > 0) {
                String ecNum = (String)map.get(EnzymeFields.EC_NUM.toString());
                log.info("ecNum =" + ecNum);
                if (ecNum != null) {
                    Enzyme enzyme = getEnzyme(ecNum, subGraph);
                    if (enzyme != null) {
                        createPubMedRelation(pubMed, enzyme);
                    }
                }
            }
         }
    }
    
    
    /**
     * get PubMed
     * @param pubMedId
     * @param subGraph
     * @return PubMed {@link PubMed}
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws Exception 
     */
    public static PubMed getPubMed(String pubMedId, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, URISyntaxException, IOException, HttpException, InterruptedException, InvocationTargetException {
          PubMed pubMed = (PubMed)subGraph.search(BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);    
          if (pubMed == null) {
             pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
          }
          return pubMed;
    }
    
}