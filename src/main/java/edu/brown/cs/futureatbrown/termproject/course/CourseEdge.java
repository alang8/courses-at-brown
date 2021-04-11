package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * Specific edge implementation that links a start CourseNode to an end CourseNode.
 */
public class CourseEdge extends GraphEdge<CourseNode> {
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
  private Double maxHoursPref; // Number from 1 - 10 [Inclusive]
  private Double classSizePref; // Number from 1 - 10 [Inclusive]

  // HARD INPUTS
  private Integer globalSem;
  private Double avgHoursInput; // Desired Avg Hour Workload PER CLASS
  private Double totalMaxHoursInput; // TOTAL HOURS OVERALL [ENTIRE PATHWAY]
  private Integer classSizeInput; // Desired Class Size Per Class
  private Integer classSizeMax; // Max Class Size
  private Integer minNumClasses; // Minimum Number of Classes
  private Integer maxNumClasses; // Maximum Number of Classes


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
   * @param globalSem - The semester the user has picked
   */
  public void setGlobalParams(int globalSem, double crsRatingPref, double profRatingPref,
                              double avgHoursPref, double avgHoursInput, int minNumClasses,
                              int maxNumClasses, double balanceFactorPref, double maxHoursPref,
                              double totalMaxHoursInput, double classSizePref, int classSizeInput,
                              int classSizeMax) {
    // SLIDER PREFERENCES
    this.globalSem = globalSem;
    this.crsRatingPref = crsRatingPref;
    this.profRatingPref = profRatingPref;
    this.avgHoursPref = avgHoursPref;
    this.maxHoursPref = maxHoursPref;
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
       boolean satisfiedPrereq = false;
       for (String prereqId : group) {
         if (previousPath.contains(prereqId)) {
           satisfiedPrereq = true;
           break;
         }
       }
       if (!satisfiedPrereq) {
         satisfiedAllPrereqs = false;
         break;
       }
     }

     // If they aren't satisfied then nullify the path
     if (!satisfiedAllPrereqs) {
       return Double.POSITIVE_INFINITY;
     }
     System.out.println("SATISFIED PREREQS");

     //////////////////////////////////
     // PENALTY: SEMESTER MUST MATCH //
     //////////////////////////////////

//     if (null != this.globalSem && null != this.end.getSem()) {
//       if (!Objects.equals(this.globalSem, this.end.getSem())) {
//         return Double.POSITIVE_INFINITY;
//       }
//     }

     ///////////////////////////////////////////////
     // PENALTY: MUST REACH MIN NUMBER OF CLASSES //
     ///////////////////////////////////////////////
     // TODO: AFTER REDOING CACHING

     ///////////////////////////////////////
     // PENALTY: REQUIREMENTS FOR PATHWAYS //
     ///////////////////////////////////////
     // TODO: AFTER REDOING CACHING

     //////////////////////////////////////////////////
     // PENALTY: CANNOT EXCEED MAX NUMBER OF CLASSES //
     //////////////////////////////////////////////////
     if (null != this.maxNumClasses) {
       if (previousPath.size() >= this.maxNumClasses) {
         return Double.POSITIVE_INFINITY;
       }
     }

     /////////////////////////////////////
     // SLIDER COMPONENT: COURSE RATING //
     /////////////////////////////////////

     if (null != this.crsRatingPref && null != this.end.getCourse_rating()) {
       this.weight += this.end.getCourse_rating() * this.crsRatingPref;
     }

     ////////////////////////////////////////
     // SLIDER COMPONENT: PROFESSOR RATING //
     ////////////////////////////////////////

     if (null != this.profRatingPref && null != this.end.getProf_rating()) {
       this.weight += this.end.getProf_rating() * this.profRatingPref;
     }

     /////////////////////////////////
     // SLIDER COMPONENT: AVG HOURS //
     /////////////////////////////////

     if (null != this.avgHoursPref && null != this.start.getPrevTotalAvgHours() && null != this.end.getAvg_hours() &&
       null != this.maxNumClasses && null != this.minNumClasses && null != this.avgHoursInput) {
       // Get the Total Avg Hours up to this point
       Double totalAvgHours = this.start.getPrevTotalAvgHours();
       totalAvgHours += this.end.getAvg_hours();
       this.end.setPrevTotalAvgHours(totalAvgHours);

       // Calculate Desired total avg hours
       double desiredTotalAvgHours = (this.maxNumClasses + this.minNumClasses) * this.avgHoursInput / 2;

       // Penalize by distance if it goes over, Penalize by balance if it is under
       if (totalAvgHours > desiredTotalAvgHours) {
         this.weight += this.avgHoursPref * 5 * (totalAvgHours - desiredTotalAvgHours) / desiredTotalAvgHours;
       } else {
         this.weight += this.balanceFactorPref * 5 * Math.abs(this.end.getAvg_hours() - this.avgHoursInput) / this.avgHoursInput;
       }
     }


       /////////////////////////////////
       // SLIDER COMPONENT: MAX HOURS //
       /////////////////////////////////

       if (null != this.maxHoursPref && null != this.start.getPrevTotalMaxHours() && null != this.end.getMax_hours() &&
         null != this.totalMaxHoursInput) {
         // Get the Total Max Hours up to this point
         double totalMaxHours = this.start.getPrevTotalMaxHours();
         totalMaxHours += this.end.getMax_hours();
         this.end.setPrevTotalMaxHours(totalMaxHours);

         if (totalMaxHours > this.totalMaxHoursInput) {
           return Double.POSITIVE_INFINITY;
         }
       }

       //////////////////////////////////
       // SLIDER COMPONENT: CLASS SIZE //
       //////////////////////////////////
       if (null != this.classSizePref && null != this.end.getClass_size() && null != this.classSizeInput && null != this.classSizeMax) {
         this.weight += this.classSizePref * 5 * Math.abs(this.end.getClass_size() - this.classSizeInput) / this.classSizeMax;
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
