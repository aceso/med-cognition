/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 *
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.COMPLEX)
public class Complex {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;

    @UniquelyIndexed(indexName=IndexNames.COMPLEX_PREFERRED_SYMBOL)
    @Taxonomy (rbClass=TaxonomyTypes.COMPLEX_PREFERRED_SYMBOL, rbField=BioFields.COMPLEX_PREFERRED_SYMBOL)
    private String complexPreferredSymbol;

    //@GloballyIndexed
    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.COMPLEX.toString();

    @NonIndexed
    private String message;

    @Indexed (indexName=IndexNames.MOLECULE_IDREF)
    @Taxonomy (rbClass=TaxonomyTypes.MOLECULE_IDREF, rbField=BioFields.MOLECULE_IDREF)
    private String moleculeIdRef;

    @Indexed (indexName=IndexNames.GENE_ONTOLOGY_ID)
    @Taxonomy (rbClass=TaxonomyTypes.GENE_ONTOLOGY_ID, rbField=BioFields.GENE_ONTOLOGY_ID)
    private String geneOntologyId;

    @FullTextIndexed (indexName = IndexNames.COMPLEX_ALIAS)
    @Taxonomy (rbClass=TaxonomyTypes.COMPLEX_ALIAS, rbField=BioFields.ALIASES)
    private String aliases;

    @FullTextIndexed (indexName=IndexNames.LOCATION)
    @Taxonomy (rbClass=TaxonomyTypes.LOCATION, rbField=BioFields.LOCATION)
    private String location;

    @FullTextIndexed (indexName=IndexNames.ACTIVITY_STATE)
    @Taxonomy (rbClass=TaxonomyTypes.ACTIVITY_STATE, rbField=BioFields.ACTIVITY_STATE)
    private String activityState;

    @FullTextIndexed (indexName=IndexNames.FUNCTION)
    @Taxonomy (rbClass=TaxonomyTypes.FUNCTION, rbField=BioFields.FUNCTION)
    private String function;

        /**
     * {@link FullTextIndexed} {@link IndexNames#LOCATION}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#LOCATION} {@link BioFields#LOCATION}
     * @return String proteinLocation
    */
    public String getLocation() {
        return location;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#LOCATION}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#LOCATION} {@link BioFields#LOCATION}
     * @param proteinLocation
     */
    public void setLocation(String proteinLocation) {
        this.location = proteinLocation;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#ACTIVITY_STATE}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ACTIVITY_STATE} {@link BioFields#ACTIVITY_STATE}
     * @param activityState
     */
    public void setActivityState(String activityState) {
        this.activityState = activityState;
    }


    /**
     * {@link FullTextIndexed} {@link IndexNames#ACTIVITY_STATE}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#ACTIVITY_STATE} {@link BioFields#ACTIVITY_STATE}
     * @return String
     */
    public String getActivityState() {
        return this.activityState;
    }


    /**
     * {@link FullTextIndexed} {@link IndexNames#FUNCTION}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FUNCTION} {@link BioFields#FUNCTION}
     * @param function
     */
    public void setFunction(String function) {
        this.function = function;
    }

     /**
     * {@link FullTextIndexed} {@link IndexNames#FUNCTION}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#FUNCTION} {@link BioFields#FUNCTION}
     * @return String
     */
    public String getFunction() {
        return this.function;
    }


     /**
     * <code>this</code> Protein object is related to other Protein objects with an outgoing
     * relationship called <code><PtmExpressionRelation></code>
     * <code>this</code> Protein PTMExpression is of another Protein
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_PTM_EXPRESSION_OF, elementClass=PtmExpressionRelation.class)
    private Collection<PtmExpressionRelation> nciPtmExpressionRelations = new HashSet<PtmExpressionRelation>();

    /**
     * <code>this</code> Complex object is related to other Protein objects with an outgoing
     * relationship called <code><ComplexComponentRelation></code>
     * <code>this</code> Complex is contains other {@link BioEntity}
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.CONTAINS, elementClass=ComplexComponentRelation.class)
    private Collection<ComplexComponentRelation> complexComponentRelations = new HashSet<ComplexComponentRelation>();

    /**
     * Sets ptm expression relationship
     * This expression can be part of any molecule either
     *  {@link Complex}  or {@link Protein}
     *
     * The molecule_idref refers to {@link Protein} molecule
     *
     *  "@molecule_idref" : "200425",
     * "PTMExpression" : [
     *           {
     *                   "@protein" : "P16144",
     *                   "@position" : "0",
     *                   "@aa" : "S",
     *                   "@modification" : "phosphorylation"
     *          }
     *
     * @param bioEntity
     * @param id {@link PtmExpressionRelation#id}
     * @param pos {@link PtmExpressionRelation#position}
     * @param aa {@link PtmExpressionRelation#aa}
     * @param modification {@link PtmExpressionRelation#modification}
     * @return PtmExpressionRelation
     */
    public PtmExpressionRelation setPtmExpressionRelation(Object bioEntity, String id, String pos, String aa, String modification) {
        final PtmExpressionRelation ptmRelation =
                new PtmExpressionRelation(this, bioEntity, BioRelTypes.IS_PTM_EXPRESSION_OF,
                        id, pos, aa, modification);
        nciPtmExpressionRelations.add(ptmRelation);
        return ptmRelation;
    }



    /**
     * {@link PtmExpressionRelation}
     * @return Iterable<nciPtmExpressionRelations>
     */
    public Iterable<PtmExpressionRelation> getPtmExpressionRelation() {
        log.info("Complex: length of nciPtmExpressionRelations = " + nciPtmExpressionRelations.size());
        return nciPtmExpressionRelations;
    }

   /**
     * @param bioEntity {@link Protein} {@link PartProtein}
     */
    public void setComplexComponentRelations(BioEntity bioEntity) {
        final ComplexComponentRelation relation =
                new ComplexComponentRelation(this, bioEntity, BioRelTypes.CONTAINS);
        complexComponentRelations.add(relation);
    }

    /**
     *
     * @param bioEntity {@link BioEntity} {@link Protein} {@link PartProtein}
     * @param map
     */
    public void setComplexComponentLabels(Object bioEntity, Map map) {
         final ComplexComponentRelation relation =
                new ComplexComponentRelation(this, bioEntity, BioRelTypes.CONTAINS, map);
        complexComponentRelations.add(relation);

    }

     /**
     * {@link ComplexComponentRelation}
     * @return Iterable<ComplexComponentRelations>
     */
    public Iterable<ComplexComponentRelation> getComplexComponentRelation() {
        log.info("Complex: length of complexComponentRelations = " + complexComponentRelations.size());
        return complexComponentRelations;
    }

    /**
     * Complex
     */
    public Complex() {}

    /**
     *
     * @return Complex
     */
    public Complex Complex() {
        Complex complex = new Complex();
        complex.nodeType = BioTypes.COMPLEX.toString();
        return complex;
    }
    /**
     *
     * @param nodeType {@link BioTypes}
     * @param message
     */
    public Complex(BioTypes nodeType, String message) {
        this.nodeType = nodeType.toString();
        this.message = message;
    }

    /*
    public BioRelation interactsWith(Compound compound, String name) {
        final BioRelation compoundInteraction = new BioRelation(this, compound, BioRelTypes.I);
        compoundInteraction.setName(name);
        compoundInteractions.add(compoundInteraction);
        log.info("compoundInteractions size = " + compoundInteractions.size());
        return compoundInteraction;
    } */

    /**
     *
     * @return Long
     */


    public Long getId() {
    	return id;
    }


    /**
    * &#64;Indexed (indexName=IndexNames.NODE_TYPE)
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
    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType.toString();
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
     * {@link UniquelyIndexed} {@link IndexNames#COMPLEX_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#COMPLEX_PREFERRED_SYMBOL} {@link BioFields#COMPLEX_PREFERRED_SYMBOL}
     *
     * @return String */
    public String getComplexPreferredSymbol() {
        return complexPreferredSymbol;
    }

     /**
     * {@link UniquelyIndexed} {@link IndexNames#COMPLEX_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#COMPLEX_PREFERRED_SYMBOL} {@link BioFields#COMPLEX_PREFERRED_SYMBOL}
     *
     * @param ps */
    public void setComplexPreferredSymbol(String ps) {
        this.complexPreferredSymbol = ps;
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


    /**
     * GO identifier
     * @param id
     */
    public void setGeneOntologyId(String id) {
        this.geneOntologyId = id;
    }

    /**
     *  {
        "@name_type" : "GO",
        "@long_name_type" : "Gene Ontology",
        "@value" : "0035030"
        }
     *
     * {@link BioFields#GENE_ONTOLOGY_ID}
     * @return String geneOntologyId
     */
    public String getGeneOntologyId() {
        return geneOntologyId;
    }

      /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#COMPLEX_ALIAS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#COMPLEX_ALIAS} {@link BioFields#ALIAS}
     *
     * @return String
     */
    public String getAliases() {
        return aliases;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#COMPLEX_ALIAS}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#COMPLEX_ALIAS} {@link BioFields#ALIAS}
     *
     * @param aliases
     */
    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + "-" + complexPreferredSymbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Complex other = (Complex) obj;
        if ((this.complexPreferredSymbol == null) ? (other.complexPreferredSymbol != null) : !this.complexPreferredSymbol.equals(other.complexPreferredSymbol)) {
            return false;
        }
        return !((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType));
    }

}
