/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain.types;

import java.util.HashMap;
import java.util.Map;

/**
 * In the PDB structure, there are 3 types of groups.
 * {@link http://biojava.org/wiki/BioJava:CookBook:PDB:datamodel}
 *
 * For instance using BioJava:
 *
 * <pre>
 * Chain chain;
 * chain.getAtomGroups("amino");
 * chain.getAtomGroups("nucleotide");
 * chain.getAtomGroups("hetatm");
 * </pre>
 * @author jtanisha-ee
 */

public enum AtomGroupTypes {

    /**
     *
     */
    AMINO("amino"),

    /**
     *
     */
    NUCLEOTIDE("nucleotide"),

    /**
     *
     */
    HETATM("hetatm");

    private final String value;

    private static final Map<String, AtomGroupTypes> stringToEnum = new HashMap<String, AtomGroupTypes>();

    static { // init map from constant name to enum constant
        for (AtomGroupTypes en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    /**
     *
     * @param bioType
     * @return boolean
     */
    public boolean equals(AtomGroupTypes bioType) {
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

    AtomGroupTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     *
     * @param value
     * @return AtomGroupTypes
     */
    public static AtomGroupTypes fromString(String value) {
        return stringToEnum.get(value);
    }
}