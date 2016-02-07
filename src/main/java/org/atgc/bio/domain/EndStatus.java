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
import org.atgc.bio.repository.TemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * Each {@link FeatureRange} has a EndStatus in protein sequencing offsets.
 *
 * @author jtanisha-ee
 * @see BioEntity
 */
@BioEntity(bioType = BioTypes.END_STATUS)
public class EndStatus {
    /**
     *
     */
    protected static Logger log = LogManager.getLogger(EndStatus.class);

    @GraphId
    private Long id;

    /**
     * The {@link FeatureRange} does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId.
     */
    @UniquelyIndexed(indexName=IndexNames.END_STATUS_FEATURE_ID)
    @Taxonomy(rbClass=TaxonomyTypes.FEATURE_ID, rbField=BioFields.FEATURE_ID)
    private String featureId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.END_STATUS.toString();

    /**
     * The message is set to the fullName.
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.END_STATUS_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.END_STATUS_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * Eg. "certain"
     */
    @Visual
    @Indexed (indexName=IndexNames.END_STATUS_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.END_STATUS_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    /**
     * Eg. "certain sequence position"
     */
    @Indexed (indexName=IndexNames.END_STATUS_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.END_STATUS_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    /**
     * Eg. MI:0335
     */
    @Indexed (indexName=IndexNames.END_STATUS_PSI_MI_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PSI_MI_ID, rbField=BioFields.PSI_MI_ID)
    private String psiMiId;

    /**
     * Eg. EBI-540564
     */
    @Indexed (indexName=IndexNames.END_STATUS_INTACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.END_STATUS_INTACT_ID, rbField=BioFields.INTACT_ID)
    private String intactId;

    /**
     * Eg. 14755292
     */
    @Indexed (indexName=IndexNames.END_STATUS_PUBMED_ID)
    @Taxonomy (rbClass=TaxonomyTypes.END_STATUS_PUBMED_ID, rbField=BioFields.PUBMED_ID)
    private String pubmedId;

    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_END_STATUS, startNodeBioType=BioTypes.FEATURE_RANGE, endNodeBioType=BioTypes.END_STATUS, elementClass=BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_END_STATUS, elementClass=BioRelation.class)
    private BioRelation hasEndStatus;

    /**
     * Eg. 14755292<p/>
     *
     * &#64;Indexed (indexName=IndexNames.END_STATUS_PUBMED_ID)><br/>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_PUBMED_ID, rbField=BioFields.PUBMED_ID)
     *
     * @return String
     * @see BioFields
     * @see IndexNames
     * @see TaxonomyTypes
     */
    public String getPubmedId() {
        return pubmedId;
    }

    /**
     * Eg. 14755292<p/>
     *
     * &#64;Indexed (indexName=IndexNames.END_STATUS_PUBMED_ID)<br/>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_PUBMED_ID, rbField=BioFields.PUBMED_ID)
     *
     * @param pubmedId
     * @see BioFields
     * @see IndexNames
     * @see TaxonomyTypes
     */
    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    /**
     * Get the HAS_END_STATUS relationship that connects from FeatureRange
     * to EndStatus.<p/>
     *
     * &#64;RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_END_STATUS, startNodeBioType=BioTypes.FEATURE_RANGE, endNodeBioType=BioTypes.END_STATUS, elementClass=BioRelation.class)
     *
     * @return BioRelation
     * @see BioRelation
     * @see BioRelTypes
     * @see BioTypes
     */
    public BioRelation getHasEndStatus() {
        return hasEndStatus;
    }

    /**
     * Get the HAS_END_STATUS relationship that connects from FeatureRange
     * to EndStatus.<p/>
     *
     * &#64;RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_END_STATUS, startNodeBioType=BioTypes.FEATURE_RANGE, endNodeBioType=BioTypes.END_STATUS, elementClass=BioRelation.class)
     *
     * @param hasEndStatus
     * @see BioRelation
     * @see BioRelTypes
     * @see BioTypes
     */
    public void setHasEndStatus(BioRelation hasEndStatus) {
        this.hasEndStatus = hasEndStatus;
    }

    /**
     * Simple constructor for now.
     *
     */
    public EndStatus() {}

    /**
     * Graph id that is not to be populated by the developer. It is used
     * as an internal id in the graph database.
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * Graph id that is not to be populated by the developer. It is used
     * as an internal id in the graph database.
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Eg. MI:0335 - usually the primaryRef section<p>
     *
     * &#64;Indexed (indexName=IndexNames.END_STATUS_PSI_MI_ID)<br>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.PSI_MI_ID, rbField=BioFields.PSI_MI_ID)
     *
     * @return String
     * @see TaxonomyTypes
     * @see IndexNames
     * @see BioFields
     */
    public String getPsiMiId() {
        return psiMiId;
    }

    /**
     * Eg. MI:0335 - usually the primaryRef section<p>
     *
     * &#64;Indexed (indexName=IndexNames.END_STATUS_PSI_MI_ID)<br>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.PSI_MI_ID, rbField=BioFields.PSI_MI_ID)
     *
     * @param psiMiId
     * @see TaxonomyTypes
     * @see IndexNames
     * @see BioFields
     */
    public void setPsiMiId(String psiMiId) {
        this.psiMiId = psiMiId;
    }

    /**
     * Eg. EBI-540564, usually in the secondaryRef section.<p/>
     *
     * &#64;Indexed (indexName=IndexNames.END_STATUS_INTACT_ID)<br/>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_INTACT_ID, rbField=BioFields.INTACT_ID)
     *
     * @return String
     * @see TaxonomyTypes
     * @see IndexNames
     * @see BioFields
     */
    public String getIntactId() {
        return intactId;
    }

    /**
     * Eg. EBI-540564, usually in the secondaryRef section.<p/>
     *
     * &#64;Indexed (indexName=IndexNames.END_STATUS_INTACT_ID)<br/>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_INTACT_ID, rbField=BioFields.INTACT_ID)
     *
     * @param intactId
     * @see TaxonomyTypes
     * @see IndexNames
     * @see BioFields
     */
    public void setIntactId(String intactId) {
        this.intactId = intactId;
    }

    /**
     * The EndStatus itself does not have an ID. It is part of the FeatureRange.
     * which in turn is part of the Feature. The Feature has a featureId which
     * is unique.
     *
     * &#64;UniquelyIndexed (indexName=IndexNames.FEATURE_ID)<br/>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.FEATURE_ID, rbField=BioFields.FEATURE_ID)
     *
     * @return String
     * @see TaxonomyTypes
     * @see IndexNames
     * @see BioFields
     */
    public String getFeatureId() {
        return featureId;
    }

    /**
     * The EndStatus itself does not have an ID. It is part of the FeatureRange.
     * which in turn is part of the Feature. The Feature has a featureId which
     * is unique.
     *
     * &#64;UniquelyIndexed (indexName=IndexNames.FEATURE_ID)<br/>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.FEATURE_ID, rbField=BioFields.FEATURE_ID)
     *
     * @param featureId
     * @see TaxonomyTypes
     * @see IndexNames
     * @see BioFields
     */
    public void setFeatureId(String featureId) {
        this.featureId = featureId;
    }

    /**
     * The BioType that is defined in BioTypes. Essentially a @BioEntity.<br/>
     *
     * &#64;Indexed (indexName=IndexNames.NODE_TYPE)
     *
     * @return String
     * @see IndexNames
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     * The BioType that is defined in BioTypes. Essentially a @BioEntity.<br>
     *
     * &#64;Indexed (indexName=IndexNames.NODE_TYPE)
     *
     * @param nodeType
     * @see IndexNames
     */
    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType.toString();
    }

    /**
     * The message is required for display. This should have the same value
     * as fullName.
     * <p>
     * Eg. "certain sequence position"
     * <p>
     * &#64;Indexed (indexName=IndexNames.END_STATUS_FULL_NAME)<br>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_FULL_NAME, rbField=BioFields.FULL_NAME)
     *
     * @return String
     * @see BioFields
     * @see IndexNames
     * @see TaxonomyTypes
     */
    public String getMessage() {
        return message;
    }

    /**
     * The message is required for display. This should have the same value
     * as fullName.
     * <p>
     * Eg. "certain sequence position"
     * <p>
     * &#64;Indexed (indexName=IndexNames.END_STATUS_FULL_NAME)<br>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_FULL_NAME, rbField=BioFields.FULL_NAME)
     *
     * @param message
     * @see BioFields
     * @see IndexNames
     * @see TaxonomyTypes
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * &#64;Indexed (indexName=IndexNames.END_STATUS_SHORT_LABEL)<br>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
     *
     * @return String
     * @see BioFields
     * @see IndexNames
     * @see TaxonomyTypes
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * &#64;Indexed (indexName=IndexNames.END_STATUS_SHORT_LABEL)
     * &#64;Documented @Taxonomy (rbClass=TaxonomyTypes.END_STATUS_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
     *
     * @param shortLabel
     * @see BioFields
     * @see IndexNames
     * @see TaxonomyTypes
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * Eg. "certain sequence position"<p/>
     *
     * &#64;Indexed (indexName=IndexNames.END_STATUS_FULL_NAME)<br/>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_FULL_NAME, rbField=BioFields.FULL_NAME)
     * @return String
     * @see BioFields
     * @see IndexNames
     * @see TaxonomyTypes
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Eg. "certain sequence position"<p/>
     *
     * &#64;Indexed (indexName=IndexNames.END_STATUS_FULL_NAME)<br/>
     * &#64;Taxonomy (rbClass=TaxonomyTypes.END_STATUS_FULL_NAME, rbField=BioFields.FULL_NAME)
     *
     * @param fullName
     * @see BioFields
     * @see IndexNames
     * @see TaxonomyTypes
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return int
     */
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    /**
     *
     * @return String
     */
    @Override
    public String toString() {
        return (nodeType + "-" + featureId + "-" + shortLabel);
    }

    /**
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

        final EndStatus other = (EndStatus) obj;

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