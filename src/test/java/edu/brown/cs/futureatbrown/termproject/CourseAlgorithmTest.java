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
  private GraphAlgorithms<CourseNode, CourseEdge, CourseGraph> courseAlgorithms;

  @Before
  public void setup() {
    try{
      Database.init("data/courseDatabase.sqlite3");
    } catch (SQLException throwables) {
      System.out.println("ERROR: SQL EXCEPTION - " + throwables.getMessage());
    } catch (ClassNotFoundException e) {
      System.out.println("ERROR: CLASS NOT FOUND - " + e.getMessage());
    }

    // Initialize the graph and Algorithms
    courseGraph = new CourseGraph();
    courseAlgorithms = new GraphAlgorithms<>();
    CourseNode[] courseNodes;

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
        new HashSet<>(Arrays.stream(courseNodes)
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
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0070")
    ));

    Assert.assertEquals(prereqPaths.get(1), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      courseGraph.getEdgeSet().get("MATH 0190").get("APMA 1655")
    ));

    Assert.assertEquals(prereqPaths.get(2), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0090")
    ));

    Assert.assertEquals(prereqPaths.get(3), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0050")
    ));

    Assert.assertEquals(prereqPaths.get(4), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("MATH 0050"),
      courseGraph.getEdgeSet().get("MATH 0050").get("MATH 0060")
    ));

    Assert.assertEquals(prereqPaths.get(5), List.of(
      courseGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0160")
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
      courseGraph.getEdgeSet().get("CSCI 0190").get("MATH 0190"),
      courseGraph.getEdgeSet().get("MATH 0190").get("APMA 1655"),
      courseGraph.getEdgeSet().get("APMA 1655").get("CSCI 1410")
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
    Database.setupGraph(new ArrayList<>(),2);
    CourseGraph finalCSCIABMLGraph = Database.getGraph();
    finalCSCIABMLGraph.setGlobalParams(1, 1, 1,
      10, 0, 30, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("csciABMLGroups"), Database.getCourseWays("csciABMLCourses"));

    List<CourseEdge> csciABMLPathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", finalCSCIABMLGraph);

    Assert.assertEquals(csciABMLPathWithRequirements, List.of(
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 0150").get("CSCI 0190"),
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 0190").get("MATH 0190"),
      finalCSCIABMLGraph.getEdgeSet().get("MATH 0190").get("CSCI 1430"),
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 1430").get("CSCI 1951C"),
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 1951C").get("CSCI 1951A"),
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 1951A").get("CSCI 0530"),
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 0530").get("CSCI 0320"),
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 0320").get("CSCI 1310"),
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 1310").get("CSCI 1250"),
      finalCSCIABMLGraph.getEdgeSet().get("CSCI 1250").get("CSCI 0170")
    ));
    teardown();

    setup();
    Database.setupGraph(new ArrayList<>(), 2);
    CourseGraph finalMATHSCBGraph = Database.getGraph();
    finalMATHSCBGraph.setGlobalParams(1, 1, 1,
      10, 0, 18, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("mathSCBGroups"), Database.getCourseWays("mathSCBCourses"));

    List<CourseEdge> mathSCBPathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", finalMATHSCBGraph);

    Assert.assertEquals(mathSCBPathWithRequirements, List.of(
      finalMATHSCBGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 0190").get("MATH 0540"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 0540").get("MATH 0200"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 0200").get("MATH 1010"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1010").get("MATH 1130"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1130").get("MATH 1140"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1140").get("MATH 1530"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1530").get("MATH 1540"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1540").get("MATH 1110"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1110").get("MATH 1610"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1610").get("MATH 1580"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1580").get("MATH 1040"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1040").get("MATH 1120"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1120").get("MATH 1410"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1410").get("MATH 1230"),
      finalMATHSCBGraph.getEdgeSet().get("MATH 1230").get("CSCI 0170")
    ));
    teardown();

    setup();
    Database.setupGraph(new ArrayList<>(), 2);
    CourseGraph finalMATHABGraph = Database.getGraph();
    finalMATHABGraph.setGlobalParams(1, 1, 1,
      10, 0, 18, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("mathABGroups"), Database.getCourseWays("mathABCourses"));

    List<CourseEdge> mathABPathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", finalMATHABGraph);

    Assert.assertEquals(mathABPathWithRequirements, List.of(
      finalMATHABGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      finalMATHABGraph.getEdgeSet().get("MATH 0190").get("MATH 0540"),
      finalMATHABGraph.getEdgeSet().get("MATH 0540").get("MATH 1530"),
      finalMATHABGraph.getEdgeSet().get("MATH 1530").get("MATH 1410"),
      finalMATHABGraph.getEdgeSet().get("MATH 1410").get("MATH 1120"),
      finalMATHABGraph.getEdgeSet().get("MATH 1120").get("MATH 1970"),
      finalMATHABGraph.getEdgeSet().get("MATH 1970").get("MATH 1580"),
      finalMATHABGraph.getEdgeSet().get("MATH 1580").get("MATH 1540"),
      finalMATHABGraph.getEdgeSet().get("MATH 1540").get("CSCI 0170")
    ));
    teardown();

    setup();
    Database.setupGraph(new ArrayList<>(), 2);
    CourseGraph finalAPMAABGraph = Database.getGraph();
    finalAPMAABGraph.setGlobalParams(1, 1, 1,
      10, 0, 18, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("apmaABGroups"), Database.getCourseWays("apmaABCourses"));

    List<CourseEdge> apmaABPathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", finalAPMAABGraph);

    Assert.assertEquals(apmaABPathWithRequirements, List.of(
      finalAPMAABGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      finalAPMAABGraph.getEdgeSet().get("MATH 0190").get("MATH 0540"),
      finalAPMAABGraph.getEdgeSet().get("MATH 0540").get("APMA 0350"),
      finalAPMAABGraph.getEdgeSet().get("APMA 0350").get("APMA 0360"),
      finalAPMAABGraph.getEdgeSet().get("APMA 0360").get("APMA 1360"),
      finalAPMAABGraph.getEdgeSet().get("APMA 1360").get("APMA 1655"),
      finalAPMAABGraph.getEdgeSet().get("APMA 1655").get("APMA 1330"),
      finalAPMAABGraph.getEdgeSet().get("APMA 1330").get("APMA 1740"),
      finalAPMAABGraph.getEdgeSet().get("APMA 1740").get("APMA 1660"),
      finalAPMAABGraph.getEdgeSet().get("APMA 1660").get("CSCI 0170")
    ));
    teardown();


    // PRESET COURSES
    setup();
    Database.setupGraph(List.of("CSCI 0150", "CSCI 0160"), 2);
    CourseGraph presetGraph = Database.getGraph();
    presetGraph.setGlobalParams(1, 1, 1,
      10, 0, 18, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("csciABMLGroups"), Database.getCourseWays("csciABMLCourses"));

    List<CourseEdge> presetPathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", presetGraph);

    Assert.assertEquals(presetPathWithRequirements, List.of(
      presetGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      presetGraph.getEdgeSet().get("MATH 0190").get("CSCI 1430"),
      presetGraph.getEdgeSet().get("CSCI 1430").get("CSCI 1951C"),
      presetGraph.getEdgeSet().get("CSCI 1951C").get("CSCI 1951A"),
      presetGraph.getEdgeSet().get("CSCI 1951A").get("CSCI 0530"),
      presetGraph.getEdgeSet().get("CSCI 0530").get("CSCI 0220"),
      presetGraph.getEdgeSet().get("CSCI 0220").get("CSCI 1310"),
      presetGraph.getEdgeSet().get("CSCI 1310").get("CSCI 1250"),
      presetGraph.getEdgeSet().get("CSCI 1250").get("CSCI 0170")
    ));
    teardown();

    setup();
    Database.setupGraph(List.of("MATH 0190", "MATH 1110", "MATH 1410"), 2);
    CourseGraph presetfinalMATHSCBGraph = Database.getGraph();
    presetfinalMATHSCBGraph.setGlobalParams(1, 1, 1,
      10, 0, 18, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("mathSCBGroups"), Database.getCourseWays("mathSCBCourses"));

    List<CourseEdge> presetmathSCBPathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", presetfinalMATHSCBGraph);

    Assert.assertEquals(presetmathSCBPathWithRequirements, List.of(
      presetfinalMATHSCBGraph.getEdgeSet().get("CSCI 0150").get("MATH 0540"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 0540").get("MATH 0200"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 0200").get("MATH 1010"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1010").get("MATH 1130"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1130").get("MATH 1140"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1140").get("MATH 1530"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1530").get("MATH 1540"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1540").get("MATH 1580"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1580").get("MATH 1610"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1610").get("MATH 1040"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1040").get("MATH 1620"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1620").get("MATH 1120"),
      presetfinalMATHSCBGraph.getEdgeSet().get("MATH 1120").get("CSCI 0170")
    ));
    teardown();

    setup();
    Database.setupGraph(List.of("MATH 0180"), 2);
    CourseGraph presetfinalMATHABGraph = Database.getGraph();
    presetfinalMATHABGraph.setGlobalParams(1, 1, 1,
      10, 0, 18, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("mathABGroups"), Database.getCourseWays("mathABCourses"));

    List<CourseEdge> presetmathABPathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", presetfinalMATHABGraph);

    Assert.assertEquals(presetmathABPathWithRequirements, List.of(
      presetfinalMATHABGraph.getEdgeSet().get("CSCI 0150").get("MATH 0540"),
      presetfinalMATHABGraph.getEdgeSet().get("MATH 0540").get("MATH 1530"),
      presetfinalMATHABGraph.getEdgeSet().get("MATH 1530").get("MATH 1410"),
      presetfinalMATHABGraph.getEdgeSet().get("MATH 1410").get("MATH 1010"),
      presetfinalMATHABGraph.getEdgeSet().get("MATH 1010").get("MATH 1230"),
      presetfinalMATHABGraph.getEdgeSet().get("MATH 1230").get("MATH 1120"),
      presetfinalMATHABGraph.getEdgeSet().get("MATH 1120").get("MATH 1580"),
      presetfinalMATHABGraph.getEdgeSet().get("MATH 1580").get("CSCI 0170")
    ));
    teardown();

    setup();
    Database.setupGraph(List.of("APMA 1330", "APMA 1650"), 2);
    CourseGraph presetfinalAPMAABGraph = Database.getGraph();
    presetfinalAPMAABGraph.setGlobalParams(1, 1, 1,
      10, 0, 18, 1,
      Double.POSITIVE_INFINITY, 1, 50, 500,
      Database.getGroups("apmaABGroups"), Database.getCourseWays("apmaABCourses"));

    List<CourseEdge> presetapmaABPathWithRequirements = courseAlgorithms.dijkstraPath("CSCI 0150", "CSCI 0170", presetfinalAPMAABGraph);

    Assert.assertEquals(presetapmaABPathWithRequirements, List.of(
      presetfinalAPMAABGraph.getEdgeSet().get("CSCI 0150").get("MATH 0190"),
      presetfinalAPMAABGraph.getEdgeSet().get("MATH 0190").get("MATH 0540"),
      presetfinalAPMAABGraph.getEdgeSet().get("MATH 0540").get("APMA 0350"),
      presetfinalAPMAABGraph.getEdgeSet().get("APMA 0350").get("APMA 0360"),
      presetfinalAPMAABGraph.getEdgeSet().get("APMA 0360").get("APMA 1360"),
      presetfinalAPMAABGraph.getEdgeSet().get("APMA 1360").get("APMA 1655"),
      presetfinalAPMAABGraph.getEdgeSet().get("APMA 1655").get("APMA 1740"),
      presetfinalAPMAABGraph.getEdgeSet().get("APMA 1740").get("CSCI 0170")
    ));
    teardown();
  }
}
