/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.domain.types.LigandEntityTypes;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.atgc.bio.meta.Visual;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * The ligand information is found in the pdbligand collection
 *
 * {
	"_id" : ObjectId("519e97ef03648991a8deda64"),
	"@structureId" : "3CAP",
	"ligands" : [
		"BGL",
		"BMA",
		"NAG",
		"PLM"
	]
}
*
 * The ligand collection has the following sample document:
 *
 * {
	"_id" : ObjectId("519e97ef03648991a8deda60"),
	"@chemicalID" : "BGL",
	"@entityType" : "saccharide",
	"@molecularWeight" : "292.369",
	"chemicalName" : "B-2-OCTYLGLUCOSIDE",
	"formula" : "C14 H28 O6",
	"InChIKey" : "BVHPDIWLWHHJPD-RKQHYHRCSA-N",
	"InChI" : "InChI=1S/C14H28O6/c1-2-3-4-5-6-7-8-19-13-12(17)11(16)10(9-15)20-14(13)18/h10-18H,2-9H2,1H3/t10-,11-,12+,13-,14-/m1/s1",
	"smiles" : "CCCCCCCCO[C@@H]1[C@H]([C@@H]([C@H](O[C@H]1O)CO)O)O"
}
 *
 * <p>
 * The website http://www.ebi.ac.uk/pdbe-srv/pdbechem/chemicalCompound/show/BGL gives more
 * metadata.
 * </p>
 *
 * <p>
 * In biochemistry and pharmacology, a ligand (from the Latin ligandum, binding) is a substance
 * (usually a small molecule), that forms a complex with a biomolecule to serve a biological purpose.
 * In a narrower sense, it is a signal triggering molecule, binding to a site on a target protein.
 * The binding occurs by intermolecular forces, such as ionic bonds, hydrogen bonds and van der Waals
 * forces. The docking (association) is usually reversible (dissociation). Actual irreversible covalent
 * bonding between a ligand and its target molecule is rare in biological systems.
 * </p>
 *
 * <p>
 * Ligand binding to a receptor (receptor protein) alters its chemical conformation (three dimensional
 * shape). The conformational state of a receptor protein determines its functional state. Ligands
 * include substrates, inhibitors, activators, and neurotransmitters. The tendency or strength of
 * binding is called affinity. Binding affinity is determined not only by direct interactions, but
 * also by solvent effects that can play a dominant indirect role in driving non-covalent binding in
 * solution.
 * </p>
 *
 * <p>
 * The interaction of most ligands with their binding sites can be characterized in terms of a binding
 * affinity. In general, high-affinity ligand binding results from greater intermolecular force between
 * the ligand and its receptor while low-affinity ligand binding involves less intermolecular force
 * between the ligand and its receptor. In general, high-affinity binding involves a longer residence
 * time for the ligand at its receptor binding site than is the case for low-affinity binding.
 * High-affinity binding of ligands to receptors is often physiologically important when some of the
 * binding energy can be used to cause a conformational change in the receptor, resulting in altered
 * behavior of an associated ion channel or enzyme.
 * </p>
 *
 * <p>
 * A ligand that can bind to a receptor, alter the function of the receptor and trigger a
 * physiological response is called an agonist for that receptor. Agonist binding to a receptor
 * can be characterized both in terms of how much physiological response can be triggered and in
 * terms of the concentration of the agonist that is required to produce the physiological response.
 * High-affinity ligand binding implies that a relatively low concentration of a ligand is adequate
 * to maximally occupy a ligand-binding site and trigger a physiological response. Low-affinity
 * binding implies that a relatively high concentration of a ligand is required before the binding
 * site is maximally occupied and the maximum physiological response to the ligand is achieved. If two
 * different ligands bind to the same receptor binding site.
 * Only one of the agonists shown can maximally stimulate the receptor and, thus, can be defined
 * as a "full agonist". An agonist that can only partially activate the physiological response is
 * called a "partial agonist". Ligands that bind to a receptor but fail to activate the physiological
 * response are receptor "antagonists".
 * </p>
 *
 * <p>
 * Binding affinity data alone does not determine the overall potency of a drug. Potency is a result
 * of the complex interplay of both the binding affinity and the ligand efficacy. Ligand efficacy refers
 * to the ability of the ligand to produce a biological response upon binding to the target receptor
 * and the quantitative magnitude of this response. This response may be as an agonist, antagonist, or
 * inverse agonist, depending on the physiological response produced.
 * </p>
 *
 * <p>
 * Selective ligands have a tendency to bind to a very limited types of receptors, whereas non-selective
 * ligands bind to several types of receptors. This plays an important role in pharmacology, where drugs
 * that are non-selective tend to have more adverse effects, because they bind to several other receptors
 * in addition to the one generating the desired effect.
 * </p>
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.LIGAND)
public class Ligand {

    protected static Logger log = LogManager.getLogger(Ligand.class);

    /**
     * This is required for the graph database.
     */
    @GraphId
    private Long id;

    /**
     * For eg. BGL
     */
    @UniquelyIndexed (indexName=IndexNames.CHEMICAL_ID)
    @Taxonomy(rbClass=TaxonomyTypes.CHEMICAL_ID, rbField=BioFields.CHEMICAL_ID)
    private String chemicalId;

    /**
     * For eg. saccharide
     * Experimental evidence directly implicates complex carbohydrates in recognition processes, including adhesion between cells, adhesion of cells to the extracellular matrix, and specific recognition of cells by one another. In addition, carbohydrates are recognized as differentiation markers and as antigenic determinants. Lectins are nonenzymatic proteins present in plants and animals, which preferentially bind to specific carbohydrate structures and play an important role in cell recognition. Modified carbohydrates and oligosaccharides have the ability to interfere with carbohydrate-protein interactions and therefore, inhibit the cell-cell recognition and adhesion processes, which play an important role in cancer growth and progression. Carbohydrate ligands therefore, are candidates to play important roles in cancer therapeutics.
     */
    @Indexed(indexName=IndexNames.LIGAND_ENTITY_TYPE)
    @Taxonomy(rbClass=TaxonomyTypes.LIGAND_ENTITY_TYPE, rbField=BioFields.ENTITY_TYPE)
    private LigandEntityTypes entityType;

    /**
     * The molecular weight is in dalton. http://www.ebi.ac.uk/pdbe-srv/pdbechem/chemicalCompound/show/BGL
     */
    @Indexed(indexName=IndexNames.LIGAND_MOLECULAR_WEIGHT)
    @Taxonomy(rbClass=TaxonomyTypes.LIGAND_MOLECULAR_WEIGHT, rbField=BioFields.MOLECULAR_WEIGHT)
    private String molecularWeight;

    @Visual
    @Indexed(indexName=IndexNames.LIGAND_CHEMICAL_NAME)
    @Taxonomy(rbClass=TaxonomyTypes.LIGAND_CHEMICAL_NAME, rbField=BioFields.CHEMICAL_NAME)
    private String chemicalName;

    @NodeLabel
    @Indexed (indexName=IndexNames.LIGAND_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.LIGAND_MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @Indexed(indexName=IndexNames.LIGAND_FORMULA)
    @Taxonomy(rbClass=TaxonomyTypes.LIGAND_FORMULA, rbField=BioFields.FORMULA)
    private String formula;

    @Indexed(indexName=IndexNames.LIGAND_INCHI_KEY)
    @Taxonomy(rbClass=TaxonomyTypes.LIGAND_INCHI_KEY, rbField=BioFields.INCHI_KEY)
    private String inChIKey;

    @Indexed(indexName=IndexNames.LIGAND_INCHI)
    @Taxonomy(rbClass=TaxonomyTypes.LIGAND_INCHI, rbField=BioFields.INCHI)
    private String inChI;

    /**
     * The IUPAC International Chemical Identifier (InChI) is a
     * textual identifier for chemical substances, designed to provide a standard and human-readable
     * way to encode molecular information and to facilitate the search for such information in databases
     * and on the web. Initially developed by IUPAC and NIST during 2000–2005, the format and algorithms
     * are non-proprietary. The continuing development of the standard has been supported since 2010 by
     * the not-for-profit InChI Trust, of which IUPAC is a member. The current version is 1.04 and was
     * released in September 2011. Prior to 1.04, the software was freely available under the open
     * source LGPL license,[2] but it now uses a custom license called IUPAC-InChI Trust License.[3]
     *
     * @return String
     */
    public String getInChIKey() {
        return inChIKey;
    }

    public void setInChIKey(String inChIKey) {
        this.inChIKey = inChIKey;
    }
    /**
     * For eg. "C14 H28 O6"
     * @return String
     */
    public String getFormula() {
        return formula;
    }

    public void setFormula(String formula) {
        this.formula = formula;
    }

    /**
     * For eg. B-2-OCTYLGLUCOSIDE
     *
     * @return String
     */
    public String getChemicalName() {
        return chemicalName;
    }

    public void setChemicalName(String chemicalName) {
        this.chemicalName = chemicalName;
    }

    public String getMolecularWeight() {
        return molecularWeight;
    }

    public void setMolecularWeight(String molecularWeight) {
        this.molecularWeight = molecularWeight;
    }

    public String getChemicalId() {
        return chemicalId;
    }

    public void setChemicalId(String chemicalId) {
        this.chemicalId = chemicalId;
    }

    /**
     * For eg. Saccharides. Experimental evidence directly implicates complex carbohydrates in
     * recognition processes, including adhesion between cells, adhesion of cells to the
     * extracellular matrix, and specific recognition of cells by one another. In addition,
     * carbohydrates are recognized as differentiation markers and as antigenic determinants.
     * Lectins are nonenzymatic proteins present in plants and animals, which preferentially bind
     * to specific carbohydrate structures and play an important role in cell recognition. Modified
     * carbohydrates and oligosaccharides have the ability to interfere with carbohydrate-protein
     * interactions and therefore, inhibit the cell-cell recognition and adhesion processes, which
     * play an important role in cancer growth and progression. Carbohydrate ligands therefore, are
     * candidates to play important roles in cancer therapeutics.
     *
     * @return LigandEntityTypes
     */
    public LigandEntityTypes getEntityType() {
        return entityType;
    }

    public void setEntityType(LigandEntityTypes entityType) {
        this.entityType = entityType;
    }

}
