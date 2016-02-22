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
 *
 "kind" : "Refractivity",
 "value" : "152.42",
 "source" : "ChemAxon
 *
 */
@BioEntity (bioType = BioTypes.REFRACTIVITY)
@SuppressWarnings("javadoc")
public class Refractivity {
    
    protected static Logger log = LogManager.getLogger(Refractivity.class);

    @GraphId
    private Long id;

   /**
    * drugName
    */
    @Visual
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;
    
    @Indexed(indexName=IndexNames.REFRACTIVITY)
    @Taxonomy (rbClass=TaxonomyTypes.REFRACTIVITY, rbField=BioFields.REFRACTIVITY)
    private float refractivity;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.REFRACTIVITY.toString();

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
     * get refractvity
     * @return String 
     */
    public float getRefractivity() {
        return refractivity;
    }

    /**
     * refractivity the drug
     * 
     * @param refractivity
     */
    public void setRefractivity(float refractivity) {
        this.refractivity = refractivity;
    }
    
}