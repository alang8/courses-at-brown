package edu.brown.cs.termProject.graph;

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
  String getId();

  /**
   * Gets the weight of the edge.
   * @return weight
   */
  double getWeight();

  /**
   * Sets the starting Node
   * @param start
   */
  void setStart(Node startingNode);

  /**
   * returns the starting node.
   * @return start
   */
  Node getStart();

  /**
   * Sets the end node.
   * @param the end node
   */
  void setEnd(Node endingNode);

  /**
   * retrieves the end node.
   * @return the end node
   */
  Node getEnd();

  //REMEMBER TO OVERRIDE EQUALS AND HASH
}
