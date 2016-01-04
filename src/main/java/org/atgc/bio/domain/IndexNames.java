package org.atgc.bio.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Index names in Neo4J. We need to have a policy of preceding the index name
 * with the BioType. This is because different BioEntity type objects may
 * overwrite each other in the Neo4J index. The index name this way will never
 * clash, and separate index will need to be used for the same column in two
 * different BioEntity. For instance, if you have a field called MOD_DATE in
 * the Gene BioEntity, you must called the indexName as GENE_MOD_DATE. As
 * some other BioEntity may have also have the identical field name.
 *
 * Having said this, we have not currently followed the above guidelines in
 * this file. Over time, we need to fix this.
 */
public enum IndexNames {

    /**
     * Uniprot index name
     */
    NAME("name"),

    UNIPROT_ID("UniprotId"),
    
    /**
     * Drugbank taxonomy metadata for kingdom.
     */
    DRUG_KINGDOM("DrugKingdom"),
    
    /**
     * Name of the drugbank drug packagr.
     */
    DRUG_PACKAGER_NAME("DrugPackagerName"),
    
    /**
     * Compound key index for DrugPrice
     */
    DRUG_PRICE("DrugPrice"),
    
    /**
     * Drug patent number
     */
    DRUG_PATENT_NUMBER("DrugPatentNumber"),
    
    /**
     * Drug patent country
     */
    DRUG_PATENT_COUNTRY("DrugPatentCountry"),
    
    /**
     * Drug patent message
     */
    DRUG_PATENT_MESSAGE("DrugPatentMessage"),
    
    /**
     * Food interactions in drug bank
     */
    FOOD_INTERACTIONS("FoodInteractions"),
    
    /**
     * Compound index for chemical property.
     */
    CHEMICAL_PROPERTY("ChemicalProperty"),
    
    /**
     * Compound index for experimental property
     */
    EXPERIMENTAL_PROPERTY("ExperimentalProperty"),
    
    /**
     * Chemical property message
     */
    CHEMICAL_PROPERTY_MESSAGE("ChemicalPropertyMessage"),
    
    /**
     * Experimental property message
     */
    EXPERIMENTAL_PROPERTY_MESSAGE("ExperimentalPropertyMessage"),
    
    /**
     * Drug interactions in drug bank
     */
    DRUG_INTERACTIONS("DrugInteractions"),
    
    /**
     * AHFS Drug Information identifiers
     */
    AHFS_CODES("AHFSCodes"),
    
    /**
     * WHO drug classification system (ATC) identifiers
     */
    ATC_CODES("ATCCodes"),
    
    /**
     * Dosage message label
     */
    DOSAGE_MESSAGE("DosageMessage"),
    
    /**
     * Compound key index for Dosage 
     */
    DOSAGE_ID("DosageId"),
    
    /**
     * Drug affected organisms
     */
    DRUG_AFFECTED_ORGANISMS("DrugAffectedOrganisms"),
    
    /**
     * Drug categories
     */
    DRUG_CATEGORIES("DrugCategories"),
    
    /**
     * Description of drug for pricing
     */
    DRUG_PRICE_DESCRIPTION("DrugPriceDescription"),
    
    /**
     * Name of the drugbank drug packagr.
     */
    DRUG_MANUFACTURER_NAME("DrugManufacturerName"),
    
    /**
     * Drug synonyms
     */
    DRUG_SYNONYMS("DrugSynonyms"),
    
    /**
     * Drug salts
     */
    DRUG_SALTS("DrugSalts"),
    
    /**
     * Drug brands
     */
    DRUG_BRANDS("DrugBrands"),
    
    /**
     * Drug mixtures
     */
    DRUG_MIXTURES("DrugMixtures"),
    
    /**
     * Url of the drug packager.
     */
    DRUG_PACKAGER_URL("DrugPackagerUrl"),
    
    /**
     * Visual message for the drug packager node.
     */
    DRUG_PACKAGER_MESSAGE("DrugPackagerMessage"),
    
    /**
     * Visual message for the drug manufacturer node.
     */
    DRUG_MANUFACTURER_MESSAGE("DrugManufacturerMessage"),
    
    DRUG_SUBSTRUCTURES("DrugSubtructures"),
    
    SECONDARY_ACCESSION_NUMBERS("SecondaryAccessionNumbers"),

    /* approved, illicit, withdrawn */
    DRUG_GROUPS("DrugGroups"),

    PEPTIDE_UNIPROT_ID("PeptideUniprotId"),

    STRUCTURE_ID("StructureId"),

    DRUG_BANK_ID("DrugBankId"),

    DRUG_NAME("DrugName"),

    DRUG_DESCRIPTION("DrugDescription"),

    PDB_COMPOUND_ATCC("PdbCompoundAtcc"),

    PDB_COMPOUND_BIOLOGICAL_UNIT("PdbCompoundBiologicalUnit"),

    PDB_COMPOUND_CELLULAR_LOCATION("PdbCompoundCellularLocation"),

    PDB_COMPOUND_EC_NUMS("PdbCompoundEcNums"),

    PDB_COMPOUND_MOL_ID("PdbCompoundMolId"),

    PDB_COMPOUND_ORGANISM_FULL_NAME("PdbCompoundOrganismFullName"),

    PDB_COMPOUND_TAXONOMY_ID("PdbCompoundTaxonomyId"),

    PDB_ENTITY("PdbEntity"),

    PDB_ENTITY_ENTITY_TYPE("PdbEntityEntityType"),

    STRUCTURE_PUBMED_ID("StructurePubmedId"),

    END_STATUS_MESSAGE("EndStatusMessage"),

    START_STATUS_MESSAGE("StartStatusMessage"),

    CHAIN_MESSAGE("ChainMessage"),

    PDB_ENTITY_MESSAGE("PdbEntityMessage"),

    LIGAND_MESSAGE("LigandMessage"),

    CHAIN("Chain"),

    CHAIN_ATOM_SEQUENCE("ChainAtomSequence"),

    MOLECULE_NAME("CompoundMoleculeName"),

    STRUCTURE_METHOD("StructureMethod"),

    STRUCTURE_PMCID("StructurePMCID"),

    STRUCTURE_EXP_METHOD("StructureExpMethod"),

    STRUCTURE_KEYWORDS("StructureKeywords"),

    PDBGO_GOID("PdbGoId"),

    PUBMED_MESSAGE("PubmedMessage"),

    PDBGO_MESSAGE("PdbGoMessage"),

    STRUCTURE_MESSAGE("StructureMessage"),

    LIGAND_ENTITY_TYPE("LigandEntityType"),

    LIGAND_MOLECULAR_WEIGHT("LigandMolecularWeight"),

    LIGAND_FORMULA("LigandFormula"),

    LIGAND_INCHI_KEY("LigandInchiKey"),

    LIGAND_INCHI("LigandInchi"),

    LIGAND_CHEMICAL_NAME("LigandChemicalName"),

    PDBGO_CHAIN_ID("PdbGo Chain Id"),

    CHEMICAL_ID("Chemical Id"),

    SMALL_MOLECULE_UNIPROT_ID("SmallMoleculeUniprotId"),

    GENE_ID("GeneId"),

    GENE_MESSAGE("GeneMessage"),

    GENE2GO_MESSAGE("Gene2GoMessage"),

    GENE2GOONTOLOGY_MESSAGE("Gene2GoOntologyMessage"),

    GENE_TO_GO_ONTOLOGY("GeneToGoOntology"),

    TAXONOMY_MESSAGE("TaxonomyMessage"),

    TAXONOMY_RANK("TaxonomyRank"),

    NCBI_TAXONOMY_RELATED_SYNONYMS("NcbiTaxonomyRelatedSynonyms"),

    NCBI_TAXONOMY_EXACT_SYNONYMS("NcbiTaxonomyExactSynonyms"),

    NCBI_TAXONOMY_NAME("NcbiTaxonomyName"),

    /**
     * Intact index name
     */
    INTACT_ID("IntactId"),
    /**
     * EntrezGene identifier number
     */
    ENTREZ_GENE_ID("EntrezGeneId"),
    /**
     * HGNC_ID
     */
    HGNC_ID("HgncId"),
    /**
     * GENE_ALIASES
     */
    GENE_ALIASES("GeneAliases"),
    /**
     * GENE_HUGO_GENE_SYMBOL - same as GeneName
     */
    GENE_HUGO_GENE_SYMBOL("HugoGeneSymbol"),
    /**
     * NciDrug
     */
    NCI_DRUG_TERM("NciDrugTerm"),

    /**
     * NCBI GeneID
     */
    GENE_NCBI_GENE_ID("NcbiGeneId"),

    /**
     * LocusTag as found in ncbigeneinfo
     */
    GENE_LOCUS_TAG("LocusTag"),

    /**
     * description found in NCBI geneinfo
     */
    GENE_DESC("GeneDesc"),

    /**
     * TypeOfGene found in NCBI geneinfo
     */
    GENE_TYPE_OF_GENE("TypeOfGene"),

    /**
     * Modification_date as found in NCBI geneinfo
     */
    GENE_MOD_DATE("GeneModDate"),

    /**
     * GENE_ONTOLOGY namespace
     */
    GENE_ONTOLOGY_NAMESPACE("GENE_ONTOLOGY_NAMESPACE"),

    /**
     * def field in geneontology
     */
    GENE_ONTOLOGY_DEF("GENE_ONTOLOGY_DEF"),

    /**
     * The name of the GO term such as chloroplast.
     */
    GENE_ONTOLOGY_NAME("GeneOntologyName"),

    /**
     * Owner field in Pubmed for instance NLM
     */
    PUBMED_OWNER("PubmedOwner"),

    /**
     * Nci DrugCode
     */
    NCI_DRUG_CODE("NciDrugCode"),

    /**
     * Status field in Pubmed
     */
    PUBMED_STATUS("PubmedStatus"),

    /**
     * version field in Pubmed eg. 1
     */
    PUBMED_VERSION("PubmedVersion"),

    /**
     * dateCreated field in Pubmed
     */
    PUBMED_CREATED_DATE("PubmedCreateDate"),

    PUBMED_COMPLETED_DATE("PubmedCompletedDate"),

    /**
     * Pubmed related
     */
    JOURNAL_ISSUE_PUBDATE("JournalIssuePubdate"),

    AUTHOR_NAME("AuthorName"),

    JOURNAL_ISSUE("JournalIssue"),

    PUBMED_MAJOR_TOPICS("PubmedMajorTopics"),

    PUBMED_ARTICLE_ABSTRACT("PubmedArticleAbstract"),

    PUBMED_NOT_MAJOR_TOPICS("PubmedNotMajorTopics"),

    /**
     * From PubMed
     */
    PUBLICATION_TYPE_LIST("PublicationTypeList"),

    AUTHOR_VALIDYN("AuthorValidYN"),

    JOURNAL_ISSN("JournalISSN"),

    JOURNAL_TITLE("title"),

    ISO_ABBREVIATION("isoAbbreviation"),

    /**
     * This was in Journal Issue of PubMed
     */
    CITED_MEDIUM("CitedMedium"),

    PUBMED_ARTICLE_TITLE("PubmedArticleTitle"),

    /**
     * For visual - drug label
     */
    DRUG_LABEL("DrugLabel"),
    /**
     * visual - disease label
     */
    DISEASE_LABEL("DiseaseLabel"),
    /**
     * NCI PATHWAY_LABEL
     */
    NCI_PATHWAY_LABEL("NciPathwayLabel"),

    /**
     * visual
     */
    NCI_PATHWAY_SHORT_LABEL("NciPathwayShortLabel"),

   /**
    * visual
    */
    ORGANISM_LABEL("OrganismLabel"),

    /**
     * Intermediate dimension between Gene and GO.
     */
    GENE_TO_GO("GeneToGo"),

    /**
     * GENE_NAME_LABEL
     */
    GENE_NAME_LABEL("GeneNameLabel"),
    /**
     * Type of node in Neo4J typically corresponding to BioEntity.
     */
    NODE_TYPE("NodeType"),
    /**
     * The interactor id is the id of the interactor typically in Intact.
     */
    INTERACTOR_ID("InteractorId"),
    /**
     * The molecule id ref as found in NCI Pathways.
     */
    MOLECULE_IDREF("MoleculeIdRef"),
    /**
     * The part molecule id ref as found in NCI Pathways.
     */
    PART_MOLECULE_IDREF("PartMoleculeIdRef"),
    /**
     * The NCBI taxonomy Id. Each species has a unique id.
     */
    NCBI_TAX_ID("NcbiTaxId"),
    /**
     * The interactor type xref as found in Intact.
     */
    INTERACTOR_TYPE_XREF("InteractorTypeXref"),
    INTERACTOR_TYPE_SHORT_LABEL("InteractorTypeShortLabel"),
    INTERACTOR_TYPE_FULL_NAME("InteractorTypeFullName"),
    /**
     * Each participantId has a biological role that has a short label.
     */
    BIOLOGICAL_ROLE_SHORT_LABEL("BiologicalRoleShortLabel"),
    /**
     * The biological role Id from the Intact database.
     */
    BIOLOGICAL_ROLE_INTACT_ID("BiologicalRoleIntactId"),
    /**
     * The experimental role Id from the Intact database.
     */
    EXPERIMENTAL_ROLE_INTACT_ID("ExperimentalRoleIntactId"),
    /**
     * Experimental role short label.
     */
    EXPERIMENTAL_ROLE_SHORT_LABEL("ExperimentalRoleShortLabel"),
    /**
     * Experimental role full name.
     */
    EXPERIMENTAL_ROLE_FULL_NAME("ExperimentalRoleFullName"),
    /**
     * Experimental role PSI MI Id was found in the primaryRef of the
     * experimentalRole in experimentalRoleList
     */
    EXPERIMENTAL_ROLE_PSI_MI_ID("ExperimentalRolePsiMiId"),
    /**
     * Experimental role Pubmed ID was found in secondaryRef of the
     * experimentalRole
     */
    EXPERIMENTAL_ROLE_PUBMED_ID("ExperimentalRolePubMedId"),
    /**
     * Experiment Pubmed ID
     */
    EXPERIMENT_PUBMED_ID("ExperimentPubMedId"),
    /**
     * Organism Short Label
     */
    ORGANISM_SHORT_LABEL("OrganismShortLabel"),
    /**
     * Organism Full Name
     */
    ORGANISM_FULL_NAME("OrganismFullName"),
    /**
     * The biological PSI-MI role Id of the biological role for a participantId.
     * Eg. MI-0499
     */
    BIOLOGICAL_ROLE_PSI_MI_ID("BiologicalRolePsiMiId"),
    /**
     * The biological role Id as in PubMed. This pubmed article is just an
     * interaction description. The Id is always 14755292 for PubMed.
     * http://www.ncbi.nlm.nih.gov/pubmed/14755292 So we need to think if this
     * variable is required.
     */
    BIOLOGICAL_ROLE_PUBMED_ID("BiologicalRolePsiMiId"),
    /**
     * Each participantId has a biological role that has a full name.
     */
    BIOLOGICAL_ROLE_FULL_NAME("BiologicalRoleFullName"),
    /**
     * Feature as found in a participant
     */
    FEATURE_ID("FeatureId"),
    END_STATUS_FEATURE_ID("EndStatusFeatureId"),
    START_STATUS_FEATURE_ID("StartStatusFeatureId"),
    FEATURE_RANGE_FEATURE_ID("FeatureRangeFeatureId"),
    FEATURE_TYPE_FEATURE_ID("FeatureTypeFeatureId"),
    /**
     * Short label of the feature in a participant
     */
    FEATURE_SHORT_LABEL("FeatureShortLabel"),
    /**
     * Short label of the experiment
     */
    EXPERIMENT_SHORT_LABEL("ExperimentShortLabel"),
    /**
     * Full Name of the experiment
     */
    EXPERIMENT_FULL_NAME("ExperimentFullName"),
    /**
     * Feature type short label
     */
    FEATURE_TYPE_SHORT_LABEL("FeatureTypeShortLabel"),
    /**
     * Feature type full name
     */
    FEATURE_TYPE_FULL_NAME("FeatureTypeFullName"),
    /**
     * Start status short label
     */
    START_STATUS_SHORT_LABEL("StartStatusShortLabel"),
    /**
     * Start status full name
     */
    START_STATUS_FULL_NAME("StartStatusFullName"),
    /**
     * End status short label
     */
    END_STATUS_SHORT_LABEL("EndStatusShortLabel"),
    /**
     * End status full name
     */
    END_STATUS_FULL_NAME("EndStatusFullName"),
    /**
     * Start status PSI MI ID
     */
    START_STATUS_PSI_MI_ID("StartStatusPsiMiId"),
    /**
     * End status PSI MI ID
     */
    END_STATUS_PSI_MI_ID("EndStatusPsiMiId"),
    /**
     * Start status Intact ID
     */
    START_STATUS_INTACT_ID("StartStatusIntactId"),
    /**
     * Start status PubMed ID
     */
    START_STATUS_PUBMED_ID("StartStatusPubMedId"),
    /**
     * End Status Intact ID
     */
    END_STATUS_INTACT_ID("EndStatusIntactId"),
    /**
     * End Status PubMed ID
     */
    END_STATUS_PUBMED_ID("EndStatusPubMedId"),
    /**
     * Feature type alias
     */
    FEATURE_TYPE_ALIAS("FeatureTypeAlias"),
    /**
     * Feature Type PSI MI ID, found as the primaryRef
     */
    FEATURE_TYPE_PSI_MI_ID("FeatureTypePsiMiId"),
    /**
     * Feature Type Intact Id
     */
    FEATURE_TYPE_INTACT_ID("FeatureTypeIntactId"),
    /**
     * Feature Type PubMed Id
     */
    FEATURE_TYPE_PUBMED_ID("FeatureTypePubMedId"),
    /**
     * Intact Feature Id, @id found in primaryRef of xref of feature where @db
     * is intact
     */
    FEATURE_INTACT_ID("FeatureIntactId"),
    XREF("Xref"),
    PROTEIN_ALIASES("ProteinAliases"),
    PEPTIDE_ALIASES("PeptideAliases"),
    SMALL_MOLECULE_ALIASES("SmallMoleculeAliases"),
    COMPLEX_ALIAS("ComplexAlias"),
    /**
     * Full text index for secondary uniprot references for a protein
     */
    PROTEIN_UNIPROT_SECONDARY_REFS("ProteinUniprotSecondaryRefs"),
    /**
     * Full text index for secondary uniprot references for a peptide
     */
    PEPTIDE_UNIPROT_SECONDARY_REFS("PeptideUniprotSecondaryRefs"),
    /**
     * Full text index for secondary uniprot references for a small molecule
     */
    SMALL_MOLECULE_UNIPROT_SECONDARY_REFS("SmallMoleculeUniprotSecondaryRefs"),
    /**
     * Full text index for secondary interpro references for a protein
     */
    PROTEIN_INTERPRO_SECONDARY_REFS("ProteinInterproSecondaryRefs"),
    /**
     * Full text index for secondary interpro references for a peptide
     */
    PEPTIDE_INTERPRO_SECONDARY_REFS("PeptideInterproSecondaryRefs"),
    SMALL_MOLECULE_INTERPRO_SECONDARY_REFS("SmallMoleculeInterproSecondaryRefs"),
    /**
     * Full text index for secondary gene ontology references for a protein
     */
    PROTEIN_GO_SECONDARY_REFS("ProteinGoSecondaryRefs"),
    /**
     * Full text index for secondary gene ontology references for a peptide
     */
    PEPTIDE_GO_SECONDARY_REFS("PeptideGoSecondaryRefs"),
    SMALL_MOLECULE_GO_SECONDARY_REFS("SmallMoleculeGoSecondaryRefs"),
    /**
     * Full text index for secondary Refseq references for a protein
     */
    PROTEIN_REFSEQ_SECONDARY_REFS("ProteinRefseqSecondaryRefs"),
    /**
     * Full text index for secondary Refseq references for a peptide
     */
    PEPTIDE_REFSEQ_SECONDARY_REFS("PeptideRefseqSecondaryRefs"),
    SMALL_MOLECULE_REFSEQ_SECONDARY_REFS("SmallMoleculeRefseqSecondaryRefs"),
    /**
     * Full text index for secondary IPI references for a protein
     */
    PROTEIN_IPI_SECONDARY_REFS("ProteinIpiSecondaryRefs"),
    /**
     * Full text index for secondary IPI references for a peptide
     */
    PEPTIDE_IPI_SECONDARY_REFS("PeptideIpiSecondaryRefs"),
    SMALL_MOLECULE_IPI_SECONDARY_REFS("SmallMoleculeIpiSecondaryRefs"),
    /**
     * Full text index for secondary Intact references for a protein
     */
    PROTEIN_INTACT_SECONDARY_REFS("ProteinIntactSecondaryRefs"),
    /**
     * Full text index for secondary Intact references for a peptide
     */
    PEPTIDE_INTACT_SECONDARY_REFS("PeptideIntactSecondaryRefs"),
    SMALL_MOLECULE_INTACT_SECONDARY_REFS("SmallMoleculeIntactSecondaryRefs"),
    /**
     * Full text index for secondary Ensemble references for a protein
     */
    PROTEIN_ENSEMBL_SECONDARY_REFS("ProteinEnsemblSecondaryRefs"),
    PROTEIN_SHORT_LABEL("ProteinShortLabel"),
    PROTEIN_FULL_NAME("ProteinFullName"),
    PROTEIN_SHORT_NAME("ProteinShortName"),
    PROTEIN_NAMES("ProteinNames"),
    MESSAGE("Message"),
    /**
     * Full text index for secondary Ensemble references for a peptide
     */
    PEPTIDE_ENSEMBL_SECONDARY_REFS("PeptideEnsemblSecondaryRefs"),
    PEPTIDE_SHORT_LABEL("PeptideShortLabel"),
    PEPTIDE_FULL_NAME("PeptideFullName"),
    SMALL_MOLECULE_ENSEMBL_SECONDARY_REFS("SmallMoleculeEnsemblSecondaryRefs"),
    SMALL_MOLECULE_SHORT_LABEL("SmallMoleculeShortLabel"),
    SMALL_MOLECULE_FULL_NAME("SmallMoleculeFullName"),
    /**
     * Intact interaction ID that uniquely identifies an Intact interaction.
     */
    INTACT_INTERACTION_ID("IntactInteractionId"),
    /**
     * Intact experiment ID that uniquely identifies an Intact experiment.
     */
    INTACT_EXPERIMENT_ID("IntactExperimentId"),
    /**
     * Short Label of the Intact Interaction
     */
    INTACT_INTERACTION_SHORT_LABEL("IntactInteractionShortLabel"),
    /**
     * Short Label of the Intact Experiment
     */
    INTACT_EXPERIMENT_SHORT_LABEL("IntactExperimentShortLabel"),
    /**
     * Full Name of the Intact Interaction
     */
    INTACT_INTERACTION_FULL_NAME("IntactInteractionFullName"),
    /**
     * Interaction detection method short label as in an Intact experiment.
     */
    INTERACTION_DETECTION_METHOD_SHORT_LABEL("InteractionDetectionMethodShortLabel"),
    /**
     * Interaction detection method full name as in an Intact experiment.
     */
    INTERACTION_DETECTION_METHOD_FULL_NAME("InteractionDetectionMethodFullName"),
    /**
     * Interaction detection method short label as in an Intact experiment.
     */
    PARTICIPANT_IDENTIFICATION_METHOD_SHORT_LABEL("ParticipantIdentificationMethodShortLabel"),
    /**
     * Interaction detection method full name as in an Intact experiment.
     */
    PARTICIPANT_IDENTIFICATION_METHOD_FULL_NAME("ParticipantIdentificationMethodFullName"),
    /**
     * Interaction detection method alias as in an Intact experiment.
     */
    INTERACTION_DETECTION_METHOD_ALIAS("InteractionDetectionMethodAlias"),
    /**
     * Participant identification method alias as in an Intact experiment.
     */
    PARTICIPANT_IDENTIFICATION_METHOD_ALIAS("ParticipantIdentificationMethodAlias"),
    /**
     * Full Name of the Intact Experiment
     */
    INTACT_EXPERIMENT_FULL_NAME("IntactExperimentFullName"),
    /**
     * PubMed Id
     */
    PUBMED_ID("PubMedId"),
    /**
     * Experiment Dataset attribute
     */
    EXPERIMENT_DATASET("dataset"),
    /**
     * Experiment comment attribute in an Intact experiment
     */
    EXPERIMENT_COMMENT("ExperimentComment"),
    /**
     * Experiment library attribute in an Intact experiment
     */
    EXPERIMENT_LIBRARY("ExperimentLibrary"),
    /**
     * Experiment url attribute in an Intact experiment
     */
    EXPERIMENT_URL("ExperimentUrl"),
    /**
     * author-list attribute in an Intact experiment
     */
    AUTHOR_LIST("AuthorList"),
    /**
     * journal attribute in an Intact experiment
     */
    JOURNAL("Journal"),
    /**
     * publication year attribute in Intact experiment
     */
    PUBLICATION_YEAR("PublicationYear"),
    /**
     * data-processing attribute in an Intact experiment
     */
    EXPERIMENT_DATA_PROCESSING("ExperimentDataProcessing"),
    /**
     * The @id field in primary xref of Intact. Eg. EBI-65845
     */
    INTACT_SOURCE_ID("IntactSourceId"),
    /**
     * A full-text index of @db AFCS @id fields
     */
    INTACT_AFCS_SECONDARY_REFS("IntactAFCSSecondaryRefs"),
    /**
     * Intact experiment ref
     */
    INTACT_EXPERIMENT_REF("IntactExperimentRef"),
    IDM_EXPERIMENT_REF("IDMExperimentRef"),
    PIM_EXPERIMENT_REF("PIMExperimentRef"),
    EXPERIMENT_ATTRIBUTES_EXPERIMENT_REF("ExperimentAttributesExperimentRef"),
    EXPERIMENT_COMMENT_EXPERIMENT_REF("ExperimentCommentExperimentRef"),
    EXPERIMENT_DATAPROC_EXPERIMENT_REF("ExperimentDataProcExperimentRef"),
    EXPERIMENT_DATASET_EXPERIMENT_REF("ExperimentDatasetExperimentRef"),
    EXPERIMENT_LIBRARY_EXPERIMENT_REF("ExperimentLibraryExperimentRef"),
    EXPERIMENT_URL_EXPERIMENT_REF("ExperimentUrlExperimentRef"),
    /**
     * Pathway short name used as index for pathway uniquely indexed
     */
    PATHWAY_SHORT_NAME("PathwayShortName"),
    /**
     * Pathway identifier
     */
    PATHWAY_ID("PathwayId"),
    /**
     * Pathway organism
     */
    PATHWAY_ORGANISM("PathwayOrganism"),
    PATHWAY_LONG_NAME("PathwayLongName"),
    PATHWAY_SOURCE_ID("PathwaySourceid"),
    PATHWAY_SOURCE_TEXT("PathwaySourceText"),
    /**
     * full text index for curators names
     */
    PATHWAY_CURATOR_LIST("PathwayCuratorList"),
    /**
     * full text index for reviewer names
     */
    PATHWAY_REVIEWER_LIST("PathwayReviewerList"),


    /**
     * Short label of the participant in the Intact experiment's participantId
     */
    PARTICIPANT_SHORT_LABEL("ParticipantShortLabel"),
    PROTEIN_ORGANISM_SHORT_LABEL("ProteinOrganismShortLabel"),
    PROTEIN_ORGANISM_FULL_NAME("ProteinOrganismFullName"),
    PEPTIDE_ORGANISM_SHORT_LABEL("PeptideOrganismShortLabel"),
    PEPTIDE_ORGANISM_FULL_NAME("PeptideOrganismFullName"),
    SMALL_MOLECULE_ORGANISM_SHORT_LABEL("SmallMoleculeOrganismShortLabel"),
    SMALL_MOLECULE_ORGANISM_FULL_NAME("SmallMoleculeOrganismFullName"),
    /**
     * We assume that the @id field in xref primary of a participant is always
     * associated with @db of intact. This assumption may prove to be invalid,
     * as we encounter more data.
     */
    INTACT_PARTICIPANT_ID("IntactParticipantId"),
    EXPERIMENT_ROLE_PARTICIPANT_ID("ExperimentRoleParticipantId"),
    /**
     * An interaction is associated with one or more experiments
     * (experimentRef), and one or more participants (participantId). A
     * participantId is related with a list of experimental roles
     * (experimentRoleList) and one biological role (biologicalRole), and one
     * interactor (interactorRef/interactorId). A participantId is unique across
     * all experiments. But an interactorId can be involved with many different
     * participants (participantId). Also, an interaction is unique and is not
     * associated with more than one experimentId.
     */
    PARTICIPANT_ID("ParticipantId"),
    BIOLOGICAL_ROLE_PARTICIPANT_ID("BiologicalRoleParticipantId"),
    /**
     * InteractionId is associated with a given interaction that consists of
     * interactions component lists and molecules.
     */
    NCI_PATHWAY_INTERACTION_ID("NciPathwayInteractionId"),
    /**
     * This can refer to modification, transcription, tumor cell invasion,
     * hemidesmosome assembly, cell migration,
     */
    NCI_PATHWAY_INTERACTION_TYPE("NciPathwayInteractionType"),
    /**
     * Inferred from mutant phenotype(IMP), direct assay(IDA), physical
     * interaction (IPI)
     */
    EVIDENCE_LIST("NciPathwayInteractionEvidenceList"),
    /*
     * @pmid - pubmedid
     */
    REFERENCE_LIST("NciPathwayInteractionReferenceList"),
    /**
     * Ligand binding, cell adhesion, apoptosis
     */
    NCI_PATHWAY_INTERACTION_CONDITION_TYPE("NciPathwayInteractionConditionType"),
    NCI_PATHWAY_INTERACTION_CONDITION_TEXT("NciPathwayInteractionConditionText"),
    GENE_ONTOLOGY_ID("GeneOntologyId"),
    PROCESS_TYPE("ProcessType"),
    PROTEIN_PREFERRED_SYMBOL("ProteinPreferredSymbol"),
    PART_PROTEIN_PREFERRED_SYMBOL("PartProteinPreferredSymbol"),
    LOCATION("Location"),
    ACTIVITY_STATE("Activity State"),
    FUNCTION("Function"),
    COMPOUND_PREFERRED_SYMBOL("CompoundPreferredSymbol"),
    CHEMICAL_ABSTRACT_ID("ChemicalAbstractId"),
    COMPLEX_PREFERRED_SYMBOL("ComplexPreferredSymbol"),
    NAMED_PROTEIN_PREFERRED_SYMBOL("NamedProteinPreferredSymbol"),
    RNA_PREFERRED_SYMBOL("RnaPreferredSymbol"),
    IS_CONDITION_POSITIVE("IsConditionPositive"),
    PUBMED_STATEMENT("PubMedStatement"),
    DISEASE_CODE("DiseaseCode"),
    NEGATION_INDICATOR("NegationIndicator"),
    CELLINE_INDICATOR("CellineIndicator"),
    DISEASE_TERM("DiseaseTerm"),
    TERM_NAME("TermName"),
    GENE_STATUS_FLAG("GeneStatusFlag"),
    EXACT_SYNONYMS("ExactSynonyms"),
    BROAD_SYNONYMS("BroadSynonyms"),
    NARROW_SYNONYMS("NarrowSynonyms"),
    IS_OBSOLETE("IsObsolete"),
    ENZYME_ID("EnzymeId"),
    ENZYME_NAME("EnzymeName"),
    COMMENT("comment"),
    ENZYME_ACCEPTED_NAMES("EnzymeAcceptedNames"),
    ENZYME_OTHER_NAMES("EnzymeOtherNames"),
    ENZYME_REF_LINKS("EnzymeRefLinks"),
    ENZYME_REACTIONS("EnzymeReactions"),
    ENZYME_COMMENTS("EnzymeComments"),
    ENZYME_SYSTEMATIC_NAMES("EnzymeSystematicNames"),
    ENZYME_GLOSSARY("EnzymeGlossary"),
    ENZYME_LAST_CHANGE("EnzymeLastChange"),
    ENZYME_REACTION("EnzymeReaction"),
    ENZYME_COFACTOR("ENZYME_COFACTOR"),

    CELL_TYPE_ONTOLOGY_ID("CellTypeOntologyId"),
    CELL_TYPE_RELATED_SYNONYMS("CellTypeRelatedSynonyms"),
    CELL_TYPE_EXACT_SYNONYMS("CellTypeExactSynonyms"),
    CELL_TYPE_NARROW_SYNONYMS("CellTypeNarrowSynonyms"),
    CELL_TYPE_SYNONYMS("CellTypeSynonyms"),
    CELL_TYPE_ONTOLOGY_NAME("CellTypeOntologyName"),
    CELL_TYPE_ONTOLOGY_NAMESPACE("CellTypeOntologyNameSpace"),
    CELL_TYPE_ONTOLOGY_ALT_ID("CellTypeOntologyAltId"),
    CELL_TYPE_ONTOLOGY_DEF("CellTypeOntologyDef"),
    GENE_ONTOLOGY_ALT_ID("GeneOntologyAltId"),
    CELL_TYPE_NAME_SPACE("CellTypeNameSpace"),
    CELL_TYPE_SUBSETS("CellTypeSubSets"),
    CELL_TYPE_ALTERNATE_IDS("CellTypeAlternateIds"),
    CELL_TYPE_IS_OBSOLETE("CellTypeIsObsolete"),


    PROTEIN_ONTOLOGY_ID("ProteinOntologyId"),
    PROTEIN_ONTOLOGY_RELATED_SYNONYMS("ProteinOntologyRelatedSynonyms"),
    PROTEIN_ONTOLOGY_EXACT_SYNONYMS("ProteinOntologyExactSynonyms"),
    PROTEIN_ONTOLOGY_NARROW_SYNONYMS("ProteinIntologyNarrowSynonyms"),
    PROTEIN_ONTOLOGY_SYNONYMS("ProteinOntologySynonyms"),
    PROTEIN_ONTOLOGY_NAME("ProteinOntologyName"),
    PROTEIN_ONTOLOGY_NAMESPACE("ProteinOntologyNameSpace"),
    PROTEIN_ONTOLOGY_ALT_ID("ProteinOntologyAltId"),
    PROTEIN_ONTOLOGY_DEF("ProteinOntologyDef"),
    PROTEIN_ONTOLOGY_NAME_SPACE("ProteinOntologyNameSpace"),
    PROTEIN_ONTOLOGY_IS_OBSOLETE("ProteinOntologyIsObsolete"),
    PROTEIN_ONTOLOGY_CONSIDER("ProteinOntologyConsider"),
    PROTEIN_ONTOLOGY_SUBSETS("ProteinOntologySubSets"),


    CHEBI_PROPERTY_VALUE("ChebiPropertyValue"),
    CHEBI_ONTOLOGY_DEF("ChebiOntologyDef"),
    CHEBI_ONTOLOGY_NAME("ChebiOntologyName"),
    CHEBI_RELATED_NAME("ChebiRelatedName"),
    CHEBI_EXACT_SYNONYMS("ChebiExactSynonyms"),
    CHEBI_ALTERNATE_IDS("ChebiAlternateIds"),
    CHEBI_NARROW_SYNONYMS("ChebiNarrowSynonyms"),
    CHEBI_ONTOLOGY_ID("ChebiOntologyId"),
    CHEBI_NAME_SPACE("ChebiNameSpace"),
    CHEBI_RELATED_SYNONYMS("ChebiRelatedSynoyms"),


    PROTEIN_MOD_ONTOLOGY_ID("ProteinModOntologyId"),
    PROTEIN_MOD_ONTOLOGY_RELATED_SYNONYMS("ProteinModOntologyRelatedSynonyms"),
    PROTEIN_MOD_ONTOLOGY_EXACT_SYNONYMS("ProteinModOntologyExactSynonyms"),
    PROTEIN_MOD_ONTOLOGY_SYNONYMS("ProteinModOntologySynonyms"),
    PROTEIN_MOD_ONTOLOGY_NAME("ProteinModOntologyName"),
    PROTEIN_MOD_ONTOLOGY_ALT_ID("ProteinModOntologyAltId"),
    PROTEIN_MOD_ONTOLOGY_DEF("ProteinModOntologyDef"),
    PROTEIN_MOD_ONTOLOGY_SUBSETS("ProteinModificaitonOntologySubSets"),
    PROTEIN_MOD_ONTOLOGY_IS_OBSOLETE("ProteinModOntologyIsObsolete"),
    DIFF_AVG("DiffAvg"),
    DIFF_FORMULA("DiffFormula"),
    DIFF_MONO("DiffMono"),
    FORMULA("Formula"),
    MASSAVG("MassAvg"),
    MASSMONO("MassMono"),
    ORIGIN("Origin"),
    SOURCE("Source"),
    TERMSPEC("TermSpecification"),

    SYSTEMS_BIOLOGY_ONTOLOGY_ID("SystemsBiologyOntologyId"),
    SYSTEMS_BIOLOGY_ONTOLOGY_NAME("SystemsBiologyOntologyName"),
    SYSTEMS_BIOLOGY_ONTOLOGY_SYNONYMS("SystemsBiologyOntologySynonyms"),
    SYSTEMS_BIOLOGY_ONTOLOGY_IS_OBSOLETE("SystemsBiologyOntologyIsObsolete"),
    SYSTEMS_BIOLOGY_ONTOLOGY_DEF("SystemsBiologyOntologyDef"),

    TAXONOMIC_RANK_ONTOLOGY_NAME("TaxonomicRankOntologyName"),
    TAXONOMIC_RANK_ONTOLOGY_ID("TaxonomicRankOntolgyId"),
    TAXONOMIC_RANK_ONTOLOGY_DEF("TaxonomicRankOntologyDef"),
    TAXONOMIC_RANK_ONTOLOGY_EXACT_SYNONYMS("TaxononmicRankOntologyExactSynonyms"),

    SPATIAL_ONTOLOGY_IS_OBSOLETE("SpatialOntologyIsObsolete"),
    SPATIAL_ONTOLOGY_IS_TRANSITIVE("SpatialOntologyIsTransitive"),
    SPATIAL_ONTOLOGY_IS_SYMMETRIC("SpatialOntologyIsSymmetric"),
    SPATIAL_ONTOLOGY_ID("SpatialOntologyId"),
    SPATIAL_ONTOLOGY_ALT_ID("SpatialOntologyAltId"),
    SPATIAL_ONTOLOGY_NAME("SpatialOntologyName"),
    SPATIAL_ONTOLOGY_DEF("SpatialOntologyDef"),
    SPATIAL_ONTOLOGY_NARROW_SYNONYMS("SpatialOntologyNarrowSynonyms"),
    SPATIAL_ONTOLOGY_RELATED_SYNONYMS("SpatialOntologyRelatedSynonyms"),
    SPATIAL_ONTOLOGY_EXACT_SYNONYMS("SpatialOntologyExactSynonyms"),

    ANATOMICAL_ENTITY_ONTOLOGY_ID("AnatomicalEntityOntologyId"),
    ANATOMICAL_ENTITY_ONTOLOGY_DEF("AnatomicalEntityOntologyDef"),
    ANATOMICAL_ENTITY_ONTOLOGY_ALT_ID("AnatomicalEntityOntologyAltId"),
    ANATOMICAL_ENTITY_ONTOLOGY_IS_OBSOLETE("AnatomicalEntityOntologyIsObsolete"),
    ANATOMICAL_ENTITY_ONTOLOGY_EXACT_SYNONYMS("AnatomicalEntityOntologyExactSynonyms"),
    ANATOMICAL_ENTITY_ONTOLOGY_RELATED_SYNONYMS("AnatomicalEntityOntologyRelatedSynonyms"),
    ANATOMICAL_ENTITY_ONTOLOGY_NARROW_SYNONYMS("AnatomicalEntityOntologyNarrowSynonyms"),
    ANATOMICAL_ENTITY_ONTOLOGY_NAME("AnatomicalEntityOntologyName"),

    /**
     * Common Anatomy reference ontology (CARO)
     */
    CARO_ID("CAROId"),
    CARO_ALT_ID("CAROAltId"),
    CARO_IS_OBSOLETE("CAROIsObsolete"),
    CARO_DEF("CARODef"),
    CARO_EXACT_SYNONYMS("CAROExactSynonyms"),
    CARO_RELATED_SYNONYMS("CARORelatedSynonyms"),
    CARO_NARROW_SYNONYMS("CARONarrowSynonyms"),
    CARO_NAME("CAROName"),

    /**
     * Anatomical entity Ontology
     */
    AHDA_ID("AHDAId"),
    AHDA_NAME("AHDAName"),
    AHDA_IS_OBSOLETE("AHDAIsObsolete"),
    AHDA_EXACT_SYNONYMS("AHDAExactSynonyms"),

    /**
     * carnegie state
     */
    CS_ID("CSId"),
    CS_NAME("CSName"),

    HUMAN_DISEASE_ONTOLOGY_ID("HumanDiseaseOntologyId"),
    HUMAN_DISEASE_ONTOLOGY_DEF("HumanDiseaseOntologyDef"),
    HUMAN_DISEASE_ONTOLOGY_ALT_ID("HumanDiseaseOntologyAltId"),
    HUMAN_DISEASE_ONTOLOGY_IS_OBSOLETE("HumanDiseaseOntologyIsObsolete"),
    HUMAN_DISEASE_ONTOLOGY_EXACT_SYNONYMS("HumanDiseaseOntologyExactSynonyms"),
    HUMAN_DISEASE_ONTOLOGY_RELATED_SYNONYMS("HumanDiseaseOntologyRelatedSynonyms"),
    HUMAN_DISEASE_ONTOLOGY_NARROW_SYNONYMS("HumanDiseaseOntologyNarrowSynonyms"),
    HUMAN_DISEASE_ONTOLOGY_NAME("HumanDiseaseOntologyName"),
    HUMAN_DISEASE_ONTOLOGY_SUBSETS("HumanDiseaseOntologySubsets"),

    HOMOLOGY_ONTOLOGY_ID("HomologyOntologyId"),
    HOMOLOGY_ONTOLOGY_DEF("HomologyOntologyDef"),
    HOMOLOGY_ONTOLOGY_IS_OBSOLETE("HomologyOntologyIsObsolete"),
    HOMOLOGY_ONTOLOGY_EXACT_SYNONYMS("HomologyOntologyExactSynonyms"),
    HOMOLOGY_ONTOLOGY_RELATED_SYNONYMS("HomologyOntologyRelatedSynonyms"),
    HOMOLOGY_ONTOLOGY_NAME("HomologyOntologyName"),

    RNA_ONTOLOGY_ID("RNAOntologyId"),
    RNA_ONTOLOGY_DEF("RNAOntologyDef"),
    RNA_ONTOLOGY_NAME("RNAOntologyName"),

    SYMPTOM_ONTOLOGY_ID("SymptomOntologyId"),
    SYMPTOM_ONTOLOGY_NAME("SymptomOntologyName"),
    SYMPTOM_ONTOLOGY_DEF("SymptomOntologyDef"),
    SYMPTOM_ONTOLOGY_EXACT_SYNONYMS("SymptomOntologyExactSynonyms"),
    SYMPTOM_ONTOLOGY_RELATED_SYNONYMS("SymptomOntologyRelatedSynonyms"),
    CREATED_BY("CreatedBy"),
    CREATION_DATE("CreationDate"),

    PHENOTYPIC_ONTOLOGY_ID("PhenotypicOntologyId"),
    PHENOTYPIC_SYNONYMS("PhenotypicSynonyms"),
    PHENOTYPIC_NAMESPACE("PhenotypicNameSpace"),
    PHENOTYPIC_ONTOLOGY_NAME("PhenotypicOntologyName"),
    PHENOTYPIC_ONTOLOGY_ALT_ID("PhenotypicOntologyAltId"),
    PHENOTYPIC_ONTOLOGY_DEF("PhenotypicOntologyDef"),
    PHENOTYPIC_RELATED_SYNONYMS("PhenotypicRelatedSynonyms"),
    PHENOTYPIC_EXACT_SYNONYMS("PhenotypicExactSynonyms"),
    PHENOTYPIC_NARROW_SYNONYMS("PhenotypicNarrowSynonyms"),
    PHENOTYPIC_SUBSETS("PhenotypicSubSets"),
    PHENOTYPIC_ALTERNATE_IDS("PhenotypicAlternateIds"),
    PHENOTYPIC_IS_OBSOLETE("PhenotypicIsObsolete"),

    CYTOPLASM_ONTOLOGY_ID("CytoplasmOntologyId"),
    CYTOPLASM_SYNONYMS("CytoplasmSynonyms"),
    CYTOPLASM_NAMESPACE("CytoplasmNameSpace"),
    CYTOPLASM_ONTOLOGY_NAME("CytoplasmOntologyName"),
    CYTOPLASM_ONTOLOGY_DEF("CytoplasmOntologyDef"),
    CYTOPLASM_NARROW_SYNONYMS("CytoplasmNarrowSynonyms"),

    UNIPROT_ENTRY_TYPE("UniprotEntryType"),

    UNIPROT_KEYWORDS("UniprotKeywords"),
     ENTRY_VERSION("EntryVersion"),

    /**
     * First public date in EntryAudit of UniProt object.
     */
    FIRST_PUBLIC_DATE("FirstPublicDate"),

    /**
     * Last annotation update date in EntryAudit of UniProt object.
     */
    LAST_ANNOTATION_UPDATE_DATE("LastAnnotationUpdateDate"),

    /**
     * Last sequence update date in EntryAudit of UniProt object.
     */
    LAST_SEQUENCE_UPDATE_DATE("LastSequenceUpdateDate"),

    /**
     * Sequence version in EntryAudit of UniProt object.
     */
    SEQUENCE_VERSION("SequenceVersion"),

    FEATURE_LOCATION("FeatureLocation"),

    SEQUENCE_ID("SequenceId"),

    SEQUENCE_SUM("SequenceSum"),

    /**
     * The feature description could be "TNFAIP3-interacting protein 2"
     */
    FEATURE_DESCRIPTION("FeatureDescription"),

    /**
     * FeatureStatus in UniProt
     */
    FEATURE_STATUS("FeatureStatus"),

    FEATURE_INDEX("FeatureIndex"),

    /**
     * FeatureType in UniProt
     */
    FEATURE_TYPE("FeatureType"),

    /**
     * Feature list in UniProt.
     */
    FEATURE_LIST("FeatureList"),

    START_POSITION("StartPosition"),

    END_POSITION("EndPosition"),

    SEQUENCE_VALUE("SequenceValue"),

    MOLECULAR_WEIGHT("MolecularWeight"),

    SEQUENCE_LENGTH("SequenceLength"),

    CITATION_TITLE("citationTitle"),
    /**
     * Publication date of Citation in Uniprot object.
     */
    CITATION_PUBLICATION_DATE("citationPublicationDate"),
    SAMPLE_SOURCES("sampleSources"),
    CITATION_REFERENCE_DOI("citationReferenceDoi"),
    AUTHORING_GROUPS("authoringGroups"),
    SUMMARY_GROUP("summaryGroup"),
    SAMPLE_EVIDENCE_IDS("sampleEvidenceIds"),
    SAMPLE_SOURCE("sampleSource"),
    SAMPLE_SOURCE_TYPE("sampleSourceType"),
    SAMPLE_INDEX("sampleIndex"),
    EVIDENCE_IDS("evidenceIds"),
    SUMMARY_LIST("summaryList"),
    CITATION_TYPE("citationType"),
    PUBLICATION_DATE("publicationDate"),
    CITATION_INDEX("citationIndex"),
    
    COMMENT_ID("commentId"),
    COMMENT_STATUS("commentStatus"),
    COMMENT_TYPE("commentType"),
    EVIDENCE_ID("evidenceId"),

    /* drug price */
    UNIT("unit"),

    LOG_P("logP"),
    ;
    
    private final String value;
    private static final Map<String, IndexNames> stringToEnum = new HashMap<String, IndexNames>();

    static { // init map from constant name to enum constant
        for (IndexNames en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    IndexNames(String value) {
        this.value = value;
    }

    public boolean equals(String indexName) {
        return value.equals(indexName);
    }

    public boolean equals(IndexNames indexName) {
        return value.equals(indexName.toString());
    }

    @Override
    public String toString() {
        return value;
    }

    public static IndexNames fromString(String value) {
        return stringToEnum.get(value);
    }
}
