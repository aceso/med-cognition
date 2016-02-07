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
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 *
 * @author jtanisha-ee
 * @see UniquelyIndexed
 * @see BioEntity
 * @see Indexed
 * @see Taxonomy
 * @see BioFields
 * @see FullTextIndexed
 * @see NonIndexed
 * @see RelatedToVia
 */
@BioEntity(bioType = BioTypes.INTACT_EXPERIMENT)
public class IntactExperiment {

    protected static Logger log = LogManager.getLogger(IntactExperiment.class);

    @GraphId
    private Long id;

    @UniquelyIndexed (indexName=IndexNames.INTACT_EXPERIMENT_REF)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_EXPERIMENT_REF, rbField=BioFields.EXPERIMENT_REF)
    private String experimentRef;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.INTACT_EXPERIMENT.toString();

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
     * This property is required as it is not displayed without it.
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed (indexName=IndexNames.INTACT_EXPERIMENT_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    @Indexed (indexName=IndexNames.INTACT_EXPERIMENT_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.REFERENCES_PUBMED , elementClass = BioRelation.class)
    private BioRelation referencesPubMed;

    public BioRelation getReferencesPubMed() {
        return referencesPubMed;
    }

    public void setReferencesPubMed(PubMed pubMed) {
        BioRelation refPubMed = new BioRelation(this, pubMed, BioRelTypes.REFERENCES_PUBMED);
        this.referencesPubMed = refPubMed;
    }

    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> hasHostOrganisms;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_INTERACTION_DETECTION_METHOD , elementClass = BioRelation.class)
    private BioRelation hasInteractionDetectionMethod;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_PARTICIPANT_IDENTIFICATION_METHOD , elementClass = BioRelation.class)
    private BioRelation hasParticipantIdentificationMethod;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_ATTRIBUTES , elementClass = BioRelation.class)
    private BioRelation hasAttributes;

    /**
     * An experiment can have one or more interactions. An interaction
     * is associated with a unique experiment.
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> hasIntactInteractions;

    public Collection<BioRelation> getHasIntactInteractions() {
        return hasIntactInteractions;
    }

    public void setIntactInteractions(Collection<IntactInteraction> intactInteractions) {
        HashSet<BioRelation> bioRelations = new HashSet<BioRelation>();
        for (IntactInteraction intactInteraction : intactInteractions) {
            BioRelation bioRelation = new BioRelation(this, intactInteraction, BioRelTypes.HAS_INTACT_INTERACTION);
            bioRelations.add(bioRelation);
        }
        this.hasIntactInteractions = bioRelations;
    }

    public void addIntactInteraction(IntactInteraction intactInteraction) {
        if (hasIntactInteractions == null) {
            hasIntactInteractions = new HashSet<BioRelation>();
        }
        BioRelation bioRelation = new BioRelation(this, intactInteraction, BioRelTypes.HAS_INTACT_INTERACTION);
        hasIntactInteractions.add(bioRelation);
    }

    public BioRelation getHasAttributes() {
        return hasAttributes;
    }

    public void setHasAttributes(ExperimentAttributes experimentAttributes) {
        BioRelation bioRelation = new BioRelation(this, experimentAttributes, BioRelTypes.HAS_ATTRIBUTES);
        this.hasAttributes = bioRelation;
    }

    public BioRelation getHasParticipantIdentificationMethod() {
        return hasParticipantIdentificationMethod;
    }

    public void setHasParticipantIdentificationMethod(ParticipantIdentificationMethod participantIdentificationMethod) {
        BioRelation bioRelation = new BioRelation(this, participantIdentificationMethod, BioRelTypes.HAS_PARTICIPANT_IDENTIFICATION_METHOD);
        this.hasParticipantIdentificationMethod = bioRelation;
    }

    public BioRelation getHasInteractionDetectionMethod() {
        return hasInteractionDetectionMethod;
    }

    public void setHasInteractionDetectionMethod(InteractionDetectionMethod interactionDetectionMethod) {
        BioRelation bioRelation = new BioRelation(this, interactionDetectionMethod, BioRelTypes.HAS_INTERACTION_DETECTION_METHOD);
        this.hasInteractionDetectionMethod = bioRelation;
    }

    public Collection<BioRelation> hasHostOrganisms() {
        return hasHostOrganisms;
    }

    public void setHasHostOrganisms(Collection<Organism> hostOrganismList) {
        HashSet<BioRelation> hasHostOrganismList = new HashSet<BioRelation>();
        for (Organism organism : hostOrganismList) {
            BioRelation hasHostOrganism = new BioRelation(this, organism, BioRelTypes.HAS_HOST_ORGANISM);
            hasHostOrganismList.add(hasHostOrganism);
        }
        this.hasHostOrganisms = hasHostOrganismList;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getExperimentRef() {
        return experimentRef;
    }

    public void setExperimentRef(String experimentId) {
        this.experimentRef = experimentId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getShortLabel() {
        return shortLabel;
    }

    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
}
