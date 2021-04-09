package edu.brown.cs.futureatbrown.termproject;

import edu.brown.cs.futureatbrown.termproject.course.CourseEdge;
import edu.brown.cs.futureatbrown.termproject.course.CourseGraph;
import edu.brown.cs.futureatbrown.termproject.course.CourseNode;
import edu.brown.cs.futureatbrown.termproject.graph.GraphAlgorithms;
import org.junit.Before;

public class CourseAlgorithmTest {
  private CourseGraph courseGraph;
  private CourseNode[] courseNodes;
  private GraphAlgorithms<CourseNode, CourseEdge, CourseGraph> courseAlgorithms;

  @Before
  public void setup() {
    // Initialize the graph and Algorithms
    courseGraph = new CourseGraph();
    courseAlgorithms = new GraphAlgorithms<>();



  }
}
