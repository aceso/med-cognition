/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jtanisha-ee
 */

public enum ModelFields {
    
    /**
     * nodeType
     */
    NODE_TYPE("nodeType"),
    
    /**
     * Message field
     */
    MESSAGE("message");

    private final String value;

    private static final Map<String, ModelFields> stringToEnum = new HashMap<String, ModelFields>();

    static { // init map from constant name to enum constant
        for (ModelFields en : values())
            stringToEnum.put(en.toString(), en);
    }
    
    public boolean equals(ModelFields bioType) {
        return value.equals(bioType.toString());
    }
    
    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    ModelFields(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static ModelFields fromString(String value) {
        return stringToEnum.get(value);
    }   
}
