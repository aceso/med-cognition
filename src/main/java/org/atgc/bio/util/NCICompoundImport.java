/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.NCICompoundUtil;
import org.atgc.fileio.FileWrite;

import java.io.File;
import java.net.UnknownHostException;

/**
 *
 * @author jtanisha-ee
 */
public class NCICompoundImport {
    
    final static int MAX_INDEX = 6303; // currently max urls on hotdiary.com
    final static int DOC_INDEX = 3;
    private static String DIR_PATH = "/usr/local/redbasin/data/ncicompound/compound/";
    protected static Logger log = LogManager.getLogger(NCICompoundImport.class);
   
    
     /**
     * This method uses compound collection and adds gene symbol, import status and date to the compoundlistcollection
     * importstatus - due is by default.
     * @@throws UnknownHostException
     */
    /*private static void addCompoundList() throws UnknownHostException {
         NCICompoundUtil.addCompoundList();
    }*/

    public static void main(String[] args) throws java.io.IOException {

        File dir = new File(DIR_PATH);
        //File[] files = dir.listFiles(new ExtFilter("xml"));
        File[] files = dir.listFiles();

        //addCompoundList();

        for (int i = 1; i <= MAX_INDEX; i++) {
            String fileName = files[i].getName();
            log.info("fileName =" + fileName);
            String compoundName = fileName.substring(0, fileName.indexOf(".xml"));
            String s = FileWrite.readFile(DIR_PATH + fileName);
            //String url = "http://hotdiary.com/nci/compound/compound" + i + ".xml";
            //System.out.println( "i = " + i  + " url =" + url);
            NCICompoundUtil.addCompoundName(s);
            //NCICompoundUtil.addCompound(url);
        }     
        
    }
        
}
  