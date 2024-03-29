/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;

import org.atgc.bio.BioFields;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



/**
 * This is for creating compound keys on an index (typically in Neo4J).
 * These are associating with keys that are also relations. It so happens
 * the BioEntity may be a proxy. For instance, it's neither a Gene, nor
 * a GeneOntology. But it's a connector BioEntity called GeneToGo that connects the Gene
 * node to the GeneOntology node, with some special properties such as
 * a reference PubMedId, evidence and other metadata. This kind of information
 * is difficult to represent in a Neo4J graph without creating an intermediate
 * node, unfortunately. In other words, a goId is related to a pubMedId
 * only in the context of an ncbiGeneId. We could have added some metadata to
 * the relationship instead of using an intermediate dimension like GeneToGo
 * but the issue is that the pubMedId property that would have been added to
 * the relationship cannot be linked to a PubMed node visually, because of
 * the lack of such support in Neo4J.
 *
 * A single field like firstName in Author bioentity may not be unique.
 * So it needs to be combined with other fields to make it reasonably unique.
 * Both fields must be annotated as @PartKey in the bioentity object.
 * Although, Author clearly has issues because of lack of metadata that comes
 * from pubmed. The RedbasinTemplate reads the annotation metadata and
 * creates compound keys from the fields in the bioentity. We have three
 * field support currently, as annotations do not support variable params.
 * The field value separator for the compound key is not a responsibility of
 * this class, and is instead decided by the RedbasinTemplate. Not all fields
 * need be non-empty. However, the initialization must be in the order from
 * field1 to field2 and then field3. So if field1 is non-empty, and field3 is
 * non-empty, and field2 is empty, then field3 will be ignored, and an exception
 * will be thrown. If the user uses only one field, that is classified as
 * an error and an exception will be thrown. A minimum of two fields are required
 * to create a compound key. The value of the field1, field2 or field2 must
 * be the name of the java variable which is annotated with @PartRelation annotation.
 *
 * If a compound key consists of 3 @PartKey fields, and if only two are found,
 * then the template will throw an exception. @PartKey fields cannot be @Indexed
 * or @UniquelyIndexed at the same time.
 *
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface PartRelation {
     BioFields field();
}