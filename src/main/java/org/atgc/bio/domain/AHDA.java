/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * obofoundry.org
 * human-dev-anat-abstract2.obo 
 * EHDAA2 - abstract human development anatomy
 * http://www.ontobee.org/browser/index.php?o=EHDAA2
 * 
 * Relationships: http://www.geneontology.org/GO.ontology-ext.relations.shtml
 * These three reltypes are transitive
 * {@link BioRelTypes#HAS_PART} {@link BioRelTypes#PART_OF} {@link BioRelTypes#DEVELOPS_FROM}
 * 
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.AHDA)
public class AHDA {
    	
    protected static Log log = LogFactory.getLog(new Object().getClass());
   
    @GraphId
    private Long id;
   
    /**
     * id: EHDAA2:0000003
     */
    @UniquelyIndexed(indexName=IndexNames.AHDA_ID)
    @Taxonomy(rbClass=TaxonomyTypes.AHDA_ID, rbField=BioFields.AHDA_ID)
    private String ahdaId;
    
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.AHDA.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
   
    
    /**
     * name: human conceptus extraembryonic component
     */
    @FullTextIndexed (indexName=IndexNames.AHDA_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.AHDA_NAME, rbField=BioFields.AHDA_NAME)
    private String ahdaName;    
   
    /**
     * is_obsolete
     */
    @Indexed (indexName=IndexNames.AHDA_IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.AHDA_IS_OBSOLETE, rbField=BioFields.AHDA_IS_OBSOLETE)
    private String ahdaIsObsolete;    
    
    /**
     * synonym: "urogenital sinus superior part" EXACT []
     */
    @FullTextIndexed(indexName=IndexNames.AHDA_EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.AHDA_EXACT_SYNONYMS, rbField=BioFields.AHDA_EXACT_SYNONYMS)
    private String ahdaExactSynonyms;
   
    
    @Visual
    @Indexed (indexName=IndexNames.NAME)
    @Taxonomy (rbClass=TaxonomyTypes.NAME, rbField=BioFields.NAME)
    private String name;
    
     /**
      * <pre>
      * 	{
			"relationship" : "develops_in EHDAA2:0002142 ! urogenital sinus lumen"
		},
		{
			"relationship" : "ends_at CS20 ! CS20"
		},
		{
			"relationship" : "located_in EHDAA2:0002142 ! urogenital sinus lumen"
		},

      * </pre>
      */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> AHDARelationship;
   
    /**
     * <pre>
     * is_a: AEO:0000169 ! embryo
     * is_a: CARO:0000041 ! anatomical cluster
     * </pre>
     * BioRelTypes = is_a
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> isARelationship;
     
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getAhdaId() {
        return ahdaId;
    }

    public void setAhdaId(String AHDAId) {
        this.ahdaId = AHDAId;
    }

    public String getAhdaName() {
        return ahdaName;
    }

    public void setAhdaName(String str) {
        this.ahdaName = str;
    }
    
    public String getAhdaIsObsolete() {
        return ahdaIsObsolete;
    }

    public void setAhdaIsObsolete(String str) {
        this.ahdaIsObsolete = str;
    }

    public String getAhdaExactSynonyms() {
        return ahdaExactSynonyms;
    }

    public void setAhdaExactSynonyms(String ahdaSynonyms) {
        this.ahdaExactSynonyms = ahdaSynonyms;
    }
       
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }
   
    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }
        
    @Override
    public String toString() {
        return (nodeType + "-" + name + "-" + ahdaId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final AHDA other = (AHDA) obj;
        if ((this.ahdaId == null) ? (other.ahdaId != null) : !this.ahdaId.equals(other.ahdaId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }
  
    
    /**
     * <pre>
     * "isList" : [
		{
			"is_a" : "AHDA:0000073 ! unilaminar epithelium"
		}
	],
     * </pre>
     *
     * </pre>
     * @param endNode
     * @param relType 
     */
    public void setIsARelationship(Object endNode, BioRelTypes relType) {
        if (isARelationship == null) {
            isARelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        isARelationship.add(rel);
    }

    public Iterable<BioRelation> getIsARelationship() {
        return isARelationship;
    }
    
    /**
     * <pre>
     * 
     * </pre>
     * 
     * @param endNode
     * @param relType 
     */
    public void setAHDARelationship(Object endNode, BioRelTypes relType) {
        if (AHDARelationship == null) {
            AHDARelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        AHDARelationship.add(rel);
    }
    
    public Iterable<BioRelation> getAHDARelationship() {
        return AHDARelationship;
    }
 
    
}
