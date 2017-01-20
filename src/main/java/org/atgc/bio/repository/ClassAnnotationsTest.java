/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.BioFields;
import org.atgc.bio.domain.Author;
import org.atgc.bio.meta.AnnotationTypes;
import org.atgc.bio.meta.PartKey;
import org.atgc.bio.meta.UniqueCompoundIndex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;

/**
 *
 * @author jtanisha-ee
 */
public class ClassAnnotationsTest {

    public static boolean fieldMatch(Annotation anno, AnnotationTypes type) {
        return (anno.toString().startsWith(type.toString()));
    }

    public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException {
        Author author = new Author();
        author.setForeName("John");
        author.setInitials("K");
        author.setLastName("Doe");
        BioFields field1 = null;
        BioFields field2 = null;
        BioFields field3 = null;
        Annotation[] classAnnotations = author.getClass().getAnnotations();
        for (Annotation annotation : classAnnotations) {
            if (fieldMatch(annotation, AnnotationTypes.UNIQUE_COMPOUND_INDEX)) {
                field1 = ((UniqueCompoundIndex)annotation).field1();
                field2 = ((UniqueCompoundIndex)annotation).field2();
                field3 = ((UniqueCompoundIndex)annotation).field3();
            }
        }
        Field[] fields = author.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        String key1 = null;
        String key2 = null;
        String key3 = null;
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getName().equals(field1)) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    key1 = (String)f.get(author);
                }
            }
            if (f.getName().equals(field2)) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    key2 = (String)f.get(author);
                }
            }
            if (f.getName().equals(field3)) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    key3 = (String)f.get(author);
                }
            }
        }
        sb.append(key1).append("-").append(key2).append("-").append(key3);
        //System.out.println("index key = " + sb.toString());

    }
}
