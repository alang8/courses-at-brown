package edu.brown.cs.futureatbrown.termproject.exception;

/**
 * A CommandException is thrown when a message should be printed to the user.
 */
public class CommandException extends RuntimeException {

  /**
   * Constructs a CommandException with a message.
   *
   * @param message the message to print
   */
  public CommandException(String message) {
    super(message);
  }
}
