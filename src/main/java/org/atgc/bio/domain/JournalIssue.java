/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.repository.TemplateUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.atgc.bio.meta.*;
import org.neo4j.graphdb.Direction;

/**
 * This element contains information about the specific issue in which the
 * article cited resides. It has a single attribute, CitedMedium, which indicates
 * whether a citation is processed/indexed at NLM from the online or the print
 * version of the journal. The two valid attribute values are Internet and Print.
 *
 * Examples are:
 * < JournalIssue CitedMedium ="Internet ">
 * < JournalIssue CitedMedium ="Print ">
 * JournalIssue object. A specific issue of a journal. For instance
 * the May 2007 issue.
 *
 * The pubmed schema is described here:
 * http://www.nlm.nih.gov/bsd/licensee/elements_descriptions.html
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.JOURNAL_ISSUE)
@UniqueCompoundIndex(indexName=IndexNames.JOURNAL_ISSUE, field1= BioFields.ISSN, field2=BioFields.VOLUME, field3=BioFields.ISSUE)
public class JournalIssue {

    protected static Logger log = LogManager.getLogger(JournalIssue.class);

    /**
     * This is required for the graph database.
     */
    @GraphId
    private Long id;

    /**
     * For instance "electronic"
     */
    @Indexed(indexName=IndexNames.CITED_MEDIUM)
    @Taxonomy(rbClass=TaxonomyTypes.CITED_MEDIUM, rbField=BioFields.CITED_MEDIUM)
    private String citedMedium;

    /**
     * Since the journal issue does not have a unique identifier, we will
     * use the ISSN from the Journal BioEntity as a PartKey.
     */
    @Taxonomy (rbClass=TaxonomyTypes.ISSN, rbField=BioFields.ISSN)
    @PartKey
    private String issn;

    /**
     * The volume number of the journal in which the article was published is recorded here.
     * Volume for instance 438
     */
    @Taxonomy (rbClass=TaxonomyTypes.VOLUME, rbField=BioFields.VOLUME)
    @PartKey
    private String volume;

    /**
     * <Issue> identifies the issue, part or supplement of the journal in which the
     * article was published.
     * Examples are:
     * <Issue>Pt 1</Issue>
     * <Issue>Pt B</Issue>
     * <Issue>3 Spec No</Issue>
     * <Issue>7 Pt 1</Issue>
     * <Issue>First Half</Issue>
     * <Issue>3 Suppl</Issue>
     * <Issue>3 Suppl 1</Issue>
     * Additional information/background:
     * For records in the OLDMEDLINE subset (<CitationSubset> = OM): Some
     * records contain <Issue> but lack <Volume>; some records contain <Volume>
     * but lack <Issue>; and some records contain Volume and Issue data in the
     * Volume element.
     * Issue for instance 7069
     */
    @Taxonomy (rbClass=TaxonomyTypes.ISSUE, rbField=BioFields.ISSUE)
    @PartKey
    @Visual
    private String issue;

    /**
     * MM-DD-YYYY
     * <PubDate> contains the full date on which the issue of the journal was
     * published. The standardized format consists of elements for a 4-digit year,
     * a 3-character abbreviated month, and a 1 or 2-digit day. Every record does
     * not contain all of these elements; the data are taken as they are published
     * in the journal issue, with minor alterations by NLM such as abbreviating months.
     * Examples are:
     * <PubDate>
     * <Year>2001</Year>
     * <Month>Apr</Month>
     * <Day>15</Day>
     * </PubDate>
     * <PubDate>
     * <Year>2001</Year>
     * <Month>Apr</Month>
     * </PubDate>
     * <PubDate>
     * <Year>2001</Year>
     * </PubDate>
     *
     * The date of publication for the great majority of records will reside in
     * the separate date-related elements within <PubDate> as shown above and
     * in these cases the record will not contain <MedlineDate>. The date of
     * publication of the article will be found in <MedlineDate> when parsing
     * for the separate fields is not possible; i.e., cases where dates do not fit
     * the Year, Month, or Day pattern.
     *
     * Examples are:
     * <PubDate>
     * <MedlineDate>1998 Dec-1999 Jan</MedlineDate>
     * </PubDate>
     * <PubDate>
     * <MedlineDate>2000 Spring</MedlineDate>
     * </PubDate>
     * <PubDate>
     * <MedlineDate>2000 Spring-Summer</MedlineDate>
     * </PubDate>
     * <PubDate>
     * <MedlineDate>2000 Nov-Dec</MedlineDate>
     * </PubDate>
     * <PubDate>
     * <MedlineDate>2000 Dec 23- 30</MedlineDate>
     * </PubDate>
     *
     * The PubDate on citations to versions of the same article will be identical.
     */
    @Indexed (indexName=IndexNames.JOURNAL_ISSUE_PUBDATE)
    private String pubDate;

    public String getPubDate() {
        return pubDate;
    }

    public void setPubDate(String pubDate) {
        this.pubDate = pubDate;
    }

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.ISSUE_OF_JOURNAL, elementClass = BioRelation.class)
    private BioRelation issueOfJournal;

    public void setJournal(Journal journal) {
        issueOfJournal = new BioRelation();
        issueOfJournal.setStartNode(this);
        issueOfJournal.setEndNode(journal);
        issueOfJournal.setRelType(BioRelTypes.ISSUE_OF_JOURNAL);
        issueOfJournal.setMessage(BioRelTypes.ISSUE_OF_JOURNAL.toString());
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
        return nodeType + "-" + issn + "-" + volume + "-" + issue;
    }

    public JournalIssue() {};

    public JournalIssue(String issn, String volume, String issue) {
       this.issn = issn;
       this.volume = volume;
       this.issue = issue;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getIssn() {
        return issn;
    }

    public void setIssn(String issn) {
        this.issn = issn;
    }

    public String getCitedMedium() {
        return citedMedium;
    }

    public void setCitedMedium(String citedMedium) {
        this.citedMedium = citedMedium;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getIssue() {
        return issue;
    }

    public void setIssue(String issue) {
        this.issue = issue;
    }
}
