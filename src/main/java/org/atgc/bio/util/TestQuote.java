/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import java.nio.charset.Charset;
import java.util.Map;

/**
 *
 * @author jtanisha-ee
 */
public class TestQuote {

    public static void main(String[] args) {
        String mw = "2.68";
        System.out.println(String.valueOf(new Double(mw)*1000));
        String s = "\"Shewanella MacDonell and Colwell 1986\" RELATED synonym []";
        String[] str = s.split("\"");
        System.out.println(str[1]);
        Map<String, Charset> map = Charset.availableCharsets();
        for (String charset : map.keySet()) {
            System.out.println(charset);
        }
    }

}
