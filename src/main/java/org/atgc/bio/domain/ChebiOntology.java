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
 * this is imported from proteinontology
 * 
 * It also includes CHEBI (chemical entities of biological interest) - 20 of them
 * http://www.ebi.ac.uk/chebi/
 * 
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.CHEBI_ONTOLOGY)
public class ChebiOntology {
    	
    protected static Log log = LogFactory.getLog(new Object().getClass());
   
    @GraphId
    private Long id;
   
    /**
     * id: CHEBI:37577
     */
    @UniquelyIndexed (indexName=IndexNames.CHEBI_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.CHEBI_ONTOLOGY_ID, rbField=BioFields.CHEBI_ONTOLOGY_ID)
    private String chebiOntologyId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.CHEBI_ONTOLOGY.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @FullTextIndexed (indexName=IndexNames.COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
    private String comment;
    
    /**
     * name
     *   name: heteroatomic molecular entity
     */
    @FullTextIndexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
    
    @FullTextIndexed (indexName=IndexNames.CHEBI_ALTERNATE_IDS)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_ALTERNATE_IDS, rbField=BioFields.CHEBI_ALTERNATE_IDS)
    private String chebiAlternateIds;
     
    @Indexed (indexName=IndexNames.CHEBI_NAME_SPACE)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_NAME_SPACE, rbField=BioFields.CHEBI_NAME_SPACE)
    private String chebiNameSpace;

   
    /** consider list
    @FullTextIndexed (indexName=IndexNames.CHEBI_CONSIDER_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_CONSIDER_LIST, rbField=BioFields.CHEBI_ALTERNATE_IDS)
    private String chebiAlternateIds;
    */
    
    @FullTextIndexed(indexName=IndexNames.CHEBI_NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_NARROW_SYNONYMS, rbField=BioFields.CHEBI_NARROW_SYNONYMS)
    private String chebiNarrowSynonyms;
    
    /**
     * synonym: "chemical compound" RELATED [ChEBI:]
     */
    @FullTextIndexed(indexName=IndexNames.CHEBI_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_RELATED_SYNONYMS, rbField=BioFields.CHEBI_RELATED_SYNONYMS)
    private String chebiRelatedSynonyms;
    
    /**
     * synonym: "molecular entity" EXACT IUPAC_NAME [IUPAC:]
     */
    @FullTextIndexed(indexName=IndexNames.CHEBI_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_EXACT_SYNONYMS, rbField=BioFields.CHEBI_EXACT_SYNONYMS)
    private String chebiExactSynonyms;
    
    @Visual
    @Indexed (indexName=IndexNames.CHEBI_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_ONTOLOGY_NAME, rbField=BioFields.CHEBI_ONTOLOGY_NAME)
    private String chebiOntologyName;    
    
    /**
     * property_value: IAO:0000412 http://purl.obolibrary.org/obo/chebi.owl
     */
    @FullTextIndexed(indexName=IndexNames.CHEBI_PROPERTY_VALUE)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_PROPERTY_VALUE, rbField=BioFields.CHEBI_PROPERTY_VALUE)
    private String chebiPropertyValue;

    /**
     * def: "A molecular entity consisting of two or more chemical elements." []
     */
    @FullTextIndexed (indexName=IndexNames.CHEBI_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.CHEBI_ONTOLOGY_DEF, rbField=BioFields.CHEBI_ONTOLOGY_DEF)
    private String chebiOntologyDef;
    
    /**
     * BioRelTypes: is_a,  is_a: CHEBI:33304 ! chalcogen molecular entity
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> isARelationship;
    
    /**
     * relationship: part_of PR:000018263 ! amino acid chain
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> relationship;
            
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getChebiOntologyId() {
        return chebiOntologyId;
    }

    public void setChebiOntologyId(String chebiOntologyId) {
        this.chebiOntologyId = chebiOntologyId;
    }

    public String getChebiNamespace() {
        return chebiNameSpace;
    }

    public void setChebiNamespace(String chebiNamespace) {
        this.chebiNameSpace = chebiNamespace;
    }
    
    public String getChebiExactSynonyms() {
        return chebiExactSynonyms;
    }

    public void setChebiExactSynonyms(String chebiExactSynonyms) {
        this.chebiExactSynonyms = chebiExactSynonyms;
    }

    public String getChebiNarrowSynonyms() {
        return chebiNarrowSynonyms;
    }

    public void setChebiNarrowSynonyms(String chebiNarrowSynonyms) {
        this.chebiNarrowSynonyms = chebiNarrowSynonyms;
    }

    public String getChebiRelatedSynonyms() {
        return chebiRelatedSynonyms;
    }

    public void setChebiRelatedSynonyms(String chebiRelatedSynonyms) {
        this.chebiRelatedSynonyms = chebiRelatedSynonyms;
    }

    public String getChebiNameSpace() {
        return chebiNameSpace;
    }

    public void setChebiNameSpace(String chebiNameSpace) {
        this.chebiNameSpace = chebiNameSpace;
    }
    
    public String getDef() {
        return chebiOntologyDef;
    }

    public void setDef(String def) {
        this.chebiOntologyDef = def;
    }

    public String getChebiAlternateIds() {
        return chebiAlternateIds;
    }

    public void setChebiAlternateIds(String altIds) {
        this.chebiAlternateIds = altIds;
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
    
    public String getChebiOntologyName() {
        return chebiOntologyName;
    }

    public void setChebiOntologyName(String chebiOntologyName) {
        this.chebiOntologyName = chebiOntologyName;
    }
   
    public String getChebiOntologyDef() {
        return chebiOntologyDef;
    }

    public void setChebiOntologyDef(String chebiOntologyDef) {
        this.chebiOntologyDef = chebiOntologyDef;
    }
    
    public String getChebiPropertyValue() {
        return chebiPropertyValue;
    }

    public void setChebiPropertyValue(String chebiPropertyValue) {
        this.chebiPropertyValue = chebiPropertyValue;
    }
    
    
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + chebiOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ChebiOntology other = (ChebiOntology) obj;
        if ((this.chebiOntologyId == null) ? (other.chebiOntologyId != null) : !this.chebiOntologyId.equals(other.chebiOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }
  
    /**
     * setRelationship
     * @param endNode {@link BioEntity} {@link BioTypes#GENE_ONTOLOGY}
     * @param relType {@link BioRelTypes#IS_A} 
     *                {@link BioRelTypes#REGULATES}
     *                {@link BioRelTypes#NEGATIVELY_REGULATES}
     *                {@link BioRelTypes#POSITIVELY_REGULATES}
     */
    public void setRelationship(Object endNode, BioRelTypes relType) {
        if (relationship == null) {
            relationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        relationship.add(rel);
    }
    
    public Iterable<BioRelation> getRelationship() {
        return relationship;
    }
   
    
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
