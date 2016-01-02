/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



/**
 *
 * This is for displaying the node label in Neo4J. @Visual is the third component of
 * the node label. The variable in the BioEntity that will be used will be
 * marked @NodeLabel.
 *
 * bioType-uniqueIndexId-visualValue
 *
 * Exactly one variable in the @BioEntity object can be marked as @NodeLabel.
 *
 * The bioType is the nodeType or name of the BioEntity. Eg. "Protein", "Peptide".
 *
 * The visualValue is usually a shortLabel, shortName or some other name.
 * The @Visual is optional, and if not specified will not be used.
 *
 * The RedbasinTemplate looks at the @BioEntity and constructs the field that
 * has been marked as @NodeLabel.
 *
 * We use the message variable that is reserved for this use. The name of the
 * variable "message" is tied to the property name "message". This helps
 * the Neo4J visualization.
 *
 * @NodeLabel
 * @Indexed (indexName=IndexNames.MESSAGE)
 * @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
 * public String message;
 *
 * It was earlier marked as NonIndexed. Going forward, that should not be so.
 * It should be marked as @Indexed and @Taxonomy. In other words, it should be
 * possible to always search by a field that gets displayed in the visualization
 * of the graph.
 *
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface NodeLabel {
}