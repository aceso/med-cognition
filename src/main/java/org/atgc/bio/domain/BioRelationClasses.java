/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.meta.BioEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * These are primarily used in enums to check the type of the startNode and
 * endNode in case of relationships. But they can be used anywhere. We are
 * doing a equals comparison of the class object as opposed to comparing
 * the string names, as the string names are ambiguous and can be found
 * in variable paths. Also if the vendors change the package library, the
 * path names can change, but the class names will usually stay the same.
 *
 * Check places like {@link BioRelation} and {@link org.atgc.bio.meta.Feature} for
 * usage of this code. The isValid method in BioRelation in particular
 * uses this enum.
 *
 * The classes included in this are mainly {@link BioEntity} classes. We do not
 * add classes of other types here. This helps keep this enum clean.
 *
 * @author jtanisha-ee
 */

public enum BioRelationClasses {

    BIO_RELATION(BioRelation.class),

    COMPLEX_COMPONENT_RELATION(ComplexComponentRelation.class),

    PTM_EXPRESSION_RELATION(PtmExpressionRelation.class),

    FAMILY_MEMBER_RELATION(FamilyMemberRelation.class),

    ONCO_RELATION(OncoRelation.class),

    PART_MOLECULE_RELATION(PartMoleculeRelation.class);

    private final Class value;



    /**
     * This method is used to compare the class of the value in this
     * object to an external class and this equals method is recommended
     * to be used.
     *
     * @param entityClass
     * @return boolean
     */
    public boolean equals(Class entityClass) {
        return value.equals(entityClass);
    }

    /**
     * The getClass() method cannot be used, as that is associated with
     * the class name of this class. This method returns the class
     * of the value stored in this enum.
     *
     * @return Class
     */
    public Class getAnnotationClass() {
        return value;
    }

    /**
     * This only compares the string names, and we don't recommend this
     * being used.
     *
     * @param className
     * @return boolean
     */
    public boolean equals(String className) {
        return value.toString().equals(className);
    }

    BioRelationClasses(Class value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value.getName();
    }

    private static final Map<String, BioRelationClasses> stringToEnum = new HashMap<String, BioRelationClasses>();
    public static BioRelationClasses fromString(String value) {
        return stringToEnum.get(value);
    }

    static { // init map from constant name to enum constant
        for (BioRelationClasses en : values()) {
            stringToEnum.put(en.toString(), en);
        }

    }
}