package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;
import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;

import java.util.Objects;

/**
 * Specific edge implementation that links a start CourseNode to an end CourseNode.
 */
public class CourseEdge extends GraphEdge<GraphNode<?>> {
  private final String id;
  private GraphNode<?> start;
  private GraphNode<?> end;
  private double weight;

  /**
   * Constructs a CourseEdge with the given id, start id, and end id.
   *
   * @param start the id of this CourseEdge's start node
   * @param end the id of this CourseEdge's end node
   */
  public CourseEdge(String id, CourseNode start, CourseNode end) {
    this.id = id;
    this.start = start;
    this.end = end;
    this.weight = 0; // Change this after creating weight formula
  }

  /**
   * Returns the id of this CourseEdge.
   * This value should be unique to this edge.
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
   * Sets the weight of this CourseEdge.
   */
  @Override
  public void setWeight(double weight) {
    // REPLACE THIS WITH CALCULATION LATER WITH PENALTIES AND SLIDERS
    this.weight = weight;
  }

  /**
   * Sets the start node of this CourseEdge.
   *
   * @param startingNode the start node
   */
  @Override
  public void setStart(GraphNode<?> startingNode) {
    start = startingNode;
  }

  /**
   * Returns the start node of this CourseEdge.
   *
   * @return the start node
   */
  @Override
  public GraphNode<?> getStart() {
    return start;
  }

  /**
   * Sets the end node of this CourseEdge.
   *
   * @param endingNode the end node
   */
  @Override
  public void setEnd(GraphNode<?> endingNode) {
    end = endingNode;
  }

  /**
   * Returns the end node of this CourseEdge.
   *
   * @return the end node
   */
  @Override
  public GraphNode<?> getEnd() {
    return end;
  }

  /**
   * Returns a copy of the edge
   * @return a copy of the edge
   */
  @Override
  public GraphEdge<?> copy() {
    return new CourseEdge(this.id, (CourseNode) this.start.copy(), (CourseNode) this.end.copy());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CourseEdge that = (CourseEdge) o;
    return Objects.equals(id, that.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  /**
   * Creates a String representation for this CourseEdge.
   *
   * @return a String representing this CourseEdge
   */
  @Override
  public String toString() {
    return "EDGE: (" + id + ": " + start + "to " + end + ")";
  }
}
