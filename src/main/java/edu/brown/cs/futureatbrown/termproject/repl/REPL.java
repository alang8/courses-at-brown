package edu.brown.cs.student.finproject.repl;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.nio.charset.StandardCharsets;

/**
 * A REPL reads data from an InputStream and runs commands using a CommandParser.
 */
public class REPL {
  private final CommandParser parser;

  /**
   * Constructs a REPL with a CommandParser.
   *
   * @param parser the CommandParser which should be used when evaluating commands
   */
  public REPL(CommandParser parser) {
    this.parser = parser;
  }

  /**
   * Reads commands from an input stream.
   *
   * @param in a stream which contains commands
   * @param out a stream to print to
   * @param err a stream to print errors to
   */
  public void read(InputStream in, PrintStream out, PrintStream err) {
    try (BufferedReader reader
             = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8))) {
      String line;
      while ((line = reader.readLine()) != null) {
        try {
          parser.parse(line, out);
        } catch (Exception ex) {
          err.println("ERROR: " + ex.getMessage());
        }
      }
    } catch (IOException ex) {
      err.println("Unable to read from the given input stream");
    }
  }
}
