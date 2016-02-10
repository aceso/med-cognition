/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.MongoClasses;
import org.atgc.bio.PubMedMongoUtil;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.Gene;
import org.atgc.bio.domain.GeneOntology;
import org.atgc.bio.domain.GeneToGo;
import org.atgc.bio.domain.GeneToGoOntology;
import org.atgc.bio.domain.NcbiTaxonomy;
import org.atgc.bio.domain.PubMed;
import org.atgc.bio.domain.types.TaxonomyRank;
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
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpException;
import org.neo4j.graphdb.NotFoundException;

/**
 * This code should import genes from the following mongo collections into neo4j.
 *
 * ncbigeneinfo
 * ncbigene2go
 * ncbigene2pubmed
 * ncbigenegroup
 * ncbigeneneighbors
 * 
 * @author jtanisha-ee
 */
public class GeneGraphDBImportUtil {

    protected static Logger log = LogManager.getLogger(GeneGraphDBImportUtil.class);

    private static MongoCollection getCollection(ImportCollectionNames coll) throws UnknownHostException {
        MongoUtil mongoUtil = MongoUtil.getInstance();
        return mongoUtil.getCollection(coll.toString());
    }

    /**
     * This builds a gene query for the mongo collection "ncbigeneinfo".
     *
     * @param symbol
     * @return
     * @throws Exception
     */
    public static BasicDBObject getGeneQuery(String symbol) throws Exception {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(GeneMongoFields.SYMBOL.toString(), symbol);
        return queryMap;
    }

    public static BasicDBObject getTaxonomyQuery(String taxId) throws Exception {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(TaxonomyMongoFields.ID.toString(), taxId);
        return queryMap;
    }

    public static BasicDBObject getGene2GoQuery(String geneId) throws Exception {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(GeneMongoFields.GENE_ID.toString(), geneId);
        return queryMap;
    }

    public static BasicDBObject getGeneIdQuery(String geneId) throws Exception {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(GeneMongoFields.GENE_ID.toString(), geneId);
        return queryMap;
    }

    public static BasicDBObject getNeighborsQuery(String geneId) throws Exception {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(GeneMongoFields.GENE_ID.toString(), geneId);
        return queryMap;
    }

    public static BasicDBObject getPubmedQuery(String geneId) throws Exception {
        BasicDBObject queryMap = new BasicDBObject();
        queryMap.put(GeneMongoFields.GENE_ID.toString(), geneId);
        return queryMap;
    }

    public static BasicDBList getResult(ImportCollectionNames coll, String geneId) throws Exception {
        MongoCollection collection = getCollection(coll);
        return collection.findDB(getGeneQuery(geneId));
    }

    public static BasicDBList getGeneIdResult(ImportCollectionNames coll, String geneId) throws Exception {
        MongoCollection collection = getCollection(coll);
        return collection.findDB(getGeneIdQuery(geneId));
    }

    public static BasicDBList getGene2GoResult(ImportCollectionNames coll, String geneId) throws Exception {
        MongoCollection collection = getCollection(coll);
        return collection.findDB(getGene2GoQuery(geneId));
    }

    public static BasicDBList getTaxonomyResult(ImportCollectionNames coll, String taxId) throws Exception {
        MongoCollection collection = getCollection(coll);
        return collection.findDB(getTaxonomyQuery(taxId));
    }

    public static BasicDBList getNeighborsResult(ImportCollectionNames coll, String geneId) throws Exception {
        MongoCollection collection = getCollection(coll);
        return collection.findDB(getNeighborsQuery(geneId));
    }

    public static BasicDBList getPubmedResult(ImportCollectionNames coll, String geneId) throws Exception {
        MongoCollection collection = getCollection(coll);
        return collection.findDB(getPubmedQuery(geneId));
    }

    /**
     * Gets a empty placeholder Gene object with just the geneId
     * loaded. It gets it from the mongo collection ncbigeneinfo.
     *
     * @param geneId
     * @return
     * @throws Exception
     */
    public static Gene getGeneKey(String geneId) throws Exception {
        DBCursor dbCursor = getCollection(
                ImportCollectionNames.NCBI_GENE_INFO).findDBCursor(
                "{" +
                GeneMongoFields.GENE_ID +
                ": \"" + geneId + "\"}");
        Gene gene = new Gene();
        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                //log.info("pubmed");
                gene.setNcbiGeneId(geneId);
            }
        } finally {
            dbCursor.close();
        }
        return gene;
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
    private static String getString(DBObject dbObject, String field) {
        if (dbObject == null || field == null) {
            return null;
        }
        Object obj = dbObject.get(field);
        if (obj == null) {
            return null;
        }
        if (!obj.getClass().equals(String.class)) {
            log.error("Expected String object but found " + obj.getClass().getName() + ", field = " + field);
            return null;
        }
        //log.info(field.toString() + " = " + obj);
        return (String)obj;
    }

    private static String getString(DBObject dbObject, GeneMongoFields field) {
        return getString(dbObject, field.toString());
    }

    private static String getString(DBObject dbObject, TaxonomyMongoFields field) {
        return getString(dbObject, field.toString());
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
    private static BasicDBObject getDBObject(DBObject dbObject, GeneMongoFields field) {
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
    private static BasicDBList getBasicDBList(DBObject dbObject, String field) {
        if (dbObject == null || field == null) {
            throw new RuntimeException("dbObject is null for field " + field);
        }
        Object obj = dbObject.get(field);
        if (obj == null) {
            return null;
        }
        return MongoObjects.getBasicDBList(obj);
    }

    private static BasicDBList getBasicDBList(DBObject dbObject, GeneMongoFields field) {
        return getBasicDBList(dbObject, field.toString());
    }

    private static BasicDBList getBasicDBList(DBObject dbObject, TaxonomyMongoFields field) {
        return getBasicDBList(dbObject, field.toString());
    }

    private static boolean isList(DBObject dbObject, GeneMongoFields field) {
        if (dbObject == null || field == null) {
            throw new RuntimeException("dbObject is null for field ");
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

    public static NcbiTaxonomy getLightNcbiTaxonomy(Subgraph subgraph, String taxId) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        NcbiTaxonomy ncbiTaxonomy = new NcbiTaxonomy();
        ncbiTaxonomy.setNcbiTaxId(taxId);
        subgraph.add(ncbiTaxonomy);
        return ncbiTaxonomy;
    }

    /**
     * This method is used ProteinOntologyImport when NcbiTaxon document is found
     * in the ontology document.
     * @param taxId
     * @param result 
     */
    public static void processOntologyDoc(String taxId, BasicDBObject result) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, UnknownHostException, HttpException {
         Subgraph subGraph = new Subgraph();
         NcbiTaxonomy ncbiTaxonomy = getNcbiTaxonomy(subGraph, taxId, result); 
         PersistenceTemplate.saveSubgraph(subGraph);
    }
    
    public static NcbiTaxonomy getNcbiTaxonomy(Subgraph subgraph, String taxId) throws Exception {
        BasicDBList result = getTaxonomyResult(ImportCollectionNames.NCBI_TAXONOMY, taxId);
        BasicDBObject obj;
        if (!result.isEmpty()) {
            obj = (BasicDBObject) result.get(0);
            return getNcbiTaxonomy(subgraph, taxId, obj);
        } else return null;
    }
    
    public static NcbiTaxonomy getNcbiTaxonomy(Subgraph subgraph, String taxid, BasicDBObject obj) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        NcbiTaxonomy ncbiTaxonomy = new NcbiTaxonomy();
        //log.info("the ncbiTaxId = " + getString(obj, TaxonomyMongoFields.ID));
        ncbiTaxonomy.setNcbiTaxId(getString(obj, TaxonomyMongoFields.ID));
        ncbiTaxonomy.setName(getString(obj, TaxonomyMongoFields.NAME));
        String propertyValue = getString(obj, TaxonomyMongoFields.PROPERTY_VALUE);
        //log.info("propertyValue =[  " + propertyValue);
        String[] str;
        if (propertyValue != null) {
            str = propertyValue.split(TaxonomyMongoFields.COLON.toString());
            ncbiTaxonomy.setTaxonomyRank(TaxonomyRank.fromString(str[1]));
        }
        ncbiTaxonomy.setXref(getString(obj, TaxonomyMongoFields.XREF));
        BasicDBList synonymList = getBasicDBList(obj, TaxonomyMongoFields.SYNONYM_LIST);
        StringBuilder related = new StringBuilder();
        StringBuilder exact = new StringBuilder();
        if (synonymList != null && synonymList.size() > 0) { 
            for (Object synObj : synonymList) {
                BasicDBObject syn = (BasicDBObject)synObj;
                String synVal = getString(syn, TaxonomyMongoFields.SYNONYM);
                if (synVal.contains(TaxonomyMongoFields.RELATED.toString())) {
                    String[] synStr = synVal.split("\"");
                    if (synStr.length > 1) {
                        related.append(synStr[1]).append(" ");
                    } else {
                        log.warn("We may have unexpected text in Synonym list: " + synVal);
                    }
                } else {
                    if (synVal.contains(TaxonomyMongoFields.EXACT.toString())) {
                        String[] synStr = synVal.split("\"");
                        if (synStr.length > 1) {
                            exact.append(synStr[1]).append(" ");
                        } else {
                            log.warn("We may have unexpected text in Synonym list: " + synVal);
                        }
                    }
                }
            }
        }
        ncbiTaxonomy.setTaxonomyRelatedSynonyms(related.toString().trim());
        ncbiTaxonomy.setTaxonomyExactSynonyms(exact.toString().trim());
        ncbiTaxonomy.setIsA(getLightNcbiTaxonomy(subgraph, getString(obj, TaxonomyMongoFields.IS_A)));
        subgraph.add(ncbiTaxonomy);
        return ncbiTaxonomy;
    }

    public static Gene getGene(Subgraph subgraph, BasicDBObject result) throws Exception {
        String symbol = getString(result, GeneMongoFields.SYMBOL);
        //log.info("getGene() =" + symbol);
        Gene gene = new Gene();
        gene.setGeneSymbol(symbol);
        String ncbiGeneId = getString(result, GeneMongoFields.GENE_ID);
        gene.setNcbiGeneId(ncbiGeneId);
        String taxIdNum = getString(result, GeneMongoFields.TAX_ID);
        String taxId = GeneMongoFields.NCBI_TAXON.toString() + TaxonomyMongoFields.COLON + getString(result, GeneMongoFields.TAX_ID);
        log.info("taxId = " + taxId);
        gene.setNcbiTaxonomy(getNcbiTaxonomy(subgraph, taxId)); 
        String description = getString(result, GeneMongoFields.DESCRIPTION);
        gene.setDescription(description);
        String typeOfGene = getString(result, GeneMongoFields.TYPE_OF_GENE);
        gene.setTypeOfGene(typeOfGene);
        String modDate = getString(result, GeneMongoFields.MOD_DATE);
        gene.setModDate(modDate);
        String locusTag = getString(result, GeneMongoFields.LOCUS_TAG);
        if (locusTag != null && !locusTag.isEmpty()) {
           gene.setLocusTag(locusTag);
        }
        String otherDesignations = getString(result, GeneMongoFields.OTHER_DESIGNATIONS);
        if (otherDesignations != null && !otherDesignations.isEmpty()) {
           gene.setOtherDesignations(otherDesignations);
        }
        return gene;
    }

    /**
     * Look at GeneID 814630 in ncbigene2go collection, and you will find that the
     * GOList contains multiple entries. Each entry is an instance of bioentity
     * GeneToGoOntology. Each instance of GeneToGoOntology in turn points to
     * a GeneOntology instance, besides pointing to PubMed.
     *
> db.ncbigene2go.findOne({"GeneID" : "814630"})
{
	"_id" : ObjectId("52424fd20364ea87f3133d1f"),
	"GOList" : [
		{
			"GO_ID" : "GO:0003700",
			"Evidence" : "ISS",
			"GO_term" : "sequence-specific DNA binding transcription factor activity",
			"Category" : "Function",
			"PubMed" : "11118137"
		},
		{
			"GO_ID" : "GO:0005634",
			"Evidence" : "ISM",
			"GO_term" : "nucleus",
			"Category" : "Component"
		},
		{
			"GO_ID" : "GO:0006355",
			"Evidence" : "TAS",
			"GO_term" : "regulation of transcription, DNA-dependent",
			"PubMed" : "11118137",
			"Category" : "Process"
		},
		{
			"GO_ID" : "GO:0006499",
			"Evidence" : "RCA",
			"GO_term" : "N-terminal protein myristoylation",
			"Category" : "Process"
		},
		{
			"GO_ID" : "GO:0006635",
			"Evidence" : "RCA",
			"GO_term" : "fatty acid beta-oxidation",
			"Category" : "Process"
		},
		{
			"GO_ID" : "GO:0006891",
			"Evidence" : "RCA",
			"GO_term" : "intra-Golgi vesicle-mediated transport",
			"Category" : "Process"
		},
		{
			"GO_ID" : "GO:0016558",
			"Evidence" : "RCA",
			"GO_term" : "protein import into peroxisome matrix",
			"Category" : "Process"
		}
	],
	"GeneID" : "814630",
	"tax_id" : "3702"
}

     *
     * @param subgraph
     * @param gene
     * @return
     * @throws Exception
     */
    public static GeneToGo updateGene2Go(Subgraph subgraph, Gene gene) throws Exception {
        String geneId = gene.getNcbiGeneId();
        log.info("geneId = " + geneId);
        BasicDBList result = getGene2GoResult(ImportCollectionNames.NCBI_GENE2GO, geneId);
        if (result == null || result.isEmpty()) {
            return null;
        }
        GeneToGo geneToGo = new GeneToGo();
        geneToGo.setNodeType(BioTypes.GENE_TO_GO);
        BasicDBObject gene2GoEntry = (BasicDBObject)result.get(0);
        String ncbiTaxId = GeneMongoFields.NCBI_TAXON.toString() + TaxonomyMongoFields.COLON + getString(gene2GoEntry, GeneMongoFields.TAX_ID);
        geneToGo.setNcbiTaxonomy(getNcbiTaxonomy(subgraph, ncbiTaxId));
        BasicDBList goList = getBasicDBList(gene2GoEntry, GeneMongoFields.GO_LIST);
        if (goList == null || goList.isEmpty()) {
            return null;
        }
        HashSet<GeneToGoOntology> ontologies = new HashSet<GeneToGoOntology>();
        for (Object obj : goList) {
            BasicDBObject goEntry = (BasicDBObject)obj;
            GeneToGoOntology geneToGoOntology = new GeneToGoOntology();
            geneToGoOntology.setCategory(getString(goEntry, GeneMongoFields.CATEGORY));
            geneToGoOntology.setEvidence(getString(goEntry, GeneMongoFields.EVIDENCE));
            geneToGoOntology.setGene(gene);
            geneToGoOntology.setGoTerm(getString(goEntry, GeneMongoFields.GO_TERM));
            String pubMedId = getString(goEntry, GeneMongoFields.PUBMED);
            if (pubMedId != null) {
                log.info("pubMedId = " + pubMedId);
                PubMed pubMed = PubMedUtil.getPubmed(pubMedId, subgraph);
                if (pubMed == null) {
                    PubMedMongoUtil.loadPubMed(pubMedId);
                    pubMed = PubMedUtil.getPubmed(pubMedId, subgraph);
                }
                if (pubMed != null) {
                    geneToGoOntology.setPubMed(pubMed);
                } else {
                    log.warn("Could not even load pubmed article " + pubMedId);
                }
            }
            String goId = getString(goEntry, GeneMongoFields.GO_ID);
            log.info("goId = " + goId);
            GeneOntology geneOntology = GeneOntologyObo.getGeneOntology(goId, subgraph);
            geneToGoOntology.setGeneOntology(geneOntology);
            subgraph.add(geneToGoOntology);
            CompoundKey compoundKey = CompoundKey.getCompoundKey(geneToGoOntology);
            //StatusUtil.idInsert(BioTypes.GENE_TO_GO_ONTOLOGY.toString(), compoundKey.getKey(), compoundKey.getValue());
            ontologies.add(geneToGoOntology);
            
        }
        geneToGo.setOntologies(ontologies);
        geneToGo.setGene(gene);
        subgraph.add(geneToGo);
        CompoundKey compoundKey = CompoundKey.getCompoundKey(geneToGo);
        //StatusUtil.idInsert(BioTypes.GENE_TO_GO.toString(), compoundKey.getKey(), compoundKey.getValue());
        return geneToGo;
    }

    public static void updatePubMed(Subgraph subgraph, Gene gene) throws Exception {
        BasicDBList gene2PubmedList = getPubmedResult(ImportCollectionNames.NCBI_GENE2PUBMED, gene.getNcbiGeneId());
        for (Object obj : gene2PubmedList) {
            BasicDBObject gene2Pubmed = (BasicDBObject)obj;
            BasicDBList pubmedList = getBasicDBList(gene2Pubmed, GeneMongoFields.PUBMED_LIST);
            for (Object obj1 : pubmedList) {
                BasicDBObject pubMedObj = (BasicDBObject)obj1;
                PubMed pubMed = PubMedUtil.getPubmed(getString(pubMedObj, GeneMongoFields.PUBMED_ID), subgraph);
            }
        }
    }

    public static void updateGeneGroup(Subgraph subgraph, Gene gene) throws Exception {
        BasicDBList geneGroupList = getResult(ImportCollectionNames.NCBI_GENEGROUP, gene.getNcbiGeneId());
        if (geneGroupList == null || geneGroupList.isEmpty()) {
            return;
        }
        BasicDBObject geneGroup = (BasicDBObject)geneGroupList.get(0);
        BasicDBList geneList = getBasicDBList(geneGroup, GeneMongoFields.GENE_LIST);
        BasicDBObject obj = (BasicDBObject)geneList.get(0);
        gene.setRelationship(getString(obj, GeneMongoFields.RELATIONSHIP));
        gene.setOtherGeneId(getString(obj,GeneMongoFields.OTHER_GENE_ID));
        gene.setOtherTaxTd(getString(obj, GeneMongoFields.OTHER_TAX_ID));
    }

    public static void updateGeneNeighbors(Subgraph subgraph, Gene gene) throws Exception {
        BasicDBList geneNeighbors = getNeighborsResult(ImportCollectionNames.NCBI_GENE_NEIGHBORS, gene.getNcbiGeneId());
        if (geneNeighbors == null || geneNeighbors.isEmpty()) {
            return;
        }
        BasicDBObject geneNeighbor = (BasicDBObject)geneNeighbors.get(0);

        gene.setGenomicGi(getString(geneNeighbor, GeneMongoFields.GENOMIC_GI));
        gene.setGenomicAccessionVersion(getString(geneNeighbor, GeneMongoFields.GENOMIC_ACCESSION_VERSION));
        gene.setStartPosition(getString(geneNeighbor, GeneMongoFields.START_POSITION));
        gene.setEndPosition(getString(geneNeighbor, GeneMongoFields.END_POSITION));
        gene.setOrientation(getString(geneNeighbor, GeneMongoFields.ORIENTATION));
        gene.setChromosome(getString(geneNeighbor, GeneMongoFields.CHROMOSOME));
        gene.setDistanceToLeft(getString(geneNeighbor, GeneMongoFields.DISTANCE_TO_LEFT));
        gene.setDistanceToRight(getString(geneNeighbor, GeneMongoFields.DISTANCE_TO_RIGHT));

        BasicDBList leftGeneList = getBasicDBList(geneNeighbor, GeneMongoFields.GENE_IDS_ON_LEFT);
        if (leftGeneList != null) {
            HashSet<Gene> leftGenes = new HashSet<Gene>();
            for (Object obj : leftGeneList) {
                addLightGene((String)obj, subgraph, leftGenes);
                /*
                String geneId = (String)obj;
                Gene aGene = new Gene();
                aGene.setNcbiGeneId(geneId);
                if (geneId.equals("118")) { 
                    log.info("leftGenes, geneId is 118 =" + geneId);
                }
                BasicDBList result = getGeneIdResult(ImportCollectionNames.NCBI_GENE_INFO, geneId);
                if (result == null || result.isEmpty()) {
                    break;
                }
                BasicDBObject geneResult = (BasicDBObject)result.get(0);
                aGene.setGeneSymbol(getString(geneResult, GeneMongoFields.SYMBOL));
                subgraph.add(aGene);
                leftGenes.add(aGene);
                */
            }
            gene.setLeftGenes(leftGenes);
        }

        BasicDBList rightGeneList = getBasicDBList(geneNeighbor, GeneMongoFields.GENE_IDS_ON_RIGHT);
        if (rightGeneList != null) {
            HashSet<Gene> rightGenes = new HashSet<Gene>();
            for (Object obj : rightGeneList) {
                addLightGene((String)obj, subgraph, rightGenes);
                /*
                String geneId = (String)obj;
                Gene aGene = new Gene();
                aGene.setNcbiGeneId(geneId);
                if (geneId.equals("118")) {
                    log.info("rightGeneList, geneId is 118 =" + geneId);
                }
                BasicDBList result = getGeneIdResult(ImportCollectionNames.NCBI_GENE_INFO, geneId);
                if (result == null || result.isEmpty()) {
                    break;
                }
                BasicDBObject geneResult = (BasicDBObject)result.get(0);
                aGene.setGeneSymbol(getString(geneResult, GeneMongoFields.SYMBOL));
                subgraph.add(aGene);
                rightGenes.add(aGene);
                */
            }
            gene.setRightGenes(rightGenes);
        }

        BasicDBList overlappingGeneList = getBasicDBList(geneNeighbor, GeneMongoFields.OVERLAPPING_GENE_IDS);
        if (overlappingGeneList != null) {
            HashSet<Gene> overlappingGenes = new HashSet<Gene>();
            for (Object obj : overlappingGeneList) {
                addLightGene((String)obj, subgraph, overlappingGenes);
                /*
                String geneId = (String)obj;
                Gene aGene = new Gene();
                aGene.setNcbiGeneId(geneId);
                if (geneId.equals("118")) {
                    log.info("overlappingGenes, geneId is 118 =" + geneId);
                }
                BasicDBList result = getGeneIdResult(ImportCollectionNames.NCBI_GENE_INFO, geneId);
                if (result == null || result.isEmpty()) {
                    break;
                }
                BasicDBObject geneResult = (BasicDBObject)result.get(0);
                aGene.setGeneSymbol(getString(geneResult, GeneMongoFields.SYMBOL));
                String taxId = GeneMongoFields.NCBI_TAXON.toString() + TaxonomyMongoFields.COLON + getString(geneResult, GeneMongoFields.TAX_ID);
                //log.info("taxId = " + taxId);
                aGene.setNcbiTaxonomy(getNcbiTaxonomy(subgraph, taxId)); 
                subgraph.add(aGene);
                overlappingGenes.add(aGene);
                * 
                */
            }
            gene.setOverlappingGenes(overlappingGenes);
        }
    }
    
    public static void addLightGene(String geneId, Subgraph subgraph, HashSet geneSet) throws Exception {
         Gene aGene = new Gene();
         if (aGene != null && geneId != null) { 
                aGene.setNcbiGeneId(geneId);
                if (geneId.equals("118")) {
                    log.info("overlappingGenes, geneId is 118 =" + geneId);
                }
                BasicDBList result = getGeneIdResult(ImportCollectionNames.NCBI_GENE_INFO, geneId);
                if (result == null || result.isEmpty()) {
                   return;
                } else { 
                    BasicDBObject geneResult = (BasicDBObject)result.get(0);
                    aGene.setGeneSymbol(getString(geneResult, GeneMongoFields.SYMBOL));
                    String taxId = GeneMongoFields.NCBI_TAXON.toString() + TaxonomyMongoFields.COLON + getString(geneResult, GeneMongoFields.TAX_ID);
                    //log.info("taxId = " + taxId);
                    aGene.setNcbiTaxonomy(getNcbiTaxonomy(subgraph, taxId));
                    subgraph.add(aGene);
                    geneSet.add(aGene);
                }
           }
    }

    /**
     * This method is called by outside clients, as long as they know the
     * HUGOGeneSymbol or symbol, and this should work fine. Except the
     * symbol matches multiple genes in the ncbigeneinfo collection.
     *
     * symbol is not unique in ncbigeneinfo collection, but ncbigeneid is.
     * So we will return a list of matching genes for a given symbol. There will
     * be overlaps, so it is the user's responsibility to merge the result.
     * Please note the left genes, right genes and overlapping genes that are
     * related to the gene in question are light genes. This is to prevent
     * recursion in subgraph.
     *
     * @param symbol
     * @param subgraph
     * @return
     * @throws Exception
     */
    public static HashSet<Gene> getGene(String symbol, Subgraph subgraph) throws Exception {
        BasicDBList geneList = getResult(ImportCollectionNames.NCBI_GENE_INFO, symbol);
        HashSet<Gene> genes = new HashSet<Gene>();
        for (Object obj : geneList) {
            BasicDBObject result = (BasicDBObject)obj;
            Gene gene = getGene(subgraph, result);
            subgraph.add(gene);
            GeneToGo geneToGo = updateGene2Go(subgraph, gene);
            //BasicDBList gene2Pubmed = getResult(ImportCollectionNames.NCBI_GENE2PUBMED, gene.getNcbiGeneId());
            updatePubMed(subgraph, gene);
            updateGeneGroup(subgraph, gene);
            updateGeneNeighbors(subgraph, gene);
            genes.add(gene);
        }
        return genes;
    }
   
    /**
     * Utility function that matches the NCBITaxOn ID in the geneSet
     * and returns matching Gene.
     * getGene
     * @param geneSet
     * @param taxId - eg: NCBITaxon: 9606
     * @return Gene
     * @throws Exception 
     */
     public static Gene getGene(HashSet<Gene> geneSet, String taxId) throws Exception {
        for (Object obj : geneSet.toArray()) {
            Gene gene = (Gene)obj;
            NcbiTaxonomy ncbiTaxonomy = gene.getNcbiTaxonomy();
            if (ncbiTaxonomy != null) {
                String ncbiTaxId = ncbiTaxonomy.getNcbiTaxId();
                if (ncbiTaxId != null) {
                    if (ncbiTaxId.equals(taxId)) {
                        return gene;
                    }
                }
            }
        }
        return null;
    }

    /**
     * This method is called by outside clients as long as they know the
     * ncbigeneId, which they usually don't know.
     *
     * Since an ncbiGeneId is unique in ncbigeneinfo collection, we should
     * get only one gene when an ncbigeneId is specified.
     * Please note the left genes, right genes and overlapping genes that are
     * related to the gene in question are light genes. This is to prevent
     * recursion in subgraph.
     *
     * @param ncbiGeneId
     * @param subgraph
     * @return
     * @throws Exception
     */
    public static Gene getGeneById(String ncbiGeneId, Subgraph subgraph) throws Exception {
        BasicDBList geneList = getGeneIdResult(ImportCollectionNames.NCBI_GENE_INFO, ncbiGeneId);
        if (geneList.isEmpty()) {
          log.error("Did not find a gene list for ncbiGeneId " + ncbiGeneId);
          return null;
        } else {
          log.info("Found a gene list for ncbiGeneId " + ncbiGeneId);
        }
        BasicDBObject result = (BasicDBObject)geneList.get(0);
        Gene gene = getGene(subgraph, result);
        if (gene.getNcbiGeneId().equals("118")) { 
            log.info("ncbiGeneId=" + ncbiGeneId + " Gene=" + gene.toString());
        }
        subgraph.add(gene);
        CompoundKey compoundKey = CompoundKey.getCompoundKey(gene);
        //StatusUtil.idInsert(BioTypes.GENE.toString(), compoundKey.getKey(), compoundKey.getValue());
        updateGene2Go(subgraph, gene);
        //BasicDBList gene2Pubmed = getResult(ImportCollectionNames.NCBI_GENE2PUBMED, gene.getNcbiGeneId());
        updatePubMed(subgraph, gene);
        updateGeneGroup(subgraph, gene);
        updateGeneNeighbors(subgraph, gene);
        return gene;
    }

    /**
     * Load all the genes. Ignore genes marked with NEWENTRY for now.
     * Note there are lots of duplicates, but some fields are different, so
     * need to merge in those cases. The subgraph will take care of merging.
     * Please note that multiple documents in ncbigeneinfo share the same
     * symbol field. So there are duplicates. What will happen though,
     *
     * @throws UnknownHostException
     */
    public static void loadGenes() throws UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.NCBI_GENE_INFO).findDBCursor("{}" );

        Subgraph subgraph = new Subgraph();

        int cntr = 0;

        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {

                if (cntr++ > 500) break;
                //start = System.currentTimeMillis();
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                //end = System.currentTimeMillis();
                //System.out.printf("%s completed in %dms%n", "dbCursor.next()", end - start);
                String symbol = getString(result, GeneMongoFields.SYMBOL);
                if (GeneMongoFields.NEWENTRY.equals(symbol)) {
                    continue;
                }
                Gene gene = getGene(subgraph, result);   
                subgraph.add(gene);
                CompoundKey compoundKey = CompoundKey.getCompoundKey(gene);
                //StatusUtil.idInsert(BioTypes.GENE.toString(), compoundKey.getKey(), compoundKey.getValue());
                GeneToGo geneToGo = updateGene2Go(subgraph, gene);
                //BasicDBList gene2Pubmed = getResult(ImportCollectionNames.NCBI_GENE2PUBMED, gene.getNcbiGeneId());
                updatePubMed(subgraph, gene);
                updateGeneGroup(subgraph, gene);
                updateGeneNeighbors(subgraph, gene);
                //log.info(gene);
                PersistenceTemplate.saveSubgraph(subgraph);
                log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
                log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
            }
        } finally {
            dbCursor.close();
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
    }

    public static void main(String[] args) throws Exception {
        //PubMedUtil.getPubmed("16341006");
        //PubMedUtil.loadPubmed("20462514");

        // If you want to load all genes, uncomment the line below.
        GeneGraphDBImportUtil.loadGenes();

        // Some individual loads
        Subgraph subgraph = new Subgraph();
        GeneGraphDBImportUtil.getGeneById("814650", subgraph);
        GeneGraphDBImportUtil.getGeneById("814630", subgraph);
        GeneGraphDBImportUtil.getGeneById("40323", subgraph);
        PersistenceTemplate.saveSubgraph(subgraph);

        log.info("Done successfully with the program!");
    }
}