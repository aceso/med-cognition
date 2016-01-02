/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain.types;

import java.util.HashMap;
import java.util.Map;

/**
 * Taxonomy ranks as found in property_value property of ncbitaxonomy collection.
 * The entry appears as: "has_rank NCBITaxon:kingdom", and needs to be parsed out.
 *
 * @author jtanisha-ee
 */

public enum LigandEntityTypes {

    /**
     *
     */
    SACCHARIDE("saccharide"),

    /**
     *
     */
    D_SACCHARIDE("D-saccharide"),

    /**
     *
     */
    NON_POLYMER("non-polymer"),

    /**
     *
     */
    L_PEPTIDE_LINKING("L-peptide linking"),

    /**
     *
     */
    RNA_LINKING("RNA linking"),

    /**
     *
     */
    DNA_LINKING("DNA linking"),

    /**
     *
     */
    D_SACCHARIDE_1_4_LINKING("D-saccharide 1, 4 and 1, 4 linking"),

    /**
     *
     */
    D_PEPTIDE_LINKING("D-peptide linking"),

    /**
     *
     */
    D_PEPTIDE_LIKE("peptide-like"),

    /**
     *
     */
    L_SACCHARIDE("L-saccharide"),

    /**
     *
     */
    PEPTIDE_LINKING("peptide linking"),

    /**
     *
     */
    L_PEPTIDE_COOH_CARBOXY_TERMINUS("L-peptide COOH carboxy terminus"),

    /**
     *
     */
    L_SACCHARIDE_1_4_LINKING("L-saccharide 1, 4 and 1, 4 linking"),

    /**
     *
     */
    D_BETA_PEPTIDE_C_GAMMA_LINKING("D-beta-peptide, C-gamma linking"),

    /**
     *
     */
    D_GAMMA_PEPTIDE_C_DELTA_LINKING("D-gamma-peptide, C-delta linking"),

    /**
     *
     */
    L_PEPTIDE_NH3_AMINO_TERMINUS("L-peptide NH3 amino terminus"),

    /**
     *
     */
    L_BETA_PEPTIDE_C_GAMMA_LINKING("L-beta-peptide, C-gamma linking"),

    /**
     *
     */
    L_GAMMA_PEPTIDE_C_DELTA_LINKING("L-gamma-peptide, C-delta linking"),

    /**
     *
     */
    L_RNA_LINKING("L-RNA linking"),

    /**
     *
     */
    L_DNA_LINKING("L-DNA linking"),

    /**
     *
     */
    DNA_OH_3_PRIME_TERMINUS("DNA OH 3 prime terminus"),

    /**
     *
     */
    D_PEPTIDE_NH3_AMINO_TERMINUS("D-peptide NH3 amino terminus"),

    /**
     *
     */
    RNA_OH_3_PRIME_TERMINUS("RNA OH 3 prime terminus");

    private final String value;

    private static final Map<String, LigandEntityTypes> stringToEnum = new HashMap<String, LigandEntityTypes>();

    static { // init map from constant name to enum constant
        for (LigandEntityTypes en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    /**
     *
     * @param bioType
     * @return boolean
     */
    public boolean equals(LigandEntityTypes bioType) {
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

    LigandEntityTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     *
     * @param value
     * @return LigandEntityTypes
     */
    public static LigandEntityTypes fromString(String value) {
        return stringToEnum.get(value);
    }
}