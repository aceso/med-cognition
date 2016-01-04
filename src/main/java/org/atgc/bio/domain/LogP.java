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
 "kind" : "logP",
 "value" : "5.18",
 "source" : "ALOGPS"
 },

 {
 "kind" : "logP",
 "value" : "4.64",
 "source" : "ChemAxon"
 },

 *
 */
@BioEntity (bioType = BioTypes.LOG_P)
public class LogP {
    
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
    
    @Indexed(indexName=IndexNames.LOG_P)
    @Taxonomy (rbClass=TaxonomyTypes.LOG_P, rbField=BioFields.LOG_P)
    private String logP;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.LOG_P.toString();

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
    public String getLogP() {
        return logP;
    }

    /**
     * logP fo the drug
     * 
     * @param logP
     */
    public void setLogP(String logP) {
        this.logP = logP;
    }
    
}