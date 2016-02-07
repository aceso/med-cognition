package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.domain.BioEntityClasses;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.Enzyme;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.EnzymeFields;
import org.neo4j.graphdb.NotFoundException;

/**
 *
 * @author jtanisha-ee
 */
public class EnzymeImport {
    
     protected static Logger log = LogManager.getLogger(EnzymeImport.class);
     
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
                //String ecNum = (String)result.get(EnzymeFields.EC_NUM.toString());
                String ecNum = getEnzymeId(result);
                if (!StatusUtil.idExists(BioTypes.ENZYME.toString(), BioFields.ENZYME_ID.toString(), ecNum)) {
                    log.info("******* ecNum =" + ecNum);
                    try {
                        processEnzyme(ecNum, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        } finally {
            dbCursor.close();
        }   
    }
   
    /**
     * processOntologyDoc
     * @param str
     * @param dbObj 
     */
    public static void processOntologyDoc(String id, BasicDBObject doc) throws UnknownHostException, RuntimeException, Exception {
         if (!StatusUtil.idExists(BioTypes.ENZYME.toString(), BioFields.ENZYME_ID.toString(), id)) {
              processEnzyme(id, doc);
         }
    }
    
    public static String getEnzymeId(BasicDBObject obj) {
          return (String)obj.get(EnzymeFields.EC_NUM.toString());
    }
    
    public static boolean isObj(BasicDBObject obj) {
           if (obj.get(EnzymeFields.TEXT.toString()) != null) {
              // log.info(field.toString() + " value exists");
              return true;
           }
        return false;
    }
    
    /**
     * 
     * @param obj
     * @return 
     */
    public static String getValue(BasicDBObject obj) {
        return obj.getString(EnzymeFields.TEXT.toString());
    }
    
    
    public static Enzyme getEnzyme(String ecNum, BasicDBList row, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        Enzyme enzyme = (Enzyme)subGraph.search(BioTypes.ENZYME, BioFields.ENZYME_ID, ecNum);       
        if (enzyme != null) {
            return enzyme;
        } else {
            enzyme = new Enzyme();
            enzyme.setEnzymeId(ecNum);
        } 
        
        if (row != null) { 
            for (Object obj : row) {
                BasicDBObject dbObj = (BasicDBObject)obj;
                //log.info("dbObj = " + dbObj.getString("@name"));
                //log.info("dbObj = " + dbObj.getString("#text"));

                String fieldName = dbObj.getString(EnzymeFields.NAME.toString());
                if (fieldName != null && isObj(dbObj)) { 
                    EnzymeFields enumField = EnzymeFields.fromString(fieldName);
                    if (enumField != null) { 
                        switch(enumField) {
                            case ACCEPTED_NAME:
                                log.info("accepted_name =" + dbObj.getString("#text"));
                                enzyme.setEnzymeAcceptedNames(getValue(dbObj));
                                break;
                            case COMMENTS:
                                log.info("comments =" + dbObj.getString("#text"));
                                enzyme.setEnzymeComments(getValue(dbObj)); 
                                break;
                            case LAST_CHANGE:
                                log.info("lastChange = " + getValue(dbObj));
                                enzyme.setEnzymeLastChange(getValue(dbObj));
                                break;
                            case GLOSSARY:
                                enzyme.setEnzymeGlossary(getValue(dbObj));
                                break;
                            case LINKS:
                                enzyme.setEnzymeRefLinks(getValue(dbObj));
                                break;
                            case OTHER_NAMES:
                                enzyme.setEnzymeOtherNames(getValue(dbObj));
                                break;
                            case REACTION:
                                enzyme.setEnzymeReaction(getValue(dbObj));
                                break;
                            case SYS_NAME:
                                enzyme.setEnzymeSystematicNames(getValue(dbObj));
                                break;
                        }
                    }
                }
            }
        }
        if (enzyme != null) {
            subGraph.add(enzyme);
        }
        return enzyme;
    }
    
    /**
     * fetches enzyme document from enzyme collection;
     * @param ecNum
     * @return enzyme document (row)
     */
    public static Enzyme fetchEnzymeFromObject(String ecNum, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put(EnzymeFields.EC_NUM.toString(), ecNum);
        DBCursor dbCursor = getCollection(ImportCollectionNames.ENZYME).findDBCursor(basicDBObject);
        Enzyme enzyme = null;
        try {
            if (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String enzymeId = getEnzymeId(result);
                enzyme = getEnzyme(ecNum, getRow(result) , subGraph);
            } 
        } finally {
            dbCursor.close();
        }
        return enzyme;
    }
    
    public static BasicDBList getRow(BasicDBObject result) { 
         return OntologyStrUtil.getBasicDBList(result, EnzymeFields.ROW);
    }
    
    /**
     * 
     * @param ecNum
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static void processEnzyme(String ecNum, BasicDBObject result) throws UnknownHostException, RuntimeException, Exception {
        BasicDBList row = getRow(result);      
        Subgraph subGraph = new Subgraph();
        Enzyme enzyme = getEnzyme(ecNum, row, subGraph);
      /*  
        for (Object obj : row) {
             BasicDBObject dbObj = (BasicDBObject)obj;
             log.info("dbObj =" + dbObj.toString());
             if (dbObj.containsField(EnzymeFields.ACCEPTED_NAME.toString())) {
                 log.info("acceptedName");
             }
             log.info("dbObj key = " + dbObj.getString(EnzymeFields.NAME.toString()));
             log.info("value =" + dbObj.getString(EnzymeFields.TEXT.toString()));
         } */
         RedbasinTemplate.saveSubgraph(subGraph);
    }
    
    
    /**
     * Given enzymeid or ec_num, return {@link ENZYME} {@link BioEntityClasses#ENZYME}
     * Fetch information from enzyme collection and create enzyme, if it does not exist
     * @param ecNum
     * @param subGraph
     * @return 
     */
   public static Enzyme getEnzyme(String ecNum, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        return fetchEnzymeFromObject(ecNum, subGraph);
    }
    
}