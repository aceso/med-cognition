package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.atgc.bio.BioFields;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.MongoClasses;
import org.atgc.bio.PubMedMongoUtil;
import org.atgc.bio.domain.Author;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.Journal;
import org.atgc.bio.domain.JournalIssue;
import org.atgc.bio.domain.PubMed;
import org.atgc.bio.repository.CompoundKey;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoObjects;
import org.atgc.mongod.MongoUtil;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpException;
import org.neo4j.graphdb.NotFoundException;

/**
 * Loading PubMed into Redbasin Object Model and then Neo4J.
 * This is a library of methods to load pubmed articles from mongo into
 * Neo4J. It allows the outside applications to access the PubMed
 * along with a modified subgraph for any extended functionality.
 *
 * There are some inconsistency issues with pubmed documents in mongo
 * (that were originally generated from pubmed XML files). These issues include:
 *
 * 1. Providing a list of PubDate, without documenting what that means. In this
 * case we have only picked up the first PubDate.
 *
 * 2. Some fields like Country have come out as a List. We have again ignored it
 * as we only want it to be a String. This is to avoid ClassCastException.
 *
 * 3. In many cases, there was a confusion between BasicDBList v/s BasicDBObject.
 *
 * @author jtanisha-ee
 */
public class PubMedUtil {

    protected static Logger log = LogManager.getLogger(PubMedUtil.class);

    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }

    /**
     * This builds a pubmed query for the mongo collection "pubmed".
     *
     * @param pubmedId
     * @return
     * @throws Exception
     */
    public static BasicDBObject getPubMedQuery(String pubmedId) throws Exception {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(BioFields.PUBMED_ID.toString(), pubmedId);
        return queryMap;
    }

    /**
     * This just checks if a pubmedId exists in the mongo collection "pubmed".
     *
     * @param coll
     * @param pubmedId
     * @return
     * @throws Exception
     */
    public static boolean pubmedExists(ImportCollectionNames coll, String pubmedId) throws Exception {
        MongoCollection collection = getCollection(coll);

        BasicDBList result = collection.findDB(getPubMedQuery(pubmedId));
        if ((result == null) || (result.isEmpty())) {
            return false;
        }
        if (result.size() != 1) {
            log.error("result size = " + result.size());
            throw new RuntimeException("Multiple pubmedId objects detected for pubmedId: " + pubmedId);
        }
        return true;
    }

    /**
     * Gets a empty placeholder PubMed object with just the pubMedId
     * loaded. It gets it from the mongo collection pubmed.
     *
     * @param pubMedId
     * @return
     * @throws Exception
     */
    public static PubMed getPubmedKey(String pubMedId) throws Exception {
        DBCursor dbCursor = getCollection(
                ImportCollectionNames.PUBMED).findDBCursor(
                "{" +
                BioFields.PUBMED_ID +
                ": \"" + pubMedId + "\"}");
        PubMed pubMed = new PubMed();
        // we expect only one document match
        try {
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                //log.info("pubmed");
                pubMed.setPubMedId(pubMedId);
            }
        } finally {
            dbCursor.close();
        }
        return pubMed;
    }

    /**
     * Look up a field in a DBObject and return a String. If the value
     * is not found or null, then return null. If the class of the value
     * is not a String, then throw an exception.
     *
     * @param dbObject
     * @param field
     * @return
     */
    private static String getString(DBObject dbObject, PubmedMongoFields field) {
        if (dbObject == null || field == null) {
            return null;
        }
        Object obj = dbObject.get(field.toString());
        if (obj == null) {
            return null;
        }
        if (!obj.getClass().equals(String.class)) {
            log.error("Expected String object but found " + obj.getClass().getName() + ", field = " + field.toString());
            return null;
        }
        //log.info(field.toString() + " = " + obj);
        return (String)obj;
    }

    /**
     * This method looks up a field and returns a DBObject if it exists,
     * otherwise, it returns null. Users who use this method must not pass
     * a null, as that will generate an exception. If the object is found
     * but it's class is not a BasicDBObject then we throw an exception.
     *
     * @param dbObject
     * @param field
     * @return
     */
    private static BasicDBObject getDBObject(DBObject dbObject, PubmedMongoFields field) {
        if ((dbObject == null) || field == null) {
            log.error("dbObject is null for field ");
            return null;
        }
        Object obj = dbObject.get(field.toString());
        if (obj == null) {
            return null;
        }
        if (MongoClasses.BasicDBList.equals(obj.getClass()) ||
                MongoClasses.DBObject.equals(obj.getClass())) {
            log.error("Expected a BasicDBObject, but got " + obj.getClass().getName() + ", field = " + field.toString());
            return null;
        } else if (MongoClasses.BasicDBObject.equals(obj.getClass())) {
            return (BasicDBObject)dbObject.get(field.toString());
        }
        throw new RuntimeException("Unknown class found " + obj.getClass().getName());
    }

    /**
     * This method returns a BasicDBList even if the result is a DBObject.
     * This is because the import data sometimes stores them as DBObject if
     * there is only one element. Users of this method must not pass a null
     * dbObject, and that will lead to an error. However if the field is not
     * found, do not throw exception, just return a null. Many times the fields
     * are not found, that does not mean there is an error.
     *
     * @param dbObject must not be null
     * @param field must be a valid IntactFields field
     * @return BasicDBList always returns a BasicDBList or null if field not found
     */
    private static BasicDBList getBasicDBList(DBObject dbObject, PubmedMongoFields field) {
        if (dbObject == null || field == null) {
            log.error("dbObject is null for field ");
            return null;
        }
        Object obj = dbObject.get(field.toString());
        if (obj == null) {
            return null;
        }
        return MongoObjects.getBasicDBList(obj);
    }

    private static boolean isList(DBObject dbObject, PubmedMongoFields field) {
        if (dbObject == null || field == null) {
            throw new IllegalArgumentException("dbObject or field is null ");
        }
        Object obj = dbObject.get(field.toString());
        if (obj == null) {
            log.error("content is null for field " + field.toString());
            return false;
        }
        return MongoClasses.BasicDBList.equals(obj.getClass());
    }

    private static boolean isList(BasicDBList list, int cntr) {
        if (list == null) {
            throw new RuntimeException("list is null");
        }
        Object obj = list.get(cntr);
        if (obj == null) {
            log.error("content is null for list element " + cntr);
            return false;
        }
        return MongoClasses.BasicDBList.equals(obj.getClass());
    }

    private static String getPubmedId(BasicDBObject dbObject) {
        return getString(dbObject, PubmedMongoFields.PUBMED_ID);
    }

    private static BasicDBObject getMedlineCitation(BasicDBObject dbObject) {
        if (dbObject == null) {
            return null;
        }
        BasicDBList list = getBasicDBList(dbObject, PubmedMongoFields.PUBMED_ARTICLE);
        if (list != null && !list.isEmpty()) {
           return getDBObject((DBObject)list.get(0), PubmedMongoFields.MEDLINE_CITATION);
        } else {
            return null;
        }
    }

    private static String getOwner(BasicDBObject medlineCitation) {
        return getString(medlineCitation, PubmedMongoFields.OWNER);
    }

    private static String getStatus(BasicDBObject medlineCitation) {
        return getString(medlineCitation, PubmedMongoFields.STATUS);
    }

    private static String getVersion(BasicDBObject medlineCitation) {
        DBObject pmid = getDBObject(medlineCitation, PubmedMongoFields.PMID);
        return getString(pmid, PubmedMongoFields.VERSION);
    }

    private static String getPmid(BasicDBObject dbObject) {
        DBObject pmid = getDBObject(dbObject, PubmedMongoFields.PMID);
        return getString(pmid, PubmedMongoFields.TEXT);
    }

    private static String getCreatedDate(BasicDBObject medlineCitation) {
        DBObject date = getDBObject(medlineCitation, PubmedMongoFields.DATE_CREATED);
        StringBuilder sb = new StringBuilder();
        if (date != null) {
            sb.append(getString(date, PubmedMongoFields.MONTH)).append("-");
            sb.append(getString(date, PubmedMongoFields.DAY)).append("-");
            sb.append(getString(date, PubmedMongoFields.YEAR));
        }
        return sb.toString();
    }

    private static String getCompletedDate(BasicDBObject medlineCitation) {
        DBObject date = getDBObject(medlineCitation, PubmedMongoFields.DATE_COMPLETED);
        StringBuilder sb = new StringBuilder();
        if (date != null) {
            sb.append(getString(date, PubmedMongoFields.MONTH)).append("-");
            sb.append(getString(date, PubmedMongoFields.DAY)).append("-");
            sb.append(getString(date, PubmedMongoFields.YEAR));
        }
        return sb.toString();
    }

    private static String getRevisedDate(BasicDBObject medlineCitation) {
        DBObject date = getDBObject(medlineCitation, PubmedMongoFields.DATE_REVISED);
        StringBuilder sb = new StringBuilder();
        if (date != null) {
            sb.append(getString(date, PubmedMongoFields.MONTH)).append("-");
            sb.append(getString(date, PubmedMongoFields.DAY)).append("-");
            sb.append(getString(date, PubmedMongoFields.YEAR));
        }
        return sb.toString();
    }

    private static DBObject getArticle(BasicDBObject medlineCitation) {
        return getDBObject(medlineCitation, PubmedMongoFields.ARTICLE);
    }

    private static String getPubModel(DBObject article) {
        return getString(article, PubmedMongoFields.PUBMODEL);
    }

    private static String getArticleTitle(DBObject article) {
        return getString(article, PubmedMongoFields.ARTICLE_TITLE);
    }

    private static String getArticleAbstract(DBObject article) {
        if (article == null) {
            return "";
        }
        BasicDBList list = getBasicDBList(article, PubmedMongoFields.ABSTRACT);
        if (list != null) {
            Object obj = list.get(0);
            if (String.class.equals(obj.getClass())) {
                return (String)obj;
            } else {
                log.error("Expected String, got " + obj.getClass().getName());
                return "";
            }
        }
        return "";
    }

    private static String getAffiliation(DBObject article) {
        return getString(article, PubmedMongoFields.AFFILIATION);
    }

    private static String getLanguage(DBObject article) {
        return getString(article, PubmedMongoFields.LANGUAGE);
    }

    private static String getPublicationTypeList(DBObject article) {
        if (article == null) {
            return "";
        }
        BasicDBList list = getBasicDBList(article, PubmedMongoFields.PUBLICATION_TYPE_LIST);
        StringBuilder sb = new StringBuilder();
        if (list != null) {
            for (Object obj : list) {
                sb.append(obj).append(" ");
            }
        }
        return sb.toString();
    }

    private static DBObject getMedlineJournalInfo(DBObject medlineCitation) {
        return getDBObject(medlineCitation, PubmedMongoFields.MEDLINE_JOURNAL_INFO);
    }

    private static String getCountry(DBObject medlineJournalInfo) {
        return getString(medlineJournalInfo, PubmedMongoFields.COUNTRY);
    }

    private static String getMedlineTA(DBObject medlineJournalInfo) {
        return getString(medlineJournalInfo, PubmedMongoFields.MEDLINE_TA);
    }

    private static String getNlmUniqueID(DBObject medlineJournalInfo) {
        return getString(medlineJournalInfo, PubmedMongoFields.NLM_UNIQUE_ID);
    }

    private static String getISSNLinking(DBObject medlineJournalInfo) {
        return getString(medlineJournalInfo, PubmedMongoFields.ISSN_LINKING);
    }

    /**
     * If the CitationSubset appears as a list, then we just append multiple
     * items to a StringBuilder and return it as a String.
     *
     * @param medlineCitation
     * @return
     */
    private static String getCitationSubset(DBObject medlineCitation) {
        if (medlineCitation == null) {
            return "";
        }
        if (isList(medlineCitation, PubmedMongoFields.CITATION_SUBSET)) {
            BasicDBList items = getBasicDBList(medlineCitation, PubmedMongoFields.CITATION_SUBSET);
            StringBuilder sb = new StringBuilder();
            if (items != null) {
                for (Object item : items) {
                    String i = (String)item;
                    sb.append(i).append(" ");
                    //log.info("CitationSubset = " + i);
                }
            }
            return sb.toString();
        } else {
           return getString(medlineCitation, PubmedMongoFields.CITATION_SUBSET);
        }
    }

    private static BasicDBList getMeshHeadingList(DBObject medlineCitation) {
        return getBasicDBList(medlineCitation, PubmedMongoFields.MESH_HEADING_LIST);
    }

    private static BasicDBObject majorTopics(BasicDBList meshHeadingList) {
        StringBuilder majorTopics = new StringBuilder();
        StringBuilder notMajorTopics = new StringBuilder();
        if (meshHeadingList == null) {
            return null;
        }
        for (int cntr = 0; cntr < meshHeadingList.size(); cntr++) {
            if (isList(meshHeadingList, cntr)) {
                BasicDBList list = (BasicDBList)meshHeadingList.get(cntr);
                if (list != null && !list.isEmpty()) {
                    String majorTopicYN = getString((BasicDBObject)list.get(0), PubmedMongoFields.MAJOR_TOPICYN);
                    String text = getString((BasicDBObject)list.get(0), PubmedMongoFields.TEXT);
                    if (majorTopicYN.equals("Y")) {
                        majorTopics.append(text).append(" ");
                    } else {
                        notMajorTopics.append(text).append(" ");
                    }
                }
            } else {
                DBObject descriptorName = getDBObject((DBObject)meshHeadingList.get(cntr), PubmedMongoFields.DESCRIPTOR_NAME);
                if (descriptorName != null) {
                    String majorTopicYN = getString(descriptorName, PubmedMongoFields.MAJOR_TOPICYN);
                    String text = getString(descriptorName, PubmedMongoFields.TEXT);
                    if (majorTopicYN.equals("Y")) {
                        majorTopics.append(text).append(" ");
                    } else {
                        notMajorTopics.append(text).append(" ");
                    }
                }
                if (isList((DBObject)meshHeadingList.get(cntr), PubmedMongoFields.QUALIFIER_NAME)) {
                    BasicDBList items = getBasicDBList((DBObject)meshHeadingList.get(cntr), PubmedMongoFields.QUALIFIER_NAME);
                    if (items != null) {
                        for (Object item : items) {
                            DBObject qualifierName = (DBObject)item;
                            String majorTopicYN = getString(qualifierName, PubmedMongoFields.MAJOR_TOPICYN);
                            String text = getString(qualifierName, PubmedMongoFields.TEXT);
                            if (majorTopicYN.equals("Y")) {
                                majorTopics.append(text).append(" ");
                            } else {
                                notMajorTopics.append(text).append(" ");
                            }
                        }
                    }
                } else {
                    DBObject qualifierName = getDBObject((DBObject)meshHeadingList.get(cntr), PubmedMongoFields.QUALIFIER_NAME);
                    if (qualifierName != null) {
                        String majorTopicYN = getString(qualifierName, PubmedMongoFields.MAJOR_TOPICYN);
                        String text = getString(qualifierName, PubmedMongoFields.TEXT);
                        if (majorTopicYN.equals("Y")) {
                            majorTopics.append(text).append(" ");
                        } else {
                            notMajorTopics.append(text).append(" ");
                        }
                    }
                }
            }
        }
        BasicDBObject obj = new BasicDBObject();
        obj.put("majorTopics", majorTopics.toString());
        obj.put("notMajorTopics", notMajorTopics.toString());
        return obj;
    }

    /**
     * 
     * @param subgraph
     * @param article
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnknownHostException 
     */
    private static List<Author> getAuthors(Subgraph subgraph, DBObject article) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
        List<Author> authors = new ArrayList<Author>();
        DBObject obj = getDBObject(article, PubmedMongoFields.AUTHOR_LIST);
        if (obj == null) {
            return authors;
        }
        BasicDBList list = getBasicDBList(obj, PubmedMongoFields.AUTHOR);
        if (list == null || list.isEmpty()) {
            return authors;
        }
        for (Object element : list) {
            BasicDBObject a = (BasicDBObject)element;
            Author author = new Author();
            author.setValidYN(getString(a, PubmedMongoFields.VALIDYN));
            author.setLastName(getString(a, PubmedMongoFields.LAST_NAME));
            author.setForeName(getString(a, PubmedMongoFields.FORE_NAME));
            author.setInitials(getString(a, PubmedMongoFields.INITIALS));
            CompoundKey compoundKey = CompoundKey.getCompoundKey(author);
            if (compoundKey != null) {
            //StatusUtil.idInsert(BioTypes.AUTHOR.toString(), compoundKey.getKey(), compoundKey.getValue());
                authors.add(author);
                subgraph.add(author);
            }
        }
        return authors;
    }

    
    /**
     * pubDate can be a list or an object
     * "PubDate" : [
     *      "2001
     *  ] 
     * 
     * PubDate: {
     *     "Year"  : "2001"
     *     "Month" : "Mar"
     *     "Day"   : "13"
     * }
     * 
     */
    private static JournalIssue getJournalIssue(Subgraph subgraph, DBObject article) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
        Journal journal = new Journal();
        BasicDBObject journalObj = getDBObject(article, PubmedMongoFields.JOURNAL);
        if (journalObj == null) {
            return null;
        }
        String issnType = getString(getDBObject(journalObj, PubmedMongoFields.ISSN), PubmedMongoFields.ISSN_TYPE);
        journal.setIssnType(issnType);
        String issn = getString(getDBObject(journalObj, PubmedMongoFields.ISSN), PubmedMongoFields.TEXT);
        if (issn == null) {
            return null;
        }
        journal.setIssn(issn);
        journal.setTitle(getString(journalObj, PubmedMongoFields.TITLE));
        journal.setIsoAbbreviation(getString(journalObj, PubmedMongoFields.ISO_ABBREVIATION));
        JournalIssue journalIssue = new JournalIssue();
        BasicDBObject journalIssueObj = getDBObject(journalObj, PubmedMongoFields.JOURNAL_ISSUE);
        if (journalIssueObj == null) {
            return null;
        }
        journalIssue.setCitedMedium(getString(journalIssueObj, PubmedMongoFields.CITED_MEDIUM));
        journalIssue.setVolume(getString(journalIssueObj, PubmedMongoFields.VOLUME));
        journalIssue.setIssue(getString(journalIssueObj, PubmedMongoFields.ISSUE));
        journalIssue.setIssn(issn);
        StringBuilder sb = new StringBuilder();
        BasicDBObject pubDate = null;
        if (isList(journalIssueObj, PubmedMongoFields.PUBDATE)) { 
            BasicDBList result = getBasicDBList(journalIssueObj, PubmedMongoFields.PUBDATE);
            if (result != null && !result.isEmpty()) {
               journalIssue.setPubDate((String)result.get(0));
            }
        } else {
            pubDate = getDBObject(journalIssueObj, PubmedMongoFields.PUBDATE);
            if (pubDate != null) {
                sb.append(getString(pubDate, PubmedMongoFields.MONTH)).append("-");
                sb.append(getString(pubDate, PubmedMongoFields.DAY)).append("-");
                sb.append(getString(pubDate, PubmedMongoFields.YEAR));
                journalIssue.setPubDate(sb.toString());
            }
        }
        journalIssue.setJournal(journal);
        //CompoundKey compoundKey = CompoundKey.getCompoundKey(journalIssue);
        //StatusUtil.idInsert(BioTypes.JOURNAL_ISSUE.toString(), compoundKey.getKey(), compoundKey.getValue());
        //StatusUtil.idInsert(BioTypes.JOURNAL.toString(), BioFields.ISSN.toString(), journal.getIssn());
        subgraph.add(journal);
        subgraph.add(journalIssue);
        return journalIssue;
    }

    /**
     * This is an internal method that actually creates one PubMed and
     * populate the subgraph. It does not save anything. It only loads
     * all the bioentities related with this PubMed.
     *
     * @param pubMed
     * @param result
     * @param subgraph
     * @param pubmedId
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws UnknownHostException
     * @throws HttpException
     */
    private static void loadPubmed(PubMed pubMed, BasicDBObject result, Subgraph subgraph, String pubmedId)
        throws
            NotFoundException,
            NoSuchFieldException,
            IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException,
            URISyntaxException,
            UnsupportedEncodingException,
            MalformedURLException,
            IOException,
            UnknownHostException,
            HttpException {
        BasicDBObject medlineCitation = getMedlineCitation(result);
        pubMed.setOwner(getOwner(medlineCitation));
        pubMed.setStatus(getStatus(medlineCitation));
        pubMed.setVersion(getVersion(medlineCitation));
        pubMed.setCreatedDate(getCreatedDate(medlineCitation));
        pubMed.setCompletedDate(getCompletedDate(medlineCitation));
        pubMed.setDateRevised(getRevisedDate(medlineCitation));
        DBObject article = getArticle(medlineCitation);
        if (article != null) {
            pubMed.setPubModel(getPubModel(article));
            pubMed.setArticleTitle(getArticleTitle(article));
            pubMed.setArticleAbstract(getArticleAbstract(article));
            pubMed.setAffiliation(getAffiliation(article));
            pubMed.setLanguage(getLanguage(article));
            pubMed.setPublicationTypeList(getPublicationTypeList(article));
            pubMed.setAuthors(getAuthors(subgraph, article));
            JournalIssue journalIssue = getJournalIssue(subgraph, article);
            if (journalIssue != null) {
               pubMed.setJournalIssue(getJournalIssue(subgraph, article));
            }
        }
        DBObject medlineJournalInfo = getMedlineJournalInfo(medlineCitation);
        pubMed.setCountry(getCountry(medlineJournalInfo));
        pubMed.setMedlineTA(getMedlineTA(medlineJournalInfo));
        pubMed.setNlmUniqueId(getNlmUniqueID(medlineJournalInfo));
        pubMed.setIssnLinking(getISSNLinking(medlineJournalInfo));
        pubMed.setCitationSubset(getCitationSubset(medlineCitation));
        BasicDBObject obj = majorTopics(getMeshHeadingList(medlineCitation));
        pubMed.setMajorTopics(getString(obj, PubmedMongoFields.MAJOR_TOPICS));
        pubMed.setNotMajorTopics(getString(obj, PubmedMongoFields.NOT_MAJOR_TOPICS));
        subgraph.add(pubMed);
    }

    /**
     * This method can be called by outside applications, and allows the
     * user to pass in a subgraph. It returns a PubMed, but also populates
     * the subgraph with all the bioentities that were required to be
     * created in order to create the requested PubMed. It does not actually
     * save anything to Neo4J. The user's responsibility is to save the
     * subgraph themselves, since they also created it themselves. If the
     * subgraph already had preexisting bioentities, then it appends to this
     * list, and does not remove them.
     *
     * <p>
     * If the pubMedId does not exist in the local mongo collection "pubmed"
     * then we go ahead and load the pubmed article from outside into mongo,
     * and then retry fetching the article from mongo a second time.
     * </p>
     *
     * @param pubMedId
     * @param subgraph
     * @return will return a PubMed or null if not found
     * @throws UnknownHostException
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     * @throws java.lang.InterruptedException
     */
    public static PubMed getPubmed(String pubMedId, Subgraph subgraph) throws
            UnknownHostException,
            NotFoundException,
            NoSuchFieldException,
            IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException,
            URISyntaxException,
            UnsupportedEncodingException,
            MalformedURLException,
            IOException,
            HttpException,
            InterruptedException {
        BasicDBObject basicDBObject = new BasicDBObject();
        basicDBObject.put(PubmedMongoFields.PUBMED_ID.toString(), pubMedId);
        DBCursor dbCursor = getCollection(ImportCollectionNames.PUBMED).findDBCursor(basicDBObject);
        PubMed pubMed = null;
        boolean loaded;
        try {
            if (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String pubmedId = getPubmedId(result);
                pubMed = new PubMed();
                pubMed.setPubMedId(pubMedId);
                try {
                    loadPubmed(pubMed, result, subgraph, pubmedId);
                } catch (NullPointerException e) {
                    throw new RuntimeException("Null pointer exception. PubmedId: " + pubmedId, e);
                }
            } else {
                loaded = PubMedMongoUtil.loadPubMed(pubMedId);
                dbCursor.close();
                dbCursor = null;
                if (loaded) {
                    dbCursor = getCollection(ImportCollectionNames.PUBMED).findDBCursor(basicDBObject);
                    if (dbCursor.hasNext()) {
                        BasicDBObject result = (BasicDBObject)dbCursor.next();
                        String pubmedId = getPubmedId(result);
                        pubMed = new PubMed();
                        pubMed.setPubMedId(pubMedId);
                        try {
                            loadPubmed(pubMed, result, subgraph, pubmedId);
                        } catch (NullPointerException e) {
                            throw new RuntimeException("Null pointer exception. PubmedId: " + pubmedId, e);
                        }
                    }
                }
            }
        } finally {
            if (dbCursor != null) {
                dbCursor.close();
            }
        }

        return pubMed;
    }

    /**
     * This is used when you just want to load a PubMed object from
     * Mongo into RedbasinObject model and then save into Neo4J
     * automatically. It creates it's own internal subgraph. So the user will not
     * be able to use this method, if user is interested in their own
     * subgraph scenario.
     *
     * @param pubMedId
     * @return
     * @throws UnknownHostException
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     * @throws java.lang.InterruptedException
     */
    public static PubMed loadPubmed(String pubMedId) throws
            UnknownHostException,
            NotFoundException,
            NoSuchFieldException,
            IllegalArgumentException,
            IllegalAccessException,
            InvocationTargetException,
            URISyntaxException,
            UnsupportedEncodingException,
            MalformedURLException,
            IOException,
            HttpException,
            InterruptedException {
        Subgraph subgraph = new Subgraph();
        PubMed pubMed = getPubmed(pubMedId, subgraph);
        PersistenceTemplate.saveSubgraph(subgraph);
        return pubMed;
    }

    /**
     * This method should be called only if you would like to import all
     * the PubMed articles into Neo4J, which is a one-time event usually. We do check
     * if the statusPubMed collection already shows the import for a specific
     * PubMed, so we do not try to load the ones that are already loaded.
     * This method creates it's own internal subgraph.
     *
     * @throws UnknownHostException
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     */
    public static void loadPubmed() throws UnknownHostException, NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, HttpException {
        DBCursor dbCursor = getCollection(ImportCollectionNames.PUBMED).findDBCursor("{}" );

        Subgraph subgraph = new Subgraph();

        try {
        // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                String pubmedId = getPubmedId(result);
                if (!StatusUtil.idExists(BioTypes.PUBMED.toString(), BioFields.PUBMED_ID.toString(), pubmedId)) {
                    PubMed pubMed = new PubMed();
                    pubMed.setPubMedId(pubmedId);
                    try {
                        loadPubmed(pubMed, result, subgraph, pubmedId);
                        PersistenceTemplate.saveSubgraph(subgraph);
                        //StatusUtil.idInsert(BioTypes.PUBMED.toString(), BioFields.PUBMED_ID.toString(), pubmedId);
                    } catch (NullPointerException e) {
                        throw new RuntimeException("Null pointer exception. PubmedId: " + pubmedId, e);
                    }
                } else {
                    log.info("Skipping pubmedId already imported " + pubmedId);
                }
            }
        } finally {
            dbCursor.close();
        }
    }

    public static void main(String[] args) throws Exception {
        //PubMedUtil.getPubmed("16341006");
        //PubMedUtil.loadPubmed("20462514");
        //PubMedUtil.loadPubmed("4335861");
        PubMedUtil.loadPubmed("14783033");
        //PubMedUtil.loadPubmed();
    }
}