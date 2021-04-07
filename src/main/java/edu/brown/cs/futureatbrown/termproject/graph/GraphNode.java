package edu.brown.cs.futureatbrown.termproject.graph;

import java.util.List;

/**
 * The GraphNode interface represents a general Node in a graph
 * @param <Edge> is an object that extends graph edge
 * @param <Node> is an object that extends graph node
 */
public interface GraphNode<Node extends GraphNode, Edge extends GraphEdge> {

  /**
   * Gets the Node's ID. Must be Unique in the graph!
   * @return the id
   */
  String getID();

  /**
   * Sets the weight of the Node.
   * @param weight the weight
   */
  void setWeight(double weight);

  /**
   * Gets the weight of the Node.
   * @return the weight
   */
  double getWeight();

  /**
   * Sets a boolean signifying if the Node was visited.
   * @param visited the boolean
   */
  void setVisited(Boolean visited);

  /**
   * Returns a boolean signifying if the Node was visited.
   * @return the boolean
   */
  Boolean visited();

  /**
   * Sets the path used to reach this Node for backtracking purposes.
   * @param prevPath the previous Edge path
   */
  void setPreviousPath(List<Edge> prevPath);

  /**
   * Gets the path used to reach this Node for backtracking purposes.
   * @return the previous Edge path
   */
  List<Edge> getPreviousPath();

  // PENALTY function based on slider inputs and PreviousPath for prerequisite requirements
  // Maybe also consider penalizing based on distance to optimal size of path [Number of courses]
}
