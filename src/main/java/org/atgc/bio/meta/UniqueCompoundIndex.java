/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;

import org.atgc.bio.BioFields;
import org.atgc.bio.domain.IndexNames;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



/**
 * This is for creating compound keys on an index (typically in Neo4J).
 * A single field like firstName in Author bioentity may not be unique.
 * So it needs to be combined with other fields to make it reasonably unique.
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
 * be the name of the java variable which is annotated with @PartKey annotation.
 *
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface UniqueCompoundIndex {

     IndexNames indexName();

     BioFields field1();

     BioFields field2();

     BioFields field3();

}
