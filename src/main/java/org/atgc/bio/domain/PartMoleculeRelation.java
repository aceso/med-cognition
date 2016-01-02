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
public class PartMoleculeRelation {

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
     * start and end indicate the position of a molecule that is part of another whole molecule
     * start and end positions: "155" and "731"
     */
    @RelProperty
    private String start;

     /**
     * All properties that need to be saved for the relationship must
     * have a RelProperty annotation.
     * start - end indicate the position of a molecule that is part of another molecule
     * start example "155"
     * end example "731"
     */
    @RelProperty
    private String end;

    /**
     * The default relation type. The client must set this to what they want.
     */
    @RelType
    private BioRelTypes relType = BioRelTypes.IS_PART_OF;

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
     * This constructor should not be used preferably, as it does not
     * initialize the start, end nodes.
     *
     */
    public PartMoleculeRelation() {
    }

    /**
     * Create a BioRelation. This is the preferred constructor.
     *
     * @param startNode
     * @param endNode
     * @param relType
     * @param start
     * @param end
     * @see BioRelTypes
     */
    public PartMoleculeRelation(Object startNode, Object endNode, BioRelTypes relType, String start, String end) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.relType = relType;
        this.start = start;
        this.end = end;
        this.message = this.relType.toString();
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
     * Example is start, start position "155"
     *
     * @return String
     */
    public String getStart() {
        return start;
    }

    /**
     * Example is end, end position "731"
     * @return String
     */
    public String getEnd() {
        return end;
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

    public void setStart(String start) {
        this.start = start;
    }

    public void setEnd(String end) {
        this.end = end;
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
        return String.format("%s interacts %s in %s", startNode, start, end, endNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        PartMoleculeRelation relation = (PartMoleculeRelation) o;
        if (id == null) return super.equals(o);
        return id.equals(relation.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}