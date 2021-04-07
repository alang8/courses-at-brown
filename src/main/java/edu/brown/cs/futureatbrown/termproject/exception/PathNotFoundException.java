package edu.brown.cs.futureatbrown.termproject.exception;

/**
 * A PathNotFoundException can be thrown when a path is not found on a tree.
 */
public class PathNotFoundException extends RuntimeException {

  /**
   * Constructs a PathNotFoundException with a message.
   *
   * @param message the message to print
   */
  public PathNotFoundException(String message) {
    super(message);
  }
}
