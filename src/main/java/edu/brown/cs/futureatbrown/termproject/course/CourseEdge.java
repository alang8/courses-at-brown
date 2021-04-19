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
  private int currentGroup;
  private int currentNumInGroup;
  private double sensitivity = 2;

  //////////////////////
  // Global Variables //
  //////////////////////

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
  private Map<String, Integer> groupData;
  private Map<Integer, HashMap<String, CourseWay>> courseWayData;

  // GLOBAL Dijkstra Specific Inputs
  private CourseNode globalEnd;
  private Set<List<CourseNode>> prereqs; // Global Prerequisites of the target end node

  /**
   * Constructs a CourseEdge with the given id, start id, and end id.
   *
   * @param id the id of this CourseEdge
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
    if (this.overrideWeightCalc) {
      return weight;
    } else {
      return calculateWeight();
    }
  }

  /**
   * Sets up all the global parameters of the graph in this edge.
   * <p>
   * RECALL that all of these are relative so preferences of all 10s are the same as Preferences of
   * all 1s.
   *
   * @param crsRatingPref Course Rating Preference: How important is the Course Rating (0 - 10)?
   * @param profRatingPref Professor Rating Preference: How important is the professor rating
   *                       (0 - 10)?
   * @param avgHoursPref Average Hours Preference: How important is the average hours of the class
   *                     (0 - 10)? A penalty is applied when the total sum of average hours of all
   *                     courses in the pathway exceed the total acceptable average hours. This
   *                     penalty is based on how much it goes over.
   * @param avgHoursInput Average Hours Input: User inputted optimal average hours per class.
   * @param minNumClasses Min Number of Classes: Minimum number of courses the pathway must contain.
   * @param maxNumClasses Max Number of Classes: Maximum number of courses the pathway must contain.
   * @param balanceFactorPref Balance Factor Preference: How important is it that each individual
   *                          course in the pathway are close to the Average Hours Input?
   * @param totalMaxHoursInput Total Acceptable Max Number of Hours: Will shut down any pathways
   *                           that exceed the max number of hours. Use Double.POSITIVE_INFINITY if
   *                           you want to nullify this check.
   * @param classSizePref Class Size Preference: How important is the class size (0 - 10)?
   * @param classSizeInput Class Size Input: Penalized for distance from input.
   * @param classSizeMax Max Class Size Input: Penalty decreases if user can tolerate a larger class
   *                     size and increases if the user cannot.
   * @param groupData Group Data: Map of group id to number of courses required to satisfy in that
   *                  group for the particular pathway.
   * @param courseWayData CourseWay Data: Map of group id to course id to the CourseWay in the format
   *                      (CourseID, Sequence, GroupID).
   */
  public void setGlobalParams(double crsRatingPref, double profRatingPref, double avgHoursPref,
                              double avgHoursInput, int minNumClasses, int maxNumClasses,
                              double balanceFactorPref, double totalMaxHoursInput,
                              double classSizePref, int classSizeInput, int classSizeMax,
                              Map<String, Integer> groupData,
                              Map<Integer, HashMap<String, CourseWay>> courseWayData) {
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
    this.groupData = groupData;
    this.courseWayData = courseWayData;
  }

  /**
   * Sets up the global prerequisites and overall requirements.
   *
   * @param prereqs Set of groups of courses that contain the prereqs to the end node
   */
  public void setGlobalPrereqs(Set<List<CourseNode>> prereqs) {
    this.prereqs = prereqs;
  }

  /**
   * Sets up the global end node for the specific Dijkstra run.
   *
   * @param end the end node of the current Dijkstra run
   */
  public void setGlobalEnd(CourseNode end) {
    this.globalEnd = end;
  }

  /**
   * Converts a path of edges into a path of nodes.
   *
   * @param path path of edges up to the node
   * @return path of nodeIDs up to the node
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
   * Runs through the given path to see if the requirements are fulfilled
   * and gives a reward to weight fulfilled
   * currentGroup - Current Group within the pathway to be satisfied
   * currentNumInGroup - Amount of requirements currently satisfied in the group
   * @param pathToInclude - List of Nodes to check for requirements in and to reward if fulfilled
   * @param pathToExclude - List of Nodes to check for requirements in, but not to reward.
   * Represents the path that has been explored before!
   * @param reward - Amount to be rewarded if requirements fulfilled
   * @param nullify - Whether or not to nullify future group fulfillment
   * @return a boolean representing whether or not all pathway requirements have been satisfied
   */
   private boolean pathwayRequirementUpdate(List<String> pathToInclude, List<String> pathToExclude, double reward, boolean nullify) {
     boolean satisfiedAllPathwayReqs = true;
     if (null != this.courseWayData && null != this.groupData) {
       boolean checkNextGroup = true;
       while (checkNextGroup) {
         checkNextGroup = false;
         if (this.courseWayData.containsKey(currentGroup)) {
           Map<String, CourseWay> courseWayHashMap = this.courseWayData.get(currentGroup);
           Set<CourseWay> totalWayPrereqs = new HashSet<>();
           for (String courseID : courseWayHashMap.keySet()) {
             CourseWay courseway = courseWayHashMap.get(courseID);
             String coursewayGroupID = Integer.toString(courseway.getGroupID());
             boolean readyForProcessing = Integer.parseInt(coursewayGroupID) == currentGroup;
             Set<CourseWay> courseWayPrereqs = courseway.getPrerequisites();
             if (courseWayPrereqs != null) {
               totalWayPrereqs.addAll(courseWayPrereqs);
             }
             if (Integer.parseInt(coursewayGroupID) >= currentGroup) {
               satisfiedAllPathwayReqs = false;
               if (readyForProcessing) {
                 List<String> sequence = new ArrayList<>(courseway.getSequence());
                 sequence.add(courseID);
//                 System.out.println("SEQUENCE: " + sequence);

                 if (pathToInclude.containsAll(sequence) && !pathToExclude.containsAll(sequence)) {
                   currentNumInGroup += 1;
//                   System.out.println("CURRENT GROUP " + currentGroup + " UPDATING NUM TO " + currentNumInGroup);
                   this.weight -= reward;
                 }

                 int numReq = this.groupData.get(coursewayGroupID);

                 if (numReq <= currentNumInGroup) {
                   checkNextGroup = true;
                   currentGroup += 1;
                   currentNumInGroup = 0;
//                   System.out.println("NUM REQUIREMENTS: " + numReq);
//                   System.out.println("CURRENT GROUP UPDATING TO " + currentGroup);
                 }
               }
             }
           }
           if (nullify) {
             for (Integer groupID : this.courseWayData.keySet()) {
//               System.out.println("CURRENT GROUP: " + currentGroup);
               if (groupID > currentGroup) {
                 for (String futureID : this.courseWayData.get(groupID).keySet()) {
                   boolean notAPrereq = true;
                   for (CourseWay prereqGroup : totalWayPrereqs) {
                     if (prereqGroup.getSequence().contains(futureID)) {
                       notAPrereq = false;
                       break;
                     }
                   }
                   // NULLIFY THE EDGE BECAUSE OF OUT OF ORDER EXPLORATION
                   if (this.end.getID().equals(futureID) && notAPrereq) {
//                     System.out.println("NULLABLE COURSE ID: " + futureID);
//                     System.out.println("SEARCH SPACE: " + this.courseWayData.get(groupID));
                     this.weight = Double.POSITIVE_INFINITY;
                     break;
                   }
                 }
               }
             }
           }
         }
       }
     }
     return satisfiedAllPathwayReqs;
   }

  /**
   * Calculates the weight of the edge based on Penalties and Sliders
   * The rating preferences are all within the bounds of 1 - 10
   * The Course and Professor scores are all withing the bounds of 0 - 5
   * The Avg Hours, Max Hours, Number of Classes scores are dependent on distance from global input
   * @return weight - Calculated weight
   */
  private double calculateWeight() {
    final double avgRatingPref = (MIN_RATING_PREF + MAX_RATING_PREF) / 2;
    final double avgRating = (MIN_RATING + MAX_RATING) / 2;

    //////////////////////////////////////
    // OVERRIDE: MANUAL WEIGHT INPUTTED //
    //////////////////////////////////////
    if (this.overrideWeightCalc) {
//    System.out.println("OVERRIDE WEIGHT");
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
    List<CourseNode> coursesTaken = this.start.getPreviousCourses();
    List<String> coursesTakenID =
        coursesTaken.stream().map(CourseNode::getID).collect(Collectors.toList());

    // Check to make sure that all prerequisites are satisfied
    boolean satisfiedAllPrereqs = true;

    for (List<String> group : preReqs) {
      boolean satisfiedPrereq = !Collections.disjoint(group, previousPath)
          || !Collections.disjoint(group, coursesTakenID);
      if (!satisfiedPrereq) {
        satisfiedAllPrereqs = false;
        break;
      }
    }

    // If they aren't satisfied then nullify the path
    if (!satisfiedAllPrereqs) {
//    System.out.println("FAILED PREREQS");
      return Double.POSITIVE_INFINITY;
    }

    ///////////////////////////////////////////////
    // PENALTY: MUST REACH MIN NUMBER OF CLASSES //
    ///////////////////////////////////////////////
    if (null != this.minNumClasses
        && previousPath.size() + coursesTaken.size() < this.minNumClasses
        && this.end.equals(this.globalEnd)) {
//      System.out.println("ENDING BUT HASN'T REACHED MIN EDGE");
      return Double.POSITIVE_INFINITY;
    }

    //////////////////////////////////////////////////
    // PENALTY: CANNOT EXCEED MAX NUMBER OF CLASSES //
    //////////////////////////////////////////////////
    if (null != this.maxNumClasses) {
      if (previousPath.size() + coursesTaken.size() > this.maxNumClasses) {
//      System.out.println("EXCEEDED MAX NUM CLASSES");
        return Double.POSITIVE_INFINITY;
      }
    }

    /////////////////////////////////////
    // SLIDER COMPONENT: COURSE RATING //
    /////////////////////////////////////
    double courseRating;

    // Default Course Rating Preference to 5 if null (Halfway between 0 - 10)
    if (null == this.crsRatingPref) {
      this.crsRatingPref = avgRatingPref;
    }

    // Default Course Rating to 2.5 if null (Halfway between 0 - 5)
    if (null == this.end.getCourseRating()) {
      courseRating = avgRating;
    } else {
      courseRating = this.end.getCourseRating();
    }

    // Calculate the Slider Weight
//    System.out.println("COURSE RATING INCORPORATED");
    this.weight +=  Math.pow(sensitivity, this.crsRatingPref) / courseRating;

    ////////////////////////////////////////
    // SLIDER COMPONENT: PROFESSOR RATING //
    ////////////////////////////////////////
    double professorRating;

    // Default Professor Rating Preference to 5 if null (Halfway between 0 - 10)
    if (null == this.profRatingPref) {
      this.profRatingPref = avgRatingPref;
    }

    // Default Professor Rating to 2.5 if null (Halfway between 0 - 5)
    if (null == this.end.getProfRating()) {
      professorRating = avgRating;
    } else {
      professorRating = this.end.getProfRating();
    }

    // Calculate the Slider Weight
//    System.out.println("PROFESSOR RATING INCORPORATED");
    this.weight += Math.pow(sensitivity, this.profRatingPref) / professorRating;

    /////////////////////////////////
    // SLIDER COMPONENT: AVG HOURS //
    /////////////////////////////////
    double prevTotalAvgHours;
    double avgNumClasses;
    double classAvgHours;

    // Default Average Hours Preference to 5 if null (Halfway between 0 - 10)
    if (null == this.avgHoursPref) {
      this.avgHoursPref = avgRatingPref;
    }

    if (null == this.balanceFactorPref) {
      this.balanceFactorPref = avgRatingPref;
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

    avgNumClasses = (this.minNumClasses + this.maxNumClasses) / 2.0;

    // According to the credit hour guidance average hours per class should
    // be 12 hours (4 hours class + 8 hours out of class)
    if (null == this.end.getAvgHours()) {
      classAvgHours = 12;
    } else  {
      classAvgHours =  this.end.getAvgHours();
    }

    if (null == this.avgHoursInput) {
      this.avgHoursInput = 12.0;
    }

    // Get the Total Avg Hours up to this point
    double totalAvgHours = prevTotalAvgHours;
    totalAvgHours += classAvgHours;
    this.end.setPrevTotalAvgHours(totalAvgHours);

    // Calculate Desired total avg hours
    double desiredTotalAvgHours = avgNumClasses * this.avgHoursInput;

    // Penalize by distance if it goes over, Penalize by balance if it is under
    if (totalAvgHours > desiredTotalAvgHours) {
//      System.out.println("AVG HOURS RAN OVER LIMIT");
      this.weight += Math.pow(sensitivity, this.avgHoursPref) * 0.2 * (totalAvgHours - desiredTotalAvgHours)
          / desiredTotalAvgHours;
    } else {
//      System.out.println("BALANCE OF AVG HOURS - CLASSES SELECTED");
      this.weight += Math.pow(sensitivity, this.balanceFactorPref) * 0.2
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

    if (null == this.end.getMaxHours()) {
      classMaxHours = 0;
    } else {
      classMaxHours = this.end.getMaxHours();
    }

    if (null != this.totalMaxHoursInput) {
      // Get the Total Max Hours up to this point
      double totalMaxHours = prevMaxHours;
      totalMaxHours += classMaxHours;
      this.end.setPrevTotalMaxHours(totalMaxHours);

      if (totalMaxHours > this.totalMaxHoursInput) {
//      System.out.println("EXCEEDED MAX HOURS");
        return Double.POSITIVE_INFINITY;
      }
    }

    //////////////////////////////////
    // SLIDER COMPONENT: CLASS SIZE //
    //////////////////////////////////
    int courseClassSize;

    if (null == this.classSizePref) {
      this.classSizePref = avgRatingPref;
    }

    if (null == this.end.getClassSize()) {
      courseClassSize = 0;
    } else {
      courseClassSize = this.end.getClassSize();
    }

    if (null == this.classSizeInput) {
      this.classSizeInput = 0;
    }

    if (null == this.classSizeMax) {
      this.classSizeMax = 500;
    }

//    System.out.println("CLASS SIZE CONSIDERED");
    this.weight += Math.pow(sensitivity, this.classSizePref) * 0.2
        * Math.abs(courseClassSize - this.classSizeInput) / this.classSizeMax;

    /////////////////////////////////////////////////////
    //  REWARD: -2000 PER PREREQUISITE GROUP SATISFIED //
    /////////////////////////////////////////////////////
    if (null != this.prereqs) {
      for (List<CourseNode> group : this.prereqs) {
        List<String> groupIDs = group.stream()
            .filter(Objects::nonNull)
            .map(CourseNode::getID)
            .collect(Collectors.toList());
        if (!Collections.disjoint(groupIDs, previousPath)
            || !Collections.disjoint(groupIDs, coursesTakenID)) {
          continue;
        }
        if (group.contains(this.end)) {
          this.weight -= 2000;
        }
      }
    }

    /////////////////////////////////////////////////////
    // REWARD: -5000 PER PATHWAY REQUIREMENT SATISFIED //
    /////////////////////////////////////////////////////

    // FROM NOW ON INCLUDE END NODE IN PREVIOUS PATH

    // SETUP TOTAL PATH EXPLORED AND CURRENT GROUP OF REQUIREMENTS TO FULFILL
    List<String> totalPath = new ArrayList<>(previousPath);
    totalPath.addAll(coursesTakenID);
    List<String> pathCheckedBefore = new ArrayList<>(totalPath);
    totalPath.add(this.end.getID());

    this.currentGroup = this.start.getCurrentGroup();
    this.currentNumInGroup = 0;

    // DEFENSIVELY CHECK THAT PATHWAY REQUIREMENTS ARE SATISFIED
    // GIVE REWARD IF THE NEW NODE CAUSES A NEW REQUIREMENT TO BE SATISFIED>
    pathwayRequirementUpdate(pathCheckedBefore, new ArrayList<>(), 0, false);
    boolean satisfiedAllPathwayReqs = pathwayRequirementUpdate(totalPath, pathCheckedBefore, 5000, true);


//    System.out.println("GOING TO SET " + this.end.getID() + " CURRENT GROUP TO " + currentGroup);
    this.end.setCurrentGroup(currentGroup);
//    System.out.println("NEXT CURRENT GROUP SET TO " + this.end.getCurrentGroup());

    // PENALTY: STOP IT FROM FINISHING IF IT DOESN'T SATISFY ALL PATHWAY REQUIREMENTS
    if (!satisfiedAllPathwayReqs && this.end.equals(this.globalEnd)) {
//      System.out.println("CURRENT GROUP: " + currentGroup);
//      System.out.println("PATHWAY BLOCK");
      return Double.POSITIVE_INFINITY;
    }

    return this.weight;
  }

  /**
   * Sets and overrides the weight of this CourseEdge.
   *
   * @param weight weight of the edge
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
   * Sets the sensitivity of the sliders
   * @param sensitivity - sensitivity of the sliders
   */
  public void setSensitivity(double sensitivity) {
    this.sensitivity = sensitivity;
  }

  /**
   * Returns a copy of the CourseEdge.
   *
   * @return a copy of the CourseEdge
   */
  @Override
  public GraphEdge<?> copy() {
    return new CourseEdge(this.id, this.start.copy(), this.end.copy());
  }

  /**
   * Checks if this CourseEdge is equal to another object.
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
    CourseEdge that = (CourseEdge) o;
    return Objects.equals(id, that.id);
  }

  /**
   * Returns a hash representation of CourseEdge.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  /**
   * Creates a string representation for this CourseEdge.
   *
   * @return a string representing this CourseEdge
   */
  @Override
  public String toString() {
    return "EDGE: (" + id + ": " + start + " to " + end + ")";
  }
}
