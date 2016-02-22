/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;
 
import org.atgc.bio.domain.IndexNames;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 


/**
 * In case of full text indexes, if there are multiple space separated
 * terms, we add one row per term in the Taxonomy collection in Mongo.
 * Fields declared as @FullTextIndexed in @BioEntity objects should contain
 * terms that are space separated.
 *
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface FullTextIndexed {
     IndexNames indexName();
}
