package edu.brown.cs.student.finproject.repl;

import java.io.PrintStream;

/**
 * A command can be executed by a CommandParser.
 */
public interface Command {
  /**
   * Executes this command.
   *
   * @param arguments this command's arguments
   * @param out an output stream to use
   */
  void execute(String arguments, PrintStream out);
}
