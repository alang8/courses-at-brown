package edu.brown.cs.student.finproject.course;

/**
 * A RiskyFunction accepts a single argument and returns a value, optionally throwing an Exception.
 *
 * @param <T> the input type
 * @param <R> the output type
 * @param <E> the type pf error thrown
 */
@FunctionalInterface
public interface RiskyFunction<T, R, E extends Throwable> {
  /**
   * Applies this function to an input.
   *
   * @param t an input of this function
   * @return the output of this function
   * @throws E if the denoted exception is thrown
   */
  R apply(T t) throws E;
}
