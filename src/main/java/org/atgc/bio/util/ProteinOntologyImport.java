package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.BioFields;
import org.atgc.bio.ProteinOntologyFields;
import org.atgc.bio.domain.*;
import org.atgc.bio.repository.PersistenceTemplate;
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
import org.neo4j.graphdb.NotFoundException;

/**
 * Using collection of proteinontology from pro.obo imports file.
 * Uses 
 * @author jtanisha-ee
 */
public class ProteinOntologyImport {
    
    protected static Logger log = LogManager.getLogger(ProteinOntologyImport.class);
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
        DBCursor dbCursor = getCollection(ImportCollectionNames.PROTEIN_ONTOLOGY).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String ontologyId = OntologyStrUtil.getString(result, ProteinOntologyFields.ID);
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
              if (!StatusUtil.idExists(BioTypes.PROTEIN_ONTOLOGY.toString(), BioFields.PROTEIN_ONTOLOGY_ID.toString(), id)) {
                    log.info("******* protein ontology id =" + id);
                    try {
                        processProteinOntology(id, result);
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    } 
              } 
          } else if (OntologyStrUtil.isNcbiTaxon(id)) {
                processOntologyDoc(id, result);
          } else if (OntologyStrUtil.isGeneOntology(id)) {
                processOntologyDoc(id, result);
          } 
              // chebi,  MOD, OBI, SO, 
          
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
    public static ProteinOntology getProteinOntology(String prId, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (prId != null) {
            log.info("getProteinOntology(),  prId =" + prId);
            ProteinOntology prOnto = (ProteinOntology)subGraph.search(BioTypes.PROTEIN_ONTOLOGY, BioFields.PROTEIN_ONTOLOGY_ID, prId);
            if (prOnto == null) {
                prOnto = new ProteinOntology();
                prOnto.setProteinOntologyId(prId);
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
         return OntologyStrUtil.getString(obj, ProteinOntologyFields.NAME);   
    }
    
    public static String getNameSpace(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj,ProteinOntologyFields.NAME_SPACE);
    } 
    
    public static String getAltId(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, ProteinOntologyFields.ALT_ID);
    }
    
    public static String getDef(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, ProteinOntologyFields.DEF);
    }
    
    public static String getComment(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinOntologyFields.COMMENT);
    }
    
    public static String getIsObsolete(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinOntologyFields.IS_OBSOLETE);
    }
    
    public static String getDisjoint(BasicDBObject dbObj) {
        
        return OntologyStrUtil.getString(dbObj, ProteinOntologyFields.DISJOINT_FROM);
    }
    
    public static String getConsider(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, ProteinOntologyFields.CONSIDER);
    }
    
    
    /**
     * "synonym" : "\"primary cell culture cell\" EXACT []"
     * "synonym" : "\"primary cell line cell\" RELATED []
     * "synonym" : "\"unpassaged cultured cell\" NARROW []"
     * @param str
     * @param pattern
     * @return 
     */
    private static String getCleanSyn(String str, String pattern) {
        return str.split(pattern)[0].replaceAll("'\'", "").trim();
    }
    
    /**
     * getPubMedFromDef
     * @param dbObj
     * @param pList 
     */
    private static void getPubMedFromDef(BasicDBObject dbObj, List<String> pList) {
        String str = getDef(dbObj);
        OntologyStrUtil.extractPubMedId(str, pList);
    }
    
    /**
     * getPubMedFromSynonymList
     * @param dbObj
     * @param pList 
     */
    public static void getPubmedFromSynonymList(BasicDBObject dbObj, List<String> pList) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinOntologyFields.SYNONYM_LIST);
        if (list != null && list.size() > 0) { 
            for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinOntologyFields.SYNONYM);
                OntologyStrUtil.extractPubMedId(str, pList);
            }
        }
    }
    
    /**
     * createPubMedRelationship
     * @param prOnto
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
    public static void createPubMedRelationship(ProteinOntology prOnto, BasicDBObject dbObj,  Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, UnknownHostException, NotFoundException, InvocationTargetException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, HttpException, InterruptedException, Exception {
        List<String> pList = new ArrayList();
        getPubmedFromSynonymList(dbObj, pList);
        getPubMedFromDef(dbObj, pList);
        for (String pubMedId : pList) {
            PubMed pubMed = (PubMed)subGraph.search(BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);
            if (pubMed == null) { 
                log.info("getPubMed(), pubMedId =" + pubMedId);
                pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
                if (pubMed != null) {
                    pubMed.setPubMedRelation(prOnto);
                }
            }
        }
    }

    /**
     * extractProteinForDisjoint
     * @param str
     * @param pList 
     */
    public static void extractProteinForDisjoint(String str, List pList) {
        String prId = getId(str, OntologyStrUtil.prIdPattern);    
        if (prId != null) {
            pList.add(prId);
        }
    }
    
    /**
     * getDisjointFromList
     * @param dbObj
     * @return 
     */
    public static List<String> getDisjointFromList(BasicDBObject dbObj) {
        List<String> pList = new ArrayList();
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinOntologyFields.DISJOINT_FROM_LIST);
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinOntologyFields.DISJOINT_FROM);
            extractProteinForDisjoint(str, pList);
        }
        return pList;
    }
    
    /**
     * setDisjointRelationship
     * @param prOnto
     * @param dbObj
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setDisjointRelationship(ProteinOntology prOnto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
           List<String> pList = getDisjointFromList(dbObj);
           for (String str : pList) {
               setProteinDisjointRelationship(str, prOnto, subGraph);
           }
    }
    
    /**
     * setProteinDisjointRelationship
     * @param str
     * @param prOnto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setProteinDisjointRelationship(String str, ProteinOntology prOnto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String prId = getId(str, OntologyStrUtil.prIdPattern);
        //log.info("id = " + goId + ", name = " + name);
        if (prId != null) {
            ProteinOntology entity = getProteinOntology(prId, subGraph);
            if (entity != null) {
                prOnto.setDisjointRelationship(entity, BioRelTypes.DISJOINT_FROM);
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
    public static String getSynonym(BasicDBList list, ProteinOntologyFields enumField) {
        List synList = new ArrayList();
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinOntologyFields.SYNONYM);
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
     * setSynonyms
     * @param cellOnto
     * @param obj 
     */
    public static void setSynonyms(ProteinOntology prOnto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, ProteinOntologyFields.EXACT)) != null) {
            prOnto.setProteinOntologyExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, ProteinOntologyFields.RELATED)) != null) {
           prOnto.setProteinOntologyRelatedSynonyms(synStr);
        }
        if ((synStr = getSynonym(list, ProteinOntologyFields.NARROW)) != null) {
           prOnto.setProteinOntologyNarrowSynonyms(synStr);
        }
    }
    
     /**
     * 
     * "is_a" : "PR:000007517 ! FH1/FH2 domain-containing protein 3"
     * @param str
     * @return
     */
    public static String getProteinIdFromIsList(String str) {
        return OntologyStrUtil.getId(str, OntologyStrUtil.prIdPattern);
    }
 
    /**
     * "is_a" : "GO:0016592 ! mediator complex"
     */
    
    public static String getGoId(String str) {
        return OntologyStrUtil.getId(str, OntologyStrUtil.goIdPattern);
    }
   
    
    /**
     * getId - get identifier
     * @param str inputs are :
     *                      "is_a" : "PR:000007517 ! FH1/FH2 domain-containing protein 3"
     *                      relationship: only_in_taxon NCBITaxon:9606 ! Homo sapiens
     *                      relationship: lacks_part SO:0001062 ! propeptide
     * @param pattern
     * @return PR:000007517, NCBITaxon:9606, SO:0001062 
     */
    public static String getId(String str, String pattern) {
         return OntologyStrUtil.getId(str, pattern);
    }
    
    /**
     * setProteinRelationship
     * @param str
     * @param prOnto
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setProteinRelationship(String str, ProteinOntology prOnto, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String prId = getId(str, OntologyStrUtil.prIdPattern);
        //log.info("id = " + goId + ", name = " + name);
        if (prId != null) {
            ProteinOntology entity = getProteinOntology(prId, subGraph);
            if (entity != null) {
                BioRelTypes relType = getRelationshipType(str, OntologyStrUtil.prIdPattern);
                if (relType == null) {
                    //relType = BioRelTypes.AN_EQUIVALENT_TERM;
                    log.info("relType is null, " + str);
                }
                prOnto.setProteinOntologyRelationship(entity, relType);
            }
        }
     }

    /**
     * setProteinIsARelationship
     * @param str
     * @param prOnto
     * @param subGraph
     * @param relType
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException 
     */
    public static void setProteinIsARelationship(String str, ProteinOntology prOnto, Subgraph subGraph, BioRelTypes relType) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String prId = getProteinIdFromIsList(str);
        //log.info("id = " + goId + ", name = " + name);
        if (prId != null) {
            ProteinOntology entity = getProteinOntology(prId, subGraph);
            if (entity != null) {
                if (relType == null) {
                    //relType = BioRelTypes.AN_EQUIVALENT_TERM;
                    log.info("relType is null, " + str);
                }
                prOnto.setIsARelationship(entity, relType);
            }
        }
    }
    
    /**
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
    public static void setIsARelationship(ProteinOntology prOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, ProteinOntologyFields.IS_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinOntologyFields.IS_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinOntologyFields.IS_A);
                if (OntologyStrUtil.isProteinOntology(str)) {
                   setProteinIsARelationship(str, prOnto, subGraph, BioRelTypes.IS_A);
                } else if (OntologyStrUtil.isGeneOntology(str)) {
                   setGoIsARelationship(str,prOnto, subGraph, BioRelTypes.IS_A);
                }
                /*
                 * proteinmodifier (MOD)
                } else if (isProteinModifier)
                * 
                */
            }
         }
    }
    
     /**
      *setGoIsARelationship
     * If BioRelationType is not specified, set BioRelTypes to EQUIVALENT_TERM
     * @param str
     * @param proOnto
     * @param subGraph
     * @param relType
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static void setGoIsARelationship(String str, ProteinOntology prOnto, Subgraph subGraph, BioRelTypes relType) throws UnknownHostException, RuntimeException, Exception {
         String goId = getGoId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (goId != null) {
            GeneOntology goEntity = GeneOntologyObo.getGeneOntology(goId, subGraph);
            if (goEntity != null) {
                if (relType == null) {
                    log.info("relType is null, " + str);
                }
                prOnto.setIsARelationship(goEntity, relType);
            }
        }
    }
    
    /**
     * "relationship" : "has_part PR:000028879 ! mediator of RNA polymerase II transcription subunit 15 isoform 1 (mouse)"
     *  relationship: only_in_taxon NCBITaxon:9606 ! Homo sapiens
     * @param str
     * @return {@link BioRelTypes}
     */
    public static BioRelTypes getRelationshipType(String str, String pattern) {
        //log.info("GOterm relationship type =" + str.split("G")[0]);
        String str1 = str.split(pattern)[0].toUpperCase().trim();
        return BioRelTypes.getEnum(str1);
    }
    
    
  /*  
    intersection_of: PR:000000048 ! TGF-beta receptor type-2 isoform 1
intersection_of: has_part PR:000021935 ! immature protein part
intersection_of: lacks_part PR:000025513 ! modified amino-acid residue
disjoint_from: PR:000018264 ! proteolytic cleavage product
*/

    /**
     * "relationshipList" : [
     *          {
     *          "relationship" : "has_part PR:000028879 ! mediator of RNA polymerase II transcription subunit 15 isoform 1 (mouse)"
     *          },
     *          {
     *          "relationship" : "has_part PR:000028880 ! mediator of RNA polymerase II transcription subunit 28 isoform 1 (mouse)"
     *          },
     *          relationship: only_in_taxon NCBITaxon:9606 ! Homo sapiens
     *          relationship: only_in_taxon NCBITaxon:10090 ! Mus musculus
     *          relationship: lacks_part SO:0001062 ! propeptide
     *          relationship: has_part MOD:00693 ! glycosylated residue
     *
     */
     public static void setRelationshipList(ProteinOntology prOnto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception {
         if (OntologyStrUtil.listExists(dbObj, ProteinOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinOntologyFields.RELATIONSHIP);
                 if (OntologyStrUtil.isNcbiTaxon(str)) {
                    setNcbiTaxonRelationship(prOnto, subGraph, str);
                 } else if (OntologyStrUtil.isProteinOntology(str)) {
                    setProteinRelationship(str, prOnto, subGraph);
                 } 
                 // MOD (protein modifier)
             }
         }
     }
     
     public static NcbiTaxonomy getNcbiTaxon(String str, Subgraph subGraph) throws Exception {
         String id = getId(str, OntologyStrUtil.NCBITaxonPattern); 
         log.info("setRelationshipList = " + id);
         if (id != null) {
             return GeneGraphDBImportUtil.getNcbiTaxonomy(subGraph, id); 
         } else {
             return null;
         }
     }
    
     /**
      * setNcbiTaxonRelationship
      * only_on_taxon ==> {@link BioRelTypes#IN_ORGANISM} 
      * relationship: only_in_taxon NCBITaxon:10090 ! Mus musculus
      * setNcbiTaxonRelationship 
      * @param prOnto
      * @param subGraph
      * @param str 
      */
     public static void setNcbiTaxonRelationship(ProteinOntology prOnto, Subgraph subGraph, String str) throws Exception {
            BioRelTypes rType = BioRelTypes.IN_ORGANISM;
            NcbiTaxonomy ncbiTaxOn = getNcbiTaxon(str, subGraph);
            if (ncbiTaxOn != null) {
                prOnto.setProteinOntologyRelationship(ncbiTaxOn, BioRelTypes.IN_ORGANISM);
            }
     }
     
     
     /**
      * "intersection_of" : "only_in_taxon NCBITaxon:9606 ! Homo sapiens"
      * @param prOnto
      * @param subGraph
      * @param str
      * @throws Exception 
      */
     public static void setNcbiTaxonIntersectionRelationship(ProteinOntology prOnto, Subgraph subGraph, String str) throws Exception {
            BioRelTypes rType = getRelationshipType(str, OntologyStrUtil.NCBITaxonPattern);
            NcbiTaxonomy ncbiTaxOn = getNcbiTaxon(str, subGraph);
            if (ncbiTaxOn != null) {
                prOnto.setIntersectionRelationship(ncbiTaxOn, BioRelTypes.IN_ORGANISM);
            }
     }
     
     /**
      * setProteinIntersectionRelationship
      * @param prOnto
      * @param subGraph
      * @param str 
      */
     public static void setProteinIntersectionRelationship(ProteinOntology prOnto, Subgraph subGraph, String str) throws NotFoundException, IllegalArgumentException, NoSuchFieldException, IllegalAccessException, InvocationTargetException {
            String prId = getProteinIdFromIsList(str);
            //log.info("id = " + goId + ", name = " + name);
            if (prId != null) {
                ProteinOntology entity = getProteinOntology(prId, subGraph);
                if (entity != null) {
                    BioRelTypes relType = getRelationshipType(str, OntologyStrUtil.prIdPattern);
                    if (relType == null) {
                        //relType = BioRelTypes.AN_EQUIVALENT_TERM;
                        log.info("relType is null, " + str);
                    }
                    prOnto.setIntersectionRelationship(entity, relType);
                }
            }
     }
     
     /**
      * 
      * "intersection_of" : "PR:000025776 ! CD3 epsilon isoform 1, signal peptide removed phosphorylated 1"
      * "intersection_of" : "only_in_taxon NCBITaxon:9606 ! Homo sapiens"
      *  intersection_of: has_part MOD:00693 ! glycosylated residue
      */
     public static void setIntersectionList(ProteinOntology prOnto, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, Exception {
         if (OntologyStrUtil.listExists(dbObj, ProteinOntologyFields.INTERSECTION_OF_LIST)) {
             //log.info("createIsARelationship()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinOntologyFields.INTERSECTION_OF_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinOntologyFields.INTERSECTION_OF);
                 if (OntologyStrUtil.isNcbiTaxon( str)) {
                    setNcbiTaxonIntersectionRelationship(prOnto, subGraph, str);
                 } else if (OntologyStrUtil.isProteinOntology(str)) {
                    setProteinIntersectionRelationship(prOnto, subGraph, str);
                 } 
                 // MOD (protein modifier)
             }
         }
     }
     
     /**
      * "consider" : "CL:0000000
      * @param str
      * @return 
      */
     public static String getIdFromConsiderList(String str) {
         return OntologyStrUtil.getIdFromConsiderList(str);
     }
     
     public static void setConsiderRelationship(ProteinOntology prOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, URISyntaxException, UnknownHostException {
        if (OntologyStrUtil.listExists(dbObj, ProteinOntologyFields.CONSIDER_LIST)) {
            BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, ProteinOntologyFields.CONSIDER_LIST);
            for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, ProteinOntologyFields.CONSIDER);
                String id = getIdFromConsiderList(str);
                if (id != null) {
                    ProteinOntology prEntity = getProteinOntology(id, subGraph);
                    if (prEntity != null) {
                        prOnto.setConsiderRelationship(prEntity);
                    }
                }
            }
        }
    }
     
    /**
     * processProteinOntology
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
     public static void processProteinOntology(String ontologyId, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         Subgraph subGraph = new Subgraph();
         ProteinOntology prOnto = getProteinOntology(ontologyId, subGraph);
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.NAME)) { 
            prOnto.setProteinOntologyName(getName(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.ALT_ID)) {
            prOnto.setProteinOntologyAltId(getAltId(obj));
         }
         
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.NAME_SPACE)) {
            prOnto.setProteinOntologyNameSpace(getNameSpace(obj));
         }

         if (getDef(obj) != null) {
            prOnto.setDef(getDef(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.COMMENT)) {
             prOnto.setComment(getComment(obj));
         }
      
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.SYNONYM_LIST)) {
             setSynonyms(prOnto, obj);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.IS_LIST)) {
             setIsARelationship(prOnto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.RELATIONSHIP_LIST)) {
            setRelationshipList(prOnto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.INTERSECTION_OF_LIST)) {
            setIntersectionList(prOnto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.DISJOINT_FROM_LIST)) {
             setDisjointRelationship(prOnto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.CONSIDER)) {
             setConsiderRelationship(prOnto, obj, subGraph);
         }
         
         if (OntologyStrUtil.objectExists(obj, ProteinOntologyFields.IS_OBSOLETE)) {
             prOnto.setProteinOntologyIsObsolete(getIsObsolete(obj));
         }
         
         createPubMedRelationship(prOnto, obj, subGraph);
         PersistenceTemplate.saveSubgraph(subGraph);
     }
    
}
        

