/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import org.atgc.bio.BioFields;

import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * Connect this to the disease bioentity
 * 
 * evidence_id:
 * ProteinExistence
 * http://www.uniprot.org/docs/pe_criteriahttp://www.uniprot.org/docs/pe_criteria
 *
 * CommentStatus: http://www.uniprot.org/docs/similar
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.COMMENT_ID, field1=BioFields.UNIPROT_ID, field2=BioFields.COMMENT_STATUS, field3=BioFields.COMMENT_TYPE)
@BioEntity(bioType = BioTypes.PROTEIN_ANNOTATION_COMMENT)
public class ProteinAnnotationComment {

    protected static Logger log = LogManager.getLogger(ProteinAnnotationComment.class);

    @GraphId
    private Long id;
    
    @Taxonomy(rbClass=TaxonomyTypes.UNIPROT_ID, rbField=BioFields.UNIPROT_ID)
    @PartKey
    private String uniprot;
    
    /**
     * INTERACTION, FUNCTION, SUBCELLULAR LOCATION, ALTERNATIVE PRODUCTS,
     * TISSUE SPECIFICITY,PTM, SUBUNIT, CAUTION
     */
    @Indexed (indexName=IndexNames.COMMENT_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT_TYPE, rbField=BioFields.COMMENT_TYPE)
    @PartKey
    private String commentType;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PROTEIN_ANNOTATION_COMMENT.toString();
    
   /**
    * Experimental, By Similarity, None, 
    */
   @FullTextIndexed (indexName=IndexNames.COMMENT_STATUS)
   @Taxonomy (rbClass=TaxonomyTypes.COMMENT_STATUS, rbField=BioFields.COMMENT_STATUS)
   @PartKey private String commentStatus;
   
   @FullTextIndexed (indexName=IndexNames.COMMENT)
   @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
   private String comment;
   
   /**
    * {@link ProteinEvidenceFields}
    */
   @SubList (indexName=IndexNames.EVIDENCE_ID)
   @Taxonomy (rbClass=TaxonomyTypes.EVIDENCE_ID, rbField=BioFields.EVIDENCE_ID)
   List<String> evidenceId;
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;

    
    
    public ProteinAnnotationComment() {}

    public ProteinAnnotationComment(String type, String status, String uniprotId) {
        this.commentType= type;
        this.commentStatus = status;
        this.uniprot = uniprotId;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCommentStatus() {
        return commentStatus;
    }

    public void setCommentStatus(String commentStatus) {
        this.commentStatus = commentStatus;
    }

    /**
     * 
     * @return String
     */
    public String getCommentType() {
        return commentType;
    }

    public void setCommentType(String commentType) {
        this.commentType = commentType;
    }

    public List<String> getEvidenceId() {
        return evidenceId;
    }

    public void setEvidenceId(List<String> evidenceIds) {
        this.evidenceId = evidenceIds;
    }
    
    public Long getId() {
    	return id;
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
        return (nodeType + "-" + commentType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final ProteinAnnotationComment other = (ProteinAnnotationComment) obj;
        if ((this.commentType == null) ? (other.commentType != null) : !this.commentType.equals(other.commentType)) {
            return false;
        }
        return !((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType));
    }

}
