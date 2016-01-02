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
 * spatial.obo,   obofoundry.org
 *
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.SPATIAL_ONTOLOGY)
public class SpatialOntology {

    /**
     *
     */
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed (indexName=IndexNames.SPATIAL_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_ID, rbField=BioFields.SPATIAL_ONTOLOGY_ID)
    private String spatialOntologyId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.SPATIAL_ONTOLOGY.toString();

    /**
     *
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    public String message;

    @FullTextIndexed (indexName=IndexNames.COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
    private String comment;

    /**
     * set to true
     */
    @Indexed (indexName=IndexNames.SPATIAL_ONTOLOGY_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_IS_OBSOLETE, rbField=BioFields.SPATIAL_ONTOLOGY_IS_OBSOLETE)
    private String spatialOntologyIsObsolete;

    @Indexed (indexName=IndexNames.SPATIAL_ONTOLOGY_IS_TRANSITIVE)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_IS_TRANSITIVE, rbField=BioFields.SPATIAL_ONTOLOGY_IS_TRANSITIVE)
    private String spatialOntologyIsTransitive;

     @Indexed (indexName=IndexNames.SPATIAL_ONTOLOGY_IS_SYMMETRIC)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_IS_SYMMETRIC, rbField=BioFields.SPATIAL_ONTOLOGY_IS_SYMMETRIC)
    private String spatialOntologyIsSymmertric;

    /**
     * name" : "immature protein part
     */
    @FullTextIndexed (indexName=IndexNames.SPATIAL_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_NAME, rbField=BioFields.SPATIAL_ONTOLOGY_NAME)
    private String spatialOntologyName;


    @FullTextIndexed(indexName=IndexNames.SPATIAL_ONTOLOGY_NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_NARROW_SYNONYMS, rbField=BioFields.SPATIAL_ONTOLOGY_NARROW_SYNONYMS)
    private String spatialOntologyNarrowSynonyms;

    @FullTextIndexed(indexName=IndexNames.SPATIAL_ONTOLOGY_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_RELATED_SYNONYMS, rbField=BioFields.SPATIAL_ONTOLOGY_RELATED_SYNONYMS)
    private String spatialOntologyRelatedSynonyms;

    @FullTextIndexed(indexName=IndexNames.SPATIAL_ONTOLOGY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_EXACT_SYNONYMS, rbField=BioFields.SPATIAL_ONTOLOGY_EXACT_SYNONYMS)
    private String spatialOntologyExactSynonyms;


    /**
     * alt_id: CL:0000274
     */
    @Indexed (indexName=IndexNames.SPATIAL_ONTOLOGY_ALT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_ALT_ID, rbField=BioFields.SPATIAL_ONTOLOGY_ALT_ID)
    private String spatialOntologyAltId;

    /**
     * def: "A protein that belongs to the clade D of the bHLH proteins (see PMID:20219281) with a
     * core domain composition consisting of a helix-loop-helix DNA-binding domain (PF00010) (HLH),
     * common to the basic HLH family of transcription factors, but lacking the DNA binding domain
     * to the consensus E box response element (CANNTG). By binding to basic HLH transcription factors,
     * proteins in this class regulate gene expression." [PMID:20219281, PRO:CNA]
     */
    @FullTextIndexed (indexName=IndexNames.SPATIAL_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.SPATIAL_ONTOLOGY_DEF, rbField=BioFields.SPATIAL_ONTOLOGY_DEF)
    private String spatialOntologyDef;


    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;

     /**
      * BioRelTypes: derives_from, has_part, only_in_taxon, lacks_part, has_modification
      *  MOD - protein modification, NCBITaxon, SpatialOntology (PR)
      * "relationship" : "has_part PR:000028879 ! mediator of RNA polymerase II transcription subunit 15 isoform 1 (mouse)"
      *  relationship: only_in_taxon NCBITaxon:9606 ! Homo sapiens
      *  relationship: has_part MOD:00693 ! glycosylated residue
      *
      */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> spatialOntologyRelationship;

     /*
      * BioRelTypes: BioRelTypes: derives_from, has_part, only_in_taxon, lacks_part, has_modification
      * "intersection_of" : "PR:000025776 ! CD3 epsilon isoform 1, signal peptide removed phosphorylated 1"
      * "intersection_of" : "only_in_taxon NCBITaxon:9606 ! Homo sapiens"
      *  intersection_of: has_part MOD:00693 ! glycosylated residue
      */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> intersectionRelationship;

    /**
     * BioRelTypes = is_a
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> isARelationship;

    /**
     *  <pre>
        inverse_of: BSPO:0000099 ! posterior_to
        inverse_of: BSPO:0000100 ! proximal_to
        inverse_of: BSPO:0000102 ! ventral_to
        inverse_of: BSPO:0000109 ! surrounded_by
        inverse_of: BSPO:0000108 ! superficial_to
        inverse_of_on_instance_level: BSPO:0000111 ! right_of
        inverse_of_on_instance_level: BSPO:0000121 ! right_side_of
        inverse_of: part_of ! part_of
     * </pre>
     *
     */
    @RelatedTo(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private BioRelation inverseRelationship;

    @RelatedTo(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private BioRelation inverseInstanceRelationship;

    /**
     * <pre>
     * range: BSPO:0000010 ! anatomical axis
        range: BSPO:0000010 ! anatomical axis
        range: BSPO:0000010 ! anatomical axis
        range: CARO:0000000 ! anatomical entity
        range: CARO:0000000 ! anatomical entity
        range: BSPO:0000400 ! anatomical section
        range: BSPO:0000010 ! anatomical axis
     * </pre>
     *
     */
     @RelatedTo(direction=Direction.OUTGOING, elementClass=BioRelation.class)
     private BioRelation rangeRelationship;


     /**
      * <pre>
      * domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000054 ! anatomical side
        domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000010 ! anatomical axis
        domain: BSPO:0000054 ! anatomical side
        domain: BSPO:0000005 ! anatomical surface
      * </pre>
      */
     @RelatedTo(direction=Direction.OUTGOING, elementClass=BioRelation.class)
     private BioRelation domainRelationship;

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
    public String getSpatialOntologyId() {
        return spatialOntologyId;
    }

    /**
     *
     * @param spatialOntologyId
     */
    public void setSpatialOntologyId(String spatialOntologyId) {
        this.spatialOntologyId = spatialOntologyId;
    }

    /**
     *
     * @return String
     */
    public String getSpatialOntologyName() {
        return spatialOntologyName;
    }

    /**
     *
     * @param spatialOntologyName
     */
    public void setSpatialOntologyName(String spatialOntologyName) {
        this.spatialOntologyName = spatialOntologyName;
    }

    /**
     *
     * @return String
     */
    public String getSpatialOntologyExactSynonyms() {
        return spatialOntologyExactSynonyms;
    }

    /**
     *
     * @param spatialOntologyExactSynonyms
     */
    public void setSpatialOntologyExactSynonyms(String spatialOntologyExactSynonyms) {
        this.spatialOntologyExactSynonyms = spatialOntologyExactSynonyms;
    }

    /**
     *
     * @return String
     */
    public String getSpatialOntologyNarrowSynonyms() {
        return spatialOntologyNarrowSynonyms;
    }

    /**
     *
     * @param spatialOntologyNarrowSynonyms
     */
    public void setSpatialOntologyNarrowSynonyms(String spatialOntologyNarrowSynonyms) {
        this.spatialOntologyNarrowSynonyms = spatialOntologyNarrowSynonyms;
    }

    /**
     *
     * @return String
     */
    public String getSpatialOntologyRelatedSynonyms() {
        return spatialOntologyRelatedSynonyms;
    }

    /**
     *
     * @param spatialOntologyRelatedSynonyms
     */
    public void setSpatialOntologyRelatedSynonyms(String spatialOntologyRelatedSynonyms) {
        this.spatialOntologyRelatedSynonyms = spatialOntologyRelatedSynonyms;
    }

    /**
     *
     * @return String
     */
    public String getDef() {
        return spatialOntologyDef;
    }

    /**
     *
     * @param def
     */
    public void setDef(String def) {
        this.spatialOntologyDef = def;
    }

    /**
     *
     * @return String
     */
    public String getSpatialOntologyAltId() {
        return spatialOntologyAltId;
    }

    /**
     *
     * @param spatialOntologyAltId
     */
    public void setSpatialOntologyAltId(String spatialOntologyAltId) {
        this.spatialOntologyAltId = spatialOntologyAltId;
    }

    /**
     *
     * @return String
     */
    public String getSpatialOntologyIsTransitive() {
        return spatialOntologyIsTransitive;
    }

    /**
     *
     * @param str
     */
    public void setSpatialOntologyIsTransitive(String str) {
        this.spatialOntologyIsTransitive = str;
    }

    /**
     *
     * @return String
     */
    public String getSpatialOntologyIsSymmetric() {
        return spatialOntologyIsSymmertric;
    }

    /**
     *
     * @param str
     */
    public void setSpatialOntologyIsSymmetric(String str) {
        this.spatialOntologyIsSymmertric = str;
    }

    /**
     *
     * @return BioRelation
     */
    public BioRelation getDomainRelationship() {
        return domainRelationship;
    }

    /**
     *
     * @param endNode
     */
    public void setDomainRelationship(Object endNode) {
        domainRelationship = new BioRelation();
        domainRelationship.setEndNode(endNode);
        domainRelationship.setStartNode(this);
        domainRelationship.setRelType(BioRelTypes.DOMAIN);
    }

    /**
     *
     * @return BioRelation
     */
    public BioRelation getRangeRelationsihip() {
        return rangeRelationship;
    }

    /**
     *
     * @param endNode
     */
    public void setRangeRelationship(Object endNode) {
        rangeRelationship = new BioRelation();
        rangeRelationship.setEndNode(endNode);
        rangeRelationship.setStartNode(this);
        rangeRelationship.setRelType(BioRelTypes.RANGE);
    }

    /**
     *
     * @param endEntity
     * @param relType
     */
    public void setInverseRelationship(Object endEntity, BioRelTypes relType) {
        inverseRelationship = new BioRelation();
        inverseRelationship.setEndNode(endEntity);
        inverseRelationship.setStartNode(this);
        inverseRelationship.setRelType(relType);
    }

    /**
     *
     * @return BioRelation
     */
    public BioRelation getInverseRelationship() {
        return inverseRelationship;
    }

    /**
     *
     * @param endEntity
     * @param relType
     */
    public void setInverseInstanceRelationship(Object endEntity, BioRelTypes relType) {
        inverseInstanceRelationship = new BioRelation();
        inverseInstanceRelationship.setEndNode(endEntity);
        inverseInstanceRelationship.setStartNode(this);
        inverseInstanceRelationship.setRelType(relType);

    }

    /**
     *
     * @return BioRelation
     */
    public BioRelation getInverseInstanceRelationship() {
        return inverseInstanceRelationship;
    }

    /**
     *
     * @return String
     */
    public String getSpatialOntologyIsObsolete() {
        return spatialOntologyIsObsolete;
    }

    /**
     *
     * @param isObsolete
     */
    public void setSpatialOntologyIsObsolete(String isObsolete) {
        this.spatialOntologyIsObsolete = isObsolete;
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
        return (nodeType + "-" + name + "-" + spatialOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SpatialOntology other = (SpatialOntology) obj;
        if ((this.spatialOntologyId == null) ? (other.spatialOntologyId != null) : !this.spatialOntologyId.equals(other.spatialOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }


    /**
     * endNode can be
     *    {@link BioTypes#GENE_ONTOLOGY} {@link BioTypes#SPATIAL_ONTOLOGY}
     *    {@link BioTypes#PROTEIN_MODIFICATION} (MOD)
     *
     * is_a: MOD:00033 ! crosslinked residues
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

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getIsARelationship() {
        return isARelationship;
    }

    /**
     * All entities connected with relationship
     * {@link BioTypes#SPATIAL_ONTOLOGY} {@link BioTypes#ENZYME}
     * {@link BioTypes#NCBI_TAXONOMY} {@link BioTypes#PROTEIN_MODIFICATION_ONTOLOGY}
     *
     * ProtienModificationOntology: BioRelTypes has_modification, lacks_part, has_part
     *
     * "relationship" : "has_part PR:000028879 ! mediator of RNA polymerase II transcription subunit 15 isoform 1 (mouse)"
     *  relationship: only_in_taxon NCBITaxon:9606 ! Homo sapiens
     * @param endNode
     * @param relType
     */
    public void setSpatialOntologyRelationship(Object endNode, BioRelTypes relType) {
        if (spatialOntologyRelationship == null) {
            spatialOntologyRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        spatialOntologyRelationship.add(rel);
    }

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getSpatialOntologyRelationship() {
        return spatialOntologyRelationship;
    }

    /*
     * "intersection_of" : "PR:000025776 ! CD3 epsilon isoform 1, signal peptide removed phosphorylated 1"
     * "intersection_of" : "only_in_taxon NCBITaxon:9606 ! Homo sapiens"
     *  intersection_of: has_part MOD:00693 ! glycosylated residue
     *
     */

    /**
     *
     * @param endNode
     * @param relType
     */

    public void setIntersectionRelationship(Object endNode, BioRelTypes relType) {
        if (intersectionRelationship == null) {
            intersectionRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        intersectionRelationship.add(rel);
    }

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getIntersectionRelationship() {
        return intersectionRelationship;
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
