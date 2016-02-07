/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.PartKey;
import org.atgc.bio.meta.PartRelation;
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniqueCompoundIndex;
import org.atgc.bio.meta.UniquelyIndexed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * This is an intermediate dimension between Structure and GeneOntology, since we would
 * like to capture the @chainId metadata. Here is the sample document from pdbgo collection
 * in mongo:
 *
 * {
	"_id" : ObjectId("519ff6f70364c046222c9956"),
	"@structureId" : "3CAP",
	"goTerms" : [
		{
			"@id" : "GO:0006468",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0007165",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0007186",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0007601",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0007602",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0009416",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0009583",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0018298",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0050896",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0050953",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0060041",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0071482",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0001750",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0001917",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0005794",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0005886",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0016020",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0016021",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0042622",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0060342",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0004871",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0004930",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0005515",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0009881",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0046872",
			"@chainId" : "A"
		},
		{
			"@id" : "GO:0006468",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0007165",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0007186",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0007601",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0007602",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0009416",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0009583",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0018298",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0050896",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0050953",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0060041",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0071482",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0001750",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0001917",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0005794",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0005886",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0016020",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0016021",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0042622",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0060342",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0004871",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0004930",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0005515",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0009881",
			"@chainId" : "B"
		},
		{
			"@id" : "GO:0046872",
			"@chainId" : "B"
		}
	]
}

 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.PDBGO_GOID, field1=BioFields.STRUCTURE_ID, field2=BioFields.GO_ID, field3=BioFields.NONE)
@BioEntity(bioType = BioTypes.PDBGO)
public class PdbGo {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(PdbGo.class);

    @GraphId
    private Long id;

    /**
     * The featureRange does not have a unique id of it's own. So we
     * associate that with a uniqueId such as featureId. Eg. 212408
     */
    @PartKey
    @UniquelyIndexed(indexName=IndexNames.STRUCTURE_ID)
    @Taxonomy(rbClass=TaxonomyTypes.STRUCTURE_ID, rbField=BioFields.STRUCTURE_ID)
    private String structureId;

    @NodeLabel
    @Indexed(indexName=IndexNames.PDBGO_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.PDBGO_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Indexed (indexName=IndexNames.PDBGO_CHAIN_ID)
    @Taxonomy(rbClass=TaxonomyTypes.PDBGO_CHAIN_ID, rbField=BioFields.CHAIN_ID)
    private String chainId;

    @PartRelation (field=BioFields.GO_ID)
    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.EXHIBITS_ONTOLOGY, elementClass = BioRelation.class)
    private BioRelation hasOntologyRelation;

    /**
     *
     * @param geneOntology
     */
    public void setGeneOntology(GeneOntology geneOntology) {
        hasOntologyRelation = new BioRelation(this, geneOntology, BioRelTypes.EXHIBITS_ONTOLOGY);
    }

    /**
     *
     * @return GeneOntology
     */
    public GeneOntology getGeneOntology() {
        if (hasOntologyRelation != null) {
            return (GeneOntology)hasOntologyRelation.getEndNode();
        }
        return null;
    }

    /**
     *
     * @return String
     */
    public String getStructureId() {
        return structureId;
    }

    /**
     *
     * @param structureId
     */
    public void setStructureId(String structureId) {
        this.structureId = structureId;
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
}
