/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.meta.*;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.RelProperty;
import org.atgc.bio.meta.RelType;
import org.atgc.bio.meta.RelationshipEntity;
import org.atgc.bio.meta.StartNode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Developers can declare their own classes that use the {@link RelationshipEntity}.
 * However, the relationships do not seem to have so many properties. So
 * the {@link BioRelation} class works in most cases. We will use the BioRelation
 * more frequently, but the template supports any {@link RelationshipEntity}
 * classes.
 *
 * The following annotations are supported:
 *
 * This is used in NciGene-Drug relationships.
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
public class NciDrugGeneRoleRelation {

    protected static Log log = LogFactory.getLog(NciDrugGeneRoleRelation.class);

    /**
     * DrugGeneRelationship
     *
     * There can be multiple genes associated with a drug.
     * A single gene can be associated with multiple drugs.
     * Evidence for this is maintained in the NciDrug BioEntity
     *
     * OtherRole - role details
     *
     *
     */
    @RelProperty
    String not_assigned;

    @RelProperty
    String Chemical_or_Drug_Affects_Cell_Type_or_Tissue;

    @RelProperty
    String Chemical_or_Drug_Plays_Role_in_Biological_Process;

    @RelProperty
    String Chemical_or_Drug_FDA_Approved_for_Disease;

    @RelProperty
    String Chemical_or_Drug_Is_Metabolized_By_Enzyme;

    @RelProperty
    String Chemical_or_Drug_Has_Accepted_Therapeutic_Use_For;

    @RelProperty
    String Chemical_or_Drug_Has_Study_Therapeutic_Use_For;

    @RelProperty
    String Chemical_or_Drug_Has_Mechanism_Of_Action;

    @RelProperty
    String Chemical_or_Drug_Affects_Gene_Product;

    @RelProperty
    String Chemical_or_Drug_Has_Target_Gene_Product;


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
     * All properties that need to be saved for the relationship must
     * have a @RelProperty annotation.
     * @RelProperty roles {@NciGEneCompoundRoles}
     *
     * Create properties for each role
     */


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
    private BioRelTypes relType = BioRelTypes.ROLE_OF_GENE;


    /**
     * This property is required as it is not displayed without it.
     */
    @RelProperty
    private String message;

    public void setNot_assigned(NciGeneDrugRoles s) {
        this.not_assigned = s.toString();
    }

    public String getNot_assigned(){
        return this.not_assigned;
    }

    public void setChemical_or_Drug_Affects_Cell_Type_or_Tissue(NciGeneDrugRoles s) {
        this.Chemical_or_Drug_Affects_Cell_Type_or_Tissue = s.toString();
    }

    public String getChemical_or_Drug_Affects_Cell_Type_or_Tissue() {
        return this.Chemical_or_Drug_Affects_Cell_Type_or_Tissue;
    }

    public void setChemical_or_Drug_FDA_Approved_for_Disease(NciGeneDrugRoles Chemical_or_Drug_FDA_Approved_for_Disease) {
        this.Chemical_or_Drug_FDA_Approved_for_Disease = Chemical_or_Drug_FDA_Approved_for_Disease.toString();
    }

    public String getChemical_or_Drug_FDA_Approved_for_Disease() {
        return Chemical_or_Drug_FDA_Approved_for_Disease;
    }


    public void setChemical_or_Drug_Has_Accepted_Therapeutic_Use_For(NciGeneDrugRoles Chemical_or_Drug_Has_Accepted_Therapeutic_Use_For) {
        this.Chemical_or_Drug_Has_Accepted_Therapeutic_Use_For = Chemical_or_Drug_Has_Accepted_Therapeutic_Use_For.toString();
    }

    public String getChemical_or_Drug_Has_Accepted_Therapeutic_Use_For() {
        return Chemical_or_Drug_Has_Accepted_Therapeutic_Use_For;
    }

    public void setChemical_or_Drug_Plays_Role_in_Biological_Process(NciGeneDrugRoles Chemical_or_Drug_Plays_Role_in_Biological_Process) {
        this.Chemical_or_Drug_Plays_Role_in_Biological_Process = Chemical_or_Drug_Plays_Role_in_Biological_Process.toString();
    }

    public String getChemical_or_Drug_Plays_Role_in_Biological_Process() {
        return Chemical_or_Drug_Plays_Role_in_Biological_Process;
    }

    public void setChemical_or_Drug_Is_Metabolized_By_Enzyme(NciGeneDrugRoles Chemical_or_Drug_Is_Metabolized_By_Enzyme) {
        this.Chemical_or_Drug_Is_Metabolized_By_Enzyme = Chemical_or_Drug_Is_Metabolized_By_Enzyme.toString();
    }

    public String getChemical_or_Drug_Is_Metabolized_By_Enzyme() {
        return Chemical_or_Drug_Is_Metabolized_By_Enzyme;
    }

    public void setChemical_or_Drug_Has_Target_Gene_Product(NciGeneDrugRoles Chemical_or_Drug_Has_Target_Gene_Product) {
        this.Chemical_or_Drug_Has_Target_Gene_Product = Chemical_or_Drug_Has_Target_Gene_Product.toString();
    }

    public String getChemical_or_Drug_Has_Target_Gene_Product() {
        return Chemical_or_Drug_Has_Target_Gene_Product;
    }

    public void setChemical_or_Drug_Has_Study_Therapeutic_Use_For(NciGeneDrugRoles Chemical_or_Drug_Has_Study_Therapeutic_Use_For) {
        this.Chemical_or_Drug_Has_Study_Therapeutic_Use_For = Chemical_or_Drug_Has_Study_Therapeutic_Use_For.toString();
    }

    public String getChemical_or_Drug_Has_Study_Therapeutic_Use_For() {
        return Chemical_or_Drug_Has_Study_Therapeutic_Use_For;
    }

    public void setChemical_or_Drug_Affects_Gene_Product(NciGeneDrugRoles Chemical_or_Drug_Affects_Gene_Product) {
        this.Chemical_or_Drug_Affects_Gene_Product = Chemical_or_Drug_Affects_Gene_Product.toString();
    }

    public String getChemical_or_Drug_Affects_Gene_Product() {
        return Chemical_or_Drug_Affects_Gene_Product;
    }

    public void setChemical_or_Drug_Has_Mechanism_Of_Action(NciGeneDrugRoles Chemical_or_Drug_Has_Mechanism_Of_Action) {
        this.Chemical_or_Drug_Has_Mechanism_Of_Action = Chemical_or_Drug_Has_Mechanism_Of_Action.toString();
    }

    public String getChemical_or_Drug_Has_Mechanism_Of_Action() {
        return Chemical_or_Drug_Has_Mechanism_Of_Action;
    }


    public void setMessage(String message) {
        this.message = message;
    }


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

    public void setMatchedGeneTerm(String s) {
        this.matchedGeneTerm = s;
    }

    public String getMatchedGeneTerm() {
        return this.matchedGeneTerm;
    }

    /**
     * This constructor should not be used preferably, as it does not
     * initialize the start, end nodes.
     *
     */
    public NciDrugGeneRoleRelation() {
    }

    /**
     * Create a BioRelation. This is the preferred constructor.
     *
     * @param startNode
     * @param endNode
     * @param geneTerm
     * @see BioRelTypes
     */
    public NciDrugGeneRoleRelation(Object startNode, Object endNode,
            String geneTerm) {

        this.startNode = startNode;
        this.endNode = endNode;
        this.message = this.relType.toString();
        this.matchedGeneTerm = geneTerm;
    }



    /**
     * {@link NciGeneDrugRoles}
     * @param role
     */
    public void setRole(NciGeneDrugRoles role) {
        switch(role) {
            case not_assigned:
                setNot_assigned(role);
                break;
            case Chemical_or_Drug_Affects_Cell_Type_or_Tissue:
                 setChemical_or_Drug_Affects_Cell_Type_or_Tissue(role);
                 break;
            case Chemical_or_Drug_Affects_Gene_Product:
                 setChemical_or_Drug_Affects_Gene_Product(role);
                 break;
            case Chemical_or_Drug_FDA_Approved_for_Disease:
                 setChemical_or_Drug_FDA_Approved_for_Disease(role);
                 break;
            case Chemical_or_Drug_Has_Accepted_Therapeutic_Use_For:
                 setChemical_or_Drug_Has_Accepted_Therapeutic_Use_For(role);
                 break;
            case Chemical_or_Drug_Has_Mechanism_Of_Action:
                 setChemical_or_Drug_Has_Mechanism_Of_Action(role);
                  break;
            case Chemical_or_Drug_Has_Study_Therapeutic_Use_For:
                 setChemical_or_Drug_Has_Study_Therapeutic_Use_For(role);
                 break;
            case Chemical_or_Drug_Has_Target_Gene_Product:
                 setChemical_or_Drug_Has_Target_Gene_Product(role);
                 break;
            case Chemical_or_Drug_Is_Metabolized_By_Enzyme:
                 setChemical_or_Drug_Is_Metabolized_By_Enzyme(role);
                 break;
            case Chemical_or_Drug_Plays_Role_in_Biological_Process:
                 this.setChemical_or_Drug_Plays_Role_in_Biological_Process(role);
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
    public BioRelTypes getRelType() {
        return relType;
    }

    /**
     * Every relationship has a type that is declared in <code>BioRelTypes</code>
     *
     * @param relType
     */
    public void setRelType(BioRelTypes relType) {
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

        NciDrugGeneRoleRelation relation = (NciDrugGeneRoleRelation)o;
        if (id == null) return super.equals(o);
        return id.equals(relation.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}