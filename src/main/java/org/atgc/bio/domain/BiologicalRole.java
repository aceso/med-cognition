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
 * A biological role of a given participantId.
 *
 * @author jtanisha-ee
 * @see BioEntity
 * @see BioTypes
 * @see TaxonomyTypes
 * @see BioFields
 * @see BioTypes
 */
@BioEntity(bioType = BioTypes.BIOLOGICAL_ROLE)
public class BiologicalRole {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(BiologicalRole.class);

    @GraphId
    private Long id;

    /**
     * The biological role does not have a unique id of it's own. So we
     * associate that with a uniqueId such as participantId. Eg. 212407
     */
    @UniquelyIndexed (indexName=IndexNames.BIOLOGICAL_ROLE_PARTICIPANT_ID)
    @Taxonomy(rbClass=TaxonomyTypes.PARTICIPANT_ID, rbField=BioFields.PARTICIPANT_ID)
    private String participantId;

    /**
     * The nodeType is the BioType as declared in BioTypes.
     */
    @Indexed(indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.BIOLOGICAL_ROLE.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * Eg. "unspecified role"
     */
    @Visual
    @Indexed (indexName=IndexNames.BIOLOGICAL_ROLE_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.BIOLOGICAL_ROLE_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    /**
     * Eg. "unspecified role"
     */
    @Indexed (indexName=IndexNames.BIOLOGICAL_ROLE_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.BIOLOGICAL_ROLE_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    /**
     * Eg. MI-0499
     */
    @Indexed (indexName=IndexNames.BIOLOGICAL_ROLE_PSI_MI_ID)
    @Taxonomy (rbClass=TaxonomyTypes.BIOLOGICAL_ROLE_PSI_MI_ID, rbField=BioFields.BIOLOGICAL_ROLE_PSI_MI_ID)
    private String biologicalRolePsiMiId;

    /**
     * Eg. EBI-77781
     */
    @Indexed (indexName=IndexNames.BIOLOGICAL_ROLE_INTACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.BIOLOGICAL_ROLE_INTACT_ID, rbField=BioFields.BIOLOGICAL_ROLE_INTACT_ID)
    private String biologicalRoleIntactId;

    /**
     * Eg. 14755292
     */
    @Indexed (indexName=IndexNames.BIOLOGICAL_ROLE_PUBMED_ID)
    @Taxonomy (rbClass=TaxonomyTypes.BIOLOGICAL_ROLE_PUBMED_ID, rbField=BioFields.BIOLOGICAL_ROLE_PUBMED_ID)
    private String biologicalRolePubMedId;

    /**
     * Participant has a biological role.
     */
    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_BIOLOGICAL_ROLE, startNodeBioType=BioTypes.PARTICIPANT, endNodeBioType=BioTypes.BIOLOGICAL_ROLE, elementClass = BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_BIOLOGICAL_ROLE, elementClass = BioRelation.class)
    private BioRelation hasBiologicalRole;

    /**
     *
     */
    public BiologicalRole() {}

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
     * Eg. "unspecified role"
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * Eg. "unspecified role"
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * Eg. "unspecified role"
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Eg. "unspecified role"
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_FULL_NAME} {@link BioFields#FULL_NAME}
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * The biological role does not have a unique id of it's own. So we
     * associate that with a uniqueId such as participantId. Eg. 212407
     * <p>
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
     * The biological role does not have a unique id of it's own. So we
     * associate that with a uniqueId such as participantId. Eg. 212407
     * <p>
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
     * Eg. MI-0499
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_PSI_MI_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_PSI_MI_ID} {@link BioFields#BIOLOGICAL_ROLE_PSI_MI_ID}
     *
     * @return String
     */
    public String getBiologicalPsiMiRoleId() {
        return biologicalRolePsiMiId;
    }

    /**
     * Eg. MI-0499
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_PSI_MI_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_PSI_MI_ID} {@link BioFields#BIOLOGICAL_ROLE_PSI_MI_ID}
     *
     * @param biologicalRolePsiMiId
     */
    public void setBiologicalPsiMiRoleId(String biologicalRolePsiMiId) {
        this.biologicalRolePsiMiId = biologicalRolePsiMiId;
    }

    /**
     * Eg. EBI-77781
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_INTACT_ID} {@link BioFields#BIOLOGICAL_ROLE_INTACT_ID}

     * @return String
     */
    public String getBiologicalRoleIntactId() {
        return biologicalRoleIntactId;
    }

    /**
     * Eg. EBI-77781
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_INTACT_ID} {@link BioFields#BIOLOGICAL_ROLE_INTACT_ID}
     *
     * @param biologicalRoleIntactId
     */
    public void setBiologicalIntactRoleId(String biologicalRoleIntactId) {
        this.biologicalRoleIntactId = biologicalRoleIntactId;
    }

    /**
     * Eg. 14755292
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_PUBMED_ID} {@link BioFields#BIOLOGICAL_ROLE_PUBMED_ID}
     *
     * @return String
     */
    public String getBiologicalRolePubMedId() {
        return biologicalRolePubMedId;
    }

    /**
     * Eg. 14755292
     * <p>
     * {@link Indexed} {@link IndexNames#BIOLOGICAL_ROLE_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#BIOLOGICAL_ROLE_PUBMED_ID} {@link BioFields#BIOLOGICAL_ROLE_PUBMED_ID}
     *
     * @param biologicalRolePubMedId
     */
    public void setBiologicalPubMedRoleId(String biologicalRolePubMedId) {
        this.biologicalRolePubMedId = biologicalRolePubMedId;
    }

    /**
     * This is the bio type as defined in BioTypes, one for each @BioEntity.
     *
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
     * @return String
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * This is the bio type as defined in BioTypes, one for each {@link BioEntity}.
     *
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
     * @param nodeType
     */
    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType.toString();
    }

    /**
     * Every node seems to need a message property which looks like a hack.
     * Just use the message to mean something user friendly that tells us
     * about the node. Avoid using ids, unless there is no choice.
     *
     * {@link NonIndexed}
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * Every node seems to need a message property which looks like a hack.
     * Just use the message to mean something user friendly that tells us
     * about the node. Avoid using ids, unless there is no choice.
     *
     * {@link NonIndexed}
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * Each Participant has a BiologicalRole.
     *
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_BIOLOGICAL_ROLE}
     * <UL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#BIOLOGICAL_ROLE}
     * </LI>
     * </UL>
     * {@link BioRelation}
     *
     * @param hasBiologicalRole
     */
    public void setHasBiologicalRole(BioRelation hasBiologicalRole) {
        this.hasBiologicalRole = hasBiologicalRole;
    }

    /**
     * Each Participant has a BiologicalRole.
     *
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_BIOLOGICAL_ROLE}
     * <UL>
     * <LI>
     * startNodeBioType {@link BioTypes#PARTICIPANT}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#BIOLOGICAL_ROLE}
     * </LI>
     * </UL>
     * {@link BioRelation}
     *
     * @return String
     */
    public BioRelation getHasBiologicalRule() {
        return hasBiologicalRole;
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

        final BiologicalRole other = (BiologicalRole) obj;

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