/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import java.util.HashMap;
import java.util.Map;

/**
 * These are all the supported Annotation Fields or {@link BioEntityClasses} objects.
 * These also are treated as dimensions.
 *
 * @author jtanisha-ee
 */

public enum AnnoFields {

    /**
     * BIOTYPE
     */
    BIOTYPE("bioType");

    private final String value;

    private static final Map<String, AnnoFields> stringToEnum = new HashMap<String, AnnoFields>();

    static { // init map from constant name to enum constant
        for (AnnoFields en : values())
            stringToEnum.put(en.toString(), en);
    }

    /**
     *
     * @param bioType
     * @return boolean
     */
    public boolean equals(AnnoFields bioType) {
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

    AnnoFields(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     *
     * @param value
     * @return AnnoFields
     */
    public static AnnoFields fromString(String value) {
        return stringToEnum.get(value);
    }
}
