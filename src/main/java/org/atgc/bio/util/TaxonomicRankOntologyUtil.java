package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import java.lang.reflect.InvocationTargetException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.BioFields;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.TaxonomicRankOntologyFields;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.TaxonomicRankOntology;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of taxonomicrank ontology from taxonomic_rank.obo imports file.
 * Uses 
 * @author jtanisha-ee
 */
public class TaxonomicRankOntologyUtil {
    
    protected static Logger log = LogManager.getLogger(TaxonomicRankOntologyUtil.class);
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.TAXONOMIC_RANK_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, TaxonomicRankOntologyFields.ID);
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
          if (OntologyStrUtil.isModOntology(id)) { 
              if (!StatusUtil.idExists(BioTypes.TAXONOMIC_RANK_ONTOLOGY.toString(), BioFields.TAXONOMIC_RANK_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* protein modification ontology id =" + id);
                    try {
                        processTaxRankOntology(id, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } 
              } 
          } 
    }
    
    
    /**
     * getModOntology
     * @param id OntologyIdentifier
     * @param subGraph
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static TaxonomicRankOntology getTaxRankOntology(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getModOntology(),  id =" + id);
            TaxonomicRankOntology onto = (TaxonomicRankOntology)subGraph.search(BioTypes.TAXONOMIC_RANK_ONTOLOGY, BioFields.TAXONOMIC_RANK_ONTOLOGY_ID, id);
            if (onto == null) {
                onto = new TaxonomicRankOntology();
                onto.setTaxonomicRankOntologyId(id);
                subGraph.add(onto);
            }
            return onto;
        }
        return null;
    }
   
    /**
     * getName
     * name" :  N6-(L-isoaspartyl)-L-lysine
     * @param obj
     * @return String
     */
    public static String getName(BasicDBObject obj) {
         return OntologyStrUtil.getString(obj, TaxonomicRankOntologyFields.NAME);   
    }

    
    public static String getDef(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, TaxonomicRankOntologyFields.DEF);
    }
  
   
    
    /**
     * "synonym" : "\"3-methyl-norvaline\" EXACT RESID-alternate []"
     * @param str
     * @param pattern
     * @return 
     */
    private static String getCleanSyn(String str, String pattern) {
        return str.split(pattern)[0].replaceAll("'\'", "").trim();
    }
  
   
   
     /**
     * 
     * @param obj
     * @return 
     */
    public static String getSynonym(BasicDBList list, TaxonomicRankOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, TaxonomicRankOntologyFields.SYNONYM);
            if (str != null && str.contains(enumField.toString())) {
                switch(enumField) {
                    case EXACT:
                        synList.add(getCleanSyn(str, " EXACT"));
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
     * "synonym" : "\"variety\" EXACT []",
     * setSynonyms
     * @param cellOnto
     * @param obj 
     */
    public static void setSynonyms(TaxonomicRankOntology onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, TaxonomicRankOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, TaxonomicRankOntologyFields.EXACT)) != null) {
            onto.setTaxonomicRankOntologyExactSynonyms(synStr);
        } 
    }
   
    /**
     * We ignore the URL just connect the NCBITaxon relationship
     * <pre>
     * id: TAXRANK:0000001
        name: phylum
        synonym: "division" EXACT []
        xref: http://rs.tdwg.org/ontology/voc/TaxonRank#Phylum
        xref: NCBITaxon:phylum
        is_a: TAXRANK:0000000 ! taxonomic_rank
     * </pre>
     * xref: NCBITaxon:phylum
     * xref: http://rs.tdwg.org/ontology/voc/TaxonRank#patho-variety
     */
    public static void setXrefRelationship(TaxonomicRankOntology onto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, TaxonomicRankOntologyFields.XREF_LIST);
        if (list != null && list.size() > 0) {
            for (Object ref : list) {
                String xRef = getXRef((BasicDBObject)ref);
                if (xRef != null) {
                    onto.setXRefRelationship(xRef);
                }
            }
        } else {
            String xRef = getXRef(dbObj);
            if (xRef != null) {
                onto.setXRefRelationship(xRef);
            }
        }
    } 
    
    /**
     * getXRef of NCBITaxon:
     * @param dbObj
     * @return 
     */
    public static String getXRef(BasicDBObject dbObj) {
        if (!OntologyStrUtil.isObjectNull(dbObj, TaxonomicRankOntologyFields.XREF)) {
            String str = OntologyStrUtil.getString(dbObj, TaxonomicRankOntologyFields.XREF);
            return OntologyStrUtil.getId(str, OntologyStrUtil.NCBITaxonPattern);
        } else {
            return null;
        }
    }
    
    /**
     * is_a: TAXRANK:0000000 ! taxonomic_rank
     * getId - get identifier
     * @param str inputs                    
     * @param pattern
     * @return MOD: 
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
   
    public static String getIdFromIsA(String str) {
        return OntologyStrUtil.getId(str, OntologyStrUtil.taxRankIdPattern);
    }
   
    /**
     * All IS_A relationships 
     * is_a: TAXRANK:0000000 ! taxonomic_rank
     * setTaxonIsARelationship
     * @param string
     * @param onto {@link TaxonomicRankOntology}
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setTaxonIsARelationship(String str, TaxonomicRankOntology onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getIdFromIsA(str);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            TaxonomicRankOntology entity = getTaxRankOntology(id, subGraph);
            if (entity != null) {
               onto.setIsARelationship(entity);
            }
        }
    }
    
    /**
     * setIsARelationship
     * "is_a" : "TAXRANK:0000000 ! taxonomic_rank"   
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
    public static void setIsARelationship(TaxonomicRankOntology onto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, TaxonomicRankOntologyFields.IS_A_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, TaxonomicRankOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, TaxonomicRankOntologyFields.IS_A);
                if (OntologyStrUtil.isTaxonmicRankOntology(str)) {
                   setTaxonIsARelationship(str, onto, subGraph);
                } 
            }
         } else {
             if (OntologyStrUtil.isObjectNull(dbObj, TaxonomicRankOntologyFields.IS_A)) {
                String str = OntologyStrUtil.getString(dbObj, TaxonomicRankOntologyFields.IS_A);
                if (str != null) {
                    setTaxonIsARelationship(str, onto, subGraph);
                }
             }
         }
    }
     
    /**
     * processTaxonRankOntology
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
     public static void processTaxRankOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         TaxonomicRankOntology onto = getTaxRankOntology(ontologyId, subGraph);
         if (OntologyStrUtil.objectExists(obj, TaxonomicRankOntologyFields.NAME)) { 
            onto.setTaxonomicRankOntologyName(getName(obj));
         }
        
         if (getDef(obj) != null) {
            onto.setTaxonomicRankOntologyDef(getDef(obj));
         }
      
         if (OntologyStrUtil.objectExists(obj, TaxonomicRankOntologyFields.SYNONYM_LIST)) {
             setSynonyms(onto, obj);
         }
         
         if (OntologyStrUtil.objectExists(obj, TaxonomicRankOntologyFields.IS_A_LIST)) {
             setIsARelationship(onto, obj, subGraph);
         }
        
         PersistenceTemplate.saveSubgraph(subGraph);
     }
    
}
        

