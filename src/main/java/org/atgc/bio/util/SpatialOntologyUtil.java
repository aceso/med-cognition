package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.SpatialOntologyFields;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.SpatialOntology;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of spatial.obo - spatial ontology imports file.
 * Uses 
 * @author jtanisha-ee
 */
public class SpatialOntologyUtil {
    
    protected static Log log = LogFactory.getLog(SpatialOntologyUtil.class);
    /*
    private static String cellIdPattern = "CL:";
    private static String prIdPattern = "PR:";
    private static String goIdPattern = "GO:";
    private static String enzymePattern = "EC:";
    private static String exclamationPattern = "!";
    private static String spacePattern = " ";
    private static String NCBITaxonPattern = "NCBITaxon:";
    private static String uberonIdPattern = "UBERON:";
    private static String chebiIdPattern = "CHEBI:";
    private static String patoIdPattern = "PATO:";
    private static String cpIdPattern = "CP:";
    private static String obiIdPattern = "OBI:";
    private static String soIdPattern = "SO:";
    private static String modIdPAttern = "MOD:";
    * */
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.SPATIAL_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, SpatialOntologyFields.ID);
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
          if (OntologyStrUtil.isProteinOntology(id)) { 
              if (!StatusUtil.idExists(BioTypes.SPATIAL_ONTOLOGY.toString(), BioFields.SPATIAL_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* spatial ontology id =" + id);
                    try {
                        processSpatialOntology(id, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } 
              } 
          } 
          
    }
    
    /**
     * getProteinOntology
     * @param prId
     * @param subGraph
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static SpatialOntology getSpatialOntology(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getSpatialOntology(),  id =" + id);
            SpatialOntology prOnto = (SpatialOntology)subGraph.search(BioTypes.SPATIAL_ONTOLOGY, BioFields.SPATIAL_ONTOLOGY_ID, id);
            if (prOnto == null) {
                prOnto = new SpatialOntology();
                prOnto.setSpatialOntologyId(id);
                subGraph.add(prOnto);
            }
            return prOnto;
        }
        return null;
    }
   
    /**
     * getName
     * name" : "immature protein part"
     * @param obj
     * @return String
     */
    public static String getName(BasicDBObject obj) {
         return OntologyStrUtil.getString(obj, SpatialOntologyFields.NAME);   
    }
       
    public static String getDef(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, SpatialOntologyFields.DEF);
    }
    
    public static String getComment(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.COMMENT);
    }
    
    public static String getAltId(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.ALT_ID);
    }
    
    public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.IS_OBSOLETE);
    }
    
    public static String getDomain(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.DOMAIN);
    }
    
    public static String getRange(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.RANGE);
    }
    
    public static String getIsTransitive(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.IS_TRANSITIVE);
    }
    
    public static String getIsSymmetric(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.IS_SYMMETRIC);
    }
    
    public static String getInverseOf(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.INVERSE_OF);
    } 
    
    public static String getInverseInstance(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SpatialOntologyFields.INVERSE_OF_ON_INSTANCE_LEVEL);
    }
    
    /**
     * 	"synonym" : "\"A-P axis\" EXACT []"
     *  "synonym" : "\"cross-section\" RELATED []"
     * @param str
     * @param pattern
     * @return 
     */
    private static String getCleanSyn(String str, String pattern) {
        return str.split(pattern)[0].replaceAll("'\'", "").trim();
    }
   
     /**
     * 
     *
     * @param obj
     * @return 
     */
    public static String getSynonym(BasicDBList list, SpatialOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, SpatialOntologyFields.SYNONYM);
            if (str != null && str.contains(enumField.toString())) {
                switch(enumField) {
                    case EXACT:
                        synList.add(getCleanSyn(str, " EXACT"));
                        break;
                    case RELATED:
                        synList.add(getCleanSyn(str, " RELATED"));
                        break;
                    case NARROW:
                        synList.add(getCleanSyn(str," NARROW"));
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
     * <pre>
     * 	"synonymList" : [
		{
			"synonym" : "\"dextro-sinister axis\" EXACT []"
		},
		{
			"synonym" : "\"left to right axis\" EXACT []"
		}
	],

     * </pre>
     * setSynonyms
     * @param onto
     * @param obj 
     */
    public static void setSynonyms(SpatialOntology onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, SpatialOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, SpatialOntologyFields.EXACT)) != null) {
            onto.setSpatialOntologyExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, SpatialOntologyFields.RELATED)) != null) {
           onto.setSpatialOntologyRelatedSynonyms(synStr);
        }
        if ((synStr = getSynonym(list, SpatialOntologyFields.NARROW)) != null) {
           onto.setSpatialOntologyNarrowSynonyms(synStr);
        }
    }
     
    /**
     * "id" : "BSPO:0000017",
     * getId - get identifier
     * 
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
    
    /**
     * "relationship" : "BSPO:0000110 BSPO:0000007 ! right side"
     * setSpatialRelationship
     * @param str
     * @param onto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setSpatialRelationship(String str, SpatialOntology onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.spatialPattern);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            SpatialOntology entity = getSpatialOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = getRelationshipType(str, OntologyStrUtil.spatialPattern);
                if (relType == null) {
                    //relType = BioRelTypes.AN_EQUIVALENT_TERM;
                    log.info("relType is null, " + str);
                }
                onto.setSpatialOntologyRelationship(entity, relType);
            }
        }
     }

    /**
     * 
     * <pre>
     * "is_a" : "BSPO:0000400 ! anatomical section"
     * </pre>
     * setIsARelationship
     * @param prOnto
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
    public static void setIsARelationship(SpatialOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
        String str = OntologyStrUtil.getString((BasicDBObject)dbObj, SpatialOntologyFields.IS_A);
        if (str != null) {
            String id = getId(str, OntologyStrUtil.spatialPattern);
            if (id != null) {
                SpatialOntology entity = getSpatialOntology(id, subGraph);
                if (entity != null) {
                    onto.setIsARelationship(entity, BioRelTypes.IS_A);
                }
            }
        }
    }
    
    
    /**
     * "relationship" : "BSPO:0000110 BSPO:0000007 ! right side"
     * "relationship" : "starts_axis BSPO:0000017 ! left/right axis"
     * getRelationshipType
     * @param str
     * @return {@link BioRelTypes}
     */
    public static BioRelTypes getRelationshipType(String str, String pattern) {
        return OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.spatialPattern);
    }
  
    /**
     * <pre>
     * 	"relationshipList" : [
		{
			"relationship" : "BSPO:0000110 BSPO:0000007 ! right side"
		},
		{
			"relationship" : "starts_axis BSPO:0000017 ! left/right axis"
		}
	]

     * </pre>
     */
     public static void setRelationshipList(SpatialOntology prOnto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception {
         if (OntologyStrUtil.listExists(dbObj, SpatialOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, SpatialOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, SpatialOntologyFields.RELATIONSHIP);
                 if (OntologyStrUtil.isSpatialOntology(str)) {
                    setSpatialRelationship(str, prOnto, subGraph);
                 } 
             }
         }
     }
     
     /**
      * <pre>
      * "intersectionList" : [
		{
			"intersection_of" : "BSPO:0000010 ! anatomical axis"
		},
		{
			"intersection_of" : "orthogonal_to BSPO:0000417 ! sagittal section"
		}
	]
      * </pre>
      * setIntersectionRelationship
      * @param prOnto
      * @param subGraph
      * @param str 
      */
     public static void setIntersectionRelationship(SpatialOntology onto, Subgraph subGraph, String str) throws NotFoundException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
            String prId = OntologyStrUtil.getId(str, OntologyStrUtil.spatialPattern);
            //log.info("id = " + goId + ", name = " + name);
            if (prId != null) {
                SpatialOntology entity = getSpatialOntology(prId, subGraph);
                if (entity != null) {
                    BioRelTypes relType = getRelationshipType(str, OntologyStrUtil.spatialPattern);
                    if (relType == null) {
                        //relType = BioRelTypes.AN_EQUIVALENT_TERM;
                        log.info("relType is null, " + str);
                    }
                    onto.setIntersectionRelationship(entity, relType);
                }
            }
     }
     
     /**
      * <pre>
      * 	"intersectionList" : [
		{
			"intersection_of" : "BSPO:0000010 ! anatomical axis"
		},
		{
			"intersection_of" : "orthogonal_to BSPO:0000417 ! sagittal section"
		}
	]

      * </pre>
      * 
      */
     public static void setIntersectionList(SpatialOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception {
         if (OntologyStrUtil.listExists(dbObj, SpatialOntologyFields.INTERSECTION_OF_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, SpatialOntologyFields.INTERSECTION_OF_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, SpatialOntologyFields.INTERSECTION_OF);
                 if (OntologyStrUtil.isSpatialOntology(str)) {
                    setIntersectionRelationship(onto, subGraph, str);
                 } 
             }
         }
     }
     
     
     /***
      * <pre>
      * domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000054 ! anatomical side
        domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000054 ! anatomical side
        domain: BSPO:0000005 ! anatomical surface
      * </pre>
      * setDomainRelationship
      */
     public static void setDomainRelationship(SpatialOntology onto, BasicDBObject obj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
          String str = getDomain(obj);    
          if (str != null) {
              String id = getId(str, OntologyStrUtil.spatialPattern);
              SpatialOntology entity = getSpatialOntology(id, subGraph);
              if (entity != null) {
                  onto.setDomainRelationship(entity);
              }
          }
     }
     
     /**
      * <pre>
      * range: BSPO:0000010 ! anatomical axis
        range: BSPO:0000010 ! anatomical axis
        range: BSPO:0000010 ! anatomical axis
        range: CARO:0000000 ! anatomical entity
        range: CARO:0000000 ! anatomical entity
        range: BSPO:0000400 ! anatomical section
        range: BSPO:0000010 ! anatomical axis
      * </pre>
      * @param onto
      * @param obj
      * @param subGraph
      * @throws NotFoundException
      * @throws NoSuchFieldException
      * @throws IllegalArgumentException
      * @throws IllegalAccessException
      * @throws InvocationTargetException 
      */
     public static void setRangeRelationship(SpatialOntology onto, BasicDBObject obj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
          String str = getRange(obj);    
          if (str != null) {
              String id = getId(str, OntologyStrUtil.spatialPattern);
              SpatialOntology entity = getSpatialOntology(id, subGraph);
              if (entity != null) {
                  onto.setRangeRelationship(entity);
              }
          }
     }
     
     /**
      * <pre>
      * inverse_of: BSPO:0000099 ! posterior_to
        inverse_of: BSPO:0000100 ! proximal_to
        inverse_of: BSPO:0000102 ! ventral_to
        inverse_of: BSPO:0000109 ! surrounded_by
        inverse_of: BSPO:0000108 ! superficial_to
        inverse_of_on_instance_level: BSPO:0000111 ! right_of
        inverse_of_on_instance_level: BSPO:0000121 ! right_side_of
        inverse_of: part_of ! part_of
        </pre>
      * @param onto
      * @param obj
      * @param subGraph
      * @throws NotFoundException
      * @throws NoSuchFieldException
      * @throws IllegalArgumentException
      * @throws IllegalAccessException
      * @throws InvocationTargetException 
      */
      public static void setInverseRelationship(SpatialOntology onto, BasicDBObject obj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
          String str = getInverseOf(obj);    
          if (str != null) {
              String id = getId(str, OntologyStrUtil.spatialPattern);
              SpatialOntology entity = getSpatialOntology(id, subGraph);
              if (entity != null) {
                  onto.setInverseRelationship(entity, BioRelTypes.INVERSE_OF);
              }
          }
     }
      
      /**
      * <pre>
      * inverse_of: BSPO:0000099 ! posterior_to
        inverse_of: BSPO:0000100 ! proximal_to
        inverse_of: BSPO:0000102 ! ventral_to
        inverse_of: BSPO:0000109 ! surrounded_by
        inverse_of: BSPO:0000108 ! superficial_to
        inverse_of_on_instance_level: BSPO:0000111 ! right_of
        inverse_of_on_instance_level: BSPO:0000121 ! right_side_of
        inverse_of: part_of ! part_of
        </pre>
      * @param onto
      * @param obj
      * @param subGraph
      * @throws NotFoundException
      * @throws NoSuchFieldException
      * @throws IllegalArgumentException
      * @throws IllegalAccessException
      * @throws InvocationTargetException 
      */
      public static void setInverseInstanceRelationship(SpatialOntology onto, BasicDBObject obj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
          String str = getInverseInstance(obj);    
          if (str != null) {
              String id = getId(str, OntologyStrUtil.spatialPattern);
              SpatialOntology entity = getSpatialOntology(id, subGraph);
              if (entity != null) {
                  onto.setInverseInstanceRelationship(entity, BioRelTypes.INVERSE_OF_ON_INSTANCE_LEVEL);
              }
          }
     }
     
     
     
    /**
     * processSpatialOntology
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
     public static void processSpatialOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         SpatialOntology onto = getSpatialOntology(ontologyId, subGraph);
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.NAME)) { 
            onto.setSpatialOntologyName(getName(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.IS_TRANSITIVE)) {
            onto.setSpatialOntologyIsTransitive(getIsTransitive(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.IS_SYMMETRIC)) {
            onto.setSpatialOntologyIsSymmetric(getIsSymmetric(obj));
         }
       
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.DOMAIN)) {
            setDomainRelationship(onto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.RANGE)) {
            setRangeRelationship(onto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.ALT_ID)) {
            onto.setSpatialOntologyAltId(getAltId(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.INVERSE_OF)) {
            setInverseRelationship(onto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.INVERSE_OF_ON_INSTANCE_LEVEL)) {
            setInverseInstanceRelationship(onto, obj, subGraph);
         }
         
         if (getDef(obj) != null) {
            onto.setDef(getDef(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.COMMENT)) {
             onto.setComment(getComment(obj));
         }
      
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.SYNONYM_LIST)) {
             setSynonyms(onto, obj);
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.IS_A)) {
             setIsARelationship(onto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.RELATIONSHIP_LIST)) {
            setRelationshipList(onto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.INTERSECTION_OF_LIST)) {
            setIntersectionList(onto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, SpatialOntologyFields.IS_OBSOLETE)) {
             onto.setSpatialOntologyIsObsolete(getIsObsolete(obj));
         }
         RedbasinTemplate.saveSubgraph(subGraph);
     }
    
}
        

