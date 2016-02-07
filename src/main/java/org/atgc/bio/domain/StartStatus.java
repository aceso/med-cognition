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
 * A {@link StartStatus} of a given {@link FeatureRange}.
 *
 * @author jtanisha-ee
 * @see BioEntity
 * @see BioTypes
 * @see Indexed
 * @see UniquelyIndexed
 */
@BioEntity(bioType = BioTypes.START_STATUS)
public class StartStatus {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(StartStatus.class);

    @GraphId
    private Long id;

    /**
     * The featureRange does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     */
    @UniquelyIndexed (indexName=IndexNames.START_STATUS_FEATURE_ID)
    @Taxonomy(rbClass=TaxonomyTypes.FEATURE_ID, rbField=BioFields.FEATURE_ID)
    private String featureId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.START_STATUS.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.START_STATUS_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.START_STATUS_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * Eg. shortLabel is "certain", which is kind of strange and uninformative
     */
    @Visual
    @Indexed (indexName=IndexNames.START_STATUS_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.START_STATUS_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    /**
     * Eg. shortLabel is "certain sequence position" which is vague
     */
    @Indexed (indexName=IndexNames.START_STATUS_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.START_STATUS_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    /**
     * Eg. MI:0335
     */
    @Indexed (indexName=IndexNames.START_STATUS_PSI_MI_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PSI_MI_ID, rbField=BioFields.PSI_MI_ID)
    private String psiMiId;

    /**
     * Eg. EBI-540564
     */
    @Indexed (indexName=IndexNames.START_STATUS_INTACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.START_STATUS_INTACT_ID, rbField=BioFields.INTACT_ID)
    private String intactId;

    /**
     * Eg. 14755292
     */
    @Indexed (indexName=IndexNames.START_STATUS_PUBMED_ID)
    @Taxonomy (rbClass=TaxonomyTypes.START_STATUS_PUBMED_ID, rbField=BioFields.PUBMED_ID)
    private String pubmedId;

    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_START_STATUS, startNodeBioType=BioTypes.FEATURE_RANGE, endNodeBioType=BioTypes.START_STATUS, elementClass=BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_START_STATUS, elementClass=BioRelation.class)
    private BioRelation hasStartStatus;

    //@RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_END_STATUS, startNodeBioType=BioTypes.FEATURE_RANGE, endNodeBioType=BioTypes.END_STATUS, elementClass=BioRelation.class)
    @RelatedTo(direction=Direction.INCOMING, relType=BioRelTypes.HAS_END_STATUS, elementClass=BioRelation.class)
    private BioRelation hasEndStatus;

    /**
     * Eg. 14755292
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#START_STATUS_PUBMED_ID} {@link BioFields#PUBMED_ID}
     *
     * @return String
     */
    public String getPubmedId() {
        return pubmedId;
    }

    /**
     * Eg. 14755292
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#START_STATUS_PUBMED_ID} {@link BioFields#PUBMED_ID}
     *
     *
     * @param pubmedId
     */
    public void setPubmedId(String pubmedId) {
        this.pubmedId = pubmedId;
    }

    /**
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_START_STATUS}
     * <p>
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#START_STATUS}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getHasStartStatus() {
        return hasStartStatus;
    }

    /**
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_START_STATUS}
     * <p>
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#START_STATUS}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param hasStartStatus
     */
    public void setHasStartStatus(BioRelation hasStartStatus) {
        this.hasStartStatus = hasStartStatus;
    }

    /**
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_END_STATUS}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#END_STATUS}
     * </LI>
     *
     * elementClass {@link BioRelation}
     *
     * @return BioRelation
     */
    public BioRelation getHasEndStatus() {
        return hasEndStatus;
    }

    /**
     * {@link RelatedTo} {@link Direction#INCOMING} {@link BioRelTypes#HAS_END_STATUS}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#FEATURE_RANGE}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#END_STATUS}
     * </LI>
     *
     * elementClass {@link BioRelation}
     *
     * @param hasEndStatus
     */
    public void setHasEndStatus(BioRelation hasEndStatus) {
        this.hasEndStatus = hasEndStatus;
    }

    /**
     *
     */
    public StartStatus() {}

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
     * Eg. MI:0335
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_PSI_MI_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PSI_MI_ID} {@link BioFields#PSI_MI_ID}
     *
     * @return String
     */
    public String getPsiMiId() {
        return psiMiId;
    }

    /**
     * Eg. MI:0335
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_PSI_MI_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#PSI_MI_ID} {@link BioFields#PSI_MI_ID}
     *
     * @param psiMiId
     */
    public void setPsiMiId(String psiMiId) {
        this.psiMiId = psiMiId;
    }

    /**
     * Eg. EBI-540564
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#START_STATUS_INTACT_ID} {@link BioFields#INTACT_ID}
     *
     * @return String
     */
    public String getIntactId() {
        return intactId;
    }

    /**
     * Eg. EBI-540564
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#START_STATUS_INTACT_ID} {@link BioFields#INTACT_ID}
     *
     * @param intactId
     */
    public void setIntactId(String intactId) {
        this.intactId = intactId;
    }

    /**
     * The {@link FeatureRange} does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     * <p>
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
     * The {@link FeatureRange} does not have a unique id of it's own. So we
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
     * Eg. shortLabel is "certain", which is kind of strange and uninformative
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#START_STATUS_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * Eg. shortLabel is "certain", which is kind of strange and uninformative
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#START_STATUS_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * Eg. shortLabel is "certain sequence position" which is vague
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#START_STATUS_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * Eg. shortLabel is "certain sequence position" which is vague
     * <p>
     * {@link Indexed} {@link IndexNames#START_STATUS_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#START_STATUS_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
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

        final StartStatus other = (StartStatus) obj;

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