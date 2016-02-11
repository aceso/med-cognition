/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.atgc.bio.BioFields;
import org.atgc.bio.MongoClasses;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.mongod.MongoObjects;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This contains basic Enum utilities
 *  Used for NciFields & NciCompoundSources 
 *
 * @author Smitha Gudur
 */
public class OntologyStrUtil {

    /** Logger for this class and subclasses */
    protected static final Logger log = LogManager.getLogger(OntologyStrUtil.class);
    
    public static String cellIdPattern = "CL:";
    public static String prIdPattern = "PR:";
    public static String goIdPattern = "GO:";
    public static String enzymePattern = "EC:";
    public static String exclamationPattern = "!";
    public static String spacePattern = " ";
    public static String NCBITaxonPattern = "NCBITaxon:";
    public static String uberonIdPattern = "UBERON:";
    public static String chebiIdPattern = "CHEBI:";
    public static String patoIdPattern = "PATO:";
    public static String cpIdPattern = "CP:";
    public static String obiIdPattern = "OBI:";
    public static String soIdPattern = "SO:";
    public static String modIdPattern = "MOD:";
    public static String sboIdPattern = "SBO:";
    public static String taxRankIdPattern = "TAXRANK:";
    public static String spatialPattern = "BSPO:";
    public static String aeoPattern = "AEO:";
    public static String caroPattern = "CARO:";
    public static String ehdaa2Pattern = "EHDAA2";
    public static String csPattern = "CS";
    public static String HDOPattern = "DOID:";
    public static String HOMPattern = "HOM:";
    public static String RNAOPattern = "RNAO";
    public static String symptomPattern = "SYMP:";
    
   
    /**
     * getString
     * @param map
     * @param field
     * @return 
     */
    public static String getString(BasicDBObject map,  Enum field) {
        return (String)map.get(getStringFromEnum(field));
    }
     
     /**
     * getString
     * @param map
     * @param field
     * @return 
     */
     /*
    public static String getString(Map map, Enum field) {
        return (String)map.get(field.toString());
    }*/

    /**
     *
     * @param map
     * @param field
     * @return
     */
     public static boolean isString(BasicDBObject map, Enum field) {
       return map.get(field.toString()).getClass().equals(String.class);
     }

    public static BasicDBList getList(BasicDBObject map, Enum field) {
        log.info(map.get(field.toString()).getClass().getName());
        return (BasicDBList)map.get(field.toString());
    }
    
    public static String getStringFromEnum(Enum field) {
       return field.toString();    
    }
    
    public static String getString(BasicDBObject molecule, BioFields field) {
        return (String)molecule.getString(getStringFromEnum(field));
    }
    
    public static BasicDBObject getDBObject(BasicDBObject map, Enum field) {
        return (BasicDBObject)map.get(field.toString());
    }
   
    public static BasicDBList getDBList(BasicDBObject dbObject, Enum field) {
        if (dbObject == null || field == null) {
            throw new RuntimeException("dbObject is null for field " + field.toString());
        }
        Object obj = dbObject.get(field.toString());
        if (obj == null) return null;
        if (obj.getClass().equals(MongoClasses.BasicDBList)) {
            log.info("basicDBList");
            return (BasicDBList)((BasicDBObject)dbObject).get(getStringFromEnum(field));
        } else {
            return null;
        }
    }
    
    /**
    * This method returns a BasicDBList even if the result is a DBObject.
     * This is because the import data sometimes stores them as DBObject if 
     * there is only one element. Users of this method must not pass a null
     * dbObject, and that will lead to an error. However if the field is not
     * found, do not throw exception, just return a null. Many times the fields
     * are not found, that does not mean there is an error. 
     * 
    * getBasicDBList
    * @param dbObject
    * @param field
    * @return 
    */
   public static BasicDBList getBasicDBList(DBObject dbObject, Enum field) {
        if (dbObject == null || field == null) {
            throw new RuntimeException("dbObject is null, field= " + getStringFromEnum(field));
        }
        Object obj = dbObject.get(getStringFromEnum(field));
        if (obj == null) return null;
        return MongoObjects.getBasicDBList(obj);
   }
   
   
   
   public static boolean isObjectString(Object obj) {
       if (String.class.equals(obj.getClass())) {
           return true;
       } else {
           return false;
       }
   }

   // object is null return false, else return true
   public static boolean isObjectNull(BasicDBObject dbObject, Enum field) {
       //if (dbObject == null || dbObject.toString() == null) 
       if (dbObject.get(field.toString()) == null) {
           return false;
       } else {
           return true;
       }
   }
   
   public static boolean isObjectString(BasicDBObject dbObject, Enum field) {
        Object obj = dbObject.get(field.toString());
        if (String.class.equals(obj.getClass())) {
            return true;
        } else {
            return false;
        }
    }
   
   /**
     * objectExists 
     * @param map
     * @param field
     * @return 
     */
    public static boolean objectExists(Map map, Enum field) {
       //log.info(map.get(field.toString()));
       if (map.get(field.toString()) == null) {
           return false;
       } else {
           return true;
       }
    }
    
    public static boolean objectContains(BasicDBObject dbObj, Enum field) {
        if (dbObj.containsField(field.toString())) {
            return true;
        } else {
            return false;
        }
    }
    
   /**
     * 
     * @param map
     * @param field
     * @return 
     */
    public static boolean listExists(BasicDBObject map, Enum field) {
        log.info("list exists");
        if (objectExists(map, field)) {
            log.info("listExists(), non null, " + field.toString());
            BasicDBList list = (BasicDBList)getList(map, field);
            log.info("list size=" + list.size() + list.toString());
            if (list == null || list.size() <= 0) {
                return false;
            } else {
                return true;
            }
        } else {
            return false;
        } 
    }
   
    
    public static String removeComma(String s) {
        if (s.contains(",")) {
            return s.split(",")[0].trim();
        } else {
            return s;
        }
    }
    
    public static String removeHalfBracket(String s) {
        if (s.contains("]")) {
            return s.split("]")[0].trim();
        } else {
            return s;
        }
    }
    
    public static boolean hasDigits(String body) {
       if (body != null) {
          /* "\\d+" */
          Pattern pattern = Pattern.compile("^[0-9]+$");
          Matcher matcher = pattern.matcher(body);
          return matcher.find();
       }
       return false;
    }
     
    
    public static List<String> extractPubMedId(String val, List<String> pList) {   
        String pmidPattern = "PMID:";
        return getPubMedIdFromPattern(val, pList, pmidPattern);
    }
    
    /**
     * pattern can be: PMID: or PUBMED:
     * @param val
     * @param pList
     * @param pmidPattern
     * @return 
     */
    public static List<String> getPubMedIdFromPattern(String val, List<String> pList, String pmidPattern) {
        String halfBracketPattern = "]";
        String commaPattern = ",";
        
        if (val != null) {
            if (val.contains(pmidPattern)) {
                String vals[] = val.split(pmidPattern);
                for (String s3 : vals ) {
                    if (s3.contains(halfBracketPattern)) {
                        String cs = removeHalfBracket(s3);
                        cs= removeComma(cs);
                        if (hasDigits(cs)) {
                            pList.add(cs);
                        }
                    } else if (s3.contains(commaPattern)) {
                        String cs = removeComma(s3);
                        cs = removeHalfBracket(cs);
                        if (hasDigits(cs)) {
                            pList.add(cs);
                        }
                    }
                }
            }
         }
        return pList;
    }
    
    
    
    /**
     * appropriate for is_a
     * orthogonal_to BSPO:0000417 ! sagittal section
     * only_in_taxon NCBITaxon:9606 !
     * 
     * applies to "GO:" "CL:" "PR:", "SBO:"
     * @param val
     * @param pattern "NCBITaxon:", "GO:" or "CL:" or  "PR:" SBO:
     * @return GO:23434343 or CL:334343 or PR:00002322, NCBITaxon:9606, SBO:000183
     */
    public static String getId(String val, String pattern) {
        String str[] = val.split(pattern);
        if (str.length > 0) { 
            for (String s: str) {
                if (s != null && s.contains("!")) {
                    String id = pattern + s.split("!")[0].trim();
                    log.info("id " + id);
                    return id;
                }
            }
        }
        return null;
    }
    
    /**
     * relationship: has_functional_parent MOD:00037 ! 5-hydroxy-L-lysine
     * 
     * relationship: MOD:00039
     * @param str
     * @param pattern
     * @return 
     */
    public static BioRelTypes getRelationshipType(String str, String pattern) {
        if (str.contains(pattern)) { 
            String str1 = str.split(pattern)[0].toUpperCase().trim();
            log.info("str1 =" + str1);
            if (str1 != null) { 
                return BioRelTypes.getEnum(str1);
            } 
        } 
        return null;
    }
    
    /**
     * "consider" : "CL:0000003", 
     * "consider" : "GO:0003756"
     * 
     * @param str
     * @return 
     */
    public static String getIdFromConsiderList(String str) {
        return str.trim();
    }
    
    public static boolean patternExists(String str, String pattern) {
          if (str.contains(pattern)) {
              return true;
          } else {
              return false;
          }
    }
    
    /**
     * check for ids
     */
    public static boolean isProteinOntology(String str) {
        return patternExists(str, prIdPattern);
    }
    
    public static boolean isGeneOntology(String str) {
        return patternExists(str, goIdPattern);
    }
    
    public static boolean isCellTypeOntology(String str) {
        return patternExists(str, cellIdPattern);
    }
    
    public static boolean isNcbiTaxon(String str) {
        return patternExists(str, NCBITaxonPattern);
    }
    
    public static boolean isEnzyme(String str) {
        return patternExists(str, enzymePattern);
    }
    
    public static boolean isChebiOntology(String str) {
        return patternExists(str, chebiIdPattern);
    }
    
    public static boolean isModOntology(String str) {
        return patternExists(str, modIdPattern);
    }
    
    public static boolean isSBOOntology(String str) {
        return patternExists(str, sboIdPattern);
    }
   
    public static boolean isTaxonmicRankOntology(String str) {
        return patternExists(str, taxRankIdPattern);
    }
    
     public static boolean isSpatialOntology(String str) {
        return patternExists(str, spatialPattern);
    }
    
    /**
     * Antomical Entity Ontology
     * @param str
     * @return 
     */
    public static boolean isAEOntology(String str) {
        return patternExists(str, aeoPattern);
    }
    
    /**
     * Common anatomy reference ontology
     * @param str
     * @return 
     */
    public static boolean isCARO(String str) {
        return patternExists(str, caroPattern);
    }
     
    /**
     * Abstract human developmental anatomy
     * @param str
     * @return 
     */
    public static boolean isEHDAA2(String str) {
        return patternExists(str, ehdaa2Pattern);    
    }
    
    /**
     * carnegie system
     * @param str
     * @return 
     */
    public static boolean isCS(String str) {
        return patternExists(str, csPattern);
    }
    
    public static boolean isHumanDiseaseOntology(String str) {
        return patternExists(str, HDOPattern);
    }
    
    /**
     * homology ontology (HOM)
     * @return 
     */
    public static boolean isHOM(String str) {
        return patternExists(str, HOMPattern);
    }
    
    public static boolean isRNAO(String str) {
        return patternExists(str, RNAOPattern);
    }
    
    public static boolean isSymptom(String str) {
        return patternExists(str, symptomPattern);
    }
    
    public static boolean isPATO(String str) {
        return patternExists(str, patoIdPattern);
    }
    
    public static boolean isCytoplasm(String str) {
        return patternExists(str, cpIdPattern);
    }
    
    /**
     * 	"synonym" : "\"amino acid residue\" EXACT []"
     *  "synonym" : "\"protein residue\" NARROW [PRO:DAN]"
     * @param str
     * @param pattern is EXACT, NARROW
     * @return 
     */
    public static String getCleanSyn(String str, String pattern) {
        return str.split(pattern)[0].replaceAll("'\'", "").trim();
    }  
    
}