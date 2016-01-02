/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;
 
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 


/**
 * GraphId is required for Neo4J. These graphIds can change, and are not guaranteed.
 * So it does not make sense to index them. This is one way of telling do not
 * index name, by using a special annotation called @GraphId. We distinguish that
 * from the @NonIndexed case, where it's actual metadata of our application (and not
 * of Neo4J) that we do not wish to index.
 * 
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface GraphId {}
