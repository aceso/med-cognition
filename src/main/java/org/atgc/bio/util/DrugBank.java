/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import org.atgc.bio.*;
import org.atgc.bio.BioFields;
import org.atgc.bio.DrugBankFields;
import org.atgc.bio.DrugBankSplitter;
import org.atgc.bio.domain.Drug;
import org.atgc.bio.domain.DrugManufacturer;
import org.atgc.bio.domain.DrugPackager;
import org.atgc.bio.repository.Subgraph;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.domain.BioTypes;

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
            drug.setCasId(getCasId(obj));
            drug.setDrugDescription(getDrugDescription(obj));
            drug.setDrugType(getDrugType(obj));
            drug.setDrugbankId(getDrugBankId(obj));

            //general references - pubmed

            // synthesis references -
            drug.setIndication(getIndication(obj));
            drug.setPharmacology(getPharmcology(obj));
            drug.setMechanismOfAction((getMechanismOfAction(obj)));
            drug.setToxicity(getValue(obj, DrugBankFields.TOXICITY));
            drug.setBiotransformation(getValue(obj, DrugBankFields.BIO_TRANSFORMATION));
            drug.setAbsorption(getValue(obj, DrugBankFields.ABSORPTION));
            drug.setHalfLife(getValue(obj, DrugBankFields.HALF_LIFE));
            drug.setProteinBinding(getValue(obj, DrugBankFields.PROTEIN_BINDING));
            drug.setRouteOfElimination(getValue(obj, DrugBankFields.ROUTE_OF_ELIMINATION));
            drug.setVolumeOfDistribution(getValue(obj, DrugBankFields.VOLUME_OF_DISTRIBUTION));
            drug.setClearance(getValue(obj, DrugBankFields.CLEARANCE));

            //secondary accession numbers
            addAccessionNumbers(obj, drug);
            addStatusGroups(obj, drug);
            //addDrugSubStructures();
            // verify Tanisha
            //addSalts();
            addSynonyms(obj, drug);
            drug.setBrands(getList(obj, drug, DrugBankFields.BRANDS));
            setMixtures(obj, drug);
            setDrugPackagers(obj, drug, subGraph);
            setManufacturers(obj, drug, subGraph);
            subGraph.add(drug);
        }

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

    private static List<String> getList(BasicDBObject dbObj, Drug drug, DrugBankFields name) {
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
                    DrugPackager drugPackager = new DrugPackager();
                    drugPackager.setName(packagerName);
                    drugPackager.setUrl(url);
                    subGraph.add(drugPackager);
                    dpSet.add(drugPackager);
                }
                drug.setDrugPackagers(dpSet);
            }
        }
    }

    private static void setManufacturers(BasicDBObject dbObj, Drug drug, Subgraph subGraph) throws Exception {
        if (OntologyStrUtil.isObjectNull(dbObj, DrugBankFields.PACKAGERS)) {
            BasicDBList dbList = OntologyStrUtil.getBasicDBList(dbObj, DrugBankFields.PACKAGERS);
            if (dbList != null) {
                HashSet<DrugPackager> dpSet = new HashSet<DrugPackager>();
                for (Object obj : dbList) {
                    String packagerName = getValue((BasicDBObject)obj, DrugBankFields.NAME);
                    String url = getValue((BasicDBObject)obj, DrugBankFields.URL);
                    DrugPackager drugPackager = new DrugPackager();
                    drugPackager.setName(packagerName);
                    drugPackager.setUrl(url);
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
                    DrugManufacturer drugManufacturer = new DrugManufacturer();
                    drugManufacturer.setName(manufacturerName);
                    drugManufacturer.setGeneric(generic);
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
                    DrugPrice drugPrice = new DrugPrice();
                    drugPrice.setDrugName((drug.getDrugName()));
                    drugPrice.setDescription(description);
                    drugPrice.setCost(cost);
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

}



}


