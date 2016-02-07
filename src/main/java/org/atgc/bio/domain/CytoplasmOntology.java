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

@BioEntity(bioType = BioTypes.CYTOPLASM_ONTOLOGY)
public class CytoplasmOntology {
    	
    protected static Logger log = LogManager.getLogger(CytoplasmOntology.class);
   
    @GraphId
    private Long id;
   
    /**
     * 
     */
    @UniquelyIndexed(indexName=IndexNames.CYTOPLASM_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.CYTOPLASM_ONTOLOGY_ID, rbField=BioFields.CYTOPLASM_ONTOLOGY_ID)
    private String cytoplasmOntologyId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.CYTOPLASM_ONTOLOGY.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
   
    /**
     * 
     */
    @FullTextIndexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
   
    @FullTextIndexed (indexName=IndexNames.CYTOPLASM_NAMESPACE)
    @Taxonomy (rbClass=TaxonomyTypes.CYTOPLASM_NAMESPACE, rbField=BioFields.CYTOPLASM_NAMESPACE)
    private String cytoplasmNameSpace;
    
    @FullTextIndexed(indexName=IndexNames.CYTOPLASM_NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.CYTOPLASM_NARROW_SYNONYMS, rbField=BioFields.CYTOPLASM_NARROW_SYNONYMS)
    private String cytoplasmNarrowSynonyms;
   
    
    @Visual
    @Indexed (indexName=IndexNames.CYTOPLASM_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.CYTOPLASM_ONTOLOGY_NAME, rbField=BioFields.CYTOPLASM_ONTOLOGY_NAME)
    private String cytoplasmOntologyName;    
   
    
    @FullTextIndexed (indexName=IndexNames.CYTOPLASM_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.CYTOPLASM_ONTOLOGY_DEF, rbField=BioFields.CYTOPLASM_ONTOLOGY_DEF)
    private String cytoplasmOntologyDef;
    
    @FullTextIndexed(indexName=IndexNames.CREATED_BY)
    @Taxonomy (rbClass=TaxonomyTypes.CREATED_BY, rbField=BioFields.CREATED_BY)
    private String createdBy;

    @FullTextIndexed(indexName=IndexNames.CREATION_DATE)
    @Taxonomy (rbClass=TaxonomyTypes.CREATION_DATE, rbField=BioFields.CREATION_DATE)
    private String creationDate;
   

    /**
     * 
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> intersectionRelationship;
    
    /**
     * BioRelTypes: is_a:
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> isARelationship;
    
    /**
     * 
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> relationship;
   
            
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCytoplasmOntologyId() {
        return cytoplasmOntologyId;
    }

    public void setCytoplasmOntologyId(String cytoplasmOntologyId) {
        this.cytoplasmOntologyId = cytoplasmOntologyId;
    }

    public String getCytoplasmNarrowSynonyms() {
        return cytoplasmNarrowSynonyms;
    }

    public void setCytoplasmNarrowSynonyms(String cytoplasmNarrowSynonyms) {
        this.cytoplasmNarrowSynonyms = cytoplasmNarrowSynonyms;
    }

    public String getCytoplasmNameSpace() {
        return cytoplasmNameSpace;
    }

    public void setCytoplasmNameSpace(String cytoplasmNameSpace) {
        this.cytoplasmNameSpace = cytoplasmNameSpace;
    }
    
    public String getDef() {
        return cytoplasmOntologyDef;
    }

    public void setDef(String def) {
        this.cytoplasmOntologyDef = def;
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
    
    public String getCytoplasmOntologyName() {
        return cytoplasmOntologyName;
    }

    public void setCytoplasmOntologyName(String cytoplasmOntologyName) {
        this.cytoplasmOntologyName = cytoplasmOntologyName;
    }
   
    public String getCytoplasmOntologyDef() {
        return cytoplasmOntologyDef;
    }

    public void setCytoplasmOntologyDef(String cytoplasmOntologyDef) {
        this.cytoplasmOntologyDef = cytoplasmOntologyDef;
    }
    
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + cytoplasmOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CytoplasmOntology other = (CytoplasmOntology) obj;
        if ((this.cytoplasmOntologyId == null) ? (other.cytoplasmOntologyId != null) : !this.cytoplasmOntologyId.equals(other.cytoplasmOntologyId)) {
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
    
     public void setCreationDate(String str) {
        this.creationDate = str;
    }
    
    public String getCreationDate() {
        return creationDate;
    }
    
    public String getCreatedBy() {
        return createdBy;
    }
    
    public void setCreatedBy(String str) {
        createdBy = str;
    }
  

    /**
     * XRef databases are not stored
     */
}
