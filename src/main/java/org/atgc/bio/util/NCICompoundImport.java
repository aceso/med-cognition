/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;
import org.atgc.bio.NCICompoundUtil;

import java.net.UnknownHostException;

/**
 *
 * @author jtanisha-ee
 */
public class NCICompoundImport {
    
    final static int MAX_INDEX = 6303; // currently max urls on hotdiary.com
    final static int DOC_INDEX = 3;
   
    
     /**
     * This method uses compound collection and adds gene symbol, import status and date to the compoundlistcollection
     * importstatus - due is by default.
     * @@throws UnknownHostException
     */
    private static void addCompoundList() throws UnknownHostException {
         NCICompoundUtil.addCompoundList();
    }
    
    
    public static void main(String[] args) throws java.io.IOException {
        for (int i = 1; i <= MAX_INDEX; i++) {           
            String url = "http://hotdiary.com/nci/compound/compound" + i + ".xml";
            //System.out.println( "i = " + i  + " url =" + url);
            NCICompoundUtil.addCompound(url);
        }     
        
    }
        
}
  