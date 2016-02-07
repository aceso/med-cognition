/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.repository.TemplateUtils;
import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * An organism as defined in http://www.ncbi.nlm.nih.gov/Taxonomy
 * This includes species data in the long run. Currently no properties
 * are added.
 *
 * @author jtanisha-ee
 * @see BioEntity
 * @see BioTypes
 * @see Indexed
 * @see UniquelyIndexed
 */
@BioEntity(bioType = BioTypes.ORGANISM)
public class Organism {
    protected static Logger log = LogManager.getLogger(Organism.class);

    @GraphId
    private Long id;

    /**
     * The short label of the organism. Eg. "yeast"
     */
    @Visual
    @UniquelyIndexed (indexName=IndexNames.ORGANISM_SHORT_LABEL)
    @Taxonomy(rbClass=TaxonomyTypes.ORGANISM_SHORT_LABEL, rbField=BioFields.ORGANISM_SHORT_LABEL)
    private String organismShortLabel;

    /**
     * The NCBI Taxonomy ID. Eg. 9606 for human (homo sapien)
     */
    @Indexed (indexName=IndexNames.NCBI_TAX_ID)
    @Taxonomy (rbClass=TaxonomyTypes.NCBI_TAX_ID, rbField=BioFields.NCBI_TAX_ID)
    private String ncbiTaxId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.ORGANISM.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * The full name of the organism. Eg. "Saccharomyces cerevisiae (Baker's yeast)"
     */
    @Indexed (indexName=IndexNames.ORGANISM_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.ORGANISM_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    /**
     * Organism from which the data were collected
     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.FROM_WHICH_DATA_IS_COLLECTED, elementClass=BioRelation.class)
    //@RelatedToVia(direction=Direction.BOTH, relType="foo", elementClass=BioRelation.class)
    private Collection<BioRelation> organismRelation = new HashSet<BioRelation>();

    @Visual
    @Indexed (indexName=IndexNames.ORGANISM_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.ORGANISM_LABEL, rbField=BioFields.SHORT_LABEL)  // this is value
    private String shortLabel;

    public Organism() {
    }

    public void setNcbiTaxId(String taxId) {
        this.ncbiTaxId = taxId;
    }

    public String getNcbiTaxId() {
        return ncbiTaxId;
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
     * {@link Indexed} {@link IndexNames#ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * Eg. shortLabel is "certain", which is kind of strange and uninformative
     * <p>
     * {@link Indexed} {@link IndexNames#ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     */
   /* public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    } */

    public String getOrganismShortLabel() {
        return organismShortLabel;
    }

    public void setOrganismShortLabel(String s) {
        this.organismShortLabel = s;
    }

    /**
     * Eg. shortLabel is "certain sequence position" which is vague
     * <p>
     * {@link Indexed} {@link IndexNames#ORGANISM_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * {@link Disease} ...
     * @param endNode {@link Disease} {@link Gene}
     */
    public void setOrganismRelation(Object endNode) {
        BioRelation rel = new BioRelation();
        rel.setEndNode(endNode);
        rel.setStartNode(this);
        rel.setMessage(BioRelTypes.FROM_WHICH_DATA_IS_COLLECTED.toString());
        rel.setName(this.getShortLabel());
    }

    /**
     * Eg. shortLabel is "certain sequence position" which is vague
     * <p>
     * {@link Indexed} {@link IndexNames#ORGANISM_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ORGANISM_FULL_NAME} {@link BioFields#FULL_NAME}
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
        return (nodeType + "-" + ncbiTaxId + "-" + shortLabel);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (!getClass().getName().equals((obj.getClass().getName()))) {
            return false;
        }

        final Organism other = (Organism) obj;

        if ((this.ncbiTaxId != null) && (other.getNcbiTaxId() != null)) {
            if (!this.ncbiTaxId.equals(other.getNcbiTaxId())) {
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