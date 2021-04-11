package edu.brown.cs.futureatbrown.termproject;

import edu.brown.cs.futureatbrown.termproject.course.CourseEdge;
import edu.brown.cs.futureatbrown.termproject.course.CourseGraph;
import edu.brown.cs.futureatbrown.termproject.course.CourseNode;
import edu.brown.cs.futureatbrown.termproject.course.Database;
import edu.brown.cs.futureatbrown.termproject.exception.SQLRuntimeException;
import edu.brown.cs.futureatbrown.termproject.graph.GraphAlgorithms;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.security.InvalidAlgorithmParameterException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.stream.Collectors;

public class CourseAlgorithmTest {
  private CourseGraph courseGraph;
  private CourseNode[] courseNodes;
  private GraphAlgorithms<CourseNode, CourseEdge, CourseGraph> courseAlgorithms;

  @Before
  public void setup() {
    try{
      Database.init("data/courseDatabase.sqlite3");
    } catch (SQLException throwables) {
      throwables.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }

    // Initialize the graph and Algorithms
    courseGraph = new CourseGraph();
    courseAlgorithms = new GraphAlgorithms<>();

    // Initialize all of the nodes
    CourseNode cs0150 = Database.getCourseNode("CSCI 0150");
    CourseNode cs0160 = Database.getCourseNode("CSCI 0160");
    CourseNode cs0170 = Database.getCourseNode("CSCI 0170");
    CourseNode cs0180 = Database.getCourseNode("CSCI 0180");
    CourseNode cs0190 = Database.getCourseNode("CSCI 0190");
    CourseNode cs0220 = Database.getCourseNode("CSCI 0220");
    CourseNode cs1410 = Database.getCourseNode("CSCI 1410");
    CourseNode cs1450 = Database.getCourseNode("CSCI 1450");
    CourseNode math0050 = Database.getCourseNode("MATH 0050");
    CourseNode math0060 = Database.getCourseNode("MATH 0060");
    CourseNode math0070 = Database.getCourseNode("MATH 0070");
    CourseNode math0090 = Database.getCourseNode("MATH 0090");
    CourseNode math0100 = Database.getCourseNode("MATH 0100");
    CourseNode math0190 = Database.getCourseNode("MATH 0190");
    CourseNode math0350 = Database.getCourseNode("MATH 0350");
    CourseNode apma1650 = Database.getCourseNode("APMA 1650");
    CourseNode apma1655 = Database.getCourseNode("APMA 1655");
    courseNodes = new CourseNode[]{
      cs0150, cs0160, cs0170, cs0180, cs0190, cs0220, cs1410, cs1450,
      math0050, math0060, math0070, math0090, math0100, math0190, math0350,
      apma1650, apma1655};

    // Add Nodes to the Graph
    for (CourseNode startNode : courseNodes) {
      courseGraph.addNode(startNode,
        new HashSet<>(Arrays.asList(courseNodes)
          .stream()
          .filter(node -> !node.equals(startNode))
          .map(node -> new CourseEdge(startNode.getID() + " - " + node.getID(), startNode, node))
          .collect(Collectors.toList())));
    }
  }



  @After
  public void teardown() {
    try{
      Database.closeDB();
    } catch (SQLRuntimeException throwables) {
      throwables.printStackTrace();
    }
    courseGraph = null;
    courseAlgorithms = null;
  }

  @Test
  public void PrereqTest() throws InvalidAlgorithmParameterException {
    setup();
    courseGraph.setGlobalParams(1, 1, 1, 1,
    10, 3, 5, 1, 1,
    100, 1, 50, 500);
    List<List<CourseEdge>> prereqPaths = courseAlgorithms.dijkstraPathTree("CSCI 0150" ,courseGraph);
    Assert.assertEquals(prereqPaths, new ArrayList<>());
    teardown();
  }
}
