/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * For every relationship which has a new string relation, we add an enum here.
 * There would be lots of relationships in this file eventually.
 * <p>
 * Some graph vendors force us to have no spaces in the relationship. That is
 * why we use an underscore. This string is displayed in the graphdb gui, so we
 * need to be more user friendly. Use upper case for all the string names for
 * consistency.
 * <p>
 * Also add a property called "message" with the same value as this relationship.
 * This relation string is the same as the type property for the <code>RelationshipType</code>
 *
 * Evidence code information
 *
 * Evidence codes qualify the assertions with respect to the
 * association of a gene to a disease or compound term by telling how the assertions
 * were made (for example, through inference or experimental data).
 * The curators may have identified the means by which an assertion using the extracted sentence,
 * alone, or through careful reading of the abstract from which the sentence originated.
 * These codes follow the suggestions of Karp et al.
 * Exit Disclaimer logo for ontologies used in pathway and genome databases.
 * The Evidence Code associated with a specific gene-disease or gene-compound pair is
 * found in the text contents of the XML EvidenceCode element and is the
 * EvidenceCode attribute of the caBIO Evidence Code class, gov.nih.nci.cabio.domain.EvidenceCode.
 * https://wiki.nci.nih.gov/display/cageneindex/Data,+Metadata,+and+Annotations
 *
 *
 * @author jtanisha-ee
 */
public enum EvidenceCodes {


    /**
     * Inferred by curator. An assertion was inferred by a curator
     * from relevant information such as other assertions in a database.
     */
    EV_IC("EV-IC:Inferred by curator. An assertion was inferred by a curator from relevant information such as other assertions in a database."),

    /**
    * Inferred from computation. The evidence for an assertion comes from a computational analysis.
    * The assertion itself might have been made by an author or by a computer,
    * that is, EV-COMP does not specify whether manual interpretation of the computation occurred.
    */
    EV_COMP("EV-COMP:Inferred from computation. The evidence for an assertion comes from a computational analysis. The assertion itself might have been made by an author or by a computer, that is, EV-COMP does not specify whether manual interpretation of the computation occurred."),

    /**
     * Human inference. A curator or author inferred this assertion after
     * review of one or more possible types of computational evidence such as sequence similarity,
     * recognized motifs or consensus sequence, etc.
     * When the inference was made by a computer in an automated fashion, use EV-AINF.
     */
    EV_COMP_HINF("EV-COMP-HINF:Human inference such as sequence similarity or consensus sequence."),


    /**
     * An author inferred, or reviewed a computer inference of,
     * sequence function based on similarity to a consensus sequence.
     */
    EV_COMP_HINF_SIMILAR_TO_CONSENSUS("EV-COMP-HINF-SIMILAR-TO-CONSENSUS:Computer inference of sequence function based on similarity to a consensus sequence."),

    /**
     * Not mentioned in the Metadata in wikis, but is used in XML files
     */
    EV_COMP_HINF_SIMILARITY_TO_CONSENSUS("EV-COMP-HINF-SIMILARITY-TO-CONSENSUS:Computer inference of sequence function based on similarity to a consensus sequence."),

   /**
    * An author inferred, or reviewed a computer inference of,
    * promoter position relative to the -10 and -35 boxes.
    */
    EV_COMP_HINF_POSITIONAL_IDENTIFICATION("EV-COMP-HINF-POSITIONAL-IDENTIFICATION:An author inferred, or reviewed a computer inference of promoter position relative to the -10 and -35 boxes."),

    EVCOMP_HINF_FN_FROM_SEQ("EV-COMP-HINF-FN-FROM-SEQ:An author inferred, or reviewed a computer inference of, gene function based on sequence, profile, or structural similarity (as computed from sequence) to one or more other sequences."),

    EV_COMP_AINF("EV-COMP-AINF:Automated inference. A computer inferred this assertion through one of many possible methods such as sequence similarity, recognized motifs or consensus sequence, etc. When a person made the inference from computational evidence, use EV-HINF."),

    EV_COMP_AINF_SINGLE_DIRECTON("EV-COMP-AINF-SINGLE-DIRECTON:Automated inference of transcription unit based on single-gene direction. Existence of a single-gene transcription unit for gene G is inferred computationally by the existence of upstream and downstream genes transcribed in the opposite direction of G."),

   EV_COMP_AINF_SIMILAR_TO_CONSENSUS("EV-COMP-AINF-SIMILAR-TO-CONSENSUS:A DNA sequence similar to previously known consensus sequences is computationally identified."),

   EV_COMP_AINF_SIMILARITY_TO_CONSENSUS("EV-COMP-AINF-SIMILARITY-TO-CONSENSUS:A DNA sequence similar to previously known consensus sequences is computationally identified."),

   EV_COMP_AINF_POSITIONAL_IDENTIFICATION("EV-COMP-AINF-POSITIONAL-IDENTIFICATION:Automated inference of promoter position relative to the -10 and -35 boxes."),

   EV_COMP_AINF_FN_FROM_SEQ("EV-COMP-AINF-FN-FROM-SEQ:Automated inference of function from sequence. A computer inferred a gene function based on sequence, profile, or structural similarity (as computed from sequence) to one or more other sequences."),

   EV_AS_TAS("EV-AS-TAS:Traceable author statement. The assertion was made in a publication ? such as a review ? that itself did not describe an experiment supporting the assertion. The statement referenced another publication that supported the assertion, but it is unclear whether that publication described an experiment that supported the assertion."),

   EV_AS_NAS("EV-AS-NAS:Non-traceable author statement. The assertion was made in a publication such as a review, without a reference to a publication describing an experiment that supports the assertion."),

   EV_EXP("EV-EXP:Inferred from experiment. The evidence for an assertion comes from a wet-lab experiment of some type."),

   EV_EXP_IPI("EV-EXP-IPI:IPI inferred from physical interaction The assertion was inferred from a physical interaction such as 2-hybrid interactions, Co-purification, Co-immunoprecipitation, Ion/protein binding experiments This code covers physical interactions between the gene product of interest and another molecule (or ion, or complex). For functions such as protein binding or nucleic acid binding, a binding assay is simultaneously IPI and IDA; IDA is preferred because the assay directly detects the binding."),

   EV_EXP_IDA("EV-EXP-IDA:IDA inferred from direct assay. The assertion was inferred from a direct experimental assay such as Enzyme assays, In vitro reconstitution (for example, transcription), Immunofluorescence, Cell fractionation, etc."),

   EV_EXP_IDA_UNPURIFIED_PROTEIN("EV-EXP-IDA-UNPURIFIED-PROTEIN:Direct assay of unpurified protein. Presence of a protein activity is indicated by an assay. However, the precise identity of the protein with that activity is not established by this experiment (protein has not been purified."),

   EV_EXP_IDA_TRANSCRIPTION_INIT_MAPPING("EV-EXP-IDA-TRANSCRIPTION-INIT-MAPPING:The transcription start site is identified by primer extension."),

   EV_EXP_IDA_TRANSCRIPTION_INITIATION_MAPPING("EV-EXP-IDA-TRANSCRIPTION-INITIATION-MAPPING:The transcription start site is identified by primer extension."),

   EV_EXP_IDA_TRANSCRIPT_LEN_DETERMINATION("EV-EXP-IDA-TRANSCRIPT-LEN-DETERMINATION:The length of the (transcribed) RNA is experimentally determined. The length of the mRNA is compared with that of the DNA sequence and by this means the number of genes transcribed are established."),

   EV_EXP_IDA_RNA_POLYMERASE_FOOTPRINTING("EV-EXP-IDA-RNA-POLYMERASE-FOOTPRINTING:The binding of RNA polymerase to a DNA region (the promoter) is shown by footprinting."),

   EV_EXP_IDA_PURIFIED_PROTEIN_MULTSPECIES("EV-EXP-IDA-PURIFIED-PROTEIN-MULTSPECIES:Protein purified from mixed culture or other multispecies environment (such as, infected plant or animal tissue), and activity measured through in vitro assay."),

   EV_EXP_IDA_PURIFIED_PROTEIN("EV-EXP-IDA-PURIFIED-PROTEIN:Protein purified to homogeneity from specific species (or from heterologous expression vector), and activity measured through in vitro assay."),

   EV_EXP_IDA_BOUNDARIES_DEFINED("EV-EXP-IDA-BOUNDARIES-DEFINED:Sites or genes bounding the transcription unit are experimentally identified. Several possible cases exist, such as defining the boundaries of a transcription unit with an experimentally identified promoter and terminator, or with a promoter and a downstream gene that is transcribed in the opposite direction, or with a terminator and an upstream gene that is transcribed in the opposite direction."),

   EV_EXP_IDA_BINDING_OF_PURIFIED_PROTEINS("EV-EXP-IDA-BINDING-OF-PURIFIED-PROTEINS:IDA inferred from direct assay. The assertion was inferred from a direct experimental assay such as Enzyme assays, In vitro reconstitution (for example, transcription), Immunofluorescence, Cell fractionation."),

   EV_EXP_IDA_BINDING_OF_CELLULAR_EXTRACTS("EV-EXP-IDA-BINDING-OF-CELLULAR-EXTRACTS: There exists physical evidence of the binding of cellular extracts containing a regulatory protein to its DNA binding site. This can be either by footprinting or mobility shift assays."),

   EV_EXP_IEP("EV-EXP-IEP: IEP inferred from expression pattern. The assertion was inferred from a pattern of expression data such as Transcript levels (for example, Northerns, microarray data), Protein levels (for example, Western blots)."),

   EV_EXP_IEP_GENE_EXPRESSION_ANALYSIS("EV-EXP-IEP-GENE-EXPRESSION-ANALYSIS: The expression of the gene is analyzed through a transcriptional fusion (that is, lacZ), and a difference in expression levels is observed when the regulatory protein is present (wild type) vs in its absence. Note that this evidence does not eliminate the possibility of an indirect effect of the regulator on the regulated gene."),

   EV_EXP_IGI("EV-EXP-IGI: IGI inferred from genetic interaction. The assertion was inferred from a genetic interaction such as Traditional genetic interactions such as suppressors, synthetic lethals, etc., Functional complementation, Inference about one gene drawn from the phenotype of a mutation in a different gene. This category includes any combination of alterations in the sequence (mutation) or expression of more than one gene/gene product. This category can therefore cover any of the IMP experiments that are done in a non-wild-type background, although we prefer to use it only when all mutations are documented."),

   EV_EXP_IGI_FUNC_COMPLEMENTATION("EV-EXP-IGI-FUNC-COMPLEMENTATION: Protein activity inferred by isolating its gene and performing functional complementation of a well characterized heterologous mutant for the protein."),

   EV_EXP_IMP("EV-EXP-IMP:IMP inferred from mutant phenotype. The assertion was inferred from a mutant phenotype such as Any gene mutation/knockout, Overexpression/ectopic expression of wild-type or mutant genes, Anti-sense experiments, RNA interference experiments, Specific protein inhibitors, Complementation. Inferences made from examining mutations or abnormal levels of only the product(s) of the gene of interest are covered by code EV-IMP (compare to code EV-IGI). Use this code for experiments that use antibodies or other specific inhibitors of RNA or protein activity, even though no gene may be mutated (the rationale is that EV-IMP is used where an abnormal situation prevails in a cell or organism)."),

   EV_EXP_IMP_REACTION_ENHANCED("EV-EXP-IMP-REACTION-ENHANCED:Gene is isolated and over-expressed, and increased accumulation of reaction product is observed."),

   EV_EXP_IMP_POLAR_MUTATION("EV-EXP-IMP-POLAR-MUTATION: If a mutation in a gene or promoter prevents expression of the downstream genes due to a polar effect, the mutated gene is clearly part of the transcription unit."),

   EV_EXP_IMP_REACTION_BLOCKED("EV-EXP-IMP-REACTION-BLOCKED: Mutant is characterized, and blocking of reaction is demonstrated."),

   EV_EXP_IMP_SITE_MUTATION("EV-EXP-IMP-SITE-MUTATION: A cis-mutation in the DNA sequence of the transcription-factor binding site interferes with the operation of the regulatory function. This is considered strong evidence for the existence and functional role of the DNA binding site."),

   BASED_ON_ABSTRACT("based on abstract: based on abstract"),

   EV_EXP_IDA_TRANSCRIPT_LENGTH_DETERMINATION("EV-EXP-IDA-TRANSCRIPT-LENGTH-DETERMINATION:The length of the (transcribed) RNA is experimentally determined. The length of the mRNA is compared with that of the DNA sequence and by this means the number of genes transcribed are established."),

   /** not mentioned in the meta data file, but is being used in the xml */
   FALSE_POSITIVE ("false positive: false positive"),

   BASED_ON_PAPER("based on paper: based on paper"),

   NOT_ASSIGNED("not_assigned:not assigned");


    private final String value;
    protected static Log log = LogFactory.getLog(EvidenceCodes.class);

    private static final Map<String, EvidenceCodes> stringToEnum = new HashMap<String, EvidenceCodes>();

    static { // init map from constant name to enum constant
        for (EvidenceCodes en : values())
            stringToEnum.put(en.toString(), en);
    }

    EvidenceCodes(String value) {
        this.value = value;
    }

    public boolean equals(EvidenceCodes eCode) {
        return value.equals(eCode.toString());
    }

    public boolean equals(String eCode) {
        return value.equals(eCode);
    }

    @Override
    public String toString() {
        return value;
    }

    public static EvidenceCodes fromString(String value) {
        return stringToEnum.get(value);
    }

    /**
     * The code will be EV-COMP-INF and return value will be "Inferred from computation"
     * @param code
     * @return String
     */
    public static String getLongString(String code) {
        for (String value : stringToEnum.keySet()) {
            if (value.startsWith(code)) {
                return value.split(":")[1];
            }
        }
        return "";
    }


    /**
     * prefixVal can be EV-COMP-INF
     * @param prefixVal
     * @return EvidenceCodes
     */
    public static EvidenceCodes getEnumCode(String prefixVal) {
        for (String value : stringToEnum.keySet()) {
            if (value.startsWith(prefixVal)) {
                return fromString(value);
            }
        }
        return null;
    }

    public static boolean contains(String val) {
        for (EvidenceCodes en : values()) {
            if (en.toString().equals(val))
                return true;
        }
        return false;
    }

    public static boolean isOutput(String code) {
       // if (role.equals((EvidenceCodes.OUTPUT).toString()) ||
       //     role.equals((EvidenceCodes.OUTPUT_OF).toString()))  {
            return true;
        //} else {
            //return false;
        //}
    }
}
