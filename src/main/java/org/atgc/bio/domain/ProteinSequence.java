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
 * Connect this to the disease bioentity
 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.SEQUENCE_ID, field1=BioFields.UNIPROT_ID, field2=BioFields.SEQUENCE_SUM, field3=BioFields.SEQUENCE_LENGTH)
@BioEntity(bioType = BioTypes.PROTEIN_SEQUENCE)
public class ProteinSequence {

    protected static Logger log = LogManager.getLogger(ProteinSequence.class);

    @GraphId
    private Long id;
    
    @Taxonomy (rbClass=TaxonomyTypes.UNIPROT_ID, rbField=BioFields.UNIPROT_ID)
    @PartKey
    private String uniprot;
    /**
     * CRC64 or checksum is used as sequenceId
     */
    @Indexed (indexName=IndexNames.SEQUENCE_SUM)
    @Taxonomy (rbClass=TaxonomyTypes.SEQUENCE_SUM, rbField=BioFields.SEQUENCE_SUM)
    @PartKey
    private String sequenceSum;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PROTEIN_SEQUENCE.toString();
    
    /**
     * "entryAudit" : {
     *           "entryVersion" : 56,
     *           "firstPublicDate" : "Mon Feb 25 17:00:00 PST 2008",
     *          "lastAnnotationUpdateDate" : "Tue May 28 17:00:00 PDT 2013",
     *          "lastSequenceUpdateDate" : "Thu May 31 17:00:00 PDT 2001",
     *           "sequenceVersion" : 1
     *   },
     */
   @FullTextIndexed (indexName=IndexNames.SEQUENCE_VERSION)
   @Taxonomy (rbClass=TaxonomyTypes.SEQUENCE_VERSION, rbField=BioFields.SEQUENCE_VERSION)
   private String sequenceVersion;
   
   @FullTextIndexed (indexName=IndexNames.LAST_SEQUENCE_UPDATE_DATE)
   @Taxonomy (rbClass=TaxonomyTypes.LAST_SEQUENCE_UPDATE_DATE, rbField=BioFields.LAST_SEQUENCE_UPDATE_DATE)
   private String lastSequenceUpdateDate;
   
   @FullTextIndexed (indexName=IndexNames.LAST_ANNOTATION_UPDATE_DATE)
   @Taxonomy (rbClass=TaxonomyTypes.LAST_ANNOTATION_UPDATE_DATE, rbField=BioFields.LAST_ANNOTATION_UPDATE_DATE)
   private String lastAnnotationUpdateDate;
   
   @FullTextIndexed (indexName=IndexNames.FIRST_PUBLIC_DATE)
   @Taxonomy (rbClass=TaxonomyTypes.FIRST_PUBLIC_DATE, rbField=BioFields.FIRST_PUBLIC_DATE)
   private String firstPublicDate;
   
   @Indexed (indexName=IndexNames.ENTRY_VERSION)
   @Taxonomy (rbClass=TaxonomyTypes.ENTRY_VERSION, rbField=BioFields.ENTRY_VERSION)
   private String entryVersion;
   
   
   /**
    * <pre>
    *  "sequence" : {
                "crc64" : "35F533967072512D",
                "sequenceLength" : 430,
                "molecularWeight" : 49094,
                "sequenceValue" : "MSSGDPRSGRQDGAPRAAAALCGLYHEAGQQLQRLKDQLAARDALIASLRTRLAALEGHTAPSLVDALLDQVERFREQLRRQEEGASETQLRQEVERLTERLEEKEREMQQLMSQPQHEQEKEVVLLRRSVAEKEKARAASDVLCRSLADETHQLRRTLAATAHMCQHLAKCLDERQCAQGDAGEKSPAELEQTSSDASGQSVIKKLQEENRLLKQKVTHVEDLNAKWQRYDASRDEYVKGLHAQLKRRQVPLEPELMKKEISRLNRQLEEKISDCAEANQELTAMRMSRDTALERVQMLEQQILAYKDDFKSERADRERAHSRIQELEEKIMSLMYQVSQRQDSREPGPCRIHTGNKTAKYLEMDALEHVTPGGWRPESRSQQMEPSAEGGHVCTAQRGQGDLQCPHCLRCFSDEQGEAFLRHLSECCQ"
        }
    * </pre>
    * 
    */
   /**
    * SequenceId is CRC64
    */
   @Taxonomy (rbClass=TaxonomyTypes.SEQUENCE_LENGTH, rbField=BioFields.SEQUENCE_LENGTH)
   @PartKey
   private String sequenceLength;
  
   @Indexed (indexName=IndexNames.MOLECULAR_WEIGHT)
   @Taxonomy (rbClass=TaxonomyTypes.MOLECULAR_WEIGHT, rbField=BioFields.MOLECULAR_WEIGHT)
   private String molecularWeight;
  
   @Indexed (indexName=IndexNames.SEQUENCE_VALUE)
   @Taxonomy (rbClass=TaxonomyTypes.SEQUENCE_VALUE, rbField=BioFields.SEQUENCE_VALUE)
   private String sequenceValue;
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;


   /**
    * ProteinSequenceFeature
    * sequence contains features
    */
   @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
   private Collection<BioRelation> featureRelations;

    public ProteinSequence() {}

    public ProteinSequence(String id, String nodeType, String message) {
        this.sequenceSum = id;
        this.nodeType = nodeType;
        this.message = message;
    }

    public Long getId() {
    	return id;
    }
    
    /**
     * 
     * ProteinSequence contains ProteinSequenceFeature (chain, region (coiled-coil)
     * @param feature 
     */
    public void setFeatureRelations(ProteinSequenceFeature feature) {
        if (featureRelations == null) {
            featureRelations = new HashSet<BioRelation>();
        }
        BioRelation bioRelation = new BioRelation(this, feature, BioRelTypes.CONTAINS);
        if (bioRelation != null) {
            featureRelations.add(bioRelation);
        }
    }
    
    public Iterable<BioRelation> getFeatureRelations() {
        return featureRelations;
    }
    
     public String getEntryVersion() {
        return entryVersion;
    }

    public void setEntryVersion(String entryVersion) {
        this.entryVersion = entryVersion;
    }

    public String getFirstPublicDate() {
        return firstPublicDate;
    }

    public void setFirstPublicDate(String firstPublicDate) {
        this.firstPublicDate = firstPublicDate;
    }

    public String getLastAnnotationUpdateDate() {
        return lastAnnotationUpdateDate;
    }

    public void setLastAnnotationUpdateDate(String lastAnnotationUpdateDate) {
        this.lastAnnotationUpdateDate = lastAnnotationUpdateDate;
    }

    public String getLastSequenceUpdateDate() {
        return lastSequenceUpdateDate;
    }

    public void setLastSequenceUpdateDate(String lastSequenceUpdateDate) {
        this.lastSequenceUpdateDate = lastSequenceUpdateDate;
    }

    public String getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(String molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public String getSequenceLength() {
        return sequenceLength;
    }

    public void setSequenceLength(String sequenceLength) {
        this.sequenceLength = sequenceLength;
    }

    public String getSequenceSum() {
        return sequenceSum;
    }

    public void setSequenceSum(String sequenceSum) {
        this.sequenceSum = sequenceSum;
    }

    public String getSequenceValue() {
        return sequenceValue;
    }

    public void setSequenceValue(String sequenceValue) {
        this.sequenceValue = sequenceValue;
    }

    public String getSequenceVersion() {
        return sequenceVersion;
    }

    public void setSequenceVersion(String sequenceVersion) {
        this.sequenceVersion = sequenceVersion;
    }

    public String getUniprot() {
        return uniprot;
    }

    public void setUniprot(String uniprotId) {
        this.uniprot = uniprotId;
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

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + sequenceSum);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProteinSequence other = (ProteinSequence) obj;
        if ((this.sequenceSum == null) ? (other.sequenceSum != null) : !this.sequenceSum.equals(other.sequenceSum)) {
            return false;
        }
        return !((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType));
    }

}
