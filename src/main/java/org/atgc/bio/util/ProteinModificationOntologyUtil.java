package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.ProteinModificationOntologyFields;
import org.atgc.bio.repository.RedbasinTemplate;
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
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.ProteinModificationOntology;
import org.atgc.bio.domain.PubMed;
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of proteinmodificationontology from psi-mod.obo imports file.
 * Uses 
 * @author jtanisha-ee
 */
public class ProteinModificationOntologyUtil {
    
    
    protected static Logger log = LogManager.getLogger(ProteinModificationOntologyUtil.class);
     private static String diffAvgPattern = "DiffAvg"; 
     private static String diffFormulaPattern = "DiffFormula";
     private static String diffMonoPattern = "DiffMono";
     private static String massAvgPattern = "MassAvg";
     private static String massMonoPattern = "MassMono";
     private static String originPattern = "Origin";
     private static String sourcePattern = "Source";
     private static String termSpecPattern = "TermSpec";
     private static String formulaPattern = "Formula";
     
     private static String pubMedPattern = "PubMed:";
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.PROTEIN_MODIFICATION_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, ProteinModificationOntologyFields.ID);
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
              if (!StatusUtil.idExists(BioTypes.PROTEIN_MODIFICATION_ONTOLOGY.toString(), BioFields.PROTEIN_MOD_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* protein modification ontology id =" + id);
                    try {
                        processModOntology(id, result);
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
    public static ProteinModificationOntology getModOntology(String id, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (id != null) {
            log.info("getModOntology(),  id =" + id);
            ProteinModificationOntology modOnto = (ProteinModificationOntology)subGraph.search(BioTypes.PROTEIN_MODIFICATION_ONTOLOGY, BioFields.PROTEIN_MOD_ONTOLOGY_ID, id);
            if (modOnto == null) {
                modOnto = new ProteinModificationOntology();
                modOnto.setProteinModOntologyId(id);
                subGraph.add(modOnto);
            }
            return modOnto;
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
         return OntologyStrUtil.getString(obj, ProteinModificationOntologyFields.NAME);   
    }
    
    public static String getNameSpace(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj,ProteinModificationOntologyFields.NAME_SPACE);
    } 
    
    public static String getDef(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, ProteinModificationOntologyFields.DEF);
    }
    
    public static String getComment(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.COMMENT);
    }
    
    public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.IS_OBSOLETE);
    }
    
    /**
     * xref list
     * @param dbObj
     * @return 
     */
    /*
    public static String getDiffMono(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.DIFF_MONO);
    }
    
    public static String getFormula(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.FORMULA);
    }
    
    public static String getDiffAvg(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.DIFF_AVG);
    }
    
    public static String getMassMono(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.MASSMONO);
    }
    
    public static String getMassAvg(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.MASSAVG);
    }

    public static String getOrigin(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.ORIGIN);
    }
    
    public static String getSource(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.SOURCE);
    }
     
    public static String getTermSpec(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.TERMSPEC);
    }
   */
    
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
     * "A protein modification that effectively converts a glycine residue to N-myristoylglycine."
     * [PubMed:11955007, PubMed:11955008, PubMed:1326520, PubMed:1386601, PubMed:6436247, PubMed:7543369, RESID:AA0059, UniMod:45]
     * @param dbObj
     * @param pList 
     */
    public static void getPubmedFromDef(BasicDBObject dbObj, List<String> pList) {
       String str = OntologyStrUtil.getString(dbObj, ProteinModificationOntologyFields.DEF);
       OntologyStrUtil.getPubMedIdFromPattern(str, pList, pubMedPattern);  
    }
   
   
     /**
     * "synonym" : "\"2-azanyl-3-methylpentanoic acid\" EXACT RESID-alternate []"
     * "synonym" : "\"3-methyl-norvaline\" EXACT RESID-alternate []"
     * @param obj
     * @return 
     */
    public static String getSynonym(BasicDBList list, ProteinModificationOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinModificationOntologyFields.SYNONYM);
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
     * setSynonyms
     * @param cellOnto
     * @param obj 
     */
    public static void setSynonyms(ProteinModificationOntology mod, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinModificationOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, ProteinModificationOntologyFields.EXACT)) != null) {
            mod.setProteinModOntologyExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, ProteinModificationOntologyFields.RELATED)) != null) {
           mod.setProteinModOntologyRelatedSynonyms(synStr);
        }
    }
    
     /**
     * 
     * "is_a" : "MOD:01441 ! natural, standard, encoded residue"
     * @param str
     * @return
     */
    public static String getModIdFromIsList(String str) {
        return OntologyStrUtil.getId(str, OntologyStrUtil.modIdPattern);
    }
   
    /**
     * "is_a" : "MOD:01441 ! natural, standard, encoded residue"
     * getId - get identifier
     * @param str inputs                    
     * @param pattern
     * @return MOD: 
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
    
    /**
     * All relationships are MOD bio entities
     * Relationship is any of these:  derives_from,  has_functional_parent, contains
     * 
     * setProteinModRelationship
     * @param str
     * @param onto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setModRelationship(String str, ProteinModificationOntology onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getId(str, OntologyStrUtil.modIdPattern);
        if (id != null) {
            ProteinModificationOntology entity = getModOntology(id, subGraph);
            if (entity != null) {
                BioRelTypes relType = getRelationshipType(str, OntologyStrUtil.modIdPattern);
                if (relType == null) {
                    log.info("relType is null, " + str);
                }
                onto.setRelationship(entity, relType);
            }
        }
     }

    /**
     * All IS_A relationships are MOD entities
     * is_a: MOD:01659 ! Uniblue A derivatized residue
     * setModIsARelationship
     * @param string
     * @param onto {@link ProteinModificationOntology}
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setModIsARelationship(String str, ProteinModificationOntology onto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String id = getModIdFromIsList(str);
        //log.info("id = " + goId + ", name = " + name);
        if (id != null) {
            ProteinModificationOntology entity = getModOntology(id, subGraph);
            if (entity != null) {
               onto.setIsARelationship(entity);
            }
        }
    }
    
    /**
     * setIsARelationship
     * @param mod
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
    public static void setIsARelationship(ProteinModificationOntology mod, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, ProteinModificationOntologyFields.IS_A_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinModificationOntologyFields.IS_A_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinModificationOntologyFields.IS_A);
                if (OntologyStrUtil.isModOntology(str)) {
                   setModIsARelationship(str, mod, subGraph);
                } 
            }
         }
    }
    
    /**
     * relationship: has_functional_parent MOD:00037 ! 5-hydroxy-L-lysine
     * @param str
     * @return {@link BioRelTypes}
     */
    
    public static BioRelTypes getRelationshipType(String str, String pattern) {
        return OntologyStrUtil.getRelationshipType(str, pattern);
    } 
   
     /**
     * XRef Property
     * 		"xref" : "DiffAvg: \"0.00\""
     *            "Formula: \"C 6 H 7 N 3 O 1\""
     */     
     public static boolean isDiffAvg(String str) {
         return OntologyStrUtil.patternExists(str, diffAvgPattern);
     }
     
     public static boolean isFormula(String str) {
         return OntologyStrUtil.patternExists(str, formulaPattern);
     }
     
     public static boolean isDiffFormula(String str) {
         return OntologyStrUtil.patternExists(str, diffFormulaPattern);
     }
     
     public static boolean isDiffMono(String str) {
         return OntologyStrUtil.patternExists(str, diffMonoPattern);
     }
     
     public static boolean isMassAvg(String str) {
         return OntologyStrUtil.patternExists(str, massAvgPattern);
     }
     
     public static boolean isMassMono(String str) {
         return OntologyStrUtil.patternExists(str, massMonoPattern);
     }
     
     public static boolean isOrigin(String str) {
         return OntologyStrUtil.patternExists(str, originPattern);
     }
     
     public static boolean isSource(String str) {
         return OntologyStrUtil.patternExists(str, sourcePattern);
     }
     
     public static boolean isTermSpec(String str) {
         return OntologyStrUtil.patternExists(str, termSpecPattern);
     }
     
     
     public static String getPropertyVal(String str, String pattern) {
         return str.split(pattern)[1].replaceAll("'\'", "").trim();
     }     
     
    /**
     * "relationshipList" : [
     *         
     */
     public static void setRelationshipList(ProteinModificationOntology mod, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception {
         if (OntologyStrUtil.listExists(dbObj, ProteinModificationOntologyFields.RELATIONSHIP_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinModificationOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinModificationOntologyFields.RELATIONSHIP);
                 if (OntologyStrUtil.isModOntology(str)) {
                    setModRelationship(str, mod, subGraph);
                 } 
             }
         }
     }
   
     /**
      * 
      * @param mod
      * @param dbObj
      * @param subGraph 
      */
     public static void setXRefPropertyList(ProteinModificationOntology mod, BasicDBObject dbObj) {
         if (OntologyStrUtil.listExists(dbObj, ProteinModificationOntologyFields.XREF_LIST)) {
             log.info("xRefList exists");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinModificationOntologyFields.XREF_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinModificationOntologyFields.XREF);
                 if (str != null) {
                    setXRefPropertyRelationship(str, mod);
                 } 
             }
         }
     }
     
     /**
      *   "xref" : "DiffAvg: \"0.00\""
      *   "xref" : "DiffFormula: \"C 0 H 0 N 0 O 0\""
      * @param str
      * @param mod
      * @param subGraph 
      */
     public static void setXRefPropertyRelationship(String str, ProteinModificationOntology mod) {
         if (isDiffAvg(str)) {
             mod.setDiffAvg(getPropertyVal(str, diffAvgPattern));
         } 
         if (isDiffFormula(str)) {
             mod.setDiffFormula(getPropertyVal(str, diffFormulaPattern));
         }
         if (isFormula(str)) {
             mod.setFormula(getPropertyVal(str, formulaPattern));
         }
         if (isDiffMono(str)) {
             mod.setDiffMono(getPropertyVal(str, diffMonoPattern));
         }
         
         if (isMassMono(str)) {
             mod.setMassMono(getPropertyVal(str, massMonoPattern));
         }
         
         if (isMassAvg(str)) {
             mod.setMassAvg(getPropertyVal(str, massAvgPattern));
         }

         if (isOrigin(str)) {
             mod.setOrigin(getPropertyVal(str, originPattern));
         }
         
         if (isSource(str)) {
             mod.setSource(getPropertyVal(str, sourcePattern));
         }
         
         if (isTermSpec(str)) {
             mod.setSource(getPropertyVal(str, termSpecPattern));
         }
     }
     
     /**
      * PubMed:1097438, PubMed:339692, PubMed:4399050,
      * @param pubMedId
      * @param subGraph
      * @return
      * @throws NoSuchFieldException
      * @throws IllegalArgumentException
      * @throws IllegalAccessException
      * @throws Exception 
      */
     public static PubMed getPubMed(String pubMedId, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, Exception {
            PubMed pubMed = (PubMed)subGraph.search(BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);
            if (pubMed == null) { 
                log.info("getPubMed(), pubMedId =" + pubMedId);
                pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
            }
            return pubMed;
      }
     
     /**
      * def" : "\"A protein modification that effectively converts a source amino acid residue to an L-aspartic acid.\" 
      * [ChEBI:29958, DeltaMass:0, PubMed:1097438, PubMed:339692, PubMed:4399050]
      *       
      * @param mod
      * @param obj
      * @param subGraph 
      */
     public static void setPubMedRelationship(ProteinModificationOntology mod, BasicDBObject obj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, Exception {
         List<String> pList = new ArrayList();
         getPubmedFromDef(obj, pList);
         for (String pubMedId : pList) {
             PubMed pubMed = getPubMed(pubMedId, subGraph);
             if (pubMed != null) {
                 pubMed.setPubMedRelation(mod);
             }
         }
     }
     
     
    /**
     * processModOntology
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
     public static void processModOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         ProteinModificationOntology mod = getModOntology(ontologyId, subGraph);
         if (OntologyStrUtil.objectExists(obj, ProteinModificationOntologyFields.NAME)) { 
            mod.setProteinModOntologyName(getName(obj));
         }
        
         if (getDef(obj) != null) {
            mod.setDef(getDef(obj));
            setPubMedRelationship(mod, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinModificationOntologyFields.COMMENT)) {
             mod.setComment(getComment(obj));
         }
      
         if (OntologyStrUtil.objectExists(obj, ProteinModificationOntologyFields.SYNONYM_LIST)) {
             setSynonyms(mod, obj);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinModificationOntologyFields.IS_A_LIST)) {
             setIsARelationship(mod, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinModificationOntologyFields.RELATIONSHIP_LIST)) {
            setRelationshipList(mod, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinModificationOntologyFields.IS_OBSOLETE)) {
             mod.setProteinModOntologyIsObsolete(getIsObsolete(obj));
         }
         
         setXRefPropertyList(mod, obj);
        
         RedbasinTemplate.saveSubgraph(subGraph);
     }
    
}
        

