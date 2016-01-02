/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import org.atgc.bio.BioFields;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * 
 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.FEATURE_INDEX, field1=BioFields.SEQUENCE_ID, field2=BioFields.START_POSITION, field3=BioFields.END_POSITION)
@BioEntity(bioType = BioTypes.PROTEIN_SEQUENCE_FEATURE)
public class ProteinSequenceFeature {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy(rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PROTEIN_SEQUENCE_FEATURE.toString();

    /**
     * SequenceId - > sequence checksum/CRC
     */
    @Taxonomy (rbClass=TaxonomyTypes.SEQUENCE_ID, rbField=BioFields.SEQUENCE_ID)
    @PartKey
    String sequenceId;
    
   /**
    * <pre>
    *  "featureList" : [
                {
                        "featureLocation" : {
                                "locationEnd" : 430,
                                "locationStart" : 1,
                                "startModifier" : "EXACT",
                                "endModifier" : "EXACT"
                        },
                        "featureStatus" : "Experimental",
                        "featureType" : "chain"
                },
                {
                        "featureLocation" : {
                                "locationEnd" : 348,
                                "locationStart" : 290,
                                "startModifier" : "EXACT",
                                "endModifier" : "EXACT"
                        },
                        "featureStatus" : "Experimental",
                        "featureType" : "region of interest"
                },

    * </pre>
    */
    
   @Taxonomy (rbClass=TaxonomyTypes.START_POSITION, rbField=BioFields.START_POSITION) 
   @PartKey
   private String startPosition;
   
   @Taxonomy (rbClass=TaxonomyTypes.END_POSITION, rbField=BioFields.END_POSITION)
   @PartKey
   private String endPosition;
   
   @Indexed (indexName=IndexNames.FEATURE_TYPE)
   @Taxonomy (rbClass=TaxonomyTypes.FEATURE_TYPE, rbField=BioFields.FEATURE_TYPE)
   String featureType;
   
   @Indexed (indexName=IndexNames.FEATURE_STATUS)
   @Taxonomy (rbClass=TaxonomyTypes.FEATURE_STATUS, rbField=BioFields.FEATURE_STATUS)
   String featureStatus;
   
   @FullTextIndexed(indexName=IndexNames.FEATURE_DESCRIPTION)
   @Taxonomy (rbClass=TaxonomyTypes.FEATURE_DESCRIPTION, rbField=BioFields.FEATURE_DESCRIPTION)
   String featureDescription;
   
   @NodeLabel
   @Indexed (indexName=IndexNames.MESSAGE)
   @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
   public String message;
   
    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;

    
  //  @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, startNodeBioType=BioTypes.PATHWAY, endNodeBioType=BioTypes.NCI_PATHWAY_INTERACTION, elementClass = BioRelation.class)
  // private Collection<BioRelation> pathwayInteractions = new HashSet<BioRelation>();


    public ProteinSequenceFeature() {}

    public ProteinSequenceFeature(String id, String nodeType, String message) {
        this.sequenceId = id;
        this.nodeType = nodeType;
        this.message = message;
    }

    public String getEndPosition() {
        return endPosition;
    }

    public void setEndPosition(String endPosition) {
        this.endPosition = endPosition;
    }

    public String getFeatureStatus() {
        return featureStatus;
    }

    public void setFeatureStatus(String featureStatus) {
        this.featureStatus = featureStatus;
    }

    public String getFeatureType() {
        return featureType;
    }

    public void setFeatureType(String featureType) {
        this.featureType = featureType;
    }

    public String getFeatureDescription() {
        return featureDescription;
    }

    public void setFeatureDescription(String featureDescription) {
        this.featureDescription = featureDescription;
    }
    
    public String getSequenceId() {
        return sequenceId;
    }

    public void setSequenceId(String sequenceId) {
        this.sequenceId = sequenceId;
    }

    public String getStartPosition() {
        return startPosition;
    }

    public void setStartPosition(String startPosition) {
        this.startPosition = startPosition;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    public Long getId() {
    	return id;
    }


    /**
    * &#64;GloballyIndexed (indexName=IndexNames.NODE_TYPE)
    * &#64;Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    * @return
    */
    public String getNodeType() {
        return nodeType;
    }

   /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * {@link NonIndexed} {@link NonIndexed}
     * @return
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link NonIndexed} {@link NonIndexed}
     * @param message */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + sequenceId + " " + startPosition + " " + endPosition);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProteinSequenceFeature other = (ProteinSequenceFeature) obj;
        if ((this.sequenceId == null) ? (other.sequenceId != null) : !this.sequenceId.equals(other.sequenceId)) {
            return false;
        }
        return !((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType));
    }

}
