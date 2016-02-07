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
 * HumanDO.obo - human disease ontology obofoundry
 * 
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.HUMAN_DISEASE_ONTOLOGY)
public class HumanDiseaseOntology {
    	
    protected static Logger log = LogManager.getLogger(HumanDiseaseOntology.class);
   
    @GraphId
    private Long id;
   
    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed(indexName=IndexNames.HUMAN_DISEASE_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.HUMAN_DISEASE_ONTOLOGY_ID, rbField=BioFields.HUMAN_DISEASE_ONTOLOGY_ID)
    private String humanDiseaseOntologyId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.HUMAN_DISEASE_ONTOLOGY.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @FullTextIndexed (indexName=IndexNames.COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
    private String comment;
    
    /**
     * name
     *   The term name. Any term may have only one name defined.
     *   If multiple term names are defined, it is a parse error.
     * 
     *   
     */
    @FullTextIndexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
     
    @Indexed (indexName=IndexNames.HUMAN_DISEASE_ONTOLOGY_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.HUMAN_DISEASE_ONTOLOGY_IS_OBSOLETE, rbField=BioFields.HUMAN_DISEASE_ONTOLOGY_IS_OBSOLETE)
    private String humanDiseaseIsObsolete;
   
    
    @FullTextIndexed(indexName=IndexNames.HUMAN_DISEASE_ONTOLOGY_NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.HUMAN_DISEASE_ONTOLOGY_NARROW_SYNONYMS, rbField=BioFields.HUMAN_DISEASE_ONTOLOGY_NARROW_SYNONYMS)
    private String humanDiseaseNarrowSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.HUMAN_DISEASE_ONTOLOGY_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.HUMAN_DISEASE_ONTOLOGY_RELATED_SYNONYMS, rbField=BioFields.HUMAN_DISEASE_ONTOLOGY_RELATED_SYNONYMS)
    private String humanDiseaseRelatedSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.HUMAN_DISEASE_ONTOLOGY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.HUMAN_DISEASE_ONTOLOGY_EXACT_SYNONYMS, rbField=BioFields.HUMAN_DISEASE_ONTOLOGY_EXACT_SYNONYMS)
    private String humanDiseaseExactSynonyms;
    
    @Visual
    @Indexed (indexName=IndexNames.HUMAN_DISEASE_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.HUMAN_DISEASE_ONTOLOGY_NAME, rbField=BioFields.HUMAN_DISEASE_ONTOLOGY_NAME)
    private String humanDiseaseOntologyName;    
    
    @FullTextIndexed (indexName=IndexNames.HUMAN_DISEASE_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.HUMAN_DISEASE_ONTOLOGY_DEF, rbField=BioFields.HUMAN_DISEASE_ONTOLOGY_DEF)
    private String humanDiseaseOntologyDef;
    
    /**
     * <pre>
     *   This tag indicates a term subset to which this term belongs. 
     *   The value of this tag must be a subset name as defined in a subsetdef tag in 
     *   the file header. If the value of this tag is not mentioned in a subsetdef tag, 
     *   a parse error will be generated. 
     *   A term may belong to any number of subsets.
     * 
     *   subset: gram-negative_bacterial_infectious_disease
     * </pre>
     */
    @FullTextIndexed (indexName=IndexNames.HUMAN_DISEASE_ONTOLOGY_SUBSETS)
    @Taxonomy (rbClass=TaxonomyTypes.HUMAN_DISEASE_ONTOLOGY_SUBSETS, rbField=BioFields.HUMAN_DISEASE_ONTOLOGY_SUBSETS)
    private String humanDiseaseSubsets;

    
    /**
     * 
     * BioRelTypes: is_a:
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> isARelationship;
    
    /**
     *  alt_id: DOID:10636
     *  Defines an alternate id for this term. A term may have any number of alternate ids 
     */
    @RelatedToVia(direction=Direction.BOTH, elementClass = BioRelation.class)
    private Collection<BioRelation> altIdRelationship;
    
            
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHumanDiseaseOntologyId() {
        return humanDiseaseOntologyId;
    }

    public void setHumanDiseaseOntologyId(String humanDiseaseOntologyId) {
        this.humanDiseaseOntologyId = humanDiseaseOntologyId;
    }

    public String getHumanDiseaseExactSynonyms() {
        return humanDiseaseExactSynonyms;
    }

    public void setHumanDiseaseExactSynonyms(String humanDiseaseExactSynonyms) {
        this.humanDiseaseExactSynonyms = humanDiseaseExactSynonyms;
    }

    public String getHumanDiseaseNarrowSynonyms() {
        return humanDiseaseNarrowSynonyms;
    }

    public void setHumanDiseaseNarrowSynonyms(String humanDiseaseNarrowSynonyms) {
        this.humanDiseaseNarrowSynonyms = humanDiseaseNarrowSynonyms;
    }

    public String getHumanDiseaseRelatedSynonyms() {
        return humanDiseaseRelatedSynonyms;
    }

    public void setHumanDiseaseRelatedSynonyms(String humanDiseaseRelatedSynonyms) {
        this.humanDiseaseRelatedSynonyms = humanDiseaseRelatedSynonyms;
    }
    
    public String getDef() {
        return humanDiseaseOntologyDef;
    }

    public void setDef(String def) {
        this.humanDiseaseOntologyDef = def;
    }
    
    public String getHumanDiseaseSubsets() {
        return humanDiseaseSubsets;
    }

    public void setHumanDiseaseSubsets(String humanDiseaseSubsets) {
        this.humanDiseaseSubsets = humanDiseaseSubsets;
    }
    
    public void setHumanDiseaseIsObsolete(String str) {
        this.humanDiseaseIsObsolete = str;
    }
    
    public String getHumanDiseaseIsObsolete() {
        return this.humanDiseaseIsObsolete;
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
    
    public String getHumanDiseaseOntologyName() {
        return humanDiseaseOntologyName;
    }

    public void setHumanDiseaseOntologyName(String humanDiseaseOntologyName) {
        this.humanDiseaseOntologyName = humanDiseaseOntologyName;
    }
   
    public String getHumanDiseaseOntologyDef() {
        return humanDiseaseOntologyDef;
    }

    public void setHumanDiseaseOntologyDef(String humanDiseaseOntologyDef) {
        this.humanDiseaseOntologyDef = humanDiseaseOntologyDef;
    }
    
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + humanDiseaseOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HumanDiseaseOntology other = (HumanDiseaseOntology) obj;
        if ((this.humanDiseaseOntologyId == null) ? (other.humanDiseaseOntologyId != null) : !this.humanDiseaseOntologyId.equals(other.humanDiseaseOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }
  
    /**
     * {@link BioRelTypes#IS_AN_ALTERNATE_ID}
     * setAltIdRelationship
     * @param endNode 
     * @param relType 
     */
    public void setAltIdRelationship(Object endNode, BioRelTypes relType) {
        if (altIdRelationship == null) {
            altIdRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        altIdRelationship.add(rel);
    }
    
    public Iterable<BioRelation> getAltIdRelationship() {
        return altIdRelationship;
    }
   
   /**
    * {@link BioRelTypes#IS_A}
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
      
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * XRef databases are not stored
     */
}
