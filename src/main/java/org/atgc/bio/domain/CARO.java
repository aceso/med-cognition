/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 *    obofoundry.org
 * human-dev-anat-abstract2.obo
 * CARO: http://www.bioontology.org/wiki/images/0/0d/CAROchapter.pdf
 * 
 * Relationships: http://www.geneontology.org/GO.ontology-ext.relations.shtml
 * These three reltypes are transitive
 * {@link BioRelTypes#HAS_PART} {@link BIORELTYPES#PART_OF} {@link BioRelTypes#DEVELOPS_FROM}
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.CARO)
public class CARO {
    	
    protected static Logger log = LogManager.getLogger(CARO.class);
   
    @GraphId
    private Long id;
   
    /**
     * "id" : "CARO:0000067",
     */
    @UniquelyIndexed (indexName=IndexNames.CARO_ID)
    @Taxonomy(rbClass=TaxonomyTypes.CARO_ID, rbField=BioFields.CARO_ID)
    private String CAROId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.CARO.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @FullTextIndexed(indexName=IndexNames.COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
    private String comment; 
    
    /**
     * "name" : "simple cuboidal epithelium",
     */
    @FullTextIndexed (indexName=IndexNames.CARO_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.CARO_NAME, rbField=BioFields.CARO_NAME)
    private String CAROName;    
   
    /**
     * synonym: "portion of organism substance" RELATED []
     */
    @FullTextIndexed(indexName=IndexNames.CARO_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.CARO_RELATED_SYNONYMS, rbField=BioFields.CARO_RELATED_SYNONYMS)
    private String CARORelatedSynonyms;
    
    /**
     * 
     */
    @Indexed (indexName=IndexNames.CARO_ALT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.CARO_ALT_ID, rbField=BioFields.CARO_ALT_ID)
    private String CAROAltId;
    
    /**
     * 	"def" : "\"Unilaminar epithelium that consists of a single layer of 
     *   cuboidal cells.\" [CARO:MAH]",
     */
    @FullTextIndexed (indexName=IndexNames.CARO_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.CARO_DEF, rbField=BioFields.CARO_DEF)
    private String CARODef;
   
    
    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
    
     /**
      * has_part CL:0000066 ! epithelial cell
      * has_part
      */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> CARORelationship;
   
    /**
     * <pre>
     * "isList" : [
		{
			"is_a" : "AEO:0000192 ! anatomical surface feature"
		},
		{
			"is_a" : "CARO:0000010 ! anatomical surface"
		}
	]
     * </pre>
     * BioRelTypes = is_a
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> isARelationship;
     
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCAROId() {
        return CAROId;
    }

    public void setCAROId(String CAROId) {
        this.CAROId = CAROId;
    }

    public String getCAROName() {
        return CAROName;
    }

    public void setCAROName(String CAROName) {
        this.CAROName = CAROName;
    }

    public String getCARORelatedSynonyms() {
        return CARORelatedSynonyms;
    }

    public void setCARORelatedSynonyms(String CARORelatedSynonyms) {
        this.CARORelatedSynonyms = CARORelatedSynonyms;
    }
    
    public String getDef() {
        return CARODef;
    }

    public void setDef(String def) {
        this.CARODef = def;
    }

    public String getCAROAltId() {
        return CAROAltId;
    }

    public void setCAROAltId(String geneOntologyAltId) {
        this.CAROAltId = CAROAltId;
    }
      
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
   
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + CAROId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CARO other = (CARO) obj;
        if ((this.CAROId == null) ? (other.CAROId != null) : !this.CAROId.equals(other.CAROId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }
  
    
    /**
     * <pre>
     * "isList" : [
		{
			"is_a" : "CARO:0000073 ! unilaminar epithelium"
		}
	],
     * </pre>
     *
     * </pre>
     * @param endNode
     * @param relType 
     */
    public void setIsARelationship(Object endNode, BioRelTypes relType) {
        if (isARelationship == null) {
            isARelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        isARelationship.add(rel);
    }

    public Iterable<BioRelation> getIsARelationship() {
        return isARelationship;
    }
    
    /**
     * <pre>
     * 
     * </pre>
     * 
     * @param endNode
     * @param relType 
     */
    public void setCARORelationship(Object endNode, BioRelTypes relType) {
        if (CARORelationship == null) {
            CARORelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        CARORelationship.add(rel);
    }
    
    public Iterable<BioRelation> getCARORelationship() {
        return CARORelationship;
    }
  
    
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
}
