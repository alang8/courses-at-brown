package edu.brown.cs.futureatbrown.termproject.exception;

/**
 * A CommandAlreadyExistsException signifies that a command with a certain order of arguments has
 * been registered.
 */
public class CommandAlreadyExistsException extends RuntimeException {

  /**
   * Constructs a CommandAlreadyExistsException with no message.
   */
  public CommandAlreadyExistsException() {
  }

  /**
   * Constructs a CommandAlreadyExistsException with a message.
   *
   * @param message the message to print
   */
  public CommandAlreadyExistsException(String message) {
    super(message);
  }
}
