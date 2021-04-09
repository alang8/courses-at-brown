package edu.brown.cs.futureatbrown.termproject.graph;

import edu.brown.cs.futureatbrown.termproject.graph.Graph;
import edu.brown.cs.futureatbrown.termproject.graph.GraphEdge;
import edu.brown.cs.futureatbrown.termproject.graph.GraphNode;

import java.security.InvalidAlgorithmParameterException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * A class which contains all the short-path algorithms and short-path data of the inputted graph
 * for the Future @ Brown (FAB) app
 */
public class GraphAlgorithms<Node extends GraphNode, Edge extends GraphEdge, G extends Graph<Node, Edge>> {

  private HashMap<String, List<List<Edge>>> results;

  // ------------------------------------- Comparators ------------------------------------
  class ShortPathComparator implements Comparator<List<Edge>> {
    @Override
    public int compare(List<Edge> path1, List<Edge> path2) {
      double path1Cost = path1.stream().map(edge -> edge.getWeight()).reduce(0.0, (a, b) -> a + b);
      double path2Cost = path2.stream().map(edge -> edge.getWeight()).reduce(0.0, (a, b) -> a + b);
      // We want an ascending order comparator
      if (path1Cost > path2Cost) {
        return 1;
      } else if (path1Cost < path2Cost) {
        return -1;
      } else {
        return 0;
      }
    }
  }

  // ------------------------------------- Constructors -----------------------------------
  public GraphAlgorithms() {
    this.results = new HashMap<>();
  }

  // ----------------------------- Graph Short Path Algorithms ----------------------------

  /**
   * A helper function which initializes/reinitializes the nodes in the current graph to
   * the initial conditions needed for Dijkstra's algorithm. Assumes that start is in nodeSet
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
   * A function which uses an dijkstra algorithm to find the 'shortest' path,
   * represented as an ordered list of Edges, from the start Node to the end Node.
   *
   * @param startID the start Node's ID
   * @param endID   the end Node's ID
   * @return a List of Edges which represents the optimal path from the start Node to
   * the end Node. The List will be empty if the start Node is the end Node.
   * Null will be returned if the end node isn't reachable by the start node.
   * @throws InvalidAlgorithmParameterException if either the start node or the end node is not
   *           in the graph.
   */
  public final List<Edge> dijkstraPath(String startID, String endID, G graph)
    throws InvalidAlgorithmParameterException {
    if (this.results.isEmpty() || !this.results.containsKey(startID)) {
//     System.out.println("CALCULATING PATH FROM " + startID + " TO " + endID +": " + graph);
      dijkstraPathTree(startID, graph);
    }

    for (List<Edge> path : this.results.get(startID)) {
      if (path.size() > 0 && path.get(path.size() - 1).getEnd().getID().equals(endID)) {
        return path;
      }
    }
    return null;
  }

  /**
   * A function which uses an yen's algorithm to find the k 'shortest' paths,
   * represented as an ordered List of paths (from best to worst) where each path
   * is an ordered List of Edges, from the start Node to the end Node.
   *
   * @param startID the start Node's ID
   * @param endID   the end Node's ID
   * @param k     the number of paths to return
   * @return a List of k List of Edges which represents the optimal paths from the start Node to
   * the end Node. The List will be empty if the start Node is the end Node.
   * Null will be returned if the end node isn't reachable by the start node.
   * @throws InvalidAlgorithmParameterException if either the start node or the end node is not
   *           in the graph.
   */
  public final List<List<Edge>> yenPaths(String startID, String endID, G graph, int k)
    throws InvalidAlgorithmParameterException {

    // List to store the k shortest path results
    List<List<Edge>> results = new ArrayList<>();
    G graphCopy = (G) graph.copy();
    GraphAlgorithms uncached = new GraphAlgorithms();

    // Determine the shortest path from the start to the end.
    results.add(uncached.dijkstraPath(startID, endID, graphCopy));
//    System.out.println("----------------------------------");
//    System.out.println("RESULTS INITIALIZED TO: " + results);

    // List to store potential k shortest paths
    PriorityQueue<List<Edge>> potentialPaths =
      new PriorityQueue<>(new ShortPathComparator());

    // Parallel Set to ensure there are no duplicates
    Set<List<Edge>> potentialPathsSet = new HashSet<>();

    for (int kidx = 1; kidx < k; kidx++) {
//      System.out.println("FINDING PATH " + (kidx + 1));
      // Spur Node selection follows the last shortest path
      for (int i = 0; i < results.get(kidx - 1).size(); i++) {
//        System.out.println("LATEST PATH: " + results.get(kidx - 1));
        // Grab the current spur node from the last shortest path
        Node spurNode;
        if (i == 0) {
          spurNode = (Node) results.get(kidx - 1).get(0).getStart();
        } else {
          spurNode = (Node) results.get(kidx - 1).get(i - 1).getEnd();
        }

//        System.out.println("INDEX: " + i);
//        System.out.println("SPUR NODE: " + spurNode);

        // Create a temporary copy of the graph to modify and traverse through
        G currGraph = (G) graph.copy();
        GraphAlgorithms currAlgos = new GraphAlgorithms();

        // Get the Root Path up to but excluding the spur node to create deviations from
        List<Edge> rootPath = results.get(kidx - 1).stream().limit(i).collect(Collectors.toList());

//        System.out.println("ROOT PATH: " + rootPath);

        for (List<Edge> path : results) {
//          System.out.println("PATH: " + path);
          // If they have the same root path, nullify the edge that was explored before
          List<Edge> rootOfPath = path.stream().limit(i).collect(Collectors.toList());
          if (rootPath.equals(rootOfPath)) {
            String fromNodeID = path.get(i).getStart().getID();
            String toNodeID = path.get(i).getEnd().getID();
//            System.out.println("NULLIFYING EDGE " + currGraph.getEdgeSet().get(fromNodeID).get(toNodeID));
            currGraph.getEdgeSet().get(fromNodeID).get(toNodeID).setWeight(Double.POSITIVE_INFINITY);
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
//        System.out.println("SPUR PATH: " + spurPath);

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

      // Break if no spur path, spur paths are exhuasted, or reached dead end
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
   * A Function that creates a Short Path Tree from a starting Node and returns a ordered list
   *  containing all of the Short Paths to every other node in the same concentration (Graph)
   *  using Dijkstras. The list is ordered from shortest path to longest path!
   *
   * @param startID the start Node's ID
   * @param graph   the graph that the short path tree will be built from
   * @return a List of k List of Edges which represents the optimal paths from the start Node to
   * the end Node. The List will be empty if the start Node is the end Node.
   * Null will be returned if the end node isn't reachable by the start node.
   * @throws InvalidAlgorithmParameterException if either the start node or the end node is not
   *         in the graph.
   */
  public final List<List<Edge>> dijkstraPathTree(String startID, G graph)
    throws InvalidAlgorithmParameterException {

    HashMap<String, Node> nodeSet = graph.getNodeSet();
    HashMap<String, HashMap<String, Edge>> edgeSet = graph.getEdgeSet();

    if (!nodeSet.containsKey(startID)) {
      throw new InvalidAlgorithmParameterException("Nodes must exist in the graph");
    }

    // Create priority queue of (Node, distance)
    dijkstraSetup(startID, graph);
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
        for (Edge E : edgeSet.get(currID).values()) {

          // Get Neighbor and calculate score to neighbor from currrent node
          Node neighbor = (Node) E.getEnd();

          List<Edge> prevPath = new ArrayList<>(nodeSet.get(currID).getPreviousPath());
          double newWeight = currNode.getWeight() + E.getWeight();

          // Update hashMap and add to minHeap if neighbor can be reached betterly
          if (nodeSet.get(neighbor.getID()).getWeight() > newWeight) {
            nodeSet.get(neighbor.getID()).setWeight(newWeight);
            prevPath.add(E);
            nodeSet.get(neighbor.getID()).setPreviousPath(prevPath);
            minHeap.add(nodeSet.get(neighbor.getID()));
          }
        }
      }

      // Consider this node visited
      currNode.setVisited(true);
    }

    // Traceback and return the optimal path
    List<List<Edge>> results = new ArrayList<>();
    for (String nodeID : nodeSet.keySet()) {
      List<Edge> prevPath = nodeSet.get(nodeID).getPreviousPath();
      if (prevPath.size() > 0) {
        Edge startingEdge = prevPath.get(0);
        if (startingEdge.getStart().getID().equals(startID)) {
          results.add(nodeSet.get(nodeID).getPreviousPath());
        }
      }
    }

    Collections.sort(results, new ShortPathComparator());
    this.results.put(startID, results);

    // Otherwise just return the path
    return results;
  }
}