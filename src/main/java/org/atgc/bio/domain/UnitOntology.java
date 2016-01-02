/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * 
 * Symptom Ontology - gemina_symptom.obo  obofoundry
 * MongoColleciton: symptomontology 
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.SYMPTOM_ONTOLOGY)
public class UnitOntology {
    	
    protected static Log log = LogFactory.getLog(new Object().getClass());
   
    @GraphId
    private Long id;
   
    /**
     * SYMP:0000832
     */
    @UniquelyIndexed (indexName=IndexNames.SYMPTOM_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.SYMPTOM_ONTOLOGY_ID, rbField=BioFields.SYMPTOM_ONTOLOGY_ID)
    private String symptomOntologyId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.SYMPTOM_ONTOLOGY.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @Indexed(indexName=IndexNames.IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.IS_OBSOLETE, rbField=BioFields.IS_OBSOLETE)
    private String isObsolete;
     
    /**
     * name: neuropathic pain
     */
    @FullTextIndexed(indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
    
    
    @Visual
    @Indexed (indexName=IndexNames.SYMPTOM_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.SYMPTOM_ONTOLOGY_NAME, rbField=BioFields.SYMPTOM_ONTOLOGY_NAME)
    private String symptomOntologyName;   
    
    @FullTextIndexed (indexName=IndexNames.COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
    private String comment;
    
    
    
    /**
     * 	<pre>
     * def: "Neuropathic pain is a pain experienced by cancer patients caused by radiotherapy or chemotherapy damage to nerves,
     * when cancer invades or compresses nerves, or when, during cancer treatment. 
     * This type of pain is typically described as burning, electrical, or 
     * strange feeling." [URL:http\://www.nationalpainfoundation.org/MyTreatment/articles/Cancer_PainDefinitions.asp]
     * </pre>
     */
    @FullTextIndexed (indexName=IndexNames.SYMPTOM_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.SYMPTOM_ONTOLOGY_DEF, rbField=BioFields.SYMPTOM_ONTOLOGY_DEF)
    private String symptomOntologyDef;
    
    @FullTextIndexed(indexName=IndexNames.SYMPTOM_ONTOLOGY_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.SYMPTOM_ONTOLOGY_RELATED_SYNONYMS, rbField=BioFields.SYMPTOM_ONTOLOGY_RELATED_SYNONYMS)
    private String symptomOntologyRelatedSynonyms;
    
    @FullTextIndexed(indexName=IndexNames.SYMPTOM_ONTOLOGY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.SYMPTOM_ONTOLOGY_EXACT_SYNONYMS, rbField=BioFields.SYMPTOM_ONTOLOGY_EXACT_SYNONYMS)
    private String symptomOntologyExactSynonyms;
    
    /**
     * applicable only to few
     * created_by: lschriml
     */
    @FullTextIndexed(indexName=IndexNames.CREATED_BY)
    @Taxonomy (rbClass=TaxonomyTypes.CREATED_BY, rbField=BioFields.CREATED_BY)
    private String createdBy;

     @FullTextIndexed(indexName=IndexNames.CREATION_DATE)
     @Taxonomy (rbClass=TaxonomyTypes.CREATION_DATE, rbField=BioFields.CREATION_DATE)
     private String creationDate;
    
    /**
     * <pre>
     * 		"isList" : [
		{
			"is_a" : "SYMP:0000099 ! pain"
		}
	]
     * </pre>
     */
    @RelatedTo(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private BioRelation isARelationship;
    
    /**
     * 	relationship:
     */
    @RelatedToVia(direction=Direction.BOTH, elementClass = BioRelation.class)
    private Collection<BioRelation> relationship;
    
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getSymptomOntologyId() {
        return symptomOntologyId;
    }

    public void setSymptomOntologyId(String symptomOntologyId) {
        this.symptomOntologyId = symptomOntologyId;
    }
    
    public String getSymptomOntologyDef() {
        return symptomOntologyDef;
    }

    public void setSymptomOntologyDef(String def) {
        this.symptomOntologyDef = def;
    }
    
    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }
    
     public String getIsObsolete() {
        return isObsolete;
    }

    public void setIsObsolete(String isObsolete) {
        this.isObsolete = isObsolete;
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
    
    public String getSymptomOntologyName() {
        return symptomOntologyName;
    }

    public void setSymptomOntologyName(String symptomOntologyName) {
        this.symptomOntologyName = symptomOntologyName;
    }
    
    public void setSymptomOntologyRelatedSynonyms(String str) {
        this.symptomOntologyRelatedSynonyms = str;
    }
    
    public String getSymptomOntologyRelatedSynonyms() {
        return symptomOntologyRelatedSynonyms;
    }
    
    public String getSymptomOntologyExactSynonyms() {
        return symptomOntologyExactSynonyms;
    }
    
    public void setSymptomOntologyExactSynonyms(String str) {
        this.symptomOntologyExactSynonyms = str;
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
    
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + symptomOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final UnitOntology other = (UnitOntology) obj;
        if ((this.symptomOntologyId == null) ? (other.symptomOntologyId != null) : !this.symptomOntologyId.equals(other.symptomOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }
  
   /**
    * {@link BioRelTypes#IS_A}
    * @param endNode
    * @param relType 
    */
    public void setIsARelationship(Object endNode, BioRelTypes relType) {
        isARelationship = new BioRelation();
        isARelationship.setEndNode(endNode);
        isARelationship.setStartNode(this);
        isARelationship.setRelType(relType);
    }
    
    public BioRelation getIsARelationship() {
        return isARelationship;
    }
    
}
