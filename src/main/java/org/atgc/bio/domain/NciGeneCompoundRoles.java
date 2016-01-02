/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import org.atgc.bio.meta.BioEntity;

import java.util.HashMap;
import java.util.Map;

/**
 * These are all the supported Annotation Fields or {@link BioEntity} objects.
 * These also are treated as dimensions.
 *
 * @author jtanisha-ee
 */

public enum NciGeneCompoundRoles {

   /**
    *
    * https://wiki.nci.nih.gov/display/cageneindex/Data%2C+Metadata%2C+and+Annotations#DataMetadataandAnnotations-RoleDetails
    *
    * Role Codes
    * Gene-Disease and Gene-Compound Role Codes most often describe that a gene is associated
    * with a disease or compound (for example, GENE_ASSOCIATED_WITH_DISEASE) or
    * how the concepts are associated (for example, Chemical_or_Drug_Is_Metabolized_By_Enzyme),
    * but they also may describe relevant features of the role of a particular gene
    * (for example, GENE_HAS_FUNCTION). For the former, the gene name, Role Code,
    * and disease or compound often can form a sentence, such as "BRCA1 GENE_ASSOCIATED_WITH_DISEASE BREAST CANCER."
    * The Role Code not_assigned indicates that the curator did not or could not assign a specific code.
    * he Role Code associated describing evidence of a gene-disease or gene-compound pair
    * is found in the text contents of the XML PrimaryNCIRoleCode element and is the
    * role attribute of the caBIO GeneDiseaseAssociation and GeneAgentAssociation classes,
    * gov.nih.nci.cabio.domain.GeneDiseaseAssociation and gov.nih.nci.cabio.domain.
    * GeneAgentAssociation.
    */
    not_assigned("Not assigned"),
    Chemical_or_Drug_Affects_Cell_Type_or_Tissue("Chemical or Drug affects cell type or tissue"),
    Chemical_or_Drug_Plays_Role_in_Biological_Process("Chemical or Drug plays role in biological process"),
    Chemical_or_Drug_FDA_Approved_for_Disease("Chemical or Drug FDA approved for disease"),
    Chemical_or_Drug_Is_Metabolized_By_Enzyme("Chemical or Drug is metabolized by enzyme"),
    Chemical_or_Drug_Has_Accepted_Therapeutic_Use_For("Chemical or Drug has accepted therapeutic use"),
    Chemical_or_Drug_Has_Study_Therapeutic_Use_For("Chemical or Drug has study therapeutic use for"),
    Chemical_or_Drug_Has_Mechanism_Of_Action("Chemical or Drug has mechanism of action"),
    Chemical_or_Drug_Affects_Gene_Product("Chemical or Drug affects gene product"),
    Chemical_or_Drug_Has_Target_Gene_Product("Chemical or Drug has target gene product");


    private final String value;

    private static final Map<String, NciGeneCompoundRoles> stringToEnum = new HashMap<String, NciGeneCompoundRoles>();

    static { // init map from constant name to enum constant
        for (NciGeneCompoundRoles en : values())
            stringToEnum.put(en.toString(), en);
    }

    public boolean equals(NciGeneCompoundRoles bioType) {
        return value.equals(bioType.toString());
    }

    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    NciGeneCompoundRoles(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static NciGeneCompoundRoles fromString(String value) {
        return stringToEnum.get(value);
    }

     /**
     * The role will be Gene_Anormaly_has_Disease-Related_Function
     * will return value: "Gene anormaly has disease related function"
     * @return
     */
    /*
    public static String getLongString(String role) {
        for (String value : stringToEnum.keySet()) {
            if (value.startsWith(role)) {
                return value.split(":")[1];
            }
        }
        return "";
    }
    *
    */

    /*
    public static NciGeneCompoundRoles getRoleEnum(String prefixVal) {
        for (String value : stringToEnum.keySet()) {
            if (value.startsWith(prefixVal)) {
                return fromString(value);
            }
        }
        return null;
    } */

}
