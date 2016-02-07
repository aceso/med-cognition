package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.CellTypeOntologyFields;
import org.atgc.bio.domain.*;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.ImportCollectionNames;
import org.neo4j.graphdb.NotFoundException;

/**
 * for chemical bioentities: ftp://ftp.ebi.ac.uk/pub/databases/chebi/ontology/ 
 * @author jtanisha-ee
 */
public class CellTypeOntologyImport {

    private static final Logger log = LogManager.getLogger(CellTypeOntologyImport.class);
    private static String cellIdPattern = "CL:";
    private static String goIdPattern = "GO:";
    private static String prIdPattern = "PR:";
    private static String modIdPattern = "MOD:";
    private static String exclamationPattern = "!";
    private static String spacePattern = " ";
    
     private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
   
    public static void main(String[] args) throws java.io.IOException, UnknownHostException, Exception {
        // gives CellTypeOntology documents whose importstatus is due. 
        
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
    }
    
    /**
     * 
     * @param id
     * @param result
     * @throws UnknownHostException 
     */
    public static void processOntologyDoc(String id, BasicDBObject result) throws UnknownHostException {
        if (OntologyStrUtil.isCellTypeOntology(id)) {
            log.info("cellTypeOntology =" + id);
            if (!StatusUtil.idExists(BioTypes.CELL_TYPE_ONTOLOGY.toString(), BioFields.CELL_TYPE_ONTOLOGY_ID.toString(), id)) {
                log.info("******* cellTypeOntologyId =" + id);
                try {
                    processCellTypeOntology(id, result);
                } catch (Exception e) {
                    throw new RuntimeException(e);
                }     
            }
         } else if (OntologyStrUtil.isGeneOntology(id)) {
            log.info("GoId = " + id);
            GeneOntologyObo.processOntologyDoc(id, result);
         } else if (OntologyStrUtil.isProteinOntology(id)) {
            ProteinOntologyImport.processOntologyDoc(id, result);
         }  if (OntologyStrUtil.isChebiOntology(id)) {
             ChebiOntologyImport.processOntologyDoc(id, result);
         } else if (OntologyStrUtil.isPATO(id)) {
             PhenotypicOntologyUtil.processOntologyDoc(id, result);
         } else if (OntologyStrUtil.isCytoplasm(id)) {
             log.info("cytoplasm id = " + id);
             CytoplasmOntologyUtil.processOntologyDoc(id, result);
         }
         // CP,
         //else if (OntologyStrUtil.isUberonOntology(id)) { 
    }
    
      
    /**
     * synonym: "primary cell culture cell" EXACT []
     * synonym: "primary cell line cell" RELATED []
     * synonym: "unpassaged cultured cell" EXACT []
     * synonym: "marrow stromal cells" NARROW [PMID:11378515]
     * synonym: "mesenchymal precursor cell" RELATED []
     * synonym: "mesenchymal progenitor cells" RELATED PLURAL [MESH:D044982]
     * synonym: "mesenchymal stem cell" RELATED []
     * synonym: "mesenchymal stromal cell" RELATED []
     * synonym: "mesenchymal stromal cells" RELATED PLURAL []
     * synonym: "MSC" RELATED [PMID:11378515]
     * synonym: "stem cells, mesenchymal" RELATED PLURAL [MESH:D044982]
     * @param list
     * @param enumField
     * @return 
     */
    public static String getSynonym(BasicDBList list, CellTypeOntologyFields enumField) {
        List synList = new ArrayList();
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
     * "synonym" : "\"primary cell culture cell\" EXACT []"
     * "synonym" : "\"primary cell line cell\" RELATED []
     *  "synonym" : "\"unpassaged cultured cell\" NARROW []"
     * @param str
     * @param pattern
     * @return 
     */
    /*
    private static String getCleanSyn(String str, String pattern) {
        return str.split(pattern)[0].replaceAll("'\'", "").trim();
    }
    * 
    */
    
    public static void getPubmedFromSynonymList(BasicDBObject dbObj, List pList) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.SYNONYM_LIST);
        for (Object obj : list) {
            String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.SYNONYM);
            extractPubMedId(str, pList);
        }
    }
    
    /**
     * synonym: "marrow stromal cells" NARROW [PMID:11378515]
     * synonym: "MSC" RELATED [PMID:11378515]
     * @param list
     * @param cellOnto 
     */
    
    public static void setPubmedRelationship(CellTypeOntology cellOnto, BasicDBObject dbObj, Subgraph subGraph) throws Exception {
         List<String> pList = getAllPubMeds(cellOnto, dbObj);  
         for (String pubMedId : pList) {
             log.info("pubmedId =" + pubMedId);
             PubMed pubMed = getPubMed(pubMedId, subGraph);
             if (pubMed != null) {
                 pubMed.setPubMedRelation(cellOnto);
             }
         }
    }
    
    /**
     * getPubMed
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
    
    public static List<String> getAllPubMeds(CellTypeOntology cellOnto, BasicDBObject dbObj) {
         List<String> pList = new ArrayList();
         if (OntologyStrUtil.objectExists(dbObj, CellTypeOntologyFields.COMMENT)) {
             String comment = getComment(dbObj);
             extractPubMedId(comment, pList);
         }
         
         if (OntologyStrUtil.objectExists(dbObj, CellTypeOntologyFields.SYNONYM_LIST)) {
            getPubmedFromSynonymList(dbObj, pList);
         }
         
         String def = getDef(dbObj);
         if (def != null) {
             extractPubMedId(def, pList);
         }  
         return pList;
    }
    
    public static void extractPubMedId(String val, List pList) {
        OntologyStrUtil.extractPubMedId(val, pList);
        /*
         String str[] = val.split("PMID:", 0);
         for (String s : str) { 
            String pmid = s.split(",")[0].split("]")[0].trim(); 
            if (pmid != null) {
                pList.add(pmid);
            }
            //log.info("pmid " + s.split(",")[0].split("]")[0].trim());
        } */
    }
    
    /**
     * 
     * setIsARelationship between CellTypeOntology <->CellTypeOntology
     * @param CellTypeOntology
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setIsARelationship(CellTypeOntology cellOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.IS_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.IS_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.IS_A);
                if (OntologyStrUtil.isCellTypeOntology(str)) {
                   setCellTypeIsARelationship(str, cellOnto, subGraph);
                } else if (OntologyStrUtil.isGeneOntology(str)) {
                   setGoIsARelationship(str, cellOnto, subGraph);
                }
            }
         }
    }
    
    /**
     *  disjoint_from: GO:0005575 ! cellular_component
     * "disjoint_from" : "CL:0000623 ! natural killer cell"
     * @param cellOnto
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
    public static void setDisjointRelationship(CellTypeOntology cellOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.DISJOINT_FROM_LIST)) {
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.DISJOINT_FROM_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.DISJOINT_FROM);
                if (OntologyStrUtil.isCellTypeOntology(str)) {
                   setCellTypeDisjointRelationship(str, cellOnto, subGraph);
                } else if (OntologyStrUtil.isGeneOntology(str)) {
                   setGoDisjointRelationship(str, cellOnto, subGraph);
                }
            }
         }
    }  
    
    /**
     * If BioRelationType is not specified, set BioRelTypes to EQUIVALENT_TERM
     * @param str
     * @param cellOnto
     * @param subGraph
     * @param relType
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static GeneOntology getGeneOntology(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
         String goId = getGoId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (goId != null) {
            return GeneOntologyObo.getGeneOntology(goId, subGraph);
        } else {
            return null;
        }
    }
    
    /**
     * 
     * setGoIsARelationship
     * @param str
     * @param cellOnto
     * @param subGraph
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static void setGoIsARelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        GeneOntology go = getGeneOntology(str, cellOnto, subGraph);
        cellOnto.setIsARelationship(go, BioRelTypes.IS_A);
    }
    
    /**
     * intersection_of: RO:0002215 GO:0050906 ! capable_of detection of stimulus involved in sensory perception
     * 
     * setGoIntersectionRelationship
     * @param str
     * @param cellOnto
     * @param subGraph
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static void setGoIntersectionRelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        GeneOntology go = getGeneOntology(str, cellOnto, subGraph);
        if (go != null) { 
            BioRelTypes relType = getBioRelationType(str, goIdPattern);
            if (relType != null) { 
                cellOnto.setIntersectionRelationship(go, relType);
            } else {
                log.info(" ###### relType is null for " + goIdPattern);
            }
        }
    }
    
    /**
     * relationship: RO:0002215 GO:0048137 ! capable_of spermatocyte division
     * relationship: RO:0002215 GO:0050906 ! capable_of detection of stimulus involved in sensory perception
     * @param str
     * @param cellOnto
     * @param subGraph
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static void setGoRelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        GeneOntology go = getGeneOntology(str, cellOnto, subGraph);
        if (go != null)  {
            BioRelTypes relType = getBioRelationType(str, goIdPattern);
            if (relType != null) { 
                cellOnto.setRelationship(go, relType);
            } else {
                log.info(" ###### relType is null for " + goIdPattern);
            }
        }
    }
    
    public static void setGoDisjointRelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        GeneOntology go = getGeneOntology(str, cellOnto, subGraph);
        if (go != null)  {
            BioRelTypes relType = getBioRelationType(str, goIdPattern);
            if (relType != null) { 
                cellOnto.setDisjointRelationship(go, relType);
            } else {
                log.info(" ###### relType is null for " + goIdPattern);
            }
        }
    }
    
    /**
     * getGoId
     * @param str
     * @return 
     */
    public static String getGoId(String str) {
        return OntologyStrUtil.getId(str, goIdPattern);
    }
    
    /**
     * 
     * intersection_of: CL:0000540 ! neuron
     * Relationship type is not mentioned in some intersections. so use the default
     * BioRelTypes.INTERSECTION_OF
     * relationship
     * setCellTypeIntersectionRelationship
     * @param str
     * @param cellOnto
     * @param subGraph
     * @param relType 
     */
    public static void setCellTypeIntersectionRelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, NotFoundException, IllegalAccessException, InvocationTargetException {
        String cellId = getCellTypeId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (cellId != null) {
            CellTypeOntology cellEntity = getCellTypeOntology(cellId, subGraph);
            if (cellEntity != null) {
                BioRelTypes relType = getBioRelationType(str, cellIdPattern);
                log.info("relType = " + relType);
                if (relType == null) {
                    relType = BioRelTypes.INTERSECTION_OF;
                }
                cellOnto.setIntersectionRelationship(cellEntity, relType);
            }
        }
    }
    
    
    /**
     *
     * relationship: develops_from CL:0000008 ! migratory cranial neural crest cell
     * @param str
     * @param cellOnto
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setCellTypeRelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        String cellId = getCellTypeId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (cellId != null) {
            CellTypeOntology cellEntity = getCellTypeOntology(cellId, subGraph);
            if (cellEntity != null) {
                BioRelTypes relType = getBioRelationType(str, cellIdPattern);
                if (relType != null) {
                    cellOnto.setRelationship(cellEntity, relType);
                }
            }
        }
    }
    
    /**
     * <pre>
     * 	"relationship" : "has_low_plasma_membrane_amount PR:000001412 ! CD86 molecule"
        "relationship" : "has_low_plasma_membrane_amount PR:000001438 ! CD80 molecule"
       </pre>
     * @param str
     * @param cellOnto
     * @param subGraph
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception 
     */
    public static void setProteinRelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws UnknownHostException, RuntimeException, Exception {
        ProteinOntology entity = ProteinOntologyImport.getProteinOntology(str, subGraph);
        if (entity != null)  {
            BioRelTypes relType = getBioRelationType(str, OntologyStrUtil.prIdPattern);
            if (relType != null) { 
                cellOnto.setRelationship(entity, relType);
            } else {
                log.info(" ###### relType is null for " + OntologyStrUtil.prIdPattern);
            }
        }
    }
    
    /**
     * is_a: CL:0000197 ! receptor cell
     * setCellTypeIsARelationship
     * @param str
     * @param cellOnto
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setCellTypeIsARelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        String cellId = getCellTypeId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (cellId != null) {
            CellTypeOntology cellEntity = getCellTypeOntology(cellId, subGraph);
            if (cellEntity != null) {
                cellOnto.setIsARelationship(cellEntity, BioRelTypes.IS_A);
            }
        }
    }
    
    public static void setCellTypeDisjointRelationship(String str, CellTypeOntology cellOnto, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException {
        String cellId = getCellTypeId(str);
        //log.info("id = " + goId + ", name = " + name);
        if (cellId != null) {
            CellTypeOntology cellEntity = getCellTypeOntology(cellId, subGraph);
            if (cellEntity != null) {
                BioRelTypes relType = getBioRelationType(str, cellIdPattern);
                if (relType != null) {
                   cellOnto.setDisjointRelationship(cellEntity, relType);
                }
            }
        }
    }
    
    
    /*
     * "consider" : "CL:0000000"
     * @param str
     * @return 
     */
    public static String getIdFromConsiderList(String str) {
        return str.trim();
    }
    
    /**
     * 
     * http://www.geneontology.org/GO.format.obo-1_2.shtml
     * This tag indicates that this term is equivalent to the intersection of several other terms. 
     * The value is either a term id, or a relationship type id, a space, and a term id. 
     * For example: this celltype is equivalent to any term that is "female germ cell"
     * 
     * "intersection_of" : "CL:0000021 ! female germ cell"
     * 
     * "intersection_of" : "participates_in GO:0007143
     * "intersection_of" : "CL:0000021 ! female germ cell"
     * "intersection_of" : develops_from CL:0000333 ! migratory neural crest cell
     * "intersection_of" : RO:0002215 GO:0017145 ! capable_of stem cell division
     * "intersection_of" : lacks_plasma_membrane_part PR:000001865 ! interleukin-3 receptor class 2 alpha chain
     * 
     * @param cellOnto
     * @param dbObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void setIntersectionRelationship(CellTypeOntology cellOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
         if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.INTERSECTION_OF_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.INTERSECTION_OF_LIST);
             for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.INTERSECTION_OF);
                if (str != null) { 
                    if (OntologyStrUtil.isCellTypeOntology(str)) {
                        setCellTypeIntersectionRelationship(str, cellOnto, subGraph);
                    } else {
                        if (OntologyStrUtil.isGeneOntology(str)) {
                            setGoIntersectionRelationship(str, cellOnto, subGraph);
                        }
                    }
                }
            }
         }
    }
    
    /**
     * 
     * @param cellOnto
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
    public static void setConsiderRelationship(CellTypeOntology cellOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException, URISyntaxException, UnknownHostException {
        if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.CONSIDER_LIST)) {
            BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.CONSIDER_LIST);
            for (Object obj : list) {
                String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.CONSIDER);
                String cellId = getIdFromConsiderList(str);
                if (cellId != null) {
                    CellTypeOntology cellEntity = getCellTypeOntology(cellId, subGraph);
                    if (cellEntity != null) {
                        cellOnto.setConsiderRelationships(cellEntity);
                    }
                }
            }
        }
    }
    
    
    /**
    * "CL:0000003 ! native cell"
    * first split:  0000003 ! native cell"
    * second split: 0000003
    */
    private static String getCellTypeId(String str) {
        return OntologyStrUtil.getId(str, cellIdPattern);
        //return "cellIdPattern" + str.split(cellIdPattern)[1].split(exclamationPattern)[0].trim();
    }
    
    /**
     * the relationship can be specified either in the beginning or the end of the string
     * eg: develops_from (beginning) or capable_of (after id) 
     *
     * "intersection_of" : develops_from CL:0000333 ! migratory neural crest cell
     * "intersection_of" : RO:0002215 GO:0017145 ! capable_of stem cell division
     * 
     *
     * @param str
     * @param pattern can be either GO: or CL:
     * @return 
     */
    private static BioRelTypes getBioRelationType(String str, String pattern) {
        return OntologyStrUtil.getRelationshipType(str, pattern);       
    }
    
    /**
     * setSynonyms
     * @param cellOnto
     * @param dbObj 
     */
    public static void setSynonyms(CellTypeOntology cellOnto, BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.SYNONYM_LIST);
        String synStr;
        if ((synStr = getSynonym(list, CellTypeOntologyFields.EXACT)) != null) {
            cellOnto.setCellTypeExactSynonyms(synStr);
        } 
        if ((synStr = getSynonym(list, CellTypeOntologyFields.RELATED)) != null) {
           cellOnto.setCellTypeRelatedSynonyms(synStr);
        }
        if ((synStr = getSynonym(list, CellTypeOntologyFields.NARROW)) != null) {
           cellOnto.setCellTypeNarrowSynonyms(synStr);
        }
    }
    
    public static String getBasicDBListAsString(BasicDBObject dbObj, Enum field, Enum objField) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, field);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            String val = OntologyStrUtil.getString((BasicDBObject)obj, objField);
            str.append(StrUtil.goodText(OntologyStrUtil.getString((BasicDBObject)obj, objField)));
            str.append(" ");   

        }
        return str.toString();  
    }
    
     /**
     * "namespace" : "cell",
     * @param dbObj
     * @return String
     */ 
    public static String getNamespace(BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.NAMESPACE_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.NAMESPACE));
            str.append(" ");       
        }
        return str.toString();
    }
    
    public static String getAlternateIds(BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.ALT_ID_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.ALT_ID));
            str.append(" ");       
        }
        return str.toString();
    }
    
    public static String getDef(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, CellTypeOntologyFields.DEF);
    }
   
    /**
     * "comment" : "This term was made obsolete because it refers to a class
     * of gene products and a biological process rather than a molecular function."
     * @param dbObj
     * @return 
     */
    public static String getComment(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, CellTypeOntologyFields.COMMENT);
    }
    
    public static String getIsObsolete(BasicDBObject obj) {
        return OntologyStrUtil.getString(obj, CellTypeOntologyFields.IS_OBSOLETE);
    }
    
    /**
     * "subsetList" : [
     *      {
     *          "subset" : "ubprop:upper_level"
     *      }
     *  ],
     * @param dbObj
     * @return 
     */
    public static String getSubsets(BasicDBObject dbObj) {
        BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.SUBSET_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.SUBSET));
            str.append(" ");       
        }
        return str.toString();
    }
    
    /**
     * 
     * @param ontologyId
     * @param SubGraph
     * @return 
     */
    public static CellTypeOntology processOntology(String ontologyId, BasicDBObject obj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, Exception {
         CellTypeOntology cellOnto = getCellTypeOntology(ontologyId, subGraph);
        
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.NAME)) { 
            cellOnto.setName(getName(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.ALT_ID_LIST)) { 
            cellOnto.setCellTypeAlternateIds(getAlternateIds(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.NAMESPACE_LIST)) {
            cellOnto.setCellTypeNameSpace(getNamespace(obj));
         }

         if (getDef(obj) != null) {
            cellOnto.setDef(getDef(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.COMMENT)) {
             cellOnto.setComment(getComment(obj));
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.SYNONYM_LIST)) {
             setSynonyms(cellOnto, obj);
         }
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.SUBSET_LIST)) {
             cellOnto.setCellTypeSubsets(getSubsets(obj));
         }
         
         
         if (OntologyStrUtil.objectExists(obj, CellTypeOntologyFields.IS_OBSOLETE)) {
             cellOnto.setCellTypeIsObsolete(getIsObsolete(obj));
         }
         
         
         setDisjointRelationship(cellOnto, obj, subGraph);
         setConsiderRelationship(cellOnto, obj, subGraph);
         setPubmedRelationship(cellOnto, obj, subGraph);
         setIsARelationship(cellOnto, obj, subGraph);
         setIntersectionRelationship(cellOnto, obj, subGraph);
         processRelationshipList(cellOnto, obj, subGraph);
        // setXRefRelationship(cellOnto, obj);  
         return cellOnto;
    }
    
    
    /**
     * /**
     * relationship types: part_of, is_a, regulates, etc
     * returns part_of
     * relationship: develops_from CL:0000670 ! primordial germ cell
     * relationship: RO:0002215 GO:0007067 ! capable_of mitosis
     * "relationship" : "has_low_plasma_membrane_amount PR:000001438 ! CD80 molecule"
     * @param str
     * @return {@link BioRelTypes} 
     */
    public static void processRelationshipList(CellTypeOntology cellOnto, BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, NotFoundException, IllegalAccessException, InvocationTargetException, UnknownHostException, RuntimeException, Exception {
        if (OntologyStrUtil.listExists(dbObj, CellTypeOntologyFields.RELATIONSHIP_LIST)) {
             //log.info("createIsARelationships()");
             BasicDBList list = (BasicDBList)OntologyStrUtil.getList(dbObj, CellTypeOntologyFields.RELATIONSHIP_LIST);
             for (Object obj : list) {
                 String str = OntologyStrUtil.getString((BasicDBObject)obj, CellTypeOntologyFields.RELATIONSHIP);
                 if (OntologyStrUtil.isCellTypeOntology(str)) {
                    setCellTypeRelationship(str, cellOnto, subGraph);
                 } else if (OntologyStrUtil.isGeneOntology(str)) {
                    setGoRelationship(str, cellOnto, subGraph);
                 } else if (OntologyStrUtil.isProteinOntology(str)) {
                    setProteinRelationship(str, cellOnto, subGraph);
                 }
             }
        }
    }
     
    
    
    /*
     * getName
     * 
     *  isList" : [
     *      {
     *      "is_a" : "CL:0000197 ! receptor cell"
     *      },
     *      {
     *      "is_a" : "CL:0000540 ! neuron"
     *      }
     *  ],
     *  
     * name: immortal cell line cell
     * getName
     * @param dbObj
     * @return String 
     */
    public static String getName(BasicDBObject dbObj) {
          return (String)dbObj.get(CellTypeOntologyFields.NAME.toString());
    }
    
    public static void processCellTypeOntology(String ontologyId, BasicDBObject result) throws Exception {
          Subgraph subGraph = new Subgraph();
          CellTypeOntology cellOntoEntity = processOntology(ontologyId, result, subGraph);
          RedbasinTemplate.saveSubgraph(subGraph);
    }
   
    
    /**
     * getCellTypeOntology
     * @param id
     * @param subGraph
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static CellTypeOntology getCellTypeOntology(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, NotFoundException, InvocationTargetException { 
           if (id != null) {
               log.info("getCellTypentology(), cellId =" + id);
               CellTypeOntology cellOnto = (CellTypeOntology)subGraph.search(BioTypes.CELL_TYPE_ONTOLOGY, BioFields.CELL_TYPE_ONTOLOGY_ID, id);
               if (cellOnto == null) {
                   cellOnto = new CellTypeOntology();
                   cellOnto.setCellTypeOntologyId(id);
                   subGraph.add(cellOnto);
               } 
               return cellOnto;
           } else {
               return null;
           } 
     }
    
     
    
}
        

