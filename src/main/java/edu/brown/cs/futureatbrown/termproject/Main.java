package edu.brown.cs.futureatbrown.termproject;

import edu.brown.cs.futureatbrown.termproject.repl.CommandParser;
import edu.brown.cs.futureatbrown.termproject.repl.REPL;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  private String[] args;

  /**
   * The initial method called when execution begins.
   *
   * @param args an array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  /**
   * Constructs an Main object with a String array of arguments.
   *
   * @param args an array of command line arguments
   */
  private Main(String[] args) {
    this.args = args;
  }

  /**
   * The method that runs the functions of the Main object.
   */
  private void run() {
    // Parse command line arguments
    // Do not need any frontend components for this part
    CommandParser commandParser = new CommandParser();
    REPL repl = new REPL(commandParser);
    repl.read(System.in, System.out, System.err);
  }
}
