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
 "kind" : "Molecular Formula",
 "value" : "C29H26ClFN4O4S",
 "source" : "ChemAxon"
 },

 *
 */
@BioEntity (bioType = BioTypes.MOLECULAR_FORMULA)
public class MolecularFormula {
    
    protected static Logger log = LogManager.getLogger(MolecularFormula.class);

    @GraphId
    private Long id;

   /**
    * drugName
    */
    @Visual
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;
    
    @Indexed(indexName=IndexNames.MOLECULAR_FORMULA)
    @Taxonomy (rbClass=TaxonomyTypes.MOLECULAR_FORMULA, rbField=BioFields.MOLECULAR_FORMULA)
    private float molecularFormula;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.MOLECULAR_FORMULA.toString();

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
     *
     * @return String 
     */
    public float getMolecularFormula() {
        return molecularFormula;
    }

    /**
     * molecularFormula fo the drug
     * 
     * @param molecularFormula
     */
    public void setMolecularFormula(float molecularFormula) {
        this.molecularFormula = molecularFormula;
    }
    
}