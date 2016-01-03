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
import org.atgc.bio.repository.Subgraph;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.ImportCollectionNames;

import java.net.URISyntaxException;
import java.util.*;


/**
 *
 * uses Compound bio entity
 *
 */
public class DrugBank {

    protected static Log log = LogFactory.getLog(DrugBank.class);

    /**
     * This method uses compound collection and adds genesymbol, importstatus
     * and date to Druglistcollection importstatus - due is by default.
     *
     * @@throws UnknownHostException
     */

    public static void main(String[] args) throws Exception {

        DBCursor dbCursor = DrugBankSplitter.getCollection(ImportCollectionNames.DRUGBANK).findDBCursor("{}" );
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String drugName = DrugBankSplitter.getDrugName(result);
                if (!StatusUtil.idExists(DrugBankFields.DRUG_BANK.toString(), DrugBankFields.NAME.toString(), drugName)) {
                    log.info("******* drugName =" + drugName);
                }
                setDrug(result, drugName);
            }
        } finally {
            dbCursor.close();
        }
    }


    private static void setDrug(BasicDBObject obj, String drugName) throws Exception {
        Subgraph subGraph = new Subgraph();
        Object bio = NCICompound.getBioEntityFromBioType(subGraph, BioTypes.DRUG, BioFields.DRUG_NAME, drugName);
        Drug drug = (Drug)bio;
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
            drug.setToxicity(getValue(obj, DrugBankFields.TOXICITY));
        }
        if (drug.getBiotransformation() == null) {
            drug.setBiotransformation(getValue(obj, DrugBankFields.BIO_TRANSFORMATION));
        }
        if (drug.getAbsorption() == null) {
            drug.setAbsorption(getValue(obj, DrugBankFields.ABSORPTION));
        }
        if (drug.getHalfLife() == null) {
            drug.setHalfLife(getValue(obj, DrugBankFields.HALF_LIFE));
        }
        if (drug.getProteinBinding() == null) {
            drug.setProteinBinding(getValue(obj, DrugBankFields.PROTEIN_BINDING));
        }
        if (drug.getRouteOfElimination() == null) {
            drug.setRouteOfElimination(getValue(obj, DrugBankFields.ROUTE_OF_ELIMINATION));
        }
        if (drug.getVolumeOfDistribution() == null) {
            drug.setVolumeOfDistribution(getValue(obj, DrugBankFields.VOLUME_OF_DISTRIBUTION));
        }
        if (drug.getClearance() == null) {
            drug.setClearance(getValue(obj, DrugBankFields.CLEARANCE));
        }

        //secondary accession numbers
        if (drug.getSecondaryAccessionNumbers() == null) {
            addAccessionNumbers(obj, drug);
        }
        if (drug.getDrugStatusGroups() == null) {
            addStatusGroups(obj, drug);
        }

        // add pubmed generic references
            //addDrugSubStructures();
            // verify Tanisha

        //addSalts();
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
        subGraph.add(drug);

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
        return OntologyStrUtil.getString(dbObj, DrugBankFields.CAS_NUMBER);
    }

    private static String getDrugDescription(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.DESCRIPTION);
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
        return OntologyStrUtil.getString(dbObj, DrugBankFields.INDICATION);
    }

    private static String getMechanismOfAction(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.MECHANISM_OF_ACTION);
    }

    private static String getPharmcology(BasicDBObject dbObj) {
        return OntologyStrUtil.getString(dbObj, DrugBankFields.PHARMACOLOGY);
    }

    private static String getValue(BasicDBObject dbObj, DrugBankFields key ) {
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


    private static void setMixtures(BasicDBObject dbObj, Drug drug) {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.MIXTURES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.MIXTURES);
            if (dbList != null) {
                Map<String, String> map = new HashMap<String, String>();
                for (Object obj : dbList) {
                    String key = getValue((BasicDBObject)obj, DrugBankFields.NAME);
                    String value = getValue((BasicDBObject)obj, DrugBankFields.INGREDIENTS);
                    map.put(key, value);
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
                for (Object obj : dbList) {
                    String packagerName = getValue((BasicDBObject)obj, DrugBankFields.NAME);
                    String url = getValue((BasicDBObject)obj, DrugBankFields.URL);
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_PACKAGER, BioFields.NAME, packagerName);
                    DrugPackager drugPackager = (DrugPackager)bioEntity;
                    if (drugPackager != null) {
                        drugPackager = new DrugPackager();
                        drugPackager.setName(packagerName);
                    }
                    if (drugPackager.getUrl() == null) {
                        drugPackager.setUrl(url);
                    }
                    subGraph.add(drugPackager);
                    dpSet.add(drugPackager);
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
                for (Object obj : dbList) {
                    String manufacturerName = getValue((BasicDBObject)obj, DrugBankFields.TEXT);
                    String generic = getValue((BasicDBObject)obj, DrugBankFields.GENERIC);
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_MANUFACTURER, BioFields.NAME, manufacturerName);
                    DrugManufacturer drugManufacturer = (DrugManufacturer)bioEntity;
                    if (drugManufacturer == null) {
                        drugManufacturer = new DrugManufacturer();
                        drugManufacturer.setName(manufacturerName);

                    }
                    if (drugManufacturer.getGeneric() == null) {
                        drugManufacturer.setGeneric(generic);
                    }
                    subGraph.add(drugManufacturer);
                    dmSet.add(drugManufacturer);
                }
                drug.setDrugManufacturers(dmSet);
            }
        }
    }

    private static List setDrugPrices(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.PRICES)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.PRICES);
            if (dbList != null) {
                HashSet<DrugPrice> dmSet = new HashSet<DrugPrice>();
                for (Object obj : dbList) {
                    String description = getValue((BasicDBObject)obj, DrugBankFields.DESCRIPTION);
                    String unit = getValue((BasicDBObject)obj, DrugBankFields.UNIT);
                    String currency = getValue((BasicDBObject)obj, DrugBankFields.CURRENCY);
                    String cost = getValue((BasicDBObject)obj, DrugBankFields.TEXT);
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.DRUG_PRICE, BioFields.DRUG_NAME, drug.getDrugName());
                    DrugPrice drugPrice = (DrugPrice)bioEntity;
                    if (drugPrice == null) {
                        drugPrice = new DrugPrice();
                        drugPrice.setDrugName((drug.getDrugName()));
                    }
                    if (drugPrice.getDescription() == null) {
                        drugPrice.setDescription(description);
                    }
                    if (drugPrice.getCost() == null) {
                        drugPrice.setCost(cost);
                    }
                    subGraph.add(drugPrice);
                    dmSet.add(drugPrice);
                }
                drug.setDrugPrices(dmSet);
            }
        }
        return null;
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
                for (Object obj : dbList) {
                    String organismName = obj.toString();
                    Object bioEntity = subGraph.getBioEntityFromBioType(subGraph, BioTypes.ORGANISM, BioFields.ORGANISM_SHORT_LABEL, organismName);
                    Organism organism = (Organism)bioEntity;
                    if (organism == null) {
                       organism = new Organism();
                       organism.setOrganismShortLabel(organismName);
                    }
                    subGraph.add(organism);
                    drug.setOrganismComponents(organism);
                }
            }
        }
    }



}



