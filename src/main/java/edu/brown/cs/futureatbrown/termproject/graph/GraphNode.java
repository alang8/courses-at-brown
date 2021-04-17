package edu.brown.cs.futureatbrown.termproject.graph;

import java.util.List;

/**
 * The GraphNode interface represents a general Node in a graph.
 *
 * @param <Edge> is an object that extends graph edge
 */
public abstract class GraphNode<Edge extends GraphEdge> {

  /**
   * Gets the Node's id. Must be unique in the graph!
   *
   * @return the id
   */
  public abstract String getID();

  /**
   * Sets the weight of the Node.
   *
   * @param weight the weight
   */
  public abstract void setWeight(double weight);

  /**
   * Gets the weight of the Node.
   *
   * @return the weight
   */
  public abstract double getWeight();

  /**
   * Sets a boolean signifying if the Node was visited.
   *
   * @param visited the boolean
   */
  public abstract void setVisited(Boolean visited);

  /**
   * Returns a boolean signifying if the Node was visited.
   *
   * @return the boolean
   */
  public abstract Boolean visited();

  /**
   * Sets the path used to reach this Node for backtracking purposes.
   *
   * @param prevPath the previous path
   */
  public abstract void setPreviousPath(List<Edge> prevPath);

  /**
   * Gets the path used to reach this Node for backtracking purposes.
   *
   * @return the previous path
   */
  public abstract List<Edge> getPreviousPath();

  /**
   * Creates a copy of the Node.
   *
   * @return the copy
   */
  public abstract GraphNode copy();

  /**
   * Force implementation to override equals.
   *
   * @param other the object to compare to
   * @return a boolean signifying whether the two objects are equal
   */
  public abstract boolean equals(Object other);

  /**
   * Force implementation to override hashCode.
   *
   * @return a hash representation of the Node
   */
  public abstract int hashCode();

  /**
   * Force implementation to override toString.
   *
   * @return a string representation of the Node
   */
  public abstract String toString();
}
