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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Direction;

/**
 * A biological role of a given participantId.
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.FEATURE)
public class Feature {
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /**
     * Eg. 212408
     */
    @UniquelyIndexed (indexName=IndexNames.FEATURE_ID)
    @Taxonomy(rbClass=TaxonomyTypes.FEATURE_ID, rbField=BioFields.FEATURE_ID)
    private String featureId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.FEATURE.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * We only found a shortLabel for feature, and not a fullName yet.
     * So for now, we will not add the fullName. Eg. "region"
     */
    @Visual
    @Indexed (indexName=IndexNames.FEATURE_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    /**
     * Eg. EBI-658422
     */
    @Indexed (indexName=IndexNames.FEATURE_INTACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_INTACT_ID, rbField=BioFields.FEATURE_INTACT_ID)
    private String featureIntactId;

    /**
     * A Feature has a featureType but not the other way around. And it has
     * exactly one featureType, so it's not a collection.
     */
    //@RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_FEATURE_TYPE, startNodeBioType=BioTypes.FEATURE, endNodeBioType=BioTypes.FEATURE_TYPE, elementClass = BioRelation.class)
    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_FEATURE_TYPE, elementClass = BioRelation.class)
    private BioRelation hasFeatureType;

    /**
     * A feature has a list of featureRanges.
     */
    //@RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_FEATURE_RANGE, startNodeBioType=BioTypes.FEATURE, endNodeBioType=BioTypes.FEATURE_RANGE, elementClass = BioRelation.class)
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_FEATURE_RANGE, elementClass = BioRelation.class)
    private Collection<BioRelation> featureRanges;

    /**
     * An external participant has this feature. Note the relationship is INCOMING.
     * The start node is the participant, and the end node is this node.
     */
    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_FEATURE, startNodeBioType=BioTypes.PARTICIPANT, endNodeBioType=BioTypes.FEATURE, elementClass = BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_FEATURE, elementClass = BioRelation.class)
    private BioRelation hasFeature;

    /**
     *
     */
    public Feature() {}

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
     * This is called either when a Collection<{@link BioRelation}> is updated or
     * just a BioRelation is updated. It updates the endNode's relation
     * property. The FeatureType must be associated only with this Feature.
     * Since we are setting the hasFeatureType property in the FeatureType endNode
     * with the startNode being the Feature node, we are implying that this
     * property cannot be overwritten. So this FeatureType node instance cannot be
     * re-used by another Feature node. We may need to verify if this is true
     * with all one-way relations, where the endNode also has the property set.
     *
     * @param bioRelation
     */
    private void updateEndNodeFeatureType(BioRelation bioRelation) {
        Object startNode = bioRelation.getStartNode();
        if (!bioRelation.isValid(BioEntityClasses.FEATURE, BioEntityClasses.FEATURE_TYPE)) {
           throw new RuntimeException("The BioRelation is invalid. Check the startNode and endNode");
        }
        FeatureType endNode = (FeatureType)bioRelation.getEndNode();
        BioRelation relation = new BioRelation(startNode, endNode, BioRelTypes.HAS_FEATURE_TYPE);
        endNode.setHasFeatureType(relation);
    }

    /**
     * A Feature has a featureType but not the other way around. And it has
     * exactly one featureType, so it's not a collection.
     * <p>
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE_TYPE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE_TYPE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @param hasFeatureType
     */
    public void setHasFeatureType(BioRelation hasFeatureType) {
        this.hasFeatureType = hasFeatureType;
        updateEndNodeFeatureType(hasFeatureType);
    }

    /**
     * * A Feature has a featureType but not the other way around. And it has
     * exactly one featureType, so it's not a collection.
     * <p>
     * {@link RelatedTo} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE_TYPE}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#FEATURE_TYPE}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getHasFeatureType() {
        return hasFeatureType;
    }

    /**
     * This is called either when a Collection<BioRelation> is updated or
     * just a BioRelation is updated. It updates the endNode's relation
     * property.
     *
     * @param bioRelation
     */
    private void updateEndNodeFeatureRange(BioRelation bioRelation) {
        Object startNode = bioRelation.getStartNode();
        if (!bioRelation.isValid(BioEntityClasses.FEATURE, BioEntityClasses.FEATURE_RANGE)) {
           throw new RuntimeException("The BioRelation is invalid. Check the startNode and endNode");
        }
        FeatureRange endNode = (FeatureRange)bioRelation.getEndNode();
        BioRelation relation = new BioRelation(startNode, endNode, BioRelTypes.HAS_FEATURE_RANGE);
        endNode.setFeatureRelation(relation);
    }

    /**
     * A feature has a list of featureRanges.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE_RANGE}
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
     * @param featureRange
     */
    public void addFeatureRange(BioRelation featureRange) {
        if (featureRange == null) {
            featureRanges = new HashSet<BioRelation>();
        }
        featureRanges.add(featureRange);
        updateEndNodeFeatureRange(featureRange);
    }

    /**
     * A feature has a list of featureRanges.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE_RANGE}
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
     * @param featureRanges
     */
    public void setFeatureRanges(Collection<BioRelation> featureRanges) {
        this.featureRanges = featureRanges;
        for (BioRelation relation : featureRanges) {
            updateEndNodeFeatureRange(relation);
        }
    }

    /**
     * A feature has a list of featureRanges.
     * <p>
     * {@link RelatedToVia} {@link Direction#OUTGOING} {@link BioRelTypes#HAS_FEATURE_RANGE}
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
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getFeatureRanges() {
        return featureRanges;
    }

    /**
     * An external participant has this feature. Note the relationship is INCOMING.
     * The start node is the participant, and the end node is this node.
     * <p>
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_FEATURE}
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
    public void setHasFeature(BioRelation hasFeature) {
        this.hasFeature = hasFeature;
    }

    /**
     * * An external participant has this feature. Note the relationship is INCOMING.
     * The start node is the participant, and the end node is this node.
     * <p>
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_FEATURE}
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
     * @return BioRelation
     */
    public BioRelation getHasFeature() {
        return hasFeature;
    }

    /**
     * We only found a shortLabel for feature, and not a fullName yet.
     * So for now, we will not add the fullName. Eg. "region"
     * <p>
     * {@link Indexed} {@link IndexNames#FEATURE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * We only found a shortLabel for feature, and not a fullName yet.
     * So for now, we will not add the fullName. Eg. "region"
     * <p>
     * {@link Indexed} {@link IndexNames#FEATURE_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * {@link UniquelyIndexed} {@link IndexNames#FEATURE_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_ID} {@link BioFields#FEATURE_ID}
     *
     * @return String
     */
    public String getFeatureId() {
        return featureId;
    }

    /**
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
     * {@link Indexed} {@link IndexNames#FEATURE_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_INTACT_ID} {@link BioFields#FEATURE_INTACT_ID}
     *
     * @return String
     */
    public String getFeatureIntactId() {
        return featureIntactId;
    }

    /**
     * {@link Indexed} {@link IndexNames#FEATURE_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_INTACT_ID} {@link BioFields#FEATURE_INTACT_ID}
     *
     * @param featureIntactId
     */
    public void setFeatureIntactId(String featureIntactId) {
        this.featureIntactId = featureIntactId;
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

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + featureId + "-" + shortLabel);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }

        final Feature other = (Feature) obj;

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