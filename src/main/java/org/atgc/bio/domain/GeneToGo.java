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
import org.atgc.bio.meta.PartRelation;
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.RelatedToVia;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniqueCompoundIndex;
import org.atgc.bio.repository.CompoundKey;
import java.net.URISyntaxException;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * The GeneToGo is linked to the GO item through additional attributes like PubMedId.
 * So we will need an intermediate dimension between GeneToGo and GO, which we call
 * GeneToGoOntology. Each instance of GeneToGo is linked with one or more
 * GeneToGoOntology instances, depending upon the number of GO_ID links. Each instance
 * of GeneToGo is linked to a unique GeneID or unique instance of Gene.
 *
> db.ncbigene2go.findOne({"GeneID" : "814630"})
{
	"_id" : ObjectId("52424fd20364ea87f3133d1f"),
	"GOList" : [
		{
			"GO_ID" : "GO:0003700",
			"Evidence" : "ISS",
			"GO_term" : "sequence-specific DNA binding transcription factor activity",
			"Category" : "Function",
			"PubMed" : "11118137"
		},
		{
			"GO_ID" : "GO:0005634",
			"Evidence" : "ISM",
			"GO_term" : "nucleus",
			"Category" : "Component"
		},
		{
			"GO_ID" : "GO:0006355",
			"Evidence" : "TAS",
			"GO_term" : "regulation of transcription, DNA-dependent",
			"PubMed" : "11118137",
			"Category" : "Process"
		},
		{
			"GO_ID" : "GO:0006499",
			"Evidence" : "RCA",
			"GO_term" : "N-terminal protein myristoylation",
			"Category" : "Process"
		},
		{
			"GO_ID" : "GO:0006635",
			"Evidence" : "RCA",
			"GO_term" : "fatty acid beta-oxidation",
			"Category" : "Process"
		},
		{
			"GO_ID" : "GO:0006891",
			"Evidence" : "RCA",
			"GO_term" : "intra-Golgi vesicle-mediated transport",
			"Category" : "Process"
		},
		{
			"GO_ID" : "GO:0016558",
			"Evidence" : "RCA",
			"GO_term" : "protein import into peroxisome matrix",
			"Category" : "Process"
		}
	],
	"GeneID" : "814630",
	"tax_id" : "3702"
}


 * This class has no @Visual field.
 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.GENE_ID, field1=BioFields.NCBI_TAX_ID_RELATION, field2=BioFields.NCBI_GENE_RELATION, field3=BioFields.NONE)
@BioEntity(bioType = BioTypes.GENE_TO_GO)
public class GeneToGo {

    protected static Logger log = LogManager.getLogger(GeneToGo.class);

    @GraphId
    private Long id;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy(rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private BioTypes nodeType = BioTypes.GENE_TO_GO;

    public BioTypes getNodeType() {
        return nodeType;
    }

    @Override
    public String toString() {
        return "GeneToGo{" + "nodeType=" + nodeType + ", message=" + message + '}';
    }

    public String getMessage() {
        return message;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 29 * hash + (this.nodeType != null ? this.nodeType.hashCode() : 0);
        hash = 29 * hash + (this.message != null ? this.message.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GeneToGo other = (GeneToGo) obj;
        if (this.nodeType != other.nodeType) {
            return false;
        }
        return !((this.message == null) ? (other.message != null) : !this.message.equals(other.message));
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType;
    }

    @PartRelation(field = BioFields.NCBI_GENE_ID)
    @RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.GENE_RELATION, elementClass = BioRelation.class)
    private BioRelation ncbiGeneRelation;

    @PartRelation (field=BioFields.NCBI_TAX_ID)
    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_TAXONOMY, elementClass = BioRelation.class)
    private BioRelation ncbiTaxIdRelation;

    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.HAS_ONTOLOGIES, elementClass=BioRelation.class)
    private HashSet<BioRelation> geneToGoOntologyRelations;

    public void setOntologies(HashSet<GeneToGoOntology> ontologies) {
        geneToGoOntologyRelations = new HashSet<BioRelation>();
        for (GeneToGoOntology ontology : ontologies) {
            BioRelation ontologyRelation = new BioRelation(this, ontology, BioRelTypes.HAS_ONTOLOGIES);
            geneToGoOntologyRelations.add(ontologyRelation);
        }
    }

    public HashSet<GeneToGoOntology> getOntologies() {
        if (geneToGoOntologyRelations == null) {
            return null;
        }
        HashSet<GeneToGoOntology> ontologies = new HashSet<GeneToGoOntology>();
        for (BioRelation ontologyRelation : geneToGoOntologyRelations) {
            GeneToGoOntology ontology = (GeneToGoOntology)(ontologyRelation.getEndNode());
            ontologies.add(ontology);
        }
        return ontologies;
    }

    public NcbiTaxonomy getNcbiTaxonomy() {
        return (NcbiTaxonomy)(ncbiTaxIdRelation.getEndNode());
    }

    public void setNcbiTaxonomy(NcbiTaxonomy ncbiTaxonomy) {
        ncbiTaxIdRelation = new BioRelation(this, ncbiTaxonomy, BioRelTypes.HAS_TAXONOMY);
    }

    @NodeLabel
    @Indexed (indexName=IndexNames.GENE2GO_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.GENE2GO_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * Get the ncbiGeneId if the Gene node is not null.
     *
     * @return String
     */
    public String getNcbiGeneId() {
        Object endNode = ncbiGeneRelation.getEndNode();
        if (endNode != null) {
            Gene gene = (Gene)endNode;
            return gene.getNcbiGeneId();
        }
        return null;
    }

    /**
     * Return the @UniqueCompoundIndex value for the gene.
     *
     * @return String
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public String getGeneCompoundKey() throws IllegalAccessException, URISyntaxException, IllegalArgumentException, NoSuchFieldException {
        Object endNode = ncbiGeneRelation.getEndNode();
        if (endNode != null) {
            Gene gene = (Gene)endNode;
            return CompoundKey.getCompoundKey(gene).getValue();
        }
        return null;
    }

    public BioRelation getNcbiGeneRelation() {
        return ncbiGeneRelation;
    }

    public void setGene(Gene gene) {
        ncbiGeneRelation = new BioRelation(this, gene, BioRelTypes.GENE_RELATION);
    }
}