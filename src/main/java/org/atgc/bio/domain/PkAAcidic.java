/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.BioFields;
import org.atgc.bio.meta.*;

/**
 *
 "kind" : "pKa (strongest acidic)",
 "value" : "15.99",
 "source" : "ChemAxon"
 *
 */
@BioEntity (bioType = BioTypes.PKA_ACIDIC)
public class PkAAcidic {
    
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

   /**
    * drugName
    */
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;
    
    @Indexed(indexName=IndexNames.PKA_ACIDIC)
    @Taxonomy (rbClass=TaxonomyTypes.PKA_ACIDIC, rbField=BioFields.PKA_ACIDIC)
    private float pkaAcidic;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PKA_ACIDIC.toString();

    @Indexed(indexName=IndexNames.SOURCE)
    @Taxonomy (rbClass=TaxonomyTypes.SOURCE, rbField=BioFields.SOURCE)
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
     *
     * @return float
     */
    public float getPkaAcidic() {
        return pkaAcidic;
    }

    /**
     * pKa acidic value of drug
     * 
     * @param value
     */
    public void setPkaAcidic(float value) {
        this.pkaAcidic = value;
    }
    
}