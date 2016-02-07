/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.meta.EndNode;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.RelProperty;
import org.atgc.bio.meta.RelType;
import org.atgc.bio.meta.RelationshipEntity;
import org.atgc.bio.meta.StartNode;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;

/**
 * Developers can declare their own classes that use the {@link RelationshipEntity}.
 * However, the relationships do not seem to have so many properties. So
 * the {@link org.atgc.bio.BioRelation} class works in most cases. We will use the BioRelation
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
public class DiseaseGeneRoleRelation {

    protected static Logger log = LogManager.getLogger(DiseaseGeneRoleRelation.class);

    /**
     * DiseaseGeneRelationship
     *
     * There can be multiple genes associated with a disease.
     * A single gene can be associated with multiple diseases.
     * Evidence for this is maintained in the Disease BioEntity
     *
     * OtherRole - role details
     *
     * <GeneData>
     *    <MatchedGeneTerm>tlh6</MatchedGeneTerm>
     *   <NCIGeneConceptCode></NCIGeneConceptCode>
     * </GeneData>
     *       <Roles>
     *                   <PrimaryNCIRoleCode>Gene_Expressed_In_Tissue</PrimaryNCIRoleCode>
     *                   <OtherRole>Gene_Expression_Changed_in_Disease</OtherRole>
     *                   <OtherRole>Gene_May_have_Therapeutic_Relevance</OtherRole>
     *       </Roles>
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
     * The start node must be a BioEntity class. And it cannot be null,
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
     * All properties that need to be saved for the relationship must
     * have a @RelProperty annotation.
     * @RelProperty roles
     *  <Roles>
     *                   <PrimaryNCIRoleCode>Gene_Expressed_In_Tissue</PrimaryNCIRoleCode>
     *                   <OtherRole>Gene_Expression_Changed_in_Disease</OtherRole>
     *                   <OtherRole>Gene_May_have_Therapeutic_Relevance</OtherRole>
     *  </Roles>
     *  "Roles" : {
     *              "PrimaryNCIRoleCode" : "Gene_Product_Expressed_In_Tissue",
     *               "OtherRole" : "Gene_Product_Increased_in_Disease"
     *          },
     *
     *  	"Roles" : [
     *                 "Gene_is_Biomarker_of"
     *		],
     *          "Roles" : "\n\t\t",
     *
     *  Roles" : {
     *   "PrimaryNCIRoleCode" : [
     *       "Gene_Product_is_Biomarker_of",
     *       "Gene_Product_Expressed_In_Tissue"
     *    ],
     *    "OtherRole" : "Gene_Product_Level_Changed_in_Disease"
     *  },
     *
     * Create properties for each role
     */

    /**
     * PrimaryNCIRoleCode
     */
    @RelProperty
    String Gene_Associated_With_Disease;

    @RelProperty
    String Gene_Product_Anormaly_Affects_Pathway;

    @RelProperty
    String Gene_Product_Anomaly_Related_To_Gene_Anormaly;

    @RelProperty
    String Gene_Product_Encoded_By_Gene;

    @RelProperty
    String Gene_Product_Expressed_In_Tissue;

    @RelProperty
    String Gene_Product_Has_Associated_Anatomie;

    @RelProperty
    String Gene_Product_Has_Biochemical_Function;

    @RelProperty
    String Gene_Product_Has_Chemical_Classification;

    @RelProperty
    String Gene_Product_Has_Malfunction_Type;

    @RelProperty
    String Gene_Product_Has_Organism_Source;

    @RelProperty
    String Gene_Product_Has_Structural_Domain_Or_Motif;

    @RelProperty
    String Gene_Product_is_Biomarker_of;

    @RelProperty
    String Gene_Product_is_Biomarker_Type;

    @RelProperty
    String Gene_Product_is_Pathway_Element;

    @RelProperty
    String Gene_Product_is_Physical_Part_Of;

    @RelProperty
    String Gene_Product_Malfunction_Associated_With_Disease;

    @RelProperty
    String Gene_Product_Plays_Role_In_Biological_Process;

    @RelProperty
    String Gene_Malfunction_Associated_With_Disease;

    @RelProperty
    String Gene_Expressed_In_Tissue;

    @RelProperty
    String Gene_Found_In_Organism;

    @RelProperty
    String Gene_Has_Anormally;

    @RelProperty
    String Gene_Has_Clone;

    @RelProperty
    String Gene_Has_Expression_Measurement;

    @RelProperty
    String Gene_Has_Function;

    @RelProperty
    String Gene_In_Chromosomal_Location;

    @RelProperty
    String Gene_is_Biomarker_of;

    @RelProperty
    String Gene_Is_Pathway_Element;

    @RelProperty
    String Gene_Plays_Role_In_Process;

    @RelProperty
    String Disease_Has_Cytogenetic_Abnormality;

    @RelProperty
    String Disease_May_Have_Cytogenetic_Abnormality;

    @RelProperty
    String Disease_Has_Molecular_Abnormality;

    @RelProperty
    String Disease_May_Have_Molecular_Abnormality;

    /**
     *
     * @param role
     */
    public void setGene_Associated_With_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Associated_With_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Associated_With_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Associated_With_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Anormaly_Affects_Pathway(NciDiseaseGeneRoles role) {
        this.Gene_Product_Anormaly_Affects_Pathway = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Anormaly_Affects_Pathway() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Anormaly_Affects_Pathway.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Anomaly_Related_To_Gene_Anormaly(NciDiseaseGeneRoles role) {
            Gene_Product_Anomaly_Related_To_Gene_Anormaly = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Anomaly_Related_To_Gene_Anormaly() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Anomaly_Related_To_Gene_Anormaly.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Encoded_By_Gene(NciDiseaseGeneRoles role) {
        this.Gene_Product_Encoded_By_Gene = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Encoded_By_Gene() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Encoded_By_Gene.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Expressed_In_Tissue(NciDiseaseGeneRoles role) {
        this.Gene_Product_Expressed_In_Tissue = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Expressed_In_Tissue() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Expressed_In_Tissue.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Has_Associated_Anatomie(NciDiseaseGeneRoles role) {
        this.Gene_Product_Has_Associated_Anatomie = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Has_Associated_Anatomie() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Has_Associated_Anatomie.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Has_Biochemical_Function(NciDiseaseGeneRoles role) {
        this.Gene_Product_Has_Biochemical_Function = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Has_Biochemical_Function() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Has_Biochemical_Function.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Has_Chemical_Classification(NciDiseaseGeneRoles role) {
        this.Gene_Product_Has_Chemical_Classification = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Has_Chemical_Classification() {
        return NciDiseaseGeneRoles.getLongString(Gene_Product_Has_Chemical_Classification.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Has_Malfunction_Type(NciDiseaseGeneRoles role) {
            Gene_Product_Has_Malfunction_Type = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Has_Malfunction_Type() {
        return NciDiseaseGeneRoles.getLongString(Gene_Product_Has_Malfunction_Type.toString());
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Has_Organism_Source() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Has_Organism_Source.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Has_Organism_Source(NciDiseaseGeneRoles role) {
        this.Gene_Product_Has_Organism_Source = role.toString();
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Has_Structural_Domain_Or_Motif(NciDiseaseGeneRoles role) {
        Gene_Product_Has_Structural_Domain_Or_Motif = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Has_Structural_Domain_Or_Motif() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Has_Structural_Domain_Or_Motif.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_is_Biomarker_of(NciDiseaseGeneRoles role) {
        this.Gene_Product_is_Biomarker_of = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_is_Biomarker_of() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_is_Biomarker_of.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_is_Biomarker_Type(NciDiseaseGeneRoles role) {
        this.Gene_Product_is_Biomarker_Type = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_is_Biomarker_Type() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_is_Biomarker_Type.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_is_Pathway_Element(NciDiseaseGeneRoles role) {
        this.Gene_Product_is_Pathway_Element = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_is_Pathway_Element() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_is_Pathway_Element.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_is_Physical_Part_Of(NciDiseaseGeneRoles role) {
        Gene_Product_is_Physical_Part_Of = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_is_Physical_Part_Of() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_is_Physical_Part_Of.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Malfunction_Associated_With_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Product_Malfunction_Associated_With_Disease = role.toString();
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Plays_Role_In_Biological_Process(NciDiseaseGeneRoles role) {
        this.Gene_Product_Plays_Role_In_Biological_Process = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Plays_Role_In_Biological_Process() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Plays_Role_In_Biological_Process.toString());
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Malfunction_Associated_With_Disease() {
        return NciDiseaseGeneRoles.getLongString(Gene_Product_Malfunction_Associated_With_Disease.toString());
    }

    /**
     *
     * @return String
     */
    public String getGene_Malfunction_Associated_With_Disease() {
        return NciDiseaseGeneRoles.getLongString(Gene_Malfunction_Associated_With_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Malfunction_Associated_With_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Malfunction_Associated_With_Disease = role.toString();
    }

    /**
     *
     * @param role
     */
    public void setGene_Expressed_In_Tissue(NciDiseaseGeneRoles role) {
        this.Gene_Expressed_In_Tissue = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Expressed_In_Tissue() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Expressed_In_Tissue.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Found_In_Organism(NciDiseaseGeneRoles role) {
        this.Gene_Found_In_Organism = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Found_In_Organism() {
        return NciDiseaseGeneRoles.getLongString(Gene_Found_In_Organism.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Has_Anormally(NciDiseaseGeneRoles role) {
        this.Gene_Has_Anormally = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Has_Anormally() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Has_Anormally.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Has_Clone(NciDiseaseGeneRoles role) {
        this.Gene_Has_Clone = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Has_Clone() {
        return NciDiseaseGeneRoles.getLongString(Gene_Has_Clone.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Has_Expression_Measurement(NciDiseaseGeneRoles role) {
        this.Gene_Has_Expression_Measurement = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Has_Expression_Measurement() {
        return NciDiseaseGeneRoles.getLongString(Gene_Has_Expression_Measurement.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Has_Function(NciDiseaseGeneRoles role) {
        this.Gene_Has_Function = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Has_Function() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Has_Function.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_In_Chromosomal_Location(NciDiseaseGeneRoles role) {
        this.Gene_In_Chromosomal_Location = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_In_Chromosomal_Location() {
        return NciDiseaseGeneRoles.getLongString(Gene_In_Chromosomal_Location.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_is_Biomarker_of(NciDiseaseGeneRoles role) {
        this.Gene_is_Biomarker_of = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_is_Biomarker_of() {
        return NciDiseaseGeneRoles.getLongString(Gene_is_Biomarker_of.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Is_Pathway_Element(NciDiseaseGeneRoles role) {
        this.Gene_Is_Pathway_Element = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Is_Pathway_Element() {
        return NciDiseaseGeneRoles.getLongString(Gene_Is_Pathway_Element.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Plays_Role_In_Process(NciDiseaseGeneRoles role) {
        this.Gene_Plays_Role_In_Process = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Plays_Role_In_Process() {
        return NciDiseaseGeneRoles.getLongString(Gene_Plays_Role_In_Process.toString());
    }

    /**
     *
     * @param role
     */
    public void setDisease_Has_Cytogenetic_Abnormality(NciDiseaseGeneRoles role) {
        this.Disease_Has_Cytogenetic_Abnormality = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getDisease_Has_Cytogenetic_Abnormality() {
        return NciDiseaseGeneRoles.getLongString(Disease_Has_Cytogenetic_Abnormality.toString());
    }

    /**
     *
     * @param role
     */
    public void setDisease_May_Have_Cytogenetic_Abnormality(NciDiseaseGeneRoles role) {
        this.Disease_May_Have_Cytogenetic_Abnormality = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getDisease_May_Have_Cytogenetic_Abnormality() {
        return NciDiseaseGeneRoles.getLongString(Disease_Has_Molecular_Abnormality.toString());
    }

    /**
     *
     * @return String
     */
    public String getDisease_Has_Molecular_Abnormality() {
        return NciDiseaseGeneRoles.getLongString(Disease_Has_Molecular_Abnormality.toString());
    }

    /**
     *
     * @param role
     */
    public void setDisease_Has_Molecular_Abnormality(NciDiseaseGeneRoles role) {
         this.Disease_Has_Molecular_Abnormality = role.toString();
    }

    /**
     *
     * @param role
     */
    public void setDisease_May_Have_Molecular_Abnormality(NciDiseaseGeneRoles role) {
        this.Disease_May_Have_Molecular_Abnormality =  role.toString();
    }

    /**
     *
     * @return String
     */
    public String getDisease_May_Have_Molecular_Abnormality() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Disease_May_Have_Molecular_Abnormality.toString());
    }


    /**
     * Role Details - otherRole
     */
    @RelProperty
    String Gene_Product_Affects_Disease;

    @RelProperty
    String Gene_Product_Affects_Disease_Process;

    @RelProperty
    String Gene_Product_Expressed_in_Disease;

    @RelProperty
    String Gene_Product_Decreased_in_Disease;

    @RelProperty
    String Gene_Product_Increased_in_Disease;

    @RelProperty
    String Gene_Product_Level_Changed_in_Disease;

    @RelProperty
    String Gene_Expressed_in_Disease;

    @RelProperty
    String Gene_Expression_Downregulated_in_Disease;

    @RelProperty
    String Gene_Expression_Upregulated_in_Disease;

    @RelProperty
    String Gene_Expression_Changed_in_Disease;

    @RelProperty
    String Gene_May_Be_Associated_With_Disease;

    /**
     *
     * @param role
     */
    public void setGene_Product_Affects_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Product_Affects_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Affects_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Affects_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Affects_Disease_Process(NciDiseaseGeneRoles role) {
        this.Gene_Product_Affects_Disease_Process = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Affects_Disease_Process() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Affects_Disease_Process.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Expressed_in_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Product_Expressed_in_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Expressed_in_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Expressed_in_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Decreased_in_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Product_Decreased_in_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Decreased_in_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Decreased_in_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Increased_in_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Product_Increased_in_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Increased_in_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Increased_in_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Level_Changed_in_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Product_Level_Changed_in_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Level_Changed_in_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Level_Changed_in_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Expressed_in_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Expressed_in_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Expressed_in_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Expressed_in_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Expression_Downregulated_in_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Expression_Downregulated_in_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Expression_Downregulated_in_Disease() {
        return NciDiseaseGeneRoles.getLongString(Gene_Expression_Downregulated_in_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Expression_Upregulated_in_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Expression_Upregulated_in_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Expression_Upregulated_in_Disease() {
        return NciDiseaseGeneRoles.getLongString(Gene_Expression_Upregulated_in_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Expression_Changed_in_Disease(NciDiseaseGeneRoles role) {
        this.Gene_Expression_Changed_in_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Expression_Changed_in_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Expression_Changed_in_Disease.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_May_Be_Associated_With_Disease(NciDiseaseGeneRoles role) {
        this.Gene_May_Be_Associated_With_Disease = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_May_Be_Associated_With_Disease() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_May_Be_Associated_With_Disease.toString());
    }

    /**
     * Related_Function appears as -Related_Function
     */
    @RelProperty
    String Gene_Anormaly_has_Disease_Related_Function;

    @RelProperty
    String Gene_Anormaly_May_have_Disease_Related_Function;

    @RelProperty
    String Gene_Product_Anormaly_has_Disease_Related_Function;

    @RelProperty
    String Gene_Product_Anormaly_May_have_Disease_Related_Function;

    @RelProperty
    String Gene_has_Therapeutic_Relevance;

    @RelProperty
    String Gene_May_have_Therapeutic_Relevance;

    @RelProperty
    String Gene_Product_has_Therapeutic_Relevance;

    @RelProperty
    String Gene_Product_May_have_Therapeutic_Relevance;

    /**
     *
     * @param role
     */
    public void setGene_Anormaly_has_Disease_Related_Function(NciDiseaseGeneRoles role) {
        this.Gene_Anormaly_has_Disease_Related_Function = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Anormaly_has_Disease_Related_Function() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Anormaly_has_Disease_Related_Function.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Anormaly_May_have_Disease_Related_Function(NciDiseaseGeneRoles role) {
        this.Gene_Anormaly_May_have_Disease_Related_Function = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Anormaly_May_have_Disease_Related_Function() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Anormaly_May_have_Disease_Related_Function.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Anormaly_has_Disease_Related_Function(NciDiseaseGeneRoles role) {
        this.Gene_Product_Anormaly_has_Disease_Related_Function = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Anormaly_has_Disease_Related_Function() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Anormaly_has_Disease_Related_Function.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_Anormaly_May_have_Disease_Related_Function(NciDiseaseGeneRoles role) {
        this.Gene_Product_Anormaly_May_have_Disease_Related_Function = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_Anormaly_May_have_Disease_Related_Function() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_Anormaly_May_have_Disease_Related_Function.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_has_Therapeutic_Relevance(NciDiseaseGeneRoles role) {
        this.Gene_has_Therapeutic_Relevance = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_has_Therapeutic_Relevance() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_has_Therapeutic_Relevance.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_May_have_Therapeutic_Relevance(NciDiseaseGeneRoles role) {
        this.Gene_May_have_Therapeutic_Relevance = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_May_have_Therapeutic_Relevance() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_May_have_Therapeutic_Relevance.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_has_Therapeutic_Relevance(NciDiseaseGeneRoles role) {
        this.Gene_Product_has_Therapeutic_Relevance = role.toString();
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_has_Therapeutic_Relevance() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_has_Therapeutic_Relevance.toString());
    }

    /**
     *
     * @return String
     */
    public String getGene_Product_May_have_Therapeutic_Relevance() {
        return NciDiseaseGeneRoles.getLongString(NciDiseaseGeneRoles.Gene_Product_May_have_Therapeutic_Relevance.toString());
    }

    /**
     *
     * @param role
     */
    public void setGene_Product_May_have_Therapeutic_Relevance(NciDiseaseGeneRoles role) {
        this.Gene_Product_May_have_Therapeutic_Relevance = role.toString();
    }

     /**
     * All properties that need to be saved for the relationship must
     * have a @RelProperty annotation.
     *  @RelProperty
     */
    @RelProperty
    private String matchedGeneTerm;

    /**
     * The default relation type. The client must set this to what they want.
     */
    @RelType
    private String relType = BioRelTypes.ROLE_OF_GENE.toString();


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
     * @param s
     */
    public void setMatchedGeneTerm(String s) {
        this.matchedGeneTerm = s;
    }

    /**
     *
     * @return String
     */
    public String getMatchedGeneTerm() {
        return this.matchedGeneTerm;
    }

    /**
     * This constructor should not be used preferably, as it does not
     * initialize the start, end nodes.
     *
     */
    public DiseaseGeneRoleRelation() {
    }

    /**
     * Create a BioRelation. This is the preferred constructor.
     *
     * @param startNode
     * @param endNode
     * @param geneTerm
     * @see BioRelTypes
     */
    public DiseaseGeneRoleRelation(Object startNode, Object endNode,
            String geneTerm) {

        this.startNode = startNode;
        this.endNode = endNode;
        this.message = this.relType;
        this.matchedGeneTerm = geneTerm;
    }


    /**
     * {@link NciDiseaseGeneRoles}
     * @param role
     */
    public void setRole(NciDiseaseGeneRoles role) {
        switch(role) {
            case Gene_Product_May_have_Therapeutic_Relevance:
                setGene_Product_May_have_Therapeutic_Relevance(role);
                break;
            case Gene_Product_has_Therapeutic_Relevance:
                setGene_Product_has_Therapeutic_Relevance(role);
                break;
            case Gene_May_have_Therapeutic_Relevance:
                setGene_May_have_Therapeutic_Relevance(role);
                break;
            case Gene_has_Therapeutic_Relevance:
                setGene_has_Therapeutic_Relevance(role);
                break;
            case Gene_Product_Anormaly_May_have_Disease_Related_Function:
                setGene_Product_Anormaly_May_have_Disease_Related_Function(role);
                break;
            case Gene_Product_Anormaly_has_Disease_Related_Function:
                setGene_Product_Anormaly_May_have_Disease_Related_Function(role);
                break;
            case Gene_Anormaly_May_have_Disease_Related_Function:
                setGene_Anormaly_May_have_Disease_Related_Function(role);
                break;
            case Gene_Anormaly_has_Disease_Related_Function:
                setGene_Anormaly_has_Disease_Related_Function(role);
                break;
            case Disease_Has_Cytogenetic_Abnormality:
                setDisease_Has_Cytogenetic_Abnormality(role);
                break;
            case Gene_Product_Affects_Disease:
                setGene_Product_Affects_Disease(role);
                break;
            case Gene_Product_Affects_Disease_Process:
                setGene_Product_Affects_Disease_Process(role);
                break;
            case Gene_Product_Expressed_in_Disease:
                setGene_Product_Expressed_in_Disease(role);
                break;
            case Gene_Product_Decreased_in_Disease:
                setGene_Product_Decreased_in_Disease(role);
                break;
            case Gene_Product_Increased_in_Disease:
                setGene_Product_Increased_in_Disease(role);
                break;
            case Gene_Product_Level_Changed_in_Disease:
                setGene_Product_Level_Changed_in_Disease(role);
                break;
            case Gene_Expressed_in_Disease:
                setGene_Expressed_in_Disease(role);
                break;
            case Gene_Expression_Downregulated_in_Disease:
                setGene_Expression_Downregulated_in_Disease(role);
                break;
            case Gene_Expression_Upregulated_in_Disease:
                setGene_Expression_Upregulated_in_Disease(role);
                break;
            case Gene_Expression_Changed_in_Disease:
                setGene_Expression_Changed_in_Disease(role);
                break;
            case Gene_May_Be_Associated_With_Disease:
                setGene_May_Be_Associated_With_Disease(role);
                break;
            case Gene_Associated_With_Disease:
                setGene_Associated_With_Disease(role);
                break;
            case Gene_Product_Anormaly_Affects_Pathway:
                setGene_Product_Anormaly_Affects_Pathway(role);
                break;
            case Gene_Product_Anomaly_Related_To_Gene_Anormaly:
                setGene_Product_Anomaly_Related_To_Gene_Anormaly(role);
                break;
            case Gene_Product_Encoded_By_Gene:
                setGene_Product_Encoded_By_Gene(role);
                break;
            case Gene_Product_Expressed_In_Tissue:
                setGene_Product_Expressed_In_Tissue(role);
                break;
            case Gene_Product_Has_Associated_Anatomie:
                setGene_Product_Has_Associated_Anatomie(role);
                break;
            case Gene_Product_Has_Biochemical_Function:
                setGene_Product_Has_Biochemical_Function(role);
                break;
            case Gene_Product_Has_Chemical_Classification:
                setGene_Product_Has_Chemical_Classification(role);
                break;
            case Gene_Product_Has_Malfunction_Type:
                setGene_Product_Has_Malfunction_Type(role);
                break;
            case Gene_Product_Has_Organism_Source:
                setGene_Product_Has_Organism_Source(role);
                break;
            case Gene_Product_Has_Structural_Domain_Or_Motif:
                setGene_Product_Has_Structural_Domain_Or_Motif(role);
                break;
            case Gene_Product_is_Biomarker_of:
                setGene_Product_is_Biomarker_of(role);
                break;
            case Gene_Product_is_Biomarker_Type:
                setGene_Product_is_Biomarker_Type(role);
                break;
            case Gene_Product_is_Pathway_Element:
                setGene_Product_is_Pathway_Element(role);
                break;
            case Gene_Product_is_Physical_Part_Of:
                setGene_Product_is_Physical_Part_Of(role);
                break;
            case Gene_Product_Malfunction_Associated_With_Disease:
                setGene_Product_Malfunction_Associated_With_Disease(role);
                break;
            case Gene_Product_Plays_Role_In_Biological_Process:
                setGene_Product_Plays_Role_In_Biological_Process(role);
                break;
            case Gene_Malfunction_Associated_With_Disease:
                setGene_Malfunction_Associated_With_Disease(role);
                break;
            case Gene_Expressed_In_Tissue:
                setGene_Expressed_In_Tissue(role);
                break;
            case Gene_Found_In_Organism:
                setGene_Found_In_Organism(role);
                break;
            case Gene_Has_Anormally:
                setGene_Has_Anormally(role);
                break;
            case Gene_Has_Clone:
                setGene_Has_Clone(role);
                break;
            case Gene_Has_Expression_Measurement:
                setGene_Has_Expression_Measurement(role);
                break;
            case Gene_Has_Function:
                setGene_Has_Function(role);
                break;
            case Gene_In_Chromosomal_Location:
                setGene_In_Chromosomal_Location(role);
                break;
            case Gene_is_Biomarker_of:
                setGene_is_Biomarker_of(role);
                break;
            case Gene_Is_Pathway_Element:
                setGene_Is_Pathway_Element(role);
                break;
            case Gene_Plays_Role_In_Process:
                setGene_Plays_Role_In_Process(role);
                break;
            case Disease_May_Have_Cytogenetic_Abnormality:
                setDisease_May_Have_Cytogenetic_Abnormality(role);
                break;
            case Disease_Has_Molecular_Abnormality:
                setDisease_Has_Molecular_Abnormality(role);
                break;
            case Disease_May_Have_Molecular_Abnormality:
                setDisease_May_Have_Molecular_Abnormality(role);
                break;
            default:
                break;
        }
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
     * This method is implemented yet.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format("%s interacts %s  ", startNode, endNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        DiseaseGeneRoleRelation relation = (DiseaseGeneRoleRelation)o;
        if (id == null) return super.equals(o);
        return id.equals(relation.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}