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
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.atgc.bio.repository.TemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * A experimental role of a given participantId.
 *
 * @author jtanisha-ee
 * @see BioEntity
 */
@BioEntity(bioType = BioTypes.EXPERIMENTAL_ROLE)
public class ExperimentalRole {
    protected static Logger log = LogManager.getLogger(ExperimentalRole.class);

    @GraphId
    private Long id;

    /**
     * The experimental role does not have a unique id of it's own. So we
     * associate that with a unique Id such as participantId.
     */
    @UniquelyIndexed (indexName=IndexNames.EXPERIMENT_ROLE_PARTICIPANT_ID)
    @Taxonomy(rbClass=TaxonomyTypes.PARTICIPANT_ID, rbField=BioFields.PARTICIPANT_ID)
    private String participantId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.EXPERIMENTAL_ROLE.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * Eg: "prey"
     */
    @Visual
    @Indexed (indexName=IndexNames.EXPERIMENTAL_ROLE_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENTAL_ROLE_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    /**
     * Eg. "prey"
     */
    @Indexed (indexName=IndexNames.EXPERIMENTAL_ROLE_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENTAL_ROLE_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    /**
     * MI:0498
     */
    @Indexed (indexName=IndexNames.EXPERIMENTAL_ROLE_PSI_MI_ID)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENTAL_ROLE_PSI_MI_ID, rbField=BioFields.EXPERIMENTAL_ROLE_PSI_MI_ID)
    private String experimentalRolePsiMiId;

    /**
     * Eg. EBI-58
     */
    @Indexed (indexName=IndexNames.EXPERIMENTAL_ROLE_INTACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENTAL_ROLE_INTACT_ID, rbField=BioFields.EXPERIMENTAL_ROLE_INTACT_ID)
    private String experimentalRoleIntactId;

    /**
     * Eg. 14755292
     */
    @Indexed (indexName=IndexNames.EXPERIMENTAL_ROLE_PUBMED_ID)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENTAL_ROLE_PUBMED_ID, rbField=BioFields.EXPERIMENTAL_ROLE_PUBMED_ID)
    private String experimentalRolePubMedId;

    /**
     * Participant has experimental roles.
     */
    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_EXPERIMENTAL_ROLE, startNodeBioType=BioTypes.PARTICIPANT, endNodeBioType=BioTypes.EXPERIMENTAL_ROLE, elementClass = BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_EXPERIMENTAL_ROLE, elementClass = BioRelation.class)
    private BioRelation hasExperimentalRole;

    /**
     *
     */
    public ExperimentalRole() {}

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
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
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
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_PSI_MI_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_PSI_MI_ID} {@link BioFields#EXPERIMENTAL_ROLE_PSI_MI_ID}
     *
     * @return String
     */
    public String getExperimentalRolePsiMiId() {
        return experimentalRolePsiMiId;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_PSI_MI_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_PSI_MI_ID} {@link BioFields#EXPERIMENTAL_ROLE_PSI_MI_ID}
     *
     * @param experimentalRolePsiMiId
     */
    public void setExperimentalRolePsiMiId(String experimentalRolePsiMiId) {
        this.experimentalRolePsiMiId = experimentalRolePsiMiId;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_INTACT_ID} {@link BioFields#EXPERIMENTAL_ROLE_INTACT_ID}
     *
     * @return String
     */
    public String getExperimentalRoleIntactId() {
        return experimentalRoleIntactId;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_INTACT_ID} {@link BioFields#EXPERIMENTAL_ROLE_INTACT_ID}
     *
     * @param experimentalRoleIntactId
     */
    public void setExperimentalRoleIntactId(String experimentalRoleIntactId) {
        this.experimentalRoleIntactId = experimentalRoleIntactId;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_PUBMED_ID} {@link BioFields#EXPERIMENTAL_ROLE_PUBMED_ID}
     *
     * @return String
     */
    public String getExperimentalRolePubMedId() {
        return experimentalRolePubMedId;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENTAL_ROLE_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENTAL_ROLE_PUBMED_ID} {@link BioFields#EXPERIMENTAL_ROLE_PUBMED_ID}
     *
     * @param experimentalRolePubMedId
     */
    public void setExperimentalRolePubMedId(String experimentalRolePubMedId) {
        this.experimentalRolePubMedId = experimentalRolePubMedId;
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
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_EXPERIMENTAL_ROLE}
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
    public void setHasExperimentalRole(BioRelation hasExperimentalRole) {
        this.hasExperimentalRole = hasExperimentalRole;
    }

    /**
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_EXPERIMENTAL_ROLE}
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
     * @return BioRelation
     */
    public BioRelation getHasExperimentalRole() {
        return hasExperimentalRole;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + participantId + "-" + shortLabel);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }

        final ExperimentalRole other = (ExperimentalRole) obj;

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