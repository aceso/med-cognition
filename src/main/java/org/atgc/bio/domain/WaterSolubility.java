/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.BioFields;
import org.atgc.bio.meta.*;

/**
 *
 "kind" : "Water Solubility",
 "value" : "2.23e-02 g/l",
 "source" : "ALOGPS"
 *
 */
@BioEntity (bioType = BioTypes.WATER_SOLUBILITY)
public class WaterSolubility {
    
    protected static Logger log = LogManager.getLogger(WaterSolubility.class);

    @GraphId
    private Long id;

   /**
    * drugName
    */
    @Visual
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;
    
    @Indexed(indexName=IndexNames.WATER_SOLUBILITY)
    @Taxonomy (rbClass=TaxonomyTypes.WATER_SOLUBILITY, rbField=BioFields.WATER_SOLUBILITY)
    private float waterSolubility;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.WATER_SOLUBILITY.toString();

    @NonIndexed
    private String source;

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * Name of the drug
     * 
     * @return String 
     */
    public String getDrugName() {
        return drugName;
    }

    /**
     * set name of the drug
     * 
     * @param drugName
     */
    public void setDrugName(String drugName) {
        this.drugName = drugName;
    }

    /**
     * get Water Solubility
     * @return String 
     */
    public float getWaterSolubility() {
        return waterSolubility;
    }

    /**
     * waterSolubility fo the drug
     * 
     * @param waterSolubility
     */
    public void setWaterSolubility(float waterSolubility) {
        this.waterSolubility = waterSolubility;
    }
    
}