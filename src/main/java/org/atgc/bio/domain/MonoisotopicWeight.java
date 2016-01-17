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
 * {
 "kind" : "Molecular Weight",
 "value" : "581.058",
 "source" : "ChemAxon"
 },

 *
 */
@BioEntity (bioType = BioTypes.MONOISOTOPIC_WEIGHT)
public class MonoisotopicWeight {
    
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

   /**
    * drugName
    */
    @Visual
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;
    
    @Indexed(indexName=IndexNames.MONOISOTOPIC_WEIGHT)
    @Taxonomy (rbClass=TaxonomyTypes.MONOISOTOPIC_WEIGHT, rbField=BioFields.MONOISOTOPIC_WEIGHT)
    private float monoisotopicWeight;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.MONOISOTOPIC_WEIGHT.toString();

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
     * Url of the drug packager.
     * 
     * @return String 
     */
    public float getMonoisotopicWeight() {
        return monoisotopicWeight;
    }

    /**
     * monoisotopicWeight fo the drug
     * 
     * @param monoisotopicWeight
     */
    public void setMonoisotopicWeight(float monoisotopicWeight) {
        this.monoisotopicWeight = monoisotopicWeight;
    }
    
}