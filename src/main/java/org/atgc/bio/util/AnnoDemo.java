/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import java.lang.annotation.Annotation;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.reflect.Field;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


/**
 * This code demos how to use Annotations. The main class has two annotations. Each annotation 
 * has one or more params.
 * 
 * @author jtanisha-ee
 */
@AnnoOne (fluke = "lute")
@AnnoTwo (foo = "good", boo = "bad")
public class AnnoDemo {
    
    protected static Logger log = LogManager.getLogger(AnnoDemo.class);
    
    /**
     * This is an annotation demo for a class member or field.
     * We have params here.
     */
    @AnnoField (pooh = "bald")
    public String bar;
    
    /**
     * This is an annotation demo for a class member with a second field.
     * We have params again.
     */
    @AnnoFieldTwo (moo = "popat")
    public String cow;
    
    /**
     * This is an annotation demo for a method. In this case main.
     * Again it has params.
     * @param args 
     */
    @AnnoMethod (papa = "lalla")
    public static void main(String[] args) {
        
        // First create an instance of the class.
        AnnoDemo annoTest = new AnnoDemo();
        
        // You must get the Class for this instance
        Class annoClass = annoTest.getClass();
        
        // Get a list of all annotations for the class. So in this case, we 
        // expect two annotations. AnnoOne and AnnoTwo. Then for each annotation
        // get the value of the params. We can check the annotation type, in this
        // case we were forced to do a contains(). We would have preferred  not
        // doing that.
        Annotation[] anno = annoClass.getAnnotations();
        for (Annotation annotation : anno) {
            log.info(annotation.annotationType().getName());
            if (annotation.annotationType().getName().contains("AnnoTwo")) {
                log.info("AnnoTwo: foo =  " + ((AnnoTwo)annotation).foo());
                log.info("AnnoTwo: boo = " + ((AnnoTwo)annotation).boo());
            } else if (annotation.annotationType().getName().contains("AnnoOne")) {
                log.info("AnnoOne: fluke =  " + ((AnnoOne)annotation).fluke());
            }
        }
        
        // We get the list of all fields, in this case there are two fields
        // bar and cow. Then we get the annotations for these fields. We have one
        // annotation for each field. Each annotation has params. 
        Field[] fields = annoClass.getFields();
        for (Field field : fields) {
            field.setAccessible(true);
            Annotation[] annos = field.getAnnotations();
            for (Annotation a : annos) {
                if (field.getName().equals("bar")) {
                    log.info("field = " + field.getName() + ", value = " + ((AnnoField)a).pooh());
                } else {
                    if (field.getName().equals("cow")) {
                        log.info("field = " + field.getName() + ", value = " + ((AnnoFieldTwo)a).moo());
                    }
                }
                
            }
        }
    }
}

/**
 * We declare all the annotation classes as private in this file.
 * 
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
@interface AnnoTwo {
    public String foo();
    public String boo();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AnnoOne {
    public String fluke();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AnnoMethod {
    public String papa();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AnnoField {
    public String pooh();
}

@Retention(RetentionPolicy.RUNTIME)
@interface AnnoFieldTwo {
    public String moo();
}