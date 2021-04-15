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
    courseGraph.setGlobalParams(1, 1, 1,
    10, 1, 5, 1,
    100, 1, 50, 500,
    null, null);
    List<List<CourseEdge>> prereqPaths = courseAlgorithms.dijkstraPathTree("CSCI 0150" ,courseGraph);

    Assert.assertEquals(prereqPaths.get(0), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      courseGraph.getEdgeSet().get("MATH 0190").get("APMA 1655")
    ));

    Assert.assertEquals(prereqPaths.get(1), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0090")
    ));

    Assert.assertEquals(prereqPaths.get(2), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0050")
    ));

    Assert.assertEquals(prereqPaths.get(3), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0050"),
      courseGraph.getEdgeSet().get("MATH 0050").get("MATH 0060")
    ));

    Assert.assertEquals(prereqPaths.get(4), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0160")
    ));

    Assert.assertEquals(prereqPaths.get(5), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0070")
    ));

    Assert.assertEquals(prereqPaths.get(6), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0170")
    ));

    Assert.assertEquals(prereqPaths.get(7), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190")
    ));

    Assert.assertEquals(prereqPaths.get(8), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190"),
      courseGraph.getEdgeSet().get("CSCI 0190").get("CSCI 0180")
    ));

    Assert.assertEquals(prereqPaths.get(9), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190")
    ));

    Assert.assertEquals(prereqPaths.get(10), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0350")
    ));

    Assert.assertEquals(prereqPaths.get(11), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0090"),
      courseGraph.getEdgeSet().get("MATH 0090").get("MATH 0100")
    ));

    Assert.assertEquals(prereqPaths.get(12),  List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190"),
      courseGraph.getEdgeSet().get("CSCI 0190").get("CSCI 0220"),
      courseGraph.getEdgeSet().get("CSCI 0220").get("CSCI 1410")
    ));

    Assert.assertEquals(prereqPaths.get(13), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0220")
    ));

    Assert.assertEquals(prereqPaths.get(14),  List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      courseGraph.getEdgeSet().get("MATH 0190").get("APMA 1650")
    ));
    Assert.assertEquals(prereqPaths.get(15), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      courseGraph.getEdgeSet().get("MATH 0190").get("MATH 0070"),
      courseGraph.getEdgeSet().get("MATH 0070").get("CSCI 1450")
    ));

    List<CourseEdge> pathToProbComp = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1450", courseGraph);
    Assert.assertEquals(pathToProbComp, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      courseGraph.getEdgeSet().get("MATH 0190").get("MATH 0070"),
      courseGraph.getEdgeSet().get("MATH 0070").get("CSCI 1450")
      ));

    List<CourseEdge> pathToAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1410", courseGraph);
    Assert.assertEquals(pathToAI, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190"),
      courseGraph.getEdgeSet().get("CSCI 0190").get("CSCI 0220"),
      courseGraph.getEdgeSet().get("CSCI 0220").get("CSCI 1410")
    ));

    teardown();
  }

  @Test
  public void SliderTests() throws InvalidAlgorithmParameterException {

    ////////////////////////////////
    // COURSE RATING - PRIORITIZE //
    ////////////////////////////////
    setup();
    courseGraph.setGlobalParams(10, 0, 0,
      10, 1, 5, 0,
      100, 0, 50, 500,
      null, null);

    // Went from CS190 to CS160 because CS160 has a slightly higher course rating and the preference for course
    // rating is MAXED OUT.
    List<CourseEdge> highCourseRatingAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1410", courseGraph);
    Assert.assertEquals(highCourseRatingAI, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0220"),
      courseGraph.getEdgeSet().get("CSCI 0220").get("CSCI 0160"),
      courseGraph.getEdgeSet().get("CSCI 0160").get("CSCI 1410")
    ));
    teardown();

    ///////////////////////////////////
    // PROFESSOR RATING - PRIORITIZE //
    ///////////////////////////////////
    setup();
    courseGraph.setGlobalParams(0, 10, 0,
      10, 1, 5, 0,
      100, 0, 50, 500,
      null, null);

    // Went from MATH0070 to MATH0090 because MATH0090 has a slightly higher course Professor rating
    // and the preference for Professor rating is MAXED OUT.
    // Not MATH0060 because of the prerequisite of MATH0050.
    List<CourseEdge> highProfRatingProbClass = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1450", courseGraph);
    Assert.assertEquals(highProfRatingProbClass, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      courseGraph.getEdgeSet().get("MATH 0190").get("MATH 0090"),
      courseGraph.getEdgeSet().get("MATH 0090").get("CSCI 1450")
    ));
    teardown();

    /////////////////////////////////
    // AVGERAGE HOURS - PRIORITIZE //
    /////////////////////////////////
    setup();
    courseGraph.setGlobalParams(0, 0, 10,
      10, 1, 5, 0,
      100, 0, 50, 500,
      null, null);

    // CS0190's AVG Hours are slightly closer to 10 than CS0160's
    List<CourseEdge> closeAVGHourProbClass = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1410", courseGraph);
    Assert.assertEquals(closeAVGHourProbClass, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190"),
      courseGraph.getEdgeSet().get("CSCI 0190").get("CSCI 0220"),
      courseGraph.getEdgeSet().get("CSCI 0220").get("CSCI 1410")
    ));
    teardown();

    //////////////////////////////
    // CLASS SIZE  - PRIORITIZE //
    //////////////////////////////
    setup();
    courseGraph.setGlobalParams(0, 0, 0,
      10, 1, 5, 0,
      100, 10, 50, 500,
      null, null);

    // CS0190 > CS0160 because it is much much closer to a class size of 50.
    List<CourseEdge> closeClassSizeAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1410", courseGraph);
    Assert.assertEquals(closeClassSizeAI, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190"),
      courseGraph.getEdgeSet().get("CSCI 0190").get("CSCI 0220"),
      courseGraph.getEdgeSet().get("CSCI 0220").get("CSCI 1410")
    ));
    teardown();
  }

  @Test
  public void LimiterTests() throws InvalidAlgorithmParameterException {
    ///////////////////////////
    // MAX NUMBER OF CLASSES //
    ///////////////////////////
    setup();
    courseGraph.setGlobalParams(1, 1, 1,
      10, 1, 3, 1,
      100, 1, 50, 500,
      null, null);

    // Notice that the Max number of classes is 3 classes but we need at least 4 to include the starting node and
    // satisfy the prerequisites of AI. Thus this path will return null.
    List<CourseEdge> blockedFromAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1410", courseGraph);
    Assert.assertNull(blockedFromAI);
    teardown();

    ///////////////////////////
    // MIN NUMBER OF CLASSES //
    ///////////////////////////
    setup();
    courseGraph.setGlobalParams(1, 1, 1,
      10, 3, 5, 1,
      100, 1, 50, 500,
      null, null);

    // Resultant path has at least 3 nodes now, more than before.
    List<CourseEdge> longerPath = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", courseGraph);
    Assert.assertEquals(longerPath, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190"),
      courseGraph.getEdgeSet().get("CSCI 0190").get("CSCI 0170")
    ));
    teardown();

    /////////////////////////
    // MAX NUMBER OF HOURS //
    /////////////////////////
    setup();
    courseGraph.setGlobalParams(1, 1, 1,
      10, 1, 5, 1,
      1, 1, 50, 500,
      null, null);

    // Since we're only allowing 1 Max Hour there is no possible way of taking the
    // 4 classes needed for AI, or even 1 class at all. Thus the path is blocked off.
    List<CourseEdge> notEnoughTimeAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1410", courseGraph);
    Assert.assertNull(notEnoughTimeAI);
    teardown();
  }

  @Test
  public void PathwayRequirementTests() throws InvalidAlgorithmParameterException, SQLException {
    setup();
    Database.setupGraph();
    CourseGraph finalGraph = Database.getGraph();
    finalGraph.setGlobalParams(1, 1, 1,
      10, 0, 18, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("csciABMLGroups"), Database.getCourseWays("csciABMLCourses"));

    List<CourseEdge> pathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", finalGraph);

    Assert.assertEquals(pathWithRequirements, List.of(
      finalGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190"),
      finalGraph.getEdgeSet().get("CSCI 0190").get("MATH 0190"),
      finalGraph.getEdgeSet().get("MATH 0190").get("CSCI 1430"),
      finalGraph.getEdgeSet().get("CSCI 1430").get("CSCI 1850"),
      finalGraph.getEdgeSet().get("CSCI 1850").get("CSCI 1951K"),
      finalGraph.getEdgeSet().get("CSCI 1951K").get("CSCI 0530"),
      finalGraph.getEdgeSet().get("CSCI 0530").get("CSCI 0320"),
      finalGraph.getEdgeSet().get("CSCI 0320").get("CSCI 1310"),
      finalGraph.getEdgeSet().get("CSCI 1310").get("CSCI 1250"),
      finalGraph.getEdgeSet().get("CSCI 1250").get("CSCI 0170")
    ));
    teardown();
  }

}
