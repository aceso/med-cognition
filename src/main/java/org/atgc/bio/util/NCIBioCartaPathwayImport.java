
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.NCIBioCartaPathwayUtil;
import org.atgc.fileio.FileWrite;
import java.io.File;
import java.net.URISyntaxException;

/**
 * 
 * 
 * @author jtanisha-ee
 */
public class NCIBioCartaPathwayImport {
   //private static String DIR_PATH = "/Users/smitha/Downloads/nci-nature-pathways/";
   private static String DIR_PATH = "/usr/local/redbasin/data/nci-biocarta-pathways/";
    protected static Logger log = LogManager.getLogger(NCIBioCartaPathwayImport.class);

    public static void main(String[] args) throws java.io.IOException, URISyntaxException {
       
        File dir = new File(DIR_PATH);
        //File[] files = dir.listFiles(new ExtFilter("xml"));
        File[] files = dir.listFiles();
       
        log.info("length =" + files.length);
        //for (int i = 0; i < diseases.length; i++) {
        for (int i = 0; i < files.length; i++) {
            String fileName = files[i].getName();
            log.info("fileName =" + fileName);
            String pathwayName = fileName.substring(0, fileName.indexOf(".xml"));
            String s = FileWrite.readFile(DIR_PATH + fileName);
            //System.out.println("file =" + s);
            NCIBioCartaPathwayUtil.addPathwayFile(s, pathwayName);
            NCIBioCartaPathwayUtil.addPathwayList(pathwayName);
        }
    }      
}