package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.OBOGeneOntologyFields;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.domain.*;
//import org.neo4j.graphdb.NotFoundException;

/**
 *
 * @author jtanisha-ee
 */
public class GeneOntologyObo {

    protected static Logger log = LogManager.getLogger(GeneOntologyObo.class);
    private static String goPattern = "GO:";
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
    
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
           DBCursor dbCursor = getCollection(ImportCollectionNames.GENE_ONTOLOGY).findDBCursor("{}" );
           try {
                // we expect only one document match
                while (dbCursor.hasNext()) {
                    BasicDBObject result = (BasicDBObject)dbCursor.next();
                    String ontologyId = getOntologyId(result);
                    processOntologyDoc(ontologyId, result);
                }
            } finally {
               dbCursor.close();
           }
    }

    /**
     * processOntologyDoc
     * @param ontologyId
     * @param result
     * @throws UnknownHostException 
     */
    public static void processOntologyDoc(String ontologyId, BasicDBObject result) throws UnknownHostException {
        if (ontologyId != null) {
           if (!StatusUtil.idExists(BioTypes.GENE_ONTOLOGY.toString(), OBOGeneOntologyFields.GENE_ONTOLOGY_OBO_ID.toString() , ontologyId)) {
                log.info("******* GeneOntologyId =" + ontologyId);
                try {
                    processOBOGeneOntology(ontologyId, result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }
           }
        }
    }
    
    
    /**
     * isPubMed
     * @param val
     * @return
     */
     public static boolean isPubMed(String val) {
        log.info("pubMed =" + val);
        return val.contains("[PMID:");
    }

    /**
     * [PMID:19620282, PMID:20976105]
     * @return
     */
     /*
    public static String getPubMedId(String str) {
            str.split
            return str.split("PMID:")[1].split(",")[].trim();
            //return enzymeVal.split("]")[0];
    }
    *
    */

    /**
     * [PMID: 1987933]
     * exact_synonym: "HIRA complex" [PMID:19620282, PMID:20976105]
     * def: [GOC:elh, GOC:mah, PMID:16303565, PMID:17180700]
     * @param val
     * @param pList 
     */
     
     /*
    public static void extractPubMedId(String val, List pList) {
         if (val != null) {
            if (val.contains("PMID:")) {
                String str1 = val.split("PMID:")[1];
                String str = str1.split("]")[0].trim();
                if (str.contains("PMID:")) {
                    stripPMID(str, pList);
                } else {
                    log.info("pmid =" + str);
                    pList.add(str);
                }
            }
         }
    }
    
    public static void stripPMID(String str, List pList) {
        if (str.contains("PMID:")) {
            String pmid = str.split(",")[0].trim();
            log.info("stripPMID() pmid =" + pmid);
            pList.add(pmid);
            stripPMID(str.split(",")[1], pList);
        } else {
            return; 
        }
    }
    */
     
   
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
     
    public static List<String> extractPubMedId(String val, List pList) {   
        if (val != null) {
            if (val.contains("PMID:")) {
                String vals[] = val.split("PMID:");
                for (String s3 : vals ) {
                    if (s3.contains("]")) {
                        String cs = removeHalfBracket(s3);
                        cs= removeComma(cs);
                        if (hasDigits(cs)) {
                            pList.add(cs);
                        }
                    } else if (s3.contains(",")) {
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
     *
     * exact_synonym: "HIRA complex" [PMID:19620282, PMID:20976105]
     * def: [GOC:elh, GOC:mah, PMID:16303565, PMID:17180700]
     * @param dbObj
     * @param field
     * @param subGraph
     */
    public static void setPubMedRelationship(GeneOntology geneOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, Exception {
        List<String> pList = getPubMedList(dbObj);
        if (!pList.isEmpty()) { 
            for (String pmid : pList) {
               if (pmid != null) { 
                    log.info("pmid = " + pmid);
                    //try { 
                        PubMed pubMed = getPubMed(pmid, subGraph);
                        if (pubMed != null) {
                            pubMed.setPubMedRelation(geneOnto);
                        }
                   // } 
               }
            }
        }  
    }
    
    /**
     * 
     * getPubMed
     * @param pubMedId
     * @param subGraph
     * @return PubMed {@link PubMed}
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws Exception 
     */
    public static PubMed getPubMed(String pubMedId, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, Exception {
          PubMed pubMed = (PubMed)subGraph.search(BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);
          if (pubMed == null) {
             pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
          }
          return pubMed;
    }
     
    /**
     * getPubMedList from all fields
     * @param dbObj
     * @return List<String> a list of pubmedid's
     */
    public static List<String> getPubMedList(BasicDBObject dbObj) {
        log.info("getPubMedList =");
         List pList = new ArrayList();
         String str = getDef(dbObj);
         if ( str!= null) {
             extractPubMedId(str, pList);
         }
         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.COMMENT)) {
            extractPubMedId(getComment(dbObj), pList);
         }

         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.EXACT_SYNONYM_LIST)) {
             extractPubMedId(getExactSynonyms(dbObj), pList);
         }
         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.BROAD_SYNONYM_LIST)) {
             extractPubMedId(getBroadSynonyms(dbObj), pList);
         }
         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.NARROW_SYNONYM_LIST)) {
             extractPubMedId(getNarrowSynonyms(dbObj), pList);
         }
         return pList;
    }


    /**
     *  getName
     *  "name" : "alpha-glucoside transport",
     * @param dbObj
     * @return
     */
    public static String getName(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, OBOGeneOntologyFields.NAME);
    }

    /**
     * "namespace" : "biological_process",
     * @param dbObj
     * @return
     */
    public static String getNamespace(BasicDBObject dbObj) {
       return OntologyStrUtil.getString(dbObj, OBOGeneOntologyFields.NAME_SPACE);
    }

    public static String getDef(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, OBOGeneOntologyFields.DEF);
    }

    /**
     * "comment" : "This term was made obsolete because it refers to a class
     * of gene products and a biological process rather than a molecular function."
     * @param dbObj
     * @return
     */
    public static String getComment(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, OBOGeneOntologyFields.COMMENT);
    }

    public static BasicDBList getConsiderList(BasicDBObject dbObj) {
        return OntologyStrUtil.getList(dbObj, OBOGeneOntologyFields.CONSIDER_LIST);
    }
    
     public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, OBOGeneOntologyFields.IS_OBSOLETE);
    } 

    /**
     * "subsetList" : [
     *   {
     *     "subset" : "goslim_generic"
     *   },
     *   {
     *     "subset" : "goslim_pir"
     *   },
     *   {
     *     "subset" : "goslim_plant"
     *   },
     *   {
     *      "subset" : "gosubset_prok"
     *    }
     * @param BasicDBObject
     * @param field
     * @return
     */
    public static String getSubsets(BasicDBObject dbObj, Enum field) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, OBOGeneOntologyFields.SUBSET_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, OBOGeneOntologyFields.SUBSET));
            str.append(" ");
        }
        return str.toString();
    }

    public static boolean isEnzyme(String val) {
        log.info("enzyme =" + val);
        return val.contains("[EC:");
    }

    /**
     * Input is [EC:3.2.1.108] output is 3.2.1.108
     * Enzyme Commission key [EC:3.2.1.108]
     * @param str
     * @return
     */
    public static String getEnzymeId(String str) {
            return str.split("EC:")[1].split("]")[0].trim();
            //return enzymeVal.split("]")[0];
    }

    public static String getEnzymeDesc(String str) {
        return str.split("\"")[1].split("\"")[0].trim();
    }

    /**
     * "exactSynonymList" : [
     *  {
     *      "exact_synonym" : "\"lactose galactohydrolase activity\" [EC:3.2.1.108]"
     *  }
     *  ],
     * EC refers to Enzyme commission
     * @param dbObj
     * @param field
     * @param objField
     * @return String from DBList
     */
    public static String getBasicDBListAsString(BasicDBObject dbObj, Enum field, Enum objField) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, field);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            String val = OntologyStrUtil.getString((BasicDBObject)obj, objField);
            if (!isEnzyme(val)) {
               str.append(StrUtil.goodText(OntologyStrUtil.getString((BasicDBObject)obj, objField)));
               str.append(" ");
            }
        }
        return str.toString();
    }

    /**
     * retrieve all EC numbers and description or name
     * @param dbObj
     * @param field
     * @param objField
     * @return
     */
    public static List<String> getEnzymeList(BasicDBObject dbObj, Enum field, Enum objField) {
         if (OntologyStrUtil.objectExists(dbObj, field)) {
            BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, field);
            if (list != null && list.size() > 0 ) {
                List<String> enzymeList = new ArrayList<String>();
                for (Object obj : list) {
                    String str = OntologyStrUtil.getString((BasicDBObject)obj, objField);
                    if (isEnzyme(str)) {
                        String id = getEnzymeId(str);
                        if (id != null) {
                            enzymeList.add(id);
                        }
                    }
                }
                return enzymeList;
            } else {
                return null;
            }
         } else {
            return null;
         }
    }



    /**
     * getEnzyme - fetches from EnzymeImport {@link Enzyme}
     * "exact_synonym" : "\"lactose galactohydrolase activity\" [EC:3.2.1.108]"
     * id: 3.2.1.108,  description = "lactose galactohydrolase activity
     * description - not added as sys_name as it is not exact match to Enzyme database
     * createEnzyme
     * @param id - {@link Enzyme#enzymeId}
     * @param subGraph
     * @return
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.net.URISyntaxException
     * @throws java.net.UnknownHostException
     */
    public static Enzyme getEnzyme(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException, RuntimeException, Exception {
         Enzyme enzyme = (Enzyme)subGraph.search(BioTypes.ENZYME, BioFields.ENZYME_ID, id);
         if (enzyme == null) {
              enzyme = EnzymeImport.getEnzyme(id, subGraph);
         }
         return enzyme;
    }


    /**
     * createEnzymeRel - creates enzyme relationship with {@link GeneOntology}
     * @param goEntity
     * @param list EnzymeList
     * @param relType
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnknownHostException
     */
    public static void createEnzymeRel(GeneOntology goEntity, List<String> list, BioRelTypes relType, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException, RuntimeException, Exception {
        for (Object key : list) {
            log.info("key =" + key);
            if (key != null) {
                Enzyme enzyme = getEnzyme((String)key, subGraph);
                if (enzyme != null) {
                    goEntity.setEnzymeRelationship(enzyme, relType);
                }
            }
        }
    }

    /**
     * Alternative words or phrases closely related in meaning to the term name,
     * with indication of the relationship between the name and synonym given by the
     * synonym scope.
     *  The scopes for GO synonyms are:  exact, broad, narrow, related
     *
     *  an exact equivalent; interchangeable with the term name
     *  e.g. ornithine cycle is an exact synonym of urea cycle
     *
     *  broad
     *  the synonym is broader than the term name
     *  e.g. cell division is a broad synonym of cytokinesis
     *
     *  narrow
     *  the synonym is narrower or more precise than the term name
     *  e.g. pyrimidine-dimer repair by photolyase is a narrow synonym of photoreactive repair
     *
     *  related
     *  the terms are related in some way not covered above
     *  e.g. cytochrome bc1 complex is a related synonym of ubiquinol-cytochrome-c reductase activity
     *  virulence is a related synonym of pathogenesis
     *  Custom synonym types are also used in the ontology.
     *
     *  For example, a number of synonyms are designated as systematic synonyms;
     *  synonyms of this type are exact synonyms of the term name.
     * @param goEntity
     * @param map
     * @param subGraph
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.net.UnknownHostException
     */
       public static void createEnzymeRelationships(GeneOntology goEntity, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException,InvocationTargetException, UnknownHostException, RuntimeException, Exception {
        if (OntologyStrUtil.listExists(dbObj, OBOGeneOntologyFields.EXACT_SYNONYM_LIST)) {
           List<String> enzymeList = getEnzymeList(dbObj, OBOGeneOntologyFields.EXACT_SYNONYM_LIST, OBOGeneOntologyFields.EXACT_SYNONYM);
           createEnzymeRel(goEntity, enzymeList, BioRelTypes.EXACT_SYNONYM_OR_INTERCHANGEABLE_WITH_TERM_NAME, subGraph);
        }

        if (OntologyStrUtil.listExists(dbObj, OBOGeneOntologyFields.BROAD_SYNONYM_LIST)) {
           List<String> enzymeList = getEnzymeList(dbObj, OBOGeneOntologyFields.BROAD_SYNONYM_LIST, OBOGeneOntologyFields.BROAD_SYNONYM);
           createEnzymeRel(goEntity, enzymeList, BioRelTypes.IS_BROADER_THAN_TERM_NAME, subGraph);
        }

        if (OntologyStrUtil.listExists(dbObj, OBOGeneOntologyFields.NARROW_SYNONYM_LIST)) {
           List<String> enzymeList = getEnzymeList(dbObj, OBOGeneOntologyFields.NARROW_SYNONYM_LIST, OBOGeneOntologyFields.NARROW_SYNONYM);
           createEnzymeRel(goEntity, enzymeList, BioRelTypes.IS_MORE_PRECISE_THAN_TERM_NAME, subGraph);
        }
     }


    /**
     * getExactSynonyms,  EC - enzyme commission
     * http://enzyme.expasy.org/enzyme_details.html
     *
     * "exactSynonymList" : [
     *  {
     *      "exact_synonym" : "\"lactose galactohydrolase activity\" [EC:3.2.1.108]"
     *  }
     *  ],
     * EC refers to Enzyme commission
     * @param map
     * @return String from DBList
     */
    public static String getExactSynonyms(BasicDBObject dbObj) {
        if (!OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.EXACT_SYNONYM_LIST)) {
             return getBasicDBListAsString(dbObj,
                OBOGeneOntologyFields.EXACT_SYNONYM_LIST,
                OBOGeneOntologyFields.EXACT_SYNONYM);
        } else {
            return null;
        }
    }

    /**
     * getBroadSynonyms  EC - Enzyme Commission
     * "broadSynonymList" : [
     *   {
     *    "broad_synonym" : "\"lactase-phlorizin hydrolase activity\" [EC:3.2.1.108]"
     *   }
     * @param map
     * @return String
     */
    public static String getBroadSynonyms(BasicDBObject dbObj) {
        return getBasicDBListAsString(dbObj,
                OBOGeneOntologyFields.BROAD_SYNONYM_LIST,
                OBOGeneOntologyFields.BROAD_SYNONYM);
    }

    /**
     * getNarrowSynonyms
     * @param map
     * @return String
     */
    public static String getNarrowSynonyms(BasicDBObject dbObj) {
        return getBasicDBListAsString(dbObj,
                OBOGeneOntologyFields.NARROW_SYNONYM_LIST,
                OBOGeneOntologyFields.NARROW_SYNONYM);
    }

    /**
     * getGeneOntology
     * @param goId
     * @param name
     * @param subGraph
     * @return GeneOntology
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.net.URISyntaxException
     * @throws java.net.UnknownHostException
     */
    public static GeneOntology createGeneOntologyLight(String goId, String name, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
        if (goId != null) {
           log.info("createGeneOntology(), goId =" + goId);
           GeneOntology geneOntology = (GeneOntology)subGraph.search(BioTypes.GENE_ONTOLOGY, BioFields.GENE_ONTOLOGY_ID, goId);
           if (geneOntology == null) {
                geneOntology = new GeneOntology();
                geneOntology.setGeneOntologyId(goId);
                if (name != null) {
                    geneOntology.setTermName(name);
                }
                subGraph.add(geneOntology);
                //PrimaryKey primaryKey = PrimaryKey.getPrimaryKey(geneOntology);
                //StatusUtil.idInsert(BioTypes.GENE_ONTOLOGY.toString(), primaryKey.getKey(), primaryKey.getValue());
           }
           return geneOntology;
        } else {
            return null;
        }
    }

    public static GeneOntology getGeneOntology(String goId, String name, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
        return createGeneOntologyLight(goId, name, subGraph);
    }

    /**
     * Relationship: "part_of GO:0007052 ! mitotic spindle organization"
     * disjoint_from : "GO:0005575 ! cellular_component"
     * is_a" : "GO:0006281 ! DNA repair"
     * getIdFromRelationship
     * @param str returns GO:0007052
     * @return gene ontology id
     */
    public static String getGoId(String str) {
        return OntologyStrUtil.getId(str, goPattern);
    }

    /**
     * relationship" : "part_of GO:0007052 ! mitotic spindle organization"
     * returns mitotic spindle organization
     *
     * "is_a" : "GO:0006281 ! DNA repair"
     * returns DNA repair
     *
     * getGOName
     * @param str
     * @return name
     */
    public static String getGOName(String str) {
        log.info("getGOName() " + str);
        return str.split("!")[1].trim();
    }

    /**
     * "relationship" : "part_of GO:0007052 ! mitotic spindle organization"
     * createGORelationships
     * http://www.geneontology.org/GO.ontology.relations.shtml
     * relationship: part_of, is_a, regulates, negatively regulates, positively regulates
     * "relationship" : "part_of GO:0007052 ! mitotic spindle organization"
     * @param geneOntology
     * @param map
     * @param subGraph
     * @throws java.lang.NoSuchFieldException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     * @throws java.net.URISyntaxException
     * @throws java.net.UnknownHostException
     */
    public static void createGORelationships(GeneOntology geneOntology, BasicDBObject map, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
         if (OntologyStrUtil.listExists(map, OBOGeneOntologyFields.RELATIONSHIP_LIST)) {
            BasicDBList list = OntologyStrUtil.getList(map,OBOGeneOntologyFields.RELATIONSHIP_LIST);
            HashMap<String, String> goMap = new HashMap<String, String>();
            for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, OBOGeneOntologyFields.RELATIONSHIP);
                //if (isEnzyme(str)) {
                    BioRelTypes rType = OntologyStrUtil.getRelationshipType(str, goPattern);
                    String goId = getGoId(str);
                    String name = getGOName(str);
                    log.info("createGORelationships(), name = " + name);
                    log.info("rType = " + rType);
                    log.info("createGORelationships() goId = " + goId);
                    if (goId != null) {
                        GeneOntology goEntity = getGeneOntology(goId, name, subGraph);
                        if (goEntity != null) {
                            geneOntology.setGoRelationship(goEntity, rType);
                        }
                    }
                //}
            }
         }
    }


    /**
     * "consider" : "GO:0003756"
     *  
     * @param str
     * @return
     */
    public static String getIdFromConsiderList(String str) {
        return str.trim();
    }



    /**
     * "isList" : [
     * {
     *     "is_a" : "GO:0006281 ! DNA repair"
     * }
     * ]
     * @param geneOntology
     * @param map
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws java.net.URISyntaxException
     * @throws java.net.UnknownHostException
     */
    public static void createIsARelationships(GeneOntology geneOntology, BasicDBObject map, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
         if (OntologyStrUtil.listExists(map, OBOGeneOntologyFields.IS_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(map, OBOGeneOntologyFields.IS_LIST);
             HashMap<String, String> goMap = new HashMap<String, String>();
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, OBOGeneOntologyFields.IS_A);
                String goId = getGoId(str);
                String name = getGOName(str);
                log.info("id = " + goId + ", name = " + name);
                if (goId != null) {
                    GeneOntology goEntity = getGeneOntology(goId, name, subGraph);
                    if (goEntity != null) {
                        geneOntology.setIsARelationship(goEntity, BioRelTypes.IS_A);
                    }
                }
            }
         }
    }

    /*
     *
     * "considerList" : [
		{
			"consider" : "GO:0003756"
		},
		{
			"consider" : "GO:0015036"
		}
	]

     * Current GO terms needs to be set as relations to obsolete GO term
     * createConsiderRelationship()
     * @param geneOntology
     * @param map
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     */
    public static void createConsiderRelationship(GeneOntology geneOntology, BasicDBObject map, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
        if (OntologyStrUtil.listExists(map, OBOGeneOntologyFields.CONSIDER_LIST)) {
            BasicDBList list = (BasicDBList)OntologyStrUtil.getList(map, OBOGeneOntologyFields.CONSIDER_LIST);
            HashMap<String, String> goMap = new HashMap<String, String>();
            for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, OBOGeneOntologyFields.CONSIDER);
                String goId = getIdFromConsiderList(str);
                if (goId != null) {
                    GeneOntology goEntity = getGeneOntology(goId, null, subGraph);
                    if (goEntity != null) {
                        geneOntology.setConsiderRelationships(goEntity);
                    }
                }
            }
        }
    }

    /**
     * 
     * "disjointList" : [
     *	{
     *      "disjoint_from" : "GO:0005575 ! cellular_component"
     *  },
     *  {
     *      "disjoint_from" : "GO:0008150 ! biological_process"
     *  }
     *  ]
     *
     * createDisjointRelationship
     * @param geneOntology
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnknownHostException 
     */
    public static void createDisjointRelationship(GeneOntology geneOntology, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
        if (OntologyStrUtil.listExists(dbObj, OBOGeneOntologyFields.DISJOINT_LIST)) {
            BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, OBOGeneOntologyFields.DISJOINT_LIST);
            HashMap<String, String> goMap = new HashMap<String, String>();
            for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, OBOGeneOntologyFields.DISJOINT_FROM);
                String goId = getGoId(str);
                if (goId != null) {
                    GeneOntology goEntity = getGeneOntology(goId, null, subGraph);
                    if (goEntity != null) {
                        geneOntology.setDisjointRelationship(goEntity);
                    }
                }
            }
        }
    }

    /**
     * createGeneOntology
     * @param id
     * @param dbObj
     * @param subGraph
     * @return GeneOntology {@link BioTypes#GENE_ONTOLOGY}
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException
     */
    public static GeneOntology createGeneOntology(String id, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception {
         GeneOntology geneOntology = (GeneOntology)subGraph.search(BioTypes.GENE_ONTOLOGY, BioFields.GENE_ONTOLOGY_OBO_ID, id);
         if (geneOntology == null) {
             geneOntology  = new GeneOntology();
             geneOntology.setGeneOntologyId(id);
             subGraph.add(geneOntology);
         }
         
         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.NAME)) {
            geneOntology.setName(getName(dbObj));
         }

         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.NAME_SPACE)) {
            geneOntology.setNamespace(getNamespace(dbObj));
         }

         if (getDef(dbObj) != null) {
            geneOntology.setDef(getDef(dbObj));
         }

         /** it's obsolete */
         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.COMMENT)) {
             geneOntology.setComment(getComment(dbObj));
         }

         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.IS_OBSOLETE)) {
             geneOntology.setIsObsolete(getIsObsolete(dbObj));
         }
       
         /* If not an enzyme, set below */
         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.EXACT_SYNONYM_LIST)) {
             geneOntology.setExactSynonyms(getExactSynonyms(dbObj));
         }
         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.BROAD_SYNONYM_LIST)) {
             geneOntology.setBroadSynonyms(getBroadSynonyms(dbObj));
         }
         if (OntologyStrUtil.objectExists(dbObj, OBOGeneOntologyFields.NARROW_SYNONYM_LIST)) {
             geneOntology.setNarrowSynonyms(getNarrowSynonyms(dbObj));
         }
         
         setPubMedRelationship(geneOntology, dbObj, subGraph);
         return geneOntology;
    }

    /**
     * {@link BioTypes#GENE_ONTOLOGY} {@link BioTypes#ENZYME}
     * processOBOGeneOntology
     * @param ontologyId
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception
     */
    public static void processOBOGeneOntology(String ontologyId, BasicDBObject result) throws UnknownHostException, RuntimeException, Exception {
            Subgraph subGraph = new Subgraph();
            fetchOBOGeneOntology(ontologyId, subGraph, result);
            //subGraph.traverse();
            RedbasinTemplate.saveSubgraph(subGraph);
    }

    public static GeneOntology fetchOBOGeneOntology(String ontologyId, Subgraph subGraph, BasicDBObject dbObj) throws UnknownHostException, RuntimeException, Exception {
          
           if (dbObj != null) {
               log.info("*** docu" + dbObj.toString());
               GeneOntology geneOntology = createGeneOntology(ontologyId, dbObj, subGraph);
               if (geneOntology != null) {
                    /* if these are Enzymes, set relationship */
                    createEnzymeRelationships(geneOntology, dbObj, subGraph);

                    /**
                    *   This GO term is part_of of GO:0007052
                    *	"relationshipList" : [
                    *      {
                    *      "relationship" : "part_of GO:0007052 ! mitotic spindle organization"
                    *      }
                    *  ]
                    *  }
                    */
                    createGORelationships(geneOntology, dbObj, subGraph);

                    /**
                     * isList
                     */
                    createIsARelationships(geneOntology, dbObj, subGraph);

                    /**
                     * considerList - refers to current GO terms when this GO term is obsolete
                     */
                    createConsiderRelationship(geneOntology, dbObj, subGraph);
                    
                    /**
                     * 
                     */
                    createDisjointRelationship(geneOntology, dbObj, subGraph);
                    return geneOntology;
              }
          } 
          return null;
          
      }
    
      public static String getOntologyId(BasicDBObject obj) {
          return (String)obj.get(OBOGeneOntologyFields.GENE_ONTOLOGY_OBO_ID.toString()); 
      }
      
      /**
       * fetchGeneOntology
       * @param ontologyId
       * @param subGraph
       * @return GeneOntology
       */
      public static GeneOntology fetchGeneOntology(String ontologyId, Subgraph subGraph) throws /* UnknownHostException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, */Exception {
            log.info("fetchGeneOntology()" + ontologyId);
            GeneOntology geneOntology = null;
            BasicDBObject basicDBObject = new BasicDBObject();
            basicDBObject.put(OBOGeneOntologyFields.GENE_ONTOLOGY_OBO_ID.toString(), ontologyId);
            DBCursor dbCursor = getCollection(ImportCollectionNames.GENE_ONTOLOGY).findDBCursor(basicDBObject);
            try {
                if (dbCursor.hasNext()) {
                    BasicDBObject result = (BasicDBObject)dbCursor.next();
                    String id = getOntologyId(result);
                    geneOntology = createGeneOntology(ontologyId, result, subGraph);
                } 
            } finally {
                dbCursor.close();
            }
            return geneOntology;
      }

      /**
       * returns geneOntology and subGraph
       * subGraph contains all bioEntities created by geneOntology
       * getGeneOntology
       * @param ontologyId
       * @param subGraph
       * @return
       * @throws UnknownHostException
       * @throws RuntimeException
       * @throws Exception
       */
      public static GeneOntology getGeneOntology(String ontologyId, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
          log.info("getGeneOntology()");
          return fetchGeneOntology(ontologyId, subGraph);
      }

}


