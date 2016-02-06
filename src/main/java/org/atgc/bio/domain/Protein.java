/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.FullTextIndexed;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.repository.TemplateUtils;
/*
 import org.atgc.bio.meta.BioRelation;
 import FullTextIndexed;
 import Taxonomy;
 import UniquelyIndexed;
 import Indexed;
 import NonIndexed;
 */
import java.util.Collection;
import java.util.HashSet;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * cell death protease (Ced-3), which is associated with activating (Ced-4) and
 * inhibitory (Ced-9) proteins. Homologous proteins have been found in organisms
 * throughout the animal kingdom, including insects, amphibians, and humans.
 * Ced-3 is ?the founding member? of the caspase family of cysteine proteases,
 * and overexpression of these proteins results in cell death with apoptotic
 * morphology in a variety of organisms. cell death protease (Ced-3), which is
 * associated with activating (Ced-4) and inhibitory (Ced-9) proteins.
 * Homologous proteins have been found in organisms throughout the animal
 * kingdom, including insects, amphibians, and humans. Ced-3 is ?the founding
 * member? of the caspase family of cysteine proteases, and overexpression of
 * these proteins results in cell death with apoptotic morphology in a variety
 * of organisms.
 */
/**
 * Proteins are functional entities composed primarily of one or more
 * polypeptides and often non-polypeptide cofactors. A protein without
 * its co-factors is known as an apoprotein.
 * Sometimes the Interactor object in Intact EBI is also nothing but a Protein.
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to uniprot ID. Currently not supporting NON_INDEXED.
 * 
 * Uniprot fields are added.
 *
 * index:  http://localhost:7474/db/data/index/node/
 * for uniprotId:   key:  uniprot  value = P20809
 * Relationships:  http://localhost:7474/db/data/node/15959/relationships/out/INHIBITS&FOUND_EVIDENCE_IN&AUTHOR&CONTAINS&EVIDENCE_IN_COMMENT&IS_A&DRUG_PACKAGED_BY&DRUG_PRICED_AS&DRUG_MANUFACTURED_BY&HAS_DRUG_DOSAGE&DRUG_INTERACTS_WITH&REFERENCES_PATENT&ISSUE_OF_JOURNAL&HAS_AUTHOR&PUBLISHED_IN_JOURNAL&REFERENCES_PUBMED&HAS_TAXONOMY&GENE_RELATION&EXHIBITS_ONTOLOGY&HAS_ONTOLOGIES&GENE_ON_RIGHT&GENE_ON_LEFT&OVERLAPPING_GENE
 * 
 * @author jtanisha-ee
 * @see BioEntity
 * @see Indexed
 * @see NonIndexed
 * @see BioTypes
 */
@BioEntity(bioType = BioTypes.PROTEIN)
public class Protein {

    protected static Log log = LogFactory.getLog(new Object().getClass());

    @GraphId
    private Long id;
    /**
     * primaryUniprotAccession :  : "Q99JG7"
     *
     *
     */
    @UniquelyIndexed(indexName=IndexNames.UNIPROT_ID)
    @Taxonomy(rbClass=TaxonomyTypes.UNIPROT_ID, rbField=BioFields.UNIPROT_ID)  // this is value
    private String uniprot;

    @Indexed(indexName = IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @NodeLabel
    @Indexed(indexName = IndexNames.MESSAGE)
    @Taxonomy(rbClass = TaxonomyTypes.MESSAGE, rbField = BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed(indexName = IndexNames.PROTEIN_SHORT_LABEL)
    @Taxonomy(rbClass = TaxonomyTypes.PROTEIN_SHORT_LABEL, rbField = BioFields.SHORT_LABEL)  // this is value
    private String shortLabel;

    /**
     *  
     */
    @FullTextIndexed (indexName=IndexNames.PROTEIN_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_FULL_NAME, rbField=BioFields.FULL_NAME)
    private String fullName;
    
    @FullTextIndexed (indexName=IndexNames.PROTEIN_SHORT_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_SHORT_NAME, rbField=BioFields.SHORT_NAME)
    private String shortName;
    
    @FullTextIndexed (indexName=IndexNames.PROTEIN_NAMES)
    @Taxonomy (rbClass=TaxonomyTypes.PROTEIN_NAMES, rbField=BioFields.PROTEIN_NAMES)
    private String proteinNames;
  
    
    @FullTextIndexed (indexName=IndexNames.UNIPROT_KEYWORDS)
    @Taxonomy (rbClass=TaxonomyTypes.UNIPROT_KEYWORDS, rbField=BioFields.UNIPROT_KEYWORDS)
    private String uniprotKeywords;
    
    
    /**
     * Uniprot - type
     * Translated coding sequence is transferred to TrEMBL 
     * EMBL - european Molecular biology Lab (EBI)
     * uniProtEntryType" : "TrEMBL", Swiss-Prot
     */
    @Indexed (indexName=IndexNames.UNIPROT_ENTRY_TYPE) 
    @Taxonomy (rbClass=TaxonomyTypes.UNIPROT_ENTRY_TYPE, rbField=BioFields.UNIPROT_ENTRY_TYPE)
    private String uniprotEntryType;

    
    /**
     * protein attribute:
     * 
     * based on UniProtEvidenceFields
     * "proteinExistence" : "1: Evidence at protein level" 
     *  "2: Evidence at transcript level" "4: Predicted"
     *  
     */
    /*
    @FullTextIndexed (indexName=IndexNames.UNIPROT_PROTEIN_EVIDENCE_EXISTENCE)
    @Taxonomy (rbClass=TaxonomyTypes.UNIPROT_PROTEIN_EVIDENCE_EXISTENCE, rbField=BioFields.PROTEIN_EVIDENCE_EXISTENCE)
    private String proteinEvidenceExistence;
    */
    
    /**
     * When not known, this will be set to the uniprot.
     */
    @Indexed(indexName = IndexNames.INTERACTOR_ID)
    @Taxonomy(rbClass = TaxonomyTypes.INTERACTOR_ID, rbField = BioFields.INTERACTOR_ID)
    private String interactorId;

    /**
     * When not known, this will be set to the uniprot.
     */
    @Indexed(indexName = IndexNames.MOLECULE_IDREF)
    @Taxonomy(rbClass = TaxonomyTypes.MOLECULE_IDREF, rbField = BioFields.MOLECULE_IDREF)
    private String moleculeIdRef;

    @Indexed(indexName = IndexNames.INTERACTOR_TYPE_XREF)
    private String interactorTypeXref;

    /**
     * Name of the intact XML file. use Uniprot when null.
     */
    @Indexed(indexName = IndexNames.INTACT_ID)
    @Taxonomy(rbClass = TaxonomyTypes.INTACT_ID, rbField = BioFields.INTACT_ID)
    private String intactId;

    //@Indexed (indexType=IndexType.FULLTEXT, indexName = "interactoralias")
    @FullTextIndexed(indexName = IndexNames.PROTEIN_ALIASES)
    @Taxonomy(rbClass = TaxonomyTypes.PROTEIN_ALIAS, rbField = BioFields.ALIASES)
    private String aliases;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * 
     */
    @FullTextIndexed(indexName = IndexNames.PROTEIN_UNIPROT_SECONDARY_REFS)
    @Taxonomy(rbClass = TaxonomyTypes.UNIPROT_ID, rbField = BioFields.UNIPROT_SECONDARY_REFS)
    private String uniprotSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed(indexName = IndexNames.PROTEIN_INTERPRO_SECONDARY_REFS)
    @Taxonomy(rbClass = TaxonomyTypes.INTERPRO_ID, rbField = BioFields.INTERPRO_SECONDARY_REFS)
    private String interproSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed(indexName = IndexNames.PROTEIN_IPI_SECONDARY_REFS)
    @Taxonomy(rbClass = TaxonomyTypes.IPI_ID, rbField = BioFields.IPI_SECONDARY_REFS)
    private String ipiSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed(indexName = IndexNames.PROTEIN_GO_SECONDARY_REFS)
    @Taxonomy(rbClass = TaxonomyTypes.GO_ID, rbField = BioFields.GO_SECONDARY_REFS)
    private String goSecondaryRefs;

    @FullTextIndexed(indexName = IndexNames.PROTEIN_REFSEQ_SECONDARY_REFS)
    @Taxonomy(rbClass = TaxonomyTypes.REFSEQ_ID, rbField = BioFields.REFSEQ_SECONDARY_REFS)
    private String refseqSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed(indexName = IndexNames.PROTEIN_ENSEMBL_SECONDARY_REFS)
    @Taxonomy(rbClass = TaxonomyTypes.ENSEMBL_ID, rbField = BioFields.ENSEMBL_SECONDARY_REFS)
    private String ensemblSecondaryRefs;

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     */
    @FullTextIndexed(indexName = IndexNames.PROTEIN_INTACT_SECONDARY_REFS)
    @Taxonomy(rbClass = TaxonomyTypes.INTACT_ID, rbField = BioFields.INTACT_SECONDARY_REFS)
    private String intactSecondaryRefs;

    @FullTextIndexed(indexName = IndexNames.PROTEIN_PREFERRED_SYMBOL)
    @Taxonomy(rbClass = TaxonomyTypes.PROTEIN_PREFERRED_SYMBOL, rbField = BioFields.PROTEIN_PREFERRED_SYMBOL)
    private String preferredSymbol;

    @FullTextIndexed(indexName = IndexNames.LOCATION)
    @Taxonomy(rbClass = TaxonomyTypes.LOCATION, rbField = BioFields.LOCATION)
    private String location;

    @FullTextIndexed(indexName = IndexNames.ACTIVITY_STATE)
    @Taxonomy(rbClass = TaxonomyTypes.ACTIVITY_STATE, rbField = BioFields.ACTIVITY_STATE)
    private String activityState;

    @FullTextIndexed(indexName = IndexNames.FUNCTION)
    @Taxonomy(rbClass = TaxonomyTypes.FUNCTION, rbField = BioFields.FUNCTION)
    private String function;

    /**
     * This is the species.
     */
    @Indexed(indexName = IndexNames.INTERACTOR_ID)
    @Taxonomy(rbClass = TaxonomyTypes.NCBI_TAX_ID, rbField = BioFields.NCBI_TAX_ID)
    private String ncbiTaxId;

    @Indexed(indexName = IndexNames.INTERACTOR_TYPE_SHORT_LABEL)
    private String interactorTypeShortLabel;

    @Indexed(indexName = IndexNames.INTERACTOR_TYPE_FULL_NAME)
    private String interactorTypeFullName;

   
    @Indexed(indexName = IndexNames.PROTEIN_ORGANISM_SHORT_LABEL)
    @Taxonomy(rbClass = TaxonomyTypes.ORGANISM_SHORT_LABEL, rbField = BioFields.ORGANISM_SHORT_LABEL)
    private String organismShortLabel;


    /**
     * scientific name
     */
    @Indexed (indexName = IndexNames.PROTEIN_ORGANISM_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.ORGANISM_FULL_NAME, rbField=BioFields.ORGANISM_FULL_NAME)
    private String organismFullName;

    //@Indexed (indexName = IndexNames.INTACT_INTERACTION_ID)
    //@Taxonomy (rbClass=TaxonomyTypes.INTACT_INTERACTION_ID, rbField=BioFields.INTERACTION_ID)
    //private String interactionId;
    
    /**
     * list of taxonomyId's
     * "ncbiTaxId" : "10090"
     */
     @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.FOUND_EVIDENCE_IN, elementClass = BioRelation.class)
     private Collection<BioRelation> ncbiTaxonomyRelations;
     
     /**
     *  
     */
     @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.GENE_RELATION, elementClass = BioRelation.class)
     private Collection<BioRelation> geneRelations;

     /**
      *  goTermList [
      *  "goId" : "GO:0019901",
      *  ]               
      */
     
     @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.GO_RELATION, elementClass = BioRelation.class)
     private Collection<BioRelation> goRelations;
     
     @Indexed (indexName = IndexNames.NCI_PATHWAY_INTERACTION_ID)
     @Taxonomy (rbClass=TaxonomyTypes.NCI_PATHWAY_INTERACTION_ID, rbField=BioFields.NCI_PATHWAY_INTERACTION_ID)
     private String[] nciPathwayInteractionId;
     
     @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.AUTHOR, elementClass = BioRelation.class)
     private Collection<BioRelation> authorRelations;

    /**
     * Its is an outgoing relationship of a <code>Protein<code> with
     * <code>NamedProtein</code> belongs to <code>this<code> protein family
     */

     @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_MEMBER_OF_FAMILY, elementClass = FamilyMemberRelation.class)
     private Collection<FamilyMemberRelation> memberOfProteinFamily;

    /**
     * In the following, <code>this<code> Protein object is related to other Protein objects
     * with an outgoing relationship called <code>IntactInteraction</code>.
     */
    @RelatedToVia(direction = Direction.BOTH, elementClass = BioRelation.class)
    private Collection<BioRelation> intactInteractions = new HashSet<BioRelation>();

     /**
      * Method name is used in NciPathwayTemplate & NciPathway to set relations
      */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.CONTAINS, elementClass = PartMoleculeRelation.class)
    private Collection<PartMoleculeRelation> nciPartMoleculeRelations;


    /**
     * <code>this</code> Protein object is related to other Protein objects with
     * an outgoing relationship called <code><PtmExpressionRelation></code>
     * <code>this</code> Protein PTMExpression is of another Protein
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_PTM_EXPRESSION_OF, elementClass=PtmExpressionRelation.class)
    private Collection<PtmExpressionRelation> nciPtmExpressionRelations;


    /**
     * <code>this</code> Complex object is related to other Protein objects with
     * an outgoing relationship called <code><ComplexComponentRelation></code>
     * <code>this</code> Complex is contains other {@link BioEntity}
     */
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.IS_A_COMPONENT_OF, elementClass=ComplexComponentRelation.class)
    private Collection<ComplexComponentRelation> complexComponentRelations;
    
    @RelatedToVia(direction=Direction.OUTGOING, relType=BioRelTypes.CONTAINS)
    private Collection<BioRelation> proteinSequenceRelations;
    
    /**
     * "commentType" : "FUNCTION",
     *                   "commentStatus" : "Experimental",
     *                   "evidenceIds" : [
     *                           "EC1",
     *                           "EC2",
     *                           "EC3"
     *                   ]
     *
     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.EVIDENCE_IN_COMMENT, elementClass=ProteinAnnotationComment.class)
    private Collection<BioRelation> commentRelations;
    
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.FOUND_EVIDENCE_IN, elementClass=BioRelation.class)
    private Collection<BioRelation> evidenceRelations;       
    
    public void setProteinSequenceRelations(ProteinSequence entity) {
        if (proteinSequenceRelations == null) {
            proteinSequenceRelations = new HashSet<BioRelation>();
        }    
        BioRelation relation = new BioRelation(this, entity, BioRelTypes.CONTAINS);
        proteinSequenceRelations.add(relation);
    }
    
    public Iterable<BioRelation> getProteinSequenceRelations() {
        return proteinSequenceRelations;
    }
    
    public void setCommentRelations(ProteinAnnotationComment entity) {
        if (commentRelations == null) {
            commentRelations = new HashSet<BioRelation>();
        }    
        BioRelation relation = new BioRelation(this, entity, BioRelTypes.EVIDENCE_IN_COMMENT);
        commentRelations.add(relation);
    }
    
    public Iterable<BioRelation> getCommentRelations() {
        return commentRelations;
    }
    
    public void setEvidenceRelations(Object endNode, BioRelTypes eType) {
        if (evidenceRelations == null) {
            evidenceRelations = new HashSet<BioRelation>();
        }
        BioRelation rel = new BioRelation(this, endNode, eType);
        evidenceRelations.add(rel);
    }
    
    /**
     * {@link ComplexComponentRelation}
     * {@link Protein}
     * {@link BioEntity}
     * @param complex
     */
    public void setComplexComponentRelations(Complex complex) {

        if (complexComponentRelations == null) {
            complexComponentRelations = new HashSet<ComplexComponentRelation>();
        }
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
    
    
    public void setNcbiTaxonomyRelations(NcbiTaxonomy ncbi) {
        if (ncbiTaxonomyRelations == null) {
            ncbiTaxonomyRelations = new HashSet<BioRelation>();
        }
        final BioRelation relation = new BioRelation(this, ncbi, BioRelTypes.FOUND_EVIDENCE_IN);
        ncbiTaxonomyRelations.add(relation);
    }
    
    public Iterable<BioRelation> getNcbiTaxonomyRelations() {
        return ncbiTaxonomyRelations;
    }
     
    public void setGeneRelations(Gene gene) {
        if (geneRelations == null) {
            geneRelations = new HashSet<BioRelation>();
        }
        final BioRelation relation = new BioRelation(this, gene, BioRelTypes.GENE_RELATION);
    }
    
    public Iterable<BioRelation> getGeneRelations() {
        return geneRelations;
    }
    
    /**
     * GeneOntology relationship
     * @param go {@link BioTypes#GENE_ONTOLOGY}
     */
    public void setGoRelations(GeneOntology go) {
        if (goRelations == null) {
            goRelations = new HashSet<BioRelation>();
        }
        final BioRelation relation = new BioRelation(this, go, BioRelTypes.GO_RELATION);
    }
    
    public Iterable<BioRelation> getGoRelations() {
        return goRelations;
    }

    /**
     * setComplexComponentLabels
     *
     * @param complex {@link Complex}
     * @param map {@link java.util.Map}
     */
    public void setComplexComponentLabels(Complex complex, Map map) {
        final ComplexComponentRelation relation
                = new ComplexComponentRelation(this, complex, BioRelTypes.IS_A_COMPONENT_OF, map);
        complexComponentRelations.add(relation);
    }

    /**
     *
     */
    public Protein() {
    }

    /**
     *
     * @param uniprot
     * @param nodeType
     * @param message
     */
    public Protein(String uniprot, String nodeType, String message) {
        this.uniprot = uniprot;
        this.nodeType = nodeType;
        this.message = message;
    }

    /**
     *
     * @return String
     */
    public String getUniprot() {
        return this.uniprot;
    }

    /**
     * {@link Indexed} {@link IndexNames#PROTEIN_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#PROTEIN_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @param shortLabel
     */
    public void setShortLabel(String shortLabel) {
        this.shortLabel = shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#PROTEIN_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#PROTEIN_SHORT_LABEL} {@link BioFields#SHORT_LABEL}
     *
     * @return String
     */
    public String getShortLabel() {
        return shortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#PROTEIN_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#PROTEIN_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @param fullName
     */
    public void setFullName(String fullName) {
        this.fullName = fullName;
    }
    
    public void setShortName(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }
    
    public void setProteinNames(String names) {
        this.proteinNames = names;
    }
    
    public String getProteinNames() {
        return proteinNames;
    }
    
    /**
     * {@link Indexed} {@link IndexNames#PROTEIN_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#PROTEIN_FULL_NAME} {@link BioFields#FULL_NAME}
     *
     * @return String
     */
    public String getFullName() {
        return fullName;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_UNIPROT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#UNIPROT_ID} {@link BioFields#UNIPROT_SECONDARY_REFS}
     *
     * @return String
     */
    public String getUniprotSecondaryRefs() {
        return uniprotSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_UNIPROT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#UNIPROT_ID} {@link BioFields#UNIPROT_SECONDARY_REFS}
     *
     * @param uniprotSecondaryRefs
     */
    public void setUniprotSecondaryRefs(String uniprotSecondaryRefs) {
        this.uniprotSecondaryRefs = uniprotSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_INTERPRO_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTERPRO_ID} {@link BioFields#INTERPRO_SECONDARY_REFS}
     *
     * @return String
     */
    public String getInterproSecondaryRefs() {
        return interproSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_INTERPRO_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTERPRO_ID} {@link BioFields#INTERPRO_SECONDARY_REFS}
     *
     * @param interproSecondaryRefs
     */
    public void setInterproSecondaryRefs(String interproSecondaryRefs) {
        this.interproSecondaryRefs = interproSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_IPI_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#IPI_ID} {@link BioFields#IPI_SECONDARY_REFS}
     *
     * @return String
     */
    public String getIpiSecondaryRefs() {
        return ipiSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_IPI_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#IPI_ID} {@link BioFields#IPI_SECONDARY_REFS}
     *
     * @param ipiSecondaryRefs
     */
    public void setIpiSecondaryRefs(String ipiSecondaryRefs) {
        this.ipiSecondaryRefs = ipiSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_GO_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#GO_ID} {@link BioFields#GO_SECONDARY_REFS}
     *
     * @return String
     */
    public String getGoSecondaryRefs() {
        return goSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_GO_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#GO_ID} {@link BioFields#GO_SECONDARY_REFS}
     *
     * @param goSecondaryRefs
     */
    public void setGoSecondaryRefs(String goSecondaryRefs) {
        this.goSecondaryRefs = goSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_INTACT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTACT_ID} {@link BioFields#INTACT_SECONDARY_REFS}
     *
     * @return String
     */
    public String getIntactSecondaryRefs() {
        return intactSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_INTACT_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTACT_ID} {@link BioFields#INTACT_SECONDARY_REFS}
     *
     * @param intactSecondaryRefs
     */
    public void setIntactSecondaryRefs(String intactSecondaryRefs) {
        this.intactSecondaryRefs = intactSecondaryRefs;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTACT_INTERACTION_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTACT_INTERACTION_ID} {@link BioFields#INTERACTION_ID}
     *
     * @return
     */
    //public String getInteractionId() {
    //return interactionId;
    //}
    /**
     * {@link Indexed} {@link IndexNames#INTACT_INTERACTION_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTACT_INTERACTION_ID} {@link BioFields#INTERACTION_ID}
     *
     * @param interactionId
     */
    //public void setInteractionId(String interactionId) {
    //  this.interactionId = interactionId;
    //}
   
    
    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_ENSEMBL_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#ENSEMBL_ID} {@link BioFields#ENSEMBL_SECONDARY_REFS}
     *
     * @return String
     */
    public String getEnsemblSecondaryRefs() {
        return ensemblSecondaryRefs;
    }

    /**
     * In case of full text indexes, if there are multiple space separated
     * terms, we add one row per term in the Taxonomy collection in Mongo.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_ENSEMBL_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#ENSEMBL_ID} {@link BioFields#ENSEMBL_SECONDARY_REFS}
     *
     * @param ensemblSecondaryRefs
     */
    public void setEnsemblSecondaryRefs(String ensemblSecondaryRefs) {
        this.ensemblSecondaryRefs = ensemblSecondaryRefs;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_REFSEQ_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#REFSEQ_ID} {@link BioFields#REFSEQ_SECONDARY_REFS}
     *
     * @return String
     */
    public String getRefseqSecondaryRefs() {
        return refseqSecondaryRefs;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_REFSEQ_SECONDARY_REFS}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#REFSEQ_ID} {@link BioFields#REFSEQ_SECONDARY_REFS}
     *
     * @param refseqSecondaryRefs
     */
    public void setRefseqSecondaryRefs(String refseqSecondaryRefs) {
        this.refseqSecondaryRefs = refseqSecondaryRefs;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_ALIASES}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#PROTEIN_ALIAS} {@link BioFields#ALIASES}
     *
     * @return String
     */
    public String getAliases() {
        return aliases;
    }

    /**
     * A space separated list of aliases.
     * <p>
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_ALIASES}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#PROTEIN_ALIAS} {@link BioFields#ALIASES}
     *
     * @param aliases
     */
    public void setAliases(String aliases) {
        this.aliases = aliases;
    }

    /**
     * Name of the intact XML file. Use Uniprot when null.
     * <p>
     * {@link Indexed} {@link IndexNames#INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTACT_ID} {@link BioFields#INTACT_ID}
     *
     * @return String
     */
    public String getIntactId() {
        return intactId;
    }

    /**
     * Name of the intact XML file. Use Uniprot when null.
     * <p>
     * {@link Indexed} {@link IndexNames#INTACT_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTACT_ID} {@link BioFields#INTACT_ID}
     *
     * @param intactId
     */
    public void setIntactId(String intactId) {
        this.intactId = intactId;
    }

    /**
     * When not known, this will be set to the uniprot.
     * <p>
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTERACTOR_ID} {@link BioFields#INTERACTOR_ID}
     *
     * @return String
     */
    public String getInteractorId() {
        return interactorId;
    }

    /**
     * When not known, this will be set to the uniprot.
     * <p>
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#INTERACTOR_ID} {@link BioFields#INTERACTOR_ID}
     *
     * @param interactorId
     */
    public void setInteractorId(String interactorId) {
        this.interactorId = interactorId;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_FULL_NAME}
     *
     * @return String
     */
    public String getInteractorTypeFullName() {
        return interactorTypeFullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_FULL_NAME}
     *
     * @param interactorTypeFullName
     */
    public void setInteractorTypeFullName(String interactorTypeFullName) {
        this.interactorTypeFullName = interactorTypeFullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_SHORT_LABEL}
     *
     * @return String
     */
    public String getInteractorTypeShortLabel() {
        return interactorTypeShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_SHORT_LABEL}
     *
     * @param interactorTypeShortLabel
     */
    public void setInteractorTypeShortLabel(String interactorTypeShortLabel) {
        this.interactorTypeShortLabel = interactorTypeShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_XREF}
     *
     * @return String
     */
    public String getInteractorTypeXref() {
        return interactorTypeXref;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_TYPE_XREF}
     *
     * @param interactorTypeXref
     */
    public void setInteractorTypeXref(String interactorTypeXref) {
        this.interactorTypeXref = interactorTypeXref;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#NCBI_TAX_ID} {@link BioFields#NCBI_TAX_ID}
     *
     * @return String
     */
    public String getNcbiTaxId() {
        return ncbiTaxId;
    }

    /**
     * {@link Indexed} {@link IndexNames#INTERACTOR_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#NCBI_TAX_ID} {@link BioFields#NCBI_TAX_ID}
     *
     * @param ncbiTaxId
     */
    public void setNcbiTaxId(String ncbiTaxId) {
        this.ncbiTaxId = ncbiTaxId;
    }

    /**
     * In the following, <code>this<code> Protein object is related to other Protein objects
     * with an outgoing relationship called <code>IntactInteraction</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction.BOTH}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param intactInteractions
     */
    public void setIntactInteractions(Collection<BioRelation> intactInteractions) {
        if (intactInteractions != null) {
           this.intactInteractions = intactInteractions;
        }
    }

    /**
     * Method name is used in NciPathwayTemplate & NciPathway to set relations
     * In the following, <code>NamedProtein<code> Protein object is related to <code>this<code> object
     * with an outgoing relationship called <code>memberOfProteinFamily</code>.
     * <p>
     * {@link RelatedTo} {@link org.neo4j.graphdb.Direction.OUTGOING}
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
        log.info("setMemberOfProteinFamily() protein");
        if (memberOfProteinFamily == null) { 
            memberOfProteinFamily = new HashSet<FamilyMemberRelation>();
        } 
        if (!proteinFamilyRelation.isValid(BioEntityClasses.PROTEIN, BioEntityClasses.NAMED_PROTEIN)) {
            throw new RuntimeException("proteinFamilyBioRelation is invalid. Check the startNode and endNode");
        }
        this.memberOfProteinFamily.add(proteinFamilyRelation);
    }

    /**
     * In the following, <code>NamedProtein<code> Protein object is related to <code>this<code> object
     * with an outgoing relationship called <code>memberOfProteinFamily</code>.
     * <p>
     * {@link RelatedTo} {@link org.neo4j.graphdb.Direction.OUTGOING}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#NAMED_PROTEIN} or
     * {@link BioTypes#PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @return Iterable<FamilyMemberRelation> collection of memberOfProteinFamily
     */
    public Iterable<FamilyMemberRelation> getMemberOfProteinFamily() {
        return this.memberOfProteinFamily;
    }

    /**
     * This expression can be part of any molecule either {@link Complex} or
     * {@link Protein}
     *
     * The molecule_idref refers to {@link Protein} molecule
     *
     * "@molecule_idref" : "200425", "PTMExpression" : [ { "@protein" :
     * "P16144", "@position" : "0", "@aa" : "S", "@modification" :
     * "phosphorylation" }
     *
     * In the following <code>this</code> Protein object is related to
     * <code>protein</code> with an outgoing relationship called
     * <code>nciPtmExpressionRelation</code>
     *
     * <p>
     * {@link RelatedTo} {@link org.neo4j.graphdb.Direction.OUTGOING}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param protein
     * @param pi
     * @param pos
     * @param aa
     * @param modification
     */
    public void setPtmExpressionRelation(Protein protein, String pi, String pos, String aa, String modification) {

        if (nciPtmExpressionRelations == null) {
            nciPtmExpressionRelations = new HashSet<PtmExpressionRelation>();
        }
        
        final PtmExpressionRelation ptmRelation
                = new PtmExpressionRelation(this, protein, BioRelTypes.IS_PTM_EXPRESSION_OF,
                        pi, pos, aa, modification);
        nciPtmExpressionRelations.add(ptmRelation);
    } 

    /**
     *
     * @return Iterable<PtmExpressionRelation>
     */
    public Iterable<PtmExpressionRelation> getPtmExpressionRelation() {
        log.info("Complex:  length of nciPtmExpressionRelations = " + nciPtmExpressionRelations.size());
        return nciPtmExpressionRelations;
    }

    /**
     * {@link UniquelyIndexed} {@link IndexNames#UNIPROT_ID}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#UNIPROT_ID} {@link BioFields#UNIPROT_ID}
     *
     * @param uniprot
     */
    public void setUniprot(String uniprot) {
        this.uniprot = uniprot;
    }

    /**
     * {@link Indexed} {@link IndexNames#PROTEIN_ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#ORGANISM_SHORT_LABEL} {@link BioFields#ORGANISM_SHORT_LABEL}
     *
     * @return String
     */
    public String getOrganismShortLabel() {
        return organismShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#PROTEIN_ORGANISM_SHORT_LABEL}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#ORGANISM_SHORT_LABEL} {@link BioFields#ORGANISM_SHORT_LABEL}
     *
     * @param organismShortLabel
     */
    public void setOrganismShortLabel(String organismShortLabel) {
        this.organismShortLabel = organismShortLabel;
    }

    /**
     * {@link Indexed} {@link IndexNames#PROTEIN_ORGANISM_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#ORGANISM_FULL_NAME} {@link BioFields#ORGANISM_FULL_NAME}
     *
     * @return String
     */
    public String getOrganismFullName() {
        return organismFullName;
    }

    /**
     * {@link Indexed} {@link IndexNames#PROTEIN_ORGANISM_FULL_NAME}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#ORGANISM_FULL_NAME} {@link BioFields#ORGANISM_FULL_NAME}
     *
     * @param organismFullName
     */
    public void setOrganismFullName(String organismFullName) {
        this.organismFullName = organismFullName;
    }

    /**
     * In the following, <code>this<code> Protein object is related to other Protein objects
     * with an outgoing relationship called <code>IntactInteraction</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction.BOTH}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getIntactInteractions() {
        log.info("length = " + intactInteractions.size());
        return intactInteractions;
        //return IteratorUtil.asCollection(bioRelations);
    }

    /**
     * In the following, <code>this<code> Protein object is related to other Protein objects
     * with an outgoing relationship called <code>IntactInteraction</code>.
     * <p>
     * {@link RelatedToVia} {@link org.neo4j.graphdb.Direction.BOTH}
     * <DL>
     * <LI>
     * startNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * <LI>
     * endNodeBioType {@link BioTypes#PROTEIN}
     * </LI>
     * </DL>
     *
     * elementClass {@link BioRelation}
     *
     * @param protein
     * @param name
     * @return BioRelation
     */
    public BioRelation interactsWith(Protein protein, String name) {
        final BioRelation intactInteraction = new BioRelation(this, protein, BioRelTypes.INTERACTS_WITH);
        intactInteraction.setName(name);
        intactInteractions.add(intactInteraction);
        log.info("intactInteractions size = " + intactInteractions.size());
        return intactInteraction;
    }

    /**
     * Method name is used in NciPathwayTemplate & NciPathway to set relations
     * Part of this molecule consists of another protein which is a whole
     * molecule. Some proteins can be part of multiple molecules. Some
     * PartMolecules are part of other molecules.
     *
     * These are identified in the existing protein with start and end for each
     * such protein
     *
     * "Part" : { "@whole_molecule_idref" : "200436", "@part_molecule_idref" :
     * "202926", "@start" : "701", "@end" : "882" }
     * {@link BioFields} {@link NciOntology#Start} {@link NciOntology#End}
     *
     * @param partProtein
     * @param start
     * @param end
     */
    public void setNciPartOfMolecule(Object partProtein, String start, String end) {
        // check for validity

       if (nciPartMoleculeRelations == null) {
           nciPartMoleculeRelations = new HashSet<PartMoleculeRelation>();
       }
       final PartMoleculeRelation partRelation = new PartMoleculeRelation(this, partProtein, BioRelTypes.CONTAINS, start, end);
        nciPartMoleculeRelations.add(partRelation);
    }

    /**
     *
     * @return Iterable<PartMoleculeRelation>
     */
    public Iterable<PartMoleculeRelation> getNciPartMoleculeRelations() {
        log.info("length of nciPartMoleculeRelations = " + nciPartMoleculeRelations.size());
        return nciPartMoleculeRelations;
    }
    
    

    /**
     *
     * @return Long
     */
    public Long getId() {
        return id;
    }

    /**
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     *
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
    public void setNodeType(BioTypes nodeType) {
        this.nodeType = nodeType.toString();
    }

    /**
     * {@link NonIndexed}
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     * {@link NonIndexed}
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     * When not know, this will be set to the uniprot.
     * <p>
     * {@link Indexed} {@link IndexNames#MOLECULE_IDREF}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#MOLECULE_IDREF} {@link BioFields#MOLECULE_IDREF}
     *
     * @return String
     */
    public String getMoleculeIdRef() {
        return moleculeIdRef;
    }

    /**
     * When not know, this will be set to the uniprot.
     * <p>
     * {@link Indexed} {@link IndexNames#MOLECULE_IDREF}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#MOLECULE_IDREF} {@link BioFields#MOLECULE_IDREF}
     *
     * @param moleculeIdRef
     */
    public void setMoleculeIdRef(String moleculeIdRef) {
        this.moleculeIdRef = moleculeIdRef;
    }

    public void setAuthorRelations(Author author) {
        if (authorRelations == null) {
            authorRelations = new HashSet<BioRelation>();
        }    
        BioRelation rel = new BioRelation(this, author, BioRelTypes.AUTHOR);
        if (rel != null) {
            authorRelations.add(rel);
        }
    }
    
    public Iterable<BioRelation> getAuthorRelations() {
        return authorRelations;
    }
    
    @Override
    public int hashCode() {
        return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + uniprot);
    }

    /**
     * Creates a new protein
     *
     * @return Protein {@link BioEntity#PROTEIN}
     */
    public Protein Protein() {
        Protein protein = new Protein();
        protein.setNodeType(BioTypes.PROTEIN);
        return protein;
    }

    /**
     *
     * @param obj
     * @return boolean
     */
    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }

        if (!getClass().getName().equals(obj.getClass().getName())) {
            return false;
        }

        final Protein other = (Protein) obj;

        if ((this.uniprot != null) && (other.getUniprot() != null)) {
            if (!this.uniprot.equals(other.getUniprot())) {
                return false;
            }
        } else {
            return false;
        }

        if ((this.nodeType != null) && (other.getNodeType() != null)) {
            if (!this.nodeType.equals(other.getNodeType())) {
                return false;
            }
        } else {
            return false;
        }

        return true;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#PROTEIN_PREFERRED_SYMBOL} {@link BioFields#PROTEIN_PREFERRED_SYMBOL}
     *
     * @return String
     */
    public String getPreferredSymbol() {
        return preferredSymbol;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#PROTEIN_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#PROTEIN_PREFERRED_SYMBOL} {@link BioFields#PROTEIN_PREFERRED_SYMBOL}
     *
     * @param preferredSymbol
     */
    public void setPreferredSymbol(String preferredSymbol) {
        this.preferredSymbol = preferredSymbol;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#LOCATION}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#LOCATION} {@link BioFields#LOCATION}
     *
     * @return String proteinLocation
     */
    public String getLocation() {
        return location;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#LOCATION}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#LOCATION} {@link BioFields#LOCATION}
     *
     * @param proteinLocation
     */
    public void setLocation(String proteinLocation) {
        this.location = proteinLocation;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#ACTIVITY_STATE}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#ACTIVITY_STATE} {@link BioFields#ACTIVITY_STATE}
     *
     * @param activityState
     */
    public void setActivityState(String activityState) {
        this.activityState = activityState;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#ACTIVITY_STATE}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#ACTIVITY_STATE} {@link BioFields#ACTIVITY_STATE}
     *
     * @return String
     */
    public String getActivityState() {
        return this.activityState;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#FUNCTION}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#FUNCTION} {@link BioFields#FUNCTION}
     *
     * @param function
     */
    public void setFunction(String function) {
        this.function = function;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#FUNCTION}
     * <p>
     * {@link Taxonomy} {@link org.atgc.bio.meta.TaxonomyTypes#FUNCTION} {@link BioFields#FUNCTION}
     *
     * @return String
     */
    public String getFunction() {
        return this.function;
    }
    
    public String getUniprotEntryType() {
        return uniprotEntryType;
    }

    public void setUniprotEntryType(String uniProtEntryType) {
        this.uniprotEntryType = uniProtEntryType;
    }
   

    public String getUniprotKeywords() {
        return uniprotKeywords;
    }

    public void setUniprotKeywords(String keywords) {
        this.uniprotKeywords = keywords;
    }
    
    /**
     * In the format: [ { "@typeAc" : "MI:0302", "#text" : "Kiaa0964" }, {
     * "@typeAc" : "MI:0302", "#text" : "Sapap4" }, { "@typeAc" : "MI:0302",
     * "#text" : "SAP90/PSD-95-associated protein 4" }, { "@typeAc" : "MI:0302",
     * "#text" : "PSD-95/SAP90-binding protein 4" }, { "@typeAc" : "MI:0301",
     * "#text" : "Dlgap4" } ]
     */
    /**
     * In the format:
     *
     * {
     * "primaryRef": { "@refTypeAc": "MI:0356", "@refType": "identity",
     * "@version": "SP_5", "@id": "B1AZP2", "@dbAc": "MI:0486", "@db":
     * "uniprotkb" }, "secondaryRef": [ { "@refTypeAc": "MI:0360", "@refType":
     * "secondary-ac", "@version": "SP_5", "@id": "Q6PD44", "@dbAc": "MI:0486",
     * "@db": "uniprotkb" }, { "@refTypeAc": "MI:0360", "@refType":
     * "secondary-ac", "@version": "SP_5", "@id": "Q80TN3", "@dbAc": "MI:0486",
     * "@db": "uniprotkb" }, { "@id": "IPR005026", "@dbAc": "MI:0449", "@db":
     * "interpro" }, { "@id": "IPI00330186", "@dbAc": "MI:0675", "@db": "ipi" },
     * { "@id": "IPI00377469", "@dbAc": "MI:0675", "@db": "ipi" }, { "@id":
     * "IPI00752220", "@dbAc": "MI:0675", "@db": "ipi" }, { "@refTypeAc":
     * "MI:0356", "@refType": "identity", "@id": "EBI-645637", "@dbAc":
     * "MI:0469", "@db": "intact" }, { "@id": "GO:0007267", "@dbAc": "MI:0448",
     * "@db": "go" }, { "@refTypeAc": "MI:0360", "@refType": "secondary-ac",
     * "@version": "SP_5", "@id": "Q8R3U9", "@dbAc": "MI:0486", "@db":
     * "uniprotkb" }, { "@id": "NP_666240.4", "@dbAc": "MI:0481", "@db":
     * "refseq" }, { "@refTypeAc": "MI:0360", "@refType": "secondary-ac",
     * "@version": "SP_5", "@id": "Q6XBF1", "@dbAc": "MI:0486", "@db":
     * "uniprotkb" }, { "@id": "GO:0045202", "@dbAc": "MI:0448", "@db": "go" },
     * { "@refTypeAc": "MI:0360", "@refType": "secondary-ac", "@version":
     * "SP_5", "@id": "B1AZP3", "@dbAc": "MI:0486", "@db": "uniprotkb" }, {
     * "@refTypeAc": "MI:0360", "@refType": "secondary-ac", "@version": "SP_18",
     * "@id": "B7ZNS1", "@dbAc": "MI:0486", "@db": "uniprotkb" }, { "@id":
     * "NP_001035952.1", "@dbAc": "MI:0481", "@db": "refseq" }, { "@refTypeAc":
     * "MI:0360", "@refType": "secondary-ac", "@version": "SP_5", "@id":
     * "Q3KQQ8", "@dbAc": "MI:0486", "@db": "uniprotkb" }, { "@id":
     * "NP_001035953.1", "@dbAc": "MI:0481", "@db": "refseq" }, { "@id":
     * "ENSMUSG00000061689", "@dbAc": "MI:0476", "@db": "ensembl" } ] },
     */
    /**
     * {
     * "primaryRef": { "@refTypeAc": "MI:0356", "@refType": "identity", "@id":
     * "MI:0326", "@dbAc": "MI:0488", "@db": "psi-mi" }, "secondaryRef": [ {
     * "@refTypeAc": "MI:0361", "@refType": "see-also", "@id": "SO:0000358",
     * "@dbAc": "MI:0601", "@db": "so" }, { "@refTypeAc": "MI:0356", "@refType":
     * "identity", "@id": "EBI-619654", "@dbAc": "MI:0469", "@db": "intact" }, {
     * "@refTypeAc": "MI:0358", "@refType": "primary-reference", "@id":
     * "14755292", "@dbAc": "MI:0446", "@db": "pubmed" } ] } }
     */
}
