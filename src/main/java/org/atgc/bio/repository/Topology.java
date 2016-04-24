package org.atgc.bio.repository;

import org.neo4j.graphdb.Node;
import org.neo4j.graphdb.Path;
import org.neo4j.graphdb.Relationship;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * @author aceso
 */
public class Topology {
  private long aggregateScore;
  private Map<Long, Long> tuples = new HashMap<>();

  /**
   * We add a tuple to the tuples map. Everytime a new node pair is
   * found, we take the source nodeId and the dest nodeId and add this
   * pair to the tuples.
   * @param sourceId the source nodeId
   * @param destId the dest nodeId
   */
  public void add(Long sourceId, Long destId) {
    tuples.put(sourceId, destId);
  }

  /**
   * This returns the key set of all nodeIds in the current Topology.
   * @return a key set of nodeIds
   */
  public Set<Long> getNodeIds() {
    return tuples.keySet();
  }
}
