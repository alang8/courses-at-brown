package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.exception.SQLRuntimeException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.*;
import java.util.stream.Collectors;

/**
 * The class that instantiates connections to a course data database and makes queries.
 */
public final class Database {
  private static Connection conn = null;
  private static final CourseGraph COURSE_GRAPH = new CourseGraph();
  private static final List<String> ALLOWED_GROUPS =
      List.of("apmaABGroups", "csciABMLGroups", "mathABGroups", "mathSCBGroups");
  private static final List<String> ALLOWED_COURSES =
      List.of("apmaABCourses", "csciABMLCourses", "mathABCourses", "mathSCBCourses");

  /**
   * Constructor that instantiates a new Database object.
   */
  public Database() {
  }

  /**
   * Constructor that instantiates the database and creates tables.
   *
   * @param filepath file name of SQLite3 database to open
   * @throws ClassNotFoundException if the driver manager class is not found
   * @throws SQLException if an error occurs in any SQL query
   */
  public static void init(String filepath) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filepath;
    conn = DriverManager.getConnection(urlToDB);
  }

  /**
   * Returns whether this database has a connection.
   *
   * @return true if this database has been initialized, false otherwise
   */
  public static boolean isReady() {
    return conn != null;
  }

  /**
   * Queries a CourseNode based on the user inputted id. Returns null if not found.
   *
   * @param id the id of the CourseNode
   * @return the found CourseNode
   */
  public static CourseNode getCourseNode(String id) {
    try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM courseCR JOIN "
        + "courseData ON courseCR.id = ? AND courseCR.id = courseData.id")) {
      statement.setString(1, id);
      try (ResultSet results = statement.executeQuery()) {
        if (null == results || results.isClosed()) {
          return null;
        }
        return CourseConversions.resultToCourseNode(results);
      }
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
  }

  /**
   * Queries all CourseNodes.
   *
   * @return an iterator for all CourseNodes in the database
   */
  private static Iterator<CourseNode> iterateAllCourseNodes() throws SQLException {
    Statement query = conn.createStatement();
    ResultSet res = query.executeQuery("SELECT * FROM courseCR JOIN courseData "
        + "ON courseCR.id = courseData.id");
    return CourseConversions.iterateResults(res, CourseConversions::resultToCourseNode);
  }

  /**
   * Sets up a k-complete Graph connecting all of the CourseNodes in the database.
   *
   * @param prevCoursesID a list of the previous courses ids
   * @throws SQLException if an error occurs in any SQL query
   */
   public static void setupGraph(List<String> prevCoursesID, double sensitivity) throws SQLException {
     List<CourseNode> courseNodes = new ArrayList<>();
     List<CourseNode> prevCourses = new ArrayList<>();

     for (Iterator<CourseNode> it = iterateAllCourseNodes(); it.hasNext(); ) {
       CourseNode node = it.next();
       courseNodes.add(node);
       if (prevCoursesID.contains(node.getID())) {
         prevCourses.add(node);
         node.setVisited(true);
       }
     }

     // Add Nodes to the Graph
     for (CourseNode startNode : courseNodes) {
       List<CourseEdge> edgesFromStartNode = courseNodes
           .stream()
           .filter(node -> !node.equals(startNode))
           .map(node -> new CourseEdge(startNode.getID() + " - " + node.getID(), startNode, node))
           .collect(Collectors.toList());
       for (CourseEdge edge : edgesFromStartNode) {
         edge.setSensitivity(sensitivity);
       }
       COURSE_GRAPH.addNode(startNode,
         new HashSet<>(edgesFromStartNode));
       startNode.setPreviousCourses(prevCourses);
     }
   }


  /**
   * Returns the Graph that was set up from the above method.
   *
   * @return the Graph from the CourseNodes in the database
   */
  public static CourseGraph getGraph() {
    return COURSE_GRAPH;
  }

  /**
   * Queries a HashMap of a Pathway groups based on the user inputted id. Returns null if not found.
   *
   * @param id the id of the Pathway
   * @return the found HashMap of groups
   */
  public static HashMap<String, Integer> getGroups(String id) {
    if (ALLOWED_GROUPS.contains(id))  {
      try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + id)) {
        try (ResultSet results = statement.executeQuery()) {
          if (results.isClosed()) {
            return null;
          }
          return CourseConversions.resultToGroupMap(results);
        }
      } catch (SQLException e) {
        throw new SQLRuntimeException(e);
      }
    } else {
      throw new IllegalArgumentException("Invalid Groups Table! Must be one of " + ALLOWED_GROUPS);
    }
  }

  /**
   * Queries a HashMap of CourseWays based on the user inputted id. Returns null if not found.
   *
   * @param id the id of the Pathway
   * @return the found HashMap of CourseWays of form <GroupID, <CourseID, CourseWay>>>
   */
  public static Map<Integer, HashMap<String, CourseWay>> getCourseWays(String id) {
    if (ALLOWED_COURSES.contains(id)) {
      try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM " + id)) {
        try (ResultSet results = statement.executeQuery()) {
          if (results.isClosed()) {
            return null;
          }
          return CourseConversions.resultToCourseWayGroup(results, COURSE_GRAPH);
        }
      } catch (SQLException e) {
        throw new SQLRuntimeException(e);
      }
    } else {
      throw new IllegalArgumentException("Invalid Courses! Must be one of " + ALLOWED_COURSES);
    }

  }

  /**
   * Closes the connection to the database.
   */
  public static void closeDB() {
    try {
      conn.close();
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
  }

  // Can't query or interact with CourseEdges in this class because they aren't in the database.
  // However, can interact with CourseWays because they are in the database.
}
