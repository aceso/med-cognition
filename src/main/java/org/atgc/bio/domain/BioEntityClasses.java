/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.meta.BioEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * These are primarily used in enums to check the type of the startNode and
 * endNode in case of relationships. But they can be used anywhere. We are
 * doing a equals comparison of the class object as opposed to comparing
 * the string names, as the string names are ambiguous and can be found
 * in variable paths. Also if the vendors change the package library, the
 * path names can change, but the class names will usually stay the same.
 *
 * Check places like {@link BioRelation} and {@link Feature} for
 * usage of this code. The isValid method in BioRelation in particular
 * uses this enum.
 *
 * The classes included in this are mainly {@link BioEntity} classes. We do not
 * add classes of other types here. This helps keep this enum clean.
 *
 * @author jtanisha-ee
 */

public enum BioEntityClasses {

    PARTICIPANT(Participant.class),

    PARTICIPANT_IDENTIFICATION_METHOD(ParticipantIdentificationMethod.class),

    BIOLOGICAL_ROLE(BiologicalRole.class),
    
    DRUG_PACKAGER(DrugPackager.class),
    
    DRUG_MANUFACTURER(DrugManufacturer.class),
    
    DRUG_PRICE(DrugPrice.class),
    
    DRUG_PATENT(DrugPatent.class),
    
    DOSAGE(Dosage.class),

    EXPERIMENTAL_ROLE(ExperimentalRole.class),
    
    CHEMICAL_PROPERTY(ChemicalProperty.class),
    
    EXPERIMENTAL_PROPERTY(ExperimentalProperty.class),

    GENE_TO_GO(GeneToGo.class),

    DNA(Dna.class),

    LIGAND(Ligand.class),

    PDB_COMPOUND(PdbCompound.class),

    PDBGO(PdbGo.class),

    PDB_ENTITY(PdbEntity.class),

    STRUCTURE(Structure.class),

    GENE(Gene.class),

    INTACT_GENE(IntactGene.class),

    GENE_ONTOLOGY(GeneOntology.class),

    GENE_TO_GO_ONTOLOGY(GeneToGoOntology.class),

    FEATURE(Feature.class),

    FEATURE_TYPE(FeatureType.class),

    FEATURE_RANGE(FeatureRange.class),

    START_STATUS(StartStatus.class),

    END_STATUS(EndStatus.class),

    ENZYME(Enzyme.class),

    EXPERIMENT(Experiment.class),

    EXPERIMENT_ATTRIBUTES(ExperimentAttributes.class),

    EXPERIMENT_COMMENT(ExperimentComment.class),

    EXPERIMENT_DATA_PROCESSING(ExperimentDataProcessing.class),

    EXPERIMENT_DATASET(ExperimentDataset.class),

    EXPERIMENT_LIBRARY(ExperimentLibrary.class),

    EXPERIMENT_URL(ExperimentUrl.class),

    INTACT(Intact.class),

    INTACT_INTERACTION(IntactInteraction.class),

    INTACT_EXPERIMENT(IntactExperiment.class),

    JOURNAL(Journal.class),

    JOURNAL_ISSUE(JournalIssue.class),

    PUBMED(PubMed.class),

    PROTEIN(Protein.class),

    ORGANISM(Organism.class),

    PEPTIDE(Peptide.class),

    SMALL_MOLECULE(SmallMolecule.class),

    COMPLEX(Complex.class),

    COMPOUND(Compound.class),

    RNA(Rna.class),

    NCI_PATHWAY_INTERACTION(NciPathwayInteraction.class),

    NCI_PATHWAY(NciPathway.class),

    DRUG(Drug.class),

    PART_PROTEIN(PartProtein.class),

    NCBI_TAXONOMY(NcbiTaxonomy.class),

    NAMED_PROTEIN(NamedProtein.class);

    private final Class value;

    /**
     * This method is used to compare the class of the value in this
     * object to an external class and this equals method is recommended
     * to be used.
     *
     * @param entityClass
     * @return boolean
     */
    public boolean equals(Class entityClass) {
        return value.equals(entityClass);
    }

    /**
     * The getClass() method cannot be used, as that is associated with
     * the class name of this class. This method returns the class
     * of the value stored in this enum.
     *
     * @return Class
     */
    public Class getAnnotationClass() {
        return value;
    }

    /**
     * This only compares the string names, and we don't recommend this
     * being used.
     *
     * @param className
     * @return boolean
     */
    public boolean equals(String className) {
        return value.toString().equals(className);
    }

    BioEntityClasses(Class value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.getName();
    }

    private static final Map<String, BioEntityClasses> stringToEnum = new HashMap<String, BioEntityClasses>();

    public static BioEntityClasses fromString(String value) {
        return stringToEnum.get(value);
    }
}