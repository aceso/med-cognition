
/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import com.mongodb.DBObject;
import org.atgc.bio.*;
import org.atgc.bio.domain.BioRelTypes;
import org.atgc.bio.domain.BioRelation;
import org.atgc.bio.domain.BioTypes;
import org.atgc.bio.domain.BiologicalRole;
import org.atgc.bio.domain.EndStatus;
import org.atgc.bio.domain.ExperimentAttributes;
import org.atgc.bio.domain.ExperimentComment;
import org.atgc.bio.domain.ExperimentDataProcessing;
import org.atgc.bio.domain.ExperimentDataset;
import org.atgc.bio.domain.ExperimentLibrary;
import org.atgc.bio.domain.ExperimentUrl;
import org.atgc.bio.domain.ExperimentalRole;
import org.atgc.bio.domain.Feature;
import org.atgc.bio.domain.FeatureRange;
import org.atgc.bio.domain.FeatureType;
import org.atgc.bio.domain.Intact;
import org.atgc.bio.domain.IntactExperiment;
import org.atgc.bio.domain.IntactInteraction;
import org.atgc.bio.domain.InteractionDetectionMethod;
import org.atgc.bio.domain.Organism;
import org.atgc.bio.domain.Participant;
import org.atgc.bio.domain.ParticipantIdentificationMethod;
import org.atgc.bio.domain.Peptide;
import org.atgc.bio.domain.Protein;
import org.atgc.bio.domain.PubMed;
import org.atgc.bio.domain.SmallMolecule;
import org.atgc.bio.domain.StartStatus;
import org.atgc.bio.repository.PersistenceTemplate;
import org.atgc.bio.repository.Subgraph;
import org.atgc.json.JsonChars;
import org.atgc.mongod.MongoObjects;
import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.InvocationTargetException;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.apache.http.HttpException;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.NotFoundException;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;

/**
 * This is IntactFields import system.
 *
 * @author jtanisha-ee
 */
public class IntactImport {

    //relationships
    private static final String IS_INHIBITOR_OF = "inhibits";
    private static final String IS_PART_OF = "is a part of";
    private static final String IS_PART_OF_PATHWAY = "is a part of pathway";
    private static final String IS_OUTPUT_OF = "produces";
    private static final String IS_INTERACTION_OF = "belongs to this interacion";
    private static final String IS_INPUT_OF = "produces";
    private static final String IS_AGENT_OF = "if agent present";
    private static final String IS_EDGETYPE_OF = "if edge type present";
    private static final String IS_INCOMING_EDGE_OF = "is incoming edge present";
    private static final String IS_OUTGOING_EDGE_OF = "is outgoing edge present";
    private static final String IS_MEMBER_OF = "is a member of";
    private static final String IS_COMPLEX_COMPONENT_OF = "is complex component of";
    private static final String IS_MODIFICATION_OF = "is a modification of";
    private static final String IS_PART_OF_COMPLEX = "is part of complex";
    private static final String IS_IN_FAMILY_OF = "is in family of";

    // indexes
    private static RestGraphDatabase graphDb;
    private static RestIndex<Node> rbidIndex;

    private static final String DB_PATH= "neo4j-shortest-path";
    public static final String NCI_ID = "@id";
    public static final String NCI_VALUE = "@value";
    public static final String MESSAGE = "message";

    protected static Logger log = LogManager.getLogger(IntactImport.class);

    private static void setup() throws URISyntaxException {
        graphDb = new RestGraphDatabase(BioEntityType.DB_URL.toString());
        registerShutdownHook( graphDb );
    }

    private static void registerShutdownHook( RestGraphDatabase graphDb1) {
    // Registers a shutdown hook for the Neo4j instance so that it
    // shuts down nicely when the VM exits (even if you "Ctrl-C" the
    // running example before it's completed)
        Runtime.getRuntime().addShutdownHook( new Thread() {
        @Override
        public void run() {
            graphDb.shutdown();
        }
    } );
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
    private static String getString(DBObject dbObject, IntactFields field) {
        if (dbObject == null || field == null) {
            return null;
        }
        Object obj = dbObject.get(field.toString());
        if (obj == null) {
            return null;
        }
        if (!obj.getClass().equals(String.class)) {
            throw new RuntimeException("Expected String object but found " + obj.getClass().getName());
        }
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
    private static BasicDBObject getDBObject(DBObject dbObject, IntactFields field) {
        if ((dbObject == null) || field == null) {
            log.error("dbObject is null for field " + field.toString());
            return null;
        }
        Object obj = dbObject.get(field.toString());
        if (obj == null) {
            return null;
        }
        if (MongoClasses.BasicDBList.equals(obj.getClass()) ||
                MongoClasses.DBObject.equals(obj.getClass())) {
            throw new RuntimeException("Expected a BasicDBObject " + obj.getClass().getName());
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
    private static BasicDBList getBasicDBList(DBObject dbObject, IntactFields field) {
        if (dbObject == null || field == null) {
            throw new RuntimeException("dbObject is null for field " + field.toString());
        }
        Object obj = dbObject.get(field.toString());
        if (obj == null) {
            return null;
        }
        return MongoObjects.getBasicDBList(obj);
    }

    private static BioTypes getBioType(DBObject interactor) {
        DBObject interactorType = getDBObject(interactor, IntactFields.INTERACTOR_TYPE);
        String shortLabel = getString(getDBObject(interactorType, IntactFields.NAMES), IntactFields.SHORT_LABEL);
        //log.info("shortLabel = " + shortLabel);
        if (IntactFields.PROTEIN_SHORT_LABEL.equals(shortLabel)) {
            return BioTypes.PROTEIN;
        } else {
            if (IntactFields.PEPTIDE_SHORT_LABEL.equals(shortLabel)) {
                return BioTypes.PEPTIDE;
            } else {
                if (IntactFields.SMALL_MOLECULE.equals(shortLabel)) {
                   return BioTypes.SMALL_MOLECULE;
                } else {
                   throw new RuntimeException("Couldn't map bioType to " + shortLabel);
                }
            }
        }

    }

    private static String getUniprot(DBObject interactor) {
        return getString(getDBObject(getDBObject(interactor, IntactFields.XREF), IntactFields.PRIMARY_REF), IntactFields.ID);
    }

    private static String getIntactSourceId(DBObject interaction) {
        return getString(getDBObject(getDBObject(interaction, IntactFields.XREF), IntactFields.PRIMARY_REF), IntactFields.ID);
    }

    private static String getIntactParticipantId(DBObject dbObj) {
        return getString(getDBObject(getDBObject(dbObj, IntactFields.XREF), IntactFields.PRIMARY_REF), IntactFields.ID);
    }

    private static String getExperimentRef(DBObject interaction) {
        return getString(getDBObject(interaction, IntactFields.EXPERIMENT_LIST), IntactFields.EXPERIMENT_REF);
    }

    private static BasicDBList getParticipantList(DBObject interaction) {
        return getBasicDBList(interaction, IntactFields.PARTICIPANT_LIST);
    }

    private static String getShortLabel(DBObject intactObj) {
        return getString(getDBObject(intactObj, IntactFields.NAMES), IntactFields.SHORT_LABEL);
    }

    private static String getFullName(DBObject interactor) {
        return getString(getDBObject(interactor, IntactFields.NAMES), IntactFields.FULL_NAME);
    }

    private static String getAliases(DBObject interactor) {
        BasicDBList aliasArray = getBasicDBList(getDBObject(interactor, IntactFields.NAMES), IntactFields.ALIAS);
        StringBuilder sb = new StringBuilder();
        if (aliasArray == null) {
            return sb.toString();
        }
        for (String alias : aliasArray.keySet()) {
            if (sb.length() == 0) {
               sb.append(alias);
            } else {
               sb.append(" ").append(alias);
            }
        }
        return sb.toString();
    }

    private static String getAlias(DBObject obj) {
        return getString(getDBObject(getDBObject(obj, IntactFields.NAMES), IntactFields.ALIAS), IntactFields.TEXT);

    }

    private static String getSecondaryRefs(DBObject interactor, IntactSources intactSource) {
        //log.info("interactor = " + interactor);
        //log.info("xref = " + getDBObject(interactor, IntactFields.XREF));
        DBObject xref = getDBObject(interactor, IntactFields.XREF);
        if (xref == null) {
            return null;
        }
        BasicDBList secRefArray = getBasicDBList(xref, IntactFields.SECONDARY_REF);
        if (secRefArray == null) {
            return null;
        }
        StringBuilder sb = new StringBuilder();
        for (Object v : secRefArray) {
            BasicDBObject value = (BasicDBObject)v;
            String source = getString(value, IntactFields.DB);
            if (intactSource.equals(source)) {
                String proteinName = getString(value, IntactFields.INTACT_ID);
                if (sb.length() == 0) {
                   sb.append(proteinName);
                } else {
                   sb.append(" ").append(proteinName);
                }
            }
        }
        return sb.toString();
    }

    /**
     * Check if the @db is the requested IntactSources type (eg. "psi-mi") and if
     * it is, then return the @id, else return an empty string.
     *
     * @param intactObj
     * @param intactSource
     * @return
     */
    private static String getPrimaryRefs(DBObject intactObj, IntactSources intactSource) {
        //log.info("intactObj = " + intactObj);
        BasicDBObject xref = getDBObject(intactObj, IntactFields.XREF);
        //log.info("xref = " + xref);
        BasicDBObject primaryRef = getDBObject(xref, IntactFields.PRIMARY_REF);
        //log.info("primaryRef = " + primaryRef);
        //log.info("intactSource = " + intactSource.toString());
        if (getString(primaryRef, IntactFields.DB).equals(intactSource.toString())) {
            return getString(primaryRef, IntactFields.ID);
        }
        return "";
    }

    private static String getIntactId(DBObject intactObj) {
        return getString(intactObj, IntactFields.ID);
    }

    private static String getExperimentId(DBObject intactObj) {
        return getString(intactObj, IntactFields.EXPERIMENT_ID);
    }

    private static String getInteractionId(DBObject intactObj) {
        return getString(intactObj, IntactFields.INTERACTION_ID);
    }

    private static String getInteractorId(DBObject interactor) {
        return getString(interactor, IntactFields.INTERACTOR_ID);
    }

    private static String getInteractorRef(DBObject dbObj) {
        return getString(dbObj, IntactFields.INTERACTOR_REF);
    }

    private static String getOrganismShortLabel(DBObject interactor) {
        return getString(getDBObject(interactor, IntactFields.ORGANISM), IntactFields.SHORT_LABEL);
    }

    private static String getOrganismFullName(DBObject interactor) {
        return getString(getDBObject(interactor, IntactFields.ORGANISM), IntactFields.FULL_NAME);
    }

    private static String getNcbiTaxId(DBObject interactor) {
        return getString(getDBObject(interactor, IntactFields.ORGANISM), IntactFields.NCBI_TAX_ID);
    }

    private static Protein getProtein(DBObject interactor) {
       Protein protein = new Protein();
       protein.setIntactId(getIntactId(interactor));
       protein.setInteractorId(getInteractorId(interactor));
       //protein.setInteractionId(getInteractionId(interactor));
       protein.setNodeType(BioTypes.PROTEIN);
       protein.setUniprot(getUniprot(interactor));
       protein.setShortLabel(getShortLabel(interactor));
       String fullName = getFullName(interactor);
       protein.setMessage(fullName);
       protein.setFullName(getFullName(interactor));
       protein.setAliases(getAliases(interactor));
       protein.setUniprotSecondaryRefs(getSecondaryRefs(interactor, IntactSources.UNIPROT));
       protein.setIpiSecondaryRefs(getSecondaryRefs(interactor, IntactSources.IPI));
       protein.setInterproSecondaryRefs(getSecondaryRefs(interactor, IntactSources.INTERPRO));
       protein.setGoSecondaryRefs(getSecondaryRefs(interactor, IntactSources.GO));
       protein.setEnsemblSecondaryRefs(getSecondaryRefs(interactor, IntactSources.ENSEMBL));
       protein.setIntactSecondaryRefs(getSecondaryRefs(interactor, IntactSources.INTACT));
       protein.setOrganismShortLabel(getOrganismShortLabel(interactor));
       protein.setRefseqSecondaryRefs(getSecondaryRefs(interactor, IntactSources.REFSEQ));
       protein.setOrganismFullName(getOrganismFullName(interactor));
       protein.setOrganismShortLabel(getOrganismShortLabel(interactor));
       protein.setNcbiTaxId(getNcbiTaxId(interactor));
       return protein;
    }

    private static Peptide getPeptide(DBObject interactor) {
       Peptide peptide = new Peptide();
       peptide.setIntactId(getIntactId(interactor));
       peptide.setInteractorId(getInteractorId(interactor));
       peptide.setNodeType(BioTypes.PEPTIDE);
       peptide.setUniprot(getUniprot(interactor));
       peptide.setShortLabel(getShortLabel(interactor));
       String fullName = getFullName(interactor);
       peptide.setMessage(fullName);
       peptide.setFullName(getFullName(interactor));
       peptide.setAliases(getAliases(interactor));
       peptide.setUniprotSecondaryRefs(getSecondaryRefs(interactor, IntactSources.UNIPROT));
       peptide.setIpiSecondaryRefs(getSecondaryRefs(interactor, IntactSources.IPI));
       peptide.setInterproSecondaryRefs(getSecondaryRefs(interactor, IntactSources.INTERPRO));
       peptide.setGoSecondaryRefs(getSecondaryRefs(interactor, IntactSources.GO));
       peptide.setEnsemblSecondaryRefs(getSecondaryRefs(interactor, IntactSources.ENSEMBL));
       peptide.setIntactSecondaryRefs(getSecondaryRefs(interactor, IntactSources.INTACT));
       peptide.setOrganismShortLabel(getOrganismShortLabel(interactor));
       peptide.setRefseqSecondaryRefs(getSecondaryRefs(interactor, IntactSources.REFSEQ));
       peptide.setOrganismFullName(getOrganismFullName(interactor));
       peptide.setOrganismShortLabel(getOrganismShortLabel(interactor));
       peptide.setNcbiTaxId(getNcbiTaxId(interactor));
       return peptide;
    }

    private static SmallMolecule getSmallMolecule(DBObject interactor) {
       SmallMolecule smallMolecule = new SmallMolecule();
       smallMolecule.setIntactId(getIntactId(interactor));
       smallMolecule.setInteractorId(getInteractorId(interactor));
       smallMolecule.setNodeType(BioTypes.SMALL_MOLECULE);
       smallMolecule.setUniprot(getUniprot(interactor));
       smallMolecule.setShortLabel(getShortLabel(interactor));
       String fullName = getFullName(interactor);
       smallMolecule.setMessage(fullName);
       smallMolecule.setFullName(getFullName(interactor));
       smallMolecule.setAliases(getAliases(interactor));
       smallMolecule.setUniprotSecondaryRefs(getSecondaryRefs(interactor, IntactSources.UNIPROT));
       smallMolecule.setIpiSecondaryRefs(getSecondaryRefs(interactor, IntactSources.IPI));
       smallMolecule.setInterproSecondaryRefs(getSecondaryRefs(interactor, IntactSources.INTERPRO));
       smallMolecule.setGoSecondaryRefs(getSecondaryRefs(interactor, IntactSources.GO));
       smallMolecule.setEnsemblSecondaryRefs(getSecondaryRefs(interactor, IntactSources.ENSEMBL));
       smallMolecule.setIntactSecondaryRefs(getSecondaryRefs(interactor, IntactSources.INTACT));
       smallMolecule.setOrganismShortLabel(getOrganismShortLabel(interactor));
       smallMolecule.setRefseqSecondaryRefs(getSecondaryRefs(interactor, IntactSources.REFSEQ));
       smallMolecule.setOrganismFullName(getOrganismFullName(interactor));
       smallMolecule.setOrganismShortLabel(getOrganismShortLabel(interactor));
       smallMolecule.setNcbiTaxId(getNcbiTaxId(interactor));
       return smallMolecule;
    }

    public static HashSet<PubMed> getPubMedList(Subgraph subgraph, DBObject method) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
       HashSet<PubMed> pubMedList = new HashSet<PubMed>();
       DBObject xref = getDBObject(method, IntactFields.XREF);
       BasicDBList secondaryRef = getBasicDBList(xref, IntactFields.SECONDARY_REF);
       PubMed pubMed;
       for (Object element : secondaryRef) {
           BasicDBObject obj = (BasicDBObject)element;
           if (getString(obj, IntactFields.DB).equals(IntactFields.PUBMED.toString())) {
               String id = getString(obj, IntactFields.ID);
               //log.info("obj = " + obj.toString());
               //log.info("pubMed id = " + id);
               pubMed = new PubMed();
               subgraph.add(pubMed);
               pubMed.setPubMedId(id);
               pubMed.setMessage(id);
               pubMedList.add(pubMed);
           }
       }
       return pubMedList;
    }

    public static String getNodeLabel(BioTypes bioType, String indexValue, String shortLabel) {
        return bioType + "-" + indexValue + "-" + shortLabel;
    }

    public static InteractionDetectionMethod getInteractionDetectionMethod(String experimentId, Subgraph subgraph, DBObject experiment) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
       InteractionDetectionMethod interactionDetectionMethod = new InteractionDetectionMethod();
       subgraph.add(interactionDetectionMethod);
       DBObject method = getDBObject(experiment, IntactFields.INTERACTION_DETECTION_METHOD);
       interactionDetectionMethod.setFullName(getFullName(method));
       String shortLabel = getShortLabel(method);
       interactionDetectionMethod.setExperimentId(experimentId);
       interactionDetectionMethod.setShortLabel(shortLabel);
       interactionDetectionMethod.setMessage(getNodeLabel(BioTypes.INTERACTION_DETECTION_METHOD, experimentId, shortLabel));
       //log.info("method = " + method);
       BasicDBObject names = getDBObject(method, IntactFields.NAMES);
       BasicDBList aliases = getBasicDBList(names, IntactFields.ALIAS);
       if (aliases != null) {
           StringBuilder sb = new StringBuilder();
           for (Object obj : aliases) {
               BasicDBObject alias = (BasicDBObject)obj;
               if (sb.length() > 0) {
                   sb.append(JsonChars.COMMA);
               }
               sb.append(getString(alias, IntactFields.TEXT));
           }
           if (sb.length() > 0) {
               sb.append(JsonChars.COMMA);
           }
           interactionDetectionMethod.setAliases(sb.toString());
       }
       interactionDetectionMethod.setReferencesPubMed(getPubMedList(subgraph, method));
       return interactionDetectionMethod;
    }

    public static ParticipantIdentificationMethod getParticipantIdentificationMethod(Subgraph subgraph, DBObject experiment) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
       ParticipantIdentificationMethod participantIdentificationMethod = new ParticipantIdentificationMethod();
       subgraph.add(participantIdentificationMethod);
       DBObject method = getDBObject(experiment, IntactFields.PARTICIPANT_IDENTIFICATION_METHOD);
       participantIdentificationMethod.setFullName(getFullName(method));
       participantIdentificationMethod.setShortLabel(getShortLabel(method));
       //log.info("method = " + method);
       BasicDBObject names = getDBObject(method, IntactFields.NAMES);
       BasicDBList aliases = getBasicDBList(names, IntactFields.ALIAS);
       if (aliases != null) {
           StringBuilder sb = new StringBuilder();
           for (Object obj : aliases) {
               BasicDBObject alias = (BasicDBObject)obj;
               if (sb.length() > 0) {
                   sb.append(JsonChars.COMMA);
               }
               sb.append(getString(alias, IntactFields.TEXT));
           }
           if (sb.length() > 0) {
               sb.append(JsonChars.COMMA);
           }
           participantIdentificationMethod.setAliases(sb.toString());
       }
       participantIdentificationMethod.setReferencesPubMed(getPubMedList(subgraph, method));
       return participantIdentificationMethod;
    }

    public static Collection<Organism> getHostOrganismList(Subgraph subgraph, DBObject experiment) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
       Collection<Organism> organismList = new HashSet<Organism>();
       //log.info("experiment = " + experiment);
       BasicDBList list = getBasicDBList(experiment, IntactFields.HOST_ORGANISM_LIST);
       //log.info("host organism list = " + list.toString());
       for (Object obj : list) {
           DBObject element = (DBObject)obj;
           Organism organism = new Organism();
           subgraph.add(organism);
           DBObject hostOrganism = getDBObject(element, IntactFields.HOST_ORGANISM);
           String fullName = getFullName(hostOrganism);
           organism.setFullName(fullName);
           organism.setOrganismShortLabel(getOrganismShortLabel(hostOrganism));
           organism.setMessage(fullName);
           organism.setNodeType(BioTypes.ORGANISM);
           String ncbiTaxId = getString(hostOrganism, IntactFields.NCBI_TAX_ID);
           organism.setNcbiTaxId(ncbiTaxId);
           //log.info("ncbiTaxId = " + ncbiTaxId);
           organismList.add(organism);
       }
       return organismList;
    }

    public static PubMed getPubMed(Subgraph subgraph, DBObject experiment) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        PubMed pubMed = new PubMed();
        subgraph.add(pubMed);
        BasicDBObject primaryRef = getDBObject(getDBObject(getDBObject(experiment, IntactFields.BIBREF), IntactFields.XREF), IntactFields.PRIMARY_REF);
        if (getString(primaryRef, IntactFields.DB).equals(IntactFields.PUBMED.toString())) {
            pubMed.setPubMedId(getString(primaryRef, IntactFields.ID));
        }
        return pubMed;
    }

    public static ExperimentAttributes getExperimentAttributes(String experimentRef, Subgraph subgraph, DBObject experiment) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        BasicDBList attributeList = getBasicDBList(experiment, IntactFields.ATTRIBUTE_LIST);
        HashSet<ExperimentDataset> experimentDatasetList = new HashSet<ExperimentDataset>();
        HashSet<ExperimentComment> experimentCommentList = new HashSet<ExperimentComment>();
        HashSet<ExperimentLibrary> experimentLibraryList = new HashSet<ExperimentLibrary>();
        HashSet<ExperimentDataProcessing> experimentDataProcessingList = new HashSet<ExperimentDataProcessing>();
        HashSet<ExperimentUrl> experimentUrlList = new HashSet<ExperimentUrl>();
        ExperimentAttributes experimentAttributes = new ExperimentAttributes();
        subgraph.add(experimentAttributes);
        String authorList = null;
        String journal = null;
        String publicationYear = null;
        for (Object obj : attributeList) {
            BasicDBObject attribute = (BasicDBObject)obj;
            String name = getString(attribute, IntactFields.NAME);
            if (IntactFields.DATASET.equals(name)) {
                ExperimentDataset experimentDataset = new ExperimentDataset();
                subgraph.add(experimentDataset);
                experimentDataset.setExperimentRef(experimentRef);
                experimentDataset.setDataset(name);
                experimentDatasetList.add(experimentDataset);
            } else if (IntactFields.COMMENT.equals(name)) {
                ExperimentComment experimentComment = new ExperimentComment();
                subgraph.add(experimentComment);
                experimentComment.setExperimentRef(experimentRef);
                experimentComment.setComment(name);
                experimentCommentList.add(experimentComment);
            } else if (IntactFields.LIBRARY_USED.equals(name)) {
                ExperimentLibrary experimentLibrary = new ExperimentLibrary();
                experimentLibrary.setExperimentRef(experimentRef);
                subgraph.add(experimentLibrary);
                experimentLibrary.setLibrary(name);
                experimentLibraryList.add(experimentLibrary);
            } else if (IntactFields.DATA_PROCESSING.equals(name)) {
                ExperimentDataProcessing experimentDataProcessing = new ExperimentDataProcessing();
                experimentDataProcessing.setExperimentRef(experimentRef);
                subgraph.add(experimentDataProcessing);
                experimentDataProcessing.setDataProcessing(name);
                experimentDataProcessingList.add(experimentDataProcessing);
            } else if (IntactFields.URL.equals(name)) {
                ExperimentUrl experimentUrl = new ExperimentUrl();
                experimentUrl.setExperimentRef(experimentRef);
                subgraph.add(experimentUrl);
                experimentUrl.setUrl(name);
                experimentUrlList.add(experimentUrl);
            }   else if (IntactFields.AUTHOR_LIST.equals(name)) {
                authorList = name;
            } else if (IntactFields.JOURNAL.equals(name)) {
                journal = name;
            } else if (IntactFields.PUBLICATION_YEAR.equals(name)) {
                publicationYear = name;
            }
            experimentAttributes.setHasDataset(experimentDatasetList);
            experimentAttributes.setHasComments(experimentCommentList);
            experimentAttributes.setHasDataProcessing(experimentDataProcessingList);
            experimentAttributes.setUsesLibrary(experimentLibraryList);
            experimentAttributes.setHasUrl(experimentUrlList);
            experimentAttributes.setJournal(journal);
            experimentAttributes.setPublicationYear(publicationYear);
            experimentAttributes.setAuthorList(authorList);
        }
        return experimentAttributes;
    }

    public static IntactExperiment getIntactExperiment(Subgraph subgraph, DBObject experiment) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        IntactExperiment intactExperiment = new IntactExperiment();
        String experimentId = getExperimentId(experiment);
        intactExperiment.setExperimentRef(experimentId);
        String fullName = getFullName(experiment);
        intactExperiment.setFullName(fullName);
        intactExperiment.setMessage(fullName);
        intactExperiment.setNodeType(BioTypes.INTACT_EXPERIMENT);
        intactExperiment.setShortLabel(getShortLabel(experiment));
        intactExperiment.setHasHostOrganisms(getHostOrganismList(subgraph, experiment));
        InteractionDetectionMethod idm = getInteractionDetectionMethod(experimentId, subgraph, experiment);

        intactExperiment.setHasInteractionDetectionMethod(idm);
        ParticipantIdentificationMethod pim = getParticipantIdentificationMethod(subgraph, experiment);
        pim.setExperimentId(experimentId);
        intactExperiment.setHasParticipantIdentificationMethod(pim);
        intactExperiment.setReferencesPubMed(getPubMed(subgraph, experiment));
        ExperimentAttributes experimentAttributes = getExperimentAttributes(experimentId, subgraph, experiment);
        experimentAttributes.setExperimentRef(experimentId);
        intactExperiment.setHasAttributes(experimentAttributes);
        return intactExperiment;
    }

    public static IntactInteraction getIntactInteraction(DBObject interaction) {
        IntactInteraction intactInteraction = new IntactInteraction();
        intactInteraction.setInteractionId(getIntactId(interaction));
        intactInteraction.setShortLabel(getShortLabel(interaction));
        String fullName = getFullName(interaction);
        intactInteraction.setFullName(fullName);
        intactInteraction.setMessage(fullName);
        intactInteraction.setIntactSourceId(getIntactSourceId(interaction));
        intactInteraction.setExperimentId(getExperimentRef(interaction));
        intactInteraction.setNodeType(BioTypes.INTACT_INTERACTION);
        intactInteraction.setSecondaryAFCSSourceIds(getSecondaryRefs(interaction, IntactSources.AFCS));
        return intactInteraction;
    }

    public static Participant getParticipant(DBObject part) {
        Participant participant = new Participant();
        participant.setParticipantId(getString(part, IntactFields.ID));
        String shortLabel = getShortLabel(part);
        participant.setShortLabel(shortLabel);
        participant.setIntactParticipantId(getIntactParticipantId(part));
        participant.setInteractorRef(getInteractorRef(part));
        participant.setMessage(shortLabel);  // since fullName not available
        participant.setNodeType(BioTypes.PARTICIPANT);
        return participant;
    }

    public static BiologicalRole getBiologicalRole(DBObject bioRole, String participantId) {
        BiologicalRole biologicalRole = new BiologicalRole();
        biologicalRole.setShortLabel(getShortLabel(bioRole));
        String fullName = getFullName(bioRole);
        biologicalRole.setFullName(fullName);
        biologicalRole.setMessage(fullName);
        biologicalRole.setNodeType(BioTypes.BIOLOGICAL_ROLE);
        biologicalRole.setParticipantId(participantId);
        biologicalRole.setBiologicalIntactRoleId(getSecondaryRefs(bioRole, IntactSources.INTACT));
        biologicalRole.setBiologicalPubMedRoleId(getSecondaryRefs(bioRole, IntactSources.PUBMED));
        biologicalRole.setBiologicalPsiMiRoleId(getPrimaryRefs(bioRole, IntactSources.PSI_MI));
        return biologicalRole;
    }

    public static ExperimentalRole getExperimentalRole(DBObject expRole, String participantId) {
        ExperimentalRole experimentalRole = new ExperimentalRole();
        experimentalRole.setShortLabel(getShortLabel(expRole));
        String fullName = getFullName(expRole);
        experimentalRole.setFullName(fullName);
        experimentalRole.setMessage(fullName);
        experimentalRole.setNodeType(BioTypes.EXPERIMENTAL_ROLE);
        experimentalRole.setParticipantId(participantId);
        experimentalRole.setExperimentalRoleIntactId(getSecondaryRefs(expRole, IntactSources.INTACT));
        experimentalRole.setExperimentalRolePubMedId(getSecondaryRefs(expRole, IntactSources.PUBMED));
        experimentalRole.setExperimentalRolePsiMiId(getPrimaryRefs(expRole, IntactSources.PSI_MI));
        return experimentalRole;
    }

    public static Collection<ExperimentalRole> getExperimentalRoles(Subgraph subgraph, DBObject expRole, String participantId) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        BasicDBList expRoles = getBasicDBList(expRole, IntactFields.EXPERIMENTAL_ROLE_LIST);
        Collection<ExperimentalRole> experimentalRoles = new HashSet<ExperimentalRole>();
        for (Object obj : expRoles) {
            if (!MongoClasses.BasicDBObject.equals(obj.getClass())) {
                throw new RuntimeException("BasicDBList does not contain BasicDBObjects. " + obj.getClass().getName());
            }
            //log.info("obj = " + (DBObject)obj);
            DBObject eRole = getDBObject((DBObject)obj, IntactFields.EXPERIMENTAL_ROLE);
            //log.info("eRole = " + eRole);
            ExperimentalRole experimentalRole = getExperimentalRole(eRole, participantId);
            subgraph.add(experimentalRole);
            experimentalRoles.add(experimentalRole);
        }
        return experimentalRoles;
    }

    public static StartStatus getStartStatus(DBObject feat, String featureId) {
        StartStatus startStatus = new StartStatus();
        startStatus.setShortLabel(getShortLabel(feat));
        String fullName = getFullName(feat);
        startStatus.setFullName(fullName);
        startStatus.setMessage(fullName);
        startStatus.setFeatureId(featureId);
        startStatus.setNodeType(BioTypes.START_STATUS);
        startStatus.setPsiMiId(getPrimaryRefs(feat, IntactSources.PSI_MI));
        startStatus.setIntactId(getSecondaryRefs(feat, IntactSources.INTACT));
        startStatus.setPubmedId(getSecondaryRefs(feat, IntactSources.PUBMED));
        return startStatus;
    }

    public static EndStatus getEndStatus(DBObject feat, String featureId) {
        EndStatus endStatus = new EndStatus();
        endStatus.setShortLabel(getShortLabel(feat));
        String fullName = getFullName(feat);
        endStatus.setFullName(fullName);
        endStatus.setMessage(fullName);
        endStatus.setFeatureId(featureId);
        endStatus.setNodeType(BioTypes.START_STATUS);
        endStatus.setPsiMiId(getPrimaryRefs(feat, IntactSources.PSI_MI));
        endStatus.setIntactId(getSecondaryRefs(feat, IntactSources.INTACT));
        endStatus.setPubmedId(getSecondaryRefs(feat, IntactSources.PUBMED));
        return endStatus;
    }

    public static FeatureRange getFeatureRange(Subgraph subgraph, DBObject feat, String featureId) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        FeatureRange featureRange = new FeatureRange();
        featureRange.setMessage(featureId);
        featureRange.setFeatureId(featureId);
        featureRange.setIsLink(getString(feat, IntactFields.IS_LINK));
        featureRange.setNodeType(BioTypes.FEATURE_RANGE);
        featureRange.setBeginPosition(getString(feat, IntactFields.BEGIN));
        featureRange.setEndPosition(getString(feat, IntactFields.END));
        StartStatus startStatus = getStartStatus(feat, featureId);
        subgraph.add(startStatus);
        BioRelation relation = new BioRelation(featureRange, startStatus, BioRelTypes.HAS_START_STATUS);
        featureRange.setHasStartStatus(relation);
        EndStatus endStatus = getEndStatus(feat, featureId);
        subgraph.add(endStatus);
        relation = new BioRelation(featureRange, endStatus, BioRelTypes.HAS_END_STATUS);
        featureRange.setHasEndStatus(relation);
        return featureRange;
    }

    public static FeatureType getFeatureType(DBObject feat, String featureId) {
        FeatureType featureType = new FeatureType();
        featureType.setShortLabel(getString(feat, IntactFields.SHORT_LABEL));
        String fullName = getString(feat, IntactFields.FULL_NAME);
        featureType.setFullName(fullName);
        featureType.setMessage(fullName);
        featureType.setFeatureId(featureId);
        featureType.setNodeType(BioTypes.FEATURE_TYPE);
        featureType.setAlias(getAlias(feat));
        featureType.setFeatureTypeIntactId(getSecondaryRefs(feat, IntactSources.INTACT));
        featureType.setFeatureTypePubMedId(getSecondaryRefs(feat, IntactSources.PUBMED));
        featureType.setFeatureTypePsiMiId(getPrimaryRefs(feat, IntactSources.PSI_MI));
        return featureType;
    }

    public static Collection<FeatureRange> getFeatureRanges(Subgraph subgraph, DBObject fea, String featureId) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        BasicDBList feats = getBasicDBList(fea, IntactFields.FEATURE_RANGE_LIST);
        Collection<FeatureRange> featureRanges = new HashSet<FeatureRange>();
        for (Object obj : feats) {
            if (!MongoClasses.DBObject.equals(obj)) {
                throw new RuntimeException("BasicDBList does not contain DBObjects. " + obj.getClass().getName());
            }
            FeatureRange featureRange = getFeatureRange(subgraph, (DBObject)obj, featureId);
            subgraph.add(featureRange);
            featureRanges.add(featureRange);
        }
        return featureRanges;
    }

    public static Feature getFeature(Subgraph subgraph, DBObject feat) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        Feature feature = new Feature();
        String shortLabel = getShortLabel(feat);
        feature.setShortLabel(shortLabel);
        feature.setMessage(shortLabel);
        String featureId = getString(feat, IntactFields.ID);
        feature.setFeatureId(featureId);
        feature.setNodeType(BioTypes.FEATURE);
        feature.setFeatureIntactId(getPrimaryRefs(feat, IntactSources.INTACT));
        FeatureType featureType = getFeatureType(feat, featureId);
        subgraph.add(featureType);
        BioRelation relation = new BioRelation(feature, featureType, BioRelTypes.HAS_FEATURE_TYPE);
        feature.setHasFeatureType(relation);
        Collection<FeatureRange> featureRanges = getFeatureRanges(subgraph, feat, featureId);
        Collection<BioRelation> relations = new HashSet<BioRelation>();
        for (FeatureRange featureRange : featureRanges) {
            relation = new BioRelation(feature, featureRange, BioRelTypes.HAS_FEATURE_RANGE);
            relations.add(relation);
        }
        feature.setFeatureRanges(relations);
        return feature;
    }

    public static Collection<Feature> getFeatures(Subgraph subgraph, DBObject fea) throws NotFoundException, NoSuchFieldException, IllegalArgumentException, IllegalAccessException, InvocationTargetException {
        BasicDBList feats = getBasicDBList(fea, IntactFields.FEATURE_LIST);
        Collection<Feature> features = new HashSet<Feature>();
        for (Object obj : feats) {
            if (!MongoClasses.DBObject.equals(obj)) {
                throw new RuntimeException("BasicDBList does not contain DBObjects. " + obj.getClass().getName());
            }
            Feature feature = getFeature(subgraph, (DBObject)obj);
            subgraph.add(feature);
            features.add(feature);
        }
        return features;
    }

    public static Collection<BioRelation> getHasExperimentalRoles(Participant participant, Collection<ExperimentalRole> roles) {
        Collection<BioRelation> relations = new HashSet<BioRelation>();
        for (ExperimentalRole role : roles) {
            BioRelation relation = new BioRelation(participant, role, BioRelTypes.HAS_EXPERIMENTAL_ROLE);
            relations.add(relation);
        }
        return relations;
    }

    /**
     * For now, we will skip adding the source to Neo4J, as every intactId has the same
     * information for source. Perhaps, we can store it just once somewhere. This is to
     * be verified, as we have not done an exhaustive check. But checking a few
     * intactIds, all seemed to have the same source.
     *
     * @param intactId
     * @param intactSource
     * @param experiments
     * @param interactors
     * @param interactions
     * @return
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Subgraph processIntact(String intactId, DBObject intactSource, BasicDBList experiments, BasicDBList interactors, BasicDBList interactions) throws NoSuchFieldException, IllegalAccessException, InvocationTargetException  {

        //log.info("source = " + intactSource.toString());
        log.info("intactId = " + intactId);

        Subgraph subgraph = new Subgraph();

        Intact intact = new Intact();
        intact.setIntactId(intactId);
        subgraph.add(intact);

        HashMap<String, Object> interactorHash = new HashMap<String, Object>();

        for (Object intr : interactors) {
            DBObject interactor = (DBObject)intr;
            //log.info("interactor = " + interactor.toString());
            BioTypes bioType = getBioType(interactor);
            //log.info("bioType of interactor = " + bioType);
            if (bioType.equals(BioTypes.PROTEIN)) {
               Protein protein = getProtein(interactor);
               //log.info("protein interactorId = " + protein.getInteractorId());
               interactorHash.put(protein.getInteractorId(), protein);
               subgraph.add(protein);
            } else {
                if (bioType.equals(BioTypes.PEPTIDE)) {
                   Peptide peptide = getPeptide(interactor);
                   //log.info("peptide interactorId = " + peptide.getInteractorId());
                   interactorHash.put(peptide.getInteractorId(), peptide);
                   subgraph.add(peptide);
                } else {
                   if (bioType.equals(BioTypes.SMALL_MOLECULE)) {
                      SmallMolecule smallMolecule = getSmallMolecule(interactor);
                      //log.info("peptide interactorId = " + peptide.getInteractorId());
                       interactorHash.put(smallMolecule.getInteractorId(), smallMolecule);
                      subgraph.add(smallMolecule);
                   } else {
                       throw new RuntimeException("Unrecognized bioType " + bioType);
                   }
                }
            }
        }

        HashMap<String, IntactInteraction> interactionHash = new HashMap<String, IntactInteraction>();
        for (Object intn : interactions) {
            DBObject interaction = (DBObject)intn;
            IntactInteraction intactInteraction = getIntactInteraction(interaction);
            subgraph.add(intactInteraction);
            BasicDBList participantList = getParticipantList(interaction);
            Collection<BioRelation> hasParticipants = new HashSet<BioRelation>();
            for (Object parti : participantList) {
                DBObject particip = (DBObject)parti;
                if (particip.containsField(IntactFields.PARTICIPANT.toString())) {
                   particip = getDBObject(particip, IntactFields.PARTICIPANT);
                }
                Participant participant = getParticipant(particip);
                subgraph.add(participant);
                String interactorId = participant.getInteractorRef();
                Object interactorObj = interactorHash.get(interactorId);
                //log.info("interactor from interactorHash = " + interactorObj);
                BioRelation hasInteractor = new BioRelation(participant, interactorObj, BioRelTypes.HAS_INTERACTOR);
                participant.setHasInteractor(hasInteractor);
                BioRelation bioRelation = new BioRelation(intactInteraction, participant, BioRelTypes.HAS_PARTICIPANT);
                hasParticipants.add(bioRelation);
                BiologicalRole biologicalRole = getBiologicalRole(particip, participant.getParticipantId());
                subgraph.add(biologicalRole);
                BioRelation hasBiologicalRole = new BioRelation(participant, biologicalRole, BioRelTypes.HAS_BIOLOGICAL_ROLE);
                participant.setHasBiologicalRole(hasBiologicalRole);
                Collection<ExperimentalRole> experimentalRoles = getExperimentalRoles(subgraph, particip, participant.getParticipantId());
                Collection<BioRelation> hasExperimentalRoles = getHasExperimentalRoles(participant, experimentalRoles);
                participant.setHasExperimentalRoles(hasExperimentalRoles);
            }
            intactInteraction.setHasParticipants(hasParticipants);
            interactionHash.put(intactInteraction.getInteractionId(), intactInteraction);
        }

        HashMap<String, IntactExperiment> experimentHash = new HashMap<String, IntactExperiment>();
        for (Object exp : experiments) {
            DBObject experiment = (DBObject)exp;
            IntactExperiment intactExperiment = getIntactExperiment(subgraph, experiment);
            subgraph.add(intactExperiment);
            String experimentId = intactExperiment.getExperimentRef();
            IntactInteraction currentInteraction = null;
            for (IntactInteraction intactInteraction : interactionHash.values()) {
                if (intactInteraction.getExperimentId().equals(experimentId)) {
                    currentInteraction = intactInteraction;
                    break;
                }
            }
            intactExperiment.addIntactInteraction(currentInteraction);
            experimentHash.put(experimentId, intactExperiment);
            intact.addIntactExperiment(intactExperiment);
        }

        return subgraph;
    }

    /**
     * This is the main Neo4J intact process kick-off method. It fetches all the necessary
     * data from Mongo  into 1 Map and 4 Lists. And this data is passed on to the top-level
     * processIntact method to process this data.
     *
     * @param e
     * @return
     * @throws UnknownHostException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     */
    public static Subgraph processIntact(DBObject e) throws UnknownHostException, NoSuchFieldException, IllegalAccessException, InvocationTargetException  {
        String intactId = (String)e.get(InTactUtil.INTACT_ID);
        DBObject intactSource = InTactUtil.getSource(intactId);
        BasicDBList experiments = InTactUtil.getSections(InTactUtil.EXPERIMENT_INTACT_COLLECTION, intactId);
        BasicDBList interactors = InTactUtil.getSections(InTactUtil.INTERACTOR_INTACT_COLLECTION, intactId);
        BasicDBList interactions = InTactUtil.getSections(InTactUtil.INTERACTION_INTACT_COLLECTION, intactId);
        Subgraph subgraph = processIntact(intactId, intactSource, experiments, interactors, interactions);
        return subgraph;
    }

    /**
     * For each intactId, we fetch data from 5 different collections in Mongo
     * and then process them together for possible connections, before we load
     * them into Neo4J. Once this is done, we update the respective status
     * in the "intactlist" collection in Mongo.
     *
     * @throws UnknownHostException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws URISyntaxException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws IOException
     * @throws HttpException
     */
    public static void loadInNeo4J() throws UnknownHostException, NoSuchFieldException, NoSuchFieldException, IllegalAccessException, InvocationTargetException, URISyntaxException, UnsupportedEncodingException, MalformedURLException, IOException, HttpException {
        BasicDBList intactList = InTactUtil.getIntactList(); // return all that are DUE
        log.info("intactList.length = " + intactList.size());
        Subgraph subgraph;
        PersistenceTemplate template = new PersistenceTemplate();
        for (Object e : intactList) {
            String intactId = (String)((DBObject)e).get(InTactUtil.INTACT_ID);
            try {
                subgraph = processIntact((DBObject)e);
                PersistenceTemplate.saveSubgraph(subgraph);
                InTactUtil.updateImportStatus(intactId, BioEntityType.DONE.toString());
            } catch (UnknownHostException ex) {
                InTactUtil.updateImportStatus(intactId, BioEntityType.ERROR.toString());
                throw ex;
            }
            log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
            log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
        }
        log.info("ADDED NEW PROPERTIES: " + PersistenceTemplate.getPropertyCount() + ", SET PROPERTIES: " + PersistenceTemplate.getPropertySetCount() + ", ADDED NEW NODES: " + PersistenceTemplate.getIndexNodeCount());
        log.info("ADDED NEW PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertyCounts() + ", SET PROPERTIES BY INDEX: " + PersistenceTemplate.getPropertySetCounts() + ", ADDED NEW NODES BY INDEX: " + PersistenceTemplate.getIndexNodeCounts());
    }

    /**
     * This method loads all the intact files into Mongo, in 5 different collections.
     * It is assumed that the intact files are located on the disk in the directory
     * INTACT_DIR. This directory contains several sub-directories, each typically for
     * a disease or a category. Each of these sub-directories contains a flat list of
     * intact XML files.
     *
     * Some of these intact XML files are big enough to break the net.sf parser.
     * It is not clear at the time of writing this code, what exact threshold size
     * breaks the parser. But the idea of the IntactSplitter is to split the
     * intact XML file into several parts. The parts are represented by the
     * IntactFile object. This object is then used to insert 5 different Mongo collections
     * with various parts. For instance, one source document is inserted into the
     * "intactsource" collection. Potentially variable number of experiment documents
     * are inserted into the "intactexperiment" collection. Variable number of
     * interactor documents are inserted into the "intactinteractor" collection.
     * Variable number of interaction documents are inserted into the "intactinteraction"
     * collection.
     *
     * @throws IOException
     * @throws URISyntaxException
     */
    public static void loadInMongo() throws IOException, URISyntaxException {
       File dir = new File(InTactUtil.INTACT_DIR);
        //File[] diseases = dir.listFiles(new ExtFilter("xml"));
        File[] intacts = dir.listFiles();
        IntactSplitter intactSplitter = new IntactSplitter();
        for (File intact : intacts) {
            File[] intactFiles = intact.listFiles();
           for (File intactFile1 : intactFiles) {
               String file = intact + "/" + intactFile1.getName();
               log.info("file = " + file);
               IntactFile intactFile = intactSplitter.splitFile(file);
               InTactUtil.addIntactFile(intactFile1.getName(), intactFile);
           }
        }
    }

    /**
     * The main program first loads all the intact files in Mongo. It updates the "intactlist"
     * collection if it does not already exist with the status of the loads. The main program
     * then loads the collection data into Neo4J, after which it updates the status of the
     * "intactlist" for a second time indicating the status of the Neo4J load depending upon
     * whether it was a success or a failure.
     *
     * @param args
     * @throws java.io.IOException
     * @throws URISyntaxException
     * @throws UnknownHostException
     * @throws NoSuchFieldException
     * @throws IllegalAccessException
     * @throws InvocationTargetException
     * @throws UnsupportedEncodingException
     * @throws MalformedURLException
     * @throws HttpException
     */
    public static void main(String[] args) throws java.io.IOException, URISyntaxException, UnknownHostException, UnknownHostException, NoSuchFieldException, IllegalAccessException, IllegalAccessException, InvocationTargetException, UnsupportedEncodingException, MalformedURLException, HttpException {
        //loadInMongo();
        loadInNeo4J();
    }
}