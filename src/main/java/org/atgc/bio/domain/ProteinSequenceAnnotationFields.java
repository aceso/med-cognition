/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import java.util.HashMap;
import java.util.Map;

/**
 *  Position-dependent annotation
 *  http://www.uniprot.org/manual/
 *  
 * 
 * 
 * @author jtanisha-ee
 */

public enum ProteinSequenceAnnotationFields {
  
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
     TRANSIT_PEPTIDE("TRANSIT_PEPTIDE"),
     CHAIN("CHAIN");
    
   
    private final String value;

    private static final Map<String, ProteinSequenceAnnotationFields> stringToEnum = new HashMap<String, ProteinSequenceAnnotationFields>();

    static { // init map from constant name to enum constant
        for (ProteinSequenceAnnotationFields en : values())
            stringToEnum.put(en.toString(), en);
    }
    
    public boolean equals(ProteinSequenceAnnotationFields enumField) {
        return value.equals(enumField.toString());
    }
    
    public boolean equals(String s) {
        return value.equals(s);
    }

    ProteinSequenceAnnotationFields(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ProteinSequenceAnnotationFields fromString(String value) {
        return stringToEnum.get(value);
    }  

}


