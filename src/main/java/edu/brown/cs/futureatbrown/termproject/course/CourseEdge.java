package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;

import java.util.*;
import java.util.stream.Collectors;

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
  private Double avgHoursInput; // Desired Avg Hour Workload PER CLASS
  private Double totalMaxHoursInput; // TOTAL HOURS OVERALL [ENTIRE PATHWAY]
  private Integer classSizeInput; // Desired Class Size Per Class
  private Integer classSizeMax; // Max Class Size
  private Integer minNumClasses; // Minimum Number of Classes
  private Integer maxNumClasses; // Maximum Number of Classes

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
  public void setGlobalParams(double crsRatingPref, double profRatingPref,
                              double avgHoursPref, double avgHoursInput, int minNumClasses,
                              int maxNumClasses, double balanceFactorPref, double maxHoursPref,
                              double totalMaxHoursInput, double classSizePref, int classSizeInput,
                              int classSizeMax) {
    // SLIDER PREFERENCES
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
   * Sets up the Global Prerequisites and overall requirements
   *
   */
   public void setGlobalPrereqs(Set<List<CourseNode>> prereqs) {
     this.prereqs = prereqs;
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
     // TODO: AFTER REDOING CACHING

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

     if (null != this.crsRatingPref && null != this.end.getCourse_rating()) {
       this.weight +=  Math.pow(2, this.end.getCourse_rating()) / this.crsRatingPref;
     }

     ////////////////////////////////////////
     // SLIDER COMPONENT: PROFESSOR RATING //
     ////////////////////////////////////////

     if (null != this.profRatingPref && null != this.end.getProf_rating()) {
       this.weight += Math.pow(2, this.end.getProf_rating()) / this.profRatingPref;
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
         this.weight += Math.pow(2, this.avgHoursPref) * 0.2 * (totalAvgHours - desiredTotalAvgHours) / desiredTotalAvgHours;
       } else {
         this.weight += Math.pow(2, this.balanceFactorPref) * 0.2 * Math.abs(this.end.getAvg_hours() - this.avgHoursInput) / this.avgHoursInput;
       }
     }


       /////////////////////////////////
       // SLIDER COMPONENT: MAX HOURS //
       /////////////////////////////////

       if (null != this.maxHoursPref && null != this.start.getPrevTotalMaxHours() && null != this.end.getMax_hours() &&
         null != this.totalMaxHoursInput) {

         // Get the Total Max Hours up to this point
         double totalMaxHours = this.start.getPrevTotalMaxHours();
         System.out.println("PREVIOUS TOTAL MAX HOURS: " + totalMaxHours);
         totalMaxHours += this.end.getMax_hours();
         System.out.println("NEW MAX HOURS: " + this.end.getMax_hours());
         this.end.setPrevTotalMaxHours(totalMaxHours);

         if (totalMaxHours > this.totalMaxHoursInput) {
           System.out.println("EXCEEDED MAX HOURS: " + totalMaxHours + " > " + this.totalMaxHoursInput);
           return Double.POSITIVE_INFINITY;
         }
       }

       //////////////////////////////////
       // SLIDER COMPONENT: CLASS SIZE //
       //////////////////////////////////
       if (null != this.classSizePref && null != this.end.getClass_size() && null != this.classSizeInput && null != this.classSizeMax) {
         this.weight += Math.pow(2, this.classSizePref) * 0.2 * Math.abs(this.end.getClass_size() - this.classSizeInput) / this.classSizeMax;
       }

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
