/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.domain.types.TaxonomyRank;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.FullTextIndexed;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.NonIndexed;
import org.atgc.bio.meta.RelatedTo;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * An Organism is part of NcbiTaxonomy, but not the other way around. NcbiTaxonomy
 * is the classification of the organisms, and not the actual organisms themselves.
 * At the final level the classification may yield leaf elements which are
 * actual organisms. For instance ncbiTaxId=9606 is "Homo Sapiens".
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.NCBI_TAXONOMY)
public class NcbiTaxonomy {
    protected static Logger log = LogManager.getLogger(NcbiTaxonomy.class);

    @GraphId
    private Long id;

    /**
     * 	id:  "NCBITaxon:9606" for humans or homosapiens,
     */
    @UniquelyIndexed (indexName=IndexNames.NCBI_TAX_ID)
    @Taxonomy(rbClass=TaxonomyTypes.NCBI_TAX_ID, rbField=BioFields.NCBI_TAX_ID)
    private String ncbiTaxId;

    /**
     * "name" : "Homo sapiens",
     */
    @Visual
    @Indexed(indexName=IndexNames.NCBI_TAXONOMY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NCBI_TAXONOMY_NAME, rbField=BioFields.NCBI_TAXONOMY_NAME)
    private String name;

    @NodeLabel
    @Indexed (indexName=IndexNames.TAXONOMY_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.TAXONOMY_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.IS_A, elementClass = BioRelation.class)
    private BioRelation isA;

    /**
     * {
     *     "synonym" : "\"man\" EXACT common_name []"
     *  },
     * {
     *     "synonym" : "\"human\" EXACT genbank_common_name []"
     * }
     */
    @FullTextIndexed (indexName=IndexNames.NCBI_TAXONOMY_RELATED_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.NCBI_TAXONOMY_RELATED_SYNONYMS, rbField=BioFields.TAXONOMY_RELATED_SYNONYMS)
    private String taxonomyRelatedSynonyms;

    @FullTextIndexed (indexName=IndexNames.NCBI_TAXONOMY_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.NCBI_TAXONOMY_EXACT_SYNONYMS, rbField=BioFields.TAXONOMY_EXACT_SYNONYMS)
    private String taxonomyExactSynonyms;

    public void setIsA(NcbiTaxonomy ncbiTaxonomy) {
        isA = new BioRelation(this, ncbiTaxonomy, BioRelTypes.IS_A);
    }

    public NcbiTaxonomy getIsA() {
        return (NcbiTaxonomy)(isA.getEndNode());
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTaxonomyRelatedSynonyms() {
        return taxonomyRelatedSynonyms;
    }

    public void setTaxonomyRelatedSynonyms(String taxonomyRelatedSynonyms) {
        this.taxonomyRelatedSynonyms = taxonomyRelatedSynonyms;
    }

    public String getTaxonomyExactSynonyms() {
        return taxonomyExactSynonyms;
    }

    public void setTaxonomyExactSynonyms(String taxonomyExactSynonyms) {
        this.taxonomyExactSynonyms = taxonomyExactSynonyms;
    }

    public TaxonomyRank getTaxonomyRank() {
        return taxonomyRank;
    }

    public void setTaxonomyRank(TaxonomyRank taxonomyRank) {
        this.taxonomyRank = taxonomyRank;
    }

    public String getXref() {
        return xref;
    }

    public void setXref(String xref) {
        this.xref = xref;
    }

    @Indexed (indexName=IndexNames.TAXONOMY_RANK)
    @Taxonomy (rbClass=TaxonomyTypes.TAXONOMY_RANK, rbField=BioFields.TAXONOMY_RANK)
    private TaxonomyRank taxonomyRank;

    @NonIndexed
    private String xref;

    public String getNcbiTaxId() {
        return ncbiTaxId;
    }

    @Override
    public int hashCode() {
        int hash = 7;
        hash = 11 * hash + (this.ncbiTaxId != null ? this.ncbiTaxId.hashCode() : 0);
        hash = 11 * hash + (this.name != null ? this.name.hashCode() : 0);
        hash = 11 * hash + (this.taxonomyRank != null ? this.taxonomyRank.hashCode() : 0);
        hash = 11 * hash + (this.xref != null ? this.xref.hashCode() : 0);
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
        final NcbiTaxonomy other = (NcbiTaxonomy) obj;
        if ((this.ncbiTaxId == null) ? (other.ncbiTaxId != null) : !this.ncbiTaxId.equals(other.ncbiTaxId)) {
            return false;
        }
        if ((this.name == null) ? (other.name != null) : !this.name.equals(other.name)) {
            return false;
        }
        if ((this.taxonomyRank == null) ? (other.taxonomyRank != null) : !this.taxonomyRank.equals(other.taxonomyRank)) {
            return false;
        }
        return !((this.xref == null) ? (other.xref != null) : !this.xref.equals(other.xref));
    }

    @Override
    public String toString() {
        return "NcbiTaxonomy{" + "ncbiTaxId=" + ncbiTaxId + ", name=" + name + ", taxonomyRelatedSynonyms=" + taxonomyRelatedSynonyms + ", taxonomyExactSynonyms=" + taxonomyExactSynonyms + ", taxonomyRank=" + taxonomyRank + ", xref=" + xref + '}';
    }


    public void setNcbiTaxId(String ncbiTaxId) {
        this.ncbiTaxId = ncbiTaxId;
    }
}