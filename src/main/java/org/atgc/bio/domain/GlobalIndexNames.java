package org.atgc.bio.domain;

import java.util.HashMap;
import java.util.Map;

/**
 * Index names in Neo4J.
 */
public enum GlobalIndexNames {
 
    /**
     * node type
     */
   NODE_TYPE("NodeType");
    
   
 
    private final String value;

    private static final Map<String, GlobalIndexNames> stringToEnum = new HashMap<String, GlobalIndexNames>();

    static { // init map from constant name to enum constant
        for (GlobalIndexNames en : values())
            stringToEnum.put(en.toString(), en);
    }

    GlobalIndexNames(String value) {
        this.value = value;
    }
    
    public boolean equals(String indexName) {
        return value.equals(indexName);
    }
    
    public boolean equals(GlobalIndexNames indexName) {
        return value.equals(indexName.toString());
    }

    @Override
    public String toString() {
        return value;
    }

    public static GlobalIndexNames fromString(String value) {
        return stringToEnum.get(value);
    }   
}
