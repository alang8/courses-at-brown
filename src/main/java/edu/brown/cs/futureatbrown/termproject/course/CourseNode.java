package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;

import java.util.*;
import edu.brown.cs.futureatbrown.termproject.kdtree.Locatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Specific node implementation that contains data about a course at Brown.
 */
public class CourseNode extends GraphNode<CourseEdge> implements Locatable {
  private final String id;
  private final String name;
  private final String instr;
  private final Integer sem;
  private final String rawprereq;
  private final String prereq;
  private final String description;
  private final Double course_rating;
  private final Double prof_rating;
  private final Double avg_hours;
  private final Double max_hours;
  private final Integer class_size;
  private double weight;
  private boolean visited;
  private List<CourseEdge> prevPath;

  private final double[] coordinates;

  /**
   * Constructs a new CourseNode with the given parameters.
   *  @param id  the unique id
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
                    String description, Double course_rating, Double prof_rating, Double avg_hours,
                    Double max_hours, Integer class_size) {
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

    coordinates = new double[] {course_rating, prof_rating}; // Change this after creating scoring
  }

  /**
   * Returns the ID of this CourseNode.
   * This value should be unique to this node.
   *
   * @return the ID
   */
  @Override
  public String getID() {
    return this.id;
  }

  /**
   * Returns the name of this CourseNode.
   *
   * @return the name
   */
  public String getName() {
    return this.name;
  }

  /**
   * Returns the instructor name of this CourseNode.
   *
   * @return the instructor name
   */
  public String getInstr() {
    return this.instr;
  }

  /**
   * Returns the semester number of this CourseNode.
   *
   * @return the semester number
   */
  public int getSem() {
    return this.sem;
  }

  /**
   * Returns the name of this CourseNode's raw prerequisite name.
   *
   * @return the raw prerequisite name
   */
  public String getRawprereq() {
    return this.rawprereq;
  }

  /**
   * Returns the name of this CourseNode's prerequisite name.
   *
   * @return the prerequisite name
   */
  public String getPrereq() {
    return this.prereq;
  }

  /**
   * Returns the set of this CourseNode's prerequisite ids.
   * Each group contains the options required to fulfill that group's requirement
   *
   * @return the set of prerequisites of groups
   */
  public Set<String[]> getPrereqSet() {
    Set<String[]> answer =
      Arrays.stream(prereq.split("&"))
            .map(group -> group.replaceAll("[()]", "").split("|"))
            .collect(Collectors.toSet());
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
   * Returns the number of dimensions that the CourseNode is in.
   *
   * @return an int representing the number of dimensions this CourseNode's coordinates have
   */
  @Override
  public int getNumOfDimensions() {
    return coordinates.length;
  }

  /**
   * Returns the position of the CourseNode.
   *
   * @return an array of doubles representing this CourseNode's location
   */
  @Override
  public double[] getCoordinates() {
    return Arrays.copyOf(coordinates, coordinates.length);
  }

  /**
   * Returns the nth dimension of this CourseNode.
   *
   * @param dim which dimension to get from the locatable
   * @return the specified dimension from the locatable
   */
  @Override
  public double getCoordinate(int dim) {
    return coordinates[dim];
  }

  /**
   * Sets the weight of the CourseNode.
   *
   * @param weight the weight
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
   * Sets if the CourseNode has been visited or not.
   *
   * @param visited a boolean signifying if the CourseNode has been visited
   */
  @Override
  public void setVisited(Boolean visited) {
    this.visited = visited;
  }

  /**
   * Checks if the CourseNode has been visited yet.
   *
   * @return a boolean signifying if the CourseNode has been visited
   */
  @Override
  public Boolean visited() {
    return visited;
  }

  /**
   * Sets the previous path of the CourseNode.
   *
   * @param prevPath the path
   */
  @Override
  public void setPreviousPath(List prevPath) {
    this.prevPath = prevPath;
  }

  /**
   * Returns the previous path of the CourseNode.
   *
   * @return the path
   */
  @Override
  public List<CourseEdge> getPreviousPath() {
    return prevPath;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CourseNode that = (CourseNode) o;

    // WHEN NODES ARE NOT EQUAL IDENTIFY THE OFFENDING COMPONENT
    if (!id.equals(that.id)) {
      System.out.println("ID: Expected " + id + ", GOT " + that.id);
    }
    if (!Objects.equals(name, that.name)) {
      System.out.println("NAME: Expected " + name + ", GOT " + that.name);
    }
    if (!Objects.equals(instr, that.instr)) {
      System.out.println("INSTRUCTOR: Expected " + instr + ", GOT " + that.instr);
    }
    if (!sem.equals(that.sem)) {
      System.out.println("SEMESTER: Expected " + sem + ", GOT " + that.sem);
    }
    if (!(Double.compare(that.course_rating, course_rating) == 0)) {
      System.out.println("COURSE RATING: Expected " + course_rating + ", GOT " + that.course_rating);
    }
    if (!(Double.compare(that.prof_rating, prof_rating) == 0)) {
      System.out.println("PROF RATING: Expected " + prof_rating + ", GOT " + that.prof_rating);
    }
    if (!(Double.compare(that.avg_hours, avg_hours) == 0)) {
      System.out.println("AVG HOURS: Expected " + avg_hours + ", GOT " + that.avg_hours);
    }
    if (!(Double.compare(that.max_hours, max_hours) == 0)) {
      System.out.println("MAX HOURS: Expected " + max_hours + ", GOT " + that.max_hours);
    }
    if (!class_size.equals(that.class_size)) {
      System.out.println("CLASS SIZE: Expected " + class_size + ", GOT " + that.class_size);
    }
    if (!(Double.compare(that.weight, weight) == 0)){
      System.out.println("WEIGHT: EXPECTED " + weight + ", GOT " + that.weight);
    }
    if (visited != that.visited) {
      System.out.println("VISITED: Expected " + visited + ", GOT " + that.visited);
    }
    if (!Objects.equals(prevPath, that.prevPath)) {
      System.out.println("PREVIOUS PATH: Expected " + prevPath + ", GOT " + that.prevPath);
    }
    if (!Arrays.equals(coordinates, that.coordinates)) {
      System.out.println("COORDINATES: Expected " + coordinates + ", GOT " + that.coordinates);
    }
    return sem.equals(that.sem) && Double.compare(that.course_rating, course_rating) == 0 && Double.compare(that.prof_rating, prof_rating) == 0 && Double.compare(that.avg_hours, avg_hours) == 0 && Double.compare(that.max_hours, max_hours) == 0 && class_size.equals(that.class_size) && Double.compare(that.weight, weight) == 0 && visited == that.visited && id.equals(that.id) && Objects.equals(name, that.name) && Objects.equals(instr, that.instr) && Objects.equals(prereq, that.prereq) && Objects.equals(prevPath, that.prevPath) && Arrays.equals(coordinates, that.coordinates);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  /**
   * Returns a string representing a node.
   *
   * @return A string representing a node.
   */
  @Override
  public String toString() {
    return "NODE (" + id + ": " + name + ")";
  }

  /**
   * Returns a copy of the node
   * @return A copy of the node
   */
  @Override
  public GraphNode copy() {
    return new CourseNode(this.id, this.name, this.instr, this.sem, this.rawprereq, this.prereq,
                          this.description, this.course_rating, this.prof_rating, this.avg_hours,
                          this.max_hours, this.class_size);
  }
}
