package org.atgc.bio.repository;

import org.atgc.bio.domain.BioTypes;
import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Relationship;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Each node in the knowledge corresponds to a BioEntityVector
 * instance.
 *
 * @author aceso
 */
public class BioEntityFeatureVector {

  public static final double DECAY = 0.9;

  /**
   * Every bioEntity has a graph nodeId that is unique.
   */
  private long nodeId;

  /**
   * Instead of changing a single scalar score, we use a List that
   * tracks the evolution of scores in each knowledge iteration.
   * The first element in this list, is the initial score. The
   * second element is the score in the 2nd iteration and so on.
   * So as the knowledge grows, the intensity of hotspots evolve,
   * and the intensity is not necessarily monotonic. It may increase
   * or decrease, or in other words fluctuate.
   */
  private List<Double> scoreEvolution;

  /**
   * This is the score computed during a pass, and updated into the
   * latest score in scoreEvolution. This prevents recursive feedback
   * of scores for neighbors.
   */
  private double interimScore;

  /**
   * The bioEntity type tells us if this is a Protein, Gene,
   * Enzyme, Drug, Disease, etc.
   */
  private BioTypes bioEntityType;

  /**
   * Get the BioTypes bioType for this feature vector.
   * @return the BioTypes biotype
   */
  public BioTypes getBioEntityType() {
    return bioEntityType;
  }

  /**
   * Set the BioTypes bioType for this feature vector.
   * @param bioEntityType the BioTypes biotype
   */
  public void setBioEntityType(BioTypes bioEntityType) {
    this.bioEntityType = bioEntityType;
  }

  /**
   * Get the score evolution list. The latest score is the last
   * score in the list for the most recent knowledge iteration.
   * @return the score evolution list.
   */
  public List<Double> getScoreEvolution() {
    return scoreEvolution;
  }

  /**
   * Set the initial score evolution. This method may not be used.
   * @param scoreEvolution the evolving score list
   */
  public void setScoreEvolution(List<Double> scoreEvolution) {
    this.scoreEvolution = scoreEvolution;
  }

  /**
   * Get the nodeId from this feature vector. Each vector has
   * a unique nodeId.
   * @return the nodeId which is unique
   */
  public long getNodeId() {
    return nodeId;
  }

  /**
   * Set the nodeId for this feature vector
   * @param nodeId the nodeId from graph
   */
  public void setNodeId(long nodeId) {
    this.nodeId = nodeId;
  }

  /**
   * We populate each vector with the information about the Node.
   * We will need to do some normalization of properties.
   * @param node the node from graph
   */
  public void populate(Node node) {
    setNodeId(node.getId());
    scoreEvolution = new ArrayList<>();
    scoreEvolution.add((double) node.getDegree());
    // some more things
  }

  /**
   * We do depth=3 relationship cardinality traversing.
   * @param nodeMap the map of all nodes
   */
  public void computeRelationsScore(Map<Long, Node> nodeMap) {
    Node node = nodeMap.get(nodeId);
    interimScore = computeRelationsScore(node, DECAY);
  }

  public double computeRelationsScore(Node node, double decay) {
    double score = 0;
    for (Relationship relationship : node.getRelationships()) {
      Node endNode = relationship.getEndNode();
      score += endNode.getDegree()*decay;
      score += computeRelationsScore(endNode, decay*decay);
    }
    return score;
  }

  /**
   * Update the relations score by looking up the latest score
   * that has evolved and adding the interimScore to it.
   * Why do we compute the interimScore? This is to avoid neighbour
   * node recursive feedback. For instance node A is connected to
   * node B, so node A's score increases by node B's score. Similarly node B is
   * connected to node A, so node B's score increases by node A's score.
   * So we will end up cumulating node B's score by node A's score that
   * also includes node B's score. Hence, we create two separate phases.
   * The first phase is to compute the interimScore based on the
   * degree of neighbor nodes. This is done for every node. The second
   * phase is to increase each node's scoreEvolution score by the interimScore.
   *
   */
  public void updateRelationsScore() {
    Double score = scoreEvolution.get(scoreEvolution.size()-1);
    scoreEvolution.set(scoreEvolution.size()-1, score+interimScore);
  }
}
