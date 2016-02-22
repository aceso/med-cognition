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
 *
 * RNA ontology - ribonucleic acid ontology, rnao.obo,  obofoundry
 * MongoColleciton: rnaontology
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.RNA_ONTOLOGY)
@SuppressWarnings("javadoc")
public class RNAOntology {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(RNAOntology.class);

    @GraphId
    private Long id;

    /**
     * "id" : "RNAO:0000002",
     */
    @UniquelyIndexed(indexName=IndexNames.RNA_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.RNA_ONTOLOGY_ID, rbField=BioFields.RNA_ONTOLOGY_ID)
    private String rnaOntologyId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.RNA_ONTOLOGY.toString();

    /**
     *
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    public String message;

    /**
     * <pre>
     * "nameList" : [
		{
			"name" : "deoxyribose ring"
		}
	],
     * </pre>
     */
    @FullTextIndexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;


    @Visual
    @Indexed (indexName=IndexNames.RNA_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.RNA_ONTOLOGY_NAME, rbField=BioFields.RNA_ONTOLOGY_NAME)
    private String rnaOntologyName;

    /**
     * 	"def" : "\"A ribose ring that does not have a hydroxy group at the 2prime position.\" [RNAO:ROC]",
     */
    @FullTextIndexed (indexName=IndexNames.RNA_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.RNA_ONTOLOGY_DEF, rbField=BioFields.RNA_ONTOLOGY_DEF)
    private String rnaOntologyDef;

    /**
     * 	"is_a" : "RNAO:0000147"
     * "relationshipList" : [
		{
			"relationship" : "ro.owl#part_of RNAO:0000021"
		}
	]
     */
    @RelatedTo(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private BioRelation isARelationship;

    /**
     * 	relationship:
     */
    @RelatedToVia(direction=Direction.BOTH, elementClass = BioRelation.class)
    private Collection<BioRelation> relationship;

    /**
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return String
     */
    public String getRNAOntologyId() {
        return rnaOntologyId;
    }

    /**
     *
     * @param rnaOntologyId
     */
    public void setRNAOntologyId(String rnaOntologyId) {
        this.rnaOntologyId = rnaOntologyId;
    }

    /**
     *
     * @return String
     */
    public String getRnaOntologyDef() {
        return rnaOntologyDef;
    }

    /**
     *
     * @param def
     */
    public void setRnaOntologyDef(String def) {
        this.rnaOntologyDef = def;
    }

    /**
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     *
     * @return String
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     *
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     *
     * @return String
     */
    public String getRNAOntologyName() {
        return rnaOntologyName;
    }

    /**
     *
     * @param rnaOntologyName
     */
    public void setRNAOntologyName(String rnaOntologyName) {
        this.rnaOntologyName = rnaOntologyName;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + rnaOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final RNAOntology other = (RNAOntology) obj;
        if ((this.rnaOntologyId == null) ? (other.rnaOntologyId != null) : !this.rnaOntologyId.equals(other.rnaOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }

    /**
     * {@link BioRelTypes}
     * setRelationship
     * @param endNode
     * @param relType
     */
    public void setRelationship(Object endNode, BioRelTypes relType) {
        if (relationship == null) {
            relationship = new HashSet<>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        relationship.add(rel);
    }

    /**
     *
     * @return String
     */
    public Iterable<BioRelation> getRelationship() {
        return relationship;
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

    /**
     *
     * @return BioRelation
     */
    public BioRelation getIsARelationship() {
        return isARelationship;
    }

}
