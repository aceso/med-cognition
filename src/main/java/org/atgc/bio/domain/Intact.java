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
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * This Intact BioEntity hangs on to collections of Experiment objects. Each Experiment object
 * can have one or more Interaction objects. Each Interaction has one or more
 * Participant objects. Each Participant object can have
 *
 * A) one or more Interactor(Protein) objects and
 * B) one or more ExperimentalRole objects and
 * C) one or more Feature objects and
 * D) one BiologicalRole object
 *
 * Each Feature object is connected to
 *
 * A) FeatureType object and
 * B) FeatureRange object
 *
 * @author jtanisha-ee
 * @see UniquelyIndexed
 * @see BioEntity
 * @see Indexed
 * @see Taxonomy
 * @see BioFields
 * @see FullTextIndexed
 * @see NonIndexed
 * @see RelatedToVia
 */
@BioEntity(bioType = BioTypes.INTACT)
public class Intact {
    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * Each Intact bioentity has an intactId. This id is the key and not the
     * @GraphId above.
     */
    @UniquelyIndexed (indexName=IndexNames.INTACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.INTACT_ID, rbField=BioFields.INTACT_ID)
    private String intactId;

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * The data type of the end node - intact experiment is encapsulated inside the
     * BioRelation instance. The client that creates a collection of
     * BioRelation objects needs to specify the data type of the end node.
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> hasIntactExperiments;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIntactId() {
        return intactId;
    }

    public void setIntactId(String intactId) {
        this.intactId = intactId;
    }

    public Collection<BioRelation> getHasIntactExperiments() {
        return hasIntactExperiments;
    }

    public void setHasIntactExperiments(Collection<BioRelation> hasIntactExperiments) {
        this.hasIntactExperiments = hasIntactExperiments;
    }

    public void addIntactExperiment(IntactExperiment intactExperiment) {
        if (hasIntactExperiments == null) {
            hasIntactExperiments = new HashSet<BioRelation>();
        }
        BioRelation bioRelation = new BioRelation(this, intactExperiment, BioRelTypes.HAS_INTACT_EXPERIMENT);
        hasIntactExperiments.add(bioRelation);
    }
}