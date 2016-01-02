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
import org.atgc.bio.repository.TemplateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Direction;

/**
 * A biological role of a given participantId.
 *
 * @author jtanisha-ee
 * @see BioTypes
 * @see BioEntity
 * @see Taxonomy
 * @see UniquelyIndexed
 * @see Indexed
 * @see BioFields
 */
@BioEntity(bioType = BioTypes.FEATURE_RANGE)
public class FeatureRange {
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /**
     * The featureRange does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     */
    @UniquelyIndexed (indexName=IndexNames.FEATURE_RANGE_FEATURE_ID)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_ID, rbField=BioFields.FEATURE_ID)
    private String featureId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.FEATURE_RANGE.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * For now we do not index this, and also don't care for taxonomy.
     * Eg. 564
     */
    @NonIndexed
    private String beginPosition;

    /**
     * For now we do not index this, and also don't care for taxonomy.
     * Eg. 804
     */
    @NonIndexed
    private String endPosition;

    /**
     * A feature has a list of featureRanges.
     */
    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_FEATURE_RANGE, startNodeBioType=BioTypes.FEATURE, endNodeBioType=BioTypes.FEATURE_RANGE, elementClass=BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_FEATURE_RANGE, elementClass=BioRelation.class)
    private BioRelation featureRelation;

    //@RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_START_STATUS, startNodeBioType=BioTypes.FEATURE_RANGE, endNodeBioType=BioTypes.START_STATUS, elementClass=BioRelation.class)
    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_START_STATUS, elementClass=BioRelation.class)
    private BioRelation hasStartStatus;

   // @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_END_STATUS, startNodeBioType=BioTypes.FEATURE_RANGE, endNodeBioType=BioTypes.END_STATUS, elementClass=BioRelation.class)
    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_END_STATUS, elementClass=BioRelation.class)
    private BioRelation hasEndStatus;

    /**
     * Eg. false
     */
    @NonIndexed
    public String isLink;

    /**
     *
     */
    public FeatureRange() {}

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
     * The {@link org.atgc.bio.FeatureRange} does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     *
     * {@link UniquelyIndexed} {@link IndexNames#FEATURE_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_ID} {@link BioFields#FEATURE_ID}
     *
     * @return BioRelation
     */
    public String getFeatureId() {
        return featureId;
    }

    /**
     * The {@link org.atgc.bio.FeatureRange} does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     *
     * {@link UniquelyIndexed} {@link IndexNames#FEATURE_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_ID} {@link BioFields#FEATURE_ID}
     *
     * @param featureId
     */
    public void setFeatureId(String featureId) {
        this.featureId = featureId;
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
     * For now we do not index this, and also don't care for taxonomy.
     * Eg. 564
     *
     * {@link NonIndexed}
     *
     * @return String
     */
    public String getBeginPosition() {
        return beginPosition;
    }

    /**
     * For now we do not index this, and also don't care for taxonomy.
     * Eg. 564
     *
     * {@link NonIndexed}
     *
     * @param beginPosition
     */
    public void setBeginPosition(String beginPosition) {
        this.beginPosition = beginPosition;
    }

    /**
     * For now we do not index this, and also don't care for taxonomy.
     * Eg. 804
     *
     * {@link NonIndexed}
     *
     * @return String
     */
    public String getEndPosition() {
        return endPosition;
    }

    /**
     * For now we do not index this, and also don't care for taxonomy.
     * Eg. 804
     *
     * {@link NonIndexed}
     *
     * @param endPosition
     */
    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    /**
     * {@link NonIndexed}
     *
     * @return String
     */
    public String isLink() {
        return isLink;
    }

    /**
     * {@link NonIndexed}
     *
     * @param isLink
     */
    public void setIsLink(String isLink) {
        this.isLink = isLink;
    }

    /**
     * Set the feature relation that allows a Feature to be connected
     * to this FeatureRange using the HAS_FEATURE_RANGE relation.
     *
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_FEATURE_RANGE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param featureRelation
     */
    public void setFeatureRelation(BioRelation featureRelation) {
        this.featureRelation = featureRelation;
    }

    /**
     * This is called either when a Collection<BioRelation> is updated or
     * just a BioRelation is updated. It updates the endNode's relation
     * property.
     *
     * @param bioRelation
     */
    private void updateEndNodeStartStatus(BioRelation bioRelation) {
        Object startNode = bioRelation.getStartNode();
        if (!bioRelation.isValid(BioEntityClasses.FEATURE_RANGE, BioEntityClasses.START_STATUS)) {
           throw new RuntimeException("The BioRelation is invalid. Check the startNode and endNode");
        }
        StartStatus endNode = (StartStatus)bioRelation.getEndNode();
        BioRelation relation = new BioRelation(startNode, endNode, BioRelTypes.HAS_START_STATUS);
        endNode.setHasStartStatus(relation);
    }

    /**
     * This is called either when a Collection<BioRelation> is updated or
     * just a BioRelation is updated. It updates the endNode's relation
     * property.
     *
     * @param bioRelation
     */
    private void updateEndNodeEndStatus(BioRelation bioRelation) {
        Object startNode = bioRelation.getStartNode();
        if (!bioRelation.isValid(BioEntityClasses.FEATURE_RANGE, BioEntityClasses.END_STATUS)) {
           throw new RuntimeException("The BioRelation is invalid. Check the startNode and endNode");
        }
        EndStatus endNode = (EndStatus)bioRelation.getEndNode();
        BioRelation relation = new BioRelation(startNode, endNode, BioRelTypes.HAS_END_STATUS);
        endNode.setHasEndStatus(relation);
    }

    /**
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_START_STATUS}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#START_STATUS}
     * </LI>
     * elementClass {@link BioRelation}
     *
     * @param hasStartStatus
     */
    public void setHasStartStatus(BioRelation hasStartStatus) {
        this.hasStartStatus = hasStartStatus;
        updateEndNodeStartStatus(hasStartStatus);
    }

    /**
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_END_STATUS}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#END_STATUS}
     * </LI>
     * </DL>
     * <p>
     *
     * elementClass {@link BioRelation}
     *
     * @param hasEndStatus
     */
    public void setHasEndStatus(BioRelation hasEndStatus) {
        this.hasEndStatus = hasEndStatus;
        updateEndNodeEndStatus(hasEndStatus);
    }

    /**
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_END_STATUS}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#END_STATUS}
     * </LI>
     * </DL>
     * <p>
     * elementClass {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getHasEndStatus() {
        return hasEndStatus;
    }

    /**
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_START_STATUS}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#START_STATUS}
     * </LI>
     * elementClass {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getHasStartStatus() {
        return hasStartStatus;
    }

    /**
     * Get the feature relation that allows a Feature to be connected
     * to this FeatureRange using the HAS_FEATURE_RANGE relation.
     *
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_FEATURE_RANGE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getFeatureRelation() {
        return featureRelation;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + featureId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }
        final FeatureRange other = (FeatureRange) obj;

        if ((this.featureId != null) && (other.getFeatureId() != null)) {
            if (!this.featureId.equals(other.getFeatureId())) {
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