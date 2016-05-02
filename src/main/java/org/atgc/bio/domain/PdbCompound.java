/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NonIndexed;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniquelyIndexed;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.repository.TemplateUtils;

/**
 * <a href="http://www.rcsb.org/pdb/101/static101.do?p=education_discussion/Looking-at-Structures/bioassembly_tutorial.html">Bioassembly</a>
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.PDB_COMPOUND)
public class PdbCompound {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(PdbCompound.class);

    @GraphId
    private Long id;

    /**
     *
     */
    @Indexed(indexName=IndexNames.PDB_COMPOUND_ATCC)
    @Taxonomy(rbClass=TaxonomyTypes.PDB_COMPOUND_ATCC, rbField=BioFields.ATCC)
    private String atcc;

    @Indexed (indexName=IndexNames.PDB_COMPOUND_BIOLOGICAL_UNIT)
    @Taxonomy (rbClass=TaxonomyTypes.PDB_COMPOUND_BIOLOGICAL_UNIT, rbField=BioFields.BIOLOGICAL_UNIT)
    private String biologicalUnit;

    @NonIndexed
    private String cell;

    @NonIndexed
    private String cellLine;

    @Indexed (indexName=IndexNames.PDB_COMPOUND_CELLULAR_LOCATION)
    @Taxonomy (rbClass=TaxonomyTypes.PDB_COMPOUND_CELLULAR_LOCATION, rbField=BioFields.CELLULAR_LOCATION)
    private String cellularLocation;

    @NonIndexed
    private String details;

    @Indexed (indexName=IndexNames.PDB_COMPOUND_EC_NUMS)
    @Taxonomy (rbClass=TaxonomyTypes.PDB_COMPOUND_EC_NUMS, rbField=BioFields.EC_NUMS)
    private String ecNums;

    @NonIndexed
    private String engineered;

    @NonIndexed
    private String expressionSystem;

    @NonIndexed
    private String expressionSystemAtccNumber;

    @NonIndexed
    private String expressionSystemCell;

    @NonIndexed
    private String expressionSystemCellLine;

    @NonIndexed
    private String expressionSystemCellularLocation;

    @NonIndexed
    private String expressionSystemGene;

    @NonIndexed
    private String expressionSystemOrgan;

    @NonIndexed
    private String expressionSystemOrganelle;

    @NonIndexed
    private String expressionSystemOtherDetails;

    @NonIndexed
    private String expressionSystemPlasmid;

    @NonIndexed
    private String expressionSystemStrain;

    @NonIndexed
    private String expressionSystemTaxId;

    @NonIndexed
    private String expressionSystemTissue;

    @NonIndexed
    private String expressionSystemVariant;

    @NonIndexed
    private String expressionSystemVector;

    @NonIndexed
    private String expressionSystemVectorType;

    @NonIndexed
    private String fragment;

    @NonIndexed
    private String gene;

    @NonIndexed
    private String headerVars;

    @Indexed (indexName=IndexNames.PDB_COMPOUND_MOL_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PDB_COMPOUND_MOL_ID, rbField=BioFields.MOL_ID)
    private String molId;

    @UniquelyIndexed(indexName = IndexNames.MOLECULE_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PDB_COMPOUND_MOLECULE_NAME, rbField=BioFields.MOL_NAME)
    private String molName;

    @NonIndexed
    private String mutation;

    @NonIndexed
    private String numRes;

    @NonIndexed
    private String organ;

    @NonIndexed
    private String organelle;

    @NonIndexed
    private String organismCommon;

    @Indexed (indexName=IndexNames.PDB_COMPOUND_ORGANISM_FULL_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.PDB_COMPOUND_ORGANISM_FULL_NAME, rbField=BioFields.ORGANISM_FULL_NAME)
    private String organismFullName;

    @Indexed (indexName=IndexNames.PDB_COMPOUND_TAXONOMY_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PDB_COMPOUND_TAXONOMY_ID, rbField=BioFields.NCBI_TAXONOMY_ID)
    private String taxonomyId;

    @NonIndexed
    private String refChainId;

    @NonIndexed
    private String resNames;

    @NonIndexed
    private String secretion;

    @NonIndexed
    private String strain;

    @NonIndexed
    private String synonyms;

    @NonIndexed
    private String synthetic;

    @NonIndexed
    private String tissue;

    @NonIndexed
    private String title;

    @NonIndexed
    private String variant;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    /**
     *
     * @return
     */
    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * American Type Culture Collection tissue culture number.
     * {@linkplain http://www.atcc.org/}
     * ATCC was entrusted with its first cell line in 1962 and has consistently attained the
     * highest standards and used the most reliable procedures to verify every cell line since.
     * The ATCC Cell Biology Collection is one of the largest bioresources in the world, and
     * offers a complex array of human, animal, insect, fish and stem cell lines from which to choose.
     *
     * @return String
     */
    public String getAtcc() {
        return atcc;
    }

    /**
     *
     * @param atcc
     */
    public void setAtcc(String atcc) {
        this.atcc = atcc;
    }

    /**
     * {@linkplain http://www.rcsb.org/pdb/101/static101.do?p=education_discussion/Looking-at-Structures/bioassembly_tutorial.html}
     *
     * <p>
     * A biological unit (abbreviated BU) consists of the smallest number of protein molecules
     * which form a biologically active (e.g. catalytically active) unit.
     * The Biological Unit, also called the Biological Assembly, is the quaternary structure
     * that is believed to be the functional form of a macromolecule.
     *
     * <p>
     * The Biological Unit, also called the Biological Assembly, is the quaternary structure of
     * a protein that is believed to be the functional form of the molecule. It can be a single
     * chain, or a quaternary assembly of multiple identical or non-identical chains. For example,
     * the biological unit of hemoglobin includes two alpha chains and two beta chains, making it
     * a tetrameric α2β2 structure. When a biological unit contains multiple chains that have
     * co-evolved to bind to each other, it may also be referred to as a specific oligomer.
     *
     * <p>
     * Of course, what is the functional form (biological unit) under one set of conditions may
     * change under a different set of conditions, so there may be more than one functional form
     * (biological unit) that includes a given protein chain. For example, phosphorylation or
     * dephosphorylation by protein kinases or phosphatases often change the affinities between
     * proteins, and hence their quaternary assemblies.
     *
     * <p>
     * When a structure is deposited in the PDB, the authors are required to specify the biological
     * unit if it is known. This is given in REMARK 350 in the header of the PDB file format.
     * Unfortunately, information in REMARK 350 is often incorrect (see discussion of this problem
     * by Roland Dunbrack). There are numerous examples in which the authors state that the
     * biological unit is a monomer in REMARK 350, but provide good experimental evidence in
     * the paper reporting the structure that the biological unit is a dimer. Jose Duarte
     * provided a list of examples.
     *
     * <p>
     * In some cases, more than one putative biological unit is specified in REMARK 350.
     * Biological units specified by the author(s) are distinguished from those predicted by
     * software. An example is 3fad, which is explained in Looking at Structures: Introduction
     * to Biological Assemblies and the PDB Archive.
     *
     * <p>
     * The most reliable way to find out the biological unit is to read the literature and/or
     * contact experts on the molecule in question. Short of such efforts, here are some suggestions:
     *
     * <p>
     * When the "author determined" biological unit stated in REMARK 350 has a different number
     * of chains than the asymmetric unit, the biological unit is more likely to be correct,
     * simply because the difference in chains shows that the authors gave REMARK 350 some real
     * consideration.
     *
     * <p>
     * When the "author determined biological unit stated in REMARK 350 has the same number of
     * chains, but in a different conformation, than the asymmetric unit, the stated biological
     * unit is more likely to be correct, for the same reason given in the previous case above.
     * An example is 1qrd, given in the table in the Examples section above.
     *
     * <p>
     * When the "author determined" biological unit stated in REMARK 350 has the same number of
     * chains, in the same conformation, as the asymmetric unit, the stated biological unit is
     * less likely to be correct. There is a significant chance that the authors failed to state
     * a known biological unit in REMARK 350 (see examples above).
     *
     * <p>
     * When a biological unit is determined only by software, it is less likely to be correct.
     * The software makes an educated guess based upon the characteristics of the contacts
     * present in the protein crystal, but it is sometimes incorrect.
     *
     * <p>
     * In chemistry, an oligomer (\ə-ˈli-gə-mər\, ολιγος, or oligos, is Greek for "a few") is a
     * molecular complex that consists of a few monomer units, in contrast to a polymer that, at
     * least in principle, consists of a nearly unlimited number of monomers.[1] Dimers, trimers,
     * and tetramers are, for instance, oligomers respectively composed of two, three and four
     * monomers. In the context of biochemistry, an oligomer is usually referred to a
     * macromolecular complex formed by non-covalent bonding of few macromolecules like
     * proteins or nucleic acids. In this sense, a homo-oligomer would be formed by few
     * identical molecules and by contrast, a hetero-oligomer would be made of three
     * different macromolecules. Collagen is an example of homo-oligomeric protein that is
     * composed of three identical protein chains.
     *
     * @return String
     */
    public String getBiologicalUnit() {
        return biologicalUnit;
    }

    /**
     * {@linkplain http://www.rcsb.org/pdb/101/static101.do?p=education_discussion/Looking-at-Structures/bioassembly_tutorial.html}
     *
     * <p>
     * A biological unit (abbreviated BU) consists of the smallest number of protein molecules
     * which form a biologically active (e.g. catalytically active) unit.
     * The Biological Unit, also called the Biological Assembly, is the quaternary structure
     * that is believed to be the functional form of a macromolecule.
     *
     * <p>
     * The Biological Unit, also called the Biological Assembly, is the quaternary structure of
     * a protein that is believed to be the functional form of the molecule. It can be a single
     * chain, or a quaternary assembly of multiple identical or non-identical chains. For example,
     * the biological unit of hemoglobin includes two alpha chains and two beta chains, making it
     * a tetrameric α2β2 structure. When a biological unit contains multiple chains that have
     * co-evolved to bind to each other, it may also be referred to as a specific oligomer.
     *
     * <p>
     * Of course, what is the functional form (biological unit) under one set of conditions may
     * change under a different set of conditions, so there may be more than one functional form
     * (biological unit) that includes a given protein chain. For example, phosphorylation or
     * dephosphorylation by protein kinases or phosphatases often change the affinities between
     * proteins, and hence their quaternary assemblies.
     *
     * <p>
     * When a structure is deposited in the PDB, the authors are required to specify the biological
     * unit if it is known. This is given in REMARK 350 in the header of the PDB file format.
     * Unfortunately, information in REMARK 350 is often incorrect (see discussion of this problem
     * by Roland Dunbrack). There are numerous examples in which the authors state that the
     * biological unit is a monomer in REMARK 350, but provide good experimental evidence in
     * the paper reporting the structure that the biological unit is a dimer. Jose Duarte
     * provided a list of examples.
     *
     * <p>
     * In some cases, more than one putative biological unit is specified in REMARK 350.
     * Biological units specified by the author(s) are distinguished from those predicted by
     * software. An example is 3fad, which is explained in Looking at Structures: Introduction
     * to Biological Assemblies and the PDB Archive.
     *
     * <p>
     * The most reliable way to find out the biological unit is to read the literature and/or
     * contact experts on the molecule in question. Short of such efforts, here are some suggestions:
     *
     * <p>
     * When the "author determined" biological unit stated in REMARK 350 has a different number
     * of chains than the asymmetric unit, the biological unit is more likely to be correct,
     * simply because the difference in chains shows that the authors gave REMARK 350 some real
     * consideration.
     *
     * <p>
     * When the "author determined biological unit stated in REMARK 350 has the same number of
     * chains, but in a different conformation, than the asymmetric unit, the stated biological
     * unit is more likely to be correct, for the same reason given in the previous case above.
     * An example is 1qrd, given in the table in the Examples section above.
     *
     * <p>
     * When the "author determined" biological unit stated in REMARK 350 has the same number of
     * chains, in the same conformation, as the asymmetric unit, the stated biological unit is
     * less likely to be correct. There is a significant chance that the authors failed to state
     * a known biological unit in REMARK 350 (see examples above).
     *
     * <p>
     * When a biological unit is determined only by software, it is less likely to be correct.
     * The software makes an educated guess based upon the characteristics of the contacts
     * present in the protein crystal, but it is sometimes incorrect.
     *
     * <p>
     * In chemistry, an oligomer (\ə-ˈli-gə-mər\, ολιγος, or oligos, is Greek for "a few") is a
     * molecular complex that consists of a few monomer units, in contrast to a polymer that, at
     * least in principle, consists of a nearly unlimited number of monomers.[1] Dimers, trimers,
     * and tetramers are, for instance, oligomers respectively composed of two, three and four
     * monomers. In the context of biochemistry, an oligomer is usually referred to a
     * macromolecular complex formed by non-covalent bonding of few macromolecules like
     * proteins or nucleic acids. In this sense, a homo-oligomer would be formed by few
     * identical molecules and by contrast, a hetero-oligomer would be made of three
     * different macromolecules. Collagen is an example of homo-oligomeric protein that is
     * composed of three identical protein chains.
     *
     * @param biologicalUnit
     */
    public void setBiologicalUnit(String biologicalUnit) {
        this.biologicalUnit = biologicalUnit;
    }

    /**
     * Identifies the particular cell type.
     * {@linkplain http://en.wikipedia.org/wiki/List_of_distinct_cell_types_in_the_adult_human_body}
     *
     * @return String
     */
    public String getCell() {
        return cell;
    }

    /**
     * Identifies the particular cell type.
     * {@linkplain http://en.wikipedia.org/wiki/List_of_distinct_cell_types_in_the_adult_human_body}
     *
     * @param cell
     */
    public void setCell(String cell) {
        this.cell = cell;
    }

    /**
     * The specific line of cells used in the experiment.
     * A cell line is a product of immortal cells that are used for biological research. Cells used
     * for cell lines are immortal, that happens if a cell is cancerous. The cells can perpetuate
     * division indefinitely which is unlike regular cells which can only divide approximately 50 times.
     * These cells are 'useful' for experimentation in labs as they are always available to researchers
     * as a product and do not require what is known as 'harvesting' (the acquiring of tissue from a
     * host) every time cells are needed in the lab.
     *
     * <p>
     * Cell line is a permanently established cell culture that will proliferate
     * indefinitely given appropriate fresh medium and space. A general term referring to
     * the maintenance of cell strains or lines in the laboratory. Cell strain consists of cells
     * adapted to culture, but with finite division potential. A cell culture is to grow in vitro.
     *
     * @return String
     */
    public String getCellLine() {
        return cellLine;
    }

    /**
     * The specific line of cells used in the experiment.
     * A cell line is a product of immortal cells that are used for biological research. Cells used
     * for cell lines are immortal, that happens if a cell is cancerous. The cells can perpetuate
     * division indefinitely which is unlike regular cells which can only divide approximately 50 times.
     * These cells are 'useful' for experimentation in labs as they are always available to researchers
     * as a product and do not require what is known as 'harvesting' (the acquiring of tissue from a
     * host) every time cells are needed in the lab.
     *
     * <p>
     * Cell line is a permanently established cell culture that will proliferate
     * indefinitely given appropriate fresh medium and space. A general term referring to
     * the maintenance of cell strains or lines in the laboratory. Cell strain consists of cells
     * adapted to culture, but with finite division potential. A cell culture is to grow in vitro.
     *
     * @param cellLine
     */
    public void setCellLine(String cellLine) {
        this.cellLine = cellLine;
    }

    /**
     * This could be a GO term for instance "endoplasmic reticulum" in geneontology.obo
     * or the geneontology collection with goId GO:0005783.
     * Identifies the location inside/outside the cell.
     * CELLULAR_LOCATION may be used to indicate where in the organism the compound was found.
     * Examples are: extracellular, periplasmic, cytosol.
     *
     * @return String
     */
    public String getCellularLocation() {
        return cellularLocation;
    }

    /**
     * This could be a GO term for instance "endoplasmic reticulum" in geneontology.obo
     * or the geneontology collection with goId GO:0005783.
     * Identifies the location inside/outside the cell.
     *
     * @param cellularLocation
     */
    public void setCellularLocation(String cellularLocation) {
        this.cellularLocation = cellularLocation;
    }

    /**
     * Eg. THIS PROTEIN IS LIGAND-FREE RHODOPSIN, OPSIN.
     *
     * @return String
     */
    public String getDetails() {
        return details;
    }

    /**
     * Eg. THIS PROTEIN IS LIGAND-FREE RHODOPSIN, OPSIN.
     *
     * @param details
     */
    public void setDetails(String details) {
        this.details = details;
    }

    /**
     * EC The Enzyme Commission number associated with the molecule. If there is more than one EC number,
     * they are presented as a comma-separated list.
     *
     * <a href="http://en.wikipedia.org/wiki/Enzyme_Commission_number">Enzyme Commission Number</a>
     *
     * <p>
     * The Enzyme Commission number (EC number) is a numerical classification scheme for enzymes, based on the
     * chemical reactions they catalyze.[1] As a system of enzyme nomenclature, every EC number is associated
     * with a recommended name for the respective enzyme. Strictly speaking, EC numbers do not specify enzymes,
     * but enzyme-catalyzed reactions. If different enzymes (for instance from different organisms) catalyze the
     * same reaction, then they receive the same EC number.[2] Furthermore, through convergent evolution,
     * completely different protein folds can catalyze an identical reaction and therefore would be assigned an
     * identical EC number.[3] By contrast, UniProt identifiers uniquely specify a protein by its amino acid sequence.
     *
     *
     * <a href="http://www.brenda-enzymes.org/information/all_enzymes.php4">List of all EC Numbers</a>
     *
     * <a href="http://www.google.com/patents/EP1356077A2?cl=en">Enzyme Classification</a>
     *
       <pre>
       E.C. Number     Enzyme               Reaction
           1            Oxidoreductase      Oxidation-reduction reactions
           2            Transferases        Transfer of groups
           3            Hydrolases          Hydrolysis reactions
           4            Lysases             Addition reactions
           5            Isomerases          Isomerization reactions
           6            Ligases             Bond formation and ATP cleavage
       </pre>

     * @return String
     */
    public String getEcNums() {
        return ecNums;
    }

    /**
     * EC The Enzyme Commission number associated with the molecule. If there is more than one EC number,
     * they are presented as a comma-separated list.
     *
     *
     * <a href="http://en.wikipedia.org/wiki/Enzyme_Commission_number">Enzyme Commission Number</a>
     *
     * <p>
     * The Enzyme Commission number (EC number) is a numerical classification scheme for enzymes, based on the
     * chemical reactions they catalyze.[1] As a system of enzyme nomenclature, every EC number is associated
     * with a recommended name for the respective enzyme. Strictly speaking, EC numbers do not specify enzymes,
     * but enzyme-catalyzed reactions. If different enzymes (for instance from different organisms) catalyze the
     * same reaction, then they receive the same EC number.[2] Furthermore, through convergent evolution,
     * completely different protein folds can catalyze an identical reaction and therefore would be assigned an
     * identical EC number.[3] By contrast, UniProt identifiers uniquely specify a protein by its amino acid sequence.
     *
     *
     * <a href="http://www.brenda-enzymes.org/information/all_enzymes.php4">List of all EC Numbers</a>
     *
     * <a href="http://www.google.com/patents/EP1356077A2?cl=en">Enzyme Classification</a>
     *
       <pre>
       E.C. Number     Enzyme               Reaction
           1            Oxidoreductase      Oxidation-reduction reactions
           2            Transferases        Transfer of groups
           3            Hydrolases          Hydrolysis reactions
           4            Lysases             Addition reactions
           5            Isomerases          Isomerization reactions
           6            Ligases             Bond formation and ATP cleavage
       </pre>

     * @param ecNums
     */
    public void setEcNums(String ecNums) {
        this.ecNums = ecNums;
    }

    /**
     * If the molecule was made using recombinant techniques.
     *
     * <a href="http://www.wwpdb.org/documentation/format23/sect3.html">Engineered Sequences</a>
     * <a href="http://gersbach.bme.duke.edu/node/25">Engineered Enzymes and Sequences</a>
     *
     * @return String
     */
    public String getEngineered() {
        return engineered;
    }

    /**
     *
     * <a href="http://www.wwpdb.org/documentation/format23/sect3.html">Engineered Sequences</a>
     * <a href="http://gersbach.bme.duke.edu/node/25">Engineered Enzymes and Sequences</a>
     *
     * @param engineered
     */
    public void setEngineered(String engineered) {
        this.engineered = engineered;
    }

    /**
     * System used to express recombinant macromolecules.
     * Transgenic organisms, such as mouse producing human proteins, are treated as expression systems.
     * Protein expression is a subcomponent of gene expression. It consists of the stages after DNA has been
     * transcribed to messenger RNA (mRNA). The mRNA is then translated into polypeptide chains, which are
     * ultimately folded into proteins.[citation needed] Protein expression is commonly used by proteomics
     * researchers to denote the measurement of the presence and abundance of one or more proteins in a particular
     * cell or tissue.
     *
     * <p>
     * Protein expression systems are very widely used in the life sciences, biotechnology and medicine. Molecular
     * biology research uses numerous proteins and enzymes many of which are from expression systems; particularly
     * DNA polymerase for PCR, reverse transcriptase for RNA analysis and restriction endonucleases for cloning.
     * There are also significant medical applications for expression systems, notably the production of human
     * insulin to treat diabetes. Protein expression systems are used to produce certain proteins in biotechnology
     * and industry, and more recently to produce sets (combinatorial series) of protein that are screened for
     * drug discovery purpose. For. eg. Drosophila Melanogaster
     *
     * <a href="http://en.wikipedia.org/wiki/Protein_expression_(biotechnology)">Expression Systems</a>
     *
     * @return String
     */
    public String getExpressionSystem() {
        return expressionSystem;
    }

     /**
      * System used to express recombinant macromolecules.
      * Transgenic organisms, such as mouse producing human proteins, are treated as expression systems.
     * Protein expression is a subcomponent of gene expression. It consists of the stages after DNA has been
     * transcribed to messenger RNA (mRNA). The mRNA is then translated into polypeptide chains, which are
     * ultimately folded into proteins.[citation needed] Protein expression is commonly used by proteomics
     * researchers to denote the measurement of the presence and abundance of one or more proteins in a particular
     * cell or tissue.
     *
     * <p>
     * Protein expression systems are very widely used in the life sciences, biotechnology and medicine. Molecular
     * biology research uses numerous proteins and enzymes many of which are from expression systems; particularly
     * DNA polymerase for PCR, reverse transcriptase for RNA analysis and restriction endonucleases for cloning.
     * There are also significant medical applications for expression systems, notably the production of human
     * insulin to treat diabetes. Protein expression systems are used to produce certain proteins in biotechnology
     * and industry, and more recently to produce sets (combinatorial series) of protein that are screened for
     * drug discovery purpose. For. eg. Drosophila Melanogaster
     *
     * @param expressionSystem
     */
    public void setExpressionSystem(String expressionSystem) {
        this.expressionSystem = expressionSystem;
    }

    /**
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemAtccNumber() {
        return expressionSystemAtccNumber;
    }

    /**
     * @param expressionSystemAtccNumber
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemAtccNumber(String expressionSystemAtccNumber) {
        this.expressionSystemAtccNumber = expressionSystemAtccNumber;
    }

    /**
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemCell() {
        return expressionSystemCell;
    }

     /**
     * @param expressionSystemCell
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemCell(String expressionSystemCell) {
        this.expressionSystemCell = expressionSystemCell;
    }

    /**
     * The specific line of cells used as the expression system.
     *
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemCellLine() {
        return expressionSystemCellLine;
    }

    /**
     * The specific line of cells used as the expression system.
     *
     * @param expressionSystemCellLine
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemCellLine(String expressionSystemCellLine) {
        this.expressionSystemCellLine = expressionSystemCellLine;
    }

    /**
     * The organism and cell location given are for the source of the gene used in the cloning experiment.
     *
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemCellularLocation() {
        return expressionSystemCellularLocation;
    }

    /**
     * The organism and cell location given are for the source of the gene used in the cloning experiment.
     *
     * @param expressionSystemCellularLocation
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemCellularLocation(String expressionSystemCellularLocation) {
        this.expressionSystemCellularLocation = expressionSystemCellularLocation;
    }

    /**
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemGene() {
        return expressionSystemGene;
    }

    /**
     * @param expressionSystemGene
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemGene(String expressionSystemGene) {
        this.expressionSystemGene = expressionSystemGene;
    }
    /**
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemOrgan() {
        return expressionSystemOrgan;
    }

    /**
     * @param expressionSystemOrgan
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemOrgan(String expressionSystemOrgan) {
        this.expressionSystemOrgan = expressionSystemOrgan;
    }

     /**
     * Specific organelle which expressed the molecule.
     *
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemOrganelle() {
        return expressionSystemOrganelle;
    }

    /**
     * Specific organelle which expressed the molecule.
     *
     * @param expressionSystemOrganelle
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemOrganelle(String expressionSystemOrganelle) {
        this.expressionSystemOrganelle = expressionSystemOrganelle;
    }

    /**
     * Used to present information on the source which is not given elsewhere.
     *
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemOtherDetails() {
        return expressionSystemOtherDetails;
    }

    /**
     * Used to present information on the source which is not given elsewhere.
     *
     * @param expressionSystemOtherDetails
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemOtherDetails(String expressionSystemOtherDetails) {
        this.expressionSystemOtherDetails = expressionSystemOtherDetails;
    }

    /**
     * A plasmid is a small DNA molecule that is physically separate from, and can replicate independently of,
     * chromosomal DNA within a cell. Most commonly found as small circular, double-stranded DNA molecules in
     * bacteria, plasmids are sometimes present in archaea and eukaryotic organisms. In nature, plasmids carry
     * genes that may benefit survival of the organism (e.g. antibiotic resistance), and can frequently be
     * transmitted from one bacterium to another (even of another species) via horizontal gene transfer.
     * Artificial plasmids are widely used as vectors in molecular cloning, serving to drive the replication
     * of recombinant DNA sequences within host organisms.[1]
     *
     * <p>
     * Plasmid sizes vary from 1 to over 1,000 kbp.[1] The number of identical plasmids in a single cell can
     * range anywhere from one to thousands under some circumstances. Plasmids can be considered part of the
     * mobilome because they are often associated with conjugation, a mechanism of horizontal gene transfer.
     * The term plasmid was first introduced by the American molecular biologist Joshua Lederberg in 1952.[2]
     * Plasmids are considered replicons, capable of replicating autonomously within a suitable host.
     * Plasmids can be found in all three major domains: Archaea, Bacteria, and Eukarya.[3] Similar to viruses,
     * plasmids are not considered by some to be a form of life.[4] Unlike viruses, plasmids are naked DNA and
     * do not encode genes necessary to encase the genetic material for transfer to a new host, though some
     * classes of plasmids encode the sex pilus necessary for their own transfer.
     *
     * @return String
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public String getExpressionSystemPlasmid() {
        return expressionSystemPlasmid;
    }

    /**
     * A plasmid is a small DNA molecule that is physically separate from, and can replicate independently of,
     * chromosomal DNA within a cell. Most commonly found as small circular, double-stranded DNA molecules in
     * bacteria, plasmids are sometimes present in archaea and eukaryotic organisms. In nature, plasmids carry
     * genes that may benefit survival of the organism (e.g. antibiotic resistance), and can frequently be
     * transmitted from one bacterium to another (even of another species) via horizontal gene transfer.
     * Artificial plasmids are widely used as vectors in molecular cloning, serving to drive the replication
     * of recombinant DNA sequences within host organisms.[1]
     *
     * <p>
     * Plasmid sizes vary from 1 to over 1,000 kbp.[1] The number of identical plasmids in a single cell can
     * range anywhere from one to thousands under some circumstances. Plasmids can be considered part of the
     * mobilome because they are often associated with conjugation, a mechanism of horizontal gene transfer.
     * The term plasmid was first introduced by the American molecular biologist Joshua Lederberg in 1952.[2]
     * Plasmids are considered replicons, capable of replicating autonomously within a suitable host.
     * Plasmids can be found in all three major domains: Archaea, Bacteria, and Eukarya.[3] Similar to viruses,
     * plasmids are not considered by some to be a form of life.[4] Unlike viruses, plasmids are naked DNA and
     * do not encode genes necessary to encase the genetic material for transfer to a new host, though some
     * classes of plasmids encode the sex pilus necessary for their own transfer.
     *
     * @param expressionSystemPlasmid
     *
     * @see {@link #getAttc()}
     * @see {@link #getExpressionSystem()}
     */
    public void setExpressionSystemPlasmid(String expressionSystemPlasmid) {
        this.expressionSystemPlasmid = expressionSystemPlasmid;
    }

    /**
     * The strain of organism in which the protein is being expressed.
     *
     * @return String
     */
    public String getExpressionSystemStrain() {
        return expressionSystemStrain;
    }

    /**
     * The strain of organism in which the protein is being expressed.
     *
     * @param expressionSystemStrain
     */
    public void setExpressionSystemStrain(String expressionSystemStrain) {
        this.expressionSystemStrain = expressionSystemStrain;
    }

    /**
     * The taxonomyId of organism in which the protein is being expressed.
     *
     * @return String
     */
    public String getExpressionSystemTaxId() {
        return expressionSystemTaxId;
    }

    /**
     * The taxonomyId of organism in which the protein is being expressed.
     *
     * @param expressionSystemTaxId
     */
    public void setExpressionSystemTaxId(String expressionSystemTaxId) {
        this.expressionSystemTaxId = expressionSystemTaxId;
    }

    /**
     * Specific tissue which expressed the molecule.
     *
     * @return String
     */
    public String getExpressionSystemTissue() {
        return expressionSystemTissue;
    }

    /**
     * Specific tissue which expressed the molecule.
     *
     * @param expressionSystemTissue
     */
    public void setExpressionSystemTissue(String expressionSystemTissue) {
        this.expressionSystemTissue = expressionSystemTissue;
    }

    /**
     * Variant of the organism used as the expression system.
     *
     * @return String
     */
    public String getExpressionSystemVariant() {
        return expressionSystemVariant;
    }

    /**
     * Variant of the organism used as the expression system.
     *
     * @param expressionSystemVariant
     */
    public void setExpressionSystemVariant(String expressionSystemVariant) {
        this.expressionSystemVariant = expressionSystemVariant;
    }

    /**
     * An expression vector, otherwise known as an expression construct, is usually a plasmid or virus
     * designed for protein expression in cells. Identifies the vector used.
     *
     * @return String
     */
    public String getExpressionSystemVector() {
        return expressionSystemVector;
    }

    /**
     * An expression vector, otherwise known as an expression construct, is usually a plasmid or
     * virus designed for protein expression in cells. Identifies the vector used.
     *
     * @param expressionSystemVector
     */
    public void setExpressionSystemVector(String expressionSystemVector) {
        this.expressionSystemVector = expressionSystemVector;
    }

    /**
     * Identifies the type of vector used, i.e., plasmid, virus, or cosmid.
     *
     * @return String
     */
    public String getExpressionSystemVectorType() {
        return expressionSystemVectorType;
    }

    /**
     * Identifies the type of vector used, i.e., plasmid, virus, or cosmid.
     *
     * @param expressionSystemVectorType
     */
    public void setExpressionSystemVectorType(String expressionSystemVectorType) {
        this.expressionSystemVectorType = expressionSystemVectorType;
    }

    /**
     * A domain or fragment of the molecule may be specified.
     * Hybrid molecules prepared by fusion of genes are treated as multi-molecular systems for the purpose of
     * specifying the source. The token FRAGMENT is used to associate the source with its corresponding fragment.
     *
     * <DL>
     * <LI>
     * When necessary to fully describe hybrid molecules, tokens may appear more than once for a given MOL_ID.
     * <LI>
     * All relevant token: value pairs that taken together fully describe each fragment are grouped following the appropriate FRAGMENT.
     * <LI>
     * Descriptors relative to the full system appear before the FRAGMENT.
     * </DL>
     *
     * @return String
     */
    public String getFragment() {
        return fragment;
    }

    /**
     * A domain or fragment of the molecule may be specified.
     *
     * @param fragment
     */
    public void setFragment(String fragment) {
        this.fragment = fragment;
    }

    /**
     * Identifies the gene.
     *
     * @return
     */
    public String getGene() {
        return gene;
    }

    /**
     * Identifies the gene.
     *
     * @param gene
     */
    public void setGene(String gene) {
        this.gene = gene;
    }

    /**
     *
     * @return
     */
    public String getHeaderVars() {
        return headerVars;
    }

    /**
     *
     * @param headerVars
     */
    public void setHeaderVars(String headerVars) {
        this.headerVars = headerVars;
    }

    /**
     * Numbers each molecule.
     *
     * @return String
     * <a href="http://www.bmsc.washington.edu/CrystaLinks/man/pdb/part_19.html">COMPND</a>
     */
    public String getMolId() {
        return molId;
    }

    /**
     * Numbers each molecule.
     *
     * @param molId
     * <a href="http://www.bmsc.washington.edu/CrystaLinks/man/pdb/part_19.html">COMPND</a>
     */
    public void setMolId(String molId) {
        this.molId = molId;
    }

    /**
     * Name of the molecule.
     *
     * @return String
     */
    public String getMolName() {
        return molName;
    }

    /**
     * Name of the molecule.
     *
     * @param molName
     */
    public void setMolName(String molName) {
        this.molName = molName;
    }

    /**
     *
     * @return String
     */
    public String getMutation() {
        return mutation;
    }

    /**
     *
     * @param mutation
     */
    public void setMutation(String mutation) {
        this.mutation = mutation;
    }

    /**
     *
     * @return String
     */
    public String getNumRes() {
        return numRes;
    }

    /**
     *
     * @param numRes
     */
    public void setNumRes(String numRes) {
        this.numRes = numRes;
    }

    /**
     *
     * @return String
     */
    public String getOrgan() {
        return organ;
    }

    /**
     *
     * @param organ
     */
    public void setOrgan(String organ) {
        this.organ = organ;
    }

    /**
     *
     * @return String
     */
    public String getOrganelle() {
        return organelle;
    }

    /**
     *
     * @param organelle
     */
    public void setOrganelle(String organelle) {
        this.organelle = organelle;
    }

    /**
     *
     * @return String
     */
    public String getOrganismCommon() {
        return organismCommon;
    }

    /**
     *
     * @param organismCommon
     */
    public void setOrganismCommon(String organismCommon) {
        this.organismCommon = organismCommon;
    }

    /**
     * ORGANISM_SCIENTIFIC provides the Latin genus and species. Virus names are listed as the scientific name.
     *
     * @return String
     */
    public String getOrganismFullName() {
        return organismFullName;
    }

    /**
     * ORGANISM_SCIENTIFIC provides the Latin genus and species. Virus names are listed as the scientific name.
     *
     * @param organismFullName
     */
    public void setOrganismFullName(String organismFullName) {
        this.organismFullName = organismFullName;
    }

    /**
     *
     * @return
     */
    public String getTaxonomyId() {
        return taxonomyId;
    }

    /**
     *
     * @param taxonomyId
     */
    public void setTaxonomyId(String taxonomyId) {
        this.taxonomyId = taxonomyId;
    }

    /**
     *
     * @return
     */
    public String getRefChainId() {
        return refChainId;
    }

    /**
     *
     * @param refChainId
     */
    public void setRefChainId(String refChainId) {
        this.refChainId = refChainId;
    }

    /**
     *
     * @return
     */
    public String getResNames() {
        return resNames;
    }

    /**
     *
     * @param resNames
     */
    public void setResNames(String resNames) {
        this.resNames = resNames;
    }

    /**
     *
     * @return
     */
    public String getSecretion() {
        return secretion;
    }

    /**
     *
     * @param secretion
     */
    public void setSecretion(String secretion) {
        this.secretion = secretion;
    }

    /**
     *
     * @return String
     */
    public String getStrain() {
        return strain;
    }

    /**
     *
     * @param strain
     */
    public void setStrain(String strain) {
        this.strain = strain;
    }

    /**
     *
     * @return
     */
    public String getSynonyms() {
        return synonyms;
    }

    /**
     *
     * @param synonyms
     */
    public void setSynonyms(String synonyms) {
        this.synonyms = synonyms;
    }

    /**
     * Molecules prepared by purely chemical synthetic methods are described by the specification
     * SYNTHETIC followed by "YES" or an optional value, such as NON-BIOLOGICAL SOURCE or BASED ON THE
     * NATURAL SEQUENCE. ENGINEERED must appear in the COMPND record.
     *
     * @return String
     */
    public String getSynthetic() {
        return synthetic;
    }

    /**
     * Molecules prepared by purely chemical synthetic methods are described by the specification
     * SYNTHETIC followed by "YES" or an optional value, such as NON-BIOLOGICAL SOURCE or BASED ON THE
     * NATURAL SEQUENCE. ENGINEERED must appear in the COMPND record.
     *
     * @param synthetic
     */
    public void setSynthetic(String synthetic) {
        this.synthetic = synthetic;
    }

    /**
     *
     * @return String
     */
    public String getTissue() {
        return tissue;
    }

    /**
     *
     * @param tissue
     */
    public void setTissue(String tissue) {
        this.tissue = tissue;
    }

    /**
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    /**
     *
     * @param title
     */
    public void setTitle(String title) {
        this.title = title;
    }

    /**
     *
     * @return String
     */
    public String getVariant() {
        return variant;
    }

    /**
     *
     * @param variant
     */
    public void setVariant(String variant) {
        this.variant = variant;
    }
}