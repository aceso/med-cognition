/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.atgc.bio.*;
import org.atgc.bio.domain.*;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.EndNode;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.RelQueue;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoObjects;
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
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;


/**
 * This is the National Cancer Index pathways import module. It can process a list of
 * pathway ShortName(s) mentioned in a mongo list collection called NCIPathwayList
 * and import the corresponding XML files on hotdiary.com into Mongo, and then
 * subsequently load them into Neo4J. It updates the status of pathways to DONE when
 * the pathways are imported (or merged) into Neo4J successfully. Otherwise, it marks 
 * them as ERROR in the Mongo NCIPathwayList as then they will not be repeatedly imported
 * in future if there is an error.
 * 
 * 
 * @author jtanisha-ee
 */


/**
 * http://www.ncbi.nlm.nih.gov/pmc/articles/PMC2686461/
 * The Pathway Interaction Database (PID, http://pid.nci.nih.gov) is a growing collection of 
 * human signaling and regulatory pathways curated from peer-reviewed literature and stored 
 * in a computable format. PID was designed to deal with two issues affecting the representation
 * of biological processes: the arbitrariness of pathway boundaries and the need to 
 * capture knowledge at different levels of detail. 
 * 
 * The Pathway Interaction Database (PID, http://pid.nci.nih.gov) is a growing collection of
 * human signaling and regulatory pathways curated from peer-reviewed literature and stored
 * in a computable format. PID was designed to deal with two issues affecting the representation of
 * biological processes: the arbitrariness of pathway boundaries and the need to capture knowledge
 * at different levels of detail. 
*/ 

/**
 * No restrictions to use
 * PID has adopted a network-level representation, similar to Reactome (1), HumanCyc (2) and KEGG (3). Like Reactome and HumanCyc, 
 * PID annotates interactions with citations to the literature. 
 * 
 * PID differs from Reactome, HumanCyc and KEGG in its focus on signaling and regulatory pathways; 
 * it does not attempt to cover metabolic processes or generic mechanisms like transcription 
 * and translation (see Table 1 for a comparison of PID with other publicly accessible pathway databases). PID contains only structured data and it links to but does not reproduce molecular information readily available from other sources, such as nucleotide or amino acid sequence, molecular weight and chemical formula. The principal source of data in PID is the highly curated ?NCI-Nature Curated? collection of pathways, but PID also includes two other sources of data: data imported into the PID data model from Reactome's Biological Pathways Exchange (BioPAX) Level 2 (4) export, and an import of information from the BioCarta collection of pathways (Table 2). All data in PID is freely available, without restriction on use. Bulk downloads are available in BioPAX Level 2, a standard format for exchanging pathway information, and a PID-specific XML format at http://pid.nci.nih.gov/PID/download.shtml. PID has adopted a network-level representation, similar to Reactome (1), HumanCyc (2) and KEGG (3). Like Reactome and HumanCyc, PID annotates interactions with citations to the literature. PID differs from Reactome, HumanCyc and KEGG in its focus on signaling and regulatory pathways; it does not attempt to cover metabolic processes or generic mechanisms like transcription and translation (see Table 1 for a comparison of PID with other publicly accessible pathway databases). PID contains only structured data and it links to but does not reproduce molecular information readily available from other sources, such as nucleotide or amino acid sequence, molecular weight and chemical formula. The principal source of data in PID is the highly curated ?NCI-Nature Curated? collection of pathways, but PID also includes two other sources of data: data imported into the PID data model from Reactome's Biological Pathways Exchange (BioPAX) Level 2 (4) export, and an import of information from the BioCarta collection of pathways (Table 2). All data in PID is freely available, without restriction on use. Bulk downloads are available in BioPAX Level 2, a standard format for exchanging pathway information, 
 * and a PID-specific XML format at http://pid.nci.nih.gov/PID/download.shtml. 
 * 
 * @author jtanisha-ee
 */
@SuppressWarnings("javadoc")
public class NCIPathwayNeo4JImport {
    
   // public static final String DB_URL = "http://saibaba.local:7474/db/data";
   
    //private static final RedbasinTemplate template = new RedbasinTemplate();
    private static final NciPathwayTemplate npt = new NciPathwayTemplate();
    private static final Map<String, GeneOntology> locationMap = new HashMap<>();
    private static final Map<String, GeneOntology> processMap = new HashMap<>();
    private static final Map<String, GeneOntology> functionMap = new HashMap<>();
    // private static ArrayList<RelQueue> foundList, notFoundList;  
    protected static Logger log = LogManager.getLogger(NCIPathwayNeo4JImport.class);
    private static ArrayList <RelQueue>relGraph;
    
    /**
     * 
     * @param args
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws NoSuchMethodException
     * @throws InvocationTargetException 
     */  
    public static void main(String[] args) throws java.io.IOException, java.net.URISyntaxException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException, HttpException, NoSuchFieldException, RuntimeException, InterruptedException {
        List<Map> pathwayList = NCIPathwayUtil.getPathwayList(); // return all that are DUE
        Iterator<Map> pathwayIter = pathwayList.iterator();
        NCIPathwayNeo4JImport nciPathwayNeo4JImport = new NCIPathwayNeo4JImport();
        //foundList = new <RelQueue>ArrayList();
        //notFoundList = new <RelQueue>ArrayList();
        while (pathwayIter.hasNext()) {
            Map map = pathwayIter.next(); 
            String shortName = (String)map.get(getStringFromEnum(NciPathwayFields.SHORT_NAME));
            log.info("********** shortName " + shortName);
            try {
                nciPathwayNeo4JImport.createPathwayNode(shortName);
            } catch (java.io.IOException e) {
                throw new RuntimeException(e);
            }
            log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
            log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
    }
    
    private static BioTypes getBioType(String moleculeType) {
        if (moleculeType.equalsIgnoreCase(BioTypes.COMPLEX.toString())) {
            return BioTypes.COMPLEX;
        } else if (moleculeType.equalsIgnoreCase(BioTypes.COMPOUND.toString())) {
            return BioTypes.COMPOUND;
        } else if (moleculeType.equalsIgnoreCase(BioTypes.PROTEIN.toString())) {
            return BioTypes.PROTEIN;
        } else if (moleculeType.equalsIgnoreCase(BioTypes.RNA.toString())) {
            return BioTypes.RNA;
        }
        return null;
    }
    
    /**
     * Creates Protein BioEntity
     * Creates Protein Node and its relations
     * @param molecule
     * @return None
     */
    public static void createProtein(BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException, InvocationTargetException, ServiceException, InterruptedException {
        log.info("createProtein(BasicDBObject, subGraph)");
        String proteinId = getUniProtIdentifier(molecule);
        getId(molecule);
        getBioEntityFromBioType(subGraph, BioTypes.PROTEIN, BioFields.UNIPROT_ID, proteinId);
        Protein protein = UniprotUtil.getProtein(proteinId, subGraph);
        /** 
         * important to set moleculeIdRef as this is used to search protein in
         * a subgraph
         * 
         */
        String moleculeId = getId(molecule);
        if (null != moleculeId && null != protein)
           protein.setMoleculeIdRef(moleculeId);
        
        
         /*
             * we will have to revisit this code for PartProtein
             */
           // String preferredSymbol = getPreferredSymbol(molecule);
           // bio = getBioEntityFromBioType(subGraph, BioTypes.BIO_MOLECULE, BioFields.ID_REF, getId(molecule));
           // log.info("Found protein as part protein " + getId(molecule));
        /*
         * 
        bolean created = false;
        Protein protein = (Protein)bio;
        if (protein == null) {
           
            if (bio == null) {
                protein = new Protein();
                created = true;
            } 
        }
        
        setProteinNames(molecule, protein);
        protein.setMoleculeIdRef(getId(molecule));
        protein.setMessage(proteinId);
        protein.setNodeType(BioTypes.PROTEIN);
 
        if (ptmExists(molecule)) {
           setProteinPTMValues(protein, molecule, subGraph);
        }
        
        setLabel(molecule, (Object)protein);
        
        if (created) {
            subGraph.add(protein);
        } 
        */
    }
    
        /**
     * Creates Protein BioEntity
     * Creates Protein Node and its relations
     * @param molecule
     * @return None
     */
    public static void createPartProtein(BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, UnsupportedEncodingException, URISyntaxException, MalformedURLException, IOException, UnknownHostException, HttpException, NoSuchFieldException, InvocationTargetException {
        log.info("createPartProtein(BasicDBObject, subGraph)");
        String preferredSymbol = getProteinPreferredSymbol(molecule);
        Object bio = getBioEntity(subGraph, BioTypes.PART_PROTEIN, BioFields.PART_PROTEIN_PREFERRED_SYMBOL, preferredSymbol, getId(molecule));
        boolean created = false;
        PartProtein partProtein = (PartProtein)bio;
        if (partProtein == null) {
            /* 
             * Because of late binding, we update Protein to PartProtein
             */
            // bio = getBioEntityFromBioType(subGraph, BioTypes.BIO_MOLECULE, BioFields.ID_REF, getId(molecule));
            //if (bio == null) {
            
            partProtein = new PartProtein();
            log.info("partProtein() is created");
            created = true;   
            
        } 
        
        setPartProteinNames(molecule, partProtein);
        partProtein.setMoleculeIdRef(getId(molecule));
        partProtein.setMessage(preferredSymbol);
        partProtein.setNodeType(BioTypes.PART_PROTEIN);
        setPartValues(partProtein, molecule, subGraph);    
        if (ptmExists(molecule)) {
            setPartProteinPTMValues(partProtein, molecule, subGraph);
        }
        
        /**
         * Label properties are set for a given protein
         */
        setLabel(molecule, (Object)partProtein);
        /**
         * Add it to subgraph only when this protein does not exist
         */
        if (created) {
            subGraph.add(partProtein);
          //  Protein sample = (Protein)subGraph.search(BioTypes.PROTEIN, BioFields.UNIPROT_ID, proteinId);
           // sample = (Protein)subGraph.search(BioFields.MOLECULE_IDREF, getId(molecule));
        }     
    }

    
  
   /**
    * sets the protein names including 
    * {@link BioFields#UNIPROT_ID} {@link BioFields#PROTEIN_PREFERRED_SYMBOL} and alias
    * @param molecule
    * @param protein 
    */
   private static void setProteinNames(BasicDBObject molecule, Protein protein) {
       log.info("setProteinNames()");
        BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME);  
        if (dbList != null) {
            for (Object obj : dbList) { 
                BasicDBObject dbObj = (BasicDBObject)obj;
                if (isPreferredSymbol(dbObj)) {
                    log.info("settingPreferredSymbol");
                    protein.setPreferredSymbol(getPreferredSymbol(dbObj));
                    protein.setMessage(getPreferredSymbol(dbObj));
                } else if (isUniProt(dbObj)) {
                    log.info("setting UniProt");
                    protein.setUniprot(getUniProtId(dbObj));
                } else if (isAlias(dbObj)) {
                    protein.setAliases(getAlias(dbObj));
                }
            }
        }
   }
   
   /**
    * 
    * @param molecule 
    */
   private static String getProteinPreferredSymbol(BasicDBObject molecule) {
        BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME);  
        if (dbList != null) {
            for (Object obj : dbList) { 
                BasicDBObject dbObj = (BasicDBObject)obj;
                if (isPreferredSymbol(dbObj)) {
                    log.info("gettingPreferredSymbol");
                    return getPreferredSymbol(dbObj);
                } 
            }
        } 
        return null;
   }
   
    /**
    * sets the protein names including 
    * {@link BioFields#UNIPROT_ID} {@link BioFields#PROTEIN_PREFERRED_SYMBOL} and alias
    * @param molecule
    * @param partProtein
    */
   private static void setPartProteinNames(BasicDBObject molecule, PartProtein partProtein) {
       log.info("setPartProteinNames()");
        BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME);  
        if (dbList != null) {
            for (Object obj : dbList) { 
                BasicDBObject dbObj = (BasicDBObject)obj;
                if (isPreferredSymbol(dbObj)) {
                    log.info("settingPreferredSymbol in partProtein()");
                    partProtein.setPartProteinPreferredSymbol(getPreferredSymbol(dbObj));
                    partProtein.setMessage(getPreferredSymbol(dbObj));
                } else if (isUniProt(dbObj)) {
                    partProtein.setUniprot(getUniProtId(dbObj));
                }
            }
        }
   }
   
   /**
    * Returns uniprotId
    * @param molecule
    * @return 
    */
   private static String getUniProtIdentifier(BasicDBObject molecule) {
        BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME); 
        if (dbList != null) {
            for (Object obj : dbList) { 
                BasicDBObject dbObj = (BasicDBObject)obj;
                return (getUniProtId(dbObj));
            }
        }
        return null;
   }
    
    /**
     * checks if the uniprot protein exists
     * @param molecule
     * @return 
     */
    private static boolean UniProtExists(BasicDBObject molecule) {
        log.info("UniProtExists()");    
        BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME);
        if (dbList != null) {            
            for (Object obj : dbList) { 
                BasicDBObject dbObj = (BasicDBObject)obj;
                if (isUniProt(dbObj)) {
                    return true;
                } 
            }           
        } 
        return false;
    }
   
    
   /**
    * Retrieves preferred symbol for a given object
    * @param dbObj
    * @return 
    */
   private static String getPreferredSymbol(BasicDBObject dbObj) {    
        if (isPreferredSymbol(dbObj)) {
           return getString(dbObj, NciPathwayFields.NCI_VALUE); 
        } else {              
           return null;
        }
   }
   
   /**
    * checks if the long_name_type is an alias
    * @param obj
    * @return 
    */
   private static boolean isAlias(BasicDBObject obj) {
     String alias = getString(obj, NciPathwayFields.NAME_TYPE);
     if ((alias != null && alias.equals(getStringFromEnum(NciPathwayFields.AS)))) {
          return true;
       } else {
          return false;
      } 
   }

   
  /**
   * checks if the long_name_type is preferred symbol
   * @param obj
   * @return 
   */
   private static boolean isPreferredSymbol(BasicDBObject obj) {
       String pf = getString(obj, NciPathwayFields.NAME_TYPE);
       return (pf != null && pf.equals(getStringFromEnum(NciPathwayFields.PF)));
   }
   
   /**
    * Retrieves Uniprot identifier
    * @param obj
    * @return 
    */
   private static String getUniProtId(BasicDBObject obj) {
        if (isUniProt(obj)) {   
            return getString(obj, NciPathwayFields.NCI_VALUE);
        } else {
            return null;
        }
   }
   
   /**
    * Retrieves the alias value (AS)
    * @param molecule
    * @return 
    */
   private static String getAlias(BasicDBObject molecule) {
        if (isAlias(molecule)) {  
           return getString(molecule, NciPathwayFields.NCI_VALUE);
        } else {
           return null;
        }
   }

   /**
    * 
    * checks if long_name_type is gene ontology 
    *  "@molecule_type" : "complex",
    * "@id" : "208321",
    *    "Name" : [
    *    {
    *     "@name_type" : "GO",
    *     "@long_name_type" : "Gene Ontology",
    *     "@value" : "0035030"
    *   },
    *
    * 
    * @param obj
    * @return 
    */
   private static boolean isGeneOntology(BasicDBObject obj) {
     String geneOnto = getString(obj, NciPathwayFields.NAME_TYPE);
     if (geneOnto != null && geneOnto.equals(getStringFromEnum(NciPathwayFields.GO))) {
          return true;
       } else {
          return false;
      } 
   }
  
   /**
    * gets gene ontology GO identifier from NciPathway molecule or interaction list
    *  "@molecule_type" : "complex",
    * "@id" : "208321",
    *    "Name" : [
    *    {
    *     "@name_type" : "GO",
    *     "@long_name_type" : "Gene Ontology",
    *     "@value" : "0035030"
    *   },
    *
    * @param molecule
    * @return 
    */
   private static String getGeneOntologyID(BasicDBObject molecule) {
        if (isGeneOntology(molecule)) {  
           return prepareGOID(getString(molecule, NciPathwayFields.NCI_VALUE));
        } else 
           return null;
   }
   
   private static String prepareGOID(String id) {
       return new StringBuilder("GO:" + id).toString();
   }
   
   /**
    * returns id (it may be molecule id or interaction_type id (@id)
    * @param molecule
    * @return String identifier (id)
    */
   private static String getId(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.ID);
   }
  
   /**
    * returns molecule identifier
    * @param molecule
    * @return String molecule identifier(molecule_identifier)
    */
   private static String getMoleculeId(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.MOLECULE_IDREF);    
   }
   
   /**
    * If casId does not exist, we have to create another type of Compound BioEntity
    * with preferred symbol value.
    * createCompound
    * @param molecule
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException 
    */
   private static void createCompound(BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException, InvocationTargetException {
        String casId = getCasId(molecule);
        if (casId == null) {
            log.info("casId missing, compound not created, moleculeId=" + getId(molecule));
            return;
        }
        boolean created = false;
        Compound compound = (Compound)getBioEntity(subGraph, BioTypes.COMPOUND, BioFields.CHEMICAL_ABSTRACT_ID, casId, getId(molecule));
        if (compound == null) {
            compound = new Compound();
            created = true;
        }
        compound.setNodeType(getStringFromEnum(BioTypes.COMPOUND));
        setCompoundNames(molecule, compound);
        compound.setMoleculeIdRef(getId(molecule));
        compound.setMessage(casId);
        if (created) { 
            subGraph.add(compound);
        }
   }
   
  
   
   /**
    * set compound information
    * @param molecule
    * @param compound 
    */
   private static void setCompoundNames(BasicDBObject molecule, Compound compound) {
        BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME); 
        if (dbList != null) {
            for (Object obj : dbList) { 
                BasicDBObject dbObj = (BasicDBObject)obj;
                if (isCas(dbObj)) {
                    compound.setCasId(getChemicalAbstractId(dbObj));
                } else if (isPreferredSymbol(dbObj)) {
                    compound.setCompoundPreferredSymbol(getPreferredSymbol(dbObj));
                } 
            }
        }
   }
   
   /**
    * Retrieves chemical abstracts id
    * http://www.chemcas.org/ 
    * @param molecule
    * @return casId
    */
   private static String getCasId(BasicDBObject molecule) {
        log.info("getCasId()");
        BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME); 
        if (dbList != null) {
            for (Object obj : dbList) { 
                BasicDBObject dbObj = (BasicDBObject)obj;
                if (isCas(dbObj)) {
                    return (getChemicalAbstractId(dbObj));
                }
            }
        }
        return null;     
    }
   
   
   /**
    * checks if the name type is cas
    * @param molecule
    * @return
    */
   private static boolean isCas(BasicDBObject molecule) {
       String cas = getString(molecule, NciPathwayFields.NAME_TYPE);
       if ((cas != null && cas.equals(getStringFromEnum(NciPathwayFields.CA)))) {
          return true;
       } else {
          return false;
       } 
   }
   
   /**
    * isString
    * @param obj
    * @return 
    */
   private static boolean isString(Object obj) {
       return obj.getClass().getName().equals("java.lang.String");
   }
   
   /**
    * Retrieves chemical abstracts identifier
    * @param molecule
    * @return 
    */
   private static String getChemicalAbstractId(BasicDBObject molecule) {  
        return getString(molecule, NciPathwayFields.NCI_VALUE);
   }
  
   /**
    * Searches in a given key and value pair
    * If not found, searches in key {@link BioFields#MOLECULE_IDREF} and value (moleculeIdRef)
    * @param subGraph
    * @param bioType {@link BioTypes}
    * @param key1 - used to retrieve moleculeIdRef
    * @param value1  {@link BioFields}
    * @param value2
    * @return
    * @throws NoSuchFieldException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException 
    */
   private static Object getBioEntity(Subgraph subGraph, BioTypes bioType, 
                         BioFields key1, String value1, String value2) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException {
        
        log.info("getBioEntity()," + key1 + "=" + value1 + " " + BioFields.MOLECULE_IDREF.toString() + "=" + value2);
        
        /**
         * Search either by UniquelyIndexed field or moleculeIdRef
         * As BioEntities are created by either one of the key(s).
         */  
        Object obj = subGraph.search(bioType, key1, value1);
        if (obj == null) {
            obj = subGraph.search(bioType, BioFields.MOLECULE_IDREF, value2);
            }     
        return obj;
   }
   
   /**
    * sets the protein name and also family member list of this protein
    * Based on the Preferred Symbol
    * Creates BioEntity and node for named protein and its relations.
    * @param molecule
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws IllegalArgumentException
    * @throws UnsupportedEncodingException 
    */
   private static void createNamedProtein(BasicDBObject molecule, Subgraph subGraph) throws IllegalAccessException, ClassNotFoundException, InstantiationException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, URISyntaxException, IOException, HttpException, NoSuchFieldException {
        log.info("createNamedProtein()");
        String preferredName = getPreferredSymbolFromName(molecule);
        boolean created = false;
        NamedProtein nProtein = (NamedProtein)getBioEntity(subGraph, BioTypes.NAMED_PROTEIN, BioFields.NAMED_PROTEIN_PREFERRED_SYMBOL, preferredName, getId(molecule));
        if (nProtein == null) {   
            nProtein = new NamedProtein(
                    preferredName, 
                    getStringFromEnum(BioTypes.NAMED_PROTEIN), preferredName);
            created = true;
            log.info("created NamedProtein()");
        }
        
        log.info("setting MoleculeIdRef()");
        /* update the existing protein */
        nProtein.setMoleculeIdRef(getId(molecule));
        log.info("setFamilyMemberList()");
        setFamilyMemberList(molecule, nProtein, subGraph);
        /*
        if (relations != null && relations.size() > 0) {
           nProtein.setContains(relations);
        } */

        log.info("setting Label");
        setLabel(molecule, nProtein);
        if (created) {
           subGraph.add(nProtein);
        }
            
   }
   
   /**
    * Gets preferred symbol of the protein 
    * As the preferred symbols contain text that is not URL friendly
    * cleanup the text. 
    * @param molecule 
    * @return 
    */
   private static String getPreferredSymbolValue(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.VALUE);
   }

   
   /**
    * 
    * @param protein - {@link Protein}
    * @param molecule - contains information about PTMExpression {@link BasicDBObject}
    * @param subGraph - {@link Subgraph}
    * set PTMValues - sets PTM Expression values associated with it
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException 
    */
   private static void setProteinPTMValues(Protein protein, BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException, InvocationTargetException {
     
       Protein proteinMolecule = (Protein)getPTMMolecule(getString(molecule, NciPathwayFields.MEMBER_MOLECULE_IDREF), BioTypes.PROTEIN, subGraph);
       if (proteinMolecule != null) { 
            protein.setPtmExpressionRelation(proteinMolecule,
                    getPTMProteinIdentifier(molecule),
                    getPTMPosition(molecule),
                    getPTMAa(molecule),
                    getPTMModification(molecule));
         }
   }
   
   /**
    * 
    * @param partProtein - {@link Protein}
    * @param molecule - contains information about PTMExpression {@link BasicDBObject}
    * @param subGraph - {@link Subgraph}
    * set PTMValues - sets PTM Expression values associated with it
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException 
    */
   private static void setPartProteinPTMValues(PartProtein partProtein, BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException,InvocationTargetException {
       Protein proteinMolecule = (Protein)getPTMMolecule(getString(molecule, NciPathwayFields.MEMBER_MOLECULE_IDREF), BioTypes.PROTEIN, subGraph);
       if (proteinMolecule != null) { 
            partProtein.setPartPtmExpressionRelation(proteinMolecule,
                    getPTMProteinIdentifier(molecule),
                    getPTMPosition(molecule),
                    getPTMAa(molecule),
                    getPTMModification(molecule));
         }
   }
   
   /**
    * checks if complex component list exists
    * @param dbObj - contains information about complex component list
    * @return true if list exists 
    */
   private static boolean complexComponentListExists(BasicDBObject dbObj) {
       log.info("complexComponentListExists()");
       Object obj = dbObj.get(NciPathwayFields.COMPLEX_COMPONENT_LIST.toString());
       //log.info("complexcomponentlist does not exist");
       return obj != null;
   }
   
   /**
    * set complex component list {@link NciPathwayFields#COMPLEX_COMPONENT_LIST}
    * molecule_Idref is used for {@link NciPathwayFields#MOLECULE_IDREF}
    * @param complex - {@link Complex} 
    * @param dbObj - contains information about {@link NciPathwayFields#COMPLEX_COMPONENT_LIST}
    * @param subGraph - {@link Subgraph}
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException 
    * <Molecule molecule_type="complex" id="215000">
        <Name name_type="PF" long_name_type="preferred symbol" value="Alpha-Synuclein/Torsin A" />
        <ComplexComponentList>
          <ComplexComponent molecule_idref="200419">
            <Label label_type="location" value="cytoplasm" />
            <PTMExpression>
              <PTMTerm protein="P37840" position="0" aa="Y" modification="phosphorylation" />
            </PTMExpression>
          </ComplexComponent>
          <ComplexComponent molecule_idref="204046">
          </ComplexComponent>
        </ComplexComponentList>
      </Molecule>
    */
   private static void setComplexComponentList(Complex complex, BasicDBObject dbObj, Subgraph subGraph) throws IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IllegalArgumentException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException, NoSuchFieldException,InvocationTargetException {
       log.info("setComplexComponentList()");
       log.info("complexComponentList(), " + dbObj.toString());
       BasicDBList complexComponentList = getDBList(dbObj, NciPathwayFields.COMPLEX_COMPONENT_LIST); 
       
       if (complexComponentList != null && complexComponentList.size() > 0) {
           log.info("complexComponentList(), " + complexComponentList.size());
            //Collection<BioRelation> relations = new HashSet<BioRelation>();    
            for (Object obj : complexComponentList) {
                BasicDBObject molecule = (BasicDBObject)obj;
                String moleculeIdRef = getString(molecule, NciPathwayFields.MOLECULE_IDREF);
                log.info("moleculeIdRef, " + moleculeIdRef);
                Object endEntity = getBioMolecule(moleculeIdRef,BioFields.MOLECULE_IDREF, subGraph);
                if (endEntity != null) { 
                    /**
                     * These values are set in both Complex and EndNode relationships
                     */
                   setLabelValues(complex, endEntity, molecule);
                   createComplexPTMRelationships(complex, endEntity, molecule);
                } else {
                    /* late relationship */
                    //ComplexComponentRelation.class
                    createRelGraph(complex, moleculeIdRef, BioFields.MOLECULE_IDREF, molecule, BioRelationClasses.COMPLEX_COMPONENT_RELATION.getAnnotationClass());
                    //PtmExpressRelation.class
                    createRelGraph(complex, moleculeIdRef, BioFields.MOLECULE_IDREF, molecule, BioRelationClasses.PTM_EXPRESSION_RELATION.getAnnotationClass());
                }
            }
       }
   }
   
   private static void complexRelationship(BasicDBObject molecule, String moleculeIdRef, Complex complex, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException,InvocationTargetException {   
        log.info("moleculeIdRef, " + moleculeIdRef);
        Object endEntity = getBioMolecule(moleculeIdRef, BioFields.MOLECULE_IDREF, subGraph);
        if (endEntity != null) { 
            /**
                * These values are set in both Complex and EndNode relationships
                */
            setLabelValues(complex, endEntity, molecule);
            createComplexPTMRelationships(complex, endEntity, molecule);
        }
   }
   
   /**
    * The label values are set for 
    * @param complex {@link Complex}
    * @param endEntity {@link Protein} {@link PartProtein} 
    * @param molecule
    * @throws IllegalAccessException 
    */
   private static void setLabelValues(Complex complex, Object endEntity, BasicDBObject molecule) throws IllegalAccessException {
       if (endEntity != null) { 
            if (labelExists(molecule)) {
                Map labelMap = getLabelValues(molecule);
                if (labelMap != null && labelMap.size() > 0) {
                    createComplexComponentLabelRelationship(complex, (BioEntity)endEntity, labelMap);    
                    setGeneOntologyRelationship(complex,labelMap);
                    setGeneOntologyRelationship(endEntity, labelMap);
                }
            } else {
                createComplexComponentRelationship(complex, (BioEntity)endEntity); 
            }
       }
   }
   
   
   /**
    * createComplexComponentLabelRelationship
    * The label values are set in both Complex and EndNode also.
    * @param complex {@link Complex}
    * @param endEntity {@link Protein} {@link PartProtein}
    * @param labelMap
    */
   private static void createComplexComponentLabelRelationship(Complex complex, BioEntity endEntity, Map labelMap) {
       log.info("createComplexComponentLabelRelationship()");
       complex.setComplexComponentLabels(endEntity, labelMap); 
       BioTypes bioType = endEntity.bioType();
       if (isPartProtein(bioType)) {
           ((PartProtein)endEntity).setComplexComponentLabels(complex, labelMap);
       } else if (isProtein(bioType)) {
           ((Protein)endEntity).setComplexComponentLabels(complex, labelMap);
       }
   }
   
   private static boolean isPartProtein(BioTypes bioType) {
       return bioType.equals(BioTypes.PART_PROTEIN);
   }
   
   private static boolean isProtein(BioTypes bioType) {
       return bioType.equals(BioTypes.PROTEIN);
   }
  
   /**
    * getFunction
    * @param map
    * @return 
    */
   public static String getFunction(Map map) { 
       if (map.size() > 0) {
           if (map.get(BioFields.FUNCTION) != null) 
              return (String)map.get(BioFields.FUNCTION);
       } 
       return null;
    }
    
   /**
    * getLocation
    * @param map
    * @return 
    */
    public static String getLocation(Map map) {
       if (map.size() > 0) {
            if (map.get(BioFields.LOCATION) != null) 
               return (String)map.get(BioFields.LOCATION);   
       } 
       return null;
    }
    
    public static String getProcess(Map map) {
        if (map.size() > 0) {
            if (map.get(BioFields.PROCESS) != null) 
               return (String)map.get(BioFields.PROCESS);   
       } 
       return null;
    }
    
    /**
     * getGORelationship
     * @param endEntity
     * @param key
     * @param typeGO 
     */
   
  public static void createGORelationship(Object endEntity, String key, int typeGO) {
       switch(typeGO) {
               case 0:
                   GeneOntology geneOntologyEntity = locationMap.get(key); 
                    if (geneOntologyEntity != null) {
                       geneOntologyEntity.setLocationRelation(endEntity);
                    } 
                   break;
               case 1:
                   geneOntologyEntity = processMap.get(key); 
                    if (geneOntologyEntity != null) {
                       geneOntologyEntity.setProcessRelation(endEntity);
                    } 
                   break;
               case 2:
                    geneOntologyEntity = functionMap.get(key); 
                    if (geneOntologyEntity != null) {
                       geneOntologyEntity.setProcessRelation(endEntity);
                    } 
                   break;
                   
       }
  }
    
   /**
    * setGeneOntologyRelationship {@link BioTypes#GENE_ONTOLOGY}
    * sets GeneOntology (GO) process and location relationship with an
    */
   public static void setGeneOntologyRelationship(Object endEntity, Map map) {    
       if (endEntity != null) {
           
           String location = getLocation(map);
           if (location != null) {
              createGORelationship(endEntity, location, 0);      
           } else {
               String process = getProcess(map);
               if (process != null) {
                   createGORelationship(endEntity, process, 1);
               } else {
                   String function = getFunction(map);
                   createGORelationship(endEntity, function, 2); 
               }
           }
       } 
   }
   
   /**
    * createComplexComponentRelationship
    * @param complex {@link Complex}
    * @param endEntity {@link Protein} or {@link PartProtein}
    */
   private static void createComplexComponentRelationship(Complex complex, BioEntity endEntity) {
       log.info("createComplexComponentRelationship()");
       complex.setComplexComponentRelations(endEntity);
       BioTypes bioType = endEntity.bioType();
       //setGeneOntologyRelationship(complex);
       //setGeneOntologyRelationship(endEntity);
       if (isPartProtein(bioType)) {
          ((PartProtein)endEntity).setComplexComponentRelations(complex);
       } else if (isProtein(bioType)) {
          ((Protein)endEntity).setComplexComponentRelations(complex);
       }
   } 
   
   /**
    * 
    * createComplexPTMRelationships
    * @param complex -  {@link Complex}
    * @param endEntity - {@link PartProtein} or {@link Protein}
    * @param molecule - contains information about PTMExpression
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException 
    */
   private static void createComplexPTMRelationships(Complex complex, Object endEntity, BasicDBObject molecule) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException,InvocationTargetException {
       log.info("createComplexPTMRelationships()");
       if (ptmExists(molecule)) {
            BasicDBList ptmList = getDBList(molecule, NciPathwayFields.PTM_EXPRESSION);
            if (ptmList != null && ptmList.size() > 0) {
                for (Object ptmObj : ptmList) {
                    //Protein proteinMolecule = (Protein)getPTMMolecule(molecule, BioTypes.PROTEIN, subGraph);
                    log.info("ptmObj, " + ptmObj.toString());
                    setComplexPTMValues(complex, (BasicDBObject)ptmObj, endEntity);
                }
            }
        } 
   }
   
   /**
    * setComplexPTMValues
    * @param complex - {@link Complex}
    * @param obj - contains PTM information 
    * @param endEntity {@link Protein} {@link PartProtein}
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException 
    */
   private static void setComplexPTMValues(Complex complex, BasicDBObject obj, Object endEntity) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException {  
       log.info("setComplexPTMValues()");
       if (endEntity != null) {       
           complex.setPtmExpressionRelation(endEntity,
                    getPTMProteinIdentifier(obj),
                    getPTMPosition(obj),
                    getPTMAa(obj),
                    getPTMModification(obj));
         }
   }
  
   /**
    * setPartValues - sets the part values in
    * @param partProtein {@link PartProtein}
    * @param molecule {@link BasicDBObject}
    * @param subGraph (@link Subgraph }
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException 
    */
   private static void setPartValues(PartProtein partProtein, BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NoSuchFieldException, IOException, HttpException, InvocationTargetException {
         if (partExists(molecule)) {
            BasicDBList partList = getBasicDBList(molecule, NciPathwayFields.PART);
            if (partList != null) {
                for (Object obj : partList) {
                    BasicDBObject part = (BasicDBObject)obj;
                    Protein wholeMolecule = getWholeMoleculeEntity(part, subGraph);
                    if (wholeMolecule != null) {
                        String startPos = getStartOfPartMolecule(part);
                        String endPos = getEndOfPartMolecule(part);  
                        partProtein.setPartOfMolecule(wholeMolecule, startPos, endPos);
                        wholeMolecule.setNciPartOfMolecule(partProtein, startPos, endPos);
                    } else {
                        String wholeMoleculeIdRef = getString(part, NciPathwayFields.WHOLE_MOLECULE_IDREF);
                        createRelGraph(partProtein, wholeMoleculeIdRef, BioFields.MOLECULE_IDREF, part, BioRelationClasses.PART_MOLECULE_RELATION.getAnnotationClass());
                    }
                }
            }
         }
   }
   
   private static void setPartMoleculeRelation(BasicDBObject part, Object partProtein, Object wholeMolecule) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
        log.info("setPartMoleculeRelation()");
        
        String METHOD_NAME = "setPartOfMolecule";
        String startPos = getStartOfPartMolecule(part);
        String endPos = getEndOfPartMolecule(part);  
        /*
        ((PartProtein)partProtein).setPartOfMolecule(wholeMolecule, startPos, endPos);
        ((Protein)wholeMolecule).setPartOfMolecule(partProtein, startPos, endPos);
        */
       
        log.info("wholeMolecule.getClass() " + wholeMolecule.getClass().toString());
        log.info("startPos.getClass()" + startPos.getClass());
        {
        Class<?>[] params = {wholeMolecule.getClass(), startPos.getClass(), endPos.getClass()};
        Object[] values = {(Object)wholeMolecule, (Object)startPos, (Object)endPos};   
        NciPathwayTemplate.invokeMethod(partProtein, METHOD_NAME, params, values);
        }
        
        {
        Class<?>[] params = {partProtein.getClass(), startPos.getClass(), endPos.getClass()};
        Object[] values = {(Object)partProtein,(Object)startPos, (Object)endPos};
        NciPathwayTemplate.invokeMethod(wholeMolecule, METHOD_NAME, params, values);
        }
   }
   
   
   /**
    * check if PTMExpression exists in this protein molecule
    * @param molecule
    * @return true if it exists or false if PTMExpression does not exist
    */
   private static boolean ptmExists(BasicDBObject molecule) {
       log.info("ptmExists()"); 
       Object obj = molecule.get(NciPathwayFields.PTM_EXPRESSION.toString());
       return obj != null;
   }
  
   
   /**
    * 	"Part" : {
    *    "@whole_molecule_idref" : "200381",
    *    "@part_molecule_idref" : "203561",
    *    "@start" : "1",
    *    "@end" : "29"
    *    }
    * 
    *   "Part" : [
    *    { "@whole_molecule_idref" : "200397" , "@part_molecule_idref" : "202005" , "@start" : "1426" , "@end" : "1670"} , 
    *    { "@whole_molecule_idref" : "200452" , "@part_molecule_idref" : "202005" , "@start" : "1426" , "@end" : "1670"}]}
    *  ]]
    * check if Part exists in this protein molecule
    * @param molecule
    * @return 
    */
   private static boolean partExists(BasicDBObject molecule) {
       BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.PART);
        if (dbList != null && dbList.size() > 0) {            
            return true;
        } 
        return false;
        
   }  
   
   
   private static BasicDBObject getPart(BasicDBObject molecule) {
       return getDBObject(molecule, NciPathwayFields.PART); 
   }
   
   /**
    * getWholeMolecule BioEntity
    * We have to create {@link BioEntity} {@link BioTypes#PROTEIN} ??
    */
   private static Protein getWholeMoleculeEntity(BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NoSuchFieldException, IOException, HttpException,InvocationTargetException {
       String wholeMoleculeId = getWholeMoleculeId(molecule);
       if (wholeMoleculeId != null) {
           return (Protein)getBioEntityFromBioType(subGraph, BioTypes.PROTEIN, BioFields.MOLECULE_IDREF, wholeMoleculeId);         
          /*
          if (obj == null) {
              return createProtein(wholeMoleculeId, subGraph);   
          } else {
              return (Protein)obj;
          } */
       }  else {
           return null;
       }
   }
   
   
   /**
    * getProteinPTMMolecule BioEntity
    */
   /*
   private static Protein getProteinPTMMolecule(BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NoSuchFieldException {
       String proteinPTMMoleculeId = getMoleculeId(molecule);
       if (proteinPTMMoleculeId != null) {
           getBioEntityFromBioType(subGraph, BioTypes.PROTEIN, BioFields.MOLECULE_IDREF, proteinPTMMoleculeId);
       }
       return null;
    }
   */
   
   /**
    * getPTMMolecule
    * bioMoleculeId {@link BasicDBObject}
    * bioType {@link BioTypes}
    * template {@link PersistenceTemplate}
    * return {@link BioEntity}
    * <Molecule molecule_type="protein" id="200082">
        <Name name_type="PF" long_name_type="preferred symbol" value="AKT1-2-active" />
        <FamilyMemberList>
          <Member member_molecule_idref="200519">
            <Label label_type="activity-state" value="active" />
            <PTMExpression>
              <PTMTerm protein="P31749" position="308" aa="T" modification="phosphorylation" />
            </PTMExpression>
          </Member>
          <Member member_molecule_idref="200863">
            <Label label_type="activity-state" value="active" />
            <PTMExpression>
              <PTMTerm protein="P31751" position="309" aa="T" modification="phosphorylation" />
            </PTMExpression>
          </Member>
        </FamilyMemberList>
      </Molecule>

    */
   private static BioEntity getPTMMolecule(String bioPTMMoleculeId, BioTypes bioType, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException,InvocationTargetException {
       if (bioPTMMoleculeId != null) {
           log.info("ptmMoleculeId =" + bioPTMMoleculeId);
           return (BioEntity)getBioEntityFromBioType(subGraph, bioType, BioFields.MOLECULE_IDREF, bioPTMMoleculeId);
           /*
           if (protein == null) {
                protein = createProtein(bioPTMMoleculeId, subGraph);
                return (BioEntity)protein;
           } else {
               return (BioEntity)protein;
           } */
       }  
       return null;
    }
   
    private static String getPTMProteinIdentifier(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.NCI_PROTEIN);
    }
         
    private static String getPTMPosition(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.NCI_POSITION);
    }
    
    private static String getPTMAa(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.NCI_AA);
    }
    
    private static String getPTMModification(BasicDBObject molecule) {
        return getString(molecule, NciPathwayFields.NCI_MODIFICATION);
    }
                    
   /**
    * get whole molecule id of the part of the protein information
    * This protein can include another whole molecule or molecules
    */
   private static String getWholeMoleculeId(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.WHOLE_MOLECULE_IDREF);
   }
   
   private static String getPartMoleculeId(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.PART_MOLECULE_IDREF);
   }
   
   private static String getStartOfPartMolecule(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.START);
   }
   
   private static String getEndOfPartMolecule(BasicDBObject molecule) {
       return getString(molecule, NciPathwayFields.END);
   }
   
   /**
    * Used by NamedProtein
    * Checks and sets family member 
    * @param dbObj {@link BasicDBObject}
    * @param startEntity {@link NamedProtein}
    * @param subGraph
    * 
    * MoleculeIdRef is referred to:
    * FamilyMemberList: {@link NciPathwayFields#MEMBER_MOLECULE_IDREF}
    * 
    */
   private static void setFamilyMemberList(BasicDBObject dbObj, NamedProtein startEntity, Subgraph subGraph ) throws ClassNotFoundException, IllegalAccessException, InstantiationException, IllegalArgumentException, NoSuchMethodException, InvocationTargetException, URISyntaxException, IOException, HttpException, NoSuchFieldException {
       log.info("setFamilyMemberList()");
       BasicDBList moleculeList = getBasicDBList(dbObj, NciPathwayFields.FAMILY_MEMBER_LIST); 
       if (moleculeList != null && moleculeList.size() > 0) {
        Collection<FamilyMemberRelation> relations = new HashSet<>();
        for (Object obj : moleculeList) {
                BasicDBObject molecule = (BasicDBObject)obj;     
                String moleculeIdRef = getString(molecule, NciPathwayFields.MEMBER_MOLECULE_IDREF);
                log.info("memberMoleculeIdRef =" + moleculeIdRef);
                Object endEntity = getBioMolecule(moleculeIdRef, BioFields.MOLECULE_IDREF, subGraph);
                if (endEntity != null) {
                    setFamilyMemberRelation(molecule, startEntity, endEntity);
                    setPTMExpression(molecule, startEntity, endEntity);
                } else {
                    //FamilyMemberRelation.class
                    createRelGraph(startEntity, moleculeIdRef, BioFields.MOLECULE_IDREF, molecule, BioRelationClasses.FAMILY_MEMBER_RELATION.getAnnotationClass());
                    //PtmExpressionRelation.class
                    createRelGraph(startEntity, moleculeIdRef, BioFields.MOLECULE_IDREF, molecule, BioRelationClasses.PTM_EXPRESSION_RELATION.getAnnotationClass());
                }
           }
        } 
   }
   
   /*
    private static void setFamilyMemberRelation(BasicDBObject molecule, Object startEntity, Object endEntity) throws IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
         FamilyMemberRelation relation = new FamilyMemberRelation(startEntity, endEntity, BioRelTypes.CONTAINS);
         if (labelExists(molecule)) {
            Map map = getLabelValues(molecule);
            relation.setLabels(map);
         }   
         ((NamedProtein)startEntity).setFamilyMemberRelation(relation);
         
         FamilyMemberRelation relationTo = new FamilyMemberRelation(endEntity, startEntity, BioRelTypes.IS_MEMBER_OF_FAMILY);
         if (labelExists(molecule)) {
            Map map = getLabelValues(molecule);
            relationTo.setLabels(map);
         }   
         log.info("calling setFamilyMemberRelation()" + endEntity.toString());
         ((Protein)endEntity).setMemberOfProteinFamily(relationTo);
    }
    */

    /*
   private static void setFamilyMember(BasicDBObject molecule, Object startEntity, Object endEntity, Collection<FamilyMemberRelation> relations) throws IllegalAccessException {
        FamilyMemberRelation relation = new FamilyMemberRelation(startEntity, endEntity, BioRelTypes.IS_MEMBER_OF_FAMILY);
         if (labelExists(molecule)) {
            Map map = getLabelValues(molecule);
            relation.setLabels(map);
         }   
         relations.add(relation);
   }
   */
    
    
   /**
    * 
    * @param molecule
    * @param startEntity  {@link BioEntity} 
    * @param endEntity {@link BioEntity}
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws NoSuchMethodException
    * @throws InstantiationException
    * @throws InvocationTargetException 
    */
   private static void setPTMExpression(BasicDBObject molecule, Object startEntity, Object endEntity) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, NoSuchMethodException, InstantiationException, InvocationTargetException {
       if (ptmExists(molecule)) {
            log.info("ptmExists");
            BasicDBList ptmList = getDBList(molecule, NciPathwayFields.PTM_EXPRESSION);
            if (ptmList != null && ptmList.size() > 0) {
                String METHOD_NAME = "setPTMExpressionRelation";
                Class<?>[] params = {startEntity.getClass(), 
                                        String.class, 
                                        String.class, 
                                        String.class,
                                        String.class};

                for (Object ptmObj : ptmList) {
                    if (ptmObj != null) {
                        log.info("proteinMolecule =" + ptmObj.toString());
                        //Protein proteinMolecule = (Protein)getPTMMolecule(moleculeIdRef, BioTypes.PROTEIN, subGraph);
                        /*
                            * 
                        NciPathwayTemplate.setPTMValues((BioEntity)startEntity, (BioEntity)endEntity,
                                getPTMProteinIdentifier((BasicDBObject)ptmObj),
                                getPTMPosition((BasicDBObject)ptmObj),
                                getPTMAa((BasicDBObject)ptmObj),
                                getPTMModification((BasicDBObject)ptmObj));
                            * 
                            */

                        Object[] values = {endEntity, 
                                getPTMProteinIdentifier((BasicDBObject)ptmObj),
                                getPTMPosition((BasicDBObject)ptmObj),
                                getPTMAa((BasicDBObject)ptmObj),
                                getPTMModification((BasicDBObject)ptmObj)};
                        NciPathwayTemplate.invokeMethod(startEntity, METHOD_NAME, params, values);
                    }
                }
            }
        }         
   }
   
   /**
    * 
    * getBioEntity
    * @param indexName
    * @param key {@link BioFields}
    * @param value  value of the BioField
    * @param subGraph {@link Subgraph}
    * @return
    * @throws ClassNotFoundException
    * @throws IllegalAccessException
    * @throws InstantiationException 
    */
   private static Object getBioEntityFromIndex(IndexNames indexName, BioFields key, String value, Subgraph subGraph) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, NoSuchFieldException {
       //Object obj = template.getBioEntityFromIndex(key, value, indexName);
       //if (obj == null) {
           return subGraph.search(key, value);
       //}
       //return obj;
   }
   
   private static BioTypes getBioType(BioEntity bioEntity) {
       return bioEntity.bioType();    
   }
   
   
   /*
    * for relationships when a moleculeIdRef is given, it could be either PartProtein or Protein
    * Do we do conversion from PartProtein to Protein or 
    * should we create a temporary BioMolecule and create relationships with the temporary one
    * Later in PartProtein or Protein, we go through subGraph and identify the relationships in
    * each complex and namedprotein and set up this relationships with PartProtein/Protein.
    * Delete the BioMolecule relationship from complex and namedProtein.
    * 
    * If Protein and PartProtein extend from BioMolecule, how does it affect the Annotation field
    * and relationships. Does Annotation pull up the superclass and subclass
    * fields and relationships?
    * 
    */
   private static Object getBioMolecule(String value, BioFields key, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException,InvocationTargetException {
       Object entity = getBioEntityFromIndex(IndexNames.MOLECULE_IDREF, key, value, subGraph);
       if (entity == null) {
           entity = getBioEntity(key, value, subGraph);
       }
       return entity;
   }
   
   private static Object getProtein(String moleculeIdRef, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException,InvocationTargetException {
        Protein protein = (Protein)getBioEntityFromBioType(subGraph, BioTypes.PROTEIN, BioFields.MOLECULE_IDREF, moleculeIdRef);
        /*
        if (protein == null) {
            log.info("protein does not exist, createProtein(), moleculeIdRef " + moleculeIdRef);
            protein = createProtein(moleculeIdRef, subGraph);
        } */
        return protein;
   }
   
   /**
    * PartProtein is created as this moleculeIdRef refers to an existing protein
    * @param moleculeIdRef
    * @param subGraph
    * @return
    * @throws NoSuchFieldException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws URISyntaxException 
    */
   /*
   private static Object getPartProtein(String moleculeIdRef, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, , InvocationTargetException {
       PartProtein partProtein = (PartProtein)getBioEntityFromBioType(subGraph, BioTypes.PART_PROTEIN, BioFields.MOLECULE_IDREF, moleculeIdRef);
       
       if (partProtein == null) {
          partProtein = createPartProtein(moleculeIdRef, subGraph);
       } else {
          return null;
       } 
       return (Object)partProtein;
   } */
   
   
   /**
    * getBioEntityFromBioType
    * @param subGraph
    * @param bioType
    * @param key
    * @param value
    * @return
    * @throws NoSuchFieldException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws URISyntaxException 
    */
   private static Object getBioEntityFromBioType(Subgraph subGraph, BioTypes bioType, BioFields key, String value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException {
       log.info("getBioEntityFromBioType()," + bioType.toString() + "," + key.toString() + "=" + value);
       return subGraph.search(bioType, key, value);
   }
       
   /**
    * 
    * This method processes the molecules and creates BioEntities and nodes
    * process molecule list
    * @param pathwayEntity
    * @param moleculeList
    * @param subGraph
    * @throws UnsupportedEncodingException 
    */
   private static void processMoleculeList(NciPathway pathwayEntity, BasicDBList moleculeList, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException, URISyntaxException, MalformedURLException, IOException, HttpException, NoSuchFieldException, ServiceException, InterruptedException {
        for (Object obj : moleculeList) {
            BasicDBObject molecule = (BasicDBObject)obj;
            log.info("molecule = " + molecule);
            BioTypes bioType = getBioType(molecule.getString(NciPathwayFields.MOLECULE_TYPE.toString()));
            log.info("bioType = " + bioType);
            if (bioType == BioTypes.PROTEIN) {
                /**
                 * UniProtId exists and 
                 * In some cases UniProtId and Preferred Symbol exist
                 */
                if (UniProtExists(molecule)) {  
                    if (partExists(molecule)) {
                        createPartProtein(molecule, subGraph);
                    } else {
                       log.info("createProtein");
                       createProtein(molecule, subGraph);
                    }     
                } else {
                    /* uniprotId does not exist for this protein 
                     * Preferred symbol name exists
                     * 
                     *          "@molecule_type" : "protein",
                     *          "@id" : "200110",
                     *           "Name" : {
                     *                   "@name_type" : "PF",
                     *                   "@long_name_type" : "preferred symbol",
                     *                   "@value" : "14-3-3 family"
                     *           },
                     * 
                     */
                    log.info("NamedProtein");
                    createNamedProtein(molecule, subGraph);
                }
            } else if (bioType == BioTypes.COMPOUND) {
                log.info("Compound");
                createCompound(molecule, subGraph);
            } else if (bioType == BioTypes.COMPLEX) {
                log.info("Complex");
                /* 
                 * There is an error ncipathway xml files 
                 * molecule_type = COMPLEX and it has uniprotId.
                 * This should be a protein. 
                 * So we should perform a check here and call CreateProtein()
                 */
                 if (UniProtExists(molecule)) {
                    createProtein(molecule, subGraph);
                 } else {
                    createComplex(molecule, subGraph);
                 }
            } else if (bioType == BioTypes.RNA) {
                log.info("RNA");
               // createRna(molecule);
            }
            /*
            String moleculeIdRef = getString(molecule, NciPathwayFields.ID);
            log.info("Retrieving object for moleculeIdRef =" + moleculeIdRef);
            Object t = getBioEntity(BioFields.MOLECULE_IDREF, moleculeIdRef, subGraph); 
            * 
            */
        }
    }
        
   
   /**
    * createComplex
    * creates {@link Complex} BioEntity
    *
    */
   public static void createComplex(BasicDBObject molecule, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException, InvocationTargetException {
        log.info("createComplex()");
        String preferredSymbol = getPreferredSymbolFromName(molecule);
        boolean created = false;
        log.info("createComplex(), PF =" + preferredSymbol);
        Complex complex = (Complex)getBioEntity(subGraph, BioTypes.COMPLEX, BioFields.COMPLEX_PREFERRED_SYMBOL, preferredSymbol, getId(molecule));       
        if (complex == null) {
            complex = new Complex();
            created = true;
        }
        setComplexNames(molecule, complex, subGraph);
        log.info("completed complex names");
        complex.setMoleculeIdRef(getId(molecule));
                
        if (complexComponentListExists(molecule)) {
            log.info("Exists! complexComponentListExists");
            setComplexComponentList(complex, molecule, subGraph);
        }
        
        // Add FamilyMemberList -> has list of complex objects
        /*
        <Molecule molecule_type="complex" id="200327">
        <Name name_type="PF" long_name_type="preferred symbol" value="Vitronectin-binding Integrins" />
        <ComplexComponentList>
        </ComplexComponentList>
        <FamilyMemberList>
          <Member member_molecule_idref="206196">
          </Member>
          <Member member_molecule_idref="205085">
          </Member>
          <Member member_molecule_idref="206193">
          </Member>
          <Member member_molecule_idref="206192">
          </Member>
          <Member member_molecule_idref="206194">
          </Member>
          <Member member_molecule_idref="205060">
          </Member>
        </FamilyMemberList>
      </Molecule>
       */
        
        //log.info("completed setComplexComponentList");
       
        setLabel(molecule, (Object)complex);
        log.info("setLabel()");
         
        complex.setMessage(preferredSymbol);
        //log.info("complex setting complete");
        if (created) {
            log.info("adding to subgraph");
            subGraph.add(complex);
        }
        /*
        log.info("retrieving complex");
        Complex c1 = (Complex)subGraph.search(BioTypes.COMPLEX, BioFields.COMPLEX_PREFERRED_SYMBOL, preferredSymbol);
        log.info("complexPreferredSymbol =" + c1.getComplexComponentRelation());
        Iterable<ComplexComponentRelation> colls = c1.getComplexComponentRelation();
        for (ComplexComponentRelation rel : colls) {
            log.info("rel =" + rel.toString());
            log.info("rel.getMessage" + rel.getMessage());
            log.info(rel.getFunction());
        } */
   }
   
   /**
    * 
    * GO is a Complex 
    * "@molecule_type" : "complex",
    * "@id" : "208321",
    * "Name" : [
    *        {
    *                "@name_type" : "GO",
    *                "@long_name_type" : "Gene Ontology",
    *               "@value" : "0035030"
    *       }
    * 
    * getGeneOntology
    * @param dbObj
    * @param bioEntity
    * @param subGraph
    * @return
    * @throws NoSuchFieldException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException
    * @throws URISyntaxException
    * @throws 
    * @throws InvocationTargetException 
    */
   public static GeneOntology getGeneOntology(BasicDBObject dbObj, Object bioEntity, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, InvocationTargetException {
         String goId = getGeneOntologyID(dbObj);
         if (goId != null) {
             boolean created = false;
             GeneOntology goEntity = (GeneOntology)getBioEntityFromBioType(subGraph, BioTypes.GENE_ONTOLOGY, BioFields.GENE_ONTOLOGY_ID, goId);       
            /*
             if (goEntity == null) {
                 goEntity = new GeneOntology();
                 goEntity.setGeneOntologyId(goId);
                 created = true;
             } */
             /**
              * Dont create GO entity, use the existing one
              * As NCIPathway gives GO ID's that are alternateId's
              * These are not uniquely indexed. 
              */
             goEntity.setGoIsAComplexRelationship(bioEntity);
             log.info("created goEntity=" + goId);
             subGraph.add(goEntity);
             return goEntity;
         } else {
            return null;
         }
   }
   
   
   
   //public static void processGeneOntologies(BasicDBObject dbObj, Subgraph subGraph) {
       
   //}
   
   
   
   /**
    * 
    * setComplexNames
    * @param molecule
    * @param complex 
    */
   private static void setComplexNames(BasicDBObject molecule, Complex complex, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, InvocationTargetException {
       log.info("setComplexNames()");
       BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME);
       //log.info("dbList.size() " + dbList.size());
       if (dbList != null) {
          for (Object obj : dbList) { 
               log.info("obj =" + obj.toString());
                BasicDBObject dbObj = (BasicDBObject)obj;
                if (isGeneOntology(dbObj)) {
                    getGeneOntology(dbObj, complex, subGraph);
                }
                if (isPreferredSymbol(dbObj)) {
                    String preferredSymbol = getPreferredSymbolValue(dbObj);
                    if (preferredSymbol != null) {
                        complex.setComplexPreferredSymbol(preferredSymbol);
                    }
                }
                if (isAlias(dbObj)) {
                    String alias = getAlias(dbObj);
                    if (alias != null) {
                        complex.setAliases(alias);
                    }
                }
           }
       }
   }
  
   
   /**
    * getPreferredSymbolName 
    * @param molecule
    * @return String 
    */
   private static String getPreferredSymbolFromName(BasicDBObject molecule) {
       log.info("getPreferredSymbolFromName");
       if (molecule != null) {
          log.info("molecule is not null");
          BasicDBList dbList = getBasicDBList(molecule, NciPathwayFields.NAME);
          if (dbList != null) {
              log.info("dbList =" + dbList.toString());
              for (Object obj : dbList) { 
                  BasicDBObject dbObj = (BasicDBObject)obj;
                  log.info("dbObj =" + dbObj.toString());
                  if (isPreferredSymbol(dbObj)) {
                     return getPreferredSymbolValue(dbObj);
                  }
              }
           } 
        }
        return null;
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
   private static BasicDBList getBasicDBList(DBObject dbObject, NciPathwayFields field) {
        if (dbObject == null || field == null) {
            throw new RuntimeException("dbObject is null, field= " + getStringFromEnum(field));
        }
        Object obj = dbObject.get(getStringFromEnum(field));
        if (obj == null) return null;
        return MongoObjects.getBasicDBList(obj);
    }
   
   
   /**
    * This one will work for BasicDBObject and BasicDBList
    * checks and sets complex names
    * @param molecule
    * @param complex
    * @param template
    * @throws IllegalAccessException 
    */
   /*
   private static void checkAndSetComplexNames(BasicDBObject molecule, Complex complex, RedbasinTemplate template) throws IllegalAccessException, URISyntaxException {  
        BasicDBList dbList = getBasicDBList((DBObject)molecule, NciPathwayFields.NAME); 
        if (dbList != null) {
            for (Object obj : dbList) { 
                BasicDBObject dbObj = (BasicDBObject)obj;
                if (isPreferredSymbol(dbObj)) {  
                    setPreferredSymbol(dbObj, complex, template);
                } else if (isAlias(dbObj)) {
                    setAlias(dbObj, complex);
                } else if (isGeneOntology(dbObj)) {
                    setGeneOntology(dbObj, complex);
                }
            }
        }
   } 
   */
  /**
   * setPreferredSymbol 
   * @param dbObj
   * @param complex
   * @throws IllegalAccessException
   */
   private static void setPreferredSymbol(BasicDBObject dbObj, Complex complex) throws IllegalAccessException, URISyntaxException {         
        complex.setComplexPreferredSymbol(getPreferredSymbol(dbObj));
        complex.setMessage(getPreferredSymbol(dbObj));
   
   }
   
   /**
    * setAlias in a complex
    * @param dbObj
    * @param complex
    * @throws IllegalAccessException
    */
   private static void setAlias(BasicDBObject dbObj, Complex complex) throws IllegalAccessException, URISyntaxException {         
        complex.setAliases(getAlias(dbObj));    
   }

    /**
     * createInteractionEntity
     * use BioFields for BioEntity to extract the fields.
     * @param pathwayEntity
     * @param interactionObj
     * @param subGraph
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws UnknownHostException
     * @throws HttpException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     * @throws Exception
     */
    public static NciPathwayInteraction createInteractionEntity(NciPathway pathwayEntity, BasicDBObject interactionObj, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException, InvocationTargetException, InterruptedException {
        log.info("createInteractionEntity()");
        String interactionId = getString(interactionObj, NciPathwayFields.ID);
        NciPathwayInteraction entity = (NciPathwayInteraction)getBioEntityFromBioType(subGraph, BioTypes.NCI_PATHWAY_INTERACTION, BioFields.INTERACTION_ID, interactionId);
        
        boolean created = false;
        if (entity == null) {
            entity = new NciPathwayInteraction(interactionId, 
                            getString(interactionObj, NciPathwayFields.INTERACTION_TYPE));
            created = true;
        }

        if (exists(interactionObj, NciPathwayFields.CONDITION_TYPE)) {
        entity.setConditionType(getString(interactionObj, NciPathwayFields.CONDITION_TYPE));
        }
        if (exists(interactionObj, NciPathwayFields.POSITIVE_CONDITION)) {
            entity.setIsConditionPositive(getStringFromEnum(BioFields.DEFAULT_POSITIVE_CONDITION_VAL));
        } else {
            if (exists(interactionObj, NciPathwayFields.NEGATIVE_CONDITON)) {
                entity.setIsConditionPositive(getStringFromEnum(BioFields.DEFAULT_NEGATIVE_CONDITION_VAL));
            }
        }
        if (exists(interactionObj, NciPathwayFields.EVIDENCE_LIST)) {
            entity.setEvidenceList(getListAsString(getBasicDBList(interactionObj, NciPathwayFields.EVIDENCE_LIST), NciPathwayFields.NCI_VALUE));
            //log.info("setEvidenceList");
        }
        entity.setSourceId(getString(interactionObj, NciPathwayFields.SOURCE_ID));
        entity.setSourceText(getString(interactionObj, NciPathwayFields.SOURCE_TEXT));
        entity.setConditionText(getString(interactionObj, NciPathwayFields.TEXT));
        if (exists(interactionObj, NciPathwayFields.REFERENCE_LIST)) {
            //String reference = getListAsString(
            setPubMedRelation(entity, interactionObj, subGraph);
            //getBasicDBList(interactionObj, NciPathwayFields.REFERENCE_LIST), NciPathwayFields.PMID);
            //entity.setReferenceList(reference);

        }
        if (exists(interactionObj, NciPathwayFields.INTERACTION_TYPE)) {
            entity.setInteractionType(getString(interactionObj, NciPathwayFields.INTERACTION_TYPE));
        }
        processInteractionComponentList(pathwayEntity, entity, interactionObj, subGraph);
        
        /* add it only once */
        if (created) {
           subGraph.add(entity);
        }
        return entity; 
    } 
    
    /*
     * PubMed has to be created in this
     *	"ReferenceList" : [
     *       {
     *               "@pmid" : "8698818",
     *               "#text" : "8698818"
     *       },
     *       {
     *               "@pmid" : "10477766",
     *               "#text" : "10477766"
     *       }
     *     ]
    */
    private static void setPubMedRelation(NciPathwayInteraction entity, BasicDBObject interactionObj, Subgraph subGraph) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException, InterruptedException, IOException, HttpException {
         BasicDBList dbList = getBasicDBList(interactionObj, NciPathwayFields.REFERENCE_LIST);
        if (null == dbList) return;
        for (Object obj : dbList) {
             String pubMedId = getString((BasicDBObject)obj, NciPathwayFields.PMID);
             PubMed pubMed = (PubMed)subGraph.search(BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);
             if (pubMed == null) { 
                log.info("getPubMed(), pubMedId =" + pubMedId);
                pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
                if (pubMed != null) {
                    pubMed.setPubMedRelation(entity);
                }
             }
         }
    }
   
    /**
     * setPubMedRelation
     * @param obj
     * @param entity
     * @param pubMedEntity
     * @throws NoSuchMethodException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    private static void setPubMedRelation(Object obj, Object entity, Object pubMedEntity) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
         Class<?>[] params = {entity.getClass()};
         Object[] values = {entity}; 
         String METHOD_NAME = "setPubMedRelation";
         /**
          * PubMed invokes this method
          */
         NciPathwayTemplate.invokeMethod(pubMedEntity, METHOD_NAME, params, values);     
    }
    
    /**
     * Process the InteractionList section, which also contains EvidenceList, ReferenceList
     * and InteractionComponentList. Here we also create the interaction node and it's indexes.
     * We also make the interaction node isPartOf the pathway as a relationship
     * 
     * @param pathwayEntity 
     * @param interactionList 
     */
    public static void processInteractionList(NciPathway pathwayEntity, BasicDBList interactionList, Subgraph subGraph) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException, NoSuchFieldException, InvocationTargetException, InterruptedException {
        //BasicDBList interactionList = (BasicDBList)pathwayInfo.get((Object)INTERACTION_LIST);
        if (interactionList != null && interactionList.size() > 0) {
            Iterator iter = interactionList.iterator();
            while (iter.hasNext()) {
                BasicDBObject interactionDBObj = (BasicDBObject)iter.next();
                NciPathwayInteraction interactionEntity = createInteractionEntity(pathwayEntity, interactionDBObj, subGraph);
                /**
                 * sets relationship 
                 */
                pathwayEntity.setPathwayComponent(interactionEntity, interactionEntity.getInteractionType());  
            }
        }
    }

    /**
     * Process the InteractionList section, which also contains EvidenceList, ReferenceList
     * and InteractionComponentList. Here we also create the interaction node and it's indexes.
     * We also make the interaction node isPartOf the pathway as a relationship
     *
     * @param pathwayEntity
     * @param dbList
     * @param subGraph
     * @throws UnsupportedEncodingException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws MalformedURLException
     * @throws IOException
     * @throws UnknownHostException
     * @throws HttpException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     */
    public static void processOrganismList(NciPathway pathwayEntity, BasicDBList dbList, Subgraph subGraph) throws UnsupportedEncodingException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, MalformedURLException, IOException, UnknownHostException, HttpException, NoSuchFieldException,InvocationTargetException {
        //BasicDBList interactionList = (BasicDBList)pathwayInfo.get((Object)INTERACTION_LIST);
        if (dbList != null && dbList.size() > 0) {
            Iterator iter = dbList.iterator();
            while (iter.hasNext()) {
                BasicDBObject dbObj = (BasicDBObject)iter.next();
                processOrganism(pathwayEntity, dbObj, subGraph); 
            }
        }
    }
    
    /**
     * processOrganism
     * @param pathwayEntity
     * @param dbObj
     * @param subGraph
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws NoSuchFieldException
     * @throws 
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    private static void processOrganism(NciPathway pathwayEntity, BasicDBObject dbObj, Subgraph subGraph) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, NoSuchFieldException,IllegalArgumentException, InvocationTargetException {
         Organism organismEntity = getOrganism(dbObj, subGraph);
         //log.info("organismEntity.OrganismshortLabel =" + organismEntity.getOrganismShortLabel());
         pathwayEntity.setOrganismComponent(organismEntity);  
    }
    
    
    /**
     * Label exists
     * @param obj
     * @return true if it exists or false if not does exist
     */
    private static boolean labelExists(BasicDBObject obj) {
        return obj.get(getStringFromEnum(NciPathwayFields.LABEL)) != null;
    }
    
    
    /**
     * setLabel
     */
  private static void setLabel(BasicDBObject obj, Object entity) throws IllegalAccessException {
      log.info("setLabel ");
      Map map = getLabelValues(obj);
      /* 
       * set the properties of this entity for label
       */
      if (map != null && map.size() > 0) {
         setProperties(entity, map);
         setGeneOntologyRelationship(entity, map);
      } 
  }
  
    /**
     * getLabelValues properties in Map 
     * Location, ActivityState, Function are set in the Map
     * @param obj
     * @return Map of label values
     */
    private static Map getLabelValues(BasicDBObject obj) throws IllegalAccessException {
        log.info("getLabelValues()");
        if (labelExists(obj)) {
            BasicDBList labelBsonList = getBasicDBList(obj, NciPathwayFields.LABEL);
            if (labelBsonList != null) {
                BasicDBObject labelBson;
                String location, activityState, function;
                location = activityState = function = null;
                Iterator labelIter = labelBsonList.iterator();   
                while (labelIter.hasNext()) {
                    labelBson = (BasicDBObject)labelIter.next();
                    String labelType = getString(labelBson, NciPathwayFields.LABEL_TYPE); 
                    if ((location = getLocation(labelBson, labelType)) == null) {
                    } else if ((activityState = getActivityState(labelBson, labelType)) == null) {
                    } else function = getFunction(labelBson, labelType);                                                 
                }
                Map<String, String> map = new HashMap<>();
                if (location != null) {
                    map.put(BioFields.LOCATION.toString(), location);
                }
                if (activityState != null) {
                    map.put(BioFields.ACTIVITY_STATE.toString(), activityState);
                }
                if (function != null) {
                    map.put(BioFields.FUNCTION.toString(), function);
                } 
                return map;               
            }
        }
        return null;
    }
    

    /**
     * checkAndSetLabel - set label values for a given BioEntity
     * @param molecule
     * @param entity
     * @throws IllegalAccessException
     * @throws URISyntaxException
     */
    private static void checkAndSetLabel(BasicDBObject molecule, BioEntity entity) throws IllegalAccessException, URISyntaxException {  
        if (labelExists(molecule)) {
            BasicDBObject labelObj = getLabel(molecule);
            Map<String, String> map = new HashMap<>();
            String location = getLocation(labelObj, getStringFromEnum(NciPathwayFields.LOCATION));
            String function = getFunction(labelObj, getStringFromEnum(NciPathwayFields.FUNCTION));
            String activityState = getActivityState(labelObj, getStringFromEnum(NciPathwayFields.ACTIVITY_STATE));
            map.put(BioFields.LOCATION.toString(), location);
            map.put(BioFields.ACTIVITY_STATE.toString(), activityState);
            map.put(BioFields.FUNCTION.toString(), function);
            setProperties(entity, map);
         }
     }
    
    
    /**
     * 
     * getLabel 
     * @param obj
     * @return BasicDBObject - label object
     */
    private static BasicDBObject getLabel(BasicDBObject obj) {
        return getDBObject(obj, NciPathwayFields.LABEL);
    }
    
    /**
     * getLocation
     * @param labelBson
     * @param labelType
     * @return location
     */
    private static String getLocation(BasicDBObject labelBson, String labelType) {
        if (labelType.equals(getStringFromEnum(NciPathwayFields.LOCATION))) { // only if it is location
           return getString(labelBson, NciPathwayFields.NCI_VALUE);
        } else 
            return null;
    }
    
    private static String getActivityState(BasicDBObject labelBson, String labelType) {
       if (labelType.equals(getStringFromEnum(NciPathwayFields.ACTIVITY_STATE))) {
           return getString(labelBson, NciPathwayFields.NCI_VALUE);  
       }
       return null;
    }                           
    
    private static String getFunction(BasicDBObject labelBson, String labelType) {
       if (labelType.equals(getStringFromEnum(NciPathwayFields.FUNCTION))) {
           return getString(labelBson, NciPathwayFields.NCI_VALUE);  
       }
       return null;
    }
    

    /**
     * Creates nodes for the InteractionComponentList and links them through relationships.
     * Currently supports creating one or more input nodes, and one output node. It links
     * each input node to the output node using the isOutputOf relationship. Additionally
     * each input node and the output node isPartOf the pathwayNode and isInteractionOf
     * interactionNode. Input and output nodes here can be used as protein nodes in future.
     * In future, we will support other role types such as agent, inhibitor.
     *
     * For each input node and the output node, we also create indexes for @role_type,
     * @molecule_idref and an optional index for location, if it exists.
     *
     * We assume that the location of a protein does not depend on the interaction. It remains
     * the same. If it was in cytoplasm, it will continue to be in cytoplasm irrespective of
     * the interaction.
     *
     * @param pathwayEntity
     * @param interactionEntity
     * @param interactionDbObj
     * @param subGraph
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws NoSuchFieldException
     */
    public static void processInteractionComponentList(NciPathway pathwayEntity, NciPathwayInteraction interactionEntity, BasicDBObject interactionDbObj, Subgraph subGraph) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, NoSuchFieldException {            
       
        Object interactionObj = interactionDbObj.get(getStringFromEnum(NciPathwayFields.INTERACTION_COMPONENT_LIST));
        if (!isString(interactionObj)) {
            BasicDBList interactionComponentList = getBasicDBList(interactionDbObj, NciPathwayFields.INTERACTION_COMPONENT_LIST);
            if (interactionComponentList == null) return;
            Iterator interactionComponentIter = interactionComponentList.iterator();
            List<Map<String, String>> moleculeList = new ArrayList();
            List<Map> outputMoleculeList = new ArrayList();
            while (interactionComponentIter.hasNext()) {
                Map<String, String> outputMap = new HashMap<>();
                Map<String, String> roleMap = new HashMap<>();
                BasicDBObject interactionComponent = (BasicDBObject)interactionComponentIter.next();   
                String roleType = getString(interactionComponent, NciPathwayFields.ROLE_TYPE);
                String moleculeIdref = getString(interactionComponent, NciPathwayFields.MOLECULE_IDREF);                                       
                String location = null;
                String activityState = null;
                String function = null;   // is this a Java reserved word?
                if (labelExists(interactionComponent)) {  
                    BasicDBList labelBsonList = getBasicDBList(interactionComponent, NciPathwayFields.LABEL);
                    BasicDBObject labelBson;
                    Iterator labelIter = labelBsonList.iterator();                                                       
                    while (labelIter.hasNext()) {
                        labelBson = (BasicDBObject)labelIter.next();
                            String labelType = getString(labelBson, NciPathwayFields.LABEL_TYPE);
                        if (null != (location = getLocation(labelBson, labelType))) {
                            if ((activityState = getActivityState(labelBson, labelType)) != null) {
                                function = getFunction(labelBson, labelType);
                            }
                        }
                    }                       
                    if (OncologyRoles.isOutput(roleType)) {
                        if (location != null) {
                            outputMap.put(BioFields.LOCATION.toString(), location);
                        }
                        if (activityState != null) {
                            outputMap.put(BioFields.ACTIVITY_STATE.toString(), activityState);
                        }
                        if (function != null) {
                            outputMap.put(BioFields.FUNCTION.toString(), function);
                        }
                        outputMap.put(BioFields.ROLE_TYPE.toString(), roleType);
                        outputMap.put(BioFields.MOLECULE_IDREF.toString(), moleculeIdref);
                        outputMoleculeList.add(outputMap);
                    } else {  
                        if (location != null) {
                            roleMap.put(BioFields.LOCATION.toString(), location);
                        }
                        if (activityState != null) {
                            roleMap.put(BioFields.ACTIVITY_STATE.toString(), activityState);
                        }
                        if (function != null) {
                            roleMap.put(BioFields.FUNCTION.toString(), function);
                        }
                        roleMap.put(BioFields.ROLE_TYPE.toString(), roleType);
                        roleMap.put(BioFields.MOLECULE_IDREF.toString(), moleculeIdref);
                        moleculeList.add(roleMap);
                    }                  
                } else {  // if there is no label for a given molecule
                    if (OncologyRoles.isOutput(roleType)) {
                        outputMap.put(BioFields.ROLE_TYPE.toString(), roleType);
                        outputMap.put(BioFields.MOLECULE_IDREF.toString(), moleculeIdref);
                        outputMoleculeList.add(outputMap);
                    } else {
                        roleMap.put(BioFields.ROLE_TYPE.toString(), roleType);
                        roleMap.put(BioFields.MOLECULE_IDREF.toString(), moleculeIdref);
                        moleculeList.add(roleMap);
                    }
                }
            }
            try {
               createInteractionComponent(interactionEntity, moleculeList, outputMoleculeList, subGraph);
            } catch (UnsupportedEncodingException e) {
                log.error("Error in createInteractionComponent," + e.getMessage());
                throw new RuntimeException(e);
            }       
        }
    } 
    
    /**
     * sets the values of Label:  activity, function, location
     * Make sure the entity has all these properties set in it.
     * @param entity
     * @param mapObj
     * @throws IllegalAccessException 
     */
    private static void setProperties(Object entity, Map mapObj) throws IllegalAccessException { 
                     
         Set keys = mapObj.keySet();
         Iterator iter = keys.iterator();
         while (iter.hasNext()) {
             String key = (String)iter.next();
             npt.setFieldValue(entity, getStringFromMap(mapObj, key), key);
         }
    }

     /**
     * createInteractionComponentList and links them through relationships.
     * Currently supports creating one or more input nodes, and one output node. It links
     * each input node to the output node using the isOutputOf relationship. Additionally
     * each input node and the output node isPartOf the pathwayNode and isInteractionOf
     * interactionNode. Input and output nodes here can be used as protein nodes in future.
     * In future, we will support other role types such as agent, inhibitor.
     *
     * For each input node and the output node, we also create indexes for @role_type,
     * @molecule_idref and an optional index for location, if it exists.
     *
     * We assume that the location of a protein does not depend on the interaction. It remains
     * the same. If it was in cytoplasm, it will continue to be in cytoplasm irrespective of
     * the interaction.
     * @param interactionEntity
     * @param moleculeList
     * @param outputMoleculeList
     * @param subGraph
     * @throws UnsupportedEncodingException
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws NoSuchFieldException
     */
    public static void createInteractionComponent(NciPathwayInteraction interactionEntity, List moleculeList, List outputMoleculeList, Subgraph subGraph) throws UnsupportedEncodingException, ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, NoSuchFieldException {
         log.info("createInteractionComponent()");
         List<BioEntity> outputEntityList = new ArrayList<>();
         if (interactionEntity != null) { 
            String interactionId = interactionEntity.getInteractionId();
            if (interactionId != null) {
                if (outputMoleculeList != null && outputMoleculeList.size() > 0) {
                    for (Object obj : outputMoleculeList) {
                        Map outputMap = (Map)obj;
                        if (outputMap.size() > 0) {
                            BioEntity outputBioEntity = getMoleculeEntity(outputMap, subGraph);
                            if (outputBioEntity != null) { 
                                createInteractionComponentOncoRelation(
                                        BioRelTypes.IS_OUTPUT_OF, 
                                        BioRelTypes.AN_INTERACTION, 
                                        outputBioEntity, /* startEntity */
                                        (BioEntity)interactionEntity, /* endEntity */ 
                                        outputMap);
                            }  
                            outputEntityList.add(outputBioEntity);
                        }
                    }
                }

                for (Object moleculeObj : moleculeList) {
                    Map moleculeMap = (Map)moleculeObj;
                    if (moleculeMap.size() > 0) {
                        BioEntity moleculeEntity = getMoleculeEntity(moleculeMap, subGraph);
                        if (moleculeEntity != null) {
                            createInteractionComponentOncoRelation(
                                getOncoRole(moleculeMap),  /* BioFields.ROLE_TYPE */
                                BioRelTypes.AN_INTERACTION, /* endRole */
                                moleculeEntity, /* startEntity */
                                (BioEntity)interactionEntity, /* endEntity */
                                moleculeMap
                                );   
                        } else {
                            String startMoleculeId = (String)moleculeMap.get(getStringFromEnum(BioFields.MOLECULE_IDREF));
                            //OncoRelation.class 
                            createRelGraph(interactionEntity, startMoleculeId, BioFields.MOLECULE_IDREF, moleculeObj, BioRelationClasses.ONCO_RELATION.getAnnotationClass());
                        }
                    }
                }
            }
         }
    } 
    
    private static Enum getOncoRole(Map map) {
        if (OncologyRoles.contains((String)map.get(BioFields.ROLE_TYPE))) {
           return OncologyRoles.valueOf((String)map.get(BioFields.ROLE_TYPE));   
        } else {
            return BioFields.ROLE_TYPE;
        }
    }
    

    /**
     * Retrieves molecule entity
     * {@link BioFields#ROLE_TYPE}
     * @param objMap
     * @param subGraph
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws NoSuchFieldException
     */
    private static BioEntity getMoleculeEntity(Map objMap, Subgraph subGraph) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, NoSuchFieldException {
        log.info("getMoleculeEntity()");
        if (objMap.size() > 0) {
            String moleculeId = (String)objMap.get(getStringFromEnum(BioFields.MOLECULE_IDREF));
            log.info("moleculeId =" + moleculeId);
            if (moleculeId != null) {
                Object bioEntity = getBioEntityFromIndex(
                    IndexNames.MOLECULE_IDREF,
                    BioFields.MOLECULE_IDREF,
                    moleculeId, 
                    subGraph);  
                return (BioEntity)bioEntity;
            }
        }
        return null;
    }

    /**
     * This creates relationship for InteractionComponentOncoRelation
     * @param startRole
     * @param endRole
     * @param startEntity
     * @param endEntity
     * @param map
     */
    private static void createInteractionComponentOncoRelation(Enum startRole, Enum endRole, BioEntity startEntity, BioEntity endEntity, Map map ) {
           ((NciPathwayInteraction)endEntity).interactsWith(
                  getStringFromEnum(startRole),
                  getStringFromEnum(endRole),
                  (Object)startEntity, 
                  (Object)endEntity, 
                  map);            
    } 
    
    private static void setOncoRelation(Map dbObj, Object startEntity, Object interactionEntity) {
        createInteractionComponentOncoRelation(
                getOncoRole(dbObj),
                BioRelTypes.AN_INTERACTION,
                (BioEntity)startEntity,
                (BioEntity)interactionEntity,
                dbObj);
    }
    
    /**
     * @param dbObj
     * @return 
     */
    private static boolean isUniProt(BasicDBObject dbObj) {
          if ( (getString(dbObj, NciPathwayFields.LONG_NAME_TYPE).equals(getStringFromEnum(NciPathwayFields.UNIPROT)))) {
              return true;
          } else {
              return false;
          }
    }
    
    /**
     * check if this is location list
     * @param dbObj
     * @return 
     */
    public static boolean isLocation(BasicDBObject dbObj) {
          String name = getString(dbObj, NciPathwayFields.AT_NAME);
        return name.equals(getStringFromEnum(NciPathwayFields.LOCATION));
     }
    
    /**
     * isGO
     * @param dbObj
     * @return 
     */
    public static boolean isGO(BasicDBObject dbObj) {
        if ((getString(dbObj, NciPathwayFields.AT_GO)) != null) {
           return true;
        } else {
           return false;
        }
    }
    
    /**
     * Given "@GO" : "GO:0007254" returns 0007254
     * 
     * @param obj
     * @return String 
     */
    public static String getGeneOntologyIdentifier(BasicDBObject obj) {
        String goStr = getString(obj, NciPathwayFields.AT_GO);
        if (goStr != null) {
            return goStr.split(":")[1];
        } else {
            return null;
        }
    }
    
    /**
     * setGOName
     * @param dbObj
     * @return 
     */
    public static String getGOName(BasicDBObject dbObj) {
        return getString(dbObj, NciPathwayFields.AT_NAME);
    }
    
    /**
     * 
     * Gene Ontology
     * 
     * Location information
     *  "@name" : "cellular_component",
     *  "@id" : "5000",
     *  "@parent_idref" : "28",
     *  "@GO" : "GO:0005575"
     * 
     *@param obj 
     */
    public static GeneOntology getGeneOntology(BasicDBObject obj, Subgraph subGraph) throws NoSuchFieldException, NoSuchFieldException, NoSuchFieldException, NoSuchFieldException, NoSuchFieldException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, InvocationTargetException, IOException, RuntimeException, InterruptedException, HttpException {
        if (obj != null) {
            String goId = getGeneOntologyIdentifier(obj);
            if (goId != null) {
              GeneOntologyObo.getGeneOntology(goId, subGraph);  
            }
                /*
                GeneOntology goEntity = (GeneOntology)getBioEntityFromBioType(subGraph, BioTypes.GENE_ONTOLOGY, BioFields.GENE_ONTOLOGY_ID, goId);
                if (goEntity == null) {
                    goEntity = new GeneOntology();
                    goEntity.setGeneOntologyId(goId);
                    goEntity.setName(getGOName(obj));
                    subGraph.add(goEntity);
                } 
                return goEntity;
            } */
        } 
        return null;
    }

    /**
     * Process location GO objects
     * @param ontologyObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception
     */
    public static void setLocationList(BasicDBList ontologyObj, Subgraph subGraph) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, InvocationTargetException, IOException, RuntimeException, InterruptedException, HttpException {
         for (Object obj : ontologyObj) {
             if (isLocation((BasicDBObject)obj)) {
                for (Object lObj : ontologyObj) {
                     BasicDBObject labelObj = (BasicDBObject)lObj;
                     if (isGO(labelObj)) {
                         GeneOntology geneOntology = getGeneOntology(labelObj, subGraph);
                        // geneOntology.setLocation(getGOName(labelObj));
                         locationMap.put(getGOName(labelObj),geneOntology);
                     }
                }  
             }
         }
    }  

    /**
     * process_function
     * "LabelValueList" : [
     * {
     *     "@name" : "JNK cascade",
     *      "@id" : "97",
     *     "@parent_idref" : "19000",
     *      "@GO" : "GO:0007254"
     * @param ontologyObj
     * @param subGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     * @throws RuntimeException
     * @throws Exception
     */
    public static void setProcessList(BasicDBList ontologyObj, Subgraph subGraph) throws NoSuchFieldException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, InvocationTargetException, IOException, RuntimeException, InterruptedException, HttpException {
          for (Object obj : ontologyObj) {
             if (isLocation((BasicDBObject)obj)) {
                for (Object lObj : ontologyObj) {
                     BasicDBObject labelObj = (BasicDBObject)lObj;
                     if (isGO(labelObj)) {
                         GeneOntology geneOntology = getGeneOntology(labelObj, subGraph);
                        // geneOntology.setProcessType(getGOName(labelObj));
                         processMap.put(getGOName(labelObj),geneOntology);
                     }
                }  
             }
         }
    }

    /**
     * check if this process-type
     * @param dbObj
     * @return
     */
    public static boolean isProcessType(BasicDBObject dbObj) {
        String name = getString(dbObj, NciPathwayFields.AT_NAME);
        return name.equals(getStringFromEnum(NciPathwayFields.PROCESS_TYPE));
    }
    
    
    /**
     * exists: Does this field exist
     * @param map
     * @param field (@link NciPathwayFields}
     * @return 
     */
    private static boolean exists(BasicDBObject map, NciPathwayFields field) {
        return map.get(field.toString()) != null;
    }

    /**
     *
     * @param list
     * @param field
     * @return
     */
    private static String getListAsString(BasicDBList list, NciPathwayFields field) {
       // BasicDBList list = getBasicDBList(interaction, NciPathwayFields.EVIDENCE_LIST);
        StringBuilder str = new StringBuilder();
        for (Object obj : list) {
            str.append(getString((BasicDBObject)obj, field));
            str.append(" ");
        }
        return str.toString();
    }
   
    private static String getStringFromMap(Map mapObj, String field) {
        return (String)mapObj.get(field);
        
    }
    
    private static String getStringFromEnum(Enum field) {
       return field.toString();    
    }
    
    private static String getString(BasicDBObject map,  NciPathwayFields field) {
        return (String)map.get(getStringFromEnum(field));
    }
    
    private static String getString(BasicDBObject molecule, BioFields field) {
        return (String)molecule.getString(getStringFromEnum(field));
    }
    
    private static BasicDBObject getDBObject(BasicDBObject map, NciPathwayFields field) {
        return (BasicDBObject)map.get(field.toString());
    }
   
    private static BasicDBList getDBList(BasicDBObject dbObject, NciPathwayFields field) {
        if (dbObject == null || field == null) {
            throw new RuntimeException("dbObject is null for field " + field);
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
    
    private static boolean isObjectString(BasicDBObject dbObject, NciPathwayFields field) {
        Object obj = dbObject.get(field.toString());
        return String.class.equals(obj.getClass());
    }
    
    /**
     * This returns a string with space between the strings
     * @param map
     * @param field
     * @return String
     */
    private static String getStringFromList(BasicDBObject map, NciPathwayFields field) {
        BasicDBList dbList = getBasicDBList(map, field);
        if (dbList != null) {
            String[] list = dbList.toArray(new String[0]);
            StringBuilder sb = new StringBuilder();
            for (String str : list) {
                sb.append(str);
                sb.append(" ");
            }
            return sb.toString();
        } else {
            return null;
        }
    }
    
    public static Organism getOrganism(BasicDBObject molecule, Subgraph subGraph) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, NoSuchFieldException, IllegalArgumentException, InvocationTargetException {
       log.info("getOrganism()");
        String taxId = getNcbiTaxId(molecule);
        Object bio = getBioEntityFromIndex(IndexNames.NCBI_TAX_ID, BioFields.NCBI_TAX_ID, taxId, subGraph);
        
        boolean created = false;
        Organism organism = (Organism)bio;
        if (organism == null) {
            organism = new Organism();
            organism.setNcbiTaxId(taxId);
            created = true;
        }
        organism.setOrganismShortLabel(molecule.getString(NciPathwayFields.ORGANISM.toString()));
        //log.info("organism OrganismshortLabel =" + organism.getOrganismShortLabel());
        if (created) {
            subGraph.add(organism);
        }
        return organism;    
    }

    /**
     * returns organism ncbitaxid
     * @param obj
     * @return
     */
    private static String getNcbiTaxId(BasicDBObject obj) {
        return "9606";
       //return (String)obj.get(getStringFromEnum(NciPathwayFields.ORGANISM)); 
    }
    
    /**
     * 
     * @param pathwayInfo
     * @return 
     */
    private static String getSourceId(BasicDBObject pathwayInfo) {
        return (String)((BasicDBObject)pathwayInfo.get(getStringFromEnum(NciPathwayFields.SOURCE))).get(MongoFields.NCI_ID.toString());
    }
    
    private static String getSource(BasicDBObject pathwayInfo) {
        return(String)((BasicDBObject)pathwayInfo.get(getStringFromEnum(NciPathwayFields.SOURCE))).get(getStringFromEnum(NciPathwayFields.NCI_SOURCE_TEXT));
    }
   
    /**
     * createPathway
     * @param pathwayObj
     * @param subGraph 
     */
    private static NciPathway getPathway(BasicDBObject pathwayObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ClassNotFoundException, InstantiationException, URISyntaxException, IOException, HttpException {
        log.info("getPathway()");
        NciPathway pathwayEntity = (NciPathway)getBioEntityFromBioType(subGraph, BioTypes.NCI_PATHWAY, BioFields.PATHWAY_SHORT_NAME, getString(pathwayObj, NciPathwayFields.SHORT_NAME));
        
        boolean created = true;
        if (pathwayEntity == null) {
            String cStr;
            if ((cStr = getString(pathwayObj, NciPathwayFields.SHORT_NAME)) != null) {
                pathwayEntity = new NciPathway(cStr, BioTypes.NCI_PATHWAY.toString(), getString(pathwayObj, NciPathwayFields.SHORT_NAME));
                created = true;
            }
        } 
        if (pathwayEntity != null) {  
            String cStr;
            if ((cStr = getString(pathwayObj, NciPathwayFields.SUBNET)) != null) {
                pathwayEntity.setSubNet(cStr);
            }
            if (exists(pathwayObj, NciPathwayFields.ORGANISM)) {
                if (isObjectString(pathwayObj, NciPathwayFields.ORGANISM)) {
                    processOrganism(pathwayEntity, pathwayObj, subGraph);             
                } else {
                    BasicDBList list = getDBList(pathwayObj, NciPathwayFields.ORGANISM);
                    processOrganismList(pathwayEntity, list, subGraph);    
                }
            }
            if (exists(pathwayObj, NciPathwayFields.CURATOR_LIST)) {
                if ((cStr = getStringFromList(pathwayObj, NciPathwayFields.CURATOR_LIST)) != null) {
                pathwayEntity.setCuratorList(cStr);
                }
            }
            if (exists(pathwayObj, NciPathwayFields.REVIEWER_LIST)) {
                if ((cStr = getStringFromList(pathwayObj, NciPathwayFields.REVIEWER_LIST)) != null) {
                pathwayEntity.setReviewerList(cStr);
                }
            }
            if ((cStr = getString(pathwayObj, NciPathwayFields.PATHWAY_ID)) != null) {
                pathwayEntity.setPathwayId(cStr);
            }

            if ((cStr = getSource(pathwayObj)) != null) {
                pathwayEntity.setSourceText(cStr);
            }

            if ((cStr = getString(pathwayObj, NciPathwayFields.LONG_NAME)) != null) {
                pathwayEntity.setLongName(cStr);
            }

            if ((cStr = getSourceId(pathwayObj)) != null) {
                pathwayEntity.setSourceId(cStr);
            }           
        }
        subGraph.add(pathwayEntity);
        return pathwayEntity;
    }
    
    /**
     * getBioEntity
     * @param key
     * @param value
     * @return
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws URISyntaxException 
     */
    
    private static Object getBioEntity(BioFields key, String value, Subgraph subGraph) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException, NoSuchFieldException {
        return subGraph.search(key, value);
    }
   
    
    /**
     * 
     * Creates a pathway BioEntity 
     * {@link NciPathway}
     * Pathway
     * @param name
     * @return
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException 
     */
    public <BioEntity> NciPathway createPathwayNode(String name) throws java.io.IOException, java.net.URISyntaxException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException, HttpException, NoSuchFieldException, RuntimeException, InterruptedException {
        
        Map map = NCIPathwayUtil.getPathwayObject(name);
        /**
         * provides information on PathwayComponentList
         */
        BasicDBObject pathwayObj = NCIPathwayUtil.getPathwayInfo(map);
        BasicDBList interactionList = NCIPathwayUtil.getInteractionList(map);
        BasicDBList moleculeList = NCIPathwayUtil.getMoleculeList(map);
        BasicDBList ontologyList = NCIPathwayUtil.getOntology(map);
        
        Subgraph subGraph = new Subgraph();
        relGraph = new ArrayList<>();
        NciPathway pathwayEntity;
        
        log.info("moleculeList.size() =" + moleculeList.size());
        log.info("moleculeList =" + moleculeList.toString()); 
        log.info("interactionList.size()" + interactionList.size());
        log.info("interactionList = " + interactionList.toString());
        
        setProcessList(ontologyList, subGraph);
        setLocationList(ontologyList, subGraph);
        
        try {
            pathwayEntity = getPathway(pathwayObj, subGraph);
            if (pathwayEntity != null) {
                processMoleculeList(pathwayEntity, moleculeList, subGraph);
                processInteractionList(pathwayEntity, interactionList, subGraph);
            } 
        } catch(Exception e) {
            log.error("Error in processing pathway" + e.getMessage());
            throw new RuntimeException(e);
        }
        
        subGraph.traverse(); 
        processRelGraph(subGraph, relGraph);
        PersistenceTemplate.saveSubgraph(subGraph);
        return pathwayEntity;
    }
    
    private static void createRelGraph(Object startEntity, String idRef, BioFields key, Object molecule, Class entityClass) {
        RelQueue relQueue = new RelQueue();
        relQueue.add(startEntity, idRef, key, molecule, entityClass);
        relGraph.add(relQueue);
    }
    
    private static void traverseRelGraph() {
        for (int i = 0; i < relGraph.size(); i++) {
           RelQueue rel = relGraph.get(i);
           if (rel != null) {
              log.info("moleculeId " + rel.getEndNodeId());
              log.info("relationship " + rel.getRelClassName().getSimpleName());
              log.info("startEntity " + BioTypes.fromString(rel.getStartNode().getClass().getSimpleName()));
           }
        }
    }
    
    
    /**
     * creates two sets of FamilyMemberRelationships of {@link org.atgc.bio.meta.StartNode} {@link EndNode}
     * {@link org.atgc.bio.meta.StartNode} {@link BioRelTypes#CONTAINS}
     *           set with GeneOntology values (process and location
     * {@link EndNode}  {@link BioRelTypes#IS_A_COMPONENT_OF}
     *           set with GeneOntology Values
     * 
     * @param molecule
     * @param startEntity
     * @param endEntity
     * @throws IllegalAccessException
     * @throws NoSuchMethodException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException 
     */
    private static void setFamilyMemberRelation(BasicDBObject molecule, Object startEntity, Object endEntity) throws IllegalAccessException, NoSuchMethodException, IllegalArgumentException, InvocationTargetException {
         String METHOD_NAME = "setFamilyMemberRelation";
         //((NamedProtein)startEntity).setFamilyMemberRelation(relation);
         Class<?>[] params = {BioRelationClasses.FAMILY_MEMBER_RELATION.getAnnotationClass()};
         {
            FamilyMemberRelation relation = new FamilyMemberRelation(startEntity, endEntity, BioRelTypes.CONTAINS);
            if (labelExists(molecule)) {
                Map map = getLabelValues(molecule);
                relation.setLabels(map);
                setGeneOntologyRelationship(startEntity, map);
            }   
            Object[] values = {relation};
            NciPathwayTemplate.invokeMethod(startEntity, METHOD_NAME, params, values);
         }
         
         log.info("calling setFamilyMemberRelation(), " + endEntity.toString());
         FamilyMemberRelation relation = new FamilyMemberRelation(endEntity, startEntity, BioRelTypes.IS_MEMBER_OF_FAMILY);
         if (labelExists(molecule)) {
            Map map = getLabelValues(molecule);
            relation.setLabels(map);
            setGeneOntologyRelationship(endEntity, map);
         }   
         //NciPathwayTemplate.setFamilyMemberRelation(endEntity, relation);
         Object[] values = {relation}; 
         NciPathwayTemplate.invokeMethod(endEntity, METHOD_NAME, params, values);     
   }

    /**
     * late binding of relationships for the molecule entities
     * @param subGraph
     * @param relGraph
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws UnknownHostException
     * @throws HttpException
     * @throws InvocationTargetException
     * @throws NoSuchMethodException
     */
    private static void processRelGraph(Subgraph subGraph, ArrayList<RelQueue> relGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException, InvocationTargetException, NoSuchMethodException {
       for (int i = 0; i < relGraph.size(); i++) {
           RelQueue rel = relGraph.get(i);
           if (rel != null) {
              String idRef = rel.getEndNodeId();
              BioFields key = rel.getEndNodeKey();
              Object endEntity = getBioMolecule(idRef, key, subGraph);
              if (endEntity != null) {
                  log.info(endEntity.toString() + " moleculeIdRef =" + idRef);
                  Object bioEntity = rel.getStartNode();
                  Object dbObj = rel.getRelInfo();
                  if (BioRelationClasses.COMPLEX_COMPONENT_RELATION.equals(rel.getRelClassName())) {
                      setLabelValues((Complex)bioEntity, endEntity, (BasicDBObject)dbObj);
                  } else if (BioRelationClasses.PTM_EXPRESSION_RELATION.equals(rel.getRelClassName())) {
                      BioTypes bioType = BioTypes.fromString(bioEntity.getClass().getSimpleName());
                      log.info("bioType " + bioType);
                      if (bioType.equals(BioTypes.COMPLEX)) {
                          createComplexPTMRelationships((Complex)bioEntity, endEntity, (BasicDBObject)dbObj);
                      } else if (bioType.equals(BioTypes.NAMED_PROTEIN)) {
                          setPTMExpression((BasicDBObject)dbObj, bioEntity, endEntity);
                      } 
                  } else if (BioRelationClasses.FAMILY_MEMBER_RELATION.equals(rel.getRelClassName())) {
                      setFamilyMemberRelation((BasicDBObject)dbObj, bioEntity, endEntity);
                  } else if (BioRelationClasses.PART_MOLECULE_RELATION.equals(rel.getRelClassName())) {
                      setPartMoleculeRelation((BasicDBObject)dbObj, bioEntity, endEntity);
                  } else if (BioRelationClasses.ONCO_RELATION.equals(rel.getRelClassName())) {
                      setOncoRelation((Map)dbObj, endEntity, bioEntity);              
                  }
                  //foundList.add(rel.copy());
              } else {
                  //notFoundList.add(rel);
              } 
           }
       }   
    }
    
    /*
    private static void processFinal(Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException, , InvocationTargetException, NoSuchMethodException {
        notFoundList = new <RelQueue>ArrayList();
        foundList = new <RelQueue>ArrayList();
        processRelGraph(subGraph, notFoundList);
        for (int i = 0; i < foundList.size(); i++) {
            RelQueue rel = foundList.get(i);
            if (rel != null) {
                notFoundList.remove(rel);
            }
        }
        log.info("notFoundList =" + notFoundList.size());
        log.info("foundList =" + foundList.size());
    }
    * 
    */
    
    
}