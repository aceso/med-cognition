/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.BioEntityType;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.domain.BioRelation;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.RelationshipType;

/**
 *
 * Use {@link BioFields} to fetch {@link BioEntity}  and Nodes
 * @author jtanisha-ee
 *
 */
public class GraphMapper {

    protected static Log log = LogFactory.getLog(GraphMapper.class);
    private static final String BIO_RELATION = "BioRelation";


    /**
     * Given a node, returns BioEntity with properties
     * @param <T>
     * @param node
     * @return BioEntity
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     */
    public static <T> T getBioEntity(Node node) throws ClassNotFoundException, IllegalAccessException, InstantiationException {

        log.info("getBioEntity(node)");
        BioTypes bioType = getBioType(node);
        if (bioType != null) {
            log.info("getBioEntity(node), bioType =" + bioType);
            T obj = (T)new ClassInstantiator().createInstance(bioType.toString());
            Class bioEntity = obj.getClass();
            Annotation bioEntityAnno = bioEntity.getAnnotation(BioEntity.class);
            if (bioEntityAnno == null) {
                throw new RuntimeException("Only retrieving @BioEntity objects are supported.");
            }

            Field[] fields = bioEntity.getDeclaredFields();
            /** set Unique Index */
            /** set Node properties in BioEntity */
            for (String propKey : node.getPropertyKeys()) {
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equals(propKey)) {
                        field.set(obj, node.getProperty(propKey));
                        log.info(field.getName() + "=" + field.get(obj));
                    }
                }
            }
            log.info("BioEntity =" + bioType + " " + obj.toString());
            return obj;
        } else {
            log.info("getBioEntity(node), bioType is null");
            return null;
        }
    }

    /**
     * Returns BioType of the node
     * getBioType
     * @param node
     * @return BioTypes
     */
    public static BioTypes getBioType(Node node) throws NotFoundException {
        log.info("getBioType(node)");
        if (node != null && node.hasProperty(BioEntityType.NODE_TYPE.toString())) {
            String bioType = (String)node.getProperty(BioEntityType.NODE_TYPE.toString());
            if (bioType != null) {
                log.info("bioType =" + bioType);
                return BioTypes.fromString(bioType);
            }

        } else {
            return null;
        }
        return null;
    }

    /**
     * Given a relationship, returns BioRelation
     * @param rel
     * @return BioRelation
     * @throws ClassNotFoundException
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    public static BioRelation getRelation(Relationship rel) throws ClassNotFoundException, InstantiationException, IllegalAccessException {

        log.info("getRelation(rel)");

        Object obj = new ClassInstantiator().createInstance(BIO_RELATION);
        Class bioRelationClass = obj.getClass();
        Annotation bioRelationAnno = bioRelationClass.getAnnotation(BioRelation.class);
        if (bioRelationAnno == null) {
           throw new RuntimeException("Only retrieving BioRelation object is supported.");
        }

        BioRelation bioRelation = (BioRelation)obj;
        RelationshipType relationshipType = rel.getType();
        String type = relationshipType.name();
        for (String propKey : rel.getPropertyKeys()) {
            BioRelTypes brt = BioRelTypes.fromString(type);
            if (brt == null) {
                throw new RuntimeException("Only known relations are supported, not this one: " + type);
            }
            bioRelation.setRelType(brt);
            for (Field field : bioRelation.getClass().getDeclaredFields()) {
                field.setAccessible(true);
                if (field.getName().equals(propKey)) {
                    log.info("relation propKey = " + propKey + ", relation propValue = " + rel.getProperty(propKey));
                    field.set(bioRelation, rel.getProperty(propKey));
                }
            }
        }
        return bioRelation;
    }





    public static void main(String[] args) throws ClassNotFoundException, InstantiationException, IllegalAccessException {
        GraphMapper grMapper = new GraphMapper();
        //createNode();
        Node node = null;
        BioEntity bioEntity = GraphMapper.getBioEntity(node);
        log.info("protein class = " + bioEntity.getClass().getName());
    }

}
