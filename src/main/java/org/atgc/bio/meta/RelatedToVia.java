/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;

import org.atgc.bio.domain.BioRelTypes;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import org.neo4j.graphdb.Direction;

/**
 * This annotation allows objects (which must match startNode) declared with <code>@BioEntity</code> annotation
 * to declare a relationship abstraction to a finite number of nodes (end nodes), that share
 * a common direction, common relationship class.
 * <p>
 * The idea is to store all the metadata in one place in this object, as that way,
 * the relationship class (elementClass) can be re-used for another relationship.
 * This is because, we anticipate that the properties of the relationship class
 * can be shared across many @RelatedToVia instances. The code to implement the
 * relationship class is bulky enough to justify not hard-coding this metadata
 * in the relationship (element) class.
 *
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface RelatedToVia {

    /**
     * This property is optional, as the default is <code>Direction.BOTH</code>.
     * The way Direction.BOTH works is that it allows both <code>Direction.INCOMING</code>
     * and <code>Direction.OUTGOING</code> directions. So upon saving the
     * {@link BioEntity} object, two explicit relationships each going in a different
     * direction are saved for each instance of the collection that is linked to
     * {@link RelatedToVia}.
     * <p>
     * Although the property is optional, please do exercise caution in using the
     * default, as indiscriminate paired relationships will be created, which besides
     * being needless, will also make no biological sense. For instance,
     * "Protein" isAPartOf "Pathway" makes sense, but "Pathway" isAPartOf "Protein"
     * makes no sense.
     * <p>
     * Each uni-directional relationship has a startNode and endNode. In case
     * of bi-directional relationships, the startNode and endNode are irrelevant.
     *
     * @return
     */
     Direction direction() default Direction.BOTH;

    /**
     * This is the relation type. There is also a variable called relType
     * in the BioRelation or @RelationshipEntity class, which stores the
     * same value. This is added for convenience as when we declare the
     * relationships in the @BioEntity, we can then know which relTypes
     * are being supported. Additionally the relType variable is required
     * when we populate the @RelationshipEntity after we fetch a relation from the
     * graph database using the template getRelations method.
     *
     * @return
     */
     BioRelTypes relType() default BioRelTypes.DEFAULT_RELATION;

    /**
     * Even though this field is optional, it is really should not be optional, as the
     * relationship class must be instantiated to make this work meaningfully.
     * The element class is the relationship class that can have many properties.
     * This element class is implemented by the users of this "meta" package.
     * The element classes are part of the "domain" or the object model that
     * defines the application.
     *
     * @return
     */
     Class<?> elementClass() default Object.class;

    /**
     * The endNode bioType of a relationship. In case of bi-directional relationships
     * this is just one end of the relationship. However from the vantage point
     * of the @BioEntity domain object, this is the "other" node's bioType which is
     * distinct from "this" node which is the startNode's bioType. By default
     * the BioType is unknown.
     * <p>
     * The unknown bioTypes are determined dynamically
     * using the nodeType parameter. If they are determined dynamically, then
     * why do we requiring specifying them as a parameter to this annotation?
     * This is because, we can then do faster type specific validation of
     * end node bio types in the template. This helps prevent inadvertently
     * linking nodes of incompatible biotypes by a relationship. This technique
     * is called bioType enforcement.
     *
     * @return
     */
 //   public BioTypes endNodeBioType() default BioTypes.UNKNOWN;

    /**
     * The startNode bioType of a relationship. In case of bi-directional relationships
     * this is just one end of the relationship. However from the vantage point
     * of the @BioEntity domain object, this is the "other" node's bioType which is
     * distinct from "this" node which is the startNode's bioType. By default
     * the BioType is unknown.
     * <p>
     * The unknown bioTypes are determined dynamically
     * using the nodeType parameter. If they are determined dynamically, then
     * why do we requiring specifying them as a parameter to this annotation?
     * This is because, we can then do faster type specific validation of
     * end node bio types in the template. This helps prevent inadvertently
     * linking nodes of incompatible biotypes by a relationship. This technique
     * is called bioType enforcement.
     */
  //  public BioTypes startNodeBioType() default BioTypes.UNKNOWN;
}