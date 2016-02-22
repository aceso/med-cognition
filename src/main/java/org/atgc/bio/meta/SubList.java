/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;
 
import org.atgc.bio.domain.IndexNames;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 


/**
 * SubList are a nested list of String values such as alternate accession numbers inside
 * a BioEntity. These can be persisted without structural loss only to Mongo, 
 * since Mongo supports nested objects, unlike Lucene and Neo4J. In case of
 * Lucene and Neo4J we will convert them into full text field and concatenate
 * all the values with space separation.
 * 
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SubList {
     IndexNames indexName();
}