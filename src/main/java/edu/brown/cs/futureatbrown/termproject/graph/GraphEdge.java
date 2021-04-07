package edu.brown.cs.futureatbrown.termproject.graph;

/**
 * The graph Edge interface represents a general Edge in a graph.
 * @param <Edge> is an object that extends graph edge
 * @param <Node> is an object that extends graph node
 */
public interface GraphEdge<Node extends GraphNode, Edge extends GraphEdge> {

  /**
   * Gets the Edge's ID.
   * @return the id
   */
  String getID();

  /**
   * Gets the weight of the Edge.
   * @return the weight
   */
  double getWeight();

  /**
   * Sets the start Node.
   * @param startingNode the start Node
   */
  void setStart(Node startingNode);

  /**
   * Gets the start Node.
   * @return the start Node
   */
  Node getStart();

  /**
   * Sets the End node.
   * @param endingNode the End node
   */
  void setEnd(Node endingNode);

  /**
   * Gets the End node.
   * @return the End node
   */
  Node getEnd();
}