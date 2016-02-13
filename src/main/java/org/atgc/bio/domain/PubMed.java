/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.domain;

import org.atgc.bio.BioFields;
import org.atgc.bio.meta.*;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.TemplateUtils;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Direction;

/**
 * PubMed object. For now there is not much here. Created this as a
 * placeholder. We will add more fields when required.
 *
 * The pubmed schema is described here:
 * http://www.nlm.nih.gov/bsd/licensee/elements_descriptions.html
 *
 * @author jtanisha-ee
 */
@BioEntity(bioType = BioTypes.PUBMED)
public class PubMed {

    /**
     *
     */
    protected static Logger log = LogManager.getLogger(PubMed.class);

    /**
     * This is required for the graph database.
     */
    @GraphId
    private Long id;

    /**
     * PubMed Id or accession number.
     */
    @UniquelyIndexed (indexName=IndexNames.PUBMED_ID)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_ID, rbField=BioFields.PUBMED_ID)
    private String pubMedId;

    @Indexed (indexName=IndexNames.NODE_TYPE)
    private String nodeType = TemplateUtils.extractBioType(this).toString();
    //private String nodeType = BioTypes.PUBMED.toString();

    @NodeLabel
    @Indexed (indexName=IndexNames.PUBMED_MESSAGE)
    @Taxonomy (rbClass=TaxonomyTypes.MESSAGE, rbField=BioFields.MESSAGE)
    private String message;

    /**
     * The owner can be NLM
     */
    @Indexed (indexName=IndexNames.PUBMED_OWNER)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_OWNER, rbField=BioFields.OWNER)
    private String owner;

    /**
     * status can be MEDLINE
     */
    @Indexed (indexName=IndexNames.PUBMED_STATUS)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_STATUS, rbField=BioFields.STATUS)
    private String status;

    @Indexed (indexName=IndexNames.PUBMED_VERSION)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_VERSION, rbField=BioFields.VERSION)
    private String version;

    @Indexed (indexName=IndexNames.PUBMED_CREATED_DATE)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_CREATED_DATE, rbField=BioFields.CREATED_DATE)
    private String createdDate;

    @Indexed (indexName=IndexNames.PUBMED_COMPLETED_DATE)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_COMPLETED_DATE, rbField=BioFields.COMPLETED_DATE)
    private String completedDate;

    @RelatedToVia(direction=Direction.BOTH, elementClass=BioRelation.class)
    private Collection<BioRelation> authorRelations;

    /**
     * Cache for authors
     */
    @NonIndexed
    private Collection<Author> authors = new HashSet<Author>();

    /**
     * Cache for relations
     */
    @NonIndexed
    private Collection<BioRelation> relations = new HashSet<BioRelation>();

    @NonIndexed
    private BioRelation journalIssueRelation;

    @Indexed (indexName=IndexNames.PUBMED_ARTICLE_TITLE)
    @Taxonomy (rbClass=TaxonomyTypes.PUBMED_ARTICLE_TITLE, rbField=BioFields.ARTICLE_TITLE)
    private String articleTitle;

    @NonIndexed
    private String pubModel;

    @NonIndexed
    private String affiliation;

    @NonIndexed
    private String language;

    @FullTextIndexed (indexName=IndexNames.PUBLICATION_TYPE_LIST)
    @Taxonomy (rbClass=TaxonomyTypes.PUBLICATION_TYPE_LIST, rbField=BioFields.PUBLICATION_TYPE_LIST)
    private String publicationTypeList;

    @NonIndexed
    private String country;

    /**
     *
     * @param country
     */
    public void setCountry(String country) {
        this.country = country;
    }

    /**
     * <Country> carries the place of publication of the journal. When used
     * in <GrantList> this element contains the country of the granting agency
     * identified in the cited article. Valid values are those country names
     * found in the Z category of the Medical Subject Headings (MeSH) Tree
     * Structures that may be downloaded from http://www.nlm.nih.gov/mesh.
     * <Country> values may appear in all upper case or in mixed case. On
     * older records, in cases where the place of publication is unknown,
     * the <Country> value is Unknown.
     *
     * Examples are:
     *
     * <Country>United States</Country>
     * <Country>UNITED STATES</Country>
     * <Country>FRANCE</Country>
     * <Country>Unknown</Country>
     *
     * Country data are not maintained when names may change over time.
     * These data are where the journal is published, not where the research
     * was conducted.
     *
     * @return String
     */
    public String getCountry() {
        return country;
    }

    @NonIndexed
    private String medlineTA;

    /**
     *
     * @param medlineTA
     */
    public void setMedlineTA(String medlineTA) {
        this.medlineTA = medlineTA;
    }

    /**
     * This element contains the standard abbreviation for the title of the journal
     * in which an article appeared. See the NLM Fact Sheet "Construction of
     * National Library of Medicine Title Abbreviations" at
     * http://www.nlm.nih.gov/pubs/factsheets/constructitle.html which discusses
     * the rules currently used by the National Library of Medicine (NLM) to
     * construct title abbreviations for journals cited in MEDLINE. See <Title>
     * for the full journal name.
     *
     * Examples are:
     *
     * <MedlineTA>JAMA</MedlineTA>
     * <MedlineTA>J Pediatr</MedlineTA>
     * <MedlineTA>J Comp Physiol B</MedlineTA>
     * <MedlineTA>Ann Biol Clin (Paris)</MedlineTA>
     *
     * @return String
     */
    public String getMedLineTA() {
        return medlineTA;
    }

    @NonIndexed
    private String nlmUniqueId;

    /**
     *
     * @param nlmUniqueId
     */
    public void setNlmUniqueId(String nlmUniqueId) {
        this.nlmUniqueId = nlmUniqueId;
    }

    /**
     * <NlmUniqueID> may be used to locate the complete serial record for the
     * journal cited in MEDLINE records. The element's value is the accession number
     * for the journal's record assigned in the NLM Integrated Library System,
     * LocatorPlus at http://locatorplus.gov/. A <NLMUniqueID> may appear as seven,
     * eight or nine charcaters and is the preferred element to use when looking
     * for the serial record for the journal in which the article was published.
     *
     * Examples are:
     *
     * The LocatorPlus accession number for the New England Journal of Medicine is
     * 0255562 and the MEDLINE records contain:
     *
     * <NlmUniqueID>0255562</NlmUniqueID>.
     *
     * The LocatorPlus accession number for the Japanese Journal of Infectious Diseases
     * is 100893704 and the MEDLINE records contain:
     *
     * <NlmUniqueID>100893704</NlmUniqueID>.
     *
     * The LocatorPlus accession number for Sicilia Sanitaria is 20740130R and the
     * MEDLINE records contain: <NlmUniqueID>20740130R</NlmUniqueID>.<NlmUniqueID> may
     * be used to locate the complete serial record for the journal cited in MEDLINE
     * records. The element's value is the accession number for the journal's record
     * assigned in the NLM Integrated Library System, LocatorPlus at http://locatorplus.
     * gov/. A <NLMUniqueID> may appear as seven, eight or nine charcaters and is the
     * preferred element to use when looking for the serial record for the journal in
     * which the article was published.
     *
     * Examples are:
     *
     * The LocatorPlus accession number for the New England Journal of Medicine is
     * 0255562 and the MEDLINE records contain:
     *
     * <NlmUniqueID>0255562</NlmUniqueID>.
     *
     * The LocatorPlus accession number for the Japanese Journal of Infectious Diseases
     * is 100893704 and the MEDLINE records contain:
     *
     * <NlmUniqueID>100893704</NlmUniqueID>.
     *
     * The LocatorPlus accession number for Sicilia Sanitaria is 20740130R and the
     * MEDLINE records contain: <NlmUniqueID>20740130R</NlmUniqueID>.
     *
     * @return String
     */
    public String getNlmUniqueId() {
        return nlmUniqueId;
    }

    @NonIndexed
    private String issnLinking;

    /**
     *
     * @param issnLinking
     */
    public void setIssnLinking(String issnLinking) {
        this.issnLinking = issnLinking;
    }

    /**
     * The ISSNLinking element contains the ISSN designated by the ISSN Network to
     * enable co-location or linking among the different media versions of a continuing
     * resource (separate ISSN???s are assigned for each media type in which a resource
     * is issued). The ISSNLinking element designates the single unique ISSN for the
     * resource, regardless of its medium. The element was defined in the 2008 DTD but
     * was first added to records in the 2010 MEDLINE/PubMed baseline files. A
     * relatively small number of records lack this element because the ISSN Network has
     * not assigned some very old or very new serials a linking ISSN.
     *
     * An example is:
     *
     * <ISSNLinking>0108-7673</ISSNLinking>
     *
     * @return String
     */
    public String getIssnLinking() {
        return issnLinking;
    }

    @FullTextIndexed (indexName=IndexNames.PUBMED_MAJOR_TOPICS)
    @Taxonomy (rbClass=TaxonomyTypes.MAJOR_TOPICS, rbField=BioFields.MAJOR_TOPICS)
    private String majorTopics;

    /**
     *
     * @param majorTopics
     */
    public void setMajorTopics(String majorTopics) {
        this.majorTopics = majorTopics;
    }

    /**
     * The MajorTopic attribute for <DescriptorName> is set to Y (for YES) when
     * the MeSH Heading alone is a central concept of the article (without a
     * QualifierName).
     *
     * @return String
     */
    public String getMajorTopics() {
        return majorTopics;
    }

    @FullTextIndexed (indexName=IndexNames.PUBMED_NOT_MAJOR_TOPICS)
    @Taxonomy (rbClass=TaxonomyTypes.NOT_MAJOR_TOPICS, rbField=BioFields.NOT_MAJOR_TOPICS)
    private String notMajorTopics;

    /**
     *
     * @param notMajorTopics
     */
    public void setNotMajorTopics(String notMajorTopics) {
        this.notMajorTopics = notMajorTopics;
    }

    /**
     *
     * @return String
     */
    public String getNotMajorTopics() {
        return notMajorTopics;
    }

    @FullTextIndexed (indexName=IndexNames.PUBMED_ARTICLE_ABSTRACT)
    @Taxonomy (rbClass=TaxonomyTypes.ARTICLE_ABSTRACT, rbField=BioFields.ARTICLE_ABSTRACT)
    private String articleAbstract;

    /**
     *
     * @param articleAbstract
     */
    public void setArticleAbstract(String articleAbstract) {
        this.articleAbstract = articleAbstract;
    }

    /**
     * English-language abstracts are taken directly from the published article.
     * If the article does not have a published abstract, the National Library
     * of Medicine does not create one, thus the record lacks the <Abstract>
     * and <AbstractText> elements. However, in the absence of a formally
     * labeled abstract in the published article, text from a substantive
     * "summary", "summary and conclusions" or "conclusions and summary" may be used.
     * Publishers have given the National Library of Medicine permission to use abstracts for which they claim copyright; NLM does not hold copyright on the abstracts in MEDLINE. Licensees should obtain an opinion from their legal counsel for any use they plan for the abstracts in the database.
     *
     * @return String
     */
    public String getArticleAbstract() {
        return articleAbstract;
    }

    @NonIndexed
    private String citationSubset;

    /**
     *
     * @param citationSubset
     */
    public void setCitationSubset(String citationSubset) {
        this.citationSubset = citationSubset;
    }

    /**
     * <CitationSubset> identifies the subset for which MEDLINE records from certain
     * journal lists or records on specialized topics were created. Some of these
     * values are found on extremely small numbers of records. Citations may contain
     * more than one occurrence of <CitationSubset>.
     *
     * The value is true at the time the record was created; if the status of a journal
     * changes, the value on the MEDLINE record does not change.
     *
     * The values and their definitions for <CitationSubset> are as follows. Note that
     * several are closed subsets no longer being assigned.
     *
     * AIM = citations from Abridged Index Medicus journals, a list of about 120 core
     * clinical, English language journals.
     * B = citations from non-Index Medicus journals in the field of biotechnology (not
     * currently used).
     * C = citations from non-Index Medicus journals in the field of communication
     * disorders (not currently used).
     * D = citations from dental journals.
     * E = citations in the field of bioethics. (includes records from the former
     * BIOETHICS database)
     * F = older citations from one journal prior to its selection for Index Medicus; used to augment the database for NLM International MEDLARS Centers (not currently used
     * H = citations from non-Index Medicus journals in the field of health
     * administration. (includes records from the former HealthSTAR database)
     * IM = citations from Index Medicus journals.
     * J = citations in the field of population information. (not currently used; on
     * records from the former POPLINE?? database)
     * K = citations from non-Index Medicus journals relating to consumer health.
     * N = citations from nursing journals.
     * OM = pre-1966 citations from the older print indices of the Cumulated Index
     * Medicus (CIM) and the Current List of Medical Literature (CLML) (see more
     * information about OLDMEDLINE). See Additional information/background: notations
     * for specialized treatment of selected elements for OLDMEDLINE subset records, e.
     * g., <DateCreated> and <DateCompleted>.
     * Q = citations in the field of the history of medicine. (includes records from
     * the former HISTLINE?? database)
     * QIS = citations from non-Index Medicus journals in the field of the history of
     * medicine. (For NLM use effective in late 2006 because they require special
     * handling at NLM; not a subset of Q; some journals previously designated as Q are
     * now QIS.)
     * QO is subset of Q - indicates older history of medicine journal citations that
     * were created before the former HISTLINE file was converted to a MEDLINE-like
     * format. (For NLM use because they require special handling at NLM).
     * R = citations from non-Index Medicus journals in the field of population and
     * reproduction (not currently used).
     * S = citations in the field of space life sciences. (includes records from the
     * former SPACELINE??? database)
     * T = citations from non-Index Medicus journals in the field of health technology
     * assessment. (includes records from the former HealthSTAR database)
     * X = citations in the field of AIDS/HIV. (includes records from the former
     * AIDSLINE?? database)
     *
     * Examples are:
     *
     * <CitationSubset>AIM</CitationSubset>
     * <CitationSubset>IM</CitationSubset>
     *
     * @return String
     */
    public String getCitationSubset() {
        return citationSubset;
    }

    /**
     *
     * @param publicationTypeList
     */
    public void setPublicationTypeList(String publicationTypeList) {
       this.publicationTypeList = publicationTypeList;
    }

    /**
     * This element is used to identify the type of article indexed for MEDLINE;
     * it characterizes the nature of the information or the manner in which it
     * is conveyed (e.g., Review, Letter, Retracted Publication, Clinical Conference).
     * Records may contain more than one <Publication Type> that are listed in
     * alphabetical order. <PublicationTypeList> is always complete; there is
     * no attribute to indicate completeness.
     *
     * An example is:
     * <PublicationTypeList>
     * <PublicationType>Journal Article</PublicationType>
     * <PublicationType>Retracted Publication</PublicationType>
     * <PublicationType>Review</PublicationType>
     * <PublicationType>Review, Tutorial</PublicationType>
     * </PublicationTypeList>
     *
     * The <PublicationType> values with their descriptions may be downloaded
     * from http://www.nlm.nih.gov/mesh. The publication type headings contain
     * the value D in the Record Type (RY) field and the value 3 in the
     * DescriptorName Form (DF) field. A simple list of the Publication Types
     * is available at from the PubMed online Help.
     *
     * @return String
     */
    public String getPublicationTypeList() {
        return publicationTypeList;
    }

    /**
     *
     * @param language
     */
    public void setLanguage(String language) {
        this.language = language;
    }

    /**
     * The language in which an article was published is recorded in <Language>.
     * All entries are three letter abbreviations stored in lower case, such as
     * eng, fre, ger, jpn, etc. When a single record contains more than one
     * language value the XML export program extracts the languages in alphabetic
     * order by the 3-letter language value. Some records provided by collaborating
     * data producers may contain the value und to identify articles whose language
     * is undetermined.
     *
     * Examples are:
     * <Language>eng</Language>
     * <Language>rus</Language>
     *
     * A table listing all languages found in MEDLINE is at
     * http://www.nlm.nih.gov/bsd/language_table.html. A chart showing the number
     * of English language MEDLINE articles in various segments of MEDLINE is
     * available at: http://www.nlm.nih.gov/bsd/medline_lang_distr.html.
     *
     * @return String
     */
    public String getLanguage() {
        return language;
    }

    /**
     *
     * @param affiliation
     */
    public void setAffiliation(String affiliation) {
       this.affiliation = affiliation;
    }

    /**
     * <Affiliation> is associated with author names (see <AuthorList>) and
     * investigator names (see <InvestigatorList>).
     *
     * Regarding author affiliations:
     *
     * The affiliation of the first author resides in the separate <Affiliation>
     * element preceding <AuthorList>. Starting in 1988, NLM began to include
     * the address of the first author's affiliation on the record. Originally
     * the address was intended to help differentiate between two authors of
     * the same name, not to provide detailed mailing information. It evolved
     * that the institution, city, and state including zip code for U.S. addresses,
     * and country for foreign countries, were included if provided in the journal;
     * sometimes the street address was also included if provided in the journal.
     * In 1995, NLM began to add the designation USA at the end of <Affiliation>
     * where the first author's affiliation is in the 50 United States or the
     * District of Columbia. Effective January 1, 1996, NLM includes the first
     * author's electronic mail (e-mail) address at the end of <Affiliation>,
     * if present in the journal. Starting in 2003 the complete first author
     * address is entered as it appears in the article with no words omitted.
     * Note that the first author is not necessarily the corresponding or senior
     * author identified in the published article; simply the first name in the
     * published author list is entered.
     *
     * Examples are:
     *
     * <Affiliation>Department of Anesthesiology, University of Virginia Health
     * Sciences Center Charlottesville 22908, USA. med2p@virginia.edu</Affiliation>
     * <Affiliation>Departamento de Farmacologia, Facultad de Medicina, Universidad Complutense de Madrid (UCM), 28040 Madrid, Spain.</Affiliation>
     * <Affiliation>Center for Children With Special Needs, Children's Hospital, and
     * the Department of Pediatrics, University of Washington School of Medicine,
     * 4800 Sand Point Way NE, CM:09, Seattle, WA 98105-0371, USA. jneff@chmc.org</Affiliation>
     * Regarding investigator affiliations (see <InvestigatorList>):
     *
     * The investigator affiliation identifies the organization that the researcher
     * was affiliated with at the time the article was written and as published
     * in the journal. Unlike <Affiliation> associated with Author names, this
     * affiliation generally does not include detailed address information.
     *
     * Examples are:
     * <Affiliation>Marquette U, Milwaukee, WI</Affiliation>
     * <Affiliation>VA Med Ctr, Richmond, VA</Affiliation>
     *
     * @return String
     */
    public String getAffiliation() {
        return affiliation;
    }

    /**
     *
     * @param pubModel
     */
    public void setPubModel(String pubModel) {
        this.pubModel = pubModel;
    }

    /**
     * Is used to identify the medium/media in which the cited article is
     * published. There are five possible values for PubModel:
     * Print | Print-Electronic | Electronic | Electronic-Print | Electronic-eCollection.
     *
     * <Article PubModel="Print"> - the journal is published in print format only
     * <Article PubModel="Print-Electronic"> - the journal is published in both print and electronic format
     * <Article PubModel="Electronic"> - the journal is published in electronic format only
     * <Article PubModel="Electronic-Print"> - the journal is published first in electronic format followed by print (this value is currently used for just one journal, Nucleic Acids Research).
     * <Article PubModel="Electronic-eCollection"> - used for electronic-only journals that publish individual articles first and then later collect them into an ???issue??? date that is typically called an eCollection.
     *
     * @return String
     */
    public String getPubModel() {
        return pubModel;
    }

    /**
     * <ArticleTitle> contains the entire title of the journal article.
     * <ArticleTitle> is always in English; those titles originally published
     * in a foreign language and translated for <ArticleTitle> are enclosed in
     * square brackets. All titles end with a period unless another punctuation
     * mark such as a question mark or bracket is present. Explanatory
     * information about the title itself is enclosed in parentheses, e.g.:
     * (author's transl). Corporate/collective authors may appear at the end
     * of <ArticleTitle> for citations up to about the year 2000. See also
     * <AuthorList> for more information about corporate/collective authors.
     *
     * Records distributed with [In Process Citation] in <ArticleTitle> are non-English language citations in In-Process <MedlineCitation> status that do not yet have the article title translated into English.
     *
     * Examples are:
     *
     * <ArticleTitle>The Kleine-Levin syndrome as a neuropsychiatric disorder: a case report.</ArticleTitle>
     * <ArticleTitle>Why is xenon not more widely used for anaesthesia?</ArticleTitle>
     * <ArticleTitle>[Biological rhythms and human disease]</ArticleTitle>
     * <ArticleTitle>[In Process Citation]</ArticleTitle>
     * <ArticleTitle>[The effect of anti-arrhythmic drugs on myocardial function (author's transl)]</ArticleTitle>
     * <ArticleTitle>Prevalence of Helicobacter pylori resistance to antibiotics in Northeast Italy: a multicentre study. GISU. Interdisciplinary Group for the Study of Ulcer.</ArticleTitle>
     *
     * Additional information/background:
     *
     * For records in the OLDMEDLINE subset (<CitationSubset> = OM): For citations from the 1964 and 1965 Cumulated Index Medicus (CIM), <ArticleTitle> is in all upper case letters. Some citations contain the value "Not Available" for <ArticleTitle>. OLDMEDLINE records do not contain Corporate/Collective authors in <ArticleTitle>.
     *
     * @return String
     */
    public String getArticleTitle() {
        return articleTitle;
    }

    /**
     *
     * @param articleTitle
     */
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    @RelatedTo(direction=Direction.OUTGOING, relType=BioRelTypes.PUBLISHED_IN_JOURNAL, elementClass = BioRelation.class)
    private BioRelation publishedInJournal;

    /**
     *
     * @param journalIssue
     */
    public void setJournalIssue(JournalIssue journalIssue) {
        publishedInJournal = new BioRelation();
        publishedInJournal.setStartNode(this);
        publishedInJournal.setEndNode(journalIssue);
        publishedInJournal.setRelType(BioRelTypes.PUBLISHED_IN_JOURNAL);
        publishedInJournal.setMessage(BioRelTypes.PUBLISHED_IN_JOURNAL.toString());
    }

    /**
     *
     * @return BioRelation
     * @throws InstantiationException
     * @throws IllegalAccessException
     * @throws URISyntaxException
     * @throws IllegalArgumentException
     * @throws IllegalArgumentException
     * @throws NoSuchFieldException
     */
    public BioRelation getJournalIssueRelation() throws InstantiationException, IllegalAccessException, URISyntaxException, IllegalArgumentException, IllegalArgumentException, NoSuchFieldException {
        if (journalIssueRelation == null) {
           if (relations.isEmpty()) {
               relations = PersistenceTemplate.getRelations(this, BioRelation.class);
           }
           for (BioRelation relation : relations) {
               if (relation.getRelType().equals(BioRelTypes.PUBLISHED_IN_JOURNAL)) {
                   journalIssueRelation = relation;
                   break;
               }
           }
        }
        return journalIssueRelation;
    }

    /**
     *
     * @param authors
     */
    public void setAuthors(List<Author> authors) {
        authorRelations = new HashSet<BioRelation>();
        for (Author author : authors) {
            BioRelation authorRelation = new BioRelation();
            authorRelation.setStartNode(this);
            authorRelation.setEndNode(author);
            authorRelation.setRelType(BioRelTypes.HAS_AUTHOR);
            authorRelation.setMessage(BioRelTypes.HAS_AUTHOR.toString());
            authorRelations.add(authorRelation);
        }
    }

    /**
     * Personal and collective (corporate) author names published with the article
     * are found in <AuthorList>. Anonymous articles (including those with pseudonyms)
     * are identified by the absence of <AuthorList>. For records created from
     * 1966 - 1983, every author of every journal article is included in <AuthorList>.
     * For records created from 1984 - 1995, a maximum of 10 author names was entered
     * in the database. Beginning with journal issues published in 1996 and through
     * 1999, a maximum of 25 author names was entered, and beginning with journal
     * issues published in 2000 all author names published in the journal again
     * are entered. During the 1996 - 1999 time period, when there were more than
     * 25 authors, the first 24 were taken plus the last author as the 25th occurrence.
     * Beginning mid-2005, the various policy restrictions on number of author names
     * entered in past years were lifted so that on an individual basis, a record may
     * be edited to include all author names present in the published article
     * regardless of the limitation in effect at the time the record was first created.
     * Fetch the authors using the template if they don't exist locally.
     * For now this is not implemented.
     *
     * @return Collection<Author>
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public Collection<Author> getAuthors() throws InstantiationException, IllegalAccessException, URISyntaxException, IllegalArgumentException, NoSuchFieldException {
        if (authors.isEmpty()) {
            Collection<BioRelation> aRelations = getAuthorRelations();
            for (BioRelation aRelation : aRelations) {
                Author author = (Author)aRelation.getEndNode();
                authors.add(author);
            }
        }
        return authors;
    }

    /**
     * Fetch the authorRelations using the template if they don't exist locally.
     * For now this is not implemented.
     * @return Collection<BioRelation>
     * @throws java.lang.InstantiationException
     * @throws java.lang.IllegalAccessException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public Collection<BioRelation> getAuthorRelations() throws InstantiationException, IllegalAccessException, URISyntaxException, IllegalArgumentException, NoSuchFieldException {
        if (authorRelations == null || authorRelations.isEmpty()) {
            if (relations.isEmpty()) {
                relations = PersistenceTemplate.getRelations(this, BioRelation.class);
            }
            for (BioRelation relation : relations) {
                if (relation.getRelType().equals(BioRelTypes.HAS_AUTHOR)) {
                    authorRelations.add(relation);
                }
            }
        }
        return authorRelations;
    }

    /**
     * The party responsible for creating and validating the citation is recorded as
     * the MedlineCitation Owner attribute. Each citation has only one MedlineCitation
     * Owner and there are eight possible values for this attribute:
     * NLM | NASA | PIP | KIE | HSR | HMD | SIS | NOTNLM. The valid Owner values
     * for the various NLM departments and outside collaborating data partners are:
     *
     * NLM - National Library of Medicine, Index Section (the vast majority of citations carry this value)
     * NASA - National Aeronautics and Space Administration
     * PIP - Population Information Program, Johns Hopkins School of Public Health (not a current value; only on older citations)
     * KIE - Kennedy Institute of Ethics, Georgetown University
     * HSR - National Information Center on Health Services Research and Health Care Technology, National Library of Medicine
     * HMD - History of Medicine Division, National Library of Medicine
     * SIS - Specialized Information Services Division, National Library of Medicine (not yet used; reserved for possible future use)
     * NOTNLM - NLM does not plan to use this value for <MedlineCitation> or <GeneralNote> on citations it exports; licensees may want to use this value if they want to adapt the MEDLINE DTD for other applications.
     *
     * Some of the above Owner attributes - NASA, PIP, and KIE - may also be used with <GeneralNote> and <KeywordList> elements if the citation has been enriched with additional data by a collaborating partner. As of January 2013, NLM also uses NOTNLM with <KeywordList>.
     *
     * @return String
     */
    public String getOwner() {
        return owner;
    }

    /**
     *
     * @param owner
     */
    public void setOwner(String owner) {
        this.owner = owner;
    }

    /**
     * The Status attribute indicates the stage of a citation. There are seven
     * possible values for the MedlineCitation Status attribute:
     * Completed | In-Process | PubMed-not-MEDLINE | In-Data-Review | Publisher | MEDLINE | OLDMEDLINE,
     * as described below in the order in which processing of records distributed
     * to licensees occurs.
     *
     * MedlineCitation Status attribute: In-Data-Review
     * Records submitted to NLM electronically by publishers are added to PubMed
     * at NLM and distributed to licensees in In-Data-Review status. Records in this
     * status have undergone review at the journal issue level; i.e., the journal
     * title, date of publication and volume/issue elements (referred to as the
     * source data) are checked. Before records are distributed in In-Data-Review
     * status, the source data have either:
     *
     * been matched to the print copy in the NLM collection and are correct; or
     * been matched to the online version of the journal (when NLM assigns MeSH
     * headings from the online version) and are correct; or
     * been compared to previously checked in issues and appear to match the
     * pattern or have been changed to match the established pattern. In these
     * cases, the physical item has not yet been received for the NLM collection
     * and the data have not been positively verified and may still change during
     * the NLM processing cycle.
     *
     * While all three reviews are at the issue level, most citations fit into the
     * last condition above. It is possible that the source information may be
     * changed at a later point in the NLM quality assurance cycle once the hard
     * copy issue is available for exact comparison.
     *
     * In-Data-Review records lack the <DateCompleted> element. They are not yet
     * MEDLINE records because they have not undergone complete quality review and
     * MeSH indexing; thus they should not be identified as MEDLINE records in
     * licensees' systems/products.
     *
     * The issue level review for In-Data-Review status is the first step in
     * quality control and the records will either be typically reissued as
     * In-process status records or go to PubMed-not-MEDLINE final record status.
     * The <DateRevised> element is not applied when a record moves out of
     * In-Data-Review status.
     *
     * Records that are in In-Data-Review status at the beginning of a new
     * production year are not distributed as part of the annual baseline files.
     * They are distributed shortly after the baseline files with the remaining
     * In-process records as the new production year begins.
     *
     * See additional information for retrospective records.
     *
     * MedlineCitation Status attribute: In-Process
     * Records in this status have undergone a citation level review; i.e., the
     * author names, article title, and pagination are checked. All In-Data-Review
     * records that entered the workflow via publisher electronic submission are
     * redistributed again in In-Process status whether or not they were revised
     * as a result of the second citation level review, and are not identified
     * in any way as having been revised or not having been revised. Licensees
     * will simply see that this is the second time records with the same PMID,
     * now in In-process status, are received. The In-process version of the
     * record replaces the In-Data-Review version. This workflow means that
     * licensees will receive many records twice: once after the review of the
     * issue level information of electronically submitted records (i.e., the
     * In-Data-Review status records) and again after the review of the individual
     * citation data (In-process status records).
     *
     * Note: Beginning at the end of January 2009, In-process status citations
     * may be edited to add funding agency data provided in author manuscripts
     * submitted to PubMed Central (PMC). Thus, an In-process status citation
     * may be re-exported with this additional data, and, less likely, may be
     * subsequently re-exported if the data are found to be incorrect or need
     * to be deleted. Revised In-process records do not contain a DateRevised element.
     *
     * Records created via NLM current other data entry mechanism, scanning/optical
     * character recognition (OCR), are distributed for the first time in In-Process
     * status after their creation.
     *
     * In-process records lack the <DateCompleted> element; however, they do
     * contain the <CitationSubset> element. They are not yet MEDLINE records
     * because they have not undergone complete quality review and MeSH indexing;
     * thus they should not be identified as MEDLINE records in licensees'
     * systems/products.
     *
     * Most in-process records are eventually indexed with MeSH Headings and are
     * elevated to completed MEDLINE status. However, some are determined to be
     * out of scope (e.g., articles on plate tectonics or astrophysics from
     * certain MEDLINE journals, primarily general science and chemistry journals,
     * for which the life sciences articles are indexed for MEDLINE) and are not
     * elevated to MEDLINE status; instead they become PubMed-not-Medline final
     * status records. In rare cases the records are deleted and do not become
     * PubMed-not-MEDLINE records. The <DateRevised> element is not applied when
     * a record moves out of In-process status.
     *
     * Some records originally in PubMed-not-MEDLINE status are re-exported
     * in In-Process status; see <GeneralNote>.
     *
     * Records that are in In-process status at the beginning of a new production
     * year are not distributed as part of the annual baseline files. They are
     * distributed shortly after the baseline files with the remaining In-Data-Review
     * records as the new production year begins.
     *
     * See additional information for retrospective records.
     *
     * MedlineCitation Status attribute: MEDLINE
     *
     * In-process records undergo rigorous quality assurance routines before
     * they are elevated to MEDLINE status or to PubMed-not-MEDLINE status.
     *
     * Records in MEDLINE status are the only 'true' MEDLINE records in the
     * xml distribution. They contain <DateCompleted> and <CitationSubset> and,
     * in most cases, contain <MeshHeadingList>. MEDLINE records that are
     * Retractions of Publications (see Publication Type element) are exceptions
     * and do not contain <MeshHeadingList>. MEDLINE records may be new or
     * existing records that have been revised (see maintenance).
     *
     * MEDLINE status records are distributed as part of the annual baseline
     * files along with OLDMEDLINE and PubMed-not-MEDLINE records.
     *
     * MedlineCitation Status attribute: OLDMEDLINE
     *
     * A small percentage of the records in the OLDMEDLINE subset (designated
     * by <CitationSubset> value OM) are in MedlineCitation Status = OLDMEDLINE.
     * The criterion for records to be in OLDMEDLINE status is that all the
     * original MeSH Headings which reside in the <KeywordList> have not yet been
     * mapped to current MeSH. It is possible, however, that one or more old
     * Keyword terms have been mapped. For the larger number of OLDMEDLINE subset
     * records whose <MedlineCitation> Status is MEDLINE, all old Keyword terms
     * have been mapped to current MeSH. NLM exports both new and revised OLDMEDLINE
     * records on an irregular and infrequent basis.
     *
     * Beginning with the 2005 baseline distribution, OLDMEDLINE status records
     * are distributed as part of the annual baseline files along with MEDLINE
     * and PubMed-not-MEDLINE records.
     *
     * MedlineCitation Status attribute: PubMed-not-MEDLINE
     *
     * Records in this status are from journals included in MEDLINE and have
     * undergone quality review but are not assigned MeSH headings because the
     * cited item is not in scope for MEDLINE either by topic or by date of
     * publication, or from non-MEDLINE journals and have undergone quality
     * review. The specific categories of non-MEDLINE records in this status are:
     *
     * citations to articles that precede the date a journal was selected for
     * MEDLINE indexing and are submitted for inclusion in PubMed after July 2003;
     * out of scope citations to articles in journals covered by MEDLINE;
     * analytical summaries of articles published elsewhere (see the article,
     * "Linking MEDLINE Citations to Evidence-Based Medicine Assessments and
     * Summaries", in the May-Jun 2002 NLM Technical Bulletin, page e2); and
     * starting in summer 2005, prospective citations to articles from non-MEDLINE
     * journals that submit full text to PubMed Central and are thus cited in PubMed.
     * NLM first began distributing records in PubMed-not-MEDLINE status at the
     * end of July 2003 when it ceased using the old MedlineCitation Status value
     * of Out-of-scope. Records previously distributed in the old Out-of-scope
     * status were converted to the more generic PubMed-not-MEDLINE status and
     * redistributed with the 2004 baseline database.
     *
     * Records in PubMed-not-MEDLINE status have most often first been
     * distributed in In-Data-Review status prior to their quality review.
     *
     * PubMed-not-MEDLINE records contain <DateCompleted> element and lack
     * <CitationSubset> and <MeshHeadingList> elements.
     *
     * PubMed-not-MEDLINE status records are distributed as part of the annual
     * reload files along with MEDLINE and OLDMEDLINE records.
     *
     * See additional information for retrospective records.
     *
     * MedlineCitation Status attribute: Publisher
     *
     * Records in Publisher status are not distributed to licensees. At this
     * time approximately 98% of PubMed's content is distributed to MEDLINE
     * licensees. There are approximately 420,000 additional records in Publisher
     * MedlineCitation Status in PubMed. The majority of the non-exported records
     * contain the notation [PubMed - as supplied by publisher] in the PubMed
     * display. A relatively small percentage of additional records which are
     * citations to author manuscripts of articles published by NIH-funded
     * researchers and citations to books in the NCBI Bookshelf are also in
     * Publisher status and contain the notation [PubMed].
     *
     * The records in Publisher MedlineCitation Status are:
     *
     * the retrospective records for the relatively few non-MEDLINE journals
     * in PubMed (Note: starting July 2005 prospective non-MEDLINE journal
     * records in PubMed are distributed in PubMed-not-MEDLINE status);
     * the retrospective records for MEDLINE journals prior to date of selection
     * for MEDLINE and that were submitted electronically by the publishers before
     * late July 2003;
     *
     * the prospective records for currently indexed journals when the publisher
     * has submitted an issue's citation data electronically and NLM still awaits
     * its print copy or access to the electronic copy to use for issue level review
     * (i.e., the journal title, date of publication and volume/issue elements)
     * AND the publisher-supplied record contains a validation error of some kind
     * that prevents it from being exported from the NLM Data Creation and Maintenance
     * System (DCMS) along with the records not containing errors from the same issue.
     * If there were no errors, the record would move to MedlineCitation Status
     * In-Data-Review right away and be exported. In these cases, however, NLM
     * staff must take corrective action before the record can be elevated to
     * In-Data-Review status for export; and citations electronically submitted
     * for articles that appear on the Web in advance of the journal issue's
     * release (i.e., ahead of print citations). Following publication of the
     * completed issue, the item will be queued for issue level review and released
     * in In-Data-Review status.
     *
     * citations to author manuscripts per NIH's Public Access policy. Many of
     * the scientists who receive research funding from NIH publish the results
     * of this research in journals that are not available in NLM PubMed Central
     * (PMC). In order to improve access to these research articles, these authors
     * are required to give PMC the final, peer reviewed manuscripts of such
     * articles once they have been accepted for publication. Citations to these
     * author manuscripts in PMC reside in PubMed in Publisher status.
     *
     * citations to books and book chapters in the NCBI Bookshelf.
     *
     * MedlineCitation Status attribute: Completed
     *
     * This attribute is no longer used. Beginning with the 2005 baseline
     * distribution, records previously distributed in Completed status are
     * distributed in either MEDLINE or OLDMEDLINE status.
     *
     * @return String
     */
    public String getStatus() {
        return status;
    }

    /**
     *
     * @param status
     */
    public void setStatus(String status) {
        this.status = status;
    }

    /**
     * Version, added with the 2011 DTD, to accommodate the model of publishing
     * known as "versioning" discussed in <MedlineCitation>
     * A PMID Version attribute value higher than 1 will indicate that there is a
     * citation for at least one prior version (although it might happen, rarely,
     * that a prior version subsequently gets deleted)
     *
     * @return String
     */
    public String getVersion() {
        return version;
    }

    /**
     *
     * @param version
     */
    public void setVersion(String version) {
        this.version = version;
    }

    /**
     * Is the date processing of the record begins.
     * Is not the same as NLM PubMed Entrez Date (EDAT) that is the date the
     * record entered PubMed. The PubMed Entrez Date does not reside on records
     * distributed to licensees as it is generated when the record gets to PubMed.
     *
     * @return String
     */
    public String getCreatedDate() {
        return createdDate;
    }

    /**
     *
     * @param createdDate
     */
    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    /**
     * Is the date processing of the record ends; i.e., MeSH?? Headings have
     * been added, quality assurance validations are completed, and the completed
     * record subsequently is distributed to PubMed and licensees.
     *
     * @return String
     */
    public String getCompletedDate() {
        return completedDate;
    }

    /**
     *
     * @param completedDate
     */
    public void setCompletedDate(String completedDate) {
        this.completedDate = completedDate;
    }

    /**
     * It may reside on records in MedlineCitation Status = MEDLINE, MedlineCitation
     * Status = OLDMEDLINE, and MedlineCitation Status = PubMed-not-MEDLINE. It
     * identifies the date a change is made to a record in one of those statuses,
     * either as a result of individual or global maintenance. There is no
     * indication of what the change is on the record and only the latest revision
     * date is distributed. The <DateRevised> element is not assigned as a result
     * of a change in MedlineCitation Status; e.g., <DateRevised> is not
     * automatically generated as a result of a record elevating from
     * MedlineCitation Status=In-process to MedlineCitation Status=MEDLINE.
     *
     * It is possible for large numbers of records to be maintained and not
     * have an initial or updated <DateRevised> element. Do not depend on
     * initial presence of <DateRevised> or change to an existing <DateRevised>
     * value to indicate that a record has been maintained.
     *
     * @return String
     */
    public String getDateRevised() {
        return dateRevised;
    }

    /**
     *
     * @param dateRevised
     */
    public void setDateRevised(String dateRevised) {
        this.dateRevised = dateRevised;
    }

    @NonIndexed
    private String dateRevised;

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

    @Override
    public String toString() {
        return nodeType + "-" + pubMedId;
    }

    /**
     * There may be multiple BioEntities associated with a pmid. (pubmedId)
     * {@link Disease} {@link Gene} {@link NciPubMedGeneDrugDiseaseRelation}
     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.REFERENCES_PUBMED, elementClass = NciPubMedGeneDrugDiseaseRelation.class)
    private Collection<NciPubMedGeneDrugDiseaseRelation> pubMedStatementRelation = new HashSet<NciPubMedGeneDrugDiseaseRelation>();


    /**
     * Used in NciPathway
     * {@link NciPathway} {@link Enzyme}
     */
    @RelatedToVia(direction=Direction.BOTH, relType=BioRelTypes.REFERENCES_PUBMED, elementClass=BioRelation.class)
    private Collection<BioRelation> pubMedRelation = new HashSet<BioRelation>();

    /**
     *
     */
    public PubMed() {};

    /**
     *
     * @param pmid
     */
    public PubMed(String pmid) {
       this.pubMedId = pmid;
       this.message = nodeType + "-" + pmid;
    }

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
     * <PMID>, the PubMed (NLM database that incorporates MEDLINE) unique
     * identifier, is a 1 to 8-digit accession number with no leading zeros.
     *
     * @return String
     */
    public String getPubMedId() {
        return pubMedId;
    }

    /**
     *
     * @param pubMedId
     */
    public void setPubMedId(String pubMedId) {
        this.pubMedId = pubMedId;
    }

    /**
     * BioEntity can refer to multiple PubMeds
     * A single PubMedId can be referenced by multiple BioEntities
     * @param endEntity
     */
    /*
    public void setPubMedGeneDiseaseRelation(Object endEntity) {
        final NciPubMedGeneDrugDiseaseRelation relation  = new NciPubMedGeneDrugDiseaseRelation();
        relation.setNciPubMedGeneDiseaseRelation(this, endEntity, BioRelTypes.REFERENCES_PUBMED);
        pubMedGeneDiseaseRelation.add(relation);
    }
    *
    */

    /**
     * This is used NciGene-Drug (NciCompound), NciGene-Disease (NciDisease)
     * Statement and other citation information is part of this relationship
     * @param rel
     */
    public void setPubMedStatementRelation(NciPubMedGeneDrugDiseaseRelation rel) {
        pubMedStatementRelation.add(rel);
    }

    /**
     *
     * @return Iterable<NciPubMedGeneDrugDiseaseRelation>
     */
    public Iterable <NciPubMedGeneDrugDiseaseRelation> getPubMedStatementRelation() {
        return this.pubMedStatementRelation;
    }

    /**
     * {@link NciPathway} uses this
     * @param endEntitysing 
     */
    public void setPubMedRelation(Object endEntity) {
        final BioRelation relation = new BioRelation();
        relation.setEndNode(endEntity);
        relation.setStartNode(this);
        relation.setRelType(BioRelTypes.REFERENCES_PUBMED);
        relation.setMessage(relation.getRelType().toString());
        if (!pubMedRelation.add(relation)) {
            throw new RuntimeException("could not add," + endEntity.toString() + "relation to pubmed " + this.toString());
        }
    }

    /**
     *
     * @return Iterable<BioRelation>
     */
    public Iterable <BioRelation> getPubMedRelation() {
        return this.pubMedRelation;
    }

}
