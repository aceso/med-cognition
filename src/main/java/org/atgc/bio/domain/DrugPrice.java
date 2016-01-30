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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * <pre>
 * "prices" : [
		{
			"description" : "Permethrin technical liquid",
			"cost" : {
				"@currency" : "USD",
				"#text" : "1.32"
			},
			"unit" : "g"
		},
		{
			"description" : "Elimite 5% cream",
			"cost" : {
				"@currency" : "USD",
				"#text" : "1.33"
			},
			"unit" : "g"
		},
		{
			"description" : "Permethrin 1% Lotion 59ml Bottle",
			"cost" : {
				"@currency" : "USD",
				"#text" : "16.99"
			},
			"unit" : "bottle"
		},
		{
			"description" : "Acticin 5% Cream 60 gm Tube",
			"cost" : {
				"@currency" : "USD",
				"#text" : "30.31"
			},
			"unit" : "tube"
		},
		{
			"description" : "Permethrin 5% Cream 60 gm Tube",
			"cost" : {
				"@currency" : "USD",
				"#text" : "30.42"
			},
			"unit" : "tube"
		},
		{
			"description" : "Elimite 5% Cream 60 gm Tube",
			"cost" : {
				"@currency" : "USD",
				"#text" : "83.61"
			},
			"unit" : "tube"
		},
		{
			"description" : "CVS Pharmacy lice bedding spray",
			"cost" : {
				"@currency" : "USD",
				"#text" : "0.03"
			},
			"unit" : "g"
		},
		{
			"description" : "Sm lice bedding spray",
			"cost" : {
				"@currency" : "USD",
				"#text" : "0.04"
			},
			"unit" : "g"
		},
		{
			"description" : "Nix 1% creme rinse liquid",
			"cost" : {
				"@currency" : "USD",
				"#text" : "0.15"
			},
			"unit" : "ml"
		},
		{
			"description" : "Sm lice treatment permethrin",
			"cost" : {
				"@currency" : "USD",
				"#text" : "0.18"
			},
			"unit" : "ml"
		},
		{
			"description" : "Acticin 5% cream",
			"cost" : {
				"@currency" : "USD",
				"#text" : "0.49"
			},
			"unit" : "g"
		},
		{
			"description" : "Permethrin 5% cream",
			"cost" : {
				"@currency" : "USD",
				"#text" : "0.49"
			},
			"unit" : "g"
		}
	],

 * </pre>

 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.DRUG_PRICE, field1=BioFields.CHEMICAL_ABSTRACT_ID, field2=BioFields.DRUG_NAME, field3=BioFields.DESCRIPTION)
@BioEntity (bioType = BioTypes.DRUG_PRICE)
public class DrugPrice {

	protected static Log log = LogFactory.getLog(new Object().getClass());

	@GraphId
	private Long id;

	/**
	 * cas identifier. Eg. "120993-53-5"
	 */
	@PartKey
	@Visual
	@Taxonomy (rbClass=TaxonomyTypes.CHEMICAL_ABSTRACT_ID, rbField=BioFields.CHEMICAL_ABSTRACT_ID)
	private String chemicalAbstractId;

	@PartKey
	@Visual
	@Taxonomy(rbClass = TaxonomyTypes.DRUG_NAME, rbField = BioFields.DRUG_NAME)
	private String drugName;

	@Indexed(indexName = IndexNames.NODE_TYPE)
	@Taxonomy(rbClass = TaxonomyTypes.NODE_TYPE, rbField = BioFields.NODE_TYPE)
	private String nodeType = BioTypes.DRUG_PRICE.toString();

	/**
	 * description of the priced drug
	 */
	@PartKey
	@Taxonomy(rbClass = TaxonomyTypes.DRUG_PRICE_DESCRIPTION, rbField = BioFields.DESCRIPTION)
	private String description;

	@NonIndexed
	private String cost;

	/**
	 * Get the cost of the drug.
	 *
	 * @return String
	 */
	public String getCost() {
		return cost;
	}

	/**
	 * Set the cost of the drug.
	 *
	 * @param cost
	 */
	public void setCost(String cost) {
		this.cost = cost;
	}

	/**
	 * Get the currency.
	 *
	 * @return String
	 */
	public String getCurrency() {
		return currency;
	}

	/**
	 * Set the currency.
	 *
	 * @param currency
	 */
	public void setCurrency(String currency) {
		this.currency = currency;
	}

	@NonIndexed
	private String currency;

	@NodeLabel
	@Indexed(indexName = IndexNames.DRUG_MANUFACTURER_MESSAGE)
	@Taxonomy(rbClass = TaxonomyTypes.DRUG_MANUFACTURER_MESSAGE, rbField = BioFields.MESSAGE)
	private String message;


	/**
	 * Description of the drug pricing.
	 *
	 * @return String
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Description of the drug pricing.
	 *
	 * @param description
	 */
	public void setDescription(String description) {
		this.description = description;
	}

	/* <pre>
                db.drugbank.distinct("prices.price.unit")
                [
                "vial",
                "ml",
                "g",
                "kit",
                "each",
                "capsule",
                "tablet",
                "ampul",
                "inhaler"
                ]
                </pre>
        */
	@Indexed(indexName = IndexNames.UNIT)
	@Taxonomy(rbClass = TaxonomyTypes.NODE_TYPE, rbField = BioFields.UNIT)
	private String unit;

	/**
	 * get Unit of drug
	 */
	public String getUnit() {
		return unit;
	}

	/**
	 * Unit of drug
	 *
	 * @param unit
	 */
	public void setUnit(String unit) {
		this.unit = unit;
	}

	/*
     * setDrugName
     * @param drugName
     */
	public void setDrugName(String drugName) {
		this.drugName = drugName;
	}

	/**
	 * get getDrugName
	 **/
	public String getDrugName() {
		return drugName;
	}

	public String getChemicalAbstractId() { return chemicalAbstractId;};

	public void setChemicalAbstractId(String casId) {
		this.chemicalAbstractId = casId;
	}

}