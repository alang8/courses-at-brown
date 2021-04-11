package edu.brown.cs.futureatbrown.termproject.course;

import com.google.common.collect.Iterators;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

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
   * @param results a ResultSet with CourseNode data.
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

    Double course_rating;
    Double prof_rating;
    Double avg_hours;
    Double max_hours;
    Integer class_size;
    try {
      sem = results.getInt("sem");
      if (results.wasNull()) {
        sem = null;
      }

      course_rating = results.getDouble("course_rating");
      if (results.wasNull()) {
        course_rating = null;
      }

      prof_rating = results.getDouble("prof_rating");
      if (results.wasNull()) {
        prof_rating = null;
      }

      avg_hours = results.getDouble("avg_hours");
      if (results.wasNull()) {
        avg_hours = null;
      }

      max_hours = results.getDouble("max_hours");
      if (results.wasNull()) {
        max_hours = null;
      }

      class_size = results.getInt("class_size");
      if (results.wasNull()) {
        class_size = null;
      }

    } catch (NumberFormatException e) {
      throw new SQLException("Invalid number format");
    }
    return new CourseNode(id, name, instr, sem, rawprereq, prereq, description, course_rating,
        prof_rating, avg_hours, max_hours, class_size);
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
