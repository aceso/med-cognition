/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
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

import java.util.*;


/**
 *
 * uses Compound bio entity
 *
 */
public class DrugBankImportUtil {

    private static final Logger log = LogManager.getLogger(DrugBankImportUtil.class);

    /**
     * This method uses compound collection and adds genesymbol, importstatus
     * and date to Druglistcollection importstatus - due is by default.
     *
     * @@throws UnknownHostException
     */

    public static void main(String[] args) throws Exception {

        DBCursor dbCursor = DrugBankSplitter.getCollection(ImportCollectionNames.DRUGBANK).findDBCursor("{}");
        try {
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
        } finally {
            dbCursor.close();
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
    }


    private static void setDrug(BasicDBObject obj, String drugName) throws Exception {
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
            drug.setToxicity(getList(obj, DrugBankFields.TOXICITY).toString());
        }
        if (drug.getBiotransformation() == null) {
            drug.setBiotransformation(getList(obj, DrugBankFields.BIO_TRANSFORMATION).toString());
        }
        if (drug.getAbsorption() == null) {
            drug.setAbsorption(getList(obj, DrugBankFields.ABSORPTION).toString());
        }
        if (drug.getHalfLife() == null) {
            drug.setHalfLife(getList(obj, DrugBankFields.HALF_LIFE).toString());
        }
        if (drug.getProteinBinding() == null) {
            drug.setProteinBinding(getList(obj, DrugBankFields.PROTEIN_BINDING).toString());
        }
        if (drug.getRouteOfElimination() == null) {
            drug.setRouteOfElimination(getList(obj, DrugBankFields.ROUTE_OF_ELIMINATION).toString());
        }
        if (drug.getVolumeOfDistribution() == null) {
            drug.setVolumeOfDistribution(getList(obj, DrugBankFields.VOLUME_OF_DISTRIBUTION).toString());
        }
        if (drug.getClearance() == null) {
            drug.setClearance(getList(obj, DrugBankFields.CLEARANCE).toString());
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

    private static String getCasId(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.CAS_NUMBER)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.CAS_NUMBER);
        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.CAS_NUMBER).toString();
    }

    private static String getDrugDescription(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.DESCRIPTION)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.DESCRIPTION);
        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.DESCRIPTION).toString();
    }

    private static String getDrugBankId(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.DRUGBANK_ID);
    }

    private static String getDateCreated(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.CREATED);
    }

    private static String getDateUpdated(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.UPDATED);
    }

    private static String getVersion(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.VERSION);
    }

    private static String getIndication(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.INDICATION)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.INDICATION);

        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.INDICATION).toString();
    }

    private static String getMechanismOfAction(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.MECHANISM_OF_ACTION)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.MECHANISM_OF_ACTION);
        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.MECHANISM_OF_ACTION).toString();
    }

    private static String getPharmcology(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.PHARMACOLOGY)) {
            return OntologyStrUtil.getString(dbObj, DrugBankFields.PHARMACOLOGY);
        }
        return OntologyStrUtil.getList(dbObj, DrugBankFields.PHARMACOLOGY).toString();
    }

    private static String getValue(BasicDBObject dbObj, DrugBankFields key) {
        return OntologyStrUtil.getString(dbObj, key);
    }

    private static void addAccessionNumbers(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.SECONDARY_ACCESSION_NUMBERS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.SECONDARY_ACCESSION_NUMBERS);
            for (Object obj : dbList) {
                drug.addSecondaryAccessionNumber(obj.toString());
            }
        }
    }

    private static void addStatusGroups(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.GROUPS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.GROUPS);
            for (Object obj : dbList) {
                drug.addDrugStatusGroup(obj.toString());
            }
        }
    }

    private static void addSynonyms(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.SYNONYMS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.SYNONYMS);
            if (dbList != null) {
                List<String> synList = new ArrayList<String>();
                for (Object obj : dbList) {
                    synList.add(obj.toString());
                }
                drug.setSynonyms(synList);
            }
        }
    }

    private static List<String> getList(BasicDBObject dbObj, DrugBankFields name) {
        if (OntologyStrUtil.isObjectNull(dbObj, name)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, name);
            if (dbList != null) {
                List<String> list = new ArrayList<String>();
                for (Object obj : dbList) {
                    list.add(obj.toString());
                }
                return list;
            }
        }
        return null;
    }

    private static void setSubstructures(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.SUB_STRUCTURES)) {
            List<String> subStructures = getList(dbObj, DrugBankFields.SUB_STRUCTURES);
            if (subStructures != null) {
                drug.setSubstructures(subStructures);
            }
        }
    }

    private static String getIngredients(BasicDBObject dbObj) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.INGREDIENTS)) {
            if (OntologyStrUtil.isObjectString(dbObj, DrugBankFields.INGREDIENTS)) {
                return OntologyStrUtil.getString(dbObj, DrugBankFields.INGREDIENTS);
            }
            return OntologyStrUtil.getList(dbObj, DrugBankFields.INGREDIENTS).toString();
        }
        return null;
    }

    private static String getKingdom(BasicDBObject dbObj) {
        return getList(dbObj, DrugBankFields.KINGDOM).toString();
    }


    private static void setMixtures(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.MIXTURES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.MIXTURES);
            if (dbList != null) {
                Map<String, String> map = new HashMap<String, String>();
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

    private static void setDrugPackagers(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.PACKAGERS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.PACKAGERS);
            if (dbList != null) {
                HashSet<DrugPackager> dpSet = new HashSet<DrugPackager>();
                for (Object packageObj : dbList) {
                    log.info("packagers " + packageObj);
                    BasicDBObject obj = (BasicDBObject)packageObj;
                    if (OntologyStrUtil.isObjectNull((BasicDBObject) obj, DrugBankFields.PACKAGER)) {
                        obj = OntologyStrUtil.getDBObject((BasicDBObject) obj, DrugBankFields.PACKAGER);
                    }
                    String packagerName = getValue(obj, DrugBankFields.NAME);
                    if (packagerName == null) {
                        continue;
                    }
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_PACKAGER, BioFields.NAME, packagerName);
                    DrugPackager drugPackager = (DrugPackager) bioEntity;
                    if (drugPackager == null) {
                        drugPackager = new DrugPackager();
                        drugPackager.setName(packagerName);
                    }
                    if (drugPackager.getUrl() == null) {
                        String url = getList(obj, DrugBankFields.URL).toString();
                        drugPackager.setUrl(url);
                    }
                    if (drugPackager != null) {
                        subGraph.add(drugPackager);
                        dpSet.add(drugPackager);
                    }
                }
                drug.setDrugPackagers(dpSet);
            }
        }
    }

    private static void setDrugManufacturers(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.MANUFACTURERS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.MANUFACTURERS);
            if (dbList != null) {
                HashSet<DrugManufacturer> dmSet = new HashSet<DrugManufacturer>();
                for (Object mfrObj : dbList) {
                    BasicDBObject obj = (BasicDBObject)mfrObj;
                    if (OntologyStrUtil.isObjectNull(obj, DrugBankFields.MANUFACTURER)) {
                        obj = OntologyStrUtil.getDBObject(obj, DrugBankFields.MANUFACTURER);
                    }
                    String manufacturerName = getValue(obj, DrugBankFields.TEXT);
                    if (manufacturerName == null) {
                        continue;
                    }
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_MANUFACTURER, BioFields.NAME, manufacturerName);
                    DrugManufacturer drugManufacturer = (DrugManufacturer) bioEntity;
                    if (drugManufacturer == null) {
                        drugManufacturer = new DrugManufacturer();
                        drugManufacturer.setName(manufacturerName);

                    }
                    if (drugManufacturer.getGeneric() == null) {
                        drugManufacturer.setGeneric(getValue(obj, DrugBankFields.GENERIC));
                    }
                    if (drugManufacturer != null) {
                        subGraph.add(drugManufacturer);
                        dmSet.add(drugManufacturer);
                    }
                }
                drug.setDrugManufacturers(dmSet);
            }
        }
    }

    private static void setDrugPrices(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        if (drug.getCasId() == null) {
            return;
        }
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.PRICES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.PRICES);
            if (dbList != null) {
                HashSet<DrugPrice> dmSet = new HashSet<DrugPrice>();
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
                    DrugPrice drugPrice = new DrugPrice();
                    drugPrice = new DrugPrice();
                    drugPrice.setChemicalAbstractId(drug.getCasId());
                    drugPrice.setDescription(description);
                    CompoundKey compoundKey = CompoundKey.getCompoundKey(drugPrice);
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_PRICE, BioFields.DRUG_PRICE, compoundKey.getValue());
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
                    if (drugPrice != null) {
                        subGraph.add(drugPrice);
                        dmSet.add(drugPrice);
                    }
                }
                drug.setDrugPrices(dmSet);
            }
        }
    }

    private static void addCategories(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.CATEGORIES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.CATEGORIES);
            for (Object obj : dbList) {
                drug.addCategory(obj.toString());
            }
        }
    }

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

    private static void setDosages(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        String casId = drug.getCasId();
        if (casId == null) {
            return;
        }
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.DOSAGES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.DOSAGES);
            if (dbList != null) {
                HashSet<Dosage> dosageSet = new HashSet<Dosage>();
                for (Object obj : dbList) {
                    log.info("dosages object =" + (BasicDBObject)obj);
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
                    Dosage bioEntity = null;
                    if (compoundKey != null) {
                        bioEntity = (Dosage) subGraph.getBioEntityFromBioType(subGraph, BioTypes.DOSAGE, BioFields.DOSAGE, compoundKey.getValue());
                        if (bioEntity != null) {
                            dosage = bioEntity;
                        }
                    }

                    if (dosage.getStrength() == null) {
                        dosage.setStrength(getList(dosageObj, DrugBankFields.STRENGTH).toString());
                    }
                    if (dosage != null) {
                        subGraph.add(dosage);
                        dosageSet.add(dosage);
                    }
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
     * @throws Exception
     */
    private static void setDrugPatents(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.PATENTS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.PATENTS);
            if (dbList != null) {
                HashSet<DrugPatent> dpSet = new HashSet<DrugPatent>();
                for (Object obj : dbList) {
                    String patentNo = getValue((BasicDBObject)obj, DrugBankFields.NUMBER);
                    if (patentNo == null) {
                        continue;
                    }
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_PATENT, BioFields.NAME, patentNo);
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
                    if (drugPatent != null) {
                        subGraph.add(drugPatent);
                        dpSet.add(drugPatent);
                    }
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
    private static void setDrugInteractions(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.DRUG_INTERACTIONS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.DRUG_INTERACTIONS);
            if (dbList != null) {
                HashSet<Drug> dpSet = new HashSet<Drug>();
                for (Object obj : dbList) {
                    String drugName= getValue((BasicDBObject)obj, DrugBankFields.NAME);
                    if (drugName == null) {
                        continue;
                    }
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG, BioFields.NAME, drugName);
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
                    if (drugInteraction != null) {
                        subGraph.add(drugInteraction);
                        dpSet.add(drugInteraction);
                    }
                }
                drug.setDrugInteraction(dpSet);
            }
        }
    }

    private static void setFoodInteractions(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.FOOD_INTERACTIONS)) {
            List<String> foodInteractions = getList(dbObj, DrugBankFields.FOOD_INTERACTIONS);
            if (foodInteractions != null) {
                drug.setSubstructures(foodInteractions);
            }
        }
    }

    private static void setProteinRelation(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
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
                            Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.PROTEIN, BioFields.UNIPROT_ID, uniprotId);
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



