package edu.brown.cs.futureatbrown.termproject.course;

import com.google.common.collect.Iterators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.*;

import static edu.brown.cs.futureatbrown.termproject.exception.SQLRuntimeException.unwrap;
import static edu.brown.cs.futureatbrown.termproject.exception.SQLRuntimeException.wrap;

/**
 * Converts CourseNode and CourseEdge data from results.
 */
public final class CourseConversions {

  private CourseConversions() {
  }

  /**
   * Converts the first result to a CourseNode. Does not advance the ResultSet's row.
   *
   * @param results a ResultSet with CourseNode data
   * @return the CourseNode represented at the result's current row, or null if there are none left
   * @throws SQLException if the ResultSet's data cannot be queried
   */
  public static CourseNode resultToCourseNode(ResultSet results) throws SQLException {
    if (results.isClosed()) {
      throw new SQLException("Closed results");
    }
    String id = results.getString("id");
    if (results.wasNull()) {
      id = null;
    }
    String name = results.getString("name");
    if (results.wasNull()) {
      name = null;
    }
    String instr = results.getString("instr");
    if (results.wasNull()) {
      instr = null;
    }
    Integer sem;
    String rawprereq = results.getString("rawprereq");
    if (results.wasNull()) {
      rawprereq = null;
    }
    String prereq = results.getString("prereq");
    if (results.wasNull()) {
      prereq = null;
    }
    String description = results.getString("description");
    if (results.wasNull()) {
      description = null;
    }
    Double courseRating;
    Double profRating;
    Double avgHours;
    Double maxHours;
    Integer classSize;
    try {
      sem = results.getInt("sem");
      if (results.wasNull()) {
        sem = null;
      }
      courseRating = results.getDouble("course_rating");
      if (results.wasNull()) {
        courseRating = null;
      }
      profRating = results.getDouble("prof_rating");
      if (results.wasNull()) {
        profRating = null;
      }
      avgHours = results.getDouble("avg_hours");
      if (results.wasNull()) {
        avgHours = null;
      }
      maxHours = results.getDouble("max_hours");
      if (results.wasNull()) {
        maxHours = null;
      }
      classSize = results.getInt("class_size");
      if (results.wasNull()) {
        classSize = null;
      }
    } catch (NumberFormatException e) {
      throw new SQLException("Invalid number format");
    }
    return new CourseNode(id, name, instr, sem, rawprereq, prereq, description, courseRating,
        profRating, avgHours, maxHours, classSize);
  }

  /**
   * Returns an iterator to query all rows of a ResultSet.
   *
   * @param results a ResultSet with a query
   * @param converter a function to convert a single result to a Java object
   * @param <T> the type of object represented by each row
   * @return an iterator through the converted results
   */
  public static <T> Iterator<T> iterateResults(ResultSet results,
                                               SQLFunction<ResultSet, T> converter) {
    return new Iterator<>() {
      @Override
      public boolean hasNext() {
        return wrap(() -> !results.isClosed());
      }
      @Override
      public T next() throws NoSuchElementException {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        return wrap(() -> {
          T t = converter.apply(results);
          results.next();
          return t;
        });
      }
    };
  }

  /**
   * Converts the ResultSet to a HashMap of groups.
   *
   * @param results a ResultSet with group data
   * @return the converted ResultSet
   * @throws SQLException if the ResultSet's data cannot be queried
   */
  public static HashMap<String, Integer> resultToGroupMap(ResultSet results)
      throws SQLException {
    if (results.isClosed()) {
      throw new SQLException("Closed results");
    }
    HashMap<String, Integer> groups = new HashMap<>();
    try {
      while (results.next()) {
        groups.put(results.getString("id"),
            Integer.parseInt(results.getString("num_courses")));
      }
    } catch (NumberFormatException e) {
      throw new SQLException("Invalid number format");
    }
    return groups;
  }

  /**
   * Converts the ResultSet to a HashMap of CourseWays.
   *
   * @param results a ResultSet with CourseWay data
   * @return the converted ResultSet
   * @throws SQLException if the ResultSet's data cannot be queried
   */
  public static HashMap<String, CourseWay> resultToCourseWayMap(ResultSet results, CourseGraph graph)
      throws SQLException {
    if (results.isClosed()) {
      throw new SQLException("Closed results");
    }
    HashMap<String, CourseWay> courseWays = new HashMap<>();
    try {
      while (results.next()) {
        String id = results.getString("id");
        String sequenceString = results.getString("sequence");
        Set<String> sequence;
        if (null == sequenceString) {
          sequence = new HashSet<>();
        } else {
          sequence = new HashSet<>(Arrays.asList(sequenceString.split(",")));
        }

        int group_id = Integer.parseInt(results.getString("group_id"));
        courseWays.put(id, new CourseWay(id, sequence, group_id, graph));
      }
    } catch (NumberFormatException e) {
      throw new SQLException("Invalid number format");
    }
    return courseWays;
  }

  /**
   * Converts the ResultSet to a Group Hashmap of a HashMap of CourseWays.
   *
   * @param results a ResultSet with CourseWay data
   * @return the converted ResultSet
   * @throws SQLException if the ResultSet's data cannot be queried
   */
   public static Map<Integer, HashMap<String, CourseWay>> resultToCourseWayGroup(ResultSet results, CourseGraph graph)
    throws SQLException {
    HashMap<String, CourseWay> courseIDCourseWayMap = resultToCourseWayMap(results, graph);
    HashMap<Integer, HashMap<String, CourseWay>> groupCourseWayMap = new HashMap<>();
    for (CourseWay cw : courseIDCourseWayMap.values()) {
      if (!groupCourseWayMap.containsKey(cw.getGroupID())) {
        groupCourseWayMap.put(cw.getGroupID(), new HashMap<>());
      }
      groupCourseWayMap.get(cw.getGroupID()).put(cw.getID(),cw);
    }
    return groupCourseWayMap;
   }

  /**
   * Uses a function to query all rows of a ResultSet.
   *
   * @param results a ResultSet with a query
   * @param converter a function to convert a single result to a Java object
   * @param <T> the type of object represented by each row
   * @return the set of converted results
   * @throws SQLException if the results are invalid
   */
  public static <T> Set<T> convertAllResults(ResultSet results, SQLFunction<ResultSet, T> converter)
      throws SQLException {
    Set<T> converted = new HashSet<>();
    unwrap(() -> Iterators.addAll(converted, iterateResults(results, converter)));
    return converted;
  }
}
