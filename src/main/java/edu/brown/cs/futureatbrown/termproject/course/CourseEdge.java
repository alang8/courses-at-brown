package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;
import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;

import java.util.Objects;
import java.util.Set;

/**
 * Specific edge implementation that links a start CourseNode to an end CourseNode.
 */
public class CourseEdge extends GraphEdge<CourseNode> {
  private final String id;
  private CourseNode start;
  private CourseNode end;
  private double weight;
  private boolean overrideWeightCalc;

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
    this.overrideWeightCalc = false;
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
    // REPLACE THIS WITH CALCULATION LATER WITH PENALTIES AND SLIDERS
    if (this.overrideWeightCalc) {
      return weight;
    } else {
      return calculateWeight();
    }
  }

  /**
   * Calculates the weight of the edge based on Penalties and Sliders
   *
   * @return weight - Calculated weight
   */
   private double calculateWeight() {
    // HAS TO SATISFY PREREQS
    Set<String[]> preReqs = this.end.getPrereqSet();
    return 0; // TODO: REPLACE THIS VALUE
   }

  /**
   * Sets the weight of this CourseEdge.
   */
  @Override
  public void setWeight(double weight) {
    this.overrideWeightCalc = true;
    this.weight = weight;
  }

  /**
   * Sets the start node of this CourseEdge.
   *
   * @param startingNode the start node
   */
  @Override
  public void setStart(CourseNode startingNode) {
    start = startingNode;
  }

  /**
   * Returns the start node of this CourseEdge.
   *
   * @return the start node
   */
  @Override
  public CourseNode getStart() {
    return start;
  }

  /**
   * Sets the end node of this CourseEdge.
   *
   * @param endingNode the end node
   */
  @Override
  public void setEnd(CourseNode endingNode) {
    end = endingNode;
  }

  /**
   * Returns the end node of this CourseEdge.
   *
   * @return the end node
   */
  @Override
  public CourseNode getEnd() {
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
