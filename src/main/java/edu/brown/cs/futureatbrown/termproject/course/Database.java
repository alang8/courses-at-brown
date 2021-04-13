package edu.brown.cs.futureatbrown.termproject.course;

import edu.brown.cs.futureatbrown.termproject.exception.SQLRuntimeException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * The class that instantiates connections to a course data database and makes queries.
 */
public final class Database {
  private static Connection conn = null;

  private Database() {}

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
    try (PreparedStatement statement = conn
        .prepareStatement("SELECT * FROM courseCR JOIN courseData ON courseCR.id = ? AND courseCR.id = courseData.id")) {
      statement.setString(1, id);
      try (ResultSet results = statement.executeQuery()) {
        if (results.isClosed() || null == results) {
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
    ResultSet res = query.executeQuery("SELECT * FROM courseCR JOIN courseData ON courseCR.id = courseData.id");
    return CourseConversions.iterateResults(res, CourseConversions::resultToCourseNode);
  }

  /**
   * Queries a HashMap of a Pathway groups based on the user inputted id. Returns null if not found.
   *
   * @param id the id of the Pathway
   * @return the found HashMap of groups
   */
  public static HashMap<String, Integer> getGroups(String id) {
    try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM ?")) {
      statement.setString(1, id);
      try (ResultSet results = statement.executeQuery()) {
        if (results.isClosed()) {
          return null;
        }
        return CourseConversions.resultToGroupMap(results);
      }
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
  }

  /**
   * Queries a HashMap of CourseWays based on the user inputted id. Returns null if not found.
   *
   * @param id the id of the Pathway
   * @return the found HashMap of CourseWays
   */
  public static HashMap<String, CourseWay> get(String id) {
    try (PreparedStatement statement = conn.prepareStatement("SELECT * FROM ?")) {
      statement.setString(1, id);
      try (ResultSet results = statement.executeQuery()) {
        if (results.isClosed()) {
          return null;
        }
        return CourseConversions.resultToCourseWayMap(results);
      }
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
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
