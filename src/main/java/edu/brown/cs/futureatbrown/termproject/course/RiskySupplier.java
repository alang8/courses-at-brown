package edu.brown.cs.student.finproject.course;

/**
 * A RiskySupplier accepts a single argument and returns a value, optionally throwing an Exception.
 *
 * @param <R> the return type
 * @param <E> the type pf error thrown
 */
@FunctionalInterface
public interface RiskySupplier<R, E extends Throwable> {
  /**
   * Gets a value.
   *
   * @return a generated value
   * @throws E if an error occurs
   */
  R get() throws E;
}
