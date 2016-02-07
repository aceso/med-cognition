package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.SymptomOntology;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.SymptomOntologyFields;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of SymptomOntologyUtil imports file - gemina_symptom.obo
 * Mongo collection - symptomontology 
 * Uses 
 * @author jtanisha-ee
 */
public class SymptomOntologyUtil {
    
    protected static Logger log = LogManager.getLogger(SymptomOntologyUtil.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.SYMPTOM_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, SymptomOntologyFields.ID);
                processOntologyDoc(ontologyId, result);
            }
         } finally {
            dbCursor.close();
         }   
    }
    
    /**
     * processOntologyDoc
     * @param id
     * @param result 
     */
    public static void processOntologyDoc(String id, BasicDBObject result) throws UnknownHostException {
          if (OntologyStrUtil.isSymptom(id)) { 
              if (!StatusUtil.idExists(BioTypes.SYMPTOM_ONTOLOGY.toString(), BioFields.SYMPTOM_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* symptom ontology id =" + id);
                    try {
                        processSymptomOntology(id, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } 
              } 
          } 
          
    }
    
    /**
     * getSystemBiologyOntology
     * @param id
     * @param subGraph
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static SymptomOntology getSymptomOntology(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getSymptomOntology(),  id =" + id);
            SymptomOntology onto = (SymptomOntology)subGraph.search(BioTypes.SYMPTOM_ONTOLOGY, BioFields.SYMPTOM_ONTOLOGY_ID, id);
            if (onto == null) {
                onto = new SymptomOntology();
                onto.setSymptomOntologyId(id);
                subGraph.add(onto);
            }
            return onto;
        }
        return null;
    }
   
    /**
     * name, def, comment, is_a, synonym, (comment hasPMID:), is_obsolete, 
     * getName
     * name" : "immature protein part"
     * @param obj
     * @return String
     */
    public static String getName(BasicDBObject obj) {
         return OntologyStrUtil.getString(obj, SymptomOntologyFields.NAME);   
    }
    
    public static String getDef(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, SymptomOntologyFields.DEF);
    }
    
    public static String getComment(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SymptomOntologyFields.COMMENT);
    }
    
    public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SymptomOntologyFields.IS_OBSOLETE);
    }
    
    /**
     * 
     * @param str
     * @param pattern
     * @return 
     */
    private static String getCleanSyn(String str, String pattern) {
        return str.split(pattern)[0].replaceAll("'\'", "").trim();
    }
   
    /**
     * <pre>
     * "synonymList" : [
		{
			"synonym" : "\"bloody vomit\" EXACT []"
		},
		{
			"synonym" : "\"haematemesis\" EXACT []"
		},
		{
			"synonym" : "\"vomiting blood\" EXACT []"
		}
	],
       </pre>
     * 
     * @param obj
     * @return 
     */
    public static String getSynonym(BasicDBList list, SymptomOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, SymptomOntologyFields.SYNONYM);
            if (str != null && str.contains(enumField.toString())) {
                switch(enumField) {
                    case EXACT:
                        synList.add(OntologyStrUtil.getCleanSyn(str, " EXACT"));
                        break;
                    case RELATED:
                        synList.add(OntologyStrUtil.getCleanSyn(str, " RELATED"));
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
     * setSynonyms
     * @param onto
     * @param dbObj 
     */
    public static void setSynonyms(SymptomOntology onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, SymptomOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, SymptomOntologyFields.EXACT)) != null) {
            onto.setSymptomOntologyExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, SymptomOntologyFields.RELATED)) != null) {
           onto.setSymptomOntologyRelatedSynonyms(synStr);
        }
    }
    
     /**
     * 
     * @param str
     * @return
     */
    public static String getIdFromIsList(String str) {
        return OntologyStrUtil.getId(str, OntologyStrUtil.symptomPattern);
    }
 
    /**
     * getId - get identifier
     * 	"id" : "SYMP:0019147",
     * @param str inputs are :
     *                
     * @param pattern
     * @return SBO:0000183
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
   
   
    /**
     * <pre>
     * "isList" : [
		{
			"is_a" : "SYMP:0019145 ! vomiting"
                        "is_a" : "CHEBI:25805"
		}
	]
     *
     *  </pre>   
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
    public static void setIsARelationship(SymptomOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, SymptomOntologyFields.IS_A_LIST)) {
             log.info("setIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, SymptomOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, SymptomOntologyFields.IS_A);
                if (OntologyStrUtil.isSymptom(str)) {
                   String id = getId(str, OntologyStrUtil.symptomPattern);
                   if (id != null) { 
                       SymptomOntology entity = getSymptomOntology(id, subGraph); 
                       if (entity != null) { 
                           onto.setIsARelationship(entity, BioRelTypes.IS_A);
                       }
                   }
                } 
            }
         }
    }
    
     
    /**
     * processSymptomOntology
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
     public static void processSymptomOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         SymptomOntology onto = getSymptomOntology(ontologyId, subGraph);
         if (onto != null) {
            if (OntologyStrUtil.objectExists(obj, SymptomOntologyFields.NAME)) { 
               onto.setSymptomOntologyName(getName(obj));
            }

            if (getDef(obj) != null) {
               onto.setSymptomOntologyDef(getDef(obj));
            }
         
            if (OntologyStrUtil.objectExists(obj, SymptomOntologyFields.COMMENT)) {
                onto.setComment(getComment(obj));
            }
      
            if (OntologyStrUtil.objectExists(obj, SymptomOntologyFields.SYNONYM_LIST)) {
                setSynonyms(onto, obj);
            }
         
            if (OntologyStrUtil.objectExists(obj, SymptomOntologyFields.IS_A_LIST)) {
                setIsARelationship(onto, obj, subGraph);
            }
         
            if (OntologyStrUtil.objectExists(obj, SymptomOntologyFields.IS_OBSOLETE)) {
               onto.setIsObsolete(getIsObsolete(obj));
            }
            
            if (OntologyStrUtil.objectExists(obj, SymptomOntologyFields.CREATED_BY)) {
                onto.setCreatedBy(OntologyStrUtil.getString(obj, SymptomOntologyFields.CREATED_BY));
            }
            
            if (OntologyStrUtil.objectExists(obj, SymptomOntologyFields.CREATION_DATE)) {
                onto.setCreationDate(OntologyStrUtil.getString(obj, SymptomOntologyFields.CREATION_DATE));
            }
            RedbasinTemplate.saveSubgraph(subGraph);
         }
     }
    
}
