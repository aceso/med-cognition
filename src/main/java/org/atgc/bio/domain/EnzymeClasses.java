/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import java.util.HashMap;
import java.util.Map;

/**
 * Database: ftp://ftp.expasy.org/databases/enzyme/
 * Readme: ftp://ftp.expasy.org/databases/enzyme/enzuser.txt
 *
    * http://www.enzyme-database.org/class.php
    *
    *  [+subclass]. [+sub-subclass]. [+serial]
    *
    *   EC 1	 [+] 	Oxidoreductases
    *   EC 2	 [+] 	Transferases
    *   EC 3	 [+] 	Hydrolases
    *   EC 4	 [+] 	Lyases
    *  EC 5	 [+] 	Isomerases
    *  EC 6	 [+] 	Ligases
    *

 *
 * @author jtanisha-ee
 */


public enum EnzymeClasses {
  
 
     EC_1("oxidoreducatses"),
     EC_2("Transferases"),
     EC_3("Hydrolases"),
     EC_4("Lysaes"),
     EC_5("Isomerases"),
     EC_6("Ligases");
    
    private final String value;

    private static final Map<String, EnzymeClasses> stringToEnum = new HashMap<String, EnzymeClasses>();

    static { // init map from constant name to enum constant
        for (EnzymeClasses en : values())
            stringToEnum.put(en.toString(), en);
    }
    
    public boolean equals(EnzymeClasses enumField) {
        return value.equals(enumField.toString());
    }
    
    public boolean equals(String s) {
        return value.equals(s);
    }

    EnzymeClasses(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static EnzymeClasses fromString(String value) {
        return stringToEnum.get(value);
    }  

}


