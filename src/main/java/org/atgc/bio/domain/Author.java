/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.BioEntity;
import org.atgc.bio.meta.GraphId;
import org.atgc.bio.meta.Indexed;
import org.atgc.bio.meta.NodeLabel;
import org.atgc.bio.meta.PartKey;
import org.atgc.bio.meta.Taxonomy;
import org.atgc.bio.meta.UniqueCompoundIndex;
import org.atgc.bio.meta.Visual;
import org.atgc.bio.repository.TemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Author as found in PubMed, Pdb and other places. The author can be of several
 * types, including Researcher, Statistician, Principal Investigator,
 * Clinical Researcher, Doctor, Scientist. Currently, we don't have
 * sub-dimensions for these, as we don't need them.
 *
 * <p>
 * There could be potential duplicates as two researchers may have the same identical
 * foreName, initial and lastName. The plan is to develop a heuristic algorithm to
 * distinguish authors by context. The issue is that literature and curated data
 * has not standardized on author ids. We are waiting for pubmed to support
 * something like <a href="http://orcid.org/">Orcid</a>
 *
 * <p>
 * The pubmed schema is described here:
 *
 * <a href="http://www.nlm.nih.gov/bsd/licensee/elements_descriptions.html">PubMed Schema</a>
 *
 * @author jtanisha-ee
 */
@UniqueCompoundIndex(indexName=IndexNames.AUTHOR_NAME, field1= BioFields.FORE_NAME, field2=BioFields.INITIALS, field3=BioFields.LAST_NAME)
@BioEntity(bioType = BioTypes.AUTHOR)
public class Author {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(Author.class);

    @GraphId
    private Long id;

    /**
     * The attribute ValidYN is used on each Author occurrence to indicate the true
     * spelling of the name (some published author names are subsequently corrected
     * by the publishers and NLM retains both versions in the MEDLINE/PubMed record).
     * ValidYN=Y (present for most author names) indicates the spelling of the name
     * is correct; ValidYN=N (present for a small number of author names) indicates
     * the spelling of the name is not correct, per publisher's erratum published
     * in the journal.
     */
    @Indexed(indexName=IndexNames.AUTHOR_VALIDYN)
    @Taxonomy(rbClass=TaxonomyTypes.AUTHOR_VALIDYN, rbField=BioFields.VALIDYN)
    private String validYN;

    /**
     * contains the surname
     */
    @Taxonomy (rbClass=TaxonomyTypes.AUTHOR_LAST_NAME, rbField=BioFields.LAST_NAME)
    @PartKey
    private String lastName;

    /**
     * first name
     * contains the remainder of name except for suffix
     */
    @Taxonomy (rbClass=TaxonomyTypes.AUTHOR_FORE_NAME, rbField=BioFields.FORE_NAME)
    @PartKey
    @Visual
    private String foreName;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

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
    public String getValidYN() {
        return validYN;
    }

    /**
     *
     * @param validYN
     */
    public void setValidYN(String validYN) {
        this.validYN = validYN;
    }

    /**
     *
     * @return String
     */
    public String getLastName() {
        return lastName;
    }

    /**
     *
     * @param lastName
     */
    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    /**
     *
     * @return String
     */
    public String getForeName() {
        return foreName;
    }

    /**
     *
     * @param foreName
     */
    public void setForeName(String foreName) {
        this.foreName = foreName;
    }

    /**
     *
     * @return String
     */
    public String getInitials() {
        return initials;
    }

    /**
     *
     * @param initials
     */
    public void setInitials(String initials) {
        this.initials = initials;
    }

    /**
     *  contains up to two initials
     */
    @PartKey
    private String initials;
}