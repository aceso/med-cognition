/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;

/**
 * Developers can declare their own classes that use the {@link RelationshipEntity}.
 * However, the relationships do not seem to have so many properties. So
 * the {@link BioRelation} class works in most cases. We will use the BioRelation
 * more frequently, but the template supports any {@link RelationshipEntity}
 * classes.
 *
 * The following annotations are supported:
 *
 * <DL>
 * <LI>
 * {@link StartNode} - What is the start node {@link BioEntity} of the relationship.
 * </LI>
 * <LI>
 * {@link EndNode} - What is the end node {@link BioEntity} of the relationship.
 * </LI>
 * <LI>
 * {@link GraphId} - The internal graph id used by the vendor, not by us.
 * </LI>
 * <LI>
 * {@link RelProperty} - All properties that need to be added to the relationship
 * are added with this annotation.
 * </LI>
 * <LI>
 * {@link RelType} - Every relationship has a type. It is also passed via the
 * annotation {@link RelatedToVia} and {@link RelatedTo}.
 * </LI>
 * </DL>
 *
 * @author jtanisha-ee
 */
@RelationshipEntity
public class DiseaseGeneEvidenceRelation {

     protected static Log log = LogFactory.getLog(DiseaseGeneEvidenceRelation.class);

    /**
     *
     * EvidenceCode" : [
     *             "based on abstract",
     *               "EV-EXP-IDA",
     *               "EV-AS-NAS"
     * ],
     *
     * "EvidenceCode" : "EV-AS-NAS"
     */


    /**
     * The end node must be a {@link BioEntity} class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The end node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @EndNode
    private Object endNode;

    /**
     * The start node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @StartNode
    private Object startNode;

    /**
     * This is for the graph database internal use. This need not be
     * modified by the user. It will be ignored while saving. However,
     * while retrieving, it will be populated.
     */
    @GraphId
    private Long id;

    /**
     * The binary indicators yes or no were set for cell line and
     * negation annotations of each gene-disease and gene-compound association.
     * Cell line indicators denote whether the evidence came from a cell line (yes)
     * or other source, such a human subject, animal model, or primary cells (no).
     * The cell line indicator is the text contents of the XML CelllineIndicator element
     * and the celllineStatus attribute of the caBIO Evidence class.
     * Negation indicators specify whether the evidence actually described a lack of
     * association between the candidate binary concept pair (yes), or whether
     * there was a true relationship between them (no). The curators may have deduced
     * the negation indicator by the extracted sentence, alone, or through careful
     * reading of the abstract from which the sentence originated. Occasionally,
     * the curators did not set a negation indicator (-). The negation indicator is
     * the text contents of the XML NegationIndicator element and the negationStatus
     * attribute of the caBIO Evidence class.
     */
    @RelProperty
    String cellLineIndicator;

    @RelProperty
    String negationIndicator;

    /**
     *  Often, the expert curators made free-text comments on records within the Gene-Disease
     * or Gene-Compound databases. Comments included, but were not limited to,
     * notations of genetic anomalies (for example, loss of heterozygosity,
     * polymorphisms, or aberrant methylation), additional disease information,
     * name of the non-human organism from which the experimental data were collected,
     * information on the cell line or other notable reagents used in the execution of
     * the experiment, and other miscellaneous information. Any comments on a sentence
     * are found in the text contents of the XML Comments element and the comment
     * attribute of the caBIO Evidence class.
     */
    @RelProperty
    String comments;

    /**
     * create @RelProperty for each evidence code
     * {@link RelProperty} {@link EvidenceCodes}
     * There can be more than one evidence code associated with the relationship
     */
    @RelProperty
    private String ev_ic;

    @RelProperty
    private String ev_comp;

    @RelProperty
    private String basedOnAbstract;

    @RelProperty
    private String evcomp_hinf_fn_from_seq;

    @RelProperty
    private String ev_as_nas;

    @RelProperty
    private String ev_as_tas;

    @RelProperty
    private String ev_comp_ainf;

    @RelProperty
    private String ev_comp_ainf_fn_from_seq;

    @RelProperty
    private String ev_comp_ainf_positional_identification;

    @RelProperty
    private String ev_comp_ainf_similar_to_consensus;

    @RelProperty
    private String ev_comp_ainf_similarity_to_consensus;

    @RelProperty
    private String ev_comp_ainf_single_direction;

    @RelProperty
    private String ev_comp_hinf;

    @RelProperty
    private String ev_comp_hinf_positional_identification;

    @RelProperty
    private String ev_comp_hinf_similar_to_consensus;

    @RelProperty
    private String ev_exp;

    @RelProperty
    private String ev_exp_ida;

    @RelProperty
    private String ev_exp_ida_binding_of_celluar_extracts;

    @RelProperty
    private String ev_exp_ida_binding_of_purified_proteins;

    @RelProperty
    private String ev_exp_ida_boundaries_defined;

    @RelProperty
    private String ev_exp_ida_purified_protein;

    @RelProperty
    private String ev_exp_ida_purified_protein_multispecies;

    @RelProperty
    private String ev_exp_ida_rna_polymerase_footprinting;

    @RelProperty
    private String ev_exp_ida_transcription_init_mapping;

    @RelProperty
    private String ev_exp_ida_transcription_initiation_mapping;

    @RelProperty
    private String ev_exp_ida_transcript_len_determination;

    @RelProperty
    private String ev_exp_ida_transcript_length_determination;

    @RelProperty
    private String ev_exp_ida_unpurified_protein;

    @RelProperty
    private String ev_exp_iep;

    @RelProperty
    private String ev_exp_iep_gene_expression_analysis;

    @RelProperty
    private String ev_exp_igi;

    @RelProperty
    private String ev_exp_igi_func_complementation;

    @RelProperty
    private String ev_exp_imp;

    @RelProperty
    private String ev_exp_imp_polar_mutation;

    @RelProperty
    private String ev_exp_imp_reaction_blocked;

    @RelProperty
    private String ev_exp_imp_reaction_enhanced;

    @RelProperty
    private String ev_exp_imp_site_mutation;

    @RelProperty
    private String ev_exp_ipi;

    @RelProperty
    private String ev_comp_hinf_similarity_to_consensus;

    @RelProperty
    private String notAssigned;

    @RelProperty
    private String falsePositive;

    @RelProperty
    private String basedOnPaper;

    /**
     * The default relation type. The client must set this to what they want.
     */
    @RelType
    private String relType = BioRelTypes.FOUND_EVIDENCE_IN.toString();

    /**
     * This property is required as it is not displayed without it.
     */
    @RelProperty
    private String message;

    /**
     * This is for the graph database internal use. This need not be
     * modified by the user. It will be ignored while saving. However,
     * while retrieving, it will be populated.
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * This is for the graph database internal use. This need not be
     * modified by the user. It will be ignored while saving. However,
     * while retrieving, it will be populated.
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @param cellLineIndicator
     */
    public void setCellLineIndicator(String cellLineIndicator) {
        this.cellLineIndicator = cellLineIndicator;
    }

    /**
     *
     * @return  String
     */
    public String getCellLineIndicator() {
        return this.cellLineIndicator;
    }

    /**
     *
     * @param negationIndicator
     */
    public void setNegationIndicator(String negationIndicator) {
        this.negationIndicator = negationIndicator;
    }

    /**
     *
     * @return String
     */
    public String getNegationIndicator() {
        return negationIndicator;
    }

    /**
     *
     * @param comments
     */
    public void setComments(String comments) {
        this.comments = comments;
    }

    /**
     *
     * return  String
     */
    public String getComments() {
        return this.comments;
    }

    /**
     * This constructor should not be used preferably, as it does not
     * initialize the start, end nodes.
     */
    public DiseaseGeneEvidenceRelation() {
    }

    /**
     * Create a BioRelation. This is the preferred constructor.
     *
     * @param startNode
     * @param endNode
     * @see BioRelTypes
     */
    public DiseaseGeneEvidenceRelation(Object startNode, Object endNode) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.message = this.relType;
    }

    /**
     * This method should be used to validate the BioRelation, especially
     * before it is used to connect BioEntities. We recommend that
     * developers use this method always.
     *
     * @param startClass
     * @param endClass
     * @return boolean
     * @see BioEntityClasses
     */
    public boolean isValid(BioEntityClasses startClass, BioEntityClasses endClass) {
        if ((startNode == null) || (endNode == null) ) return false;
        if (!startNode.getClass().equals(startClass.getAnnotationClass())) return false;
        if (!endNode.getClass().equals(endClass.getAnnotationClass())) return false;
        return true;
    }

    /**
     * The start node must be a {@link BioEntity} class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     *
     * @param startNode
     */
    public void setStartNode(Object startNode) {
        this.startNode = startNode;
    }

    /**
     * The end node must be a {@link BioEntity} class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The end node is typically populated at the time of
     * creating this relationship, through a constructor.
     *
     * @param endNode
     */
    public void setEndNode(Object endNode) {
        this.endNode = endNode;
    }

    /**
     * Every relationship has a type that is declared in <code>BioRelTypes</code>
     *
     * @return String
     */
    public String getRelType() {
        return relType;
    }

    /**
     * Every relationship has a type that is declared in <code>BioRelTypes</code>
     *
     * @param relType
     */
    public void setRelType(String relType) {
        this.relType = relType;
    }

    /**
     * The start node must be a {@link BioEntity} class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     *
     * @return Object
     */
    public Object getStartNode() {
        return startNode;
    }

    /**
     * The end node must be a {@link BioEntity} class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The end node is typically populated at the time of
     * creating this relationship, through a constructor.
     *
     * @return Object
     */
    public Object getEndNode() {
        return endNode;
    }

    /**
     * getMessage
     * This returns the Relationship Type as a string
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     *
     * @param str
     */
    public void setMessage(String str) {
        this.message = str;
    }

    /**
     *
     * @return String
     */
    public String getEv_ic() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_IC.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_ic(EvidenceCodes eCode) {
        this.ev_ic = EvidenceCodes.EV_IC.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp() {
       return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp(EvidenceCodes eCode) {
        this.ev_comp = EvidenceCodes.EV_COMP.toString();
    }

    /**
     *
     * @param eCode
     */
    public void setBasedOnAbstract(EvidenceCodes eCode) {
        this.basedOnAbstract = EvidenceCodes.BASED_ON_ABSTRACT.toString();
    }

    /**
     *
     * @return String
     */
    public String getBasedOnAbstract() {
        return EvidenceCodes.getLongString(EvidenceCodes.BASED_ON_ABSTRACT.toString());
    }

    /**
     *
     * @return String
     */
    public String getEvcomp_hinf_fn_from_seq() {
        return EvidenceCodes.getLongString(EvidenceCodes.EVCOMP_HINF_FN_FROM_SEQ.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEvcomp_hinf_fn_from_seq(EvidenceCodes eCode) {
        this.evcomp_hinf_fn_from_seq = EvidenceCodes.EVCOMP_HINF_FN_FROM_SEQ.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_as_nas() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_AS_NAS.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_as_nas(EvidenceCodes eCode) {
        this.ev_as_nas = EvidenceCodes.EV_AS_NAS.toString();
    }

    /**
     *
     * @param eCode
     */
    public void setEv_as_tas(EvidenceCodes eCode) {
        this.ev_as_tas = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_as_tas() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_AS_TAS.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_ainf(EvidenceCodes eCode) {
        this.ev_comp_ainf = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_ainf() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_AINF.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_ainf_fn_from_seq(EvidenceCodes eCode) {
        this.ev_comp_ainf_fn_from_seq = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_ainf_fn_from_seq() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_AINF_FN_FROM_SEQ.toString());
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_ainf_positional_identification() {
       return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_AINF_POSITIONAL_IDENTIFICATION.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_ainf_positional_identification(EvidenceCodes eCode) {
        this.ev_comp_ainf_positional_identification = EvidenceCodes.EV_COMP_AINF_POSITIONAL_IDENTIFICATION.toString();
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_ainf_similar_to_consensus(EvidenceCodes eCode) {
         this.ev_comp_ainf_similar_to_consensus = EvidenceCodes.EV_COMP_AINF_SIMILAR_TO_CONSENSUS.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_ainf_similar_to_consensus() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_AINF_SIMILAR_TO_CONSENSUS.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_ainf_similarity_to_consensus(EvidenceCodes eCode) {
         this.ev_comp_ainf_similarity_to_consensus = EvidenceCodes.EV_COMP_AINF_SIMILARITY_TO_CONSENSUS.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_ainf_similarity_to_consensus() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_AINF_SIMILARITY_TO_CONSENSUS.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_ainf_single_direction(EvidenceCodes eCode) {
        this.ev_comp_ainf_single_direction = (EvidenceCodes.EV_COMP_AINF_SINGLE_DIRECTON.toString());
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_ainf_single_direction() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_AINF_SINGLE_DIRECTON.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_hinf(EvidenceCodes eCode) {
        this.ev_comp_hinf = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_hinf() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_HINF.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_hinf_positional_identification(EvidenceCodes eCode) {
        this.ev_comp_hinf_positional_identification = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_hinf_positional_identification() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_HINF_POSITIONAL_IDENTIFICATION.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_hinf_similar_to_consensus(EvidenceCodes eCode) {
        this.ev_comp_hinf_similar_to_consensus = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_hinf_similar_to_consensus() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_HINF_SIMILAR_TO_CONSENSUS.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_comp_hinf_similarity_to_consensus(EvidenceCodes eCode) {
        this.ev_comp_hinf_similarity_to_consensus = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_comp_hinf_similarity_to_consensus() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_COMP_HINF_SIMILARITY_TO_CONSENSUS.toString());
    }

    /**
     *
     * @return String
     */
    public String getEv_exp() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp(EvidenceCodes eCode) {
        this.ev_exp = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida(EvidenceCodes eCode) {
        this.ev_exp_ida = eCode.toString();
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_binding_of_celluar_extracts(EvidenceCodes eCode) {
        this.ev_exp_ida_binding_of_celluar_extracts = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_binding_of_celluar_extracts() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_BINDING_OF_CELLULAR_EXTRACTS.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_binding_of_purified_proteins(EvidenceCodes eCode) {
        this.ev_exp_ida_binding_of_purified_proteins = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_binding_of_purified_proteins() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_BINDING_OF_PURIFIED_PROTEINS.toString());
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_boundaries_defined() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_BOUNDARIES_DEFINED.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_boundaries_defined(EvidenceCodes eCode) {
        this.ev_exp_ida_boundaries_defined = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_purified_protein() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_PURIFIED_PROTEIN.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_purified_protein(EvidenceCodes eCode) {
        this.ev_exp_ida_purified_protein = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_purified_protein_multispecies() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_PURIFIED_PROTEIN_MULTSPECIES.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_purified_protein_multispecies(EvidenceCodes eCode) {
        this.ev_exp_ida_purified_protein_multispecies = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_rna_polymerase_footprinting() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_RNA_POLYMERASE_FOOTPRINTING.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_rna_polymerase_footprinting(EvidenceCodes eCode) {
        this.ev_exp_ida_rna_polymerase_footprinting = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_transcription_init_mapping() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_TRANSCRIPTION_INIT_MAPPING.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_transcription_init_mapping(EvidenceCodes eCode) {
        this.ev_exp_ida_transcription_init_mapping = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_transcription_initiation_mapping() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_TRANSCRIPTION_INITIATION_MAPPING.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_transcription_initiation_mapping(EvidenceCodes eCode) {
        this.ev_exp_ida_transcription_initiation_mapping = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_transcript_length_determination() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_TRANSCRIPT_LENGTH_DETERMINATION.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_transcript_length_determination(EvidenceCodes eCode) {
        this.ev_exp_ida_transcript_length_determination = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_transcript_len_determination() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_TRANSCRIPT_LEN_DETERMINATION.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_transcript_len_determination(EvidenceCodes eCode) {
        this.ev_exp_ida_transcript_len_determination = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ida_unpurified_protein() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IDA_UNPURIFIED_PROTEIN.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ida_unpurified_protein(EvidenceCodes eCode) {
        this.ev_exp_ida_unpurified_protein = eCode.toString();
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_iep(EvidenceCodes eCode) {
        this.ev_exp_iep = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_iep() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IEP.toString());
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_iep_gene_expression_analysis() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IEP_GENE_EXPRESSION_ANALYSIS.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_iep_gene_expression_analysis(EvidenceCodes eCode) {
        this.ev_exp_iep_gene_expression_analysis = eCode.toString();
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_igi(EvidenceCodes eCode) {
        this.ev_exp_igi = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_igi() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IGI.toString());
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_igi_func_complementation() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IGI_FUNC_COMPLEMENTATION.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_igi_func_complementation(EvidenceCodes eCode) {
        this.ev_exp_igi_func_complementation = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_imp() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IMP.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_imp(EvidenceCodes eCode) {
        this.ev_exp_imp = eCode.toString();
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_imp_polar_mutation(EvidenceCodes eCode) {
        this.ev_exp_imp_polar_mutation = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_imp_polar_mutation() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IMP_POLAR_MUTATION.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_imp_reaction_blocked(EvidenceCodes eCode) {
        this.ev_exp_imp_reaction_blocked = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_imp_reaction_blocked() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IMP_REACTION_BLOCKED.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_imp_reaction_enhanced(EvidenceCodes eCode) {
        this.ev_exp_imp_reaction_enhanced = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_imp_reaction_enhanced() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IMP_REACTION_ENHANCED.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_imp_site_mutation(EvidenceCodes eCode) {
        this.ev_exp_imp_site_mutation = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_imp_site_mutation() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IMP_SITE_MUTATION.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setEv_exp_ipi(EvidenceCodes eCode) {
        this.ev_exp_ipi = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getEv_exp_ipi() {
        return EvidenceCodes.getLongString(EvidenceCodes.EV_EXP_IPI.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setNotAssigned(EvidenceCodes eCode) {
        this.notAssigned = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getNotAssigned() {
        return EvidenceCodes.getLongString(EvidenceCodes.NOT_ASSIGNED.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setFalsePositive(EvidenceCodes eCode) {
        this.falsePositive = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getFalsePositive() {
        return EvidenceCodes.getLongString(EvidenceCodes.FALSE_POSITIVE.toString());
    }

    /**
     *
     * @param eCode
     */
    public void setBasedOnPaper(EvidenceCodes eCode) {
        this.basedOnPaper = eCode.toString();
    }

    /**
     *
     * @return String
     */
    public String getBasedOnPaper() {
        return EvidenceCodes.getLongString(EvidenceCodes.BASED_ON_PAPER.toString());
    }

    /**
     *
     * @param code
     */
    public void setCode(EvidenceCodes code) {
        switch(code) {
            case NOT_ASSIGNED:
                setNotAssigned(code);
                break;

            case EV_EXP_IPI:
                setEv_exp_ipi(code);
                break;

            case EV_EXP_IMP_SITE_MUTATION:
                setEv_exp_imp_site_mutation(code);
                break;

            case EV_EXP_IMP_REACTION_ENHANCED:
                setEv_exp_imp_reaction_enhanced(code);
                break;

            case EV_EXP_IMP_REACTION_BLOCKED:
                setEv_exp_imp_reaction_blocked(code);
                break;
            case EV_EXP_IMP_POLAR_MUTATION:
                setEv_exp_imp_polar_mutation(code);
                break;
            case EV_EXP_IMP:
                setEv_exp_imp(code);
                break;
            case EV_EXP_IGI_FUNC_COMPLEMENTATION:
                setEv_exp_igi_func_complementation(code);
                break;
            case EV_EXP_IGI:
                setEv_exp_igi(code);
                break;
            case EV_EXP_IEP_GENE_EXPRESSION_ANALYSIS:
                setEv_exp_iep_gene_expression_analysis(code);
                break;
            case EV_EXP_IEP:
                setEv_exp_iep(code);
                break;
            case EV_EXP_IDA_UNPURIFIED_PROTEIN:
                setEv_exp_ida_unpurified_protein(code);
                break;
            case EV_EXP_IDA_TRANSCRIPT_LEN_DETERMINATION:
                setEv_exp_ida_transcript_len_determination(code);
                break;
            case EV_EXP_IDA_TRANSCRIPT_LENGTH_DETERMINATION:
                setEv_exp_ida_transcript_length_determination(code);
                break;
            case EV_EXP_IDA_TRANSCRIPTION_INIT_MAPPING:
                setEv_exp_ida_transcription_init_mapping(code);
                break;
            case EV_EXP_IDA_TRANSCRIPTION_INITIATION_MAPPING:
                setEv_exp_ida_transcription_initiation_mapping(code);
                break;
            case EV_EXP_IDA_RNA_POLYMERASE_FOOTPRINTING:
                setEv_exp_ida_rna_polymerase_footprinting(code);
                break;
            case EV_EXP_IDA_PURIFIED_PROTEIN_MULTSPECIES:
                setEv_exp_ida_purified_protein_multispecies(code);
                break;
            case EV_EXP_IDA_PURIFIED_PROTEIN:
                setEv_exp_ida_purified_protein(code);
                break;
            case EV_EXP_IDA_BOUNDARIES_DEFINED:
                setEv_exp_ida_boundaries_defined(code);
                break;
            case EV_EXP_IDA_BINDING_OF_PURIFIED_PROTEINS:
                setEv_exp_ida_binding_of_purified_proteins(code);
                break;
            case EV_EXP_IDA_BINDING_OF_CELLULAR_EXTRACTS:
                setEv_exp_ida_binding_of_celluar_extracts(code);
                break;
            case EV_EXP_IDA:
                setEv_exp_ida(code);
                break;
            case EV_EXP:
                setEv_exp(code);
                break;
            case EV_COMP_HINF_SIMILAR_TO_CONSENSUS:
                setEv_comp_hinf_similar_to_consensus(code);
                break;
            case EV_COMP_HINF_SIMILARITY_TO_CONSENSUS:
                setEv_comp_hinf_similarity_to_consensus(code);
                break;
            case EV_COMP_HINF_POSITIONAL_IDENTIFICATION:
                setEv_comp_hinf_positional_identification(code);
                break;
            case EV_COMP_AINF_FN_FROM_SEQ:
                 setEv_comp_hinf(code);
                 break;
            case EV_COMP_AINF_SINGLE_DIRECTON:
                setEv_comp_ainf_single_direction(code);
                break;

            case EV_COMP_AINF_SIMILAR_TO_CONSENSUS:
                setEv_comp_ainf_similar_to_consensus(code);
                break;
            case EV_COMP_AINF_SIMILARITY_TO_CONSENSUS:
                setEv_comp_ainf_similarity_to_consensus(code);
                break;
            case EV_COMP_AINF_POSITIONAL_IDENTIFICATION:
                setEv_comp_ainf_positional_identification(code);
                break;
            case EV_COMP_AINF:
                setEv_comp_ainf(code);
                break;
            case EV_AS_TAS:
                setEv_as_tas(code);
                break;
            case EV_AS_NAS:
                setEv_as_nas(code);
                break;
            case EVCOMP_HINF_FN_FROM_SEQ:
                setEvcomp_hinf_fn_from_seq(code);
                break;
            case BASED_ON_ABSTRACT:
                setBasedOnAbstract(code);
                break;
            case EV_COMP:
                setEv_comp(code);
                break;
            case EV_IC:
                setEv_ic(code);
                break;
            case FALSE_POSITIVE:
                setFalsePositive(code);
                break;
            case BASED_ON_PAPER:
                setBasedOnPaper(code);
                break;
            default:
                break;
        }
    }

    /**
     * This method is implemented yet.
     * @return String
     */
    @Override
    public String toString() {
        return String.format("%s interacts %s", startNode, endNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiseaseGeneEvidenceRelation relation = (DiseaseGeneEvidenceRelation)o;
        if (id == null) return super.equals(o);
        return id.equals(relation.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}