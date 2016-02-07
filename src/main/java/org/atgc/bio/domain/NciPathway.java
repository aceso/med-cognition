/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * Connect this to the disease BioEntity
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.NCI_PATHWAY)
public class NciPathway {

    protected static Logger log = LogManager.getLogger(NciPathway.class);

    @GraphId
    private Long id;

    @UniquelyIndexed(indexName=IndexNames.PATHWAY_SHORT_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.PATHWAY_SHORT_NAME, rbField=BioFields.PATHWAY_SHORT_NAME)
    private String shortName;

    //@GloballyIndexed
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.NCI_PATHWAY.toString();

    /*
    @Indexed (indexName=IndexNames.PATHWAY_ORGANISM)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_ORGANISM, rbField=BioFields.PATHWAY_ORGANISM)
    private String organism;
    */

    // pathwayId is same as nciId
    @Indexed (indexName=IndexNames.PATHWAY_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_ID, rbField=BioFields.PATHWAY_ID)
    private String pathwayId;

    @Indexed (indexName=IndexNames.PATHWAY_LONG_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_LONG_NAME, rbField=BioFields.PATHWAY_LONG_NAME)
    private String longName;

    @Indexed (indexName=IndexNames.PATHWAY_SOURCE_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_SOURCE_ID, rbField=BioFields.PATHWAY_SOURCE_ID)
    private String sourceId;

    @Indexed (indexName=IndexNames.PATHWAY_SOURCE_TEXT)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_SOURCE_TEXT, rbField=BioFields.PATHWAY_SOURCE_TEXT)
    private String sourceText;

    @FullTextIndexed(indexName = IndexNames.PATHWAY_CURATOR_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_CURATOR_LIST, rbField=BioFields.PATHWAY_CURATOR_LIST)
    private String curatorList;

    @FullTextIndexed (indexName = IndexNames.PATHWAY_REVIEWER_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.PATHWAY_REVIEWER_LIST, rbField=BioFields.PATHWAY_REVIEWER_LIST)
    private String reviewerList;

    @NonIndexed
    private String subNet;

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed (indexName=IndexNames.NCI_PATHWAY_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.NCI_PATHWAY_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)  // this is value
    private String shortLabel;

    //@RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, startNodeBioType=BioTypes.PATHWAY, endNodeBioType=BioTypes.NCI_PATHWAY_INTERACTION, elementClass = BioRelation.class)
    //private BioRelation isPartOf;
    /**
     * Each pathwayComponent sets the relation with NciPathwayInteraction object
     * "PathwayComponentList" : [
     *     {
     *       "@interaction_idref" : "209473"
     *     }"PathwayComponentList" : [
     *     {
     *       "@interaction_idref" : "209473"
     *     }
     *
     */

    //@RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, startNodeBioType=BioTypes.NCI_PATHWAY, endNodeBioType=BioTypes.NCI_PATHWAY_INTERACTION, elementClass = BioRelation.class)
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, elementClass = BioRelation.class)
    private Collection<BioRelation> pathwayComponents = new HashSet<BioRelation>();

    /* organism */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_OBSERVED_IN, elementClass = BioRelation.class)
    private Collection<BioRelation> organismComponents = new HashSet<BioRelation>();

    public String getPathwayId() {
        return pathwayId;
    }

    public void setPathwayId(String pathwayId) {
        this.pathwayId = pathwayId;
    }

    public String getSubNet() {
        return subNet;
    }

    public void setSubNet(String subNet) {
        this.subNet = subNet;
    }

    public String getShortName() {
        return shortName;
    }

    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getLongName() {
        return longName;
    }

    public void setLongName(String longName) {
        this.longName = longName;
    }


    public Iterable<BioRelation> getPathwayComponents() {
        log.info("length = " + pathwayComponents.size());
        return pathwayComponents;
        //return IteratorUtil.asCollection(proteinInteractions);
    }

     public Iterable<BioRelation> getOrganismComponent() {
        log.info("length = " + organismComponents.size());
        return organismComponents;
        //return IteratorUtil.asCollection(proteinInteractions);
    }

    public void setOrganismComponent(Organism organism) {
        final BioRelation organismRelation = new BioRelation(this, organism, BioRelTypes.IS_OBSERVED_IN);

       // log.info("organism.shortLabel =" + organism.getShortLabel());
        organismRelation.setName(organism.getShortLabel());
        this.organismComponents.add(organismRelation);
       // log.info("organismComponents size = " + organismComponents.size());
    }

    public NciPathway(String shortName, String nodeType, String message) {
        this.shortName = shortName;
        this.nodeType = nodeType;
        this.message = message;
    }

    /**
     * This integrates all the interactions that are components of the pathway.
     * These interactions are {@link NciPathwayInteraction} with this entity.
     *
     *
     * "PathwayComponentList" : [
     *     {
     *       "@interaction_idref" : "209473"
     *     }
     *     {
     *       "@interaction_idref" : "209435"
     *     }
     *     {
     *      "@interaction_idref" : "209445"
     *     }
     *   ]
     *
     * pathwayComponent
     * @param interaction
     * @param interactionType
     *
     */
    public void setPathwayComponent(NciPathwayInteraction interaction, String interactionType) {
        final BioRelation pathwayRelation = new BioRelation(this, interaction, BioRelTypes.IS_PART_OF_PATHWAY);
        pathwayRelation.setName(interactionType);
        this.pathwayComponents.add(pathwayRelation);
        log.info("pathwayComponents size = " + pathwayComponents.size());
    }

    public NciPathway() {}

    public Long getId() {
    	return id;
    }


    /**
    * &#64;GloballyIndexed (indexName=IndexNames.NODE_TYPE)
    * &#64;Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    *
    */
    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }


    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public String getSourceText() {
        return sourceText;
    }

    public void setSourceText(String sourceText) {
        this.sourceText = sourceText;
    }

    public void setCuratorList(String cList) {
        this.curatorList = cList;
    }

    public String getCuratorList() {
        return curatorList;
    }

    public String getReviewerList() {
        return reviewerList;
    }


    public void setReviewerList(String rList) {
        this.reviewerList = rList;
    }

    public void setShortLabel(String s) {
        this.shortLabel = s;
    }

    public String getShortLabel() {
        return this.shortLabel;
    }


    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + pathwayId + "-" + shortName + "-" + longName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NciPathway other = (NciPathway) obj;
        if ((this.shortName == null) ? (other.shortName != null) : !this.shortName.equals(other.shortName)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }


}
