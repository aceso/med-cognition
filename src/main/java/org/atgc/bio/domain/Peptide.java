/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.FullTextIndexed;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.repository.TemplateUtils;
/*
import org.atgc.bio.meta.BioRelation;
import FullTextIndexed;
import Taxonomy;
import UniquelyIndexed;
import Indexed;
import NonIndexed;
 */
import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Sometimes the Interactor object in Intact EBI is a Peptide.
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to uniprot ID. Currently not supporting NON_INDEXED.
 * A peptide is any two or more amino acids that are put together by peptide
 * bond. A protein is a functional, polypeptide chain composed of at least around
 * fifty amino acids put together.
 *
 * @author jtanisha-ee
 * @see BioEntity
 * @see Indexed
 * @see NonIndexed
 * @see BioTypes
 */
@BioEntity(bioType = BioTypes.PEPTIDE)
public class Peptide {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(Peptide.class);

    @GraphId
    private Long id;

    @UniquelyIndexed(indexName=IndexNames.PEPTIDE_UNIPROT_ID)
    @Taxonomy(rbClass=TaxonomyTypes.UNIPROT_ID, rbField=BioFields.UNIPROT_ID)  // this is value
    private String uniprot;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    //private String nodeType = BioTypes.PEPTIDE.toString();
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed (indexName=IndexNames.PEPTIDE_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.PEPTIDE_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)  // this is value
    private String shortLabel;

    @Indexed (indexName=IndexNames.PEPTIDE_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PEPTIDE_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    /**
     * When not known, this will be set to the uniprot.
     */
    @Indexed (indexName=IndexNames.INTERACTOR_ID)
    @Taxonomy (rbClass=TaxonomyTypes.INTERACTOR_ID, rbField=BioFields.INTERACTOR_ID)
    private String interactorId;

    @Indexed (indexName=IndexNames.INTERACTOR_TYPE_XREF)
    private String interactorTypeXref;

    /**
     * Name of the intact XML file. Use Uniprot when null.
     */
    @Indexed ( indexName=IndexNames.INTACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_ID, rbField=BioFields.INTACT_ID)
    private String intactId;

    //@Indexed (indexType=IndexType.FULLTEXT, indexName = "interactoralias")
    @FullTextIndexed (indexName = IndexNames.PEPTIDE_ALIASES)
    @Taxonomy (rbClass=TaxonomyTypes.PEPTIDE_ALIAS, rbField=BioFields.ALIASES)
    private String aliases;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.PEPTIDE_UNIPROT_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.UNIPROT_ID, rbField=BioFields.UNIPROT_SECONDARY_REFS)
    private String uniprotSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.PEPTIDE_INTERPRO_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.INTERPRO_ID, rbField=BioFields.INTERPRO_SECONDARY_REFS)
    private String interproSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.PEPTIDE_IPI_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.IPI_ID, rbField=BioFields.IPI_SECONDARY_REFS)
    private String ipiSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.PEPTIDE_GO_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.GO_ID, rbField=BioFields.GO_SECONDARY_REFS)
    private String goSecondaryRefs;

    @FullTextIndexed (indexName = IndexNames.PEPTIDE_REFSEQ_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.REFSEQ_ID, rbField=BioFields.REFSEQ_SECONDARY_REFS)
    private String refseqSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.PEPTIDE_ENSEMBL_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.ENSEMBL_ID, rbField=BioFields.ENSEMBL_SECONDARY_REFS)
    private String ensemblSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed (indexName = IndexNames.PEPTIDE_INTACT_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_ID, rbField=BioFields.INTACT_SECONDARY_REFS)
    private String intactSecondaryRefs;

    /**
     * This is the species.
     */
    @Indexed (indexName=IndexNames.INTERACTOR_ID)
    @Taxonomy (rbClass=TaxonomyTypes.NCBI_TAX_ID, rbField=BioFields.NCBI_TAX_ID)
    private String ncbiTaxId;

    @Indexed (indexName = IndexNames.INTERACTOR_TYPE_SHORT_LABEL)
    private String interactorTypeShortLabel;

    @Indexed (indexName = IndexNames.INTERACTOR_TYPE_FULL_NAME)
    private String interactorTypeFullName;

    @Indexed (indexName = IndexNames.PEPTIDE_ORGANISM_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.ORGANISM_SHORT_LABEL, rbField=BioFields.ORGANISM_SHORT_LABEL)
    private String organismShortLabel;

    @Indexed (indexName = IndexNames.PEPTIDE_ORGANISM_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.ORGANISM_FULL_NAME, rbField=BioFields.ORGANISM_FULL_NAME)
    private String organismFullName;

    /**
     * In the following, <code>this<code> Peptide object is related to other interactor objects
     * with an outgoing relationship called <code>IntactInteraction</code>.
     */
    @RelatedToVia(direction=Direction.BOTH, elementClass=BioRelation.class)
    private Collection<BioRelation> intactInteractions = new HashSet<BioRelation>();

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_A_PROTEIN, elementClass=BioRelation.class)
    private BioRelation proteinRelation;

    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_A_PROTEIN, elementClass=BioRelation.class)
    private Collection<BioRelation> proteinRelations;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.IN_ORGANISM, elementClass=BioRelation.class)
    private BioRelation ncbiTaxonomyRelation;

    /**
     *
     */
    public Peptide() {}

    /**
     *
     * @param uniprot
     * @param nodeType
     * @param message
     */
    public Peptide(String uniprot, String nodeType, String message) {
        this.uniprot = uniprot;
        this.nodeType = nodeType;
        this.message = message;
    }

    /**
     *
     * @return String
     */
    public String getUniprot() {
        return this.uniprot;
    }

    /**
     * {@link Indexed} {@link IndexNames#PEPTIDE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PEPTIDE_SHORT_LABEL}
     * {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#PEPTIDE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PEPTIDE_SHORT_LABEL}
     * {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#PEPTIDE_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PEPTIDE_FULL_NAME}
     * {@link BioFields#FULL_NAME}
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#PEPTIDE_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes}
     * {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_UNIPROT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#UNIPROT_ID}
     * {@link BioFields#UNIPROT_SECONDARY_REFS}
     *
     * @return String
     */
    public String getUniprotSecondaryRefs() {
       return uniprotSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_UNIPROT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#UNIPROT_ID}
     * {@link BioFields#UNIPROT_SECONDARY_REFS}
     *
     * @param uniprotSecondaryRefs
     */
    public void setUniprotSecondaryRefs(String uniprotSecondaryRefs) {
        this.uniprotSecondaryRefs = uniprotSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_INTERPRO_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTERPRO_ID}
     * {@link BioFields#INTERPRO_SECONDARY_REFS}
     *
     * @return String
     */
    public String getInterproSecondaryRefs() {
       return interproSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_INTERPRO_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTERPRO_ID}
     * {@link BioFields#INTERPRO_SECONDARY_REFS}
     *
     * @param interproSecondaryRefs
     */
    public void setInterproSecondaryRefs(String interproSecondaryRefs) {
        this.interproSecondaryRefs = interproSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_IPI_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#IPI_ID}
     * {@link BioFields#IPI_SECONDARY_REFS}
     *
     * @return String
     */
    public String getIpiSecondaryRefs() {
       return ipiSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_IPI_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#IPI_ID}
     * {@link BioFields#IPI_SECONDARY_REFS}
     *
     * @param ipiSecondaryRefs
     */
    public void setIpiSecondaryRefs(String ipiSecondaryRefs) {
        this.ipiSecondaryRefs = ipiSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_GO_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GO_ID}
     * {@link BioFields#GO_SECONDARY_REFS}
     *
     * @return String
     */
    public String getGoSecondaryRefs() {
       return goSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_GO_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#GO_ID}
     * {@link BioFields#GO_SECONDARY_REFS}
     *
     * @param goSecondaryRefs
     */
    public void setGoSecondaryRefs(String goSecondaryRefs) {
        this.goSecondaryRefs = goSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_INTACT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_ID}
     * {@link BioFields#INTACT_SECONDARY_REFS}
     *
     * @return String
     */
    public String getIntactSecondaryRefs() {
       return intactSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_INTACT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_ID}
     * {@link BioFields#INTACT_SECONDARY_REFS}
     *
     * @param intactSecondaryRefs
     */
    public void setIntactSecondaryRefs(String intactSecondaryRefs) {
        this.intactSecondaryRefs = intactSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_ENSEMBL_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ENSEMBL_ID}
     * {@link BioFields#ENSEMBL_SECONDARY_REFS}
     *
     * @return String
     */
    public String getEnsemblSecondaryRefs() {
       return ensemblSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_ENSEMBL_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ENSEMBL_ID}
     * {@link BioFields#ENSEMBL_SECONDARY_REFS}
     *
     * @param ensemblSecondaryRefs
     */
    public void setEnsemblSecondaryRefs(String ensemblSecondaryRefs) {
        this.ensemblSecondaryRefs = ensemblSecondaryRefs;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_REFSEQ_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#REFSEQ_ID}
     * {@link BioFields#REFSEQ_SECONDARY_REFS}
     *
     * @return String
     */
    public String getRefseqSecondaryRefs() {
       return refseqSecondaryRefs;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_REFSEQ_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#REFSEQ_ID}
     * {@link BioFields#REFSEQ_SECONDARY_REFS}
     *
     * @param refseqSecondaryRefs
     */
    public void setRefseqSecondaryRefs(String refseqSecondaryRefs) {
        this.refseqSecondaryRefs = refseqSecondaryRefs;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_ALIASES}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PROTEIN_ALIAS}
     * {@link BioFields#ALIASES}
     *
     * @return String
     */
    public String getAliases() {
        return aliases;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PEPTIDE_ALIASES}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PEPTIDE_ALIAS}
     * {@link BioFields#ALIASES}
     *
     * @param aliases
     */
    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    /**
     * Name of the intact XML file. Use Uniprot when null.
     * <p>
     * {@link Indexed} {@link IndexNames#INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_ID}
     * {@link BioFields#INTACT_ID}
     *
     * @return String
     */
    public String getIntactId() {
        return intactId;
    }

    /**
     * Name of the intact XML file. Use Uniprot when null.
     * <p>
     * {@link Indexed} {@link IndexNames#INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_ID}
     * {@link BioFields#INTACT_ID}
     *
     * @param intactId
     */
    public void setIntactId(String intactId) {
        this.intactId = intactId;
    }

    /**
     * When not known, this will be set to the uniprot.
     * <p>
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTERACTOR_ID}
     * {@link BioFields#INTERACTOR_ID}
     *
     * @return String
     */
    public String getInteractorId() {
        return interactorId;
    }

    /**
     * When not known, this will be set to the uniprot.
     * <p>
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTERACTOR_ID}
     * {@link BioFields#INTERACTOR_ID}
     *
     * @param interactorId
     */
    public void setInteractorId(String interactorId) {
        this.interactorId = interactorId;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_FULL_NAME}
     *
     * @return String
     */
    public String getInteractorTypeFullName() {
        return interactorTypeFullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_FULL_NAME}
     *
     * @param interactorTypeFullName
     */
    public void setInteractorTypeFullName(String interactorTypeFullName) {
        this.interactorTypeFullName = interactorTypeFullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_SHORT_LABEL}
     *
     * @return String
     */
    public String getInteractorTypeShortLabel() {
        return interactorTypeShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_SHORT_LABEL}
     *
     * @param interactorTypeShortLabel
     */
    public void setInteractorTypeShortLabel(String interactorTypeShortLabel) {
        this.interactorTypeShortLabel = interactorTypeShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_XREF}
     *
     * @return String
     */
    public String getInteractorTypeXref() {
        return interactorTypeXref;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_XREF}
     *
     * @param interactorTypeXref
     */
    public void setInteractorTypeXref(String interactorTypeXref) {
        this.interactorTypeXref = interactorTypeXref;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#NCBI_TAX_ID}
     * {@link BioFields#NCBI_TAX_ID}
     *
     * @return String
     */
    public String getNcbiTaxId() {
        return ncbiTaxId;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#NCBI_TAX_ID}
     * {@link BioFields#NCBI_TAX_ID}
     *
     * @param ncbiTaxId
     */
    public void setNcbiTaxId(String ncbiTaxId) {
        this.ncbiTaxId = ncbiTaxId;
    }

    /**
     * In the following, <code>this<code> Peptide object is related to other objects
     * with an outgoing relationship called <code>IntactInteraction</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PEPTIDE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PEPTIDE}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param intactInteractions
     */
    public void setIntactInteractions(Collection<BioRelation> intactInteractions) {
        this.intactInteractions = intactInteractions;
    }

    /**
     * {@link UniquelyIndexed} {@link IndexNames#UNIPROT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#UNIPROT_ID}
     * {@link BioFields#UNIPROT_ID}
     *
     * @param uniprot
     */
    public void setUniprot(String uniprot) {
        this.uniprot = uniprot;
    }

    /**
     * {@link Indexed} {@link IndexNames#PEPTIDE_ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_SHORT_LABEL}
     * {@link BioFields#ORGANISM_SHORT_LABEL}
     *
     * @return String
     */
    public String getOrganismShortLabel() {
        return organismShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#PEPTIDE_ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_SHORT_LABEL}
     * {@link BioFields#ORGANISM_SHORT_LABEL}
     *
     * @param organismShortLabel
     */
    public void setOrganismShortLabel(String organismShortLabel) {
        this.organismShortLabel = organismShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#PEPTIDE_ORGANISM_FULL_NAME)}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_FULL_NAME}
     * {@link BioFields#ORGANISM_FULL_NAME}
     *
     * @return String
     */
    public String getOrganismFullName() {
        return organismFullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#PEPTIDE_ORGANISM_FULL_NAME)}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_FULL_NAME}
     * {@link BioFields#ORGANISM_FULL_NAME}
     *
     * @param organismFullName
     */
    public void setOrganismFullName(String organismFullName) {
        this.organismFullName = organismFullName;
    }

    /**
     * In the following, <code>this<code> Peptide object is related to other objects
     * with an outgoing relationship called <code>IntactInteraction</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PEPTIDE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getIntactInteractions() {
        log.info("length = " + intactInteractions.size());
        return intactInteractions;
    }

    /**
     * In the following, <code>this<code> Peptide object is related to other objects
     * with an outgoing relationship called <code>IntactInteraction</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PEPTIDE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PEPTIDE}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param peptide
     * @param name
     * @return BioRelation
     */
    public BioRelation interactsWith(Peptide peptide, String name) {
        final BioRelation intactInteraction = new BioRelation(this, peptide, BioRelTypes.INTERACTS_WITH);
        intactInteraction.setName(name);
        intactInteractions.add(intactInteraction);
        log.info("intactInteractions size = " + intactInteractions.size());
        return intactInteraction;
    }

    public void setNcbiTaxonomyRelation(NcbiTaxonomy ncbiTaxonomy) {
        ncbiTaxonomyRelation = new BioRelation(this, ncbiTaxonomy, BioRelTypes.IN_ORGANISM);
    }

    /**
     *
     * @return Long
     */
    public Long getId() {
    	return id;
    }

    /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
     * @return String
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
     * @param nodeType
     */
    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType.toString();
    }

    /**
     * {@link NonIndexed}
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link NonIndexed}
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + uniprot + "-" + interactorId);
    }

    /**
     * Creates a new peptide
     * @return {@link BioEntity}
     */
    public Peptide Peptide() {
        Peptide peptide = new Peptide();
        peptide.setNodeType(BioTypes.PEPTIDE);
        return peptide;
    }

    public void setProteinRelation(Protein protein) {
        proteinRelation = new BioRelation(this, protein, BioRelTypes.HAS_A_PROTEIN);
    }

    public void addProteinRelation(Protein protein) {
        if (proteinRelations == null)
            proteinRelations = new HashSet<>();
        proteinRelations.add(new BioRelation(this, protein, BioRelTypes.HAS_A_PROTEIN));
    }

    /**
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }

        final Peptide other = (Peptide) obj;

        if ((this.uniprot != null) && (other.getUniprot() != null)) {
            if (!this.uniprot.equals(other.getUniprot())) {
                return false;
            }
        } else {
            return false;
        }

        if ((this.nodeType != null) && (other.getNodeType() != null)) {
            if (!this.nodeType.equals(other.getNodeType())) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * In the format:
     * [
                        {
                                "@typeAc" : "MI:0302",
                                "#text" : "Kiaa0964"
                        },
                        {
                                "@typeAc" : "MI:0302",
                                "#text" : "Sapap4"
                        },
                        {
                                "@typeAc" : "MI:0302",
                                "#text" : "SAP90/PSD-95-associated protein 4"
                        },
                        {
                                "@typeAc" : "MI:0302",
                                "#text" : "PSD-95/SAP90-binding protein 4"
                        },
                        {
                                "@typeAc" : "MI:0301",
                                "#text" : "Dlgap4"
                        }
                ]
     */

    /**
     * In the format:
     *
     * {
    "primaryRef": {
      "@refTypeAc": "MI:0356",
      "@refType": "identity",
      "@version": "SP_5",
      "@id": "B1AZP2",
      "@dbAc": "MI:0486",
      "@db": "uniprotkb"
    },
    "secondaryRef": [
      {
        "@refTypeAc": "MI:0360",
        "@refType": "secondary-ac",
        "@version": "SP_5",
        "@id": "Q6PD44",
        "@dbAc": "MI:0486",
        "@db": "uniprotkb"
      },
      {
        "@refTypeAc": "MI:0360",
        "@refType": "secondary-ac",
        "@version": "SP_5",
        "@id": "Q80TN3",
        "@dbAc": "MI:0486",
        "@db": "uniprotkb"
      },
      {
        "@id": "IPR005026",
        "@dbAc": "MI:0449",
        "@db": "interpro"
      },
      {
        "@id": "IPI00330186",
        "@dbAc": "MI:0675",
        "@db": "ipi"
      },
      {
        "@id": "IPI00377469",
        "@dbAc": "MI:0675",
        "@db": "ipi"
      },
      {
        "@id": "IPI00752220",
        "@dbAc": "MI:0675",
        "@db": "ipi"
      },
      {
        "@refTypeAc": "MI:0356",
        "@refType": "identity",
        "@id": "EBI-645637",
        "@dbAc": "MI:0469",
        "@db": "intact"
      },
      {
        "@id": "GO:0007267",
        "@dbAc": "MI:0448",
        "@db": "go"
      },
      {
        "@refTypeAc": "MI:0360",
        "@refType": "secondary-ac",
        "@version": "SP_5",
        "@id": "Q8R3U9",
        "@dbAc": "MI:0486",
        "@db": "uniprotkb"
      },
      {
        "@id": "NP_666240.4",
        "@dbAc": "MI:0481",
        "@db": "refseq"
      },
      {
        "@refTypeAc": "MI:0360",
        "@refType": "secondary-ac",
        "@version": "SP_5",
        "@id": "Q6XBF1",
        "@dbAc": "MI:0486",
        "@db": "uniprotkb"
      },
      {
        "@id": "GO:0045202",
        "@dbAc": "MI:0448",
        "@db": "go"
      },
      {
        "@refTypeAc": "MI:0360",
        "@refType": "secondary-ac",
        "@version": "SP_5",
        "@id": "B1AZP3",
        "@dbAc": "MI:0486",
        "@db": "uniprotkb"
      },
      {
        "@refTypeAc": "MI:0360",
        "@refType": "secondary-ac",
        "@version": "SP_18",
        "@id": "B7ZNS1",
        "@dbAc": "MI:0486",
        "@db": "uniprotkb"
      },
      {
        "@id": "NP_001035952.1",
        "@dbAc": "MI:0481",
        "@db": "refseq"
      },
      {
        "@refTypeAc": "MI:0360",
        "@refType": "secondary-ac",
        "@version": "SP_5",
        "@id": "Q3KQQ8",
        "@dbAc": "MI:0486",
        "@db": "uniprotkb"
      },
      {
        "@id": "NP_001035953.1",
        "@dbAc": "MI:0481",
        "@db": "refseq"
      },
      {
        "@id": "ENSMUSG00000061689",
        "@dbAc": "MI:0476",
        "@db": "ensembl"
      }
    ]
  },
     */


    /**
     * {
      "primaryRef": {
        "@refTypeAc": "MI:0356",
        "@refType": "identity",
        "@id": "MI:0326",
        "@dbAc": "MI:0488",
        "@db": "psi-mi"
      },
      "secondaryRef": [
        {
          "@refTypeAc": "MI:0361",
          "@refType": "see-also",
          "@id": "SO:0000358",
          "@dbAc": "MI:0601",
          "@db": "so"
        },
        {
          "@refTypeAc": "MI:0356",
          "@refType": "identity",
          "@id": "EBI-619654",
          "@dbAc": "MI:0469",
          "@db": "intact"
        },
        {
          "@refTypeAc": "MI:0358",
          "@refType": "primary-reference",
          "@id": "14755292",
          "@dbAc": "MI:0446",
          "@db": "pubmed"
        }
      ]
    }
  }
     */

}
