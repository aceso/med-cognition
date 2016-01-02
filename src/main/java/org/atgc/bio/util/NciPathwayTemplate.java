/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import org.atgc.bio.meta.BioEntity;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.domain.BioEntityClasses;
import org.atgc.bio.domain.BioTypes;


/**
 *
 * @author jtanisha-ee
 */
public class NciPathwayTemplate<T> {
   
    protected static Log log = LogFactory.getLog(NciPathwayTemplate.class);
 
    private static Class getBioClass(Object entity) {
        
        Class tClass = entity.getClass();
        BioTypes bt = BioTypes.fromString(tClass.getSimpleName());
        log.info("bioType =" + tClass.toString());

        BioEntityClasses bioEntity = BioEntityClasses.fromString(bt.toString());
        if (bioEntity != null) { 
           return bioEntity.getClass();
        } else {
            return null;
        }
    }
    
    
    /**
     * Go through the nodeEntity and fetch the @UniquelyIndexed annotation
     * so that we can fetch the node.
     * 
     * @param nodeEntity
     * @return
     * @throws IllegalAccessException 
     */
            
    public boolean setFieldValue(Object obj, String fieldName, String fieldValue) throws IllegalAccessException {
       
        log.info("setFieldValue");
        if (fieldName == null) {
            return false;
        } else {
            /*
             * get the declared fields
             */
            Class tClass = obj.getClass();
            Field[] fields = obj.getClass().getDeclaredFields();
            for (Field field : fields) {
                Annotation[] fieldAnnotations = field.getAnnotations();
                for (Annotation fieldAnnotation : fieldAnnotations) {
                    if (field.getName().equals(fieldName)) {
                        field.set(obj, fieldValue);
                        return true;
                    }
                }    
            }
        }
        return false;
    } 
    
   /**
    * setPTMValues        
    *
    * @param complex - {@link BioEntity#COMPLEX}
    * @param obj - contains PTM information 
    * @param proteinMolecule {@link BioEntity#PROTEIN}
    * @throws IllegalArgumentException
    * @throws IllegalAccessException
    * @throws ClassNotFoundException
    * @throws InstantiationException 
    */
    /*
   public static void setPTMValues(BioEntity entity, BioEntity endEntity, 
           String id, String pos, String aa, String modification) throws IllegalArgumentException, IllegalAccessException, ClassNotFoundException, InstantiationException, NoSuchMethodException, InvocationTargetException {  
       
        Class tClass = entity.getClass();    
        String methodName = "setPTMExpressionRelation";         
        Method method = tClass.getDeclaredMethod(methodName, Object.class,
                                String.class, String.class, String.class, String.class);
        if (endEntity != null) {  
            method.setAccessible(true);
            Object invoke = method.invoke(endEntity, id, pos, aa, modification);
         }
   }
   */
   
   
  /*
   public static void setFamilyMemberRelation(Object entity, FamilyMemberRelation relation) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
       Class bioClass = getBioClass(entity);
       if (bioClass != null) { 
            Method method = bioClass.getDeclaredMethod("setMemberOfProteinFamily", BioRelationEntityClasses.FAMILY_MEMBER_RELATION.getAnnotationClass());
            method.setAccessible(true);
            Object invoke = method.invoke(relation);
       }            
   }
   * 
   */
   
   public static void invokeMethod(Object entity, String methodName, Class<?>[] params, Object[] values) throws NoSuchMethodException, IllegalAccessException, IllegalArgumentException, InvocationTargetException {
       log.info("invokeMethod(), " + methodName + ", params.length=" + params.length + ",values.length=" +values.length);
       Class bioClass = getBioClass(entity);
       if (bioClass != null) {
           Method method = bioClass.getDeclaredMethod(methodName, params);  
           Object invoke = method.invoke(values); 
       }
       
   }
 
}
