package org.atgc.bio.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.*;
import org.atgc.bio.*;
import org.atgc.bio.domain.*;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
public class EnzymeCitationImport {
    
    protected static Logger log = LogManager.getLogger(EnzymeCitationImport.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
     
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.ENZYME).findDBCursor("{}" );
       
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                System.out.println("result =" + result.toString());           
                String citeKey = (String)result.get(EnzymeFields.CITE_KEY.toString());
                log.info("###### citeKey =" + citeKey);
                // until then a quick fix. later remove it.
                try {
                    processEnzymeCiteKey(citeKey);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
            }
        } finally {
           dbCursor.close();
        }   
    }
    
    public static boolean isObj(BasicDBObject obj) {
           if (obj.get(EnzymeFields.TEXT.toString()) != null) {
              // log.info(field.toString() + " value exists");
              return true;
           }
        return false;
    }
    
    public static String getValue(BasicDBObject obj) {
        return obj.getString(EnzymeFields.TEXT.toString());
    }
    
    
    public static Enzyme getEnzyme(String ecNum, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
        Enzyme enzyme = (Enzyme)subGraph.search(BioTypes.ENZYME, BioFields.ENZYME_ID, ecNum);
        if (enzyme == null) {
            enzyme = EnzymeImport.getEnzyme(ecNum, subGraph);
        }
        return enzyme;
    }
    
    public static String getPubMedId(Map map) {
        return (String)map.get(EnzymeCitationFields.PUBMED_ID.toString());
    }
    
    
    public static void processEnzymeCiteKey(String citeKey) throws UnknownHostException, RuntimeException, Exception {
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put(EnzymeCitationFields.CITE_KEY.toString(), citeKey);
            DBCursor dbCursor = getCollection(ImportCollectionNames.ENZYME_CITATION).findDBCursor(basicDBObject);
            try {
                if (dbCursor.hasNext()) {
                    BasicDBObject result = (BasicDBObject)dbCursor.next();
                    log.info("map" + result.toString());
                    log.info("citeKey =" + result.get(EnzymeCitationFields.CITE_KEY.toString()));
                    log.info("pubMedId =" + result.get(EnzymeCitationFields.PUBMED_ID.toString()));
                    //log.info("row =" + map.get(EnzymeFields.ROW.toString()));
                    String pubMedId = getPubMedId(result);
                    if (pubMedId != null) { 
                        Subgraph subGraph = new Subgraph();
                        createCitationRelations(citeKey, pubMedId, subGraph);
                        RedbasinTemplate.saveSubgraph(subGraph);
                    }
                }
            } finally {
                dbCursor.close();
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
    public static List<Map> getEnzymeIdList(String citeKey) throws UnknownHostException, RuntimeException, Exception {
        return EnzymeCiteKeyUtil.getEcNumObjects(citeKey);
        
    }
    
    public static void createPubMedRelation(PubMed pubMed, Enzyme enzyme) throws Exception {
        log.info("createPubMedRelation");
          pubMed.setPubMedRelation(enzyme);
    }
    
    public static void createCitationRelations(String citeKey, String pubMedId, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {  
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
     * @param pubMed  {@link BioTypes#PubMed}
     * @param ecNumMaps - enzymeNumber  
     */
    public static void createEnzymePubMedRelation(PubMed pubMed, List<Map> ecNumMaps, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
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
    public static PubMed getPubMed(String pubMedId, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, Exception {
          PubMed pubMed = (PubMed)subGraph.search(BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);    
          if (pubMed == null) {
             pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
          }
          return pubMed;
    }
    
}