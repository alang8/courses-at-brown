package edu.brown.cs.futureatbrown.termproject.graph;

import java.util.Map;
import java.util.Set;

/**
 * The Node-based Graph interface.
 *
 * @param <Edge> is an object that extends GraphEdge
 * @param <Node> is an object that extends GraphNode
 */
public interface Graph<Node extends GraphNode, Edge extends GraphEdge> {

  /**
   * Map that contains all the Nodes.
   *
   * @return the HashMap of Nodes
   */
  Map<String, Node> getNodeSet();

  /**
   * Map that contains all the Edges from the Node with the Node id. The first String is the ID of
   * the From Node. The second String is the id of the To Node. The reason for the second hashmap is
   * just for easy editing of the edge weights for Yen's Algorithm.
   *
   * @return the HashMap of Edges
   */
  Map<String, Map<String, Edge>> getEdgeSet();

  /**
   * Adds a Node to the Node map and Edges to the Edge map.
   *
   * @param node Node to add to the Node map and id to add to Node map
   * @param edges Edges from the Node to add to the Edge map
   */
  void addNode(Node node, Set<Edge> edges);

  /**
   * Ideally creates a deep copy of the current graph.
   *
   * @return a copy of the graph
   */
  Graph<Node, Edge> copy();

  /**
   * Sets up the global parameters of the graph if needed.
   *
   * @param startID the start id
   * @param endID the end id
   */
  void setup(String startID, String endID);
}
