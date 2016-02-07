/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * Connect this to the disease Bioentity
 *
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.DISEASE)
public class Disease {

    protected static Logger log = LogManager.getLogger(Disease.class);

    @GraphId
    private Long id;

    /*
     * For generic terms like cancer, check
     */
    @UniquelyIndexed (indexName=IndexNames.DISEASE_TERM)
    @Taxonomy(rbClass=TaxonomyTypes.DISEASE_TERM, rbField=BioFields.DISEASE_TERM)
    private String diseaseTerm;

    @Indexed(indexName=IndexNames.DISEASE_CODE)
    @Taxonomy (rbClass=TaxonomyTypes.DISEASE_CODE, rbField=BioFields.DISEASE_CODE)
    private String diseaseCode;

    /**
     * Evidence Codes with respect to Gene
     * Evidence codes qualify the assertions with respect to the association of a gene to a disease or compound term by
     * telling how the assertions were made (for example, through inference or experimental data).
     * The curators may have identified the means by which an assertion using the extracted sentence,
     * alone, or through careful reading of the abstract from which the sentence originated.
     */

   /**
    * <GeneData>
    *   <MatchedGeneTerm>tlh6</MatchedGeneTerm>
    *   <NCIGeneConceptCode></NCIGeneConceptCode>
    * </GeneData>
    *  <Roles>
        * <PrimaryNCIRoleCode>Gene_Expressed_In_Tissue</PrimaryNCIRoleCode>
    *     <OtherRole>Gene_Expression_Changed_in_Disease</OtherRole>
    *     <OtherRole>Gene_May_have_Therapeutic_Relevance</OtherRole>
    *  </Roles>
    */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.ROLE_OF_GENE, elementClass=NciDiseaseGeneRoleRelation.class)
    private Collection<NciDiseaseGeneRoleRelation> geneRelations = new HashSet<NciDiseaseGeneRoleRelation>();

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.DISEASE.toString();

    /**
    * <GeneData>
    *   <MatchedGeneTerm>tlh6</MatchedGeneTerm>
    *   <NCIGeneConceptCode></NCIGeneConceptCode>
    * </GeneData>
    *
    * <EvidenceCode>based on abstract</EvidenceCode>
    * <EvidenceCode>EV-EXP-IDA</EvidenceCode>
    * <EvidenceCode>EV-AS-NAS</EvidenceCode>
    */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.FOUND_EVIDENCE_IN, elementClass=NciDiseaseGeneEvidenceRelation.class)
    private Collection<NciDiseaseGeneEvidenceRelation> evidenceRelations = new HashSet<NciDiseaseGeneEvidenceRelation>();



    /**
     * organism
     * <PubMedID>8427875</PubMedID>
     *  <Organism>Human</Organism>
     *  <NegationIndicator>no</NegationIndicator>
     *  <CellineIndicator>no</CellineIndicator>
     *  <Comments></Comments>
     *
     *  <Roles>
     *  <PrimaryNCIRoleCode>not_assigned</PrimaryNCIRoleCode>
     *  <OtherRole>not_assigned</OtherRole>
     *  </Roles>

     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_OBSERVED_IN, elementClass = BioRelation.class)
    private Collection<BioRelation> organismComponents = new HashSet<BioRelation>();
    
    @RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.IS_OBSERVED_IN_NCBI_TAXONOMY, elementClass = BioRelation.class)
    private BioRelation ncbiTaxonomyRelation;
    
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed (indexName=IndexNames.DISEASE_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.DISEASE_LABEL, rbField=BioFields.SHORT_LABEL)  // this is value
    private String shortLabel;

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable<BioRelation> getOrganismComponents() {
        log.info("length = " + organismComponents.size());
        return organismComponents;
        //return IteratorUtil.asCollection(proteinInteractions);
    }

    /**
     *
     * @param organism
     */
    public void setOrganismComponents(Organism organism) {
        final BioRelation organismRelation = new BioRelation(this, organism, BioRelTypes.IS_OBSERVED_IN);
        if (organismRelation != null) {
           // log.info("organism.shortLabel =" + organism.getShortLabel());
            organismRelation.setName(organism.getShortLabel());
            this.organismComponents.add(organismRelation);
           // log.info("organismComponents size = " + organismComponents.size());
        }
    }

    /**
     *
     * @return Iterable<NciDiseaseGeneRoleRelation>
     */
    public Iterable<NciDiseaseGeneRoleRelation> getGeneRelations() {
        log.info("length = " + geneRelations.size());
        return geneRelations;
    }

    /**
     *
     * @param gene
     * @param geneTerm
     * @param roles
     */
    public void setGeneRoleRelations(Gene gene, String geneTerm, List roles) {
        final NciDiseaseGeneRoleRelation geneRelation = new NciDiseaseGeneRoleRelation(
                this, gene,  geneTerm);
        //geneRelation.setGeneRoles(roles);
        geneRelations.add(geneRelation);
    }

    /**
     *
     * @param roleRelation
     */
    public void setGeneRelations(NciDiseaseGeneRoleRelation roleRelation) {
        geneRelations.add(roleRelation);
    }

    /**
     * The evidence code eg: EV-IC
     *
     * @param gene
     */
    public void setEvidenceRelations(Gene gene) {
        final NciDiseaseGeneEvidenceRelation eCodeRelation = new NciDiseaseGeneEvidenceRelation(this, gene);
        //eCodeRelation.setCodes(eCodes);
        evidenceRelations.add(eCodeRelation);
    }

    /**
     * eCodeValues has values : EV-IC:<text>
     * EV-EXP-IMP-REACTION-BLOCKED: Mutant is characterized, and blocking of reaction is demonstrated."),
     *
     * @param eCodeRelation
     */
    public void setEvidenceRelations(NciDiseaseGeneEvidenceRelation eCodeRelation) {
        evidenceRelations.add(eCodeRelation);
    }

    public BioRelation getNcbiTaxonomyRelation() {
        return ncbiTaxonomyRelation;
    }
    
    public void setNcbiTaxonomyRelation(NcbiTaxonomy ncbiTaxonomy) {
        ncbiTaxonomyRelation = new BioRelation(this, ncbiTaxonomy, BioRelTypes.IS_OBSERVED_IN_NCBI_TAXONOMY);
    }
    /**
     *
     * @return String
     */
    public String getDiseaseCode() {
        return this.diseaseCode;
    }

    /**
     *
     * @param code
     */
    public void setDiseaseCode(String code) {
        this.diseaseCode = code;
    }

    /**
     *
     * @return String
     */
    public String getDiseaseTerm() {
        return this.diseaseTerm;
    }

    /**
     *
     * @param term
     */
    public void setDiseaseTerm(String term) {
        this.diseaseTerm = term;
    }

    /**
     *
     * @return String
     */
    public String getNodeType() {
        return this.nodeType;
    }

    /**
     *
     * @return String
     */
    public String getMessage() {
        return this.message;
    }

    /**
     *
     * @param msg
     */
    public void setMessage(String msg) {
        this.message = msg;
    }

    /**
     *
     * @return String
     */
    public String getDiseaseLabel() {
        return this.shortLabel;
    }

    /**
     *
     * @param label
     */
    public void setShortLabel(String label) {
        this.shortLabel = label;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + "-" + diseaseTerm);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Disease other = (Disease) obj;
        if ((this.diseaseTerm== null) ? (other.diseaseTerm != null) : !this.diseaseTerm.equals(other.diseaseTerm)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }


}
