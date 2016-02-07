package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.SystemsBiologyOntology;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpException;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.SystemsBiologyOntologyFields;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.PubMed;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of system biology ontology from sbo.obo imports file.
 * Uses 
 * @author jtanisha-ee
 */
public class SystemsBiologyOntologyUtil {
    
    protected static Logger log = LogManager.getLogger(SystemsBiologyOntologyUtil.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.SYSTEMS_BIOLOGY_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, SystemsBiologyOntologyFields.ID);
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
          if (OntologyStrUtil.isSBOOntology(id)) { 
              if (!StatusUtil.idExists(BioTypes.SYSTEMS_BIOLOGY_ONTOLOGY.toString(), BioFields.SYSTEMS_BIOLOGY_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* system biology ontology id =" + id);
                    try {
                        processSystemBiologyOntology(id, result);
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
    public static SystemsBiologyOntology getSystemBiologyOntology(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getSystemBiologyOntology(),  id =" + id);
            SystemsBiologyOntology onto = (SystemsBiologyOntology)subGraph.search(BioTypes.SYSTEMS_BIOLOGY_ONTOLOGY, BioFields.SYSTEMS_BIOLOGY_ONTOLOGY_ID, id);
            if (onto == null) {
                onto = new SystemsBiologyOntology();
                onto.setSystemsBiologyOntologyId(id);
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
         return OntologyStrUtil.getString(obj, SystemsBiologyOntologyFields.NAME);   
    }
    
    public static String getDef(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, SystemsBiologyOntologyFields.DEF);
    }
    
    public static String getComment(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SystemsBiologyOntologyFields.COMMENT);
    }
    
    public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, SystemsBiologyOntologyFields.IS_OBSOLETE);
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
     * comment: Definition contributed by Denis Thieffry;\nrefer to \[SF req #3125359\];\nsee also \[PMID:4588055\]
     *
     * getPubMedFromComment
     * @param dbObj
     * @param pList 
     */
    private static void getPubMedFromComment(BasicDBObject dbObj, List<String> pList) {
        String str = getComment(dbObj);
	str.replaceAll("'\'", "").trim();
        OntologyStrUtil.extractPubMedId(str, pList);
    }
   
    /**
     * comment: Definition contributed by Denis Thieffry;\nrefer to \[SF req #3125359\];\nsee also \[PMID:4588055\]
     *
     * createPubMedRelationship
     * @param sboOnto
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws UnknownHostException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     * @throws InterruptedException
     * @throws Exception 
     */
    public static void createPubMedRelationship(SystemsBiologyOntology onto, BasicDBObject dbObj,  Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, UnknownHostException, NotFoundException, InvocationTargetException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, HttpException, InterruptedException, Exception {
        List<String> pList = new ArrayList();
        getPubMedFromComment(dbObj, pList);
        for (String pubMedId : pList) {
            PubMed pubMed = (PubMed)subGraph.search(BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);
            if (pubMed == null) { 
                log.info("getPubMed(), pubMedId =" + pubMedId);
                pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
                if (pubMed != null) { 
                    pubMed.setPubMedRelation(onto);
                }
            }
        }
    }
   
     /**
     * 
     * "synonym" : "\"FHOD3/iso:3\" EXACT PRO-short-label [PRO:DNx]"
     *  synonym: "GPT" RELATED [PMID:1931970]
     * @param obj
     * @return 
     */
    public static String getSynonym(BasicDBList list, SystemsBiologyOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, SystemsBiologyOntologyFields.SYNONYM);
            
        } 
        if (synList.isEmpty()) {
            return null;
        } else {
            return synList.toString();
        }
    }
    
    /**
     * synonym: "reaction rate constant" []
     * There are no RELATED, EXACT and NARROW in synonym in this.
     * setSynonyms
     * @param cellOnto
     * @param obj 
     */
    public static void setSynonyms(SystemsBiologyOntology sboOnto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, SystemsBiologyOntologyFields.SYNONYM_LIST);
        String synStr;
        
    }
    
     /**
     * is_a: SBO:0000064 ! mathematical expression
     * @param str
     * @return
     */
    public static String getIdFromIsList(String str) {
        return OntologyStrUtil.getId(str, OntologyStrUtil.sboIdPattern);
    }
 
    /**
     * getId - get identifier
     * @param str inputs are :
     *                is_a: SBO:0000205 ! composite biochemical process     
     * @param pattern
     * @return SBO:0000183
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
   
   
    /**
     *
     * is_a: SBO:0000205 ! composite biochemical process      
     *
     * setIsARelationship
     * @param sboOnto
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
    public static void setIsARelationship(SystemsBiologyOntology sboOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, SystemsBiologyOntologyFields.IS_A_LIST)) {
             log.info("setIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, SystemsBiologyOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, SystemsBiologyOntologyFields.IS_A);
                if (OntologyStrUtil.isSBOOntology(str)) {
                   String id = getId(str, OntologyStrUtil.sboIdPattern);
                   if (id != null) { 
                       SystemsBiologyOntology sbEntity = getSystemBiologyOntology(id, subGraph); 
                       if (sbEntity != null) { 
                           sboOnto.setIsARelationship(sbEntity);
                       }
                   }
                }
            }
         }
    }
    
     
    /**
     * processSystemBiologyOntology
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
     public static void processSystemBiologyOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         SystemsBiologyOntology sboOnto = getSystemBiologyOntology(ontologyId, subGraph);
         if (sboOnto != null) {
            if (OntologyStrUtil.objectExists(obj, SystemsBiologyOntologyFields.NAME)) { 
               sboOnto.setSystemsBiologyOntologyName(getName(obj));
            }

            if (getDef(obj) != null) {
               sboOnto.setDef(getDef(obj));
            }
         
            if (OntologyStrUtil.objectExists(obj, SystemsBiologyOntologyFields.COMMENT)) {
                sboOnto.setComment(getComment(obj));
            }
      
            if (OntologyStrUtil.objectExists(obj, SystemsBiologyOntologyFields.SYNONYM_LIST)) {
                setSynonyms(sboOnto, obj);
            }
         
            if (OntologyStrUtil.objectExists(obj, SystemsBiologyOntologyFields.IS_A_LIST)) {
                setIsARelationship(sboOnto, obj, subGraph);
            }
         
            if (OntologyStrUtil.objectExists(obj, SystemsBiologyOntologyFields.IS_OBSOLETE)) {
               sboOnto.setSystemsBiologyOntologyIsObsolete(getIsObsolete(obj));
            }
            createPubMedRelationship(sboOnto, obj, subGraph);
            RedbasinTemplate.saveSubgraph(subGraph);
         }
     }
    
}
