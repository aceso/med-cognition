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
 * <pre>
 {
		"manufacturer" : {
			"@generic" : "false",
			"#text" : "Shire human genetic therapies inc"
		}
	},
   </pre>

 * @author jtanisha-ee
 */
@BioEntity (bioType = BioTypes.DRUG_MANUFACTURER)
public class DrugManufacturer {
    
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

   /**
    * name of the manufacturer
    */
    @Visual
    @UniquelyIndexed(indexName=IndexNames.DRUG_MANUFACTURER_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_MANUFACTURER_NAME, rbField= BioFields.NAME)
    private String name;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.DRUG_MANUFACTURER.toString();
    
    @NodeLabel
    @Indexed (indexName=IndexNames.DRUG_MANUFACTURER_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_MANUFACTURER_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;
    
    @NonIndexed
    private String generic;

    /**
     * Is the drug generic
     * 
     * @return String 
     */
    public String getGeneric() {
        return generic;
    }

    /**
     * Set the generic drug
     * 
     * @param generic 
     */
    public void setGeneric(String generic) {
        this.generic = generic;
    }

    /**
     * Name of the drug packager.
     * 
     * @return String 
     */
    public String getName() {
        return name;
    }

    /**
     * Name of the drug packager.
     * 
     * @param name 
     */
    public void setName(String name) {
        this.name = name;
    }
}