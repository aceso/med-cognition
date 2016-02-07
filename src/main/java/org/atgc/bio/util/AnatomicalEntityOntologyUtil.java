package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.*;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;

import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.HumanDevAnatOntologyFields;
import org.atgc.bio.ImportCollectionNames;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of anatomical entity ontology from  imports file.
 * human-dev-anat-abstract2.obo
 * AEO:0000078
 * 
 * CARO: http://www.bioontology.org/wiki/images/0/0d/CAROchapter.pdf
 * CARO: Common Anatomy reference ontology 
 * 
 * EHDAA2: Abstract human developmental anatomy - http://www.ontobee.org/browser/index.php?o=EHDAA2
 * CS - Carnegie System
 * 
 * Relationships: http://www.geneontology.org/GO.ontology-ext.relations.shtml
 * These three reltypes are transitive
 * {@link BioRelTypes#HAS_PART} {@link BIORELTYPES#PART_OF} {@link BioRelTypes#DEVELOPS_FROM}
 * Uses 
 * @author jtanisha-ee
 */
public class AnatomicalEntityOntologyUtil {
    
    protected static Logger log = LogManager.getLogger(AnatomicalEntityOntologyUtil.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.HUMANDEVANAT_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, HumanDevAnatOntologyFields.ID);
                processOntologyDoc(ontologyId, result);
            }
         } finally {
            dbCursor.close();
         }   
    }
    
    /**
     * 
     * EHDAA2 - Abstract Human Developmental Anatomy - AHDA
     * CARO - 
     * AnatomicalEntityOntology - AEOntology
     * CS - CarnegieState
     * processOntologyDoc
     * @param id
     * @param result 
     */
    public static void processOntologyDoc(String id, BasicDBObject result) throws UnknownHostException, Exception {
          if (OntologyStrUtil.isAEOntology(id)) { 
              if (!StatusUtil.idExists(BioTypes.ANATOMICAL_ENTITY_ONTOLOGY.toString(), BioFields.ANATOMICAL_ENTITY_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* anatomical entity ontology id =" + id);
                    try {
                        processAEOntology(id, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } 
              } 
          } else if (OntologyStrUtil.isCARO(id)) {
              if (!StatusUtil.idExists(BioTypes.CARO.toString(), BioFields.CARO_ID.toString(), id)) {
                  try {
                      processCARO(id, result);
                  } catch(Exception e) {
                      throw new RuntimeException(e);
                  }
              }
          } else if (OntologyStrUtil.isCellTypeOntology(id)) {
              CellTypeOntologyImport.processCellTypeOntology(id, result);
          } else if (OntologyStrUtil.isEHDAA2(id)) {
              processAHDA(id, result);
          } else if (OntologyStrUtil.isCS(id)) {
              processCS(id, result);
          }
          
    }
    
    /**
     * getAEOntology
     * @param id
     * @param subGraph
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static AnatomicalEntityOntology getAEOntology(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getAEOntology(),  id =" + id);
            AnatomicalEntityOntology onto = (AnatomicalEntityOntology)subGraph.search(BioTypes.ANATOMICAL_ENTITY_ONTOLOGY, BioFields.ANATOMICAL_ENTITY_ONTOLOGY_ID, id);
            if (onto == null) {
                onto = new AnatomicalEntityOntology();
                onto.setAnatomicalEntityOntologyId(id);
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
         return OntologyStrUtil.getString(obj, HumanDevAnatOntologyFields.NAME);   
    }
    
    public static String getDef(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, HumanDevAnatOntologyFields.DEF);
    }
    
    public static String getNameSpace(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, HumanDevAnatOntologyFields.NAMESPACE);
    }
    
    public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, HumanDevAnatOntologyFields.IS_OBSOLETE);
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
     * 
     * "synonym" : "\"FHOD3/iso:3\" EXACT PRO-short-label [PRO:DNx]"
     *  synonym: "GPT" RELATED [PMID:1931970]
     * @param obj
     * @return 
     */
    public static String getSynonym(BasicDBList list, HumanDevAnatOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.SYNONYM);
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
     * synonym: "reaction rate constant" []
     * There are RELATED, EXACT and NARROW in synonym in this.
     * setSynonyms
     * @param cellOnto
     * @param obj 
     */
    public static void setSynonyms(AnatomicalEntityOntology onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, HumanDevAnatOntologyFields.EXACT)) != null) {
            onto.setAnatomicalEntityOntologyExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, HumanDevAnatOntologyFields.RELATED)) != null) {
           onto.setAnatomicalEntityOntologyRelatedSynonyms(synStr);
        }
        if ((synStr = getSynonym(list, HumanDevAnatOntologyFields.NARROW)) != null) {
           onto.setAnatomicalEntityOntologyNarrowSynonyms(synStr);
        }
        
    }
    
     /**
     * 
     * @param str
     * @return
     */
    public static String getIdFromIsList(String str) {
        return OntologyStrUtil.getId(str, OntologyStrUtil.aeoPattern);
    }
 
    /**
     * 
     * @param pattern
     * @return id
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
   
   
    /**
     * <pre>
     *  isList" : [
		{
			"is_a" : "CARO:0000066 ! epithelium"
		}
	],
        "isList" : [
		{
			"is_a" : "AEO:0000192 ! anatomical surface feature"
		},
		{
			"is_a" : "CARO:0000010 ! anatomical surface"
		}
	]    
       </pre>
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
    public static void setIsARelationship(AnatomicalEntityOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, HumanDevAnatOntologyFields.IS_A_LIST)) {
             log.info("setIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                 setAEOIsARelationship((BasicDBObject)obj, onto, subGraph);
             }
         }
    }
    
    public static void setAEOIsARelationship(BasicDBObject obj, AnatomicalEntityOntology onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
         String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.IS_A);
         if (OntologyStrUtil.isAEOntology(str)) {
            AnatomicalEntityOntology entity = getAEOntology(getId(str, OntologyStrUtil.aeoPattern), subGraph); 
            onto.setIsARelationship(onto, BioRelTypes.IS_A);
         }
    }
    
    
    /**
     * <pre>
     * "relationshipList" : [
		{
			"relationship" : "has_part CL:0000058 ! chondroblast"
		},
		{
			"relationship" : "has_part CL:0000062 ! osteoblast"
		},
		{
			"relationship" : "has_part CL:0000138 ! chondrocyte"
		},
		{
			"relationship" : "part_of AEO:0000168 ! skeletal system"
		}
	]
     * </pre>
     */
    public static void setRelationshipList(AnatomicalEntityOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (OntologyStrUtil.listExists(dbObj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.RELATIONSHIP);
                 if (OntologyStrUtil.isAEOntology(str)) {
                    setAERelationship(str, onto, subGraph);
                 } else if (OntologyStrUtil.isCellTypeOntology(str)) {
                     setCellRelationship(str, onto, subGraph);
                 }
                 //CARO
             }
         }
    }
    
    
    public static void setAERelationship(String str, AnatomicalEntityOntology onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.aeoPattern);
        if (id != null) {
            AnatomicalEntityOntology entity = getAEOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.aeoPattern);
                if (relType == null) { 
                    throw new RuntimeException("getAERelationship(), BioRelType is null " + str);
                } else {
                    onto.setAnatomicalEntityOntologyRelationship(entity, relType);
                }
            }
        }
     }
    
    /**
     * <pre>
     * {
     * "relationship" : "has_part CL:0000138 ! chondrocyte"
     * },
      </pre>
     * @param str
     * @param onto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
     public static void setCellRelationship(String str, AnatomicalEntityOntology onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.cellIdPattern);
        if (id != null) {
            CellTypeOntology entity = CellTypeOntologyImport.getCellTypeOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.cellIdPattern);
                if (relType == null) {
                    throw new RuntimeException("setCellRelationship() BioRelTypes is null, " + str);
                }
                onto.setAnatomicalEntityOntologyRelationship(entity, relType);
            }
        }
     }
    
     
    /**
     * processAEOntology
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
     public static void processAEOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         AnatomicalEntityOntology onto = getAEOntology(ontologyId, subGraph);
         if (onto != null) {
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.NAME)) { 
               onto.setAnatomicalEntityOntologyName(getName(obj));
            }
            
            if (getDef(obj) != null) {
               onto.setDef(getDef(obj));
            }
            
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.ALT_ID)) {
                onto.setAnatomicalEntityOntologyAltId(ontologyId);
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.SYNONYM_LIST)) {
                setSynonyms(onto, obj);
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_A_LIST)) {
                setIsARelationship(onto, obj, subGraph);
            } else {
                if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_A)) {
                    setAEOIsARelationship(obj, onto, subGraph);
                }
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_OBSOLETE)) {
               onto.setAnatomicalEntityOntologyIsObsolete(getIsObsolete(obj));
            }
            
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST)) {
                setRelationshipList(onto, obj, subGraph);
            }
            
            PersistenceTemplate.saveSubgraph(subGraph);
         }
     }
    
     /**
      * CARO - common anatomy reference ontology
      * http://www.bioontology.org/wiki/images/0/0d/CAROchapter.pdf
      */
     public static CARO getCARO(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
          if (id != null) {
            log.info("getCARO(),  id =" + id);
            CARO onto = (CARO)subGraph.search(BioTypes.CARO, BioFields.CARO_ID, id);
            if (onto == null) {
                onto = new CARO();
                onto.setCAROId(id);
                subGraph.add(onto);
            }
            return onto;
        }
        return null;
     }
     
     /**
     * synonym: "reaction rate constant" []
     * There are only RELATED, synonym in this.
     * setSynonyms
     * @param cellOnto
     * @param obj 
     */
    public static void setCAROSynonyms(CARO onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, HumanDevAnatOntologyFields.RELATED)) != null) {
            onto.setCARORelatedSynonyms(synStr);
        } 
    }
    
    public static void setCAROIsListRelationship(CARO onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, HumanDevAnatOntologyFields.IS_A_LIST)) {
             log.info("setCAROIsListRelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                 setCAROIsARelationship((BasicDBObject)obj, onto, subGraph);
             }
         }
    }
    
    public static void setCAROIsARelationship(BasicDBObject obj, CARO onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
         String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.IS_A);
         if (OntologyStrUtil.isCARO(str)) {
            CARO entity = getCARO(getId(str, OntologyStrUtil.caroPattern), subGraph); 
            onto.setIsARelationship(onto, BioRelTypes.IS_A);
         }
    }
    
    
    /**
     * {@link BioRelTypes#HAS_PART}
     * @param str
     * @param onto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setCAROCellRelationship(String str, CARO onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.cellIdPattern);
        if (id != null) {
            CellTypeOntology entity = CellTypeOntologyImport.getCellTypeOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.cellIdPattern);
                if (relType == null) {
                    //relType = BioRelTypes.AN_EQUIVALENT_TERM;
                    log.info("relType is null, " + str);
                }
                onto.setCARORelationship(entity, relType);
            }
        }
     }
     
     /**
      * relationship: surrounds CARO:0000043 ! simple tissue
      * 
      * {@link BioRelTypes#PART_OF}  {@link BioRelTypes#SURROUNDS}
      * @param str
      * @param onto
      * @param subGraph
      * @throws NotFoundException
      * @throws NoSuchFieldException
      * @throws IllegalArgumentException
      * @throws IllegalAccessException
      * @throws InvocationTargetException 
      */
     public static void setCARORelationship(String str, CARO onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.caroPattern);
        if (id != null) {
            CARO entity = getCARO(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.caroPattern);
                onto.setCARORelationship(entity, relType);
            }
        }
     }
    
    
    /**
     * relationship: part_of CARO:0000013 ! cell
     * relationship: has_part CL:0000066 !
     * @param onto
     * @param dbObj
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
     public static void setCARORelationshipList(CARO onto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (OntologyStrUtil.listExists(dbObj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.RELATIONSHIP);
                 if (OntologyStrUtil.isCARO(str)) {
                    setCARORelationship(str, onto, subGraph);
                 } else if (OntologyStrUtil.isCellTypeOntology(str)) {
                     setCAROCellRelationship(str, onto, subGraph);
                 }
             }
         }
    }
     
     
     /**
      * 
      * obsolete is not there
      * synonyms EXACT, NARROW are not there
      * 
      * processCARO
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
     public static void processCARO(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         CARO onto = getCARO(ontologyId, subGraph);
         if (onto != null) {
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.NAME)) { 
               onto.setName(OntologyStrUtil.getString(obj, HumanDevAnatOntologyFields.NAME));
            }
            
            if (getDef(obj) != null) {
               onto.setDef(getDef(obj));
            }
            
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.ALT_ID)) {
                onto.setCAROAltId(ontologyId);
            }
         
            /**
             * Related synonyms 
             */
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.SYNONYM_LIST)) {
                setCAROSynonyms(onto, obj);
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_A_LIST)) {
                setCAROIsListRelationship(onto, obj, subGraph);
            } else if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_A)) {
                setCAROIsARelationship(obj, onto,subGraph);
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST)) {
                setCARORelationshipList(onto, obj, subGraph);
            }
            
            PersistenceTemplate.saveSubgraph(subGraph);
         }
     }
     
     
     /**
      * AHDA - Abstract human developmental anatomy (AHDA)
      * 
      */
     public static AHDA getAHDA(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
          if (id != null) {
            log.info("getAHDA(),  id =" + id);
            AHDA onto = (AHDA)subGraph.search(BioTypes.AHDA, BioFields.AHDA_ID, id);
            if (onto == null) {
                onto = new AHDA();
                onto.setAhdaId(id);
                subGraph.add(onto);
            }
            return onto;
        }
        return null;
     }
     
     /**
      * only EXACT Synonym 
      * synonym: "urogenital sinus superior part" EXACT []
      */
      public static void setAHDASynonyms(AHDA onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, HumanDevAnatOntologyFields.EXACT)) != null) {
            onto.setAhdaExactSynonyms(synStr);
        } 
     } 
     
    /**
     * relTypes : develops_in, ends_at, located_in, starts_at
     * relationship" : "located_in EHDAA2:0002142 ! urogenital sinus lumen"
     * @param str
     * @param onto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
     public static void setAHDARelationship(String str, AHDA onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.ehdaa2Pattern);
        if (id != null) {
            AHDA entity = getAHDA(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.ehdaa2Pattern);
                onto.setAHDARelationship(entity, relType);
            }
        }
     }
     
     /**
      * relationship: part_of AEO:0000172 ! limb
      * @param str
      * @param onto
      * @param subGraph
      * @throws NotFoundException
      * @throws NoSuchFieldException
      * @throws IllegalArgumentException
      * @throws IllegalAccessException
      * @throws InvocationTargetException 
      */
     public static void setAHDA_AEORelationship(String str, AHDA onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.aeoPattern);
        if (id != null) {
            AnatomicalEntityOntology entity = getAEOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.aeoPattern);
                onto.setAHDARelationship(entity, relType);
            }
        }
     }
     
     /**
      * "relationship" : "ends_at CS20 ! CS20"
      * @param str
      * @param onto
      * @param subGraph
      * @throws NotFoundException
      * @throws NoSuchFieldException
      * @throws IllegalArgumentException
      * @throws IllegalAccessException
      * @throws InvocationTargetException 
      */
     public static void setAHDA_CSRelationship(String str, AHDA onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.csPattern);
        if (id != null) {
            CS entity = getCS(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.csPattern);
                onto.setAHDARelationship(entity, relType);
            }
        }
     }
     
     /**
      * 
      *  relTypes : develops_in, ends_at, located_in, starts_at
      * {@link BioRelTypes
      * AHDA
      * relationship_list
      * <pre>
      * "relationshipList" : [
      *         relationship: part_of AEO:0000172 ! limb
		{
			"relationship" : "develops_in EHDAA2:0002142 ! urogenital sinus lumen"
		},
		{
			"relationship" : "ends_at CS20 ! CS20"
		},
		{
			"relationship" : "located_in EHDAA2:0002142 ! urogenital sinus lumen"
		},
		{
			"relationship" : "located_in EHDAA2:0004740 ! urogenital sinus epithelium vesical part"
		},
		{
			"relationship" : "starts_at CS17 ! CS17"
		}
	]
      * </pre>
      */
      public static void setAHDARelationshipList(AHDA onto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (OntologyStrUtil.listExists(dbObj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.RELATIONSHIP);
                 if (OntologyStrUtil.isEHDAA2(str)) {
                    setAHDARelationship(str, onto, subGraph);
                 } else if (OntologyStrUtil.isAEOntology(str)) {
                    setAHDA_AEORelationship(str, onto, subGraph);
                 } else if (OntologyStrUtil.isCS(str)) {
                    setAHDA_CSRelationship(str, onto, subGraph);
                 }
             }
         }
     }
     
     public static void setAHDAIsListRelationship(AHDA onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, HumanDevAnatOntologyFields.IS_A_LIST)) {
             log.info("setAHDAIsListRelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                 setAHDAIsARelationship((BasicDBObject)obj, onto, subGraph);
             }
         }
    }
    
    /*
     * 	"is_a" : "AEO:0000221 ! open anatomical space"
     *  "is_a" : "CARO:0000073 ! unilaminar epithelium
     *  "is_a" : "EHDAA2:0000000 ! Abstract human developmental anatomy"
     */
    public static void setAHDAIsARelationship(BasicDBObject obj, AHDA onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
         String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.IS_A);
         if (OntologyStrUtil.isEHDAA2(str)) {
            AHDA entity = getAHDA(getId(str, OntologyStrUtil.ehdaa2Pattern), subGraph); 
            onto.setIsARelationship(onto, BioRelTypes.IS_A);
         } else if (OntologyStrUtil.isAEOntology(str)) {
             AnatomicalEntityOntology entity = getAEOntology(getId(str, OntologyStrUtil.aeoPattern), subGraph);
             onto.setIsARelationship(entity, BioRelTypes.IS_A);
         } else if (OntologyStrUtil.isCARO(str)) {
             CARO entity = getCARO(getId(str, OntologyStrUtil.caroPattern), subGraph);
             onto.setIsARelationship(onto, BioRelTypes.IS_A);
         }
     }
    
     /**
      * Abstract human developmental anatomy (AHDA)
      * processAHDA
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
     public static void processAHDA(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
        
         Subgraph subGraph = new Subgraph();
         AHDA onto = getAHDA(ontologyId, subGraph);
         
         if (onto != null) {
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.NAME)) { 
               onto.setName(OntologyStrUtil.getString(obj, HumanDevAnatOntologyFields.NAME));
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_OBSOLETE)) { 
               onto.setName(OntologyStrUtil.getString(obj, HumanDevAnatOntologyFields.IS_OBSOLETE));
            }
            
            /**
             * Related synonyms 
             */
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.SYNONYM_LIST)) {
                setAHDASynonyms(onto, obj);
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_A_LIST)) {
                setAHDAIsListRelationship(onto, obj, subGraph);
            } else if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_A)) {
                setAHDAIsARelationship(obj, onto,subGraph);
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_OBSOLETE)) {
               onto.setAhdaIsObsolete(getIsObsolete(obj));
            }
            
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST)) {
                setAHDARelationshipList(onto, obj, subGraph);
            }
            
            PersistenceTemplate.saveSubgraph(subGraph);
         }
     }
     
     /*
      * CS - carnegie stage
      */
     public static CS getCS(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
          if (id != null) {
            log.info("getCS(),  id =" + id);
            CS onto = (CS)subGraph.search(BioTypes.CS, BioFields.CS_ID, id);
            if (onto == null) {
                onto = new CS();
                onto.setCsId(id);
                subGraph.add(onto);
            }
            return onto;
        }
        return null;
     }
     
     /**
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
      * @throws RuntimeException
      * @throws Exception 
      */
     public static void setCSIsListRelationship(CS onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, HumanDevAnatOntologyFields.IS_A_LIST)) {
             log.info("setCSIsListRelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                 setCSIsARelationship((BasicDBObject)obj, onto, subGraph);
             }
         }
    }
    
    /*
     * 	
     */
    public static void setCSIsARelationship(BasicDBObject obj, CS onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
         String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.IS_A);
         if (OntologyStrUtil.isCS(str)) {
            CS entity = getCS(getId(str, OntologyStrUtil.csPattern), subGraph); 
            onto.setIsARelationship(onto, BioRelTypes.IS_A);
         } 
     }
    
    /**
     * 
     * relationship: starts_at CS05b ! CS05b
     * @param onto
     * @param dbObj
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setCSRelationshipList(CS onto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (OntologyStrUtil.listExists(dbObj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, HumanDevAnatOntologyFields.RELATIONSHIP);
                 if (OntologyStrUtil.isCS(str)) {
                    String id = getId(str, OntologyStrUtil.csPattern);
                    if (id != null) {
                        AnatomicalEntityOntology entity = getAEOntology(id, subGraph);
                        if (entity != null) {
                            BioRelTypes relType = OntologyStrUtil.getRelationshipType(str, OntologyStrUtil.csPattern);
                            onto.setCsRelationship(entity, relType);
                        }
                    }
                 } 
             }
         }
     }
     
     /**
      * <pre>
        id: CS20
        name: CS20
        namespace: carnegie_stage
        is_a: CS:0 ! Carnegie stage
        relationship: starts_at CS05b ! CS05b
        </pre>
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
     public static void processCS(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
        
         Subgraph subGraph = new Subgraph();
         CS onto = getCS(ontologyId, subGraph);
         
         if (onto != null) {
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.NAME)) { 
               onto.setName(OntologyStrUtil.getString(obj, HumanDevAnatOntologyFields.NAME));
            }
         
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_A_LIST)) {
                setCSIsListRelationship(onto, obj, subGraph);
            } else if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.IS_A)) {
                setCSIsARelationship(obj, onto,subGraph);
            }
        
            if (OntologyStrUtil.objectExists(obj, HumanDevAnatOntologyFields.RELATIONSHIP_LIST)) {
                setCSRelationshipList(onto, obj, subGraph);
            }
            
            PersistenceTemplate.saveSubgraph(subGraph);
         }
     }
     
}
