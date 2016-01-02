/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;

/**
 * ComplexComponent Relation is used to depict the relationship complex components in NCIPathway
 * @author jtanisha-ee
 */
@RelationshipEntity
public class ComplexComponentRelation {

    protected static Log log = LogFactory.getLog(new Object().getClass());

     /**
     * The start node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @StartNode
    private Object startNode;

     /**
     * The start node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @EndNode
    private Object endNode;


    /**
     * The default relation type. The client must set this to what they want.
     */
    @RelType
    private String relType = "IS_A_COMPONENT_OF";

    /**
     * This property is required as it is not displayed without it.
     */
    @RelProperty
    private String message;

    /**
     * Location property:  calcium-store, cytoplasm, cellular_component, endosome,
     * integral to membrane, nuclear pore, transmembrane, plasma membrane
     * <Label label_type="location" value="cytoplasm" />
     */
    @RelProperty String location;

    /**
     * activityState: active, inactive, activity-state
     * <Label label_type="activity-state" value="active" />
     */
    @RelProperty String activityState;

    /**
     * function: serine protease,
     * metalloendopeptidase activity,
     * calcium- and calmodulin-responsive adenylate cyclase activity
     * transcriptional repressor activity
     * <Label label_type="function" value="serine protease" />
     *
     */
    @RelProperty String function;

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
     *
     * @return Object {@link StartNode}
     */
    public Object getStartNode() {
        return this.startNode;
    }

    /**
     * @return Object {@link EndNode}
     */
    public Object getEndNode() {
        return this.endNode;
    }


     /**
     * <p>
     * Sets function
     * @param function
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * <p>
     *  {@link RelProperty} {@link BioFields#FUNCTION}
     * function: metalloendopeptidase activity,
     * calcium- and calmodulin-responsive adenylate cyclase activity
     * transcriptional repressor activity
     *
     * <InteractionComponent role_type="input" molecule_idref="204036">
     *    <Label label_type="function" value="serine protease" />
     * @return String function
     */
    public String getFunction() {
        return this.function;
    }

    /**
     * <p>
     * {@link RelProperty}
     * sets the activityState
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
     *
     * Location property:  calcium-store, cytoplasm, cellular_component, endosome,
     * integral to membrane, nuclear pore, transmembrane, plasma membrane
     * <Label label_type="location" value="cytoplasm" />
     * @param location {@link RelProperty} {@link BioFields#LOCATION}
     */
    public void setLocation(String location) {
        this.location = location;
    }

    /**
     * <p>
     * <@link RelProperty}
     * Location property:  calcium-store, cytoplasm, cellular_component, endosome,
     * integral to membrane, nuclear pore, transmembrane, plasma membrane
     * <Label label_type="location" value="cytoplasm" />
     * @return String location {@link ComplexComponentRelation#location} {@link BioFields#LOCATION}
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
    public void setRelType(String relType) {
        this.relType = relType;
    }

    /**
     * <p>
     * {@link RelType}
     * @return String
     */
    public String getRelType() {
        return this.relType;
    }

    /**
     * <p>
     * For each component
     * @param startNode {@link StartNode} {@link ComplexComponentRelation#StartNode}
     * @param endNode {@link EndNode} {@link ComplexComponentRelation#EndNode}
     * @param relType {@link org.atgc.bio.meta.BioRelTypes} {@link ComplexComponentRelation#relType}
     */
    public final void setComplexComponentRelation(Object startNode, Object endNode,
            BioRelTypes relType) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.relType = relType.toString();
        this.message = this.relType;
    }


   /**
    * <p>
    * @param startNode {@link StartNode} {@link ComplexComponentRelation#startNode}
    * @param endNode {@link EndNode} {@link ComplexComponentRelation#endNode}
    * @param relType {@link ComplexComponentRelation#relType}
    */
   public ComplexComponentRelation( Object startNode, Object endNode, BioRelTypes relType) {
        setComplexComponentRelation(startNode, endNode, relType);
   }

    /**
     *
     * @param startNode
     * @param endNode
     * @param relType
     * @param map
     */
    public ComplexComponentRelation(Object startNode, Object endNode, BioRelTypes relType,
                                Map map) {
       setComplexComponentRelation(startNode, endNode, relType);
       setLabels(map);
   }

   /**
    *
    * @param labelValues
    */
   private void setLabels(Map map) {

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
}