/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.BioFields;
import org.atgc.bio.meta.*;

/**
 * OncoRelation is used to depict the relationship of interaction components in NCIPathway
 * @author jtanisha-ee
 */
@RelationshipEntity
public class OncoRelation {

    protected static Logger log = LogManager.getLogger(OncoRelation.class);

    /**
     * <p>
     * The start node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @StartNode
    private Object startNode;

    /**
     * <p>
     * The start node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @EndNode
    private Object endNode;

    /**
     * <p>
     * The default relation type. The client must set this to what they want.
     */
    @RelType
    private BioRelTypes relType = BioRelTypes.INTERACTION_COMPONENT;

    /**
     * <p>
     * Every OncoRelation has interaction Id  And it cannot be null,
     * if user would like to persist.
     * This is typically populated at the time of
     * creating this relationship, through a constructor.
     * For NciPathway, interactionId is {@link BioFields#ID}
     */
    @RelProperty
    private String interactionId;

     /**
     * <p>
     * Start node role can be
     * NciPathwayFields.INPUT
     * NciPathwayFields.INHIBITOR,
     * NciPathwayFields.AGENT,
     * NciPathwayFields.EDGE_TYPE,
     * NciPathwayFields.INCOMING_EDGE,
     * NciPathwayFields.OUTGOING_EDGEINPUT,
     *
     * And it cannot be null,
     * This is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @RelProperty
    private String startNodeRole;

    /**
     * <p>
     * End node role can be output, interaction
     * It cannot be null
     * This is typically populated at the time of
     * creating the relationship
     */
    @RelProperty
     private String endNodeRole;

    /**
     * <p>
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
     * {@link StartNode}
     * @param node
     */
    public void setStartNode(Object node) {
        this.startNode = node;
    }

    /**
     * <p>
     * {@link EndNode}
     * @param node
     */
    public void setEndNode(Object node) {
        this.endNode = node;
    }

    /**
     * <p>
     * {@link RelProperty} {@link BioFields#INTERACTION_ID}
     * @param id
     */
    public void setInteractionId(String id) {
        this.interactionId = id;
    }

     /**
     * <p>
     * {@link StartNode} {@link RelProperty}
     * @param role
     */
    public void setStartNodeRole(String role) {
        this.startNodeRole = role;
    }

    /**
     * <p>
     * {@link EndNode} {@link RelProperty}
     * @param role
     */
    public void setEndNodeRole(String role) {
        this.endNodeRole = role;
    }

    /**
     * <p>
     * @return Object {@link StartNode}
     */
    public Object getStartNode() {
        return this.startNode;
    }

    /**
     * <p>
     * @return Object {@link EndNode}
     */
    public Object getEndNode() {
        return this.endNode;
    }

    /**
     * <p>
     * Start node role can be
     * NciPathwayFields.INPUT
     * NciPathwayFields.INHIBITOR,
     * NciPathwayFields.AGENT,
     * NciPathwayFields.EDGE_TYPE,
     * NciPathwayFields.INCOMING_EDGE,
     * NciPathwayFields.OUTGOING_EDGEINPUT,
     *
     * And it cannot be null,
     * This is typically populated at the time of
     * creating this relationship, through a constructor.
     * {@link RelProperty} {@link EndNode}
     * @return String
     */
    public String getEndNodeRole() {
        return this.endNodeRole;
    }

    /**
     * <p>
     * Start node role can be
     * NciPathwayFields.INPUT
     * NciPathwayFields.INHIBITOR,
     * NciPathwayFields.AGENT,
     * NciPathwayFields.EDGE_TYPE,
     * NciPathwayFields.INCOMING_EDGE,
     * NciPathwayFields.OUTGOING_EDGEINPUT,
     *
     * And it cannot be null,
     * This is typically populated at the time of
     * creating this relationship, through a constructor.
     * {@link RelProperty} {@link StartNode}
     * @return String
     */
    public String getStartNodeRole() {
        return this.startNodeRole;
    }

    /**
     * <p>
     * {@link RelProperty} {@link BioFields#INTERACTION_ID}
     * Returns interaction identifier for this interaction relationship for a given molecule
     * @return String
     */
    public String getInteractionId() {
        return this.interactionId;
    }


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
     * <InteractionComponent role_type="input" molecule_idref="204036">
     *    <Label label_type="function" value="serine protease" />
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
     *  <InteractionComponentList>
     *     <InteractionComponent role_type="agent" molecule_idref="200373">
     *       <Label label_type="location" value="cytoplasm" />
     *       <Label label_type="activity-state" value="active" />
     *     </InteractionComponent>
     *   </InteractionComponentList>
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
     * <p>
     * This property is required as it is not displayed without it.
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * <p>
     * This property is required as it is not displayed without it.
     *
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     * <p>
     * {@link RelType}
     * @param relType
     */
    public void setRelType(BioRelTypes relType) {
        this.relType = relType;
    }

    /**
     * <p>
     * {@link RelType}
     * @return String
     */
    public BioRelTypes getRelType() {
        return this.relType;
    }


    /**
     * <p>
     * Sets the oncoRelationship for each interaction component
     * For each component
     * @param interactionId {@link OncoRelation#interactionId}
     * @param startNodeRole {@link OncoRelation#startNodeRole}
     * @param endNodeRole {@link OncoRelation#endNodeRole}
     * @param startNode {@link OncoRelation#startNode}
     * @param endNode {@link OncoRelation#endNode}
     * @param relType {@link OncoRelation#relType}
     */
    public final void setOncoRelation(String interactionId,
                          String startNodeRole, String endNodeRole,
                          Object startNode, Object endNode, BioRelTypes relType) {

        this.interactionId = interactionId;
        this.startNodeRole = startNodeRole;
        this.endNodeRole = endNodeRole;
        this.startNode = startNode;
        this.endNode = endNode;
        this.relType = relType;
        this.message = relType.toString();
    }


   /**
    * <p>
    *
    * {@link RelProperty}
     * @param interactionId
    * @param startNodeRole {@link RelProperty} {@link OncoRelation#startNodeRole}
    * @param endNodeRole {@link RelProperty} {@link OncoRelation#endNodeRole}
    * @param startNode {@link StartNode}
    * @param endNode {@link EndNode}
     * @param roleType
    *
    */
   public OncoRelation(String interactionId, String startNodeRole, String endNodeRole,
                          Object startNode, Object endNode, BioRelTypes roleType) {

        setOncoRelation(interactionId, startNodeRole, endNodeRole,
                         startNode, endNode, roleType);
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

}