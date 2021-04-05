package edu.brown.cs.student.finproject.course;

import java.sql.SQLException;

/**
 * A SQLFunction accepts a single argument and returns a value, optionally throwing a SQLException.
 *
 * @param <T> the input type
 * @param <R> the output type
 */
@FunctionalInterface
public interface SQLFunction<T, R> extends RiskyFunction<T, R, SQLException> {
}
