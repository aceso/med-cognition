/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package org.atgc.bio.util;

import com.mongodb.BasicDBList;
import com.mongodb.BasicDBObject;
import org.atgc.bio.BioEntityType;
import org.atgc.bio.NCIPathwayUtil;
import org.atgc.neo4j.NeoUtil;
import java.io.UnsupportedEncodingException;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.util.*;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.neo4j.graphdb.DynamicRelationshipType;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;
import org.neo4j.graphdb.Transaction;
import org.neo4j.graphdb.index.IndexHits;
import org.neo4j.rest.graphdb.RestGraphDatabase;
import org.neo4j.rest.graphdb.index.RestIndex;

/**
 * This is the National Cancer Index pathways import module. It can process a list of
 * pathway ShortName(s) mentioned in a mongo list collection called NCIPathwayList
 * and import the corresponding XML files on hotdiary.com into Mongo, and then
 * subsequently load them into Neo4J. It updates the status of pathways to DONE when
 * the pathways are imported (or merged) into Neo4J successfully. Otherwise, it marks 
 * them as ERROR in the Mongo NCIPathwayList as then they will not be repeatedly imported
 * in future if there is an error.
 * 
 * @author jtanisha-ee
 */
public class NCIPathway_Direct {
    
    final static int MAX_INDEX = 7181; // currently max urls on hotdiary.com
    final static int DOC_INDEX = 1;
    
   // public static final String DB_URL = "http://saibaba.local:7474/db/data";
    
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
    
    /* private static enum RelTypes implements RelationshipType {
       IS_INHIBITOR_OF, 
       IS_PART_OF_PATHWAY, 
       IS_OUTPUT_OF, 
       IS_INTERACTION_OF,
       IS_INPUT_OF,
       IS_AGENT_OF,
       IS_EDGETYPE_OF,
       IS_INCOMING_EDGE_OF,
       IS_OUTGOING_EDGE_OF
    } */
    
    // indexes
    private static RestGraphDatabase graphDb;
    private static RestIndex<Node> rbidIndex;
    private static RestIndex<Node> organismIndex;
    private static RestIndex<Node> interactionTypeIndex;
    private static RestIndex<Node> moleculeIdIndex;
    private static RestIndex<Node> ptmIndex;
    private static RestIndex<Node> roleTypeIndex;
    private static RestIndex<Node> locationIndex;
    private static RestIndex<Node> activityIndex;
    private static RestIndex<Node> functionIndex;
    private static RestIndex<Node> interactionIdIndex;
    
    private static final String LABELS = "labels";
    private static final String DB_PATH= "neo4j-shortest-path";
    public static final String NCI_VALUE = "@value";
    public static final String PATHWAY_ID = "PathwayId";
    public static final String SUBNET = "@subnet";
    public static final String ORGANISM = "Organism";
    public static final String LONG_NAME = "LongName";
    public static final String SHORT_NAME = "ShortName";
    public static final String SOURCE_ID = "SourceId";
    public static final String SOURCE = "Source";
    public static final String SOURCE_TEXT = "SourceText";
    public static final String NCI_SOURCE_TEXT = "#text";
    public static final String CURATOR_LIST = "CuratorList";
    public static final String REVIEWER_LIST = "ReviewerList";
    public static final String MESSAGE = "message";
    public static final String RBID = "rbid";
    public static final String PATHWAY_COMPONENT_LIST = "PathwayComponentList";
    public static final String INTERACTION_LIST = "InteractionList";
    public static final String INTERACTION_COMPONENT_LIST = "InteractionComponentList";
    public static final String EVIDENCE_LIST = "EvidenceList";
    public static final String REFERENCE_LIST = "ReferenceList";
    public static final String INTERACTION_TYPE = "@interaction_type";
    public static final String INTERACTION_ID = "InteractionId";
    public static final String ROLE_TYPE = "@role_type";
    public static final String MOLECULE_IDREF = "@molecule_idref";
    public static final String LABEL = "Label";
    public static final String LABEL_TYPE = "@label_type";
    public static final String LOCATION = "location";
    public static final String ACTIVITY_STATE = "activity-state";
    public static final String FUNCTION = "function";
    public static final String PREFERRED_SYMBOL = "preferred symbol";
    public static final String NAME = "Name";
    public static final String LONG_NAME_TYPE = "@long_name_type";
    public static final String NAME_TYPE = "@name_type";
    public static final String MOLECULE_TYPE = "@molecule_type";
    public static final String INTERACTION = "interaction";
    public static final String COMPLEX_COMPONENT_LIST = "ComplexComponentList";
    public static final String FAMILY_MEMBER_LIST = "FamilyMemberList";
    public static final String PTM_EXPRESSION = "PTMExpression";
    public static final String NCI_PROTEIN = "@protein";
    public static final String NCI_POSITION = "@position";
    public static final String NCI_AA = "@aa";
    public static final String NCI_MODIFICATION = "@modification";
    private static final String PART = "Part";
    private static final String WHOLE_MOLECULE_IDREF = "@whole_molecule_idref";
    private static final String PART_MOLECULE_IDREF = "@part_molecule_idref";
    private static final String NCI_START = "@start";
    private static final String NCI_END = "@end";
    private static final String MEMBER_MOLECULE_IDREF = "@member_molecule_idref";
    private static final String INTERACTION_IDREF = "@interaction_idref";
    private static final String INPUT = "input";
    private static final String INHIBITOR = "inhibitor";
    private static final String OUTPUT = "output";
    private static final String EDGE_TYPE = "edge-type";
    private static final String AGENT = "agent";
    private static final String INCOMING_EDGE = "incoming-edge";
    private static final String OUTGOING_EDGE = "outgoing-edge";
   
    public static final List roleTypeList = Arrays.asList(
            INPUT, 
            INHIBITOR,
            AGENT, 
            EDGE_TYPE, 
            INCOMING_EDGE, 
            OUTGOING_EDGE
    );
    
    protected static Log log = LogFactory.getLog(NCIPathway_Direct.class);
    
    private static void setup() throws URISyntaxException {
        //graphDb = new GraphDatabaseFactory().newEmbeddedDatabase( DB_PATH );
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
    
    public static void main(String[] args) throws java.io.IOException, URISyntaxException {
        List<Map> pathwayList = NCIPathwayUtil.getPathwayList(); // return all that are DUE
        Iterator<Map> pathwayIter = pathwayList.iterator();
        System.out.println("pathwayList return due =" + pathwayList.size());
        while (pathwayIter.hasNext()) {
            Map map = pathwayIter.next(); 
            String shortName = (String)map.get(SHORT_NAME); 
            //Node pathwayNode = createPathwayNode("txa2pathway");
            try {
                System.out.println("shortName =" + shortName);
                Node pathwayNode = createPathwayNode(shortName);
                NCIPathwayUtil.updateImportStatus(shortName, NCIPathwayUtil.DONE);
            } catch (java.io.IOException e) {
                NCIPathwayUtil.updateImportStatus(shortName, NCIPathwayUtil.ERROR);
                throw new RuntimeException(e);
            }
         }  
        //Node pathwayNode = createPathwayNode("a6b1_a6b4_integrin_pathway");
        //NCIPathwayUtil.updateImportStatus("a6b1_a6b4_integrin_pathway", "done");
    }
    
    private static String getNodeType(String moleculeType) {
        
        if (moleculeType.equals(BioEntityType.RB_COMPLEX)) {
            return BioEntityType.RB_COMPLEX.toString();
        } else if (moleculeType.equals(BioEntityType.RB_COMPOUND)) {
            return BioEntityType.RB_COMPOUND.toString();
        } else if (moleculeType.equals(BioEntityType.RB_MOLECULE_TYPE)) {
            return BioEntityType.RB_MOLECULE_TYPE.toString();
        } else if (moleculeType.equals(BioEntityType.RB_PROTEIN)) {
            return BioEntityType.RB_PROTEIN.toString();
        } else if (moleculeType.equals(BioEntityType.RB_RNA)) {
            return BioEntityType.RB_RNA.toString();
        }
        return null;
    }
    
    /*
     * This method checks if a relationship of a specific type exists between startNode and endNode
     * If it exists, it returns null, if it does not exist, then it creates it and returns the relationship.
     * 
     * @param startNode
     * @param endNode
     * @param rel
     * @return
     * @throws UnsupportedEncodingException 
     */
    /*
    public static Relationship checkCreateRel(Node startNode, Node endNode, String rel) throws UnsupportedEncodingException {
        // Check if at least one endNode matches getEndNode() of the relationship, if it matches then return null right away
        for (Relationship relationship : startNode.getRelationships(DynamicRelationshipType.withName(URLEncoder.encode(rel, "UTF-8")), Direction.BOTH)) {
            if (relationship.getEndNode().equals(endNode)) {
               return null;
            }
        }
        // If we come here, then the relationship does not exist for sure, so we should create it
        return startNode.createRelationshipTo(endNode, DynamicRelationshipType.withName(URLEncoder.encode(rel, "UTF-8")));
    }
     * */
    
    
    public void createLabels(Node outputNode, Map outputMap) {
        if (outputNode == null && outputMap.size() > 0) {
            if (outputMap.containsKey(LOCATION)) {  // add only if location present 
                outputNode.setProperty(LOCATION, outputMap.get(LOCATION));
                if (locationIndex == null) {
                    locationIndex = graphDb.index().forNodes(LOCATION);
                }
                locationIndex.add(outputNode, LOCATION, outputMap.get(LOCATION));
            }
            if (outputMap.containsKey(FUNCTION)) {
                outputNode.setProperty(FUNCTION, outputMap.get(FUNCTION));
                if (functionIndex == null) {
                    functionIndex = graphDb.index().forNodes(FUNCTION);
                }
                functionIndex.add(outputNode, FUNCTION, outputMap.get(FUNCTION));
            }
            if (outputMap.containsKey(ACTIVITY_STATE)) {
                outputNode.setProperty(ACTIVITY_STATE, outputMap.get(ACTIVITY_STATE));
                if (activityIndex == null) {
                    activityIndex = graphDb.index().forNodes(ACTIVITY_STATE);
                }
                activityIndex.add(outputNode, ACTIVITY_STATE, outputMap.get(ACTIVITY_STATE));
            }
        }        
    }
    
    
    /**
     * Creates nodes for the InteractionComponentList and links them through relationships.
     * Currently supports creating one or more input nodes, and one output node. It links
     * each input node to the output node using the isOutputOf relationship. Additionally
     * each input node and the output node isPartOf the pathwayNode and isInteractionOf
     * interactionNode. Input and output nodes here can be used as protein nodes in future.
     * In future, we will support other role types such as agent, inhibitor.
     * 
     * For each input node and the output node, we also create indexes for @role_type,
     * @molecule_idref and an optional index for location, if it exists.
     * 
     * We assume that the location of a protein does not depend on the interaction. It remains
     * the same. If it was in cytoplasm, it will continue to be in cytoplasm irrespective of
     * the interaction.
     * 
     * @param pathwayNode
     * @param interactionNode
     * @param moleculeList
     * @param outputMap 
     */
    public static void createNodeInteractionComponent(Node pathwayNode, Node interactionNode, List moleculeList, List outputMoleculeList) throws UnsupportedEncodingException {
              
         List<Node> outputNodeList = new ArrayList<Node>();
         for (Object obj : outputMoleculeList) {
             Map outputMap = (Map)obj;
             if (outputMap.size() > 0) {
                Node outputNode = graphDb.createNode();
            // Mutating operations go here
            // First add outputNode to graph

                if (moleculeIdIndex == null) {
                   moleculeIdIndex = graphDb.index().forNodes(MOLECULE_IDREF);
                }
                IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, outputMap.get(MOLECULE_IDREF));
                if (pNodeHits.size() > 0) { // if output node already exists
                    outputNode = pNodeHits.getSingle(); 
                    if (!outputNode.hasProperty(MOLECULE_IDREF)) {
                        outputNode.setProperty(MOLECULE_IDREF, outputMap.get(MOLECULE_IDREF));
                    }
                    if (!outputNode.hasProperty(MESSAGE)) {
                        outputNode.setProperty(MESSAGE, outputMap.get(MOLECULE_IDREF));
                    }
                    Relationship partOf = null;
                    if ((partOf = NeoUtil.checkCreateRel(outputNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                        partOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                    }

                    Relationship interactionOf = null;
                    if ((interactionOf = NeoUtil.checkCreateRel(outputNode, interactionNode, IS_INTERACTION_OF)) != null) {
                        interactionOf.setProperty(MESSAGE, IS_INTERACTION_OF);
                    }
                } else { // if output node does not exist
                    outputNode = graphDb.createNode();
                    //outputNode.setProperty(ROLE_TYPE, outputMap.get(ROLE_TYPE));
                    outputNode.setProperty(MOLECULE_IDREF, outputMap.get(MOLECULE_IDREF));
                    outputNode.setProperty(MESSAGE, outputMap.get(MOLECULE_IDREF));
                    moleculeIdIndex.add(outputNode, MOLECULE_IDREF, outputMap.get(MOLECULE_IDREF));   
                    Relationship partOf = outputNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                    partOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY );
                    Relationship interactionOf = outputNode.createRelationshipTo(interactionNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_INTERACTION_OF, "UTF-8")));
                    interactionOf.setProperty(MESSAGE, IS_INTERACTION_OF );
                }
                pNodeHits.close();
                if (outputMap.containsKey(LOCATION)) {  // add only if location present 
                    outputNode.setProperty(LOCATION, outputMap.get(LOCATION));
                    if (locationIndex == null) {
                        locationIndex = graphDb.index().forNodes(LOCATION);
                    }
                    locationIndex.add(outputNode, LOCATION, outputMap.get(LOCATION));
                }
                if (outputMap.containsKey(FUNCTION)) {
                    outputNode.setProperty(FUNCTION, outputMap.get(FUNCTION));
                    if (functionIndex == null) {
                        functionIndex = graphDb.index().forNodes(FUNCTION);
                    }
                    functionIndex.add(outputNode, FUNCTION, outputMap.get(FUNCTION));
                }
                if (outputMap.containsKey(ACTIVITY_STATE)) {
                    outputNode.setProperty(ACTIVITY_STATE, outputMap.get(ACTIVITY_STATE));
                    if (activityIndex == null) {
                        activityIndex = graphDb.index().forNodes(ACTIVITY_STATE);
                    }
                    activityIndex.add(outputNode, ACTIVITY_STATE, outputMap.get(ACTIVITY_STATE));
                }
                outputNodeList.add(outputNode);
                }
            }
            
            List<Map<String, Node>> inputList = null;
            List<Map<String, Node>> inhibitorList = null;
            List<Map<String, Node>> agentList = null;
            List<Map<String, Node>> edgeTypeList = null;
            List<Map<String, Node>> incomingEdgeList = null;
            List<Map<String, Node>> outgoingEdgeList = null;
            
            Iterator<Map<String, String>> moleculeIter = moleculeList.iterator();
            while (moleculeIter.hasNext()) {
               
               Node moleculeNode = null;
               
               // Add each role node to graph
               Map<String, String> moleculeMap = moleculeIter.next();
               if (moleculeIdIndex == null) {
                   moleculeIdIndex = graphDb.index().forNodes(MOLECULE_IDREF);
               }
            
               IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, moleculeMap.get(MOLECULE_IDREF));
               if (pNodeHits.size() > 0) { // if molecule node already exists
                   moleculeNode = pNodeHits.getSingle(); 
                   if (!moleculeNode.hasProperty(MOLECULE_IDREF)) {
                       moleculeNode.setProperty(MOLECULE_IDREF, moleculeMap.get(MOLECULE_IDREF));
                   }
                   if (!moleculeNode.hasProperty(MESSAGE)) {
                       moleculeNode.setProperty(MESSAGE, moleculeMap.get(MOLECULE_IDREF));
                   }
                   
                   if (outputNodeList != null && outputNodeList.size() > 0) {
                       for (Node outputNode : outputNodeList) {
                            Relationship outputOf = null;
                            if ((outputOf = NeoUtil.checkCreateRel(outputNode, moleculeNode, IS_OUTPUT_OF)) != null) {
                                outputOf.setProperty(MESSAGE, IS_OUTPUT_OF);
                                outputOf.setProperty(ROLE_TYPE, moleculeMap.get(ROLE_TYPE));
                            }
                       }
                   }
                   Relationship inpartOf = null;
                   if ((inpartOf = NeoUtil.checkCreateRel(moleculeNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                        inpartOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                   }
                   
                   Relationship ininteractionOf = null;
                   if ((ininteractionOf = NeoUtil.checkCreateRel(moleculeNode, interactionNode, IS_INTERACTION_OF)) != null) {
                        ininteractionOf.setProperty(MESSAGE, IS_INTERACTION_OF);
                   }
               } else { // if molecule node does not exist
                   moleculeNode = graphDb.createNode();
                   moleculeNode.setProperty(MOLECULE_IDREF, moleculeMap.get(MOLECULE_IDREF));
                   moleculeNode.setProperty(MESSAGE, moleculeMap.get(MOLECULE_IDREF));
                   moleculeIdIndex.add(moleculeNode, MOLECULE_IDREF, moleculeMap.get(MOLECULE_IDREF));
                   if (outputNodeList != null && outputNodeList.size() > 0) {
                       for (Node outputNode : outputNodeList) {
                            Relationship outputOf = outputNode.createRelationshipTo(moleculeNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_OUTPUT_OF, "UTF-8")));
                            outputOf.setProperty(MESSAGE, IS_OUTPUT_OF );
                            outputOf.setProperty(ROLE_TYPE, moleculeMap.get(ROLE_TYPE));
                       }
                   }
                   Relationship inpartOf = moleculeNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                   inpartOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                   Relationship ininteractionOf = moleculeNode.createRelationshipTo(interactionNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_INTERACTION_OF, "UTF-8")));
                   ininteractionOf.setProperty(MESSAGE, IS_INTERACTION_OF);
               }
               pNodeHits.close();
               
               //inputNode.setProperty(ROLE_TYPE, moleculeMap.get(ROLE_TYPE));
               Map map = new HashMap<String, Node>();
               map.put(moleculeMap.get(ROLE_TYPE), moleculeNode);
               if (moleculeMap.get(ROLE_TYPE).equals(INPUT)) {
                   if (inputList == null) {
                       inputList = new ArrayList<Map<String, Node>>();
                   }
                   inputList.add(map);
               } else if (moleculeMap.get(ROLE_TYPE).equals(INHIBITOR)) {
                   if (inhibitorList == null) {
                       inhibitorList = new ArrayList<Map<String, Node>>();
                   }
                   inhibitorList.add(map);
               } else if (moleculeMap.get(ROLE_TYPE).equals(AGENT)) {
                   if (agentList == null) {
                       agentList = new ArrayList<Map<String, Node>>();
                   }
                   agentList.add(map);
               } else if (moleculeMap.get(ROLE_TYPE).equals(EDGE_TYPE)) {
                   if (edgeTypeList == null) {
                       edgeTypeList = new ArrayList<Map<String, Node>>();
                   }
                   edgeTypeList.add(map);
               } else if (moleculeMap.get(ROLE_TYPE).equals(INCOMING_EDGE)) {
                   if (incomingEdgeList == null) {
                       incomingEdgeList = new ArrayList<Map<String, Node>>();
                   }
                   incomingEdgeList.add(map);
               } else if (moleculeMap.get(ROLE_TYPE).equals(OUTGOING_EDGE)) {
                   if (outgoingEdgeList == null) {
                       outgoingEdgeList = new ArrayList<Map<String, Node>>();
                   }
                   outgoingEdgeList.add(map);
               }
               
               if (moleculeMap.containsKey(LOCATION)) { 
                   moleculeNode.setProperty(LOCATION, moleculeMap.get(LOCATION));
               }
               if (moleculeMap.containsKey(FUNCTION)) { 
                   moleculeNode.setProperty(FUNCTION, moleculeMap.get(FUNCTION));
               }
               if (moleculeMap.containsKey(ACTIVITY_STATE)) { 
                   moleculeNode.setProperty(ACTIVITY_STATE, moleculeMap.get(ACTIVITY_STATE));
               }
               
               // Now add the indexes
               //if (roleTypeIndex == null) {
                 //  roleTypeIndex = graphDb.index().forNodes(ROLE_TYPE);
               //}
               //roleTypeIndex.add(moleculeNode, ROLE_TYPE, moleculeMap.get(ROLE_TYPE));
               
               
               if (moleculeMap.containsKey(LOCATION)) {
                  if (locationIndex == null) {
                     locationIndex = graphDb.index().forNodes(LOCATION);
                  }
                  locationIndex.add(moleculeNode, LOCATION, moleculeMap.get(LOCATION));
               } else if (moleculeMap.containsKey(ACTIVITY_STATE)) {
                  if (activityIndex == null) {
                     activityIndex = graphDb.index().forNodes(ACTIVITY_STATE);
                  }
                  activityIndex.add(moleculeNode, ACTIVITY_STATE, moleculeMap.get(ACTIVITY_STATE));
               } else if (moleculeMap.containsKey(FUNCTION)) {
                  if (functionIndex == null) {
                     functionIndex = graphDb.index().forNodes(FUNCTION);
                  }
                  functionIndex.add(moleculeNode, FUNCTION, moleculeMap.get(FUNCTION));
               }
               
               /*if (relOutputInputIndex == null) {
                   relOutputInputIndex = graphDb.index().forRelationships(NCIPathway.RelTypes.IS_OUTPUT_OF.toString()); 
               }
               pRelationshipHits = relOutputInputIndex.get(NCIPathway.RelTypes.IS_OUTPUT_OF.toString(), NCIPathway.RelTypes.IS_OUTPUT_OF.toString(), outputNode, moleculeNode);
               
               Relationship outputOf = null;
               /*if (pRelationshipHits.size() > 0) { // if relationship already exists
                //partOf = pRelationshipHits.getSingle(); 
               } else { */
               // add relation of this moleculeNode to outputNode
               // commented by jtanisha-ee
               /* if (outputMap != null) {
                  //outputOf = outputNode.createRelationshipTo(moleculeNode, NCIPathway.RelTypes.IS_OUTPUT_OF);
                  //outputOf.setProperty(MESSAGE, NCIPathway.RelTypes.IS_OUTPUT_OF );
                  outputOf = outputNode.createRelationshipTo(moleculeNode, DynamicRelationshipType.withName(IS_OUTPUT_OF));
                  outputOf.setProperty(MESSAGE, IS_OUTPUT_OF );
                  outputOf.setProperty(ROLE_TYPE, moleculeMap.get(ROLE_TYPE));
               } */
               //}
               //pRelationshipHits.close();
               
               /*if (relInputPathwayIndex == null) {
                   relInputPathwayIndex = graphDb.index().forRelationships(NCIPathway.RelTypes.IS_PART_OF.toString()); 
               }
               pRelationshipHits = relInputPathwayIndex.get(NCIPathway.RelTypes.IS_PART_OF.toString(), NCIPathway.RelTypes.IS_PART_OF.toString(), moleculeNode, pathwayNode);
               Relationship inpartOf = null;
               /*if (pRelationshipHits.size() > 0) { // if relationship already exists
                //partOf = pRelationshipHits.getSingle(); 
               } else {*/
               // add relation of this moleculeNode to pathwayNode
                  //inpartOf = moleculeNode.createRelationshipTo(pathwayNode, NCIPathway.RelTypes.IS_PART_OF);
                  //inpartOf.setProperty(MESSAGE, NCIPathway.RelTypes.IS_PART_OF );
                 /* inpartOf = moleculeNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(IS_PART_OF_PATHWAY));
                  inpartOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                  */
                  
               /*}
               pRelationshipHits.close(); */
               
               /*if (relInputInteractionIndex == null) {
                   relInputInteractionIndex = graphDb.index().forRelationships(NCIPathway.RelTypes.IS_INTERACTION_OF.toString()); 
               }
               pRelationshipHits = relInputInteractionIndex.get(NCIPathway.RelTypes.IS_INTERACTION_OF.toString(), NCIPathway.RelTypes.IS_INTERACTION_OF.toString(), moleculeNode, interactionNode);
               Relationship ininteractionOf = null;
               /*if (pRelationshipHits.size() > 0) { // if relationship already exists
                //partOf = pRelationshipHits.getSingle();
               } else {*/
               // add relation of this moleculeNode to pathwayNode
                  //ininteractionOf = moleculeNode.createRelationshipTo(interactionNode, NCIPathway.RelTypes.IS_INTERACTION_OF);
                  //ininteractionOf.setProperty(MESSAGE, NCIPathway.RelTypes.IS_INTERACTION_OF);
                  
               /*}
               pRelationshipHits.close();*/
            }    
            
            if (inputList != null) {
                Iterator<Map<String, Node>> inputIter = inputList.iterator(); 
                while (inputIter.hasNext()) {
                   Map<String, Node> inputMap = inputIter.next();
                   Iterator<String> inputKeys = inputMap.keySet().iterator();
                   String input = null;
                   Node inputNode = null;
                   if (inputKeys.hasNext()) {
                       input = inputKeys.next();
                       inputNode = inputMap.get(input);
                   }
                   if (agentList != null) {
                       Iterator<Map<String, Node>> agentIter = agentList.iterator();
                       while (agentIter.hasNext()) {
                           Map<String, Node> agentMap = agentIter.next();
                           Iterator<String> roleKeys = agentMap.keySet().iterator();
                           if (roleKeys.hasNext()) {
                               String role = roleKeys.next();
                               Node node = agentMap.get(role);
                               if (inputNode != null) {
                                   if (node != null) {
                                      Relationship rel = null;
                                      if ((rel = NeoUtil.checkCreateRel(node, inputNode, IS_AGENT_OF)) != null) {
                                          rel.setProperty(MESSAGE, IS_AGENT_OF);
                                      }
                                      if (outputNodeList != null && outputNodeList.size() > 0) {
                                          for (Node outputNode : outputNodeList) {
                                              if ((rel = NeoUtil.checkCreateRel(node, outputNode, IS_AGENT_OF)) != null) {
                                                 rel.setProperty(MESSAGE, IS_AGENT_OF);
                                              }
                                          }
                                      }
                                   }
                               }
                           }
                       }
                   }
                   
                   if (inhibitorList != null) {
                       Iterator<Map<String, Node>> inhibitorIter = inhibitorList.iterator();
                       while (inhibitorIter.hasNext()) {
                           Map<String, Node> inhibitorMap = inhibitorIter.next();
                           Iterator<String> roleKeys = inhibitorMap.keySet().iterator();
                           if (roleKeys.hasNext()) {
                               String role = roleKeys.next();
                               Node node = inhibitorMap.get(role);
                               Relationship rel = null;
                               if ((rel = NeoUtil.checkCreateRel(node, inputNode, IS_INHIBITOR_OF)) != null) {
                                   rel.setProperty(MESSAGE, IS_INHIBITOR_OF);
                               }
                               if (outputNodeList != null && outputNodeList.size() > 0) {
                                   for (Node outputNode : outputNodeList) {                                      
                                      if ((rel = NeoUtil.checkCreateRel(node, outputNode, IS_INHIBITOR_OF)) != null) {
                                          rel.setProperty(MESSAGE, IS_INHIBITOR_OF);
                                      }
                                   }
                               }
                            }
                       }
                   }
                   
                   if (edgeTypeList != null) {
                       Iterator<Map<String, Node>> edgeTypeIter = edgeTypeList.iterator();
                       while (edgeTypeIter.hasNext()) {
                           Map<String, Node> edgeTypeMap = edgeTypeIter.next();
                           Iterator<String> roleKeys = edgeTypeMap.keySet().iterator();
                           if (roleKeys.hasNext()) {
                               String role = roleKeys.next();
                               Node node = edgeTypeMap.get(role);
                               Relationship rel = null;
                               if ((rel = NeoUtil.checkCreateRel(node, inputNode, IS_EDGETYPE_OF)) != null) {
                                   rel.setProperty(MESSAGE, IS_EDGETYPE_OF);
                               }
                               if (outputNodeList != null && outputNodeList.size() > 0) {
                                  for (Node outputNode : outputNodeList) {                                               
                                      if ((rel = NeoUtil.checkCreateRel(node, outputNode, IS_EDGETYPE_OF)) != null) {
                                        rel.setProperty(MESSAGE, IS_EDGETYPE_OF);
                                      }
                                  }
                               }
                           }
                       }
                   }
                   if (outgoingEdgeList != null) {
                       Iterator<Map<String, Node>> outgoingEdgeIter = outgoingEdgeList.iterator();
                       while (outgoingEdgeIter.hasNext()) {
                           Map<String, Node> outgoingEdgeMap = outgoingEdgeIter.next();
                           Iterator<String> roleKeys = outgoingEdgeMap.keySet().iterator();
                           if (roleKeys.hasNext()) {
                               String role = roleKeys.next();
                               Node node = outgoingEdgeMap.get(role);
                               Relationship rel = null;
                               if ((rel = NeoUtil.checkCreateRel(node, inputNode, IS_OUTGOING_EDGE_OF)) != null) {
                                   rel.setProperty(MESSAGE, IS_OUTGOING_EDGE_OF);
                               }
                               if (outputNodeList != null && outputNodeList.size() > 0) {                                          
                                   for (Node outputNode : outputNodeList) {
                                        if ((rel = NeoUtil.checkCreateRel(node, outputNode, IS_OUTGOING_EDGE_OF)) != null) {
                                            rel.setProperty(MESSAGE, IS_OUTGOING_EDGE_OF);
                                        }
                                   }
                               }
                           }
                       }
                   }
                   if (incomingEdgeList != null) {
                       Iterator<Map<String, Node>> incomingEdgeIter = incomingEdgeList.iterator();
                       while (incomingEdgeIter.hasNext()) {
                           Map<String, Node> incomingEdgeMap = incomingEdgeIter.next();
                           Iterator<String> roleKeys = incomingEdgeMap.keySet().iterator();
                           if (roleKeys.hasNext()) {
                               String role = roleKeys.next();
                               Node node = incomingEdgeMap.get(role);
                               Relationship rel = null;
                               if ((rel = NeoUtil.checkCreateRel(node, inputNode, IS_INCOMING_EDGE_OF)) != null) {
                                   rel.setProperty(MESSAGE, IS_INCOMING_EDGE_OF);
                               }
                               if (outputNodeList != null && outputNodeList.size() > 0) {          
                                  for (Node outputNode : outputNodeList) {
                                        if ((rel = NeoUtil.checkCreateRel(node, outputNode, IS_INCOMING_EDGE_OF)) != null) {
                                            rel.setProperty(MESSAGE, IS_INCOMING_EDGE_OF);
                                        }
                                  }
                               }
                           }
                       } // while
                }
            }
        } 
    }
    
    /**
     * Processes the InteractionComponentList by identifying each moleculeNode and outputNode.
     * Currently agent and other @role_type are not supported. Once these are identified
     * we call {@link #createNodeInteractionComponent} to actually create the nodes in
     * graph.
     * 
     * @param pathwayNode
     * @param interactionNode
     * @param interaction 
     */
    public static void processInteractionComponent(Node pathwayNode, Node interactionNode, BasicDBObject interaction) throws UnsupportedEncodingException {
      
        Object interactionObj = interaction.get(INTERACTION_COMPONENT_LIST);
        if (!interactionObj.getClass().getName().equals("java.lang.String")) {
            BasicDBList interactionComponentList = ((BasicDBList)interaction.get(INTERACTION_COMPONENT_LIST));
            Iterator interactionComponentIter = interactionComponentList.iterator();
            List moleculeList = new ArrayList();
            List outputMoleculeList = new ArrayList();
            Map roleMap = new HashMap();
            Map outputMap = new HashMap();
            //List<Map<String, Node>> inputList = null;

            while (interactionComponentIter.hasNext()) {
                BasicDBObject interactionComponent = (BasicDBObject)interactionComponentIter.next();   
                String roleType = interactionComponent.getString(ROLE_TYPE);
                String moleculeIdref = interactionComponent.getString(MOLECULE_IDREF);                                       
                String location = null;
                String activityState = null;
                String function = null;   // is this a Java reserved word?
                if (interactionComponent.containsKey(LABEL)) { // only if this exists
                    Object label = interactionComponent.get(LABEL);
                    BasicDBObject labelBson;
                    BasicDBList labelBsonList;
                    if (label.getClass().getName().equals("com.mongodb.BasicDBObject")) {
                        labelBson = (BasicDBObject)interactionComponent.get(LABEL);
                        String labelType = labelBson.getString(LABEL_TYPE);
                        if (labelType.equals(LOCATION)) { // only if it is location
                        location = labelBson.getString(NCI_VALUE);
                        } else if (labelType.equals(ACTIVITY_STATE)) {
                        activityState = labelBson.getString(NCI_VALUE);
                        } else if (labelType.equals(FUNCTION)) {
                        function = labelBson.getString(NCI_VALUE);
                        }                   
                    } else if (label.getClass().getName().equals("com.mongodb.BasicDBList")) {
                        labelBsonList = (BasicDBList)interactionComponent.get(LABEL);
                        Iterator labelIter = labelBsonList.iterator();                                                       
                        while (labelIter.hasNext()) {
                            labelBson = (BasicDBObject)labelIter.next();
                            String labelType = labelBson.getString(LABEL_TYPE);
                            if (labelType.equals(LOCATION)) { // only if it is location
                            location = labelBson.getString(NCI_VALUE);
                            } else if (labelType.equals(ACTIVITY_STATE)) {
                                activityState = labelBson.getString(NCI_VALUE);
                            } else if (labelType.equals(FUNCTION)) {
                                function = labelBson.getString(NCI_VALUE);
                            }
                        }
                        if (roleTypeList.contains(roleType)) {
                            if (location != null) {
                            roleMap.put(LOCATION, location);
                            }
                            if (activityState != null) {
                            roleMap.put(ACTIVITY_STATE, activityState);
                            }
                            if (function != null) {
                                roleMap.put(FUNCTION, function);
                            }
                            roleMap.put(ROLE_TYPE, roleType);
                            roleMap.put(MOLECULE_IDREF, moleculeIdref);
                            moleculeList.add(roleMap);
                        } else {
                            if (location != null) {
                            outputMap.put(LOCATION, location);
                            }
                            if (activityState != null) {
                                outputMap.put(ACTIVITY_STATE, activityState);
                            }
                            if (function != null) {
                                outputMap.put(FUNCTION, function);
                            }
                            outputMap.put(ROLE_TYPE, roleType);
                            outputMap.put(MOLECULE_IDREF, moleculeIdref);     
                            outputMoleculeList.add(outputMap);
                        }
                    }
                }
            }
            createNodeInteractionComponent(pathwayNode, interactionNode, moleculeList, outputMoleculeList);
        }
    }
    
    
    public static void createPartMolecule(Node moleculeNode, Node pathwayNode, BasicDBObject part) throws java.io.UnsupportedEncodingException {
           if (part != null && moleculeNode != null && pathwayNode != null) {
                String wholeMoleculeIdref = part.getString(WHOLE_MOLECULE_IDREF);
                //String partMoleculeIdref = part.getString(PART_MOLECULE_IDREF);
                String start = part.getString(NCI_START);
                String end = part.getString(NCI_END);
                //tx = graphDb.beginTx();
                Node wholeMoleculeNode = null;
                try {
                    IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, wholeMoleculeIdref);
                    if (pNodeHits.size() > 0) { // if output node already exists
                        wholeMoleculeNode = pNodeHits.getSingle();
                        Relationship rel = null;
                        if ((rel = NeoUtil.checkCreateRel(moleculeNode, wholeMoleculeNode, IS_PART_OF)) != null) {
                            rel.setProperty(MESSAGE, IS_PART_OF);
                        }
                        if ((rel = NeoUtil.checkCreateRel(moleculeNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                            rel.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                        }
                    } else { // if output node does not exist
                        wholeMoleculeNode = graphDb.createNode();
                        wholeMoleculeNode.setProperty(MESSAGE, wholeMoleculeIdref);  // set the molecule name instead of id
                        moleculeIdIndex.add(wholeMoleculeNode, MESSAGE, wholeMoleculeIdref);
                         // if (!moleculeNode.hasRelationship(DynamicRelationshipType.withName(IS_PART_OF), Direction.BOTH)) {
                        Relationship partOf = moleculeNode.createRelationshipTo(wholeMoleculeNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF, "UTF-8")));
                        partOf.setProperty(MESSAGE, IS_PART_OF);
                        // }

                        // if (!moleculeNode.hasRelationship(DynamicRelationshipType.withName(IS_PART_OF_PATHWAY), Direction.BOTH)) {
                        partOf = moleculeNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                        partOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                    //}
                    }
                    pNodeHits.close();
                    if (!moleculeNode.hasProperty(NCI_START)) {
                        moleculeNode.setProperty(NCI_START, start);
                        moleculeIdIndex.add(moleculeNode, NCI_START, start);
                    }
                    if (!moleculeNode.hasProperty(NCI_END)) {
                        moleculeNode.setProperty(NCI_END, end);
                        moleculeIdIndex.add(moleculeNode, NCI_END, end);
                    }
                    //tx.success();
                } finally {
                    //tx.finish();
                }
            }   
    }
    
    
    public static void processMoleculeList(Node pathwayNode, BasicDBList moleculeList) throws UnsupportedEncodingException {
        Iterator iter = moleculeList.iterator();
        if (moleculeIdIndex == null) {
            moleculeIdIndex = graphDb.index().forNodes(MOLECULE_IDREF);
        }
        while (iter.hasNext()) {
            BasicDBObject molecule = (BasicDBObject)iter.next();
            String moleculeType = molecule.getString(MOLECULE_TYPE);
            String moleculeId = molecule.getString(BioEntityType.NCI_ID.toString());
            //Transaction tx = graphDb.beginTx();
            Node moleculeNode = null;
            try {
                IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, moleculeId);
                if (pNodeHits.size() > 0) { // if output node already exists
                    moleculeNode = pNodeHits.getSingle(); 
                    if (!moleculeNode.hasProperty(MOLECULE_IDREF)) {
                        moleculeNode.setProperty(MOLECULE_IDREF, moleculeId);
                    }
                    if (!moleculeNode.hasProperty(MOLECULE_TYPE)) {
                        moleculeNode.setProperty(MOLECULE_TYPE, moleculeType);
                    }
                    if (!moleculeNode.hasProperty(BioEntityType.NODE_TYPE.toString())) {
                        moleculeNode.setProperty(BioEntityType.NODE_TYPE.toString(), getNodeType(moleculeType));
                    }
                } else { // if output node does not exist
                    moleculeNode = graphDb.createNode();
                    moleculeNode.setProperty(MOLECULE_IDREF, moleculeId);
                    moleculeIdIndex.add(moleculeNode, MOLECULE_IDREF, moleculeId); 
                    moleculeNode.setProperty(MOLECULE_TYPE, moleculeType);
                    moleculeIdIndex.add(moleculeNode, MOLECULE_TYPE, moleculeType);
                    moleculeNode.setProperty(BioEntityType.NODE_TYPE.toString(), getNodeType(moleculeType));
                }
                pNodeHits.close();
                //tx.success();
            } finally {
                //tx.finish();
            }
            
            Object partObj = molecule.get(PART);
            if (partObj != null) {
                if (partObj.getClass().getName().equals("com.mongodb.BasicDBObject")) {
                    BasicDBObject part = (BasicDBObject)partObj;
                    createPartMolecule(moleculeNode, pathwayNode, part);
                } else if (partObj.getClass().getName().equals("com.mongodb.BasicDBList")) {
                    BasicDBList partList = (BasicDBList) partObj;
                    for (Object part : partList) {
                        createPartMolecule(moleculeNode, pathwayNode, (BasicDBObject)part);
                    }                 
                }
            }
            
    /*        if (part != null) {
                String wholeMoleculeIdref = part.getString(WHOLE_MOLECULE_IDREF);
                //String partMoleculeIdref = part.getString(PART_MOLECULE_IDREF);
                String start = part.getString(NCI_START);
                String end = part.getString(NCI_END);
                //tx = graphDb.beginTx();
                Node wholeMoleculeNode = null;
                try {
                    IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, wholeMoleculeIdref);
                    if (pNodeHits.size() > 0) { // if output node already exists
                        wholeMoleculeNode = pNodeHits.getSingle();
                        Relationship rel = null;
                        if ((rel = NeoUtil.checkCreateRel(moleculeNode, wholeMoleculeNode, IS_PART_OF)) != null) {
                            rel.setProperty(MESSAGE, IS_PART_OF);
                        }
                        if ((rel = NeoUtil.checkCreateRel(moleculeNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                            rel.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                        }
                    } else { // if output node does not exist
                        wholeMoleculeNode = graphDb.createNode();
                        wholeMoleculeNode.setProperty(MESSAGE, wholeMoleculeIdref);  // set the molecule name instead of id
                        moleculeIdIndex.add(wholeMoleculeNode, MESSAGE, wholeMoleculeIdref);
                         // if (!moleculeNode.hasRelationship(DynamicRelationshipType.withName(IS_PART_OF), Direction.BOTH)) {
                        Relationship partOf = moleculeNode.createRelationshipTo(wholeMoleculeNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF, "UTF-8")));
                        partOf.setProperty(MESSAGE, IS_PART_OF);
                        // }

                        // if (!moleculeNode.hasRelationship(DynamicRelationshipType.withName(IS_PART_OF_PATHWAY), Direction.BOTH)) {
                        partOf = moleculeNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                        partOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                    //}
                    }
                    pNodeHits.close();
                    if (!moleculeNode.hasProperty(NCI_START)) {
                        moleculeNode.setProperty(NCI_START, start);
                        moleculeIdIndex.add(moleculeNode, NCI_START, start);
                    }
                    if (!moleculeNode.hasProperty(NCI_END)) {
                        moleculeNode.setProperty(NCI_END, end);
                        moleculeIdIndex.add(moleculeNode, NCI_END, end);
                    }
                    //tx.success();
                } finally {
                    //tx.finish();
                }
            }
            */
            
            BasicDBList familyMemberList = (BasicDBList)molecule.get(FAMILY_MEMBER_LIST);
            if (familyMemberList != null) {
                Iterator familyIter = familyMemberList.iterator();
                while (familyIter.hasNext()) {
                    BasicDBObject familyMolecule = (BasicDBObject)familyIter.next();
                    String memberMoleculeIdref = familyMolecule.getString(MEMBER_MOLECULE_IDREF);
                    //tx = graphDb.beginTx();
                    Node familyMoleculeNode = null;
                    try {
                        IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, memberMoleculeIdref);
                        if (pNodeHits.size() > 0) { // if output node already exists
                            familyMoleculeNode = pNodeHits.getSingle();
                            Relationship rel = null;
                            if ((rel = NeoUtil.checkCreateRel(familyMoleculeNode, moleculeNode, IS_IN_FAMILY_OF)) != null) {
                               rel.setProperty(MESSAGE, IS_IN_FAMILY_OF);
                            }
                            if ((rel = NeoUtil.checkCreateRel(familyMoleculeNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                               rel.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                            }
                        } else { // if output node does not exist
                            familyMoleculeNode = graphDb.createNode();
                            familyMoleculeNode.setProperty(MESSAGE, memberMoleculeIdref);  // set the molecule name instead of id
                            moleculeIdIndex.add(familyMoleculeNode, MESSAGE, memberMoleculeIdref);
                            Relationship partOf = familyMoleculeNode.createRelationshipTo(moleculeNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_IN_FAMILY_OF, "UTF-8")));
                            partOf.setProperty(MESSAGE, IS_IN_FAMILY_OF);
                            partOf = familyMoleculeNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                            partOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                                    
                        }             
                        pNodeHits.close();
                        //tx.success();
                    } finally {
                        //tx.finish();
                    }
                }
            }
            
            Object complexObj = molecule.get(COMPLEX_COMPONENT_LIST);
            if ((complexObj != null) && !complexObj.getClass().getName().equals("java.lang.String")) {
                BasicDBList complexComponent = (BasicDBList)complexObj;
                Iterator complexIter = complexComponent.iterator();
                while (complexIter.hasNext()) {
                    BasicDBObject member = (BasicDBObject)complexIter.next();
                    String moleculeIdref = member.getString(MOLECULE_IDREF);
                    //tx = graphDb.beginTx();
                    Node componentMoleculeNode = null;
                    try {
                        IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, moleculeIdref);
                        if (pNodeHits.size() > 0) { // if output node already exists
                           componentMoleculeNode = pNodeHits.getSingle();
                           Relationship rel = null;
                           if ((rel = NeoUtil.checkCreateRel(componentMoleculeNode, moleculeNode, IS_PART_OF_COMPLEX)) != null) {
                               rel.setProperty(MESSAGE, IS_PART_OF_COMPLEX);
                           }
                           if ((rel = NeoUtil.checkCreateRel(componentMoleculeNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                               rel.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                           }                          
                        } else { // if componentMolecule node does not exist
                           componentMoleculeNode = graphDb.createNode();
                           componentMoleculeNode.setProperty(MESSAGE, moleculeIdref);  // set the molecule name instead of id
                           moleculeIdIndex.add(componentMoleculeNode, MESSAGE, moleculeIdref);
                           Relationship partOf = componentMoleculeNode.createRelationshipTo(moleculeNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_COMPLEX, "UTF-8")));
                           partOf.setProperty(MESSAGE, IS_PART_OF_COMPLEX);
                           partOf = componentMoleculeNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                           partOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                        }
                        pNodeHits.close();
                        //tx.success();
                    } finally {
                        //tx.finish();
                    }
                    
                    
                    BasicDBList ptmExpressionList = (BasicDBList)member.get(PTM_EXPRESSION);
                    if (ptmExpressionList != null) {
                        Iterator ptmIter = ptmExpressionList.iterator();
                        while (ptmIter.hasNext()) {
                            BasicDBObject ptmExpression = (BasicDBObject)ptmIter.next();
                            String protein = ptmExpression.getString(NCI_PROTEIN);
                            String position = ptmExpression.getString(NCI_POSITION);
                            String aa = ptmExpression.getString(NCI_AA);
                            String modification = ptmExpression.getString(NCI_MODIFICATION);
                            //tx = graphDb.beginTx();
                            Node ptmNode = null;
                            try {
                                if (ptmIndex == null) {
                                    ptmIndex = graphDb.index().forNodes(PTM_EXPRESSION);
                                }
                                String ptmKey = moleculeId + "-" +  moleculeIdref + "-" + protein + "-" + position + "-" + aa + "-"  + URLEncoder.encode(modification, "UTF-8");
                                IndexHits<Node> pNodeHits = ptmIndex.get(PTM_EXPRESSION, ptmKey);
                                // if ptms were deleted, a reimport does not delete them currently
                                if (pNodeHits.size() > 0) { // if output node already exists
                                    ptmNode = pNodeHits.getSingle();
                                    Relationship rel = null;
                                    if ((rel = NeoUtil.checkCreateRel(ptmNode, moleculeNode, IS_COMPLEX_COMPONENT_OF)) != null) {
                                        rel.setProperty(MESSAGE, IS_COMPLEX_COMPONENT_OF);
                                    }
                                    if ((rel = NeoUtil.checkCreateRel(ptmNode, componentMoleculeNode, IS_MODIFICATION_OF)) != null) {
                                        rel.setProperty(MESSAGE, IS_MODIFICATION_OF);
                                    }
                                    if ((rel = NeoUtil.checkCreateRel(ptmNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                                        rel.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                                    }                                    
                                } else { // if output node does not exist
                                    ptmNode = graphDb.createNode();
                                    String message = modification + "-" + protein + "-" + moleculeIdref;
                                    ptmNode.setProperty(MESSAGE, message);  // set the molecule name instead of id
                                    ptmIndex.add(ptmNode, MESSAGE, message);
                                    if (!ptmNode.hasProperty(NCI_PROTEIN)) {
                                        ptmNode.setProperty(NCI_PROTEIN, protein);
                                        ptmIndex.add(ptmNode, NCI_PROTEIN, protein);
                                    }
                                    if (!ptmNode.hasProperty(NCI_POSITION)) {
                                        ptmNode.setProperty(NCI_POSITION, position);
                                        ptmIndex.add(ptmNode, NCI_POSITION, position);
                                    }
                                    ptmIndex.add(ptmNode, PTM_EXPRESSION, ptmKey);
                                
                                    Relationship componentOf = ptmNode.createRelationshipTo(moleculeNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_COMPLEX_COMPONENT_OF, "UTF-8")));
                                    componentOf.setProperty(MESSAGE, IS_COMPLEX_COMPONENT_OF);
                                    componentOf = ptmNode.createRelationshipTo(componentMoleculeNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_MODIFICATION_OF, "UTF-8")));
                                    componentOf.setProperty(MESSAGE, IS_MODIFICATION_OF);
                                    componentOf = ptmNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                                    componentOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                                }
                                pNodeHits.close();
                                //tx.success();
                            } finally {
                                //tx.finish();
                            }
                        }
                    }
                }
            }
            
            Object nameObj = molecule.get(NAME);
            if (nameObj.getClass().getName().equals("com.mongodb.BasicDBObject")) {
                BasicDBObject name = (BasicDBObject)nameObj;
                //String nameType = name.getString(NAME_TYPE);
                String longNameType = name.getString(LONG_NAME_TYPE);
                longNameType = URLEncoder.encode(longNameType, "UTF-8");
                String value = name.getString(NCI_VALUE);
                //tx = graphDb.beginTx();
                moleculeNode = null;
                
                try {
                    IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, moleculeId);
                    if (pNodeHits.size() > 0) { // if output node already exists
                       moleculeNode = pNodeHits.getSingle();
                       moleculeNode.setProperty(MESSAGE, value);  // set the molecule name instead of id
                       
                       if (!moleculeNode.hasProperty(longNameType)) {
                           moleculeNode.setProperty(longNameType, value);  // for eg. preferred symbol : alpha6/beta4 Integrin/SHC
                           moleculeIdIndex.add(moleculeNode, longNameType, value);
                       }
                       Relationship rel = null;
                       if ((rel = NeoUtil.checkCreateRel(moleculeNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                            rel.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                       }                       
                    } else { // if output node does not exist
                       moleculeNode = graphDb.createNode();
                       moleculeNode.setProperty(MESSAGE, value);
                       moleculeNode.setProperty(longNameType, value);
                       moleculeIdIndex.add(moleculeNode, longNameType, value);
                       Relationship partOf = moleculeNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                       partOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                    }
                    pNodeHits.close();
                    //tx.success();
                } finally {
                    //tx.finish();
                }
                
            } else if (nameObj.getClass().getName().equals("com.mongodb.BasicDBList")) {
                BasicDBList name = (BasicDBList)nameObj;   
                Iterator nameIter = name.iterator();
                while (nameIter.hasNext()) {
                    BasicDBObject nameEntry = (BasicDBObject)nameIter.next();
                    String longNameType = nameEntry.getString(LONG_NAME_TYPE);
                    longNameType = URLEncoder.encode(longNameType, "UTF-8");
                    String value = nameEntry.getString(NCI_VALUE);
                    //tx = graphDb.beginTx();
                    moleculeNode = null;
                    try {
                        IndexHits<Node> pNodeHits = moleculeIdIndex.get(MOLECULE_IDREF, moleculeId);
                        if (pNodeHits.size() > 0) { // if output node already exists
                            moleculeNode = pNodeHits.getSingle(); 
                            if (!moleculeNode.hasProperty(MOLECULE_IDREF)) {
                                moleculeNode.setProperty(MOLECULE_IDREF, moleculeId);
                            }
                            moleculeNode.setProperty(MESSAGE, value);  // set the molecule name instead of id

                            if (!moleculeNode.hasProperty(longNameType)) {
                                moleculeNode.setProperty(longNameType, value);  // for eg. preferred symbol : alpha6/beta4 Integrin/SHC
                                moleculeIdIndex.add(moleculeNode, longNameType, value);
                            }
                            Relationship rel = null;
                            if ((rel = NeoUtil.checkCreateRel(moleculeNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                                 rel.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                            }                            
                        } else { // if output node does not exist
                            moleculeNode = graphDb.createNode();
                            moleculeNode.setProperty(MOLECULE_IDREF, moleculeId);
                            moleculeNode.setProperty(MESSAGE, value);
                            moleculeNode.setProperty(longNameType, value);
                            moleculeIdIndex.add(moleculeNode, longNameType, value);
                            Relationship partOf = moleculeNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                            partOf.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                        }
                        pNodeHits.close();
                        //tx.success();
                    } finally {
                        //tx.finish();
                    }
                }
            }
            
        }
    }
    
    /**
     * Process the InteractionList section, which also contains EvidenceList, ReferenceList
     * and InteractionComponentList. Here we also create the interaction node and it's indexes.
     * We also make the interaction node isPartOf the pathwayNode.
     * 
     * @param pathwayNode
     * @param interactionList 
     */
    public static void processInteractionList(Node pathwayNode, BasicDBList interactionList) throws UnsupportedEncodingException {
        //BasicDBList interactionList = (BasicDBList)pathwayInfo.get((Object)INTERACTION_LIST);
        Iterator iter = interactionList.iterator();
        while (iter.hasNext()) {
            BasicDBObject interaction = (BasicDBObject)iter.next();
            //System.out.println("interaction = " + interaction.toString());
            String interactionType = interaction.getString(INTERACTION_TYPE);
            String interactionId = interaction.getString(BioEntityType.NCI_ID.toString());
            
            BasicDBList evidenceList = ((BasicDBList)interaction.get(EVIDENCE_LIST));
            Iterator evidenceIter = evidenceList.iterator();
            String[] evidenceArray;
            List<String> eList = new ArrayList();
            while (evidenceIter.hasNext()) {
               eList.add(((BasicDBObject)evidenceIter.next()).getString(NCI_VALUE));
            }
            evidenceArray = eList.toArray(new String[0]);
            
            BasicDBList referenceList = ((BasicDBList)interaction.get(REFERENCE_LIST));
            String[] referenceArray = null;
            if (referenceList != null) {
                Iterator referenceIter = referenceList.iterator();
                
                List<String> rList = new ArrayList();
                while (referenceIter.hasNext()) {
                   rList.add(((BasicDBObject)referenceIter.next()).getString(NCI_VALUE));
                }
                referenceArray = rList.toArray(new String[0]);
            }
            
            Node interactionNode = null;
            //Transaction tx = graphDb.beginTx();
            try {
// Mutating operations go here
                if (interactionIdIndex == null) {
                   interactionIdIndex = graphDb.index().forNodes(INTERACTION_ID);
                }
                IndexHits<Node> pNodeHits = interactionIdIndex.get(INTERACTION_ID, interactionId);
                if (pNodeHits.size() > 0) { // if interactionNode already exists
                   interactionNode = pNodeHits.getSingle(); 
                   if (!interactionNode.hasProperty(INTERACTION_ID)) {
                       interactionNode.setProperty(INTERACTION_ID, interactionId);
                   }
                   Relationship rel = null;
                   if ((rel = NeoUtil.checkCreateRel(interactionNode, pathwayNode, IS_PART_OF_PATHWAY)) != null) {
                        rel.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                   }
                } else { // if interaction node does not exist
                   interactionNode = graphDb.createNode();
                   interactionNode.setProperty(INTERACTION_ID, interactionId);
                   interactionIdIndex.add(interactionNode, INTERACTION_ID, interactionId);
                   Relationship relationship = interactionNode.createRelationshipTo(pathwayNode, DynamicRelationshipType.withName(URLEncoder.encode(IS_PART_OF_PATHWAY, "UTF-8")));
                   relationship.setProperty(MESSAGE, IS_PART_OF_PATHWAY);
                }
                pNodeHits.close();
                if (interactionTypeIndex == null) {
                   interactionTypeIndex = graphDb.index().forNodes(INTERACTION_TYPE);
                }
                if (!interactionNode.hasProperty(INTERACTION_TYPE)) {
                    interactionNode.setProperty(INTERACTION_TYPE, interactionType);
                    interactionTypeIndex.add(interactionNode, INTERACTION_TYPE, interactionType);
                    if (!interactionNode.hasProperty(MESSAGE)) {
                       interactionNode.setProperty(MESSAGE, interactionType + " " + INTERACTION + " (" + interactionId + ")");
                   }
                }
                if (!interactionNode.hasProperty(EVIDENCE_LIST)) {
                    interactionNode.setProperty(EVIDENCE_LIST, evidenceArray);
                }
                if (referenceArray != null) {
                    if (!interactionNode.hasProperty(REFERENCE_LIST)) {
                        interactionNode.setProperty(REFERENCE_LIST, referenceArray);
                    }
                }
                //tx.success();
            } finally {
                //tx.finish();
            }
            processInteractionComponent(pathwayNode, interactionNode, interaction);
        }
        
    }
    
    /**
     * 
     * @param name
     * @return
     * @throws java.io.IOException
     * @throws java.net.URISyntaxException 
     */
    public static Node createPathwayNode(String name) throws java.io.IOException, java.net.URISyntaxException {
        NCIPathwayUtil.addPathway(name);
        //Map map = NCIPathwayUtil.getPathwayObject(name);
        //Map pathwayInfo = NCIPathwayUtil.getPathwayInfo(map);
        Map map = NCIPathwayUtil.getPathwayObject(name);
        BasicDBObject pathwayInfo = NCIPathwayUtil.getPathwayInfo(map);
        BasicDBList interactionList = NCIPathwayUtil.getInteractionList(map);
        BasicDBList moleculeList = NCIPathwayUtil.getMoleculeList(map);
       // System.out.println("pathwayInfo = " + pathwayInfo);
        Node pathwayNode = null;
        setup();

        Transaction tx = graphDb.beginTx();
        try {
            if (rbidIndex == null) {
               rbidIndex = graphDb.index().forNodes(RBID);
            }
            IndexHits<Node> pNodeHits = rbidIndex.get(RBID, pathwayInfo.get((Object)SHORT_NAME));
            if (pNodeHits.size() > 0) {  
                pathwayNode = pNodeHits.getSingle(); 
                log.info("Before processing interaction list");
                processInteractionList(pathwayNode, interactionList);
                log.info("Done processing interaction list");
                processMoleculeList(pathwayNode, moleculeList);
                pNodeHits.close();
                log.info("Done processing molecule list");
            } else {
                pNodeHits.close();
    // Mutating operations go here
                pathwayNode = graphDb.createNode();
                //System.out.println("pathwayId = " + pathwayInfo.get((Object)NCI_PATHWAY_ID));
                pathwayNode.setProperty(PATHWAY_ID, pathwayInfo.get((Object)BioEntityType.NCI_ID) );
                pathwayNode.setProperty(SUBNET, pathwayInfo.get((Object)SUBNET));
                pathwayNode.setProperty(ORGANISM, pathwayInfo.get((Object)ORGANISM));
                if (organismIndex == null) {
                   organismIndex = graphDb.index().forNodes(ORGANISM);
                }
                organismIndex.add(pathwayNode, ORGANISM, pathwayInfo.get((Object)ORGANISM));
                pathwayNode.setProperty(LONG_NAME, pathwayInfo.get((Object)LONG_NAME));
                pathwayNode.setProperty(SHORT_NAME, pathwayInfo.get((Object)SHORT_NAME));
                String sourceId = (String)((Map)pathwayInfo.get((Object)SOURCE)).get(BioEntityType.NCI_ID);

                pathwayNode.setProperty(SOURCE_ID, sourceId);
                String sourceText = (String)((Map)pathwayInfo.get((Object)SOURCE)).get(NCI_SOURCE_TEXT);
                pathwayNode.setProperty(SOURCE_TEXT, sourceText);
                if ((BasicDBList)pathwayInfo.get((Object)CURATOR_LIST) != null ) {
                   String[] curatorList = (String[])(((BasicDBList)pathwayInfo.get((Object)CURATOR_LIST))).toArray(new String[0]);
                   pathwayNode.setProperty(CURATOR_LIST, curatorList);
                }
                if ((BasicDBList)pathwayInfo.get((Object)REVIEWER_LIST) != null) {
                   String[] reviewerList = ((String[])((BasicDBList)pathwayInfo.get((Object)REVIEWER_LIST)).toArray(new String[0]));
                   pathwayNode.setProperty(REVIEWER_LIST, reviewerList);
                }
                pathwayNode.setProperty(MESSAGE, pathwayInfo.get((Object)SHORT_NAME));
                pathwayNode.setProperty(RBID, pathwayInfo.get((Object)SHORT_NAME));


                rbidIndex.add(pathwayNode, RBID, pathwayInfo.get((Object)SHORT_NAME));
                processInteractionList(pathwayNode, interactionList);
                processMoleculeList(pathwayNode, moleculeList);

                //pathwayNode.setProperty("sourceId", pathwayInfo.get((Object)"source"));
                //pathwayNode.setProperty("organism", pathwayInfo.get((Object)"organism"));
                /* crapNode = graphDb.createNode();
                crapNode.setProperty("message", "speaker");
                crapNode.setProperty("pathwayId", "blast");
                relationship = pathwayNode.createRelationshipTo(crapNode, NCIPathway.RelTypes.INHIBITS);
                relationship.setProperty("message", "phosphorylation" );     */
                /* Iterable<Node> nodes = graphDb.getAllNodes();
                Iterator<Node> iter = nodes.iterator();
                while (iter.hasNext()) {
                    Node node = iter.next();
                    Iterable<Relationship> relationships = node.getRelationships();
                    Iterator<Relationship> it = relationships.iterator();
                    while (it.hasNext()) {
                        Relationship relation = it.next();
                        System.out.println(relation.toString());
                    }
                } */
            }
            tx.success();
        } catch (Exception e) {
            tx.failure();
            e.printStackTrace();
        } finally {
            log.info("Before tx.finish");
            //tx.finish();
            tx.close();
            log.info("After tx.finish");
        }
        return pathwayNode;
    }
        
}