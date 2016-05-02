/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * Whenever, an Indexed field is not found in a public vendor file, we will
 * default to. Currently not supporting NON_INDEXED.
 * Connect this to the disease Bioentity
 *
 * This is common for DrugBank and NciCompound
 *
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.DRUG)
public class Drug {

    protected static Logger log = LogManager.getLogger(Drug.class);

    @GraphId
    private Long id;

    /*
     * NCI_DRUG_TERM, name in drugbank are mapped to Drug -> drugName.
     */
    @UniquelyIndexed(indexName=IndexNames.DRUG_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.DRUG_NAME, rbField=BioFields.DRUG_NAME)
    private String drugName;

    @Indexed(indexName=IndexNames.NCI_DRUG_CODE)
    @Taxonomy (rbClass=TaxonomyTypes.NCI_DRUG_CODE, rbField=BioFields.NCI_DRUG_CODE)
    private String nciDrugCode;

    /* drug bank fields */
    @Indexed (indexName=IndexNames.DRUG_BANK_ID)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_BANK_ID, rbField=BioFields.DRUG_BANK_ID)
    private String drugBankId;

    // drug bank description -
    // maintain drug description here too.
    @FullTextIndexed(indexName=IndexNames.DRUG_DESCRIPTION)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_DESCRIPTION, rbField=BioFields.DRUG_DESCRIPTION)
    private String drugDescription;


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
    * <DrugData>
    *      <DrugTerm>blm</DrugTerm>
    *      <NCIDrugConceptCode>C313</NCIDrugConceptCode>
    * </DrugData>
    *  <Roles>
    * <Roles>
    *        <PrimaryNCIRoleCode>Chemical_or_Drug_Has_Target_Gene_Product</PrimaryNCIRoleCode>
    *        <OtherRole>Chemical_or_Drug_Binds_to_Gene_Product</OtherRole>
    * </Roles>
    *
    */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.ROLE_OF_GENE, elementClass=NciDrugGeneRoleRelation.class)
    private Collection<NciDrugGeneRoleRelation> drugGeneRelations = new HashSet<NciDrugGeneRoleRelation>();

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.DRUG.toString();
    
     
    @RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.IS_OBSERVED_IN_NCBI_TAXONOMY, elementClass = BioRelation.class)
    private BioRelation ncbiTaxonomyRelation;

    /*
       external-identifiers refer to other pharmgkb, KEGG drug too
       "external-identifiers" : [
		{
            	"resource" : "UniProtKB",
			    "identifier" : "P06621"
		}
		]
     */
    @RelatedTo(direction=Direction.BOTH, relType=BioRelTypes.IDENTIFIED_PROTEIN, elementClass = BioRelation.class)
    private BioRelation proteinRelation;


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
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.FOUND_EVIDENCE_IN, elementClass=NciDrugGeneEvidenceRelation.class)
    private Collection<NciDrugGeneEvidenceRelation> evidenceRelations = new HashSet<NciDrugGeneEvidenceRelation>();



    /**
     * organism
     * <PubMedID>1065016</PubMedID>
     *  <Organism>Mouse</Organism>
     *  <NegationIndicator>no</NegationIndicator>
     *  <CellineIndicator>no</CellineIndicator>
     *  <Comments></Comments>
     *
     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_OBSERVED_IN, elementClass = BioRelation.class)
    private Collection<BioRelation> organismComponents = new HashSet<BioRelation>();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Visual
    @Indexed (indexName=IndexNames.DRUG_LABEL)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_LABEL, rbField=BioFields.SHORT_LABEL)  // this is value
    private String shortLabel;




   public Iterable<BioRelation> getOrganismComponents() {
        log.info("length = " + organismComponents.size());
        return organismComponents;
        //return IteratorUtil.asCollection(proteinInteractions);
    }

    public void setOrganismComponents(Organism organism) {
        final BioRelation organismRelation = new BioRelation(this, organism, BioRelTypes.IS_OBSERVED_IN);
        if (organismRelation != null) {
           // log.info("organism.shortLabel =" + organism.getShortLabel());
            organismRelation.setName(organism.getShortLabel());
            this.organismComponents.add(organismRelation);
           // log.info("organismComponents size = " + organismComponents.size());
        }
    }
    
    public BioRelation getNcbiTaxonomyRelation() {
        return ncbiTaxonomyRelation;
    }
    
    public void setNcbiTaxonomyRelation(NcbiTaxonomy ncbiTaxonomy) {
        ncbiTaxonomyRelation = new BioRelation(this, ncbiTaxonomy, BioRelTypes.IS_OBSERVED_IN_NCBI_TAXONOMY);
    }

    public Iterable<NciDrugGeneRoleRelation> getDrugGeneRelations() {
        log.info("length = " + drugGeneRelations.size());
        return drugGeneRelations;
    }

    public void setDrugGeneRoleRelations(Gene gene, String geneTerm, List roles) {
        final NciDrugGeneRoleRelation drugGeneRelation = new NciDrugGeneRoleRelation(
                this, gene,  geneTerm);
        //geneRelation.setGeneRoles(roles);
        drugGeneRelations.add(drugGeneRelation);
    }


    public void setDrugGeneRelations(NciDrugGeneRoleRelation roleRelation) {
        drugGeneRelations.add(roleRelation);
    }

    /**
     * The evidence code eg: EV-IC
     *
     * @param gene
     */
    public void setEvidenceRelations(Gene gene) {
        final NciDrugGeneEvidenceRelation eCodeRelation = new NciDrugGeneEvidenceRelation(this, gene);
        //eCodeRelation.setCodes(eCodes);
        evidenceRelations.add(eCodeRelation);
    }

    /**
     * eCodeValues has values : EV-IC:<text>
     * EV-EXP-IMP-REACTION-BLOCKED: Mutant is characterized, and blocking of reaction is demonstrated."),
     *
     * @param eCodeRelation
     */
    public void setEvidenceRelations(NciDrugGeneEvidenceRelation eCodeRelation) {
        evidenceRelations.add(eCodeRelation);
    }

    /**
     *
     * @return String
     */
    public String getDrugCode() {
        return this.nciDrugCode;
    }

    public void setDrugCode(String code) {
        this.nciDrugCode = code;
    }

    public String getDrugName() {
        return this.drugName;
    }

    public void setDrugName(String name) {
        this.drugName = name;
    }

    public String getNodeType() {
        return this.nodeType;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }

    public String getMessage() {
        return this.message;
    }

    public String getShortLabel() {
        return this.shortLabel;
    }

    public void setShortLabel(String drugLabel) {
        this.shortLabel = drugLabel;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + "-" + drugName);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Drug other = (Drug) obj;
        if ((this.drugName == null) ? (other.drugName != null) : !this.drugName.equals(other.drugName)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }

    /* drugbank elements */


    /**
     * cas identifier. Eg. "120993-53-5"
     */
    @Indexed (indexName=IndexNames.CHEMICAL_ABSTRACT_ID)
    @Taxonomy (rbClass=TaxonomyTypes.CHEMICAL_ABSTRACT_ID, rbField=BioFields.CHEMICAL_ABSTRACT_ID)
    private String casId;

    @FullTextIndexed (indexName=IndexNames.COMPOUND_PREFERRED_SYMBOL)
    @Taxonomy (rbClass=TaxonomyTypes.COMPOUND_PREFERRED_SYMBOL, rbField=BioFields.COMPOUND_PREFERRED_SYMBOL)
    private String compoundPreferredSymbol;

    @Indexed (indexName=IndexNames.MOLECULE_IDREF)
    @Taxonomy (rbClass=TaxonomyTypes.MOLECULE_IDREF, rbField=BioFields.MOLECULE_IDREF)
    private String moleculeIdRef;

    /* small molecule */
    @NonIndexed
    private String drugType;

    @NonIndexed
    private String createdDate;

    /**
     * The @created field of DrugBank. Eg. "2005-06-13 07:24:05 -0600"
     *
     * @return String
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     * The @created field of DrugBank. eg. "2005-06-13 07:24:05 -0600"
     *
     * @param createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * The @updated field of DrugBank. Eg. "2013-05-12 21:37:25 -0600"
     *
     * @return String
     */
    public String getUpdatedDate() {
        return updatedDate;
    }

    /**
     * The @updated field of DrugBank. Eg. "2013-05-12 21:37:25 -0600"
     *
     * @param updatedDate
     */
    public void setUpdatedDate(String updatedDate) {
        this.updatedDate = updatedDate;
    }

    @NonIndexed
    private String updatedDate;

    @NonIndexed
    private String version;


    @NonIndexed
    private String indication;

    @NonIndexed
    private String pharmacology;

    /**
     * Get drugbank pharmacology information. For example:
     *
     * <pre>
     * "Lepirudin is used to break up clots and to reduce thrombocytopenia. It binds
     * to thrombin and prevents thrombus or clot formation. It is a highly potent,
     * selective, and essentially irreversible inhibitor of thrombin and clot-bond
     * thrombin. Lepirudin requires no cofactor for its anticoagulant action. Lepirudin
     * is a recombinant form of hirudin, an endogenous anticoagulant found in medicinal leeches."
     * </pre>
     *
     * @return String
     */
    public String getPharmacology() {
        return pharmacology;
    }

    /**
     * Set drugbank pharmacology information. For example:
     *
     * <pre>
     * "Lepirudin is used to break up clots and to reduce thrombocytopenia. It binds
     * to thrombin and prevents thrombus or clot formation. It is a highly potent,
     * selective, and essentially irreversible inhibitor of thrombin and clot-bond
     * thrombin. Lepirudin requires no cofactor for its anticoagulant action. Lepirudin
     * is a recombinant form of hirudin, an endogenous anticoagulant found in medicinal leeches."
     * </pre>
     *
     * @param pharmacology
     */
    public void setPharmacology(String pharmacology) {
        this.pharmacology = pharmacology;
    }

    @NonIndexed
    private String mechanismOfAction;

    /**
     * Description of how the drug works or what it binds to at a molecular level.
     * Example:
     *
     * <pre>
     * "Lepirudin forms a stable non-covalent complex with alpha-thrombin, thereby
     * abolishing its ability to cleave fibrinogen and initiate the clotting cascade.
     * The inhibition of thrombin prevents the blood clotting cascade. "
     * </pre>
     *
     * @return String
     */
    public String getMechanismOfAction() {
        return mechanismOfAction;
    }

    /**
     * Description of how the drug works or what it binds to at a molecular level.
     * Example:
     *
     * <pre>
     * "Lepirudin forms a stable non-covalent complex with alpha-thrombin, thereby
     * abolishing its ability to cleave fibrinogen and initiate the clotting cascade.
     * The inhibition of thrombin prevents the blood clotting cascade."
     * </pre>
     *
     * @param mechanismOfAction
     */
    public void setMechanismOfAction(String mechanismOfAction) {
        this.mechanismOfAction = mechanismOfAction;
    }

    @NonIndexed
    private String toxicity;

    /**
     * Lethal dose (LD50) values from test animals, description of side effects and
     * toxic effects seen in humans.
     * Example: "In case of overdose (eg, suggested by excessively high aPTT values)
     * the risk of bleeding is increased."
     *
     * @return String
     */
    public String getToxicity() {
        return toxicity;
    }

    /**
     * Lethal dose (LD50) values from test animals, description of side effects and
     * toxic effects seen in humans.
     * Example: "In case of overdose (eg, suggested by excessively high aPTT values) the risk
     * of bleeding is increased."
     *
     * @param toxicity
     */
    public void setToxicity(String toxicity) {
        this.toxicity = toxicity;
    }

    @NonIndexed
    private String biotransformation;

    /**
     * Biotransformation is the chemical modification made by an organism on a chemical compound.
     *
     * <pre>
     * "Lepirudin is thought to be metabolized by release of amino acids via catabolic
     * hydrolysis of the parent drug. However, con-clusive data are not available. About
     * 48% of the administration dose is excreted in the urine which consists of unchanged
     * drug (35%) and other fragments of the parent drug."
     * </pre>
     *
     * @return String
     */
    public String getBiotransformation() {
        return biotransformation;
    }

    /**
     * * Biotransformation is the chemical modification made by an organism on a chemical compound.
     *
     * <pre>
     * "Lepirudin is thought to be metabolized by release of amino acids via catabolic
     * hydrolysis of the parent drug. However, con-clusive data are not available. About
     * 48% of the administration dose is excreted in the urine which consists of unchanged
     * drug (35%) and other fragments of the parent drug."
     * </pre>
     *
     * @param biotransformation
     */
    public void setBiotransformation(String biotransformation) {
        this.biotransformation = biotransformation;
    }

    @NonIndexed
    private String absorption;

    /**
     * Description of how much of the drug or how readily the drug is taken up by the body.
     *
     * <pre>
     * "Bioavailability is 100% following injection."
     * </pre>
     *
     * @return String
     */
    public String getAbsorption() {
        return absorption;
    }

    /**
     * Description of how much of the drug or how readily the drug is taken up by the body.
     *
     * <pre>
     * "Bioavailability is 100% following injection."
     * </pre>
     *
     * @param absorption
     */
    public void setAbsorption(String absorption) {
        this.absorption = absorption;
    }

    @NonIndexed
    private String halfLife;

    /**
     * Half life of drug in body, measured in hours or days.
     *
     * <pre>
     * "Approximately 1.3 hours"
     * </pre>
     *
     * @return String
     */
    public String getHalfLife() {
        return halfLife;
    }

    /**
     * * Half life of drug in body, measured in hours or days.
     *
     * <pre>
     * "Approximately 1.3 hours"
     * </pre>
     *
     * @param halfLife
     */
    public void setHalfLife(String halfLife) {
        this.halfLife = halfLife;
    }

    @NonIndexed
    private String proteinBinding;

    /**
     * Percentage of the drug that is bound in plasma proteins.
     *
     * <pre>
     * > db.drugbank.distinct("protein-binding")
     [
     "Other than thrombin and red blood cells, bivalirudin does not bind to\rplasma proteins.",
     "43% to 49%",
     "27.3%",
     "30 to 40%",
     "5% protein bound ",
     "50%",
     "86%",
     "Approximately 25%",
     "1%",
     "Daptomycin is reversibly bound to human plasma proteins, primarily to serum albumin, in a concentration-independent manner. The overall mean binding ranged from 90 to 93%.",
     "Pancrelipase acts locally, so there is minimal plasma protein binding. ",
     "In the plasma, approximately 90% is bound to proteins, primarily lipoproteins. In blood, the distribution is concentration dependent. Approximately 33% to 47% is in plasma, 4% to 9% in lymphocytes, 5% to 12% in granulocytes, and 41% to 58% in erythrocytes. ",
     "96-99%",
     "30%",
     "92%",
     "Very high (to specific plasma proteins called transcobalamins); binding of hydroxocobalamin is slightly higher than cyanocobalamin.",
     "25%",
     "99.9%",
     "60%",
     "90-94%",
     ">99.8%",
     "Very high to plasma protein",
     "<5% of circulating vitamin A is bound to lipoproteins in blood in normal condition, but may be up to 65% when hepatic stores are saturated because of excessive intake. When released from liver, vitamin A is bound to retinol-binding protein (RBP). Most vitamin A circulates in the form of retinol bound to RBP.",
     "Bound to beta-lipoproteins in blood.",
     "22%",
     "50% to 80%",
     "50% bound to human plasma proteins. ",
     "~77-80% (plasma protein)",
     "94 - 97% bound to serum proteins, primarily serum albumin",
     "Protein binding of ramipril is about 73% and that of ramiprilat about 56%. ",
     "Approximately 40% after oral inhalation",
     "15-40%",
     "Less than 5%",
     "< 20%",
     "91�2% bound to plasma proteins when given parenterally. When given orally, it is 85% bound to plasma proteins. ",
     "55% bound to human plasma protein, while the acid metabolite is 10% bound.",
     "83% over the concentration range of 100-1000 ng/ml.",
     "35-50%",
     "76%",
     "Approximately 96.3%",
     "20% bound to plasma proteins. ",
     "24-38%",
     "11 to 12%",
     "> 99% (primarily to serum albumin)",
     "Oseltamivir carboxylate: low (3%), Oseltamivir free base: 42%.",
     "Erythromycin is largely bound to plasma proteins, ranging from 75 - 95% binding depending on the form.",
     "Very high (90%). Cobalamins are extensively bound to two specific plasma proteins called transcobalamin 1 and 2; 70% to transcobalamin 1, 5% to transcobalamin 2.",
     "Low (25 to 36%).",
     "96%",
     "60% -70%",
     "87%",
     "62%",
     "Serum protein binding is variable in the concentration range approximating human exposure, decreasing from 51% at 0.02 &micro;g/mL to 7% at 2 &micro;g/mL.",
     "Binds reversibly (98%) to plasma proteins, mainly to serum albumin and lipoproteins. The binding to albumin and lipoproteins is nonsaturable over a wide concentration range. Ticlopidine also binds to alpha-1 acid glycoprotein (about 15% or less).",
     "50-85%",
     "83%",
     "98%",
     "> 99%",
     "Citalopram, dimethylcitalopram, and didemethylcitalopram is 80% bound to plasma proteins. ",
     "Plasma protein binding is moderate and approximately 85%.",
     "50% bound to serum proteins, independent of drug concentration.",
     "93% bound to albumin",
     "&gt;98%",
     "Over 99.5% bound to plasma protein.",
     "Bound to plasma proteins in varying degrees.",
     "Low, approximately 20%",
     "Lovastatin and its ?-hydroxyacid metabolites are highly protein bound (>95%).  ",
     "97%",
     "40%",
     "Pregabalin does not bind to plasma proteins. ",
     "50-60%",
     "70 to 80%",
     "60% bound to plasma protein. ",
     "45%",
     "20%",
     "About 80% of N-Ac-5-ASA is bound to plasma proteins, whereas 40% of mesalamine is protein bound.",
     "~95% to serum proteins",
     "99%",
     "Moderately bound (40% to 42%) to human plasma proteins in a concentration-independent manner.",
     "70 to 90%",
     "94.9%",
     "Highly protein bound, 90%",
     ">90%",
     "90%",
     "80%",
     "12%",
     "50-70%",
     "40% bound to plasma proteins with a blood-to-plasma ratio of 1:1.",
     "50-80%",
     "95%",
     "15-41% (over the blood concentration range of 0.5 - 250 mg/mL).",
     "Highly bound to plasma proteins (99%) and does not penetrate red blood cells.",
     "96-98%",
     "40%, primarily to albumin.",
     "54%",
     "99.7%",
     "50%-65%",
     "60-80%",
     "Approximately 54% to human serum proteins.",
     "The degree of binding of venlafaxine to human plasma is 27% � 2% at concentrations ranging from 2.5 to 2215 ng/mL. The degree of ODV binding to human plasma is 30% � 12% at concentrations ranging from 100 to 500 ng/mL. Protein-binding-induced drug interactions with venlafaxine are not expected.",
     "90% bound to plasma proteins",
     "At therapeutic concentrations, 98% of atomoxetine in plasma is bound to protein, primarily albumin.",
     "76%, primarily to serum albumin.",
     ">93%",
     "30-40%",
     "94%, mainly to a1-acid glycoprotein",
     "Less than 20%.",
     "Very low: < 0.7% to human plasma proteins and < 7.2% to serum proteins",
     "The plasma protein binding of tranexamic acid is about 3% at therapeutic plasma levels and seems to be fully accounted for by its binding to plasminogen (does not bind serum albumin).",
     "Ertapenem is highly bound to human plasma proteins, primarily albumin, in a concentration-dependent manner. The protein binding of ertapenem decreases as plasma concentrations increase. At a plasma concentration of &lt;100 &micro;g/mL, approximately 95% of ertapenem is protein bound. Protein binding of ertapenem decreases to approximately 85% at an approximate plasma concentration of 300 &micro;g/mL.",
     "98.3%",
     ">99%",
     "65-75%",
     "High (75% [58% to albumin])",
     "~89%",
     "Concentration-dependent, from 90% at 40 �g/mL to 81.5% at 130 �g/mL. Higher than expected free fractions occur in the elderly, in hyperlipidemic patients, and in patients with hepatic and renal diseases. It may also affect the extent of protein binding of other drugs such as phenytoin or carbamazepine. ",
     "90% primarily to serum albumin and alpha 1-acid glycoproteins (independent of drug concentrations).",
     "7-25% bound to plasma proteins. ",
     "93% (to plasma proteins)",
     "Very highly protein bound (90% or more) in plasma and tissues",
     "> 99.9% (to serum albumin)",
     "20-30%",
     "Metformin is negligibly bound to plasma proteins.",
     "Minimally (0 to 9% in vitro) bound to plasma albumin and &alpha;1-acid glycoproteins",
     "In plasma, methadone is predominantly bound to &alpha;1-acid glycoprotein (85% to 90%).",
     "93%",
     "Plasma protein binding is 6-16%",
     "74%-87% (in vitro, bound to plasma proteins)",
     "95% bound to human plasma protein. ",
     "~10% (bound to plasma proteins)",
     "Very high (93%) plasma protein binding",
     "70%",
     "70%-80%",
     "82%-90%",
     "Clobazam is the primary circulating entity in the serum and is highly protein-bound (80-90%). ",
     "Minoxidil does not bind to plasma proteins.",
     "Serum protein binding averaged 56% and is independent of dose. Impaired renal function, 36 to 43%.",
     "13-18%",
     "21-25%",
     "~27%",
     "84%",
     "97% (bound to serum proteins)",
     "55%",
     "6%",
     "85%",
     "~10%",
     "Human plasma protein binding is approximately 91% in in vitro concentrations ranging from 330 to 10,000 &micro;/L.",
     "Not applicable (not hydrolyzed by digestive enzymes and not absorbed).",
     "Very low (< 2%)",
     "97.5%",
     "55-67% (93% for the OH-TA-ester metabolite)",
     "Approximately 100% bound to albumin.",
     "95% binding-plasma proteins",
     "95% bound to plasma protein ",
     "87% to albumin and transcortin",
     "96%-99%",
     "99.5% bound to plasma proteins. ",
     "Approximately 22% bound in human plasma, independent of the concentration. However, Canadian product information states binding to human plasma protein is approximately 56%.",
     "52-59%",
     "80% (mainly to albumin)",
     "89-98%",
     "99.8% bound to plasma proteins, primarily albumin.",
     "About 15% bound to plasma proteins.",
     "35% in plasma",
     "Miglustat does not bind to plasma proteins.",
     "94%",
     "Spironolactone and its metabolites are more than 90% bound to plasma proteins.",
     "Binding to plasma proteins is low (10%-33%). ",
     "92.5 &plusmn; 0.1% (independent of concentration between 40 and 790 ng/mL)",
     "20-25%",
     "91%",
     "91-99%",
     "96 to 99%",
     "Allopurinol and oxypurinol are not bound to plasma proteins",
     "< 10%",
     "More than 99% of the circulating drug is bound to plasma proteins (80% to albumin).",
     "42-46% bound to plasma proteins",
     "Plasma protein binding is negligible (<10%)",
     "Binding of entecavir to human serum proteins in vitro is approximately 13%.",
     "64%",
     "77%",
     "Plasma protein binding is 50-60% in adults and 32% is premature neonates.",
     "> 99% to serum proteins, including thyroxine-binding globulin (TBG), thyroxine-binding prealbumin (TBPA), and albumin (TBA)",
     "60-80% bound to plasma proteins, primarily albumin and &alpha;<sub>1</sub>-acid glycoprotein. The presence of cirrhosis or active viral hepatitis does not appear to affect the extent of protein binding. ",
     "97-99%",
     "65-80%",
     "60-95%",
     "Over 99.9% bound to plasma proteins, primarily albumin.",
     "The active metabolite, 6MNA, is more than 99% bound to plasma proteins.",
     "Enoxacin is approximately 40% bound to plasma proteins in healthy subjects and is approximately 14% bound to plasma proteins in patients with impaired renal function.",
     "Approximately 70%",
     "99% (to plasma proteins)",
     "94.5% bound to human serum proteins, including albumin and alpha-1-glycoprotein. ",
     "73%",
     "Protein binding is greater than 90%.",
     "> 90% to plasma proteins, primarily albumin",
     "Approximately 40% over typical plasma concentrations.",
     "0-11%",
     "30% protein bound. ",
     "97%, primarily to albumin and, to a lesser extent, a<sub>1</sub>-acid glycoprotein. Celecoxib is not preferentially bound to red blood cells. ",
     "Binds to serum protein, mainly albumin.",
     "Sotalol does not bind to plasma proteins.",
     "95% (approximately 70% bound to albumin, 30% bound to alpha 1 -acid glycoprotein)",
     "The protein binding of miglitol is negligible (&lt;4.0%).",
     "Fosinoprilat is &ge;95% protein bound",
     "98% (bind to serum albumin)",
     "30-38%",
     "Darifenacin is approximately 98% bound to plasma proteins (primarily to alpha-1-acid-glycoprotein).",
     "88%",
     "94-96%",
     "15-20%",
     "98-99%",
     "Very High (greater than 99%), bound to proteins. Binding is not affected by degree of renal impairment.",
     "Very high (90% or more).",
     "Approximately 55% serum protein bound.",
     "Cisplatin does not undergo instantaneous and reversible binding to plasma protein that is characteristic of normal drug-protein binding. However, the platinum itself is capable of binding to plasma proteins, including albumin, transferrin, and gamma globulin. Three hours after a bolus injection and two hours after the end of a three-hour infusion, 90% of the plasma platinum is protein bound. ",
     "Not Known",
     "70% bound to plasma protein",
     "Serum protein binding of trandolapril is ~ 80% (independent of concentration and not saturable) while that of trandolaprilat is 65 to 94% (concentration-dependent and saturable).",
     "50-70% bound to erythrocytes, up to 33% bound to plasma proteins, 2-5% of the drug in circulation is unbound",
     "Plasma protein binding of platinum (active metabolite) is irreversible and is greater than 90%, primarily to albumin and gamma-globulins. It is also irreversibly binds to erythrocytes. ",
     "14-17%",
     "93% protein bound to albumin and alpha-1 acid glycoprotein (AAG)",
     "20% of cyclophosphamide is protein bound with no dose dependent changes. Some metabolites are protein bound to an extent greater than 60%. ",
     "60%-70%, binding is independent of concentration.",
     "20 to 40%",
     "Toremifen is primarily bound to albumin (92%), 2% bound to ?1-acid glycoprotein, and 6% bound to  ?1-globulin in the serum.",
     "Highly protein-bound in plasma and tissues.",
     "~75%",
     "benazepril, 97%; benazeprilat, 95%",
     "In vitro tests show that amoxapine binding to human plasma proteins is approximately 90%.",
     "8-12%",
     "82%",
     "No known binding",
     "4%",
     "Zanamivir has limited plasma protein binding (<10%).",
     "Greater than 98% to plasma proteins, mainly albumin.",
     "71% to 89%",
     "50% bound to protein, primarily to albumin",
     "76% bound to plasma proteins. ",
     "The binding of cisatracurium to plasma proteins has not been successfully studied due to its rapid degradation at physiologic pH.",
     "14%",
     "94% <i>in vitro</i> protein binding specifically to ATIII",
     "More than 90%",
     "The protein binding of atropine is 14 to 22% in plasma.",
     "99% to albumin.",
     "20-40%, primarily to albumin",
     "30 to 60%",
     "58%",
     "None",
     "50-60% of enalaprilat is bound to plasma proteins",
     "35%",
     "More than 99%",
     "about 15%",
     "60-70%",
     "Approximately 80% of the drug in the blood is bound to plasma protein.",
     "31%",
     "At 1 mcg/ml concentrations, approximately 93% sulindac and 98% of its sulfide metabolite are bound to human serum albumin.",
     "89.9 &plusmn;1.5%",
     "~55% of the drug in the plasma is bound to nondiffusible plasma constituents",
     "Approximately 30% bound to proteins.",
     "Approximately 45%",
     "Serum protein binding is approximately 80%.",
     "Binding to serum proteins is approximately 30%",
     "Not significant",
     "41-50%",
     "95% protein bound, mostly to albumin and alpha-1-acid glycoprotein. ",
     "68%",
     ">95%",
     "40% of testosterone in plasma is bound to sex hormone-binding globulin and 2% remains unbound and the rest is bound to albumin and other proteins.",
     "99.5-99.75%",
     "The protein binding of nordiazepam in plasma is high (97-98%).",
     "78%",
     "47% bound to plasma proteins, predominantly to albumin.",
     "Extensively bound to plasma proteins.",
     "Highly protein-bound (95% to 97%).",
     "96.7%",
     "Both simvastatin and its ?-hydroxyacid metabolite are highly bound (approximately 95%) to human plasma proteins.",
     "81%",
     "90-95%",
     "Negligible",
     "~15%",
     "25-30%",
     "> 95%",
     "89-95% bound to plasma proteins in vitro",
     "Non detectable",
     "Approximately 80%.",
     "14%-21%",
     "65% (concentration independent)",
     "Highly bound to plasma proteins.",
     "18%",
     "99.7% protein bound, primarily to albumin",
     "Approximately 95%.",
     "Highly bound (&gt;90%) to plasma proteins.",
     "99% bound primarily to albumin",
     "97% protein bound. ",
     "The mean plasma protein bound fraction is approximately 76%, and is concentration-independent.",
     "High",
     "MPA (the active metabolite), at clinically relevant concentrations, is over 98% bound to plasma albumin. The phenolic glucuronide of MPA, mycophenolic acid glucuronide  (MPAG) has 82% protein bound. ",
     "Moexiprilat is approxomately 50% protein bound.",
     "97% binding-albumin",
     "95% bound to plasma proteins",
     "20-60% bound to plasma proteins",
     "Very high (90%). Amprenavir has the highest affinity for alpha(1)-acid glycoprotein.",
     "21% bound to plasma proteins over the therapeutic dose range.",
     "94%-99%",
     "~90%",
     "<36% bound to plasma protein. ",
     "90.9 to 99.5% over an ibandronate concentration range of 2 to 10 ng/mL",
     "> 99% bound, primarily to albumin. Binds to a different primary binding site on albumin than anticoagulants, sulfonamides and phenytoin. ",
     "94.2 +/- 2.1% (binds to serum protein, mainly albumin)",
     "~50%-albumin",
     "~ 95% bound to plasma proteins.",
     "approximately 89% protein bound in human plasma over a concentration range of 0.5 to 50 &micro;g/mL",
     "Norethindrone is 36% bound to sex hormone-binding globulin and 61% bound to albumin. ",
     "&le;4% over the adefovir concentration range of 0.1 to 25 &mu;g/mL",
     "2%-36%",
     "Lisinopril does not appear to be bound to serum proteins other than ACE.",
     "Low",
     "93%-96% (to plasma proteins)",
     "Approximately 30% bound to human plasma proteins.",
     "98% bound to serum proteins, primarily serum albumin and to a lesser extent &alpha;1 acid glycoprotein",
     "No binding to plasma proteins",
     "Risperidone, ~88% bound; 9-hydroxyrisperidone, ~77% bound. ",
     "69%",
     "96% bound to plasma proteins, mainly to albumin and lipoprotein over the clinical concentration range.",
     "Plasma protein binding is low, weak, and transient.",
     "93% bound to plasma proteins, primarily to albumin.",
     "Less than 10% bound to serum proteins <i>in vitro</i>.",
     "> 99% bound, primarily to albumin",
     "69-77%",
     "20 - 67% protein bound",
     "Approximately 2%.",
     "30%-68% protein bound, mainly to albumin. ",
     "None or minimal",
     "98% to 99% (in a concentration range of 5 to 500 ng/mL).",
     "Low (22 to 30%)",
     "Bound in plasma primarily to albumin (81% bound) and to a lesser extent alpha-globulin IV-4 fraction (55% bound).",
     "74%",
     "65%",
     "Approximately 40% of the active 10-monohydroxy metabolite (MHD) is bound to serum proteins, predominantly to albumin. Neither oxcarbazepine nor its MHD binds with alpha-1-acid glycoprotein. ",
     "96%, mainly to alpha1-acid glycoproteins",
     "Nalidixic acid is 93% bound to protein in the blood, and the active metabolite, hydroxynalidixic acid is 63% bound.",
     "9%-33%",
     "At therapeutic levels naproxen is greater than 99% albumin-bound.",
     "Perindoprilat, 10-20% bound to plasma proteins",
     "5%",
     "Candesartan is highly bound to plasma proteins (>99%) and does not penetrate red blood cells.",
     "Low (between 0 and 30%)",
     "The active form of the drug, tazarotenic acid, is highly bound to plasma proteins (>99%).",
     "71-79%",
     "80-85%",
     "99.4% bound, primarily to albumin",
     "95 to 99%, primarily to serum albumin and hemoglobin",
     "High (99%)",
     "49%",
     "60 to 80%",
     "0% (not bound to plasma proteins)",
     "98.5%",
     "21%",
     "23.5%",
     "98% (bound to plasma proteins, albumin and a 1-acid glycoprotein)",
     "80-99%",
     "Not appreciably bound.",
     "Corticosteroids are bound to plasma proteins in varying degrees. ",
     "Cysteamine has a plasma protein binding of 52% and is mostly bound to albumin. ",
     "70-76%",
     "Pseudoephedrine does not bind to human plasma proteins over the concentration range of 50 to 2000 ng/mL",
     "15%",
     ">80% (bound to plasma proteins)",
     "Very high (>90%)",
     "At least 98 to 99% of diflunisal in plasma is bound to proteins.",
     "~99% bound to human plasma protein, primarily to albumin and alpha-1-acid glycoprotein. This is independent of concentration over a range of 5-50 ng/mL. ",
     "75-99%",
     "80-90%",
     "~56%",
     "~33%",
     "Highly bound to plasma proteins (>95%)",
     "Plasma protein binding of eprosartan is high (approximately 98%) and constant over the concentration range achieved with therapeutic doses.",
     "Very low (less than 4%)",
     "Approximately 40% bound to plasma proteins.",
     "Very low",
     "~24%",
     "50 to 80%",
     "Approximately 50% bound to plasma proteins.",
     "100% (after release from dextran)",
     "~85%",
     "70% (bound to plasma proteins)",
     "Low (<5%)",
     "> 98%",
     "70%-76% (Plasma protein binding)",
     "Approximately 88% of bimatoprost is bound in human plasma.",
     "80-88%",
     "40% (at concentrations of 1.0-7.0 &micro;g/mL)",
     "99.8% (bound to plasma proteins)",
     "Plasma protein binding of tinidazole is 12%.",
     ">98% (e.g. to to albumin and &alpha;1-acid glycoprotein)",
     "Approximately 67% bound to plasma proteins over a concentration range of 0.1 to 2.0 &micro;g/mL.",
     "Less than 20% bound to plasma proteins.",
     "73%, to albumin",
     "75%",
     "96% protein bound to alpha- and beta-globulin. ",
     "98% bound to plasma protein.",
     "80.6%",
     "Very high (93%)",
     "More than 99% bound to plasma proteins, predominantly lipoproteins, whereas its active metabolite, acetretin (etretin), is predominantly bound to albumin.",
     "75-78% protein bound",
     "Extensive (> 99.9%), to both human serum albumin and &alpha;-1-acid glycoprotein.",
     "Greater than 99% bound to plasma proteins over a wide drug concentration range.",
     "14-21%",
     "Less than 4%",
     "High (99.5%) to albumin. Decreases as plasma salicylate concentration increases, with reduced plasma albumin concentration or renal dysfunction, and during pregnancy.",
     "99% (mainly VLDL, LDL, and HDL)",
     "16-59%",
     "20-36%",
     "60%-70%",
     "Very low (0-10%)",
     "28%-31% (over the concentration range of 50 to 1000 ng/mL)",
     "15 to 30% for erythromycylamine, the active compound.",
     "Protein-binding of is low and depends on the test conditions (mainly the concentration of cations in the test medium).",
     "As most agents in the 5-ring morphinan group of semi-synthetic opioids bind plasma protein to a similar degree (range 19% [hydromorphone] to 45% [oxycodone]), hydrocodone is expected to fall within this range.",
     "Carboplatin is not bound to plasma protein. However, the platinum itself from carboplatin irreversibly binds to plasma proteins and is slowly eliminated with a minimum half-life of 5 days. ",
     "Mepivacaine is approximately 75% bound to plasma proteins. Generally, the lower the plasma concentration of drug, the higher the percentage of drug bound to plasma.",
     "Approximately 60% (in vitro plasma protein binding).",
     "98.7%",
     "Highly bound to plasma proteins (&gt;99.5%), mainly albumin and a1-acid glycoprotein. Binding is not dose-dependent.",
     "82-87%",
     "Low (less than 20%).",
     "In-vitro studies in human plasma indicate that the plasma protein binding of azelastine and N-desmethylazelastine are approximately 88% and 97%, respectively.",
     ">90% bound to human plasma protein ",
     "60 - 70% bound primarily to human serum albumin",
     "10%",
     "~82% (in human serum)",
     "The binding of formoterol to human plasma proteins in vitro was 61%-64% at concentrations from 0.1 to 100 ng/mL. Binding to human serum albumin in vitro was 31%-38% over a range of 5 to 500 ng/mL. The concentrations of formoterol used to assess the plasma protein binding were higher than those achieved in plasma following inhalation of a single 120 &micro;g dose.",
     "98 to 99%.",
     "13%",
     "No information currently available on protein binding.",
     "90% (mainly ?1-acid glycoprotein and albumin)",
     ">99.5% bound to albumin",
     "Azathioprine and the metabolite mercaptopurine are moderately bound to serum proteins (30%).",
     "Protein binding studies have shown that the degree of aminoglycoside protein binding is low and, depending upon the methods used for testing, may be between 0% and 30%.",
     "Less than 3% of gabapentin circulates bound to plasma protein.",
     "Doxorubicin and its major metabolite, doxorubicinol, is 74-76% bound to plasma protein. The extent to binding is independent of plasma concentration up to 1.1 mcg/mL. Doxorubicin does not cross the blood brain barrier. ",
     "Binding to serum proteins is low (approximately 15%). Reversible binding to blood cells at equilibrium is approximately 60%.",
     "67.9%",
     ">97%",
     "1 to 2%",
     "32% bound to plasma proteins and 47% bound to red blood cells. ",
     "99% bound, primarily to albumin",
     "Approximately 93 to 97% bound to plasma proteins.",
     "&ge;99%",
     "Unchanged drug is ~99% bound to serum proteins; 4-trans-hydroxyglyburide is greater than 97% bound to serum proteins. Protein binding is primarily nonionic making glyburide and is less likely to displace or be displaced by drugs that bind via an ionic mechanism. ",
     "55% to 76%",
     "Approximately 70% bound to plasma proteins, independent of drug concentration.",
     "99%, primarily to the albumin fraction. ",
     ">98%",
     "99% (in vitro, plasma protein binding)",
     "90% bound to serum proteins (primarily albumin and a1-acid glycoprotein) with negligible binding to cellular components of blood.",
     "75-95%",
     "Plasma protein binding averages 19% over the concentration range 10 to 50 &micro;g/mL (a concentration only achieved by intravenous administration of mercaptopurine at doses exceeding 5 to 10 mg/kg).",
     "15 to 20%",
     "Approximately 96.3%.",
     "> 99.5%",
     "~99% (Serum protein binding)",
     "55% and 66% for the (+)R and (&minus;)S enantiomers, respectively.",
     "Moderate to high (60 to 90%), primarily to albumin and, to a lesser extent, alpha 1-acid glycoprotein. 30% is irreversibly bound.",
     "89%",
     "Moderate (approximately 50%)\r",
     "90-99% to whole human plasma and site II of purified albumin, binding appears to be saturable and becomes non-linear at concentrations exceeding 20 mcg/ml.",
     "Bind to serum proteins (45-68%), mainly albumin.",
     ">99.5%",
     "Approximately 10 percent bound to plasma protein.",
     "80 to 85%",
     "10 and 15% (Serum protein binding)",
     "In blood serum, amoxicillin is approximately 20% protein-bound",
     "20 to 46% bound to plasma proteins",
     "91%-93%",
     "n/a",
     "Binding of cefditoren to plasma proteins averages 88% from <i>in vitro</i> determinations, and is concentration-independent at cefditoren concentrations ranging from 0.05 to 10 mg/mL.",
     "98-99%, primarily to albumin.",
     "85% bound to plasma proteins. ",
     "86% bound to human serum proteins (alpha-1-acid glycoprotein and albumin). Protein binding is independent of concentration.",
     "19-29%",
     "Perhexiline and its metabolites are highly protein bound (>90%).",
     "98 to 99%",
     ">98% bound to plasma proteins",
     "Not protein bound ",
     "74-95%",
     ">99% bound to plasma proteins (lipoproteins and albumin were major binding proteins).",
     "98% bound to plasma proteins. At therapeutic concentrations, the protein binding of fluvastatin is not affected by warfarin, salicylic acid and glyburide.",
     ">99.3%",
     "88% bound to plasma proteins (mostly albumin). Binding is reversible and independent of plasma concentrations. ",
     "28-31%",
     "< 60% (mainly albumin)",
     "98% bound to serum proteins, principally to albumin and &alpha;<sub>1</sub>-acid glycoprotein",
     "97% (to human plasma proteins)",
     "Very high, mostly to low-density lipoproteins. It is also extensively bound by globulins and fibrinogens.",
     "50% to serum protein",
     "72%",
     "92-98%",
     "Atovaquone is extensively bound to plasma proteins (99.9%) over the concentration range of 1 to 90 &micro;g/mL.",
     ">96%",
     "Very high (more than 90%) to serum proteins.",
     "94%, highly bound to plasma proteins",
     "Approximately 95% bound to plasma proteins.",
     "Highly bound to albumin (99%) and ?-1 acid glycoprotein (96.6%). ",
     "96.3% (bound to human plasma proteins)",
     "Approximately 75%",
     "Approximately 90% bound to human serum protein (mainly albumin) at plasma concentrations between 1 and 10 mg/L.",
     "Approximately 30%.",
     "24-38% (to plasma proteins)",
     "Binding rates of cefadroxil were 28.1% by U.F. method",
     "Highly (>99%) protein bound in vitro, independent of plasma concentrations over the range of 10 to 100 &micro;g/mL. The primary binding protein is albumin; however, micafungin, at therapeutically relevant concentrations, does not competitively displace bilirubin binding to albumin. Micafungin also binds to a lesser extent to a<sub>1</sub>-acid-glycoprotein.",
     "Doxepin and desmethyldoxepin is 80% protein bound. It is also a lipophillic drug and is capable of crossing the blood-brain-barrier. ",
     "> 99% in human serum albumin",
     "Greater than 99% (in vitro, human plasma proteins).",
     "36%",
     "73-92% bound to plasma proteins",
     ">99% to plasma",
     "84 %",
     "95% (over the concentration range of 18.75 to 1000 ng/mL)",
     "32%",
     "95-98%",
     "99.8%",
     "75% bound",
     "Approximately 50% (primarily to albumin)",
     "20 to 45%",
     "Ifosfamide shows little plasma protein binding. ",
     "Plasma protein binding occurs but is relatively weak. Plasma albumin is the major binding constituent but significant binding of naloxone also occurs to plasma constituents other than albumin.",
     "Very high (99%) with 80% to sex hormone binding globulin, 19% to albumin.",
     "Protein binding is 94-97% following topical administration.",
     "92-94%",
     "26%",
     "Approximately 60%.",
     "25-30% bound to plasma proteins, primarily albumin",
     "90-96% bound to serum albumin",
     "97.7% (bound to plasma proteins)",
     "Very low (<10%)",
     "Protein binding is approximately 50%, mostly (66%) to albumin. Protein binding is reduced in patients with hepatic cirrhosis.",
     "Approximately 60% bound to plasma proteins",
     "Low plasma protein binding in serum at about 45%.",
     "~ 70% protein bound",
     "93% protein bound, independant of concentration.",
     "Approximately 90%",
     "60-70%;",
     "Significant, mostly to albumin.",
     "85-90% protein bound. ",
     "80% bound-albumin",
     "The protein binding of mivacurium has not been determined due to its rapid hydrolysis by plasma cholinesterase.",
     "Approximately 80%",
     "89%-98% bound to plasma protein. The presence of cimetidine, ranitidine, dexamethasone, or diphenhydramine did not affect protein binding of paclitaxel.",
     "Approximately 50% (bound to plasma proteins).",
     "Clomipramine is approximately 97-98% bound to plasma proteins, principally to albumin and possibly to &alpha;<sub>1</sub>-acid glycoprotein. Desmethylclomipramine is 97-99% bound to plasma proteins. ",
     "In vitro studies show that 94% protein bound, mainly to a1-acid glycoprotein, albumin, and lipoproteins. When measured in cancer patients, docetaxel is 97% bound to plasma protein. Dexamethasone does not affect the protein binding of docetaxel.",
     "Olsalazine and olsalazine-S are more than 99% bound to plasma proteins. Mesalamine (5-ASA) is 74% bound to plasma proteins.",
     "Retapamulin is approximately 94% bound to human plasma proteins, and the protein binding is independent of concentration.",
     "47-51%",
     "Highly bound (&gt;99%) to albumin and alpha-1 acid glycoprotein",
     "The fraction of sitagliptin reversibly bound to plasma proteins is low (38%).",
     "Plasma protein binding of decitabine is negligible (<1%). ",
     "Posaconazole is highly protein bound (>98%), predominantly to albumin.",
     "Darunavir is approximately 95% bound to plasma proteins. Darunavir binds primarily to plasma alpha 1-acid glycoprotein (AAG).",
     "In vitro binding of telbivudine to human plasma proteins is low (3.3%).",
     "The plasma protein binding of racemic paliperidone is 74%.",
     "Binding of sunitinib and its primary metabolite to human plasma protein in vitro was 95% and 90%, respectively.",
     "Plasma protein binding is insignificant.",
     "The binding of arformoterol to human plasma proteins in vitro was 52-65% at concentrations of 0.25, 0.5 and 1.0 ng/mL of radiolabeled arformoterol.",
     "In blood, IGF-1 is bound to six IGF binding proteins, with > 80% bound as a complex with IGFBP-3 and an acid-labile subunit.",
     "Pramlintide does not extensively bind to blood cells or albumin (approximately 40% of the drug is unbound in plasma).",
     "Nelarabine and ara-G are not substantially bound to human plasma proteins (<25%) in vitro, and binding is independent of nelarabine or ara-G concentrations up to 600 mM.",
     "Highly bound to plasma proteins (>= 98%).",
     "With a serum concentrations of 17 mcg/mL, adults and children have about 56% theophylline bound to plasma protein, and premature infants have about 36%.\r",
     "<10% bound to plasma proteins. ",
     "> 98% bound to albumin",
     "Very high (approximately 90%); primarily bound to alpha 1 -acid glycoprotein. Higher amounts of unbound amprenavir present as amprenavir serum concentrations increase.",
     "Extensively bound (95% to 99%) to human plasma proteins, primarily albumin.",
     "74-86%",
     "98% bound to plasma proteins.",
     "The degree of reversible protein binding varies with the serum concentration from 93% at 25 mcg/mL to 90% at 250 mcg/mL and 82% at 500 mcg/mL. Cefotetan is 88% plasma protein bound.",
     "Cefotetan is 88% plasma protein bound.",
     "30% protein bound",
     "77 to 91%",
     "80-98% bound to plasma proteins. Extensively bound to Alpha-1-acid glycoprotein 1. ",
     "Plasma protein binding ranges from 88-94% with mean extent of binding of 61-63% to human albumin over the concentration range of 1-100 ng/ml.",
     "Moderate.",
     "90% bound to plasma proteins.",
     "&ge; 99%, primarily to alpha 1-acid glycoprotein.",
     "94-96% of bezafibrate is bound to protein in human serum.",
     "Low to moderate (30 to 50%).",
     "95-97%",
     "Salicylate: 90-95% bound at plasma salicylate concentrations <100 mcg/mL; 70-85% bound at concentrations of 100-400 mcg/mL; 25-60% bound at concentrations >400 mcg/mL.",
     "Protein binding to human serum albumin ranges from 15 to 25 percent.",
     "72% bound to plasma proteins.",
     "The percentage of ciclesonide and des-ciclesonide bound to human plasma proteins averaged &ge; 99% each, with &le; 1% of unbound drug detected in the systemic circulation.",
     "The serum protein binding of cefepime is approximately 20% and is independent of its concentration in serum.",
     "23 to 38%",
     "Ceftibuten is 65% bound to plasma proteins. The protein binding is independent of plasma ceftibuten concentration.",
     "22 to 33% in serum and from 21 to 29% in plasma.",
     "98.7% protein bound, mainly to albumin",
     "10 to 49%",
     "0% (morphine metabolite 35%)",
     ">90% protein bound.[5] ",
     "70% ",
     "99% bound to plasma proteins.",
     "~ 74% in both healthy patients and those with moderate hepatic impairment.  ",
     "Solifenacin is approximately 98% (in vivo) bound to human plasma proteins, principally to alpha1-acid glycoprotein.",
     "20% binds to plasma proteins",
     "Lopinavir is highly bound to plasma proteins (98-99%).",
     "Deferasirox is highly (~99%) protein bound almost exclusively to serum albumin.",
     "Plasma protein binding of ganciclovir is 1% to 2% over concentrations of 0.5 and 51 mg/mL.",
     "Approximately 45%.",
     "Approximately 98%.",
     "Protein binding decreases with increased plasma concentrations. Range, 28 to 86% (average, 70 to 75%). Albumin is not thought to be the primary binding component.",
     "Roflumilast is 99% plasma protein bound.",
     "Moderate",
     "71%",
     "Approximately 30%",
     "97 to 99%",
     "Strong affinity towards protein binding.",
     ">97.5%",
     "Approximately 99.7%",
     "Variable. Plasma protein binding of rapacuronium was studied in vitro for human plasma by equilibrium dialysis. The protein binding was variable and ranged between 50% and 88%, which was at least partly due to hydrolysis of rapacuronium bromide to its 3-hydroxy metabolite. The specific plasma protein to which rapacuronium binds is unknown. Plasma protein binding of the 3-hydroxy metabolite was not determined.",
     "Approximately 76% bound to human plasma proteins, with moderate affinity for albumin and alpha-1 acid glycoprotein.",
     "99% bound to plasma proteins",
     "Tetrabenazine = 82 - 88%;\r?-HTBZ = 60 - 68%;\r?-HTBZ = 59 - 63%.   ",
     "67-77%",
     "Dronedarone and its N-debutyl metabolite is >98% protein bound - mainly to albumin. ",
     "Plasma protein binding is equal or less than 50%.",
     "Lorcaserin hydrochloride has a plasma protein binding of approximately 70%.",
     "9.3%",
     "55.4% mean plasma protein binding with 10 mg oral dose. Extent of protein binding is independent of plasma drug concentration. ",
     "Plasma protein binding is 13%. ",
     ">99% bound to serum proteins ",
     "Since crofelemer is not significantly absorbed, protein binding was not quantified.",
     "Over 99%, predominantly to serum albumin.",
     "95% of iloperidone is bound to protein. Percent bound is not altered by renal or hepatic impairment or combination therapy with ketoconazole. ",
     "80 to 90%",
     "Approximately 80% protein bound. ",
     "There is no plasma protein binding quantity since ingenol mebutate is a topical treatment",
     "The in vitro human serum and plasma protein binding was 94.1-95.3% and 95.1-96.2%, respectively.",
     "94 to 98%",
     "92% in vitro and 89.5% in vivo. ",
     "about 90%.",
     "59% to 76% and binds to alpha 1-acid glycoprotein and albumin in a concentration dependent manner. ",
     "Plasma protein binding for mipomersen is greater than or equal to 90%.",
     "DM1 has a plasma protein binding value of 93%.",
     ">99% protein bound to alpha-1-acid glycoprotein and albumin. ",
     "90% to 95%",
     "Almost 100%",
     "Canakinumab binds to plasma IL-1?, but plasma protein binding was not quantified.",
     "26.3% - 34.8% with 90% binding to albumin (27%).",
     "Alogliptin is 20% bound to plasma proteins.",
     "~20% ",
     "97% bound to protein ",
     "Approximately 98% of the active metabolite was bound to human serum albumin in a 4% buffered solution. The major inactive metabolites are also highly bound to human plasma proteins.",
     "Eltrombopag is highly protein bound (>99%).",
     "99% bound ",
     "95% protein bound ",
     "<15%",
     "Plasma protein binding is about 92% to 95%",
     "99% bound to plasma protein. Protein binding is independent of total drug concentrations, age, renal and hepatic function.",
     "99% +",
     "80% to 90% of systemically available alvimopan is bound to plasma protein.",
     "87% bound to plasma proteins <i>in vitro</i> at a concentration of 100 ng/ml",
     "Low (17%)",
     "The in vitro protein binding of saxagliptin and its active metabolite in human serum is negligible (<10%).",
     ">90% to serum albumin in a concentration independent manner (despite being highly protein bound, antimicrobial activity of telavancin is not affected)  ",
     ">99% protein bound, independent of concentrations over a range of 10-100 ?g/mL. ",
     "approximately 20%. ",
     "Apixaban is about 87% plasma protein bound.",
     "94% bound to human plasma proteins in vitro. 96% bound to human plasma proteins in healthy subjects ex vivo. Extent of protein binding is not concentration-dependent. ",
     "Plasma protein binding for axitinib is high at over 99% with most protein binding to albumin followed by ?1-acid glycoprotein.",
     "10 mg extended release = 1-3% protein bound ",
     "Plasma protein binding is 88%.",
     "Plasma protein binding was not quantified.",
     "96-99% protein bound",
     "Relatively low binding (34-35%) to plasma proteins. ",
     "3-12%",
     "Artemether and lumefantrine are both highly bound to human serum proteins in vitro (95.4% and 99.7%, respectively). Dihydroartemisinin is also bound to human serum proteins (47% to 76%). ",
     "Very low.",
     "90% of the drug is bound to plasma proteins.",
     "~ 30%, protein binding is independent of drug concentration. ",
     "5-HMT: 50% to albumin and alpha1-acid glycoprotein ",
     "No particular protein binding is displayed.",
     "99.7% bound",
     "Both fospropofol and its active metabolite propofol are highly protein bound (approximately 98%), primarily to albumin. Fospropofol does not affect the binding of propofol to albumin.  ",
     "95% +",
     "Calcium acts as a co-factor to numerous enzymes. ",
     "Lornoxicam is 99% bound to plasma proteins (almost exlusively to serum albumin).",
     "80 to 95%",
     "The protein binding of alcaftadine and the active metabolite are 39.2% and 62.7% respectively.",
     "None ",
     "Cabazitaxel is mainly bound to human serum albumin (82%) and lipoproteins (88% for HDL, 70% for LDL, and 56% for VLDL).",
     "Less than unfractionated heparin, which is more than 90%.",
     "11% to 15% bound to human plasma proteins. ",
     "Amfenac has high affinity toward serum albumin proteins. In vitro, the percent bound to human albumin and human serum was 95.4% and 99.1% respectively.",
     "58% ",
     "There is no evidence of protein binding, nor is there any evidence of metabolism of the carbohydrate moiety of the drug to carbon dioxide and water with loss through respiration.",
     "67 - 86% bound to plasma protein, albumin is the major binder. Does not significantly displace substrates from proteins. ",
     "Low protein binding compared to unfractionated heparin. ",
     "5-7%",
     "Much lower compared to heparin, which has over 90% protein bound.",
     "Binds to plasma proteins almost entirely (99%)",
     "~99% bound to serum proteins. ",
     "Both ticagrelor and AR-C124910XX are bound to plasma proteins (>99.7%), and both are pharmacologically active.",
     "Ivacaftor is approximately 99% bound to plasma proteins, primarily to alpha 1-acid glycoprotein and albumin. Ivacaftor does not bind to human red blood cells.",
     "Azilsartan medoxomil is 99% plasma protein bound.",
     "Not plasma protein bound if administered topically.",
     "Plasma protein binding is less than 10%.",
     "Plasma protein binding is about 99.8%",
     "Vismodegib is highly protein bound with plasma protein binding at about 99%. Vismodegib binds to the plasma proteins, albumin and alpha-1-acid glycoprotein (saturable bnding).",
     "99.5%, including those with renal impairment. ",
     ">99% protein bound in human plasma, mainly to albumin and alpha 1-acid glycoprotein. ",
     ">99% protein bound. ",
     ">99.7%",
     "MMAE has a plasma protein binding range of 68-82%, and highly-protein bound drugs are not likely to displace it.",
     "49 to 65%. ",
     "Gabapentin plasma protein binding is less than 3%.",
     "Cabozantinib has extensive plasma protein binding (� 99.7%).",
     "97% protein bound, primarily to albumin. ",
     "Teriflunomide is extensively plasma protein bound(>99%).",
     ">99% protein bound to serum albumin and alpha-1 acid glycoprotein. ",
     "70-80% protein bound, the extent to which is concentration dependent. Because of the propensity of linagliptin to bind to plasma protein, it has a long terminal half-life and a non-linear pharmacokinetic profile. In contrast, other DPP-4 inhibitors have linear pharmacokinetic profiles which makes linagliptin unique. ",
     "Perampanel is 95-96% plasma protein bound with most binding to the plasma proteins ?1-acid glycoprotein and albumin.\r",
     "<10% protein bound. Because it is more protein bound than other gadolinium-based contrast agents, gadoxetate disodium has increased T1 relaxivity. This results in an enhancement of the signal. ",
     "Over the concentration range of 0.4 - 4 micromolar, carfilzomib was 97% protein bound. ",
     "71% bound to plasma proteins. It binds to albumin and alpha-1-acid glycoprotein with moderate affinity. ",
     "Peginesatide does not bind to serum albumin or lipoprotein as demonstrated in in-vitro studies. ",
     "Regorafenib is highly bound (99.5%) to human plasma proteins.",
     "Enzalutamide is 97% to 98% bound to plasma proteins, primarily albumin. N-desmethyl enzalutamide is 95% bound to plasma proteins. ",
     "> 99% bound to plasma proteins. ",
     ">99.9 bound to plasma proteins. ",
     ">99% protein bound, mainly to albumin. It also binds to alpha-acid glycoprotein.  Protein binding is independent of canagliflozin plasma concentrations. Plasma protein binding is not meaningfully altered in patients with renal or hepatic impairment.",
     "MMF has a plasma protein binding range of 27 to 45%, and the binding is concentration independent.\r\r",
     "PBA = 80.6% to 98.0%; \rPAA = 37.1% to 65.6%;\rPAGN = 7% to 12%. ",
     "12-44% protein bound. It is not concentration dependent. ",
     "97.4% bound to human plasma proteins. ",
     "99.7% bound to human plasma protein. ",
     "There is negligible plasma protein binding.",
     "5% protein bound\r",
     "22% bound to human plasma protein over concentration range of 10 to 1000 ng/mL. "
     ]
     >

     * </pre>
     *
     * @return String
     */
    public String getProteinBinding() {
        return proteinBinding;
    }

    /**
     * Percentage of the drug that is bound in plasma proteins.
     *
     * @param proteinBinding
     */
    public void setProteinBinding(String proteinBinding) {
        this.proteinBinding = proteinBinding;
    }

    @NonIndexed
    private String routeOfElimination;

    /**
     * Route by which the drug is eliminated. Drugs are cleared primarily by the
     * liver and kidneys.
     *
     * <pre>
     * "Lepirudin is thought to be metabolized by release of amino acids via catabolic
     * hydrolysis of the parent drug. About 48% of the administration dose is excreted
     * in the urine which consists of unchanged drug (35%) and other fragments of the
     * parent drug."
     * </pre>
     *
     * @return String
     */
    public String getRouteOfElimination() {
        return routeOfElimination;
    }

    /**
     * Route by which the drug is eliminated. Drugs are cleared primarily by the
     * liver and kidneys.
     *
     * <pre>
     * "Lepirudin is thought to be metabolized by release of amino acids via catabolic
     * hydrolysis of the parent drug. About 48% of the administration dose is excreted
     * in the urine which consists of unchanged drug (35%) and other fragments of the
     * parent drug."
     * </pre>
     *
     * @param routeOfElimination
     */
    public void setRouteOfElimination(String routeOfElimination) {
        this.routeOfElimination = routeOfElimination;
    }

    @NonIndexed
    private String volumeOfDistribution;

    /**
     * The apparent volume of distribution is the theoretical volume of fluid into
     * which the total drug administered would have to be diluted to produce the
     * concentration in plasma.
     *
     * <pre>
     * "* 12.2 L [Healthy young subjects (n = 18, age 18-60 years)]\r* 18.7 L
     * [Healthy elderly subjects (n = 10, age 65-80 years)]\r* 18 L [Renally
     * impaired patients (n = 16, creatinine clearance below 80 mL/min)]\r* 32.1 L
     * [HIT patients (n = 73)]"
     * </pre>
     *
     * @return String
     */
    public String getVolumeOfDistribution() {
        return volumeOfDistribution;
    }

    /**
     * The apparent volume of distribution is the theoretical volume of fluid into
     * which the total drug administered would have to be diluted to produce the
     * concentration in plasma.
     *
     * <pre>
     * "* 12.2 L [Healthy young subjects (n = 18, age 18-60 years)]\r* 18.7 L
     * [Healthy elderly subjects (n = 10, age 65-80 years)]\r* 18 L [Renally
     * impaired patients (n = 16, creatinine clearance below 80 mL/min)]\r* 32.1 L
     * [HIT patients (n = 73)]"
     * </pre>
     *
     * @param volumeOfDistribution
     */
    public void setVolumeOfDistribution(String volumeOfDistribution) {
        this.volumeOfDistribution = volumeOfDistribution;
    }

    @NonIndexed
    private String clearance;

    /**
     * Clearance is a descriptive term used to evaluate efficiency of drug removal from the body.
     *
     * <pre>
     * "* 164 ml/min [Healthy 18-60 yrs]\r* 139 ml/min [Healthy 65-80 yrs]\r* 61 ml/min
     * [renal impaired]\r* 114 ml/min [HIT (Heparin-induced thrombocytopenia)]
     * </pre>
     *
     * @return String
     */
    public String getClearance() {
        return clearance;
    }

    /**
     * Clearance is a descriptive term used to evaluate efficiency of drug removal from the body.
     *
     * <pre>
     * "* 164 ml/min [Healthy 18-60 yrs]\r* 139 ml/min [Healthy 65-80 yrs]\r* 61 ml/min
     * [renal impaired]\r* 114 ml/min [HIT (Heparin-induced thrombocytopenia)]
     * </pre>
     *
     * @param clearance
     */
    public void setClearance(String clearance) {
        this.clearance = clearance;
    }

    @SubList (indexName=IndexNames.SECONDARY_ACCESSION_NUMBERS)
    private List<String> secondaryAccessionNumbers;

    /**
     * Add an accession number to the list.
     *
     * <pre>
     *     	"secondary-accession-numbers" : {
     *   "secondary-accession-number" : "DB02584"
     *    }
     * </pre>
     *
     * <pre>
     * "secondary-accession-numbers" : [
     "BIOD00024",
     "BTD00024"
     ],
     * </pre>
     *
     * @param secondaryAccessionNumber
     */
    public void addSecondaryAccessionNumber(String secondaryAccessionNumber) {
        if (secondaryAccessionNumbers == null) {
            secondaryAccessionNumbers = new ArrayList<String>();
        }
        secondaryAccessionNumbers.add(secondaryAccessionNumber);
    }

    @Indexed (indexName=IndexNames.DRUG_KINGDOM)
    private String kingdom;

    @SubList (indexName=IndexNames.DRUG_SUBSTRUCTURES)
    private List<String> substructures;

    /**
     * Drugbank taxonomy kingdom metadata.
     *
     * <pre>
     * db.drugbank.distinct("taxonomy.kingdom")
     [ "Organic", "Inorganic" ]
     * </pre>
     *
     * @return String
     */
    public String getKingdom() {
        return kingdom;
    }

    /**
     * * Drugbank taxonomy kingdom metadata.
     *
     * <pre>
     * db.drugbank.distinct("taxonomy.kingdom")
     [ "Organic", "Inorganic" ]
     * </pre>
     *
     * @param kingdom
     */
    public void setKingdom(String kingdom) {
        this.kingdom = kingdom;
    }

    /**
     * Drugbank taxonomy substructures metadata.
     *
     * <pre>
     * Taken from db.drugbank.distinct("taxonomy.substructures")
     *
     * "Monolignols",
     "Thiazepines",
     "Taxanes",
     "Prostacyclins",
     "Triazolopyrazines",
     "Cycloheptenes",
     "Azo compounds",
     "Bile Acids",
     "Thioamides",
     "Triazolidines",
     "Indolizines",
     * </pre>
     *
     * @return
     */
    public List<String> getSubstructures() {
        return substructures;
    }

    /**
     * * Drugbank taxonomy substructures metadata.
     *
     * <pre>
     * Taken from db.drugbank.distinct("taxonomy.substructures")
     *
     * "Monolignols",
     "Thiazepines",
     "Taxanes",
     "Prostacyclins",
     "Triazolopyrazines",
     "Cycloheptenes",
     "Azo compounds",
     "Bile Acids",
     "Thioamides",
     "Triazolidines",
     "Indolizines",
     * </pre>
     *
     * @param substructures
     */
    public void setSubstructures(List<String> substructures) {
        this.substructures = substructures;
    }

    @SubList (indexName=IndexNames.DRUG_SYNONYMS)
    private List<String> synonyms;

    /**
     * Get the drugbank synonyms.
     *
     * <pre>
     * "synonyms" : {
     "synonym" : "Hirudin variant-1"
     },
     </pre>
     * <pre>
     *      "synonyms" : [
     "FMM",
     "GW 572016",
     "GW572016",
     "Lapatinib ditosylate",
     "Lapatinib tosilate hydrate"
     ],

     * </pre>
     *
     * @return List<String>
     */
    public List<String> getSynonyms() {
        return synonyms;
    }

    /**
     * Set the drugbank synonyms.
     *
     * @param synonyms
     */
    public void setSynonyms(List<String> synonyms) {
        this.synonyms = synonyms;
    }

    @SubList (indexName=IndexNames.DRUG_SALTS)
    private List<String> salts;

    /**
     * Get the drugbank salts.
     *
     * <pre>
     * > db.drugbank.distinct("salts")
     [
     "Leuprolide acetate",
     "Leuprorelin acetate",
     {
     "salt" : "Goserelin acetate"
     },
     {
     "salt" : "Desmopressin acetate"
     },
     {
     "salt" : "Cetrorelix Acetate"
     },
     {
     "salt" : "Octreotide acetate"
     },
     {
     "salt" : "pyridoxine hydrochloride"
     },
     {
     "salt" : "Pravastatin Sodium "
     },
     {
     "salt" : "Phentermine Hydrochloride"
     },
     {
     "salt" : "Tramadol Hydrochloride "
     },
     {
     "salt" : "Ticlopidine Hydrochloride"
     },
     {
     "salt" : "Citalopram Hydrobromide"
     },
     {
     "salt" : "Phenytoin Sodium "
     },
     {
     "salt" : "Venlafaxine Hydrochloride "
     },
     {
     "salt" : "Morphine Sulfate "
     },
     {
     "salt" : "Tenofovir Disoproxil Fumarate"
     },
     "Divalproex Sodium",
     "Valproate Semisodium",
     "Codeine Phosphate ",
     "Codeine Sulfate ",
     {
     "salt" : "Amitriptyline Hydrochloride"
     },
     {
     "salt" : "Metformin Hydrochloride"
     },
     {
     "salt" : "Omeprazole Magnesium "
     },
     {
     "salt" : "Sorafenib Tosylate "
     },
     {
     "salt" : "Rosiglitazone Maleate"
     },
     {
     "salt" : "Pramipexole hydrochloride"
     },
     {
     "salt" : "Promazine HCL"
     },
     {
     "salt" : "Methylphenidate Hydrochloride "
     },
     {
     "salt" : "Gemcitabine Hydrochloride"
     },
     "Imipramine Hydrochloride",
     "Imipramine Pamoate",
     {
     "salt" : "Fluoxetine Hydrochloride "
     },
     {
     "salt" : "Chlorpromazine hydrochloride"
     },
     {
     "salt" : "Caspofungin acetate"
     },
     {
     "salt" : "Erlotinib Hydrochloride"
     },
     {
     "salt" : "Methotrexate Sodium"
     },
     {
     "salt" : "Chloroquine Phosphate"
     },
     "Demeclocycline HCL",
     "Demeclocycline hydrochloride",
     "Demethylchlortetracycline hydrochloride",
     {
     "salt" : "Imatinib Mesylate "
     },
     {
     "salt" : "Acamprosate Calcium "
     },
     {
     "salt" : "Tamoxifen Citrate"
     },
     {
     "salt" : "Losartan Potassium "
     },
     {
     "salt" : "Midazolam Hydrochloride "
     },
     {
     "salt" : "Mycophenolate Sodium"
     },
     {
     "salt" : "delavirdine mesylate"
     },
     "Paroxetine Hydrochloride ",
     "Paroxetine Mesylate ",
     {
     "salt" : "Norethindrone Acetate "
     },
     "Scopolamine Bromide",
     "Scopolamine Hydrobromide",
     "Scopolamine Hydrobromide Trihydrate",
     "Scopolamine Hyoscine",
     {
     "salt" : "Clopidogrel Bisulfate "
     },
     {
     "salt" : "Irinotecan Hydrochloride"
     },
     {
     "salt" : "Etoposide Phosphate "
     },
     "Estradiol acetate",
     "Estradiol benzoate",
     "Estradiol cypionate",
     "Estradiol valerate",
     {
     "salt" : "Acyclovir Sodium "
     },
     {
     "salt" : "Nalbuphine hydrochloride"
     },
     "CYSTEAMINE BITARTRATE ",
     "CYSTEAMINE HYDROCHLORIDE",
     "pseudoephedrine hydrochloride",
     "pseudoephedrine sulfate",
     {
     "salt" : "Tacrolimus Hydrate "
     },
     "Chlorhexidine acetate",
     "Chlorhexidine gluconate",
     "Chlorhexidine hydrochloride",
     {
     "salt" : "Buprenorphine Hydrochloride "
     },
     {
     "salt" : "Azelastine Hydrochloride"
     },
     {
     "salt" : "Azathioprine Sodium"
     },
     {
     "salt" : "Doxorubicin Hydrochloride"
     },
     "Diphenhydramine Hcl",
     "Diphenhydramine Salicylate",
     {
     "salt" : "Atorvastatin Calcium"
     },
     {
     "salt" : "Fluvastatin Sodium "
     },
     {
     "salt" : "Rosuvastatin Calcium "
     },
     {
     "salt" : "Doxepin Hydrochloride "
     },
     "Desipramine Hydrochloride ",
     "Desmethylimipramine Hydrochloride",
     {
     "salt" : "Propafenone hydrochloride"
     },
     {
     "salt" : "Naloxone Hydrochloride "
     },
     {
     "salt" : "Flecainide acetate"
     },
     {
     "salt" : "Mitoxantrone hydrochloride"
     },
     {
     "salt" : "Clomipramine Hydrochloride "
     },
     {
     "salt" : "Trimeprazine tartrate"
     },
     {
     "salt" : "Sunitinib Malate "
     },
     {
     "salt" : "Pramlintide acetate"
     },
     {
     "salt" : "Penbutolol sulfate"
     },
     {
     "salt" : "Neostigmine methylsulfate"
     },
     {
     "salt" : "Heroin hydrochloride"
     },
     {
     "salt" : "Difenoxin hydrochloride"
     },
     {
     "salt" : "?-ethyltryptamine acetate"
     },
     {
     "salt" : "N-2-cyanoethylamphetamine hydrochloride"
     },
     "Dihydrocodeine bitartrate",
     "Dihydrocodeine hydrobromide",
     "Dihydrocodeine hydrochloride",
     "Dihydrocodeine hydroiodide",
     "Dihydrocodeine methyliodide",
     "Dihydrocodeine phosphate",
     "Dihydrocodeine sulfate",
     "Dihydrocodeine tartrate",
     {
     "salt" : "Dextroamphetamine sulfate"
     },
     "ferric citrate",
     "ferric hydroxide",
     "ferric sodium citrate",
     "ferroglycine sulfate",
     "ferrous ascorbate",
     "ferrous aspartate",
     "ferrous carbonate",
     "ferrous chloride ",
     "ferrous fumarate",
     "ferrous gluconate",
     "ferrous glycine sulfate",
     "ferrous iodine",
     "ferrous succinate",
     "ferrous sulfate",
     "iron(II) sulfate",
     "saccharated iron oxide",
     "sodium feredetate",
     {
     "salt" : "Hydroxychloroquine sulfate"
     },
     {
     "salt" : "Alverine citrate"
     },
     {
     "salt" : "Lincomycin hydrochloride"
     },
     {
     "salt" : "D-Calcium Pantothenate"
     },
     {
     "salt" : "Estriol tripropionate"
     },
     {
     "salt" : "Dronedarone Hydrochloride "
     },
     {
     "salt" : "lorcaserin hydrochloride"
     },
     {
     "salt" : "Bepotastine Besilate "
     },
     {
     "salt" : "Milnacipran Hydrochloride "
     },
     {
     "salt" : "Indacaterol Maleate"
     },
     {
     "salt" : "Ecabet sodium"
     },
     {
     "salt" : "Rotigotine hydrochloride"
     },
     {
     "salt" : "mipomersen sodium"
     },
     {
     "salt" : "Abiraterone Acetate "
     },
     {
     "salt" : "Icatibant Acetate "
     },
     {
     "salt" : "Alogliptin Benzoate "
     },
     {
     "salt" : "Tapentadol Hydrochloride "
     },
     {
     "salt" : "Prasugrel Hydrochloride "
     },
     {
     "salt" : "Eltrombopag Olamine"
     },
     {
     "salt" : "Asenapine Maleate "
     },
     {
     "salt" : "Saxagliptin Hydrochloride "
     },
     {
     "salt" : "Telavancin Hydrochloride "
     },
     {
     "salt" : "Pazopanib Hydrochloride "
     },
     {
     "salt" : "pasireotide diaspartate"
     },
     {
     "salt" : "Vilazodone hydrochloride"
     },
     {
     "salt" : "Dabigatran etexilate mesilate "
     },
     {
     "salt" : "Degarelix Acetate "
     },
     {
     "salt" : "Desvenlafaxine Succinate "
     },
     {
     "salt" : "Fesoterodine Fumarate "
     },
     {
     "salt" : "Iobenguane Sulfate I-123"
     },
     {
     "salt" : "Naphazoline hydrochloride"
     },
     {
     "salt" : "Fospropofol Disodium "
     },
     {
     "salt" : "Fosaprepitant dimeglumine"
     },
     {
     "salt" : "Sodium Benzyloxide"
     },
     {
     "salt" : "Besifloxacin hydrochloride "
     },
     {
     "salt" : "Methylnaltrexone Bromide "
     },
     {
     "salt" : "Lurasidone Hydrochloride "
     },
     "Azilsartan Kamedoxomil",
     "Azilsartan Medoxomil Potassium",
     {
     "salt" : "Lomitapide mesylate"
     },
     {
     "salt" : "Temocapril HCl"
     },
     "Tetraethylammonium bromide",
     "Tetraethylammonium chloride",
     "Tetraethylammonium chloride hydrate",
     "tetraethylammonium hydroxide",
     "tetraethylammonium iodide",
     "tetraethylammonium phosphate ",
     {
     "salt" : "agmatine sulphate"
     },
     {
     "salt" : "Guvacine hydrochloride "
     },
     {
     "salt" : "Pitavastatin Calcium "
     },
     {
     "salt" : "Rilpivirine Hydrochlorie"
     },
     {
     "salt" : "Ruxolitinib Phosphate "
     },
     {
     "salt" : "Gadoxetate Disodium "
     },
     {
     "salt" : "Linaclotide Acetate"
     },
     {
     "salt" : "Peginesatide acetate "
     },
     {
     "salt" : "Aclidinium Bromide"
     },
     {
     "salt" : "Ponatinib Hydrochloride"
     },
     {
     "salt" : "Bedaquiline Fumarate "
     },
     {
     "salt" : "Trametinib Dimethyl Sulfoxide "
     },
     {
     "salt" : "Dabrafenib Mesylate "
     },
     {
     "salt" : "Afatinib Dimaleate"
     },
     {
     "salt" : "Levomilnacipran Hydrochloride"
     }
     ]

     * </pre>
     *
     * @return List<String>
     */
    public List<String> getSalts() {
        return salts;
    }

    /**
     * Set the drugbank salts.
     *
     * @param salts
     */
    public void setSalts(List<String> salts) {

        this.salts = salts;
    }

    @SubList (indexName=IndexNames.DRUG_BRANDS)
    private List<String> brands;

    /**
     * Get the drugbank drug brands.
     *
     * <pre>
     * "brands" : {
     "brand" : "Refludan"
     },
     * </pre>
     *
     * @return List<String>
     */
    public List<String> getBrands() {
        return brands;
    }

    /**
     * Set the drugbank drug brands.
     *
     * @param brands
     */
    public void setBrands(List<String> brands) {
        this.brands = brands;
    }

    @SubMap (indexName=IndexNames.DRUG_MIXTURES)
    private Map<String, String> mixtures;

    /**
     * Drugbank drug mixtures.
     *
     * <pre>
     * {
     "name" : "Sciatinix HP",
     "ingredients" : "Aconitinum + Arnica Montana + Colchicine + Esculin + Hypericum Perforatum + Magnesium Phosphate Dibasic + Plumbum Metallicum + Poison Ivy"
     },
     * </pre>
     *
     * @return Map<String, String>
     */
    public Map<String, String> getMixtures() {
        return mixtures;
    }

    /**
     * Drug bank drug mixtures.
     *
     * <pre>
     * {
     "name" : "Sciatinix HP",
     "ingredients" : "Aconitinum + Arnica Montana + Colchicine + Esculin + Hypericum Perforatum + Magnesium Phosphate Dibasic + Plumbum Metallicum + Poison Ivy"
     },
     * </pre>
     *
     * @param mixtures
     */
    public void setMixtures(Map<String, String> mixtures) {
        this.mixtures = mixtures;
    }

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.DRUG_PACKAGED_BY, elementClass=BioRelation.class)
    private HashSet<BioRelation> packagerRelations;

    /**
     * Set the drug packagers.
     *
     * @param drugPackagers
     */
    public void setDrugPackagers(HashSet<DrugPackager> drugPackagers) {
        if (packagerRelations == null) {
            packagerRelations = new HashSet<BioRelation>();
        }
        for (DrugPackager drugPackager : drugPackagers) {
            packagerRelations.add(new BioRelation(this, drugPackager, BioRelTypes.DRUG_PACKAGED_BY));
        }
    }

    /**
     * Get the drug packagers.
     *
     * @return
     */
    public HashSet<DrugPackager> getDrugPackagers() {
        if (packagerRelations == null) {
            return null;
        }
        HashSet<DrugPackager> drugPackagers = new HashSet<DrugPackager>();
        for (BioRelation bioRelation : packagerRelations) {
            drugPackagers.add((DrugPackager)bioRelation.getEndNode());
        }
        return drugPackagers;
    }

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.DRUG_PRICED_AS, elementClass=BioRelation.class)
    private HashSet<BioRelation> drugPriceRelations;

    /**
     * Set the drug prices.
     *
     * @param drugPrices
     */
    public void setDrugPrices(HashSet<DrugPrice> drugPrices) {
        if (drugPriceRelations == null) {
            drugPriceRelations = new HashSet<BioRelation>();
        }
        for (DrugPrice drugPrice : drugPrices) {
            drugPriceRelations.add(new BioRelation(this, drugPrice, BioRelTypes.DRUG_PRICED_AS));
        }
    }

    /**
     * Get the drug prices.
     *
     * @return HashSet<DrugPrice>
     */
    public HashSet<DrugPrice> getDrugPrices() {
        if (drugPriceRelations == null) {
            return null;
        }
        HashSet<DrugPrice> drugPrices = new HashSet<DrugPrice>();
        for (BioRelation bioRelation : drugPriceRelations) {
            drugPrices.add((DrugPrice)bioRelation.getEndNode());
        }
        return drugPrices;
    }

    /**
     * db.drugbank.find({"drugbank-id" : "DB04930"}).pretty()
     * shows many manufacturers, prices and packagers.
     * make the relationship with drug as generic manufacturer
     * or non-generic manufacturer.
     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.DRUG_MANUFACTURED_BY, elementClass=BioRelation.class)
    private HashSet<BioRelation> manufacturerRelations;

    /**
     * Set the drug manufacturers.
     *
     * @param drugManufacturers
     */
    public void setDrugManufacturers(HashSet<DrugManufacturer> drugManufacturers) {
        if (manufacturerRelations == null) {
            manufacturerRelations = new HashSet<BioRelation>();
        }
        for (DrugManufacturer drugManufacturer : drugManufacturers) {
            manufacturerRelations.add(new BioRelation(this, drugManufacturer, BioRelTypes.DRUG_MANUFACTURED_BY));
        }
    }

    /**
     * Get the drug manufacturers.
     *
     * @return
     */
    public HashSet<DrugManufacturer> getDrugManufacturers() {
        if (manufacturerRelations == null) {
            return null;
        }
        HashSet<DrugManufacturer> drugManufacturers = new HashSet<DrugManufacturer>();
        for (BioRelation bioRelation : manufacturerRelations) {
            drugManufacturers.add((DrugManufacturer)bioRelation.getEndNode());
        }
        return drugManufacturers;
    }

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.HAS_DRUG_DOSAGE, elementClass=BioRelation.class)
    private HashSet<BioRelation> dosageRelations;

    /**
     * Set the drug dosage.
     *
     * @param dosages
     */
    public void setDrugDosage(HashSet<Dosage> dosages) {
        if (dosageRelations == null) {
            dosageRelations = new HashSet<BioRelation>();
        }
        for (Dosage dosage : dosages) {
            dosageRelations.add(new BioRelation(this, dosage, BioRelTypes.HAS_DRUG_DOSAGE));
        }
    }

    /**
     * Get the drug dosages.
     *
     * @return
     */
    public HashSet<Dosage> getDosages() {
        if (dosageRelations == null) {
            return null;
        }
        HashSet<Dosage> dosages = new HashSet<Dosage>();
        for (BioRelation bioRelation : dosageRelations) {
            dosages.add((Dosage)bioRelation.getEndNode());
        }
        return dosages;
    }

    @SubList (indexName=IndexNames.DRUG_CATEGORIES)
    private List<String> categories;

    /**
     * Get the categories for a drug.
     *
     * <pre>
     * "categories" : [
     "Enzyme Inhibitors",
     "Insecticides"
     ],
     * </pre>
     *
     * @return List<String>
     */
    public List<String> getCategories() {
        return categories;
    }

    /**
     * Set the categories for a drug.
     *
     * <pre>
     * "categories" : [
     "Enzyme Inhibitors",
     "Insecticides"
     ],
     * </pre>
     *
     * @param categories
     */
    /* public void setCategories(List<String> categories) {
        this.categories = categories;
    } */

    /**
     * Set the categories for a drug.
     *
     * <pre>
     * "categories" : [
     "Enzyme Inhibitors",
     "Insecticides"
     ],
     * </pre>
     *
     * @param category
     */
    public void addCategory(String category) {
        if (categories == null) {
            categories = new ArrayList<String>();
        }
        categories.add(category);
    }


    @SubList (indexName=IndexNames.DRUG_AFFECTED_ORGANISMS)
    private List<String> affectedOrganisms;

    /**
     * Get the affected organisms for this drug.
     *
     * <pre>
     * "affected-organisms" : {
     "affected-organism" : "Scabies (Sarcoptes scabei) and other insects"
     },
     * </pre>
     *
     * @return List<String>
     */
    public List<String> getAffectedOrganisms() {
        return affectedOrganisms;
    }

    /**
     * Set the affected organism for this drug.
     *
     * @param affectedOrganisms
     */
    public void setAffectedOrganisms(List<String> affectedOrganisms) {
        this.affectedOrganisms = affectedOrganisms;
    }

    @SubList (indexName=IndexNames.ATC_CODES)
    private List<String> atcCodes;

    /**
     * WHO drug classification system (ATC) identifiers
     * <a href="http://www.whocc.no/atc_ddd_index/">ATC Codes</a>
     *
     * <pre>
     * "atc-code" : "P03AC04"
     * </pre>
     *
     * @return List<String>
     */
    public List<String> getAtcCodes() {
        return atcCodes;
    }

    /**
     * WHO drug classification system (ATC) identifiers.
     * <a href="http://www.whocc.no/atc_ddd_index/">ATC Codes</a>
     *
     * <pre>
     * "atc-code" : "P03AC04"
     * </pre>
     *
     * @param atcCodes
     */
    public void setAtcCodes(List<String> atcCodes) {
        this.atcCodes = atcCodes;
    }

    @SubList (indexName=IndexNames.AHFS_CODES)
    private List<String> ahfsCodes;

    /**
     * AHFS Drug Information identifiers.
     *
     * <pre>
     * {
     "ahfs-code" : "8:12.12.92"
     },
     {
     "ahfs-code" : "92:44 "
     * </pre>
     *
     * @return List<String>
     */
    public List<String> getAhfsCodes() {
        return ahfsCodes;
    }

    /**
     * AHFS Drug Information identifiers
     *
     * <pre>
     * {
     "ahfs-code" : "8:12.12.92"
     },
     {
     "ahfs-code" : "92:44 "
     * </pre>
     *
     * @param ahfsCodes
     */
    public void setAhfsCodes(List<String> ahfsCodes) {
        this.ahfsCodes = ahfsCodes;
    }



    /**
     * <pre>
     * "secondary-accession-numbers" : [
     "BIOD00024",
     "BTD00024"
     ],
     * </pre>
     *
     * @return List<String>
     */
    public List<String> getSecondaryAccessionNumbers() {
        return secondaryAccessionNumbers;
    }

    @SubList (indexName=IndexNames.DRUG_GROUPS)
    private List<String> drugStatusGroups;

    @SubList (indexName=IndexNames.FOOD_INTERACTIONS)
    private List<String> foodInteractions;

    /**
     * Get the food interactions.
     *
     * <pre>
     *  db.drugbank.find({"drugbank-id" : "DB00011"}, {"food-interactions" : 1}).pretty()
     {
     "_id" : ObjectId("52956553300417c0cfbb1042"),
     "food-interactions" : {
     "food-interaction" : "Avoid alcohol."
     }
     }
     db.drugbank.find({"drugbank-id" : "DB00006"}, {"food-interactions" : 1}).pretty()
     {
     "_id" : ObjectId("52956553300417c0cfbb103d"),
     "food-interactions" : [
     "Dan shen, dong quai, evening primrose oil, gingko, policosanol, willow bark",
     "Echinacea"
     ]
     }
     * </pre>
     *
     * @return List<String>
     */
    public List<String> getFoodInteractions() {
        return foodInteractions;
    }

    /**
     * Set the food interactions.
     *
     * <pre>
     *  db.drugbank.find({"drugbank-id" : "DB00011"}, {"food-interactions" : 1}).pretty()
     {
     "_id" : ObjectId("52956553300417c0cfbb1042"),
     "food-interactions" : {
     "food-interaction" : "Avoid alcohol."
     }
     }
     db.drugbank.find({"drugbank-id" : "DB00006"}, {"food-interactions" : 1}).pretty()
     {
     "_id" : ObjectId("52956553300417c0cfbb103d"),
     "food-interactions" : [
     "Dan shen, dong quai, evening primrose oil, gingko, policosanol, willow bark",
     "Echinacea"
     ]
     }

     * </pre>
     *
     * @param foodInteractions
     */
    public void setFoodInteractions(List<String> foodInteractions) {
        this.foodInteractions = foodInteractions;
    }

   /* @SubList (indexName=IndexNames.DRUG_INTERACTIONS)
    private List<String> drugInteractions;

    public List<String> getDrugInteractions() {
        return drugInteractions;
    }

    public void setDrugInteractions(List<String> drugInteractions) {
        this.drugInteractions = drugInteractions;
    } */

    /**
     * Set the drug - drug interactions.
     *
     *
     * <pre>
     *     Lapatinib: DB01259
     db.drugbank.find({"drugbank-id" : "DB01259"}, {"drug-interactions" : 1}).pretty()
     {
     "_id" : ObjectId("52956554300417c0cfbb1046"),
     "drug-interactions" : [
     {
     "drug" : "DB01381",
     "name" : "Ginkgo biloba",
     "description" : "Additive anticoagulant/antiplatelet effects may increase bleed risk. Concomitant therapy should be avoided."
     },
     {
     "drug" : "DB00208",
     "name" : "Ticlopidine",
     "description" : "Increased bleeding risk. Monitor for signs of bleeding."
     }
     ]
     }

     * </pre>
     *
     * @param drugInteractions
     */
    @RelatedToVia (direction=Direction.BOTH, elementClass=BioRelation.class)
    private HashSet<BioRelation> drugInteractionRelations;

    @RelatedToVia (direction=Direction.BOTH, elementClass=BioRelation.class)
    private HashSet<BioRelation> chemicalPropertyRelations;

    @RelatedToVia (direction=Direction.BOTH, elementClass=BioRelation.class)
    private HashSet<BioRelation> experimentalPropertyRelations;

    /**
     * Set the chemical properties.
     *
     * <pre>
     "calculated-properties" : [
     {
     "kind" : "logP",
     "value" : "6.24",
     "source" : "ALOGPS"
     },
     {
     "kind" : "logS",
     "value" : "-6.8",
     "source" : "ALOGPS"
     },
     {
     "kind" : "Water Solubility",
     "value" : "6.91e-05 g/l",
     "source" : "ALOGPS"
     },
     {
     "kind" : "logP",
     "value" : "5.7",
     "source" : "ChemAxon"
     },
     {
     "kind" : "IUPAC Name",
     "value" : "(3-phenoxyphenyl)methyl 3-(2,2-dichloroethenyl)-2,2-dimethylcyclopropane-1-carboxylate",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Molecular Weight",
     "value" : "391.288",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Monoisotopic Weight",
     "value" : "390.07894992",
     "source" : "ChemAxon"
     },
     {
     "kind" : "SMILES",
     "value" : "CC1(C)C(C=C(Cl)Cl)C1C(=O)OCC1=CC(OC2=CC=CC=C2)=CC=C1",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Molecular Formula",
     "value" : "C21H20Cl2O3",
     "source" : "ChemAxon"
     },
     {
     "kind" : "InChI",
     "value" : "InChI=1S/C21H20Cl2O3/c1-21(2)17(12-18(22)23)19(21)20(24)25-13-14-7-6-10-16(11-14)26-15-8-4-3-5-9-15/h3-12,17,19H,13H2,1-2H3",
     "source" : "ChemAxon"
     },
     {
     "kind" : "InChIKey",
     "value" : "InChIKey=RLLPVAHGXHCWKJ-UHFFFAOYSA-N",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Polar Surface Area (PSA)",
     "value" : "35.53",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Refractivity",
     "value" : "114.28",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Polarizability",
     "value" : "39.43",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Rotatable Bond Count",
     "value" : "7",
     "source" : "ChemAxon"
     },
     {
     "kind" : "H Bond Acceptor Count",
     "value" : "1",
     "source" : "ChemAxon"
     },
     {
     "kind" : "H Bond Donor Count",
     "value" : "0",
     "source" : "ChemAxon"
     },
     {
     "kind" : "pKa (strongest basic)",
     "value" : "-7.1",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Physiological Charge",
     "value" : "0",
     "source" : "ChemAxon"
     }
     ],

     * </pre>
     *
     * @param chemicalProperties
     */
    public void setChemicalProperties(HashSet<ChemicalProperty> chemicalProperties) {
        if (chemicalPropertyRelations == null) {
            chemicalPropertyRelations = new HashSet<BioRelation>();
        }
        for (ChemicalProperty chemicalProperty : chemicalProperties) {
            chemicalPropertyRelations.add(new BioRelation(this, chemicalProperty, BioRelTypes.HAS_CHEMICAL_PROPERTY));
        }
    }

    /**
     * Set the experimental properties.
     *
     * <pre>
     "experimental-properties" : [
     {
     "kind" : "Water Solubility",
     "value" : "0.006 mg/L (at 20 �C)",
     "source" : "USDA PESTICIDE PROP DATABASE"
     },
     {
     "kind" : "Melting Point",
     "value" : "34 �C",
     "source" : "PhysProp"
     },
     {
     "kind" : "Boiling Point",
     "value" : "220 �C at 5.00E-02 mm Hg",
     "source" : "PhysProp"
     },
     {
     "kind" : "logP",
     "value" : "6.50",
     "source" : "HANSCH,C ET AL. (1995)"
     }
     ],

     * </pre>
     *
     * @param experimentalProperties
     */
    public void setExperimentalProperties(HashSet<ExperimentalProperty> experimentalProperties) {
        if (experimentalPropertyRelations == null) {
            experimentalPropertyRelations = new HashSet<BioRelation>();
        }
        for (ExperimentalProperty experimentalProperty : experimentalProperties) {
            experimentalPropertyRelations.add(new BioRelation(this, experimentalProperty, BioRelTypes.HAS_EXPERIMENTAL_PROPERTY));
        }
    }

    /**
     * Get the chemical properties.
     *
     * <pre>
     "calculated-properties" : [
     {
     "kind" : "logP",
     "value" : "6.24",
     "source" : "ALOGPS"
     },
     {
     "kind" : "logS",
     "value" : "-6.8",
     "source" : "ALOGPS"
     },
     {
     "kind" : "Water Solubility",
     "value" : "6.91e-05 g/l",
     "source" : "ALOGPS"
     },
     {
     "kind" : "logP",
     "value" : "5.7",
     "source" : "ChemAxon"
     },
     {
     "kind" : "IUPAC Name",
     "value" : "(3-phenoxyphenyl)methyl 3-(2,2-dichloroethenyl)-2,2-dimethylcyclopropane-1-carboxylate",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Molecular Weight",
     "value" : "391.288",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Monoisotopic Weight",
     "value" : "390.07894992",
     "source" : "ChemAxon"
     },
     {
     "kind" : "SMILES",
     "value" : "CC1(C)C(C=C(Cl)Cl)C1C(=O)OCC1=CC(OC2=CC=CC=C2)=CC=C1",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Molecular Formula",
     "value" : "C21H20Cl2O3",
     "source" : "ChemAxon"
     },
     {
     "kind" : "InChI",
     "value" : "InChI=1S/C21H20Cl2O3/c1-21(2)17(12-18(22)23)19(21)20(24)25-13-14-7-6-10-16(11-14)26-15-8-4-3-5-9-15/h3-12,17,19H,13H2,1-2H3",
     "source" : "ChemAxon"
     },
     {
     "kind" : "InChIKey",
     "value" : "InChIKey=RLLPVAHGXHCWKJ-UHFFFAOYSA-N",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Polar Surface Area (PSA)",
     "value" : "35.53",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Refractivity",
     "value" : "114.28",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Polarizability",
     "value" : "39.43",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Rotatable Bond Count",
     "value" : "7",
     "source" : "ChemAxon"
     },
     {
     "kind" : "H Bond Acceptor Count",
     "value" : "1",
     "source" : "ChemAxon"
     },
     {
     "kind" : "H Bond Donor Count",
     "value" : "0",
     "source" : "ChemAxon"
     },
     {
     "kind" : "pKa (strongest basic)",
     "value" : "-7.1",
     "source" : "ChemAxon"
     },
     {
     "kind" : "Physiological Charge",
     "value" : "0",
     "source" : "ChemAxon"
     }
     ],

     * </pre>
     *
     * @return HashSet<ChemicalProperty>
     */
    public HashSet<ChemicalProperty> getChemicalProperties() {
        if (chemicalPropertyRelations == null) {
            return null;
        }
        HashSet<ChemicalProperty> chemicalProperties = new HashSet<ChemicalProperty>();
        for (BioRelation chemicalPropertyRelation : chemicalPropertyRelations) {
            chemicalProperties.add((ChemicalProperty)chemicalPropertyRelation.getEndNode());
        }
        return chemicalProperties;
    }

    /**
     * Get the experimental properties.
     *
     * <pre>
     "experimental-properties" : [
     {
     "kind" : "Water Solubility",
     "value" : "0.006 mg/L (at 20 �C)",
     "source" : "USDA PESTICIDE PROP DATABASE"
     },
     {
     "kind" : "Melting Point",
     "value" : "34 �C",
     "source" : "PhysProp"
     },
     {
     "kind" : "Boiling Point",
     "value" : "220 �C at 5.00E-02 mm Hg",
     "source" : "PhysProp"
     },
     {
     "kind" : "logP",
     "value" : "6.50",
     "source" : "HANSCH,C ET AL. (1995)"
     }
     ],

     * </pre>
     *
     * @return HashSet<ExperimentalProperty>
     */
    public HashSet<ExperimentalProperty> getExperimentalProperties() {
        if (experimentalPropertyRelations == null) {
            return null;
        }
        HashSet<ExperimentalProperty> experimentalProperties = new HashSet<ExperimentalProperty>();
        for (BioRelation experimentalPropertyRelation : experimentalPropertyRelations) {
            experimentalProperties.add((ExperimentalProperty)experimentalPropertyRelation.getEndNode());
        }
        return experimentalProperties;
    }

    /**
     * Add a drugbank compound group to the groups.
     *
     * We actually did not detect a single list with more than one group
     * in DrugBank. However, we leave room here for a future list with more
     * than one group. Also some of the entries have a key called "group".
     * And some of them do not. So we need to recover from this inconsistency.
     *
     * <pre>
     * > db.drugbank.distinct("groups")
     [
     {
     "group" : "approved"
     },
     "approved",
     "experimental",
     "illicit",
     "withdrawn",
     {
     "group" : "nutraceutical"
     },
     "nutraceutical",
     {
     "group" : "withdrawn"
     },
     {
     "group" : "experimental"
     }
     ]

     * </pre>
     *
     * @param group
     */
    public void addDrugStatusGroup(String group) {
        if (drugStatusGroups == null) {
            drugStatusGroups = new ArrayList<String>();
        }
        drugStatusGroups.add(group);
    }

    /**
     * Get a list of drug status groups from DrugBank (groups)
     *
     * @return List<String>
     */
    public List<String> getDrugStatusGroups() {
        return drugStatusGroups;
    }

    /**
     * Description or common names of diseases that the drug is used to treat.
     * Eg.
     * <pre>
     * "Increases leukocyte production, for treatment in non-myeloid cancer, neutropenia and bone
     * marrow transplant"
     *
     * </pre>
     * @return String
     */
    public String getIndication() {
        return indication;
    }

    /**
     * * Description or common names of diseases that the drug is used to treat.
     * Eg.
     * <pre>
     * "Increases leukocyte production, for treatment in non-myeloid cancer, neutropenia and bone
     * marrow transplant"
     *
     * @param indication
     */
    public void setIndication(String indication) {
        this.indication = indication;
    }

    /**
     * The drug description from drugbank.
     * Eg. "Lepirudin is identical to natural hirudin except for substitution of leucine for
     * isoleucine at the N-terminal end of the molecule and the absence of a sulfate group on the
     * tyrosine at position 63. It is produced via yeast cells."
     *
     * @return String
     */
    public String getDrugDescription() {
        return drugDescription;
    }

    /**
     * The drug description from drugbank.
     * Eg. "Lepirudin is identical to natural hirudin except for substitution of leucine for
     * isoleucine at the N-terminal end of the molecule and the absence of a sulfate group on the
     * tyrosine at position 63. It is produced via yeast cells."
     *
     * @param drugDescription
     */
    public void setDrugDescription(String drugDescription) {
        this.drugDescription = drugDescription;
    }


    /**
     * The @version field of DrugBank. Eg. 3.0
     *
     * @return String
     */
    public String getVersion() {
        return version;
    }

    /**
     * The @version field of DrugBank. Eg. 3.0
     *
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    @Indexed (indexName=IndexNames.DRUG_BANK_ID)
    @Taxonomy (rbClass=TaxonomyTypes.DRUG_BANK_ID, rbField=BioFields.DRUG_BANK_ID)
    private String drugbankId;

    /**
     * The drugBankId from drug bank. Eg. "DB00001"
     *
     * @return String
     */
    public String getDrugbankId() {
        return drugbankId;
    }

    /**
     * The drugBankId from drug bank. Eg. "DB00001"
     *
     * @param drugbankId
     */
    public void setDrugbankId(String drugbankId) {
        this.drugbankId = drugbankId;
    }

    /**
     * This is the "type=" field which was later renamed to "dtyp=", in DrugBank.
     * It can have a value of "biotech".
     *
     * @return String
     */
    public String getDrugType() {
        return drugType;
    }

    /**
     * This is the "type=" field which was later renamed to "dtyp=", in DrugBank.
     * It can have a value of "biotech".
     *
     * @param drugType
     */
    public void setDrugType(String drugType) {
        this.drugType = drugType;
    }

    //  @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.IS_PART_OF_PATHWAY, startNodeBioType=BioTypes.PATHWAY, endNodeBioType=BioTypes.NCI_PATHWAY_INTERACTION, elementClass = BioRelation.class)
    // private Collection<BioRelation> pathwayInteractions = new HashSet<BioRelation>();

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
     * {@link Indexed} {@link IndexNames#NODE_TYPE}
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     *
     * <p>
     * {@link Indexed} {@link IndexNames#CHEMICAL_ABSTRACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#CHEMICAL_ABSTRACT_ID} {@link BioFields#CHEMICAL_ABSTRACT_ID}
     *
     * @return String
     */
    public String getCasId() {
        return casId;
    }

    /**
     * Chemical Abstract identifier
     * <p>
     * {@link Indexed} {@link IndexNames#CHEMICAL_ABSTRACT_ID}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#CHEMICAL_ABSTRACT_ID}
     *
     * @param casId
     */
    public void setCasId(String casId) {
        this.casId = casId;
    }


    /**
     * {@link FullTextIndexed} {@link IndexNames#COMPOUND_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#COMPOUND_PREFERRED_SYMBOL} {@link BioFields#COMPOUND_PREFERRED_SYMBOL}
     *
     * @return String
     */
    public String getCompoundPreferredSymbol() {
        return compoundPreferredSymbol;
    }

    /**
     * {@link FullTextIndexed} {@link IndexNames#COMPOUND_PREFERRED_SYMBOL}
     * <p>
     * {@link Taxonomy} {@link TaxonomyTypes#COMPOUND_PREFERRED_SYMBOL} {@link BioFields#COMPOUND_PREFERRED_SYMBOL}
     *
     * @param ps */
    public void setCompoundPreferredSymbol(String ps) {
        this.compoundPreferredSymbol = ps;
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


    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.REFERENCES_PUBMED, elementClass=BioRelation.class)
    private HashSet<BioRelation> synthesisPubmedRelations;

    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.REFERENCES_PATENT, elementClass=BioRelation.class)
    private HashSet<BioRelation> drugPatentRelations;

    /**
     * Reference or patent number to description of drug's synthesis.
     * Source are: Manual Search, PubChem, Merck Manual, PubMed.
     * Not all of them have pubmed, so we only extract the pubmed objects.
     * <pre>
     * > db.drugbank.distinct("synthesis-reference")
     [
     "Humulin is synthesized in a special non-disease-producing laboratory strain of Escherichia coli bacteria that has been genetically altered to produce human insulin.",
     "# Abdine HH, Belala F, and Al-Badra AA. (2003). Ipratropium bromide: Methods of chemical and biochemical synthesis. In H.G. Brittain (Ed.). _Profiles of drug substances, excipients and related methodology_ (pp. 85-99). Amsterdam, Netherlands: Elsevier Academic Press.",
     "# Boger DL. Vancomycin, teicoplanin, and ramoplanin: synthetic and mechanistic studies. Med Res Rev. 2001 Sep;21(5):356-81. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/11579438 ",
     "# Counter FT, Ensminger PW, Preston DA, Wu CY, Greene JM, Felty-Duckworth AM, Paschal JW, Kirst HA: Synthesis and antimicrobial evaluation of dirithromycin (AS-E 136; LY237216), a new macrolide antibiotic derived from erythromycin. Antimicrob Agents Chemother. 1991 Jun;35(6):1116-26. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/1929252",
     "# Kumar AS, Ghosh S, Mehta GN: Efficient and improved synthesis of Telmisartan. Beilstein J Org Chem. 2010 Mar 11;6:25. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/20502601",
     "# Zhou XP, Zhang MX, Sun W, Yang XH, Wang GS, Sui DY, Yu XF, Qu SC: Design, synthesis, and in-vivo evaluation of 4,5-diaryloxazole as novel nonsteroidal anti-inflammatory drug. Biol Pharm Bull. 2009 Dec;32(12):1986-90. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/19952416",
     "http://en.wikipedia.org/wiki/Ibuprofen#Synthesis",
     "See General references, Filho et. al. for synthesis of oxamniquine methacylate. ",
     "DOI: 10.1002/cjoc.201201147",
     "https://www.erowid.org/archive/rhodium/chemistry/dimenoxadol.html",
     "Kohler, Maxie, et al. \"Metabolism of 4-hydroxyandrostenedione and 4-hydroxytestosterone: Mass spectrometric identification of urinary metabolites.\" Steroids 72.3 (2007): 278-286.\r",
     "<http://www.lookchem.com/Chempedia/Chemical-Technology/Organic-Chemical-Technology/18498.html>",
     "??-ET TiHKAL entry ??? Isomer Design: http://isomerdesign.com/PiHKAL/read.php?domain=tk&id=11",
     "Canonica, Luigi; Jommi, Giancarlo; Pelizzoni, Francesca; Scolastico, Carlo. 1,11-Oxido steroids. I. 1a,11a-Oxidoandrostanes. Gazzetta Chimica Italiana (1965), 95(3), 138-50.",
     "# http://www.ema.europa.eu/docs/en_GB/document_library/EPAR_-_Public_assessment_report/human/001038/WC500022817.pdf",
     "http://en.wikipedia.org/wiki/File:Roflumilast_syn.png",
     "http://en.wikipedia.org/wiki/Phenols#Synthesis_of_phenols",
     "Potential Analgesics. Part I. Synthesis of substituted 4-quinazolones, I. K. Kacker and S. H. Zaheer, J. Ind. Chem. Soc. 28 (1951), pp. 344???346.",
     "Jandacek RJ: APD-356 (Arena). Curr Opin Investig Drugs. 2005 Oct;6(10):1051-6.",
     "Ogbourne SM, Suhrbier A, Jones B, Cozzi SJ, Boyle GM, Morris M, McAlpine D, Johns J, Scott TM, Sutherland KP, Gardner JM, Le TT, Lenarczyk A, Aylward JH, Parsons PG: Antitumor activity of 3-ingenyl angelate: plasma membrane and mitochondrial disruption and necrotic cell death. Cancer Res. 2004 Apr 15;64(8):2833-9. ",
     "Znabet A, Polak MM, Janssen E, de Kanter FJ, Turner NJ, Orru RV, Ruijter E. A highly efficient synthesis of telaprevir by strategic use of biocatalysis and multicomponent reactions. Chem Commun (Camb). 2010 Nov 14;46(42):7918-20. Epub 2010 Sep 20.",
     "http://doc.sciencenet.cn/upload/file/2011531154034454.pdf",
     "http://www.google.com/patents?id=IIanAAAAEBAJ&pg=PA2&source=gbs_selected_pages&cad=3#v=onepage&q&f=false",
     "Rilonacept is expressed in recombinant Chinese hamster ovary (CHO) cells. ",
     "http://www.google.com/patents/US20110071114",
     "Hu-Lowe D, Hallin M, Feeley R, Zou H, Rewolinski D, Wickman G, Chen E, Kim Y, Riney S, Reed J, Heller D, Simmons B, Kania R, McTigue M, Niesman M, Gregory S, Shalinsky D, Bender S. Characterization of potency and activity of the VEGF/PDGF receptor tyrosine kinase inhibitor AG013736. Proc Am Assoc Cancer Res. 2002;43:A5357.\r",
     "Bruns C, Lewis I, Briner U, Meno-Tetang G, Weckbecker G: SOM230: a novel somatostatin peptidomimetic with broad somatotropin release inhibiting factor (SRIF) receptor binding and a unique antisecretory profile. Eur J Endocrinol. 2002 May;146(5):707-16.",
     "Zhou H, Jang H, Fleischmann RM, Bouman-Thio E, Xu Z, Marini JC, Pendley C, Jiao Q, Shankar G, Marciniak SJ, Cohen SB, Rahman MU, Baker D, Mascelli MA, Davis HM, Everitt DE: Pharmacokinetics and safety of golimumab, a fully human anti-TNF-alpha monoclonal antibody, in subjects with rheumatoid arthritis. J Clin Pharmacol. 2007 Mar;47(3):383-96.",
     "# Haynes RK, Vonwiller SC: Extraction of artemisinin and artemisinic acid: preparation of artemether and new analogues. Trans R Soc Trop Med Hyg. 1994 Jun;88 Suppl 1:S23-6. \"Pubmed\":http://www.ncbi.nlm.nih.gov/pubmed/8053018",
     "C6H5CH2Cl + NaOH ??? C6H5CH2OH + NaCl",
     "Kohara Y, Kubo K, Imamiya E, Wada T, Inada Y, Naka T: Synthesis and angiotensin II receptor antagonistic activities of benzimidazole derivatives bearing acidic heterocycles as novel tetrazole bioisosteres. J Med Chem. 1996 Dec 20;39(26):5228-35.",
     "http://www.proteomesci.com/content/pdf/1477-5956-9-40.pdf",
     "Zhuo, Chao; Feng, Wei; Wu, Da-jun; Xiong, Zhi-gang. Synthesis of tauroursodeoxycholic acid. Hecheng Huaxue (2002), 10(5), 444-446.",
     "Reddy AV, Ravindranath B: Synthesis of alpha-, beta- and cyclic spaglumic acids. Int J Pept Protein Res. 1992 Nov;40(5):472-6.",
     "A)Nierenstein M: The Formation of Ellagic Acid from Galloyl-Glycine by Penicillium. Biochem J. 1915 Jun;9(2):240-4. B)L??we, Zeitschrift f??r Chemie, 1868, 4, 603.",
     "Adams, Elijah; Goldstone, Alfred. Hydroxyproline metabolism. II. Enzymic preparation and properties of D1-pyrroline-3-hydroxy-5-carboxylic acid. Journal of Biological Chemistry (1960), 235 3492-8.",
     "McELVAIN SM, STORK G: Piperidine derivatives; the preparation of l-benzoyl-3-carbethoxy-4-piperidone; a synthesis of guvacine. J Am Chem Soc. 1946 Jun;68:1049-53.",
     "Francisco JA, Cerveny CG, Meyer DL, Mixan BJ, Klussman K, Chace DF, Rejniak SX, Gordon KA, DeBlanc R, Toki BE, Law CL, Doronina SO, Siegall CB, Senter PD, Wahl AF: cAC10-vcMMAE, an anti-CD30-monomethyl auristatin E conjugate with potent and selective antitumor activity. Blood. 2003 Aug 15;102(4):1458-65. Epub 2003 Apr 24",
     "1. Cundy KC, Branch R, Chernov-Rogan T, Dias T, Estrada T, Hold K, Koller K, Liu X, Mann A, Panuwat M, Raillard SP, Upadhyay S, Wu QQ, Xiang JN, Yan H, Zerangue N, Zhou CX, Barrett RW, Gallop MA: XP13512 [(+/-)-1-([(alpha-isobutanoyloxyethoxy)carbonyl] aminomethyl)-1-cyclohexane acetic acid], a novel gabapentin prodrug: I. Design, synthesis, enzymatic conversion to gabapentin, and transport by intestinal solute transporters. J Pharmacol Exp Ther. 2004 Oct;311(1):315-23. Epub 2004 May 14.\r\r",
     "Shaaltiel Y, Bartfeld D, Hashmueli S, Baum G, Brill-Almon E, Galili G, Dym O, Boldin-Adamsky SA, Silman I, Sussman JL, Futerman AH, Aviezer D: Production of glucocerebrosidase with terminal mannose glycans for enzyme replacement therapy of Gaucher's disease using a plant cell system. Plant Biotechnol J. 2007 Sep;5(5):579-90. Epub 2007 May 24.",
     "http://www.sigmaaldrich.com/catalog/papers/16078850",
     "Kohler, Maxie, et al. \"Metabolism of 4-hydroxyandrostenedione and 4-hydroxytestosterone: Mass spectrometric identification of urinary metabolites.\" Steroids 72.3 (2007): 278-286.",
     "Benardeau A, Benz J, Binggeli A, Blum D, Boehringer M, Grether U, Hilpert H, Kuhn B, Marki HP, Meyer M, Puntener K, Raab S, Ruf A, Schlatter D, Mohr P: Aleglitazar, a new, potent, and balanced dual PPARalpha/gamma agonist for the treatment of type II diabetes. Bioorg Med Chem Lett. 2009 May 1;19(9):2468-73. doi: 10.1016/j.bmcl.2009.03.036. Epub 2009 Mar "
     ]
     * </pre>
     *
     * @param pubmedList
     */
    public void setSynthesisPubmed(HashSet<PubMed> pubmedList) {
        synthesisPubmedRelations = new HashSet<BioRelation>();
        for (PubMed pubmed : pubmedList) {
            synthesisPubmedRelations.add(new BioRelation(this, pubmed, BioRelTypes.REFERENCES_PUBMED));
        }
    }

    /**
     * Reference or patent number to description of drug's synthesis.
     * Source are: Manual Search, PubChem, Merck Manual, PubMed.
     * Not all of them have pubmed, so we only extract the pubmed objects.
     *
     * @return HashSet<PubMed>
     */
    public HashSet<PubMed> getSynthesisPubmed() {
        HashSet<PubMed> pubmedList = new HashSet<PubMed>();
        for (BioRelation bioRelation : synthesisPubmedRelations) {
            pubmedList.add((PubMed)bioRelation.getEndNode());
        }
        return pubmedList;
    }

    public void setDrugPatent(HashSet<DrugPatent> drugPatentList) {
        drugPatentRelations = new HashSet<BioRelation>();
        for (DrugPatent drugPatent : drugPatentList) {
            drugPatentRelations.add(new BioRelation(this, drugPatent, BioRelTypes.REFERENCES_PATENT));
        }
    }

    public HashSet<DrugPatent> getDrugPatent() {
        HashSet<DrugPatent> drugPatentList = new HashSet<DrugPatent>();
        for (BioRelation bioRelation : drugPatentRelations) {
            drugPatentList.add((DrugPatent)bioRelation.getEndNode());
        }
        return drugPatentList;
    }

    public HashSet<Drug> getDrugInteraction() {
        HashSet<Drug> drugList = new HashSet<Drug>();
        for (BioRelation bioRelation : drugInteractionRelations) {
            drugList.add((Drug)bioRelation.getEndNode());
        }
        return drugList;
    }

    public void setDrugInteraction(HashSet<Drug> drugList) {
        drugInteractionRelations = new HashSet<BioRelation>();
        for (Drug drug : drugList) {
            drugInteractionRelations.add(new BioRelation(this, drug, BioRelTypes.DRUG_INTERACTS_WITH));
        }
    }


    public BioRelation getProteinRelation() {
        return proteinRelation;
    }

    public void setProteinRelation(Protein protein) {
       proteinRelation = new BioRelation(this, protein, BioRelTypes.IDENTIFIED_PROTEIN);
    }










}


