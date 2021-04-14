package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.Graph;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

import java.util.Set;
import java.util.stream.Collectors;

/**
 * Specific graph implementation containing CourseNodes and CourseEdges.
 */
public class CourseGraph implements Graph<CourseNode, CourseEdge> {
  // Graph Specific Variables
  private HashMap<String, CourseNode> nodeMap;
  private HashMap<String, HashMap<String, CourseEdge>> edgeMap;

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
  public HashMap<String, CourseNode> getNodeSet() {
    return nodeMap;
  }

  /**
   * Returns the HashMap of GraphEdges.
   *
   * @return the HashMap
   */
  @Override
  public HashMap<String, HashMap<String, CourseEdge>> getEdgeSet() {
    return edgeMap;
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

    // SET ALL THE EDGES TO THE SAME PARAMETER
    for (HashMap<String, CourseEdge> edgesFrom: this.edgeMap.values()) {
      for (CourseEdge edge : edgesFrom.values()) {
        edge.setGlobalParams(this.crsRatingPref, this.profRatingPref,
          this.avgHoursPref, this.avgHoursInput, this.minNumClasses,
          this.maxNumClasses, this.balanceFactorPref, this.totalMaxHoursInput,
          this.classSizePref, this.classSizeInput, this.classSizeMax);
      }
    }
  }

  /**
   * Sets up the Global Prerequisites and overall requirements
   *
   */
  public void setGlobalPrereqs(Set<List<CourseNode>> prereqs) {
    this.prereqs = prereqs;
  }

  /**
   * Helper function which returns a copy of a HashMap of Nodes
   */
  private HashMap<String, CourseNode> nodeSetCopy(HashMap<String, CourseNode> nodeMap) {
    HashMap<String, CourseNode> newNodeMap = new HashMap<>();
    for (String nodeID : nodeMap.keySet()) {
      newNodeMap.put(nodeID, nodeMap.get(nodeID).copy());
    }
    return newNodeMap;
  }

  /**
   * Helper function which returns a copy of a HashMap of Edges
   */
  private HashMap<String, CourseEdge> edgeSetCopy(HashMap<String, CourseEdge> edgeMap) {
    HashMap<String, CourseEdge> newEdgeMap = new HashMap<>();
    for (String nodeID : edgeMap.keySet()) {
      newEdgeMap.put(nodeID, (CourseEdge) edgeMap.get(nodeID).copy());
    }
    return newEdgeMap;
  }

  /**
   * Creates and returns a copy of the current CourseGraph.
   */
  @Override
  public CourseGraph copy() {
    CourseGraph graphCopy = new CourseGraph();
    HashMap<String, CourseNode> newNodeMap = nodeSetCopy(this.nodeMap);
    HashMap<String, HashMap<String, CourseEdge>> newEdgeMap = new HashMap<>();

    for (String nodeID : this.edgeMap.keySet()) {
      newEdgeMap.put(nodeID, edgeSetCopy(this.edgeMap.get(nodeID)));
    }
    graphCopy.setNodeMap(newNodeMap);
    graphCopy.setEdgeMap(newEdgeMap);
    return graphCopy;
  }

  /**
   * Sets up the global parameters of the graph if needed
   *
   * @param startID
   * @param endID
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
    for (HashMap<String, CourseEdge> edgesFrom: this.edgeMap.values()) {
      for (CourseEdge edge : edgesFrom.values()) {
        edge.setGlobalPrereqs(this.prereqs);
        edge.setGlobalEnd(this.nodeMap.get(endID));
      }
    }
    System.out.println("PREREQS: " + this.prereqs);
    for (CourseNode node: this.nodeMap.values()) {
      node.resetHours();
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
          this.classSizePref, this.classSizeInput, this.classSizeMax);
      } catch (NullPointerException e) {
        System.out.println("WARNING: Remember to Initialize the Global Parameters");
      }
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
  public void setNodeMap(HashMap<String, CourseNode> nodeMap) {
    this.nodeMap = nodeMap;
  }

  /**
   * Sets the EdgeMap of this CourseGraph.
   *
   * @param em the EdgeMap
   */
  public void setEdgeMap(HashMap<String, HashMap<String, CourseEdge>> em) {
    this.edgeMap = em;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    CourseGraph that = (CourseGraph) o;
    return nodeMap.equals(that.nodeMap) && edgeMap.equals(that.edgeMap);
  }

  @Override
  public int hashCode() {
    return Objects.hash(nodeMap, edgeMap);
  }

  @Override
  public String toString() {
    return "CourseGraph{" +
      "nodeMap=" + nodeMap +
      ", edgeMap=" + edgeMap +
      '}';
  }
}
