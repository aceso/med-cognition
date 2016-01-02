/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;
 
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 


/**
 *
 * Supports String properties for graph database
 * All other data types will throw an exception in the template
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RelProperty {}
