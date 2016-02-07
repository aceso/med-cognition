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
import org.atgc.bio.meta.NonIndexed;
import org.atgc.bio.meta.PartRelation;
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniqueCompoundIndex;
import org.atgc.bio.repository.CompoundKey;
import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * The Gene is linked to the GO item through additional attributes like PubMedId.
 * So we will need an intermediate dimension between Gene and GO.
 * Each instance of GeneToGo is linked with one or more
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
@UniqueCompoundIndex(indexName=IndexNames.GENE_TO_GO_ONTOLOGY, field1=BioFields.NCBI_GENE_RELATION, field2=BioFields.GO_ID_RELATION, field3=BioFields.NONE)
@BioEntity(bioType = BioTypes.GENE_TO_GO_ONTOLOGY)
public class GeneToGoOntology {

    protected static Logger log = LogManager.getLogger(GeneToGoOntology.class);

    @GraphId
    private Long id;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy(rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private BioTypes nodeType = BioTypes.GENE_TO_GO_ONTOLOGY;

    public BioTypes getNodeType() {
        return nodeType;
    }

    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType;
    }

    @PartRelation(field = BioFields.NCBI_GENE_ID)
    @RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.GENE_RELATION, elementClass = BioRelation.class)
    private BioRelation ncbiGeneRelation;

    @PartRelation (field = BioFields.GENE_ONTOLOGY_ID)
    @RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.GO_RELATION, elementClass = BioRelation.class)
    private BioRelation goIdRelation;

    @NonIndexed
    private String evidence;

    @NonIndexed
    private String goTerm;

    public String getGoTerm() {
        return goTerm;
    }

    @Override
    public String toString() {
        return "GeneToGoOntology{" + "nodeType=" + nodeType + ", evidence=" + evidence + ", goTerm=" + goTerm + ", message=" + message + ", category=" + category + '}';
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 23 * hash + (this.nodeType != null ? this.nodeType.hashCode() : 0);
        hash = 23 * hash + (this.evidence != null ? this.evidence.hashCode() : 0);
        hash = 23 * hash + (this.goTerm != null ? this.goTerm.hashCode() : 0);
        hash = 23 * hash + (this.message != null ? this.message.hashCode() : 0);
        hash = 23 * hash + (this.category != null ? this.category.hashCode() : 0);
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
        final GeneToGoOntology other = (GeneToGoOntology) obj;
        if (this.nodeType != other.nodeType) {
            return false;
        }
        if ((this.evidence == null) ? (other.evidence != null) : !this.evidence.equals(other.evidence)) {
            return false;
        }
        if ((this.goTerm == null) ? (other.goTerm != null) : !this.goTerm.equals(other.goTerm)) {
            return false;
        }
        if ((this.message == null) ? (other.message != null) : !this.message.equals(other.message)) {
            return false;
        }
        return !((this.category == null) ? (other.category != null) : !this.category.equals(other.category));
    }

    public void setGoTerm(String goTerm) {
        this.goTerm = goTerm;
    }

    @NodeLabel
    @Indexed (indexName=IndexNames.GENE2GOONTOLOGY_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.GENE2GOONTOLOGY_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @NonIndexed
    private String category;

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

    public String getGoId() {
        Object endNode = goIdRelation.getEndNode();
        if (endNode != null) {
            GeneOntology geneOntology = (GeneOntology)endNode;
            return geneOntology.getGeneOntologyId();
        }
        return null;
    }

    public void setGeneOntology(GeneOntology go) {
        goIdRelation = new BioRelation(this, go, BioRelTypes.EXHIBITS_ONTOLOGY);
    }

    public String getEvidence() {
        return evidence;
    }

    public void setEvidence(String evidence) {
        this.evidence = evidence;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    @RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.GENE_RELATION, elementClass = BioRelation.class)
    private BioRelation pubmedRelation;

    public void setPubMed(PubMed pubmed) {
        pubmedRelation = new BioRelation(this, pubmed, BioRelTypes.REFERENCES_PUBMED);
    }

    public PubMed getPubMed() {
        if (pubmedRelation != null) {
            return (PubMed)pubmedRelation.getEndNode();
        }
        return null;
    }
}