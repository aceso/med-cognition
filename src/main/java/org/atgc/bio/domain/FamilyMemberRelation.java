/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.EndNode;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.RelProperty;
import org.atgc.bio.meta.RelType;
import org.atgc.bio.meta.RelationshipEntity;
import org.atgc.bio.meta.StartNode;
import org.atgc.bio.meta.*;

import java.util.Map;

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
public class FamilyMemberRelation {

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
     */
    @RelProperty
    private String name = "FAMILY_MEMBER_LIST";

    /**
     * The default relation type. The client must set this to what they want.
     */
    @RelType
    private String relType = BioRelTypes.IS_MEMBER_OF_FAMILY.toString();

    /**
     * This property is required as it is not displayed without it.
     */
    @RelProperty
    private String message;

     /**
     * <p>
     * Location property:  calcium-store, cytoplasm, cellular_component, endosome,
     * integral to membrane, nuclear pore, transmembrane, plasma membrane
     * <Label label_type="location" value="cytoplasm" />
     */
    @RelProperty
    private String location;

    /**
     * <p>
     * activityState: active, inactive, activity-state
     *  <InteractionComponentList>
     *     <InteractionComponent role_type="agent" molecule_idref="200373">
     *       <Label label_type="location" value="cytoplasm" />
     *       <Label label_type="activity-state" value="active" />
     *     </InteractionComponent>
     *   </InteractionComponentList>
     */
    @RelProperty
    private String activityState;

    /**
     * <p>
     * function: metalloendopeptidase activity,
     * calcium- and calmodulin-responsive adenylate cyclase activity
     * transcriptional repressor activity
     *
     * <InteractionComponent role_type="input" molecule_idref="204036">
     *    <Label label_type="function" value="serine protease" />
     *
     */
    @RelProperty
    private String function;

     /**
     * <p>
     * @param function
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * <p>
     *  {@link RelProperty} {@link BioFields#FUNCTION}
     *
     * function: metalloendopeptidase activity,
     * calcium- and calmodulin-responsive adenylate cyclase activity
     * transcriptional repressor activity
     *
     *
     * <Label label_type="function" value="serine protease" />
     * @return String function {@link OncoRelation#function}
     */
    public String getFunction() {
        return this.function;
    }

    /**
     * <p>
     * sets activity state
     * @param activityState
     */
    public void setActivityState(String activityState) {
        this.activityState = activityState;
    }

    /**
     * <p>
     *  {@link RelProperty} {@link BioFields#ACTIVITY_STATE}
     * activityState: active, inactive, activity-state
     *  <FamilyMemberList>
     * <Member member_molecule_idref="201720">
     *       <Label label_type="activity-state" value="inactive" />
     * </Member>
     *  </FamilyMemberList>
     *
     * @return String activityState
     */
    public String getActivityState() {
        return this.activityState;
    }

    /**
     * <p>
     * {@link RelProperty}  {@link BioFields#LOCATION}
     * Location property:  calcium-store, cytoplasm, cellular_component, endosome,
     * integral to membrane, nuclear pore, transmembrane, plasma membrane
     * <Label label_type="location" value="cytoplasm" />
     * sets location
     * @param location
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * <p>
     * {@link RelProperty} {@link BioFields#LOCATION}
     * Location property:  calcium-store, cytoplasm, cellular_component, endosome,
     * integral to membrane, nuclear pore, transmembrane, plasma membrane
     * <Label label_type="location" value="cytoplasm" />
     * @return String location {@link OncoRelation#location}
     */
    public String getLocation() {
        return this.location;
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

    /**
     * This constructor should not be used preferably, as it does not
     * initialize the start, end nodes.
     *
     */
    public FamilyMemberRelation() {
    }

    /**
     * Create a BioRelation. This is the preferred constructor.
     *
     * @param startNode
     * @param endNode
     * @param relType
     * @see BioRelTypes
     */
    public FamilyMemberRelation(Object startNode, Object endNode, BioRelTypes relType) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.relType = relType.toString();
        this.message = this.relType;
    }

   /**
    * <p>
    * sets specific label properties related with a given relationship
    * {@link RelProperty} {@link BioFields#LOCATION}
    * {@link RelProperty} {@link BioFields#ACTIVITY_STATE}
    * {@link RelProperty} {@link BioFields#FUNCTION}
    * @param location {@link OncoRelation#location}
    * @param activityState {@link OncoRelation#activityState}
    * @param function {@link OncoRelation#function}
    *
    */
   public void setOtherProperties(String location, String activityState, String function) {
       this.location = location;
       this.activityState = activityState;
       this.function = function;
   }

    /**
     *
     * @param map
     */
    public void setLabels(Map map) {
        if (map.size() > 0) {
            if (map.get(BioFields.FUNCTION) != null) {
              this.setFunction((String)map.get(BioFields.FUNCTION));
            }
            if (map.get(BioFields.ACTIVITY_STATE) != null) {
               this.setActivityState((String)map.get(BioFields.ACTIVITY_STATE));
            }
            if (map.get(BioFields.LOCATION) != null) {
               this.setLocation((String)map.get(BioFields.LOCATION));
            }
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
     * Example from Intact is "phosphorylation"
     *
     * @return String
     */
    public String getName() {
        return name;
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
     *
     * @param name
     */
    public void setName(String name) {
        this.name = name;
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
     * This method is implemented yet.
     *
     * @return String
     */
    @Override
    public String toString() {
        return String.format("%s interacts %s in %s", startNode, name, endNode);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        FamilyMemberRelation bioRelation = (FamilyMemberRelation) o;
        if (id == null) return super.equals(o);
        return id.equals(bioRelation.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }


}