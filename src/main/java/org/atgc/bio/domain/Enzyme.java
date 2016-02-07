/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;

/**
 * Database: ftp://ftp.expasy.org/databases/enzyme/
 * Readme: ftp://ftp.expasy.org/databases/enzyme/enzuser.txt
 *
    * http://www.enzyme-database.org/class.php
    *
    *  [+subclass]. [+sub-subclass]. [+serial]
    *
    *   EC 1	 [+] 	Oxidoreductases
    *   EC 2	 [+] 	Transferases
    *   EC 3	 [+] 	Hydrolases
    *   EC 4	 [+] 	Lyases
    *  EC 5	 [+] 	Isomerases
    *  EC 6	 [+] 	Ligases
    *

 *
 * @author jtanisha-ee
 */

@BioEntity(bioType = BioTypes.ENZYME)
public class Enzyme {

    protected static Logger log = LogManager.getLogger(Enzyme.class);

    @GraphId
    private Long id;

    /**
     * EC: 3.1.1.1
     */
    @UniquelyIndexed(indexName=IndexNames.ENZYME_ID)
    @Taxonomy(rbClass=TaxonomyTypes.ENZYME_ID, rbField=BioFields.ENZYME_ID)
    private String enzymeId;

    /**
     * NAD<small><sup>+</sup></small>
     */
    @FullTextIndexed (indexName=IndexNames.ENZYME_ACCEPTED_NAMES)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_ACCEPTED_NAMES, rbField=BioFields.ENZYME_ACCEPTED_NAMES)
    private String enzymeAcceptedNames;

    /**
     * ADP-ribosyltransferase; mono(ADP-ribosyl)transferase; NAD<small><sup>+</sup>
     */
    @FullTextIndexed (indexName=IndexNames.ENZYME_OTHER_NAMES)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_OTHER_NAMES, rbField=BioFields.ENZYME_OTHER_NAMES)
    private String enzymeOtherNames;

    /**
     * sys_name
     * NAD<small><sup>+</sup></small>:protein-<small>L</small>-arginine ADP-
     * <small>D</small>-ribosyltransferase
     */
    @FullTextIndexed (indexName=IndexNames.ENZYME_SYSTEMATIC_NAMES)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_SYSTEMATIC_NAMES, rbField=BioFields.ENZYME_SYSTEMATIC_NAMES)
    private String enzymeSystematicNames;

    /**
     * comments
     * Protein mono-ADP-ribosylation is a reversible post-translational modification
     * that plays a role in the regulation of cellular activities [4].
     * Arginine residues in proteins act as acceptors.
     */
    @FullTextIndexed (indexName=IndexNames.ENZYME_COMMENTS)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_COMMENTS, rbField=BioFields.ENZYME_COMMENTS)
    private String enzymeComments;

    
    @FullTextIndexed (indexName=IndexNames.ENZYME_COFACTOR)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_COFACTOR, rbField=BioFields.ENZYME_COFACTOR)
    private String enzymeCofactor;
    
    /**
    *  UDP-<small>D</small>-xylose + dolichyl phosphate = UDP + dolichyl
    *        <small>D</small>-xylosyl phosphate
    */
    @FullTextIndexed (indexName=IndexNames.ENZYME_REACTION)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_REACTION, rbField=BioFields.ENZYME_REACTION)
    private String enzymeReaction;

    /**
     * http links
     * <a href=\"http://www.brenda-enzymes.info/php/result_flat.php3?ecno=2.4.2.31\" target=\"new\">BRENDA</a>
     */
    @FullTextIndexed (indexName=IndexNames.ENZYME_REF_LINKS)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_REF_LINKS, rbField=BioFields.ENZYME_REF_LINKS)
    private String enzymeRefLinks;

    /**
     * glossary
     */
    @FullTextIndexed (indexName=IndexNames.ENZYME_GLOSSARY)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_GLOSSARY, rbField=BioFields.ENZYME_GLOSSARY)
    private String enzymeGlossary;

    /**
     * last_change : date it was changed
     * 2012-02-10 23:35:07
     */
    @Indexed(indexName=IndexNames.ENZYME_LAST_CHANGE)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_LAST_CHANGE, rbField=BioFields.ENZYME_LAST_CHANGE)
    private String enzymeLastChange;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    @Taxonomy (rbClass=TaxonomyTypes.NODE_TYPE, rbField=BioFields.NODE_TYPE)
    private String nodeType = BioTypes.ENZYME.toString();

    /**
     *
     */
    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    public String message;

    @Visual
    @Indexed (indexName=IndexNames.ENZYME_NAME)
    @Taxonomy (rbClass=TaxonomyTypes.ENZYME_NAME, rbField=BioFields.ENZYME_NAME)
    private String enzymeName;



    /*
    @RelatedToVia(direction=Direction.OUTGOING, elementClass = BioRelation.class)
    private Collection<BioRelation> geneOntologyRelationships = new HashSet<BioRelation>();
    */

    /**
     *
     * @return Long
     */


    public Long getId() {
        return id;
    }

    /**
     *
     * @param id
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeId() {
        return enzymeId;
    }

    /**
     *
     * @param id
     */
    public void setEnzymeId(String id) {
        this.enzymeId = id;
    }

    /**
     *
     * @return String
     */
    public String getNodeType() {
        return nodeType;
    }

    /**
     *
     * @param nodeType
     */
    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    /**
     *
     * @return String
     */
    public String getMessage() {
        return message;
    }

    /**
     *
     * @param message
     */
    public void setMessage(String message) {
        this.message = message;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeName() {
        return enzymeName;
    }

    /**
     *
     * @param name
     */
    public void setEnzymeName(String name) {
        this.enzymeName = name;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeAcceptedNames() {
        return enzymeAcceptedNames;
    }

    /**
     *
     * @param enzymeAcceptedNames
     */
    public void setEnzymeAcceptedNames(String enzymeAcceptedNames) {
        this.enzymeAcceptedNames = enzymeAcceptedNames;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeComments() {
        return enzymeComments;
    }

    /**
     *
     * @param enzymeComments
     */
    public void setEnzymeComments(String enzymeComments) {
        this.enzymeComments = enzymeComments;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeGlossary() {
        return enzymeGlossary;
    }

    /**
     *
     * @param enzymeGlossary
     */
    public void setEnzymeGlossary(String enzymeGlossary) {
        this.enzymeGlossary = enzymeGlossary;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeLastChange() {
        return enzymeLastChange;
    }

    /**
     *
     * @param enzymeLastChange
     */
    public void setEnzymeLastChange(String enzymeLastChange) {
        log.info("last change " + enzymeLastChange);
        this.enzymeLastChange = enzymeLastChange;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeOtherNames() {
        return enzymeOtherNames;
    }

    /**
     *
     * @param enzymeOtherNames
     */
    public void setEnzymeOtherNames(String enzymeOtherNames) {
        this.enzymeOtherNames = enzymeOtherNames;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeReaction() {
        return enzymeReaction;
    }

    /**
     *
     * @param enzymeReaction
     */
    public void setEnzymeReaction(String enzymeReaction) {
        this.enzymeReaction = enzymeReaction;
    }
    
    public String getEnzymeCofactor() {
        return enzymeCofactor;
    }

    public void setEnzymeCofactor(String enzymeCofactor) {
        this.enzymeCofactor = enzymeCofactor;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeRefLinks() {
        return enzymeRefLinks;
    }

    /**
     *
     * @param enzymeRefLinks
     */
    public void setEnzymeRefLinks(String enzymeRefLinks) {
        this.enzymeRefLinks = enzymeRefLinks;
    }

    /**
     *
     * @return String
     */
    public String getEnzymeSystematicNames() {
        return enzymeSystematicNames;
    }

    /**
     *
     * @param enzymeSystematicNames
     */
    public void setEnzymeSystematicNames(String enzymeSystematicNames) {
        this.enzymeSystematicNames = enzymeSystematicNames;
    }

    @Override
    public int hashCode() {
    return (id == null) ? 0 : id.hashCode();
    }

    @Override
    public String toString() {
        return (nodeType + "-" + enzymeId);
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Enzyme other = (Enzyme) obj;
        if ((this.enzymeId == null) ? (other.enzymeId != null) : !this.enzymeId.equals(other.enzymeId)) {
            return false;
        }
        if ((this.nodeType == null) ? (other.nodeType != null) : !this.nodeType.equals(other.nodeType)) {
            return false;
        }
        return true;
    }


}

