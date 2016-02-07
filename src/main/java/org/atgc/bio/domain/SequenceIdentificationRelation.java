/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;

/**
 * OncoRelation is used to depict the relationship of interaction components in NCIPathway
 *
 * This sequenceidentification with this gene has been found in this protein
 *
 *  <SequenceIdentificationCollection>
 *               <HgncID>341</HgncID>
 *               <LocusLinkID>189</LocusLinkID>
 *               <GenbankAccession></GenbankAccession>
 *               <RefSeqID>NM_000030</RefSeqID>
 *               <UniProtID>P21549</UniProtID>
 *  </SequenceIdentificationCollection>
 *
 * @author jtanisha-ee
 */
@RelationshipEntity
public class SequenceIdentificationRelation {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(SequenceIdentificationRelation.class);

    /**
     * <p>
     * The start node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     *
     */
    @StartNode
    private Object startNode;

    /**
     * <p>
     * The end node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     *
     */
    @EndNode
    private Object endNode;

    /**
     * <p>
     * The default relation type. The client must set this to what they want.
     */
    @RelType
   // private String relType = BioRelTypes.IDENTIFIED_IN_SEQUENCE.toString();
   private BioRelTypes relType = BioRelTypes.IDENTIFIED_IN_SEQUENCE;
    
   /**
    *
    */
    @RelProperty
    private String locusLinkId;

    @RelProperty
    private String refSeqId;

    @RelProperty
    private String hgncId;

    @RelProperty
    private String genBankAccession;

    /**
     * <p>
     * This property is required as it is not displayed without it.
     */
    @RelProperty
    private String message;

    /**
     * <p>
     * {@link StartNode}
     * @param node
     */
    public void setStartNode(Object node) {
        this.startNode = node;
    }

    /**
     * <p>
     * {@link EndNode}
     * @param node
     */
    public void setEndNode(Object node) {
        this.endNode = node;
    }

    /**
     * <p>
     * {@link RelProperty} {@link BioFields#LOCUSLINK_ID}
     * @param id
     */
    public void setLocusLinkId(String id) {
        this.locusLinkId = id;
    }

    /**
     *
     * @return String
     */
    public String getLocusLinkId() {
        return this.locusLinkId;
    }

    /**
     *
     * @param genBankAccession
     */
    public void setGenBankAccession(String genBankAccession) {
        this.genBankAccession = genBankAccession;
    }

    /**
     *
     * @return String
     */
    public String getGenBankAccession() {
        return this.genBankAccession;
    }

    /**
     * <p>
     * @return Object {@link StartNode}
     */
    public Object getStartNode() {
        return this.startNode;
    }

    /**
     * <p>
     * @return Object {@link EndNode}
     */
    public Object getEndNode() {
        return this.endNode;
    }

    /**
     * <p>
     * This property is required as it is not displayed without it.
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * <p>
     * This property is required as it is not displayed without it.
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     *
     * @param hgncId
     */
    public void setHgncId(String hgncId) {
        this.hgncId = hgncId;
    }

    /**
     *
     * @return String
     */
    public String getHgncId() {
        return this.hgncId;
    }


    /**
     * <p>
     * {@link RelType}
     * @param relType
     */
    public void setRelType(BioRelTypes relType) {
        this.relType = relType;
    }

    /**
     * <p>
     * {@link RelType}
     * @return String
     */
    public BioRelTypes getRelType() {
        return this.relType;
    }


    /**
     * <p>
     * Sets the oncoRelationship for each interaction component
     * For each component
     * @param hgncId {@link SequenceIdentificationCollection#hgncId}
     * @param locusLinkId {@link SequenceIdentificationCollection#locusLinkId}
     * @param genBankAccession {@link SequenceIdentificationRelation#genBankAccession}
     * @param refSeqId
     * @param startNode {@link SequenceIdentificationRelation#startNode}
     * @param endNode {@link SequenceIdentificationRelation#endNode}
     * @param relType {@link SequenceIdentificationRelation#relType}
     */
    public final void setSequenceIdentificationRelation(String hgncId,
                          String locusLinkId,
                          String genBankAccession,
                          String refSeqId,
                          Object startNode, Object endNode, BioRelTypes relType) {

        this.hgncId = hgncId;
        this.locusLinkId = locusLinkId;
        this.genBankAccession = genBankAccession;
        this.refSeqId = refSeqId;
        this.startNode = startNode;
        this.endNode = endNode;
        this.relType = relType;
        this.message = relType.toString();
    }


   /**
    * <p>
    *
    * {@link RelProperty}
    * @param hgncId {@link RelProperty} {@link sequenceIdentificationRelation#hgncId}
    * @param locusLinkId {@link RelProperty} {@link sequenceIdentificationRelation#locusLinkId}
    * @param genBankAccession {@link RelProperty} {@link genBankAccession}
     * @param refSeqId
    * @param startNode {@link StartNode}
    * @param endNode {@link EndNode}
    * @param roleType {@link roleType}
    *
    */
   public SequenceIdentificationRelation(String hgncId, String locusLinkId, String genBankAccession,
                         String refSeqId, Object startNode, Object endNode, BioRelTypes roleType) {

        setSequenceIdentificationRelation(hgncId, locusLinkId, genBankAccession, refSeqId,
                         startNode, endNode, roleType);
   }


}