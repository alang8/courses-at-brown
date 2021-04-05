package edu.brown.cs.futureatbrown.termproject.repl;

/**
 * A CommandParseException indicates that a command or its arguments could not be parsed.
 */
public class CommandParseException extends CommandException {
  /**
   * Constructs a CommandParseException with a message.
   *
   * @param message a message to be printed
   */
  public CommandParseException(String message) {
    super(message);
  }
}
