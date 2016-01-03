/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.repository;

import org.atgc.bio.domain.StartStatus;
import org.atgc.bio.meta.AnnotationTypes;
import org.atgc.bio.meta.UniqueCompoundIndex;
import org.atgc.init.Config;
import java.io.File;
import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.analysis.standard.StandardAnalyzer;
import org.apache.lucene.document.Document;
import org.apache.lucene.document.Field;
import org.apache.lucene.index.CorruptIndexException;
import org.apache.lucene.index.IndexNotFoundException;
import org.apache.lucene.index.IndexWriter;
import org.apache.lucene.index.IndexWriterConfig;
import org.apache.lucene.index.Term;
import org.apache.lucene.queryParser.QueryParser;
import org.apache.lucene.search.IndexSearcher;
import org.apache.lucene.search.Query;
import org.apache.lucene.search.ScoreDoc;
import org.apache.lucene.search.Searcher;
import org.apache.lucene.search.TermQuery;
import org.apache.lucene.search.TopDocs;
import org.apache.lucene.store.LockObtainFailedException;
import org.apache.lucene.store.SimpleFSDirectory;
import org.apache.lucene.util.Version;

/**
 * Can process any BioEntity by indexing all the required fields.
 * Before you use LuceneTemplate, please create a directory called
 * "biotext" under RedbasinOncology/biotext
 *
 * @author jtanisha-ee
 * @param <T>
 */
public class LuceneTemplate<T> {
    private static SimpleFSDirectory idx;
    private static IndexWriterConfig indexWriterConfig;
    private static IndexWriter writer;
    private static final String BIOTEXT = "biotext";
    protected static Log log = LogFactory.getLog(new Object().getClass());

    static {
       try {
            //idx = new SimpleFSDirectory(new File(BIOTEXT));
            idx = new SimpleFSDirectory(new File(Config.LUCENE_INDEX_DIR.toString()));
       } catch (IOException e) {
            throw new RuntimeException("Could not initialize lucene ", e);
       }
    }

    /*
    public LuceneTemplate() throws CorruptIndexException, LockObtainFailedException, IOException {
        //idx = new SimpleFSDirectory(new File(BIOTEXT));
    } */

    public static boolean fieldMatch(Annotation anno, AnnotationTypes type) {
        return (anno.toString().startsWith(type.toString()));
    }

    /*
    public static String escapeChars(String key) {
        return key.replaceAll(":", "\\:").replaceAll("-", "\\-");
    }*/

    public static <T> boolean docExists(T t) throws IllegalArgumentException, IllegalAccessException, URISyntaxException, IOException, NoSuchFieldException {
        Annotation anno = t.getClass().getAnnotation(UniqueCompoundIndex.class);
        if (anno != null) {
            CompoundKey compoundKey = CompoundKey.getCompoundKey(t);
            //log.info("ckey = " + compoundKey.getKey() + ", cval = " + compoundKey.getValue());
            if (compoundKey == null) {
                log.error("compoundKey is null, so cannot check for doc existence, return docExists as false, t = " + t.toString());
                return false;
            }
            List<Document> docs = lookupTerm(compoundKey.getKey(), compoundKey.getValue());

            if (docs.size() > 0) {
               log.info("key " + compoundKey.getKey() + ", value = " + compoundKey.getValue() + ", already exists in lucene.");
               return true;
            } else {
               log.info("key " + compoundKey.getKey() + ", value = " + compoundKey.getValue() + ", does not exist in lucene.");

            }
        } else {
            PrimaryKey primaryKey = PrimaryKey.getPrimaryKey(t);
            log.info("pkey = " + primaryKey.getKey() + ", pval = " + primaryKey.getValue());
            List<Document> docs = lookupTerm(primaryKey.getKey(), primaryKey.getValue());
            if (docs.size() > 0) {
               log.info("key " + primaryKey.getKey() + ", value = " + primaryKey.getValue() + ", already exists in lucene.");
               return true;
            } else {
               log.info("key " + primaryKey.getKey() + ", value = " + primaryKey.getValue() + ", does not exist in lucene.");
            }
        }
        return false;
    }

    /**
     * Before we add a document we must check if it already exists.
     *
     * @param <T>
     * @param t
     * @throws CorruptIndexException
     * @throws IOException
     * @throws IllegalAccessException
     * @throws IllegalArgumentException
     * @throws java.net.URISyntaxException
     * @throws java.lang.NoSuchFieldException
     */
    public static <T> void addDocument(T t) throws CorruptIndexException, IOException, IllegalAccessException, IllegalArgumentException, URISyntaxException, NoSuchFieldException {
        Document doc = new Document();

        // first check if the document exists
        if (docExists(t)) {
            return;
        }

        CompoundKey compoundKey = CompoundKey.getCompoundKey(t);
        if (compoundKey != null) {
            doc.add(new Field(QueryParser.escape(compoundKey.getKey()), QueryParser.escape(compoundKey.getValue()), Field.Store.YES, Field.Index.NOT_ANALYZED));
            log.info("Adding to lucene, key = " + compoundKey.getKey() + ", value = " + compoundKey.getValue());
        }

        java.lang.reflect.Field[] fields = t.getClass().getDeclaredFields();

        for (java.lang.reflect.Field field : fields) {
            field.setAccessible(true);
            Annotation[] annos = field.getDeclaredAnnotations();
            for (Annotation anno : annos) {
                if (fieldMatch(anno, AnnotationTypes.FULLTEXT_INDEXED) ||
                        fieldMatch(anno, AnnotationTypes.INDEXED) ||
                        fieldMatch(anno, AnnotationTypes.NODE_LABEL) ||
                        fieldMatch(anno, AnnotationTypes.PARTKEY) ||
                        fieldMatch(anno, AnnotationTypes.UNIQUELY_INDEXED) ||
                        fieldMatch(anno, AnnotationTypes.VISUAL)) {
                    log.info("field name = " + field.getName());
                    Object obj = field.get(t);
                    if (obj != null) {
                       String val = obj.toString();
                       doc.add(new Field(QueryParser.escape(field.getName()), QueryParser.escape(val), Field.Store.YES, Field.Index.NOT_ANALYZED));
                       log.info("Adding to lucene, key = " + field.getName() + ", value = " + val);

                    }
                }
            }
        }

        indexWriterConfig = new IndexWriterConfig(Version.LUCENE_31, new StandardAnalyzer(Version.LUCENE_31));
        writer = new IndexWriter(idx, indexWriterConfig);
        writer.addDocument(doc);
        writer.close();
    }

    private static void addDummyDocument() throws CorruptIndexException, LockObtainFailedException, IOException {
        Document doc = new Document();
        indexWriterConfig = new IndexWriterConfig(Version.LUCENE_31, new StandardAnalyzer(Version.LUCENE_31));
        writer = new IndexWriter(idx, indexWriterConfig);
        writer.addDocument(doc);
        writer.close();
    }

    public static <T> List<Document> lookupTerm(String field, String value) throws IOException {
        //Search mails having the word "java" in the subject field
        //log.info("field = " + field + ", value = " + value);
        Searcher indexSearcher;
        try {
            indexSearcher = new IndexSearcher(new SimpleFSDirectory(new File(Config.LUCENE_INDEX_DIR.toString())));
        } catch (IndexNotFoundException e) {
            log.info("Index empty, so we will add a dummy document first time ", e);
            addDummyDocument();
            indexSearcher = new IndexSearcher(new SimpleFSDirectory(new File(Config.LUCENE_INDEX_DIR.toString())));
        }
        Term term = new Term(QueryParser.escape(field), QueryParser.escape(value));
        Query termQuery = new TermQuery(term);
        TopDocs topDocs = indexSearcher.search(termQuery, 1);

        List<Document> matches = new ArrayList<Document>();
        for ( ScoreDoc scoreDoc : topDocs.scoreDocs ) {
        //This retrieves the actual Document from the index using
        //the document number. (scoreDoc.doc is an int that is the
        //doc's id
            Document doc = indexSearcher.doc(scoreDoc.doc);
            matches.add(doc);
        }
        indexSearcher.close();
        return matches;
    }

    public static void main(String[] args) throws CorruptIndexException, IOException, IllegalAccessException, IllegalArgumentException, URISyntaxException, NoSuchFieldException {
        StartStatus startStatus = new StartStatus(); // example bioentity
        startStatus.setFeatureId("44:3-422");
        //startStatus.setFeatureId("44:3-422");
        LuceneTemplate.addDocument(startStatus);
        List<Document> docs = LuceneTemplate.lookupTerm("featureId", "44:3-422");
        log.info("docs size = " + docs.size());
    }
}