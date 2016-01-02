/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;


import java.util.HashMap;
import java.util.Map;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.atgc.bio.meta.BioEntity;

/**
 * These are all the supported Annotation Fields or {@link BioEntity} objects.
 * These also are treated as dimensions.
 *
 * @author jtanisha-ee
 */

public enum NciGeneDrugRoles {

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
    *
    * Role Codes
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
    Chemical_or_Drug_Has_Target_Gene_Product("Chemical or Drug has target gene product"),

    /**
     * Role details
     */

    Chemical_or_Drug_in_Clinical_Study("Chemical or drug in clinical study"),
    Chemical_or_Drug_May_Affect_Gene_Product("Chemical or drug may affect gene product"),
    Chemical_or_Drug_May_Affect_Gene("Chemical or drug may affect gene"),
    Chemical_or_Drug_Affects_Gene("Chemical or drug affect gene"),
    Chemical_or_Drug_Regulates_Gene("Chemical or drug regulates gene"),
    Chemical_or_Drug_Regulates_Gene_Product("Chemical or drug regulates gene product"),
    Chemical_or_Drug_Activates_Gene_Product("Chemical or drug activates gene product"),
    Chemical_or_Drug_Inhibits_Gene_Product("Chemical or drug inhibits gene product"),
    Chemical_or_Drug_Affects_Gene_Product_Function("Chemical or drug affect gene product function"),
    Chemical_or_Drug_Binds_to_Gene_Product("Chemical or drug binds to gene product"),
    Chemical_or_Drug_Affects_Expression("Chemical or drug affects expression"),
    Chemical_or_Drug_Affects_Gene_Expression("Chemical or drug affects gene expression"),
    Chemical_or_Drug_Affects_Gene_Product_Expression("Chemical or drug affects gene product expression"),
    Chemical_or_Drug_Changes_Expression("Chemical or drug changes expression"),
    Chemical_or_Drug_Induces_Gene_Expression("Chemical or drug induces gene expression"),
    Chemical_or_Drug_Induces_Gene_Product_Expression("Chemical or drug induces gene product expression"),
    Chemical_or_Drug_Regulates_Expression("Chemical or drug regulates expression"),
    Chemical_or_Drug_Represses_Gene_Expression("Chemical or drug represses gene expression"),
    Chemical_or_Drug_Represses_Gene_Product_Expression("Chemical or drug represses gene product expression"),
    Chemical_or_Drug_Mediates_Pathway_Activity("Chemical or drug mediates pathway activity"),
    Chemical_or_Drug_Increases_Pathway_Activity("Chemical or drug increases pathway activity"),
    Chemical_or_Drug_Decreases_Pathway_Activity("Chemical or drug decreases pathway activity"),
    Chemical_or_Drug_Mediates_Metabolic_Status("Chemical or drug mediates metabolic status"),
    Chemical_or_Drug_Increases_Metabolic_Status("Chemical or drug increases metabolic status"),
    Chemical_or_Drug_Increases_Affects_Metabolic_Status("Chemical or drug increases affects metabolic status"),
    Chemical_or_Drug_Decreases_Metabolic_Status("Chemical or drug decreases metabolic status"),
    Chemical_or_Drug_Has_Physiologic_Effect("Chemical or drug has physiological effect"),
    Gene_Product_Affects_Compound("Gene product affects compound"),
    Gene_Product_May_Affect_Compound("Gene product may affect compound"),
    Gene_Affects_Compound("Gene affects compound"),
    Gene_May_Affect_Compound("Gene may affect compound"),
    Gene_Product_Antagonizes_Chemical_or_Drug("Gene product antagonizes chemical or drug"),
    Gene_Product_Transports_Compound("Gene product transports compound"),
    Gene_Anomaly_Effects_Resistance_to_Chemical_or_Drug("Gene anomaly effects resistance to chemical or drug"),
    Gene_Product_Anomaly_Effects_Resistance_to_Chemical_or_Drug("Gene product anomaly efffects resistance to chemical or drug"),
    Gene_Anomaly_May_Effect_Resistance_to_Chemical_or_Drug("Gene anomaly may effect resistance to chemical or drug"),
    Gene_Product_Anomaly_May_Effect_Resistance_to_Chemical_or_Drug("Gene product anomaly may effect resistance to chemical or drug"),
    Gene_is_Associated_with_Resistance_to_Chemical_or_Drug("Gene is associated with resistance to chemical or drug"),
    Gene_Product_is_Associated_with_Resistance_to_Chemical_or_Drug("Gene product is associated with resistance to chemical or drug"),
    Gene_May_be_Associated_with_Resistance_to_Chemical_or_Drug("Gene may be associated with resistance to chemical or drug"),
    Gene_Product_May_be_Associated_with_Resistance_to_Chemical_or_Drug("Gene product may be associated with resistance to chemical or drug");


    private final String value;
    protected static Log log = LogFactory.getLog(NciGeneDrugRoles.class);

    private static final Map<String, NciGeneDrugRoles> stringToEnum = new HashMap<String, NciGeneDrugRoles>();

    static { // init map from constant name to enum constant
        for (NciGeneDrugRoles en : values())
            stringToEnum.put(en.toString(), en);
    }

    /*
     * To get the name of the enum
     */
    public static NciGeneDrugRoles getEnum(String role) {
        for (NciGeneDrugRoles en : values()) {
            //log.info(en.name());
            if (en.name().equals(role)) {
                return en;
            }
        }
        return null;
    }

    public boolean equals(NciGeneDrugRoles bioType) {
        return value.equals(bioType.toString());
    }

    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    NciGeneDrugRoles(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    public static NciGeneDrugRoles fromString(String value) {
        return stringToEnum.get(value);
    }



}
