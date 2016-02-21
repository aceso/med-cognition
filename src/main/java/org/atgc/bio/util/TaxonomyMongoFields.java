/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import java.util.HashMap;
import java.util.Map;

/**
 * For every relationship which has a new string relation, we add an enum here.
 * There would be lots of relationships in this file eventually.
 * <p>
 * Some graph vendors force us to have no spaces in the relationship. That is
 * why we use an underscore. This string is displayed in the GraphDB gui, so we
 * need to be more user friendly. Use upper case for all the string names for
 * consistency.
 * <p>
 * Also add a property called "message" with the same value as this relationship.
 * This relation string is the same as the type property for the <code>RelationshipType</code>
 *
 *
 * @author jtanisha-ee
 */

public enum TaxonomyMongoFields {

    /**
     * NCBI taxonomy
     */
    TAX_ID("tax_id"),

    /**
     * NCBI taxonomy, taxonomy id
     */
    ID("id"),

    /**
     * NCBI taxonomy, taxonomy name.
     */
    NAME("name"),

    /**
     * NCBI taxonomy, "isA relationship"
     */
    IS_A("is_a"),

    /**
     * NCBI taxonomy, synonym list
     */
    SYNONYM_LIST("synonymList"),

    /**
     * NCBI taxonomy, synonym
     */
    SYNONYM("synonym"),

    /**
     * NCBI taxonomy, contains taxonomy rank
     */
    PROPERTY_VALUE("property_value"),

    /**
     * NCBI taxonomy, parsing out colon in property_value
     */
    COLON(":"),

    /**
     * NCBI taxonomy, related synonyms
     */
    RELATED("RELATED"),

    /**
     * NCBI taxonomy, exact synonym
     */
    EXACT("EXACT"),

    /**
     * NCBI taxonomy, xref eg. GC_ID:11
     */
    XREF("xref");
    
    private final String value;

    private static final Map<String, TaxonomyMongoFields> stringToEnum = new HashMap<>();

    static { // init map from constant name to enum constant
        for (TaxonomyMongoFields en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    TaxonomyMongoFields(String value) {
        this.value = value;
    }

    public boolean equals(TaxonomyMongoFields bioRelType) {
        return value.equals(bioRelType.toString());
    }

    public boolean equals(String bioRelType) {
        return value.equals(bioRelType);
    }

    @Override
    public String toString() {
        return value;
    }

    public static TaxonomyMongoFields fromString(String value) {
        return stringToEnum.get(value);
    }
}