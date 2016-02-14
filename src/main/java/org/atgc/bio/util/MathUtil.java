/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * This contains basic calendar utilities.
 *
 * @author Smitha Gudur
 */
@SuppressWarnings("javadoc")
public class MathUtil {

    /** Logger for this class and subclasses */
    protected static final Logger logger = LogManager.getLogger(MathUtil.class);

    /**
     * Return 2 digit prec floating point.
     * @param obj send a Float obj 
     */
    public static Float twoDigitPrec(Float obj) {
        return (float) Math.floor(obj * 100) / (float) 100;
    }

    /**
     * Return 2 digit prec floating point.
     * @param obj send a Double obj 
     */
    public static Double twoDigitPrec(Double obj) {
        return Math.floor(obj * 100) / (double) 100;
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
     * @return
     */
    public static String leftPadding(int num, int digits) {
       String numStr = Integer.toString(num);
       if (numStr.length() == digits) return numStr;
       StringBuilder sb = new StringBuilder();
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