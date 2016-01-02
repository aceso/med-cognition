/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * This just tells a @RelationshipEntity's endNode java variable reference
 * whether a given end node is indeed an end node.
 * <p>
 * For instance, <code>IntactInteraction</code> is a @RelationshipEntity.
 * It has variable called endNode. We could have called this variable
 * stopNode or lastNode. The name does not matter. But the fact that we
 * declare an annotation called @EndNode for this variable, makes it the
 * end node.
 * <p>
 * This annotation is used only in relationship objects that are declared
 * with class annotation of @RelationshipEntity. There can only be one
 * such declaration of this annotation in such cases for obvious reasons.
 * Consequences are unknown if multiple declarations are made by the user.
 *
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD,ElementType.METHOD})
@Reference
public @interface EndNode {
}