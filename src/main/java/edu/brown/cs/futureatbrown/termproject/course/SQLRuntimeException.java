package edu.brown.cs.futureatbrown.termproject.course;

import java.sql.SQLException;
import java.util.function.Supplier;

/**
 * An SQLRuntimeException is the same as an SQLException, but skirts Java's try/catch requirements.
 *
 * This avoids the issue where inherited methods cannot throw SQLException when they should.
 */
public class SQLRuntimeException extends RuntimeException {
  /**
   * Constructs an SQLRuntimeException with a cause.
   *
   * @param cause a Throwable
   */
  public SQLRuntimeException(SQLException cause) {
    super(cause.getMessage(), cause);
  }

  /**
   * Wraps a risky function in a less risky one.
   * <p>
   * Most methods disallow taking functions which throw exceptions, so you may have to wrap
   * functions before passing them, then unwrap them later.
   *
   * @param f   a function to wrap
   * @param <R> the return type of the function
   * @return the result of the function
   */
  public static <R> R wrap(RiskySupplier<R, ? extends SQLException> f) {
    try {
      return f.get();
    } catch (SQLException e) {
      throw new SQLRuntimeException(e);
    }
  }

  /**
   * Unwraps a risky function into one which explicitly throws SQLException.
   *
   * @param f   a supplier which throws DatabaseRuntimeException
   * @param <R> the return type of the supplier
   * @return the returned value of the supplier
   * @throws SQLException if a DatabaseRuntimeException is thrown
   */
  public static <R> R unwrap(Supplier<R> f) throws SQLException {
    try {
      return f.get();
    } catch (SQLRuntimeException e) {
      throw new SQLException(e);
    }
  }
}
