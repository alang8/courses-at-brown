package edu.brown.cs.futureatbrown.termproject;

import edu.brown.cs.futureatbrown.termproject.course.CourseNode;
import edu.brown.cs.futureatbrown.termproject.course.GraphAlgorithms;
import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;
import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;
import edu.brown.cs.futureatbrown.termproject.graph.Graph;
import edu.brown.cs.futureatbrown.termproject.course.CourseGraph;
import org.checkerframework.checker.units.qual.C;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidAlgorithmParameterException;
import java.util.*;

/**
 * Bare Minimum Implementation of a Node
 */
class BasicNode extends GraphNode<GraphEdge> {
  String id;
  double weight;
  boolean visited;
  List<GraphEdge> prevPath;

  public BasicNode (String id, double weight) {
    this.id = id;
    this.weight = weight;
    this.visited = false;
    this.prevPath = new ArrayList<>();
  }

  public BasicNode (String id, double weight, boolean visited) {
    this.id = id;
    this.weight = weight;
    this.visited = visited;
    this.prevPath = new ArrayList<>();
  }

  public String getID() {
    return this.id;
  }

  public void setWeight(double weight) {
    this.weight = weight;
  }

  public double getWeight() {
    return this.weight;
  }

  public void setVisited(Boolean visited) {
    this.visited = visited;
  }

  public Boolean visited() {
    return this.visited;
  }

  public void setPreviousPath(List<GraphEdge> prevPath) {
    this.prevPath = prevPath;
  }

  public List<GraphEdge> getPreviousPath() {
    return this.prevPath;
  }

  @Override
  public GraphNode copy() {
    return new BasicNode(this.id, this.weight, this.visited);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BasicNode node = (BasicNode) o;
    return id.equals(node.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "BasicNode{" +
      "id='" + id + '\'' +
      '}';
  }
}

class BasicEdge extends GraphEdge<GraphNode> {
  String id;
  double weight;
  GraphNode start;
  GraphNode end;

  public BasicEdge (String id, GraphNode start, GraphNode end) {
    this.id = id;
    this.start = start;
    this.end = end;
  }

  public BasicEdge (String id, double weight, GraphNode start, GraphNode end) {
    this.id = id;
    this.weight = weight;
    this.start = start;
    this.end = end;
  }

  @Override
  public String getID() {
    return this.id;
  }

  @Override
  public double getWeight() {
    return this.weight;
  }

  @Override
  public void setWeight(double weight) {
    this.weight = weight;
  }

  @Override
  public void setStart(GraphNode startingNode) {
    this.start = startingNode;
  }

  @Override
  public GraphNode getStart() {
    return this.start;
  }

  @Override
  public void setEnd(GraphNode endingNode) {
    this.end = endingNode;
  }

  @Override
  public GraphNode getEnd() {
    return this.end;
  }

  @Override
  public GraphEdge copy() {
    return new BasicEdge(this.id, this.weight, this.start.copy(), this.end.copy());
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    BasicEdge edge = (BasicEdge) o;
    return Objects.equals(id, edge.id);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id);
  }

  @Override
  public String toString() {
    return "BasicEdge{" +
      "id='" + id + '\'' +
      ", start=" + start +
      ", end=" + end +
      '}';
  }
}

class BasicGraph implements Graph<BasicNode, BasicEdge> {
  HashMap<String, BasicNode> nodeSet;
  HashMap<String, HashMap<String, BasicEdge>> edgeSet;

  public BasicGraph() {
    this.nodeSet = new HashMap<>();
    this.edgeSet = new HashMap<>();
  }

  @Override
  public HashMap<String, BasicNode> getNodeSet() {
    return this.nodeSet;
  }

  @Override
  public HashMap<String, HashMap<String, BasicEdge>> getEdgeSet() {
    return this.edgeSet;
  }

  public void setNodeSet(HashMap<String, BasicNode> nodeSet) {
    this.nodeSet = nodeSet;
  }

  public void setEdgeSet(HashMap<String, HashMap<String, BasicEdge>> edgeSet) {
    this.edgeSet = edgeSet;
  }

  @Override
  public void addNode(BasicNode node, Set<BasicEdge> graphEdges) {
    this.nodeSet.put(node.getID(), node);
    this.edgeSet.put(node.getID(), new HashMap<>());
    for (BasicEdge edge : graphEdges) {
      if (edge.getStart().getID().equals(node.getID())) {
        this.edgeSet.get(node.getID()).put(edge.getEnd().getID(), edge);
      }
    }
  }

  /**
   * Helper function which returns a copy of a HashMap of Nodes
   */
  private HashMap<String, BasicNode> nodeSetCopy(HashMap<String, BasicNode> nodeMap) {
    HashMap<String, BasicNode> newNodeMap = new HashMap<>();
    for (String nodeID : nodeMap.keySet()) {
      newNodeMap.put(nodeID, (BasicNode) nodeMap.get(nodeID).copy());
    }
    return newNodeMap;
  }

  /**
   * Helper function which returns a copy of a HashMap of Edges
   */
  private HashMap<String, BasicEdge> edgeSetCopy(HashMap<String, BasicEdge> edgeMap) {
    HashMap<String, BasicEdge> newEdgeMap = new HashMap<>();
    for (String nodeID : edgeMap.keySet()) {
      newEdgeMap.put(nodeID, (BasicEdge) edgeMap.get(nodeID).copy());
    }
    return newEdgeMap;
  }

  @Override
  public BasicGraph copy() {
    BasicGraph graphCopy = new BasicGraph();
    HashMap<String, BasicNode> newNodeMap = nodeSetCopy(this.nodeSet);
    HashMap<String, HashMap<String, BasicEdge>> newEdgeMap = new HashMap<>();

    for (String nodeID : this.edgeSet.keySet()) {
      newEdgeMap.put(nodeID, edgeSetCopy(this.edgeSet.get(nodeID)));
    }
    graphCopy.setNodeSet(this.nodeSet);
    graphCopy.setEdgeSet(this.edgeSet);
    return graphCopy;
  }
}

/**
 * Fairly Simple tests for Dijkstra, Dijkstra Short Path, Yen's Algorithm
 */
public class GraphAlgorithmTest<Node extends GraphNode, Edge extends GraphEdge>{
  private BasicGraph simpleGraph;
  private BasicNode[] simpleNodes;
  private GraphAlgorithms<BasicNode, BasicEdge, BasicGraph> simpleAlgorithms;


  /**
   * Sets up a test graph of courses
   */
  @Before
  public void setup() {
    // Visual of Simple Graph:
    // [See edge initialization to see edge weights]
    /*    1--2-----3
     *   /|  |\    |\
     *  / |  | \   | \
     * 0  |  8  \  |  4
     *  \ | /|   \ | /
     *   \|/ |    \|/
     *    7--6-----5
     */
    simpleGraph = new BasicGraph();
    simpleAlgorithms = new GraphAlgorithms<>();

    // Initialize all of the nodes
    BasicNode node0 = new BasicNode("Node0", 0);
    BasicNode node1 = new BasicNode("Node1", 0);
    BasicNode node2 = new BasicNode("Node2", 0);
    BasicNode node3 = new BasicNode("Node3", 0);
    BasicNode node4 = new BasicNode("Node4", 0);
    BasicNode node5 = new BasicNode("Node5", 0);
    BasicNode node6 = new BasicNode("Node6", 0);
    BasicNode node7 = new BasicNode("Node7", 0);
    BasicNode node8 = new BasicNode("Node8", 0);
    simpleNodes = new BasicNode[]{node0, node1, node2, node3, node4, node5, node6, node7, node8};

    // Initialize all of the edges

    // EDGES FROM 0
    BasicEdge edge01 = new BasicEdge("Edge01", 4.0, node0, node1);
    BasicEdge edge07 = new BasicEdge("Edge07", 8.0, node0, node7);

    // EDGES FROM 1
    BasicEdge edge10 = new BasicEdge("Edge10", 4.0, node1, node0);
    BasicEdge edge12 = new BasicEdge("Edge12", 8.0, node1, node2);
    BasicEdge edge17 = new BasicEdge("Edge17", 11.0, node1, node7);


    // EDGES FROM 2
    BasicEdge edge21 = new BasicEdge("Edge21", 8.0, node2, node1);
    BasicEdge edge23 = new BasicEdge("Edge23", 7.0, node2, node3);
    BasicEdge edge25 = new BasicEdge("Edge25", 4.0, node2, node5);
    BasicEdge edge28 = new BasicEdge("Edge28", 2.0, node2, node8);

    // EDGES FROM 3
    BasicEdge edge32 = new BasicEdge("Edge32", 7.0, node3, node2);
    BasicEdge edge34 = new BasicEdge("Edge34", 9.0, node3, node4);
    BasicEdge edge35 = new BasicEdge("Edge35", 14.0, node3, node5);

    // EDGES FROM 4
    BasicEdge edge43 = new BasicEdge("Edge43", 9.0, node4, node3);
    BasicEdge edge45 = new BasicEdge("Edge45", 10.0, node4, node5);

    // EDGES FROM 5
    BasicEdge edge52 = new BasicEdge("Edge52", 4.0, node5, node2);
    BasicEdge edge53 = new BasicEdge("Edge53", 14.0, node5, node3);
    BasicEdge edge54 = new BasicEdge("Edge54", 10.0, node5, node4);
    BasicEdge edge56 = new BasicEdge("Edge56", 2.0, node5, node6);

    // EDGES FROM 6
    BasicEdge edge65 = new BasicEdge("Edge65", 2.0, node6, node5);
    BasicEdge edge67 = new BasicEdge("Edge67", 1.0, node6, node7);
    BasicEdge edge68 = new BasicEdge("Edge68", 6.0, node6, node8);

    // EDGES FROM 7
    BasicEdge edge70 = new BasicEdge("Edge70", 8.0, node7, node0);
    BasicEdge edge71 = new BasicEdge("Edge71", 11.0, node7, node1);
    BasicEdge edge76 = new BasicEdge("Edge76", 1.0, node7, node6);
    BasicEdge edge78 = new BasicEdge("Edge78", 7.0, node7, node8);

    // EDGES FROM 8
    BasicEdge edge82 = new BasicEdge("Edge82", 2.0, node8, node2);
    BasicEdge edge86 = new BasicEdge("Edge86", 6.0, node8, node6);
    BasicEdge edge87 = new BasicEdge("Edge87", 7.0, node8, node7);

    // Initialize the simple Graph
    simpleGraph.addNode(node0, new HashSet<>(List.of(edge01, edge07)));
    simpleGraph.addNode(node1, new HashSet<>(List.of(edge10, edge12, edge17)));
    simpleGraph.addNode(node2, new HashSet<>(List.of(edge21, edge23, edge25, edge28)));
    simpleGraph.addNode(node3, new HashSet<>(List.of(edge32, edge34, edge35)));
    simpleGraph.addNode(node4, new HashSet<>(List.of(edge43, edge45)));
    simpleGraph.addNode(node5, new HashSet<>(List.of(edge52, edge53, edge54, edge56)));
    simpleGraph.addNode(node6, new HashSet<>(List.of(edge65, edge67, edge68)));
    simpleGraph.addNode(node7, new HashSet<>(List.of(edge70, edge71, edge76, edge78)));
    simpleGraph.addNode(node8, new HashSet<>(List.of(edge82, edge86, edge87)));

  }

  @After
  public void teardown() {
    simpleGraph = null;
  }


  /**
   * Makes sure that the short path tree is generated correctly
   * @throws InvalidAlgorithmParameterException
   */
  @Test
  public void dijkstraPathTreeTest() throws InvalidAlgorithmParameterException {
    setup();
    Assert.assertEquals(simpleNodes.length, 9);
    Assert.assertEquals(simpleNodes[0].getID(), "Node0");
    List<List<BasicEdge>> simplePaths = simpleAlgorithms.dijkstraPathTree("Node0", simpleGraph);

    // SHORTEST PATHS QUEUED
    // 0 - 1 ==> 4
    // 0 - 7 ==> 8
    // 0 - 7 - 6 ==> 9
    // 0 - 7 - 6 - 5 ==> 11
    // 0 - 1 - 2 ==> 12
    // 0 - 1 - 2 - 8 ==> 14
    // 0 - 1 - 2 - 3 ==> 19
    // 0 - 7 - 6 - 5 - 4 ==> 21

    //////////////////////////////////
    // SIMPLE SHORT PATH TREE TESTS //
    //////////////////////////////////
    Assert.assertEquals(simplePaths.get(0),
      new ArrayList<>(List.of(
        new BasicEdge("Edge01", simpleNodes[0], simpleNodes[1])))
    );
    Assert.assertEquals(simplePaths.get(1),
      new ArrayList<>(List.of(
        new BasicEdge("Edge07", simpleNodes[0], simpleNodes[7])))
    );
    Assert.assertEquals(simplePaths.get(2),
      new ArrayList<>(List.of(
        new BasicEdge("Edge07", simpleNodes[0], simpleNodes[7]),
        new BasicEdge("Edge76", simpleNodes[7], simpleNodes[6])))
    );
    Assert.assertEquals(simplePaths.get(3),
      new ArrayList<>(List.of(
        new BasicEdge("Edge07", simpleNodes[0], simpleNodes[7]),
        new BasicEdge("Edge76", simpleNodes[7], simpleNodes[6]),
        new BasicEdge("Edge65", simpleNodes[6], simpleNodes[5])))
    );
    Assert.assertEquals(simplePaths.get(4),
      new ArrayList<>(List.of(
        new BasicEdge("Edge01", simpleNodes[0], simpleNodes[1]),
        new BasicEdge("Edge12", simpleNodes[1], simpleNodes[2])))
    );
    Assert.assertEquals(simplePaths.get(5),
      new ArrayList<>(List.of(
        new BasicEdge("Edge01", simpleNodes[0], simpleNodes[1]),
        new BasicEdge("Edge12", simpleNodes[1], simpleNodes[2]),
        new BasicEdge("Edge28", simpleNodes[2], simpleNodes[8])))
    );
    Assert.assertEquals(simplePaths.get(6),
      new ArrayList<>(List.of(
        new BasicEdge("Edge01", simpleNodes[0], simpleNodes[1]),
        new BasicEdge("Edge12", simpleNodes[1], simpleNodes[2]),
        new BasicEdge("Edge23", simpleNodes[2], simpleNodes[3])))
    );
    Assert.assertEquals(simplePaths.get(7),
      new ArrayList<>(List.of(
        new BasicEdge("Edge07", simpleNodes[0], simpleNodes[7]),
        new BasicEdge("Edge76", simpleNodes[7], simpleNodes[6]),
        new BasicEdge("Edge65", simpleNodes[6], simpleNodes[5]),
        new BasicEdge("Edge54", simpleNodes[5], simpleNodes[4])))
    );
    teardown();
  }

  /**
   * Makes sure that individual paths are selected properly
   * @throws InvalidAlgorithmParameterException
   */
  @Test
  public void dijkstraPathTest() throws InvalidAlgorithmParameterException {
    setup();
    Assert.assertEquals(
      simpleAlgorithms.dijkstraPath("Node0", "Node7", simpleGraph),
      new ArrayList<>(List.of(
        new BasicEdge("Edge07", simpleNodes[0], simpleNodes[7])))
    );
    Assert.assertEquals(
      simpleAlgorithms.dijkstraPath("Node0", "Node5", simpleGraph),
      new ArrayList<>(List.of(
        new BasicEdge("Edge07", simpleNodes[0], simpleNodes[7]),
        new BasicEdge("Edge76", simpleNodes[7], simpleNodes[6]),
        new BasicEdge("Edge65", simpleNodes[6], simpleNodes[5])))
    );
    Assert.assertEquals(
      simpleAlgorithms.dijkstraPath("Node0", "Node4", simpleGraph),
      new ArrayList<>(List.of(
        new BasicEdge("Edge07", simpleNodes[0], simpleNodes[7]),
        new BasicEdge("Edge76", simpleNodes[7], simpleNodes[6]),
        new BasicEdge("Edge65", simpleNodes[6], simpleNodes[5]),
        new BasicEdge("Edge54", simpleNodes[5], simpleNodes[4])))
    );
    Assert.assertEquals(
      simpleAlgorithms.dijkstraPath("Node2", "Node4", simpleGraph),
      new ArrayList<>(List.of(
        new BasicEdge("Edge25", simpleNodes[2], simpleNodes[5]),
        new BasicEdge("Edge54", simpleNodes[5], simpleNodes[4])))
    );

    Assert.assertEquals(
      simpleAlgorithms.dijkstraPath("Node7", "Node3", simpleGraph),
      new ArrayList<>(List.of(
        new BasicEdge("Edge76", simpleNodes[7], simpleNodes[6]),
        new BasicEdge("Edge65", simpleNodes[6], simpleNodes[5]),
        new BasicEdge("Edge52", simpleNodes[5], simpleNodes[2]),
        new BasicEdge("Edge23", simpleNodes[2], simpleNodes[3])))
    );

    teardown();
  }

  @Test
  public void YensAlgorithmTest() throws InvalidAlgorithmParameterException {
    setup();
    Assert.assertEquals(
      simpleAlgorithms.yenPaths("Node0", "Node7", simpleGraph, 3),
      List.of(
        new ArrayList<>(List.of(
          new BasicEdge("Edge07", simpleNodes[0], simpleNodes[7])
        )),
        new ArrayList<>(List.of(
          new BasicEdge("Edge01", simpleNodes[0], simpleNodes[1]),
          new BasicEdge("Edge17", simpleNodes[1], simpleNodes[7])
        )),
        new ArrayList<>(List.of(
          new BasicEdge("Edge01", simpleNodes[0], simpleNodes[1]),
          new BasicEdge("Edge12", simpleNodes[1], simpleNodes[2]),
          new BasicEdge("Edge25", simpleNodes[2], simpleNodes[5]),
          new BasicEdge("Edge56", simpleNodes[5], simpleNodes[6]),
          new BasicEdge("Edge67", simpleNodes[6], simpleNodes[7])))
        )
    );
    teardown();
  }
}