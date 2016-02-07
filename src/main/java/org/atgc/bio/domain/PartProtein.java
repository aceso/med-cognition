/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 *
 * PartProtein
 *   <Molecule molecule_type="protein" id="201803">
 *   <Name name_type="UP" long_name_type="UniProt" value="P63027" />
 *   <Name name_type="PF" long_name_type="preferred symbol" value="VAMP2 fragment 1" />
 *   <Part whole_molecule_idref="200405" part_molecule_idref="201803" start="1" end="20" />
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.PART_PROTEIN)
public class PartProtein {

    protected static Logger log = LogManager.getLogger(PartProtein.class);

    @UniquelyIndexed (indexName=IndexNames.PART_PROTEIN_PREFERRED_SYMBOL)
    @Taxonomy(rbClass=TaxonomyTypes.PART_PROTEIN_PREFERRED_SYMBOL, rbField=BioFields.PART_PROTEIN_PREFERRED_SYMBOL)
    private String partProteinPreferredSymbol;

    @GraphId
    private Long id;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.PART_PROTEIN.toString();

    @Indexed (indexName=IndexNames.UNIPROT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.UNIPROT_ID, rbField=BioFields.UNIPROT_ID)
    private String uniprot;

    @NonIndexed
    private String message;

    @Indexed (indexName=IndexNames.MOLECULE_IDREF)
    @Taxonomy (rbClass=TaxonomyTypes.MOLECULE_IDREF, rbField=BioFields.MOLECULE_IDREF)
    private String moleculeIdRef;

   /*
   @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, elementClass = BioRelation.class)
   private Collection<BioRelation> partOfPathway= new HashSet<BioRelation>();
   */

   /**
     * PartProtein
     *  Its is an outgoing relationship of a <code>PartProtein<code> with <code>NamedProtein</code>
     *  belongs to <code>this<code> protein family
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_MEMBER_OF_FAMILY, elementClass = FamilyMemberRelation.class)
    private Collection<FamilyMemberRelation> memberOfProteinFamily = new HashSet<FamilyMemberRelation>();


    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_PART_OF_PROTEIN, elementClass = PartMoleculeRelation.class)
    private Collection<PartMoleculeRelation> nciPartMoleculeRelations = new HashSet<PartMoleculeRelation>();

     /**
     * <code>this</code> Protein object is related to other Protein objects with an outgoing
     * relationship called <code><PtmExpressionRelation></code>
     * <code>this</code> Protein PTMExpression is of another Protein
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_PTM_EXPRESSION_OF, elementClass=PtmExpressionRelation.class)
    private Collection<PtmExpressionRelation> partPtmExpressionRelations = new HashSet<PtmExpressionRelation>();

    /**
     * <code>this</code>
     * Complex object is related to other PartProtein and Protein objects with an outgoing
     * relationship called <code><ComplexComponentRelation></code>
     * <code>this</code> Complex is contains other {@link BioEntity}
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_A_COMPONENT_OF, elementClass=ComplexComponentRelation.class)
    private Collection<ComplexComponentRelation> complexComponentRelations = new HashSet<ComplexComponentRelation>();

     /**
     * {@link ComplexComponentRelation}
     * {@link PartProtein}
     * {@link BioEntity}
     * @param complex
     */
    public void setComplexComponentRelations(Complex complex) {
        final ComplexComponentRelation relation =
                new ComplexComponentRelation(this, complex, BioRelTypes.IS_A_COMPONENT_OF);
        complexComponentRelations.add(relation);
    }

     /**
     * {@link ComplexComponentRelation}
     *
     * @return Iterable<ComplexComponentRelation> complexComponentRelations
     */
    public Iterable<ComplexComponentRelation> getComplexComponentRelation() {
        log.info("Complex: length of complexComponentRelations = " + complexComponentRelations.size());
        return complexComponentRelations;
    }

    /**
     * setComplexComponentLabels
     * @param complex {@link Complex}
     * @param map  {@link java.util.Map}
     */
    public void setComplexComponentLabels(Complex complex, Map map) {
        final ComplexComponentRelation relation =
                new ComplexComponentRelation(this, complex, BioRelTypes.IS_A_COMPONENT_OF, map);
        complexComponentRelations.add(relation);
    }

     /**
     * Part of this molecule consists of another protein which is a whole molecule.
     * Some proteins can be part of multiple molecules.
     * These are identified in the existing protein with start and end for each such protein
     *
     * "Part" : {
     *              "@whole_molecule_idref" : "200436",
     *              "@part_molecule_idref" : "202926",
     *              "@start" : "701",
     *              "@end" : "882"
     *          }
     *
     * @param protein
     * @param start
     * @param end
     */
    public void setPartOfMolecule(Object protein, String start, String end) {
       final PartMoleculeRelation partRelation = new PartMoleculeRelation(this, protein, BioRelTypes.IS_PART_OF_PROTEIN, start, end);
       nciPartMoleculeRelations.add(partRelation);
    }

    public Iterable<PartMoleculeRelation> getNciPartMoleculeRelations() {
        log.info("length of nciPartMoleculeRelations = " + nciPartMoleculeRelations.size());
        return nciPartMoleculeRelations;
    }

    /**
     * In the following, <code>NamedProtein<code> Protein object is related to <code>this<code> object
     * with an outgoing relationship called <code>memberOfProteinFamily</code>.
     * <p>
     * {@link RelatedTo} {@link org.neo4j.graphdb.Direction#OUTGOING}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#NAMED_PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param proteinFamilyRelation
     */
    public void setMemberOfProteinFamily(FamilyMemberRelation proteinFamilyRelation) {
        if (!proteinFamilyRelation.isValid(BioEntityClasses.PART_PROTEIN, BioEntityClasses.NAMED_PROTEIN)) {
           throw new RuntimeException("proteinFamilyBioRelation is invalid. Check the startNode and endNode");
        }
        this.memberOfProteinFamily.add(proteinFamilyRelation);
    }

     /**
     * In the following, <code>NamedProtein<code> Protein object is related to <code>this<code> object
     * with an outgoing relationship called <code>memberOfProteinFamily</code>.
     * <p>
     * {@link RelatedTo} {@link org.neo4j.graphdb.Direction#OUTGOING}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PART_PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#NAMED_PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @return Iterable<FamilyMemberRelation> BioRelation collection of memberOfProteinFamily
     */
    public Iterable <FamilyMemberRelation> getMemberOfProteinFamily() {
        return this.memberOfProteinFamily;
    }

    /**
     * Sets PTM expression relationship
     * This expression can be part of any molecule
     *  {@link Protein}
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
     * @param protein {@link Protein}
     * @param id {@link PtmExpressionRelation#id}
     * @param pos {@link PtmExpressionRelation#position}
     * @param aa {@link PtmExpressionRelation#aa}
     * @param modification {@link PtmExpressionRelation#modification}
     * @return PtmExpressionRelation
     */
    public PtmExpressionRelation setPartPtmExpressionRelation(Protein protein, String id, String pos, String aa, String modification) {
        final PtmExpressionRelation ptmRelation =
                new PtmExpressionRelation(this, protein, BioRelTypes.IS_PTM_EXPRESSION_OF,
                        id, pos, aa, modification);
        partPtmExpressionRelations.add(ptmRelation);
        return ptmRelation;
    }

    /**
     * {@link PtmExpressionRelation}
     * @return Iterable<PtmExpressionRelation> nciPtmExpressionRelations
     */
    public Iterable<PtmExpressionRelation> getPartPtmExpressionRelation() {
        log.info("Complex: length of partPtmExpressionRelations = " + partPtmExpressionRelations.size());
        return partPtmExpressionRelations;
    }

    /**
    *
    * @param pf
    * @param nodeType
    * @param message
    */
    public PartProtein(String pf, String nodeType, String message) {
        this.partProteinPreferredSymbol = pf;
        this.nodeType = nodeType;
        this.message = message;
    }

    public PartProtein(String moleculeIdRef, BioTypes nodeType) {
        this.moleculeIdRef = moleculeIdRef;
        this.nodeType = nodeType.toString();
    }


    public PartProtein() {}


    public String getUniprot() {
        return this.uniprot;
    }

    public void setUniprot(String uniProt) {
        this.uniprot = uniProt;
    }

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
     * {@link NonIndexed}
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link NonIndexed}
     *
     * @param message */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * {@link FullTextIndexed} {@link IndexNames#NAMED_PROTEIN_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#NAMED_PROTEIN_PREFERRED_SYMBOL} {@link BioFields#NAMED_PROTEIN_PREFERRED_SYMBOL}
     *
     * @param
     */
    //public String getNamedProteinPreferredSymbol() {
//        return namedProteinPreferredSymbol;
   // }

     /**
     * {@link FullTextIndexed} {@link IndexNames#NAMED_PROTEIN_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#NAMED_PROTEIN_PREFERRED_SYMBOL} {@link BioFields#NAMED_PROTEIN_PREFERRED_SYMBOL}
     *
     * @param ps */
    public void setPartProteinPreferredSymbol(String ps) {
        this.partProteinPreferredSymbol = ps;
    }

    /**
     * getPartProteinPreferredSymbol
     * @return String - partProteinPreferredSymbol
     */
    public String getPartProteinPreferredSymbol() {
        return partProteinPreferredSymbol;
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
        return (nodeType + "-" + moleculeIdRef + "-" + partProteinPreferredSymbol);
       // return (nodeType +  "-" + namedProteinPreferredSymbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final PartProtein other = (PartProtein) obj;
        return !((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType));
    }

    /*
     * Protein that does not have UniProtID
     *

    {
                                "@molecule_type" : "protein",
                                "@id" : "200110",
                                "Name" : {
                                        "@name_type" : "PF",
                                        "@long_name_type" : "preferred symbol",
                                        "@value" : "14-3-3 family"
                                },
                                "FamilyMemberList" : [
                                        {
                                                "@member_molecule_idref" : "201465"
                                        },
                                        {
                                                "@member_molecule_idref" : "201076"
                                        },
                                        {
                                                "@member_molecule_idref" : "201466"
                                        },                                        }
                                ]
                        },


       */

}
