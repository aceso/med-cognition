/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.*;
import org.atgc.bio.repository.TemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

/**
 * Journal object. For instance Nature. It has an ISSN date.
 * This was discovered while processing a PubMed article.
 *
 * The pubmed schema is described here:
 * http://www.nlm.nih.gov/bsd/licensee/elements_descriptions.html
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.JOURNAL)
public class Journal {

    protected static Logger log = LogManager.getLogger(Journal.class);

    /**
     * This is required for the graph database.
     */
    @GraphId
    private Long id;

    /**
     * <ISSN> (International Standard Serial Number) is always an eight-character
     * value that uniquely identifies the cited journal. It is nine characters
     * long in the hyphenated form: XXXX-XXXX. It has a single attribute, ISSNType,
     * which indicates which of the ISSNs assigned to the journal is recorded in
     * the citation. Some journals are published online in addition to or instead
     * of in print and a unique ISSN is assigned for each version. For journals
     * published in both media (referred to as hybrid journals), NLM chooses
     * one version to use for MeSH indexing and the ISSN and ISSNType for that
     * version appears in the MEDLINE citation. The three valid values are
     * Electronic, Print, and Undetermined, although Undetermined is not used
     * for MEDLINE/PubMed data.
     *
     * Examples are:
     * < ISSN IssnType ="Print "> 0950-382X </ ISSN >
     * < ISSN IssnType ="Electronic "> 1432-2218 </ ISSN>
     *
     * Some records do not contain an <ISSN> value. See also <NlmUniqueID> ,
     * <JournalIssue> and <ISSNLinking>.
     *
     * Information about journals cited in MEDLINE, including the complete title
     * of the journal, is found in:
     *
     * NLM online catalog available at LocatorPlus (http://locatorplus.gov) and
     * NLM Catalog http://www.ncbi.nlm.nih.gov/entrez/query.fcgi? CMD=search&DB=nlmcatalog).
     * SERFILE, another file that may be leased from NLM (see http://www.nlm.nih.gov/databases/leased.html)
     * PubMed journals files located at http://www.nlm.nih.gov/bsd/serfile_addedinfo.html (contains limited
     * journal information; updated daily)
     * The List of Serials Indexed for Online Users available at http://www.nlm.nih.gov/tsd/serials/lsiou.html
     * and the list of journals indexed in MEDLINE available at http://www.nlm.nih.gov/pubs/factsheets/j_sel_faq.html#a15.
     */
    @UniquelyIndexed (indexName=IndexNames.JOURNAL_ISSN)
    @Taxonomy(rbClass=TaxonomyTypes.ISSN, rbField=BioFields.ISSN)
    private String issn;

    @Indexed(indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * The full journal title (taken from NLM cataloging data following NLM rules
     * for how to compile a serial name) is exported in this element. Some
     * characters that are not part of the NLM MEDLINE/PubMed Character Set
     * reside in a relatively small number of full journal titles. The NLM journal
     * title abbreviation is exported in the <MedlineTA> element.
     * Examples are:
     *
     * <Title>Molecular microbiology</Title>
     * <Title>American journal of physiology. Cell physiology</Title>
     *
     * For instance Nature
     */
    @Indexed (indexName=IndexNames.JOURNAL_TITLE)
    @Taxonomy (rbClass=TaxonomyTypes.JOURNAL_TITLE, rbField=BioFields.TITLE)
    @Visual
    private String title;

    @NonIndexed
    private String issnType;

    public String getIssnType() {
        return issnType;
    }

    public void setIssnType(String issnType) {
        this.issnType = issnType;
    }

    /**
     * This element is used to export the NLM version of the journal title ISO
     * Abbreviation. ISO Abbreviations are constructed at NLM to assist NCBI in
     * linking from GenBank to PubMed. Those created prior to 2007 did not
     * necessarily conform to the ISO standard. ISO Abbreviations created after
     * this date are identical to the NLM title abbreviations. In November 2009
     * ISO abbreviations were retrospectively assigned for every record from a
     * journal that was currently indexed for MEDLINE at that time.
     *
     * Examples are:
     * <ISOAbbreviation>Mol. Microbiol.</ISOAbbreviation>
     * <ISOAbbreviation>Am. J. Physiol., Cell Physiol.</ISOAbbreviation>
     * <ISOAbbreviation>Environ Monit Assess</ISOAbbreviation>
     *
     * For instance Nature
     */
    @Indexed (indexName=IndexNames.ISO_ABBREVIATION)
    @Taxonomy (rbClass=TaxonomyTypes.ISO_ABBREVIATION, rbField=BioFields.ISO_ABBREVIATION)
    private String isoAbbreviation;

    /**
     * The full journal title (taken from NLM cataloging data following NLM rules
     * for how to compile a serial name) is exported in this element. Some
     * characters that are not part of the NLM MEDLINE/PubMed Character Set
     * reside in a relatively small number of full journal titles. The NLM journal
     * title abbreviation is exported in the <MedlineTA> element.
     * Examples are:
     *
     * <Title>Molecular microbiology</Title>
     * <Title>American journal of physiology. Cell physiology</Title>
     *
     * For instance Nature
     *
     * @return String
     */
    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    /**
     * This element is used to export the NLM version of the journal title ISO
     * Abbreviation. ISO Abbreviations are constructed at NLM to assist NCBI in
     * linking from GenBank to PubMed. Those created prior to 2007 did not
     * necessarily conform to the ISO standard. ISO Abbreviations created after
     * this date are identical to the NLM title abbreviations. In November 2009
     * ISO abbreviations were retrospectively assigned for every record from a
     * journal that was currently indexed for MEDLINE at that time.
     *
     * Examples are:
     * <ISOAbbreviation>Mol. Microbiol.</ISOAbbreviation>
     * <ISOAbbreviation>Am. J. Physiol., Cell Physiol.</ISOAbbreviation>
     * <ISOAbbreviation>Environ Monit Assess</ISOAbbreviation>
     *
     * For instance Nature
     * @return String
     */
    public String getIsoAbbreviation() {
        return isoAbbreviation;
    }

    public void setIsoAbbreviation(String isoAbbreviation) {
        this.isoAbbreviation = isoAbbreviation;
    }

    public String getNodeType() {
        return nodeType;
    }

    public void setNodeType(String nodeType) {
        this.nodeType = nodeType;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    @Override
    public String toString() {
        return nodeType + "-" + issn;
    }

    public Journal() {};

    public Journal(String issn) {
       this.issn = issn;
       this.message = nodeType + "-" + issn;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    /**
     * <ISSN> (International Standard Serial Number) is always an eight-character
     * value that uniquely identifies the cited journal. It is nine characters
     * long in the hyphenated form: XXXX-XXXX. It has a single attribute,
     * ISSNType, which indicates which of the ISSNs assigned to the journal is
     * recorded in the citation. Some journals are published online in addition
     * to or instead of in print and a unique ISSN is assigned for each version.
     * For journals published in both media (referred to as hybrid journals), NLM
     * chooses one version to use for MeSH indexing and the ISSN and ISSNType for
     * that version appears in the MEDLINE citation. The three valid values are
     * Electronic, Print, and Undetermined, although Undetermined is not used
     * for MEDLINE/PubMed data.
     *
     * Examples are:
     *
     * < ISSN IssnType ="Print "> 0950-382X </ ISSN >
     * < ISSN IssnType ="Electronic "> 1432-2218 </ ISSN>
     *
     * Some records do not contain an <ISSN> value. See also <NlmUniqueID> ,
     * <JournalIssue> and <ISSNLinking>.
     *
     * Information about journals cited in MEDLINE, including the complete title of
     * the journal, is found in:
     *
     * NLM online catalog available at LocatorPlus (http://locatorplus.gov) and NLM Catalog http://www.ncbi.nlm.nih.gov/entrez/query.fcgi? CMD=search&DB=nlmcatalog).
     * SERFILE, another file that may be leased from NLM (see http://www.nlm.nih.gov/databases/leased.html)
     * PubMed journals files located at http://www.nlm.nih.gov/bsd/serfile_addedinfo.html (contains limited journal information; updated daily)
     * The List of Serials Indexed for Online Users available at http://www.nlm.nih.gov/tsd/serials/lsiou.html and the list of journals indexed in MEDLINE available at http://www.nlm.nih.gov/pubs/factsheets/j_sel_faq.html#a15.

     * @return String
     */
    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }
}
