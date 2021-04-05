package edu.brown.cs.student.finproject.graph;

import java.util.HashMap;
import java.util.Set;

/**
 * The node based graph interface
 * @param <Edge> is an object that extends graph edge
 * @param <Node> is an objct that extends graph node
 */
public interface Graph<Node extends GraphNode, Edge extends GraphEdge> {
  /**
   * Map that contains all the nodes
   *
   * @return the HashMap of Node sets
   */
  HashMap<String, Node> getNodeSet();

  /**
   * Map that contains all the Edges from the Node with the NodeID
   * The first String is the ID of the From Node
   * The second String is the ID of the To Node
   * The reason for the second hashmap is just for easy editing of the edge weights
   * for Yen's Algorithm
   * @return the HashMap of Egde sets
   */
  HashMap<String, HashMap<String, Edge>> getEdgeSet();

  /**
   * Adds a Node to the Node set and Edges to the Edge Set
   * @param node node to add to the Node set and add ID to Node Set
   */
  void addNode(Node node, Set<Edge> edges);

  /**
   * Ideally creates a deep copy of the current graph
   */
  Graph<Node, Edge> copy();
}
