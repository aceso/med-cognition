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
@UniqueCompoundIndex(indexName=IndexNames.LOG_P, field1=BioFields.DRUG_NAME, field2=BioFields.SOURCE, field3=BioFields.NONE)
@BioEntity (bioType = BioTypes.LOG_P)
public class LogP {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /**
     * drugName
     */
    @PartKey
    @Visual
    @Indexed(indexName = IndexNames.DRUG_NAME)
    @Taxonomy(rbClass = TaxonomyTypes.DRUG_NAME, rbField = BioFields.DRUG_NAME)
    private String drugName;

    @Indexed(indexName = IndexNames.LOG_P_VALUE)
    @Taxonomy(rbClass = TaxonomyTypes.LOG_P_VALUE, rbField = BioFields.LOG_P)
    private float logP;

    @PartKey
    @Visual
    @Indexed(indexName = IndexNames.SOURCE)
    @Taxonomy(rbClass = TaxonomyTypes.SOURCE, rbField = BioFields.SOURCE)
    private String source;


    @Indexed(indexName = IndexNames.NODE_TYPE)
    @Taxonomy(rbClass = TaxonomyTypes.NODE_TYPE, rbField = BioFields.NODE_TYPE)
    private String nodeType = BioTypes.LOG_P.toString();


    @NodeLabel
    @Indexed(indexName = IndexNames.MESSAGE)
    @Taxonomy(rbClass = TaxonomyTypes.MESSAGE, rbField = BioFields.MESSAGE)
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
    public float getLogP() {
        return logP;
    }

    /**
     * logP fo the drug
     *
     * @param logP
     */
    public void setLogP(float logP) {
        this.logP = logP;
    }

    /**
     * source (ChemAxon, ALOGPS)
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     * source
     */
    public String getSource() {
        return this.source;
    }
}