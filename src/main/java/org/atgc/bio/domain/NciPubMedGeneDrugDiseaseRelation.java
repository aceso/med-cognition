/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;

/**
 * OncoRelation is used to depict the relationship of interaction components in NCIPathway
 * @author jtanisha-ee
 */
@RelationshipEntity
public class NciPubMedGeneDrugDiseaseRelation {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    /**
     * <p>
     * The start node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @StartNode
    private Object startNode;

    /**
     * <p>
     * The start node must be a @BioEntity class. And it cannot be null,
     * if user would like to persist. It also must exist in the graph
     * database. The start node is typically populated at the time of
     * creating this relationship, through a constructor.
     */
    @EndNode
    private Object endNode;

    /**
     * <p>
     * The default relation type. The client must set this to what they want.
     */
    @RelType
    private BioRelTypes relType = BioRelTypes.REFERENCES_PUBMED;

    /**
     * <p>
     * This property is required as it is not displayed without it.
     */
    @RelProperty
    private String message;

    @RelProperty
    private String pubMedStatement;

   /**
    *
    * Unlike gene status flags, which can cover many sentences associated with a single gene,
    * Sentence Status Flags describe the curator's findings for a specific sentence.
    * Sentences can be true positives, false positives, unclear, or redundant.
    * The status of a specific gene is found in the text contents of the
    * XML SentenceStatusFlag element and is the sentenceStatus attribute of the
    * caBIO Evidence class.
    * {@link NciSentenceStatusFlags#FINISHED}
    * {@link NciSentenceStatusFlags#NO_FACT}...
    *
    */
    @RelProperty
    private String sentenceStatusFlag;


    public void setPubMedStatement(String statement) {
        this.pubMedStatement = statement;
    }

    public String getPubMedStatement() {
        return pubMedStatement;
    }

    public void setSentenceStatusFlag(NciSentenceStatusFlags flag) {
        this.sentenceStatusFlag = flag.toString();
    }

    public String getSentenceStatusFlag() {
        return sentenceStatusFlag;
    }

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
     *
     * @return String
     */
    public BioRelTypes getRelType() {
        return this.relType;
    }

    public NciPubMedGeneDrugDiseaseRelation(){};

    public void setNciPubMedRelation(Object startNode, Object endNode,
                BioRelTypes relType) {
        this.startNode = startNode;
        this.endNode = endNode;
        this.relType = relType;
        this.message = relType.toString();
    }

}