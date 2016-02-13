/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jtanisha-ee
 */

public enum AnnotationTypes {

    /**
     * @Indexed annotation
     */
    INDEXED(PackagePath.META + "Indexed"),

    /**
     * @NodeLabel annotation
     */
    NODE_LABEL(PackagePath.META + "NodeLabel"),

    /**
     *
     */
    UNIQUE_COMPOUND_INDEX(PackagePath.META + "UniqueCompoundIndex"),

    /**
     *
     */
    PARTKEY(PackagePath.META + "PartKey"),

    /**
     *
     */
    PART_RELATION(PackagePath.META + "PartRelation"),

    /**
     * @Visual annotation
     */
    VISUAL(PackagePath.META + "Visual"),

    /**
     * @FullTextIndexed annotation
     */
    FULLTEXT_INDEXED(PackagePath.META + "FullTextIndexed"),

    /**
     * @UniquelyIndexed annotation, must be at least 1 uniquely indexed field in an
     * BioEntity annotated object such as Protein, Gene, Disease.
     */
    UNIQUELY_INDEXED(PackagePath.META + "UniquelyIndexed"),

    /**
     * Not all fields are indexed. Some do not need to be searched on.
     */
    NON_INDEXED(PackagePath.META + "NonIndexed"),

    /**
     * @GraphId
     */
    GRAPH_ID(PackagePath.META + "GraphId"),

    /**
     * @RelatedToVia
     */
    RELATED_TO_VIA(PackagePath.META + "RelatedToVia"),

    RELATED_TO(PackagePath.META + "RelatedTo"),

    START_NODE(PackagePath.META + "StartNode"),

    END_NODE(PackagePath.META + "EndNode"),

    RELATIONSHIP_ENTITY(PackagePath.META + "RelationshipEntity"),

    BIO_ENTITY(PackagePath.META + "BioEntity"),

    REL_PROPERTY(PackagePath.META + "RelProperty"),

    BIO_RELATION(PackagePath.META + "BioRelation"),

    REL_TYPE(PackagePath.META + "RelType");


    private final String value;

    private static final Map<String, AnnotationTypes> stringToEnum = new HashMap<>();

    static { // init map from constant name to enum constant
        for (AnnotationTypes en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    AnnotationTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public String stripAt() {
        return value.replaceFirst("@", "");
    }

    public static AnnotationTypes fromString(String value) {
        return stringToEnum.get(value);
    }
}
