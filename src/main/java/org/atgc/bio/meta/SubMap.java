/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;
 
import org.atgc.bio.domain.IndexNames;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 


/**
 * SubMap are a nested map of String key values inside
 * a BioEntity. These can be persisted without structural loss and full
 * reversibility only to Mongo, since Mongo supports nested
 * objects, unlike Lucene and Neo4J. In case of Lucene and Neo4J, we convert
 * them into a key and value fields.
 * 
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface SubMap {
    public IndexNames indexName();
}