package edu.brown.cs.futureatbrown.termproject.graph;

/**
 * The graph Edge interface represents a general Edge in a graph
 * @param <Edge> is an object that extends graph edge
 * @param <Node> is an objct that extends graph node
 */
public interface GraphEdge<Node extends GraphNode, Edge extends GraphEdge> {
  /**
   * Gets the edge's ID.
   * @return the id
   */
  String getID();

  /**
   * Gets the weight of the edge.
   * @return weight
   */
  double getWeight();

  /**
   * Sets the starting Node
   * @param startingNode the start node
   */
  void setStart(Node startingNode);

  /**
   * Retrieves the start node.
   * @return the start node
   */
  Node getStart();

  /**
   * Sets the end node.
   * @param endingNode the end node
   */
  void setEnd(Node endingNode);

  /**
   * Retrieves the end node.
   * @return the end node
   */
  Node getEnd();

  // REMEMBER TO OVERRIDE EQUALS AND HASH
  // EQUALS Based on Start and End NODE IDs
}