/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain.types;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author jtanisha-ee
 */

public enum SaccharideTypes {


    MONOSACCHARIDE("monosaccharide"),

    DISACCHARIDE("disaccharide"),

    /**
     * An oligosaccharide (from the Greek oligos, a few, and sacchar, sugar) is a saccharide polymer
     * containing a small number (typically two to nine[1]) of simple sugars (monosaccharides).
     * Oligosaccharides can have many functions; for example, they are commonly found on the plasma
     * membrane of animal cells where they can play a role in cell–cell recognition.
     *
     * <p>
     * Glycobiology is the branch of biology concerned with the study of structure,
     * biosynthesis, and function of saccharides (sugar chains), which may exist purely
     * or conjugated to other biological molecules to form glycoconjugates. The study of
     * such molecules is technically challenging as no sequencing tool, such as that used
     * in proteomics or genomics, is available. However, following on from genomics and
     * proteomics, there is increasing recognition of the importance of carbohydrate
     * based molecules in basic cellular processes. This has resulted in more extensive
     * glycomic studies in the areas of glycosylation of therapeutic proteins, glycosylation
     * patterns in cell recognition, cellular glycoprofiling studies in cancer and other
     * diseases, correlation between activity and sulphation patterns in glycosaminoglycans,
     * and the improved chemical analysis and synthesis of carbohydrate molecules.
     *
     * <p>
     * <a href="http://www.glycomar.com/documents/Oligosaccharidesindrugdiscovery2012.pdf">Oligosaccharides in Drug Discovery</a>
     */
    OLIGOSACCHARIDE("oligosaccharide"),

    /**
     * In the last three decades, numerous polysaccharides and polysaccharide-protein complexes have
     * been isolated from mushrooms and used as a source of therapeutic agents. The most promising
     * biopharmacological activities of these biopolymers are their immunomodulation and anti-cancer
     * effects. They are mainly present as glucans with different types of glycosidic linkages such as
     * (1-->3), (1-->6)-beta-glucans and (1-->3)-alpha-glucans, and as true herteroglycans, while others
     * mostly bind to protein residues as polysaccharide-protein complexes. Three antitumor mushroom
     * polysaccharides, i.e. lentinan, schizophyllan and protein-bound polysaccharide (PSK, Krestin),
     * isolated respectively, from Lentinus edodes, Schizophyllum commune and Coriolus versicolor,
     * have become large market items in Japan. Lentinan and schizophyllan are pure beta-glucans,
     * whereas PSK is a protein-bound beta-glucan. A polysaccharide peptide (PSP), isolated from a
     * strain of Coriolus versicolor in China, has also been widely used as an anti-cancer and
     * immunomodulatory agent. Although the mechansim of their antitumor action is still not
     * completely clear, these polysaccharides and polysaccharide-protein complexes are suggested
     * to enhance cell-mediated immune responses in vivo and in vitro and act as biological response
     * modifiers. Potentiation of the host defense system may result in the activation of many kinds
     * of immune cells that are vitally important for the maintenance of homeostasis. Polysaccharides
     * or polysaccharide-protein complexes are considered as multi-cytokine inducers that are able
     * to induce gene expression of vaious immunomodulatory cytokines and cytokine receptors. Some
     * interesting studies focus on investigation of the relationship between their structure and
     * antitumor activity, elucidation of their antitumor mechanism at the molecular level, and
     * improvement of their various biological activities by chemical modifications.
     *
     * <p>
     * Polysaccharide-K (Krestin, PSK) is a protein-bound polysaccharide, which is used as an anticancer
     * immunologic adjuvant in some countries. Japan began using PSK in 1977[1] and it is presently
     * covered by government health insurance. PSK is isolated from the fruitbody of Trametes versicolor.
     * Preliminary evidence indicates PSK has anticancer activity in vitro,[2] in vivo[3] and in human
     * clinical trials.[4] Preliminary research has also demonstrated that PSK may inhibit various cancer
     * onset mechanisms.[5] Preliminary evidence indicates PSK may have use as an adjuvant in the treatment
     * of gastric, esophageal, colorectal, breast and lung cancers.[6] Human clinical trials suggest PSK
     * may affect cancer recurrence when used as an adjuvant,[4][7] and basic research has demonstrated it
     * inhibited certain human cancer cell lines in vitro.[8][9][10] The MD Anderson Cancer Center
     * reported that it is a "promising candidate for chemoprevention due to the multiple effects on
     * the malignant process, limited side effects and safety of daily oral doses for extended periods
     * of time."[11] The Therapeutic Goods Administration in Australia reported that the WHO has only
     * eight records of adverse effects with PSK and none reported for PSP.
     */
    POLYSACCHARIDE("polysaccharide");

    private final String value;

    private static final Map<String, SaccharideTypes> stringToEnum = new HashMap<String, SaccharideTypes>();

    static { // init map from constant name to enum constant
        for (SaccharideTypes en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    /**
     *
     * @param bioType
     * @return boolean
     */
    public boolean equals(SaccharideTypes bioType) {
        return value.equals(bioType.toString());
    }

    /**
     *
     * @param bioType
     * @return boolean
     */
    public boolean equals(String bioType) {
        return value.equals(bioType);
    }

    SaccharideTypes(String value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return value;
    }

    /**
     *
     * @param value
     * @return StructureEntityTypes
     */
    public static SaccharideTypes fromString(String value) {
        return stringToEnum.get(value);
    }
}