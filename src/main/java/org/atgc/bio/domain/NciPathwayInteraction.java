/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * Connect this to the disease BioEntity
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.NCI_PATHWAY_INTERACTION)
public class NciPathwayInteraction {

    protected static Logger log = LogManager.getLogger(NciPathwayInteraction.class);

    @GraphId
    private Long id;

    @UniquelyIndexed(indexName=IndexNames.NCI_PATHWAY_INTERACTION_ID)
    @Taxonomy(rbClass=TaxonomyTypes.NCI_PATHWAY_INTERACTION_ID, rbField= BioFields.NCI_PATHWAY_INTERACTION_ID)
    private String interactionId;

    //@GloballyIndexed
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.NCI_PATHWAY_INTERACTION.toString();

    @NonIndexed
    public String message;

    @Indexed (indexName=IndexNames.NCI_PATHWAY_INTERACTION_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NCI_PATHWAY_INTERACTION_TYPE, rbField=BioFields.NCI_PATHWAY_INTERACTION_TYPE)
    private String interactionType;


    @FullTextIndexed(indexName = IndexNames.EVIDENCE_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.EVIDENCE_LIST, rbField=BioFields.EVIDENCE_LIST)
    private String evidenceList;

    @FullTextIndexed (indexName = IndexNames.IS_CONDITION_POSITIVE)
    @Taxonomy (rbClass=TaxonomyTypes.IS_CONDITION_POSITIVE, rbField=BioFields.IS_CONDITION_POSITIVE)
    private String isConditionPositive;

    /**
     * pubmed list
     */
    @FullTextIndexed (indexName = IndexNames.REFERENCE_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.REFERENCE_LIST, rbField=BioFields.REFERENCE_LIST)
    private String referenceList;

    @Indexed (indexName=IndexNames.PATHWAY_SOURCE_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_SOURCE_ID, rbField=BioFields.PATHWAY_SOURCE_ID)
    private String sourceId;

    @Indexed (indexName=IndexNames.PATHWAY_SOURCE_TEXT)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_SOURCE_TEXT, rbField=BioFields.PATHWAY_SOURCE_TEXT)
    private String sourceText;

    @Indexed (indexName=IndexNames.NCI_PATHWAY_INTERACTION_CONDITION_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NCI_PATHWAY_INTERACTION_CONDITION_TYPE, rbField=BioFields.NCI_PATHWAY_INTERACTION_CONDITION_TYPE)
    private String conditionType;

    @Indexed (indexName=IndexNames.NCI_PATHWAY_INTERACTION_CONDITION_TEXT)
    @Taxonomy (rbClass=TaxonomyTypes.NCI_PATHWAY_INTERACTION_CONDITION_TEXT, rbField=BioFields.NCI_PATHWAY_INTERACTION_CONDITION_TEXT)
    private String conditionText;


    @FullTextIndexed (indexName=IndexNames.LOCATION)
    @Taxonomy (rbClass=TaxonomyTypes.LOCATION, rbField=BioFields.LOCATION)
    private String location;

    @FullTextIndexed (indexName=IndexNames.ACTIVITY_STATE)
    @Taxonomy (rbClass=TaxonomyTypes.ACTIVITY_STATE, rbField=BioFields.ACTIVITY_STATE)
    private String activityState;

    /*
    @FullTextIndexed (indexName=IndexNames.FUNCTION)
    @Taxonomy (rbClass=TaxonomyTypes.FUNCTION, rbField=BioFields.FUNCTION)
    private String function;
    */

    /**
     * relationships with (interactionComponents) molecules
     */
    @RelatedToVia(direction=Direction.INCOMING, relType=BioRelTypes.AN_INTERACTION, elementClass = OncoRelation.class)
    private Collection<OncoRelation> interactionComponents = new HashSet <OncoRelation>();



        /**
     * {@link FullTextIndexed} {@link IndexNames#LOCATION}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#LOCATION} {@link BioFields#LOCATION}
     * @return String proteinLocation
    */
    public String getLocation() {
        return location;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#LOCATION}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#LOCATION} {@link BioFields#LOCATION}
     * @param proteinLocation
     */
    public void setLocation(String proteinLocation) {
        this.location = proteinLocation;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#ACTIVITY_STATE}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ACTIVITY_STATE} {@link BioFields#ACTIVITY_STATE}
     * @param activityState
     */
    public void setActivityState(String activityState) {
        this.activityState = activityState;
    }


    /**
     * {@link FullTextIndexed} {@link IndexNames#ACTIVITY_STATE}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ACTIVITY_STATE} {@link BioFields#ACTIVITY_STATE}
     * @return String activityState
     */
    public String getActivityState() {
        return this.activityState;
    }


    /**
     * {@link FullTextIndexed} {@link IndexNames#FUNCTION}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FUNCTION} {@link BioFields#FUNCTION}
     * @param function
     */
    /*
    public void setFunction(String function) {
        this.function = function;
    }*/

     /**
     * {@link FullTextIndexed} {@link IndexNames#FUNCTION}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FUNCTION} {@link BioFields#FUNCTION}
     * @param function
     */
    /*
    public String getFunction() {
        return this.function;
    }
    *
    */



    /*
     * {@link BioFields#conditionType}
     */

    /**
     * {@link FullTextIndexed} {@link IndexNames#FUNCTION}
<p>
 {@link Taxonomy} {@link TaxonomyTypes#FUNCTION} {@link BioFields#FUNCTION}
     * @return String
     */

    public String getConditionType() {
        return conditionType;
    }

    /**
     * {@link BioFields#conditionType}
     * @param conditionType
     * Indicates either positive or negative condition
     */
    public void setConditionType(String conditionType) {
        this.conditionType = conditionType;
    }

    /**
     * Indicates either positive or negative condition text
     * @param conditionText
     */
    public void setConditionText(String conditionText) {
        this.conditionText = conditionText;
    }

    /**
     * @return String
     */
    public String getConditionText() {
        return conditionText;
    }

    /**
     * @return String
     */
    public String getSourceId() {
        return sourceId;
    }

    /**
     * @param sourceId
     */
    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    /**
     * @return String
     */
    public String getSourceText() {
        return sourceText;
    }

    /**
     * @param sourceText
     */
    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    /**
     * Every interaction has an identifier
     * @param interactionId
     * @param message
     */
    public NciPathwayInteraction(String interactionId, String message) {
        this.interactionId = interactionId;
        this.message = message;
    }


    /**
     *
     * This method not only sets the InteractionComponentList as a
     * collection of <code>BioRelation</code> objects, but also sets the
     * INCOMING relationship on the initialing end can be any type of molecule.
     * Molecule could be Complex, Rna, Protein
     *
     * "InteractionComponentList"  : [
     * {
     *    "@role_type: "input",
     *    "@molecule_idref" : "213740"
     * },
     * {
     *    "@role_type" : "input",
     *    "@molecule_idref" : "200579",
     *    "Label" : {
     *        "@label_type"  : "location",
     *        "@value" : "cytoplasm""
     *     }
     * }
     *
     * ]
     *
     * * {@link RelatedToVia} {@link Direction#INCOMING}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#COMPOUND} or {@link BioTypes#RNA} or {@link BioTypes#PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#NCI_PATHWAY_INTERACTION}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     * @param startNodeRole - input/agent/output
     * @param endNodeRole - interaction
     * @param startNode - molecule (protein/complex/rna)
     * @param endNode - interaction node (for each interactionComponentList)
     * @param map
     * @return OncoRelation
     *
     */
    public OncoRelation interactsWith(
                          String startNodeRole, String endNodeRole,
                          Object startNode, Object endNode, Map map) {
        OncoRelation interactor = new OncoRelation(this.interactionId,
                          startNodeRole, endNodeRole,
                          startNode, endNode, BioRelTypes.AN_INTERACTION);

        if (map != null && map.size() > 0) {
           interactor.setOtherProperties((String)map.get(BioFields.LOCATION),
                                        (String)map.get(BioFields.ACTIVITY_STATE),
                                        (String)map.get(BioFields.FUNCTION));
        }
        interactionComponents.add(interactor);
        log.info("OncoRelation() interactorComponents size = " + interactionComponents.size());
        return interactor;
    }

    public NciPathwayInteraction() {}

    /**
     * {@link BioFields#ID_REF}
     *
     * @return Long
     */
    public Long getId() {
    	return id;
    }

    /**
     * @return String
     */
    public String getInteractionType() {
        return interactionType;
    }

    /**
     * @param interactionType
     */
    public void setInteractionType(String interactionType) {
        this.interactionType = interactionType;
    }

    /**
     * {@link BioFields#ID}
     * @return String interactionId
     */
    public String getInteractionId() {
        return interactionId;
    }

    /**
     * @param interactionId
     */
    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
    }

    /**
     * {@link BioFields#NODE_TYPE}
     * @return String
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * {@link BioFields#NODE_TYPE}
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * {@link BioFields#MESSAGE}
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link BioFields#MESSAGE}
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * {@link BioFields#EVIDENCE_LIST}
     * @param cList
     */
    public void setEvidenceList(String cList) {
        this.evidenceList = cList;
    }

    /**
     * {@link BioFields#EVIDENCE_LIST}
     * @return String
     */
    public String getEvidenceList() {
        return evidenceList;
    }

    /**
     * {@link BioFields#REFERENCE_LIST}
     * pubmed list
     * @param cList
     */
    public void setReferenceList(String cList) {
        this.referenceList = cList;
    }

    /**
     *
     * {@link BioFields#REFERENCE_LIST}
     * @return String pubmed list
     */
    public String getReferenceList() {
        return referenceList;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + interactionId + "-" + interactionType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NciPathwayInteraction other = (NciPathwayInteraction) obj;
        if ((this.interactionId == null) ? (other.interactionId != null) : !this.interactionId.equals(other.interactionId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }

    /**
     * An interaction is either positive or negative
     * {@link BioFields#DEFAULT_POSITIVE_CONDITION_VAL} or {@link BioFields#DEFAULT_NEGATIVE_CONDITION_VAL}
     * @param val
     */
    public void setIsConditionPositive(String val) {
        isConditionPositive = val;
    }


    /**
     * @return String isConditionPositive true if it is positive or false if it is negative
     */
    public String getIsConditionPositive() {
        return isConditionPositive;
    }

}
