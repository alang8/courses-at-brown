package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Specific graph implementation containing CourseNodes and CourseEdges.
 */
public class CourseGraph implements Graph<CourseNode, CourseEdge> {
  // Graph Specific Variables
  private Map<String, CourseNode> nodeMap;
  private Map<String, Map<String, CourseEdge>> edgeMap;

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
  private Map<String, Integer> groupData;
  private Map<Integer, HashMap<String, CourseWay>> courseWayData;

  private Set<List<CourseNode>> prereqs; // Global Prerequisites of the target end node

  /**
   * Constructs a new CourseGraph with the given parameters.
   */
  public CourseGraph() {
    nodeMap = new HashMap<>();
    edgeMap = new HashMap<>();
  }

  /**
   * Returns the HashMap of GraphNodes.
   *
   * @return the HashMap
   */
  @Override
  public Map<String, CourseNode> getNodeSet() {
    return nodeMap;
  }

  /**
   * Returns the HashMap of GraphEdges.
   *
   * @return the HashMap
   */
  @Override
  public Map<String, Map<String, CourseEdge>> getEdgeSet() {
    return edgeMap;
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

    // SET ALL THE EDGES TO THE SAME PARAMETER
    for (Map<String, CourseEdge> edgesFrom: this.edgeMap.values()) {
      for (CourseEdge edge : edgesFrom.values()) {
        edge.setGlobalParams(this.crsRatingPref, this.profRatingPref,
            this.avgHoursPref, this.avgHoursInput, this.minNumClasses,
            this.maxNumClasses, this.balanceFactorPref, this.totalMaxHoursInput,
            this.classSizePref, this.classSizeInput, this.classSizeMax,
            this.groupData, this.courseWayData);
      }
    }
  }

  /**
   * Sets up the global prerequisites and overall requirements.
   *
   * @param prereqs the prerequisites
   */
  public void setGlobalPrereqs(Set<List<CourseNode>> prereqs) {
    this.prereqs = prereqs;
  }

  /**
   * Helper function which returns a copy of a HashMap of nodes.
   *
   * @return a copy of the NodeSet
   */
  private Map<String, CourseNode> nodeSetCopy(Map<String, CourseNode> nodeMap) {
    Map<String, CourseNode> newNodeMap = new HashMap<>();
    for (String nodeID : nodeMap.keySet()) {
      newNodeMap.put(nodeID, nodeMap.get(nodeID).copy());
    }
    return newNodeMap;
  }

  /**
   * Helper function which returns a copy of a HashMap of edges.
   *
   * @return a copy of the EdgeSet
   */
  private Map<String, CourseEdge> edgeSetCopy(Map<String, CourseEdge> edgeMap) {
    Map<String, CourseEdge> newEdgeMap = new HashMap<>();
    for (String nodeID : edgeMap.keySet()) {
      newEdgeMap.put(nodeID, (CourseEdge) edgeMap.get(nodeID).copy());
    }
    return newEdgeMap;
  }

  /**
   * Creates and returns a copy of the current CourseGraph.
   *
   * @return a copy of the CourseGraph
   */
  @Override
  public CourseGraph copy() {
    CourseGraph graphCopy = new CourseGraph();
    Map<String, CourseNode> newNodeMap = nodeSetCopy(this.nodeMap);
    Map<String, Map<String, CourseEdge>> newEdgeMap = new HashMap<>();

    for (String nodeID : this.edgeMap.keySet()) {
      newEdgeMap.put(nodeID, edgeSetCopy(this.edgeMap.get(nodeID)));
    }
    graphCopy.setNodeMap(newNodeMap);
    graphCopy.setEdgeMap(newEdgeMap);
    return graphCopy;
  }

  /**
   * Sets up the global parameters of the CourseGraph if needed.
   *
   * @param startID the start id
   * @param endID the end id
   */
  @Override
  public void setup(String startID, String endID) {
    Set<List<String>> prereqSet = this.nodeMap.get(endID).getPrereqSet();
    System.out.println("END NODE: " + endID);
    System.out.println("PREREQ SET: " + prereqSet);
    Set<List<CourseNode>> prereqs = prereqSet
        .stream()
        .map(group -> group.stream().map(id -> this.nodeMap.get(id)).collect(Collectors.toList()))
        .collect(Collectors.toSet());
    setGlobalPrereqs(prereqs);
    for (Map<String, CourseEdge> edgesFrom: this.edgeMap.values()) {
      for (CourseEdge edge : edgesFrom.values()) {
        edge.setGlobalPrereqs(this.prereqs);
        edge.setGlobalEnd(this.nodeMap.get(endID));
      }
    }
    System.out.println("PREREQS: " + this.prereqs);
    for (CourseNode node: this.nodeMap.values()) {
      node.resetHoursAndGroups();
    }
  }

  /**
   * Adds a CourseNode to the node map and edges with the node as their start to the edge map.
   * @param node CourseNode to add to the map
   * @param edges set of CourseEdges to check
   */
  @Override
  public void addNode(CourseNode node, Set<CourseEdge> edges) {
    nodeMap.put(node.getID(), node);
    for (CourseEdge edge : edges) {
      try {
        edge.setGlobalParams(this.crsRatingPref, this.profRatingPref,
            this.avgHoursPref, this.avgHoursInput, this.minNumClasses,
            this.maxNumClasses, this.balanceFactorPref, this.totalMaxHoursInput,
            this.classSizePref, this.classSizeInput, this.classSizeMax,
            this.groupData, this.courseWayData);
      } catch (NullPointerException ignored) { }
      if (edge.getStart().getID().equals(node.getID())) {
        if (edgeMap.containsKey(edge.getStart().getID())) {
          edgeMap.get(edge.getStart().getID()).put(edge.getEnd().getID(), edge);
        } else {
          edgeMap.put(edge.getStart().getID(),
              new HashMap<>() {{ put(edge.getEnd().getID(), edge); }});
        }
      }
    }
  }

  /**
   * Sets the NodeMap of this CourseGraph.
   *
   * @param nodeMap the NodeMap
   */
  public void setNodeMap(Map<String, CourseNode> nodeMap) {
    this.nodeMap = nodeMap;
  }

  /**
   * Sets the EdgeMap of this CourseGraph.
   *
   * @param em the EdgeMap
   */
  public void setEdgeMap(Map<String, Map<String, CourseEdge>> em) {
    this.edgeMap = em;
  }

  /**
   * Checks if this CourseGraph is equal to another object.
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
    CourseGraph that = (CourseGraph) o;
    return nodeMap.equals(that.nodeMap) && edgeMap.equals(that.edgeMap);
  }

  /**
   * Returns a hash representation of CourseGraph.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return Objects.hash(nodeMap, edgeMap);
  }

  /**
   * Creates a string representation for this CourseGraph.
   *
   * @return a string representing this CourseGraph
   */
  @Override
  public String toString() {
    return "CourseGraph{"
        + "nodeMap=" + nodeMap
        + ", edgeMap=" + edgeMap
        + '}';
  }
}
