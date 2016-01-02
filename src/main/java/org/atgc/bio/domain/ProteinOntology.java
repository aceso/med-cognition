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
 * http://www.celltypeontology.org,  obofoundry.org (CL:
 *
 * It also includes CHEBI (chemical entities of biological interest) - 20 of them
 * http://www.ebi.ac.uk/chebi/
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.PROTEIN_ONTOLOGY)
public class ProteinOntology {

    /**
     *
     */
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed (indexName=IndexNames.PROTEIN_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_ID, rbField=BioFields.PROTEIN_ONTOLOGY_ID)
    private String proteinOntologyId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PROTEIN_ONTOLOGY.toString();

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
    @Indexed (indexName=IndexNames.PROTEIN_ONTOLOGY_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_IS_OBSOLETE, rbField=BioFields.PROTEIN_ONTOLOGY_IS_OBSOLETE)
    private String proteinOntologyIsObsolete;


    /**
     * name" : "immature protein part
     */

    @FullTextIndexed (indexName=IndexNames.PROTEIN_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_NAME, rbField=BioFields.PROTEIN_ONTOLOGY_NAME)
    private String proteinOntologyName;

    @FullTextIndexed (indexName=IndexNames.PROTEIN_ONTOLOGY_NAME_SPACE)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_NAME_SPACE, rbField=BioFields.PROTEIN_ONTOLOGY_NAME_SPACE)
    private String proteinOntologyNameSpace;

    @FullTextIndexed(indexName=IndexNames.PROTEIN_ONTOLOGY_NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_NARROW_SYNONYMS, rbField=BioFields.PROTEIN_ONTOLOGY_NARROW_SYNONYMS)
    private String proteinOntologyNarrowSynonyms;

    @FullTextIndexed(indexName=IndexNames.PROTEIN_ONTOLOGY_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_RELATED_SYNONYMS, rbField=BioFields.PROTEIN_ONTOLOGY_RELATED_SYNONYMS)
    private String proteinOntologyRelatedSynonyms;

    @FullTextIndexed(indexName=IndexNames.PROTEIN_ONTOLOGY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_EXACT_SYNONYMS, rbField=BioFields.PROTEIN_ONTOLOGY_EXACT_SYNONYMS)
    private String proteinOntologyExactSynonyms;


    /**
     * alt_id: CL:0000274
     */
    @Indexed (indexName=IndexNames.PROTEIN_ONTOLOGY_ALT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_ALT_ID, rbField=BioFields.PROTEIN_ONTOLOGY_ALT_ID)
    private String proteinOntologyAltId;

    /**
     * def: "A protein that belongs to the clade D of the bHLH proteins (see PMID:20219281) with a
     * core domain composition consisting of a helix-loop-helix DNA-binding domain (PF00010) (HLH),
     * common to the basic HLH family of transcription factors, but lacking the DNA binding domain
     * to the consensus E box response element (CANNTG). By binding to basic HLH transcription factors,
     * proteins in this class regulate gene expression." [PMID:20219281, PRO:CNA]
     */
    @FullTextIndexed (indexName=IndexNames.PROTEIN_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_DEF, rbField=BioFields.PROTEIN_ONTOLOGY_DEF)
    private String proteinOntologyDef;

    @FullTextIndexed (indexName=IndexNames.PROTEIN_ONTOLOGY_SUBSETS)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_ONTOLOGY_SUBSETS, rbField=BioFields.PROTEIN_ONTOLOGY_SUBSETS)
    private String proteinOntologySubsets;

    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;

     /**
      * BioRelTypes: derives_from, has_part, only_in_taxon, lacks_part, has_modification
      *  MOD - protein modification, NCBITaxon, ProteinOntology (PR)
      * "relationship" : "has_part PR:000028879 ! mediator of RNA polymerase II transcription subunit 15 isoform 1 (mouse)"
      *  relationship: only_in_taxon NCBITaxon:9606 ! Homo sapiens
      *  relationship: has_part MOD:00693 ! glycosylated residue
      *
      */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> proteinOntologyRelationship;

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

    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> disjointRelationship;

    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> considerRelationship;

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
    public String getProteinOntologyId() {
        return proteinOntologyId;
    }

    /**
     *
     * @param proteinOntologyId
     */
    public void setProteinOntologyId(String proteinOntologyId) {
        this.proteinOntologyId = proteinOntologyId;
    }

    /**
     *
     * @return String
     */
    public String getProteinOntologyName() {
        return proteinOntologyName;
    }

    /**
     *
     * @param proteinOntologyName
     */
    public void setProteinOntologyName(String proteinOntologyName) {
        this.proteinOntologyName = proteinOntologyName;
    }

    /**
     *
     * @return String
     */
    public String getProteinOntologyExactSynonyms() {
        return proteinOntologyExactSynonyms;
    }

    /**
     *
     * @param proteinOntologyExactSynonyms
     */
    public void setProteinOntologyExactSynonyms(String proteinOntologyExactSynonyms) {
        this.proteinOntologyExactSynonyms = proteinOntologyExactSynonyms;
    }

    /**
     *
     * @return String
     */
    public String getProteinOntologyNarrowSynonyms() {
        return proteinOntologyNarrowSynonyms;
    }

    /**
     *
     * @param proteinOntologyNarrowSynonyms
     */
    public void setProteinOntologyNarrowSynonyms(String proteinOntologyNarrowSynonyms) {
        this.proteinOntologyNarrowSynonyms = proteinOntologyNarrowSynonyms;
    }

    /**
     *
     * @return String
     */
    public String getProteinOntologyRelatedSynonyms() {
        return proteinOntologyRelatedSynonyms;
    }

    /**
     *
     * @param proteinOntologyRelatedSynonyms
     */
    public void setProteinOntologyRelatedSynonyms(String proteinOntologyRelatedSynonyms) {
        this.proteinOntologyRelatedSynonyms = proteinOntologyRelatedSynonyms;
    }

    /**
     *
     * @return String
     */
    public String getProteinOntologyNameSpace() {
        return proteinOntologyNameSpace;
    }

    /**
     *
     * @param proteinOntologyNameSpace
     */
    public void setProteinOntologyNameSpace(String proteinOntologyNameSpace) {
        this.proteinOntologyNameSpace = proteinOntologyNameSpace;
    }

    /**
     *
     * @return String
     */
    public String getDef() {
        return proteinOntologyDef;
    }

    /**
     *
     * @param def
     */
    public void setDef(String def) {
        this.proteinOntologyDef = def;
    }

    /**
     *
     * @return String
     */
    public String getProteinOntologyAltId() {
        return proteinOntologyAltId;
    }

    /**
     *
     * @param proteinOntologyAltId
     */
    public void setProteinOntologyAltId(String proteinOntologyAltId) {
        this.proteinOntologyAltId = proteinOntologyAltId;
    }

    /**
     *
     * @return String
     */
    public String getProteinOntologySubsets() {
        return proteinOntologySubsets;
    }

    /**
     *
     * @param proteinOntologySubsets
     */
    public void setProteinOntologySubsets(String proteinOntologySubsets) {
        this.proteinOntologySubsets = proteinOntologySubsets;
    }

    /**
     *
     * @return String
     */
    public String getProteinOntologyIsObsolete() {
        return proteinOntologyIsObsolete;
    }

    /**
     *
     * @param isObsolete
     */
    public void setProteinOntologyIsObsolete(String isObsolete) {
        this.proteinOntologyIsObsolete = isObsolete;
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
        return (nodeType + "-" + name + "-" + proteinOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProteinOntology other = (ProteinOntology) obj;
        if ((this.proteinOntologyId == null) ? (other.proteinOntologyId != null) : !this.proteinOntologyId.equals(other.proteinOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }


    /**
     * endNode can be
     *    {@link BioTypes#GENE_ONTOLOGY} {@link BioTypes#PROTEIN_ONTOLOGY}
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
     * {@link BioTypes#PROTEIN_ONTOLOGY} {@link BioTypes#ENZYME}
     * {@link BioTypes#NCBI_TAXONOMY} {@link BioTypes#PROTEIN_MODIFICATION_ONTOLOGY}
     *
     * ProtienModificationOntology: BioRelTypes has_modification, lacks_part, has_part
     *
     * "relationship" : "has_part PR:000028879 ! mediator of RNA polymerase II transcription subunit 15 isoform 1 (mouse)"
     *  relationship: only_in_taxon NCBITaxon:9606 ! Homo sapiens
     * @param endNode
     * @param relType
     */
    public void setProteinOntologyRelationship(Object endNode, BioRelTypes relType) {
        if (proteinOntologyRelationship == null) {
            proteinOntologyRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        proteinOntologyRelationship.add(rel);
    }

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getProteinOntologyRelationship() {
        return proteinOntologyRelationship;
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

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getDisjointRelationship() {
        return disjointRelationship;
    }

    /**
     *
     * @param endNode
     */
    public void setConsiderRelationship(Object endNode) {
        if (considerRelationship == null) {
            considerRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, BioRelTypes.CONSIDER_RELATIONSHIP);
        considerRelationship.add(rel);
    }

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getConsiderRelationship() {
        return considerRelationship;
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
