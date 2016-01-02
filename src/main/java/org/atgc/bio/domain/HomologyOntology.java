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
 * Homology ontology- homology ontology obofoundry
 * 
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.HOMOLOGY_ONTOLOGY)
public class HomologyOntology {
    	
    protected static Log log = LogFactory.getLog(new Object().getClass());
   
    @GraphId
    private Long id;
   
    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed(indexName=IndexNames.HOMOLOGY_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.HOMOLOGY_ONTOLOGY_ID, rbField=BioFields.HOMOLOGY_ONTOLOGY_ID)
    private String homologyOntologyId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.HOMOLOGY_ONTOLOGY.toString();
   
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
     
    @Indexed (indexName=IndexNames.HOMOLOGY_ONTOLOGY_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.HOMOLOGY_ONTOLOGY_IS_OBSOLETE, rbField=BioFields.HOMOLOGY_ONTOLOGY_IS_OBSOLETE)
    private String homologyOntologyIsObsolete;
  
   
    @FullTextIndexed(indexName=IndexNames.HOMOLOGY_ONTOLOGY_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.HOMOLOGY_ONTOLOGY_RELATED_SYNONYMS, rbField=BioFields.HOMOLOGY_ONTOLOGY_RELATED_SYNONYMS)
    private String homologyOntologyRelatedSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.HOMOLOGY_ONTOLOGY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.HOMOLOGY_ONTOLOGY_EXACT_SYNONYMS, rbField=BioFields.HOMOLOGY_ONTOLOGY_EXACT_SYNONYMS)
    private String homologyOntologyExactSynonyms;
    
    @Visual
    @Indexed (indexName=IndexNames.HOMOLOGY_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.HOMOLOGY_ONTOLOGY_NAME, rbField=BioFields.HOMOLOGY_ONTOLOGY_NAME)
    private String homologyOntologyName;    
    
    @FullTextIndexed (indexName=IndexNames.HOMOLOGY_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.HOMOLOGY_ONTOLOGY_DEF, rbField=BioFields.HOMOLOGY_ONTOLOGY_DEF)
    private String homologyOntologyDef;
    
    /**
     * 
     * "isList" : [
		{
			"is_a" : "HOM:0000066 ! iterative homology"
		}
	]
}       }
     *
     * BioRelTypes: is_a:
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> isARelationship;
    
    /**
     * 	"disjoint_from" : "HOM:0000029 ! paedomorphorsis"
     */
    @RelatedToVia(direction=Direction.BOTH, elementClass = BioRelation.class)
    private Collection<BioRelation> disjointRelationship;
    
            
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getHomologyOntologyId() {
        return homologyOntologyId;
    }

    public void setHomologyOntologyId(String homologyOntologyId) {
        this.homologyOntologyId = homologyOntologyId;
    }

    public String getHomologyOntologyExactSynonyms() {
        return homologyOntologyExactSynonyms;
    }

    public void setHomologyOntologyExactSynonyms(String homologyExactSynonyms) {
        this.homologyOntologyExactSynonyms = homologyExactSynonyms;
    }

    public String getHomologyOntologyRelatedSynonyms() {
        return homologyOntologyRelatedSynonyms;
    }

    public void setHomologyOntologyRelatedSynonyms(String homologyRelatedSynonyms) {
        this.homologyOntologyRelatedSynonyms = homologyRelatedSynonyms;
    }
    
    public String getDef() {
        return homologyOntologyDef;
    }

    public void setDef(String def) {
        this.homologyOntologyDef = def;
    }
    
    public void setHomologyOntologyIsObsolete(String str) {
        this.homologyOntologyIsObsolete = str;
    }
    
    public String getHomologyOntologyIsObsolete() {
        return this.homologyOntologyIsObsolete;
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
    
    public String getHomologyOntologyName() {
        return homologyOntologyName;
    }

    public void setHomologyOntologyName(String homologyOntologyName) {
        this.homologyOntologyName = homologyOntologyName;
    }
   
   
    public String getHomologyOntologyDef() {
        return homologyOntologyDef;
    }

    public void setHomologyOntologyDef(String homologyOntologyDef) {
        this.homologyOntologyDef = homologyOntologyDef;
    }
    
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + homologyOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final HomologyOntology other = (HomologyOntology) obj;
        if ((this.homologyOntologyId == null) ? (other.homologyOntologyId != null) : !this.homologyOntologyId.equals(other.homologyOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }
  
    /**
     * {@link BioRelTypes#DISJOINT_FROM}
     * setDisjointRelationship
     * @param endNode 
     * @param relType 
     */
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
