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
 *  SBO.obo file
 *  systems biology ontology
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.SYSTEMS_BIOLOGY_ONTOLOGY)
public class SystemsBiologyOntology {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(SystemsBiologyOntology.class);

    @GraphId
    private Long id;

    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed (indexName=IndexNames.SYSTEMS_BIOLOGY_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.SYSTEMS_BIOLOGY_ONTOLOGY_ID, rbField=BioFields.SYSTEMS_BIOLOGY_ONTOLOGY_ID)
    private String systemsBiologyOntologyId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.SYSTEMS_BIOLOGY_ONTOLOGY.toString();

    /**
     *
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    public String message;

    @FullTextIndexed(indexName=IndexNames.COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
    private String comment;


    /**
     * set to true
     */
    @Indexed (indexName=IndexNames.SYSTEMS_BIOLOGY_ONTOLOGY_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.SYSTEMS_BIOLOGY_ONTOLOGY_IS_OBSOLETE, rbField=BioFields.SYSTEMS_BIOLOGY_ONTOLOGY_IS_OBSOLETE)
    private String systemsBiologyOntologyIsObsolete;


    /**
     * N6-(L-isoaspartyl)-L-lysine
     */
    @FullTextIndexed (indexName=IndexNames.SYSTEMS_BIOLOGY_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.SYSTEMS_BIOLOGY_ONTOLOGY_NAME, rbField=BioFields.SYSTEMS_BIOLOGY_ONTOLOGY_NAME)
    private String systemsBiologyOntologyName;


    /**
     * "synonym" : "\"Ile\" EXACT PSI-MOD-label []"
     */
    @FullTextIndexed(indexName=IndexNames.SYSTEMS_BIOLOGY_ONTOLOGY_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.SYSTEMS_BIOLOGY_ONTOLOGY_SYNONYMS, rbField=BioFields.SYSTEMS_BIOLOGY_ONTOLOGY_SYNONYMS)
    private String systemsBiologyOntologySynonyms;

    /**
     * def: "A protein that belongs to the clade D of the bHLH proteins (see PMID:20219281) with a
     * core domain composition consisting of a helix-loop-helix DNA-binding domain (PF00010) (HLH),
     * common to the basic HLH family of transcription factors, but lacking the DNA binding domain
     * to the consensus E box response element (CANNTG). By binding to basic HLH transcription factors,
     * proteins in this class regulate gene expression." [PMID:20219281, PRO:CNA]
     */
    @FullTextIndexed (indexName=IndexNames.SYSTEMS_BIOLOGY_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.SYSTEMS_BIOLOGY_ONTOLOGY_DEF, rbField=BioFields.SYSTEMS_BIOLOGY_ONTOLOGY_DEF)
    private String systemsBiologyOntologyDef;


    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;


    /**
     * BioRelTypes = is_a
     * "is_a" : "MOD:00306 ! residues isobaric at 113.084064 Da"
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> isARelationship;

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
    public String getSystemsBiologyOntologyId() {
        return systemsBiologyOntologyId;
    }

    /**
     *
     * @param proteinOntologyId
     */
    public void setSystemsBiologyOntologyId(String proteinOntologyId) {
        this.systemsBiologyOntologyId = proteinOntologyId;
    }

    /**
     *
     * @return String
     */
    public String getSystemsBiologyOntologyName() {
        return systemsBiologyOntologyName;
    }

    /**
     *
     * @param name
     */
    public void setSystemsBiologyOntologyName(String name) {
        this.systemsBiologyOntologyName = name;
    }

    /**
     *
     * @return String
     */
    public String getSystemsBiologyOntologySynonyms() {
        return systemsBiologyOntologySynonyms;
    }

    /**
     *
     * @param syn
     */
    public void setSystemsBiologyOntologySynonyms(String syn) {
        this.systemsBiologyOntologySynonyms = syn;
    }

    /**
     *
     * @return String
     */
    public String getDef() {
        return systemsBiologyOntologyDef;
    }

    /**
     *
     * @param def
     */
    public void setDef(String def) {
        this.systemsBiologyOntologyDef = def;
    }

    /**
     *
     * @return String
     */
    public String getSystemsBiologyOntologyIsObsolete() {
        return systemsBiologyOntologyIsObsolete;
    }

    /**
     *
     * @param isObsolete
     */
    public void setSystemsBiologyOntologyIsObsolete(String isObsolete) {
        this.systemsBiologyOntologyIsObsolete = isObsolete;
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
        return (nodeType + "-" + name + "-" + systemsBiologyOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SystemsBiologyOntology other = (SystemsBiologyOntology) obj;
        if ((this.systemsBiologyOntologyId == null) ? (other.systemsBiologyOntologyId != null) : !this.systemsBiologyOntologyId.equals(other.systemsBiologyOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }


    /**
     * endNode is
     *    {@link BioTypes#SYSTEMS_BIOLOGY_ONTOLOGY} (SBO)
     *
     * is_a: SBO:0000240 ! material entity
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
     * @return String
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

}
