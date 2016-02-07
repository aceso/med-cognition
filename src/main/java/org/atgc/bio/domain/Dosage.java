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
import org.atgc.bio.meta.PartKey;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniqueCompoundIndex;
import org.atgc.bio.meta.Visual;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * <pre>
 "dosages" : [
		{
			"form" : "Cream",
			"route" : "Topical",
			"strength" : [ ]
		},
		{
			"form" : "Liquid",
			"route" : "Topical",
			"strength" : [ ]
		},
		{
			"form" : "Lotion",
			"route" : "Topical",
			"strength" : [ ]
		}
	],
        	"dosage" : {
			"form" : "Tablet",
			"route" : "Oral",
			"strength" : "20 mg, 30 mg, 40 mg"
		}
   </pre>

 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.DOSAGE_ID, field1=BioFields.CHEMICAL_ABSTRACT_ID, field2=BioFields.FORM, field3=BioFields.NONE)
@BioEntity (bioType = BioTypes.DOSAGE)
public class Dosage {
    
    protected static Logger log = LogManager.getLogger(Dosage.class);

    @GraphId
    private Long id;
    
    /**
	* BioFields have to match with the memberVariable
    * cas identifier. Eg. "120993-53-5"
    */
    @PartKey
    @Visual
    @Taxonomy (rbClass=TaxonomyTypes.CHEMICAL_ABSTRACT_ID, rbField=BioFields.CHEMICAL_ABSTRACT_ID)
    private String chemicalAbstractId;

   /**
    * form of dosage
    */
    @Visual
    @PartKey
	@Taxonomy (rbClass=TaxonomyTypes.FORM, rbField=BioFields.FORM)
	private String form;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.DOSAGE.toString();

    @NonIndexed
	private String route;

    /**
     * Get the Chemical Abstract Id.
     * 
     * @return String 
     */
    public String getChemicalAbstractId() {
        return chemicalAbstractId;
    }

    /**
     * Set the Chemical Abstract Id.
     * 
     * @param casId 
     */
    public void setChemicalAbstractId(String casId) {
		log.info("casid=" + casId);
        this.chemicalAbstractId = casId;
    }

    /**
     * Get the form.
     * 
     * <pre>
     * "form" : "Cream",
     * </pre>
     * 
     * @return String 
     */
    public String getForm() {
        return form;
    }

    /**
     * Set the form.
     * 
     * <pre>
     * "form" : "Cream",
     * </pre>
     * 
     * @param form 
     */
    public void setForm(String form) {
        this.form = form;
    }

    /**
     * Get the route.
     * 
     * <pre>
     * "route" : "Topical",
     * </pre>
     * 
     * @return String 
     */
    public String getRoute() {
        return route;
    }

    /**
     * Set the route.
     * 
     * <pre>
     * "route" : "Topical",
     * </pre>
     * 
     * @param route 
     */
    public void setRoute(String route) {
        this.route = route;
    }

    /**
     * Get the strength.
     * 
     * <pre>
     * 	"dosage" : {
			"form" : "Tablet",
			"route" : "Oral",
			"strength" : "20 mg, 30 mg, 40 mg"
		}
        
        db.drugbank.find({}, {"drugbank-id" : 1, "dosages.strength" : 1})
        
        db.drugbank.find({"drugbank-id" : "DB00035"}, {"dosages.strength" : 1}).pretty()
{
	"_id" : ObjectId("52956557300417c0cfbb105a"),
	"dosages" : [
		{
			"strength" : "15 mcg/ml"
		},
		{
			"strength" : "4 mcg/ml"
		},
		{
			"strength" : "0.1 mg/ml"
		},
		{
			"strength" : "0.1 mg/ml; 10 mcg per spray"
		},
		{
			"strength" : "1.5 mg/ml; 100 mcg per activation"
		},
		{
			"strength" : "0.1 mg"
		},
		{
			"strength" : "0.2 mg"
		},
		{
			"strength" : "120 mcg"
		},
		{
			"strength" : "240 mcg"
		},
		{
			"strength" : "60 mcg"
		}
	]
     }

     * </pre>
     * 
     * @return String 
     */
    public String getStrength() {
        return strength;
    }

    /**
     * Set the strength.
     * 
     * <pre>
     * 	"dosage" : {
			"form" : "Tablet",
			"route" : "Oral",
			"strength" : "20 mg, 30 mg, 40 mg"
		}
     * </pre>
     * 
     * @param strength 
     */
    public void setStrength(String strength) {
        this.strength = strength;
    }
    
    @NodeLabel
    @Indexed (indexName=IndexNames.DOSAGE_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.DOSAGE_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    
    @NonIndexed
    private String strength;
}