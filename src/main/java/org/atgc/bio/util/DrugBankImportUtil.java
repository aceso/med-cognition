/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.sun.istack.NotNull;
import org.apache.avro.generic.GenericData;
import org.apache.http.HttpException;
import org.atgc.bio.BioFields;
import org.atgc.bio.DrugBankFields;
import org.atgc.bio.DrugBankSplitter;
import org.atgc.bio.domain.*;
import org.atgc.bio.repository.CompoundKey;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.ImportCollectionNames;
import uk.ac.ebi.uniprot.dataservice.client.exception.ServiceException;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;
import java.util.stream.Collectors;


/**
 *
 * uses Compound bio entity
 *
 * unique drugs
 * Aspergillis, Candida and other fungi
 Bacteria and protozoa
 Bacteria
 Candida albicans and other yeasts
 Condyloma acuminatum
 Dermatophytic fungi including Trichophyton, Microsporum and Epidermophyton
 Enteric bacteria and other eubacteria
 Enteric gram-negative rods
 Fungi, yeast and protozoans
 Fungi
 Gram negative and gram positive bacteria
 Gram negative, positive bacteria and plasmodium
 Gram-negative Bacteria
 Gram-negative bacilli
 Gram-positive Bacteria
 Head lice
 Helminthic Microorganisms
 Hepatitis B virus
 Hepatitis C virus, RSV and other RNA/DNA viruses
 Herpes simplex virus
 Human Herpes Virus
 Human Immunodeficiency Virus
 Human Influenza A Virus
 Humans and other mammals
 Influenza A virus
 Influenza B virus
 Influenza Virus
 Microbes (bacteria, parasites)
 Mycobacteria
 Mycobacterium tuberculosis
 Mycobacterium
 Parasitic nematodes and other roundworms
 Parasitic protozoa and helminths
 Plasmodium
 Pneumocystis carinii
 Protozoa
 Roundworms, hookworms, and other helminth species
 Sarcoptes scabiei
 Scabies (Sarcoptes scabei) and other insects
 Schistosoma mansoni
 Trichomonas vaginalis, Giardia duodenalis, and Entamoeba histolytica
 Various Fungus Species
 Various aerobic and anaerobic microorganisms
 Various gram-negative and gram-positive eubacteria
 Various viruses
 Yeast and other Trichophyton or Microsporum fungi
 Yeast and other fungi
 Yeast, Molds, Trypanosomes
 *
 {
 "_id" : ObjectId("52956552300417c0cfbb1038"),
 "@dtyp" : "biotech",
 "@created" : "2005-06-13 07:24:05 -0600",
 "@updated" : "2013-05-12 21:37:25 -0600",
 "@version" : "3.0",
 "drugbank-id" : "DB00001",
 "name" : "Lepirudin",
 "description" : "Lepirudin is identical to natural hirudin except for substitution of leucine for isoleucine at the N-terminal end of the molecule and the absence of a sulfate group on the tyrosine at position 63. It is produced via yeast cells.\r",
 "cas-number" : "120993-53-5",
 "general-references" : "# Smythe MA, Stephens JL, Koerber JM, Mattson JC: A comparison of lepirudin and argatroban outcomes. Clin Appl Thromb Hemost. 2005 Oct;11(4):371-4. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/16244762\r# Tardy B, Lecompte T, Boelhen F, Tardy-Poncet B, Elalamy I, Morange P, Gruel Y, Wolf M, Francois D, Racadot E, Camarasa P, Blouch MT, Nguyen F, Doubine S, Dutrillaux F, Alhenc-Gelas M, Martin-Toutain I, Bauters A, Ffrench P, de Maistre E, Grunebaum L, Mouton C, Huisse MG, Gouault-Heilmann M, Lucke V: Predictive factors for thrombosis and major bleeding in an observational study in 181 patients with heparin-induced thrombocytopenia treated with lepirudin. Blood. 2006 Sep 1;108(5):1492-6. Epub 2006 May 11. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/16690967\r# Lubenow N, Eichler P, Lietz T, Greinacher A: Lepirudin in patients with heparin-induced thrombocytopenia - results of the third prospective study (HAT-3) and a combined analysis of HAT-1, HAT-2, and HAT-3. J Thromb Haemost. 2005 Nov;3(11):2428-36. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/16241940\r# Askari AT, Lincoff AM: Antithrombotic Drug Therapy in Cardiovascular Disease. 2009 Oct; pp. 440–. ISBN 9781603272346. \"Google books\":http://books.google.com/books?id=iadLoXoQkWEC&pg=PA440. ",
 "synthesis-reference" : [ ],
 "indication" : "For the treatment of heparin-induced thrombocytopenia",
 "pharmacology" : "Lepirudin is used to break up clots and to reduce thrombocytopenia. It binds to thrombin and prevents thrombus or clot formation. It is a highly potent, selective, and essentially irreversible inhibitor of thrombin and clot-bond thrombin. Lepirudin requires no cofactor for its anticoagulant action. Lepirudin is a recombinant form of hirudin, an endogenous anticoagulant found in medicinal leeches.",
 "mechanism-of-action" : "Lepirudin forms a stable non-covalent complex with alpha-thrombin, thereby abolishing its ability to cleave fibrinogen and initiate the clotting cascade. The inhibition of thrombin prevents the blood clotting cascade. ",
 "toxicity" : "In case of overdose (eg, suggested by excessively high aPTT values) the risk of bleeding is increased.",
 "biotransformation" : "Lepirudin is thought to be metabolized by release of amino acids via catabolic hydrolysis of the parent drug. However, con-clusive data are not available. About 48% of the administration dose is excreted in the urine which consists of unchanged drug (35%) and other fragments of the parent drug.",
 "absorption" : "Bioavailability is 100% following injection.",
 "half-life" : "Approximately 1.3 hours",
 "protein-binding" : [ ],
 "route-of-elimination" : "Lepirudin is thought to be metabolized by release of amino acids via catabolic hydrolysis of the parent drug. About 48% of the administration dose is excreted in the urine which consists of unchanged drug (35%) and other fragments of the parent drug.",
 "volume-of-distribution" : "* 12.2 L [Healthy young subjects (n = 18, age 18-60 years)]\r* 18.7 L [Healthy elderly subjects (n = 10, age 65-80 years)]\r* 18 L [Renally impaired patients (n = 16, creatinine clearance below 80 mL/min)]\r* 32.1 L [HIT patients (n = 73)]",
 "clearance" : "* 164 ml/min [Healthy 18-60 yrs]\r* 139 ml/min [Healthy 65-80 yrs]\r* 61 ml/min [renal impaired]\r* 114 ml/min [HIT (Heparin-induced thrombocytopenia)]",
 "secondary-accession-numbers" : [
 "BIOD00024",
 "BTD00024"
 ],
 "groups" : {
 "group" : "approved"
 },
 "taxonomy" : {
 "kingdom" : [ ],
 "substructures" : [ ]
 },
 "synonyms" : {
 "synonym" : "Hirudin variant-1"
 },
 "salts" : [ ],
 "brands" : {
 "brand" : "Refludan"
 },
 "mixtures" : [ ],
 "packagers" : [
 {
 "name" : "Bayer Healthcare",
 "url" : "http://www.bayerhealthcare.com"
 },
 {
 "name" : "Berlex Labs",
 "url" : "http://www.berlex.com"
 }
 ],
 "manufacturers" : {
 "manufacturer" : {
 "@generic" : "false",
 "#text" : "Bayer healthcare pharmaceuticals inc"
 }
 },
 "prices" : {
 "price" : {
 "description" : "Refludan 50 mg vial",
 "cost" : {
 "@currency" : "USD",
 "#text" : "273.19"
 },
 "unit" : "vial"
 }
 },
 "categories" : [
 "Anticoagulants",
 "Antithrombotic Agents",
 "Fibrinolytic Agents"
 ],
 "affected-organisms" : {
 "affected-organism" : "Humans and other mammals"
 },
 "dosages" : {
 "dosage" : {
 "form" : "Powder, for solution",
 "route" : "Intravenous",
 "strength" : [ ]
 }
 },
 "atc-codes" : {
 "atc-code" : "B01AE02"
 },
 "ahfs-codes" : {
 "ahfs-code" : "20:12.04.12"
 },
 "patents" : [
 {
 "number" : "5180668",
 "country" : "United States",
 "approved" : "1993-01-19",
 "expires" : "2010-01-19"
 },
 {
 "number" : "1339104",
 "country" : "Canada",
 "approved" : "1997-07-29",
 "expires" : "2014-07-29"
 }
 ],
 "food-interactions" : [ ],
 "drug-interactions" : [
 {
 "drug" : "DB01381",
 "name" : "Ginkgo biloba",
 "description" : "Additive anticoagulant/antiplatelet effects may increase bleed risk. Concomitant therapy should be avoided."
 },
 {
 "drug" : "DB00374",
 "name" : "Treprostinil",
 "description" : "The prostacyclin analogue, Treprostinil, increases the risk of bleeding when combined with the anticoagulant, Lepirudin. Monitor for increased bleeding during concomitant thearpy. "
 }
 ],
 "protein-sequences" : {
 "protein-sequence" : {
 "header" : "DB00001 sequence",
 "chain" : "LVYTDCTESGQNLCLCEGSNVCGQGNKCILGSDGEKNQCVTGEGTPKPQSHNDGDFEEIPEEYLQ"
 }
 },
 "experimental-properties" : [
 {
 "kind" : "Melting Point",
 "value" : "65 °C",
 "source" : "Otto, A. & Seckler, R. Eur. J. Biochem. 202:67-73 (1991)"
 },
 {
 "kind" : "Hydrophobicity",
 "value" : "-0.777",
 "source" : [ ]
 },
 {
 "kind" : "Isoelectric Point",
 "value" : "4.04",
 "source" : [ ]
 },
 {
 "kind" : "Molecular Weight",
 "value" : "6963.4250",
 "source" : [ ]
 },
 {
 "kind" : "Molecular Formula",
 "value" : "C287H440N80O110S6",
 "source" : [ ]
 }
 ],
 "external-identifiers" : [
 {
 "resource" : "Drugs Product Database (DPD)",
 "identifier" : "2240996"
 },
 {
 "resource" : "KEGG Drug",
 "identifier" : "D06880"
 },
 {
 "resource" : "National Drug Code Directory",
 "identifier" : "50419-150-57"
 },
 {
 "resource" : "PharmGKB",
 "identifier" : "PA450195"
 },
 {
 "resource" : "UniProtKB",
 "identifier" : "P01050"
 }
 ],
 "external-links" : [
 {
 "resource" : "Wikipedia",
 "url" : "http://en.wikipedia.org/wiki/Lepirudin"
 },
 {
 "resource" : "RxList",
 "url" : "http://www.rxlist.com/cgi/generic/lepirudin.htm"
 },
 {
 "resource" : "Drugs.com",
 "url" : "http://www.drugs.com/cdi/lepirudin.html"
 }
 ],
 "targets" : {
 "target" : {
 "@partner" : "54",
 "actions" : {
 "action" : "inhibitor"
 },
 "references" : "# Turpie AG: Anticoagulants in acute coronary syndromes. Am J Cardiol. 1999 Sep 2;84(5A):2M-6M. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/10505536\r# Warkentin TE: Venous thromboembolism in heparin-induced thrombocytopenia. Curr Opin Pulm Med. 2000 Jul;6(4):343-51. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/10912644\r# Eriksson BI: New therapeutic options in deep vein thrombosis prophylaxis. Semin Hematol. 2000 Jul;37(3 Suppl 5):7-9. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/11055889\r# Fabrizio MC: Use of ecarin clotting time (ECT) with lepirudin therapy in heparin-induced thrombocytopenia and cardiopulmonary bypass. J Extra Corpor Technol. 2001 May;33(2):117-25. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/11467439\r# Szaba FM, Smiley ST: Roles for thrombin and fibrin(ogen) in cytokine/chemokine production and macrophage adhesion in vivo. Blood. 2002 Feb 1;99(3):1053-9. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/11807012\r# Chen X, Ji ZL, Chen YZ: TTD: Therapeutic Target Database. Nucleic Acids Res. 2002 Jan 1;30(1):412-5. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/11752352",
 "known-action" : "yes"
 }
 },
 "enzymes" : [ ],
 "transporters" : [ ],
 "carriers" : [ ]
 }

 *
 */
@SuppressWarnings("javadoc")
public class DrugBankImportUtil {

    private static final Logger log = LogManager.getLogger(DrugBankImportUtil.class);

    /**
     * This method uses compound collection and adds genesymbol, importstatus
     * and date to Druglistcollection importstatus - due is by default.
     *
     * @throws UnknownHostException
     */

    public static void main(String[] args) throws IOException, IllegalAccessException, HttpException, InstantiationException, NoSuchFieldException, URISyntaxException, InvocationTargetException, ClassNotFoundException, ServiceException, InterruptedException {

        try (DBCursor dbCursor = DrugBankSplitter.getCollection(ImportCollectionNames.DRUGBANK).findDBCursor("{}")) {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject) dbCursor.next();
                String drugName = DrugBankSplitter.getDrugName(result);
                if (!StatusUtil.idExists(DrugBankFields.DRUG_BANK.toString(), DrugBankFields.NAME.toString(), drugName)) {
                    log.info("******* drugName =" + drugName);
                }
                setDrug(result, drugName);
                log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
                log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
            }
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
    }


    /**
     *
     * @param obj
     * @param drugName
     * @throws Exception
     */
    private static void setDrug(BasicDBObject obj, String drugName) throws ClassNotFoundException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, IOException, HttpException, ServiceException, InterruptedException {
        if (drugName == null || obj == null) {
            return;
        }
        Subgraph subGraph = new Subgraph();
        Object bio = NCICompound.getBioEntityFromBioType(subGraph, BioTypes.DRUG, BioFields.DRUG_NAME, drugName);
        Drug drug = (Drug) bio;
        if (bio == null) {
            drug = new Drug();
            drug.setDrugName(drugName);
        }
        if (drug.getCasId() == null) {
            drug.setCasId(getCasId(obj));
        }
        if (drug.getDrugDescription() == null) {
            drug.setDrugDescription(getDrugDescription(obj));
        }
        if (drug.getDrugType() == null) {
            drug.setDrugType(getDrugType(obj));
        }
        if (drug.getDrugbankId() == null) {
            drug.setDrugbankId(getDrugBankId(obj));
        }

        //general references - pubmed

        // synthesis references -
        if (drug.getIndication() == null) {
            drug.setIndication(getIndication(obj));
        }
        if (drug.getPharmacology() == null) {
            drug.setPharmacology(getPharmcology(obj));
        }
        if (drug.getMechanismOfAction() == null) {
            drug.setMechanismOfAction((getMechanismOfAction(obj)));
        }
        if (drug.getToxicity() == null) {
            drug.setToxicity(OntologyStrUtil.getString(obj, DrugBankFields.TOXICITY));
        }
        if (drug.getBiotransformation() == null) {
            drug.setBiotransformation(OntologyStrUtil.getString(obj, DrugBankFields.BIO_TRANSFORMATION));
        }
        if (drug.getAbsorption() == null) {
            drug.setAbsorption(OntologyStrUtil.getString(obj, DrugBankFields.ABSORPTION));
        }
        if (drug.getHalfLife() == null) {
            drug.setHalfLife(OntologyStrUtil.getString(obj, DrugBankFields.HALF_LIFE));
        }
        if (drug.getProteinBinding() == null) {
            drug.setProteinBinding(OntologyStrUtil.getString(obj, DrugBankFields.PROTEIN_BINDING));
        }
        if (drug.getRouteOfElimination() == null) {
            drug.setRouteOfElimination(OntologyStrUtil.getString(obj, DrugBankFields.ROUTE_OF_ELIMINATION));
        }
        if (drug.getVolumeOfDistribution() == null) {
            drug.setVolumeOfDistribution(OntologyStrUtil.getString(obj, DrugBankFields.VOLUME_OF_DISTRIBUTION));
        }
        if (drug.getClearance() == null) {
            drug.setClearance(OntologyStrUtil.getString(obj, DrugBankFields.CLEARANCE));
        }

        //secondary accession numbers
        if (drug.getSecondaryAccessionNumbers() == null) {
            addAccessionNumbers(obj, drug);
        }
        if (drug.getDrugStatusGroups() == null) {
            addStatusGroups(obj, drug);
        }

        // add pubmed generic references
        // addDrugSubStructures();
        // verify Tanisha

        if (drug.getSalts() == null) {
            drug.setSalts(getSalts(obj));
        }
        if (drug.getSynonyms() == null) {
            addSynonyms(obj, drug);
        }
        if (drug.getBrands() == null) {
            drug.setBrands(getBrands(obj));
        }
        if (drug.getMixtures() == null) {
            setMixtures(obj, drug);
        }
        if (drug.getDrugPackagers() == null) {
            setDrugPackagers(obj, drug, subGraph);
        }
        if (drug.getDrugManufacturers() == null) {
            setDrugManufacturers(obj, drug, subGraph);
        }
        if (drug.getDrugPrices() == null) {
            setDrugPrices(obj, drug, subGraph);
        }
        if (drug.getCategories() == null) {
            addCategories(obj, drug);
        }

        BasicDBObject taxObj;
        if ((taxObj = getTaxonomyObj(obj)) != null) {
            if (drug.getKingdom() == null) {
                drug.setKingdom(getKingdom(taxObj));
            }
            if (drug.getSubstructures() == null) {
                setSubstructures(taxObj, drug);
            }
        }

        setDosages(obj, drug, subGraph);
        if (drug.getAtcCodes() == null) {
            drug.setAtcCodes(getAtcCodes(obj));
        }
        if (drug.getAhfsCodes() == null) {
            drug.setAhfsCodes(getAhfsCodes(obj));
        }


        setDrugPatents(obj, drug, subGraph);
        setDrugInteractions(obj, drug, subGraph);
        setFoodInteractions(obj, drug);
        setProteinRelation(obj, drug, subGraph);
        subGraph.add(drug);
        PersistenceTemplate.saveSubgraph(subGraph);

    }

    private static List<String> getAtcCodes(BasicDBObject obj) {
        return getList(obj, DrugBankFields.ATC_CODES);
    }

    private static List<String> getAhfsCodes(BasicDBObject obj) {
        return getList(obj, DrugBankFields.AHFS_CODES);
    }

    private static BasicDBObject getTaxonomyObj(BasicDBObject obj) {
        return OntologyStrUtil.getDBObject(obj, DrugBankFields.TAXONOMY);
    }

    private static List<String> getSalts(BasicDBObject obj) {
        return getList(obj, DrugBankFields.SALTS);
    }

    private static List<String> getBrands(BasicDBObject obj) {
        return getList(obj, DrugBankFields.BRANDS);
    }

    private static String getDrugType(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.DTYPE);
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getCasId(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.CAS_NUMBER)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.CAS_NUMBER);
        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.CAS_NUMBER).toString();
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getDrugDescription(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.DESCRIPTION)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.DESCRIPTION);
        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.DESCRIPTION).toString();
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getDrugBankId(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.DRUGBANK_ID);
    }

    /*
     *
     * @param dbObj
     * @return
     */
    private static String getDateCreated(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.CREATED);
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getDateUpdated(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.UPDATED);
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getVersion(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.VERSION);
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getIndication(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.INDICATION)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.INDICATION);

        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.INDICATION).toString();
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getMechanismOfAction(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.MECHANISM_OF_ACTION)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.MECHANISM_OF_ACTION);
        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.MECHANISM_OF_ACTION).toString();
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getPharmcology(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.PHARMACOLOGY)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.PHARMACOLOGY);
        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.PHARMACOLOGY).toString();
    }

    /**
     *
     * @param dbObj
     * @param key
     * @return
     */
    private static String getValue(BasicDBObject dbObj, DrugBankFields key) {
        return OntologyStrUtil.getString(dbObj, key);
    }

    /**
     *
     * @param dbObj
     * @param drug
     */
    private static void addAccessionNumbers(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.SECONDARY_ACCESSION_NUMBERS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.SECONDARY_ACCESSION_NUMBERS);
            for (Object obj : dbList) {
                drug.addSecondaryAccessionNumber(obj.toString());
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param drug
     */
    private static void addStatusGroups(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.GROUPS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.GROUPS);
            for (Object obj : dbList) {
                drug.addDrugStatusGroup(obj.toString());
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param drug
     */
    private static void addSynonyms(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.SYNONYMS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.SYNONYMS);
            if (dbList != null) {
                List<String> synList = dbList.stream().map(Object::toString).collect(Collectors.toList());
                drug.setSynonyms(synList);
                /*
                List<String> synList = new ArrayList<>();
                for (Object obj : dbList) {
                    synList.add(obj.toString());
                }
                drug.setSynonyms(synList);
                 */
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param name
     * @return
     */
    private static List<String> getList(BasicDBObject dbObj, DrugBankFields name) {
        if (OntologyStrUtil.isObjectNull(dbObj, name)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, name);
            if (dbList != null) {
                List<String> list = new ArrayList<>();
                for (Object obj : dbList) {
                    list.add(obj.toString());
                }
                return list;
            }
        }
        return new ArrayList<>();
    }

    /**
     *
     * @param dbObj
     * @param drug
     */
    private static void setSubstructures(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.SUB_STRUCTURES)) {
            List<String> subStructures = getList(dbObj, DrugBankFields.SUB_STRUCTURES);
            if (subStructures != null) {
                drug.setSubstructures(subStructures);
            }
        }
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getIngredients(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.INGREDIENTS)) {
            if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.INGREDIENTS)) {
                return OntologyStrUtil.getString(dbObj, DrugBankFields.INGREDIENTS);
            }
            return OntologyStrUtil.getList(dbObj, DrugBankFields.INGREDIENTS).toString();
        }
        return null;
    }

    /**
     *
     * @param dbObj
     * @return
     */
    private static String getKingdom(BasicDBObject dbObj) {
        return getList(dbObj, DrugBankFields.KINGDOM).toString();
    }


    /**
     *
     * @param dbObj
     * @param drug
     */
    private static void setMixtures(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.MIXTURES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.MIXTURES);
            if (dbList != null) {
                Map<String, String> map = new HashMap<>();
                for (Object obj : dbList) {
                    String key = getValue((BasicDBObject) obj, DrugBankFields.NAME);
                    String value = getIngredients((BasicDBObject)obj);
                    if (key != null && value != null) {
                        map.put(key, value);
                    }
                }
                drug.setMixtures(map);
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param drug
     * @param subGraph
     * @throws Exception
     */
    private static void setDrugPackagers(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws ClassNotFoundException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.PACKAGERS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.PACKAGERS);
            if (dbList != null) {
                HashSet<DrugPackager> dpSet = new HashSet<>();
                for (Object packageObj : dbList) {
                    log.info("packagers " + packageObj);
                    BasicDBObject obj = (BasicDBObject)packageObj;
                    if (OntologyStrUtil.isObjectNull(obj, DrugBankFields.PACKAGER)) {
                        obj = OntologyStrUtil.getDBObject(obj, DrugBankFields.PACKAGER);
                    }
                    String packagerName = getValue(obj, DrugBankFields.NAME);
                    if (packagerName == null) {
                        continue;
                    }
                    Object bioEntity = Subgraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_PACKAGER, BioFields.NAME, packagerName);
                    DrugPackager drugPackager = (DrugPackager) bioEntity;
                    if (drugPackager == null) {
                        drugPackager = new DrugPackager();
                        drugPackager.setName(packagerName);
                    }
                    if (drugPackager.getUrl() == null) {
                        String url = getList(obj, DrugBankFields.URL).toString();
                        drugPackager.setUrl(url);
                    }
                    subGraph.add(drugPackager);
                    dpSet.add(drugPackager);
                }
                drug.setDrugPackagers(dpSet);
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param drug
     * @param subGraph
     * @throws Exception
     */
    private static void setDrugManufacturers(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws ClassNotFoundException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.MANUFACTURERS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.MANUFACTURERS);
            if (dbList != null) {
                HashSet<DrugManufacturer> dmSet = new HashSet<>();
                for (Object mfrObj : dbList) {
                    BasicDBObject obj = (BasicDBObject)mfrObj;
                    if (OntologyStrUtil.isObjectNull(obj, DrugBankFields.MANUFACTURER)) {
                        obj = OntologyStrUtil.getDBObject(obj, DrugBankFields.MANUFACTURER);
                    }
                    String manufacturerName = getValue(obj, DrugBankFields.TEXT);
                    if (manufacturerName == null) {
                        continue;
                    }
                    Object bioEntity = Subgraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_MANUFACTURER, BioFields.NAME, manufacturerName);
                    DrugManufacturer drugManufacturer = (DrugManufacturer) bioEntity;
                    if (drugManufacturer == null) {
                        drugManufacturer = new DrugManufacturer();
                        drugManufacturer.setName(manufacturerName);

                    }
                    if (drugManufacturer.getGeneric() == null) {
                        drugManufacturer.setGeneric(getValue(obj, DrugBankFields.GENERIC));
                    }
                    subGraph.add(drugManufacturer);
                    dmSet.add(drugManufacturer);
                }
                drug.setDrugManufacturers(dmSet);
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param drug
     * @param subGraph
     * @throws Exception
     */
    private static void setDrugPrices(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws IllegalAccessException, NoSuchFieldException, URISyntaxException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        if (drug.getCasId() == null) {
            return;
        }
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.PRICES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.PRICES);
            if (dbList != null) {
                HashSet<DrugPrice> dmSet = new HashSet<>();
                for (Object priceObj : dbList) {
                    BasicDBObject obj = (BasicDBObject)priceObj;
                    if (OntologyStrUtil.isObjectNull(obj, DrugBankFields.PRICE)) {
                        obj = OntologyStrUtil.getDBObject(obj, DrugBankFields.PRICE);
                    }
                    /* check for compound keys */
                    String description = getValue(obj, DrugBankFields.DESCRIPTION);
                    if (description == null) {
                        continue;
                    }
                    DrugPrice drugPrice;
                    drugPrice = new DrugPrice();
                    drugPrice.setChemicalAbstractId(drug.getCasId());
                    drugPrice.setDescription(description);
                    CompoundKey compoundKey = CompoundKey.getCompoundKey(drugPrice);
                    Object bioEntity = Subgraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_PRICE, BioFields.DRUG_PRICE, compoundKey.getValue());
                    if (bioEntity != null) {
                        // existing drug already has compoundkey
                        drugPrice = (DrugPrice) bioEntity;
                    }

                    if (drugPrice.getDrugName() == null) {
                        drugPrice.setDrugName((drug.getDrugName()));
                    }

                    if (drugPrice.getUnit() == null) {
                        drugPrice.setUnit(getValue(obj, DrugBankFields.UNIT));
                    }

                    if (drugPrice.getCurrency() == null) {
                        drugPrice.setCurrency(getValue(obj, DrugBankFields.CURRENCY));
                    }

                    if (drugPrice.getCost() == null) {
                        drugPrice.setCost(getValue(obj, DrugBankFields.TEXT));
                    }
                    subGraph.add(drugPrice);
                    dmSet.add(drugPrice);
                }
                drug.setDrugPrices(dmSet);
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param drug
     */
    private static void addCategories(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.CATEGORIES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.CATEGORIES);
            for (Object obj : dbList) {
                drug.addCategory(obj.toString());
            }
        }
    }

    /*
     * The ncbiTaxId is not available, so we cannot relate to the NcbiTaxonomy.
     *
     * @param dbObj
     * @param drug
     * @param subGraph
     * @throws Exception
     */
    /*
    private static void setOrganismRelation(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.AFFECTED_ORGANISMS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.AFFECTED_ORGANISMS);
            if (dbList != null) {
                for (Object organismObj : dbList) {
                    BasicDBObject obj = (BasicDBObject)organismObj;
                    if (OntologyStrUtil.isObjectNull(obj, DrugBankFields.AFFECTED_ORGANISM)) {
                        obj = OntologyStrUtil.getDBObject(obj, DrugBankFields.AFFECTED_ORGANISM);
                    }
                    String organismName = obj.toString();
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.ORGANISM, BioFields.ORGANISM_SHORT_LABEL, organismName);
                    Organism organism = (Organism)bioEntity;
                    if (organism == null) {
                        organism = new Organism();
                        organism.setOrganismShortLabel(organismName);
                    }
                    if (organism != null) {
                        subGraph.add(organism);
                        drug.setOrganismComponents(organism);
                    }
                }
            }
        }
    }
    */

    /**
     *
     * @param dbObj
     * @param drug
     * @param subGraph
     * @throws Exception
     */
    private static void setDosages(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws IllegalAccessException, NoSuchFieldException, URISyntaxException, InstantiationException, ClassNotFoundException, InvocationTargetException {
        String casId = drug.getCasId();
        if (casId == null) {
            return;
        }
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.DOSAGES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.DOSAGES);
            if (dbList != null) {
                HashSet<Dosage> dosageSet = new HashSet<>();
                for (Object obj : dbList) {
                    log.info("dosages object =" + obj);
                    BasicDBObject dosageObj = (BasicDBObject)obj;
                    if (OntologyStrUtil.isObjectNull((BasicDBObject) obj, DrugBankFields.DOSAGE)) {
                        dosageObj = OntologyStrUtil.getDBObject((BasicDBObject) obj, DrugBankFields.DOSAGE);
                    }
                    String form = getValue(dosageObj, DrugBankFields.FORM);
                    if (form == null) {
                        continue;
                    }
                    Dosage dosage = new Dosage();
                    dosage.setForm(form);
                    dosage.setChemicalAbstractId(casId);
                    dosage.setRoute(getValue(dosageObj, DrugBankFields.ROUTE));
                    CompoundKey compoundKey = CompoundKey.getCompoundKey(dosage);
                    Dosage bioEntity;
                    if (compoundKey != null) {
                        bioEntity = (Dosage) Subgraph.getBioEntityFromBioType(subGraph, BioTypes.DOSAGE, BioFields.DOSAGE, compoundKey.getValue());
                        if (bioEntity != null) {
                            dosage = bioEntity;
                        }
                    }

                    if (dosage.getStrength() == null) {
                        dosage.setStrength(getList(dosageObj, DrugBankFields.STRENGTH).toString());
                    }
                    subGraph.add(dosage);
                    dosageSet.add(dosage);
                }
                drug.setDrugDosage(dosageSet);
            }
        }
    }


    /**
     * setDrugPatents
     * @param dbObj
     * @param drug
     * @param subGraph
     * @throws ClassNotFoundException
     * @throws URISyntaxException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws InvocationTargetException
     */
    private static void setDrugPatents(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws ClassNotFoundException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.PATENTS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.PATENTS);
            if (dbList != null) {
                HashSet<DrugPatent> dpSet = new HashSet<>();
                for (Object obj : dbList) {
                    String patentNo = getValue((BasicDBObject)obj, DrugBankFields.NUMBER);
                    if (patentNo == null) {
                        continue;
                    }
                    Object bioEntity = Subgraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_PATENT, BioFields.NAME, patentNo);
                    DrugPatent drugPatent = (DrugPatent)bioEntity;
                    if (drugPatent == null) {
                        drugPatent = new DrugPatent();
                        drugPatent.setPatentNumber(patentNo);
                    }
                    if (drugPatent.getPatentCountry() == null) {
                        drugPatent.setPatentCountry(getValue((BasicDBObject)obj, DrugBankFields.COUNTRY));
                    }
                    if (drugPatent.getApprovedDate() == null) {
                        drugPatent.setApprovedDate(getValue((BasicDBObject)obj, DrugBankFields.APPROVED));
                    }
                    if (drugPatent.getExpiryDate() == null) {
                        drugPatent.setExpiryDate(getValue((BasicDBObject)obj, DrugBankFields.EXPIRES));
                    }
                    subGraph.add(drugPatent);
                    dpSet.add(drugPatent);
                }
                drug.setDrugPatent(dpSet);
            }
        }
    }


    /**
     * setDrug-Drug Interactions
     * @param dbObj
     * @param drug
     * @param subGraph
     * @throws Exception
     */
    private static void setDrugInteractions(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws ClassNotFoundException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.DRUG_INTERACTIONS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.DRUG_INTERACTIONS);
            if (dbList != null) {
                HashSet<Drug> dpSet = new HashSet<>();
                for (Object obj : dbList) {
                    String drugName= getValue((BasicDBObject)obj, DrugBankFields.NAME);
                    if (drugName == null) {
                        continue;
                    }
                    Object bioEntity = Subgraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG, BioFields.NAME, drugName);
                    Drug drugInteraction = (Drug)bioEntity;
                    if (drugInteraction == null) {
                        drugInteraction = new Drug();
                        drugInteraction.setDrugName(drugName);
                    }

                    if (drugInteraction.getDrugbankId() == null) {
                        drugInteraction.setDrugbankId(getValue((BasicDBObject)obj, DrugBankFields.DRUGBANK_ID));
                    }
                    if (drugInteraction.getDrugDescription() == null) {
                        drugInteraction.setDrugDescription(getValue((BasicDBObject)obj, DrugBankFields.DESCRIPTION));
                    }
                    subGraph.add(drugInteraction);
                    dpSet.add(drugInteraction);
                }
                drug.setDrugInteraction(dpSet);
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param drug
     */
    private static void setFoodInteractions(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.FOOD_INTERACTIONS)) {
            List<String> foodInteractions = getList(dbObj, DrugBankFields.FOOD_INTERACTIONS);
            if (foodInteractions != null) {
                drug.setSubstructures(foodInteractions);
            }
        }
    }

    /**
     *
     * @param dbObj
     * @param drug
     * @param subGraph
     * @throws Exception
     */
    private static void setProteinRelation(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws ClassNotFoundException, URISyntaxException, InstantiationException, IllegalAccessException, NoSuchFieldException, InvocationTargetException, ServiceException, HttpException, InterruptedException, IOException {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.EXTERNAL_IDENTIFIERS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.EXTERNAL_IDENTIFIERS);
            if (dbList != null) {
                for (Object obj : dbList) {
                    log.info("identifiers=" + obj);
                    String resource = getValue((BasicDBObject) obj, DrugBankFields.RESOURCE);
                    if (resource != null) {
                        if (resource.equals(DrugBankFields.UNIPROT_KB.toString())) {
                            String uniprotId = getValue((BasicDBObject) obj, DrugBankFields.IDENTIFIER);
                            log.info("uniprotId" + uniprotId);
                            Object bioEntity = Subgraph.getBioEntityFromBioType(subGraph, BioTypes.PROTEIN, BioFields.UNIPROT_ID, uniprotId);
                            Protein protein = (Protein) bioEntity;
                            if (protein == null) {
                                //protein = new Protein();
                                protein = UniprotUtil.getProtein(uniprotId, subGraph);
                                //protein.setUniprot(uniprotId);
                            }
                            if (protein != null) {
                                drug.setProteinRelation(protein);
                            }
                        }
                    }
                }
            }
        }
    }


}



