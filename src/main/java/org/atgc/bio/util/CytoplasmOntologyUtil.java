package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.http.HttpException;
import org.atgc.bio.BioFields;
import org.atgc.bio.CellTypeOntologyFields;
import org.atgc.bio.domain.*;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.NotFoundException;

/**
 * 
 * Cytoplasm ontology (CP) - from celltypeontology cl.obo
 * 
 * 
 * Uses CellTypeOntologyFields as CP and CellType are from the same file
 * 
 * @author jtanisha-ee
 */
@SuppressWarnings("javadoc")
public class CytoplasmOntologyUtil {

    private static final Logger log = LogManager.getLogger(CytoplasmOntologyUtil.class);

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
    } */
    
    /**
     * 
     * @param id
     * @param result
     * @throws UnknownHostException 
     */
    public static void processOntologyDoc(String id, BasicDBObject result) throws UnknownHostException {
        if (OntologyStrUtil.isPATO(id)) {
            log.info("cytoplasmOntology =" + id);
            if (!StatusUtil.idExists(BioTypes.CYTOPLASM_ONTOLOGY.toString(), BioFields.CYTOPLASM_ONTOLOGY_ID.toString(), id)) {
                log.info("******* cytoplasmOntologyId =" + id);
                try {
                    processCytoplasmOntology(id, result);
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
        List<String> synList = new ArrayList<>();
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
     * "is_a" : "GO:0005737 ! cytoplasm"
     * setIsARelationship between CytoplasmOntology <->GO
     * @param onto
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     */
    public static void setIsARelationship(CytoplasmOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, IOException, RuntimeException, InterruptedException, HttpException, URISyntaxException {
         if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.IS_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.IS_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.IS_A);
                if (OntologyStrUtil.isGeneOntology(str)) {
                   setGOIsARelationship(str, onto, subGraph);
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
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void setPATOIntersectionRelationship(String str, CytoplasmOntology onto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, NotFoundException, IllegalAccessException, InvocationTargetException {
        String id = getId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            CytoplasmOntology entity = getCytoplasmOntology(id, subGraph);
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
     * relationship: bearer_of PATO:0001979 ! lobed
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
    public static void setPATORelationship(String str, CytoplasmOntology onto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        String id = getId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            PhenotypicOntology entity = PhenotypicOntologyUtil.getPhenotypicOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = getBioRelationType(str, OntologyStrUtil.patoIdPattern);
                onto.setRelationship(entity, relType);
            }
        }
    }
    
    /**
     * "isList" : [
		{
			"is_a" : "GO:0005634 ! nucleus"
		}
	],
     * setGOIsARelationship
     * @param str
     * @param onto
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setGOIsARelationship(String str, CytoplasmOntology onto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, IOException, InterruptedException, HttpException, URISyntaxException {
        String id = OntologyStrUtil.getId(str, OntologyStrUtil.goIdPattern);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            GeneOntology entity = GeneOntologyObo.getGeneOntology(id, subGraph);
            if (entity != null) {
                onto.setIsARelationship(entity, BioRelTypes.IS_A);
            }
        }
    }
    
     public static void setGOIntersectionRelationship(String str, CytoplasmOntology onto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, IOException, InterruptedException, HttpException, URISyntaxException {
        String id = OntologyStrUtil.getId(str, OntologyStrUtil.goIdPattern);
        if (id != null) {
            GeneOntology entity = GeneOntologyObo.getGeneOntology(id, subGraph);
            if (entity != null) {
                onto.setIsARelationship(entity, BioRelTypes.INTERSECTION_OF);
            }
        }
    }
    
    
    /**
     * <pre>
     * "intersectionList" : [
		{
			"intersection_of" : "GO:0005634 ! nucleus"
		},
		{
			"intersection_of" : "bearer_of PATO:0001871 ! reniform"
		}
	],
     * </pre>
     * 
     * @param onto
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     */
    public static void setIntersectionRelationship(CytoplasmOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, IOException, InterruptedException, HttpException, URISyntaxException {
         if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.INTERSECTION_OF_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.INTERSECTION_OF_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.INTERSECTION_OF);
                if (str != null) { 
                    if (OntologyStrUtil.isPATO(str)) {
                        setPATOIntersectionRelationship(str, onto, subGraph); 
                    } else if (OntologyStrUtil.isGeneOntology(str)) {
                        setGOIntersectionRelationship(str, onto, subGraph);
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
     * Only one synyonym NARROW is defined in CP:0000027
     * <pre>
     * 	"synonymList" : [
		{
			"synonym" : "\"eosinophilic\" NARROW []"
		}
	],
       </pre>
     * setSynonyms
     * @param onto
     * @param dbObj 
     */
    public static void setSynonyms(CytoplasmOntology onto, BasicDBObject dbObj) {
        BasicDBList list = OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, CellTypeOntologyFields.NARROW)) != null) {
           onto.setCytoplasmNarrowSynonyms(synStr);
        }
    }
    
     /**
     * "namespaceList" : [
		{
			"namespace" : "cell"
		}
	],
     *
     * @param dbObj
     * @return String
     */ 
    public static String getNamespace(BasicDBObject dbObj) {
        BasicDBList list = OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.NAMESPACE_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.NAMESPACE));
            str.append(" ");       
        }
        return str.toString();
    }
    
    public static String getDef(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, CellTypeOntologyFields.DEF);
    }
   
    
    /*
     * "subsetList" : [
     *      {
     *          "subset" : "ubprop:upper_level"
     *      }
     *  ],
     * @param dbObj
     * @return 
     */
    /*
    public static String getSubsets(BasicDBObject dbObj) {
        BasicDBList list = OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.SUBSET_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.SUBSET));
            str.append(" ");       
        }
        return str.toString();
    }*/
    
    
    /**
     * @param ontologyId
     * @param obj
     * @param subGraph
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws Exception
     */
    public static CytoplasmOntology processOntology(String ontologyId, BasicDBObject obj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, IOException, InterruptedException, HttpException, URISyntaxException {
         CytoplasmOntology onto = getCytoplasmOntology(ontologyId, subGraph);
        
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.NAME)) { 
            onto.setName(getName(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.NAMESPACE_LIST)) {
            onto.setCytoplasmNameSpace(getNamespace(obj));
         }

         if (getDef(obj) != null) {
            onto.setDef(getDef(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.SYNONYM_LIST)) {
             setSynonyms(onto, obj);
         }
          
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.CREATED_BY)) {
            onto.setCreatedBy(OntologyStrUtil.getString(obj, CellTypeOntologyFields.CREATED_BY));
         }

         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.CREATION_DATE)) {
            onto.setCreationDate(OntologyStrUtil.getString(obj, CellTypeOntologyFields.CREATION_DATE));
         }
         
         setIsARelationship(onto, obj, subGraph);
         setIntersectionRelationship(onto, obj, subGraph);
         processRelationshipList(onto, obj, subGraph);
         return onto;
    }
    
    
    /**
     * relationship: bearer_of PATO:0001979 ! lobed
     * @param onto
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws NotFoundException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception
     */
    public static void processRelationshipList(CytoplasmOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, NotFoundException, IllegalAccessException, InvocationTargetException, UnknownHostException {
        if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.RELATIONSHIP_LIST);
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
     * 	"name" : "reniform nucleus",
     * getName
     * @param dbObj
     * @return String 
     */
    public static String getName(BasicDBObject dbObj) {
          return (String)dbObj.get(CellTypeOntologyFields.NAME.toString());
    }
    
    public static void processCytoplasmOntology(String ontologyId, BasicDBObject result) throws Exception {
          Subgraph subGraph = new Subgraph();
          processOntology(ontologyId, result, subGraph);
          PersistenceTemplate.saveSubgraph(subGraph);
    }
   
    
    /**
     * getCytoplasmOntology
     * @param id
     * @param subGraph
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static CytoplasmOntology getCytoplasmOntology(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException { 
           if (id != null) {
               log.info("getCytoplasmOntology(), id =" + id);
               CytoplasmOntology onto = (CytoplasmOntology)subGraph.search(BioTypes.CELL_TYPE_ONTOLOGY, BioFields.CELL_TYPE_ONTOLOGY_ID, id);
               if (onto == null) {
                   onto = new CytoplasmOntology();
                   onto.setCytoplasmOntologyId(id);
                   subGraph.add(onto);
               } 
               return onto;
           } else {
               return null;
           } 
     }
   
}
        