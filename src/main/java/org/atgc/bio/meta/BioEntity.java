/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.meta;

import org.atgc.bio.domain.BioTypes;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;



/**
 *
 * @author jtanisha-ee
 */
@Retention(RetentionPolicy.RUNTIME)
public @interface BioEntity {
    public BioTypes bioType();
}
