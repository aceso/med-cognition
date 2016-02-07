/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Citation is used by Protein and Uniprot entries
 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.CITATION_INDEX, field1=BioFields.UNIPROT_ID, field2=BioFields.CNTR, field3=BioFields.PUBLICATION_DATE)
@BioEntity(bioType = BioTypes.CITATION)
public class Citation {

    protected static Logger log = LogManager.getLogger(Citation.class);

    @GraphId
    private Long id;
   
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.CITATION.toString();
    
    /**
     * 	"citationPublicationDate" : "JUL-2011", or 2005
     */
    @Taxonomy (rbClass=TaxonomyTypes.PUBLICATION_DATE, rbField=BioFields.PUBLICATION_DATE)
    @PartKey
    String publicationDate;
    
    /**
     * citation counter
     */
    @PartKey
    String cntr;
    
    @Taxonomy (rbClass=TaxonomyTypes.UNIPROT_ID, rbField=BioFields.UNIPROT_ID)
    @PartKey
    String uniprot;
    
    /**
     * "citationType" : "Unpublished/no plans to publish",
     */
    @Indexed (indexName=IndexNames.CITATION_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.CITATION_TYPE, rbField=BioFields.CITATION_TYPE)
    private String citationType;
    
    /**
     * doi: digital object identifier
     * 	"citationXrefs" : {
				"doi" : "10.1038/nature05805",
				"pubmedId" : "17495919"
			},
     * gb - genome biology
     * nature - nature
     */
    @FullTextIndexed (indexName=IndexNames.CITATION_REFERENCE_DOI)
    @Taxonomy (rbClass=TaxonomyTypes.CITATION_REFERENCE_DOI, rbField=BioFields.CITATION_REFERENCE_DOI)
    private String citationReferenceDoi;
 
    @FullTextIndexed (indexName=IndexNames.CITATION_TITLE)
    @Taxonomy (rbClass=TaxonomyTypes.CITATION_TITLE, rbField=BioFields.CITATION_TITLE)
    private String citationTitle;
    
    @FullTextIndexed (indexName=IndexNames.EVIDENCE_IDS)
    @Taxonomy (rbClass=TaxonomyTypes.EVIDENCE_IDS, rbField=BioFields.EVIDENCE_IDS)
    private String evidenceIds;
    
    /**
     * "authoringGroups" : [
				"Ensembl"
			],
     *  cGRASP (B.F. Koop & W.S. Davidson)
     */ 
    @FullTextIndexed (indexName=IndexNames.AUTHORING_GROUPS)
    @Taxonomy (rbClass=TaxonomyTypes.AUTHORING_GROUPS, rbField=BioFields.AUTHORING_GROUPS)
    private String authoringGroups;
    
    
   
    
    
    /**
     * 	"citationSummaryList" : [
     *	"IDENTIFICATION"
     *		],  
     *   eg: "NUCLEOTIDE SEQUENCE [LARGE SCALE GENOMIC DNA]"
     */
    @FullTextIndexed (indexName=IndexNames.SUMMARY_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.SUMMARY_LIST, rbField=BioFields.SUMMARY_LIST)
    private String summaryList;
    
    /**
     * authors relationship
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> authorRelationship;
    
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
    
    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
  
   /**
    * 
    */
   //@RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
   //private Collection<BioRelation> featureRelations;


    public Citation() {}

    public Long getId() {
    	return id;
    }
    
    public void setAuthorRelationship(EndNode endNode) {
        if (authorRelationship == null) {
            authorRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, BioRelTypes.HAS_AUTHOR);
    }
    
    public Iterable<BioRelation> getAuthorRelationship() {
        return authorRelationship;
    }

    public String getAuthoringGroups() {
        return authoringGroups;
    }

    public void setAuthoringGroups(String authoringGroups) {
        this.authoringGroups = authoringGroups;
    }

    public String getCitationReferenceDoi() {
        return citationReferenceDoi;
    }

    public void setCitationReferenceDoi(String citationReferenceDoi) {
        this.citationReferenceDoi = citationReferenceDoi;
    }

    public String getCitationTitle() {
        return citationTitle;
    }

    public void setCitationTitle(String citationTitle) {
        this.citationTitle = citationTitle;
    }

    public String getCitationType() {
        return citationType;
    }

    public void setCitationType(String citationType) {
        this.citationType = citationType;
    }

    public String getCntr() {
        return cntr;
    }

    public void setCntr(String cntr) {
        this.cntr = cntr;
    }

    public String getEvidenceIds() {
        return evidenceIds;
    }

    public void setEvidenceIds(String evidenceIds) {
        this.evidenceIds = evidenceIds;
    }

    public String getPublicationDate() {
        return publicationDate;
    }

    public void setPublicationDate(String publicationDate) {
        this.publicationDate = publicationDate;
    }

    public String getSummaryList() {
        return summaryList;
    }

    public void setSummaryList(String summaryList) {
        this.summaryList = summaryList;
    }

    /** uniprotId */
    public String getUniprot() {
        return uniprot;
    }

    public void setUniprot(String uniProtId) {
        this.uniprot = uniProtId;
    }
    
    
    /**
    * &#64;GloballyIndexed (indexName=IndexNames.NODE_TYPE)
    * &#64;Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    * @return String
    */
    public String getNodeType() {
        return nodeType;
    }

   /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * {@link NonIndexed} {@link NonIndexed}
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link NonIndexed} {@link NonIndexed}
     * @param message 
     */
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
        return (nodeType + "-" + citationTitle);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Citation other = (Citation) obj;
        if ((this.citationTitle == null) ? (other.citationTitle != null) : !this.citationTitle.equals(other.citationTitle)) {
            return false;
        }
        return !((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType));
    }

}

/**
 * {
                        "citationTitle" : "ABIN-2 is required for optimal activation of Erk MAP kinase in innate immune responses.",
                        "citationPublicationDate" : "2006",
                        "authors" : [
                                "Papoutsopoulou S.",
                                "Symons A.",
                                "Tharmalingham T.",
                                "Belich M.P.",
                                "Kaiser F.",
                                "Kioussis D.",
                                "O'Garra A.",
                                "Tybulewicz V.",
                                "Ley S.C."
                        ],
                        "citationSummaryList" : [
                        "FUNCTION"
                        ],
                        "citationType" : "journal article",
                        "citationXrefs" : {
                                "doi" : "10.1038/ni1334",
                                "pubmedId" : "16633345"
                        },
                        "sampleSources" : [ ]
                }
                
   */

   /* Another example
    * sampleSources
                            {
                                        "sampleSourceType" : "STRAIN",
                                        "sampleSource" : "C57BL/6J"
                                },
                                {
                                        "sampleSourceType" : "TISSUE",
                                        "sampleSource" : "Pancreas"
                                }
                        ]
                         "citationSummaryList" : [
                                "NUCLEOTIDE SEQUENCE [LARGE SCALE MRNA]"
                        ],


               

  */