/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;

/**
 *
 * @author jtanisha-ee
 */

public enum AnnotationClasses {

    /**
     * RelationshipEntity class
     */
    RELATIONSHIP_ENTITY(RelationshipEntity.class);

    private final Class value;

    public boolean equals(AnnotationClasses bioType) {
        return value.equals(bioType.getClass());
    }

    public Class getAnnotationClass() {
        return value;
    }

    public boolean equals(String bioType) {
        return value.toString().equals(bioType);
    }

    AnnotationClasses(Class value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.getName();
    }
}