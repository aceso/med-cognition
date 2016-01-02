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

public enum TaxonomyRank {

    /**
     *
     */
    SUPERKINGDOM("superkingdom"),

    /**
     *
     */
    GENUS("genus"),

    /**
     *
     */
    SPECIES("species"),

    /**
     *
     */
    ORDER("order"),

    /**
     *
     */
    FAMILY("family"),

    /**
     *
     */
    SUBSPECIES("subspecies"),

    /**
     *
     */
    SUBFAMILY("subfamily"),

    /**
     *
     */
    TRIBE("tribe"),

    /**
     *
     */
    PHYLUM("phylum"),

    /**
     *
     */
    CLASS("class"),

    /**
     *
     */
    FORMA("forma"),

    /**
     *
     */
    SUBORDER("suborder"),

    /**
     *
     */
    SUPERCLASS("superclass"),

    /**
     *
     */
    VARIETAS("varietas"),

    /**
     *
     */
    SUBCLASS("subclass"),

    /**
     *
     */
    KINGDOM("kingdom"),

    /**
     *
     */
    SUPERFAMILY("superfamily"),

    /**
     *
     */
    INFRAORDER("infraorder"),

    /**
     *
     */
    SUBPHYLUM("subphylum"),

    /**
     *
     */
    INFRACLASS("infraclass"),

    /**
     *
     */
    SUPERORDER("superorder"),

    /**
     *
     */
    SUBGENUS("subgenus"),

    /**
     *
     */
    PARVORDER("parvorder"),

    /**
     *
     */
    SUPERPHYLUM("superphylum"),

    /**
     *
     */
    SPECIES_GROUP("species_group"),

    /**
     *
     */
    SPECIES_SUBGROUP("species_subgroup"),

    /**
     *
     */
    SUBTRIBE("subtribe"),

    /**
     *
     */
    SUBKINGDOM("subkingdom");

    private final String value;

    private static final Map<String, TaxonomyRank> stringToEnum = new HashMap<String, TaxonomyRank>();

    static { // init map from constant name to enum constant
        for (TaxonomyRank en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    /**
     *
     * @param bioType
     * @return boolean
     */
    public boolean equals(TaxonomyRank bioType) {
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

    TaxonomyRank(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     *
     * @param value
     * @return TaxonomyRank
     */
    public static TaxonomyRank fromString(String value) {
        return stringToEnum.get(value);
    }
}