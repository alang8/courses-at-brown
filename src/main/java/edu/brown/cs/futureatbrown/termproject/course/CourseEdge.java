package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;

import java.util.*;
import java.util.stream.Collectors;

/**
 * Specific edge implementation that links a start CourseNode to an end CourseNode.
 */
public class CourseEdge extends GraphEdge<CourseNode> {
  // CONSTANTS
  private static final double MIN_RATING_PREF = 0;
  private static final double MAX_RATING_PREF = 10;
  private static final double MIN_RATING = 0;
  private static final double MAX_RATING = 5;
  private static final int DEFAULT_MIN_CLASSES = 0;
  private static final int DEFAULT_MAX_CLASSES = 32; // 8 Semesters * 4 Classes per Semesters

  // Edge Specific Variables
  private final String id;
  private CourseNode start;
  private CourseNode end;
  private double weight;
  private boolean overrideWeightCalc;

  // Global Variables

  // SLIDER PREFERENCES
  private Double crsRatingPref; // Number from 1 - 10 [Inclusive]
  private Double profRatingPref; // Number from 1 - 10 [Inclusive]
  private Double avgHoursPref; // Number from 1 - 10 [Inclusive]
  private Double balanceFactorPref; // Number from 1 - 10 [Inclusive]
  private Double classSizePref; // Number from 1 - 10 [Inclusive]

  // HARD INPUTS
  private Double avgHoursInput; // Desired Avg Hour Workload PER CLASS
  private Double totalMaxHoursInput; // TOTAL HOURS OVERALL [ENTIRE PATHWAY]
  private Integer classSizeInput; // Desired Class Size Per Class
  private Integer classSizeMax; // Max Class Size
  private Integer minNumClasses; // Minimum Number of Classes
  private Integer maxNumClasses; // Maximum Number of Classes

  // GLOBAL Dijkstra Specific Inputs
  private CourseNode globalEnd;
  private Set<List<CourseNode>> prereqs; //Global Prerequisites of the target end node

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
   * Sets up all the global parameters of the graph in this edge
   */
  public void setGlobalParams(double crsRatingPref, double profRatingPref, double avgHoursPref,
                              double avgHoursInput, int minNumClasses, int maxNumClasses,
                              double balanceFactorPref, double totalMaxHoursInput,
                              double classSizePref, int classSizeInput, int classSizeMax) {
    // SLIDER PREFERENCES
    this.crsRatingPref = crsRatingPref;
    this.profRatingPref = profRatingPref;
    this.avgHoursPref = avgHoursPref;
    this.balanceFactorPref = balanceFactorPref;
    this.classSizePref = classSizePref;

    // HARD INPUTS
    this.avgHoursInput = avgHoursInput; // AVG HOURS PER CLASS
    this.totalMaxHoursInput = totalMaxHoursInput; // TOTAL HOURS OVERALL
    this.minNumClasses = minNumClasses;
    this.maxNumClasses = maxNumClasses;
    this.classSizeInput = classSizeInput;
    this.classSizeMax = classSizeMax;
  }

  /**
   * Sets up the Global Prerequisites and overall requirements
   *
   */
  public void setGlobalPrereqs(Set<List<CourseNode>> prereqs) {
    this.prereqs = prereqs;
  }

  /**
   * Sets up the Global End Node for the specific dijkstra run
   */
  public void setGlobalEnd(CourseNode end) {
    this.globalEnd = end;
  }

  /**
   * Converts a path of edges into a path of nodes
   * @param path - Path of edges up to the node
   * @return Path of nodeIDs up to the node
   */
  private List<String> convertPath(List<CourseEdge> path) {
    List<String> nodes = new ArrayList<>();
    if (path.size() > 0) {
      nodes.add(path.get(0).getStart().getID());
    }
    for (CourseEdge edge : path) {
      nodes.add(edge.getEnd().getID());
    }
    return nodes;
  }

  /**
   * Calculates the weight of the edge based on Penalties and Sliders
   * The rating preferences are all within the bounds of 1 - 10
   * The Course and Professor scores are all withing the bounds of 0 - 5
   * The Avg Hours, Max Hours, Number of Classes scores are dependent on distance from global input
   * @return weight - Calculated weight
   */
  private double calculateWeight() {
    double AVG_RATING_PREF = (MIN_RATING_PREF + MAX_RATING_PREF) / 2;
    double AVG_RATING = (MIN_RATING + MAX_RATING) / 2;

    //////////////////////////////////////
    // OVERRIDE: MANUAL WEIGHT INPUTTED //
    //////////////////////////////////////
    if (this.overrideWeightCalc) {
      return this.weight;
    }

    // Otherwise Initialize weight to 0
    this.weight = 0;

    //////////////////////////////////////////////
    // PENALTY: PREREQUISITES MUST BE SATISFIED //
    //////////////////////////////////////////////

    // Grab the prereqs and compare to previous path
    Set<List<String>> preReqs = this.end.getPrereqSet();
    List<String> previousPath = convertPath(this.start.getPreviousPath());
    previousPath.add(this.start.getID()); // Make sure to add the from node to the path

    // Check to make sure that all prerequisites are satisfied
    boolean satisfiedAllPrereqs = true;

    for (List<String> group : preReqs) {
      boolean satisfiedPrereq = !Collections.disjoint(group, previousPath);
      if (!satisfiedPrereq) {
        satisfiedAllPrereqs = false;
        break;
      }
    }

    // If they aren't satisfied then nullify the path
    if (!satisfiedAllPrereqs) {
      System.out.println("FAILED PREREQS");
      return Double.POSITIVE_INFINITY;
    }

    ///////////////////////////////////////////////
    // PENALTY: MUST REACH MIN NUMBER OF CLASSES //
    ///////////////////////////////////////////////
    if (null != this.minNumClasses &&
        previousPath.size() < this.minNumClasses &&
        this.end.equals(this.globalEnd)) {
      return Double.POSITIVE_INFINITY;
    }

    ////////////////////////////////////////
    // PENALTY: REQUIREMENTS FOR PATHWAYS //
    ////////////////////////////////////////
    // TODO: AFTER REDOING CACHING

    //////////////////////////////////////////////////
    // PENALTY: CANNOT EXCEED MAX NUMBER OF CLASSES //
    //////////////////////////////////////////////////
    if (null != this.maxNumClasses) {
      if (previousPath.size() >= this.maxNumClasses) {
        System.out.println("EXCEEDED MAX NUM CLASSES");
        return Double.POSITIVE_INFINITY;
      }
    }

    /////////////////////////////////////
    // SLIDER COMPONENT: COURSE RATING //
    /////////////////////////////////////
    double courseRating;

    // Default Course Rating Preference to 5 if null (Halfway between 0 - 10)
    if (null == this.crsRatingPref) {
      this.crsRatingPref = AVG_RATING_PREF;
    }

    // Default Course Rating to 2.5 if null (Halfway between 0 - 5)
    if (null == this.end.getCourse_rating()) {
      courseRating = AVG_RATING;
    } else {
      courseRating = this.end.getCourse_rating();
    }

    // Calculate the Slider Weight
    this.weight +=  Math.pow(2, this.crsRatingPref) / courseRating;

    ////////////////////////////////////////
    // SLIDER COMPONENT: PROFESSOR RATING //
    ////////////////////////////////////////
    double professorRating;

    // Default Professor Rating Preference to 5 if null (Halfway between 0 - 10)
    if (null == this.profRatingPref) {
      this.profRatingPref = AVG_RATING_PREF;
    }

    // Default Professor Rating to 2.5 if null (Halfway between 0 - 5)
    if (null == this.end.getProf_rating()) {
      professorRating = AVG_RATING;
    } else {
      professorRating = this.end.getProf_rating();
    }

    // Calculate the Slider Weight
    this.weight += Math.pow(2, this.profRatingPref) / professorRating;

    /////////////////////////////////
    // SLIDER COMPONENT: AVG HOURS //
    /////////////////////////////////
    double prevTotalAvgHours;
    double avgNumClasses;
    double classAvgHours;

    // Default Average Hours Preference to 5 if null (Halfway between 0 - 10)
    if (null == this.avgHoursPref) {
      this.avgHoursPref = AVG_RATING_PREF;
    }

    if (null == this.balanceFactorPref) {
      this.balanceFactorPref = AVG_RATING_PREF;
    }

    // Initialize the total avg hours to 0 if null
    if (null == this.start.getPrevTotalAvgHours()) {
      prevTotalAvgHours = 0;
    } else {
      prevTotalAvgHours = this.start.getPrevTotalAvgHours();
    }

    if (null == this.minNumClasses) {
      this.minNumClasses = DEFAULT_MIN_CLASSES;
    }

    if (null == this.maxNumClasses) {
      this.maxNumClasses = DEFAULT_MAX_CLASSES;
    }

    avgNumClasses = (this.minNumClasses + this.maxNumClasses) / 2;

    // According to the credit hour guidance average hours per class should
    // be 12 hours (4 hours class + 8 hours out of class)
    if (null == this.end.getAvg_hours()) {
      classAvgHours = 12;
    } else  {
      classAvgHours =  this.end.getAvg_hours();
    }

    if (null == this.avgHoursInput) {
      this.avgHoursInput = 12.0;
    }

    // Get the Total Avg Hours up to this point
    Double totalAvgHours = prevTotalAvgHours;
    totalAvgHours += classAvgHours;
    this.end.setPrevTotalAvgHours(totalAvgHours);

    // Calculate Desired total avg hours
    double desiredTotalAvgHours = avgNumClasses * this.avgHoursInput;

    // Penalize by distance if it goes over, Penalize by balance if it is under
    if (totalAvgHours > desiredTotalAvgHours) {
      this.weight += Math.pow(2, this.avgHoursPref) * 0.2 * (totalAvgHours - desiredTotalAvgHours)
          / desiredTotalAvgHours;
    } else {
      this.weight += Math.pow(2, this.balanceFactorPref) * 0.2
          * Math.abs(classAvgHours - this.avgHoursInput) / this.avgHoursInput;
    }

    /////////////////////////////////
    // SLIDER COMPONENT: MAX HOURS //
    /////////////////////////////////
    double prevMaxHours;
    double classMaxHours;

    if (null == this.start.getPrevTotalMaxHours()) {
      prevMaxHours = 0;
    } else {
      prevMaxHours = this.start.getPrevTotalMaxHours();
    }

    if (null == this.end.getMax_hours()) {
      classMaxHours = 0;
    } else {
      classMaxHours = this.end.getMax_hours();
    }

    if (null != this.totalMaxHoursInput) {
      // Get the Total Max Hours up to this point
      double totalMaxHours = prevMaxHours;
      totalMaxHours += classMaxHours;
      this.end.setPrevTotalMaxHours(totalMaxHours);

      if (totalMaxHours > this.totalMaxHoursInput) {
        return Double.POSITIVE_INFINITY;
      }
    }

    //////////////////////////////////
    // SLIDER COMPONENT: CLASS SIZE //
    //////////////////////////////////
    int courseClassSize;

    if (null == this.classSizePref) {
      this.classSizePref = AVG_RATING_PREF;
    }

    if (null == this.end.getClass_size()) {
      courseClassSize = 0;
    } else {
      courseClassSize = this.end.getClass_size();
    }

    if (null == this.classSizeInput) {
      this.classSizeInput = 0;
    }

    if (null == this.classSizeMax) {
      this.classSizeMax = 500;
    }

    this.weight += Math.pow(2, this.classSizePref) * 0.2
        * Math.abs(courseClassSize - this.classSizeInput) / this.classSizeMax;

    ////////////////////////////////////////////////////////
    //  PENALTY: +2000 PER PREREQUISITE GROUP UNSATISFIED //
    ////////////////////////////////////////////////////////
    if (null != this.prereqs) {
      for (List<CourseNode> group : this.prereqs) {
        List<String> groupIDs = group.stream()
            .filter(elem -> elem != null)
            .map(elem -> elem.getID())
            .collect(Collectors.toList());
        if (!Collections.disjoint(groupIDs, previousPath)) {
          continue;
        }
        if (group.contains(this.end)) {
          this.weight -= 2000;
        }
      }
    }
    return this.weight;
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
    return new CourseEdge(this.id, this.start.copy(), this.end.copy());
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
