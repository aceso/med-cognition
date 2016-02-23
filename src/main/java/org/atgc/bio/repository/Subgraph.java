/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.AnnotationTypes;
import org.atgc.bio.meta.BioEntity;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.*;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.domain.BioTypes;
import org.neo4j.graphdb.NotFoundException;

/**
 *
 * Use {@link BioFields} to fetch {@link BioEntity}  and Nodes
 * @author jtanisha-ee
 * @param <T>
 *
 */
@SuppressWarnings("javadoc")
public class Subgraph<T> {

    protected static Logger log = LogManager.getLogger(Subgraph.class);

    private final Map<BioTypes, List> beMap = new EnumMap<>(BioTypes.class);

    private int cntr = 0;

   /**
    * Annotation bioAnno = @BioEntity(bioType=Complex)
    * @param t
    * @throws NotFoundException
    * @throws NoSuchFieldException
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
     * @throws java.lang.reflect.InvocationTargetException
    */
   public void add(T t) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Class tClass = t.getClass();
        String bioType = tClass.getSimpleName();  // For IntactGene.class, it will return "IntactGene"

        log.info("bioType = " + bioType);
        BioTypes bt = BioTypes.fromString(bioType);
        if (bt != null) {
            List beList;
            if (!beMap.containsKey(bt)) {
                beList = new ArrayList();
                beMap.put(bt, beList);
            } else {
                beList = beMap.get(bt);
            }
            beList.add(t);
            //log.info("add() BioEntity " + bt.toString());
            log.info("add() bioType = " + bioType + "," + (++cntr));
        } else {
            throw new RuntimeException("add() failed, missing BioType: " + bioType);
        }
    }

    public <T> T search(BioFields key, String value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        //log.info("search() " + key + "=" + value);
        for (List list : beMap.values()) {
           T t = fetch(list, key, value);
           if (t != null) {
              return t;
           }
        }
        return null;
    }

    public <T> T search(BioTypes bioType, BioFields key, String value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        //log.info("search()" + bioType.toString() + "," + key + "=" + value);
        if (beMap.containsKey(bioType)) {
            List list = beMap.get(bioType);
            T t = fetch(list, key, value);
            return t;
        }
        return null;
    }

    public <T> T fetch(List list, BioFields key, String value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException {
        //log.info("fetch() " + key.toString() + "="+ value);
        try {
            for (int i = 0; i < list.size(); i++) {
                Object obj = (Object)list.get(i);
                if (valueMatch(obj, key, value)) {
                    return (T)obj;
                }
            }
        } catch(IllegalArgumentException e) {
            throw new RuntimeException("Fetch(), error with argument " + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Fetch(), error with access " + e.getMessage(), e);
        }
        return null;
    }

    /**
     * Traverse all the entities.
     * @throws java.lang.IllegalAccessException
     */
    public void traverse() throws IllegalArgumentException, IllegalAccessException {
        log.info("**traverse()****");
        log.info(beMap.size());
        for (List list : beMap.values()) {
            log.info("list.size() " + list.size());
            for (int k = 0; k < list.size(); k++) {
                Object obj = list.get(k);
                if (obj != null) {
                    T t = (T)obj;
                    Class tClass = t.getClass();
                    Field[] fields = tClass.getDeclaredFields();
                    for (Field field : fields) {
                        field.setAccessible(true);
                        Annotation[] fieldAnnotations = field.getAnnotations();
                        for (Annotation fieldAnnotation : fieldAnnotations) {
                            if (fieldMatch(fieldAnnotation, AnnotationTypes.INDEXED)) {

                            }
                        }
                    }
                }
            }
        }
    }

    /**
     * fieldMatch
     * @param anno {@link Annotation}
     * @param type {@link AnnotationTypes}
     * @return boolean
     */
    public boolean fieldMatch(Annotation anno, AnnotationTypes type) {
        return (anno.toString().startsWith(type.toString()));
    }

    /**
     * valueMatch
     * @param t
     * @param bioField {@link BioFields}
     * @param value
     * @return
     * @throws java.lang.IllegalAccessException
     */
    public boolean valueMatch(Object t, BioFields bioField, String value) throws IllegalArgumentException, IllegalAccessException {
        Class tClass = t.getClass();
        Field[] fields = tClass.getDeclaredFields();
        for (Field field : fields) {
            field.setAccessible(true);
            if (field.getName().equals(bioField.toString())) {
                if (field.get(t) != null) {
                    String fVal = field.get(t).toString();
                    if (fVal != null && fVal.equals(value)) {
                        return true;
                    }
                }
            }
        }
        return false;
    }


    public Map<BioTypes, List> getBeMap() {
        return beMap;
    }


    /**
     *
     * @param args
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws NoSuchFieldException
     * @throws NotFoundException
     * @throws IllegalArgumentException
     * @throws InvocationTargetException
     */
    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException, NoSuchFieldException, NotFoundException, IllegalArgumentException, InvocationTargetException {
        Subgraph grMapper = new Subgraph();
    }

    /**
     * getBioEntityFromBioType
     * @param subGraph
     * @param bioType
     * @param key
     * @param value
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws URISyntaxException
     */
    public static Object getBioEntityFromBioType(Subgraph subGraph, BioTypes bioType, BioFields key, String value) throws NoSuchFieldException, IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException {
        //log.info("getBioEntityFromBioType()," + bioType.toString() + "," + key.toString() + "=" + value);
        return subGraph.search(bioType, key, value);
    }

}
