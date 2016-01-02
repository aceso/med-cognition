/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;
import org.atgc.bio.NCIDiseaseUtil;
import java.net.UnknownHostException;

/**
 *
 * @author jtanisha-ee
 */
public class NCIDiseaseImport {
    
    final static int MAX_INDEX = 7181; // currently max urls on hotdiary.com
    final static int DOC_INDEX = 3;
   
    
     /**
     * This method uses compound collection and adds gene symbol, import status and date to the diseaselistcollection
     * importstatus - due is by default. 
     * @@throws UnknownHostException
     */
    private static void addDiseaseList() throws UnknownHostException {
         NCIDiseaseUtil.addDiseaseList();
    }
    
    
    public static void main(String[] args) throws java.io.IOException {
        for (int i = 1; i <= MAX_INDEX; i++) {           
            String url = "http://hotdiary.com/nci/disease/disease" + i + ".xml";
            //System.out.println( "i = " + i  + " url =" + url);
            String geneSymbol = NCIDiseaseUtil.addDisease(url);
            NCIDiseaseUtil.addOneDiseaseToList(geneSymbol);
        }     
        
    }
        
}
  