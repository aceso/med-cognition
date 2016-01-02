/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.util;

import java.util.regex.Pattern;
import java.util.regex.Matcher;
import java.util.StringTokenizer;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;

/**
 * This contains some string functions.
 *
 * @author Smitha Gudur
 */
public class StrUtil {

    /** Logger for this class and subclasses */
    protected final Log logger = LogFactory.getLog(getClass());
    /**
     * This checks for null and also trims before it compares with "".
     * @param str the string to check
     * @return boolean return true if found null or ""
     */
    public static boolean isNull(String str) {
        return (str == null) || str.trim().equals("");
    }

    /**
    * Converts a user entered string in text area of gui to a format that
    * is liked by sql.
    *
    * @param str the string to replace
    * @return String
    */
   public static String goodStr(String str) {
       if (!isNull(str)) {
          return str.replaceAll("'", "&apos;").replaceAll("\"", "&quot;");
       }
       return str;
   }


    public static String htmlStr(String str) {
	if (!isNull(str)) {
	    return str.replaceAll("<", "&lt;").replaceAll(">", "&gt;");
	}
	return str;
    }

   /**
    * Converts a string to display correctly in AJAX text area.
    *
    * @param str the string to replace
    * @return String
   */
   public static String ajaxFormStr(String str) {
       if (!isNull(str)) {
          return str.replaceAll("&apos;", "'").replaceAll("&quot;", "\"");
       }
       return str;
   }

   /**
    * Converts a string to display correctly in non AJAX text area.
    *
    * @param str the string to replace
    * @return String
   */
   public static String formStr(String str) {
       if (!isNull(str)) {
          return str.replaceAll("<br>", "\n").replaceAll("<BR>", "\n");
       }
       return str;
   }

   /**
    * A bit demented method that cleans strings for text fields.
    * Good for description and other text fields.
    * used for street, url related fields
    *
    * @param str
    * @return String
    */
    public static String goodText(String str) {
       if (!isNull(str)) {
         return str.replaceAll("[^\\w&&^\\(&&^\\.&&^!&&^\\\"&&^#&&^\\$&&^\\&&&^\\'&&^\\(&&^\\)&&^*&&^+&&^,&&^\\-&&^+/&&^:&&^\\s&&^;&&^<&&^=&&^>&&^?&&^@&&^\\[&&^\\]&&^\\^&&^`&&^\\{&&^\\|&&^\\}&&^~&&^%]", "");
       }
       return str;
    }


   /**
    * Strip all br tags and EOL.
    *
    * @param str the string to replace
    * @return String
   */
   public static String stripStr(String str) {
       if (!isNull(str)) {
          return str.replaceAll("<br>", "").replaceAll("<BR>", "").replaceAll("\r\n", "").replaceAll("\n", "");
       }
       return str;
   }

    /**
     * Add slashes to quotes
     *
     * @param str the string to replace
     * @return String
     */
    public static String addSlashes(String str){
	if(str==null) return "";
	StringBuilder s = new StringBuilder ((String) str);
	for (int i = 0; i < s.length(); i++)
	    if (s.charAt (i) == '\'' || s.charAt(i) == '"')
		s.insert (i++, '\\');
	return s.toString();
    }

    /**
     * Convert seconds to String.
     *
     * @param length the length in seconds
     * @return String
     */
    public static String getTime(Integer length) {
	int sec = length.intValue();
	int min = 0;
	int hour = 0;
	String ttr = "";
	if(sec>=60) {
	    min = sec / 60;
	    sec = sec%60;
	    if(min > 60) {
		hour = min/60;
		min = min%60;
	    }
	}
	if(hour<10) {
	    ttr = ttr+"0";
	}
	ttr = ttr+hour+":";
	if(min<10) {
            ttr = ttr+"0";
        }
        ttr = ttr+min+":";
        if(sec<10) {
            ttr = ttr+"0";
        }
        ttr = ttr+sec;
	return ttr;
    }

    /**
     * Convert seconds to Integer.
     *
     * @param value the length in String
     * @return Integer
     */
    public static Integer getLength(String value) {
	if(value != null) {
	    String [] values = value.split(":");
	    int hour, min, sec;
	    if(values.length==3) {
		hour = Integer.parseInt(values[0]);
		min = Integer.parseInt(values[1]);
		sec = Integer.parseInt(values[2]);
	    } else {
		hour = 1;
		min = Integer.parseInt(values[0]);
		sec = Integer.parseInt(values[1]);
	    }
	    int length = hour*60+min*60+sec;
	    return new Integer(length);
	} else {
	    return 0;
	}
    }


   public static boolean hasHTML(String body) {
       if (!isNull(body)) {
          Pattern pattern = Pattern.compile("<.*?>", Pattern.DOTALL);
          Matcher matcher = pattern.matcher(body);
          return matcher.find();
       }
       return false;
   }

    public static String stripHTML(String body) {
	if (!isNull(body)) {
	    Pattern pattern = Pattern.compile("<.*?>", Pattern.DOTALL);
	    Matcher matcher = pattern.matcher(body);
	    StringBuffer sb = new StringBuffer(body);
	    while (matcher.find()) {
		//logger.info("start = " + matcher.start() + ", end = " + matcher.end());
		sb = sb.replace(matcher.start(), matcher.end(), "");
		matcher = pattern.matcher(sb.toString());
	    }
	    return sb.toString();
	}
	return null;
    }


   /**
    * Converts a user entered string in text area of gui to a format that
    * is liked by web.
    *
    * @param str the string to replace
    * @return String
    */
   public static String goodWebStr(String str) {
       if (isNull(str)) {
          return str;
       }
       /**
        * check if this str contains html tags. Leave them intact.
        */
       if (hasHTML(str)) {
          return str;
       }
       if (str.indexOf("<") == -1) {
           return str.replaceAll("\r\n", "<br>").replaceAll("\n", "<br>").replaceAll("\r", "<br>");
       } return str;
   }

   public static String truncateText(String text) {
       StringTokenizer st = new StringTokenizer(text);
       final int MAX_WORDS = 12;
       int cnt = 0;
       StringBuilder sb = new StringBuilder();
       while (st.hasMoreTokens() && cnt < MAX_WORDS) {
           sb.append(st.nextToken());
           sb.append(" ");
           cnt++;
       }
       if (st.hasMoreTokens() && (cnt == MAX_WORDS)) {
           sb.append("...");
       }
       return sb.toString();
   }

    public static String truncateText2(String text) {
	StringTokenizer st = new StringTokenizer(text);
	final int MAX_WORDS = 24;
	int cnt = 0;
	StringBuilder sb = new StringBuilder();
	while (st.hasMoreTokens() && cnt < MAX_WORDS) {
	    sb.append(st.nextToken());
	    sb.append(" ");
	    cnt++;
	}
	if (st.hasMoreTokens() && (cnt == MAX_WORDS)) {
	    sb.append("...");
	}
	return sb.toString();
    }

    public static String truncateText3(String text) {
        StringTokenizer st = new StringTokenizer(text);
        final int MAX_WORDS = 30;
        int cnt = 0;
        StringBuilder sb = new StringBuilder();
        while (st.hasMoreTokens() && cnt < MAX_WORDS) {
            sb.append(st.nextToken());
            sb.append(" ");
            cnt++;
        }
        if (st.hasMoreTokens() && (cnt == MAX_WORDS)) {
            sb.append("...");
        }
        return sb.toString();
    }

   /**
    * Removes bad ascii characters.
    *
    * @param s
    * @return String Good ASCII string
    */
    public static String removeBadASCIIReplace(String s) {
         String stripped = "";
         int value;

         for (int i=0; i<s.length(); i++) {
            value = s.charAt(i);
            if (value >= 0 && value <= 126) {
                  stripped += s.charAt(i);
            }else{
               stripped += " ";
            }

         }

         return stripped;
      }

   /**
    * Removes bad ascii characters.
    *
    * @param s
    * @return String Good ASCII string
    */
    public static String removeBadASCII(String s) {
         String stripped = "";
         int value;

         for (int i=0; i<s.length(); i++) {
            value = s.charAt(i);
            if (value >= 0 && value <= 126) {
                  stripped += s.charAt(i);
            }else{
               stripped += "&nbsp;";
            }

         }

         return stripped;
      }

    /**
     * Replaces bad ascii characters.
     *
     * @param s
     * @return String Good ASCII string
     */
    public static String replaceBadASCII(String s) {
	String stripped = "";
	int value;

	for (int i=0; i<s.length(); i++) {
            value = s.charAt(i);
	    //            logger.info(value+" "+(char)value);
            if (value >= 0 && value <= 126) {
		stripped += s.charAt(i);
            }else{
		stripped += "&nbsp;";
            }

	}

	return stripped;
    }
    /**
    * fixes issue when saving from a text area.
    *
    * @param s
    * @return String testAreaSafe string
    */
    public static String textareaSafeText(String s) {
         String stripped;
         stripped = s.replaceAll("&", "&amp;");
         return stripped;
      }

   /**
    * Determines if the related content box should be shown.
    *
    * @param body
    * @return String The flag (0 or 1)
    */
    public static String setRelatedFlag(String body) {

       // If the length is less than 2500 or there is an img, code block (pre tag), or an HTML
       // table in the first 2500 characters, flag it.
       if (body != null && body.length() > 0) {

          if(body.length() < 2500) {
             return "1";
          }

          if (body.indexOf("<img") != -1 && body.indexOf("<img") < 2500) {
             return "1";
          }

          if (body.indexOf("<code") != -1 && body.indexOf("<pre") < 2500) {
             return "1";
          }

          if (body.indexOf("<html") != -1 && body.indexOf("<html") < 2500) {
             return "1";
          }

          // If we get here then all the cases didn't match and we are ok
          return "0";
       }else {
          // No body at all
          return "1";
       }
    }

    /**
     * Compute length in java, as in velocity it does not work.
     *
     * @param a
     * @return
     * @reeturn int
     */
    public static int length(String[] a) {
       return a.length;
    }

    /**
     * Replace UTF-8 characters
     * @param str
     * @return String the processed string
     */
    public static String replaceUTF8(String str) {
	str = str.replaceAll("\u0096", "&#8211;"); // en dash
        str = str.replaceAll("\u2013", "&#8211;"); // en dash
        str = str.replaceAll("\u2014", "&#8212;"); // em dash
        str = str.replaceAll("\u0097", "&#8212;"); // em dash
        str = str.replaceAll("\u0091", "&#8216;"); // single quote
        str = str.replaceAll("\u0092", "&#8217;"); // single quote
        str = str.replaceAll("\u0093", "&#8220;"); // double quote
        str = str.replaceAll("\u0094", "&#8221;"); // double quote
	str = str.replaceAll("\u2019", "&#8217;"); // single quote
	str = str.replaceAll("\u00B5", "&#181;"); // micro sign
	str = str.replaceAll("\u00A0", "&nbsp;"); // micro sign

	return str;
    }

    /*
     /**
     * A simple json string escape util.
     *
     * @param in
     * @return

    public static String escapeJson(String in) {
        in = in.replaceAll("\"", "\\\\u0022");
        return in.replaceAll("\\\\", "\\\\u005C");
    }
     */

    /**
     * example in MongoJsonUtil in Mongod
     * @param str
     * @return
     */
    public static String replaceASCII(String str) {
        str = str.replaceAll("\\(", "\\\\u0028");
        str = str.replaceAll("\\)", "\\\\u0029");
        str = str.replaceAll("\\/", "\\\\u002F");
        str = str.replaceAll(" ", "\\\\u0020");
       // str = str.replaceAll("&", "\\\\u0026");
        str = str.replaceAll("-", "\\\\u002D");
        str = str.replaceAll("\\+", "\\\\u002B");
        //str = str.replaceAll("I", "\u0049");
        str = str.replaceAll("_", "\\\\u005F");
        return str;
    }

}