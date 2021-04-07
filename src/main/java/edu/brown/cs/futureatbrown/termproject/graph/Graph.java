package edu.brown.cs.futureatbrown.termproject.graph;

import java.util.HashMap;
import java.util.Set;

/**
 * The node based graph interface.
 * @param <Edge> is an object that extends graph edge
 * @param <Node> is an object that extends graph node
 */
public interface Graph<Node extends GraphNode, Edge extends GraphEdge> {

  /**
   * Map that contains all the Nodes.
   *
   * @return the HashMap of Nodes
   */
  HashMap<String, Node> getNodeMap();

  /**
   * Map that contains all the Edges from the Node with the NodeID. The first String is the ID of
   * the from Node. The second String is the ID of the to Node. The reason for the second hashmap is
   * just for easy editing of the edge weights for Yen's Algorithm.
   * @return the HashMap of Edges
   */
  HashMap<String, HashMap<String, Edge>> getEdgeMap();

  /**
   * Adds a Node to the Node map and Edges with the Node as their start to the Edge map.
   * @param node Node to add to the map
   * @param edges a Set of edges to check
   */
  void addNode(Node node, Set<Edge> edges);

  /**
   * Ideally creates a deep copy of the current Graph.
   */
  Graph<Node, Edge> copy();
}
