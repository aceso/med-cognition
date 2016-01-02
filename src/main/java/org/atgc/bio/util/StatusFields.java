/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;


import java.util.HashMap;
import java.util.Map;

/**
 * Fields in NciThesaurus that are potentially lists. As we want to make sure
 * we create a BasicDBList for such fields while reading the ncithesaurus.obo
 * file. Because in Mongo, since a BasicDBObject cannot have two keys with
 * the same name, these keys must be kept in a BasicDBList.
 *
 * @author jtanisha-ee
 */

public enum StatusFields {

    DATE("createDate"),

    KEY("key"),

    VALUE("value"),

    STATUS("status");

    private final String value;

    private static final Map<String, StatusFields> stringToEnum = new HashMap<String, StatusFields>();

    static { // init map from constant name to enum constant
        for (StatusFields en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    public static boolean contains(String key) {
        for (StatusFields en : values()) {
            if (en.toString().equals(key)) {
                return true;
            }
        }
        return false;
    }

    public static boolean contains(StatusFields key) {
        for (StatusFields en : values()) {
            if (en.equals(key)) {
                return true;
            }
        }
        return false;
    }

    public boolean equals(StatusFields bioType) {
        return value.equals(bioType.toString());
    }

    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    StatusFields(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static StatusFields fromString(String value) {
        return stringToEnum.get(value);
    }
}