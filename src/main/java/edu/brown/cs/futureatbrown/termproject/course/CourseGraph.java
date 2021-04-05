package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.Graph;
import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;
import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;

import java.util.HashMap;
import java.util.Set;

/**
 * Specific graph implementation containing CourseNodes and CourseEdges.
 */
public class CourseGraph implements Graph<GraphNode, GraphEdge> {
  private HashMap<String, GraphNode> nodeMap;
  private HashMap<String, HashMap<String, GraphEdge>> edgeMap;

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
  public HashMap<String, GraphNode> getNodeSet() {
    return nodeMap;
  }

  /**
   * Returns the HashMap of GraphEdges.
   *
   * @return the HashMap
   */
  @Override
  public HashMap<String, HashMap<String, GraphEdge>> getEdgeSet() {
    return edgeMap;
  }

  @Override
  public void addNode(GraphNode node, Set<GraphEdge> graphEdges) {
    // Implement this after full design
  }

  /**
   * Creates and returns a copy of the current CourseGraph.
   */
  @Override
  public Graph<GraphNode, GraphEdge> copy() {
    CourseGraph graphCopy = new CourseGraph();
    graphCopy.setNodeMap(this.nodeMap);
    graphCopy.setEdgeMap(this.edgeMap);
    return graphCopy;
  }

  /**
   * Sets the NodeMap of this CourseGraph.
   *
   * @param nm the NodeMap
   */
  public void setNodeMap(HashMap<String, GraphNode> nm) {
    this.nodeMap = nm;
  }

  /**
   * Sets the EdgeMap of this CourseGraph.
   *
   * @param em the EdgeMap
   */
  public void setEdgeMap(HashMap<String, HashMap<String, GraphEdge>> em) {
    this.edgeMap = em;
  }
}
