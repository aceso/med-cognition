package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.HomologyOntology;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.HomologyOntologyFields;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of Homology Ontology file from Homology_ontology.obo,   obofoundry.org
 * Reference to terms used:
 * http://www.geneontology.org/GO.format.obo-1_2.shtml
 * Uses 
 * @author jtanisha-ee
 */
public class HomologyOntologyUtil {
    
    protected static Logger log = LogManager.getLogger(HomologyOntologyUtil.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.HOMOLOGY_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, HomologyOntologyFields.ID);
                processOntologyDoc(ontologyId, result);
                log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
                log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
            }
         } finally {
            dbCursor.close();
         }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
    }
    
    /**
     * processOntologyDoc
     * @param id
     * @param result 
     */
    public static void processOntologyDoc(String id, BasicDBObject result) throws UnknownHostException {
        log.info("enter processOntologyDoc");
        if (OntologyStrUtil.isHOM(id)) {
            log.info("isHOM");
              if (!StatusUtil.idExists(BioTypes.HOMOLOGY_ONTOLOGY.toString(), BioFields.HOMOLOGY_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* Homology ontology id =" + id);
                    try {
                        processHomologyOntology(id, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } 
              } 
          } 
    }
    
    
    /**
     * getHomologyOntology
     * @param id OntologyIdentifier
     * @param subGraph
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static HomologyOntology getHomologyOntology(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getHomologyOntology,  id =" + id);
            HomologyOntology onto = (HomologyOntology)subGraph.search(BioTypes.HOMOLOGY_ONTOLOGY, BioFields.HOMOLOGY_ONTOLOGY_ID, id);
            if (onto == null) {
                onto = new HomologyOntology();
                onto.setHomologyOntologyId(id);
                subGraph.add(onto);
            }
            return onto;
        }
        return null;
    }
   
    /**
     * 
     * @param obj
     * @return String
     */
    public static String getName(BasicDBObject obj) {
         return OntologyStrUtil.getString(obj, HomologyOntologyFields.NAME);   
    }
    
    /**
     * <pre> 
     * def: "An autosomal dominant disease that is caused by mutations in the APC gene and 
     * involves formation of numerous polyps in the epithelium of the large intestine 
     * which are initially benign and later transform into colon cancer." 
     * [DO:ls, url:http\://en.wikipedia.org/wiki/Familial_adenomatous_polyposis, 
     * url:http\://www.omim.org/entry/175100?search=adenomatous%20polyposis]
     * </pre>
     * @param obj
     * @return 
     */
    public static String getDef(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, HomologyOntologyFields.DEF);
    }
    
    /**
     * 
     * @param dbObj
     * @return 
     */
    public static String getComment(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, HomologyOntologyFields.COMMENT);
    }
    
    /**
     * is_obsolete: true
     * @param dbObj
     * @return 
     */
    public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, HomologyOntologyFields.IS_OBSOLETE);
    }
   
    
    /**
     * 
     * @param str  
     * @param pattern EXACT, RELATED 
     * @return 
     */
    private static String getCleanSyn(String str, String pattern) {
        return str.split(pattern)[0].replaceAll("'\'", "").trim();
    }
   

    /**
     * Does not have NARROW
     * <pre>
     * "synonymList" : [
     {
     "synonym" : "\"correspondence\" RELATED [DOI:10.1007/BF02814479 \"Richter S (2005) Homologies in phylogenetic analyses?concept and tests. Theory in Biosciences 124, 2: 105-120\"]"
     },
     {
     "synonym" : "\"resemblance\" RELATED []"
     },
     {
     "synonym" : "\"sameness\" EXACT []"
     }
     * </pre>
     * @param list
     * @param enumField
     * @return
     */
    public static String getSynonym(BasicDBList list, HomologyOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, HomologyOntologyFields.SYNONYM);
            if (str != null && str.contains(enumField.toString())) {
                switch(enumField) {
                    case EXACT:
                        synList.add(getCleanSyn(str, " EXACT"));
                        break;
                    case RELATED:
                        synList.add(getCleanSyn(str, " RELATED"));
                        break;
                }                  
            } else {
               continue;
            } 
        } 
        if (synList.isEmpty()) {
            return null;
        } else {
            return synList.toString();
        }
    }

    /**
     *
     * @param onto
     * @param dbObj
     */
    public static void setSynonyms(HomologyOntology onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HomologyOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, HomologyOntologyFields.EXACT)) != null) {
            onto.setHomologyOntologyExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, HomologyOntologyFields.RELATED)) != null) {
            onto.setHomologyOntologyRelatedSynonyms(synStr);
        }
        
    }
   
    /**
     * 
     * getId - get identifier
     * @param str inputs                    
     * @param pattern
     * @return 
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
    
    /**
     *
     *
     * setIsARelationship
     * @param onto
     * @param dbObj
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
    public static void setIsListRelationship(HomologyOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, HomologyOntologyFields.IS_A_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HomologyOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, HomologyOntologyFields.IS_A);
                if (OntologyStrUtil.isHumanDiseaseOntology(str)) {
                   String id = getId(str, OntologyStrUtil.HOMPattern); 
                   if (id != null) {
                       HomologyOntology entity = getHomologyOntology(id, subGraph);
                       if (entity != null) {
                           onto.setIsARelationship(entity, BioRelTypes.IS_A);
                       }   
                   }
                } 
            }
         }
    }

     
   /**
    * "disjoint_from" : "HOM:0000029 ! paedomorphorsis"
    * @param onto
    * @param dbObj
    * @param subGraph
    * @throws NotFoundException
    * @throws NoSuchFieldException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException 
    */
    public static void setDisjointRelationship(HomologyOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        log.info("dbObj = " + dbObj.toString());
        if (OntologyStrUtil.isString(dbObj, HomologyOntologyFields.DISJOINT_FROM)) {
            String str = OntologyStrUtil.getString((BasicDBObject)dbObj, HomologyOntologyFields.DISJOINT_FROM);
            if (OntologyStrUtil.isHOM(str)) {
                String id = getId(str, OntologyStrUtil.HOMPattern); 
                if (id != null) {
                    HomologyOntology entity = getHomologyOntology(id, subGraph);
                    if (entity != null) {
                        onto.setDisjointRelationship(entity, BioRelTypes.DISJOINT_FROM);
                    }   
                }
            } 
         }
           
    }
   
     
    /**
     * processHomologyOntology
     * @param ontologyId
     * @param obj
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
     public static void processHomologyOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         log.info("Enter process");
         Subgraph subGraph = new Subgraph();
         HomologyOntology HO = getHomologyOntology(ontologyId, subGraph);
         if (OntologyStrUtil.objectExists(obj, HomologyOntologyFields.NAME)) { 
            HO.setHomologyOntologyName(getName(obj));
         }
        
         if (getDef(obj) != null) {
            HO.setDef(getDef(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, HomologyOntologyFields.COMMENT)) {
             HO.setComment(getComment(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, HomologyOntologyFields.SYNONYM_LIST)) {
             setSynonyms(HO, obj);
         }
         
         if (OntologyStrUtil.objectExists(obj, HomologyOntologyFields.IS_A_LIST)) {
             setIsListRelationship(HO, obj, subGraph);
         }        
         
         if (OntologyStrUtil.objectExists(obj, HomologyOntologyFields.IS_OBSOLETE)) {
             HO.setHomologyOntologyIsObsolete(getIsObsolete(obj));
         }
         
         /**
          * BasicDBList
          */
         if (OntologyStrUtil.objectExists(obj, HomologyOntologyFields.DISJOINT_FROM)) {
             setDisjointRelationship(HO, obj, subGraph);
         }        
        
         PersistenceTemplate.saveSubgraph(subGraph);
     }
    
}
        

