package edu.brown.cs.termProject.graph;

/**
 * The GraphNode interface represents a general Node in a graph
 * @param <Edge> is an object that extends graph edge
 */
public abstract class GraphNode<Edge extends GraphEdge> {

  /**
   * Gets the Node's ID. Must be Unique in the graph!
   * @return the id
   */
  public abstract String getID();

  /**
   * Sets the weight of the node.
   * @param weight of the node
   */
  public abstract void setWeight(double weight);

  /**
   * Gets the weight.
   * @return weight
   */
  public abstract double getWeight();

  /**
   * Sets a boolean if the node was visited.
   * @param visited
   */
  public abstract void setVisited(Boolean visited);

  /**
   * Returns a check if the vertex was visited.
   * @return visited
   */
  public abstract Boolean visited();

  /**
   * Sets the path used to reach this node for backtracking purposes
   * @param previous Edge Path
   */
  public abstract void setPreviousPath(List<Edge> prevPath);

  /**
   * Gets the path used to reach this node for backtracking purposes
   * @return the previous edge Path
   */
<<<<<<< Updated upstream:src/main/java/edu/brown/cs/termProject/graph/GraphNode.java
  Edge getPreviousPath();
=======
  public abstract List<Edge> getPreviousPath();
>>>>>>> Stashed changes:src/main/java/edu/brown/cs/futureatbrown/termproject/graph/GraphNode.java

  /**
   * Creates a copy of the node
   * @return a copy of the node
   */
  public abstract GraphNode copy();

  /**
   * Force implementation to override equals
   */
  public abstract boolean equals(Object other);

  /**
   * Force implementation to override Hash
   */
  public abstract int hashCode();

  /**
   * Force implementation to override toString
   */
  public abstract String toString();
  // REMEMBER TO OVERRIDE EQUALS AND HASH
  // EQUALS Based on NODE ID
  // PENALTY function based on slider inputs and PreviousPath for prerequisite requirements
  // Maybe also consider penalizing based on distance to optimal size of path [Number of courses]

}