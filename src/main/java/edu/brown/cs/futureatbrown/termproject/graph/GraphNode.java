package edu.brown.cs.futureatbrown.termproject.graph;

import java.util.List;

/**
 * The GraphNode interface represents a general Node in a graph
 * @param <Edge> is an object that extends graph edge
 * @param <Node> is an objct that extends graph node
 */
public interface GraphNode<Node extends GraphNode, Edge extends GraphEdge> {

  /**
   * Gets the Node's ID. Must be Unique in the graph!
   * @return the id
   */
  String getID();

  /**
   * Sets the weight of the node.
   * @param weight of the node
   */
  void setWeight(double weight);

  /**
   * Gets the weight.
   * @return weight
   */
  double getWeight();

  /**
   * Sets a boolean if the node was visited.
   * @param visited
   */
  void setVisited(Boolean visited);

  /**
   * Returns a check if the vertex was visited.
   * @return visited
   */
  Boolean visited();

  /**
   * Sets the path used to reach this node for backtracking purposes
   * @param prevPath previous Edge Path
   */
  void setPreviousPath(List<Edge> prevPath);

  /**
   * Gets the path used to reach this node for backtracking purposes
   * @return the previous edge Path
   */
  List<Edge> getPreviousPath();

  // REMEMBER TO OVERRIDE EQUALS AND HASH
  // EQUALS Based on NODE ID
  // PENALTY function based on slider inputs and PreviousPath for prerequisite requirements
  // Maybe also consider penalizing based on distance to optimal size of path [Number of courses]
}
