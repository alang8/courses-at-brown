package edu.brown.cs.futureatbrown.termproject.graph;

/**
 * The GraphEdge interface represents a general Edge in a graph.
 *
 * @param <Node> is an object that extends GraphNode
 */
public abstract class GraphEdge<Node extends GraphNode> {

  /**
   * Gets the id of the Edge.
   *
   * @return the id
   */
  public abstract String getID();

  /**
   * Gets the weight of the Edge.
   *
   * @return the weight
   */
  public abstract double getWeight();

  /**
   * Sets the weight of the edge.
   *
   * @param weight the weight
   */
  public abstract void setWeight(double weight);

  /**
   * Sets the start Node of the Edge.
   *
   * @param startingNode the start Node
   */
  public abstract void setStart(Node startingNode);

  /**
   * Gets the start Node of the Edge.
   *
   * @return the start Node
   */
  public abstract Node getStart();

  /**
   * Sets the end Node of the Edge.
   *
   * @param endingNode the end Node
   */
  public abstract void setEnd(Node endingNode);

  /**
   * Gets the end Node of the Edge.
   *
   * @return the end Node
   */
  public abstract Node getEnd();

  /**
   * Creates a copy of the Edge.
   *
   * @return a copy of the Edge
   */
  public abstract GraphEdge copy();

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
   * @return a hash representation of the Edge
   */
  public abstract int hashCode();

  /**
   * Force implementation to override toString.
   *
   * @return a string representation of the Edge
   */
  public abstract String toString();
}
