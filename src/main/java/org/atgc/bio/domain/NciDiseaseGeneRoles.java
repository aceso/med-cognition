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

public enum NciDiseaseGeneRoles {

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
    Not_Assigned("not_assigned: Not assigned"),
    Gene_Associated_With_Disease("Gene_Associated_With_Disease: Gene associated with disease"),
    Gene_Product_Anormaly_Affects_Pathway("Gene_Product_Anormaly_Affects_Pathway: Gene product anormaly affects disease"),
    Gene_Product_Anomaly_Related_To_Gene_Anormaly("Gene_Product_Anomaly_Related_To_Gene_Anormaly: Gene product anomaly related to gene anormaly"),
    Gene_Product_Encoded_By_Gene("Gene_Product_Encoded_By_Gene: Gene product encoded by gene"),
    Gene_Product_Expressed_In_Tissue("Gene_Product_Expressed_In_Tissue: Gene product expressed in tissue"),
    Gene_Product_Has_Associated_Anatomie("Gene_Product_Has_Associated_Anatomie: Gene product has associated anatomie"),
    Gene_Product_Has_Biochemical_Function("Gene_Product_Has_Biochemical_Function: Gene product has biochemical function"),
    Gene_Product_Has_Chemical_Classification("Gene_Product_Has_Chemical_Classification: Gene product has chemical classification"),
    Gene_Product_Has_Malfunction_Type("Gene_Product_Has_Malfunction_Type: Gene product has malfunction type"),
    Gene_Product_Has_Organism_Source("Gene_Product_Has_Organism_Source: Gene product has organism source"),
    Gene_Product_Has_Structural_Domain_Or_Motif("Gene_Product_Has_Structural_Domain_Or_Motif: Gene product has structural domain or motif"),
    Gene_Product_is_Biomarker_of("Gene_Product_is_Biomarker_of: Gene product is biomarker of"),
    Gene_Product_is_Biomarker_Type("Gene_Product_is_Biomarker_Type: Gene product is biomarker type"),
    Gene_Product_is_Pathway_Element("Gene_Product_is_Pathway_Element: Gene product is pathway element"),
    Gene_Product_is_Physical_Part_Of("Gene_Product_is_Physical_Part_Of: Gene product is physical part of"),
    Gene_Product_Malfunction_Associated_With_Disease("Gene_Product_Malfunction_Associated_With_Disease: Gene product malfunction associated with disease"),
    Gene_Product_Plays_Role_In_Biological_Process("Gene_Product_Plays_Role_In_Biological_Process: Gene product plays role in biological process"),
    Gene_Malfunction_Associated_With_Disease("Gene_Malfunction_Associated_With_Disease: Gene malfunction associated with disease"),
    Gene_Expressed_In_Tissue("Gene_Expressed_In_Tissue: Gene expressed in tissue"),
    Gene_Found_In_Organism("Gene_Found_In_Organism: Gene found in organism"),
    Gene_Has_Anormally("Gene_Has_Anormally: Gene has anormally"),
    Gene_Has_Clone("Gene_Has_Clone: Gene has clone"),
    Gene_Has_Expression_Measurement("Gene_Has_Expression_Measurement: Gene has expression measurement"),
    Gene_Has_Function("Gene_Has_Function: Gene has function"),
    Gene_In_Chromosomal_Location("Gene_In_Chromosomal_Location: Gene in chromosomal location"),
    Gene_is_Biomarker_of("Gene_is_Biomarker_of: Gene is biomarker of"),
    Gene_Is_Pathway_Element("Gene_Is_Pathway_Element: Gene is pathway element"),
    Gene_Plays_Role_In_Process("Gene_Plays_Role_In_Process: Gene play role in process"),
    Disease_Has_Cytogenetic_Abnormality("Disease_Has_Cytogenetic_Abnormality: Disease has cytogenetic abnormally"),
    Disease_May_Have_Cytogenetic_Abnormality("Disease_May_Have_Cytogenetic_Abnormality: Disease may have cytogenetic abnormality"),
    Disease_Has_Molecular_Abnormality("Disease_Has_Molecular_Abnormality: Disease has molecular abnormally"),
    Disease_May_Have_Molecular_Abnormality("Disease_May_Have_Molecular_Abnormality: Disease may have molecular abnormally"),

    /**
     * Role Details - OtherRole
     * Gene-Disease and Gene-Compound Role Details most often provide precise descriptions
     * of the association of a gene term and a corresponding disease or compound term.
     * These Details can also describe relevant features of the role of
     * a particular gene (for example, Chemical_or_Drug_Represses_Gene_Product_Expression).
     * While similar to Role Codes, Role Details give more specific semantic descriptions.
     * For example, a Role Detail for a particular gene-disease concept pair association
     * may be GENE_PRODUCT_UPREGULATED_IN_DISEASE, whereas a similar role code
     * may be GENE_ASSOCIATED_WITH_DISEASE. The Role Detail not_assigned indicates
     * that the curator did not or could not assign a specific semantic detail.
     * The Role Detail associated with a specific gene-disease or gene-compound pair
     * is found in the text contents of the XML OtherRole element and is, like Role Codes,
     * the role attribute of the caBIO GeneDiseaseAssociation and GeneAgentAssociation classes.
     */

    Gene_Product_Affects_Disease("Gene_Product_Affects_Disease: Gene product affects disease"),
    Gene_Product_Affects_Disease_Process("Gene_Product_Affects_Disease_Process: Gene product affects disease process"),
    Gene_Product_Expressed_in_Disease("Gene_Product_Expressed_in_Disease: Gene product expressed in disease"),
    Gene_Product_Decreased_in_Disease("Gene_Product_Decreased_in_Disease: Gene product decreased in disease"),
    Gene_Product_Increased_in_Disease("Gene_Product_Increased_in_Disease: Gene product increased in disease"),
    Gene_Product_Level_Changed_in_Disease("Gene_Product_Level_Changed_in_Disease: Gene product level changed in disease"),
    Gene_Expressed_in_Disease("Gene_Expressed_in_Disease: Gene expressed in disease"),
    Gene_Expression_Downregulated_in_Disease("Gene_Expression_Downregulated_in_Disease: Gene expression downregulated in disease"),
    Gene_Expression_Upregulated_in_Disease("Gene_Expression_Upregulated_in_Disease: Gene expression upregulated in disease"),
    Gene_Expression_Changed_in_Disease("Gene_Expression_Changed_in_Disease: Gene expression changed in disease"),
    Gene_May_Be_Associated_With_Disease("Gene_May_Be_Associated_With_Disease: Gene may be associated with disease"),

    /**
     * Related_Function appears as -Related_Function
     */
    Gene_Anormaly_has_Disease_Related_Function("Gene_Anormaly_has_Disease-Related_Function: Gene anormaly has disease related function"),
    Gene_Anormaly_May_have_Disease_Related_Function("Gene_Anormaly_May_have_Disease-Related_Function: Gene anormaly may have disease related function"),
    Gene_Product_Anormaly_has_Disease_Related_Function("Gene_Product_Anormaly_has_Disease-Related_Function: Gene product anormaly has disease related function"),
    Gene_Product_Anormaly_May_have_Disease_Related_Function("Gene_Product_Anormaly_May_have_Disease-Related_Function: Gene product anormaly may have disease related function"),

    Gene_has_Therapeutic_Relevance("Gene_has_Therapeutic_Relevance: Gene has therapeutic relevance"),
    Gene_May_have_Therapeutic_Relevance("Gene_May_have_Therapeutic_Relevance: Gene may have therapeutic relevance"),
    Gene_Product_has_Therapeutic_Relevance("Gene_Product_has_Therapeutic_Relevance: Gene product has therapeutic relevance"),
    Gene_Product_May_have_Therapeutic_Relevance("Gene_Product_May_have_Therapeutic_Relevance: Gene product may have therapeutic relevance");

    private final String value;

    private static final Map<String, NciDiseaseGeneRoles> stringToEnum = new HashMap<String, NciDiseaseGeneRoles>();

    static { // init map from constant name to enum constant
        for (NciDiseaseGeneRoles en : values())
            stringToEnum.put(en.toString(), en);
    }

    public boolean equals(NciDiseaseGeneRoles bioType) {
        return value.equals(bioType.toString());
    }

    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    NciDiseaseGeneRoles(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static NciDiseaseGeneRoles fromString(String value) {
        return stringToEnum.get(value);
    }

     /**
     * The role will be Gene_Anormaly_has_Disease-Related_Function
     * will return value: "Gene anormaly has disease related function"
     * @param role
     * @return String
     */
    public static String getLongString(String role) {
        for (String value : stringToEnum.keySet()) {
            if (value.startsWith(role)) {
                return value.split(":")[1];
            }
        }
        return "";
    }

    public static NciDiseaseGeneRoles getRoleEnum(String prefixVal) {
        for (String value : stringToEnum.keySet()) {
            if (value.startsWith(prefixVal)) {
                return fromString(value);
            }
        }
        return null;
    }

}
