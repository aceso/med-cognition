/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.util;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This contains basic calendar utilities.
 *
 * @author Smitha Gudur
 */
public class MathUtil {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());

    private volatile StrUtil strUtil;

    /**
     * Return 2 digit prec floating point.
     * @param obj send a Float obj 
     */
    public static Float twoDigitPrec(Float obj) {
        return new Float(((float)(Math.floor(((Float)obj).floatValue()*100)))/(float)100);
    }

    /**
     * Return 2 digit prec floating point.
     * @param obj send a Double obj 
     */
    public static Double twoDigitPrec(Double obj) {
        return new Double(((double)(Math.floor(((Double)obj).doubleValue()*100)))/(double)100);
    }

    /**
    * Return a random int in a specified range.
    *
    * @param mn the min
    * @param mx the max
    * @return int the random
    */
    public static int randomInt(int mn, int mx) {
       double d = Math.random();
       return (int)(mn + (mx+1-mn)*d);
    }

    /**
     * Left padding for specified number of digits.
     *
     * @param num the number to be padded
     * @param digits the number of total digits after the padding
     * @param String return the padded number as a string
     */
    public static String leftPadding(int num, int digits) {
       String numStr = new Integer(num).toString();
       if (numStr.length() == digits) return numStr;
       StringBuffer sb = new StringBuffer();
       for (int i = 0; i < (digits - numStr.length()); i++) {
           sb.append("0"); 
       }
       return sb.append(numStr).toString();
    }

    public static boolean isInteger(String s) {
        try {
            if (StrUtil.isNull(s)) return false;
            new Integer(s);
        } catch (NumberFormatException e) {
            return false;
        }
        return true;
    }
}