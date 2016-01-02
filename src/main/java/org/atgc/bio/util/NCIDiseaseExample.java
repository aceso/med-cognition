/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import org.atgc.bio.NCIDiseaseUtil;

/**
 *
 * @author jtanisha-ee
 */
public class NCIDiseaseExample {
    
    final static int MAX_INDEX = 7181; // currently max urls on hotdiary.com
    final static int DOC_INDEX = 3;
    
    public static void main(String[] args) throws java.io.IOException {
        for (int i = 1; i <= DOC_INDEX; i++) {
            String diseaseUrl = "http://hotdiary.com/nci/disease/disease" + i + ".xml";
            NCIDiseaseUtil.addDisease(diseaseUrl);
        }
    }
        
}
