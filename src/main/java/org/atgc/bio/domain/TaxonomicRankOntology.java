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
 *  obofoundry.org
 *  taxonomic_rank.obo
 *  taxonomy rank ontology
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.TAXONOMIC_RANK_ONTOLOGY)
public class TaxonomicRankOntology {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(TaxonomicRankOntology.class);

    @GraphId
    private Long id;

    /**
     * "id" : "TAXRANK:0000019",
     */
    @UniquelyIndexed(indexName=IndexNames.TAXONOMIC_RANK_ONTOLOGY_ID)
    @Taxonomy (rbClass=TaxonomyTypes.TAXONOMIC_RANK_ONTOLOGY_ID, rbField=BioFields.TAXONOMIC_RANK_ONTOLOGY_ID)
    private String taxonomicRankOntologyId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.TAXONOMIC_RANK_ONTOLOGY.toString();

    /**
     *
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    public String message;

    /**
     * 	"name" : "infraclass",
     */
    @FullTextIndexed (indexName=IndexNames.TAXONOMIC_RANK_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.TAXONOMIC_RANK_ONTOLOGY_NAME, rbField=BioFields.TAXONOMIC_RANK_ONTOLOGY_NAME)
    private String taxonomicRankOntologyName;


    /**
     * "synonym" : "\"variety\" EXACT []",
     */
    @FullTextIndexed(indexName=IndexNames.TAXONOMIC_RANK_ONTOLOGY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.TAXONOMIC_RANK_ONTOLOGY_EXACT_SYNONYMS, rbField=BioFields.TAXONOMIC_RANK_ONTOLOGY_EXACT_SYNONYMS)
    private String taxonomicRankOntologyExactSynonyms;


    /**
     * def: "A level of depth of a taxon in a taxonomic hierarchy." [TAXRANK:curator]
     */
    @FullTextIndexed (indexName=IndexNames.TAXONOMIC_RANK_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.TAXONOMIC_RANK_ONTOLOGY_DEF, rbField=BioFields.TAXONOMIC_RANK_ONTOLOGY_DEF)
    private String taxonomicRankOntologyDef;


    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;

    /**
     * 	"is_a" : "TAXRANK:0000000 ! taxonomic_rank",
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> isARelationship;

    @RelatedToVia(direction=Direction.BOTH, elementClass=BioRelation.class)
    private Collection<BioRelation> xRefRelationship;

    /**
     *
     * @return String
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
    public String getTaxonomicRankOntologyId() {
        return taxonomicRankOntologyId;
    }

    /**
     *
     * @param OntologyId
     */
    public void setTaxonomicRankOntologyId(String OntologyId) {
        this.taxonomicRankOntologyId = OntologyId;
    }

    /**
     *
     * @return String
     */
    public String getTaxonomicRankOntologyName() {
        return taxonomicRankOntologyName;
    }

    /**
     *
     * @param name
     */
    public void setTaxonomicRankOntologyName(String name) {
        this.taxonomicRankOntologyName = name;
    }

    /**
     *
     * @return String
     */
    public String getTaxonomicRankOntologyExactSynonyms() {
        return taxonomicRankOntologyExactSynonyms;
    }

    /**
     *
     * @param syn
     */
    public void setTaxonomicRankOntologyExactSynonyms(String syn) {
        this.taxonomicRankOntologyExactSynonyms = syn;
    }

    /**
     *
     * @return String
     */
    public String getTaxonomicRankOntologyDef() {
        return taxonomicRankOntologyDef;
    }

    /**
     *
     * @param def
     */
    public void setTaxonomicRankOntologyDef(String def) {
        this.taxonomicRankOntologyDef = def;
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

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + taxonomicRankOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final TaxonomicRankOntology other = (TaxonomicRankOntology) obj;
        if ((this.taxonomicRankOntologyId == null) ? (other.taxonomicRankOntologyId != null) : !this.taxonomicRankOntologyId.equals(other.taxonomicRankOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }


    /**
     * endNode is
     *
     * 	"is_a" : "TAXRANK:0000000 ! taxonomic_rank",
     *
     * @param endNode
     */
    public void setIsARelationship(Object endNode) {
        if (isARelationship == null) {
            isARelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, BioRelTypes.IS_A);
        isARelationship.add(rel);
    }

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getIsARelationship() {
        return isARelationship;
    }

    /**
     *
     * @param endNode
     */
    public void setXRefRelationship(Object endNode) {
        if (xRefRelationship == null) {
            xRefRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, BioRelTypes.XREF);
        xRefRelationship.add(rel);
    }

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getXRefRelationship() {
        return xRefRelationship;
    }

}
