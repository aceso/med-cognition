/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.repository.TemplateUtils;
import java.util.Collection;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
@BioEntity(bioType = BioTypes.INTACT_INTERACTION)
public class IntactInteraction {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /*
     * IndexName has to match with the BioField name as this is used as the key in
     * saving index fields in Neo4J
     */
    @UniquelyIndexed (indexName=IndexNames.INTACT_INTERACTION_ID)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_INTERACTION_ID, rbField=BioFields.INTERACTION_ID)
    private String interactionId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.INTACT_INTERACTION.toString();

    /**
     * This property is required as it is not displayed without it.
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed (indexName=IndexNames.INTACT_INTERACTION_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_INTERACTION_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    @Indexed (indexName=IndexNames.INTACT_INTERACTION_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_INTERACTION_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    /**
     * The @id field in xref for an interaction. Eg. EBI-658415
     */
    @Indexed (indexName=IndexNames.INTACT_SOURCE_ID)
    @Taxonomy (rbClass=TaxonomyTypes.AFCS_SOURCE, rbField=BioFields.INTACT_SOURCE_ID)
    private String intactSourceId;

    /**
     * The @id field in secondaryRefs when @db is AFCS. Eg. 58183
     * AFCS stands for Alliance for Cell Signaling
     */
    @FullTextIndexed (indexName = IndexNames.INTACT_AFCS_SECONDARY_REFS)
    @Taxonomy (rbClass=TaxonomyTypes.AFCS_SOURCE, rbField=BioFields.SECONDARY_AFCS_SOURCE_ID)
    private String secondaryAFCSSourceIds;

    @FullTextIndexed (indexName = IndexNames.INTACT_EXPERIMENT_REF)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_REF, rbField=BioFields.EXPERIMENT_REF)
    private String experimentId;

    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> hasParticipants;

    /**
     * {@link UniquelyIndexed} {@link IndexNames#INTACT_INTERACTION_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_INTERACTION_ID} {@link BioFields#INTERACTION_ID}
     *
     * @return String
     */
    public String getInteractionId() {
        return interactionId;
    }

    /**
     * {@link UniquelyIndexed} {@link IndexNames#INTACT_INTERACTION_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_INTERACTION_ID} {@link BioFields#INTERACTION_ID}
     *
     * @param interactionId
     */
    public void setInteractionId(String interactionId) {
        this.interactionId = interactionId;
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
     * The @id field in xref for an interaction. Eg. EBI-658415
     * <p>
     * {@link Indexed} {@link IndexNames#INTACT_SOURCE_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#AFCS_SOURCE} {@link BioFields#INTACT_SOURCE_ID}
     *
     * @return String
     */
    public String getIntactSourceId() {
        return intactSourceId;
    }

    /**
     * The @id field in xref for an interaction. Eg. EBI-658415
     * <p>
     * {@link Indexed} {@link IndexNames#INTACT_SOURCE_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#AFCS_SOURCE} {@link BioFields#INTACT_SOURCE_ID}
     *
     * @param intactSourceId
     */
    public void setIntactSourceId(String intactSourceId) {
        this.intactSourceId = intactSourceId;
    }

    /**
     * The @id field in secondaryRefs when @db is AFCS. Eg. 58183
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#INTACT_AFCS_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#AFCS_SOURCE} {@link BioFields#SECONDARY_AFCS_SOURCE_ID}
     *
     * @return String
     */
    public String getSecondaryAFCSSourceIds() {
        return secondaryAFCSSourceIds;
    }

    /**
     * The @id field in secondaryRefs when @db is AFCS. Eg. 58183
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#INTACT_AFCS_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#AFCS_SOURCE} {@link BioFields#SECONDARY_AFCS_SOURCE_ID}
     *
     * @param secondaryAFCSSourceIds
     */
    public void setSecondaryAFCSSourceIds(String secondaryAFCSSourceIds) {
        this.secondaryAFCSSourceIds = secondaryAFCSSourceIds;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#INTACT_EXPERIMENT_REF}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_REF} {@link BioFields#EXPERIMENT_REF}
     *
     * @return String
     */
    public String getExperimentId() {
        return experimentId;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#INTACT_EXPERIMENT_REF}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_REF} {@link BioFields#EXPERIMENT_REF}
     *
     * @param experimentId
     */
    public void setExperimentId(String experimentId) {
        this.experimentId = experimentId;
    }

    /**
     * {@link RelatedToVia} {@link Direction#OUTGOING}
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
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getHasParticipants() {
        return hasParticipants;
    }

    /**
     * This method not only sets the participantList relations as a
     * collection of <code>BioRelation</code> objects, but also sets the
     * INCOMING relationship on the receiving end in each participant.
     *
     * If any existing INCOMING relationship exists in the Participant,
     * this method overwrites such a reference in the Participant.
     * The isValid method in BioRelation checks for a valid startNode
     * and endNode. This includes checking for null, as well as if the
     * classes match the BioEntity classes.
     *
     * {@link RelatedToVia} {@link Direction#OUTGOING}
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
     * @param hasParticipants
     */
    public void setHasParticipants(Collection<BioRelation> hasParticipants) {
        this.hasParticipants = hasParticipants;
        for (BioRelation bioRelation : hasParticipants) {
            Object startNode = bioRelation.getStartNode();
            if (!bioRelation.isValid(BioEntityClasses.INTACT_INTERACTION, BioEntityClasses.PARTICIPANT)) {
               throw new RuntimeException("The BioRelation is invalid. Check the startNode and endNode");
            }
            Participant endNode = (Participant)bioRelation.getEndNode();
            BioRelation hasParticipant = new BioRelation(this, endNode, BioRelTypes.HAS_PARTICIPANT);
            endNode.setHasParticipant(hasParticipant);
        }
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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
     * {@link Indexed} {@link IndexNames#INTACT_INTERACTION_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_INTERACTION_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     *  @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTACT_INTERACTION_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_INTERACTION_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTACT_INTERACTION_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_INTERACTION_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTACT_INTERACTION_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#INTACT_INTERACTION_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public IntactInteraction() {
    }

    @Override
    public String toString() {
        return nodeType + "-" + interactionId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || (!getClass().getName().equals(o.getClass().getName()))) {
            return false;
        }

        IntactInteraction bioRelation = (IntactInteraction) o;
        if (id == null) {
            return super.equals(o);
        }
        return id.equals(bioRelation.id);

    }

    @Override
    public int hashCode() {
        return id != null ? id.hashCode() : super.hashCode();
    }
}