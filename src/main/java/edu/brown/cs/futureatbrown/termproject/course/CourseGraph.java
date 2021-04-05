package edu.brown.cs.student.finproject.course;

import edu.brown.cs.student.finproject.graph.Graph;
import edu.brown.cs.student.finproject.graph.GraphEdge;
import edu.brown.cs.student.finproject.graph.GraphNode;

import java.util.HashMap;
import java.util.Set;

/**
 * Specific graph implementation formed from CourseNodes and CourseEdges.
 */
public class CourseGraph implements Graph<GraphNode, GraphEdge> {
  HashMap<String, GraphNode> nodeMap;
  HashMap<String, HashMap<String, GraphEdge>> edgeMap;

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

  @Override
  public Graph<GraphNode, GraphEdge> copy() {
    CourseGraph graphCopy = new CourseGraph();
    // Implement this after full design
    return graphCopy;
  }
}
