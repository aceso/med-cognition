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

public enum OncologyRoles {
      
    /**
     * Roles for OncolRelations
     */
    INPUT("INPUT"),
    
    INPUT_TO("INPUT_TO"),
    
    OUTPUT("OUTPUT"),
    
    OUTPUT_OF("OUTPUT_OF"),
    
    AGENT("AGENT"),
     
    INHIBITOR("INHIBITOR"),
    
    EDGE_TYPE("EDGE_TYPE"),
    
    INCOMING_EDGE("INCOMING_EDGE"),
    
    INTERACTOR("INTERACTOR"),
    
    OUTGOING_EDGE("OUTGOING_EDGE");
     
    private final String value;

    private static final Map<String, OncologyRoles> stringToEnum = new HashMap<String, OncologyRoles>();

    static { // init map from constant name to enum constant
        for (OncologyRoles en : values())
            stringToEnum.put(en.toString(), en);
    }

    OncologyRoles(String value) {
        this.value = value;
    }
    
    public boolean equals(OncologyRoles oncoRole) {
        return value.equals(oncoRole.toString());
    }
    
    public boolean equals(String role) {
        return value.equals(role);
    }

    @Override
    public String toString() {
        return value;
    }

    public static OncologyRoles fromString(String value) {
        return stringToEnum.get(value);
    }   
    
    public static boolean contains(String role) {
        for (OncologyRoles en : values()) {
            if (en.toString().equals(role)) 
                return true;
        }
        return false;
    } 
    
    public static boolean isOutput(String role) {
        if (role.equals((OncologyRoles.OUTPUT).toString()) ||
            role.equals((OncologyRoles.OUTPUT_OF).toString()))  {
            return true;
        } else {
            return false;
        }
    }
}