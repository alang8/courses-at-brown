package edu.brown.cs.futureatbrown.termproject.course;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The class that instantiates connections to a course data database and makes queries.
 */
public final class Database {
  private static Connection conn = null;

  private Database() {
  }

  /**
   * Constructor that instantiates the database and creates tables.
   *
   * @param filename File name of SQLite3 database to open.
   * @throws ClassNotFoundException If the driver manager class is not found.
   * @throws SQLException If an error occurs in any SQL query.
   */
  public static void init(String filename) throws ClassNotFoundException, SQLException {
    Class.forName("org.sqlite.JDBC");
    String urlToDB = "jdbc:sqlite:" + filename;
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
   * Queries a CourseNode based on the user inputted id.
   *
   * @param id The id of the CourseNode.
   * @return The found CourseNode.
   */
  public static CourseNode getCourseNode(String id) {
    try (PreparedStatement statement = conn
        .prepareStatement("SELECT courseCR.course_rating, courseCR.prof_rating, " +
            "courseCR.avg_hours, courseCR.max_hours, courseCR.class_size, courseData.name, " +
            "courseData.instr, courseData.sem, courseData.rawprereq, courseData.prereq," +
            "courseData.description FROM courseCR JOIN courseData WHERE id = ?")) {
      statement.setString(1, id);
      try (ResultSet results = statement.executeQuery()) {
        if (results.isClosed()) {
          return null;
        }
        return CourseConversions.resultToCourseNode(results);
      }
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
  }

  /**
   * Queries a CourseNode based on the user inputted id.
   *
   * @param id The id of the CourseNode.
   * @return The found CourseNode.
   */
  public static CourseNode getCourseNodeUnchecked(String id) {
    try (PreparedStatement statement = conn
        .prepareStatement("SELECT courseCR.course_rating, courseCR.prof_rating, " +
            "courseCR.avg_hours, courseCR.max_hours, courseCR.class_size, courseData.name, " +
            "courseData.instr, courseData.sem, courseData.rawprereq, courseData.prereq," +
            "courseData.description FROM courseCR JOIN courseData WHERE id = ?")) {
      statement.setString(1, id);
      try (ResultSet results = statement.executeQuery()) {
        if (results.isClosed()) {
          throw new NoSuchElementException();
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
   * @return An iterator for all CourseNodes in the database.
   */
  private static Iterator<CourseNode> iterateAllCourseNodes() throws SQLException {
    Statement query = conn.createStatement();
    ResultSet res = query.executeQuery("SELECT courseCR.id, courseCR.course_rating, " +
        "courseCR.prof_rating, courseCR.avg_hours, courseCR.max_hours, courseCR.class_size, " +
        "courseData.name, courseData.instr, courseData.sem, courseData.rawprereq, " +
        "courseData.prereq, courseData.description FROM courseCR JOIN courseData");
    return CourseConversions.iterateResults(res, CourseConversions::resultToCourseNode);
  }

  // Can't really query for CourseEdges because they aren't in the database
}
