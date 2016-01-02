/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.meta.AnnotationTypes;
import org.atgc.bio.meta.BioEntity;

import java.lang.annotation.Annotation;

/**
 * Some template utils to operate on Redbasin classes.
 *
 * @author jtanisha-ee
 * @param <T>
 */
public class TemplateUtils<T> {

    /**
     * This method extracts the BioTypes bioType of any class declared with the BioEntity
     * annotation. We strip the "@" symbol before we do an equals.
     *
     * @param <T>
     * @param t
     * @return
     */
    public static <T> BioTypes extractBioType(T t) {
        BioTypes bioType = null;
        Class annoClass = t.getClass();
        Annotation[] annotations = annoClass.getAnnotations();
        for (Annotation annotation : annotations) {
            if (annotation.annotationType().getName().equals(AnnotationTypes.BIO_ENTITY.stripAt())) {
                bioType = ((BioEntity)annotation).bioType();
                break;
            }
        }
        return bioType;
    }
}
