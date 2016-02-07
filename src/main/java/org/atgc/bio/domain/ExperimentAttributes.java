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
import org.atgc.bio.meta.RelatedToVia;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.repository.TemplateUtils;
import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * Attribute List in an Intact Experiment. For example:
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
@BioEntity(bioType = BioTypes.EXPERIMENT_ATTRIBUTES)
public class ExperimentAttributes {

    protected static Logger log = LogManager.getLogger(ExperimentAttributes.class);

    /**
     * This is required for the graph database.
     */
    @GraphId
    private Long id;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @UniquelyIndexed (indexName=IndexNames.EXPERIMENT_ATTRIBUTES_EXPERIMENT_REF)
    @Taxonomy(rbClass=TaxonomyTypes.INTACT_EXPERIMENT_REF, rbField=BioFields.EXPERIMENT_REF)  // this is value
    private String experimentRef;

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
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
     * dataset in attributeList.
     */
    @RelatedToVia (direction=Direction.OUTGOING, relType=BioRelTypes.HAS_DATASET)
    private Collection<BioRelation> hasDataset;

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
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getHasDataset() {
        return hasDataset;
    }

    /**
     *
     * @param dataset
     */
    public void setHasDataset(Collection<ExperimentDataset> dataset) {
        HashSet<BioRelation> bioRelations = new HashSet<BioRelation>();
        for (ExperimentDataset experimentDataset : dataset) {
             BioRelation bioRelation = new BioRelation(this, experimentDataset, BioRelTypes.HAS_DATASET);
             bioRelations.add(bioRelation);
        }
        this.hasDataset = bioRelations;
    }

    /**
     *
     * @param dataset
     */
    public void addDataset(ExperimentDataset dataset) {
        if (hasDataset == null) {
            hasDataset = new HashSet<BioRelation>();
        }
        hasDataset.add(new BioRelation(this, dataset, BioRelTypes.HAS_DATASET));
    }

    /**
     *
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getHasComments() {
        return hasComments;
    }

    /**
     *
     * @param comments
     */
    public void setHasComments(Collection<ExperimentComment> comments) {
        HashSet<BioRelation> bioRelations = new HashSet<BioRelation>();
        for (ExperimentComment experimentComment : comments) {
            BioRelation bioRelation = new BioRelation(this, experimentComment, BioRelTypes.HAS_COMMENT);
            bioRelations.add(bioRelation);
        }
        this.hasComments = bioRelations;
    }

    /**
     *
     * @param comment
     */
    public void addHasComment(ExperimentComment comment) {
        if (hasComments == null) {
            hasComments = new HashSet<BioRelation>();
        }
        BioRelation bioRelation = new BioRelation(this, comment, BioRelTypes.HAS_COMMENT);
        hasComments.add(bioRelation);
    }

    /**
     *
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getUsesLibrary() {
        return usesLibrary;
    }

    /**
     *
     * @param libraryUsed
     */
    public void setUsesLibrary(Collection<ExperimentLibrary> libraryUsed) {
        HashSet<BioRelation> bioRelations = new HashSet<BioRelation>();
        for (ExperimentLibrary experimentLibrary : libraryUsed) {
            BioRelation bioRelation = new BioRelation(this, experimentLibrary, BioRelTypes.USES_LIBRARY);
            bioRelations.add(bioRelation);
        }
        this.usesLibrary = bioRelations;
    }

    /**
     *
     * @param libraryUsed
     */
    public void addUsesLibrary(ExperimentLibrary libraryUsed) {
        if (usesLibrary == null) {
            usesLibrary = new HashSet<BioRelation>();
        }
        BioRelation bioRelation = new BioRelation(this, libraryUsed, BioRelTypes.USES_LIBRARY);
        usesLibrary.add(bioRelation);
    }

    /**
     *
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getHasDataProcessing() {
        return hasDataProcessing;
    }

    /**
     *
     * @param dataProcessing
     */
    public void setHasDataProcessing(Collection<ExperimentDataProcessing> dataProcessing) {
        HashSet<BioRelation> bioRelations = new HashSet<BioRelation>();
        for (ExperimentDataProcessing experimentDataProcessing : dataProcessing) {
            BioRelation bioRelation = new BioRelation(this, experimentDataProcessing, BioRelTypes.HAS_DATA_PROCESSING);
            bioRelations.add(bioRelation);
        }
        this.hasDataProcessing = bioRelations;
    }

    /**
     *
     * @param dataProcessing
     */
    public void addHasDataProcessing(ExperimentDataProcessing dataProcessing) {
        if (hasDataProcessing == null) {
            hasDataProcessing = new HashSet<BioRelation>();
        }
        hasDataProcessing.add(new BioRelation(this, dataProcessing, BioRelTypes.HAS_DATA_PROCESSING));
    }

    /**
     *
     * @return String
     */
    public String getAuthorList() {
        return authorList;
    }

    /**
     *
     * @param authorList
     */
    public void setAuthorList(String authorList) {
        this.authorList = authorList;
    }

    /**
     *
     * @return String
     */
    public String getJournal() {
        return journal;
    }

    /**
     *
     * @param journal
     */
    public void setJournal(String journal) {
        this.journal = journal;
    }

    /**
     *
     * @return String
     */
    public String getPublicationYear() {
        return publicationYear;
    }

    /**
     *
     * @param publicationYear
     */
    public void setPublicationYear(String publicationYear) {
        this.publicationYear = publicationYear;
    }

    /**
     * <code>this</code> comment object is related to ExperimentAttributes object with an outgoing
     * relationship called <code>BioRelation</code>
     * This comment is in attributeList.
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_COMMENT, elementClass=BioRelation.class)
    private Collection<BioRelation> hasComments;

    /**
     * <code>this</code> library object is related to ExperimentAttributes object with an outgoing
     * relationship called <code>BioRelation</code>
     * This library-used is in attributeList.
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.USES_LIBRARY, elementClass=BioRelation.class)
    private Collection<BioRelation> usesLibrary;

    /**
     * <code>this</code> url object is related to ExperimentAttributes object with an outgoing
     * relationship called <code>BioRelation</code>
     * This url is in attributeList.
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_URL, elementClass=BioRelation.class)
    private Collection<BioRelation> hasUrl;

    /**
     *
     * @return Collection<BioRelation>
     */
    public Collection<BioRelation> getHasUrl() {
        return hasUrl;
    }

    /**
     *
     * @param url
     */
    public void setHasUrl(Collection<ExperimentUrl> url) {
        HashSet<BioRelation> bioRelations = new HashSet<BioRelation>();
        for (ExperimentUrl experimentUrl : url) {
            BioRelation bioRelation = new BioRelation(this, experimentUrl, BioRelTypes.HAS_URL);
            bioRelations.add(bioRelation);
        }
        this.hasUrl = bioRelations;
    }

    /**
     *
     * @param url
     */
    public void addHasUrl(ExperimentUrl url) {
        if (hasUrl == null) {
            hasUrl = new HashSet<BioRelation>();
        }
        hasUrl.add(new BioRelation(this, url, BioRelTypes.HAS_URL));
    }

    /**
     * <code>this</code> data-processing object is related to ExperimentAttributes object with an outgoing
     * relationship called <code>BioRelation</code>
     * This data-processing is in attributeList.
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_DATA_PROCESSING, elementClass=BioRelation.class)
    private Collection<BioRelation> hasDataProcessing;

    /**
     * <code>this</code> author-list object is related to ExperimentAttributes object with an outgoing
     * relationship called <code>BioRelation</code>
     * This author-list is in attributeList. It is a list of comma-separated
     * authors.
     */
    @Indexed (indexName=IndexNames.AUTHOR_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.AUTHOR_LIST, rbField=BioFields.AUTHOR_LIST)
    private String authorList;

    /**
     * <code>this</code> journal object is related to ExperimentAttributes object with an outgoing
     * relationship called <code>BioRelation</code>
     * This journal is in attributeList.
     */
    @Indexed (indexName=IndexNames.JOURNAL)
    @Taxonomy (rbClass=TaxonomyTypes.JOURNAL, rbField=BioFields.JOURNAL)
    private String journal;

    /**
     * <code>this</code> journal object is related to ExperimentAttributes object with an outgoing
     * relationship called <code>BioRelation</code>
     * This journal is in attributeList.
     */
    @Indexed (indexName=IndexNames.PUBLICATION_YEAR)
    @Taxonomy (rbClass=TaxonomyTypes.PUBLICATION_YEAR, rbField=BioFields.PUBLICATION_YEAR)
    private String publicationYear;
}