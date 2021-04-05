package edu.brown.cs.student.finproject.util;

/**
 * A PathNotFoundException can be thrown when a path is not found on a tree.
 */
public class PathNotFoundException extends RuntimeException {
  /**
   * Constructs a PathNotFoundException with a message.
   *
   * @param message the message to be printed
   */
  public PathNotFoundException(String message) {
    super(message);
  }
}
