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
 * PATO 
 * http://www.cellontology.org,  obofoundry.org (CL:
 * 
 * It also includes CHEBI (chemical entities of biological interest) - 20 of them
 * http://www.ebi.ac.uk/chebi/
 * 
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.PHENOTYPIC_ONTOLOGY)
public class PhenotypicOntology {
    	
    protected static Logger log = LogManager.getLogger(PhenotypicOntology.class);
   
    @GraphId
    private Long id;
   
    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed(indexName=IndexNames.PHENOTYPIC_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.PHENOTYPIC_ONTOLOGY_ID, rbField=BioFields.PHENOTYPIC_ONTOLOGY_ID)
    private String phenotypicOntologyId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PHENOTYPIC_ONTOLOGY.toString();
   
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
     *   name" : "primary cultured cell",
     */
    @FullTextIndexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
    
    @FullTextIndexed (indexName=IndexNames.PHENOTYPIC_ALTERNATE_IDS)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_ALTERNATE_IDS, rbField=BioFields.PHENOTYPIC_ALTERNATE_IDS)
    private String phenotypicAlternateIds;
     
    @Indexed (indexName=IndexNames.PHENOTYPIC_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_IS_OBSOLETE, rbField=BioFields.PHENOTYPIC_IS_OBSOLETE)
    private String phenotypicIsObsolete;

   
    /** consider list
    @FullTextIndexed (indexName=IndexNames.PHENOTYPIC_CONSIDER_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_CONSIDER_LIST, rbField=BioFields.PHENOTYPIC_ALTERNATE_IDS)
    private String phenotypicAlternateIds;
    */
    
    @FullTextIndexed (indexName=IndexNames.PHENOTYPIC_NAMESPACE)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_NAMESPACE, rbField=BioFields.PHENOTYPIC_NAMESPACE)
    private String phenotypicNameSpace;
    
    @FullTextIndexed(indexName=IndexNames.PHENOTYPIC_NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_NARROW_SYNONYMS, rbField=BioFields.PHENOTYPIC_NARROW_SYNONYMS)
    private String phenotypicNarrowSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.PHENOTYPIC_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_RELATED_SYNONYMS, rbField=BioFields.PHENOTYPIC_RELATED_SYNONYMS)
    private String phenotypicRelatedSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.PHENOTYPIC_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_EXACT_SYNONYMS, rbField=BioFields.PHENOTYPIC_EXACT_SYNONYMS)
    private String phenotypicExactSynonyms;
    
    @Visual
    @Indexed (indexName=IndexNames.PHENOTYPIC_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_ONTOLOGY_NAME, rbField=BioFields.PHENOTYPIC_ONTOLOGY_NAME)
    private String phenotypicOntologyName;    
    
    @FullTextIndexed (indexName=IndexNames.PHENOTYPIC_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_ONTOLOGY_DEF, rbField=BioFields.PHENOTYPIC_ONTOLOGY_DEF)
    private String phenotypicOntologyDef;
    
    @FullTextIndexed (indexName=IndexNames.PHENOTYPIC_SUBSETS)
    @Taxonomy (rbClass=TaxonomyTypes.PHENOTYPIC_SUBSETS, rbField=BioFields.PHENOTYPIC_SUBSETS)
    private String phenotypicSubsets;

    /**
     * http://www.geneontology.org/GO.ontology.relations.shtml
     * intersection_of: RO:0002215 GO:0045730 ! capable_of respiratory burst
     * BioRelTypes: has_completed, has_part, participates_in, has_plasma_membrane_part,
     * capable_of, has_low_plasma_membrane_amount
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> intersectionRelationship;
    
    /**
     * BioRelTypes: is_a:
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> isARelationship;
    
    /**
     * relationship
     * relationship: has_completed GO:0030217 ! T cell differentiation
     * relationship: has_part GO:0000792 ! heterochromatin
     * relationship: participates_in GO:0007143 ! female meiosis
     * relationship: has_low_plasma_membrane_amount PR:000001012 ! integrin alpha-M
     * 
     * BioRelTypes: develops_from, is_a, bearer_of,
     * participates_in, lacks_plasma_membrane_part, has_plasma_membrane_part,
     * has_part, produces, has_high_plasma_membrane_amount, lacks_part, has_completed
     * has_low_plasma_membrane_amount
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> relationship;
   
            
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPhenotypicOntologyId() {
        return phenotypicOntologyId;
    }

    public void setPhenotypicOntologyId(String phenotypicOntologyId) {
        this.phenotypicOntologyId = phenotypicOntologyId;
    }
    
    public String getPhenotypicExactSynonyms() {
        return phenotypicExactSynonyms;
    }

    public void setPhenotypicExactSynonyms(String phenotypicExactSynonyms) {
        this.phenotypicExactSynonyms = phenotypicExactSynonyms;
    }

    public String getPhenotypicNarrowSynonyms() {
        return phenotypicNarrowSynonyms;
    }

    public void setPhenotypicNarrowSynonyms(String phenotypicNarrowSynonyms) {
        this.phenotypicNarrowSynonyms = phenotypicNarrowSynonyms;
    }

    public String getPhenotypicRelatedSynonyms() {
        return phenotypicRelatedSynonyms;
    }

    public void setPhenotypicRelatedSynonyms(String phenotypicRelatedSynonyms) {
        this.phenotypicRelatedSynonyms = phenotypicRelatedSynonyms;
    }

    public String getPhenotypicNameSpace() {
        return phenotypicNameSpace;
    }

    public void setPhenotypicNameSpace(String phenotypicNameSpace) {
        this.phenotypicNameSpace = phenotypicNameSpace;
    }
    
    public String getDef() {
        return phenotypicOntologyDef;
    }

    public void setDef(String def) {
        this.phenotypicOntologyDef = def;
    }

    public String getPhenotypicAlternateIds() {
        return phenotypicAlternateIds;
    }

    public void setPhenotypicAlternateIds(String altIds) {
        this.phenotypicAlternateIds = altIds;
    }
    
    public String getPhenotypicSubsets() {
        return phenotypicSubsets;
    }

    public void setPhenotypicSubsets(String phenotypicSubsets) {
        this.phenotypicSubsets = phenotypicSubsets;
    }
    
    public void setPhenotypicIsObsolete(String str) {
        this.phenotypicIsObsolete = str;
    }
    
    public String getCellIsObsolete() {
        return this.phenotypicIsObsolete;
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
    
    public String getPhenotypicOntologyName() {
        return phenotypicOntologyName;
    }

    public void setPhenotypicOntologyName(String phenotypicOntologyName) {
        this.phenotypicOntologyName = phenotypicOntologyName;
    }
   
    public String getPhenotypicOntologyDef() {
        return phenotypicOntologyDef;
    }

    public void setPhenotypicOntologyDef(String phenotypicOntologyDef) {
        this.phenotypicOntologyDef = phenotypicOntologyDef;
    }
    
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + phenotypicOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PhenotypicOntology other = (PhenotypicOntology) obj;
        if ((this.phenotypicOntologyId == null) ? (other.phenotypicOntologyId != null) : !this.phenotypicOntologyId.equals(other.phenotypicOntologyId)) {
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
    
    public void setIntersectionRelationship(Object endNode, BioRelTypes relType) {
        if (intersectionRelationship == null) {
            intersectionRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        intersectionRelationship.add(rel);
    }
    
    public Iterable<BioRelation> getIntersectionRelationship() {
        return intersectionRelationship;
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
