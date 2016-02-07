/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * Connect this to the disease bio entity
 * Named protein that does not have yet a uniprotId associated with it.
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.NAMED_PROTEIN)
public class NamedProtein {

    protected static Logger log = LogManager.getLogger(NamedProtein.class);

    @UniquelyIndexed(indexName=IndexNames.NAMED_PROTEIN_PREFERRED_SYMBOL)
    @Taxonomy(rbClass=TaxonomyTypes.NAMED_PROTEIN_PREFERRED_SYMBOL, rbField= BioFields.NAMED_PROTEIN_PREFERRED_SYMBOL)
    private String namedProteinPreferredSymbol;

    @GraphId
    private Long id;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.NAMED_PROTEIN.toString();

    @NonIndexed
    private String message;

    @Indexed (indexName=IndexNames.MOLECULE_IDREF)
    @Taxonomy (rbClass=TaxonomyTypes.MOLECULE_IDREF, rbField=BioFields.MOLECULE_IDREF)
    private String moleculeIdRef;

   //@RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, startNodeBioType=BioTypes.NCI_PATHWAY, endNodeBioType=BioTypes.NCI_PATHWAY_INTERACTION, elementClass = BioRelation.class)
   @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, elementClass = BioRelation.class)
   private Collection<BioRelation> partOfPathway= new HashSet<BioRelation>();

   /**
     * NamedProtein is member of family of a Protein or PartProtein's
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.CONTAINS, elementClass = FamilyMemberRelation.class)
    private Collection<FamilyMemberRelation> containsFamilyMembers = new HashSet<FamilyMemberRelation>();

     /**
     * <code>this</code> Protein object is related to other Protein objects with an outgoing
     * relationship called <code><PtmExpressionRelation></code>
     * <code>this</code> Protein PTMExpression is of another Protein
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_PTM_EXPRESSION_OF, elementClass=PtmExpressionRelation.class)
    private Collection<PtmExpressionRelation> nciPtmExpressionRelations = new HashSet<PtmExpressionRelation>();


    /**
     * In the following, <code>this<code> NamedProtein object is related to pathway object
     * with an outgoing relationship called <code>partOfPathway</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction#BOTH}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#NAMED_PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#NAMED_PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param partPathway
     */
   public void setPartofPathway(Collection<BioRelation> partPathway) {
       this.partOfPathway = partPathway;
   }

   /**
     * In the following, <code>this<code> NamedProtein object is related to pathway object
     * with an outgoing relationship called <code>partOfPathway</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction#BOTH}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#NAMED_PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @return Collection<BioRelation>
     */
   public Collection<BioRelation> getPartofPathway() {
       return partOfPathway;
   }

   /**
     * In the following, <code>this<code> NamedProtein contains one or more protein objects
     * with an outgoing relationship called <code>contains</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction#OUTGOING}
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
     * @param contains
     */
   public void setContains(Collection<FamilyMemberRelation> contains) {
       this.containsFamilyMembers = contains;
       /**
        * Extend this code to do the relationship pass later
        */
       /*
       for (FamilyMemberRelation relation : containsFamilyMembers) {
            updateEndNodeMemberOfFamily(relation);
        } */
   }

   public void setFamilyMemberRelation(FamilyMemberRelation relation) {
       this.containsFamilyMembers.add(relation);
   }

   /*
    * Returns relationship collection of family member list
    * This NamedProtein contains one or more protein objects.
    * In the following, <code>this<code> NamedProtein contains one or more protein objects
     * with an outgoing relationship called <code>containsFamilyMembers</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction#OUTGOING}
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
    */
   public Iterable<FamilyMemberRelation> getContains() {
       return this.containsFamilyMembers;
   }

    /**
     * This is called either when a Collection<BioRelation> is updated or
     * just a BioRelation is updated.
     * It updates EndNode relationship for the BioRelation
     * It updates IS_MEMBER_OF_FAMILY relation property
     *
     * @param relation
     * @throws java.lang.NoSuchMethodException
     * @throws java.lang.IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
     */
   public void updateEndNodeMemberOfFamily(FamilyMemberRelation relation) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
       Object startNode = relation.getStartNode();
       Object endNode = relation.getEndNode();
       log.info("endNodeType =" + BioTypes.fromString(endNode.getClass().getSimpleName()));
       log.info("startNodeType =" + BioTypes.fromString(startNode.getClass().getSimpleName()) + " moleculeIdRef=" + startNode.toString());

       /*
       if ((!relation.isValid(BioEntityClasses.NAMED_PROTEIN, BioEntityClasses.PROTEIN)) &&
            (!relation.isValid(BioEntityClasses.NAMED_PROTEIN, BioEntityClasses.PART_PROTEIN))) {
           throw new RuntimeException("The BioRelation is invalid. Check the startNode and endNode");
        } */

        /**
         * The EndNode is the startNode for this relationship
         */
        Object pNode = (Object)relation.getEndNode();
        Class tClass = pNode.getClass();
        FamilyMemberRelation famRelation = new FamilyMemberRelation(pNode, startNode, BioRelTypes.IS_MEMBER_OF_FAMILY);
        String methodName = "setMemberOfProteinFamily";
        Method method = tClass.getDeclaredMethod(methodName, FamilyMemberRelation.class);
        if (pNode != null) {
            Object invoke = method.invoke(famRelation);
        }

   }

        /*
        if (relation.isValid(BioEntityClasses.NAMED_PROTEIN, BioEntityClasses.PROTEIN)) {
           Protein pNode = (Protein)relation.getEndNode();
           pNode.setMemberOfProteinFamily(famRelation);
        } else if (relation.isValid(BioEntityClasses.NAMED_PROTEIN, BioEntityClasses.PART_PROTEIN)) {
           PartProtein pNode = (PartProtein)relation.getEndNode();
           FamilyMemberRelation famRelation = new FamilyMemberRelation(pNode, startNode, BioRelTypes.IS_MEMBER_OF_FAMILY);
           pNode.setMemberOfProteinFamily(famRelation);
        } *
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
     */
    public PtmExpressionRelation setPtmExpressionRelation(Object protein, String id, String pos, String aa, String modification) {
        final PtmExpressionRelation ptmRelation =
                new PtmExpressionRelation(this, protein, BioRelTypes.IS_PTM_EXPRESSION_OF,
                        id, pos, aa, modification);
        nciPtmExpressionRelations.add(ptmRelation);
        return ptmRelation;
    }

    /**
     * {@link PtmExpressionRelation}
     * @return nciPtmExpressionRelations
     */
    public Iterable<PtmExpressionRelation> getPtmExpressionRelation() {
        log.info("Complex: length of nciPtmExpressionRelations = " + nciPtmExpressionRelations.size());
        return nciPtmExpressionRelations;
    }

    /**
    *
    * @param pf
    * @param nodeType
    * @param message
    */
    public NamedProtein(String pf, String nodeType, String message) {
        this.namedProteinPreferredSymbol = pf;
        this.nodeType = nodeType;
        this.message = message;
    }


    public NamedProtein() {}

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
     *
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
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }


    /**
     * {@link FullTextIndexed} {@link IndexNames#NAMED_PROTEIN_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#NAMED_PROTEIN_PREFERRED_SYMBOL} {@link BioFields#NAMED_PROTEIN_PREFERRED_SYMBOL}
     *
     * @return  String
     */
    public String getNamedProteinPreferredSymbol() {
        return namedProteinPreferredSymbol;
    }

     /**
     * {@link FullTextIndexed} {@link IndexNames#NAMED_PROTEIN_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#NAMED_PROTEIN_PREFERRED_SYMBOL} {@link BioFields#NAMED_PROTEIN_PREFERRED_SYMBOL}
     *
     * @param ps */
    public void setNamedProteinPreferredSymbol(String ps) {
        this.namedProteinPreferredSymbol = ps;
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
        return (nodeType +  "-" + namedProteinPreferredSymbol);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final NamedProtein other = (NamedProtein) obj;
        if ((this.namedProteinPreferredSymbol == null) ? (other.namedProteinPreferredSymbol != null) : !this.namedProteinPreferredSymbol.equals(other.namedProteinPreferredSymbol)) {
            return false;
        }
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
