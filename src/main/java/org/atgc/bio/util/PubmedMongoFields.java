/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import java.util.HashMap;
import java.util.Map;

/**
 * For every relationship which has a new string relation, we add an enum here.
 * There would be lots of relationships in this file eventually.
 * <p>
 * Some graph vendors force us to have no spaces in the relationship. That is
 * why we use an underscore. This string is displayed in the GraphDB gui, so we
 * need to be more user friendly. Use upper case for all the string names for
 * consistency.
 * <p>
 * Also add a property called "message" with the same value as this relationship.
 * This relation string is the same as the type property for the <code>RelationshipType</code>
 *
 *
 * @author jtanisha-ee
 */

public enum PubmedMongoFields {

    PUBMED_ID("pubmedId"),
    PUBMED_ARTICLE("PubmedArticle"),
    MEDLINE_CITATION("MedlineCitation"),
    OWNER("@Owner"),
    STATUS("@Status"),
    PMID("PMID"),
    ISSN("ISSN"),
    VERSION("@Version"),
    TEXT("#text"),
    DATE_CREATED("DateCreated"),
    YEAR("Year"),
    MONTH("Month"),
    DAY("Day"),
    DATE_COMPLETED("DataCompleted"),
    DATE_REVISED("DateRevised"),
    ARTICLE("Article"),
    PUBMODEL("@PubModel"),
    JOURNAL("Journal"),
    ARTICLE_TITLE("ArticleTitle"),
    TITLE("Title"),
    PAGINATION("Pagination"),
    ABSTRACT("Abstract"),
    AFFILIATION("Affiliation"),
    AUTHOR_LIST("AuthorList"),
    ISO_ABBREVIATION("ISOAbbreviation"),
    AUTHOR("Author"),
    VALIDYN("@ValidYN"),
    LAST_NAME("LastName"),
    FORE_NAME("ForeName"),
    INITIALS("Initials"),
    LANGUAGE("Language"),
    JOURNAL_ISSUE("JournalIssue"),
    ISSUE("Issue"),
    CITED_MEDIUM("@CitedMedium"),
    VOLUME("Volume"),
    PUBLICATION_TYPE_LIST("PublicationTypeList"),
    MEDLINE_JOURNAL_INFO("MedlineJournalInfo"),
    COUNTRY("Country"),
    MEDLINE_TA("MedlineTA"),
    NLM_UNIQUE_ID("NlmUniqueID"),
    ISSN_LINKING("ISSNLinking"),
    ISSN_TYPE("@IssnType"),
    CITATION_SUBSET("CitationSubset"),
    MESH_HEADING_LIST("MeshHeadingList"),
    MAJOR_TOPICYN("@MajorTopicYN"),
    DESCRIPTOR_NAME("DescriptorName"),
    PUBDATE("PubDate"),
    QUALIFIER_NAME("QualifierName"),
    MAJOR_TOPICS("majorTopics"),
    NOT_MAJOR_TOPICS("notMajorTopics");

    private final String value;

    private static final Map<String, PubmedMongoFields> stringToEnum = new HashMap<String, PubmedMongoFields>();

    static { // init map from constant name to enum constant
        for (PubmedMongoFields en : values()) {
            stringToEnum.put(en.toString(), en);
        }
    }

    PubmedMongoFields(String value) {
        this.value = value;
    }

    public boolean equals(PubmedMongoFields bioRelType) {
        return value.equals(bioRelType.toString());
    }

    public boolean equals(String bioRelType) {
        return value.equals(bioRelType);
    }

    @Override
    public String toString() {
        return value;
    }

    public static PubmedMongoFields fromString(String value) {
        return stringToEnum.get(value);
    }
}