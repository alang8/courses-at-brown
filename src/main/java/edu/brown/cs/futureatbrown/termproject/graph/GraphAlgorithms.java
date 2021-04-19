package edu.brown.cs.futureatbrown.termproject.graph;

import java.security.InvalidAlgorithmParameterException;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A class which contains all the short-path algorithms and short-path data of the inputted graph
 * for the Future @ Brown (FAB) app.
 *
 * @param <Edge> an Edge implementation
 * @param <G> a generic Graph
 * @param <Node> a Node implementation
 */
public class GraphAlgorithms<Node extends GraphNode, Edge extends GraphEdge,
    G extends Graph<Node, Edge>> {

  private final HashMap<String, HashMap<String, List<Edge>>> results;

  // ------------------------------------- Comparators ------------------------------------
  /**
   * A class that compares the weights of two paths to find the better one.
   */
  class ShortPathComparator implements Comparator<List<Edge>> {
    @Override
    public int compare(List<Edge> path1, List<Edge> path2) {
      double path1Cost = path1.stream().map(GraphEdge::getWeight).reduce(0.0,
        Double::sum);
      double path2Cost = path2.stream().map(GraphEdge::getWeight).reduce(0.0,
        Double::sum);
      // We want an ascending order comparator
      return Double.compare(path1Cost, path2Cost);
    }
  }

  // ------------------------------------- Constructors ------------------------------------
  /**
   * A constructor for a GraphAlgorithms object.
   */
  public GraphAlgorithms() {
    this.results = new HashMap<>();
  }

  // ----------------------------- Graph Short Path Algorithms -----------------------------

  /**
   * A helper function which initializes/re-initializes the nodes in the current graph to
   * the initial conditions needed for Dijkstra's algorithm. Assumes that start is in nodeSet.
   */
  private void dijkstraSetup(String startID, G graph) {
    for (Node n : graph.getNodeSet().values()) {
      n.setWeight(Double.POSITIVE_INFINITY);
      n.setPreviousPath(new ArrayList<>());
      n.setVisited(false);
    }
    graph.getNodeSet().get(startID).setWeight(0.0);
  }

  /**
   * A helper function which uses an dijkstra algorithm to find the 'shortest' path, represented as
   * an ordered list of Edges, from the start Node to the end Node.
   *
   * @param startID the start Node's id
   * @param endID the end Node's id
   * @return a List of Edges which represents the optimal path from the start Node to the end Node;
   * the List will be empty if the start Node is the end Node; null will be returned if the end node
   * isn't reachable by the start node
   * @throws InvalidAlgorithmParameterException if either the start node or the end node is not
   * contained in the graph
   */
  private List<Edge> dijkstraHelper(String startID, String endID, G graph)
      throws InvalidAlgorithmParameterException {
    Map<String, Node> nodeSet = graph.getNodeSet();
    Map<String, Map<String, Edge>> edgeSet = graph.getEdgeSet();
    if (!nodeSet.containsKey(startID)) {
      throw new InvalidAlgorithmParameterException("Nodes must exist in the graph");
    }
    // Create priority queue of (Node, distance)
    dijkstraSetup(startID, graph);
    graph.setup(startID, endID);
    PriorityQueue<Node> minHeap = new PriorityQueue<>(
        Comparator.comparingDouble(Node::getWeight));
    // Add starting node to the Min Heap
    minHeap.add(nodeSet.get(startID));
    // Iterate over minHeap
    while (minHeap.peek() != null) {
      // Pop off the minHeap
      Node currNode = minHeap.remove();
      String currID = currNode.getID();

      // Process Node only if it hasn't been visited yet
      if (!currNode.visited()) {
        // Iterate through all of the neighbors of the current node
        for (Edge edge : edgeSet.get(currID).values()) {
          // Get Neighbor and calculate score to neighbor from currrent node
          Node neighbor = (Node) edge.getEnd();
          ArrayList prevPath = new ArrayList<>(nodeSet.get(currID).getPreviousPath());

          double newWeight = currNode.getWeight() + edge.getWeight();
          // Update hashMap and add to minHeap if neighbor can be reached betterly
          if (nodeSet.get(neighbor.getID()).getWeight() > newWeight) {
            nodeSet.get(neighbor.getID()).setWeight(newWeight);
            prevPath.add(edge);
            nodeSet.get(neighbor.getID()).setPreviousPath(prevPath);
            minHeap.add(nodeSet.get(neighbor.getID()));
          }
        }
      }
      if (currNode.getID().equals(endID)) {
        // Traceback and return the optimal path
        List<Edge> prevPath = nodeSet.get(endID).getPreviousPath();
        if (prevPath.size() > 0) {
          Edge startingEdge = prevPath.get(0);
          if (startingEdge.getStart().getID().equals(startID)) {
            return prevPath;
          }
        }
      }
      // Consider this node visited
      currNode.setVisited(true);
    }
    return null;
  }

  /**
   * A function which checks the cache for the 'shortest' path, and calculates the shortest path
   * from the start to end node represented as an ordered list of Edges if the 'shortest' path is
   * not in the cache.
   *
   * @param startID the start Node's id
   * @param endID the end Node's id
   * @param graph the Graph to search through
   * @return a List of Edges which represents the optimal path from the start Node to the end Node;
   * the List will be empty if the start Node is the end Node; null will be returned if the end node
   * isn't reachable by the start node
   * @throws InvalidAlgorithmParameterException if either the start node or the end node is not
   * contained in the graph
   */
  public final List<Edge> dijkstraPath(String startID, String endID, G graph)
      throws InvalidAlgorithmParameterException {
    if (this.results.isEmpty() || !this.results.containsKey(startID)) {
      this.results.put(startID, new HashMap<>());
    }
    if (!this.results.get(startID).containsKey(endID)) {
      this.results.get(startID).put(endID, dijkstraHelper(startID, endID, graph));
    }
    return this.results.get(startID).get(endID);
  }

  /**
   * A function that creates a Short Path Tree from a starting Node and returns a ordered list
   * containing all of the Short Paths to every other node in the same concentration (Graph)
   * using Dijkstras. The list is ordered from shortest path to longest path!
   *
   * @param startID the start Node's id
   * @param graph the graph that the short path tree will be built from
   * @return a List of k List of Edges which represents the optimal paths from the start Node to
   * the end Node; the List will be empty if the start Node is the end Node; null will be returned
   * if the end node isn't reachable by the start node
   * @throws InvalidAlgorithmParameterException if either the start node or the end node is not
   * contained in the graph
   */
  public final List<List<Edge>> dijkstraPathTree(String startID, G graph)
      throws InvalidAlgorithmParameterException {
    Map<String, Node> nodeSet = graph.getNodeSet();
    this.results.put(startID, new HashMap<>());
    for (Node node: nodeSet.values()) {
      if (!startID.equals(node.getID())) {
        List<Edge> shortPath = dijkstraHelper(startID, node.getID(), graph);
        if (shortPath != null) {
          this.results.get(startID).put(node.getID(), dijkstraHelper(startID, node.getID(), graph));
        }
      }
    }
    List<List<Edge>> results = new ArrayList<>(this.results.get(startID).values());
    results.sort(new ShortPathComparator());
    return results;
  }

  /**
   * A function which uses an Yen's algorithm to find the k 'shortest' paths, represented as an
   * ordered List of paths (from best to worst) where each path is an ordered List of Edges, from
   * the start Node to the end Node.
   *
   * @param startID the start Node's id
   * @param endID the end Node's id
   * @param graph the Graph to search through
   * @param k the number of paths to return
   * @return a List of k List of Edges which represents the optimal paths from the start Node to
   * the end Node; the List will be empty if the start Node is the end Node; null will be returned
   * if the end node isn't reachable by the start node
   * @throws InvalidAlgorithmParameterException if either the start node or the end node is not
   * contained in the graph
   */
  public final List<List<Edge>> yenPaths(String startID, String endID, G graph, int k)
      throws InvalidAlgorithmParameterException {
    // List to store the k shortest path results
    List<List<Edge>> results = new ArrayList<>();
    G graphCopy = (G) graph.copy();
    GraphAlgorithms<Node, Edge, Graph<Node, Edge>> uncached = new GraphAlgorithms<>();
    // Determine the shortest path from the start to the end.
    results.add(uncached.dijkstraPath(startID, endID, graphCopy));
    // List to store potential k shortest paths
    PriorityQueue<List<Edge>> potentialPaths =
        new PriorityQueue<>(new ShortPathComparator());
    // Parallel Set to ensure there are no duplicates
    Set<List<Edge>> potentialPathsSet = new HashSet<>();
    for (int kidx = 1; kidx < k; kidx++) {
      // Spur Node selection follows the last shortest path
      for (int i = 0; i < results.get(kidx - 1).size(); i++) {
        // Grab the current spur node from the last shortest path
        Node spurNode;
        if (i == 0) {
          spurNode = (Node) results.get(kidx - 1).get(0).getStart();
        } else {
          spurNode = (Node) results.get(kidx - 1).get(i - 1).getEnd();
        }
        // Create a temporary copy of the graph to modify and traverse through
        G currGraph = (G) graph.copy();
        GraphAlgorithms<Node, Edge, G> currAlgos = new GraphAlgorithms<>();
        // Get the Root Path up to but excluding the spur node to create deviations from
        List<Edge> rootPath = results.get(kidx - 1).stream().limit(i).collect(Collectors.toList());
        for (List<Edge> path : results) {
          // If they have the same root path, nullify the edge that was explored before
          List<Edge> rootOfPath = path.stream().limit(i).collect(Collectors.toList());
          if (rootPath.equals(rootOfPath)) {
            String fromNodeID = path.get(i).getStart().getID();
            String toNodeID = path.get(i).getEnd().getID();
            currGraph.getEdgeSet().get(fromNodeID).get(toNodeID)
                .setWeight(Double.POSITIVE_INFINITY);
          }
        }
        // Make it so that the Root Path has all been visited before
        if (rootPath.size() > 0) {
          currGraph.getNodeSet().get(rootPath.get(0).getStart().getID()).setVisited(true);
          for (Edge e : rootPath) {
            currGraph.getNodeSet().get(e.getEnd().getID()).setVisited(true);
          }
        }
        // Calculate path from Spur Node to end with Dijkstras
        List<Edge> spurPath = currAlgos.dijkstraPath(spurNode.getID(), endID, currGraph);
        if (spurPath == null) {
          continue;
        }
        // Concatenate to get potential alternate path
        List<Edge> totalPath = new ArrayList<>();
        totalPath.addAll(rootPath);
        totalPath.addAll(spurPath);
        // Add to potential paths if not already in there
        if (!potentialPathsSet.contains(totalPath)) {
          potentialPathsSet.add(totalPath);
          potentialPaths.add(totalPath);
        }
      }
      // Break if no spur path, spur paths are exhausted, or reached dead end
      if (null == potentialPaths.peek()) {
        break;
      }
      // Otherwise pop off a potential path and add it to the result before continuing!
      List<Edge> newPath = potentialPaths.remove();
      results.add(newPath);
    }
    return results;
  }

  /**
   * Merges K sorted lists together with a Comparator.
   * @param sortedLists List of sorted Lists of elements
   * @param comparator Comparator that compares the elements
   * @return a sorted list of all the elements in sortedLists
   */
  private <T> List<T> mergeSortedLists(List<List<T>> sortedLists, Comparator<T> comparator) {
    PriorityQueue<T> sortedQueue = new PriorityQueue<>(comparator);

    for (List<T> sortedList : sortedLists) {
      sortedQueue.addAll(sortedList);
    }

    List<T> results = new ArrayList<>();
    while (!sortedQueue.isEmpty()) {
      results.add(sortedQueue.remove());
    }
    return results;
  }

  /**
   * PATHWAY ALGORITHM: Run Dijkstra Path Tree from every starting node. Merge the Pathways together
   * while maintaining sorted order.
   *
   * @param introCourses List of Introductory classes to start the pathway search from
   * @param courseGraph Graph to search within
   * @return List of Paths sorted by "shortest" path
   * @throws InvalidAlgorithmParameterException if either the start node or the end node is not
   * contained in the graph
   */
  public List<List<Edge>> pathway(List<String> introCourses, G courseGraph)
      throws InvalidAlgorithmParameterException {
    List<List<List<Edge>>> toMerge = new ArrayList<>();
    for (String introClassID : introCourses) {
      List<List<Edge>> l = dijkstraPathTree(introClassID, courseGraph);
      toMerge.add(l);
    }
    System.out.println("In Pathway:");
    System.out.println(toMerge);
    return mergeSortedLists(toMerge, new ShortPathComparator());
  }
}
