package org.atgc.bio.util;

import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.RNAOntologyFields;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.BioFields;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.ChebiOntology;
import org.atgc.bio.domain.RNAOntology;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of Homology Ontology file from Homology_ontology.obo,   obofoundry.org
 * MongoDB: rnaontology
 * Reference to terms used:
 * http://www.geneontology.org/GO.format.obo-1_2.shtml
 * Uses 
 * @author jtanisha-ee
 */
public class RNAOntologyUtil {
    
    protected static Log log = LogFactory.getLog(RNAOntologyUtil.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.RNA_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, RNAOntologyFields.ID);
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
              if (!StatusUtil.idExists(BioTypes.RNA_ONTOLOGY.toString(), BioFields.RNA_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* RNA ontology id =" + id);
                    try {
                        processRNAOntology(id, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } 
              } 
          } 
    }
    
    
    /**
     * getRNAOntology
     * @param id OntologyIdentifier
     * @param subGraph
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static RNAOntology getRNAOntology(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getRNAOntology,  id =" + id);
            RNAOntology onto = (RNAOntology)subGraph.search(BioTypes.RNA_ONTOLOGY, BioFields.RNA_ONTOLOGY_ID, id);
            if (onto == null) {
                onto = new RNAOntology();
                onto.setRNAOntologyId(id);
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
         return OntologyStrUtil.getString(obj, RNAOntologyFields.NAME);   
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
        return OntologyStrUtil.getString(obj, RNAOntologyFields.DEF);
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
     * getId - get identifier
     * @param str inputs                    
     * @param pattern
     * @return 
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
    
    /**
     * is_a: CHEBI:25805
     * setIsARelationship
     * @param HOM
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
    /*
    public static void setIsListRelationship(RNAOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, RNAOntologyFields.IS_A_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, RNAOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                //String str = OntologyStrUtil.getString((BasicDBObject)obj, RNAOntologyFields.IS_A);
                setIsARelationship(onto, (BasicDBObject)obj, subGraph);
            }
         }
    } */
    
    public static void setIsARelationship(RNAOntology onto, BasicDBObject obj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String str = OntologyStrUtil.getString((BasicDBObject)obj, RNAOntologyFields.IS_A);
        if (OntologyStrUtil.isRNAO(str)) {
            String id = getId(str, OntologyStrUtil.RNAOPattern); 
            if (id != null) {
                RNAOntology entity = getRNAOntology(id, subGraph);
                if (entity != null) {
                    onto.setIsARelationship(entity, BioRelTypes.IS_A);
                }   
            }
         } else if (OntologyStrUtil.isChebiOntology(str)) {
             String id = getId(str, OntologyStrUtil.chebiIdPattern);
             if (id != null) {
                 ChebiOntology entity = ChebiOntologyImport.getChebi(id, subGraph);
                 if (entity != null) {
                     onto.setIsARelationship(entity, BioRelTypes.IS_A);
                 }
             }
         }
    }
    
    /**
     *  relationship: ro.owl#part_of RNAO:0000000
     *  relationship: ro.owl#has_quality RNAO:0000200
     *  relationship: ro.owl#part_of RNAO:0000021
     *  relationship: has_functional_parent RNAO:0000105
     *  relationship: relationship: RNAO:0000352 RNAO:0000111
     *  has_proper_part, part_of, has_quality, part_of, functional_parent, 
     *  has_functional_parent
     *  relationship: RNAO:0000354 snap#MaterialEntity
     * @param str
     * @return 
     */
    public static BioRelTypes getRelationshipType(String str) {
        log.info("BioRelationship =" + str);
        if (str.contains(OntologyStrUtil.RNAOPattern)) { 
            String str1 = str.split(OntologyStrUtil.RNAOPattern)[0].toUpperCase().trim();
            log.info("str1 =" + str1);
            if (str1 != null) {
                if (str1.contains("#")) { 
                    String str2 = str1.split("#")[1];
                    return BioRelTypes.getEnum(str2);
                } else {
                    log.info("str1 =" + str1);
                    return BioRelTypes.getEnum(str1);
                }
            } else {
                log.info("default relationship " + str);
                return BioRelTypes.IS_RELATIONSHIP;
            }
        } else {
            return null;
        }
    }
       
     
   /**
    * relationship: ro.owl#part_of RNAO:0000000
    * relationship: ro.owl#has_quality RNAO:0000200
    * @param onto
    * @param dbObj
    * @param subGraph
    * @throws NotFoundException
    * @throws NoSuchFieldException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws InvocationTargetException 
    */
    public static void setRelationship(RNAOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
         if (OntologyStrUtil.listExists(dbObj, RNAOntologyFields.RELATIONSHIP)) {
            String str = OntologyStrUtil.getString((BasicDBObject)dbObj, RNAOntologyFields.RELATIONSHIP);
            if (OntologyStrUtil.isHOM(str)) {
                String id = getId(str, OntologyStrUtil.RNAOPattern); 
                if (id != null) {
                    RNAOntology entity = getRNAOntology(id, subGraph);
                    if (entity != null) {
                        BioRelTypes relType = getRelationshipType(str);
                        onto.setRelationship(entity, relType);
                    }   
                }
            } 
         }
           
    }
   
     
    /**
     * processRNAOntology
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
     public static void processRNAOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         RNAOntology RNAO = getRNAOntology(ontologyId, subGraph);
         if (OntologyStrUtil.objectExists(obj, RNAOntologyFields.NAME)) { 
            RNAO.setRNAOntologyName(getName(obj));
         }
        
         if (getDef(obj) != null) {
            RNAO.setRnaOntologyDef(getDef(obj));
         }
             
         if (OntologyStrUtil.objectExists(obj, RNAOntologyFields.IS_A)) {
             setIsARelationship(RNAO, obj, subGraph);
         }       
        
         
         /**
          * BasicDBList
          */
         if (OntologyStrUtil.objectExists(obj, RNAOntologyFields.RELATIONSHIP_LIST)) {
             setRelationship(RNAO, obj, subGraph);
         }        
        
         RedbasinTemplate.saveSubgraph(subGraph);
     }
    
}
        

