/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import javafx.scene.control.Cell;
import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * http://www.cellontology.org,  obofoundry.org (CL:
 * 
 * It also includes CHEBI (chemical entities of biological interest) - 20 of them
 * http://www.ebi.ac.uk/chebi/
 * 
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.CELL_TYPE_ONTOLOGY)
public class CellTypeOntology {
    	
    protected static Logger log = LogManager.getLogger(CellTypeOntology.class);
   
    @GraphId
    private Long id;
   
    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed (indexName=IndexNames.CELL_TYPE_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.CELL_TYPE_ONTOLOGY_ID, rbField=BioFields.CELL_TYPE_ONTOLOGY_ID)
    private String cellTypeOntologyId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.CELL_TYPE_ONTOLOGY.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @FullTextIndexed(indexName=IndexNames.COMMENT)
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
    
    @FullTextIndexed (indexName=IndexNames.CELL_TYPE_ALTERNATE_IDS)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_ALTERNATE_IDS, rbField=BioFields.CELL_TYPE_ALTERNATE_IDS)
    private String cellTypeAlternateIds;
     
    @Indexed (indexName=IndexNames.CELL_TYPE_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_IS_OBSOLETE, rbField=BioFields.CELL_TYPE_IS_OBSOLETE)
    private String cellTypeIsObsolete;

   
    /** consider list
    @FullTextIndexed (indexName=IndexNames.CELL_TYPE_CONSIDER_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_CONSIDER_LIST, rbField=BioFields.CELL_TYPE_ALTERNATE_IDS)
    private String cellTypeAlternateIds;
    */
    
    @FullTextIndexed (indexName=IndexNames.CELL_TYPE_NAME_SPACE)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_NAME_SPACE, rbField=BioFields.CELL_TYPE_NAME_SPACE)
    private String cellTypeNameSpace;
    
    @FullTextIndexed(indexName=IndexNames.CELL_TYPE_NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_NARROW_SYNONYMS, rbField=BioFields.CELL_TYPE_NARROW_SYNONYMS)
    private String cellTypeNarrowSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.CELL_TYPE_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_RELATED_SYNONYMS, rbField=BioFields.CELL_TYPE_RELATED_SYNONYMS)
    private String cellTypeRelatedSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.CELL_TYPE_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_EXACT_SYNONYMS, rbField=BioFields.CELL_TYPE_EXACT_SYNONYMS)
    private String cellTypeExactSynonyms;
    
    @Visual
    @Indexed (indexName=IndexNames.CELL_TYPE_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_ONTOLOGY_NAME, rbField=BioFields.CELL_TYPE_ONTOLOGY_NAME)
    private String cellTypeOntologyName;    
    
    /**
     * namespace" : "cell"
     */
    @Indexed (indexName=IndexNames.CELL_TYPE_ONTOLOGY_NAMESPACE)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_ONTOLOGY_NAMESPACE, rbField=BioFields.CELL_TYPE_ONTOLOGY_NAMESPACE)
    private String cellTypeOntologyNamespace;
    
    @FullTextIndexed (indexName=IndexNames.CELL_TYPE_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_ONTOLOGY_DEF, rbField=BioFields.CELL_TYPE_ONTOLOGY_DEF)
    private String cellTypeOntologyDef;
    
    @FullTextIndexed (indexName=IndexNames.CELL_TYPE_SUBSETS)
    @Taxonomy (rbClass=TaxonomyTypes.CELL_TYPE_SUBSETS, rbField=BioFields.CELL_TYPE_SUBSETS)
    private String cellTypeSubsets;

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
    
    /**
     * disjoint relationship
     * disjoint_from: GO:0005575 ! cellular_component
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> disjointRelationship;
    
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.CONSIDER_RELATIONSHIP, elementClass=BioRelation.class)
    private Collection<BioRelation> considerRelationship;
            
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCellTypeOntologyId() {
        return cellTypeOntologyId;
    }

    public void setCellTypeOntologyId(String cellTypeOntologyId) {
        this.cellTypeOntologyId = cellTypeOntologyId;
    }

    public String getCellTypeOntologyNamespace() {
        return cellTypeOntologyNamespace;
    }

    public void setCellTypeOntologyNamespace(String cellTypeOntologyNamespace) {
        this.cellTypeOntologyNamespace = cellTypeOntologyNamespace;
    }
    
    public String getCellTypeExactSynonyms() {
        return cellTypeExactSynonyms;
    }

    public void setCellTypeExactSynonyms(String cellTypeExactSynonyms) {
        this.cellTypeExactSynonyms = cellTypeExactSynonyms;
    }

    public String getCellTypeNarrowSynonyms() {
        return cellTypeNarrowSynonyms;
    }

    public void setCellTypeNarrowSynonyms(String cellTypeNarrowSynonyms) {
        this.cellTypeNarrowSynonyms = cellTypeNarrowSynonyms;
    }

    public String getCellTypeRelatedSynonyms() {
        return cellTypeRelatedSynonyms;
    }

    public void setCellTypeRelatedSynonyms(String cellTypeRelatedSynonyms) {
        this.cellTypeRelatedSynonyms = cellTypeRelatedSynonyms;
    }

    public String getCellTypeNameSpace() {
        return cellTypeNameSpace;
    }

    public void setCellTypeNameSpace(String cellTypeNameSpace) {
        this.cellTypeNameSpace = cellTypeNameSpace;
    }
    
    public String getDef() {
        return cellTypeOntologyDef;
    }

    public void setDef(String def) {
        this.cellTypeOntologyDef = def;
    }

    public String getCellTypeAlternateIds() {
        return cellTypeAlternateIds;
    }

    public void setCellTypeAlternateIds(String altIds) {
        this.cellTypeAlternateIds = altIds;
    }
    
    public String getCellTypeSubsets() {
        return cellTypeSubsets;
    }

    public void setCellTypeSubsets(String cellTypeSubsets) {
        this.cellTypeSubsets = cellTypeSubsets;
    }
    
    public void setCellTypeIsObsolete(String str) {
        this.cellTypeIsObsolete = str;
    }
    
    public String getCellIsObsolete() {
        return this.cellTypeIsObsolete;
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
    
    public String getCellTypeOntologyName() {
        return cellTypeOntologyName;
    }

    public void setCellTypeOntologyName(String cellTypeOntologyName) {
        this.cellTypeOntologyName = cellTypeOntologyName;
    }
   
    public String getCellTypeOntologyDef() {
        return cellTypeOntologyDef;
    }

    public void setCellTypeOntologyDef(String cellTypeOntologyDef) {
        this.cellTypeOntologyDef = cellTypeOntologyDef;
    }
    
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + cellTypeOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CellTypeOntology other = (CellTypeOntology) obj;
        if ((this.cellTypeOntologyId == null) ? (other.cellTypeOntologyId != null) : !this.cellTypeOntologyId.equals(other.cellTypeOntologyId)) {
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
    
    public void setDisjointRelationship(Object endNode, BioRelTypes relType) {
        if (disjointRelationship == null) {
            disjointRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        disjointRelationship.add(rel);
    }
    
    public Iterable<BioRelation> getDisjointRelationship() {
        return disjointRelationship;
    }
    
    public void setConsiderRelationships(Object newTermGOEntity) {
        if (considerRelationship == null) {
            considerRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, newTermGOEntity, BioRelTypes.CONSIDER_RELATIONSHIP);
        considerRelationship.add(rel);
    }

    public Iterable<BioRelation> getConsiderRelationships() {
        return considerRelationship;
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
