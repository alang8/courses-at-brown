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
  public HashMap<String, GraphNode> getNodeMap() {
    return nodeMap;
  }

  /**
   * Returns the HashMap of GraphEdges.
   *
   * @return the HashMap
   */
  @Override
  public HashMap<String, HashMap<String, GraphEdge>> getEdgeMap() {
    return edgeMap;
  }

  /**
   * Adds a CourseNode to the node map and edges with the node as their start to the edge map.
   * @param node CourseNode to add to the map
   * @param edges set of CourseEdges to check
   */
  @Override
  public void addNode(GraphNode node, Set<GraphEdge> edges) {
    nodeMap.put(node.getID(), node);
    for (GraphEdge edge : edges) {
      if (edge.getStart().equals(node.getID())) {
        edgeMap.put(edge.getID(), new HashMap<>() {{ put(edge.getID(), edge); }});
      }
    }
  }

  /**
   * Creates and returns a copy of the CourseGraph.
   *
   * @return a copy of the CourseGraph
   */
  @Override
  public Graph<GraphNode, GraphEdge> copy() {
    CourseGraph graphCopy = new CourseGraph();
    graphCopy.setNodeMap(nodeMap);
    graphCopy.setEdgeMap(edgeMap);
    return graphCopy;
  }

  /**
   * Sets the NodeMap of this CourseGraph.
   *
   * @param nodeMap the NodeMap
   */
  public void setNodeMap(HashMap<String, GraphNode> nodeMap) {
    this.nodeMap = nodeMap;
  }

  /**
   * Sets the EdgeMap of this CourseGraph.
   *
   * @param edgeMap the EdgeMap
   */
  public void setEdgeMap(HashMap<String, HashMap<String, GraphEdge>> edgeMap) {
    this.edgeMap = edgeMap;
  }
}
