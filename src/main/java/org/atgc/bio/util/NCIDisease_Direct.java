/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.atgc.bio.BioEntityTypes;
import org.atgc.bio.NCIDiseaseUtil;
import org.atgc.neo4j.NeoUtil;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;


/**
 *
 * @author jtanisha-ee
 */
public class NCIDisease_Direct {
    
    final static int MAX_INDEX = 7181; // currently max urls on hotdiary.com
    final static int DOC_INDEX = 3;
    
    private static RestIndex<Node> geneidIndex;
    private static RestIndex<Node> seqidIndex;
    private static RestIndex<Node> statementIdIndex;
    private static RestIndex<Node> proteinIdIndex;
    private static RestIndex<Node> diseaseIdIndex;
   private static RestIndex<Node> pubMedIdIndex;
   private static RestIndex<Node> organismIdIndex;
  
   
    private static Map map;
    private static RestGraphDatabase graphDb;

    public final static String MESSAGE = "message";
    //index
    public final static String GENEID = "geneid";
    public final static String SEQID = "seqid";
    public final static String ORGANISMID = "organismid";
    
    //relationship
    public final static String HAS_SEQUENCE = "has sequence"; 
    public final static String HAS_PROTEIN ="has protein";
    
    public final static String SENTENCE_LIST = "Sentence";
    public final static String HGNCID = "HgncID";
    public final static String GENE_ALIAS_COLLECTION = "GeneAliasCollection";
    public final static String HUGO_GENE_SYMBOL = "HUGOGeneSymbol"; 
    public final static String SEQUENCE_IDENTIFICATION_COLLECTION = "SequenceIdentificationCollection";
    public final static String LOCUS_LINK_ID = "LocusLinkID";
    public final static String GEN_BANK_ACCESSION = "GenbankAccession";
    public final static String REFSEQ_ID = "RefSeqID";
    public static final String UNIPROT_ID = "UniProtID";
    public static final String SENTENCE_ID = "sentence";
    public static final String GENE_DATA = "GeneData";
    public static final String MATCHED_GENE_TERM = "MatchedGeneTerm";
    public static final String NCI_GENECONCEPT_CODE = "NCIGeneConceptCode";
    public static String DISEASE_DATA = "DiseaseData";
    public static String MATCHED_DISEASE_TERM = "MatchedDiseaseTerm";
    public static String NCI_DISEASE_CONCEPT_CODE = "NCIDiseaseConceptCode";
    public static String STATEMENT = "Statement";
    public static String UNIPROT = "UniProt";  // from ncipathway
    public static String STATEMENT_ID = "statementid";
    public static String DISEASE_ID = "diseaseid";
    public static String PUBMEDID =  "PubMedID";
    public static String EXPLAINS_THE_CAUSE = "explains the cause";
    private static String AFFECTS_DISEASE = "affects disease";
    private static String PUBMED_CITATION= "pubmed citation";
    private static String ROLES = "Roles";
    public static String PRIMARY_NCI_ROLE_CODE = "PrimaryNCIRoleCode";
    public static String OTHER_ROLE = "OtherRole";
    private static String ORGANISM = "Organism";
    private static String NEGATION_INDICATOR = "NegationIndicator";
    private static String CELLINE_INDICATOR = "CellineIndicator";
    private static String EVIDENCE_CODE = "EvidenceCode";
    private static String COMMENTS = "Comments";
    private static int MAX_MSG_SIZE = 60;
    private static boolean INITIALIZED = false;
       
    
     /**
     * This method uses disease collection and adds genesymbol, importstatus and date to the diseaselistcollection
     * importstatus - due is by default.
     * @@throws UnknownHostException
     */
    public void addDiseaseList() throws UnknownHostException {
         NCIDiseaseUtil.addDiseaseList();
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
    
    
    private static void setup() throws URISyntaxException {
        //graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
        graphDb = new RestGraphDatabase(BioEntityTypes.DB_URL);
        registerShutdownHook(graphDb);
    }    
   
    
    public static void main(String[] args) throws java.io.IOException {
        /*for (int i = 1; i <= DOC_INDEX; i++) {
            String diseaseUrl = "http://hotdiary.com/nci/disease/disease" + i + ".xml";
            NCIDiseaseUtil.addDisease(diseaseUrl);
        } */
    
        // gives diseaselist documents whose importstatus is due. 
        List<Map> diseaseList = NCIDiseaseUtil.getNciDiseaseListDueCollection();
        Iterator<Map> diseaseIter = diseaseList.iterator();
        //System.out.println("diseaseIter.count() =" + diseaseList.size());
       
        for (int i = 0; i < diseaseList.size(); i++ ) { 
        //while (diseaseIter.hasNext()) {
            //System.out.println("i =" + i);
            Map map = diseaseIter.next(); 
            //System.out.println("map =" + map.toString());           
            String geneSymbol = (String)map.get(BioEntityTypes.HUGO_GENE_SYMBOL);
            
            //System.out.println("geneSymbol =" + geneSymbol);
            try {
                Node diseaseNode = createGeneNode(geneSymbol);
                //NCIDiseaseUtil.updateImportStatus(geneSymbol, BioEntityTypes.DONE);
            } catch (Exception e) {
                //NCIDiseaseUtil.updateImportStatus(geneSymbol,  BioEntityTypes.ERROR);
                throw new RuntimeException(e);
            } 
        }  
        
     /*   try {
             createGeneNode("PKP4");        
        } catch(Exception e) {
            throw new RuntimeException("PKP4" + e.getMessage(), e);
        } */
    }
        
    public static String processSequenceIdentification(BasicDBObject diseaseInfo, Node geneNode) throws java.io.IOException, java.net.URISyntaxException {
        
        Object seqObj = diseaseInfo.get(SEQUENCE_IDENTIFICATION_COLLECTION);
        BasicDBObject sequence = (BasicDBObject)seqObj;
        if (sequence != null) {
            //System.out.println("sequence =" + sequence.toString());

            String proteinId = sequence.getString(UNIPROT_ID);
            //System.out.println("sequence.proteinId =" + proteinId);

                if (seqidIndex == null) {
                    seqidIndex = graphDb.index().forNodes(SEQID);
                }
                String hgncId = sequence.getString(HGNCID);
                Node seqNode = null;
                IndexHits<Node> gNodeHits = seqidIndex.get(SEQID, hgncId);
                if (gNodeHits != null && gNodeHits.size() > 0) {
                    seqNode = gNodeHits.getSingle(); 
                    Relationship rel = null;
                    if ((rel = NeoUtil.checkCreateRel(geneNode, seqNode, HAS_SEQUENCE)) != null) {
                        rel.setProperty(MESSAGE, HAS_SEQUENCE);
                    }
                    if (!seqNode.hasProperty(BioEntityTypes.NODE_TYPE)) {
                        seqNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_SEQNODE);
                    }
                    if (!seqNode.hasProperty(HGNCID) && hgncId != null) {
                        seqNode.setProperty(HGNCID, hgncId);    
                    }
                    if (!seqNode.hasProperty(MESSAGE)) {
                        seqNode.setProperty(MESSAGE, SEQUENCE_IDENTIFICATION_COLLECTION);
                    }
                    if (!seqNode.hasProperty(LOCUS_LINK_ID) && sequence.getString(LOCUS_LINK_ID) != null ) {
                        seqNode.setProperty(LOCUS_LINK_ID, sequence.getString(LOCUS_LINK_ID));
                    }
                    if (!seqNode.hasProperty(GEN_BANK_ACCESSION) && sequence.getString(GEN_BANK_ACCESSION) != null) {
                        seqNode.setProperty(GEN_BANK_ACCESSION, sequence.getString(GEN_BANK_ACCESSION));
                    }
                    if (!seqNode.hasProperty(REFSEQ_ID) && sequence.getString(REFSEQ_ID) != null) {
                        seqNode.setProperty(REFSEQ_ID, sequence.getString(REFSEQ_ID));
                    }
                    if (!seqNode.hasProperty(UNIPROT_ID) && sequence.getString(UNIPROT_ID) != null) {
                    seqNode.setProperty(UNIPROT_ID, sequence.getString(UNIPROT_ID));
                    }
                    //processSentence();
                    gNodeHits.close();
                } else {
                    gNodeHits.close();
                    seqNode = graphDb.createNode();
                    if (seqNode != null ) {
                        seqidIndex.add(seqNode, SEQID, hgncId);
                        seqNode.setProperty(MESSAGE, SEQUENCE_IDENTIFICATION_COLLECTION);
                        if (hgncId != null) {
                            seqNode.setProperty(HGNCID, hgncId);
                        }
                        if (sequence.getString(LOCUS_LINK_ID) != null) {
                           seqNode.setProperty(LOCUS_LINK_ID, sequence.getString(LOCUS_LINK_ID));
                        }
                        if (sequence.getString(GEN_BANK_ACCESSION) != null) {
                           seqNode.setProperty(GEN_BANK_ACCESSION, sequence.getString(GEN_BANK_ACCESSION));
                        }
                        if (sequence.getString(REFSEQ_ID) != null) {
                           seqNode.setProperty(REFSEQ_ID, sequence.getString(REFSEQ_ID));
                        }
                        if (sequence.getString(UNIPROT_ID) != null) {
                           seqNode.setProperty(UNIPROT_ID, sequence.getString(UNIPROT_ID));
                        }
                        
                        Relationship rel = geneNode.createRelationshipTo(seqNode, DynamicRelationshipType.withName(URLEncoder.encode(HAS_SEQUENCE, "UTF-8")));
                        rel.setProperty(MESSAGE, HAS_SEQUENCE );
                        seqNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_SEQNODE);
                    }
                }
                return proteinId;
        } else {
            //System.out.println("sequence Object is null");
            return null;
        }    
    }
   
    
    public static void processSentence(BasicDBObject zeroObject, Node geneNode, Node proteinNode, String geneSymbol, String proteinId) throws java.io.IOException, java.net.URISyntaxException {
      //BasicDBObject sentence = (BasicDBObject)NCIDiseaseUtil.getSentence(zeroObject);
            Object sentenceObj = (Object)zeroObject.get(SENTENCE_LIST);
            if (sentenceObj != null) {
                if (sentenceObj.getClass().getName().equals("com.mongodb.BasicDBObject")) {
                    BasicDBObject sentence = (BasicDBObject)zeroObject.get(SENTENCE_LIST);
                    if (sentence != null) {
                        //System.out.println("found one sentence");
                        createSentence(sentence, geneNode, proteinNode, geneSymbol, proteinId, 0);
                    }                   
                } else {
                    if (sentenceObj.getClass().getName().equals("com.mongodb.BasicDBList")) {
                        BasicDBList sentenceList = (BasicDBList)zeroObject.get(SENTENCE_LIST);
                        if (sentenceList != null) {
                            Iterator sentenceIter = sentenceList.iterator();
                            //System.out.println("found sentence list =" + sentenceList.size());
                            int i = 0;
                            while (sentenceIter.hasNext()) {
                                BasicDBObject sentence = (BasicDBObject)sentenceIter.next();      
                                if (sentence != null) {
                                    createSentence(sentence, geneNode, proteinNode, geneSymbol, proteinId, i); 
                                    i++;
                                }
                            }
                        } 
                    }
                }
            } else {
                //System.out.println("sentence Object is null");
                return;
            }
    }
    
    public static void createSentence(BasicDBObject sentence, Node geneNode, Node proteinNode, String geneSymbol, String proteinId, int statementNum) throws java.io.IOException, java.net.URISyntaxException {
            
            Object geneDataObj = sentence.get(GENE_DATA);
            if (geneDataObj == null || sentence == null) {
                //System.out.println("geneDataObj or sentence is null, " + geneSymbol);
                return;
            } else {
                //System.out.println("geneData " + geneDataObj.toString());
                BasicDBObject geneData = (BasicDBObject)geneDataObj;
                if (geneData != null) {                    
                    String geneTerm = geneData.getString(MATCHED_GENE_TERM);
                    BasicDBList geneConceptCode = (BasicDBList)geneData.get(NCI_GENECONCEPT_CODE);
                    Object diseaseDataObj = sentence.get(DISEASE_DATA);
                    if (diseaseDataObj != null) {
                        BasicDBObject diseaseData = (BasicDBObject)diseaseDataObj;
                        String diseaseTerm = diseaseData.getString(MATCHED_DISEASE_TERM);
                        String diseaseConceptCode = diseaseData.getString(NCI_DISEASE_CONCEPT_CODE);    
                        String statement = sentence.getString(STATEMENT);             
                        Node diseaseNode = null;
                        if (diseaseIdIndex == null ) { 
                            diseaseIdIndex = graphDb.index().forNodes(DISEASE_ID);
                        }                        
                            if (diseaseConceptCode != null) {
                                IndexHits <Node> pNodeHits = diseaseIdIndex.get(DISEASE_ID,  diseaseConceptCode);
                                if (pNodeHits != null && pNodeHits.size() > 0) { // if output node already exists
                                    diseaseNode = pNodeHits.getSingle();              
                                    if (diseaseNode != null) {
                                        if (!diseaseNode.hasProperty(NCI_DISEASE_CONCEPT_CODE))  {
                                            diseaseNode.setProperty(NCI_DISEASE_CONCEPT_CODE,  diseaseConceptCode);
                                        }
                                        if (!diseaseNode.hasProperty(BioEntityTypes.NODE_TYPE)) {
                                            diseaseNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_DISEASE);
                                        }
                                        if (!diseaseNode.hasProperty(MATCHED_DISEASE_TERM) && diseaseTerm != null) {
                                            diseaseNode.setProperty(MATCHED_DISEASE_TERM, diseaseTerm);
                                        }
                                        if (!diseaseNode.hasProperty(MESSAGE) && diseaseTerm != null) {
                                            diseaseNode.setProperty(MESSAGE, diseaseTerm);
                                        }
                                        Relationship rel = null;
                                        if ((rel = NeoUtil.checkCreateRel(proteinNode,  diseaseNode, AFFECTS_DISEASE)) != null) {
                                            rel.setProperty(MESSAGE,  AFFECTS_DISEASE);
                                        } 
                                        if ((rel = NeoUtil.checkCreateRel(geneNode, diseaseNode, AFFECTS_DISEASE)) != null) {
                                            rel.setProperty(MESSAGE,  AFFECTS_DISEASE);
                                        }

                                        Object rolesObj = sentence.get(ROLES);
                                        if (rolesObj.getClass().getName().equals("com.mongodb.BasicDBObject")) {                
                                            BasicDBObject roles = (BasicDBObject)sentence.get(ROLES);
                                            Object primaryRoles = roles.get(PRIMARY_NCI_ROLE_CODE);
                                            Object otherRoles = roles.get(OTHER_ROLE);

                                            if (primaryRoles.getClass().getName().equals("com.mongodb.BasicDBList")) {
                                                BasicDBList primaryRoleList = (BasicDBList)roles.get(PRIMARY_NCI_ROLE_CODE);
                                                Iterator primaryRoleIter =  primaryRoleList.iterator();
                                                if (primaryRoleIter != null) {
                                                        while (primaryRoleIter.hasNext()) {            
                                                            String roleCode =  primaryRoleIter.next().toString();
                                                            //System.out.println("List primaryRoleCode =" + roleCode);
                                                            if ((rel = NeoUtil.checkCreateRel(geneNode,  diseaseNode, roleCode)) != null) {
                                                                rel.setProperty(roleCode,  roleCode);
                                                            }
                                                        }
                                                }
                                            } else {
                                                if (primaryRoles.getClass().getName().equals("com.mongodb.BasicDBObject")) {
                                                    String roleCode =  roles.getString(PRIMARY_NCI_ROLE_CODE);
                                                    //System.out.println("Object primaryRoleCode =" + roleCode);
                                                    if ((rel = NeoUtil.checkCreateRel(geneNode,  diseaseNode, roleCode)) != null) {
                                                        rel.setProperty(roleCode,  roleCode);
                                                    }
                                                }
                                            }
                                            if (otherRoles.getClass().getName().equals("com.mongodb.BasicDBList")) {
                                                BasicDBList otherRoleList = (BasicDBList)roles.get(OTHER_ROLE);
                                                Iterator otherRoleIter = otherRoleList.iterator();
                                                if (otherRoleIter != null) 
                                                    while (otherRoleIter.hasNext()) {
                                                        String otherRoleCode = otherRoleIter.next().toString();
                                                        if ((rel = NeoUtil.checkCreateRel(geneNode,  diseaseNode, otherRoleCode)) != null) {
                                                            rel.setProperty(otherRoleCode,  otherRoleCode);
                                                        }
                                                    }
                                            } else {
                                                if (otherRoles.getClass().getName().equals("com.mongodb.BasicDBObject"))  {
                                                    String otherRoleCode = roles.getString(OTHER_ROLE);
                                                    if ((rel = NeoUtil.checkCreateRel(geneNode,  diseaseNode, otherRoleCode)) != null) {
                                                        rel.setProperty(otherRoleCode,  otherRoleCode);
                                                    }
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    diseaseNode = graphDb.createNode();
                                    if (diseaseNode != null) {
                                        diseaseIdIndex.add(diseaseNode, DISEASE_ID,  diseaseConceptCode);
                                        diseaseNode.setProperty(NCI_DISEASE_CONCEPT_CODE,  diseaseConceptCode);
                                        diseaseNode.setProperty(BioEntityTypes.NODE_TYPE,  BioEntityTypes.RB_DISEASE);
                                        if (diseaseTerm != null) {
                                           diseaseNode.setProperty(MESSAGE, diseaseTerm);
                                           diseaseNode.setProperty(MATCHED_DISEASE_TERM, diseaseTerm);
                                        }                                                                                                                       
                                        Relationship rel = geneNode.createRelationshipTo(diseaseNode, DynamicRelationshipType.withName(URLEncoder.encode(AFFECTS_DISEASE, "UTF-8")));
                                        rel.setProperty(MESSAGE,  AFFECTS_DISEASE); 
                                        if (proteinNode != null) {
                                           rel = proteinNode.createRelationshipTo(diseaseNode, DynamicRelationshipType.withName(URLEncoder.encode(AFFECTS_DISEASE, "UTF-8")));
                                           rel.setProperty(MESSAGE,  AFFECTS_DISEASE);
                                        }
                                        
                                        Object rolesObj = sentence.get(ROLES);
                                        if (rolesObj.getClass().getName().equals("com.mongodb.BasicDBObject")) {                
                                            BasicDBObject roles = (BasicDBObject)sentence.get(ROLES);
                                            Object primaryRoles = roles.get(PRIMARY_NCI_ROLE_CODE);
                                            Object otherRoles = roles.get(OTHER_ROLE);
                                            if (primaryRoles != null && primaryRoles.getClass().getName().equals("com.mongodb.BasicDBList")) {
                                                    BasicDBList primaryRoleList = (BasicDBList)roles.get(PRIMARY_NCI_ROLE_CODE);
                                                    if (primaryRoleList != null) {
                                                        Iterator primaryRoleIter =  primaryRoleList.iterator();
                                                        while (primaryRoleIter.hasNext()) {            
                                                            String roleCode =  primaryRoleIter.next().toString();
                                                            if (roleCode != null) {
                                                            rel = geneNode.createRelationshipTo(diseaseNode, DynamicRelationshipType.withName(URLEncoder.encode(roleCode, "UTF-8")));
                                                            rel.setProperty(roleCode,  roleCode);
                                                            }
                                                        }  
                                                    }
                                            } else {
                                                    if (primaryRoles != null && primaryRoles.getClass().getName().equals("com.mongodb.BasicDBObject")) {
                                                        String roleCode = roles.getString(PRIMARY_NCI_ROLE_CODE);  
                                                        if (roleCode != null) {
                                                            rel = geneNode.createRelationshipTo(diseaseNode, DynamicRelationshipType.withName(URLEncoder.encode(roleCode, "UTF-8")));
                                                            rel.setProperty(roleCode,  roleCode);
                                                        }
                                                    }
                                            }

                                            if (otherRoles != null && otherRoles.getClass().getName().equals("com.mongodb.BasicDBList")) {
                                                BasicDBList otherRoleList = (BasicDBList)roles.get(OTHER_ROLE);
                                                if (otherRoleList != null) {
                                                    Iterator otherRoleIter = otherRoleList.iterator();
                                                    while (otherRoleIter != null && otherRoleIter.hasNext()) {
                                                        String otherRoleCode = otherRoleIter.next().toString();
                                                        if (otherRoleCode != null) {
                                                            rel = geneNode.createRelationshipTo(diseaseNode, DynamicRelationshipType.withName(URLEncoder.encode(otherRoleCode, "UTF-8")));                           
                                                            rel.setProperty(otherRoleCode,  otherRoleCode); 
                                                        }
                                                    }
                                                }
                                            } else {
                                                if (otherRoles != null && otherRoles.getClass().getName().equals("com.mongodb.BasicDBObject")) {                                               
                                                    String roleCode = roles.getString(OTHER_ROLE);
                                                    if (roleCode != null) {
                                                        rel = geneNode.createRelationshipTo(diseaseNode, DynamicRelationshipType.withName(URLEncoder.encode(roleCode, "UTF-8")));
                                                        rel.setProperty(roleCode,  roleCode);
                                                    } 
                                                }
                                            }
                                        }
                                    }
                                }
                                pNodeHits.close();
                            } 

                            String pubMedId = sentence.getString(PUBMEDID);
                            Node pubMedNode = null;

                                if (pubMedIdIndex == null) {
                                    pubMedIdIndex = graphDb.index().forNodes(PUBMEDID);
                                }
                                IndexHits<Node> pNodeHits = pubMedIdIndex.get(PUBMEDID,  pubMedId);
                                if (pubMedId != null) {
                                    if  (pNodeHits != null && pNodeHits.size() > 0) { // if output node already exists
                                        pubMedNode = pNodeHits.getSingle();                  
                                        if (pubMedNode != null) {
                                            if (!pubMedNode.hasProperty(PUBMEDID)) {
                                                pubMedNode.setProperty(PUBMEDID,  pubMedId);
                                            }
                                            if (!pubMedNode.hasProperty(BioEntityTypes.NODE_TYPE)) {
                                                pubMedNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_PUBMED);
                                            }
                                            if (!pubMedNode.hasProperty(MESSAGE)) {
                                                pubMedNode.setProperty(MESSAGE, pubMedId);
                                            }
                                            Relationship rel = null;
                                            if (geneNode != null) {
                                            if ((geneNode != null && (rel = NeoUtil.checkCreateRel(geneNode,  pubMedNode,  PUBMED_CITATION)) != null)) {
                                                rel.setProperty(MESSAGE,  PUBMED_CITATION);
                                            }
                                            if  ((diseaseNode != null && (rel = NeoUtil.checkCreateRel(diseaseNode, pubMedNode,  PUBMED_CITATION)) != null)) {
                                                rel.setProperty(MESSAGE,  PUBMED_CITATION);
                                            }  
                                            if  ((proteinNode != null && (rel = NeoUtil.checkCreateRel(proteinNode, pubMedNode,  PUBMED_CITATION)) != null)) {
                                                rel.setProperty(MESSAGE,  PUBMED_CITATION);
                                            }
                                        } 
                                    } else {
                                        pubMedNode = graphDb.createNode();
                                        if (pubMedNode != null) {
                                            pubMedIdIndex.add(pubMedNode, PUBMEDID,  pubMedId);  
                                            pubMedNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_PUBMED);
                                            pubMedNode.setProperty(PUBMEDID,  pubMedId);
                                            pubMedNode.setProperty(MESSAGE, pubMedId);
                                            Relationship rel = null;
                                            if (geneNode != null) {
                                                rel = geneNode.createRelationshipTo(pubMedNode, DynamicRelationshipType.withName(URLEncoder.encode(PUBMED_CITATION, "UTF-8")));
                                                rel.setProperty(MESSAGE,  PUBMED_CITATION);
                                            }
                                            if (proteinNode != null) {
                                                rel = proteinNode.createRelationshipTo(pubMedNode, DynamicRelationshipType.withName(URLEncoder.encode(PUBMED_CITATION, "UTF-8")));
                                                rel.setProperty(MESSAGE,  PUBMED_CITATION);
                                            }
                                            if (diseaseNode != null) {
                                                rel = diseaseNode.createRelationshipTo(pubMedNode, DynamicRelationshipType.withName(URLEncoder.encode(PUBMED_CITATION, "UTF-8")));
                                                rel.setProperty(MESSAGE,  PUBMED_CITATION);
                                            }
                                        }
                                    }
                                    pNodeHits.close();
                                }


                            String organism = sentence.getString(ORGANISM);
                            Node organismNode = null;
                            if (organismIdIndex == null) {
                                organismIdIndex = graphDb.index().forNodes(ORGANISMID);
                            } 
                            if (organism != null) {
                                pNodeHits = organismIdIndex.get(ORGANISMID, organism);
                                if  (pNodeHits != null && pNodeHits.size() > 0) { // i node already exists
                                    organismNode = pNodeHits.getSingle();  
                                    if (organismNode != null) {
                                        if (!organismNode.hasProperty(BioEntityTypes.NODE_TYPE)) {
                                            organismNode.setProperty(BioEntityTypes.NODE_TYPE, ORGANISM);  
                                        }
                                        if (!organismNode.hasProperty(ORGANISMID)) {
                                            organismNode.setProperty(ORGANISMID, organism);
                                        }
                                        if (!organismNode.hasProperty(MESSAGE)) {
                                        organismNode.setProperty(MESSAGE, organism);
                                        }

                                        Relationship rel = null;
                                        if  ((proteinNode != null && (rel = NeoUtil.checkCreateRel(proteinNode, organismNode,  ORGANISM)) != null)) {
                                            rel.setProperty(MESSAGE,  ORGANISM);
                                        }
                                        if  ((geneNode != null && (rel = NeoUtil.checkCreateRel(geneNode, organismNode,  ORGANISM)) != null)) {
                                            rel.setProperty(MESSAGE,  ORGANISM);
                                        }
                                        if ((pubMedNode != null && (rel = NeoUtil.checkCreateRel(pubMedNode, organismNode,  ORGANISM ))!=null)) {
                                            rel.setProperty(MESSAGE, ORGANISM);                       
                                        }
                                        if ((diseaseNode != null && (rel = NeoUtil.checkCreateRel(diseaseNode, organismNode,  ORGANISM ))!=null)) {
                                            rel.setProperty(MESSAGE, ORGANISM);                       
                                        }
                                    }
                                }
                            } else {
                                organismNode = graphDb.createNode();
                                if (organismNode != null) {
                                    organismIdIndex.add(organismNode, ORGANISMID,  organism);  
                                    organismNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_ORGANISM);
                                    organismNode.setProperty(MESSAGE, organism);
                                    organismNode.setProperty(ORGANISMID, organism);
                                    Relationship rel = null;
                                    if (pubMedNode != null) {
                                       rel = pubMedNode.createRelationshipTo(organismNode, DynamicRelationshipType.withName(URLEncoder.encode(ORGANISM, "UTF-8")));
                                       rel.setProperty(MESSAGE,  ORGANISM);
                                    }
                                    if (geneNode != null) {
                                        rel = geneNode.createRelationshipTo(organismNode, DynamicRelationshipType.withName(URLEncoder.encode(ORGANISM, "UTF-8")));
                                        rel.setProperty(MESSAGE,  ORGANISM);
                                    }
                                    if (proteinNode != null) {
                                        rel = proteinNode.createRelationshipTo(organismNode, DynamicRelationshipType.withName(URLEncoder.encode(ORGANISM, "UTF-8")));
                                        rel.setProperty(MESSAGE,  ORGANISM);
                                    }
                                    if (diseaseNode != null) {
                                        rel = diseaseNode.createRelationshipTo(organismNode, DynamicRelationshipType.withName(URLEncoder.encode(ORGANISM, "UTF-8")));
                                        rel.setProperty(MESSAGE,  ORGANISM);
                                    }
                                }
                            }  
                            pNodeHits.close();
                            if (geneNode != null) {
                                String negationIndicator = sentence.getString(NEGATION_INDICATOR);
                                if (negationIndicator != null) {
                                geneNode.setProperty(NEGATION_INDICATOR, negationIndicator);
                                }                    
                                String cellineIndicator = sentence.getString(CELLINE_INDICATOR);
                                if (cellineIndicator != null) {
                                    geneNode.setProperty(CELLINE_INDICATOR, cellineIndicator);
                                }                                                      
                                Object commentObj = sentence.get(COMMENTS);
                                if (commentObj != null && commentObj.getClass().getName().equals("com.mongodb.BasicDBList")) {
                                    BasicDBList commentList = (BasicDBList)sentence.get(COMMENTS);
                                    if (commentList != null) {
                                        Iterator commentIter = commentList.iterator();
                                        while (commentIter.hasNext()) {
                                            if (commentIter.next() != null) {
                                                String comment =  commentIter.next().toString();
                                                if (comment != null) {
                                                   geneNode.setProperty(COMMENTS, comment);
                                                }
                                            }
                                        }
                                    }
                                } else {
                                    if (commentObj != null && commentObj.getClass().getName().equals("com.mongodb.BasicDBObject")) {
                                        BasicDBObject comment = (BasicDBObject)sentence.get(COMMENTS);
                                        if (comment != null || comment.toString() != null) {
                                            geneNode.setProperty(COMMENTS, comment.toString());
                                        }
                                    }
                                }
                            }

                            String evidenceCode = sentence.getString(EVIDENCE_CODE);
                            geneNode.setProperty(EVIDENCE_CODE, evidenceCode);
                            if (statementIdIndex == null) {
                                statementIdIndex = graphDb.index().forNodes(STATEMENT_ID);
                             }
                            if (geneSymbol != null) {
                                String statementId = geneSymbol + " " +  statementNum;
                                //System.out.println("statementId =" + statementId);
                                Node statementNode = null;
                                String msgStatement;
                                if (statement != null) {
                                    if (statement.length() > MAX_MSG_SIZE) {
                                        msgStatement = statement.substring(0, MAX_MSG_SIZE);
                                    } else {
                                        msgStatement = statement;
                                    }
                                    pNodeHits = statementIdIndex.get(STATEMENT_ID, statementId);
                                    if (pNodeHits != null && pNodeHits.size() > 0) { // if output node already exists
                                        statementNode = pNodeHits.getSingle();      
                                        if (statementNode != null) {
                                            if (!statementNode.hasProperty(STATEMENT_ID)) {
                                                statementNode.setProperty(STATEMENT_ID, statementId);
                                            }
                                            if (!statementNode.hasProperty(BioEntityTypes.NODE_TYPE)) {
                                                statementNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_STATEMENT);
                                            }
                                        //ask jtanisha-ee about this
                                            if (!statementNode.hasProperty(MESSAGE)) {
                                                statementNode.setProperty(MESSAGE, msgStatement);
                                            }
                                            if (!statementNode.hasProperty(STATEMENT)) {
                                            statementNode.setProperty(STATEMENT,  statement);
                                            }
                                            Relationship rel = null;

                                            if ((geneNode != null && (rel = NeoUtil.checkCreateRel(geneNode, statementNode, EXPLAINS_THE_CAUSE)) != null)) {
                                                rel.setProperty(MESSAGE, EXPLAINS_THE_CAUSE);
                                            }
                                            if ((proteinNode != null && (rel = NeoUtil.checkCreateRel(proteinNode, statementNode, EXPLAINS_THE_CAUSE)) != null)) {
                                                rel.setProperty(MESSAGE, EXPLAINS_THE_CAUSE);
                                            }
                                            if ((diseaseNode != null && (rel = NeoUtil.checkCreateRel(diseaseNode, statementNode, EXPLAINS_THE_CAUSE)) != null)) {
                                                rel.setProperty(MESSAGE, EXPLAINS_THE_CAUSE);
                                            }
                                            if ((pubMedNode != null && (rel = NeoUtil.checkCreateRel(pubMedNode, statementNode, EXPLAINS_THE_CAUSE)) != null)) {
                                                rel.setProperty(MESSAGE, EXPLAINS_THE_CAUSE);
                                            }
                                            if ((organismNode != null && (rel = NeoUtil.checkCreateRel(organismNode, statementNode, EXPLAINS_THE_CAUSE)) != null)) {
                                                rel.setProperty(MESSAGE, EXPLAINS_THE_CAUSE);
                                            } 
                                        }
                                    } else {
                                        //System.out.println("creating a new node, could not find statementNode=" + statementId);
                                        statementNode = graphDb.createNode();
                                        if (statementNode != null) {
                                            statementIdIndex.add(statementNode, STATEMENT_ID,  statementId);  
                                            statementNode.setProperty(BioEntityTypes.NODE_TYPE,  BioEntityTypes.RB_STATEMENT);
                                            //ask jtanisha-ee about this
                                            statementNode.setProperty(MESSAGE,  msgStatement);
                                            statementNode.setProperty(STATEMENT_ID, statementId);

                                            //System.out.println("statement = " + statement);
                                            statementNode.setProperty(STATEMENT,  statement);
                                            //statementNode.setProperty();  
                                            Relationship rel = null;
                                            if (geneNode != null) {
                                               rel = geneNode.createRelationshipTo(statementNode, DynamicRelationshipType.withName(URLEncoder.encode(EXPLAINS_THE_CAUSE, "UTF-8")));
                                               rel.setProperty(MESSAGE,  EXPLAINS_THE_CAUSE);
                                            }
                                            if (proteinNode != null) {
                                                rel = proteinNode.createRelationshipTo(statementNode, DynamicRelationshipType.withName(URLEncoder.encode(EXPLAINS_THE_CAUSE, "UTF-8")));
                                                rel.setProperty(MESSAGE,  EXPLAINS_THE_CAUSE);
                                            }
                                            if (diseaseNode != null) {
                                                rel = diseaseNode.createRelationshipTo(statementNode, DynamicRelationshipType.withName(URLEncoder.encode(EXPLAINS_THE_CAUSE, "UTF-8")));
                                                rel.setProperty(MESSAGE,  EXPLAINS_THE_CAUSE);
                                            }
                                            if (pubMedNode != null) {
                                               rel = pubMedNode.createRelationshipTo(statementNode, DynamicRelationshipType.withName(URLEncoder.encode(EXPLAINS_THE_CAUSE, "UTF-8")));
                                               rel.setProperty(MESSAGE,  EXPLAINS_THE_CAUSE);
                                            }
                                            if (organismNode != null) {
                                               rel = organismNode.createRelationshipTo(statementNode, DynamicRelationshipType.withName(URLEncoder.encode(EXPLAINS_THE_CAUSE, "UTF-8")));
                                               rel.setProperty(MESSAGE,  EXPLAINS_THE_CAUSE);
                                            }
                                        }
                                    }
                                    pNodeHits.close();
                                }
                            }
                        }
                    }
                }

                //System.out.println("createSentence completed");
         }
    }
    
    public static Node createProteinNode(Node geneNode,  String geneSymbol, String proteinId) throws UnsupportedEncodingException {
        
        //System.out.println("proteinId = " + proteinId);
        
       
        if (proteinIdIndex == null) {
            proteinIdIndex = graphDb.index().forNodes(UNIPROT);
        }
        
        Node proteinNode = null; 
        
                IndexHits<Node> pNodeHits = proteinIdIndex.get(UNIPROT,  proteinId);
                if (pNodeHits.size() > 0) { // if output node already exists
                    proteinNode = pNodeHits.getSingle();                  
                          
                    if (!proteinNode.hasProperty(UNIPROT)) {
                        proteinNode.setProperty(UNIPROT, proteinId);
                    }
                    if (!proteinNode.hasProperty(MESSAGE)) {
                        proteinNode.setProperty(MESSAGE, proteinId);
                    }
                    if (!proteinNode.hasProperty(BioEntityTypes.NODE_TYPE)) {
                        proteinNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_PROTEIN);
                    }
                    Relationship rel = null;
                    if  ((rel = NeoUtil.checkCreateRel(geneNode, proteinNode,  HAS_PROTEIN)) !=  null ) {
                         rel.setProperty(MESSAGE, HAS_PROTEIN);
                    }
                } else {
                   proteinNode = graphDb.createNode();
                   proteinIdIndex.add(proteinNode, UNIPROT,  proteinId);  
                   proteinNode.setProperty(UNIPROT, proteinId);
                   proteinNode.setProperty(MESSAGE, proteinId);
                   proteinNode.setProperty(BioEntityTypes.NODE_TYPE, BioEntityTypes.RB_PROTEIN);                   
                   Relationship rel = geneNode.createRelationshipTo(proteinNode, DynamicRelationshipType.withName(URLEncoder.encode(HAS_PROTEIN, "UTF-8")));
                   rel.setProperty(MESSAGE,  HAS_PROTEIN);
                }
                pNodeHits.close();
                
            return  proteinNode;
    }
    
    public static Node createGeneNode(String name) throws java.io.IOException, java.net.URISyntaxException {
        
        //System.out.println("calling createGeneNode");
        Map map = NCIDiseaseUtil.getDiseaseObject(name);
        //System.out.println("disease Map"  + map.toString());
        
        BasicDBObject zeroObject = NCIDiseaseUtil.getZeroObject(map);
        if (zeroObject == null) {
           // System.out.println(name + " disease object is null");
            return null;
        } else {   
            String geneSymbol = NCIDiseaseUtil.getHugoGeneSymbol(zeroObject);
            //System.out.println("geneSymbol " + geneSymbol);
            Node geneNode = null;
            setup();
            Transaction tx = graphDb.beginTx();

            try {        
                if (geneidIndex == null) {
                geneidIndex = graphDb.index().forNodes(GENEID);
                
                }
                Object[] geneAliasArray = NCIDiseaseUtil.getGeneAliasCollection(zeroObject);
                IndexHits<Node> gNodeHits = geneidIndex.get(GENEID, geneSymbol);
                if (gNodeHits != null && gNodeHits.size() > 0) {
                    geneNode = gNodeHits.getSingle(); 
                    if (geneNode == null) {
                        //System.out.println(name + " could not retrieve geneNode from index");
                        return null;
                    }
                    if (!geneNode.hasProperty(BioEntityTypes.NODE_TYPE )) {
                    geneNode.setProperty(BioEntityTypes.NODE_TYPE,  BioEntityTypes.RB_GENE);
                    }
                    if (!geneNode.hasProperty(MESSAGE) && geneSymbol != null) {
                    geneNode.setProperty(MESSAGE,  geneSymbol);
                    }
                    if (geneAliasArray != null) {
                        if (!geneNode.hasProperty(GENE_ALIAS_COLLECTION)) {
                            geneNode.setProperty(GENE_ALIAS_COLLECTION, geneAliasArray);
                        }
                        //if (geneNode.has)
                    }
                    if (!geneNode.hasProperty(HUGO_GENE_SYMBOL) && geneSymbol != null) {
                        geneNode.setProperty(HUGO_GENE_SYMBOL, geneSymbol );
                    }
                } else {
                    geneNode = graphDb.createNode();
                    if (geneSymbol != null) {
                        geneidIndex.add(geneNode,GENEID, geneSymbol);  
                         
                        geneNode.setProperty(BioEntityTypes.NODE_TYPE,  BioEntityTypes.RB_GENE);
                        if (geneSymbol != null) {
                            geneNode.setProperty(HUGO_GENE_SYMBOL, geneSymbol );
                            geneNode.setProperty(MESSAGE, geneSymbol );
                        }
                        if (geneAliasArray != null) {
                           geneNode.setProperty(GENE_ALIAS_COLLECTION, geneAliasArray);
                        }  
                    } else {
                        //System.out.println("cannot create geneNode");
                        return null;
                    }
                }    
                String proteinId = processSequenceIdentification(zeroObject, geneNode);
                Node proteinNode = createProteinNode(geneNode,  geneSymbol, proteinId);
                //System.out.println("completed proteinNode");
                processSentence(zeroObject, geneNode, proteinNode, geneSymbol, proteinId);
                //System.out.println("completed sentence");
                gNodeHits.close();
                tx.success();
            } catch (Exception e) {
                // do we need to close gNodeHits.close() here: ask Manoj
                //System.out.println("exception caught");
                e.printStackTrace();
            } finally {
                //System.out.println("geneNode" + geneNode.toString());
                //System.out.println("finally");
                tx.finish();
                //System.out.println("came out of finally");
            }
            //System.out.println("completed geneNode");
            return geneNode;
        }
    }
}
