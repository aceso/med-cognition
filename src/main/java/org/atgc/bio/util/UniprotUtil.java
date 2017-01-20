package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
//import org.atgc.bio.UniProtAccess;
import org.atgc.bio.repository.CompoundKey;
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
import org.atgc.bio.UniprotFields;
import org.atgc.bio.domain.*;
import org.neo4j.graphdb.NotFoundException;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;

/**
 * Uniprot entries are mapped to BioEntity
 *
 * @author jtanisha-ee
 */
@SuppressWarnings("javadoc")
public class UniprotUtil {

    protected static Logger log = LogManager.getLogger(UniprotUtil.class);

    public static Protein getProtein(String id) throws IllegalAccessException, InterruptedException, HttpException, ServiceException, IOException, URISyntaxException, InvocationTargetException, NoSuchFieldException {
        return getProtein(id, new Subgraph());
    }

    public static Protein getProtein(String id, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, ServiceException, IOException, InterruptedException, HttpException, URISyntaxException {

        if (id == null || id == "" || id == " " )  return null;
        if (id.equals("-")) {
            log.info("proteinId = -");
            return null;
        }

        Protein protein = (Protein) subGraph.search(BioTypes.PROTEIN, BioFields.UNIPROT_ID, id);
        if (id.equals("-")) {
            log.info("proteinId = -");
            return null;
        }
        if (protein == null) {
            log.info("getting protein from uniprot " + id);
            protein = new Protein();
            protein.setUniprot(id);
            // why do we add this before we find the protein, postpone adding it until we find it.
            //subGraph.add(protein);
            processProtein(protein, id, subGraph);
        }
        return protein;
    }

    /**
     * get PubMed
     *
     * @param pubMedId
     * @param subGraph
     * @return PubMed {@link PubMed}
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws URISyntaxException
     * @throws IOException
     * @throws HttpException
     * @throws InterruptedException
     * @throws InvocationTargetException
     */
    public static PubMed getPubMed(String pubMedId, Subgraph subGraph) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, URISyntaxException, IOException, HttpException, InterruptedException, InvocationTargetException {
        PubMed pubMed = (PubMed) subGraph.search(BioTypes.PUBMED, BioFields.PUBMED_ID, pubMedId);
        if (pubMed == null) {
            pubMed = PubMedUtil.getPubmed(pubMedId, subGraph);
        }
        return pubMed;
    }

    /**
     *
     * @param pubMed
     * @param protein
     */
    public static void createPubMedRelation(PubMed pubMed, Protein protein) {
        // log.info("createPubMedRelation");
        pubMed.setPubMedRelation(protein);
    }

    /**
     * * "citationXrefs" : {
     * "doi" : "10.1038/nature03154",
     * "pubmedId" : "15592404"
     * },
     *
     * @param protein
     * @param obj
     * @param subGraph
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws HttpException
     * @throws IOException
     * @throws URISyntaxException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    public static void setCitationPubMedRelation(Protein protein, BasicDBObject obj, Subgraph subGraph) throws IllegalAccessException, InterruptedException, HttpException, IOException, URISyntaxException, InvocationTargetException, NoSuchFieldException {
        if (obj != null) {
            if (OntologyStrUtil.objectExists(obj, UniprotFields.CITATIONXREF)) {
                BasicDBObject citationObj = OntologyStrUtil.getDBObject(obj, UniprotFields.CITATIONXREF);
                if (citationObj != null) {
                    String pubMedId = OntologyStrUtil.getString(citationObj, UniprotFields.PUBMED_ID);
                    if (pubMedId != null) {
                        PubMed pubMed = getPubMed(pubMedId, subGraph);
                        if (pubMed != null) {
                            createPubMedRelation(pubMed, protein);
                        }
                    }
                }
            }
        }
    }

    /**
     * getFullName
     * @param dbList
     * @param field
     * @return
     */
    public static String getFieldValueFromList(BasicDBList dbList, Enum field) {
        if (dbList != null && dbList.size() > 0) {
            for (Object fieldObj : dbList) {
                if (fieldObj != null) {
                    if (OntologyStrUtil.objectExists((BasicDBObject) fieldObj, UniprotFields.FIELD_TYPE)) {
                        if (OntologyStrUtil.getString((BasicDBObject) fieldObj, UniprotFields.FIELD_TYPE).equals(field.toString())) {
                            return OntologyStrUtil.getString((BasicDBObject) fieldObj, UniprotFields.FIELD_VALUE);
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     *
     * @param dbList
     * @return
     */
    public static String getValuesFromList(BasicDBList dbList) {
        boolean exists = false;
        if (dbList != null && dbList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object fieldObj : dbList) {
                if (fieldObj != null) {
                    if (OntologyStrUtil.objectExists((BasicDBObject) fieldObj, UniprotFields.FIELD_VALUE)) {
                        String val = OntologyStrUtil.getString((BasicDBObject) fieldObj, UniprotFields.FIELD_VALUE);
                        if (val != null) {
                            exists = true;
                            sb.append(val);
                            sb.append(" ");
                        }
                    }
                }
            }
            if (exists) {
                return sb.toString();
            }
        }
        return null;
    }

    /**
     * <pre>
     * "recommendedName" : {
     * "fields" : [
     * {
     * "fieldType" : "Full",
     * "fieldValue" : "TNFAIP3-interacting protein 2"
     * }
     * ]
     * },
     * </pre>
     *
     * @param obj
     * @param field
     */
    public static String getFieldValue(BasicDBObject obj, Enum field) {
        BasicDBList dbList = getFieldsFromRecommendedName(obj);
        String fullName = getFieldValueFromList(dbList, field);
        if (fullName == null) {
            dbList = getFieldsFromProteinDescription(obj);
            fullName = getFieldValueFromList(dbList, field);
            if (fullName == null) {
                dbList = getFieldsFromSubNames(obj);
                fullName = getFieldValueFromList(dbList, field);
            }
        }
        return fullName;
    }

    /**
     * <pre>
     *   "alternativeNames" : [
     * {
     * "fields" : [
     * {
     * "fieldType" : "Full",
     * "fieldValue" : "A20-binding inhibitor of NF-kappa-B activation 2"
     * },
     * {
     * "fieldType" : "Short",
     * "fieldValue" : "ABIN-2"
     * }
     * ],
     * "nameType" : "AltName"
     * }
     * ],
     * </pre>
     *
     * @param obj
     * @return
     */
    public static BasicDBList getFieldsFromAlternativeNames(BasicDBObject obj) {
        if (OntologyStrUtil.objectExists(obj, UniprotFields.ALTERNATIVE_NAMES)) {
            BasicDBObject dbObj = (BasicDBObject) obj.get(UniprotFields.ALTERNATIVE_NAMES.toString());
            if (dbObj != null) {
                return getFieldListFromAlternativeName(dbObj);
            }
        }
        return null;
    }

    /**
     *
     * @param dbObj
     * @return
     */
    public static BasicDBList getFieldListFromAlternativeName(BasicDBObject dbObj) {
        if (dbObj != null) {
            BasicDBList list = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.ALTERNATIVE_NAMES);
            if (list != null) {
                if (OntologyStrUtil.objectExists(dbObj, UniprotFields.NAME_TYPE)) {
                    String nameType = OntologyStrUtil.getString(dbObj, UniprotFields.NAME_TYPE);
                    if (nameType != null && nameType.equals(UniprotFields.ALTNAME.toString())) {
                        for (Object obj : list) {
                            if (OntologyStrUtil.objectExists((BasicDBObject)obj, UniprotFields.FIELDS)) {
                                return OntologyStrUtil.getBasicDBList((BasicDBObject)obj, UniprotFields.FIELDS);
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     * setFullName
     *
     * @param protein
     * @param obj
     */
    public static void setFullName(Protein protein, BasicDBObject obj) {
        String fullName = getFieldValue(obj, UniprotFields.FULL);
        if (fullName != null) {
            protein.setFullName(fullName);
        }
    }

    /**
     * setShortName
     *
     * @param protein
     * @param obj
     */
    public static void setShortName(Protein protein, BasicDBObject obj) {
        String shortName = getFieldValue(obj, UniprotFields.SHORT);
        if (shortName != null) {
            protein.setShortName(shortName);
        }
    }

    /**
     * setProteinNames
     *
     * @param protein
     * @param obj
     */
    public static void setProteinNames(Protein protein, BasicDBObject obj) {
        StringBuilder sb = new StringBuilder();
        BasicDBList dbList = getFieldsFromAlternativeNames(obj);
        String altNames = getValuesFromList(dbList);
        dbList = getFieldsFromSubNames(obj);
        String subNames = getValuesFromList(dbList);
        if (altNames != null) {
            sb.append(altNames);
        }
        if (subNames != null) {
            sb.append(subNames);
        }
        if (subNames != null) {
            protein.setProteinNames(sb.toString());
        }
    }

    /**
     * <pre>
     * "recommendedName" : {
     *      "fields" : [
     *               {
     *                       "fieldType" : "Full",
     *                       "fieldValue" : "TNFAIP3-interacting protein 2"
     *              }
     *      ]
     *  },
     * </pre>
     */
    public static BasicDBList getFieldListFromRecommendedName(BasicDBObject obj) {
        return OntologyStrUtil.getBasicDBList(obj, UniprotFields.FIELDS);
    }


    /**
     * <pre>
     * "subNames" : [
     * {
     * "fields" : [
     * {
     * "evidenceIds" : [
     * "EI1"
     * ],
     * "fieldType" : "Full",
     * "fieldValue" : "Uncharacterized protein"
     * }
     * ],
     * "nameType" : "SubName"
     * }
     * ]
     *
     * </pre>
     *
     * @param dbObj
     * @return
     */
    public static BasicDBList getFieldListFromSubNames(BasicDBObject dbObj) {
        if (dbObj != null) {
            BasicDBList list = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.SUBNAMES);
            if (list != null) {
                for (Object obj : list) {
                    if (OntologyStrUtil.objectExists((BasicDBObject)obj, UniprotFields.FIELDS)) {
                        return OntologyStrUtil.getBasicDBList((BasicDBObject)obj, UniprotFields.FIELDS);
                    }
                }
            }
        }
        return null;
    }


    /**
     * <pre>
     * proteinDescription" : {
     *     "section" : {
     *
     *      },
     *  }
     * </pre>
     *
     * @param obj
     * @return
     */
    public static BasicDBList getFieldListFromProteinDesc(BasicDBObject obj) {
        if (OntologyStrUtil.objectExists(obj, UniprotFields.PROTEIN_DESCRIPTION)) {
            BasicDBObject pObj = OntologyStrUtil.getDBObject(obj, UniprotFields.PROTEIN_DESCRIPTION);
            if (pObj != null) {
                BasicDBObject sectionObj = OntologyStrUtil.getDBObject(pObj, UniprotFields.SECTION);
                if (sectionObj != null) {
                    if (OntologyStrUtil.objectExists(sectionObj, UniprotFields.NAMES)) {
                        BasicDBList nameList = OntologyStrUtil.getBasicDBList(sectionObj, UniprotFields.NAMES);
                        if (nameList != null) {
                            for (Object nameObj : nameList) {
                                if (OntologyStrUtil.objectExists((BasicDBObject) nameObj, UniprotFields.FIELDS)) {
                                    return OntologyStrUtil.getBasicDBList((BasicDBObject) nameObj, UniprotFields.FIELDS);
                                }
                            }
                        }
                    }
                }
            }
        }
        return null;
    }


    /**
     *
     * @param obj
     * @return
     */
    public static BasicDBList getFieldsFromRecommendedName(BasicDBObject obj) {
        if (OntologyStrUtil.objectExists(obj, UniprotFields.RECOMMENDED_NAME)) {
            BasicDBObject dbObj = (BasicDBObject) obj.get(UniprotFields.RECOMMENDED_NAME.toString());
            if (dbObj != null) {
                return getFieldListFromRecommendedName(dbObj);
            }
        }
        return null;
    }

    /**
     *
     * @param obj
     * @return
     */
    public static BasicDBList getFieldsFromSubNames(BasicDBObject obj) {
        if (OntologyStrUtil.objectExists(obj, UniprotFields.SUBNAMES)) {
            return getFieldListFromSubNames(obj);
        }
        return null;
    }

    /**
     *
     * @param obj
     * @return
     */
    public static BasicDBList getFieldsFromProteinDescription(BasicDBObject obj) {
        if (OntologyStrUtil.objectExists(obj, UniprotFields.PROTEIN_DESCRIPTION)) {
            return getFieldListFromProteinDesc(obj);
        }
        return null;
    }

    /**
     * <pre>
     *
     * "secondaryUniProtAccessions" : [
     * "B1AKS4",
     * "B3KTY8",
     * "D3DVQ9",
     * "Q7L5L2",
     * "Q9BQR6",
     * "Q9H682"
     * ]
     * </pre>
     * @param dbObj
     * @return
     */
    public static String getSecondaryUniProtAccessions(BasicDBObject dbObj) {
        return getListAsString(dbObj, UniprotFields.SECONDARY_UNIPROT_ACCESSIONS);
    }

    /**
     * "uniProtEntryType" : "TrEMBL", SwissProt
     *
     * @param dbObj
     * @return
     */
    public static String getUniProtEntryType(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, UniprotFields.UNIPROT_ENTRY_TYPE);
    }

    /**
     *
     * @param dbObj
     * @param field
     * @return
     */
    public static String getListAsString(BasicDBObject dbObj, Enum field) {
        if (OntologyStrUtil.objectExists(dbObj, field)) {
            BasicDBList list = OntologyStrUtil.getList(dbObj, field);
            if (list != null && list.size() > 0) {
                StringBuilder str = new StringBuilder();
                list.stream().filter(obj -> obj != null).forEach(obj -> {
                    str.append(obj.toString());
                    str.append(" ");
                });
                return str.toString();
            }
        }
        return null;
    }

    /*
    for (Object obj : list) {
                    if (obj != null) {
                        str.append(obj.toString());
                        str.append(" ");
                    }
                }
     */

    /**
     * <pre>
     * "ncbiTaxonomyIds" : [
     * {
     * "ncbiTaxId" : "10090"
     * }
     * ],
     *
     * </pre>
     * @param protein
     * @param dbObj
     * @param subGraph
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws UnknownHostException
     */
    public static void setNcbiTaxonmyRelationship(Protein protein, BasicDBObject dbObj, Subgraph subGraph) throws InvocationTargetException, NoSuchFieldException, IllegalAccessException, UnknownHostException {
        BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.NCBI_TAXONOMY_IDS);
        // log.info("ncbitaxid list =" + dbList.toString());
        for (Object id : Objects.requireNonNull(dbList)) {
            BasicDBObject obj = (BasicDBObject) id;
            if (OntologyStrUtil.objectExists(obj, UniprotFields.NCBI_TAX_ID)) {
                String ncbiTaxId = OntologyStrUtil.getString(obj, UniprotFields.NCBI_TAX_ID);
                if (ncbiTaxId != null) {
                    String ncbiTaxonPrefix = "NCBITaxon:";
                    NcbiTaxonomy entity = GeneGraphDBImportUtil.getNcbiTaxonomy(subGraph, ncbiTaxonPrefix + ncbiTaxId);
                    if (entity != null) {
                        protein.setNcbiTaxonomyRelations(entity);
                    }
                }
            }
        }
    }

    /**
     * <pre>
     * "organism" : {
     * "organismCommonName" : "Cat",
     * "organismCommonNames" : [
     * "Felis catus",
     * "Cat",
     * "Felis silvestris catus"
     * ],
     * "organismScientificName" : "Felis catus",
     * "organismSynonym" : "Felis silvestris catus"
     * }
     *
     * </pre>
     * @param protein
     * @param dbObj
     * @param subGraph
     */
    public static void setOrganismNames(Protein protein, BasicDBObject dbObj, Subgraph subGraph) {
    }


    /**
     * <pre>
     * "keywords" : [
     * {
     * "keyword" : "Apoptosis"
     * },
     * {
     * "keyword" : "Coiled coil"
     * },
     * {
     * "keyword" : "Complete proteome"
     * },
     * </pre>
     * keywords
     * @param dbObj
     * @return
     */
    public static String getKeywords(BasicDBObject dbObj) {
        BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.KEYWORDS);
        if (dbList != null && dbList.size() > 0) {
            StringBuilder sb = new StringBuilder();
            for (Object obj : dbList) {
                BasicDBObject label = (BasicDBObject) obj;
                if (label != null) {
                    String keyword = OntologyStrUtil.getString(label, UniprotFields.KEYWORD);
                    if (keyword != null) {
                        sb.append(keyword);
                        sb.append(",");
                    }
                }
            }
            return sb.toString();
        }
        return null;
    }

    /**
     * <pre>
     * "genes" : [
     * {
     * "hugoGeneSymbol" : "Tnip2",
     * "geneNameSynonyms" : [
     * "Abin2"
     * ],
     * "orderedLocusNames" : [ ]
     * }
     * ],
     *
     * setGeneRelationship
     * or with only one
     * @param protein
     * @param dbObj
     * @param subGraph
     */
    public static void setGeneRelationship(Protein protein, BasicDBObject dbObj, Subgraph subGraph) throws IllegalAccessException, InterruptedException, HttpException, IOException, URISyntaxException, InvocationTargetException, NoSuchFieldException {
        BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.GENES);
        if (dbList != null && dbList.size() > 0) {
            for (Object obj : dbList) {
                BasicDBObject geneObj = (BasicDBObject) obj;
                if (geneObj != null) {
                    if (OntologyStrUtil.objectExists(geneObj, UniprotFields.HUGO_GENE_SYMBOL)) {
                        String geneSymbol = OntologyStrUtil.getString(geneObj, UniprotFields.HUGO_GENE_SYMBOL);
                        if (geneSymbol != null) {
                            HashSet<Gene> entitySet = GeneGraphDBImportUtil.getGene(geneSymbol, subGraph);
                            if (!entitySet.isEmpty()) {
                                entitySet.stream().filter(gene -> gene != null).forEach(protein::setGeneRelations);
                                /*
                                for (Gene gene : entitySet) {
                                    if (gene != null) {
                                        protein.setGeneRelations(gene);
                                    }
                                }
                                 */
                            }
                        }
                    }
                }
            }
        }
    }


    /**
     * setGeneOntologyRelations
     * <pre>
     * "goTermList" : [
     * {
     * "goEvidenceSource" : "MGI",
     * "goId" : "GO:0005737",
     * "goTerm" : "cytoplasm",
     * "ontologyType" : "C:"
     * },
     *
     * </pre>
     * GeneOntologyRelations
     */
    public static void setGeneOntologyRelationship(Protein protein, BasicDBObject dbObj, Subgraph subGraph) throws IOException, RuntimeException, IllegalAccessException, InterruptedException, HttpException, URISyntaxException, InvocationTargetException, NoSuchFieldException {
        BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.GO_TERM_LIST);
        if (dbList != null && dbList.size() > 0) {
            for (Object obj : dbList) {
                BasicDBObject go = (BasicDBObject) obj;
                GeneOntology entity = getGeneOntology(go, subGraph);
                if (entity != null) {
                    protein.setGoRelations(entity);
                }
            }
        }
    }

    /**
     *
     * @param obj
     * @param subGraph
     * @return
     * @throws IOException
     * @throws RuntimeException
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws HttpException
     * @throws URISyntaxException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    public static GeneOntology getGeneOntology(BasicDBObject obj, Subgraph subGraph) throws IOException, RuntimeException, IllegalAccessException, InterruptedException, HttpException, URISyntaxException, InvocationTargetException, NoSuchFieldException {
        if (obj != null) {
            String goStr = OntologyStrUtil.getString(obj, UniprotFields.GO_ID);
            if (goStr != null) {
                if (OntologyStrUtil.isGeneOntology(goStr)) {
                    return GeneOntologyObo.getGeneOntology(goStr, subGraph);
                }
            }
        }
        return null;
    }

    /**
     * <pre>
     * "featureList" : [
     * {
     * "featureLocation" : {
     * "locationEnd" : 430,
     * "locationStart" : 1,
     * "startModifier" : "EXACT",
     * "endModifier" : "EXACT"
     * },
     * "featureStatus" : "Experimental",
     * "featureType" : "chain"
     * },
     *
     * </pre>
     * @param seq
     * @param dbObj
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void setFeature(ProteinSequence seq, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String sequenceCheckSum = seq.getSequenceSum();
        if (OntologyStrUtil.objectExists(dbObj, UniprotFields.FEATURE_LIST)) {
            BasicDBList featureList = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.FEATURE_LIST);
            if (featureList != null && featureList.size() > 0) {
                for (Object featureObj : featureList) {
                    BasicDBObject obj = (BasicDBObject) featureObj;
                    log.info("obj*********************** =" + featureObj);
                    if (obj != null) {
                        ProteinSequenceFeature feature = getFeature(obj, sequenceCheckSum);
                        if (feature != null) {
                            seq.setFeatureRelations(feature);
                            subGraph.add(feature);
                        }
                    }
                }
            }
        }
    }


    /**
     * {
     * "featureLocation" : {
     * "locationEnd" : 430,
     * "locationStart" : 1,
     * "startModifier" : "EXACT",
     * "endModifier" : "EXACT"
     * },
     * "featureStatus" : "Experimental",
     * "featureType" : "chain"
     * },
     *
     * @param featureObj
     * @param seqId
     * @return
     */
    public static ProteinSequenceFeature getFeature(BasicDBObject featureObj, String seqId) {
        ProteinSequenceFeature feature = createFeature();
        log.info("featureObj =" + featureObj);
        BasicDBObject locationObj = OntologyStrUtil.getDBObject(featureObj, UniprotFields.FEATURE_LOCATION);

        feature.setSequenceId(seqId);

        String val = getIntegerVal(locationObj, UniprotFields.LOCATION_END);
        if (val != null) {
            feature.setEndPosition(val);
        }
        log.info("locationEnd =" + val);

        val = getIntegerVal(locationObj, UniprotFields.LOCATION_START);
        if (val != null) {
            feature.setStartPosition(val);
        }
        log.info("locationStart =" + val);

        val = getString(featureObj, UniprotFields.FEATURE_STATUS);
        if (val != null) {
            feature.setFeatureStatus(val);
        }
        val = getString(featureObj, UniprotFields.FEATURE_DESCRIPTION);
        if (val != null) {
            feature.setFeatureDescription(val);
        }
        val = getString(featureObj, UniprotFields.FEATURE_TYPE);
        if (val != null) {
            feature.setFeatureType(val);
        }
        return feature;
    }

    /**
     *
     * @return
     */
    public static ProteinSequenceFeature createFeature() {
        return new ProteinSequenceFeature();
    }

    /**
     *
     * @param protein
     * @param dbObj
     * @param subGraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static void setSequence(Protein protein, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        ProteinSequence sequence = createSequence();
        sequence.setUniprot(protein.getUniprot());
        setSequenceInfo(sequence, dbObj);
        setFeature(sequence, dbObj, subGraph);
        protein.setProteinSequenceRelations(sequence);
        subGraph.add(sequence);
    }

    /**
     * <pre>
     * "sequence" : {
     * "crc64" : "35F533967072512D",
     * "sequenceLength" : 430,
     * "molecularWeight" : 49094,
     * "sequenceValue" : "MSSGDPRSGRQDGA...
     * </pre>
     */
    public static ProteinSequence createSequence() {
        // indexName=IndexNames.SEQUENCE_ID,
        // field1=BioFields.UNIPROT_ID,
        // field2=BioFields.SEQUENCE_SUM
        // field3=BioFields.SEQUENCE_LENGTH)
        return new ProteinSequence();
    }

    /**
     *
     * @param dbObj
     * @param field
     * @return
     */
    public static String getString(BasicDBObject dbObj, Enum field) {
        return OntologyStrUtil.getString(dbObj, field);
    }

    /**
     *
     * @param dbObj
     * @param field
     * @return
     */
    public static String getIntegerVal(BasicDBObject dbObj, Enum field) {
        Object obj = dbObj.get(field.toString());
        if (obj != null) return obj.toString();
        return null;
    }


    /**
     * "sequence" : {
     * "crc64" : "0F6B049C2B3483DC",
     * "sequenceLength" : 429,
     * "molecularWeight" : 48700,
     * "sequenceValue" : "MSRDPG..."
     * },
     *
     * @param seq
     * @param obj
     */
    public static void setSequenceInfo(ProteinSequence seq, BasicDBObject obj) {
        if (seq != null) {
            String val;
            if (OntologyStrUtil.objectExists(obj, UniprotFields.SEQUENCE)) {
                BasicDBObject dbObj = OntologyStrUtil.getDBObject(obj, UniprotFields.SEQUENCE);
            
            /* SEQUENCE_SUM */
                val = getString(dbObj, UniprotFields.CRC64);
                if (val != null) {
                    seq.setSequenceSum(val);
                }

                /**
                 * An integer as it does not have double quotes
                 */
                val = getIntegerVal(dbObj, UniprotFields.SEQUENCE_LENGTH);
                log.info("length =" + val);
                if (val != null) {
                    seq.setSequenceLength(val);
                }
                val = getIntegerVal(dbObj, UniprotFields.MOLECULAR_WEIGHT);
                if (val != null) {
                    seq.setMolecularWeight(val);
                }
                log.info("weight =" + val);

                val = getString(dbObj, UniprotFields.SEQUENCE_VALUE);
                if (val != null) {
                    seq.setSequenceValue(val);
                }
            }
            setEntryAuditInfo(seq, obj);
        }
    }

    /**
     * <pre>
     * "entryAudit" : {
     * "entryVersion" : 56,
     * "firstPublicDate" : "Mon Feb 25 17:00:00 PST 2008",
     * "lastAnnotationUpdateDate" : "Tue May 28 17:00:00 PDT 2013",
     * "lastSequenceUpdateDate" : "Thu May 31 17:00:00 PDT 2001",
     * "sequenceVersion" : 1
     * }
     *
     * </pre>
     * @param seq
     * @param obj
     */
    public static void setEntryAuditInfo(ProteinSequence seq, BasicDBObject obj) {
        if (OntologyStrUtil.objectExists(obj, UniprotFields.ENTRY_AUDIT)) {
            BasicDBObject dbObj = OntologyStrUtil.getDBObject(obj, UniprotFields.ENTRY_AUDIT);
            String val = getIntegerVal(dbObj, UniprotFields.ENTRY_VERSION);
            if (val != null) {
                seq.setEntryVersion(val);
            }
            val = getString(dbObj, UniprotFields.FIRST_PUBLIC_DATE);
            if (val != null) {
                seq.setFirstPublicDate(val);
            }
            val = getString(dbObj, UniprotFields.LAST_ANNOTATION_UPDATE_DATE);
            if (val != null) {
                seq.setLastAnnotationUpdateDate(val);
            }
            val = getString(dbObj, UniprotFields.LAST_SEQUENCE_UPDATE_DATE);
            if (val != null) {
                seq.setLastSequenceUpdateDate(val);
            }
            val = getIntegerVal(dbObj, UniprotFields.SEQUENCE_VERSION);
            if (val != null) {
                seq.setSequenceVersion(val);
            }
        }
    }

    /**
     * @param protein
     * @param dbObj
     * @param subgraph
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     */
    public static void setCitations(Protein protein, BasicDBObject dbObj, Subgraph subgraph) throws InvocationTargetException, URISyntaxException, NoSuchFieldException, IllegalAccessException {
        if (OntologyStrUtil.objectExists(dbObj, UniprotFields.CITATIONS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.CITATIONS);
            int cntr = 0;
            if (dbList != null && dbList.size() > 0) {
                for (Object objCite : dbList) {
                    BasicDBObject obj = (BasicDBObject) objCite;
                    if (obj != null) {
                        setCitationInfo(protein, obj, cntr++, subgraph);
                    }
                }
            }
        }
    }

    /**
     * Citation
     * setCitationInfo
     *
     * @param obj
     */
    public static void setCitationInfo(Protein protein, BasicDBObject obj, int cntr, Subgraph subGraph) throws IllegalAccessException, NoSuchFieldException, InvocationTargetException, URISyntaxException {
        Citation citation = new Citation();
        String val = getString(obj, UniprotFields.CITATION_TYPE);
        if (val != null) {
            citation.setCitationType(val);
        }
        val = getString(obj, UniprotFields.CITATION_TITLE);
        if (val != null) {
            citation.setCitationTitle(val);
        }
        val = getString(obj, UniprotFields.CITATION_PUBLICATION_DATE);
        if (val != null) {
            citation.setPublicationDate(val);
        }
        val = getStringFromList(obj, UniprotFields.AUTHORING_GROUPS);
        if (val != null) {
            citation.setAuthoringGroups(val);
        }
        val = getString(obj, UniprotFields.EVIDENCE_IDS);
        if (val != null) {
            citation.setEvidenceIds(val);
        }

        citation.setUniprot(protein.getUniprot());
        citation.setCntr(Integer.toString(cntr));
        setSummaryList(citation, obj);
        setSampleSources(obj, citation.getPublicationDate());
        BasicDBObject dbObj = getCitationXRefs(obj);
        if (dbObj != null) setDOI(citation, obj);
        subGraph.add(citation);
        createAuthors(protein, citation, obj, subGraph);
    }

    /**
     * @param protein
     * @param objList
     * @param subgraph
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     */
    public static void setAuthor(Protein protein, BasicDBList objList, Subgraph subgraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException {
        for (Object element : objList) {
            String a = (String) element;
            //log.info("a = " + a);
            //BasicDBObject a = (BasicDBObject)element;
            if (a != null) {
                CompoundKey compoundKey = getCompoundKey(a);
                if (compoundKey != null) {
                    String value = compoundKey.getValue();
                    Author author = (Author) subgraph.search(BioTypes.AUTHOR, BioFields.AUTHOR_NAME, value);
                    if (author == null) {
                        author = new Author();
                        setName(a, author);
                        subgraph.add(author);
                    }
                    //StatusUtil.idInsert(BioTypes.AUTHOR.toString(), compoundKey.getKey(), compoundKey.getValue());
                    protein.setAuthorRelations(author);

                }
            }
        }
    }

    /**
     * compoundkey index used in {@link BioTypes#AUTHOR}
     * getCompoundKey
     *
     * @param obj
     * @return CompoundKey
     */
    public static CompoundKey getCompoundKey(String obj) {
        List<String> list = getName(obj);
        if (list.size() < 3) {
            log.error("Could not extract the author name: " + obj);
            return null;
        }
        return new CompoundKey(IndexNames.AUTHOR_NAME,
                BioFields.FORE_NAME, BioFields.INITIALS, BioFields.LAST_NAME,
                list.get(0), list.get(1), list.get(2));
    }

    /**
     * "authors" : [
     * "Hillier L.W.",
     * "Warren W.",
     * "Obrien S.",
     * "Wilson R.K."
     * Van Huffel S.C.
     * ],
     * returns name <firstname> < initials> <lastname>
     * getName
     *
     * @param str
     * @return List<String>
     */
    public static List<String> getName(String str) {
        List<String> list = new ArrayList<>();
        if (null == str) return list;
        // firstname or forename
        list.add("");
        if (str != null) {
            int index = str.indexOf(".");
            //log.info("index dot =" + new Integer(index).toString());
            if (index > 0) {
                String lname = "";
                try {
                    lname = (str.substring(0, index - 1).trim());
                } catch (StringIndexOutOfBoundsException e) {
                    log.error("Could not extract last name: " + lname + " from name: " + str, e);
                }
                list.add(lname);
            }
            String initials = "";
            try {
                initials = (str.substring(index - 1, str.length()).trim());
            } catch (StringIndexOutOfBoundsException e) {
                log.error("Could not extract initials: " + initials + " from name: " + str, e);
            }
            list.add(initials);
        }
        return list;
    }

    /**
     * "authors" : [
     * "Leong J.S.",
     * "Jantzen S.G.",
     * "von Schalburg K.R.",
     * "Cooper G.A.",
     *
     * @param obj
     * @param author
     */
    public static void setName(String obj, Author author) {
        List<String> list = getName(obj);
        //log.info("author name =" + list.toString());
        if (list != null && list.size() == 3) {
            author.setForeName(list.get(0));
            author.setInitials(list.get(1));
            author.setLastName(list.get(2));
        }
    }


    /**
     * <pre>
     * 	"authors" : [
     * "Hillier L.W.",
     * "Warren W.",
     * "Obrien S.",
     * "Wilson R.K."
     * ],
     * </pre>
     *
     * @param protein
     * @param citation
     * @param obj
     * @param subGraph
     */
    public static void createAuthors(Protein protein, Citation citation, BasicDBObject obj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException {
        if (OntologyStrUtil.objectExists(obj, UniprotFields.AUTHORS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(obj, UniprotFields.AUTHORS);
            if (dbList != null) {
                if (dbList.size() > 0) {
                    setAuthor(protein, dbList, subGraph);
                }
            }
        }
    }


    /**
     * <pre>
     * "citationSummaryList" : [
     * "IDENTIFICATION"
     * ],
     * </pre>
     * setSummaryList
     *
     * @param citation
     * @param obj
     */
    public static void setSummaryList(Citation citation, BasicDBObject obj) {
        String val = getStringFromList(obj, UniprotFields.CITATION_SUMMARY_LIST);
        if (val != null) {
            citation.setSummaryList(val);
        }
    }

    /**
     * "citationXrefs" : {
     * "doi" : "10.1038/nature05805",
     * "pubmedId" : "17495919"
     * },
     * setDOI
     *
     * @param citation
     * @param dbObj
     */
    public static void setDOI(Citation citation, BasicDBObject dbObj) {
        if (dbObj != null) {
            if (OntologyStrUtil.objectExists(dbObj, UniprotFields.DOI)) {
                String val = OntologyStrUtil.getString(dbObj, UniprotFields.DOI);
                if (val != null) {
                    citation.setCitationReferenceDoi(val);
                }
            }
        }
    }

    /**
     * <pre>
     * 	"citationXrefs" : {
     * "doi" : "10.1038/nature05805",
     * "pubmedId" : "17495919"
     * },
     * </pre>
     *
     * @param obj
     */
    public static BasicDBObject getCitationXRefs(BasicDBObject obj) {
        if (!OntologyStrUtil.isObjectNull(obj, UniprotFields.CITATION_XREFS)) {
            return (BasicDBObject) obj.get(UniprotFields.CITATION_XREFS.toString());
        }
        return null;
    }

    /**
     * Example, Lists:
     * "evidenceIds" : [
     * "EI1"
     * ],
     * "authoringGroups" : [
     * "Ensembl"
     * ],
     * "citationSummaryList" : [
     * "IDENTIFICATION"
     * ],
     *
     * @param obj
     * @param field
     * @return
     */
    public static String getStringFromList(BasicDBObject obj, Enum field) {
        BasicDBList dbList = OntologyStrUtil.getBasicDBList(obj, field);
        if (dbList != null && dbList.size() > 0) {
            for (Object item : dbList) {
                if (item != null) {
                    return item.toString();
                }
            }
        }
        return null;
    }

    /**
     * <pre>
     * "comments" : [
     * {
     * "commentType" : "FUNCTION",
     * "commentStatus" : "Experimental",
     * "evidenceIds" : [
     * "EC1",
     * "EC2",
     * "EC3"
     * ]
     * },
     * {
     * "commentType" : "SUBUNIT",
     * "commentStatus" : "By similarity",
     * "evidenceIds" : [
     * "EC1"
     * ]
     * }
     *  ]
     *
     * </pre>
     *
     * @param protein
     * @param dbObj
     * @param subGraph
     */
    public static void setComments(Protein protein, BasicDBObject dbObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        if (OntologyStrUtil.objectExists(dbObj, UniprotFields.COMMENTS)) {
            BasicDBList list = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.COMMENTS);
            if (list != null && list.size() > 0) {
                for (Object obj : list) {
                    BasicDBObject commentObj = (BasicDBObject) obj;
                    if (commentObj != null) {
                        ProteinAnnotationComment comment = getComment(protein, commentObj, subGraph);
                        if (comment != null) {
                            protein.setCommentRelations(comment);
                        }
                    }
                }
            }
        }
    }

    /**
     *
     * @param protein
     * @param commentObj
     * @param subGraph
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static ProteinAnnotationComment getComment(Protein protein, BasicDBObject commentObj, Subgraph subGraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        String comment = OntologyStrUtil.getString(commentObj, UniprotFields.COMMENT);
        String commentType = OntologyStrUtil.getString(commentObj, UniprotFields.COMMENT_TYPE);
        String commentStatus = OntologyStrUtil.getString(commentObj, UniprotFields.COMMENT_STATUS);
        List<String> evidenceIds = getEvidenceIds(commentObj);
        ProteinAnnotationComment prComment = new ProteinAnnotationComment();
        prComment.setUniprot(protein.getUniprot());
        prComment.setComment(comment);
        prComment.setCommentType(commentType);
        prComment.setCommentStatus(commentStatus);
        if (evidenceIds != null) {
            prComment.setEvidenceId(evidenceIds);
        }
        subGraph.add(prComment);

        return prComment;
    }


    /**
     * "evidenceIds" : [
     * "EC1",
     * "EC2",
     * "EC3"
     * ]
     *
     * @param dbObj
     */
    public static List<String> getEvidenceIds(BasicDBObject dbObj) {
        if (OntologyStrUtil.listExists(dbObj, UniprotFields.EVIDENCE_IDS)) {
            BasicDBList ids = OntologyStrUtil.getList(dbObj, UniprotFields.EVIDENCE_IDS);
            if (ids != null) {
                List<String> list = new ArrayList<>();
                for (Object obj : ids) {
                    String value = (String) obj;
                    if (value != null) {
                        list.add(value.trim());
                    }
                }
                return list;
            }
        }
        return null;
    }


    /**
     * "sampleSources" : [
     * {
     * "sampleSourceType" : "TISSUE",
     * "sampleSource" : "Kidney epithelium"
     * "evidenceIds" : []
     * }
     * ]
     * <p>
     * For citation, sample sources
     *
     * @param dbObj
     * @param publicationDate
     */
    public static void setSampleSources(BasicDBObject dbObj, String publicationDate)  {
        //StringBuilder typeVal = new StringBuilder();
        if (OntologyStrUtil.objectExists(dbObj, UniprotFields.SAMPLE_SOURCES)) {
            BasicDBList objList = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.SAMPLE_SOURCES);
            if (objList != null && objList.size() > 0) {
                //if (objList.size() > 1) {
                    // throw new Exception("SampleSource objList > 1 " + objList.size());
                //}
                for (Object sample : objList) {
                    SampleSource sampleSource = new SampleSource();
                    sampleSource.setPublicationDate(publicationDate);
                    BasicDBObject obj = (BasicDBObject) sample;
                    String val = getString(obj, UniprotFields.SAMPLE_SOURCE_TYPE);
                    if (val != null) {
                        sampleSource.setSampleSourceType(val);
                    }
                    val = getString(obj, UniprotFields.SAMPLE_SOURCE);
                    if (val != null) {
                        sampleSource.setSampleSource(val);
                    }
                    List<String> list = getEvidenceIds(obj);
                    if (list != null && list.size() > 0) {
                        sampleSource.setSampleEvidenceIds(list);
                    }
                }
            }
        }
    }


    /**
     * "evidenceList" : [
     * {
     * "evidenceAttribute" : "PubMed=11390377",
     * "evidenceCategory" : "C",
     * "evidenceDate" : "Mon Dec 31 17:00:00 PST 2012",
     * "evidenceCode" : "experimental evidence",
     * "evidenceOriginName" : "XXX",
     * "evidenceType" : "Experimental"
     * },
     * <p>
     * {
     * "evidenceAttribute" : "Q8NFZ5",
     * "evidenceCategory" : "C",
     * "evidenceDate" : "Sun Jan 27 17:00:00 PST 2008",
     * "evidenceCode" : "sequence similarity",
     * "evidenceOriginName" : "SAO",
     * "evidenceType" : "Similarity"
     * }
     *
     * @param attribute
     */
    public static String getPubMedId(String attribute) {
        if (attribute != null) {
            if (attribute.contains("PubMed")) {
                return attribute.split("PubMed=")[1].trim();
            }
        }
        return null;

    }

    public static void setEvidence(Protein protein, BasicDBObject dbObj, Subgraph subgraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, IOException, HttpException, InterruptedException, ServiceException {
        if (OntologyStrUtil.objectExists(dbObj, UniprotFields.EVIDENCE_LIST)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, UniprotFields.EVIDENCE_LIST);
            if (dbList != null) {
                for (Object obj : dbList) {
                    setEvidenceAttributeRelation(protein, (BasicDBObject)obj, subgraph);
                }
            }
        }
    }

    /**
     *
     * @param pubMedId
     * @param protein
     * @param subgraph
     * @throws UnknownHostException
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     * @throws InterruptedException
     */
    public static void setPubMedRelation(String pubMedId, Protein protein, Subgraph subgraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, IOException, HttpException, InterruptedException {
        if (pubMedId != null) {
            PubMed pubmed = PubMedUtil.getPubmed(pubMedId, subgraph);
            if (null != pubmed)
               pubmed.setPubMedRelation(protein);
            // subgraph.add(pubmed);   this is added to subgraph in the PubMedUtil, so we do not need it here
        }
    }

    /**
     *
     * @param code
     * @return
     */
    public static BioRelTypes getEvidenceCode(String code)  {

        String assertion = "automatic assertion";
        String experiment = "experimental evidence";
        String similarity = "sequence similarity";
        String inferred = "inference from background scientific knowledge";
        String assertion_long = "imported information used in automatic assertion";
        String curated = "curated automatic assertion";
        String author = "non-traceable author statement";
        String literature = "curated literature reference";

        if (code != null) {
            code = code.trim();
            if (code.equals(assertion)) {
                return BioRelTypes.AUTOMATIC_ASSERTION;
            } else if (code.equals(experiment)) {
                return BioRelTypes.EXPERIMENTAL_EVIDENCE;
            } else if (code.equals(similarity)) {
                return BioRelTypes.SEQUENCE_SIMILARITY;
            } else if (code.equals(inferred)) {
                return BioRelTypes.INFERRED_FROM_SCIENTIFIC_KNOWLEDGE;
            } else if (code.equals(assertion_long)) {
                return BioRelTypes.AUTOMATIC_ASSERTION;
            } else if (code.equals(curated)) {
                return BioRelTypes.CURATED;
            } else if (code.equals(author)) {
                return BioRelTypes.NON_TRACEABLE_AUTHOR_STATEMENT;
            } else if (code.equals(literature)) {
                return BioRelTypes.CURATED_LITERATURE_REFERENCE;
            } else {
                //log.info("Code is not found =" + code);
                throw new RuntimeException("EvidenceList, evidenceCode not found =" + code);
            }
        }
        return BioRelTypes.FOUND_EVIDENCE_IN;
    }

    /**
     *
     * @param obj
     * @param protein
     * @param subgraph
     * @throws IllegalAccessException
     * @throws InterruptedException
     * @throws HttpException
     * @throws ServiceException
     * @throws IOException
     * @throws URISyntaxException
     * @throws InvocationTargetException
     * @throws NoSuchFieldException
     */
    public static void setProteinEvidenceRelation(BasicDBObject obj, Protein protein, Subgraph subgraph) throws IllegalAccessException, InterruptedException, HttpException, ServiceException, IOException, URISyntaxException, InvocationTargetException, NoSuchFieldException {
        String attribute = OntologyStrUtil.getString(obj, UniprotFields.EVIDENCE_ATTRIBUTE);
        String code = OntologyStrUtil.getString(obj, UniprotFields.EVIDENCE_CODE);
        log.info("code =" + code);
        if (attribute != null) {
            Protein proteinEntity = getProtein(attribute.trim(), subgraph);
            if (code != null) {
                protein.setEvidenceRelations(proteinEntity, getEvidenceCode(code));
            }
        }
            
            /*String category = OntologyStrUtil.getString(obj, UniprotFields.EVIDENCE_CATEGORY);
            String date = OntologyStrUtil.getString(obj, UniprotFields.EVIDENCE_DATE);
            
            String originName = OntologyStrUtil.getString(obj, UniprotFields.EVIDENCE_ORIGIN_NAME);
            String type = OntologyStrUtil.getString(obj, UniprotFields.EVIDENCE_TYPE);
            ProteinEvidenceAttributeRelation relation = new ProteinEvidenceAttributeRelation();
            
            if (relation != null) {
                if (category != null) { 
                    relation.setCategory(category);
                }
                if (date != null) {
                    relation.setDate(date);
                }
                if (code != null) {
                    relation.setCode(code);
                }
                if (originName != null) {
                    relation.setOrginName(originName);
                }
                if (type != null) {
                    relation.setType(type);
                }
              */

    }

    /**
     *
     * @param protein
     * @param obj
     * @param subgraph
     * @throws UnknownHostException
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     * @throws InterruptedException
     */
    public static void setEvidenceAttributeRelation(Protein protein, BasicDBObject obj, Subgraph subgraph) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, IOException, HttpException, InterruptedException, ServiceException {
        String attribute = OntologyStrUtil.getString(obj, UniprotFields.EVIDENCE_ATTRIBUTE);
        String pubMedId = getPubMedId(attribute);
        if (pubMedId != null) {
            setPubMedRelation(pubMedId, protein, subgraph);
        } else {
            setProteinEvidenceRelation(obj, protein, subgraph);
        }
    }


    /**
     * processProtein
     *
     * @param protein
     * @param id
     * @param subGraph
     * @return
     * @throws ServiceException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     * @throws InterruptedException
     * @throws HttpException
     * @throws URISyntaxException
     */
    public static void processProtein(Protein protein, String id, Subgraph subGraph) throws ServiceException, IOException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, InterruptedException, HttpException, URISyntaxException {
        log.info("processProtein(), id = " + id);
        if (id.equals("-") || id == null) {
            log.info("did not add protein as proteinId= " + id);
            return;
        }

        BasicDBObject obj = UniProtAccess.getProteinObj(id);
        if (obj != null) {
            log.info("found protein =" + id);
            setFullName(protein, obj);
            setShortName(protein, obj);
            setProteinNames(protein, obj);
            // add protein now
            subGraph.add(protein);

            if (OntologyStrUtil.objectExists(obj, UniprotFields.CITATIONS)) {
                setCitationPubMedRelation(protein, obj, subGraph);
            }

            if (OntologyStrUtil.objectExists(obj, UniprotFields.SECONDARY_UNIPROT_ACCESSIONS)) {
                protein.setUniprotSecondaryRefs(getSecondaryUniProtAccessions(obj));
            }

            if (OntologyStrUtil.objectContains(obj, UniprotFields.UNIPROT_ENTRY_TYPE)) {
                protein.setUniprotEntryType(getUniProtEntryType(obj));
            }


            if (OntologyStrUtil.objectExists(obj, UniprotFields.KEYWORDS)) {
                protein.setUniprotKeywords(getKeywords(obj));
            }

            if (OntologyStrUtil.objectContains(obj, UniprotFields.NCBI_TAXONOMY_IDS)) {
                setNcbiTaxonmyRelationship(protein, obj, subGraph);
            }

            if (OntologyStrUtil.objectContains(obj, UniprotFields.GENES)) {
                setGeneRelationship(protein, obj, subGraph);
            }

            if (OntologyStrUtil.objectContains(obj, UniprotFields.GO_TERM_LIST)) {
                setGeneOntologyRelationship(protein, obj, subGraph);
            }

            /**
             * citations, authors, pubmed (crossXRef) relationships
             */
            setCitations(protein, obj, subGraph);

            // sequence - entryaudit, sequence, featureList 
            setSequence(protein, obj, subGraph);

            /**
             * comments
             */
            setComments(protein, obj, subGraph);

            // evidenceList - pubMed

            setEvidence(protein, obj, subGraph);
            log.info("added protein" + protein.toString());

            //RedbasinTemplate.saveSubgraph(subGraph);
        }
        //return null;
    }
}
      

