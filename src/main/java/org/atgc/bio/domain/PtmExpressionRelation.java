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
public class PtmExpressionRelation {

    /**
     * This expression can be part of any molecule either
     *  {@link Complex}  or {@link Protein}
     *
     * The molecule_idref refers to {@link Protein} molecule
     *
     *  "@molecule_idref" : "200425",
     * "PTMExpression" : [
     *           {
     *                   "@protein" : "P16144",
     *                   "@position" : "0",
     *                   "@aa" : "S",
     *                   "@modification" : "phosphorylation"
     *          }
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
     * All properties that need to be saved for the relationship must
     * have a @RelProperty annotation.
     * "@protein" : "P31751"
     */
    @RelProperty
    private String proteinIdentifier;

     /**
     * All properties that need to be saved for the relationship must
     * have a @RelProperty annotation.
     *  "@position" : "309"
     *
     */
    @RelProperty
    private String position;


    /**
     * "@aa" : "S"
     */
    @RelProperty
    private String aa;

    /**
     * The default relation type. The client must set this to what they want.
     */
    @RelType
    private BioRelTypes relType = BioRelTypes.IS_PTM_EXPRESSION_OF;

    /**
     * @modification : "phosphorylation"
     */
    @RelType
    private String modification;

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
     * @return
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
     * This constructor should not be used preferably, as it does not
     * initialize the start, end nodes.
     *
     */
    public PtmExpressionRelation() {
    }

    /**
     * Create a BioRelation. This is the preferred constructor.
     *
     * @param startNode
     * @param endNode
     * @param relType
     * @param pi
     * @param pos
     * @param aa
     * @param modification
     * @see BioRelTypes
     */
    public PtmExpressionRelation(Object startNode, Object endNode, BioRelTypes relType,
            String pi, String pos, String aa, String modification) {

        this.startNode = startNode;
        this.endNode = endNode;
        this.relType = relType;
        this.proteinIdentifier = pi;
        this.position = pos;
        this.aa = aa;
        this.modification = modification;
        this.message = this.relType.toString();
    }

    /**
     * This method should be used to validate the BioRelation, especially
     * before it is used to connect BioEntities. We recommend that
     * developers use this method always.
     *
     * @param startClass
     * @param endClass
     * @return
     * @see BioEntityClasses
     */
    public boolean isValid(BioEntityClasses startClass, BioEntityClasses endClass) {
        if ((startNode == null) || (endNode == null) ) return false;
        if (!startNode.getClass().equals(startClass.getAnnotationClass())) return false;
        return endNode.getClass().equals(endClass.getAnnotationClass());
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
     * @protein : "P31751"
     *
     * @return protein identifier {@link BioFields#UNIPROT_ID}
     */
    public String getProteinIdentifier() {
        return proteinIdentifier;
    }

    /**
     * @return
     * @position : 309
     * return position
     */
    public String getPosition() {
        return position;
    }


    /**
     * @aa : "S"
     * @return aa;
     */
    public String getAa() {
        return aa;
    }

    /**
     *
     * @modification : "phosphorlyation"
     * @return
     */
    public String getModification() {
        return modification;
    }

    /**
     * Every relationship has a type that is declared in <code>BioRelTypes</code>
     *
     * @return
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

    public void setProteinIdentifier(String pi) {
        this.proteinIdentifier = pi;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public void setAa(String aa) {
        this.aa = aa;
    }

    public void setModification(String modification) {
        this.modification = modification;
    }

    /**
     * The start node must be a {@link BioEntity} class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     *
     * @return
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
     * @return
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
     * @return
     */
    @Override
    public String toString() {
        return String.format("%s interacts %s in %s", startNode, proteinIdentifier, position, endNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PtmExpressionRelation relation = (PtmExpressionRelation) o;
        if (id == null) return super.equals(o);
        return id.equals(relation.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}