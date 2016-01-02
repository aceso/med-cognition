/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import java.lang.reflect.Field;
import java.util.Collection;
import java.util.HashSet;

/**
 *
 * @author jtanisha-ee
 */
public class TemplateInstanceTest {

    public <R> Collection<R> getCollection(Class<R> rClass) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        //Class rClass = r.getClass();
        Collection<R> coll = new HashSet<R>();
        for (int i = 0; i < 5; i++) {
            R inst = (R)((Class)rClass).newInstance();
            Field field = ((Class)rClass).getDeclaredField("i");
            field.setAccessible(true);
            field.set(inst, i);
            coll.add(inst);
        }
        return coll;
    }

    static class SomeRelation {
        int i;
        public void setI(int i) {
            this.i = i;
        }
        @Override
        public String toString() {
            return new Integer(i).toString();
        }
    }

    public static void main(String[] args) throws InstantiationException, IllegalAccessException, NoSuchFieldException {
        TemplateInstanceTest t = new TemplateInstanceTest();
        Collection<SomeRelation> coll = t.getCollection(SomeRelation.class);
        for (SomeRelation relation : coll) {
            System.out.println("relation = " + relation);
        }
    }
}
