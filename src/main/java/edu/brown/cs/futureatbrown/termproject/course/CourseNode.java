package edu.brown.cs.student.finproject.course;

import edu.brown.cs.student.finproject.graph.GraphEdge;
import edu.brown.cs.student.finproject.graph.GraphNode;
import edu.brown.cs.student.finproject.kdtree.Locatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Specific node implementation that contains data about a course at Brown.
 */
public class CourseNode implements Locatable, GraphNode<GraphNode, GraphEdge> {
  private final String id;
  private final String name;
  private final String instr;
  private final int sem;
  private final String rawprereq;
  private final String prereq;
  private final String description;
  private final double course_rating;
  private final double prof_rating;
  private final double avg_hours;
  private final double max_hours;
  private final int class_size;
  private double weight;
  private boolean visited;
  private List<GraphEdge> prevPath;

  private final double[] coordinates;

  /**
   * Constructs a new CourseNode with the given parameters.
   *
   * @param id  the unique id
   * @param name the name
   * @param instr the instructor name
   * @param sem the semester number
   * @param rawprereq the raw prerequisite
   * @param prereq the prerequisite
   * @param description the description
   * @param course_rating the course rating
   * @param prof_rating the professor rating
   * @param avg_hours the average hours
   * @param max_hours the max hours
   * @param class_size the class size
   */
  public CourseNode(String id, String name, String instr, int sem, String rawprereq, String prereq,
                    String description, double course_rating, double prof_rating, double avg_hours,
                    double max_hours, int class_size) {
    this.id = id;
    this.name = name;
    this.instr = instr;
    this.sem = sem;
    this.rawprereq = rawprereq;
    this.prereq = prereq;
    this.description = description;
    this.course_rating = course_rating;
    this.prof_rating = prof_rating;
    this.avg_hours = avg_hours;
    this.max_hours = max_hours;
    this.class_size = class_size;
    this.weight = 0; // Change this after creating weight formula
    this.visited = false;
    this.prevPath = new ArrayList<>();

    coordinates = new double[] {course_rating, prof_rating};
  }

  /**
   * Returns the name of this CourseNode.
   *
   * @return the name
   */
  public String getName() {
    return name;
  }

  /**
   * Returns the instructor name of this CourseNode.
   *
   * @return the instructor name
   */
  public String getInstr() {
    return instr;
  }

  /**
   * Returns the semester number of this CourseNode.
   *
   * @return the semester number
   */
  public int getSem() {
    return sem;
  }

  /**
   * Returns the name of this CourseNode's raw prerequisite name.
   *
   * @return the raw prerequisite name
   */
  public String getRawprereq() {
    return rawprereq;
  }

  /**
   * Returns the name of this CourseNode's prerequisite name.
   *
   * @return the prerequisite name
   */
  public String getPrereq() {
    return prereq;
  }

  /**
   * Returns the set of this CourseNode's prerequisite ids.
   *
   * @return the set of prerequisites
   */
  public Set<String> getPrereqSet() {
    Set<String> answer = new HashSet<>();
    Collections.addAll(answer, prereq.split("|"));
    return answer;
  }

  /**
   * Returns the description of this CourseNode.
   *
   * @return the description
   */
  public String getDescription() {
    return description;
  }

  /**
   * Returns the course rating of this CourseNode.
   *
   * @return the course rating
   */
  public double getCourse_rating() {
    return course_rating;
  }

  /**
   * Returns the professor rating of this CourseNode.
   *
   * @return the professor rating
   */
  public double getProf_rating() {
    return prof_rating;
  }

  /**
   * Returns the average hours of this CourseNode.
   *
   * @return the average hours
   */
  public double getAvg_hours() {
    return avg_hours;
  }

  /**
   * Returns the max hours of this CourseNode.
   *
   * @return the max hours
   */
  public double getMax_hours() {
    return max_hours;
  }

  /**
   * Returns the class size of this CourseNode.
   *
   * @return the class size
   */
  public double getClass_size() {
    return class_size;
  }

  /**
   * Returns the ID of this CourseNode.
   * This value should be unique to this node.
   *
   * @return the ID
   */
  @Override
  public String getID() {
    return null;
  }

  /**
   * Returns the number of dimensions that the CourseNode is in.
   *
   * @return Return an int representing the number of dimensions this CourseNode's coordinates have.
   */
  @Override
  public int getNumOfDimensions() {
    return coordinates.length;
  }

  /**
   * Returns the position of the CourseNode.
   *
   * @return An array of doubles representing this CourseNode's location.
   */
  @Override
  public double[] getCoordinates() {
    return Arrays.copyOf(coordinates, coordinates.length);
  }

  /**
   * Returns the nth dimension of this CourseNode.
   *
   * @param dim Which dimension to get from the locatable.
   * @return The specified dimension from the locatable.
   */
  @Override
  public double getCoordinate(int dim) {
    return coordinates[dim];
  }

  /**
   * Compares this CourseNode to another object for equality.
   * Another object is equal to this node if it is also a CourseNode and shares the same id.
   *
   * @param other another object
   * @return whether the other object is equal to this CourseNode
   */
  @Override
  public boolean equals(Object other) {
    return other instanceof CourseNode && id.equals(((CourseNode) other).id);
  }

  /**
   * Returns a unique hash for this Node.
   *
   * @return a unique hash code
   */
  @Override
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Returns a string representing a node.
   *
   * @return A string representing a node.
   */
  @Override
  public String toString() {
    return id + ": " + name;
  }

  /**
   * Sets the weight of the node.
   *
   * @param weight The weight.
   */
  @Override
  public void setWeight(double weight) {
    this.weight = weight;
  }

  /**
   * Returns the weight of the node.
   *
   * @return The weight.
   */
  @Override
  public double getWeight() {
    return weight;
  }

  /**
   * Sets if the node has been visited or not.
   *
   * @param visited A boolean signifying if node has been visited.
   */
  @Override
  public void setVisited(Boolean visited) {
    this.visited = visited;
  }

  /**
   * Checks if node has been visited yet.
   *
   * @return A boolean signifying if node has been visited.
   */
  @Override
  public Boolean visited() {
    return visited;
  }

  /**
   * Sets the previous path of the node.
   *
   * @param prevPath The path.
   */
  @Override
  public void setPreviousPath(List prevPath) {
    this.prevPath = prevPath;
  }

  /**
   * Returns the previous path of the node.
   *
   * @return The path.
   */
  @Override
  public List<GraphEdge> getPreviousPath() {
    return prevPath;
  }
}
