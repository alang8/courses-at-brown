package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.graph.Graph;

import java.util.HashMap;
import java.util.Objects;
import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;
import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;

import java.util.HashMap;
import java.util.Set;

/**
 * Specific graph implementation containing CourseNodes and CourseEdges.
 */
public class CourseGraph implements Graph<CourseNode, CourseEdge> {
  private HashMap<String, CourseNode> nodeMap;
  private HashMap<String, HashMap<String, CourseEdge>> edgeMap;

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
   * Helper function which returns a copy of a HashMap of Nodes
   */
  private HashMap<String, CourseNode> nodeSetCopy(HashMap<String, CourseNode> nodeMap) {
    HashMap<String, CourseNode> newNodeMap = new HashMap<>();
    for (String nodeID : nodeMap.keySet()) {
      newNodeMap.put(nodeID, (CourseNode) nodeMap.get(nodeID).copy());
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
   * Adds a CourseNode to the node map and edges with the node as their start to the edge map.
   * @param node CourseNode to add to the map
   * @param edges set of CourseEdges to check
   */
  @Override
  public void addNode(CourseNode node, Set<CourseEdge> edges) {
    nodeMap.put(node.getID(), node);
    for (CourseEdge edge : edges) {
      if (edge.getStart().equals(node.getID())) {
        edgeMap.put(edge.getID(), new HashMap<>() {{ put(edge.getID(), edge); }});
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
