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
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * "packagers" : [
		{
			"name" : "Bayer Healthcare",
			"url" : "http://www.bayerhealthcare.com"
		},
		{
			"name" : "Berlex Labs",
			"url" : "http://www.berlex.com"
		}
	],

 * @author jtanisha-ee
 */
@BioEntity (bioType = BioTypes.DRUG_PACKAGER)
public class DrugPackager {
    
    protected static Logger log = LogManager.getLogger(DrugPackager.class);

    @GraphId
    private Long id;

   /**
    * name of the packager
    */
    @Visual
    @UniquelyIndexed(indexName=IndexNames.DRUG_PACKAGER_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_PACKAGER_NAME, rbField=BioFields.NAME)
    private String name;
    
    @Indexed(indexName=IndexNames.DRUG_PACKAGER_URL)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_PACKAGER_URL, rbField=BioFields.URL)
    private String url;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.DRUG_PACKAGER.toString();
    
    @NodeLabel
    @Indexed (indexName=IndexNames.DRUG_PACKAGER_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_PACKAGER_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

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

    /**
     * Url of the drug packager.
     * 
     * @return String 
     */
    public String getUrl() {
        return url;
    }

    /**
     * Url fo the drug packager.
     * 
     * @param url 
     */
    public void setUrl(String url) {
        this.url = url;
    }
    
}