/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 *
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.RNA)
@SuppressWarnings("javadoc")
public class Rna {

    protected static Logger log = LogManager.getLogger(Rna.class);

    @GraphId
    private Long id;

    @UniquelyIndexed (indexName=IndexNames.RNA_PREFERRED_SYMBOL)
    @Taxonomy(rbClass=TaxonomyTypes.RNA_PREFERRED_SYMBOL, rbField=BioFields.RNA_PREFERRED_SYMBOL)
    private String rnaPreferredSymbol;

    @FullTextIndexed(indexName=IndexNames.ENTREZ_GENE_ID)
    @Taxonomy (rbClass=TaxonomyTypes.ENTREZ_GENE_ID, rbField=BioFields.ENTREZ_GENE_ID)
    private String entrezGeneId;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.RNA.toString();

    @NonIndexed
    private String message;

    @Indexed (indexName=IndexNames.MOLECULE_IDREF)
    @Taxonomy (rbClass=TaxonomyTypes.MOLECULE_IDREF, rbField=BioFields.MOLECULE_IDREF)
    private String moleculeIdRef;

    @RelatedToVia(direction=Direction.INCOMING, relType=BioRelTypes.AN_INTERACTION, elementClass = OncoRelation.class)
    private Collection<OncoRelation> interactionComponents;

  //  @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, startNodeBioType=BioTypes.PATHWAY, endNodeBioType=BioTypes.NCI_PATHWAY_INTERACTION, elementClass = BioRelation.class)
  // private Collection<BioRelation> pathwayInteractions = new HashSet<BioRelation>();

    /**
     *
     * @param cId
     * @param nodeType
     * @param message
     */
    public Rna(String cId, String nodeType, String message) {
        this.nodeType = nodeType;
        this.message = message;
    }

     /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE
     * @param nodeType
     */
    public void setNodeType(BioTypes nodeType) { this.nodeType = nodeType.toString(); }

    /**
     *
     * This method not only sets the InteractionComponentList as a
     * collection of <code>BioRelation</code> objects, but also sets the
     * INCOMING relationship on the initialing end can be any type of molecule.
     * Molecule could be Complex, Rna, Protein
     *
     * "InteractionComponentList"  : [
     * {
     *    "@role_type: "input",
     *    "@molecule_idref" : "213740"
     * },
     * {
     *    "@role_type" : "input",
     *    "@molecule_idref" : "200579",
     *    "Label" : {
     *        "@label_type"  : "location",
     *        "@value" : "cytoplasm""
     *     }
     * }
     *
     * ]
     *
     * * {@link RelatedToVia} {@link Direction#INCOMING}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#COMPOUND} or {@link BioTypes#RNA} or {@link BioTypes#PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#NCI_PATHWAY_INTERACTION}
     * </LI>
     * </DL>
     * elementClass {@link BioRelation}
     * @param interactionId
     * @param startNodeRole - roles {@link OncologyRoles}
     * @param endNodeRole - EndNode role {@link OncologyRoles}
     * @param startNode
     * @param endNode - NciPathwayInteraction/RNA/Protein/Complex
     * @return  OncoRelation
     *
     */
    public OncoRelation interactsWith(
                          String interactionId,
                          String startNodeRole, String endNodeRole,
                          Object startNode, Object endNode) {

        OncoRelation interactor = new OncoRelation(interactionId,
                          startNodeRole, endNodeRole,
                          startNode, endNode, BioRelTypes.INTERACTS_WITH);

        interactionComponents.add(interactor);
        log.info("interactorComponens size = " + interactionComponents.size());
        return interactor;
    }

    /**
     *
     */
    public Rna() {}

    /**
     *
     * @return Long
     */
    public Long getId() {
    	return id;
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
     * {@link NonIndexed} {@link NonIndexed}
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link NonIndexed} {@link NonIndexed}
     * @param message */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * {@link UniquelyIndexed} {@link IndexNames#RNA_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#RNA_PREFERRED_SYMBOL} {@link BioFields#RNA_PREFERRED_SYMBOL}
     *
     * @return String
     */
    public String getRnaPreferredSymbol() {
        return rnaPreferredSymbol;
    }

     /**
     * {@link UniquelyIndexed} {@link IndexNames#RNA_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#RNA_PREFERRED_SYMBOL} {@link BioFields#RNA_PREFERRED_SYMBOL}
     *
     * @param ps */
    public void setRnaPreferredSymbol(String ps) {
        this.rnaPreferredSymbol = ps;
    }

     /**
     * {@link FullTextIndexed} {@link IndexNames#ENTREZ_GENE_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ENTREZ_GENE_ID} {@link BioFields#ENTREZ_GENE_ID}
     *
     * @return  String
     */
    public String getEntrezGeneId() {
        return entrezGeneId;
    }

     /**
     * {@link FullTextIndexed} {@link IndexNames#ENTREZ_GENE_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ENTREZ_GENE_ID} {@link BioFields#ENTREZ_GENE_ID}
     *
     * @param id */
    public void setEntrezGeneId(String id) {
        this.entrezGeneId = id;
    }

    /**
     * internal id reference
     * <p>
     * {@link Indexed} {@link IndexNames#MOLECULE_IDREF}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#MOLECULE_IDREF} {@link BioFields#MOLECULE_IDREF}
     *
     * @return String
     */
    public String getMoleculeIdRef() {
        return moleculeIdRef;
    }

    /**
     * <p>
     * {@link Indexed} {@link IndexNames#MOLECULE_IDREF}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#MOLECULE_IDREF} {@link BioFields#MOLECULE_IDREF}
     *
     * @param moleculeIdRef
     */
    public void setMoleculeIdRef(String moleculeIdRef) {
        this.moleculeIdRef = moleculeIdRef;
    }


    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + "-" + rnaPreferredSymbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Rna other = (Rna) obj;
        if ((this.rnaPreferredSymbol == null) ? (other.rnaPreferredSymbol != null) : !this.rnaPreferredSymbol.equals(other.rnaPreferredSymbol)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }

}
