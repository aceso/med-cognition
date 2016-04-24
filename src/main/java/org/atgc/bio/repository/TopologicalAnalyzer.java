package org.atgc.bio.repository;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author aceso
 */
public class TopologicalAnalyzer {

  protected static final Logger log = LogManager.getLogger(TopologicalAnalyzer.class);

  /**
   * This is a map of nodeId which is a long and a feature vector.
   * Each feature vector corresponds to a unique node.
   */
  private Map<Long, BioEntityFeatureVector> knowledgeCache = new HashMap<>();

  /**
   * Keep track of the evolution of topology. Each knowledge iteration
   * should generate one new topology. The latest knowledge iteration
   * is the last topology in the list.
   */
  List<Topology> topologyEvolution = new ArrayList<>();

  /**
   * We map a nodeId to a Node as found in the graph. This helps us
   * find Node instances easily by using a lightweight numeric key.
   */
  Map<Long, Node> nodeMap = new HashMap<>();

  /**
   * We fetch or create a new feature vector for a given node.
   * @param node the node to be processed
   * @return a bio entity based feature vector
   */
  private BioEntityFeatureVector getVector(Node node) {
    Long nodeId = node.getId();
    BioEntityFeatureVector vector = knowledgeCache.get(nodeId);
    if (vector == null) {
      vector = new BioEntityFeatureVector();
      vector.populate(node);
      nodeMap.put(nodeId, node);
    }
    return vector;
  }

  /**
   * The first pass topology creates all the necessary feature
   * vectors for all bioentities. The knowledge cache is also updated,
   * and the feature vectors are populated.
   * @param topology the topology to be updated
   * @param path the path to be processed
   */
  private void firstPassTopology(Topology topology, Path path) {
    for (Node node : path.nodes()) {
      Long nodeId = node.getId();
      BioEntityFeatureVector vector = getVector(node);
      knowledgeCache.put(nodeId, vector);
    }
  }

  /**
   * Once we set initial scores, we do a second pass topology that
   * handles the relationship scores.
   * @param topology the topology to be updated
   */
  private void secondPassTopology(Topology topology) {
    for (Long nodeId : topology.getNodeIds()) {
      BioEntityFeatureVector vector = knowledgeCache.get(nodeId);
      vector.computeRelationsScore(nodeMap);
    }
    for (Long nodeId : topology.getNodeIds()) {
      BioEntityFeatureVector vector = knowledgeCache.get(nodeId);
      vector.updateRelationsScore();
    }
  }

  private void finalPassTopology(Topology topology, double networkDensity) {
    for (Long nodeId : topology.getNodeIds()) {
      BioEntityFeatureVector vector = knowledgeCache.get(nodeId);
      vector.normalizeScore(networkDensity);
    }
  }

  private int countRelationships(Path path) {
    int count = 0;
    for (Relationship relationship : path.relationships()) {
      count++;
    }
    return count;
  }

  private int countNodes(Path path) {
    int count = 0;
    for (Node node : path.nodes()) {
      count++;
    }
    return count;
  }

  /**
   * For each iteration of finding new knowledge, we create
   * a new topology. This is based on the new paths found in the iteration.
   * @param paths a list of paths found in a knowledge iteration
   */
  public void createTopology(List<Path> paths) {
    Topology topology = new Topology();
    double networkDensity = 0;
    for (Path path : paths) {
      firstPassTopology(topology, path);
      secondPassTopology(topology);
      int numNodes = countNodes(path);
      int numRelations = countRelationships(path);
      int maxRelations = numNodes*(numNodes-1)/2;
      networkDensity += (double)numRelations/maxRelations;
      log.info("networkDensity = " + networkDensity);
    }
    networkDensity = networkDensity/paths.size();
    finalPassTopology(topology, networkDensity);
    topologyEvolution.add(topology);
  }
}
