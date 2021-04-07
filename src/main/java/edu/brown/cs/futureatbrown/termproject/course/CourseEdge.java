package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;
import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;

/**
 * Specific edge implementation that links a start CourseNode to an end CourseNode.
 */
public class CourseEdge implements GraphEdge<GraphNode, GraphEdge> {
  private final String id; // Should the ID be unique even for edges with the same start and end?
  private GraphNode start;
  private GraphNode end;
  private double weight;

  /**
   * Constructs a CourseEdge with the given id, start id, and end id.
   *
   * @param start the id of this CourseEdge's start node
   * @param end the id of this CourseEdge's end node
   */
  public CourseEdge(String id, GraphNode start, GraphNode end) {
    this.id = id;
    this.start = start;
    this.end = end;
    this.weight = 0; // Change this after creating weight formula
  }

  /**
   * Returns the id of this CourseEdge.
   *
   * @return the id
   */
  @Override
  public String getID() {
    return id;
  }

  /**
   * Returns the weight of this CourseEdge.
   *
   * @return the weight
   */
  @Override
  public double getWeight() {
    return weight;
  }

  /**
   * Sets the start node of this CourseEdge.
   *
   * @param startingNode the start node
   */
  @Override
  public void setStart(GraphNode startingNode) {
    start = startingNode;
  }

  /**
   * Returns the start node of this CourseEdge.
   *
   * @return the start node
   */
  @Override
  public GraphNode getStart() {
    return start;
  }

  /**
   * Sets the end node of this CourseEdge.
   *
   * @param endingNode the end node
   */
  @Override
  public void setEnd(GraphNode endingNode) {
    end = endingNode;
  }

  /**
   * Returns the end node of this CourseEdge.
   *
   * @return the end node
   */
  @Override
  public GraphNode getEnd() {
    return end;
  }

  /**
   * Compares this CourseEdge to another object for equality.
   * <p>
   * Another object is equal to this node if its start and end CourseNodes are the same.
   *
   * @param other another object
   * @return whether the other object is equal to this CourseEdge
   */
  @Override
  public boolean equals(Object other) {
    return other instanceof CourseEdge && start.equals(((CourseEdge) other).start) &&
        end.equals(((CourseEdge) other).end);
  }

  /**
   * Returns a unique code denoting this CourseEdge.
   *
   * @return a hash code for this CourseEdge
   */
  @Override
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Creates a String representation for this CourseEdge.
   *
   * @return a String representing this CourseEdge
   */
  @Override
  public String toString() {
    return id + ": " + start + "-> " + end;
  }
}
