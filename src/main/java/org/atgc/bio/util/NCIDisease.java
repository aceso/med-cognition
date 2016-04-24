/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.BioFields;
import org.atgc.bio.NCIDiseaseUtil;
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
import org.atgc.bio.NciFields;
import org.atgc.bio.domain.*;
import org.neo4j.graphdb.*;


/**
 *
 * @author jtanisha-ee
 */
public class NCIDisease {
    
    protected static Logger log = LogManager.getLogger(NCIDisease.class);

     /**
     * This method uses disease collection and adds genesymbol, importstatus 
     * and date to diseaselistcollection importstatus - due is by default.
     * @@throws UnknownHostException
     */
    public void addDiseaseList() throws UnknownHostException {
         NCIDiseaseUtil.addDiseaseList();
    }
    
    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }
   
    public static void main(String[] args) throws java.io.IOException {
        // gives diseaselist documents whose importstatus is due.

        //Q15904
        //Q8NFZ5,  tumor

        /*
        try {
           Subgraph subGraph  = new Subgraph();

           // getBioEntityFromIndex(BioFields propName, String propValue, IndexNames indexName)Object bioEntity = PersistenceTemplate.getBioEntityFromIndex(BioFields.UNIPROT_ID, "Q8NFZ5", IndexNames.UNIPROT_ID);
            Protein protein = (Protein)bioEntity;
            //Object bioE = getBioEntityFromBioType(subGraph, BioTypes.DISEASE, BioFields.DISEASE_TERM, "tumor");
            Object bioE = PersistenceTemplate.getBioEntityFromIndex(BioFields.DISEASE_TERM, "tumor", IndexNames.DISEASE_TERM);
            Disease disease = (Disease)bioE;
            log.info("disease =" + disease.getDiseaseTerm());


            log.info("protein =" + protein.getUniprot());
            disease.setProteinRelation(protein);
            //PersistenceTemplate.save(disease);
            BioRelation bioRel = disease.getProteinRelation();
            Protein endObj = (Protein)bioRel.getEndNode();
            if (endObj != null) {
                log.info("endNode protein =" + endObj.getUniprot());
                log.info("rel = " + bioRel.getName() + " endobjid=" + endObj.getMessage());
                log.info("disease nodeid =" + disease.getMessage());

            }
        } catch (Exception e) {
            throw new IOException("exception " + e.getMessage(), e);
        }
        System.exit(0);
        */


        DBCursor dbCursor = getCollection(ImportCollectionNames.NCI_DISEASE).findDBCursor("{}" );
        int i = 0;
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                log.info("result = " + result);
                
                //String geneSymbol = (String)result.get(BioEntityType.ZERO_HUGO_GENE_SYMBOL.toString());
                //log.info("******* geneSymbol =" + geneSymbol);
                // if (geneSymbol.equals("CACNA1G")) {
                    try {
                        processDisease(result);
                        //NCIDiseaseUtil.updateImportStatus(geneSymbol, BioEntityType.DONE);
                    } catch (Exception e) {
                        //NCIDiseaseUtil.updateImportStatus(geneSymbol,  BioEntityType.ERROR);
                        throw new RuntimeException(e);
                    }
                //}
                log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
                log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
                if (i == 1) {
                    break;
                }
                i++;
            }
        } finally {
            dbCursor.close();
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
    }
        
    
     /**
    * Retrieves UniprotID identifier
    * @param obj
    * @return 
    */
   private static String getUniProtId(BasicDBObject obj) {
       log.info("getUniProtId, protein id =" + getString(obj, NciFields.UNIPROT_ID));
        return getString(obj, NciFields.UNIPROT_ID);
   }
  
   private static String getGenBankAccession(BasicDBObject obj) {
       BasicDBList acc = (BasicDBList)obj.get(NciFields.GEN_BANK_ACCESSION.toString());
       if (acc != null) { 
           return acc.toString();
       } 
       return null;
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
   
   public static Protein getProtein(String proteinId, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException, Exception {
        if (proteinId == null || proteinId.equals("-")) {
            log.info("proteinId = -" + proteinId);
            return null;
        }
       log.info("proteinId = -" + proteinId);
        Object bio = getBioEntityFromBioType(subGraph, BioTypes.PROTEIN, BioFields.UNIPROT_ID, proteinId);
        Protein protein = (Protein)bio;
       System.out.println("getBioEntityFromBioType() protein id=" + protein.getUniprot());
        if (protein == null)
            protein = UniprotUtil.getProtein(proteinId, subGraph);
        return protein;
   }

    
    public static BasicDBObject getSequenceIdentificationObj(BasicDBObject diseaseInfo) {
        //log.info(diseaseInfo);
        return (BasicDBObject)diseaseInfo.get(NciFields.SEQUENCE_IDENTIFICATION_COLLECTION.toString());
    }
    
    
    /**
     * {   "HgncID" : "19118" , 
     *     "LocusLinkID" : "79155" , 
     *     "GenbankAccession" : [ ] , 
     *     "RefSeqID" : "NM_024309" , 
     *      "UniProtID" : "Q8NFZ5"
     * }
     * 
     * @param sequence
     * @param protein
     * @param geneSet
     * @param subGraph
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException 
     */
    public static void setSequenceIdentificationRelation(BasicDBObject sequence, Protein protein, HashSet<Gene> geneSet, Subgraph subGraph) throws java.io.IOException, java.net.URISyntaxException {
        //String proteinId = getUniProtId(sequence);
       // log.info("proteinId =" + proteinId);
        log.info("sequence =" + sequence.toString());
        if (geneSet != null && geneSet.size() > 0) {
           Object[] geneList = geneSet.toArray();
           Gene gene = (Gene)geneList[0];
           if (gene != null) { 
               log.info("gene =" + gene);
               if (sequence != null) {
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

    public static Disease getDisease(BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException {
        String diseaseTerm = OntologyStrUtil.getString(dbObj, NciFields.MATCHED_DISEASE_TERM);
        Object bio = getBioEntityFromBioType(subGraph, BioTypes.DISEASE, BioFields.DISEASE_TERM, diseaseTerm);
        boolean created = false;
        Disease disease = (Disease)bio;
        if (disease == null) {
            disease = new Disease();
            disease.setDiseaseTerm(diseaseTerm);
            disease.setDiseaseCode(OntologyStrUtil.getString(dbObj,NciFields.NCI_DISEASE_CONCEPT_CODE));
            setHumanDiseaseOntologyRelation(subGraph, diseaseTerm, disease);
            created = true; 
        }
        if (created) {
            subGraph.add(disease);
        }
        return disease;
    }
    
    public static BasicDBObject getDiseaseData(BasicDBObject dbObj) {
        return OntologyStrUtil.getDBObject(dbObj, NciFields.DISEASE_DATA);    
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
    
    public static NciDiseaseGeneRoleRelation getDiseaseGeneRoleRelation(Disease disease, Gene gene, BasicDBObject dbObj) {
        String geneTerm = getMatchedGeneTerm(getGeneData(dbObj));
        log.info("matched geneTerm " + geneTerm  + " gene= " + gene.getGeneSymbol());
        NciDiseaseGeneRoleRelation roleRelation = new NciDiseaseGeneRoleRelation(disease, gene, geneTerm);
        List<String> roleList = getGeneRoles(getGeneRolesObj(dbObj));
        if (roleList != null && !roleList.isEmpty()) {
            log.info("setDiseaseGeneRoles()");
           setDiseaseGeneRoles(roleRelation, roleList);
        }
        return roleRelation;
    }
    /*
    public static NciDiseaseGeneRoleRelation createDiseaseGeneRoleRelation() {
        String geneTerm = getMatchedGeneTerm(geneGeneData(dbObj));
        List<String> roleList = getGeneRoles(getGeneRolesObj(dbObj));
        
    }*/
    
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
        log.info("objList.length()" + objList.length);
        StringBuilder sb = new StringBuilder();
        int i = 0;
        for (Object obj : objList) {
            if (obj != null) {
               
                if (i == 54) {
                    // size is 3055 + 
                    sb.append("appended voltage-dependent channel alpha 1");
                    //channel alpha 1g subunit isoform 5");
                    log.info("geneAlias appended " + obj.toString());
                    break;
                } else {
                     sb.append(obj.toString());
                     sb.append(" ");
                }
                i++;
            }
        }
        // worked for 3095 3081 2878, 3055
        // failed at 3097, 3101, 3166, 3173, 3855 size
        log.info("sb.length" + sb.length());
        return sb.toString();
        //return StrUtil.replaceUTF8(StrUtil.removeBadASCII(StrUtil.goodWebStr(sb.toString())));
        //return sb.toString();
        
    }
     
    /**
     * ROLES is a string when it's value is  "not-assigned" which indicates
     * there are no roles in it. Perform the check using isObjectString
     * @param dbList
     * @return 
     */
    public static List getGeneRoles(BasicDBList dbList) {
        if (dbList == null || dbList.isEmpty()) {
            return null;
        } else { 
            List<String> roleList = new ArrayList();
            for (Object obj : dbList) {
                if (!OntologyStrUtil.isObjectString(obj)) {
                    BasicDBObject dbObj = (BasicDBObject)obj;
                    if (dbObj != null) {
                        roleList = getPrimaryRoleList(dbObj, roleList);
                        log.info("roleList of Gene Primary Roles" + roleList.size() + " list=" + roleList.toString());
                        roleList = getOtherRoleList(dbObj, roleList);
                        if (roleList.isEmpty()) {
                            log.info("roleList of Gene is empty");
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
     * @param dbObj {@link NciGeneDiseaseRoles}
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
     * roleList contains roles {Gene_Product_Anormaly_has_Disease-Related_Function} which
     * gets mapped to enum {@link NciGeneDiseaseRoles#Gene_Product_Anormaly_May_have_Disease_Related_Function}
     * @param roleRelation
     * @param roleList 
     */
    public static void setDiseaseGeneRoles(NciDiseaseGeneRoleRelation roleRelation, List<String> roleList ) {
         for (String str : roleList) {
             if (str != null) {
                 log.info("DiseaseGeneRoles =" + str);
                 roleRelation.setRole(NciGeneDiseaseRoles.getRoleEnum(str));
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
               List<String> codeList = new ArrayList();
               codeList.add(code);
               return codeList;    
           } else {
               BasicDBList dbList = (BasicDBList)OntologyStrUtil.getBasicDBList(dbObj, NciFields.EVIDENCE_CODE);
               if (dbList != null && !dbList.isEmpty()) {
                   List<String> codeList = new ArrayList();
                   for (Object obj : dbList) {
                        codeList.add(obj.toString());
                   }
                   return codeList;
               }
           }
        }
        return null;
    }
    
    public static void setCellLineIndicator(NciDiseaseGeneEvidenceRelation relation, BasicDBObject dbObj) {
          if (OntologyStrUtil.isObjectString(dbObj, NciFields.CELL_LINE_INDICATOR)) {
              relation.setCellLineIndicator(getCellLineIndicator(dbObj));
          } else {
              OntologyStrUtil.getBasicDBList(dbObj, NciFields.CELL_LINE_INDICATOR);
              //log.info("CellLineIndicator is null");
          }
    }
    
    public static void setNegationIndicator(NciDiseaseGeneEvidenceRelation relation, BasicDBObject dbObj) {
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
     * @param disease
     * @param gene
     * @param dbObj
     * @return 
     */
    public static NciDiseaseGeneEvidenceRelation getDiseaseGeneEvidenceRelation(Disease disease, Gene gene, BasicDBObject dbObj) {
          List<String> codeList = getEvidenceCodes(dbObj);
          NciDiseaseGeneEvidenceRelation relation = new NciDiseaseGeneEvidenceRelation(disease, gene);
          if (codeList != null && !codeList.isEmpty()) {
              for (String str : codeList) {
                  log.info("EvidenceCode for + " +  str + " Enum =" + EvidenceCodes.getEnumCode(str));
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
    public static PubMed getPubMed(BasicDBObject dbObj, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException, UnknownHostException, UnsupportedEncodingException, MalformedURLException, IOException, HttpException, InterruptedException {
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
                    subGraph.add(organism);
            }
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
    
    public static NciPubMedGeneDrugDiseaseRelation getPubMedDiseaseRelation(PubMed pubMed, Disease disease, BasicDBObject dbObj) {
        NciPubMedGeneDrugDiseaseRelation rel = new NciPubMedGeneDrugDiseaseRelation();
        rel.setNciPubMedRelation(pubMed, disease, BioRelTypes.REFERENCES_PUBMED);
        rel.setPubMedStatement(getStatement(dbObj));
        rel.setSentenceStatusFlag(getSentenceStatusFlag(dbObj));
        return rel;
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
    public static void processSentence(BasicDBObject zeroObject, HashSet<Gene> geneSet, Subgraph subGraph, Protein protein) throws java.io.IOException, java.net.URISyntaxException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, NotFoundException, InvocationTargetException, Exception {
         BasicDBList dbList = (BasicDBList)OntologyStrUtil.getBasicDBList(zeroObject, NciFields.SENTENCE);
         if (dbList != null) {
            if (dbList != null) {
                /**
                 * sentenceList
                 */
                for (Object obj : dbList) {
                    BasicDBObject dbObj = (BasicDBObject)obj;
                    BasicDBObject diseaseData = getDiseaseData(dbObj);
                    Disease disease = getDisease(diseaseData, subGraph);
                    if (protein != null) {
                        if (protein.getUniprot().equals("-")) {
                            System.out.println("proteinid =" + protein.getUniprot() + " disease=" + disease.getDiseaseTerm());
                        } else {
                            if (protein.getMessage() == null) {
                                System.out.println("protein.getMessage() is null, proteinid =" + protein.getUniprot());
                                System.out.println("disease=" + disease.getDiseaseTerm());
                            }
                            /*if (protein.getMessage() != null && protein.getMessage().equals("Protein--")) {
                                System.out.println("Protein--, proteinid =" + protein.getUniprot());
                                System.out.println("disease=" + disease.getDiseaseTerm());
                            } else { */
                                disease.setProteinRelation(protein);
                                System.out.println("disease relationtype =" + disease.getProteinRelation().getRelType());
                                System.out.println("proteinid =" + protein.getUniprot());
                           // }
                        }
                    }

                    Gene gene = getGene(dbObj, geneSet);
                    if (gene != null) {
                        log.info("gene = " + gene.getGeneSymbol());
                        NciDiseaseGeneRoleRelation roleRelation = getDiseaseGeneRoleRelation(disease, gene, dbObj);   
                        disease.setGeneRelations(roleRelation);
                        NciDiseaseGeneEvidenceRelation eRelation = getDiseaseGeneEvidenceRelation(disease, gene, dbObj);
                        disease.setEvidenceRelations(eRelation);

                        PubMed pubMed = getPubMed(dbObj, subGraph);
                        if (pubMed != null) { 
                            NciPubMedGeneDrugDiseaseRelation pubMedDiseaseRelation = getPubMedDiseaseRelation(pubMed, disease, dbObj);
                            NciPubMedGeneDrugDiseaseRelation pubMedGeneRelation = getPubMedGeneRelation(pubMed, gene, dbObj);
                            pubMed.setPubMedRelation(pubMedGeneRelation);
                            pubMed.setPubMedRelation(pubMedDiseaseRelation);
                        }
                        Organism organism = getOrganism(dbObj, subGraph);
                        setOrganismRelations(organism, gene, disease);
                        NcbiTaxonomy ncbiTaxonomy = getNcbiTaxonomy(dbObj, subGraph);
                        if (ncbiTaxonomy != null) {
                            disease.setNcbiTaxonomyRelation(ncbiTaxonomy);
                        }
                    }
                }  
            }                
        }   
    }
    
    
    public static void setOrganismRelations(Organism organism, Gene gene, Disease disease) {
          if (organism != null) {
              organism.setOrganismRelation(gene);
              organism.setOrganismRelation(disease);  
          }    
    }
     
    public static void processDisease(BasicDBObject dbObject) throws java.io.IOException, java.net.URISyntaxException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, NotFoundException, InvocationTargetException, UnsupportedEncodingException, MalformedURLException, UnknownHostException, HttpException, Exception {
        log.info("processDisease()");
        BasicDBObject zeroObject = NCIDiseaseUtil.getZeroObject(dbObject);
        
        if (zeroObject == null) {
            return;
        } else {   
            String geneSymbol = NCIDiseaseUtil.getHugoGeneSymbol(zeroObject);
            log.info("************  geneSymbol " + geneSymbol);
            Subgraph subGraph = new Subgraph();
            HashSet<Gene> geneSet = getGeneSet(geneSymbol, subGraph);
            BasicDBObject sequence = getSequenceIdentificationObj(zeroObject);
            String proteinId = getUniProtId(sequence);
            if (proteinId.equals("-")) {
               System.out.println("proteinId= -");
            }
            Protein protein = null;
            if (proteinId != null) {
                protein = getProtein(proteinId, subGraph);
                if (protein != null) {
                    System.out.println("protein =" + protein.getUniprot());
                    setSequenceIdentificationRelation(sequence, protein, geneSet, subGraph);
                }
            }
            processSentence(zeroObject, geneSet, subGraph, protein);
            subGraph.traverse();
            log.info("traverse completed");
            PersistenceTemplate.saveSubgraph(subGraph);
        }
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
   private static Object getBioEntityFromBioType(Subgraph subGraph, BioTypes bioType, BioFields key, String value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException {
       //log.info("getBioEntityFromBioType()," + bioType.toString() + "," + key.toString() + "=" + value);
       Object bioEntity = subGraph.search(bioType, key, value);
       return bioEntity;
   }
   
   public static HashSet<Gene> getGeneSet(String geneSymbol, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException, Exception {
        return GeneGraphDBImportUtil.getGene(geneSymbol, subGraph);    
       
        /*
        gene.setOrganismComponent(Organism);
        gene.setHugoGeneSymbol(geneSymbol); 
        Object[] geneAliasArray = NCIDiseaseUtil.getGeneAliasCollection(zeroObject);
        gene.setGeneAliases(getObjectListString(geneAliasArray));   
        gene.setGeneStatusFlag(getGeneStatusFlag(zeroObject));
        //gene.setOrganismComponent(Organism);
        if (created) {
           subGraph.add(gene);
        } 
        * 
        */
       
    }
   
   public static Gene getGene(HashSet<Gene> geneSet, String taxId) throws Exception {
        return GeneGraphDBImportUtil.getGene(geneSet, taxId);
   }
   
   public static String getNcbiTaxId(BasicDBObject dbObj) {
        String organismLabel = getOrganismShortLabel(dbObj);
        if (organismLabel != null) {
           return NcbiTaxIdTypes.getNcbiTaxIdValue(organismLabel);
        } else {
            return null;
        }
   }
   
   public static NcbiTaxonomy getNcbiTaxonomy(BasicDBObject dbObj, Subgraph subgraph) throws Exception {
       String ncbiTaxId = getNcbiTaxId(dbObj);
       return GeneGraphDBImportUtil.getNcbiTaxonomy(subgraph, ncbiTaxId);
   } 
   
   public static Gene getGene(BasicDBObject dbObj, HashSet<Gene> geneSet) throws Exception {
        String organismLabel = getOrganismShortLabel(dbObj);
        if (organismLabel != null) {
            String taxId = NcbiTaxIdTypes.getNcbiTaxIdValue(organismLabel);
            //log.info("taxId =" + taxId);
            if (taxId != null) {
                return getGene(geneSet, taxId);
            }
        } else {
            //log.info("organism not found =" + organismLabel);
            getGene(geneSet, NcbiTaxIdTypes.HUMAN.toString());
        } 
        return null;
   }

   public static void setHumanDiseaseOntologyRelation(Subgraph subGraph, String diseaseTerm, Disease disease) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NotFoundException, InvocationTargetException {
        Object bio = PersistenceTemplate.getBioEntity(BioTypes.HUMAN_DISEASE_ONTOLOGY, BioFields.HUMAN_DISEASE_ONTOLOGY_NAME, diseaseTerm);
        HumanDiseaseOntology hdo= (HumanDiseaseOntology)bio;
        if (hdo != null) {
            log.info("humanDiseaseOntology term found =" + hdo.getMessage());
            hdo.setIsARelationship(disease, BioRelTypes.IS_A);
            subGraph.add(hdo);
        } else {
            log.info("humanDiseaseOntology is null for disease term" + diseaseTerm);
        }
   }

}
