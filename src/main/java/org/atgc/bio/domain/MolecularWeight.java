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
 * {
 "kind" : "Molecular Weight",
 "value" : "581.058",
 "source" : "ChemAxon"
 },

 *
 */
@BioEntity (bioType = BioTypes.MOLECULAR_WEIGHT)
public class MolecularWeight {
    
    protected static Logger log = LogManager.getLogger(MolecularWeight.class);

    @GraphId
    private Long id;

   /**
    * drugName
    */
    @Visual
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;
    
    @Indexed(indexName=IndexNames.MOLECULAR_WEIGHT)
    @Taxonomy (rbClass=TaxonomyTypes.MOLECULAR_WEIGHT, rbField=BioFields.MOLECULAR_WEIGHT)
    private float molecularWeight;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.MOLECULAR_WEIGHT.toString();

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
    public float getMolecularWeight() {
        return molecularWeight;
    }

    /**
     * molecularWeight fo the drug
     * 
     * @param molecularWeight
     */
    public void setMolecularWeight(float molecularWeight) {
        this.molecularWeight = molecularWeight;
    }
    
}