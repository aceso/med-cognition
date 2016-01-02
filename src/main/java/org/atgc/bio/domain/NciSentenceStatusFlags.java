/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * For every relationship which has a new string relation, we add an enum here.
 * There would be lots of relationships in this file eventually.
 * <p>
 * Some graph vendors force us to have no spaces in the relationship. That is
 * why we use an underscore. This string is displayed in the graphdb gui, so we
 * need to be more user friendly. Use upper case for all the string names for
 * consistency.
 * <p>
 * Also add a property called "message" with the same value as this relationship.
 * This relation string is the same as the type property for the <code>RelationshipType</code>
 * 
 * 
 * @author jtanisha-ee
 */

public enum NciSentenceStatusFlags {
      
    /**
     * Roles for OncolRelations
     */
    FINISHED("finished"),
    
    NO_FACT("no_fact"),
    
    UNCLEAR("unclear"),
    
    REDUNDANT("redundant"),
    
    REDUNDANT_INFORMATION("redundant_information");
     
    private final String value;

    private static final Map<String, NciSentenceStatusFlags> stringToEnum = new HashMap<String, NciSentenceStatusFlags>();

    static { // init map from constant name to enum constant
        for (NciSentenceStatusFlags en : values())
            stringToEnum.put(en.toString(), en);
    }

    NciSentenceStatusFlags(String value) {
        this.value = value;
    }
    
    public boolean equals(NciSentenceStatusFlags flag) {
        return value.equals(flag.toString());
    }
    
    public boolean equals(String flag) {
        return value.equals(flag);
    }

    @Override
    public String toString() {
        return value;
    }

    public static NciSentenceStatusFlags fromString(String value) {
        return stringToEnum.get(value);
    }   
    
    public static boolean contains(String role) {
        for (NciSentenceStatusFlags en : values()) {
            if (en.toString().equals(role)) 
                return true;
        }
        return false;
    } 
   
}