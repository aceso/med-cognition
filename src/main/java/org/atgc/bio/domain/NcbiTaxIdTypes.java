/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import java.util.HashMap;
import java.util.Map;

/**
 *  http://www.uniprot.org/manual/protein_existence
 *  http://www.uniprot.org/docs/pe_criteria
 * 
 * @author jtanisha-ee
 */

public enum NcbiTaxIdTypes {
 
    /**
     * db.ncidisease.distinct("0.Sentence.Organism")
[
	"Human",
	"Rat",
	"Mouse",
	"others",
	"Cow",
	"Dog",
	"Chicken",
	"Cat",
	"Fruitfly",
	"Chimpanzee",
	"Zebrafish",
	"Macaque",
	"0",
	"Gorilla",
	"H"
     */
    
    /**
     * Homo sapiens
     */
    HUMAN("NCBITaxon:9606"),
    
    
    /**
     * Rattus
     */
    RAT("NCBITaxon:10114"),
    
    /**
     * Mus musculus
     */
    MOUSE("NCBITaxon:10090"),
    
    /**
     * Drosophila melanogaster
     */
    FRUITFLY("NCBITaxon:7227"),
    
    /**
     * Canis lupus familiaris
     */
    DOG("NCBITaxon:9615"),
    
    /**
     * Felis catusFelis catus
     */
    CAT("NCBITaxon:96859685"),
    
    /**
     * Danio rerio
     */
    ZEBRAFISH("NCBITaxon:7955"),
    
    /**
     * Pan troglodytes
     */
    CHIMPANZEE("NCBITaxon:9598"),
    
    /**
     * Bos taurus
     */
    COW("NCBITaxon:9913"),
    
    
    GORILLA("NCBITaxon:9592"),
    
    MACAQUE("NCBITaxon:9539"),
    
    /**
     * Gallus gallus
     */
    CHICKEN("NCBITaxon:9031"),
    
    /**
     * unidentified
     */
    OTHER("NCBITaxon:32644");
   

    private final String value;

    private static final Map<String, NcbiTaxIdTypes> stringToEnum = new HashMap<String, NcbiTaxIdTypes>();

    static { // init map from constant name to enum constant
        for (NcbiTaxIdTypes en : values())
            stringToEnum.put(en.toString(), en);
    }
    
    public boolean equals(NcbiTaxIdTypes bioType) {
        return value.equals(bioType.toString());
    }
    
    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    NcbiTaxIdTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static NcbiTaxIdTypes fromString(String value) {
        return stringToEnum.get(value);
    }  
    
    
    /**
     * Given an organism name (common) return NCBITaxon ID.
     * getNcbiTaxIdVale
     * @param str - String "Human" 
     * @return String
     */
    public static String getNcbiTaxIdValue(String str) {
        if (str.equalsIgnoreCase("Zebrafish")) {
            return ZEBRAFISH.toString();
        } else if (str.equalsIgnoreCase("Human")) {
            return HUMAN.toString();
        } else if (str.equalsIgnoreCase("Rat")) {
            return RAT.toString();
        } else if (str.equalsIgnoreCase("Cat")) {
            return CAT.toString();
        } else if (str.equalsIgnoreCase("Fruitfly")) {
            return FRUITFLY.toString();
        } else if (str.equalsIgnoreCase("Mouse")) {
            return MOUSE.toString();
        } else if (str.equalsIgnoreCase("Dog")) {
            return DOG.toString();
        } else {
            return null;
        }
    }
}


/**
* Protein existence
   Last modified September 9, 2013

This subsection of the ?Protein attributes? section indicates the type of evidence that supports the existence of the protein. Note that this subsection does not give information on the accuracy or correctness of the sequence(s) displayed. While it gives information on the existence of a protein, it may happen that the sequence slightly differs, especially for sequences derived from gene model predictions, from genomic sequences.

In UniProtKB there are 5 types of evidence for the existence of a protein:
1. Evidence at protein level
2. Evidence at transcript level
3. Inferred from homology
4. Predicted
5. Uncertain
The value ?Evidence at protein level? indicates that there is clear experimental evidence for the existence of the protein. The criteria include partial or complete Edman sequencing, clear identification by mass spectrometry, X-ray or NMR structure, good quality protein-protein interaction or detection of the protein by antibodies.

The value ?Evidence at transcript level? indicates that the existence of a protein has not been strictly proven but that expression data (such as existence of cDNA(s), RT-PCR or Northern blots) indicate the existence of a transcript.

The value ?Inferred by homology? indicates that the existence of a protein is probable because clear orthologs exist in closely related species.

The value ?Predicted? is used for entries without evidence at protein, transcript, or homology levels.

The value ?Uncertain? indicates that the existence of the protein is unsure.

Only the highest or most reliable level of supporting evidence for the existence of a protein is displayed for each entry. For example, if the existence of a protein is supported by both the presence of ESTs and direct protein sequencing, the protein is assigned the value ?Evidence at protein level?.

The ?protein existence? value is assigned automatically, based on the annotation elements present in the entry. The criteria used by this automatic procedure are listed in the document ?Criteria used to assign the PE level of entries?.

Link to relevant document
Criteria description for protein existence.
     */


