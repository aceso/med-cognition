/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import java.util.HashMap;
import java.util.Map;

/**
 *  http://www.uniprot.org/manual/
 *  
 * 
 * @author jtanisha-ee
 */

public enum ProteinGeneralAnnotationFields {
  
    /**
        General annotation (Comments)
        Position-independent annotation
      
        Non-experimental qualifier Non-experimental qualifiers indicate that the information given is not based on experimental findings.
        Function	General description of the function(s) of a protein
        Catalytic activity	Description of the reaction(s) catalyzed by an enzyme
        Cofactor	Description of non-protein substance required by an enzyme to be active
        Enzyme regulation	Description of an enzyme regulatory mechanism
        Biophysicochemical properties	Description of biophysical and physicochemical properties
        Subunit structure	Description of the quaternary structure of a protein
        Pathway	Description of associated metabolic pathways
        Subcellular location	Description of the subcellular location of the mature protein
        Tissue specificity	Description of the expression of a gene in different tissues
        Developmental stage	Description of how the expression of a gene varies according to the stage of cell, tissue or organism development
        Induction	Description of the effects of environmental factors on the gene expression
        Domain	Description of the domain(s) present in a protein
        Post-translational modification	Description of post-translational modifications
        RNA editing	Description of amino acid change(s) due to RNA editing
        Mass spectrometry	Information derived from mass spectrometry experiments
        Polymorphism	Description of polymorphism(s)
        Involvement in disease	Description of the disease(s) associated with the deficiency of a protein
        Disruption phenotype	Description of the effects caused by the disruption of a protein-encoding gene.
        Allergenic properties	Information relevant to allergenic proteins
        Toxic dose	Lethal, paralytic, or effect dose, or lethal concentration of a toxin
        Biotechnological use	Description of the use of a specific protein in a biotechnological process
        Pharmaceutical use	Description of the use of a protein as a pharmaceutical drug
        Miscellaneous	Any relevant information that doesn't fit in any other defined sections
        Sequence similarities	Description of the sequence similaritie(s) with other proteins
        Caution	Warning about possible errors and/or grounds for confusion
        Sequence caution	Warning about possible errors related to the protein sequence 
        */
   
    TOXIC_DOSE("TOXIC DOSE");
    
   
    private final String value;

    private static final Map<String, ProteinGeneralAnnotationFields> stringToEnum = new HashMap<String, ProteinGeneralAnnotationFields>();

    static { // init map from constant name to enum constant
        for (ProteinGeneralAnnotationFields en : values())
            stringToEnum.put(en.toString(), en);
    }
    
    public boolean equals(ProteinGeneralAnnotationFields enumField) {
        return value.equals(enumField.toString());
    }
    
    public boolean equals(String s) {
        return value.equals(s);
    }

    ProteinGeneralAnnotationFields(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ProteinGeneralAnnotationFields fromString(String value) {
        return stringToEnum.get(value);
    }  
    
    /**
     * ProteinExistence 
     * @param str
     * @return 
     */
    /*
    public static String getProteinAnnotationValue(String str) {
       
        if (str.equals("1: Evidence at protein level")) {
            return EI1.toString();
        } else if (str.equals("2. Evidence at transcript level")) {
            return EI2.toString();
        } else if (str.equals("3. Inferred from homology")) {
            return EI3.toString();
        } else if (str.equals("4. Predicted")) {
            return EI4.toString();
        } else if (str.equals("5. Uncertain")) {
            return EI5.toString();
        } else {
            return null;
        }
        
    }
    */
}

/**
 * Sequence annotation (Features)
Position-dependent annotation
Non-experimental qualifiers	Non-experimental qualifiers indicate that the information given is not based on experimental findings.
Initiator methionine	Cleaved initiator methionine
Propeptide	Part of a protein that is cleaved during maturation or activation
Beta strand	Beta strand regions within the experimentally determined protein structure
Cross-link	Residues participating in covalent linkage(s) between proteins
Disulfide bond	Cysteine residues participating in disulfide bonds
Glycosylation	Covalently attached glycan group(s)
Lipidation	Covalently attached lipid group(s)
Coiled coil	Denotes the positions of regions of coiled coil within the protein
Region	Region of interest in the sequence
Turn	Turns within the experimentally determined protein structure
Helix	Helical regions within the experimentally determined protein structure
Non-terminal residue	The sequence is incomplete. Indicate that a residue is not the terminal residue of the complete protein.
Non-adjacent residues	Indicates that two residues in a sequence are not consecutive
Sequence conflict	Description of sequence discrepancies of unknown origin
Sequence uncertainty	Regions of uncertainty in the sequence
Mutagenesis	Site which has been experimentally altered by mutagenesis
Natural variant	Description of a natural variant of the protein
Alternative sequence	Amino acid change(s) producing alternate protein isoforms
Modified residue	Modified residues excluding lipids, glycans and protein crosslinks
Non-standard residue	Occurence of non-standard amino acids (selenocysteine and pyrrolysine) in the protein sequence
Site	Any interesting single amino acid site on the sequence
Binding site	Binding site for any chemical group (co-enzyme, prosthetic group, etc.)
Metal binding	Binding site for a metal ion
Active site	Amino acid(s) directly involved in the activity of an enzyme
Compositional bias	Region of compositional bias in the protein
Motif	Short (up to 20 amino acids) sequence motif of biological interest
DNA binding	Denotes the position and type of a DNA-binding domain
Nucleotide binding	Nucleotide phosphate binding region
Signal 	Sequence targeting proteins to the secretory pathway or periplasmic space
Zinc finger	Denotes the position(s) and type(s) of zinc fingers within the protein
Calcium binding	Denotes the position(s) of calcium binding region(s) within the protein
Repeat	Denotes the positions of repeated sequence motifs or repeated domains
Domain	Denotes the position and type of each modular protein domain
Transmembrane	Extent of a membrane-spanning region
Topological domain	Location of non-membrane regions of membrane-spanning proteins
Intramembrane	Extent of a region located in a membrane without crossing it
Peptide	Extent of an active peptide in the mature protein
Chain	Extent of a polypeptide chain in the mature protein
Transit peptide	Extent of a transit peptide for organelle targeting 
 */

