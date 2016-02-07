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
 *    obofoundry.org
 * human-dev-anat-abstract2.obo 
 * CS - Carnegie Stage
 * 
 * Relationships: http://www.geneontology.org/GO.ontology-ext.relations.shtml
 * These three reltypes are transitive
 * {@link BioRelTypes#HAS_PART} {@link BioRelTypes#PART_OF} {@link BioRelTypes#DEVELOPS_FROM}
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.CS)
public class CS {
    	
    protected static Logger log = LogManager.getLogger(CS.class);
   
    @GraphId
    private Long id;
   
    /**
     * id: EHDAA2:0000003
     */
    @UniquelyIndexed(indexName=IndexNames.CS_ID)
    @Taxonomy(rbClass=TaxonomyTypes.CS_ID, rbField=BioFields.CS_ID)
    private String csId;
    
    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.CS.toString();
   
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE) 
    public String message;
   
    
    /**
     * name: human conceptus extraembryonic component
     */
    @FullTextIndexed (indexName=IndexNames.CS_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.CS_NAME, rbField=BioFields.CS_NAME)
    private String csName;    
     
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
    private Collection<BioRelation> csRelationship;
   
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

    public String getCsId() {
        return csId;
    }

    public void setCsId(String CSId) {
        this.csId = CSId;
    }

    public String getCsName() {
        return csName;
    }

    public void setCsName(String str) {
        this.csName = str;
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
        return (nodeType + "-" + name + "-" + csId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final CS other = (CS) obj;
        if ((this.csId == null) ? (other.csId != null) : !this.csId.equals(other.csId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }
  
    
    /**
     * <pre>
     * is_a: CS:0 ! Carnegie stage
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
     * relationship: starts_at CS05b ! CS05b
     * </pre>
     * 
     * @param endNode
     * @param relType 
     */
    public void setCsRelationship(Object endNode, BioRelTypes relType) {
        if (csRelationship == null) {
            csRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        csRelationship.add(rel);
    }
    
    public Iterable<BioRelation> getCsRelationship() {
        return csRelationship;
    }
 
    
}
