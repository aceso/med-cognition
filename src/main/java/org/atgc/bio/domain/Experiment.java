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
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.atgc.bio.repository.TemplateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jtanisha-ee
 * @see BioEntity
 * @see BioTypes
 * @see Indexed
 * @see UniquelyIndexed
 * @see Taxonomy
 * @see NonIndexed
 */
@BioEntity(bioType = BioTypes.EXPERIMENT)
public class Experiment {
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    /**
     * Eg. 207447
     */
    @UniquelyIndexed (indexName=IndexNames.INTACT_EXPERIMENT_REF)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_REF, rbField=BioFields.EXPERIMENT_REF)  // this is value
    private String experimentRef;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.EXPERIMENT.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed (indexName=IndexNames.EXPERIMENT_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    @Indexed (indexName=IndexNames.EXPERIMENT_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    @Indexed (indexName=IndexNames.EXPERIMENT_PUBMED_ID)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_PUBMED_ID, rbField=BioFields.BIB_PRIMARY_PUBMED_ID)
    private String bibPrimaryPubmedId;

    @Indexed (indexName=IndexNames.EXPERIMENT_PUBMED_ID)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_PUBMED_ID, rbField=BioFields.PRIMARY_PUBMED_ID)
    private String primaryPubmedId;

    /**
     *
     */
    public Experiment() {}

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
     * Eg. 207447
     * <p>
     * {@link UniquelyIndexed} {@link IndexNames#UNIPROT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_REF} {@link BioFields#EXPERIMENT_REF}
     *
     * @return String
     */
    public String getExperimentRef() {
        return experimentRef;
    }

    /**
     * Eg. 207447
     * <p>
     * {@link UniquelyIndexed} {@link IndexNames#UNIPROT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_REF} {@link BioFields#EXPERIMENT_REF}
     *
     * @param experimentRef
     */
    public void setExperimentRef(String experimentRef) {
        this.experimentRef = experimentRef;
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
     * {@link Indexed} {@link IndexNames#EXPERIMENT_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENT_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENT_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENT_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENT_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_PUBMED_ID} {@link BioFields#BIB_PRIMARY_PUBMED_ID}
     *
     * @return String
     */
    public String getBibPrimaryPubmedId() {
        return bibPrimaryPubmedId;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENT_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_PUBMED_ID} {@link BioFields#BIB_PRIMARY_PUBMED_ID})
     *
     * @param bibPrimaryPubmedId
     */
    public void setBibPrimaryPubmedId(String bibPrimaryPubmedId) {
        this.bibPrimaryPubmedId = bibPrimaryPubmedId;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENT_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_PUBMED_ID} {@link BioFields#PRIMARY_PUBMED_ID}
     *
     * @return String
     */
    public String getPrimaryPubmedId() {
        return primaryPubmedId;
    }

    /**
     * {@link Indexed} {@link IndexNames#EXPERIMENT_PUBMED_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#EXPERIMENT_PUBMED_ID} {@link BioFields#PRIMARY_PUBMED_ID}
     *
     * @param primaryPubmedId
     */
    public void setPrimaryPubmedId(String primaryPubmedId) {
        this.primaryPubmedId = primaryPubmedId;
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
        return (nodeType + "-" + experimentRef + "-" + shortLabel);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }

        final Experiment other = (Experiment) obj;

        if ((this.experimentRef != null) && (other.getExperimentRef() != null)) {
            if (!this.experimentRef.equals(other.getExperimentRef())) {
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