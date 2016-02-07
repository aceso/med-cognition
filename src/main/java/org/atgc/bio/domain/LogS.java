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
 {
 "kind" : "logS",
 "value" : "-4.4",
 "source" : "ALOGPS"
 },

 *
 */
@UniqueCompoundIndex(indexName=IndexNames.LOG_S, field1=BioFields.DRUG_NAME, field2=BioFields.SOURCE, field3=BioFields.NONE)
@BioEntity (bioType = BioTypes.LOG_S)
public class LogS {
    
    protected static Logger log = LogManager.getLogger(LogS.class);

    @GraphId
    private Long id;

   /**
    * drugName
    */
    @PartKey
    @Visual
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;
    
    @Indexed(indexName=IndexNames.LOG_S_VALUE)
    @Taxonomy (rbClass=TaxonomyTypes.LOG_S_VALUE, rbField=BioFields.LOG_S)
    private float logS;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.LOG_S.toString();


    @PartKey
    @Visual
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