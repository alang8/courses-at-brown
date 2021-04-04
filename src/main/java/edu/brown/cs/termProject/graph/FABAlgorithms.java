package edu.brown.cs.termProject.graph;

/**
 * A class which contains all the short-path algorithms and short-path data of the inputted graph
 * for the Future @ Brown (FAB) app
 */
  public final class FABAlgorithms<Node extends GraphNode, Edge extends GraphEdge> {
  //TODO: Replace all Graphs, GraphNodes, and GraphEdges with Course and Pathways
  //TODO: Node needs to have penalty scorer function [Takes into account prereqs here]
  //private Graph graph;
  private List<List<Edge>> results;

  // ------------------------------------ Constructors -------------------------------------

//  /**
//   * The constructor for this class.
//   *
//   * @param graph the graph that the algorithms will run on.
//   */
//  public FABAlgorithms(Graph graph) {
//    if (graph == null) {
//      throw new IllegalArgumentException(
//        "The underlying graph cannot be null");
//    }
//    this.graph = graph;
//  }
  // ------------------------------------- Comparators ------------------------------------
  class ShortPathComparator implements Comparator<List<Edge>> {
    @Override
    public int compare(List<Edge> path1, List<Edge> path2) {
      path1Cost = path1.stream().map(edge -> edge.getWeight()).sum();
      path2Cost = path2.stream().map(edge -> edge.getWeight()).sum();
      // We want an ascending order comparator
      if (path1Cost > path2Cost) {
        return 1;
      } else if (path1Cost < path2Cost) {
        return -1;
      } else {
        return 1;
      }
    }
  }

  // ----------------------------- Graph Short Path Algorithms ----------------------------

  /**
   * A helper function which initializes/reinitializes the nodes in the current graph to
   * the initial conditions needed for Dijkstra's algorithm. Assumes that start is in nodeSet
   */
  private void dijkstraSetup(String startID, Graph graph) {
    for (Node n : graph.getNodeSet().keySet()) {
      n.setWeight(Double.POSITIVE_INFINITY);
      n.setPrevious(null);
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
   * @throws a Invalid Arguement Exception if either the start node or the end node is not
   *           in the graph.
   */
  public final List<Edge> dijkstraPath(String startID, String endID, Graph graph)
    throws InvalidArguementException {
      if (null == this.results) {
        dijkstraPathTree(startID, graph);
      }
      for (List<Edge> path : this.results) {
        if (path.get(path.length - 1).getEnd().getID().equals(endID)) {
          return path;
        }
      }
  }

  /**
   * A function which uses an yen's algorithm to find the k 'shortest' paths,
   * represented as an ordered List of paths (from best to worst) where each path
   * is an ordered List of Edges, from the start Node to the end Node.
   *
   * @param startID the start Node's ID
   * @param endiD   the end Node's ID
   * @param k     the number of paths to return
   * @return a List of k List of Edges which represents the optimal paths from the start Node to
   * the end Node. The List will be empty if the start Node is the end Node.
   * Null will be returned if the end node isn't reachable by the start node.
   * @throws a Invalid Arguement Exception if either the start node or the end node is not
   *           in the graph.
   */
   public final List<List<Edge>> yenPaths(Node startID, Node endID, Graph graph, int k)
    throws InvalidArguementException {
     // List to store the k shortest path results
     List<List<Edge>> results = new ArrayList<>(k);
     Graph graphCopy = graph.copy();

     // Prepare the graph for dijkstras
     dijkstraSetup(startID, graphCopy);
     // Determine the shortest path from the start to the end.
     results.set(0, dijkstraPath(startID, endID, graphCopy));

     // List to store potential k shortest paths
     PriorityQueue<List<Edge>> potentialPaths =
      new PriorityQueue<List<Edge>>(new ShortPathComparator())

      // Parallel Set to ensure there are no duplicates
      Set<List<Edge>> potentialPathsSet = new HashSet<List<Edge>>();

     for (int kidx = 1; kidx < k; k++) {

       // Spur Node selection follows the last shortest path
       for (int i = 0; i < results.get(kidx - 1).length - 1; i++) {

         // Grab the current spur node from the last shortest path
         Node spurNode;
         if (i == 0) {
           spurNode = results.get(kidx - 1).get(0).getStart();
         } else {
           spurNode = results.get(kidx - 1).get(i - 1).getEnd();
         }

         // Create a temporary copy of the graph to modify and traverse through
         Graph currGraph = graph.copy()
         // Prepare the graph for dijkstras
         dijkstraSetup(start, currGraph);

         // Get the Root Path up to but excluding the spur node to create deviations from
         List<Edge> rootPath = results.get(kidx - 1).stream().limit(i).collect(Collectors.toList());
         for (List<Edge> path : results) {
          // If they have the same root path, nullify the edge that was explored before
          List<Edge> rootOfPath = path.stream().limit(i).collect(Collectors.toList()));
          if (rootPath.equals(rootOfPath)) {
            String fromNodeID = path.get(i).getStart().getID();
            String toNodeID = path.get(i).getEnd().getID();
            currGraph.getEdgeSet().get(fromNodeID).get(toNodeID).setWeight(Double.POSITIVE_INFINITY);
          }
         }

         // Make it so that the Root Path has all been visited before
         currGraph.getNodeSet().get(rootPath.get(0).getStart().getID()).setVisited(true);
         for (Edge e : rootPath) {
           currGraph.getNodeSet().get(e.getEnd().getID()).setVisited(true);
         }

         // Calculate path from Spur Node to end with Dijkstras
         List<Edge> spurPath = dijkstraPath(spurNode.getID(), endID, currGraph);

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
       results.set(kidx, newPath);
     }
     return results;
   }

  /**
   * A Function that creates a Short Path Tree from a starting Node and returns a ordered list
   *  containing all of the Short Paths to every other node in the same concentration (Graph)
   *  using Dijkstras. The list is ordered from shortest path to longest path!
   *
   * @param startID the start Node's ID
   * @param endiD   the end Node's ID
   * @param k     the number of paths to return
   * @return a List of k List of Edges which represents the optimal paths from the start Node to
   * the end Node. The List will be empty if the start Node is the end Node.
   * Null will be returned if the end node isn't reachable by the start node.
   * @throws a Invalid Arguement Exception if either the start node or the end node is not
   *         in the graph.
   */
  public final List<List<Edge>> dijkstraPathTree(String startID, Graph graph)
    throws InvalidArguementException {
    HashMap<String, Node> nodeSet = graph.getNodeSet();
    HashMap<Node, Set<Edge>> edgeSet = graph.getNodeSet();
    if (!nodeSet.containsKey(startID)) {
      throw new InvalidArguementException("Nodes must exist in the graph");
    }

    // Create priority queue of (Node, distance)
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
          Node neighbor = E.getEnd();
          List<Edge> prevPath = nodeSet.get(neighbor.getID()).getPrevious()
          double newWeight = currNode.getWeight() + E.getWeight() + E.getEnd().penalty()

          // Update hashMap and add to minHeap if neighbor can be reached betterly
          if (nodeSet.get(neighbor.getID()).getWeight() > newWeight) {
            nodeSet.get(neighbor.getID()).setWeight(newWeight);
            prevPath.add(E);
            nodeSet.get(neighbor.getID()).setPrevious(prevPath);
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
     if (!nodeSet.get(nodeID).getPreviousPath().get(0).getStart().getID().equals(startID)) {
       results.add(nodeSet.get(nodeID).getPreviousPath());
     }
    }

    Collections.sort(results, new ShortPathComparator());
    this.results = results;
    // Otherwise just return the path
    return results;
  }

  // BELLMAN FORD'S ALGORITHM

}