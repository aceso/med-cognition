/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * spatial.obo,   obofoundry.org
 * 
 * Relationships: http://www.geneontology.org/GO.ontology-ext.relations.shtml
 * These three reltypes are transitive
 * {@link BioRelTypes#HAS_PART} {@link BioRelTypes#PART_OF} {@link BioRelTypes#DEVELOPS_FROM}
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.ANATOMICAL_ENTITY_ONTOLOGY)
public class AnatomicalEntityOntology {
    	
    protected static Log log = LogFactory.getLog(new Object().getClass());
   
    @GraphId
    private Long id;
   
    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed (indexName=IndexNames.ANATOMICAL_ENTITY_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.ANATOMICAL_ENTITY_ONTOLOGY_ID, rbField=BioFields.ANATOMICAL_ENTITY_ONTOLOGY_ID)
    private String anatomicalEntityOntologyId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.ANATOMICAL_ENTITY_ONTOLOGY.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @FullTextIndexed (indexName=IndexNames.COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
    private String comment; 
    
    /**
     * set to true
     */
    @Indexed (indexName=IndexNames.ANATOMICAL_ENTITY_ONTOLOGY_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.ANATOMICAL_ENTITY_ONTOLOGY_IS_OBSOLETE, rbField=BioFields.ANATOMICAL_ENTITY_ONTOLOGY_IS_OBSOLETE)
    private String anatomicalEntityOntologyIsObsolete;
   
    /**
     * name" : "immature protein part
     */
    @FullTextIndexed (indexName=IndexNames.ANATOMICAL_ENTITY_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.ANATOMICAL_ENTITY_ONTOLOGY_NAME, rbField=BioFields.ANATOMICAL_ENTITY_ONTOLOGY_NAME)
    private String anatomicalEntityOntologyName;    
    
    
    @FullTextIndexed(indexName=IndexNames.ANATOMICAL_ENTITY_ONTOLOGY_NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.ANATOMICAL_ENTITY_ONTOLOGY_NARROW_SYNONYMS, rbField=BioFields.ANATOMICAL_ENTITY_ONTOLOGY_NARROW_SYNONYMS)
    private String anatomicalEntityOntologyNarrowSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.ANATOMICAL_ENTITY_ONTOLOGY_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.ANATOMICAL_ENTITY_ONTOLOGY_RELATED_SYNONYMS, rbField=BioFields.ANATOMICAL_ENTITY_ONTOLOGY_RELATED_SYNONYMS)
    private String anatomicalEntityOntologyRelatedSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.ANATOMICAL_ENTITY_ONTOLOGY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.ANATOMICAL_ENTITY_ONTOLOGY_EXACT_SYNONYMS, rbField=BioFields.ANATOMICAL_ENTITY_ONTOLOGY_EXACT_SYNONYMS)
    private String anatomicalEntityOntologyExactSynonyms;

   
    /**
     * alt_id: CL:0000274
     */
    @Indexed (indexName=IndexNames.ANATOMICAL_ENTITY_ONTOLOGY_ALT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.ANATOMICAL_ENTITY_ONTOLOGY_ALT_ID, rbField=BioFields.ANATOMICAL_ENTITY_ONTOLOGY_ALT_ID)
    private String anatomicalEntityOntologyAltId;
    
    /**
     * def: "A protein that belongs to the clade D of the bHLH proteins (see PMID:20219281) with a 
     * core domain composition consisting of a helix-loop-helix DNA-binding domain (PF00010) (HLH), 
     * common to the basic HLH family of transcription factors, but lacking the DNA binding domain 
     * to the consensus E box response element (CANNTG). By binding to basic HLH transcription factors, 
     * proteins in this class regulate gene expression." [PMID:20219281, PRO:CNA]
     */
    @FullTextIndexed (indexName=IndexNames.ANATOMICAL_ENTITY_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.ANATOMICAL_ENTITY_ONTOLOGY_DEF, rbField=BioFields.ANATOMICAL_ENTITY_ONTOLOGY_DEF)
    private String anatomicalEntityOntologyDef;
   
    
    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
    
     /**
      * Reltypes: {@link BioRelTypes#SURROUNDS} {@link BioRelTypes#HAS_PART}
      * {@link BioRelTypes#PART_OF}
      */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> anatomicalEntityOntologyRelationship;
   
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

    public String getAnatomicalEntityOntologyId() {
        return anatomicalEntityOntologyId;
    }

    public void setAnatomicalEntityOntologyId(String anatomicalEntityOntologyId) {
        this.anatomicalEntityOntologyId = anatomicalEntityOntologyId;
    }

    public String getAnatomicalEntityOntologyName() {
        return anatomicalEntityOntologyName;
    }

    public void setAnatomicalEntityOntologyName(String anatomicalEntityOntologyName) {
        this.anatomicalEntityOntologyName = anatomicalEntityOntologyName;
    }
    
    public String getAnatomicalEntityOntologyExactSynonyms() {
        return anatomicalEntityOntologyExactSynonyms;
    }

    public void setAnatomicalEntityOntologyExactSynonyms(String anatomicalEntityOntologyExactSynonyms) {
        this.anatomicalEntityOntologyExactSynonyms = anatomicalEntityOntologyExactSynonyms;
    }

    public String getAnatomicalEntityOntologyNarrowSynonyms() {
        return anatomicalEntityOntologyNarrowSynonyms;
    }

    public void setAnatomicalEntityOntologyNarrowSynonyms(String anatomicalEntityOntologyNarrowSynonyms) {
        this.anatomicalEntityOntologyNarrowSynonyms = anatomicalEntityOntologyNarrowSynonyms;
    }

    public String getAnatomicalEntityOntologyRelatedSynonyms() {
        return anatomicalEntityOntologyRelatedSynonyms;
    }

    public void setAnatomicalEntityOntologyRelatedSynonyms(String anatomicalEntityOntologyRelatedSynonyms) {
        this.anatomicalEntityOntologyRelatedSynonyms = anatomicalEntityOntologyRelatedSynonyms;
    }
    
    public String getDef() {
        return anatomicalEntityOntologyDef;
    }

    public void setDef(String def) {
        this.anatomicalEntityOntologyDef = def;
    }

    public String getAnatomicalEntityOntologyAltId() {
        return anatomicalEntityOntologyAltId;
    }

    public void setAnatomicalEntityOntologyAltId(String geneOntologyAltId) {
        this.anatomicalEntityOntologyAltId = anatomicalEntityOntologyAltId;
    }
      
    public String getAnatomicalEntityOntologyIsObsolete() {
        return anatomicalEntityOntologyIsObsolete;
    }

    public void setAnatomicalEntityOntologyIsObsolete(String isObsolete) {
        this.anatomicalEntityOntologyIsObsolete = isObsolete;
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
        return (nodeType + "-" + name + "-" + anatomicalEntityOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AnatomicalEntityOntology other = (AnatomicalEntityOntology) obj;
        if ((this.anatomicalEntityOntologyId == null) ? (other.anatomicalEntityOntologyId != null) : !this.anatomicalEntityOntologyId.equals(other.anatomicalEntityOntologyId)) {
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
			"is_a" : "AEO:0000192 ! anatomical surface feature"
		},
		{
			"is_a" : "CARO:0000010 ! anatomical surface"
		}
	]
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
     * 	"relationshipList" : [
		{
			"relationship" : "has_part CL:0000058 ! chondroblast"
		},
		{
			"relationship" : "has_part CL:0000062 ! osteoblast"
		},
		{
			"relationship" : "has_part CL:0000138 ! chondrocyte"
		},
		{
			"relationship" : "part_of AEO:0000168 ! skeletal system"
		} 
	]
     * </pre>
     * Reltypes: {@link BioRelTypes#SURROUNDS} {@link BioRelTypes#HAS_PART}
     * {@link BioRelTypes#PART_OF}
     * @param endNode
     * @param relType 
     */
    public void setAnatomicalEntityOntologyRelationship(Object endNode, BioRelTypes relType) {
        if (anatomicalEntityOntologyRelationship == null) {
            anatomicalEntityOntologyRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        anatomicalEntityOntologyRelationship.add(rel);
    }
    
    public Iterable<BioRelation> getAnatomicalEntityOntologyRelationship() {
        return anatomicalEntityOntologyRelationship;
    }
  
    
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
}
