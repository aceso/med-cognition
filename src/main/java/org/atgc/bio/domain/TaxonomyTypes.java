/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import java.util.HashMap;
import java.util.Map;

/**
 * Taxonomy 5 dimensional classification for random user search.
 * These labels will be displayed in the GUI, so they need to be
 * user friendly.
 *
 * @author jtanisha-ee
 */

public enum TaxonomyTypes {

    /**
     * Uniprot ID
     */
    UNIPROT_ID("Uniprot ID"),

    /**
     * PDB structures (identified by structureId) have chains (identified by chainId)
     */
    CHAIN_ID("Chain Id"),
    
    /**
     * Drug packager's name.
     */
    DRUG_PACKAGER_NAME("Drug Packager Name"),
    
    /**
     * Drug manufacturer's name.
     */
    DRUG_MANUFACTURER_NAME("Drug Manufacturer Name"),
    
    /**
     * Drug packager message
     */
    DRUG_PACKAGER_MESSAGE("Drug Packager Message"),
    
    /**
     * Drug manufacturer message
     */
    DRUG_MANUFACTURER_MESSAGE("Drug Manufacturer Message"),
    
    /**
     * Drug price description
     */
    DRUG_PRICE_DESCRIPTION("Drug Price Description"),
    
    /**
     * Drug categories
     */
    DRUG_CATEGORIES("Drug Categories"),
    
    /**
     * Drug patent number
     */
    DRUG_PATENT_NUMBER("Drug Patent Number"),
    
    /**
     * Drug patent country
     */
    DRUG_PATENT_COUNTRY("Drug Patent Country"),
    
    DRUG_PATENT_MESSAGE("Drug Patent Message"),
    
    /**
     * Message label for Chemical Property
     */
    CHEMICAL_PROPERTY_MESSAGE("Chemical Property Message"),
    
    /**
     * Message label for Experimental Property
     */
    EXPERIMENTAL_PROPERTY_MESSAGE("Experimental Property Message"),
    
    /**
     * Dosage message
     */
    DOSAGE_MESSAGE("Dosage Message"),
    
    /**
     * Is this drug generic or not. True or false.
     */
    DRUG_GENERIC("Drug Generic"),
    
    /**
     * Drug synonyms
     */
    DRUG_SYNONYMS("Drug Synonyms"),
    
    /**
     * Drug brands
     */
    DRUG_BRANDS("Drug Brands"),
    
    /**
     * Drug mixtures
     */
    DRUG_MIXTURES("Drug Mixtures"),
    
    /**
     * Drug salts
     */
    DRUG_SALTS("Drug Salts"),
    
    /**
     * Drug packager's url
     */
    DRUG_PACKAGER_URL("Drug Packager Url"),

    /**
     * Drug bank id
     */
    DRUG_BANK_ID("Drug Bank Id"),

    /**
     * Drug name from DrugBank
     */
    DRUG_NAME("Drug Name"),

    /**
     * Drug description from DrugBank
     */
    DRUG_DESCRIPTION("Drug Description"),

    /**
     *
     */
    STRUCTURE_METHOD("Structural Method"),

    /**
     *
     */
    PDB_COMPOUND_ATCC("PdbCompoundAtcc"),

    /**
     *
     */
    PDB_COMPOUND_BIOLOGICAL_UNIT("PdbCompoundBiologicalUnit"),

    /**
     *
     */
    PDB_COMPOUND_CELLULAR_LOCATION("PdbCompoundCellularLocation"),

    /**
     *
     */
    PDB_COMPOUND_EC_NUMS("PdbCompoundEcNums"),

    /**
     *
     */
    PDB_COMPOUND_MOL_ID("PdbCompoundMolId"),

    /**
     *
     */
    PDB_COMPOUND_ORGANISM_FULL_NAME("PdbCompoundOrganismFullName"),

    /**
     *
     */
    PDB_COMPOUND_MOLECULE_NAME("PdbCompoundMoleculeName"),

    /**
     *
     */
    PDB_ENTITY_ENTITY_ID("PdbEntityEntityId"),

    /**
     *
     */
    PDB_ENTITY_STRUCTURE_ID("PdbEntityStructureId"),

    /**
     *
     */
    PDB_COMPOUND_TAXONOMY_ID("PdbCompoundTaxonomyId"),

    /**
     *
     */
    LIGAND_CHEMICAL_NAME("Ligand Chemical Name"),

    /**
     *
     */
    LIGAND_INCHI_KEY("Ligand Inchi Key"),

    /**
     *
     */
    LIGAND_INCHI("Ligand Inchi"),

    /**
     *
     */
    LIGAND_FORMULA("Ligand Formula"),

    /**
     *
     */
    STRUCTURE_EXP_METHOD("Structure Experimental Method"),

    /**
     *
     */
    STRUCTURE_KEYWORDS("Structure keywords"),

    /**
     *
     */
    STRUCTURE_MESSAGE("Structure message"),

    /**
     *
     */
    PDBGO_CHAIN_ID("PdbGo Chain Id"),

    /**
     *
     */
    LIGAND_ENTITY_TYPE("Entity Type of Ligand"),

    /**
     *
     */
    LIGAND_MOLECULAR_WEIGHT("Ligand Molecular Weight"),

    /**
     *
     */
    CHEMICAL_ID("Chemical Id"),

    /**
     *
     */
    PDBGO_MESSAGE("PdbGo message"),

    /**
     *
     */
    PUBMED_CENTRAL_ID("Pubmed Central Id (PMCID"),

    /**
     * Structure ID as in PDB.
     */
    STRUCTURE_ID("Structure ID"),

    /**
     * HGNC ID  - Human genome nomenclature for diseases
     */
    HGNC_ID("Hgnc ID"),

    /**
     *
     */
    GENE_MESSAGE("Gene Message"),

    /**
     *
     */
    GENE2GO_MESSAGE("Gene2Go Message"),

    /**
     *
     */
    GENE2GOONTOLOGY_MESSAGE("Gene2GoOntology Message"),

    /**
     *
     */
    TAXONOMY_MESSAGE("Taxonomy Message"),

    /**
     * HUGO_GENE_SYMBOL is the gene Name
     * @deprecated
     */
    HUGO_GENE_SYMBOL("Hugo Gene Symbol"),

    /**
     *
     */
    GENE_SYMBOL("Gene Symbol"),

    /**
     *
     */
    TAXONOMY_RANK("Taxonomy Rank"),

    /**
     *
     */
    NCBI_TAXONOMY_RELATED_SYNONYMS("NCBI Taxonomy Related Synonyms"),

    /**
     *
     */
    NCBI_TAXONOMY_EXACT_SYNONYMS("NcbiTaxonomyExactSynonyms"),

    /**
     *
     */
    NCBI_TAXONOMY_NAME("NCBI Taxonomy Name"),

    /**
     * NCBI GeneID field as used in NCBI gene.
     */
    NCBI_GENE_ID("NCBI Gene ID"),

    /**
     * Locus Tag as found in NCBI gene.
     */
    LOCUS_TAG("Locus Tag"),

    /**
     * Type of Gene as found in NCBI gene.
     */
    TYPE_OF_GENE("Type of Gene"),

    /**
     * Modification date of gene in NCBI gene.
     */
    GENE_MOD_DATE("Modification date of the NCBI gene"),

    /**
     * Name of the GO field. For eg. GO:0009941
     */
    GENE_ONTOLOGY_NAME("Name of the GO field"),

    /**
     *
     */
    GENE_ONTOLOGY_NAMESPACE("Namespace of the GO field"),

    /**
     * Owner field in Pubmed eg. NLM
     */
    PUBMED_OWNER("Pubmed owner"),

    /**
     *
     */
    PUBMED_CREATED_DATE("Date created"),

    /**
     *
     */
    PUBMED_COMPLETED_DATE("Date completed"),

    /**
     *
     */
    PUBMED_ARTICLE_TITLE("Title of the Pubmed article"),

    /**
     *
     */
    PUBLICATION_TYPE_LIST("Publication Type List"),

    /**
     *
     */
    MAJOR_TOPICS("Major topics in Pubmed"),

    /**
     *
     */
    NOT_MAJOR_TOPICS("Not major topics in Pubmed"),

    /**
     *
     */
    ARTICLE_ABSTRACT("Article abstract"),

    /**
     *
     */
    ISSN("ISSN number of the journal"),

    /**
     *
     */
    JOURNAL_TITLE("Journal title"),

    /**
     *
     */
    ISO_ABBREVIATION("ISO Abbreviation"),

    /**
     *
     */
    CITED_MEDIUM("Cited medium of journal issue"),

    /**
     * Journal issue volume
     */
    VOLUME("Volume"),

    /**
     * Issue of journal
     */
    ISSUE("Issue"),

    /**
     *
     */
    AUTHOR_VALIDYN("Valid name for author"),

    /**
     *
     */
    AUTHOR_LAST_NAME("Last Name of Author"),

    /**
     *
     */
    AUTHOR_FORE_NAME("First Name of Author"),

    /**
     *
     */
    ARTICLE_TITLE("Title of the Pubmed article"),

    /**
     * version field in Pubmed eg. 1
     */
    PUBMED_VERSION("Pubmed article version"),

    /**
     * status field in Pubmed. eg. MEDLINE
     */
    PUBMED_STATUS("Pubmed status"),

    /**
     * def field found in geneontology
     */
    GENE_ONTOLOGY_DEF("Definition of the GO field"),

    /**
     * NCBI Gene Description
     */
    GENE_DESC("NCBI Gene Description"),

    /**
     * GENE_NAME_LABEL
     */
    GENE_NAME_LABEL("Gene Name Label"),

    /**
     * Gene Aliases
     */
    GENE_ALIASES("Gene Aliases"),

    /**
     * Protein Alias
     */
    PROTEIN_ALIAS("Protein Alias"),

    /**
     * Message of the visualization label using @NodeLabel as annotation.
     */
    MESSAGE("Message"),

    /**
     * Nci Pathway
     */
    NCI_PATHWAY_SHORT_LABEL("Nci Pathway Short Label"),

    /**
     * @visual
     */
    ORGANISM_LABEL("Organism Label"),

    /**
     * Peptide Alias
     */
    PEPTIDE_ALIAS("Peptide Alias"),

    /**
     *
     */
    SMALL_MOLECULE_ALIAS("Small Molecule Alias"),

    /**
     * Complex Alias
     */
    COMPLEX_ALIAS("Complex Alias"),

    /**
     *  Interpro ID
     */
    INTERPRO_ID("Interpro ID"),

    /**
     * IPI ID
     */
    IPI_ID("IPI ID"),

    /**
     * Protein Short Label
     */
    PROTEIN_SHORT_LABEL("Protein Short Label"),

    /**
     * Protein Full Name
     */
    PROTEIN_FULL_NAME("Protein Full Name"),

    PROTEIN_SHORT_NAME("Protein Short Name"),

    PROTEIN_NAMES("Protein Names"),

    /**
     * protein sequence
     * Uniprot
     * Sequence Entry
     */
    ENTRY_VERSION("Entry Version"),

    /**
     * First public date in EntryAudit of UniProt object.
     */
    FIRST_PUBLIC_DATE("First Public Date"),

    /**
     * Last annotation update date in EntryAudit of UniProt object.
     */
    LAST_ANNOTATION_UPDATE_DATE("Last Annotation Update Date"),

    /**
     * Last sequence update date in EntryAudit of UniProt object.
     */
    LAST_SEQUENCE_UPDATE_DATE("Last Sequence Update Date"),

    /**
     * Sequence version in EntryAudit of UniProt object.
     */
    SEQUENCE_VERSION("Sequence Version"),

    SEQUENCE_SUM("Sequence Sum"),

    /**
     * protein sequence
     * Compound key of UniprotId and sequence_sum(crc)
     */
    SEQUENCE_ID("Sequence Id"),


    /**
     * Peptide Short Label
     */
    PEPTIDE_SHORT_LABEL("Peptide Short Label"),

    /**
     *
     */
    SMALL_MOLECULE_SHORT_LABEL("Small Molecule Short Label"),


    /**
     * Peptide Full Name
     */
    PEPTIDE_FULL_NAME("Peptide Full Name"),

    /**
     *
     */
    SMALL_MOLECULE_FULL_NAME("Small Molecule Full Name"),

    /**
     * Interactor ID
     */
    INTERACTOR_ID("Interactor ID"),

    /**
     * Molecule ID Reference
     */
    MOLECULE_IDREF("Molecule ID Reference - InTact"),

    /**
     * Part Molecule Id Reference
     */
    PART_MOLECULE_IDREF("Part Molecule Id Ref"),

    /**
     * Intact ID of the InTact File
     */
    INTACT_ID("Intact ID Of the InTact File"),

    /**
     * Gene Ontology ID
     */
    GO_ID("Gene Ontology ID"),

    /**
     * NciDrugTerm
     */
    NCI_DRUG_TERM("Nci Drug Term"),

    /**
     * NciDrugCode
     */
    NCI_DRUG_CODE("Nci Drug Code"),

    /**
     * Drug label
     */
    DRUG_LABEL("Drug Label"),

    /**
     * Disease label
     */
    DISEASE_LABEL("Disease Label"),



    /**
     * RefSeq ID
     */
    REFSEQ_ID("RefSeq ID"),

    /**
     * Ensembl ID
     */
    ENSEMBL_ID("Ensembl ID"),

    /**
     * NCBI Taxonomy ID of the species
     */
    NCBI_TAX_ID("NCBI Taxonomy ID"),

    /**
     * Short label of the species
     */
    ORGANISM_SHORT_LABEL("Organism or Species Short Label"),

    /**
     * Full name of the species
     */
    ORGANISM_FULL_NAME("Organism or Species Full Name"),

    /**
     * Alliance for Cellular Signalling source Id
     */
    AFCS_SOURCE("Alliance For Cellular Signalling Source"),

    /**
     * Experiment ID in Intact experimentList.
     */
    EXPERIMENT_REF("Experiment ID in Intact Experiment List"),

    /**
     * PubMed ID in Experiment.
     */
    EXPERIMENT_PUBMED_ID("PubMed ID in Experiment"),

    /**
     * An interaction is associated with one or more experiments (experimentRef), and
     * one or more participants (participantId). A participantId is related
     * with a list of experimental roles (experimentRoleList) and one biological role
     * (biologicalRole), and one interactor (interactorRef/interactorId). A
     * participantId is unique across all experiments. But an interactorId can be
     * involved with many different participants (participantId). Also, an interaction
     * is unique and is not associated with more than one experimentId.
     */
    PARTICIPANT_SHORT_LABEL("Short Label of the Participant in Intact Experiments"),

    /**
     * Short Label for Biological Role for Intact Participant Id
     */
    BIOLOGICAL_ROLE_SHORT_LABEL("Short Label of Biological Role for Intact Participant"),

    /**
     * Full Name for Biological Role for Intact Participant Id
     */
    BIOLOGICAL_ROLE_FULL_NAME("Full Name of Biological Role for Intact Participant"),

    /**
     * Biological Role ID from the PSI-MI Database.
     */
    BIOLOGICAL_ROLE_PSI_MI_ID("Biological Role ID from PSI-MI Database."),

    /**
     * Experimental Role ID from the PSI-MI Database.
     */
    EXPERIMENTAL_ROLE_PSI_MI_ID("Experimental Role ID from PSI-MI Database."),

    /**
     * The biological role Id from the Intact database.
     */
    BIOLOGICAL_ROLE_INTACT_ID("Biological Role ID from Intact Database"),

    /**
     * The experimental role Id from the Intact database.
     */
    EXPERIMENTAL_ROLE_INTACT_ID("Experimental Role ID from Intact Database"),

    /**
     * Short Label for Experimental Role in Intact
     */
    EXPERIMENTAL_ROLE_SHORT_LABEL("Short Label for Experimental Role in Intact"),

    /**
     * Full Name for Experimental Role in Intact
     */
    EXPERIMENTAL_ROLE_FULL_NAME("Full Name for Experimental Role in Intact"),

    /**
     * Interaction detection method short label in an Intact experiment.
     */
    INTERACTION_DETECTION_METHOD_SHORT_LABEL("Interaction Detection Method Short Label"),

    /**
     * Interaction detection method full name in an Intact experiment.
     */
    INTERACTION_DETECTION_METHOD_FULL_NAME("Interaction Detection Method Full Name"),

    /**
     * Participant identification method short label in an Intact experiment.
     */
    PARTICIPANT_IDENTIFICATION_METHOD_SHORT_LABEL("Participant Identification Method Short Label"),

    /**
     * Participant identification detection method full name in an Intact experiment.
     */
    PARTICIPANT_IDENTIFICATION_METHOD_FULL_NAME("Participant Identification Method Full Name"),

    /**
     * Interaction detection method aliases in an Intact experiment.
     */
    INTERACTION_DETECTION_METHOD_ALIAS("Interaction Detection Method Aliases"),

    /**
     * Participant identification method aliases in an Intact experiment.
     */
    PARTICIPANT_IDENTIFICATION_METHOD_ALIAS("Participant Identification Method Aliases"),

    /**
     * Experiment dataset attribute in an Intact experiment.
     */
    EXPERIMENT_DATASET("Experiment Dataset"),

    /**
     * Experiment comment attribute in an Intact experiment.
     */
    EXPERIMENT_COMMENT("Experiment Comment"),

    /**
     * library-used attribute in an Intact experiment.
     */
    EXPERIMENT_LIBRARY("Experiment Library"),

    /**
     * url attribute in an Intact experiment
     */
    EXPERIMENT_URL("Experiment Url"),

    /**
     * author-list attribute in an Intact experiment
     */
    AUTHOR_LIST("Author List"),

    /**
     * journal attribute in an Intact experiment
     */
    JOURNAL("Journal"),

    /**
     * Publication year attribute in an Intact experiment.
     */
    PUBLICATION_YEAR("Publication Year"),

    /**
     * data-processing attribute in an Intact experiment
     */
    EXPERIMENT_DATA_PROCESSING("Experiment Data Processing"),

    /**
     * Pubmed accession number
     */
    PUBMED_ID("Pubmed Accession Number"),

    /**
     * The experimental role Id as in PubMed. This pubmed article is just an
     * interaction description. The Id is always 14755292 for PubMed.
     * http://www.ncbi.nlm.nih.gov/pubmed/14755292
     * So we need to think if this variable is required.
     */
    EXPERIMENTAL_ROLE_PUBMED_ID("Experimental Role ID as specified in PubMed"),

    /**
     * The biological role Id as in PubMed. This pubmed article is just an
     * interaction description. The Id is always 14755292 for PubMed.
     * http://www.ncbi.nlm.nih.gov/pubmed/14755292
     * So we need to think if this variable is required.
     */
    BIOLOGICAL_ROLE_PUBMED_ID("Biological Role ID as specified in PubMed"),

    /**
     * Feature as found in an experiment
     */
    FEATURE_ID("Feature in an Experiment"),



    /**
     * Short Label of the Feature in an Experiment
     */
    FEATURE_SHORT_LABEL("Short Label of the Feature in Experiment"),

    /**
     * Short Label of the Experiment
     */
    EXPERIMENT_SHORT_LABEL("Short Label of the Experiment"),

    /**
     * Full Name of the Experiment
     */
    EXPERIMENT_FULL_NAME("Full Name of the Experiment"),

    /**
     * Short Label of feature type
     */
    FEATURE_TYPE_SHORT_LABEL("Short Label of Feature Type in Intact Feature"),

    /**
     * End status short label
     */
    START_STATUS_SHORT_LABEL("Short Label of Start Status of Protein Sequence"),

    /**
     * Full Name of End Status of Protein Sequence
     */
    START_STATUS_FULL_NAME("Full Name of Start Status of Protein Sequence"),

    /**
     * End status short label
     */
    END_STATUS_SHORT_LABEL("Short Label of End Status of Protein Sequence"),

    /**
     * Full Name of End Status of Protein Sequence
     */
    END_STATUS_FULL_NAME("Full Name of End Status of Protein Sequence"),

    /**
     * PSI MI ID
     */
    PSI_MI_ID("PSI MI ID"),

    /**
     * Intact ID of Start Status of Protein Sequence
     */
    START_STATUS_INTACT_ID("Intact ID of Start Status of Protein Sequence"),

    /**
     * PubMed ID of Start Status of Protein Sequence
     */
    START_STATUS_PUBMED_ID("PubMed ID of Start Status of Protein Sequence"),

    /**
     * Intact ID of End Status of Protein Sequence
     */
    END_STATUS_INTACT_ID("Intact ID of End Status of Protein Sequence"),

    /**
     * PubMed ID of End Status of Protein Sequence
     */
    END_STATUS_PUBMED_ID("PubMed ID of End Status of Protein Sequence"),

    /**
     * Full Name of feature type
     */
    FEATURE_TYPE_FULL_NAME("Full Name of Feature Type in Intact Feature"),

    /**
     * Alias Name of Feature Type in Intact Feature
     */
    FEATURE_TYPE_ALIAS("Alias Name of Feature Type in Intact Feature"),

    /**
     * Feature Type PSI MI ID in Intact
     */
    FEATURE_TYPE_PSI_MI_ID("Feature Type PSI MI ID in Intact"),

    /**
     * Feature Type Intact ID
     */
    FEATURE_TYPE_INTACT_ID("Feature Type Intact ID"),

    /**
     * Feature Type Pubmed ID
     */
    FEATURE_TYPE_PUBMED_ID("Feature Type Pubmed ID"),

    /**
     * Intact ID of the Feature in Experiment
     */
    FEATURE_INTACT_ID("Intact ID of the Feature in Experiment"),

    /**
     * We assume that the @id field in xref primary of a participant is always associated
     * with @db of intact. This assumption may prove to be invalid, as we encounter
     * more data.
     */
    INTACT_PARTICIPANT_ID("Xref @id of Intact Participant"),

    /**
     * The Intact interactionId that uniquely identifies an Intact interaction.
     */
    INTACT_INTERACTION_ID("Intact Interaction ID"),

    /**
     * The Intact experimentRef that uniquely identifies an Intact experiment.
     */
    INTACT_EXPERIMENT_REF("Intact Experiment Reference"),

    /**
     * The short label of an Intact Interaction.
     */
    INTACT_INTERACTION_SHORT_LABEL("Intact Interaction Short Label"),

    /**
     * The full name of an Intact Interaction.
     */
    INTACT_INTERACTION_FULL_NAME("Intact Interaction Full Name"),

    /**
     * An interaction is associated with one or more experiments (experimentRef), and
     * one or more participants (participantId). A participantId is related
     * with a list of experimental roles (experimentRoleList) and one biological role
     * (biologicalRole), and one interactor (interactorRef/interactorId). A
     * participantId is unique across all experiments. But an interactorId can be
     * involved with many different participants (participantId). Also, an interaction
     * is unique and is not associated with more than one experimentId.
     */
    PARTICIPANT_ID("Participant ID in Intact Participant List"),

    /**
     * Node type
     */
    NODE_TYPE("Node Type"),

    /**
     *  Pathway identifier in PathwayList
     */
    PATHWAY_ID("Pathway Identifier"),

    /**
     * Pathway short name in PathwayList
     */
    PATHWAY_SHORT_NAME("Pathway Short Name"),

    /**
     *  Pathway organism in PathwayList
     */
    PATHWAY_ORGANISM("Pathway Organism"),

    /**
     * Pathway long name
     */
    PATHWAY_LONG_NAME("Pathway Long Name"),

    /**
     * Pathway source identifier in PathwayList
     */
    PATHWAY_SOURCE_ID("Pathway Source id"),

    /**
     *  Pathway source text in PathwayList
     */
    PATHWAY_SOURCE_TEXT("Pathway SourceText"),

    /**
     * Pathway curator list in PathwayList
     */
    PATHWAY_CURATOR_LIST("Pathway Curator List"),

    /**
     * Pathway reviewer list in PathwayList
     */
    PATHWAY_REVIEWER_LIST("Pathway Reviewer List"),

    /**
     * This can refer to modification, transcription, tumor cell invasion,hemidesmosome assembly,
     * cell migration
     */
    NCI_PATHWAY_INTERACTION_TYPE("Nci Pathway Interaction Type"),

    /**
     *  Inferred from mutant phenotype(IMP), direct assay(IDA), physical interaction (IPI)
     */
    EVIDENCE_LIST("Nci Pathway Interaction Evidence List"),

    /*
     * @pmid - pubmedid
     */

    /**
     *
     */
    REFERENCE_LIST("Nci Pathway Interaction Reference List"),

    /**
     * @id: <number> can refer to a molecule (protein e.t.c)
     */
    NCI_PATHWAY_INTERACTION_ID("Nci Pathway Interaction Identifier"),

    /**
     * Ligand Binding, cell adhesion, apoptosis
     */
    NCI_PATHWAY_INTERACTION_CONDITION_TYPE("Nci Pathway Interacton Condition Type"),

    /**
     *
     */
    NCI_PATHWAY_INTERACTION_CONDITION_TEXT("Nci Pathway Interaction Condition Text"),

    /**
     *
     */
    PROCESS_TYPE("Process Type"),

    /**
     *
     */
    PROTEIN_PREFERRED_SYMBOL("Protein Preferred Symbol"),

    /**
     *
     */
    PART_PROTEIN_PREFERRED_SYMBOL("Part Protein Preferred Symbol"),

    /**
     *
     */
    LOCATION("Location"),

    /**
     *
     */
    FUNCTION("Function"),

    /**
     *
     */
    ACTIVITY_STATE("Activity State"),

    /**
     *
     */
    COMPOUND_PREFERRED_SYMBOL("Compound Preferred Symbol"),

    /**
     *
     */
    CHEMICAL_ABSTRACT_ID("Chemical Abstract Id"),

    FORM("Form"),

    /**
     *
     */
    COMPLEX_PREFERRED_SYMBOL("Complex Preferred Symbol"),

    /**
     *
     */
    NAMED_PROTEIN_PREFERRED_SYMBOL("Named Protein Preferred Symbol"),

    /**
     *
     */
    RNA_PREFERRED_SYMBOL("Rna Preferred Symbol"),

    /**
     *
     */
    ENTREZ_GENE_ID("Entrez Gene Id"),

    /**
     *
     */
    IS_CONDITION_POSITIVE("Is Condition Positive"),

    /**
     *
     */
    GENE_ONTOLOGY_ID("Gene Ontology ID"),

    /**
     *
     */
    PUBMED_STATEMENT("Statement Found in Pubmed"),

    /**
     *
     */
    DISEASE_CODE("Disease Code"),

    /**
     *
     */
    NEGATION_INDICATOR("Negation Indictor"),

    /**
     *
     */
    CELLINE_INDICATOR("Celline Indicator"),

    /**
     *
     */
    DISEASE_TERM("Disease Term"),

    /**
     *
     */
    TERM_NAME("Term Name"),

    /**
     *
     */
    GENE_STATUS_FLAG("Gene Status Flag"),

    /**
     * Evidence Codes
     */
    EV_IC("EV_IC"),

    /**
     *
     */
    EXACT_SYNONYMS("Exact Synonyms"),

    /**
     *
     */
    BROAD_SYNONYMS("Broad Synonyms"),

    /**
     *
     */
    NARROW_SYNONYMS("Narrow Synonyms"),

    /**
     *
     */
    ENZYME_ID("Enzyme Id"),

    /**
     *
     */
    ENZYME_NAME("Enzyme Name"),

    /**
     *
     */
    COMMENT("Comment"),

    /**
     * EVIDENCE_ID
     */
    EVIDENCE_ID("Evidence Id"),

    /**
     *
     */
    GENE_ONTOLOGY_ALT_ID("Gene Ontology Alternative Id"),

    /**
     *
     */
    ENZYME_ACCEPTED_NAMES("Enzyme Accepted Names"),

    /**
     *
     */
    ENZYME_OTHER_NAMES("Enzyme Other Names"),

    /**
     *
     */
    ENZYME_REF_LINKS("Enzyme Ref Links"),

    /**
     *
     */
    ENZYME_REACTIONS("Enzyme Reactions"),

    /**
     *
     */
    ENZYME_COMMENTS("Enzyme Comments"),

    /**
     * non protein part of enzyme is called cofactor
     */
    ENZYME_COFACTOR("ENZYME_COFACTOR"),

    /**
     *
     */
    ENZYME_SYSTEMATIC_NAMES("Enzyme Systematic Names"),

    /**
     *
     */
    ENZYME_GLOSSARY("Enzyme Glossary"),

    /**
     *
     */
    ENZYME_REACTION("Enzyme Reaction"),

    /**
     *
     */
    ENZYME_LAST_CHANGE("Enzyme Last Change"),

    /**
     *
     */
    CELL_TYPE_ONTOLOGY_ID("Cell Type Ontology Id"),

    /**
     *
     */
    CELL_TYPE_SYNONYMS("Cell Type Synonyms"),

    /**
     *
     */
    CELL_TYPE_ONTOLOGY_NAME("Cell Type Ontology Name"),

    /**
     *
     */
    CELL_TYPE_ONTOLOGY_NAMESPACE("Cell Type Ontology Name Space"),

    /**
     *
     */
    CELL_TYPE_ONTOLOGY_ALT_ID("Cell Type Ontology Alternative Identifier"),

    /**
     *
     */
    CELL_TYPE_ONTOLOGY_DEF("Cell Type Ontology Def"),

    /**
     *
     */
    CELL_TYPE_NAME_SPACE("Cell Type Name Space"),

    /**
     *
     */
    CELL_TYPE_RELATED_SYNONYMS("Cell Type Related Synonyms"),

    /**
     *
     */
    CELL_TYPE_EXACT_SYNONYMS("Cell Type Exact Synonyms"),

    /**
     *
     */
    CELL_TYPE_NARROW_SYNONYMS("Cell Type Narrow Synonyms"),

    /**
     *
     */
    CELL_TYPE_SUBSETS("Cell Type SubSets"),

    /**
     *
     */
    CELL_TYPE_ALTERNATE_IDS("Cell Type Alternate Ids"),

    /**
     *
     */
    CELL_TYPE_IS_OBSOLETE("Cell Type Is Obsolete"),

    /**
     *
     */
    NAME("name"),

    /**
     *
     */
    IS_OBSOLETE("Is Obsolete"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_ID("Protein Ontology Id"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_RELATED_SYNONYMS("Protein Ontology Related Synonyms"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_EXACT_SYNONYMS("Protein Ontology Exact Synonyms"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_NARROW_SYNONYMS("Protein Ontology Narrow Synonyms"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_SYNONYMS("Protein Ontology Synonyms"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_NAME("Protein Ontology Name"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_NAME_SPACE("Protein Ontology Name Space"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_ALT_ID("Protein Ontology AltId"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_DEF("Protein Ontology Def"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_SUBSETS("Protein Ontology SubSets"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_IS_OBSOLETE("Protein Ontology Is Obsolete"),

    /**
     *
     */
    PROTEIN_ONTOLOGY_CONSIDER("Protein Ontology Consider"),

    /**
     *
     */
    CHEBI_PROPERTY_VALUE("Chebi Property Value"),

    /**
     *
     */
    CHEBI_ONTOLOGY_DEF("Chebi Ontology Def"),

    /**
     *
     */
    CHEBI_ONTOLOGY_NAME("Chebi Ontology Name"),

    /**
     *
     */
    CHEBI_RELATED_NAME("Chebi Related Name"),

    /**
     *
     */
    CHEBI_EXACT_SYNONYMS("Chebi Exact Synonyms"),

    /**
     *
     */
    CHEBI_ALTERNATE_IDS("Chebi Alternate Ids"),

    /**
     *
     */
    CHEBI_NARROW_SYNONYMS("Chebi Narrow Synonyms"),

    /**
     *
     */
    CHEBI_ONTOLOGY_ID("Chebi Ontology Id"),

    /**
     *
     */
    CHEBI_NAME_SPACE("Chebi Name Space"),

    /**
     *
     */
    CHEBI_RELATED_SYNONYMS("Chebi Related Synoyms"),

    /**
     *
     */
    STRUCTURE_PUBMED_ID("Pubmed Id"),

    /**
     * The message label for the ligand.
     */
    LIGAND_MESSAGE("Ligand message label"),

    /**
     * The message label for the PDB entity.
     */
    PDB_ENTITY_MESSAGE("PdbEntity message label"),

    /**
     * The message label for the start status.
     */
    START_STATUS_MESSAGE("StartStatus message label"),

    /**
     * The message label for the end status.
     */
    END_STATUS_MESSAGE("EndStatus message label"),

    /**
     * The message label for the protein chain.
     */
    CHAIN_MESSAGE("Chain message label"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_ID("Protein Modification Ontology Id"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_RELATED_SYNONYMS("Protein Modification Ontology Related Synonyms"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_EXACT_SYNONYMS("Protein Modification Ontology Exact Synonyms"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_SYNONYMS("Protein Modification Ontology Synonyms"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_NAME("Protein Modification Ontology Name"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_ALT_ID("Protein Modification Ontology AltId"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_DEF("Protein Modification Ontology Def"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_SUBSETS("Protein Modificaiton Ontology SubSets"),

    /**
     *
     */
    PROTEIN_MOD_ONTOLOGY_IS_OBSOLETE("Protein Modification Ontology Is Obsolete"),

    /**
     *
     */
    DIFF_AVG("Diff Avg"),

    /**
     *
     */
    DIFF_FORMULA("Diff Formula"),

    /**
     *
     */
    DIFF_MONO("Diff Mono"),

    /**
     *
     */
    FORMULA("Formula"),

    /**
     *
     */
    MASSAVG("Mass Avg"),

    /**
     *
     */
    MASSMONO("Mass Mono"),

    /**
     *
     */
    ORIGIN("Origin"),

    /**
     *
     */
    SOURCE("Source"),

    /**
     *
     */
    TERMSPEC("Term Specification"),

    /**
     *
     */
    SYSTEMS_BIOLOGY_ONTOLOGY_ID("Systems Biology Ontology Id"),

    /**
     *
     */
    SYSTEMS_BIOLOGY_ONTOLOGY_NAME("Systems Biology Ontology Name"),

    /**
     *
     */
    SYSTEMS_BIOLOGY_ONTOLOGY_SYNONYMS("Systems Biology Ontology Synonyms"),

    /**
     *
     */
    SYSTEMS_BIOLOGY_ONTOLOGY_IS_OBSOLETE("Systems Biology Ontology Is Obsolete"),

    /**
     *
     */
    SYSTEMS_BIOLOGY_ONTOLOGY_DEF("Systems Biology Ontology Def"),

    /**
     *
     */
    TAXONOMIC_RANK_ONTOLOGY_NAME("Taxonomic RankOntology Name"),

    /**
     *
     */
    TAXONOMIC_RANK_ONTOLOGY_ID("Taxonomic Rank Ontolgy Id"),

    /**
     *
     */
    TAXONOMIC_RANK_ONTOLOGY_DEF("Taxonomic Rank Ontology Def"),

    /**
     *
     */
    TAXONOMIC_RANK_ONTOLOGY_EXACT_SYNONYMS("Taxononmic Rank Ontology Exact Synonyms"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_IS_OBSOLETE("Spatial Ontology Is Obsolete"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_IS_TRANSITIVE("Spatia lOntology Is Transitive"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_IS_SYMMETRIC("Spatial Ontology Is Symmetric"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_ID("Spatial Ontology Id"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_ALT_ID("Spatial Ontology Alt Id"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_NAME("Spatial Ontology Name"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_DEF("Spatial Ontology Def"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_NARROW_SYNONYMS("Spatial Ontology Narrow Synonyms"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_RELATED_SYNONYMS("Spatial Ontology Related Synonyms"),

    /**
     *
     */
    SPATIAL_ONTOLOGY_EXACT_SYNONYMS("Spatial Ontology Exact Synonyms"),

    /**
     *
     */
    ANATOMICAL_ENTITY_ONTOLOGY_ID("Anatomical Entity Ontology Id"),

    /**
     *
     */
    ANATOMICAL_ENTITY_ONTOLOGY_DEF("Anatomical Entity Ontology Def"),

    /**
     *
     */
    ANATOMICAL_ENTITY_ONTOLOGY_ALT_ID("Anatomical EntityOntology Alt Id"),

    /**
     *
     */
    ANATOMICAL_ENTITY_ONTOLOGY_IS_OBSOLETE("Anatomical Entity Ontology Is Obsolete"),

    /**
     *
     */
    ANATOMICAL_ENTITY_ONTOLOGY_EXACT_SYNONYMS("Anatomical Entity Ontology Exact Synonyms"),

    /**
     *
     */
    ANATOMICAL_ENTITY_ONTOLOGY_RELATED_SYNONYMS("Anatomical Entity Ontology Related Synonyms"),

    /**
     *
     */
    ANATOMICAL_ENTITY_ONTOLOGY_NARROW_SYNONYMS("Anatomical Entity Ontology Narrow Synonyms"),

    /**
     *
     */
    ANATOMICAL_ENTITY_ONTOLOGY_NAME("Anatomical Entity Ontology Name"),

    /**
     *
     */
    CARO_ID("CARO Id"),

    /**
     *
     */
    CARO_ALT_ID("CARO Alt Id"),

    /**
     *
     */
    CARO_IS_OBSOLETE("CARO Is Obsolete"),

    /**
     *
     */
    CARO_DEF("CARO Def"),

    /**
     *
     */
    CARO_EXACT_SYNONYMS("CARO Exact Synonyms"),

    /**
     *
     */
    CARO_RELATED_SYNONYMS("CARO Related Synonyms"),

    /**
     *
     */
    CARO_NARROW_SYNONYMS("CARO Narrow Synonyms"),

    /**
     *
     */
    CARO_NAME("CARO Name"),

    /**
     *
     */
    AHDA_ID("AHDA Id"),

    /**
     *
     */
    AHDA_NAME("AHDA Name"),

    /**
     *
     */
    AHDA_IS_OBSOLETE("AHDA Is Obsolete"),

    /**
     *
     */
    AHDA_EXACT_SYNONYMS("AHDA Exact Synonyms"),

    /**
     *
     */
    CS_ID("CS Id"),

    /**
     *
     */
    CS_NAME("CS Name"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_ID("Human Disease Ontology Id"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_DEF("Human Disease Ontology Def"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_ALT_ID("Human Disease Ontology Alt Id"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_IS_OBSOLETE("Human Disease Ontology Is Obsolete"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_EXACT_SYNONYMS("Human Disease Ontology Exact Synonyms"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_RELATED_SYNONYMS("Human Disease Ontology Related Synonyms"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_NARROW_SYNONYMS("Human Disease Ontology NarrowS ynonyms"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_NAME("Human Disease Ontology Name"),

    /**
     *
     */
    HUMAN_DISEASE_ONTOLOGY_SUBSETS("Human Disease Ontology Subsets"),

    /**
     * comment, name, id are already specified
     */
    HOMOLOGY_ONTOLOGY_ID("Homology Ontology Id"),

    /**
     *
     */
    HOMOLOGY_ONTOLOGY_DEF("Homology Ontology Def"),

    /**
     *
     */
    HOMOLOGY_ONTOLOGY_IS_OBSOLETE("Homology Ontology Is Obsolete"),

    /**
     *
     */
    HOMOLOGY_ONTOLOGY_EXACT_SYNONYMS("Homology Ontology Exact Synonyms"),

    /**
     *
     */
    HOMOLOGY_ONTOLOGY_RELATED_SYNONYMS("Homology Ontology Related Synonyms"),

    /**
     *
     */
    HOMOLOGY_ONTOLOGY_NAME("Homology Ontology Name"),

    /**
     *
     */
    RNA_ONTOLOGY_ID("RNA Ontology Id"),

    /**
     *
     */
    RNA_ONTOLOGY_DEF("RNA Ontology Def"),

    /**
     *
     */
    RNA_ONTOLOGY_NAME("RNA Ontology Name"),

    /**
     *
     */
    SYMPTOM_ONTOLOGY_ID("Symptom Ontology Id"),

    /**
     *
     */
    SYMPTOM_ONTOLOGY_NAME("Symptom Ontology Name"),

    /**
     *
     */
    SYMPTOM_ONTOLOGY_DEF("Symptom Ontology Def"),

    /**
     *
     */
    SYMPTOM_ONTOLOGY_RELATED_SYNONYMS("Symptom Ontology Related Synonyms"),

    /**
     *
     */
    SYMPTOM_ONTOLOGY_EXACT_SYNONYMS("Symptom Ontology Exact Synonyms"),

    /**
     *
     */
    CREATED_BY("Created By"),

    /**
     *
     */
    CREATION_DATE("Creation Date"),

    /**
     *
     */
    PHENOTYPIC_ONTOLOGY_ID("Phenotypic Ontology Id"),

    /**
     *
     */
    PHENOTYPIC_SYNONYMS("Phenotypic Synonyms"),

    /**
     *
     */
    PHENOTYPIC_NAMESPACE("Phenotypic NameSpace"),

    /**
     *
     */
    PHENOTYPIC_ONTOLOGY_NAME("Phenotypic Ontology Name"),

    /**
     *
     */
    PHENOTYPIC_ONTOLOGY_ALT_ID("Phenotypic Ontology AltId"),

    /**
     *
     */
    PHENOTYPIC_ONTOLOGY_DEF("Phenotypic Ontology Def"),

    /**
     *
     */
    PHENOTYPIC_RELATED_SYNONYMS("Phenotypic Related Synonyms"),

    /**
     *
     */
    PHENOTYPIC_EXACT_SYNONYMS("Phenotypic Exact Synonyms"),

    /**
     *
     */
    PHENOTYPIC_NARROW_SYNONYMS("Phenotypic Narrow Synonyms"),

    /**
     *
     */
    PHENOTYPIC_SUBSETS("Phenotypic SubSets"),

    /**
     *
     */
    PHENOTYPIC_ALTERNATE_IDS("Phenotypic Alternate Ids"),

    /**
     *
     */
    PHENOTYPIC_IS_OBSOLETE("PhenotypicIs Obsolete"),

    /**
     *
     */
    CYTOPLASM_ONTOLOGY_ID("Cytoplasm Ontology Id"),

    /**
     *
     */
    CYTOPLASM_SYNONYMS("Cytoplasm Synonyms"),

    /**
     *
     */
    CYTOPLASM_NAMESPACE("Cytoplasm NameSpace"),

    /**
     *
     */
    CYTOPLASM_ONTOLOGY_NAME("Cytoplasm Ontology Name"),

    /**
     *
     */
    CYTOPLASM_ONTOLOGY_DEF("Cytoplasm Ontology Def"),

    /**
     *
     */
    CYTOPLASM_NARROW_SYNONYMS("Cytoplasm Narrow Synonyms"),

    UNIPROT_ENTRY_TYPE("Uniprot Entry Type"),

    UNIPROT_KEYWORDS("Uniprot keywords"),

    FEATURE_LOCATION("Feature Location"),

    /**
     * The feature description could be "TNFAIP3-interacting protein 2"
     */
    FEATURE_DESCRIPTION("Feature Description"),

    /**
     * FeatureStatus in UniProt
     */
    FEATURE_STATUS("Feature Status"),

    /**
     * FeatureType in UniProt
     */
    FEATURE_TYPE("Feature Type"),

    /**
     * Feature list in UniProt.
     */
    FEATURE_LIST("Feature List"),

    START_POSITION("Start Position"),

    END_POSITION("End Position"),

    SEQUENCE_VALUE("Sequence Value"),

    MOLECULAR_WEIGHT("Molecular Weight"),

    SEQUENCE_LENGTH("Sequence Length"),

    /**
     * Uniprot citation
     * Title of Citation in Uniprot object.
     */
    CITATION_TITLE("citationTitle"),
    /**
     * Publication date of Citation in Uniprot object.
     */
    CITATION_PUBLICATION_DATE("Citation Publication Date"),
    SAMPLE_SOURCES("Sample Sources"),
    CITATION_REFERENCE_DOI("Citation Reference DOI"),
    AUTHORING_GROUPS("Authoring Groups"),
    SUMMARY_GROUP("Summary Group"),
    SAMPLE_EVIDENCE_ID("Sample Evidence Id"),
    SAMPLE_SOURCE("Sample Source"),
    SAMPLE_SOURCE_TYPE("Sample Source Type"),
    EVIDENCE_IDS("Evidence Ids"),
    SUMMARY_LIST("Summary List"),
    CITATION_TYPE("Citation Type"),
    PUBLICATION_DATE("Publication Date"),

    COMMENT_TYPE("Comment Type"),
    COMMENT_STATUS("Comment Status"),
    COMMENT_ID("Comment Id"),

    LOG_P("logP");

    private final String value;

    private static final Map<String, TaxonomyTypes> stringToEnum = new HashMap<String, TaxonomyTypes>();

    static { // init map from constant name to enum constant
        for (TaxonomyTypes en : values())
            stringToEnum.put(en.toString(), en);
    }

    /**
     *
     * @param taxonomyTypes
     * @return boolean
     */
    public boolean equals(TaxonomyTypes taxonomyTypes) {
        return value.equals(taxonomyTypes.toString());
    }

    /**
     *
     * @param taxonomyTypes
     * @return boolean
     */
    public boolean equals(String taxonomyTypes) {
        return value.equals(taxonomyTypes);
    }

    TaxonomyTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     *
     * @param value
     * @return TaxonomyTypes
     */
    public static TaxonomyTypes fromString(String value) {
        return stringToEnum.get(value);
    }
}
