/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;


import java.util.HashMap;
import java.util.Map;

/**
 * When package paths change due to refactoring, factory, template and generics 
 * code tend to be affected big-time. So we just standardize here.
 * 
 * @author jtanisha-ee
 */

public enum PackagePath {
    
    /**
     * Meta Path
     */
    META("@org.atgc.bio.meta."),
    
    /**
     * Domain Path
     */
    DOMAIN("org.atgc.bio.domain.");
    
    private final String value;
    
    @Override
    public String toString() {
        return value;
    }

    private static final Map<String, PackagePath> stringToEnum = new HashMap<String, PackagePath>();

    static { // init map from constant name to enum constant
        for (PackagePath en : values())
            stringToEnum.put(en.toString(), en);
    }

    PackagePath(String value) {
        this.value = value;
    }

    public static PackagePath fromString(String value) {
        return stringToEnum.get(value);
    }   
}
