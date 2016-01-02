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

public enum ProteinEvidenceFields {
  
    /*
        1. Evidence at protein level
        2. Evidence at transcript level
        3. Inferred from homology
        4. Predicted
        5. Uncertain
    */
   
    EI1("EVIDENCE_AT_PROTEIN_LEVEL"),
    
    EI2("EVIDENCE_AT_TRANSCRIPT_LEVEL"),
        
    EI3("INFERRED_FROM_HOMOLOGY"),
        
    EI4("PREDICTED"),
        
    EI5("UNCERTAIN");
    
    
    /**
     * ProteinExistence field:
     *  2: Evidence at transcript level
     */

    private final String value;

    private static final Map<String, ProteinEvidenceFields> stringToEnum = new HashMap<String, ProteinEvidenceFields>();

    static { // init map from constant name to enum constant
        for (ProteinEvidenceFields en : values())
            stringToEnum.put(en.toString(), en);
    }
    
    public boolean equals(ProteinEvidenceFields bioType) {
        return value.equals(bioType.toString());
    }
    
    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    ProteinEvidenceFields(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ProteinEvidenceFields fromString(String value) {
        return stringToEnum.get(value);
    }  
    
    public static String getValue(String enumField) {
        if (enumField.equals(EI1)) {
           return EI1.toString();
        } else if (enumField.equals(EI2)) {
            return EI2.toString();
        } else if (enumField.equals(EI3)) {
            return EI3.toString();
        } else if (enumField.equals(EI4)) {
            return EI4.toString();
        } else if (enumField.equals(EI5)) {
            return EI5.toString();
        } else
            return null;
    }
    
    /**
     * ProteinExistence 
     * @param str
     * @return 
     */
    public static String getProteinExistenceValue(String str) {
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


