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
 * http://www.geneontology.org/GO.doc.shtml
 * http://www.geneontology.org/GO.ontology.structure.shtml
 *
 * http://www.geneontology.org/GO.format.obo-1_2.shtml#S.1.1
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * Connect this to the disease bioentity
 * Gene ontology ids like GO:0009941 can be stored here.
 *
 * > db.geneontology.findOne({"id":"GO:0009941"})
{
	"_id" : ObjectId("5193d5bc036486993c00db1f"),
	"id" : "GO:0009941",
	"name" : "chloroplast envelope",
	"namespace" : "cellular_component",
	"def" : "\"The double lipid bilayer enclosing the chloroplast and separating its contents from the rest of the cytoplasm; includes the intermembrane space.\" [GOC:tb]",
	"isList" : [
		{
			"is_a" : "GO:0009526 ! plastid envelope"
		},
		{
			"is_a" : "GO:0044434 ! chloroplast part"
		}
	]
}

 *
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.GENE_ONTOLOGY)
public class GeneOntology {

    protected static Logger log = LogManager.getLogger(GeneOntology.class);

    @GraphId
    private Long id;

    /**
     * For instance GO:0009941 as found in the geneontology collection.
     * It is the "id" field in the geneontology collection.
     */
    @UniquelyIndexed (indexName=IndexNames.GENE_ONTOLOGY_ID)
    @Taxonomy(rbClass=TaxonomyTypes.GENE_ONTOLOGY_ID, rbField=BioFields.GENE_ONTOLOGY_ID)
    private String geneOntologyId;

    @Indexed(indexName=IndexNames.GENE_ONTOLOGY_ALT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_ONTOLOGY_ALT_ID, rbField=BioFields.GENE_ONTOLOGY_ALT_ID)
    private String geneOntologyAltId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.GENE_ONTOLOGY.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    public String message;

    /**
     * TODO: Do not know what is comment. Please research this.
     */
    @FullTextIndexed(indexName=IndexNames.COMMENT)
    @Taxonomy (rbClass=TaxonomyTypes.COMMENT, rbField=BioFields.COMMENT)
    private String comment;

    /**
     * TODO: Need to research this.
     * name
     *   The term name. Any term may have only one name defined.
     *   If multiple term names are defined, it is a parse error.
     *
     *   name" : "maltose metabolic process",
     */
    @FullTextIndexed (indexName=IndexNames.TERM_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.TERM_NAME, rbField=BioFields.TERM_NAME)
    private String termName;

    /**
     * A comma separated list of synonyms.
     */
    @FullTextIndexed(indexName=IndexNames.EXACT_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.EXACT_SYNONYMS, rbField=BioFields.EXACT_SYNONYMS)
    private String exactSynonyms;

    @FullTextIndexed(indexName=IndexNames.BROAD_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.BROAD_SYNONYMS, rbField=BioFields.BROAD_SYNONYMS)
    private String broadSynonyms;

    @FullTextIndexed(indexName=IndexNames.NARROW_SYNONYMS)
    @Taxonomy (rbClass=TaxonomyTypes.NARROW_SYNONYMS, rbField=BioFields.NARROW_SYNONYMS)
    private String narrowSynonyms;

    @Indexed(indexName=IndexNames.IS_OBSOLETE)
    @Taxonomy (rbClass=TaxonomyTypes.IS_OBSOLETE, rbField=BioFields.IS_OBSOLETE)
    private String isObsolete;

    @Visual
    @Indexed (indexName=IndexNames.GENE_ONTOLOGY_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_ONTOLOGY_NAME, rbField=BioFields.NAME)
    private String name;


    /**
     * http://www.geneontology.org/GO.component.guidelines.shtml
     * namespace can be cellular_component (location), molecular_function, biological_process
     * "namespace" : "cellular_component"
     */
    @Indexed (indexName=IndexNames.GENE_ONTOLOGY_NAMESPACE)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_ONTOLOGY_NAMESPACE, rbField=BioFields.NAMESPACE)
    private String namespace;

    /**
     * The follow three relationships are used in NCIPathway
     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_LOCATED_AT, elementClass = BioRelation.class)
    private Collection<BioRelation> locationRelation;

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.PROCESS_USED, elementClass = BioRelation.class)
    private Collection<BioRelation> processTypeRelation;

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.CONDITION_TYPE, elementClass = BioRelation.class)
    private Collection<BioRelation> conditionTypeRelation;

    /**
     * http://www.geneontology.org/GO.ontology.relations.shtml
     * relationship type or BioRelTypes can be: part_of, regulates, negatively_regulates
     * positively_regulates, is_a
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> isARelationships;

    /**
     * Is an enzyme
     * is an exact_synonym enzyme, broad_synonym enzyme, precise_synonym enzyme
     */
    @RelatedToVia(direction=Direction.OUTGOING, elementClass=BioRelation.class)
    private Collection<BioRelation> enzymeRelationship;

    /**
     * {@linkplain http://www.geneontology.org/GO.ontology.relations.shtml}
     * newTermRelationships
     * @return Collection<BioRelation>
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.CONSIDER_RELATIONSHIP, elementClass=BioRelation.class)
    private Collection<BioRelation> considerRelationship;

    /**
     * "relationship" : "part_of GO:0007052 ! mitotic spindle organization"
     */
     @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.GO_RELATIONSHIP, elementClass=BioRelation.class)
    private Collection<BioRelation> goRelationship;

    @FullTextIndexed (indexName=IndexNames.GENE_ONTOLOGY_DEF)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_ONTOLOGY_DEF, rbField=BioFields.DEF)
    private String def;

     /**
      * Nci Pathway refers to GO for function, location, process
      */
     @RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.GO_IS_A_COMPLEX, elementClass = BioRelation.class)
     private BioRelation goIsAComplexRelationship;

     /**
      *  "disjoint_from" : "GO:0005575 ! cellular_component"
      */
     @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.DISJOINT_FROM, elementClass = BioRelation.class)
     private Collection<BioRelation> disjointRelationship;


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getGeneOntologyId() {
        return geneOntologyId;
    }

    public void setGeneOntologyId(String geneOntologyId) {
        this.geneOntologyId = geneOntologyId;
    }

    public void setGeneOntologyAltId(String geneOntologyAltId) {
        this.geneOntologyAltId = geneOntologyAltId;
    }

    public String getGeneOntologyAltId() {
        return geneOntologyAltId;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * @return String
     */
    public String getName() {
        return name;
    }

    /**
     * @param name
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getNamespace() {
        return namespace;
    }

    public void setNamespace(String namespace) {
        this.namespace = namespace;
    }

     public String getTermName() {
        return termName;
    }

    public void setTermName(String termName) {
        this.termName = termName;
    }

    public String getIsObsolete() {
        return isObsolete;
    }

    public void setIsObsolete(String isObsolete) {
        this.isObsolete = isObsolete;
    }

    public String getDef() {
        return def;
    }

    public void setDef(String def) {
        this.def = def;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + termName + "-" + geneOntologyId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final GeneOntology other = (GeneOntology) obj;
        if ((this.geneOntologyId == null) ? (other.geneOntologyId != null) : !this.geneOntologyId.equals(other.geneOntologyId)) {
            return false;
        }
        return !((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType));
    }

    /*
    public void setPathwayInteractions(Object endNode) {
        BioRelation rel = new BioRelation(this, endNode, BioRelTypes.IS_PART_OF_PATHWAY);
        pathwayInteractions.add(rel);
    }

    public Iterable<BioRelation> getPathwayInteractions() {
        return pathwayInteractions;
    }
    */
    /**
     * setIsARelationships
     * @param endNode {@link BioEntity} {@link BioTypes#GENE_ONTOLOGY}
     * @param relType {@link BioRelTypes#IS_A}
     *                {@link BioRelTypes#REGULATES}
     *                {@link BioRelTypes#NEGATIVELY_REGULATES}
     *                {@link BioRelTypes#POSITIVELY_REGULATES}
     */
    public void setIsARelationship(Object endNode, BioRelTypes relType) {
        if (isARelationships == null) {
            isARelationships = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, relType);
        isARelationships.add(rel);
    }

    public Iterable<BioRelation> getIsARelationship() {
        return isARelationships;
    }

    /**
     * GeneOntology is an Enzyme that is an exact synonym, broad synonym or precise synonym
     * @param enzymeNode
     * @param relType
     */
    public void setEnzymeRelationship(Object enzymeNode, BioRelTypes relType) {
        if (enzymeRelationship == null) {
            enzymeRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, enzymeNode, relType);
        enzymeRelationship.add(rel);
    }

    public Iterable<BioRelation> getEnzymeRelationship() {
        return enzymeRelationship;
    }

    /**
     * GeneOntology is an Enzyme that is an exact synonym, broad synonym or precise synonym
     * @param newTermGOEntity
     * NewTerm
     */
    public void setConsiderRelationships(Object newTermGOEntity) {
        if (considerRelationship == null) {
            considerRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, newTermGOEntity, BioRelTypes.CONSIDER_RELATIONSHIP);
        considerRelationship.add(rel);
    }

    public Iterable<BioRelation> getConsiderRelationships() {
        return considerRelationship;
    }

    /**
     * "relationship" : "part_of GO:0007052 ! mitotic spindle organization""relationship" : "part_of GO:0007052 ! mitotic spindle organization"
     * @param newTermGOEntity
     * @param relType
     */
    public void setGoRelationship(Object newTermGOEntity, BioRelTypes relType) {
        if (goRelationship == null) {
            goRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, newTermGOEntity, relType);
        goRelationship.add(rel);
    }

    public Iterable<BioRelation> getGoRelationship() {
        return goRelationship;
    }

    public void setDisjointRelationship(Object newTermGOEntity) {
        if (disjointRelationship == null) {
            disjointRelationship = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, newTermGOEntity, BioRelTypes.DISJOINT_FROM);
        disjointRelationship.add(rel);
    }

    public Iterable<BioRelation> getDisjointRelationship() {
        return disjointRelationship;
    }

    public void setLocationRelation(Object endObj) {
        if (locationRelation == null) {
           locationRelation = new HashSet();
         }
         BioRelation locationRelationship = new BioRelation();
         locationRelationship.setEndNode(endObj);
         locationRelationship.setStartNode(this);
         locationRelation.add(locationRelationship);
    }

    public Iterable<BioRelation> getLocationRelation() {
        return locationRelation;
    }

    public void setProcessRelation(Object endObj) {
         if (processTypeRelation == null) {
             processTypeRelation = new HashSet<BioRelation>();
         }
         BioRelation relationship = new BioRelation();
         relationship.setEndNode(endObj);
         relationship.setStartNode(this);
         processTypeRelation.add(relationship);
    }

    public Iterable<BioRelation> getProcessRelation() {
        return processTypeRelation;
    }

    public void setFunctionRelation(Object endObj) {
          if (conditionTypeRelation == null) {
              conditionTypeRelation = new HashSet<BioRelation>();
          }
          BioRelation relationship = new BioRelation();
          relationship.setEndNode(endObj);
          relationship.setStartNode(this);
          conditionTypeRelation.add(relationship);
    }

    public Iterable<BioRelation> getFunctionRelation() {
        return conditionTypeRelation;
    }


    public void setGoIsAComplexRelationship(Object complex) {
         goIsAComplexRelationship = new BioRelation(this, complex, BioRelTypes.GO_IS_A_COMPLEX);
    }

    public BioRelation getGoIsAComplexRelationship() {
        return goIsAComplexRelationship;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }


    public void setExactSynonyms(String s) {
        this.exactSynonyms = s;
    }

    public String getExactSynonyms() {
        return exactSynonyms;
    }

    public void setBroadSynonyms(String s) {
        this.broadSynonyms = s;
    }

    public String getBroadSynonyms() {
        return this.broadSynonyms;
    }

    public void setNarrowSynonyms(String s) {
        this.narrowSynonyms = s;
    }

    public String getNarrowSynonyms() {
        return this.narrowSynonyms;
    }

    /**
     * XRef databases are not stored
     */
}
