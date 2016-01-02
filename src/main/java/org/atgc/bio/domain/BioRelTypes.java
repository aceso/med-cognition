/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.ProteinModificationOntologyFields;

/**
 * For every relationship which has a new string relation, we add an enum here.
 * There would be lots of relationships in this file eventually.
 *
 * en.name returns enum
 *
 * <p>
 * Some graph vendors force us to have no spaces in the relationship. That is
 * why we use an underscore. This string is displayed in the GraphDB gui, so we
 * need to be more user friendly. Use upper case for all the string names for
 * consistency.
 * <p>
 * Also add a property called "message" with the same value as this relationship.
 * This relation string is the same as the type property for the <code>RelationshipType</code>
 *
 *
 * @author jtanisha-ee
 */

public enum BioRelTypes {

    /**
     * Interacts With
     */
    INTERACTS_WITH("INTERACTS_WITH"),

    EXHIBITS_ONTOLOGY("EXHIBITS_ONTOLOGY"),

    /**
     * For instance a protein <code>Chain</code> has a <code>Protein</a>.
     */
    HAS_A_PROTEIN("HAS_A_PROTEIN"),

    /**
     * For instance a <code>PdbEntity</code> can be a <code>Protein</code>. So a PdbEntity IS_A_PROTEIN in that scenario.
     * PdbEntity can be any of <code>StructureEntityTypes</code>.
     */
    IS_A_PROTEIN("IS_A_PROTEIN"),

    PART_OF_STRUCTURE("PART_OF_STRUCTURE"),
    
    AUTHOR("AUTHOR"),
    
    DRUG_PACKAGED_BY("DRUG_PACKAGED_BY"),
    
    DRUG_MANUFACTURED_BY("DRUG_MANUFACTURED_BY"),
    
    /**
     * DrugPrice relation to a drug.
     */
    DRUG_PRICED_AS("DRUG_PRICED_AS"),
    
    /**
     * Drug has a chemjcal property.
     */
    HAS_CHEMICAL_PROPERTY("HAS_CHEMICAL_PROPERTY"),
    
    /**
     * Drug has a experimental property.
     */
    HAS_EXPERIMENTAL_PROPERTY("HAS_EXPERIMENTAL_PROPERTY"),
    
    /**
     * Drug has a drug dosage.
     */
    HAS_DRUG_DOSAGE("HAS_DRUG_DOSAGE"),

    /**
     * A PDB structure has chains. Each chains has molecules.
     */
    HAS_CHAIN("HAS_CHAIN"),

    HAS_LIGAND("HAS_LIGAND"),

    HAS_PDBGO("HAS_PDBGO"),

    HAS_ONTOLOGIES("HAS_ONTOLOGIES"),

    /**
     * For instance a gene is part of a taxonomy (organism).
     */
    HAS_TAXONOMY("HAS_TAXONOMY"),

    /**
     * From gene neighbors.
     */
    GENE_ON_LEFT("GENE_ON_LEFT"),

    /**
     * From Gene neighbors
     */
    GENE_ON_RIGHT("GENE_ON_RIGHT"),
    
    /**
     * used in NciDisease
     */
    GENE_ASSOCIATED_WITH_DISEASE("GENE_ASSOCIATED_WITH_DISEASE"),

    /**
     *
     */
    OVERLAPPING_GENE("OVERLAPPING_GENE"),

    /**
     * A gene object related to a GeneToGo object.
     */
    HAS_GENE_TO_GO("HAS_GENE_TO_GO"),

    /**
     * NCBI gene referenced by PubMed, but could be anything referenced by PubMed.
     */
    REFERENCED_BY_PUBMED("REFERENCED_BY_PUBMED"),

    /**
     * NCBI gene is related to the GeneToGo.
     */
    GENE_RELATION("GENE_RELATION"),

    /**
     * NCBI gene is related to the GO
     */
    GO_RELATION("GO_RELATION"),

    /**
     * Found in PubMed
     */
    HAS_AUTHOR("HAS_AUTHOR"),

    /**
     *
     */
    IS_PART_OF_PATHWAY("IS_PART_OF_PATHWAY"),

    /**
     * A participant in an experiment has features.
     */
    HAS_FEATURE("HAS_FEATURE"),

    /**
     * A participant has an interactor, typically a protein.
     */
    HAS_INTERACTOR("HAS_INTERACTOR"),

    /**
     * A participant for instance has a biological role.
     */
    HAS_BIOLOGICAL_ROLE("HAS_BIOLOGICAL_ROLE"),

    /**
     * An Intact experiment has a host organism.
     */
    HAS_HOST_ORGANISM("HAS_HOST_ORGANISM"),

    /**
     * An Intact experiment has an intact interaction.
     */
    HAS_INTACT_INTERACTION("HAS_INTACT_INTERACTION"),

    /**
     * An Intact has an intact experiment.
     */
    HAS_INTACT_EXPERIMENT("HAS_INTACT_EXPERIMENT"),

    /**
     * A participant for instance has multiple experimental roles.
     */
    HAS_EXPERIMENTAL_ROLE("HAS_EXPERIMENTAL_ROLE"),

    /**
     * A feature for a participant has a feature type.
     */
    HAS_FEATURE_TYPE("HAS_FEATURE_TYPE"),

    /**
     * A feature for a participant has multiple feature ranges.
     */
    HAS_FEATURE_RANGE("HAS_FEATURE_RANGE"),

    /**
     * An interaction has one or more participants.
     */
    HAS_PARTICIPANT("HAS_PARTICIPANT"),

    /**
     * A FeatureRange has EndStatus.
     */
    HAS_END_STATUS("HAS_END_STATUS"),

    /**
     * A FeatureRange has StartStatus.
     */
    HAS_START_STATUS("HAS_START_STATUS"),

    /**
     * This is the default relation. Use if judiciously.
     */
    DEFAULT_RELATION("RELATED_TO"),

    IS_MEMBER_OF_FAMILY("IS_MEMBER_OF_FAMILY"),

    AN_INTERACTION("AN_INTERACTION"),

    IS_OUTPUT_OF("IS_OUTPUT_OF"),

    MODIFICATION("MODIFICATION"),

    IS_PTM_EXPRESSION_OF("IS_PTM_EXPRESSION_OF"),

    IS_LOCATED_AT("IS_LOCATED_AT"),

    PROCESS_USED("PROCESS_USED"),

    CONDITION_TYPE("CONDITION_TYPE"),

    /**
     * Comment attribute in intact experiment's attribute list.
     */
    HAS_COMMENT("HAS_COMMENT"),

    /**
     * DATASET attribute in intact experiment's attribute list.
     */
    HAS_DATASET("HAS_DATASET"),

    /**
     * Any object that references a PubMed article.
     */
    REFERENCES_PUBMED("REFERENCES_PUBMED"),

    /**
     * IntactExperiment has an interaction detection method.
     */
    HAS_INTERACTION_DETECTION_METHOD("HAS_INTERACTION_DETECTION_METHOD"),

    /**
     * IntactExperiment has a participant identification method.
     */
    HAS_PARTICIPANT_IDENTIFICATION_METHOD("HAS_PARTICIPANT_IDENTIFICATION_METHOD"),

    /**
     * IntactExperiment has attributes.
     */
    HAS_ATTRIBUTES("HAS_ATTRIBUTES"),

    /**
     * library-used attribute in intact experiment's attribute list.
     */
    USES_LIBRARY("USES_LIBRARY"),

    /**
     * url attribute in intact experiment's attribute list.
     */
    HAS_URL("HAS_URL"),

    /**
     * data-processing attribute in intact experiment's attribute list
     */
    HAS_DATA_PROCESSING("HAS_DATA_PROCESSING"),

    /**
     * {@link FamilyMemberRelation#relType} and {@link ComplexComponentRelation#relType}
     *
     * "\"'Entity A' contains 'Entity B' implies that 'Entity B' is a part of the structure of 'Entity A'.\" [PubMed:18688235]",
     *"comment" : "The inverse relationship to \"part of\".",
     * "is_transitive" : "true"
     */
    CONTAINS("CONTAINS"),

    /**
     * {@link ComplexComponentRelation#relType}
     */
    IS_A_COMPONENT_OF("IS_A_COMPONENT_OF"),

    /*
     * {@link FamilyMemberRelation#relType}
     */
    IS_A_MEMBER_OF("IS_A_MEMBER_OF"),

    /**
     * This molecule is part of whole molecule
     * {@link PartMoleculeRelation#relType}
     */
    IS_PART_OF_PROTEIN("IS_PART_OF_PROTEIN"),

    /**
     * gene - disease, sequence identification collection
     */
    IDENTIFIED_IN_SEQUENCE("IDENTIFIED_IN_THIS_SEQUENCE"),

    /**
     * pathway is observed in organisms
     */
    IS_OBSERVED_IN("IS_OBSERVED_IN"),
    
    IS_OBSERVED_IN_NCBI_TAXONOMY("IS_OBSERVED_IN_NCBI_TAXONOMY"),

    /**
     * Gene and Disease
     */
    ROLE_OF_GENE("ROLE_OF_GENE"),

    /**
     * Disease evidence in experiments, uniprot (protein) evidenceList etc
     */
    FOUND_EVIDENCE_IN("FOUND_EVIDENCE_IN"),

    PUBLISHED_IN_JOURNAL("PUBLISHED_IN_JOURNAL"),

    ISSUE_OF_JOURNAL("ISSUE_OF_JOURNAL"),

    FROM_WHICH_DATA_IS_COLLECTED("FROM_WHICH_DATA_IS_COLLECTED"),

    EXACT_SYNONYM_OR_INTERCHANGEABLE_WITH_TERM_NAME("EXACT_SYNONYM_OR_INTERCHANGEABLE WITH_TERM_NAME"),

    IS_BROADER_THAN_TERM_NAME("IS_BROADER_THAN_TERM_NAME"),

    IS_MORE_PRECISE_THAN_TERM_NAME("IS_MORE_PRECISE_THAN_TERM_NAME"),

    /**
     * IS_A or IS_SUBTYPE_OF
     */
    IS_A("IS_A"),

    PART_OF("PART_OF"),

    REGULATES("REGULATES"),

    NEGATIVELY_REGULATES("NEGATIVELY_REGULATES"),

    POSITIVELY_REGULATES("POSITIVELY_REGULATES"),

    IS_AN_ENZYME("IS_AN_ENZYME"),

    IS_OBSOLETED_BY_NEW_TERM("IS_OBSOLETED_BY_NEW_TERM"),

    GO_IS_A_COMPLEX("GENE_ONTOLOGY_IS_A_COMPLEX"),

    /** CellTypeOntology */

    DEVELOPS_FROM("DEVELOPS_FROM"),

    BEARER_OF("BEARER_OF"),

    PARTICIPATES_IN("PARTICIPATES_IN"),

    LACKS_PLASMA_MEMBRANE_PART("LACKS_PLASMA_MEMBRANE_PART"),

    HAS_PLASMA_MEMBRANE_PART("HAS_PLASMA_MEMBRANE_PART"),

    /**
     * <pre>
     * Entity A' part_of 'Entity B' implies that
     * 'Entity A' is a part of the structure of 'Entity B'.\" [PubMed:18688235]",
     *  "is_transitive" : "true"
     * </pre>
     *
     */
    HAS_PART("HAS_PART"),

    PRODUCES("PRODUCES"),

    /**
     * {@link CellTypeOntology}
     * <pre>
     * A relation between a cell and molecule or complex such that every instance of the
     * cell has a high number of instances of that molecule expressed on the cell surface.
     * For the formal definition, see Masci et al *PMID:19243617).\"
     * </pre>
     */
    HAS_HIGH_PLASMA_MEMBRANE_PART("HAS_HIGH_PLASMA_MEMBRANE_PART"),

    /**
     * {@link CellTypeOntology}
     * <pre>
     * A relation between a cell and molecule or complex such that every instance of the
     * cell has a low number of instances of that molecule expressed on the cell surface.
     * For the formal definition, see Masci et al *PMID:19243617).\" [PMID:19243617]
     * </pre>
     */
    HAS_LOW_PLASMA_MEMBRANE_PART("HAS_LOW_PLASMA_MEMBRANE_PART"),

    /**
     * {@link CellTypeOntology} -> {@link ProteinOntology}
     */
    HAS_HIGH_PLASMA_MEMBRANE_AMOUNT("HAS_HIGH_PLASMA_MEMBRANE_AMOUNT"),

     HAS_LOW_PLASMA_MEMBRANE_AMOUNT("HAS_LOW_PLASMA_MEMBRANE_AMOUNT"),

    LACKS_PART("LACKS_PART"),

    HAS_MODIFICATION("HAS_MODIFICATION"),

    HAS_COMPLETED("HAS_COMPLETED"),

    CAPABLE_OF("CAPABLE_OF"),

    IN_ORGANISM("IN_ORGANISM"),

    DISJOINT_FROM("DISJOINT_FROM"),

    CONSIDER_RELATIONSHIP("CONSIDER_RELATIONSHIP"),

    GO_RELATIONSHIP("GO_RELATIONSHIP"),

    /**
     * ontology intersection_of
     */
    INTERSECTION_OF("INTERSECTION OF"),

    OCCURS_IN("OCCURS_IN"),

    /**
     * {@link ProteinModificationOntologyFields}
     * <pre>
     * Entity A' has_functional_parent 'Entity B' implies that 'Entity B' has at
     * least one chacteristic group from which 'Entity A' can be derived by functional
     * modification.\" [PubMed:18688235]",
     *  "comment" : "This relationship indicates that the formula and mass of the child
     * are not inherited from the mass of the parent.",
     * "is_transitive" : "true"
      </pre>
     */
    HAS_FUNCTIONAL_PARENT("HAS_FUNCTIONAL_PARENT"),

    /**
     * {@link ProteinModificationOntologyFields}
     * 'Entity A' derives_from 'Entity B' implies that 'Entity A' is
     * chemically derived from 'Entity B'.\" [PubMed:18688235]",
     *  "is_transitive" : "true"
     */
    DERIVES_FROM("DERIVES_FROM"),

    XREF("CROSS_REFERENCE"),

    /**
     * spatial ontology
     */
    DOMAIN("DOMAIN"),

    RANGE("RANGE"),

    INVERSE_OF("INVERSE_OF"),

    INVERSE_OF_ON_INSTANCE_LEVEL("INVERSE_OF_ON_INSTANCE_LEVEL"),

    /**
     * AnatomicalEntity ontology
     */
    SURROUNDS("SURROUNDS"),

    /**
     * AnatomicalEntityOntology (AEO), AHDA, CS, CL
     *  relTypes : develops_in, ends_at, located_in, starts_at, develops_from
     */
    ENDS_AT("ENDS_AT"),

    DEVELOPS_IN("DEVELOPS_IN"),

    LOCATED_IN("LOCATED_IN"),

    ATTACHED_TO("ATTACHED_TO"),

    STARTS_AT("STARTS_AT"),

    IS_AN_ALTERNATE_ID("IS_AN_ALTERNATE_ID"),

    /**
     * for ontologies, RELATIONSHIP_LIST
     */
    IS_RELATIONSHIP("IS_RELATIONSHIP"),

    /**
     * RNA ontology
     */
    HAS_PROPER_PART("HAS_PROPER_PART"),

    HAS_QUALITY("HAS_QUALITY"),

    FUNCTIONAL_PARENT("FUNCTIONAL_PARENT"),
    
    EVIDENCE_IN_COMMENT("EVIDENCE_IN_COMMENT"),
    
    INTERACTION_COMPONENT("INTERACTION_COMPONENT"),
    
    /**
     * PartMoleculeRelation
     */
    IS_PART_OF("IS_PART_OF"),
    
    /**
     * used by protein object - uniprot (evidenceList)
     */
    AUTOMATIC_ASSERTION("AUTOMATIC_ASSERTION"),
    EXPERIMENTAL_EVIDENCE("EXPERIMENTAL_EVIDENCE"),
    SEQUENCE_SIMILARITY("SEQUENCE_SIMILARITY"),
    INFERRED_FROM_SCIENTIFIC_KNOWLEDGE("INFERRED_FROM_SCIENTIFIC_KNOWLEDGE");
    

    private final String value;

    protected static Log log = LogFactory.getLog(BioRelTypes.class);

    private static final Map<String, BioRelTypes> stringToEnum = new HashMap<String, BioRelTypes>();

    static { // init map from constant name to enum constant
        for (BioRelTypes en : values()) {
            stringToEnum.put(en.toString(), en);
        }

    }

    BioRelTypes(String value) {
        this.value = value;
    }

    public boolean equals(BioRelTypes bioRelType) {
        return value.equalsIgnoreCase(bioRelType.toString());
    }

    public boolean equals(String bioRelType) {
        log.info("value " + value + " str=" + bioRelType);
        return value.equalsIgnoreCase(bioRelType);
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     * value needs to be in uppercase
     * @param value
     * @return BioRelTypes
     */
    public static BioRelTypes fromString(String value) {
        return stringToEnum.get(value);
    }

    public static BioRelTypes getEnum(String enumStr) {
        log.info("enumStr =" + enumStr.toString());
        for (BioRelTypes en : values()) {
           // log.info(en.name() + " value =" + en.value + " en =" + en);
            if (en.name().equalsIgnoreCase(enumStr)) {
               return en;
            }
        }
        return null;
    }




}