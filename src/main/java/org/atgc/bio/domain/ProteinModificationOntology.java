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
 *  PSI-MOD.obo file
 *  protein modification ontology
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.PROTEIN_MODIFICATION_ONTOLOGY)
public class ProteinModificationOntology {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(ProteinModificationOntology.class);

    @GraphId
    private Long id;

    /**
     * "id" : "CL:0000002"
     */
    @UniquelyIndexed(indexName=IndexNames.PROTEIN_MOD_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.PROTEIN_MOD_ONTOLOGY_ID, rbField=BioFields.PROTEIN_MOD_ONTOLOGY_ID)
    private String proteinModOntologyId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PROTEIN_MODIFICATION_ONTOLOGY.toString();

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
    @Indexed (indexName=IndexNames.PROTEIN_MOD_ONTOLOGY_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_MOD_ONTOLOGY_IS_OBSOLETE, rbField=BioFields.PROTEIN_MOD_ONTOLOGY_IS_OBSOLETE)
    private String proteinModOntologyIsObsolete;


    /**
     * N6-(L-isoaspartyl)-L-lysine
     */
    @FullTextIndexed (indexName=IndexNames.PROTEIN_MOD_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_MOD_ONTOLOGY_NAME, rbField=BioFields.PROTEIN_MOD_ONTOLOGY_NAME)
    private String proteinModOntologyName;

    /**
     * synonym: "Oxidation" RELATED PSI-MS-label []
     */
    @FullTextIndexed(indexName=IndexNames.PROTEIN_MOD_ONTOLOGY_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_MOD_ONTOLOGY_RELATED_SYNONYMS, rbField=BioFields.PROTEIN_MOD_ONTOLOGY_RELATED_SYNONYMS)
    private String proteinModOntologyRelatedSynonyms;

    /**
     * "synonym" : "\"Ile\" EXACT PSI-MOD-label []"
     */
    @FullTextIndexed(indexName=IndexNames.PROTEIN_MOD_ONTOLOGY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_MOD_ONTOLOGY_EXACT_SYNONYMS, rbField=BioFields.PROTEIN_MOD_ONTOLOGY_EXACT_SYNONYMS)
    private String proteinModOntologyExactSynonyms;

   /**
    * subset
    * "subset" : "PSI-MOD-slim",
    */
    @FullTextIndexed (indexName=IndexNames.PROTEIN_MOD_ONTOLOGY_SUBSETS)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_MOD_ONTOLOGY_SUBSETS, rbField=BioFields.PROTEIN_MOD_ONTOLOGY_SUBSETS)
    private String proteinModOntologySubsets;

    /**
     * alt_id: CL:0000274
     */
    @Indexed (indexName=IndexNames.PROTEIN_MOD_ONTOLOGY_ALT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_MOD_ONTOLOGY_ALT_ID, rbField=BioFields.PROTEIN_MOD_ONTOLOGY_ALT_ID)
    private String proteinModOntologyAltId;

    /**
     * def: "A protein that belongs to the clade D of the bHLH proteins (see PMID:20219281) with a
     * core domain composition consisting of a helix-loop-helix DNA-binding domain (PF00010) (HLH),
     * common to the basic HLH family of transcription factors, but lacking the DNA binding domain
     * to the consensus E box response element (CANNTG). By binding to basic HLH transcription factors,
     * proteins in this class regulate gene expression." [PMID:20219281, PRO:CNA]
     */
    @FullTextIndexed (indexName=IndexNames.PROTEIN_MOD_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_MOD_ONTOLOGY_DEF, rbField=BioFields.PROTEIN_MOD_ONTOLOGY_DEF)
    private String proteinModOntologyDef;

    @Indexed (indexName=IndexNames.DIFF_AVG)
    @Taxonomy (rbClass=TaxonomyTypes.DIFF_AVG, rbField=BioFields.DIFF_AVG)
    private String diffAvg;

    @Indexed (indexName=IndexNames.DIFF_FORMULA)
    @Taxonomy (rbClass=TaxonomyTypes.DIFF_FORMULA, rbField=BioFields.DIFF_FORMULA)
    private String diffFormula;

    @Indexed (indexName=IndexNames.DIFF_MONO)
    @Taxonomy (rbClass=TaxonomyTypes.DIFF_MONO, rbField=BioFields.DIFF_MONO)
    private String diffMono;

    @Indexed (indexName=IndexNames.FORMULA)
    @Taxonomy (rbClass=TaxonomyTypes.FORMULA, rbField=BioFields.FORMULA)
    private String formula;

    @Indexed (indexName=IndexNames.MASSAVG)
    @Taxonomy (rbClass=TaxonomyTypes.MASSAVG, rbField=BioFields.MASSAVG)
    private String massAvg;

    @Indexed (indexName=IndexNames.MASSMONO)
    @Taxonomy (rbClass=TaxonomyTypes.MASSMONO, rbField=BioFields.MASSMONO)
    private String massMono;

    @Indexed (indexName=IndexNames.ORIGIN)
    @Taxonomy (rbClass=TaxonomyTypes.ORIGIN, rbField=BioFields.ORIGIN)
    private String origin;

    @Indexed (indexName=IndexNames.SOURCE)
    @Taxonomy (rbClass=TaxonomyTypes.SOURCE, rbField=BioFields.SOURCE)
    private String source;

    @Indexed (indexName=IndexNames.TERMSPEC)
    @Taxonomy (rbClass=TaxonomyTypes.TERMSPEC, rbField=BioFields.TERMSPEC)
    private String termspec;

    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;

     /**
      *
      */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> relationship;

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
    public String getProteinModOntologyId() {
        return proteinModOntologyId;
    }

    /**
     *
     * @param proteinOntologyId
     */
    public void setProteinModOntologyId(String proteinOntologyId) {
        this.proteinModOntologyId = proteinOntologyId;
    }

    /**
     *
     * @return String
     */
    public String getProteinModOntologyName() {
        return proteinModOntologyName;
    }

    /**
     *
     * @param name
     */
    public void setProteinModOntologyName(String name) {
        this.proteinModOntologyName = name;
    }

    /**
     *
     * @return String
     */
    public String getProteinModOntologyExactSynonyms() {
        return proteinModOntologyExactSynonyms;
    }

    /**
     *
     * @param syn
     */
    public void setProteinModOntologyExactSynonyms(String syn) {
        this.proteinModOntologyExactSynonyms = syn;
    }

    /**
     *
     * @return String
     */
    public String getProteinModOntologyRelatedSynonyms() {
        return proteinModOntologyRelatedSynonyms;
    }

    /**
     *
     * @param syn
     */
    public void setProteinModOntologyRelatedSynonyms(String syn) {
        this.proteinModOntologyRelatedSynonyms = syn;
    }

    /**
     *
     * @return String
     */
    public String getDef() {
        return proteinModOntologyDef;
    }

    /**
     *
     * @param def
     */
    public void setDef(String def) {
        this.proteinModOntologyDef = def;
    }

    /**
     *
     * @return String
     */
    public String getProteinModOntologyAltId() {
        return proteinModOntologyAltId;
    }

    /**
     *
     * @param proteinModOntologyAltId
     */
    public void setProteinModOntologyAltId(String proteinModOntologyAltId) {
        this.proteinModOntologyAltId = proteinModOntologyAltId;
    }

    /**
     *
     * @return String
     */
    public String getProteinModOntologySubsets() {
        return proteinModOntologySubsets;
    }

    /**
     *
     * @param proteinModOntologySubsets
     */
    public void setProteinModOntologySubsets(String proteinModOntologySubsets) {
        this.proteinModOntologySubsets = proteinModOntologySubsets;
    }

    /**
     *
     * @return String
     */
    public String getProteinModOntologyIsObsolete() {
        return proteinModOntologyIsObsolete;
    }

    /**
     *
     * @param isObsolete
     */
    public void setProteinModOntologyIsObsolete(String isObsolete) {
        this.proteinModOntologyIsObsolete = isObsolete;
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
        return (nodeType + "-" + name + "-" + proteinModOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProteinModificationOntology other = (ProteinModificationOntology) obj;
        if ((this.proteinModOntologyId == null) ? (other.proteinModOntologyId != null) : !this.proteinModOntologyId.equals(other.proteinModOntologyId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }


    /**
     * endNode is
     *    {@link BioTypes#PROTEIN_MODIFICATION} (MOD)
     *
     * is_a: MOD:00033 ! crosslinked residues
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
     * MOD is connected through relationship
     * {@link BioTypes#PROTEIN_MODIFICATION_ONTOLOGY}
     * derives_from,  has_functional_parent, contains
     * @param endNode
     * @param relType
     */
    public void setRelationship(Object endNode, BioRelTypes relType) {
        if (relationship == null) {
            relationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        relationship.add(rel);
    }

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getRelationship() {
        return this.relationship;
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

    /**
     *
     * @return String
     */
    public String getDiffAvg() {
        return diffAvg;
    }

    /**
     *
     * @param diffAvg
     */
    public void setDiffAvg(String diffAvg) {
        this.diffAvg = diffAvg;
    }

    /**
     *
     * @return String
     */
    public String getDiffFormula() {
        return diffFormula;
    }

    /**
     *
     * @param diffFormula
     */
    public void setDiffFormula(String diffFormula) {
        this.diffFormula = diffFormula;
    }

    /**
     *
     * @return String
     */
    public String getDiffMono() {
        return diffMono;
    }

    /**
     *
     * @param diffMono
     */
    public void setDiffMono(String diffMono) {
        this.diffMono = diffMono;
    }

    /**
     *
     * @return String
     */
    public String getFormula() {
        return formula;
    }

    /**
     *
     * @param formula
     */
    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     *
     * @return String
     */
    public String getMassAvg() {
        return massAvg;
    }

    /**
     *
     * @param massAvg
     */
    public void setMassAvg(String massAvg) {
        this.massAvg = massAvg;
    }

    /**
     *
     * @return String
     */
    public String getMassMono() {
        return massMono;
    }

    /**
     *
     * @param massMono
     */
    public void setMassMono(String massMono) {
        this.massMono = massMono;
    }

    /**
     *
     * @return String
     */
    public String getOrigin() {
        return origin;
    }

    /**
     *
     * @param origin
     */
    public void setOrigin(String origin) {
        this.origin = origin;
    }

    /**
     *
     * @return String
     */
    public String getSource() {
        return source;
    }

    /**
     *
     * @param source
     */
    public void setSource(String source) {
        this.source = source;
    }

    /**
     *
     * @return String
     */
    public String getTermspec() {
        return termspec;
    }

    /**
     *
     * @param termspec
     */
    public void setTermspec(String termspec) {
        this.termspec = termspec;
    }

}
