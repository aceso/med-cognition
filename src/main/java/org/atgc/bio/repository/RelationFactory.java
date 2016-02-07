/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import java.util.Collection;
import java.util.HashSet;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This class is not being used. It is only for testing.
 *
 * @author jtanisha-ee
 * @param <E>
 */
public class RelationFactory<E> {

    protected static Logger log = LogManager.getLogger(RelationFactory.class);


    public static <E> RelationFactory<E> create(Class<E> c) {
        return new RelationFactory<E>(c);
    }

    Class<E> c;

    public RelationFactory(Class<E> c) {
        super();
        this.c = c;
    }

    public E createInstance()
                throws InstantiationException,
                IllegalAccessException {
        return c.newInstance();
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException {
        //RelationFactory factory = RelationFactory.create(BioRelation.class);
        //BioRelation bioRelation = (BioRelation)factory.createInstance();
        CollectionTest<SomeObject> collTest = new CollectionTest<SomeObject>();
        Collection<Class<SomeObject>> coll = collTest.getCollection(SomeObject.class, SomeObject.class);
        log.info("Size = " + coll.size());
        for (Object o : coll) {
            //log.info(o.getClass().getName());
            //Class<SomeObject> someObject = (Class<SomeObject>)o;
            SomeObject someObject = (SomeObject)o;
            log.info("someObject = " + someObject.toString());
        }
    }
}

class SomeObject {
    private static Integer x = 0;
    public SomeObject() {
        x++;
    }
    public int getX() {
        return x;
    }
    @Override
    public String toString() {
        return x.toString();
    }
}

class CollectionTest<E> {
    public <E> Collection<E> getCollection(E e, Class c) throws InstantiationException, IllegalAccessException {
        Collection<E> relations = new HashSet<E>();
        RelationFactory<E> relationFactory = RelationFactory.create(c);
        E instance1 = relationFactory.createInstance();
        E instance2 = relationFactory.createInstance();
        E instance3 = relationFactory.createInstance();
        relations.add(instance1);
        relations.add(instance2);
        relations.add(instance3);
        return relations;
    }
}