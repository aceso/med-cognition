/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.FullTextIndexed;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.RelatedToVia;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.atgc.bio.repository.TemplateUtils;
import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * Participant Identification Method in an Intact Experiment. For example:
 *
 * "participantIdentificationMethod" : {
                "names" : {
                        "shortLabel" : "nucleotide sequence",
                        "fullName" : "nucleotide sequence identification",
                        "alias" : {
                                "@typeAc" : "MI:0303",
                                "#text" : "sequence cloning"
                        }
                },
                "xref" : {
                        "primaryRef" : {
                                "@refTypeAc" : "MI:0356",
                                "@refType" : "identity",
                                "@id" : "MI:0078",
                                "@dbAc" : "MI:0488",
                                "@db" : "psi-mi"
                        },
                        "secondaryRef" : [
                                {
                                        "@refTypeAc" : "MI:0356",
                                        "@refType" : "identity",
                                        "@id" : "EBI-958",
                                        "@dbAc" : "MI:0469",
                                        "@db" : "intact"
                                },
                                {
                                        "@refTypeAc" : "MI:0358",
                                        "@refType" : "primary-reference",
                                        "@id" : "14755292",
                                        "@dbAc" : "MI:0446",
                                        "@db" : "pubmed"
                                }
                        ]
                }
        },
 *
 *
 * We define this object model to store the above type of data. In most
 * cases, we skip many fields.
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.PARTICIPANT_IDENTIFICATION_METHOD)
public class ParticipantIdentificationMethod {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(ParticipantIdentificationMethod.class);

    /**
     * This is required for the graph database.
     */
    @GraphId
    private Long id;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy(rbClass=TaxonomyTypes.MESSAGE, rbField= BioFields.MESSAGE)
    private String message;

    @Override
    public String toString() {
        return nodeType + "-" + experimentRef + "-" + shortLabel;
    }

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
     * The participant identification method seems to be specific to an experiment.
     * What's methods were used in this experiment to detect the interaction.
     * It would be confusing to draw commonality between different experiments.
     * We are better off using experimentRef as the unique index key in Neo4J.
     */
    @UniquelyIndexed(indexName=IndexNames.PIM_EXPERIMENT_REF)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_EXPERIMENT_REF, rbField=BioFields.EXPERIMENT_REF)  // this is value
    private String experimentRef;

    /**
     * Participant identification method short label as in Intact experiment.
     * For example: nucleotide sequence
     */
    @Visual
    @Indexed (indexName=IndexNames.PARTICIPANT_IDENTIFICATION_METHOD_SHORT_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.PARTICIPANT_IDENTIFICATION_METHOD_SHORT_LABEL, rbField=BioFields.SHORT_LABEL)
    private String shortLabel;

    /**
     * Participant identification method full name as in Intact experiment.
     * For example: two hybrid
     */
    @Indexed (indexName=IndexNames.PARTICIPANT_IDENTIFICATION_METHOD_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PARTICIPANT_IDENTIFICATION_METHOD_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;

    /**
     * Participant identification method aliases as in Intact experiment.
     *
     * For eg.
     * sequence cloning
     */
    @FullTextIndexed (indexName = IndexNames.PARTICIPANT_IDENTIFICATION_METHOD_ALIAS)
    @Taxonomy (rbClass=TaxonomyTypes.PARTICIPANT_IDENTIFICATION_METHOD_ALIAS, rbField=BioFields.METHOD_ALIAS)
    private String aliases;

    /**
     *
     * @return String
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
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    /**
     *
     * @return String
     */
    public String getAliases() {
        return aliases;
    }

    /**
     *
     * @param aliases
     */
    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    /**
     *
     * @return String
     */
    public Collection<BioRelation> getReferencesPubMed() {
        return referencesPubMed;
    }

    /**
     *
     * @param pubMedList
     */
    public void setReferencesPubMed(Collection<PubMed> pubMedList) {
        HashSet<BioRelation> bioRelations = new HashSet<BioRelation>();
        for (PubMed pubMed : pubMedList) {
            BioRelation bioRelation = new BioRelation(this, pubMed, BioRelTypes.REFERENCES_PUBMED);
            bioRelations.add(bioRelation);
        }
        this.referencesPubMed = bioRelations;
    }

    /**
     * List of secondary pubMed articles.
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> referencesPubMed;

    /**
     *
     * @return String
     */
    public String getExperimentId() {
        return experimentRef;
    }

    /**
     *
     * @param experimentId
     */
    public void setExperimentId(String experimentId) {
        this.experimentRef = experimentId;
    }
}