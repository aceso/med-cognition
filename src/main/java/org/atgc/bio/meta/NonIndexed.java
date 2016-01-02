/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;
 
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 


/**
 * No indexing will happen in Neo4J. But we will index this field in Lucene.
 * Fields in BioEntity with no annotation will not be indexed in Lucene either.
 * 
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NonIndexed {
}
