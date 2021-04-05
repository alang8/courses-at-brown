package edu.brown.cs.student.finproject.repl;

import java.io.PrintStream;

/**
 * A command can be executed with arguments and in/out streams.
 */
@FunctionalInterface
public interface ArgsCommand {
  /**
   * Executes this command with the given arguments.
   *
   * @param arguments the arguments to this command
   * @param out a stream to print to
   */
  void execute(String[] arguments, PrintStream out);
}
