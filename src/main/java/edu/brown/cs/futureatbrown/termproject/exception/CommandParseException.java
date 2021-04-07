package edu.brown.cs.futureatbrown.termproject.exception;

/**
 * A CommandParseException indicates that a command or its arguments could not be parsed.
 */
public class CommandParseException extends CommandException {

  /**
   * Constructs a CommandParseException with a message.
   *
   * @param message the message to print
   */
  public CommandParseException(String message) {
    super(message);
  }
}
