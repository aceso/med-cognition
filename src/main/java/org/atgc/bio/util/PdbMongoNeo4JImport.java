/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBCursor;
import com.mongodb.DBObject;
import org.atgc.bio.ImportCollectionNames;
import org.atgc.bio.MongoClasses;
import org.atgc.bio.PDBFields;
import org.atgc.bio.domain.Author;
import org.atgc.bio.domain.Chain;
import org.atgc.bio.domain.PdbEntity;
import org.atgc.bio.domain.Structure;
import org.atgc.bio.domain.types.StructureEntityTypes;
import org.atgc.bio.repository.RedbasinTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.ext.BioStructure;
import org.atgc.mongod.MongoCollection;
import org.atgc.mongod.MongoObjects;
import org.atgc.mongod.MongoUtil;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.HashSet;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.NotFoundException;

/**
 *
 * @author jtanisha-ee
 */
public class PdbMongoNeo4JImport {

    protected static Log log = LogFactory.getLog(new Object().getClass());

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

    public static BasicDBList getResult(ImportCollectionNames coll, String geneId) throws Exception {
        MongoCollection collection = getCollection(coll);
        return collection.findDB(getGeneQuery(geneId));
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

    private static String getString(DBObject dbObject, PDBFields field) {
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
    private static BasicDBObject getDBObject(DBObject dbObject, Enum field) {
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

    private static BasicDBList getBasicDBList(DBObject dbObject, Enum field) {
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

    private PdbMongoNeo4JImport() {
    }

    /**
     * The goal is to parse the following types of weird strings.
     *
     * <pre>
       "@structure_authors" : "Rangarajan, E.S., Bhatia, S., Watson, D.C., Munger, C., Cygler, M., Matte, A., Young, N.M., Montreal-Kingston Bacterial Structural Genomics Initiative (BSGI)",
       "@citation_authors" : "Rangarajan, E.S., Bhatia, S., Watson, D.C., Munger, C., Cygler, M., Matte, A., Young, N.M.",
       </pre>
       *
     * @param authors
     * @return
     */
    public static HashSet<Author> getAuthors(String authors) {
        HashSet<Author> authorSet = new HashSet<Author>();
        if (authors == null) {
            return null;
        }
        String[] st2 = authors.split("\\.,");
        for (String st : st2) {
            Author author = new Author();
            authorSet.add(author);
            String name = st.trim();
            log.info("name = " + name);
            String[] st1 = name.split(",");
            if (st1.length != 2) {
               log.warn("name split array not size 2, structureId "  + ", name = " + name);
               return null;
            }

            String lastName = st1[0].trim();
            if (lastName.length() > 1) {
                log.info("lastName = " + lastName);
                author.setLastName(lastName);
            } else {
                log.warn("lastName is not greater than length 1, structureId "  + ", authors = " + authors);
                return null;
            }
            String rem = st1[1].trim();
            log.info("rem = " + rem);
            if (rem.contains(".")) {
                log.info("rem contains .");
                String[] str = rem.split("\\.");
                if (str.length > 0) {
                    String initials = str[0].trim();
                    log.info("initials = " + initials);
                    author.setInitials(initials);
                }
                if (str.length > 1) {
                    String foreName = str[1].trim();
                    log.info("foreName = " + foreName);
                    author.setForeName(foreName);
                }
            } else {
                log.info("foreName = " + rem);
                author.setForeName(rem);
            }
        }
        return authorSet;
    }

    /**
     * The goal is to parse the following types of weird strings.
     *
     * <pre>
       "@structure_authors" : "Rangarajan, E.S., Bhatia, S., Watson, D.C., Munger, C., Cygler, M., Matte, A., Young, N.M., Montreal-Kingston Bacterial Structural Genomics Initiative (BSGI)",
       "@citation_authors" : "Rangarajan, E.S., Bhatia, S., Watson, D.C., Munger, C., Cygler, M., Matte, A., Young, N.M.",
       </pre>
     *
     * @param subgraph
     * @param structureId
     * @param result
     * @param authorField
     * @return
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnknownHostException
     */
    private static HashSet<Author> getAuthors(Subgraph subgraph, String structureId, DBObject result, PDBFields authorField) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
        String authors = getString(result, authorField);
        if (authors == null) {
            return null;
        }
        HashSet<Author> authorList = getAuthors(authors);
        if (authorList == null) {
            log.warn("Could not parse authors = " + authors + ", structureId = " + structureId);
            return null;
        }
        for (Author author : authorList) {
            subgraph.add(author);
        }
        return authorList;
    }

    /**
     * Typically chainId is "A", "B", "C", ... etc.
     *
     * @param structure
     * @param chainId
     * @return
     */
    private static Chain getChain(Structure structure, String chainId) {
        HashSet<Chain> chains = structure.getChains();
        for (Chain chain : chains) {
            if (chain.getChainId().equals(chainId)) {
                return chain;
            }
        }
        return null;
    }

    /**
     * In the alignment array, we are going to look for as many elements as are the number
     * of chains in this structure. It's possible because of PDB issues, the number of chains
     * in the structure may not match the number of elements in the alignment array.
     * In such cases, we will just try to get whatever we can find. Perhaps at a future date
     * when PDB fixes their issue, we will go ahead and re-import it correctly. As of this date,
     * PDB has already confirmed that they have fixed it, however, we have not yet checked
     * if that has been fixed. We only assume that it has been fixed. But the code does not
     * assume it. Only we assume it. If the alignment array has more elements than the number
     * of chains in the structure, then we just ignore the remaining elements. We again assume that
     * there is a problem with the PDB data.
     *
     * @param subgraph
     * @param structure
     * @param query
     * @throws NotFoundException
     * @throws NoSuchFieldException
     * @throws IllegalArgumentException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws UnknownHostException
     */
    private static void loadStructureUniprot(Subgraph subgraph, Structure structure, DBObject query)
            throws NotFoundException,
                   NoSuchFieldException,
                   IllegalArgumentException,
                   IllegalAccessException,
                   InvocationTargetException,
                   UnknownHostException,
                   Exception {

        DBObject uniprotResult = getCollection(ImportCollectionNames.PDB_UNIPROT).findOneDB(query);
        if (uniprotResult == null) {
            log.warn("Found no uniprot mapings for structureId " + structure.getStructureId());
            return;
        }
        HashSet<Chain> chains = structure.getChains();
        BasicDBList alignmentList = getBasicDBList(uniprotResult, PDBFields.ALIGNMENT);
        int numElements = chains.size();
        int cntr = 0;
        for (Object alignmentItem : alignmentList) {
            cntr++;
            if (cntr > numElements) {
                log.warn("Found too many elements in alignment " + cntr + ", structureId = " + structure.getStructureId() + ", ignoring the rest for now. Must be a PDB issue, not ours.");
                break;
            }
            BasicDBObject alignItem = (BasicDBObject)alignmentItem;
            log.info("alignItem = " + alignItem.toString());
            BasicDBList blockList = getBasicDBList(alignItem, PDBFields.BLOCK);
            for (Object blockObj : blockList) {
                BasicDBObject block = (BasicDBObject)blockObj;
                BasicDBList segment = getBasicDBList(block, PDBFields.SEGMENT);
                if (segment.size() != 2) {
                    log.warn("Segment must have two elements, structureId = " + structure.getStructureId());
                    continue;
                }
                DBObject firstItem = (DBObject)segment.get(0);
                String structureName = getString(firstItem, PDBFields.INT_OBJECT_ID);
                if (!structureName.contains(".")) {
                    log.warn("intObjectId must have dot, structureId = " + structure.getStructureId());
                    continue;
                }
                String[] str = structureName.split(".");
                if (str.length != 2) {
                    log.warn("intObjectId must have two elements, structureId = " + structure.getStructureId());
                    continue;
                }
                //String structureId = str[0];
                String chainId = str[1];
                Chain chain = getChain(structure, chainId);
                DBObject secondItem = (DBObject)segment.get(1);
                String uniprotId = getString(secondItem, PDBFields.INT_OBJECT_ID);
                chain.setProtein(uniprotId);
                String start = getString(secondItem, PDBFields.START);
                chain.setStart(new Integer(start));
                String end = getString(secondItem, PDBFields.END);
                chain.setEnd(new Integer(end));
                chain.setAtomLength(BioStructure.getAtomLength(structure.getStructureId(), chainId));
                chain.setAtomSequence(BioStructure.getAtomSequence(structure.getStructureId(), chainId));
            }
        }
    }

    private static void loadStructureEntity(Subgraph subgraph, Structure structure, DBObject query)
            throws NotFoundException,
                   NoSuchFieldException,
                   IllegalArgumentException,
                   IllegalAccessException,
                   InvocationTargetException,
                   UnknownHostException {

        DBObject entityResult = getCollection(ImportCollectionNames.PDB).findOneDB(query);
        String bioassemblies = getString(entityResult, PDBFields.BIO_ASSEMBLIES);
        structure.setBioAssemblies(bioassemblies);
        String structureId = structure.getStructureId();
        log.info("structureId = " + structureId);
        BasicDBList basicDBList = getBasicDBList(entityResult, PDBFields.ENTITY);
        HashSet<Chain> structureChains = new HashSet<Chain>();
        for (Object entityObj : basicDBList) {
            PdbEntity pdbEntity = new PdbEntity();
            String entityId = getString((BasicDBObject)entityObj, PDBFields.PDB_ID);
            pdbEntity.setEntityId(entityId);
            String entityType = getString((BasicDBObject)entityObj, PDBFields.ENTITY_TYPE);
            pdbEntity.setEntityType(StructureEntityTypes.fromString(entityType));
            pdbEntity.setStructureId(structureId);
            subgraph.add(pdbEntity);
            HashSet<Chain> chains = new HashSet<Chain>();
            BasicDBList chainList = getBasicDBList((BasicDBObject)entityObj, PDBFields.CHAIN);
            for (Object chainObj : chainList) {
                Chain chain = new Chain();
                chain.setStructureId(structureId);
                chain.setChainId(getString((BasicDBObject)chainObj, PDBFields.PDB_ID));
                subgraph.add(chain);
                chains.add(chain);
                structureChains.add(chain);
            }
            pdbEntity.setChains(chains);
        }
        structure.setChains(structureChains);
    }

    private static Structure getStructure(Subgraph subgraph, BasicDBObject desc) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnknownHostException {
        Structure structure = new Structure();
        String structureId = getString(desc, PDBFields.STRUCTURE_ID);
        structure.setStructureId(structureId);
        String title = getString(desc, PDBFields.TITLE);
        structure.setTitle(title);
        String pubmedId = getString(desc, PDBFields.PUBMED_ID);
        structure.setPubmedId(pubmedId);
        String expMethod = getString(desc, PDBFields.EXP_METHOD);
        structure.setExpMethod(expMethod);
        String resolution = getString(desc, PDBFields.RESOLUTION);
        structure.setResolution(resolution);
        String keywords = getString(desc, PDBFields.KEYWORDS);
        structure.setKeywords(keywords);
        String nrEntities = getString(desc, PDBFields.NR_ENTITIES);
        structure.setNrEntities(nrEntities);
        String nrResidues = getString(desc, PDBFields.NR_RESIDUES);
        structure.setNrResidues(nrResidues);
        String nrAtoms = getString(desc, PDBFields.NR_ATOMS);
        structure.setNrAtoms(nrAtoms);
        String depositionDate = getString(desc, PDBFields.DEPOSITION_DATE);
        structure.setDepositionDate(depositionDate);
        String releaseDate = getString(desc, PDBFields.RELEASE_DATE);
        structure.setReleaseDate(releaseDate);
        String lastModificationDate = getString(desc, PDBFields.LAST_MODIFICATION_DATE);
        structure.setLastModificationDate(lastModificationDate);
        HashSet<Author> structureAuthors = getAuthors(subgraph, structureId, desc, PDBFields.STRUCTURE_AUTHORS);
        if (structureAuthors == null) { // if not parseable
            structure.setStructureAuthors(getString(desc, PDBFields.STRUCTURE_AUTHORS));
        } else {
            structure.setStructureAuthorSet(structureAuthors);
        }
        HashSet<Author> citationAuthors = getAuthors(subgraph, structureId, desc, PDBFields.CITATION_AUTHORS);
        if (citationAuthors == null) { // if not parseable
            structure.setCitationAuthors(getString(desc, PDBFields.CITATION_AUTHORS));
        } else {
            structure.setCitationAuthorSet(citationAuthors);
        }
        String status = getString(desc, PDBFields.STATUS);
        structure.setStatus(status);
        subgraph.add(structure);
        return structure;
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
    public static void loadStructures() throws UnknownHostException, Exception {
        DBCursor dbCursor = getCollection(ImportCollectionNames.PDB_DESC).findDBCursor("{}" );

        Subgraph subgraph = new Subgraph();

        int cntr = 0;

        try {
            // we expect only one document match
            while (dbCursor.hasNext()) {
                //if (cntr++ > 5) break;
                //start = System.currentTimeMillis();
                BasicDBObject result = (BasicDBObject)dbCursor.next();
                //end = System.currentTimeMillis();
                //System.out.printf("%s completed in %dms%n", "dbCursor.next()", end - start);
                String structureId = getString(result, PDBFields.STRUCTURE_ID);
                //if (StatusUtil.idExists(BioTypes.STRUCTURE, BioFields.STRUCTURE_ID, structureId)) {
                  //  continue;
                //}
                Structure structure = getStructure(subgraph, result);
                //CompoundKey compoundKey = CompoundKey.getCompoundKey(structure);
                BasicDBObject query = new BasicDBObject();
                query.put(PDBFields.STRUCTURE_ID.toString(), structureId);
                loadStructureEntity(subgraph, structure, query);
                loadStructureUniprot(subgraph, structure, query);
                log.info("structure persistence count = " + ++cntr);
                RedbasinTemplate.saveSubgraph(subgraph);
            }
        } finally {
            dbCursor.close();
        }
    }

    /*
    public static void testAuthors(String authors) {
        String[] st2 = authors.split("\\.,");
        for (String st : st2) {
            Author author = new Author();
            String name = st;
            log.info("name = " + name);
            String[] st1 = name.split(",");
            if (st1.length != 2) {
               log.warn("name split array not size 2, structureId "  + ", name = " + name);
               return;
            }

            String lastName = st1[0];
            if (lastName.length() > 1) {
                log.info("lastName = " + lastName);
                author.setLastName(lastName);
            } else {
                log.warn("lastName is not greater than length 1, structureId "  + ", authors = " + authors);
                return;
            }
            String rem = st1[1];
            log.info("rem = " + rem);
            if (rem.contains(".")) {
                log.info("rem contains .");
                String[] str = rem.split("\\.");
                if (str.length > 0) {
                    String initials = str[0];
                    log.info("initials = " + initials);
                    author.setInitials(initials);
                }
                if (str.length > 1) {
                    String foreName = str[1];
                    log.info("foreName = " + foreName);
                    author.setForeName(foreName);
                }
            } else {
                author.setForeName(rem);
            }
        }
    }
    */

    public static void main(String[] args) throws Exception {
        loadStructures();
        //String authors = "Syvitski, R.T., Jakeman, D.L., Li, Y.";
        //testAuthors(authors);
    }

}
