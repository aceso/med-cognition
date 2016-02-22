/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;
 
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
 


/**
 * This is for displaying the node label in Neo4J. It is the third component of 
 * the node label. The variable in the BioEntity that will be used will be 
 * marked @NodeLabel.
 * 
 * bioType-uniqueIndexId-visualValue
 * 
 * At most one variable in the @BioEntity object can be marked as @Visual. 
 * If none are marked, then the label would need to look like
 * 
 * bioType-uniqueIndexId
 * 
 * The uniqueIndexId field is taken from the @UniquelyIndexed field in the
 * {@link BioEntity}. This is mandatory. All BioEntities must have a unique @UniquelyIndexed
 * field.
 * 
 * The bioType is the nodeType or name of the BioEntity. Eg. "Protein", "Peptide".
 * 
 * The visualValue is usually a shortLabel, shortName or some other name.
 * 
 * The RedbasinTemplate looks at the @BioEntity and constructs the field.
 * 
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface Visual {
}
