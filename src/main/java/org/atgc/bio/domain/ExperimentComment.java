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
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.repository.TemplateUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * Experiment comment in an Intact Experiment. For example:
 *
 * The comment field below is what we are talking about.
 *
        "attributeList" : [
                {
                        "@nameAc" : "MI:0875",
                        "@name" : "dataset",
                        "#text" : "Apoptosis - Interactions involving proteins with a function related to apoptosis"
                },
                {
                        "@nameAc" : "MI:0612",
                        "@name" : "comment",
                        "#text" : "This experiment contains the data released by AFCS on January 20,2003."
                },
                {
                        "@nameAc" : "MI:0672",
                        "@name" : "library-used",
                        "#text" : "Mouse B cell library used for yeast two-hybrid screening."
                },
                {
                        "@nameAc" : "MI:0875",
                        "@name" : "dataset",
                        "#text" : "AFCS - Interactions from the Alliance for Cell Signaling database"
                },
                {
                        "@nameAc" : "MI:0614",
                        "@name" : "url",
                        "#text" : "http://www.signaling-gateway.org/data/Y2H/about_Y2H.html"
                },
                {
                        "@nameAc" : "MI:0633",
                        "@name" : "data-processing",
                        "#text" : "The UniProtKB entry was mapped using BLAST-WU (98% identity, 100% of the fragment length) when the protein / nucleotide sequence of the fragment was provided, or using the International Protein Index (IPI) when only the protein_gi was provided. Protein fragment ranges were aligned to the UniProtKB entry when necessary. A temporary IntAct protein entry has been created when no matching UniProtKB entry was found. It will be replaced by a UniProtKB entry when it becomes available."
                },
                {
                        "@nameAc" : "MI:0636",
                        "@name" : "author-list",
                        "#text" : "Papin J., Subramaniam S."
                },
                {
                        "@nameAc" : "MI:0885",
                        "@name" : "journal",
                        "#text" : "Curr. Opin. Biotechnol. (0958-1669)"
                },
                {
                        "@nameAc" : "MI:0886",                        "@name" : "publication year",
                        "#text" : "2004"
                },
                {
                        "@nameAc" : "MI:0612",
                        "@name" : "comment",
                        "#text" : "This experiment contains the data released by AFCS on March 10,2004."
                },
                {
                        "@nameAc" : "MI:0672",
                        "@name" : "library-used",
                        "#text" : "Mouse Macrophage library used for yeast two-hybrid screening."
                }
        ],

 *
 * We define this object model to store the above type of data. In most
 * cases, we skip many fields.
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.EXPERIMENT_COMMENT)
public class ExperimentComment {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    /**
     * This is required for the graph database.
     */
    @GraphId
    private Long id;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy(rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     *
     * @return String
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     *
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return nodeType + "-" + experimentRef;
    }

    @UniquelyIndexed (indexName=IndexNames.EXPERIMENT_COMMENT_EXPERIMENT_REF)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_EXPERIMENT_REF, rbField=BioFields.EXPERIMENT_REF)
    private String experimentRef;

    /**
     *
     * @return String
     */
    public String getExperimentRef() {
        return experimentRef;
    }

    /**
     *
     * @param experimentRef
     */
    public void setExperimentRef(String experimentRef) {
        this.experimentRef = experimentRef;
    }

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
     *
     * @return String
     */
    public String getComment() {
        return comment;
    }

    /**
     *
     * @param comment
     */
    public void setComment(String comment) {
        this.comment = comment;
    }

    /**
     * comment in attributeList.
     */
    @Indexed (indexName=IndexNames.EXPERIMENT_COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.EXPERIMENT_COMMENT, rbField=BioFields.COMMENT)
    private String comment;
}