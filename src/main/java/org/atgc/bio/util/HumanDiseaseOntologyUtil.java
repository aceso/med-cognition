package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.HumanDOFields;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.HumanDiseaseOntology;
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
import org.atgc.bio.domain.BioRelTypes;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of HumanDO file from HumanDO.obo,   obofoundry.org
 * Reference to terms used:
 * http://www.geneontology.org/GO.format.obo-1_2.shtml
 * Uses 
 * @author jtanisha-ee
 */
public class HumanDiseaseOntologyUtil {
    
    protected static Logger log = LogManager.getLogger(HumanDiseaseOntologyUtil.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.HUMAN_DISEASE_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, HumanDOFields.ID);
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
          if (OntologyStrUtil.isHumanDiseaseOntology(id)) { 
              if (!StatusUtil.idExists(BioTypes.HUMAN_DISEASE_ONTOLOGY.toString(), BioFields.HUMAN_DISEASE_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* Human Disease ontology id =" + id);
                    try {
                        processHumanDO(id, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } 
              } 
          } 
    }
    
    
    /**
     * getHumanDO
     * @param id OntologyIdentifier
     * @param subGraph
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static HumanDiseaseOntology getHumanDO(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getHumanDO(),  id =" + id);
            HumanDiseaseOntology onto = (HumanDiseaseOntology)subGraph.search(BioTypes.HUMAN_DISEASE_ONTOLOGY, BioFields.HUMAN_DISEASE_ONTOLOGY_ID, id);
            if (onto == null) {
                onto = new HumanDiseaseOntology();
                onto.setHumanDiseaseOntologyId(id);
                subGraph.add(onto);
            }
            return onto;
        }
        return null;
    }
   
    /**
     * getName
     * "name" : "juvenile myoclonic epilepsy",
     * @param obj
     * @return String
     */
    public static String getName(BasicDBObject obj) {
         return OntologyStrUtil.getString(obj, HumanDOFields.NAME);   
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
        return OntologyStrUtil.getString(obj, HumanDOFields.DEF);
    }
    
    /**
     * comment: OMIM mapping confirmed by DO. [SN].
     * @param dbObj
     * @return 
     */
    public static String getComment(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, HumanDOFields.COMMENT);
    }
    
    /**
     * is_obsolete: true
     * @param dbObj
     * @return 
     */
    public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, HumanDOFields.IS_OBSOLETE);
    }
   
    
    /**
     * String:  "\"Janz syndrome\" NARROW []"
     * @param str  
     * @param pattern is NARROW or EXACT, RELATED
     * @return 
     */
    private static String getCleanSyn(String str, String pattern) {
        return str.split(pattern)[0].replaceAll("'\'", "").trim();
    }
   
   
     /**
     * <pre>
     * "synonymList" : [
		{
			"synonym" : "\"Janz syndrome\" NARROW []"
		},
		{
			"synonym" : "\"juvenile myoclonic epilepsy\" EXACT [CSP2005:0485-7984]"
		},
		{
			"synonym" : "\"Juvenile myoclonic epilepsy (disorder)\" EXACT [SNOMEDCT_2005_07_31:6204001]"
		}
	],

     * </pre>
     * @param obj
     * @return 
     */
    public static String getSynonym(BasicDBList list, HumanDOFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDOFields.SYNONYM);
            if (str != null && str.contains(enumField.toString())) {
                switch(enumField) {
                    case EXACT:
                        synList.add(getCleanSyn(str, " EXACT"));
                        break;
                    case RELATED:
                        synList.add(getCleanSyn(str, " RELATED"));
                        break;
                    case NARROW:
                        synList.add(getCleanSyn(str, " NARROW"));
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
     * setSynonyms
     * @param HumanDO
     * @param obj 
     */
    public static void setSynonyms(HumanDiseaseOntology HDO, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDOFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, HumanDOFields.EXACT)) != null) {
            HDO.setHumanDiseaseExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, HumanDOFields.RELATED)) != null) {
           HDO.setHumanDiseaseRelatedSynonyms(synStr);
        }
        if ((synStr = getSynonym(list, HumanDOFields.NARROW)) != null) {
           HDO.setHumanDiseaseNarrowSynonyms(synStr);
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
     * <pre>
     * "isList" : [
		{
			"is_a" : "DOID:0050705 ! adolescence-adult electroclinical syndrome"
		}
	]
}
     * </pre>
     *
     * setIsARelationship
     * @param HDO
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
    public static void setIsListRelationship(HumanDiseaseOntology HDO, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, HumanDOFields.IS_A_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDOFields.IS_A_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDOFields.IS_A);
                if (OntologyStrUtil.isHumanDiseaseOntology(str)) {
                   String id = getId(str, OntologyStrUtil.HDOPattern); 
                   if (id != null) {
                       HumanDiseaseOntology entity = getHumanDO(id, subGraph);
                       if (entity != null) {
                           HDO.setIsARelationship(entity, BioRelTypes.IS_A);
                       }   
                   }
                } 
            }
         }
    }
 
    /**
     * <pre>
     * 	"subset" : [
		{
			"subset" : "gram-positive_bacterial_infectious_disease"
		}
	],
        
        "subset" : [
		{
			"subset" : "gram-negative_bacterial_infectious_disease"
		},
		{
			"subset" : "zoonotic_infectious_disease"
		}
	],
       </pre>
     * @param dbObj
     * @return 
     */
     public static String getSubsets(BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDOFields.SUBSET);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, HumanDOFields.SUBSET));
            str.append(" ");       
        }
        return str.toString();
    }
     
    /*
     * <pre>
     * "altList" : [
		{
			"alt_id" : "DOID:14091"
		}
	],

     * </pre>
     */
    public static void setAltIdRelationships(HumanDiseaseOntology HDO, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
         if (OntologyStrUtil.listExists(dbObj, HumanDOFields.ALT_ID_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDOFields.ALT_ID_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDOFields.ALT_ID);
                if (OntologyStrUtil.isHumanDiseaseOntology(str)) {
                   String id = getId(str, OntologyStrUtil.HDOPattern); 
                   if (id != null) {
                       HumanDiseaseOntology entity = getHumanDO(id, subGraph);
                       if (entity != null) {
                           HDO.setAltIdRelationship(entity, BioRelTypes.IS_AN_ALTERNATE_ID);
                       }   
                   }
                } 
            }
         }
    }
   
     
    /**
     * processHumanDO
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
     public static void processHumanDO(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         HumanDiseaseOntology HDO = getHumanDO(ontologyId, subGraph);
         if (OntologyStrUtil.objectExists(obj, HumanDOFields.NAME)) { 
            HDO.setHumanDiseaseOntologyName(getName(obj));
         }
        
         if (getDef(obj) != null) {
            HDO.setDef(getDef(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, HumanDOFields.COMMENT)) {
             HDO.setComment(getComment(obj));
         }
      
         if (OntologyStrUtil.objectExists(obj, HumanDOFields.ALT_ID_LIST)) {
             setAltIdRelationships(HDO, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, HumanDOFields.SYNONYM_LIST)) {
             setSynonyms(HDO, obj);
         }
         
         if (OntologyStrUtil.objectExists(obj, HumanDOFields.IS_A_LIST)) {
             setIsListRelationship(HDO, obj, subGraph);
         }        
         
         if (OntologyStrUtil.objectExists(obj, HumanDOFields.IS_OBSOLETE)) {
             HDO.setHumanDiseaseIsObsolete(getIsObsolete(obj));
         }
         
         /**
          * BasicDBList
          */
         if (OntologyStrUtil.objectExists(obj, HumanDOFields.SUBSET)) {
             HDO.setHumanDiseaseSubsets(getSubsets(obj));
         }        
        
         RedbasinTemplate.saveSubgraph(subGraph);
     }
    
}
        

