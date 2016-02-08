/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.domain.*;
import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.atgc.bio.BioFields;
import org.atgc.bio.meta.*;
import org.atgc.bio.util.StatusUtil;
import org.atgc.init.Config;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoUtil;
import org.atgc.neo4j.NeoUtil;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.*;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpException;
import org.apache.lucene.index.CorruptIndexException;
import org.neo4j.graphdb.*;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;

/**
 * @param <T>
 * @author jtanisha-ee
 *         Use BioFields to fetch BioEntities and Nodes
 */
public class PersistenceTemplate<T> {

    protected static final Logger log = LogManager.getLogger(PersistenceTemplate.class);

    private static RestGraphDatabase graphDb;

    private static int cntr = 0;
    private static long indexNodeCount;
    private static long propertyCount;

    static {
        try {
            setup();
        } catch (URISyntaxException ex) {
            log.error("Could not initialize RestGraphDatabase ", ex);
        }
    }

    private static void setup() throws URISyntaxException {
        //String dbUrl = "http://localhost:7474/db/data";
        String dbUrl = Config.NEO4J_DATA_DIR.toString();
        //String dbUrl = "http://saibaba.local:7474/db/data";
        graphDb = new RestGraphDatabase(dbUrl);
        registerShutdownHook(graphDb);
    }

    private static void registerShutdownHook(RestGraphDatabase graphDb1) {
        // Registers a shutdown hook for the Neo4j instance so that it
        // shuts down nicely when the VM exits (even if you "Ctrl-C" the
        // running example before it's completed)
        Runtime.getRuntime().addShutdownHook(new Thread() {
            @Override
            public void run() {
                graphDb.shutdown();
            }
        });
    }

    public static boolean fieldMatch(Annotation anno, AnnotationTypes type) {
        return (anno.toString().startsWith(type.toString()));
    }


    /**
     * Save {@link#BioEntity} in Graph
     *
     * @param <T>
     * @param subGraph {@link#Subgraph}
     * @throws IllegalAccessException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws UnknownHostException
     * @throws HttpException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> void saveSubgraph(Subgraph subGraph) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException, IllegalArgumentException, NoSuchFieldException {
        //log.info("saveSubGraph()");
        if (subGraph != null) {
            Map<BioTypes, List> beMap = subGraph.getBeMap();
            if (!beMap.isEmpty()) {
                for (List list : beMap.values()) {
                    for (Object obj : list.toArray()) {
                        if (obj != null) {
                            //log.info("Saving BioEntity " + (++cntr) + "," + obj.toString());
                            persistGraph((T) obj);
                        }
                    }
                }
                // save Relations
                for (List list : beMap.values()) {
                    for (Object obj : list.toArray()) {
                        if (obj != null) {
                            //saveRelations((T)obj);
                            log.info("saveRelations for obj = " + obj.getClass().getSimpleName());
                            saveRelations(obj);
                            if (!StatusUtil.idExists(obj)) {
                                StatusUtil.idInsert(obj);
                            }
                            //getBioEntity((T)obj);
                        }
                    }
                }
            }
        }
    }

    /**
     * saveRelation
     *
     * @param direction
     * @param element
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     * @throws URISyntaxException
     */
    private static void saveRelation(Direction direction, Object element) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, URISyntaxException, NoSuchFieldException {
        //log.info("saveRelation()," + direction.toString());

        Class elementClass = element.getClass();
        log.info("saveRelation elementClass.getName), " + elementClass.getSimpleName());

        /*
        if (elementClass.getAnnotation(AnnotationClasses.RELATIONSHIP_ENTITY.getAnnotationClass()) == null) {
            throw new RuntimeException("Element class " + elementClass.getName() + "must be an @RelationshipEntity");
        } */
        Field[] elementFields = elementClass.getDeclaredFields(); // fetch fields in @RelationshipEntity annotated object
        Object startEntity = null, endEntity = null;
        String type = null;
        String propertyVal, propertyName;
        HashMap<String, String> propMap = new HashMap<String, String>();
        for (Field elementField : elementFields) {
            elementField.setAccessible(true); // help accessing private variables
            Annotation[] elementAnnotations = elementField.getAnnotations(); // get annotations like @StartNode, @EndNode, @RelationshipEntity
            for (Annotation elementAnno : elementAnnotations) {
                if (fieldMatch(elementAnno, AnnotationTypes.START_NODE)) {
                    startEntity = elementField.get(element);  // Potentially a node
                    log.info("startEntity = " + startEntity.getClass().getSimpleName());
                } else if (fieldMatch(elementAnno, AnnotationTypes.END_NODE)) {
                    endEntity = elementField.get(element);  // potentially a node
                    if (endEntity != null)
                        log.info("endEntity = " + endEntity.getClass().getSimpleName());
                    else
                        log.warn("endEntity is null!");
                } else if (fieldMatch(elementAnno, AnnotationTypes.RELATIONSHIP_ENTITY)) {
                    //type = ((RelationshipEntity)elementAnno).type();
                    //direction = ((RelationshipEntity)elementAnno).direction();
                } else if (fieldMatch(elementAnno, AnnotationTypes.REL_TYPE)) {
                    type = ((BioRelTypes) elementField.get(element)).toString();
                    log.info("type = " + type);
                } else if (fieldMatch(elementAnno, AnnotationTypes.REL_PROPERTY)) {
                    Object relElement = elementField.get(element);
                    if (relElement != null) {
                        if (!relElement.getClass().equals(String.class)) {
                            throw new RuntimeException("@RelProperty only supports java.lang.String");
                        } else {
                            //log.info("setting property values =" + elementField.getName());
                            propertyVal = (String) elementField.get(element);
                            propertyName = (String) elementField.getName();
                            propMap.put(propertyName, propertyVal);
                        }
                    }
                }
            }
        }
        /* if (type != null) {
            log.info("type =" + type);
        }
        if (startEntity != null) {
            log.info("startEntity=" + startEntity.toString());
        }
        if (endEntity != null) {
            log.info("endEntity =" + endEntity.toString());
        } */
        log.info("direction = " + direction);
        createRelationship(startEntity, endEntity, type, direction, propMap);
    }

    /**
     * Save all the relations for a node. Assume that the startNode and all the
     * endNodes exist. Also assume that the startNode in the @RelatedToVia collections
     * is the same as T, param passed to this method, which is the @BioEntity.
     * <p>
     * So before calling this method, make sure all the nodes are created.
     *
     * @param <T>
     * @param t   this is a @BioEntity
     * @throws java.lang.IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.io.UnsupportedEncodingException
     */
    public static <T> void saveRelations(T t) throws IllegalArgumentException, IllegalAccessException, UnsupportedEncodingException, URISyntaxException {
        //log.info("saveRelations(), " + t.toString());

        try {
            Class tClass = t.getClass();
            Field[] fields = t.getClass().getDeclaredFields();
            for (Field field : fields) {
                field.setAccessible(true);
                log.info("field name = " + field.getName());
                Annotation[] annotations = field.getAnnotations();
                for (Annotation annotation : annotations) {
                    if (fieldMatch(annotation, AnnotationTypes.RELATED_TO_VIA)) {
                        HashSet relations = (HashSet) field.get(t);
                        if (relations != null && !relations.isEmpty()) {
                            Direction direction = ((RelatedToVia) annotation).direction();
                            for (Object element : relations) { // element is a @RelationshipEntity annotated object
                                //log.info("RelatedToVia = " + element.toString());
                                //log.info("relation class = " + element.getClass().getSimpleName());
                                saveRelation(direction, element);
                            }
                        }
                    } else if (fieldMatch(annotation, AnnotationTypes.RELATED_TO)) {
                        //log.info("field.get(t)" + field.get(t).toString());
                        Direction direction = ((RelatedTo) annotation).direction();
                        //log.info("RELATED_TO()," + field.getName() + ", direction=" + direction.toString());
                        if (field.get(t) != null) {
                            //log.info("field.get(t) =" + field.get(t));
                            saveRelation(direction, field.get(t));
                        }
                    }
                }
            }
        } catch (SecurityException e) {
            throw new RuntimeException("Something went wrong while saving relations." + e.getMessage(), e);
        } catch (IllegalArgumentException e) {
            throw new RuntimeException("Something went wrong while saving relations." + e.getMessage(), e);
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Something went wrong while saving relations." + e.getMessage(), e);
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException("Something went wrong while saving relations." + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Something went wrong while saving relations." + e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Something went wrong while saving relations." + e.getMessage(), e);
        }
    }


    /**
     * Fetch BioEntity and not the relations.
     * This call uses any type of {@link IndexNames} to retrieve BioEntity
     * IndexNames are retrieved within the method
     *
     * @param <T>
     * @param bioType {@link BioTypes}
     * @param value   {@link BioTypes} value
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> T getBioEntity(BioTypes bioType, String value) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException, NoSuchFieldException {
        //log.info("getBioEntity(bioType, value");

        T t = (T) new ClassInstantiator().createInstance(bioType.toString());
        Class tClass = t.getClass();

        Annotation bioEntityAnno = tClass.getAnnotation(BioEntity.class);
        if (bioEntityAnno == null) {
            throw new RuntimeException("BioEntityAnnotation is null, for " + value);
        }

        Node node;

        CompoundKey compoundKey = CompoundKey.getCompoundKey(t);

        if (compoundKey != null) {
            node = getUniquelyIndexedNode(compoundKey.getKey(), compoundKey.getValue(), compoundKey.getIndexName());
            if (node != null) {
                //log.info("*** node exists ***, " + field.getName() + "=" + value + " nodeId " + new Long(node.getId()).toString());
                return GraphMapper.getBioEntity(node);
            } else {
                return null;
            }
        } else {
            Field[] fields = tClass.getDeclaredFields();

            // Try to find a uniquely indexed field, so we can fetch the node
            for (Field field : fields) {
                field.setAccessible(true);
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    if (fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED)) {
                        IndexNames indexName = ((UniquelyIndexed) fieldAnnotation).indexName();
                        /**
                         * Retrieves Node if it exists
                         */
                        node = getUniquelyIndexedNode(field.getName(), value, indexName);
                        if (node != null) {
                            //log.info("*** node exists ***, " + field.getName() + "=" + value + " nodeId " + new Long(node.getId()).toString());
                            return GraphMapper.getBioEntity(node);
                        } else {
                            return null;
                        }
                    }
                }
            }
        }
        //log.info("BioEntity " + value + " does not exist");
        return null;
    }

    /**
     * Fetch BioEntity and not the relations.
     * This call uses any type of {@link IndexNames} to retrieve BioEntity
     * IndexNames are retrieved within the method
     *
     * @param <T>
     * @param bioType  {@link BioTypes}
     * @param bioField {@link BioFields}
     * @param value    {@link BioTypes} value
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws java.lang.ClassNotFoundException
     * @throws java.lang.InstantiationException
     * @throws java.net.URISyntaxException
     */
    public static <T> T getBioEntity(BioTypes bioType, BioFields bioField, String value) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, URISyntaxException {
        //log.info("getBioEntity()," + bioType.toString() + "," + bioField.toString() + "=" + value);
        T t = (T) new ClassInstantiator().createInstance(bioType.toString());
        Class tClass = t.getClass();

        Annotation bioEntityAnno = tClass.getAnnotation(BioEntity.class);
        if (bioEntityAnno == null) {
            throw new RuntimeException("BioEntityAnnotation is null, for " + value);
        }

        Field[] fields = tClass.getDeclaredFields();

        // Try to find a uniquely indexed field, so we can fetch the node
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation fieldAnnotation : fieldAnnotations) {
                if (field.getName().equals(bioField.toString())) {
                    if (fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED)) {
                        IndexNames indexName = ((UniquelyIndexed) fieldAnnotation).indexName();
                        return getBioEntityFromIndex(bioField, value, indexName);
                    }
                    if (fieldMatch(fieldAnnotation, AnnotationTypes.INDEXED)) {
                        IndexNames indexName = ((Indexed) fieldAnnotation).indexName();
                        return getBioEntityFromIndex(bioField, value, indexName);
                    }
                }
            }
        }
        //log.info("Could not find BioEntity, " +  bioType.toString() + "," + bioField.toString() + "=" + value);
        return null;
    }


    /**
     * Checks if a property exists in a template
     *
     * @param <T> {@link BioEntity}
     * @param t
     * @param key {@link String}
     * @param val
     * @return
     * @throws java.lang.IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> boolean checkIfPropertyExists(T t, String key, Object val) throws IllegalAccessException, URISyntaxException, IllegalArgumentException, NoSuchFieldException {
        Node node = getNode(t);
        Object obj;
        if ((obj = node.getProperty(key, null)) == null) {
            return false;
        } else {
            return obj.equals(val);
        }
    }

    /**
     * This method is vendor specific. It gets the node from the graph db.
     *
     * @param propName
     * @param propVal
     * @param indexName
     * @return
     * @throws java.net.URISyntaxException
     */
    public static Node getNodeFromGraphDb(String propName, String propVal, IndexNames indexName) throws URISyntaxException {
        //log.info("getNodeFromGraphDb()," + propName + "=" + propVal + ", IndexName=" + indexName.toString());
        RestIndex<Node> nodeIndex;
        if (indexName == null) {
            throw new RuntimeException("The indexName is null for property " + propName + " and value " + propVal);
        }
        if (graphDb == null) {
            setup();
        }

        String indexStr = indexName.toString();
        IndexHits<Node> pNodeHits = null;
        nodeIndex = graphDb.index().forNodes(indexStr);

        if (propVal != null) {
            //log.info("propVal =" + propVal);
            pNodeHits = nodeIndex.get(propName, propVal);
        }

        Node node = null;
        if (pNodeHits != null && pNodeHits.size() > 0) { // if  node already exists
            if (pNodeHits.size() > 1) {
                throw new RuntimeException("Node for index " + indexName.toString() + " with property name " + propName + ", and value " + propVal + " has non-unique value");
            }
            node = pNodeHits.getSingle();
            //log.info("Node exists, propName = " + propName + " Value=  " + propVal + " indexName=" + indexName.toString());
        } else {
            //log.info("getNodeFromGraphDb(), node does not exist, " + propName + "=" + propVal + indexName.toString());
        }
        if (pNodeHits != null) {
            pNodeHits.close();
        }
        return node;
    }

    /**
     * Go through the nodeEntity and fetch the @UniquelyIndexed or @Indexed annotation
     * so that we can fetch the node.
     *
     * @param nodeEntity
     * @return
     * @throws IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public static Node getNode(Object nodeEntity) throws IllegalAccessException, URISyntaxException, IllegalArgumentException, NoSuchFieldException {
        //log.info("getNode() " + nodeEntity.toString());

        Node node = null;

        CompoundKey compoundKey = CompoundKey.getCompoundKey(nodeEntity);
        if (compoundKey != null) {
            node = getNodeFromGraphDb(compoundKey.getKey(), compoundKey.getValue(), compoundKey.getIndexName());
            return node;
        }

        // Get the startNode
        Field[] fields = nodeEntity.getClass().getDeclaredFields();

        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation fieldAnnotation : fieldAnnotations) {
                IndexNames indexName;
                if (fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED)) {
                    indexName = ((UniquelyIndexed) fieldAnnotation).indexName();
                    if (field.get(nodeEntity) == null) {
                        //log.info("indexName=" + indexName.toString());
                        //log.info(field.getName());
                    } else {
                        node = getNodeFromGraphDb(field.getName(), field.get(nodeEntity).toString(), indexName);
                    }
                }
            }
        }
        return node;
    }

    /**
     * Create a relationship between a startNode and an endNode.
     *
     * @param startEntity
     * @param endEntity
     * @param type
     * @param direction
     * @param propMap
     * @throws IllegalAccessException
     * @throws UnsupportedEncodingException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public static void createRelationship(Object startEntity, Object endEntity, String type, Direction direction, HashMap<String, String> propMap) throws IllegalAccessException, UnsupportedEncodingException, URISyntaxException, IllegalArgumentException, NoSuchFieldException {
        //log.info("createRelationship(), startEntity," + startEntity.toString());
        //log.info("createRelationship() endEntity," + endEntity.toString());
        //log.info("createRelationship(), type=" + type + ",Direction=" + direction.toString());

        //log.info("createRelationship(), startEntity," + startEntity.toString());
        //log.info("createRelationship() endEntity," + endEntity.toString());
        //log.info("createRelationship(), type=" + type + ",Direction=" + direction.toString());

        if (startEntity == null || endEntity == null || type == null || direction == null) {
            if (startEntity == null)
                log.error("startEntity is null, propMap = " + propMap.toString());
            else if (endEntity == null)
                log.error("endEntity is null, startEntity = " + startEntity.toString() + ", propMap = " + propMap.toString());
            else if (type == null)
                log.error("type is null, startEntity = " + startEntity.toString() + ", endEntity = " + endEntity.toString() + ", propMap = " + propMap.toString());
            else if (direction == null)
                log.error("direction is null, startEntity = " + startEntity.toString() + ", endEntity = " + endEntity.toString() + ", type = " + type + ", propMap = " + propMap.toString());
            return;

        }
        log.info("Good news! startEntity, endEntity, type and direction are not null");

        Node startNode = getNode(startEntity);
        Node endNode = getNode(endEntity);
        if (startNode != null && endNode != null) {
            Iterable<Relationship> relations = startNode.getRelationships();
            //log.info("relations.size =" + relations.toString());
            /*
            for (Relationship relation : relations) {
                log.info("relation =" + relation.toString());
            } */
            Relationship relation = NeoUtil.checkCreateRel(startNode, endNode, type, direction);
            if (relation != null) {
                for (String propName : propMap.keySet()) {
                    relation.setProperty(propName, propMap.get(propName));
                }
            }

        }
    }

    /*
    public static <T> CompoundKey getCompoundKey(T t) throws IllegalArgumentException, IllegalAccessException, URISyntaxException {
        String field1 = null;
        String field2 = null;
        String field3 = null;
        IndexNames indexName = null;

        Annotation[] classAnnotations = t.getClass().getAnnotations();
        for (Annotation annotation : classAnnotations) {
            if (fieldMatch(annotation, AnnotationTypes.UNIQUE_COMPOUND_INDEX)) {
                indexName = ((UniqueCompoundIndex)annotation).indexName();
                field1 = ((UniqueCompoundIndex)annotation).field1();
                field2 = ((UniqueCompoundIndex)annotation).field2();
                field3 = ((UniqueCompoundIndex)annotation).field3();
            }
        }
        Field[] fields = t.getClass().getDeclaredFields();
        StringBuilder sb = new StringBuilder();
        String val1 = null;
        String val2 = null;
        String val3 = null;
        for (Field f : fields) {
            f.setAccessible(true);
            if (f.getName().equals(field1)) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    val1 = (String)f.get(t);
                }
            }
            if (f.getName().equals(field2)) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    val2 = (String)f.get(t);
                }
            }
            if (f.getName().equals(field3)) {
                Annotation anno = f.getAnnotation(PartKey.class);
                if (anno != null) {
                    val3 = (String)f.get(t);
                }
            }
        }
        return new CompoundKey(indexName, field1, field2, field3, val1, val2, val3);
    } */

    /**
     * Fetch just the BioEntity properties from a node, and not the relations.
     * getBioEntity
     *
     * @param <T>
     * @param t
     * @return
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> T getBioEntity(T t) throws IllegalArgumentException, IllegalAccessException, URISyntaxException, NoSuchFieldException {
        Class tClass = t.getClass();

        Annotation bioEntityAnno = tClass.getAnnotation(BioEntity.class);
        if (bioEntityAnno == null) {
            throw new RuntimeException("Only saving @BioEntity objects are supported.");
        }

        boolean foundCompoundKey = false;
        CompoundKey compoundKey = CompoundKey.getCompoundKey(t);
        if (compoundKey != null) {
            foundCompoundKey = true;
        }

        Field[] fields = tClass.getDeclaredFields();
        Node node = null;
        boolean exitOuterLoop = false;
        String uniqProp = null;

        if (!foundCompoundKey) {
            /**
             * Try to find a uniquely indexed field, so we can fetch the node
             */
            for (Field field : fields) {
                field.setAccessible(true);
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    if (fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED)) {
                        IndexNames indexName = ((UniquelyIndexed) fieldAnnotation).indexName();
                        node = getUniquelyIndexedNode(field.getName(), field.get(t).toString(), indexName);
                        exitOuterLoop = true;
                        uniqProp = field.getName();
                        break;
                    }
                }
                if (exitOuterLoop) {
                    break;
                }
            }
        } else {
            if (compoundKey != null) {
                node = getUniquelyIndexedNode(compoundKey.getKey(), compoundKey.getValue(), compoundKey.getIndexName());
            }
        }

        /**
         * UniqueIndex already exists in T, so skip it
         * Match the Node property in T in private field.
         */

        if (node != null) {
            for (String propKey : node.getPropertyKeys()) {
                if (propKey.equals(uniqProp)) {
                    continue;
                }
                for (Field field : fields) {
                    field.setAccessible(true);
                    if (field.getName().equals(propKey)) {
                        //log.info("propKey = " + propKey + ", propValue = " + node.getProperty(propKey));
                        field.set(t, node.getProperty(propKey));
                    }
                }
            }
        }
        /* if (node != null) {
            log.info("node =" + node.toString());
        } else {
            log.info("node is null");
        } */
        return t;
    }


    /**
     * Fetches BioEntity from index
     * Use BioField values for fetching entities and nodes
     *
     * @param propName
     * @param propValue
     * @param indexName
     * @return BioEntity
     * @throws ClassNotFoundException
     * @throws IllegalAccessException
     * @throws InstantiationException
     * @throws java.net.URISyntaxException
     */
    public static <T> T getBioEntityFromIndex(BioFields propName, String propValue, IndexNames indexName) throws ClassNotFoundException, IllegalAccessException, InstantiationException, URISyntaxException {
        Node node = getNodeFromGraphDb(propName.toString(), propValue, indexName);
        if (node != null) {
            return GraphMapper.getBioEntity(node);
        } else {
            return null;
        }
    }

    /**
     * Get an array of relations
     *
     * @param <T>
     * @param <R>
     * @param t
     * @param rClass
     * @return
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T, R> Collection<R> getRelations(T t, Class<R> rClass) throws InstantiationException, IllegalAccessException, URISyntaxException, IllegalArgumentException, NoSuchFieldException {
        Class tClass = t.getClass();
        Annotation bioEntityAnno = tClass.getAnnotation(BioEntity.class);
        if (bioEntityAnno == null) {
            throw new RuntimeException("Only @BioEntity objects are supported.");
        }

        boolean foundCompoundKey = false;
        CompoundKey compoundKey = CompoundKey.getCompoundKey(t);
        if (compoundKey != null) {
            foundCompoundKey = true;
        }

        Field[] fields = tClass.getDeclaredFields();
        Node node = null;
        boolean exitOuterLoop = false;

        if (!foundCompoundKey) {
            // Try to find a uniquely indexed field, so we can fetch the node
            for (Field field : fields) {
                field.setAccessible(true);
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    if (fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED)) {
                        IndexNames indexName = ((UniquelyIndexed) fieldAnnotation).indexName();
                        /**
                         * Retrieve Node if it exists
                         */
                        node = getUniquelyIndexedNode(field.getName(), field.get(t).toString(), indexName);
                        exitOuterLoop = true;
                        break;
                    }
                }
                if (exitOuterLoop) {
                    break;
                }
            }
        } else {
            if (compoundKey != null) {
                node = getUniquelyIndexedNode(compoundKey.getKey(), compoundKey.getValue(), compoundKey.getIndexName());
            }
        }

        Collection<R> relations = new HashSet();
        if (node != null) {

            for (Relationship rel : node.getRelationships()) {
                RelationshipType relationshipType = rel.getType();
                String type = relationshipType.name();
                for (String propKey : rel.getPropertyKeys()) {
                    R relation = (R) rClass.newInstance();
                    for (Field field : relation.getClass().getDeclaredFields()) {
                        if (field.getName().equals(propKey)) {
                            field.setAccessible(true);
                            //log.info("relation propKey = " + propKey + ", relation propValue = " + rel.getProperty(propKey));
                            field.set(relation, rel.getProperty(propKey));
                        } else if (BioFields.REL_TYPE.equals(propKey)) {
                            field.set(relation, type);
                        }
                    }
                    relations.add(relation);
                }
            }
        }
        return relations;
    }


    private static MongoCollection getCollection(String bioType) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        MongoCollection bioEntityCollection = mongoUtil.getCollection(bioType);
        return bioEntityCollection;
    }

    private static void createIndex(MongoCollection coll, String field, String uniqueKey) {
        // null implies use default name, and true implies unique index
        boolean uniqueIndex = field.equals(uniqueKey);
        coll.getCollection().ensureIndex(new BasicDBObject(field, new Integer(Config.ASCENDING_INDEX.toString())), null, uniqueIndex);

    }

    public static BasicDBObject getMongoQuery(String field, String value) {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(field, value);
        return queryMap;
    }

    /**
     * Persist this  entity to Mongo. The bioType is the type of collection
     * to be updated. Every bio entity has one uniquely indexed field.
     * The index needs to be created only on the indexed fields.
     * Mongo does not support full-text index.
     * Insert the record if it does not exist. Update it if it does exist.
     *
     * @param indexedFields
     * @param entity
     * @param bioType
     * @param uniqueKey
     * @throws UnknownHostException
     */
    private static void saveMongo(List<String> indexedFields, BasicDBObject entity, String bioType, String uniqueKey) throws UnknownHostException {
        MongoCollection coll = getCollection(bioType);
        for (String key : indexedFields) {
            createIndex(coll, key, uniqueKey);  // creates only if not exists
        }
        BasicDBObject mongoQuery = getMongoQuery(uniqueKey, entity.getString(uniqueKey));
        BasicDBList result = coll.findDB(mongoQuery);
        if ((result == null) || (result.isEmpty())) {
            coll.insert(entity);
        } else {
            coll.findAndModify(result, entity);
        }
    }

    /**
     * Persist a bio entity generic using reflection and annotation.
     * This method is not being used.
     *
     * @param <T>
     * @param t
     * @return
     * @throws IllegalAccessException
     * @throws java.net.URISyntaxException
     */
    public static <T> BasicDBObject persist(T t) throws IllegalAccessException, URISyntaxException {
        Class tClass = t.getClass();
        String nodeType = null;
        String uniqueKey = null;
        BasicDBObject basicDBObject = new BasicDBObject();
        List<String> indexedFields = new ArrayList<String>();
        Annotation bioEntityAnno = tClass.getAnnotation(BioEntity.class);
        if (bioEntityAnno == null) {
            throw new RuntimeException("Only saving @BioEntity objects to Mongo are supported.");
        }

        if (graphDb == null) {
            setup();
        }
        Transaction tx = graphDb.beginTx();
        try {
            Field[] fields = tClass.getDeclaredFields();

            // now process all other annotations other than @UniquelyIndexed
            for (Field field : fields) {
                field.setAccessible(true);
                if (field.get(t) == null) {
                    continue;
                }
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    //log.info("annotation = " + fieldAnnotation.toString());
                    //log.info("potential node property = " + field.getName() + ", potential node property value = " + field.get(t));
                    if (fieldMatch(fieldAnnotation, AnnotationTypes.GRAPH_ID) ||
                            fieldMatch(fieldAnnotation, AnnotationTypes.INDEXED) ||
                            fieldMatch(fieldAnnotation, AnnotationTypes.FULLTEXT_INDEXED) ||
                            fieldMatch(fieldAnnotation, AnnotationTypes.NON_INDEXED) ||
                            fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED) ||
                            fieldMatch(fieldAnnotation, AnnotationTypes.PARTKEY)) {
                        String key = field.getName();
                        String value = field.get(t).toString();
                        //log.info("key = " + key);
                        if (ModelFields.NODE_TYPE.equals(key)) {
                            nodeType = value;
                        }
                        if (fieldMatch(fieldAnnotation, AnnotationTypes.INDEXED) ||
                                fieldMatch(fieldAnnotation, AnnotationTypes.FULLTEXT_INDEXED) ||
                                fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED)) {
                            indexedFields.add(field.getName());
                        }
                        if (fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED)) {
                            uniqueKey = field.getName();
                        }
                        basicDBObject.put(field.getName(), field.get(t).toString());
                    } else {
                        // do nothing for fields that are not properties
                    }
                }
            }
            tx.success();
            saveMongo(indexedFields, basicDBObject, nodeType, uniqueKey);
        } catch (IllegalAccessException e) {
            tx.failure(); //rollback
            throw new RuntimeException("Something went wrong while saving bioentity.", e);
        } catch (IllegalArgumentException e) {
            tx.failure();
            throw new RuntimeException("Something went wrong while saving bioentity.", e);
        } catch (SecurityException e) {
            tx.failure();
            throw new RuntimeException("Something went wrong while saving bioentity.", e);
        } catch (UnknownHostException e) {
            tx.failure();
            throw new RuntimeException("Something went wrong while saving bioentity.", e);
        } finally {
            //tx.finish();
            tx.close();
        }
        return basicDBObject;
    }


    /**
     * There must be at least 1 UniquelyIndexed field in a BioEntity.
     * This does not save the relations. To save relations, call
     * <code>saveRelations(t)</code>.
     *
     * @param <T>
     * @param t    {@link#BioEntity}
     * @param node
     * @throws IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.net.MalformedURLException
     * @throws java.lang.NoSuchFieldException
     * @throws org.apache.http.HttpException
     * @throws java.net.UnknownHostException
     * @throws org.apache.lucene.index.CorruptIndexException
     */

    public static <T> void saveProperties(T t, Node node) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException, CorruptIndexException, IllegalArgumentException, NoSuchFieldException {
        Class tClass = t.getClass();
        if (node != null) {
            try {
                Field[] fields = tClass.getDeclaredFields();
                // now process all other annotations other than @UniquelyIndexed
                for (Field field : fields) {
                    field.setAccessible(true);
                    Annotation[] fieldAnnotations = field.getAnnotations();
                    for (Annotation fieldAnnotation : fieldAnnotations) {
                        if (fieldMatch(fieldAnnotation, AnnotationTypes.GRAPH_ID)) {
                            continue; // we skip GraphId as it's an internal field
                        }
                        if (field.get(t) == null) { // || field.get(t).toString() == null) {
                            continue;
                        } else {
                            /* UniquelyIndexed is saved as property when a node is created */
                            if (fieldMatch(fieldAnnotation, AnnotationTypes.INDEXED)) {
                                IndexNames indexName = ((Indexed) fieldAnnotation).indexName();
                                /**
                                 * save indexed fields as properties. These index fields are non-unique
                                 */
                                saveNodeProperty(node, field.getName(), field.get(t).toString(), true, false, indexName.toString());
                            } else if (fieldMatch(fieldAnnotation, AnnotationTypes.FULLTEXT_INDEXED)) {
                                //log.info("fieldAnnotation =" + fieldAnnotation.toString());
                                //log.info("annotationType =" + AnnotationTypes.FULLTEXT_INDEXED.toString());
                                IndexNames indexName = ((FullTextIndexed) fieldAnnotation).indexName();
                                processFullTextIndex(indexName, node.getId(), field.getName(), field.get(t).toString());
                            } else if (fieldMatch(fieldAnnotation, AnnotationTypes.NON_INDEXED)) {
                                /**
                                 * save fields which non indexed fields & not unique as properties
                                 */
                                saveNodeProperty(node, field.getName(), field.get(t).toString(), false, false, null);
                            }
                        }
                    }
                }
            } catch (SecurityException e) {
                throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
            } catch (IllegalArgumentException e) {
                throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
            } catch (URISyntaxException e) {
                throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
            } catch (IOException e) {
                throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
            } catch (HttpException e) {
                throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
            }
        }
        LuceneTemplate.addDocument(t); // add this doc to lucene
    }

    /**
     * t); // add this doc to lucene
     * }
     * <p>
     * /**
     * persistGraph
     *
     * @param <T>
     * @param t
     * @throws IllegalAccessException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws UnknownHostException
     * @throws HttpException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> void persistGraph(T t) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException, IllegalArgumentException, NoSuchFieldException {
        //log.info("persistGraph()," + t.toString());

        Node node = getNode(t);
        Annotation a = t.getClass().getAnnotation(BioEntity.class);
        String bioType = ((BioEntity) a).bioType().toString();
        if (node == null) {
            log.info("persistGraph(), node does not exist, add() " + (++cntr) + ", bioType = " + bioType);
            save(t);
        } else {

            log.info("persistGraph(), node exists" + t.toString());

            log.info("persistGraph(), node exists, bioType = " + bioType);

            saveProperties(t, node);
        }
    }

    /**
     * There must be at least 1 UniquelyIndexed field in a BioEntity.
     * This does not save the relations. To save relations, call
     * <code>saveRelations(t)</code>.
     *
     * @param <T>
     * @param t   {@link#BioEntity}
     * @throws IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.io.UnsupportedEncodingException
     * @throws java.net.MalformedURLException
     * @throws org.apache.lucene.index.CorruptIndexException
     * @throws java.net.UnknownHostException
     * @throws org.apache.http.HttpException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> void save(T t) throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException, CorruptIndexException, IllegalArgumentException, NoSuchFieldException {
        //log.info("save()" + t.toString());

        Class tClass = t.getClass();
        //log.info("save()" + t.toString());

        try {
            //Annotation[] classAnnotations = tClass.getAnnotations();

            Node node = null;
            String uniqueIndexId = null;
            BioTypes bioType = TemplateUtils.extractBioType(t); // moved outside for loop
            CompoundKey compoundKey = CompoundKey.getCompoundKey(t);
            if (compoundKey != null) {
                node = processUniquelyIndexedNode(compoundKey.getKey(), compoundKey.getValue(), compoundKey.getIndexName().toString());
                uniqueIndexId = compoundKey.getValue();
                if (null != bioType && !bioType.toString().isEmpty()) {
                    node.addLabel(DynamicLabel.label(bioType.toString()));
                }
            } // no else block, as we want to process @Visual annotations in the for
            // loop below even for compoundKey scenario

            Field[] fields = tClass.getDeclaredFields();
            String message = null;
            for (Field field : fields) {
                field.setAccessible(true);
                //log.info("field = " + field.getName());
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    if (compoundKey == null && fieldMatch(fieldAnnotation, AnnotationTypes.UNIQUELY_INDEXED)) {
                        IndexNames indexName = ((UniquelyIndexed) fieldAnnotation).indexName();
                        //log.info("Found unique index " + indexName + ", field name = " + field.getName());
                        /**
                         * Save the uniqueIndexId for future @NodeLabel entry.
                         * create Node if it does not exist
                         * Fetch Node if it exists.
                         */
                        if (field.get(t) != null) {
                            uniqueIndexId = field.get(t).toString();
                            node = processUniquelyIndexedNode(field.getName(), uniqueIndexId, indexName.toString());

                            /**
                             * Save IndexName which is Uniquely indexed as a Node property
                             */
                            if (node != null) {
                                saveNodeProperty(node, field.getName(), uniqueIndexId, true, true, indexName.toString());
                                if (null != bioType && !bioType.toString().isEmpty()) {
                                    node.addLabel(DynamicLabel.label(bioType.toString()));
                                }
                                break;
                            }
                        }
                    } else {
                        /*
                         * If the field is @Visual then we know that must be some shortLabel
                         * or shortName type of field that needs to be appended to the node label.
                         * The node label prefix is constructed using the @NodeLabel field
                         * that is processed below.
                         */
                        if (compoundKey == null && field.get(t) != null) {
                            if (fieldMatch(fieldAnnotation, AnnotationTypes.VISUAL)) {
                                message = field.get(t).toString();
                            }
                        }
                    }
                }
            }

            if (node == null) { // we did not find 1 uniquely indexed node
                //log.info("node is null");
                log.error("Every @BioEntity domain must have at least 1 @UniquelyIndexed field. t = " + t.toString());
                //throw new RuntimeException("Every @BioEntity domain must have at least 1 @UniquelyIndexed field.");
            }

            // First, initialize the message field
            boolean exitloop = false;
            for (Field field : fields) {
                field.setAccessible(true);
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    /*
                     * If we have a @NodeLabel annotation declared, then the current
                     * field must be an @NodeLabel, and we construct the message value.
                     * This gets displayed as the label in Neo4J graph node.
                     */
                    if (fieldMatch(fieldAnnotation, AnnotationTypes.NODE_LABEL)) {
                        //BioTypes bioType = TemplateUtils.extractBioType(t);
                        if ((message != null) && (message.length() > 0)) {
                            message = bioType + "-" + uniqueIndexId + "-" + message;
                        } else {
                            message = bioType + "-" + uniqueIndexId;
                        }
                        field.set(t, message);
                        exitloop = true;
                        break;
                    }
                }
                if (exitloop) {
                    break;
                }
            }

            // now process all other annotations other than @UniquelyIndexed
            for (Field field : fields) {
                field.setAccessible(true);
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    if (fieldMatch(fieldAnnotation, AnnotationTypes.GRAPH_ID)) {
                        continue; // we skip GraphId as it's an internal field
                    }

                    if (field.get(t) == null) { // || field.get(t).toString() == null) {
                        continue;
                    } else {
                        if (fieldMatch(fieldAnnotation, AnnotationTypes.INDEXED)) {
                            IndexNames indexName = ((Indexed) fieldAnnotation).indexName();
                            /**
                             * save indexed fields as properties. These index fields are non-unique
                             */
                            saveNodeProperty(node, field.getName(), field.get(t).toString(), true, false, indexName.toString());
                        } else if (fieldMatch(fieldAnnotation, AnnotationTypes.FULLTEXT_INDEXED)) {
                            IndexNames indexName = ((FullTextIndexed) fieldAnnotation).indexName();
                            if (node != null)
                                processFullTextIndex(indexName, node.getId(), field.getName(), field.get(t).toString());
                        } else if (fieldMatch(fieldAnnotation, AnnotationTypes.NON_INDEXED) ||
                                fieldMatch(fieldAnnotation, AnnotationTypes.PARTKEY)) {
                            // Include PARTKEY scenario as otherwise, we will not save node property for partkeys.
                            /**
                             * save fields which non indexed fields & not unique as properties
                             */
                            saveNodeProperty(node, field.getName(), field.get(t).toString(), false, false, null);
                        }
                    }
                }
            }
            log.info("Bioentity saved correctly");
        } catch (IllegalAccessException e) {
            throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
        } catch (URISyntaxException e) {
            throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
        } catch (RuntimeException e) {
            throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
        } catch (IOException e) {
            throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
        } catch (HttpException e) {
            throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
        }

        // save to lucene
        LuceneTemplate.addDocument(t);
    }

    /**
     * FullTextIndex is not in use because of Neo4J bug.
     * The full text index is supported only by the HTTP REST api. So
     * we don't use the Java driver here for Neo4J.
     *
     * @param indexName
     * @param nodeId
     * @param name
     * @param value
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws UnknownHostException
     * @throws HttpException
     */
    public static void processFullTextIndex(IndexNames indexName, Long nodeId, String name, String value) throws UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException {
        //log.info("processFulTextIndex(), indexName " + indexName.toString());

        //log.info("processFulTextIndex(), indexName " + indexName.toString() + " name=" + name + " value=" + value);
       /*
        if (!NeoRestAPI.indexExists(indexName.toString())) {
            //log.info("index Does not exist");
            String response = NeoRestAPI.createFullTextIndex(indexName.toString());
             //log.info("response for CreateFullTextIndex = " + response);
        }
        //Transaction tx = graphDb.beginTx();
        //try {
            //log.info("addNodeWithPropertyToIndex");
            String response = NeoRestAPI.addNodeWithPropertyToIndex(indexName.toString(), value, nodeId, name);
            //log.info("response after creating =" + response);
           // tx.success();
        //} catch (Exception e) {
          //  throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
       // } finally {
         //   tx.finish();
        //}
        *
        */

    }

    /**
     * Create Index, Fetch/Create the node just once based on unique index.
     *
     * @param key
     * @param value
     * @param indexName
     * @return
     * @throws java.net.URISyntaxException
     */
    public static Node processUniquelyIndexedNode(String key, String value, String indexName) throws URISyntaxException {
        //log.info("processUniquelyIndexedNode()," + key + "=" + value + ", indexName=" + indexName.toString());
        RestIndex<Node> nodeIndex = null;
        if (indexName == null) {
            throw new RuntimeException("The indexName is null for property, key " + key + " and value " + value);
        }
        if (graphDb == null) {
            setup();
        }

        /**
         * This block is in transaction as Node cannot be accessed until tx.success()
         */
        Transaction tx = graphDb.beginTx();
        IndexHits<Node> pNodeHits;
        Node node = null;
        boolean nodeCreated = false;
        try {
            nodeIndex = graphDb.index().forNodes(indexName);
            pNodeHits = nodeIndex.get(indexName, value);
            //log.info("pNodeHits = " + pNodeHits.size());

            if (pNodeHits.size() > 0) { // if  node already exists
                if (pNodeHits.size() > 1) {
                    throw new RuntimeException("Node for index " + indexName + " with property name(key) " + key + ", and value " + value + " has non-unique value");
                }
                node = pNodeHits.getSingle();
            } else { // if node does not exist
                pNodeHits.close();
                node = graphDb.createNode();
                nodeCreated = true;
                log.info("Created New Node: " + (++indexNodeCount));
            }
            tx.success();
        } catch (RuntimeException e) {
            tx.failure(); //rollback
            throw new RuntimeException("Something went wrong while saving bioentity. " + e.getMessage(), e);
        } finally {
            //tx.finish();
            tx.close();
        }
        if (node != null && nodeCreated && nodeIndex != null) {
            if (null == nodeIndex.putIfAbsent(node, key, value)) {
                log.info("New property added: "  + (++propertyCount));
            }
        }
        /* if (!nodeCreated) {
            log.info("Node exists " + key + "=" + value);
        }*/
        return node;
    }

    /**
     * How many properties were added.
     *
     * @return
     */
    public static long getPropertyCount() {
        return propertyCount;
    }

    /**
     * How many nodes were added.
     *
     * @return long
     */
    public static long getIndexNodeCount() {
        return indexNodeCount;
    }

    /**
     * Create Index, Fetch node just once based on unique index.
     * If Node does not exist, return null
     *
     * @param key
     * @param value
     * @param indexName
     * @return
     * @throws java.net.URISyntaxException
     */
    public static Node getUniquelyIndexedNode(String key, String value, IndexNames indexName) throws URISyntaxException {
        //log.info("getUniquelyIndexedNode(), " + key + "=" + value + "," + indexName.toString());
        RestIndex<Node> nodeIndex;
        if (indexName == null) {
            throw new RuntimeException("The indexName is null for property, key " + key + " and value " + value);
        }
        if (graphDb == null) {
            setup();
        }
        Node node = null;
        nodeIndex = graphDb.index().forNodes(indexName.toString());
        IndexHits<Node> pNodeHits = nodeIndex.get(key, value);
        if (pNodeHits.size() > 0) { // if  node already exists
            if (pNodeHits.size() > 1) {
                throw new RuntimeException("Node for index " + indexName + " with property name(key) " + key + ", and value " + value + " has non-unique value");
            }
            node = pNodeHits.getSingle();
        }
        pNodeHits.close();
        return node;
    }

    public static void saveNodeProperty(Node node, String name, String value, boolean indexed, boolean unique, String indexName) throws URISyntaxException {
        StringBuilder sb = new StringBuilder("saveNodeProperty(), " + name + "=" + value);
        if (indexName != null) {
            sb.append(",indexName = ").append(indexName);
        }
        log.info(sb.toString());

        // do not save null properties, it's a waste of time and money
        if ((value == null) || (value.trim().length() == 0)) {
            return;
        }
        /*
         * if node property does not exist, then add it
         */
        if (null != node && !node.hasProperty(name)) {
            node.setProperty(name, value);
            if (indexed) {
                if (indexName == null) {
                    throw new RuntimeException("The indexName is null for property " + name + " and value " + value);
                } else {
                    if (graphDb == null) {
                        setup();
                    }
                    RestIndex<Node> nodeIndex = graphDb.index().forNodes(indexName);
                    if (null == nodeIndex.putIfAbsent(node, name, value)) {   // if null, then no previous node exists
                        log.info("New property added: " + (++propertyCount));
                    }
                }
            }
        }
    }


    /**
     * Get an array of BioRelations
     *
     * @throws InstantiationException
     * @throws IllegalAccessException
     */
    /*
    public boolean checkIfPartMoleculeRelationExists(T t, T e) throws InstantiationException, IllegalAccessException {

         Node startProtein = getNode(t);
         Node endProtein = getNode(e);
         RedbasinTemplate rt = new RedbasinTemplate();

         Field[] fields = startProtein.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation fieldAnnotation : fieldAnnotations) {
                log.info("annotation = " + fieldAnnotation.toString());
                if (fieldAnnotation.toString().startsWith(PackagePath.META + "PartMoleculeRelation")) {
                    log.info("Field name = " + field.getName());
                    field.setAccessible(true);
                    log.info("Field value = " + field.get(startProtein));
                    log.info("Field value class = " + field.get(startProtein).getClass().getName());
                    HashSet<PartMoleculeRelation> partMoleculeRelations  = (HashSet<PartMoleculeRelation>)field.get(startProtein);
                    for (PartMoleculeRelation pi : partMoleculeRelations) {
                        log.info("startProtein = " + pi.getStartNode());
                        log.info("endProtein = " + pi.getEndNode());
                        log.info("message = " + pi.getMessage());
                    }
                    HashSet relations = (HashSet)field.get(startProtein);
                    for (Object element : relations) {
                        Class elementClass = element.getClass();
                        Field[] elementFields = elementClass.getDeclaredFields();
                        for (Field elementField : elementFields) {
                            elementField.setAccessible(true);
                            Annotation[] elementAnnotations = elementField.getAnnotations();
                            for (Annotation elementAnno : elementAnnotations) {
                                Object startBioEntity = null, endBioEntity = null;
                                if (rt.fieldMatch(elementAnno, AnnotationTypes.START_NODE)) {
                                    startBioEntity = elementField.get(element);
                                    log.info("bioEntity = " + startBioEntity.toString());
                                } else if (rt.fieldMatch(elementAnno, AnnotationTypes.END_NODE)) {
                                    endBioEntity = elementField.get(element);
                                    log.info("bioEntity = " + endBioEntity.toString());
                                }

                                if ((startBioEntity != null && endBioEntity != null) && (startBioEntity.equals(startProtein)) && (endBioEntity.equals(endProtein)) ) {
                                    return true;
                                }
                            }
                        }
                    }
                }
            }
        }
        return false;
    }
    */


    @Indexed(indexName = IndexNames.INTERACTOR_ID)
    public static String uniprotId = "P154876";

    //@RelatedToVia(elementClass = BioRelation.class)
    //Collection<ProteinInteraction> proteinInteractions;

    public static void testRelationships() throws IllegalAccessException {
        PersistenceTemplate rt = new PersistenceTemplate();
        //rt.proteinInteractions = new HashSet<ProteinInteraction>();
        Protein startProtein = new Protein("P156745", "protein", "P156745");
        Protein endProtein = new Protein("P156746", "protein", "P156746");
        startProtein.interactsWith(endProtein, "INTERACTS_WITH");
        //rt.proteinInteractions.add(new BioRelation(startProtein, endProtein));
        Field[] fields = startProtein.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation fieldAnnotation : fieldAnnotations) {
                //log.info("annotation = " + fieldAnnotation.toString());
                if (fieldAnnotation.toString().startsWith(PackagePath.META + "RelatedToVia")) {
                    //log.info("Field name = " + field.getName());
                    field.setAccessible(true);
                    //log.info("Field value = " + field.get(startProtein));
                    //log.info("Field value class = " + field.get(startProtein).getClass().getName());
                    HashSet<BioRelation> proteinInteractions = (HashSet<BioRelation>) field.get(startProtein);
                    for (BioRelation pi : proteinInteractions) {
                        //log.info("startProtein = " + pi.getStartNode());
                        //log.info("endProtein = " + pi.getEndNode());
                        //log.info("interaction name = " + pi.getName());
                    }
                    HashSet relations = (HashSet) field.get(startProtein);
                    for (Object element : relations) {
                        Class elementClass = element.getClass();
                        Field[] elementFields = elementClass.getDeclaredFields();
                        for (Field elementField : elementFields) {
                            elementField.setAccessible(true);
                            Annotation[] elementAnnotations = elementField.getAnnotations();
                            for (Annotation elementAnno : elementAnnotations) {
                                if (fieldMatch(elementAnno, AnnotationTypes.START_NODE)) {
                                    Object bioEntity = elementField.get(element);
                                    //log.info("bioEntity = " + bioEntity.toString());
                                } else if (fieldMatch(elementAnno, AnnotationTypes.END_NODE)) {
                                    Object bioEntity = elementField.get(element);
                                    //log.info("bioEntity = " + bioEntity.toString());
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    public static void testGenerics() throws IllegalAccessException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException, CorruptIndexException, IllegalArgumentException, NoSuchFieldException {
        //RedbasinTemplate<T> template = new RedbasinTemplate<T>();
        Protein protein = new Protein("P145345", "protein", "3533454");
        protein.setAliases("Some alias");
        protein.setIntactId("intactId342432");
        protein.setInteractorId("interactorId5345454");
        protein.setInteractorTypeFullName("interactorTypeFullName-UJGHJGJH");
        protein.setInteractorTypeShortLabel("interactorTypeShortLabel-HHJKJJL");
        protein.setInteractorTypeXref("xref-sdkfsdkfsdkf");
        protein.setMessage("message message message");
        protein.setMoleculeIdRef("moleculeIdRef-fdjf43443424");
        protein.setNcbiTaxId("ncbiTaxId-COW");
        protein.setNodeType(BioTypes.PROTEIN);
        PersistenceTemplate.save(protein);
        protein = getBioEntity(protein);
        log.info("protein = " + protein.toString());
    }

    public static void testPersistence() throws IllegalAccessException, URISyntaxException {
        //RedbasinTemplate<T> template = new RedbasinTemplate<T>();
        Protein protein = new Protein("P145346", "protein", "3533454");
        protein.setAliases("Some alias");
        protein.setIntactId("intactId342432");
        protein.setInteractorId("interactorId5345454");
        protein.setInteractorTypeFullName("interactorTypeFullName-UJGHJGJH");
        protein.setInteractorTypeShortLabel("interactorTypeShortLabel-HHJKJJL");
        protein.setInteractorTypeXref("xref-sdkfsdkfsdkf");
        protein.setMessage("message message message");
        protein.setMoleculeIdRef("moleculeIdRef-fdjf43443424");
        protein.setNcbiTaxId("ncbiTaxId-COW");
        protein.setNodeType(BioTypes.PROTEIN);
        //template.save(protein);
        BasicDBObject basicDBObject = PersistenceTemplate.persist(protein);
        //log.info("basicDBObject = " + basicDBObject.toString());
    }

    public static void main(String[] args) throws IllegalAccessException, URISyntaxException {
        setup();
        //setup("http://saibaba.local:7474/db/data");
        //testNodes();
        //testRelationships();
        //new RedbasinTemplate().testGenerics();
        //Protein protein = getBioEntity(protein);
        testPersistence();

    }

    public static void testNodes() throws IllegalAccessException {
        PersistenceTemplate rt = new PersistenceTemplate();
        Field[] fields = rt.getClass().getDeclaredFields();
        for (Field field : fields) {
            Annotation[] fieldAnnotations = field.getAnnotations();
            for (Annotation fieldAnnotation : fieldAnnotations) {
                //log.info("annotation = " + fieldAnnotation.toString());
                if (fieldAnnotation.toString().startsWith(PackagePath.META + "Indexed")) {
                    //log.info("Found the indexed class");
                    //boolean uniq = ((Indexed)fieldAnnotation).unique();
                    IndexNames indexName = ((Indexed) fieldAnnotation).indexName();
                    //log.info("indexName = " + indexName);
                    //log.info("potential node property = " + field.getName() + ", potential node property value = " + field.get(rt));
                }
            }
        }
    }
}
