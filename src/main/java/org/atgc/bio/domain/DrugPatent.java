/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.NonIndexed;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * <pre>
   {
	"_id" : ObjectId("52956554300417c0cfbb104a"),
        "drugbank-id" : "DB00019",
	"patents" : [
		{
			"number" : "1341537",
			"country" : "Canada",
			"approved" : "2007-07-31",
			"expires" : "2024-07-31"
		},
		{
			"number" : "1339071",
			"country" : "Canada",
			"approved" : "1997-07-29",
			"expires" : "2014-07-29"
		}
	]
}


 * </pre>

 * @author jtanisha-ee
 */
@BioEntity (bioType = BioTypes.DRUG_PATENT)
public class DrugPatent {
    
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;
    
    /**
    * cas identifier. Eg. "120993-53-5"
    */
    @UniquelyIndexed (indexName=IndexNames.DRUG_PATENT_NUMBER)
    @Visual
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_PATENT_NUMBER, rbField=BioFields.PATENT_NUMBER)
    private String patentNumber;

   /**
    * drug patent country
    */
    @Indexed(indexName=IndexNames.DRUG_PATENT_COUNTRY)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_PATENT_COUNTRY, rbField=BioFields.PATENT_COUNTRY)
    private String patentCountry;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.DRUG_PATENT.toString();

    /**
     * Get the patent number.
     * 
     * @return String 
     */
    public String getPatentNumber() {
        return patentNumber;
    }

    /**
     * Set the patent number.
     * 
     * @param patentNumber 
     */
    public void setPatentNumber(String patentNumber) {
        this.patentNumber = patentNumber;
    }

    /**
     * Get the patent country.
     * 
     * @return String 
     */
    public String getPatentCountry() {
        return patentCountry;
    }

    /**
     * Set the patent country.
     * 
     * @param patentCountry 
     */
    public void setPatentCountry(String patentCountry) {
        this.patentCountry = patentCountry;
    }
    
    @NonIndexed
    private String approvedDate;

    /**
     * Get the approved date.
     * 
     * @return String 
     */
    public String getApprovedDate() {
        return approvedDate;
    }

    /**
     * Set the cost of the drug.
     * 
     * @param cost 
     */
    public void setApprovedDate(String approvedDate) {
        this.approvedDate = approvedDate;
    }

    @NonIndexed
    private String expiryDate;
    
    /**
     * Get the expiryDate.
     * 
     * @return String 
     */
    public String getExpiryDate() {
        return expiryDate;
    }

    /**
     * Set the expiryDate.
     * 
     * @param expiryDate 
     */
    public void setExpiryDate(String expiryDate) {
        this.expiryDate = expiryDate;
    }
    
    @NodeLabel
    @Indexed (indexName=IndexNames.DRUG_PATENT_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_PATENT_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;
}