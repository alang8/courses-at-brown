package edu.brown.cs.termProject.graph;

/**
 * The graph Edge interface represents a general Edge in a graph
 * @param <Node> is an objct that extends graph node
 */
public abstract class GraphEdge<Node extends GraphNode> {
  /**
   * Gets the edge's ID.
   * @return the id
   */
<<<<<<< Updated upstream:src/main/java/edu/brown/cs/termProject/graph/GraphEdge.java
  String getId();
=======
  public abstract String getID();
>>>>>>> Stashed changes:src/main/java/edu/brown/cs/futureatbrown/termproject/graph/GraphEdge.java

  /**
   * Gets the weight of the edge.
   * @return weight
   */
  public abstract double getWeight();

  /**
   * Sets the weight of the edge.
   */
  public abstract void setWeight(double weight);

  /**
   * Sets the starting Node
   * @param start
   */
  public abstract void setStart(Node startingNode);

  /**
   * returns the starting node.
   * @return start
   */
  public abstract Node getStart();

  /**
   * Sets the end node.
   * @param the end node
   */
  public abstract void setEnd(Node endingNode);

  /**
   * retrieves the end node.
   * @return the end node
   */
  public abstract Node getEnd();

  /**
   * Creates a copy of the edge
   * @return a copy of the edge
   */
  public abstract GraphEdge copy();

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

  //REMEMBER TO OVERRIDE EQUALS AND HASH
}
