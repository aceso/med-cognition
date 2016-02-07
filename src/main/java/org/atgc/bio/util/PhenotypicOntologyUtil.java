package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.PhenotypicOntology;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.CellTypeOntologyFields;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.neo4j.graphdb.NotFoundException;

/**
 * 
 * Phenotypic ontology (PATO) - from celltypeontology cl.obo
 * http://obofoundry.org/wiki/index.php/PATO:Main_Page
 * 
 * Uses CellTypeOntologyFields as PATO and CellType are from the same file
 * 
 * @author jtanisha-ee
 */
public class PhenotypicOntologyUtil {
    
    protected static Logger log = LogManager.getLogger(PhenotypicOntology.class);
   
    /*
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
   
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {        
        DBCursor dbCursor = getCollection(ImportCollectionNames.CELL_TYPE_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                // CellTypeOntologyFields.ID =  "ID:"
                String id = (String)result.get(CellTypeOntologyFields.ID.toString());
                processOntologyDoc(id, result);
            }
        } finally {
            dbCursor.close();
        }
    }
    * 
    */
    
    /**
     * 
     * @param id
     * @param result
     * @throws UnknownHostException 
     */
    public static void processOntologyDoc(String id, BasicDBObject result) throws UnknownHostException {
        if (OntologyStrUtil.isPATO(id)) {
            log.info("phenotypicOntology =" + id);
            if (!StatusUtil.idExists(BioTypes.PHENOTYPIC_ONTOLOGY.toString(), BioFields.PHENOTYPIC_ONTOLOGY_ID.toString(), id)) {
                log.info("******* phenotypicOntologyId =" + id);
                try {
                    processPhenotypicOntology(id, result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }     
            }
         } 
    }
    
      
    /**
     * 	"synonymList" : [
		{
			"synonym" : "\"relational physical quality\" EXACT []"
		}
	],
     * 
     * @param list   
     * @param enumField
     * @return 
     */
    public static String getSynonym(BasicDBList list, CellTypeOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.SYNONYM);
            if (str != null && str.contains(enumField.toString())) {
                switch(enumField) {
                    case EXACT:
                        synList.add(OntologyStrUtil.getCleanSyn(str, " EXACT"));
                        break;
                    case RELATED:
                        synList.add(OntologyStrUtil.getCleanSyn(str, " RELATED"));
                        break;
                    case NARROW:
                        synList.add(OntologyStrUtil.getCleanSyn(str," NARROW"));
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
     * 	"isList" : [
		{
			"is_a" : "PATO:0001241 ! physical object quality"
		}
	],
     * setIsARelationship between PhenotypicOntology <->PhenotypicOntology
     * @param PhenotypicOntology
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setIsARelationship(PhenotypicOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.IS_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.IS_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.IS_A);
                if (OntologyStrUtil.isPATO(str)) {
                   setPATOIsARelationship(str, onto, subGraph);
                } 
            }
         }
    }
   
    /**
     * 
     * 	"intersectionList" : [
		{
			"intersection_of" : "PATO:0000050 ! life span"
		},
		{
			"intersection_of" : "decreased_in_magnitude_relative_to PATO:0000461 ! normal"
		}
	],
     *
     * Relationship type is not mentioned in some intersections. so use the default
     * BioRelTypes.INTERSECTION_OF
     * relationship
     * setCellTypeIntersectionRelationship
     * @param str
     * @param onto
     * @param subGraph
     * @param relType 
     */
    public static void setPATOIntersectionRelationship(String str, PhenotypicOntology onto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, NotFoundException, IllegalAccessException, InvocationTargetException {
        String id = getId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            PhenotypicOntology entity = getPhenotypicOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = getBioRelationType(str, OntologyStrUtil.patoIdPattern);
                log.info("relType = " + relType);
                if (relType == null) {
                    relType = BioRelTypes.INTERSECTION_OF;
                }
                onto.setIntersectionRelationship(entity, relType);
            }
        }
    }
    
    /**
     *
     * 
     * @param str
     * @param onto
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setPATORelationship(String str, PhenotypicOntology onto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        String id = getId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            PhenotypicOntology entity = getPhenotypicOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = getBioRelationType(str, OntologyStrUtil.patoIdPattern);
                if (relType == null) {
                    relType = BioRelTypes.IS_RELATIONSHIP;
                }
                onto.setRelationship(entity, relType);
            }
        }
    }
    
    /**
     * is_a: CL:0000197 ! receptor cell
     * setCellTypeIsARelationship
     * @param str
     * @param onto
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setPATOIsARelationship(String str, PhenotypicOntology onto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        String id = getId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            PhenotypicOntology entity = getPhenotypicOntology(id, subGraph);
            if (entity != null) {
                onto.setIsARelationship(entity, BioRelTypes.IS_A);
            }
        }
    }
   
    
    
    /*
     * "consider" : "CL:0000000"
     * @param str
     * @return 
     */
    public static String getIdFromConsiderList(String str) {
        return str.trim();
    }
    
    /**
     * 
     * intersection_of: decreased_in_magnitude_relative_to PATO:0000461 ! normal
     * intersection_of: PATO:0001241 ! physical object quality
     * intersection_of: increased_in_magnitude_relative_to PATO:0000461 ! normal
     * 
     * @param PhenotypicOntology
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setIntersectionRelationship(PhenotypicOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.INTERSECTION_OF_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.INTERSECTION_OF_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.INTERSECTION_OF);
                if (str != null) { 
                    if (OntologyStrUtil.isPATO(str)) {
                        setPATOIntersectionRelationship(str, onto, subGraph); 
                    }
                }
            }
         }
    }
    
    
    /**
    * "id" : "PATO:0000001",
    */
    private static String getId(String str) {
        return OntologyStrUtil.getId(str, OntologyStrUtil.patoIdPattern);
        
    }
    
    /**
     * the relationship can be specified either in the beginning or the end of the string
     * eg: develops_from (beginning) or capable_of (after id) 
     * "relationshipList" : [
		{
			"relationship" : "decreased_in_magnitude_relative_to PATO:0000461 ! normal"
		}
	],
     * 
     *
     * @param str
     * @param pattern PATO:
     * @return 
     */
    private static BioRelTypes getBioRelationType(String str, String pattern) {
        return OntologyStrUtil.getRelationshipType(str, pattern);       
    }
    
    /**
     * 
     * "synonymList" : [
		{
			"synonym" : "\"relational physical quality\" EXACT []"
		}
	],

     * setSynonyms
     * @param onto
     * @param dbObj 
     */
    public static void setSynonyms(PhenotypicOntology onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, CellTypeOntologyFields.EXACT)) != null) {
            onto.setPhenotypicExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, CellTypeOntologyFields.RELATED)) != null) {
           onto.setPhenotypicRelatedSynonyms(synStr);
        }
        if ((synStr = getSynonym(list, CellTypeOntologyFields.NARROW)) != null) {
           onto.setPhenotypicNarrowSynonyms(synStr);
        }
    }
    
    public static String getBasicDBListAsString(BasicDBObject dbObj, Enum field, Enum objField) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, field);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            String val = OntologyStrUtil.getString((BasicDBObject)obj, objField);
            str.append(StrUtil.goodText(OntologyStrUtil.getString((BasicDBObject)obj, objField)));
            str.append(" ");   

        }
        return str.toString();  
    }
    
     /**
     * "namespaceList" : [
		{
			"namespace" : "quality"
		}
	],

     * @param dbObj
     * @return String
     */ 
    public static String getNamespace(BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.NAMESPACE_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.NAMESPACE));
            str.append(" ");       
        }
        return str.toString();
    }
    
    public static String getAlternateIds(BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.ALT_ID_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.ALT_ID));
            str.append(" ");       
        }
        return str.toString();
    }
    
    public static String getDef(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, CellTypeOntologyFields.DEF);
    }
   
    /**
     * "comment" : "This term was made obsolete because it refers to a class
     * of gene products and a biological process rather than a molecular function."
     * @param dbObj
     * @return 
     */
    public static String getComment(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, CellTypeOntologyFields.COMMENT);
    }
    
    public static String getIsObsolete(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, CellTypeOntologyFields.IS_OBSOLETE);
    }
    
    /**
     * "subsetList" : [
     *      {
     *          "subset" : "ubprop:upper_level"
     *      }
     *  ],
     * @param dbObj
     * @return 
     */
    public static String getSubsets(BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.SUBSET_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.SUBSET));
            str.append(" ");       
        }
        return str.toString();
    }
    
    /**
     * 
     * 	"altList" : [
		{
			"alt_id" : "PATO:0002079"
		}
	],
     * @param ontologyId
     * @param SubGraph
     * @return 
     */
    public static PhenotypicOntology processOntology(String ontologyId, BasicDBObject obj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, Exception {
         PhenotypicOntology onto = getPhenotypicOntology(ontologyId, subGraph);
        
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.NAME)) { 
            onto.setName(getName(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.ALT_ID_LIST)) { 
            onto.setPhenotypicAlternateIds(getAlternateIds(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.NAMESPACE_LIST)) {
            onto.setPhenotypicNameSpace(getNamespace(obj));
         }

         if (getDef(obj) != null) {
            onto.setDef(getDef(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.COMMENT)) {
             onto.setComment(getComment(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.SYNONYM_LIST)) {
             setSynonyms(onto, obj);
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.SUBSET_LIST)) {
             onto.setPhenotypicSubsets(getSubsets(obj));
         }
         
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.IS_OBSOLETE)) {
             onto.setPhenotypicIsObsolete(getIsObsolete(obj));
         }
         
         setIsARelationship(onto, obj, subGraph);
         setIntersectionRelationship(onto, obj, subGraph);
         processRelationshipList(onto, obj, subGraph);
         return onto;
    }
    
    
    /**
     * relationship: has_cross_section PATO:0002006 ! 2-D shape
     * relationship: has_part PATO:0001857 ! concave
     * relationship: increased_in_magnitude_relative_to PATO:0000461 ! normal
     * relationship: decreased_in_magnitude_relative_to PATO:0000461 ! normal
     * @param str
     * @return {@link BioRelTypes} 
     */
    public static void processRelationshipList(PhenotypicOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, NotFoundException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
        if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.RELATIONSHIP);
                if (OntologyStrUtil.isPATO(str)) {
                    setPATORelationship(str, onto, subGraph);
                } 
            }
         }
    } 
    
    
    /*
     * getName
     * 
     *  isList" : [
     *      {
     *      "is_a" : "CL:0000197 ! receptor cell"
     *      },
     *      {
     *      "is_a" : "CL:0000540 ! neuron"
     *      }
     *  ],
     *  
     * name: immortal cell line cell
     * getName
     * @param dbObj
     * @return String 
     */
    public static String getName(BasicDBObject dbObj) {
          return (String)dbObj.get(CellTypeOntologyFields.NAME.toString());
    }
    
    public static void processPhenotypicOntology(String ontologyId, BasicDBObject result) throws Exception {
          Subgraph subGraph = new Subgraph();
          PhenotypicOntology entity = processOntology(ontologyId, result, subGraph);
          RedbasinTemplate.saveSubgraph(subGraph);
    }
   
    
    /**
     * getPhenotypicOntology
     * @param id
     * @param subGraph
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static PhenotypicOntology getPhenotypicOntology(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException { 
           if (id != null) {
               log.info("getPhenotypicOntology(), id =" + id);
               PhenotypicOntology onto = (PhenotypicOntology)subGraph.search(BioTypes.CELL_TYPE_ONTOLOGY, BioFields.CELL_TYPE_ONTOLOGY_ID, id);
               if (onto == null) {
                   onto = new PhenotypicOntology();
                   onto.setPhenotypicOntologyId(id);
                   subGraph.add(onto);
               } 
               return onto;
           } else {
               return null;
           } 
     }
   
}
        