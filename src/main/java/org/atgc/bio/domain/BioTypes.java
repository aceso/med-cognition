/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import org.atgc.bio.meta.BioEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * These are all the supported BioEntities or {@link BioEntity} objects.
 * These also are treated as dimensions.
 * These are also sometimes mapped to collection names.
 *
 * @author jtanisha-ee
 */

public enum BioTypes {

    /**
     * Protein
     */
    PROTEIN("Protein"),
    
    DRUG_PACKAGER("DrugPackager"),
    
    /**
     * Drug dosage and it's related metadata
     */
    DOSAGE("Dosage"),
    
    DRUG_MANUFACTURER("DrugManufacturer"),
    
    DRUG_PRICE("DrugPrice"),
    
    DRUG_PATENT("DrugPatent"),
    
    PROTEIN_SEQUENCE("ProteinSequence"),
    
    SAMPLE_SOURCE("SampleSource"),
    
    PROTEIN_SEQUENCE_FEATURE("ProteinSequenceFeature"),

    STRUCTURE("Structure"),
    
    CHEMICAL_PROPERTY("ChemicalProperty"),
    
    EXPERIMENTAL_PROPERTY("ExperimentalProperty"),

    PDBGO("Pdbgo"),

    LIGAND("Ligand"),

    CHAIN("Chain"),

    DNA("dna"),

    PDB_COMPOUND("PdbCompound"),

    PDB_ENTITY("PdbEntity"),

    NCBI_TAXONOMY("NcbiTaxonomy"),

    /**
     * Peptide - A peptide is any two or more amino acids that are put together
     * by peptide bond. A protein is a functional, polypeptide chain composed
     * of at least around fifty amino acids put together.
     */
    PEPTIDE("Peptide"),

    /**
     * The intermediate dimension between Gene and GeneToGoOntology, so that we can associate
     * each GeneID with a number of GO_IDs. Each GO_ID is mapped to GeneToGoOntology
     * which in turn is connected to GeneOntology with additional attributes such
     * as PubMedId, Evidence and Category.
     */
    GENE_TO_GO("GeneToGo"),

    /**
     * The intermediate dimension between GeneToGo and GeneOntology, so that we can
     * record metadata of connectivity to each GO_ID, which includes EVIDENCE, CATEGORY,
     * PUBMEDID and so on.
     */
    GENE_TO_GO_ONTOLOGY("GeneToGoOntology"),

    /**
     * Author as found in PubMed and other places. The author can be of several
     * types, including Researcher, Statistician, Principal Investigator,
     * Clinical Researcher, Doctor, Scientist. Currently, we don't have
     * subdimensions for these, as we don't need them.
     */
    AUTHOR("Author"),

    /**
     * Articles like in PubMed might be published in journals like Nature.
     * The journal has some metadata, and becomes it's own dimension.
     */
    JOURNAL("Journal"),

    /**
     * This element contains information about the specific issue in which the
     * article cited resides. It has a single attribute, CitedMedium, which
     * indicates whether a citation is processed/indexed at NLM from the online
     * or the print version of the journal. The two valid attribute values are
     * Internet and Print.
     */
    JOURNAL_ISSUE("JournalIssue"),

    /**
     * In the fields of molecular biology and pharmacology, a small molecule is
     * a low molecular weight (<900 Daltons[1]) organic compound that may serve
     * as a regulator of a biological process, with a size on the order of 10 exp(-9) m.
     */
    SMALL_MOLECULE("SmallMolecule"),

    /**
     * Part of Protein
     */
    PART_PROTEIN("PartProtein"),

    /**
     * PubMed
     */
    PUBMED("PubMed"),

    /**
     * Nci Drug
     */
    DRUG("Drug"),
    //NCI_DRUG("NciDrug"),


    /**
     * Gene as in HUGOGeneSymbol for now. We may need to determine if
     * there is a more generalized way to express this.
     * There is no sub-classification for genes based on other species
     * and it may not be required.
     */
    GENE("Gene"),

    /**
     * The intact interactor.
     */
    INTACT_GENE("IntactGene"),

    /**
     *
     */
    ENZYME("Enzyme"),

    /**
     * Specific classified diseases like "colo-rectal" cancer.
     */
    DISEASE("Disease"),

    /**
     * Typically a pathway as mentioned in NCI standard pathways.
     * Such as the integrin pathway.
     */
    NCI_PATHWAY("NciPathway"),

    /**
     * Organism as defined in NCBI Taxonomy
     * http://www.ncbi.nlm.nih.gov/Taxonomy
     */
    ORGANISM("Organism"),

    /**
     * These are typically experiments where participants are chosen and
     * these participants have interactors (eg. proteins). Initially added
     * for Intact.
     */
    EXPERIMENT("Experiment"),

    EXPERIMENT_DATA_PROCESSING("ExperimentDataProcessing"),

    EXPERIMENT_URL("ExperimentUrl"),

    /**
     * Each experiment has participants or participant IDs involved.
     */
    PARTICIPANT("Participant"),

    /**
     * Biological role of a participant for an experiment.
     */
    BIOLOGICAL_ROLE("BiologicalRole"),

    /**
     * Feature of a participant for an experiment.
     */
    FEATURE("Feature"),

    /**
     * Feature type of a feature for a participant.
     */
    FEATURE_TYPE("FeatureType"),

    /**
     * Feature range in a feature for a participant
     */
    FEATURE_RANGE("FeatureRange"),

    /**
     * Start status properties for a feature range in a protein sequence
     */
    START_STATUS("StartStatus"),

    /**
     * End status properties for a feature range in a protein sequence
     */
    END_STATUS("EndStatus"),

    /**
     * One participantId can have many experimental roles. (experimentalRoleList)
     * This was initiated in Intact, but applicable potentially everywhere
     * or at least in PSI MI systems.
     */
    EXPERIMENTAL_ROLE("ExperimentalRole"),

    /**
     * Intact interaction is now a @BioEntity
     * It is possible we may
     * consolidate this with the Interaction bioentity which is completely
     * generic. At this moment, we do not have enough evidence that
     * we will have the generic version. 5/28/2013
     */
    INTACT_INTERACTION("IntactInteraction"),

    /**
     * Intact is the intact file, top level BioEntity for all intact data.
     */
    INTACT("Intact"),

    /**
     * As in intact experiment.
     */
    EXPERIMENT_ATTRIBUTES("ExperimentAttributes"),

    /**
     * Interaction detection method for an experiment in Intact.
     */
    INTERACTION_DETECTION_METHOD("InteractionDetectionMethod"),

    /**
     * Participant identification method for an experiment in Intact.
     */
    PARTICIPANT_IDENTIFICATION_METHOD("ParticipantIdentificationMethod"),

    /**
     * Experiment comment in AttributeList in Intact experiment
     */
    EXPERIMENT_COMMENT("ExperimentComment"),

    /**
     * Experiment dataset in AttributeList in Intact experiment
     */
    EXPERIMENT_DATASET("ExperimentDataset"),

    /**
     * Experiment library-used in AttributeList in Intact
     */
    EXPERIMENT_LIBRARY("ExperimentLibrary"),

    /**
     * Intact experiment is a @BioEntity. It is possible we may
     * consolidate this with the Experiment bioentity which is completely
     * generic. At this moment, we do not have enough evidence that
     * we will have the generic version. 5/28/2013
     */
    INTACT_EXPERIMENT("IntactExperiment"),

    /**
     * This is used in NciPathway
     */
    NCI_PATHWAY_INTERACTION("NciPathwayInteraction"),

    /**
     * If we find an unknown or unexpected bio type while parsing
     * curated data, we will mark it as unknown.
     */
    UNKNOWN("unknown"),

    GENE_ONTOLOGY("GeneOntology"),

    COMPOUND("Compound"),

    COMPLEX("Complex"),

    RNA("Rna"),

    MOLECULE_FAMILY("MoleculeFamily"),

    NAMED_PROTEIN("NamedProtein"),

    BIO_MOLECULE("BioMolecule"),

    CELL_TYPE_ONTOLOGY("CellTypeOntology"),

    PROTEIN_ONTOLOGY("ProteinOntology"),

    CHEBI_ONTOLOGY("ChebiOntology"),

    PROTEIN_MODIFICATION_ONTOLOGY("ProteinModificationOntology"),

    SYSTEMS_BIOLOGY_ONTOLOGY("SystemsBiologyOntology"),

    TAXONOMIC_RANK_ONTOLOGY("TaxonomicRankOntology"),

    SPATIAL_ONTOLOGY("SpatialOntology"),

    ANATOMICAL_ENTITY_ONTOLOGY("AnatomicalEntityOntology"),


    CARO("CARO"),

    /* Abstract human developmental anatomy */
    AHDA("AHDA"),

    CS("CS"),

    HUMAN_DISEASE_ONTOLOGY("HumanDiseaseOntology"),

    HOMOLOGY_ONTOLOGY("HomologyOntology"),

    RNA_ONTOLOGY("RNAOntology"),

    SYMPTOM_ONTOLOGY("SymptomOntology"),

    PHENOTYPIC_ONTOLOGY("PhenotypicOntology"),

    CYTOPLASM_ONTOLOGY("CytoplasmOntology"),
    
    CITATION("Citation"),
    
    PROTEIN_ANNOTATION_COMMENT("ProteinAnnotationComment"),
    LOG_P("LogP"),

    REFRACTIVITY("Refractivity"),

    LOG_S("LogS"),

    MOLECULAR_WEIGHT("MolecularWeight"),

    MONOISOTOPIC_WEIGHT("MonoisotopicWeight"),

    MOLECULAR_FORMULA("MolecularFormula"),

    WATER_SOLUBILITY("WaterSolubility"),

    PKA_ACIDIC("PkaAcidic"),

    PKA_BASIC("PkaBasic"),

    ;

    private final String value;

    private static final Map<String, BioTypes> stringToEnum = new HashMap<String, BioTypes>();

    static { // init map from constant name to enum constant
        for (BioTypes en : values())
            stringToEnum.put(en.toString(), en);
    }

    public boolean equals(BioTypes bioType) {
        return value.equals(bioType.toString());
    }

    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    BioTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static BioTypes fromString(String value) {
        return stringToEnum.get(value);
    }
}
