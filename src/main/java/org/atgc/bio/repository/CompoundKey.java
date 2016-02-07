/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.BioFields;
import org.atgc.bio.domain.BioRelation;
import org.atgc.bio.domain.Complex;
import org.atgc.bio.domain.IndexNames;
import org.atgc.bio.meta.AnnotationTypes;
import org.atgc.bio.meta.PartKey;
import org.atgc.bio.meta.PartRelation;
import org.atgc.bio.meta.UniqueCompoundIndex;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.URISyntaxException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is used to create a home for the virtual dynamic compound key, when an @UniqueCompoundIndex
 * is applied on a @BioEntity. The Compound key supports a maximum of three components.
 * Handling sparse metadata phenomena in higher dimensional topologies projected by
 * a @BioEntity graph matrix. PartKey and PartRelation can be intermixed in
 * a compound key.
 *
 * @author jtanisha-ee
 */
public class CompoundKey {

    private final IndexNames indexName;
    private final String key;
    private final String value;
    protected static Logger log = LogManager.getLogger(CompoundKey.class);

    /**
     * We assume that key1 and key2 are not null. And their values are also
     * not null and additionally non-empty. In other words, a compound must have
     * a minimum of two non-null key components. key3 however is optional and we check
     * for null and emptiness. We consider val3 only if key3 is non-null and non-empty.
     *
     * @param indexName
     * @param key1
     * @param key2
     * @param key3
     * @param value
     */
    public CompoundKey(IndexNames indexName, BioFields key1, BioFields key2, BioFields key3, String val1, String val2, String val3) {
        this.indexName = indexName;
        StringBuilder sbKey = new StringBuilder();
        StringBuilder sbVal = new StringBuilder();
        int cntr = 0;
        if (key1 == null) {  // reverse counting of null keys
            cntr++;
        }
        if (key2 == null) {
            cntr++;
        }
        if (key3 == null) {
            cntr++;
        }
        if (cntr >= 2) {
            throw new IllegalArgumentException("At least two keys must be specified.");
        }
        
        sbKey.append(key1);
        sbKey.append("-").append(key2);
        if (key3 != null) {
            sbKey.append("-").append(key3);
        }
        int valcntr = 0;
        if (key1 != null) {
            if (val1 == null || val1.isEmpty()) {
                valcntr++;
            } else {
                sbVal.append(val1);
            }
        }
        if (key2 != null) {
            if (val2 == null || val2.isEmpty()) {
                valcntr++;
            } else {
                if (sbVal.length() != 0) {
                    sbVal.append("-");
                }
                sbVal.append(val2);
            }
        }
        if (key3 != null) {
            if (val3 == null || val3.isEmpty()) {
                valcntr++;
            } else {
                if (sbVal.length() != 0) {
                    sbVal.append("-");
                }
                sbVal.append(val3);
            }
        }
        if (valcntr > 1) { // fixed the bug
            log.info(key1 + "=" + val1 + "," + key2 + "=" + val2 + "," + key3 + "=" + val3);      
            throw new IllegalArgumentException("Not enough values provided. key1 = " + key1 + ", key2 = " + key2 + ", key3 = " + key3);
            
        }
        key = sbKey.toString();
        value = sbVal.toString();
    }

    public IndexNames getIndexName() {
        return indexName;
    }

    public String getKey() {
        return key;
    }

    public String getValue() {
        return value;
    }

    public static boolean fieldMatch(Annotation anno, AnnotationTypes type) {
        return (anno.toString().startsWith(type.toString()));
    }

    public static <T> CompoundKey getCompoundKey(T t) throws IllegalArgumentException, IllegalAccessException, URISyntaxException, NoSuchFieldException {
        BioFields field1;
        BioFields field2;
        BioFields field3;
        IndexNames indexName;

        Annotation classAnnotation = t.getClass().getAnnotation(UniqueCompoundIndex.class);
        if (classAnnotation != null) {
            indexName = ((UniqueCompoundIndex)classAnnotation).indexName();
            field1 = ((UniqueCompoundIndex)classAnnotation).field1();
            field2 = ((UniqueCompoundIndex)classAnnotation).field2();
            field3 = ((UniqueCompoundIndex)classAnnotation).field3();
        } else {
            return null;
        }
        Field[] fields = t.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        String val1 = null;
        String val2 = null;
        String val3 = null;
        for (Field f : fields) {
            f.setAccessible(true);
            if (field1.equals(f.getName())) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    val1 = (String)f.get(t);
                    if (val1 == null) {
                        val1 = "";
                    }
                    log.info("val1 = " + val1);
                } else {
                    anno = f.getAnnotation(PartRelation.class);
                    if (anno != null) {
                        //log.info("field name = " + f.getName());
                        //log.info("this node type = " + t.getClass().getSimpleName());
                        Object endNode = ((BioRelation)f.get(t)).getEndNode();
                        if (endNode != null) {
                            //log.info("endNode type = " + endNode.getClass().getSimpleName());
                            String val = ((PartRelation) anno).field().toString();
                            //log.info("val from PartRelation = " + val);
                            Field f1 = endNode.getClass().getDeclaredField(val);
                            f1.setAccessible(true);
                            val1 = (String) f1.get(endNode);
                        }
                        //val1 = ((PartRelation)anno).field().toString();
                    }
                }
            }
            if (field2.equals(f.getName())) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    val2 = (String)f.get(t);
                    if (val2 == null) {
                        val2 = "";
                    }
                    log.info("val2 = " + val2);
                } else {
                    anno = f.getAnnotation(PartRelation.class);
                    if (anno != null) {
                       // log.info("field name = " + f.getName());
                       // log.info("this node type = " + t.getClass().getSimpleName());
                        String val = ((PartRelation)anno).field().toString();
                        Object endNode = ((BioRelation)f.get(t)).getEndNode();
                        Field f1 = endNode.getClass().getDeclaredField(val);
                        f1.setAccessible(true);
                        val2 = (String)f1.get(endNode);
                    }
                }
            }
            if (field3.equals(f.getName())) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    val3 = (String)f.get(t);
                    if (val3 == null) {
                        val3 = "";
                    }
                    log.info("val3 = " + val3);
                } else {
                    anno = f.getAnnotation(PartRelation.class);
                    if (anno != null) {
                       // log.info("field name = " + f.getName());
                       // log.info("this node type = " + t.getClass().getSimpleName());
                        String val = ((PartRelation)anno).field().toString();
                        Object endNode = ((BioRelation)f.get(t)).getEndNode();
                        Field f1 = endNode.getClass().getDeclaredField(val);
                        f1.setAccessible(true);
                        val3 = (String)f1.get(endNode);
                    }
                }
            }
        }
        try {
            log.info("compoundKey=" + indexName + ", field1=" + val1 + ", field2=" + val2 + ",field3=" + val3);
            return new CompoundKey(indexName, field1, field2, field3, val1, val2, val3);
        } catch(IllegalArgumentException e) {
            log.error("CompoundKey " + e.toString(), e);
            return null;
        }
    }
}