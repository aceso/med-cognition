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
import org.atgc.bio.meta.UniquelyIndexed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * These are the calculated properties.
 * 
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.CHEMICAL_PROPERTY, field1= BioFields.CHEMICAL_ABSTRACT_ID, field2=BioFields.KIND, field3=BioFields.SOURCE)
@BioEntity(bioType = BioTypes.CHEMICAL_PROPERTY)
public class ChemicalProperty {
    
    protected static Logger log = LogManager.getLogger(ChemicalProperty.class);

    @GraphId
    private Long id;
    
    /**
    * cas identifier. Eg. "120993-53-5"
    */
    @UniquelyIndexed(indexName=IndexNames.CHEMICAL_ABSTRACT_ID)
    @Taxonomy(rbClass=TaxonomyTypes.CHEMICAL_ABSTRACT_ID, rbField=BioFields.CHEMICAL_ABSTRACT_ID)
    private String casId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.CHEMICAL_PROPERTY.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.CHEMICAL_PROPERTY_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.CHEMICAL_PROPERTY_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;
    
    @PartKey
    private String kind;

    /**
     * Get the chemical abstract Id.
     * 
     * @return String 
     */
    public String getCasId() {
        return casId;
    }

    /**
     * Set the chemical abstract id.
     * 
     * @param casId 
     */
    public void setCasId(String casId) {
        this.casId = casId;
    }

    /**
     * Get the type of chemical property. For instance. Log P.
     * 
     * @return String 
     */
    public String getKind() {
        return kind;
    }

    /**
     * Set the type of chemical property.
     * 
     * @param kind 
     */
    public void setKind(String kind) {
        this.kind = kind;
    }

    /**
     * Get the value of the chemical property.
     * 
     * @return String 
     */
    public String getValue() {
        return value;
    }

    /**
     * Set the value of the chemical property.
     * 
     * @param value 
     */
    public void setValue(String value) {
        this.value = value;
    }

    /**
     * Get the source of the chemical property.
     * 
     * @return String 
     */
    public String getSource() {
        return source;
    }

    /**
     * Set the source of the chemical property.
     * 
     * @param source 
     */
    public void setSource(String source) {
        this.source = source;
    }
    
    @NonIndexed
    private String value;
    
    @PartKey
    private String source;   
}