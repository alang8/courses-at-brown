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
    10, 3, 5, 1, 1,
    100, 1, 50, 500);
    List<List<CourseEdge>> prereqPaths = courseAlgorithms.dijkstraPathTree("CSCI 0150" ,courseGraph);
    List<List<CourseEdge>> prereqPathSol = List.of(
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0350"),
        courseGraph.getEdgeSet().get("MATH 0350").get("APMA 1655")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0090")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0050")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0050"),
        courseGraph.getEdgeSet().get("MATH 0050").get("MATH 0060")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0160")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0070")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0170")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0170"),
        courseGraph.getEdgeSet().get("CSCI 0170").get("CSCI 0180")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0350")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0090"),
        courseGraph.getEdgeSet().get("MATH 0090").get("MATH 0100")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0160"),
        courseGraph.getEdgeSet().get("CSCI 0160").get("CSCI 0220"),
        courseGraph.getEdgeSet().get("CSCI 0220").get("CSCI 1410")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0220")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0350"),
        courseGraph.getEdgeSet().get("MATH 0350").get("APMA 1650")
      ),
      List.of(
        courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0090"),
        courseGraph.getEdgeSet().get("MATH 0090").get("MATH 0100"),
        courseGraph.getEdgeSet().get("MATH 0100").get("CSCI 1450")
      )
    );

    Assert.assertEquals(prereqPaths.get(0), prereqPathSol.get(0));
    Assert.assertEquals(prereqPaths.get(1), prereqPathSol.get(1));
    Assert.assertEquals(prereqPaths.get(2), prereqPathSol.get(2));
    Assert.assertEquals(prereqPaths.get(3), prereqPathSol.get(3));
    Assert.assertEquals(prereqPaths.get(4), prereqPathSol.get(4));
    Assert.assertEquals(prereqPaths.get(5), prereqPathSol.get(5));
    Assert.assertEquals(prereqPaths.get(6), prereqPathSol.get(6));
    Assert.assertEquals(prereqPaths.get(7), prereqPathSol.get(7));
    Assert.assertEquals(prereqPaths.get(8), prereqPathSol.get(8));
    Assert.assertEquals(prereqPaths.get(9), prereqPathSol.get(9));
    Assert.assertEquals(prereqPaths.get(10), prereqPathSol.get(10));
    Assert.assertEquals(prereqPaths.get(11), prereqPathSol.get(11));
    Assert.assertEquals(prereqPaths.get(12), prereqPathSol.get(12));
    Assert.assertEquals(prereqPaths.get(13), prereqPathSol.get(13));
    Assert.assertEquals(prereqPaths.get(14), prereqPathSol.get(14));
    Assert.assertEquals(prereqPaths.get(15), prereqPathSol.get(15));

    List<CourseEdge> pathToProbComp = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1450", courseGraph);
    Assert.assertEquals(pathToProbComp, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0090"),
      courseGraph.getEdgeSet().get("MATH 0090").get("MATH 0100"),
      courseGraph.getEdgeSet().get("MATH 0100").get("CSCI 1450")
      ));

    List<CourseEdge> pathToAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1410", courseGraph);
    Assert.assertEquals(pathToAI, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0160"),
      courseGraph.getEdgeSet().get("CSCI 0160").get("CSCI 0220"),
      courseGraph.getEdgeSet().get("CSCI 0220").get("CSCI 1410")
    ));

    teardown();
  }

  @Test
  public void SliderTests() throws InvalidAlgorithmParameterException {
    ///////////////////////////
    // MAX NUMBER OF CLASSES //
    ///////////////////////////
    setup();
    courseGraph.setGlobalParams(1, 1, 1,
      10, 1, 3, 1, 1,
      100, 1, 50, 500);

    // Notice that the Max number of classes is 3 classes but we need at least 4 to include the starting node and
    // satisfy the prerequisites of AI. Thus this path will return null.
    List<CourseEdge> blockedFromAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1410", courseGraph);
    Assert.assertNull(blockedFromAI);
    teardown();

    ////////////////////////////////
    // COURSE RATING - PRIORITIZE //
    ////////////////////////////////
    setup();
    courseGraph.setGlobalParams(10, 1, 1,
      10, 3, 5, 1, 1,
      100, 1, 50, 500);

    // Went from CS190 to CS160 because CS160 has a slightly higher course rating and the preference for course
    // rating is MAXED OUT.
    List<CourseEdge> highCourseRatingAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1450", courseGraph);
    Assert.assertEquals(highCourseRatingAI, List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0090"),
      courseGraph.getEdgeSet().get("MATH 0090").get("MATH 0100"),
      courseGraph.getEdgeSet().get("MATH 0100").get("CSCI 1450")
    ));
    teardown();

    ///////////////////////////////////
    // PROFESSOR RATING - PRIORITIZE //
    ///////////////////////////////////
    setup();
    courseGraph.setGlobalParams(1, 10, 1,
      10, 3, 5, 1, 1,
      100, 1, 50, 500);

    // TODO: FIX NULL CASE - Null should be average not 0
//    List<CourseEdge> highProfRatingAI = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 1450", courseGraph);
//    Assert.assertEquals(highProfRatingAI, List.of(
//      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0160"),
//      courseGraph.getEdgeSet().get("CSCI 0160").get("CSCI 0220"),
//      courseGraph.getEdgeSet().get("CSCI 0220").get("CSCI 1410")
//    ));
    teardown();
  }
}
