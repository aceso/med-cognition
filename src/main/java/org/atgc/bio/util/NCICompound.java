/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.apache.avro.generic.GenericData;
import org.atgc.bio.*;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;
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
import org.atgc.bio.BioFields;
import org.atgc.bio.NCICompoundUtil;
import org.atgc.bio.NciFields;
import org.atgc.bio.domain.*;
import org.neo4j.graphdb.*;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;


/**
 *
 * @author jtanisha-ee
 */
@SuppressWarnings("javadoc")
public class NCICompound {
    
    protected static Logger log = LogManager.getLogger(NCICompound.class);

     /**
     * This method uses compound collection and adds genesymbol, importstatus 
     * and date to Druglistcollection importstatus - due is by default.
     * @throws UnknownHostException
     */
    /*public void addDrugList() throws UnknownHostException {
         NCICompoundUtil.addCompoundList();
    } */

     public static BasicDBObject getZeroObject(Map diseaseObject) {
         BasicDBObject zeroObject = (BasicDBObject)diseaseObject.get(MongoFields.ZERO.toString());
         return zeroObject;
     }

    public static void processSpecificGenes() throws UnknownHostException {
        List<String> genes = new ArrayList<>();
        genes.add("MKI67");
        genes.add("PTGS2");
        genes.add("FOS");
        genes.add("TYMS");
        for (int i = 0; i < genes.size(); i++ ) {
            long beginTime = System.currentTimeMillis();

            String geneSymbol;
            //geneSymbol = "CCND1";
            //geneSymbol = "KRAS";
            //geneSymbol = "VEGFA";
            geneSymbol = genes.get(i);
            log.info("******* geneSymbol =" + geneSymbol);
            if (StatusUtil.idExists(BioTypes.COMPOUND, MongoFields.HUGO_GENE_SYMBOL, geneSymbol)) {
                log.info("Compound for hugoGeneSymbol " + geneSymbol + " already imported.");
                continue;
            }
            if (null == geneSymbol) {
                log.warn("geneSymbol is null");
                continue;
            }
            try {
                long startTime = System.currentTimeMillis();
                processCompound(geneSymbol);
                log.info("Time for processCompound " + geneSymbol + " = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();
                StatusUtil.idInsert(BioTypes.COMPOUND, MongoFields.HUGO_GENE_SYMBOL, geneSymbol);
                log.info("Time for idInsert " + geneSymbol + " = " + (System.currentTimeMillis() - startTime));
                //NCICompoundUtil.updateImportStatus(geneSymbol, BioEntityType.DONE);
            } catch (Exception e) {
                //NCICompoundUtil.updateImportStatus(geneSymbol,  BioEntityType.ERROR);
                throw new RuntimeException(e);
            }
            log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
            log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
            log.info("Time for NCICompound iteration " + geneSymbol + " = " + (System.currentTimeMillis() - beginTime));
            break;
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
        log.info("Completed successfully!");
    }
   
    public static void main(String[] args) throws java.io.IOException {

        //processSpecificGenes();

        /** UNCOMMENT THIS LATER after above method processSpecificGenes() is executed.
         *
         */
        List<Map> drugList = NCICompoundUtil.getNciCompounds();
        Iterator<Map> drugIter = drugList.iterator();
       
        for (int i = 0; i < drugList.size(); i++ ) {
            long beginTime = System.currentTimeMillis();
            Map map = drugIter.next();
            log.info(i + "map =" + map.toString());
            String geneSymbol = (String)getZeroObject(map).get(MongoFields.HUGO_GENE_SYMBOL.toString());
            //geneSymbol = "CCND1";
            //geneSymbol = "KRAS";
            //geneSymbol = "VEGFA";
            //geneSymbol = "MKI67";
            log.info("******* geneSymbol =" + geneSymbol);
            if (StatusUtil.idExists(BioTypes.COMPOUND, MongoFields.HUGO_GENE_SYMBOL, geneSymbol)) {
                log.info("Compound for hugoGeneSymbol " + geneSymbol + " already imported.");
                continue;
            }
            if (null == geneSymbol) {
                log.warn("geneSymbol is null");
                continue;
            }
            try {
                long startTime = System.currentTimeMillis();
                processCompound(geneSymbol);
                log.info("Time for processCompound " + geneSymbol + " = " + (System.currentTimeMillis() - startTime));
                startTime = System.currentTimeMillis();
                StatusUtil.idInsert(BioTypes.COMPOUND, MongoFields.HUGO_GENE_SYMBOL, geneSymbol);
                log.info("Time for idInsert " + geneSymbol + " = " + (System.currentTimeMillis() - startTime));
                //NCICompoundUtil.updateImportStatus(geneSymbol, BioEntityType.DONE);
            } catch (Exception e) {
                //NCICompoundUtil.updateImportStatus(geneSymbol,  BioEntityType.ERROR);
                throw new RuntimeException(e);
            }
            log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
            log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
            log.info("Time for NCICompound iteration " + geneSymbol + " = " + (System.currentTimeMillis() - beginTime));
            break;
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
        log.info("Completed successfully!");

        /*   try {
             createGeneNode("PKP4");        
        } catch(Exception e) {
            throw new RuntimeException("PKP4" + e.getMessage(), e);
        } */
    }
        
    
     /**
    * Retrieves UniprotID identifier
    * @param obj
    * @return String
    */
   private static String getUniProtId(BasicDBObject obj) {
       log.info("Extracting uniprotId from BasicDBObject: " + obj.toString());
        return getString(obj, NciFields.UNIPROT_ID);
   }
  
   private static String getGenBankAccession(BasicDBObject obj) {
       BasicDBList acc = (BasicDBList)obj.get(NciFields.GEN_BANK_ACCESSION.toString());
       return acc.toString();
   }
   
   private static String getRefSeqId(BasicDBObject obj) {
       return getString(obj,  NciFields.REFSEQ_ID);    
   }
   
   private static String getLocusLinkId(BasicDBObject obj) {
       return getString(obj, NciFields.LOCUS_LINK_ID);
   }
   
   private static String getHgncId(BasicDBObject obj) {
       return getString(obj, NciFields.HGNC_ID);
   }
   
   private static String getString(BasicDBObject map,  NciFields field) {
        return (String)map.get(OntologyStrUtil.getStringFromEnum(field));
   }
  
   private static String getCellLineIndicator(BasicDBObject obj) {
       return getString(obj, NciFields.CELL_LINE_INDICATOR);
   }
 
   private static String getNegationIndicator(BasicDBObject obj) {
       return getString(obj, NciFields.NEGATION_INDICATOR);    
   }
   
   public static Protein getProtein(String proteinId, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException, ServiceException, HttpException, InterruptedException, IOException {
        getBioEntityFromBioType(subGraph, BioTypes.PROTEIN, BioFields.UNIPROT_ID, proteinId);
        return UniprotUtil.getProtein(proteinId, subGraph);
    } 
    
    public static BasicDBObject getSequenceIdentificationObj(BasicDBObject drugInfo) {
        return (BasicDBObject)drugInfo.get(NciFields.SEQUENCE_IDENTIFICATION_COLLECTION.toString());
    }
    
    public static void setSequenceIdentificationRelation(BasicDBObject sequence, Protein protein, HashSet<Gene> geneSet) throws java.io.IOException, java.net.URISyntaxException {
        getUniProtId(sequence);
        if (sequence != null) {
            if (geneSet != null && geneSet.size() > 0) {
                Object[] objList = geneSet.toArray();
                Gene gene = (Gene)objList[0];
                if (gene != null) { 
                    gene.setSequenceIdentificationRelation(
                            protein,
                            getHgncId(sequence), 
                            getLocusLinkId(sequence), 
                            getGenBankAccession(sequence),
                            getRefSeqId(sequence)); 
                }
            }
        }
    }

    /**
     * getDrug
     * @param dbObj
     * @param subGraph
     * @return Drug
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws URISyntaxException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static Drug getDrug(BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException {
        String drugTerm = OntologyStrUtil.getString(dbObj, NciFields.DRUG_TERM);
        //Object bio = getBioEntityFromBioType(subGraph, BioTypes.NCI_DRUG, BioFields.NCI_DRUG_TERM, drugTerm);
        Object bio = getBioEntityFromBioType(subGraph, BioTypes.DRUG, BioFields.DRUG_NAME, drugTerm);

        boolean created = false;
        Drug drug = (Drug)bio;
        if (drug == null) {
            drug = new Drug();
            drug.setDrugName(drugTerm);
            drug.setDrugCode(OntologyStrUtil.getString(dbObj,NciFields.NCI_DRUG_CONCEPT_CODE));
            created = true; 
        }
        if (created) {
            subGraph.add(drug);
        }
        return drug;
    }
    
    public static BasicDBObject getDrugData(BasicDBObject dbObj) {
        return OntologyStrUtil.getDBObject(dbObj, NciFields.DRUG_DATA);    
    }
    
    public static BasicDBObject getGeneData(BasicDBObject dbObj) {
        return OntologyStrUtil.getDBObject(dbObj, NciFields.GENE_DATA);
    }
    
    public static String getPubMedId(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, NciFields.PUBMED_ID);
    }
    
    public static String getStatement(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, NciFields.STATEMENT);
    }
    
    public static String getOrganismShortLabel(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, NciFields.ORGANISM)) {
           return OntologyStrUtil.getString(dbObj, NciFields.ORGANISM);
        } else {
           return null;
        }
    }
    
    public static String getComments(BasicDBObject dbObj) {
         if (OntologyStrUtil.isObjectString(dbObj, NciFields.COMMENTS)) {
            return getString(dbObj, NciFields.COMMENTS);
        } else {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, NciFields.COMMENTS);
            StringBuffer sb = new StringBuffer();
            for (Object obj : dbList) {
                if (obj != null) {
                   sb.append(obj.toString());
                   sb.append(",");
                }
            }
            return sb.toString();
        } 
    }
    
    public static NciSentenceStatusFlags getSentenceStatusFlag(BasicDBObject dbObj) {
        return NciSentenceStatusFlags.fromString(OntologyStrUtil.getString(dbObj, NciFields.SENTENCE_STATUS_FLAG));
    }
    
    /**
     * Actually an alias of a gene in a sentence
     * @return 
     */
    public static String getMatchedGeneTerm(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, NciFields.MATCHED_GENE_TERM);
    }
    
    public static String getNciDrugConceptCode(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, NciFields.NCI_DRUG_CONCEPT_CODE);
    }
    
    public static String getDrugTerm(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, NciFields.DRUG_TERM);
    }
    
    public static NciDrugGeneRoleRelation getDrugGeneRoleRelation(Drug drug, Gene gene, BasicDBObject dbObj) {
        String geneTerm = getMatchedGeneTerm(getGeneData(dbObj));
        List<String> roleList = getGeneRoles(getGeneRolesObj(dbObj));
        NciDrugGeneRoleRelation roleRelation = new NciDrugGeneRoleRelation(drug, gene, geneTerm);
        if (roleList != null && !roleList.isEmpty()) {
           setDrugGeneRoles(roleRelation, roleList);
        }
        return roleRelation;
    }
    
    public static String getPrimaryRole(BasicDBObject dbObj) {
        return getString(dbObj, NciFields.PRIMARY_NCI_ROLE_CODE);    
    } 
    
    public static List<String> getPrimaryRoleList(BasicDBObject dbObj, List<String> roleList) {
        if (OntologyStrUtil.isObjectString(dbObj, NciFields.PRIMARY_NCI_ROLE_CODE)) {
           String role = getPrimaryRole(dbObj);
           if (!StrUtil.isNull(role)) {
               roleList.add(role);
           }
        } else {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, NciFields.PRIMARY_NCI_ROLE_CODE);
            for (Object obj : dbList) {
                if (obj != null) {
                    if (!StrUtil.isNull(obj.toString())) {
                        roleList.add(obj.toString());
                    }
                }
            }
        }
        return roleList;
    }
    
    public static String getOtherRole(BasicDBObject dbObj) {
        return getString(dbObj, NciFields.OTHER_ROLE);    
    }
    
    public static List<String> getOtherRoleList(BasicDBObject dbObj, List<String> roleList) {
        if (OntologyStrUtil.isObjectString(dbObj, NciFields.OTHER_ROLE)) {
            String role = getOtherRole(dbObj);
            if (!StrUtil.isNull(role)) {
               roleList.add(role);
            }
        } else {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, NciFields.OTHER_ROLE);
            for (Object obj : dbList) {
                if (obj != null) {
                    if (!StrUtil.isNull(obj.toString()))
                       roleList.add(obj.toString());
                }
            }
         }
        return roleList;
    }
    
    public static String getObjectListString(Object[] objList) {
        //log.info("objList.length()" + objList.length);
        StringBuilder sb = new StringBuilder();
        for (Object obj : objList) {
            if (obj != null) {   
                sb.append(obj.toString());
                sb.append(" ");
            }
        }
        return sb.toString();
    }
     
    /**
     * ROLES is a string when it's value is  "not-assigned" which indicates
     * there are no roles in it. Perform the check using isObjectString
     * @param dbList
     * @return 
     */
    public static List<String> getGeneRoles(BasicDBList dbList) {
        if (dbList == null || dbList.isEmpty()) {
            return null;
        } else { 
            List<String> roleList = new ArrayList<>();
            for (Object obj : dbList) {
                if (!OntologyStrUtil.isObjectString(obj)) {
                    BasicDBObject dbObj = (BasicDBObject)obj;
                    if (dbObj != null) {
                        roleList = getPrimaryRoleList(dbObj, roleList);
                        //log.info("roleList of Primary Roles" + roleList.size() + " list=" + roleList.toString());
                        roleList = getOtherRoleList(dbObj, roleList);
                        if (roleList.isEmpty()) {
                            String role = dbObj.toString();
                            if (!StrUtil.isNull(role)) {
                                roleList.add(role);
                            }
                        } 
                    }
                }
            }
            return roleList;
        }
    }
    
    /**
     * If ROLES is "not-assigned", return null
     * @param dbObj {@link NciGeneDrugRoles}
     * @return 
     */
    public static BasicDBList getGeneRolesObj(BasicDBObject dbObj) {
        //log.info("getGeneRolesObj() =" + dbObj.toString());
         if ( (dbObj == null) 
            || (dbObj.toString() == null)
            || (!OntologyStrUtil.isObjectNull(dbObj, NciFields.ROLES))
            || (OntologyStrUtil.isObjectString(dbObj, NciFields.ROLES))) {
             return null;
         } else {  
             return OntologyStrUtil.getBasicDBList(dbObj, NciFields.ROLES);   
         } 
    }
    
    public static String getGeneStatusFlag(BasicDBObject dbObj) {
        return getString(dbObj, NciFields.GENE_STATUS_FLAG);
    }
   
    /**
     * roleList contains roles {Gene-has-Therapeutic-Relevance} which
     * gets mapped to enum {@link NciGeneDrugRoles#NciGeneDrugRoles(String)}
     * @param roleRelation
     * @param roleList 
     */
    public static void setDrugGeneRoles(NciDrugGeneRoleRelation roleRelation, List<String> roleList ) {
         for (String str : roleList) {
             if (str != null) {
                //log.info(str);
                if (NciGeneDrugRoles.getEnum(str) == null) {
                    log.info(str);
                } 
                roleRelation.setRole(NciGeneDrugRoles.getEnum(str));
             }
         }  
    }
    
    public static String getEvidenceCode(BasicDBObject dbObj) {
        return getString(dbObj, NciFields.EVIDENCE_CODE);
    }
    
    public static List<String> getEvidenceCodes(BasicDBObject dbObj) {
        //log.info("getEvidenceCode() = " + dbObj.toString());
        if ((dbObj == null) 
            || (dbObj.toString() == null) 
            || (!OntologyStrUtil.isObjectNull(dbObj, NciFields.EVIDENCE_CODE))) {
            return null;
        } else {
           if (OntologyStrUtil.isObjectString(dbObj, NciFields.EVIDENCE_CODE)) {
                String code = getEvidenceCode(dbObj);
                //log.info("getEvidenceCode() " + code);
               List<String> codeList = new ArrayList<>();
               codeList.add(code);
               return codeList;    
           } else {
               BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, NciFields.EVIDENCE_CODE);
               if (dbList != null && !dbList.isEmpty()) {
                   List<String> codeList = new ArrayList<>();
                   for (Object obj : dbList) {
                        codeList.add(obj.toString());
                   }
                   return codeList;
               }
           }
        }
        return null;
    }
    
    public static void setCellLineIndicator(NciDrugGeneEvidenceRelation relation, BasicDBObject dbObj) {
          if (OntologyStrUtil.isObjectString(dbObj, NciFields.CELL_LINE_INDICATOR)) {
              relation.setCellLineIndicator(getCellLineIndicator(dbObj));
          } else {
              OntologyStrUtil.getBasicDBList(dbObj, NciFields.CELL_LINE_INDICATOR);
              //log.info("CellLineIndicator is null");
          }
    }
    
    public static void setNegationIndicator(NciDrugGeneEvidenceRelation relation, BasicDBObject dbObj) {
          if (OntologyStrUtil.isObjectString(dbObj, NciFields.NEGATION_INDICATOR)) {
              relation.setNegationIndicator(getNegationIndicator(dbObj));
          } else {
              OntologyStrUtil.getBasicDBList(dbObj, NciFields.NEGATION_INDICATOR);
          }
    }
    
    /**
     * EvidenceCodes appear as {@link EvidenceCodes} EV-IC.
     * These need to be mapped to EV_IC
     * Cellline indicator and negation indicator indicate whether the evidence came from cell line
     * or other sources.
     * @param drug
     * @param gene
     * @param dbObj
     * @return 
     */
    public static NciDrugGeneEvidenceRelation getDrugGeneEvidenceRelation(Drug drug, Gene gene, BasicDBObject dbObj) {
          List<String> codeList = getEvidenceCodes(dbObj);
          NciDrugGeneEvidenceRelation relation = new NciDrugGeneEvidenceRelation(drug, gene);
          if (codeList != null && !codeList.isEmpty()) {
              for (String str : codeList) {
                  //log.info("EvidenceCode for + " +  str + " Enum =" + EvidenceCodes.getEnumCode(str));
                  relation.setCode(EvidenceCodes.getEnumCode(str));
              }
          }
          setNegationIndicator(relation, dbObj);
          setCellLineIndicator(relation, dbObj);
          relation.setComments(getComments(dbObj));
          return relation;
    }
    
    /**
     * getPubMed()
     * @param dbObj
     * @return {@link PubMed}
     */
    public static PubMed getPubMed(BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException, IOException, HttpException, InterruptedException {
        String pubMedId = getPubMedId(dbObj);
        PubMed pubMed = (PubMed)getBioEntityFromBioType(subGraph, BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);
        if (pubMed == null) {
            pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
        } 
        return pubMed;
    }
    
    /**
     * getOrganism()
     * Organism : "Human"
     * @param dbObj
     * @param subGraph
     * @return {@link Organism}
     */
    public static Organism getOrganism(BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException {
        String organismShortLabel = getOrganismShortLabel(dbObj);
        if (StrUtil.isNull(organismShortLabel)) {
            return null;
        } else {
            Object bio = getBioEntityFromBioType(subGraph, BioTypes.ORGANISM, BioFields.ORGANISM, organismShortLabel);
            Organism organism = (Organism)bio;
            if (bio == null) {
                organism = new Organism();
                organism.setOrganismShortLabel(organismShortLabel);
                organism.setNcbiTaxId(NcbiTaxIdTypes.getNcbiTaxIdValue(organismShortLabel));
                subGraph.add(organism);
            }
            //log.info("getOrganism()");
            return organism;
        } 
    }
    
    /**
     * {@link NciPubMedGeneDrugDiseaseRelation}
     * @param pubMed
     * @param gene
     * @param dbObj 
     */
    public static NciPubMedGeneDrugDiseaseRelation getPubMedGeneRelation(PubMed pubMed, Gene gene, BasicDBObject dbObj) {
        NciPubMedGeneDrugDiseaseRelation rel = new NciPubMedGeneDrugDiseaseRelation();
        rel.setNciPubMedRelation(pubMed, gene, BioRelTypes.REFERENCES_PUBMED);
        rel.setPubMedStatement(getStatement(dbObj));
        rel.setSentenceStatusFlag(getSentenceStatusFlag(dbObj));
        return rel;
    }
    
    public static NciPubMedGeneDrugDiseaseRelation getPubMedDrugRelation(PubMed pubMed, Drug drug, BasicDBObject dbObj) {
        NciPubMedGeneDrugDiseaseRelation rel = new NciPubMedGeneDrugDiseaseRelation();
        rel.setNciPubMedRelation(pubMed, drug, BioRelTypes.REFERENCES_PUBMED);
        rel.setPubMedStatement(getStatement(dbObj));
        rel.setSentenceStatusFlag(getSentenceStatusFlag(dbObj));
        return rel;
    }
    
    public static String getNcbiTaxId(BasicDBObject dbObj) {
        String organismLabel = getOrganismShortLabel(dbObj);
        if (organismLabel != null) {
           return NcbiTaxIdTypes.getNcbiTaxIdValue(organismLabel);
        } else {
            return null;
        }
    }
   
    public static NcbiTaxonomy getNcbiTaxonomy(BasicDBObject dbObj, Subgraph subgraph) throws InvocationTargetException, NoSuchFieldException, IllegalAccessException, UnknownHostException {
       String ncbiTaxId = getNcbiTaxId(dbObj);
       return GeneGraphDBImportUtil.getNcbiTaxonomy(subgraph, ncbiTaxId);
    } 
      
    /**
     * processSentence()
     * 
     * @param zeroObject
     * @param geneSet {@link Gene}
     * @param subGraph {@link Subgraph}
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws NotFoundException
     * @throws InvocationTargetException 
     */
    public static void processSentence(BasicDBObject zeroObject, HashSet<Gene> geneSet, Subgraph subGraph) throws java.io.IOException, java.net.URISyntaxException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, NotFoundException, InvocationTargetException, HttpException, InterruptedException {
         BasicDBList dbList = OntologyStrUtil.getBasicDBList(zeroObject, NciFields.SENTENCE);
        
         if (dbList != null) {
             //log.info("Sentence()" + dbList.toString());
             /**
              * sentenceList
              */
             for (Object obj : dbList) {
                 BasicDBObject dbObj = (BasicDBObject)obj;
                 BasicDBObject drugData = getDrugData(dbObj);

                 Drug drug = getDrug(drugData, subGraph);
                 Gene gene = getGene(dbObj, geneSet);
                // log.info("drug " + drug.toString());
                 NciDrugGeneRoleRelation roleRelation = getDrugGeneRoleRelation(drug, gene, dbObj);
                 drug.setDrugGeneRelations(roleRelation);
                 NciDrugGeneEvidenceRelation eRelation = getDrugGeneEvidenceRelation(drug, gene, dbObj);
                 drug.setEvidenceRelations(eRelation);

                 PubMed pubMed = getPubMed(dbObj, subGraph);
                 if (pubMed != null) {
                     NciPubMedGeneDrugDiseaseRelation pubMedDrugRelation = getPubMedDrugRelation(pubMed, drug, dbObj);
                     NciPubMedGeneDrugDiseaseRelation pubMedGeneRelation = getPubMedGeneRelation(pubMed, gene, dbObj);
                     pubMed.setPubMedRelation(pubMedGeneRelation);
                     pubMed.setPubMedRelation(pubMedDrugRelation);
                 }

                 Organism organism = getOrganism(dbObj, subGraph);
                 setOrganismRelations(organism, gene, drug);
                 NcbiTaxonomy ncbiTaxonomy = getNcbiTaxonomy(dbObj, subGraph);
                 if (ncbiTaxonomy != null) {
                    drug.setNcbiTaxonomyRelation(ncbiTaxonomy);
                 }
             }
         }
    }
    
    
    
    public static void setOrganismRelations(Organism organism, Gene gene, Drug drug) {
          if (organism != null) {
              organism.setOrganismRelation(gene);
              organism.setOrganismRelation(drug);  
          }    
    }
     
    /**
     * NciCompound - drug/compound data
     * @param name
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws NotFoundException
     * @throws InvocationTargetException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws UnknownHostException
     * @throws HttpException 
     */
    public static void processCompound(String name) throws java.io.IOException, java.net.URISyntaxException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, NotFoundException, InvocationTargetException, UnsupportedEncodingException, MalformedURLException, UnknownHostException, HttpException, InterruptedException, ServiceException {
        
        log.info("processCompound()");
        long startTime = System.currentTimeMillis();
        Map map = NCICompoundUtil.getObject(name);
        log.info("Time for NCICompoundUtil.getObject: " + name + " = " + (System.currentTimeMillis()-startTime));
        if (null != map)
           log.info("map =" + map.toString());
        else {
            log.error("Did not get a compound " + name);
            return;
        }
        startTime = System.currentTimeMillis();
        BasicDBObject zeroObject = NCICompoundUtil.getZeroObject(map);
        log.info("Time for getZeroObject: " + name + " = " + (System.currentTimeMillis()-startTime));
        startTime = System.currentTimeMillis();
        String geneSymbol;
        if (zeroObject == null) {
            return;
        } else {   
            geneSymbol = NCICompoundUtil.getHugoGeneSymbol(zeroObject);
            log.info("************  geneSymbol " + geneSymbol);
        }
        log.info("Time for getHugoGeneSymbol: " + name + " = " + (System.currentTimeMillis()-startTime));
        startTime = System.currentTimeMillis();
        Subgraph subGraph = new Subgraph();
        HashSet<Gene> geneSet = getGeneSet(zeroObject, geneSymbol, subGraph);
        log.info("Time for getGeneSet: " + name + " = " + (System.currentTimeMillis()-startTime));
       // log.info("gene =" + gene.toString());
        startTime = System.currentTimeMillis();
        BasicDBObject sequence = getSequenceIdentificationObj(zeroObject);
        log.info("Time for getSequenceIdentificationObj: " + name + " = " + (System.currentTimeMillis()-startTime));
        startTime = System.currentTimeMillis();
        Protein protein = getProtein(getUniProtId(sequence),subGraph);
        log.info("Time for getProtein: " + name + " = " + (System.currentTimeMillis()-startTime));
        startTime = System.currentTimeMillis();
        if (protein != null) { 
           setSequenceIdentificationRelation(sequence, protein, geneSet);
        }
        log.info("Time for setSequenceIdentification: " + name + " = " + (System.currentTimeMillis()-startTime));
        startTime = System.currentTimeMillis();
        processSentence(zeroObject, geneSet, subGraph);
        log.info("Time for processSentence: " + name + " = " + (System.currentTimeMillis()-startTime));
        //log.info("processSentence()");
        //subGraph.traverse();
        startTime = System.currentTimeMillis();
        PersistenceTemplate.saveSubgraph(subGraph);
        log.info("Time for saveSubgraph: " + name + " = " + (System.currentTimeMillis()-startTime));
    }
    
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
   public static Object getBioEntityFromBioType(Subgraph subGraph, BioTypes bioType, BioFields key, String value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException {
       //log.info("getBioEntityFromBioType()," + bioType.toString() + "," + key.toString() + "=" + value);
       return subGraph.search(bioType, key, value);
   }
   
    public static Gene getGene(HashSet<Gene> geneSet, String taxId) {
        return GeneGraphDBImportUtil.getGene(geneSet, taxId);
   }
   
   public static Gene getGene(BasicDBObject dbObj, HashSet<Gene> geneSet) {
        String organismLabel = getOrganismShortLabel(dbObj);
        if (organismLabel != null) {
            String taxId = NcbiTaxIdTypes.getNcbiTaxIdValue(organismLabel);
            if (taxId != null) {
                return getGene(geneSet, taxId);
            }
        } else {
            log.info("organism not found");
            getGene(geneSet, NcbiTaxIdTypes.HUMAN.toString());
        } 
        return null;
        
   }
   
   public static HashSet<Gene> getGeneSet(BasicDBObject zeroObject, String geneSymbol, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException, InterruptedException, IOException, HttpException {
        return GeneGraphDBImportUtil.getGene(geneSymbol, subGraph);   
    }
}