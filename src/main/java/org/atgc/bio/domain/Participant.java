/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.NonIndexed;
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.RelatedToVia;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.atgc.bio.repository.TemplateUtils;
import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.PARTICIPANT)
public class Participant {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(Participant.class);

    @GraphId
    private Long id;

    @UniquelyIndexed (indexName=IndexNames.PARTICIPANT_ID)
    @Taxonomy(rbClass=TaxonomyTypes.PARTICIPANT_ID, rbField=BioFields.PARTICIPANT_ID)
    private String participantId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    //private String nodeType = BioTypes.PARTICIPANT.toString();
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed (indexName=IndexNames.PARTICIPANT_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.PARTICIPANT_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    @Indexed (indexName=IndexNames.INTACT_PARTICIPANT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_PARTICIPANT_ID, rbField=BioFields.INTACT_PARTICIPANT_ID)
    private String intactParticipantId;

    /**
     * When not known, this will be set to the uniprot.
     */
    @Indexed (indexName=IndexNames.INTERACTOR_ID)
    @Taxonomy (rbClass=TaxonomyTypes.INTERACTOR_ID, rbField=BioFields.INTERACTOR_REF)
    private String interactorRef;

    /**
     * A participant has a list of features through hasFeature, called featureList. It's going
     * as Participant has features but feature does not have participants.
     */
    //@RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_FEATURE, startNodeBioType=BioTypes.PARTICIPANT, endNodeBioType=BioTypes.FEATURE, elementClass = BioRelation.class)
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_FEATURE, elementClass = BioRelation.class)
    private Collection<BioRelation> hasFeatures;

    /**
     * A participant has interactor, and not the other way around. Hence the
     * direction of the relationship. Besides, one participantId is associated
     * with a unique interactorRef. That is why this is not a collection.
     */
    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_INTERACTOR, elementClass = BioRelation.class)
    private BioRelation hasInteractor;

    /**
     * Participant has a biological role.
     */
    //@RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_BIOLOGICAL_ROLE, startNodeBioType=BioTypes.PARTICIPANT, endNodeBioType=BioTypes.BIOLOGICAL_ROLE, elementClass = BioRelation.class)
    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_BIOLOGICAL_ROLE, elementClass = BioRelation.class)
    private BioRelation hasBiologicalRole;

    /**
     * Participant has experimental roles.
     */
    //@RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_EXPERIMENTAL_ROLE, startNodeBioType=BioTypes.PARTICIPANT, endNodeBioType=BioTypes.EXPERIMENTAL_ROLE, elementClass = BioRelation.class)
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_EXPERIMENTAL_ROLE, elementClass = BioRelation.class)
    private Collection<BioRelation> hasExperimentalRoles;

    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_PARTICIPANT, startNodeBioType=BioTypes.INTACT_INTERACTION, endNodeBioType=BioTypes.PARTICIPANT, elementClass = BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_PARTICIPANT, elementClass = BioRelation.class)
    private BioRelation hasParticipant;

    /**
     *
     */
    public Participant() {}

    /**
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     *
     * @return String
     */
    public String getInteractorRef() {
        return interactorRef;
    }

    /**
     *
     * @param interactorRef
     */
    public void setInteractorRef(String interactorRef) {
        this.interactorRef = interactorRef;
    }

    /**
     * {@link UniquelyIndexed} {@link IndexNames#PARTICIPANT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PARTICIPANT_ID} {@link BioFields#PARTICIPANT_ID}
     *
     * @return String
     */
    public String getParticipantId() {
        return participantId;
    }

    /**
     * {@link UniquelyIndexed} {@link IndexNames#PARTICIPANT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PARTICIPANT_ID} {@link BioFields#PARTICIPANT_ID}
     *
     * @param participantId
     */
    public void setParticipantId(String participantId) {
        this.participantId = participantId;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTACT_PARTICIPANT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_PARTICIPANT_ID} {@link BioFields#INTACT_PARTICIPANT_ID}
     *
     * @return String
     */
    public String getIntactParticipantId() {
        return intactParticipantId;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTACT_PARTICIPANT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_PARTICIPANT_ID} {@link BioFields#INTACT_PARTICIPANT_ID}
     *
     * @param intactParticipantId
     */
    public void setIntactParticipantId(String intactParticipantId) {
        this.intactParticipantId = intactParticipantId;
    }

    /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
     * @return String
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
     * @param nodeType
     */
    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType.toString();
    }

    /**
     * {@link NonIndexed}
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link NonIndexed}
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * A participant has a list of features through hasFeature, called featureList. It's going
     * as Participant has features but feature does not have participants.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}

     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getHasFeature() {
        return hasFeatures;
    }

    /**
     * A participant has a list of features through hasFeature, called featureList. It's going
     * as Participant has features but feature does not have participants.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param hasFeatures
     */
    public void setHasFeature(Collection<BioRelation> hasFeatures) {
        this.hasFeatures = hasFeatures;
    }

    /**
     * A participant has a list of features through hasFeature, called featureList. It's going
     * as Participant has features but feature does not have participants.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param hasFeature
     */
    public void addHasFeature(BioRelation hasFeature) {
        if (hasFeatures == null) {
            hasFeatures = new HashSet<BioRelation>();
        }
        hasFeatures.add(hasFeature);
    }

    /**
     * A participant has interactor, and not the other way around. Hence the
     * direction of the relationship. Besides, one participantId is associated
     * with a unique interactorRef. That is why this is not a collection.
     * <p>
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_INTERACTOR}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     * elementClass = {@link BioRelation}
     *
     * @param hasInteractor
     */
    public void setHasInteractor(BioRelation hasInteractor) {
        this.hasInteractor = hasInteractor;
    }

    /**
     * A participant has interactor, and not the other way around. Hence the
     * direction of the relationship. Besides, one participantId is associated
     * with a unique interactorRef. That is why this is not a collection.
     * <p>
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_INTERACTOR}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     * elementClass = {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getHasInteractor() {
        return hasInteractor;
    }

    /**
     * Participant has a biological role.
     *
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_BIOLOGICAL_ROLE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#BIOLOGICAL_ROLE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param hasBiologicalRole
     */
    public void setHasBiologicalRole(BioRelation hasBiologicalRole) {
        this.hasBiologicalRole = hasBiologicalRole;
        Object startNode = hasBiologicalRole.getStartNode();
        if (!hasBiologicalRole.isValid(BioEntityClasses.PARTICIPANT, BioEntityClasses.BIOLOGICAL_ROLE)) {
           throw new RuntimeException("The BioRelation is invalid. Check the startNode and endNode");
        }
        BiologicalRole endNode = (BiologicalRole)hasBiologicalRole.getEndNode();
        BioRelation relation = new BioRelation(startNode, endNode, BioRelTypes.HAS_BIOLOGICAL_ROLE);
        endNode.setHasBiologicalRole(relation);
    }

    /**
     * Participant has a biological role.
     *
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_BIOLOGICAL_ROLE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#BIOLOGICAL_ROLE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getHasBiologicalRole() {
        return hasBiologicalRole;
    }

    /**
     * This is called either when a Collection<BioRelation> is updated or
     * just a BioRelation is updated. It updates the endNode's relation
     * property.
     *
     * @param bioRelation
     */
    private void updateEndNodeExperimentalRole(BioRelation bioRelation) {
        Object startNode = bioRelation.getStartNode();
        if (!bioRelation.isValid(BioEntityClasses.PARTICIPANT, BioEntityClasses.EXPERIMENTAL_ROLE)) {
           throw new RuntimeException("The BioRelation is invalid. Check the startNode and endNode");
        }
        ExperimentalRole endNode = (ExperimentalRole)bioRelation.getEndNode();
        BioRelation relation = new BioRelation(startNode, endNode, BioRelTypes.HAS_EXPERIMENTAL_ROLE);
        endNode.setHasExperimentalRole(relation);
    }

    /**
     * Participant has experimental roles.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_EXPERIMENTAL_ROLE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#EXPERIMENTAL_ROLE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param hasExperimentalRole
     */
    public void addHasExperimentalRole(BioRelation hasExperimentalRole) {
        if (hasExperimentalRoles == null) {
            hasExperimentalRoles = new HashSet<BioRelation>();
        }
        hasExperimentalRoles.add(hasExperimentalRole);
        updateEndNodeExperimentalRole(hasExperimentalRole);
    }

    /**
     * {@link Participant} has {@link ExperimentalRole}s.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_EXPERIMENTAL_ROLE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#EXPERIMENTAL_ROLE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param hasExperimentalRoles
     */
    public void setHasExperimentalRoles(Collection<BioRelation> hasExperimentalRoles) {
        this.hasExperimentalRoles = hasExperimentalRoles;
        for (BioRelation bioRelation : hasExperimentalRoles) {
            updateEndNodeExperimentalRole(bioRelation);
        }
    }

    /**
     * Participant has {@link ExperimentalRole}
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_EXPERIMENTAL_ROLE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#EXPERIMENTAL_ROLE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getHasExperimentalRoles() {
        return hasExperimentalRoles;
    }

    /**
     * A participant has a list of features through hasFeature, called featureList. It's going
     * as Participant has features but feature does not have participants.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getHasFeatures() {
        return hasFeatures;
    }

    /**
     * A participant has a list of features through hasFeature, called featureList. It's going
     * as Participant has features but feature does not have participants.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param hasFeatures
     */
    public void setHasFeatures(Collection<BioRelation> hasFeatures) {
        this.hasFeatures = hasFeatures;
    }

    /**
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_PARTICIPANT}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#INTACT_INTERACTION}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getHasParticipant() {
        return hasParticipant;
    }

    /**
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_PARTICIPANT}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#INTACT_INTERACTION}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param hasParticipant
     */
    public void setHasParticipant(BioRelation hasParticipant) {
        this.hasParticipant = hasParticipant;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        sb.append(nodeType).append("-");
        sb.append(participantId).append("-");
        if ( (shortLabel != null) && (shortLabel.length() > 0) && (!shortLabel.equals("N/A"))) {
            sb.append(shortLabel);
        }
        return sb.toString();
    }

    /**
     * We only compare participantId and nodeType.
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (!getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }

        final Participant other = (Participant) obj;
        if ((this.participantId != null) && (other.getParticipantId() != null)) {
            if (!this.participantId.equals(other.getParticipantId())) {
                return false;
            }
        } else {
            return false;
        }

        if ((this.nodeType != null) && (other.getNodeType() != null)) {
            if (!this.nodeType.equals(other.getNodeType())) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }
}