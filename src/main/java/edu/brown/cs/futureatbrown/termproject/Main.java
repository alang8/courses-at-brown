package edu.brown.cs.student.finproject;

import edu.brown.cs.student.finproject.repl.CommandParser;
import edu.brown.cs.student.finproject.repl.REPL;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private static final int DEFAULT_PORT = 4567;

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    // Do not need any frontend components for this part
    CommandParser commandParser = new CommandParser();
    REPL repl = new REPL(commandParser);
    repl.read(System.in, System.out, System.err);
  }
}
