/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.domain.IndexNames;
import org.atgc.bio.meta.AnnotationTypes;
import org.atgc.bio.meta.UniquelyIndexed;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URISyntaxException;

/**
 * This class is used to create a home for the primary key, when an @UniquelyIndexed
 * is applied on a @BioEntity's field. There must be only one such index in a
 * BioEntity. Also returns null if a @BioEntity with a UniqueCompoundIndex is passed
 * instead of a primary UniquelyIndexed.
 *
 * @author jtanisha-ee
 */
public class PrimaryKey {

    private final IndexNames indexName;
    private final String key;
    private final String value;

    /**
     * We scan the bioentity and extract the key, value and indexName of the
     * UniquelyIndexed field. If no field is found, we return null.
     *
     * @param indexName
     * @param key
     * @param value
     */
    PrimaryKey(IndexNames indexName, String key, String val) {
        this.indexName = indexName;
        this.key = key;
        this.value = val;
    }

    public IndexNames getIndexName() {
        return indexName;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static boolean fieldMatch(Annotation anno, AnnotationTypes type) {
        return (anno.toString().startsWith(type.toString()));
    }

    public static <T> PrimaryKey getPrimaryKey(T t) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
        String key;
        String value;
        IndexNames indexName;

        Field[] fields = t.getClass().getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation anno = field.getAnnotation(UniquelyIndexed.class);
            if (anno != null) {
                key = field.getName();
                value = field.get(t).toString();
                indexName = ((UniquelyIndexed)anno).indexName();
                return new PrimaryKey(indexName, key, value);
            }
        }
        return null;
    }
}