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
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.atgc.bio.meta.*;
import org.atgc.bio.repository.TemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * A {@link FeatureType} of a given {@link Feature}.
 *
 * @author jtanisha-ee
 * @see BioEntity
 * @see BioTypes
 * @see Indexed
 * @see UniquelyIndexed
 * @see BioRelTypes
 */
@BioEntity(bioType = BioTypes.FEATURE_TYPE)
public class FeatureType {
    protected static Logger log = LogManager.getLogger(FeatureType.class);

    @GraphId
    private Long id;

    /**
     * The featureType does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     */
    @UniquelyIndexed (indexName=IndexNames.FEATURE_TYPE_FEATURE_ID)
    @Taxonomy(rbClass=TaxonomyTypes.FEATURE_ID, rbField=BioFields.FEATURE_ID)
    private String featureId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.FEATURE_TYPE.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * Eg. "sufficient to bind"
     */
    @Visual
    @Indexed (indexName=IndexNames.FEATURE_TYPE_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_TYPE_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    /**
     * Eg. "sufficient binding region"
     */
    @Indexed (indexName=IndexNames.FEATURE_TYPE_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_TYPE_FULL_NAME, rbField=BioFields.SHORT_LABEL)
    private String fullName;

    /**
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return String
     */
    public String getAlias() {
        return alias;
    }

    /**
     *
     * @param alias
     */
    public void setAlias(String alias) {
        this.alias = alias;
    }

    /**
     *
     * @return BioRelation
     */
    public BioRelation getHasFeatureType() {
        return hasFeatureType;
    }

    /**
     *
     * @param hasFeatureType
     */
    public void setHasFeatureType(BioRelation hasFeatureType) {
        this.hasFeatureType = hasFeatureType;
    }

    /**
     * Eg. "sufficient binding site"
     */
    @Indexed (indexName=IndexNames.FEATURE_TYPE_ALIAS)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_TYPE_ALIAS, rbField=BioFields.ALIAS)
    public String alias;

    /**
     * Eg. MI:0442
     */
    @Indexed (indexName=IndexNames.FEATURE_TYPE_PSI_MI_ID)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_TYPE_PSI_MI_ID, rbField=BioFields.FEATURE_TYPE_PSI_MI_ID)
    public String featureTypePsiMiId;

    /**
     * Eg. EBI-608899
     */
    @Indexed (indexName=IndexNames.FEATURE_TYPE_INTACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_TYPE_INTACT_ID, rbField=BioFields.FEATURE_TYPE_INTACT_ID)
    public String featureTypeIntactId;

    /**
     * Eg.
     */
    @Indexed (indexName=IndexNames.FEATURE_TYPE_PUBMED_ID)
    @Taxonomy (rbClass=TaxonomyTypes.FEATURE_TYPE_PUBMED_ID, rbField=BioFields.FEATURE_TYPE_PUBMED_ID)
    public String featureTypePubMedId;

    /**
     * Feature has a FeatureType.
     */
    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_FEATURE_TYPE, startNodeBioType=BioTypes.FEATURE, endNodeBioType=BioTypes.FEATURE_TYPE, elementClass = BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_FEATURE_TYPE, elementClass = BioRelation.class)
    private BioRelation hasFeatureType;

    /**
     *
     */
    public FeatureType() {}

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
     * The featureType does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     * <p>
     * {@link UniquelyIndexed} {@link IndexNames#FEATURE_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_ID} {@link BioFields#FEATURE_ID}

     * @return String
     */
    public String getFeatureId() {
        return featureId;
    }

    /**
     * The featureType does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     * <p>
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
     * Eg. EBI-608899
     * <p>
     * {@link Indexed} {@link IndexNames#FEATURE_TYPE_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_TYPE_INTACT_ID} {@link BioFields#FEATURE_TYPE_INTACT_ID}

     * @return String
     */
    public String getFeatureTypeIntactId() {
        return featureTypeIntactId;
    }

    /**
     * * Eg. EBI-608899
     * <p>
     * {@link Indexed} {@link IndexNames#FEATURE_TYPE_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_TYPE_INTACT_ID} {@link BioFields#FEATURE_TYPE_INTACT_ID}

     * @param featureTypeIntactId
     */
    public void setFeatureTypeIntactId(String featureTypeIntactId) {
        this.featureTypeIntactId = featureTypeIntactId;
    }

    /**
     * Eg. MI:0442
     * <p>
     * {@link Indexed} {@link IndexNames#FEATURE_TYPE_PSI_MI_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_TYPE_PSI_MI_ID} {@link BioFields#FEATURE_TYPE_PSI_MI_ID}
     *
     * @return String
     */
    public String getFeatureTypePsiMiId() {
        return featureTypePsiMiId;
    }

    /**
     * Eg. MI:0442
     * <p>
     * {@link Indexed} {@link IndexNames#FEATURE_TYPE_PSI_MI_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_TYPE_PSI_MI_ID} {@link BioFields#FEATURE_TYPE_PSI_MI_ID}
     *
     * @param featureTypePsiMiId
     */
    public void setFeatureTypePsiMiId(String featureTypePsiMiId) {
        this.featureTypePsiMiId = featureTypePsiMiId;
    }

    /**
     * {@link Indexed} {@link IndexNames#FEATURE_TYPE_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_TYPE_PUBMED_ID} {@link BioFields#FEATURE_TYPE_PUBMED_ID}
     *
     * @return String
     */
    public String getFeatureTypePubMedId() {
        return featureTypePubMedId;
    }

    /**
     * {@link Indexed} {@link IndexNames#FEATURE_TYPE_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FEATURE_TYPE_PUBMED_ID} {@link BioFields#FEATURE_TYPE_PUBMED_ID}
     *
     * @param featureTypePubMedId
     */
    public void setFeatureTypePubMedId(String featureTypePubMedId) {
        this.featureTypePubMedId = featureTypePubMedId;
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
        if (getClass() != obj.getClass()) {
            return false;
        }
        final FeatureType other = (FeatureType) obj;

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