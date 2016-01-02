/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain.types;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * db.pdbentity.distinct("Entity.@entityType")
[
	"protein",
	"dna",
	"hybrid",
	"rna",
	"protein_D",
	"saccharide",
	"other"
]

 * @author jtanisha-ee
 */

public enum StructureEntityTypes {

    /**
     *
     */
    PROTEIN("protein"),

    /**
     * For instance uniprot P42371 mapped to 1PJI in PDB.
     * <a href="http://www.uniprot.org/uniprot/P42371">Formamidopyrimidine-DNA glycosylase</a>
     */
    DNA("dna"),

    /**
     * For instance 1NH3 is mapped to P11387
     * <a href="http://www.uniprot.org/uniprot/P11387">DNA topoisomerase 1</a>
     */
    HYBRID("hybrid"),

    /**
     * For. eg. 2ZUF with uniprot mapping <a href="http://www.uniprot.org/uniprot/O59147">O59147</a>
     * Arginine--tRNA ligase.
     */
    RNA("rna"),

    /**
     * For instance 3PQC in PDB is mapped to Q9X1H7 in uniprot.
     */
    PROTEIN_D("protein_D"),

    /**
     * These will not be found in uniprot. They are carbohydrates. There are 4 types:
     * 
     */
    SACCHARIDE("saccharide"),

    /**
     *
     */
    OTHER("other");

    private final String value;

    private static final Map<String, StructureEntityTypes> stringToEnum = new HashMap<String, StructureEntityTypes>();

    static { // init map from constant name to enum constant
        for (StructureEntityTypes en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    /**
     *
     * @param bioType
     * @return boolean
     */
    public boolean equals(StructureEntityTypes bioType) {
        return value.equals(bioType.toString());
    }

    /**
     *
     * @param bioType
     * @return boolean
     */
    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    StructureEntityTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     *
     * @param value
     * @return StructureEntityTypes
     */
    public static StructureEntityTypes fromString(String value) {
        return stringToEnum.get(value);
    }
}