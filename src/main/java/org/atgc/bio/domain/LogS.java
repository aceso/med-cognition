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
 "kind" : "logS",
 "value" : "-4.4",
 "source" : "ALOGPS"
 },

 *
 */
@BioEntity (bioType = BioTypes.LOG_S)
public class LogS {
    
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
    
    @Indexed(indexName=IndexNames.LOG_S)
    @Taxonomy (rbClass=TaxonomyTypes.LOG_S, rbField=BioFields.LOG_S)
    private float logS;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.LOG_S.toString();

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
    public float getLogS() {
        return logS;
    }

    /**
     * logS fo the drug
     * 
     * @param logS
     */
    public void setLogS(float logS) {
        this.logS = logS;
    }
    
}