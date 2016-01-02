/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.domain.Protein;
import org.atgc.bio.meta.PackagePath;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 *
 * @author jtanisha-ee
 */
public class ClassInstantiator {
    
    protected static Log log = LogFactory.getLog(RedbasinTemplate.class);
    
    public Object createInstance(String className) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Class c = Class.forName(PackagePath.DOMAIN + className);
        return c.newInstance();
    }
    
    /**
     * The nodeType property in Node from graph db should be "Protein".
     * 
     * @param args
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException 
     */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        Protein protein = (Protein)new ClassInstantiator().createInstance("Protein");
        log.info("protein class = " + protein.getClass().getName());
    }
}
