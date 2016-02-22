/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;
 
import org.atgc.bio.BioFields;
import org.atgc.bio.domain.TaxonomyTypes;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 


/**
 * A 5-dimensional classification system. In this annotation, two dimensions
 * are collapsed. The other two are explicit.
 * 
 * 1. rbClass (explicit) - name of the classification
 * 2. rbField (explicit) - name of the java variable/neo4j property/mongo property
 * 3. indexName (collapsed) - name of the index where this can be searched
 * 4. term (collapsed) - this is typically the search term entered by user
 * 5. full-text (collapsed) - this helps when searching in full text index
 * 
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Taxonomy { 
     TaxonomyTypes rbClass();
     BioFields rbField();
}
