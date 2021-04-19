package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;

import edu.brown.cs.futureatbrown.termproject.kdtree.Locatable;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
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
  private final Double courseRating;
  private final Double profRating;
  private final Double avgHours;
  private final Double maxHours;
  private final Integer classSize;
  private double weight;
  private boolean visited;
  private List<CourseEdge> prevPath;
  private Double prevTotalAvgHours;
  private Double prevTotalMaxHours;
  private final double[] coordinates;
  private int currentGroup = 1;
  private int currentNumInGroup = 0;
  private List<CourseNode> previousCourses = new ArrayList<>();
  private List<List<String>> coursesTakenAccountedFor = new ArrayList<>();

  /**
   * Constructs a new CourseNode with the given parameters.
   * @param id  the unique id
   * @param name the name
   * @param instr the instructor name
   * @param sem the semester number
   * @param rawprereq the raw prerequisite
   * @param prereq the prerequisite
   * @param description the description
   * @param courseRating the course rating
   * @param profRating the professor rating
   * @param avgHours the average hours
   * @param maxHours the max hours
   * @param classSize the class size
   */
  public CourseNode(String id, String name, String instr, int sem, String rawprereq, String prereq,
                    String description, Double courseRating, Double profRating, Double avgHours,
                    Double maxHours, Integer classSize) {
    this.id = id;
    this.name = name;
    this.instr = instr;
    this.sem = sem;
    this.rawprereq = rawprereq;
    this.prereq = prereq;
    this.description = description;
    this.courseRating = courseRating;
    this.profRating = profRating;
    this.avgHours = avgHours;
    this.maxHours = maxHours;
    this.classSize = classSize;
    this.weight = 0; // Change this after creating weight formula
    this.visited = false;
    this.prevPath = new ArrayList<>();
    if (null != courseRating && null != profRating) {
      this.coordinates = new double[] {courseRating, profRating};
      // Change this after creating scoring
    } else {
      this.coordinates = new double[0];
    }
    this.prevTotalAvgHours = 0.0;
    this.prevTotalMaxHours = 0.0;
  }

  /**
   * Resets the hours to 0.0 and current group to 1 at the beginning of every Dijkstra call.
   */
  public void resetHoursAndGroups() {
    this.prevTotalAvgHours = 0.0;
    this.prevTotalMaxHours = 0.0;
    this.currentGroup = 1;
    this.currentNumInGroup = 0;
  }

  /**
   * Returns the current group of this CourseNode. This value should be unique to this node.
   *
   * @return the current group
   */
  public int getCurrentGroup() {
    return currentGroup;
  }

  /**
   * Sets the current group of this CourseNode. This value should be unique to this node.
   *
   * @param currentGroup the current group
   */
  public void setCurrentGroup(int currentGroup) {
    this.currentGroup = currentGroup;
  }

  /**
   * Returns the number of courses in the current group of this CourseNode.
   *
   * @return the current number
   */
  public int getCurrentNumInGroup() {
    return currentNumInGroup;
  }

  /**
   * Returns the number of courses in the current group of this CourseNode.
   *
   * @param currentNumInGroup the number of courses
   */
  public void setCurrentNumInGroup(int currentNumInGroup) {
    this.currentNumInGroup = currentNumInGroup;
  }

  /**
   * Returns the id of this CourseNode. This value should be unique to this node.
   *
   * @return the id
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
  public Integer getSem() {
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
   * <p>
   * Each group contains the options required to fulfill that group's requirement.
   *
   * @return the set of prerequisites of groups
   */
  public Set<List<String>> getPrereqSet() {
    if (null == prereq) {
      return new HashSet<>();
    }
    return Arrays.stream(prereq.split("&"))
        .map(group -> List.of(group.replaceAll("[()]", "").split("[|]")))
        .collect(Collectors.toSet());
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
  public Double getCourseRating() {
    return courseRating;
  }

  /**
   * Returns the professor rating of this CourseNode.
   *
   * @return the professor rating
   */
  public Double getProfRating() {
    return profRating;
  }

  /**
   * Returns the average hours of this CourseNode.
   *
   * @return the average hours
   */
  public Double getAvgHours() {
    return avgHours;
  }

  /**
   * Returns the max hours of this CourseNode.
   *
   * @return the max hours
   */
  public Double getMaxHours() {
    return maxHours;
  }

  /**
   * Returns the class size of this CourseNode.
   *
   * @return the class size
   */
  public Integer getClassSize() {
    return classSize;
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
   * Returns the weight of the CourseNode.
   *
   * @return the weight
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
  public void setPreviousPath(List<CourseEdge> prevPath) {
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

  /**
   * Returns the previous total average hours of the CourseNode.
   *
   * @return the previous total average hours
   */
  public Double getPrevTotalAvgHours() {
    return prevTotalAvgHours;
  }

  /**
   * Sets the previous total average hours of the CourseNode.
   *
   * @param prevTotalAvgHours the previous total average hours
   */
  public void setPrevTotalAvgHours(Double prevTotalAvgHours) {
    this.prevTotalAvgHours = prevTotalAvgHours;
  }

  /**
   * Returns the previous total max hours of the CourseNode.
   *
   * @return the previous total max hours
   */
  public Double getPrevTotalMaxHours() {
    return prevTotalMaxHours;
  }

  /**
   * Sets the previous total max hours of the CourseNode.
   *
   * @param prevTotalMaxHours the previous total max hours
   */
  public void setPrevTotalMaxHours(Double prevTotalMaxHours) {
    this.prevTotalMaxHours = prevTotalMaxHours;
  }

  /**
   * Returns the previous courses of the CourseNode.
   *
   * @return the previous courses
   */
  public List<CourseNode> getPreviousCourses() {
    return previousCourses;
  }

  /**
   * Sets the previous courses of the CourseNode.
   *
   * @param previousCourses the previous courses
   */
  public void setPreviousCourses(List<CourseNode> previousCourses) {
    this.previousCourses = previousCourses;
  }

  /**
   * Returns the taken courses of the CourseNode that were accounted for.
   *
   * @return the taken courses that were accounted for
   */
  public List<List<String>> getCoursesTakenAccountedFor() {
    return coursesTakenAccountedFor;
  }

  /**
   * Sets the taken courses of the CourseNode that were accounted for.
   *
   * @param coursesTakenAccountedFor the taken courses that were accounted for
   */
  public void setCoursesTakenAccountedFor(List<List<String>> coursesTakenAccountedFor) {
    this.coursesTakenAccountedFor = coursesTakenAccountedFor;
  }

  /**
   * Checks if this CourseNode is equal to another object.
   *
   * @return a boolean signifying if the objects are equal
   */
  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    CourseNode that = (CourseNode) o;
//  WHEN NODES ARE NOT EQUAL IDENTIFY THE OFFENDING COMPONENT
//    if (!Objects.equals(id, that.id)) {
//      System.out.println("ID: Expected " + id + ", GOT " + that.id);
//    }
//    if (!Objects.equals(name, that.name)) {
//      System.out.println("NAME: Expected " + name + ", GOT " + that.name);
//    }
//    if (!Objects.equals(instr, that.instr)) {
//      System.out.println("INSTRUCTOR: Expected " + instr + ", GOT " + that.instr);
//    }
//    if (!Objects.equals(sem, that.sem)) {
//      System.out.println("SEMESTER: Expected " + sem + ", GOT " + that.sem);
//    }
//    if (!Objects.equals(courseRating, that.courseRating)) {
//      System.out.println("COURSE RATING: Expected " + courseRating + ", GOT "
//      + that.courseRating);
//    }
//    if (!Objects.equals(profRating, that.profRating)) {
//      System.out.println("PROF RATING: Expected " + profRating + ", GOT " + that.profRating);
//    }
//    if (!Objects.equals(avgHours, that.avgHours)) {
//      System.out.println("AVG HOURS: Expected " + avgHours + ", GOT " + that.avgHours);
//    }
//    if (!Objects.equals(maxHours, that.maxHours)) {
//      System.out.println("MAX HOURS: Expected " + maxHours + ", GOT " + that.maxHours);
//    }
//    if (!Objects.equals(classSize, that.classSize)) {
//      System.out.println("CLASS SIZE: Expected " + classSize + ", GOT " + that.classSize);
//    }
//    if (!(Double.compare(that.weight, weight) == 0)){
//      System.out.println("WEIGHT: EXPECTED " + weight + ", GOT " + that.weight);
//    }
//    if (visited != that.visited) {
//      System.out.println("VISITED: Expected " + visited + ", GOT " + that.visited);
//    }
//    if (!Objects.equals(prevPath, that.prevPath)) {
//      System.out.println("PREVIOUS PATH: Expected " + prevPath + ", GOT " + that.prevPath);
//    }
//    if (!Arrays.equals(coordinates, that.coordinates)) {
//      System.out.println("COORDINATES: Expected " + Arrays.toString(coordinates) + ", GOT "
//      + Arrays.toString(that.coordinates));
//    }
    return Objects.equals(sem, that.sem)
        && Objects.equals(courseRating, that.courseRating)
        && Objects.equals(profRating, that.profRating)
        && Objects.equals(avgHours, that.avgHours)
        && Objects.equals(maxHours, that.maxHours)
        && Objects.equals(classSize, that.classSize)
        && Objects.equals(id, that.id)
        && Objects.equals(name, that.name)
        && Objects.equals(instr, that.instr)
        && Objects.equals(prereq, that.prereq)
        && Objects.equals(prevPath, that.prevPath)
        && Double.compare(that.weight, weight) == 0
        && visited == that.visited
        && Arrays.equals(coordinates, that.coordinates);
  }

  /**
   * Returns a hash representation of a CourseNode.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  /**
   * Returns a string representing a CourseNode.
   *
   * @return a string representing a CourseNode
   */
  @Override
  public String toString() {
    return "NODE (" + id + ": " + name + ")";
  }

  /**
   * Returns a copy of the CourseNode.
   *
   * @return a copy of the CourseNode
   */
  @Override
  public CourseNode copy() {
    return new CourseNode(this.id, this.name, this.instr, this.sem, this.rawprereq, this.prereq,
        this.description, this.courseRating, this.profRating, this.avgHours, this.maxHours,
        this.classSize);
  }
}
