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

public enum GeneMongoFields {

    GENE_ID("GeneID"),

    NEWENTRY("NEWENTRY"),

    SYMBOL("Symbol"),

    DESCRIPTION("description"),

    TYPE_OF_GENE("type_of_gene"),

    MOD_DATE("Modification_date"),

    LOCUS_TAG("LocusTag"),

    OTHER_DESIGNATIONS("Other_designations"),

    GO_LIST("GOList"),

    GO_ID("GO_ID"),

    EVIDENCE("Evidence"),

    GO_TERM("GO_term"),

    CATEGORY("Category"),

    PUBMED("PubMed"),

    PUBMED_LIST("PubmedList"),

    GENE_LIST("GeneList"),

    TAX_ID("tax_id"),

    NCBI_TAXON("NCBITaxon"),

    GENOMIC_GI("genomic_gi"),

    GENOMIC_ACCESSION_VERSION("genomic_accession_version"),

    PUBMED_ID("PubMed_ID"),

    START_POSITION("start_position"),

    ORIENTATION("orientation"),

    DISTANCE_TO_LEFT("distance_to_left"),

    DISTANCE_TO_RIGHT("distance_to_right"),

    GENE_IDS_ON_RIGHT("GeneIDs_on_right"),

    GENE_IDS_ON_LEFT("GeneIDs_on_left"),

    OVERLAPPING_GENE_IDS("overlapping_GeneIDs"),

    CHROMOSOME("chromosome"),

    END_POSITION("end_position"),

    RELATIONSHIP("relationship"),

    OTHER_TAX_ID("Other_tax_id"),

    OTHER_GENE_ID("Other_GeneID");


    private final String value;

    private static final Map<String, GeneMongoFields> stringToEnum = new HashMap<String, GeneMongoFields>();

    static { // init map from constant name to enum constant
        for (GeneMongoFields en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    GeneMongoFields(String value) {
        this.value = value;
    }

    public boolean equals(GeneMongoFields bioRelType) {
        return value.equals(bioRelType.toString());
    }

    public boolean equals(String bioRelType) {
        return value.equals(bioRelType);
    }

    @Override
    public String toString() {
        return value;
    }

    public static GeneMongoFields fromString(String value) {
        return stringToEnum.get(value);
    }
}