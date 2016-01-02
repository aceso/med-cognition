/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import org.atgc.bio.BioFields;

import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;

/**
 * SampleSource is used by Citation in Protein and Uniprot entries 
 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.SAMPLE_INDEX, field1=BioFields.PUBLICATION_DATE, field2=BioFields.SAMPLE_SOURCE, field3=BioFields.SAMPLE_SOURCE_TYPE)
@BioEntity(bioType = BioTypes.SAMPLE_SOURCE)
public class SampleSource {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;
   
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy(rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.SAMPLE_SOURCE.toString();

    
    @Taxonomy (rbClass=TaxonomyTypes.PUBLICATION_DATE, rbField=BioFields.PUBLICATION_DATE)
    @PartKey
    String publicationDate;
    
    /**
     * sampleSourceType can be:  STRAIN, TISSUE,  
     * 
     */
    @Indexed (indexName=IndexNames.SAMPLE_SOURCE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.SAMPLE_SOURCE_TYPE, rbField=BioFields.SAMPLE_SOURCE_TYPE)
    @PartKey String sampleSourceType;
   
    
    /**
     * 
     * sampleSource can be: Brain, Hereford, Red Jungle fowl, pancreas, C57BL/6C57BL/6J
     * 
     * 	"sampleSources" : [
     *            {
     *                 "sampleSourceType" : "TISSUE",
     *                 "evidenceIds" : [ ],
     *                 "sampleSource" : "Brain"
     *            }
     *  ]
     *  "sampleSourceType" : "STRAIN",
     * "evidenceIds" : [ ],
     *			"sampleSource" : "Hereford"
     */
    @FullTextIndexed (indexName=IndexNames.SAMPLE_SOURCE)
    @Taxonomy (rbClass=TaxonomyTypes.SAMPLE_SOURCE, rbField=BioFields.SAMPLE_SOURCE)
    @PartKey private String sampleSource;
   
    @SubList(indexName=IndexNames.SAMPLE_EVIDENCE_IDS)
    private List<String> sampleEvidenceIds;
    
     @SubList (indexName=IndexNames.DRUG_SALTS)
    private List<String> salts;
    
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


    public SampleSource() {}

    public Long getId() {
    	return id;
    }
    
    /**
     * citation publication date
     * @return 
     */
    public String getPublicationDate() {
        return publicationDate;
    }
    
    public void setPublicationDate(String str) {
        publicationDate = str;
    }
    
    public List<String> getSampleEvidenceIds() {
        return sampleEvidenceIds;
    }

    public void setSampleEvidenceIds(List<String> sampleEvidenceIds) {
        this.sampleEvidenceIds = sampleEvidenceIds;
    }

    public String getSampleSource() {
        return sampleSource;
    }

    public void setSampleSource(String sampleSource) {
        this.sampleSource = sampleSource;
    }

    public String getSampleSourceType() {
        return sampleSourceType;
    }

    public void setSampleSourceType(String sampleSourceType) {
        this.sampleSourceType = sampleSourceType;
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
        return (nodeType + "-" + sampleSourceType);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final SampleSource other = (SampleSource) obj;
        if ((this.sampleSourceType == null) ? (other.sampleSourceType != null) : !this.sampleSourceType.equals(other.sampleSourceType)) {
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