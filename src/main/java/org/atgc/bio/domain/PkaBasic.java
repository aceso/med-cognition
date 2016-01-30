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
 {
 "kind" : "pKa (strongest basic)",
 "value" : "7.2",
 "source" : "ChemAxon"
 },
 *
 */
@BioEntity (bioType = BioTypes.PKA_BASIC)
public class PkaBasic {
    
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

   /**
    * drugName
    */
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;
    
    @Indexed(indexName=IndexNames.PKA_BASIC)
    @Taxonomy (rbClass=TaxonomyTypes.PKA_BASIC, rbField=BioFields.PKA_BASIC)
    private float pkaBasic;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PKA_BASIC.toString();

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
    public float getPkaBasic() {
        return pkaBasic;
    }

    /**
     * pKa Basic value of drug
     * 
     * @param value
     */
    public void setPkaBasic(float value) {
        this.pkaBasic = value;
    }
    
}