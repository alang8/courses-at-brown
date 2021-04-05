package edu.brown.cs.student.finproject.repl;

import java.io.PrintStream;
import java.util.HashMap;

/**
 * Parses registered commands.
 */
public class CommandParser {
  private final HashMap<String, Command> commands = new HashMap<>();

  /**
   * Registers a command under this parser.
   *
   * @param commandName the name of the command
   * @param command     the command to run
   */
  public void register(String commandName, Command command) {
    if (commandName.contains(" ")) {
      throw new IllegalArgumentException("Command names cannot contain spaces");
    }
    if (commands.containsKey(commandName)) {
      throw new IllegalArgumentException("Command " + commandName + " is already registered");
    }
    commands.put(commandName, command);
  }

  /**
   * Recognizes the command used and passes the rest of the line to the registered command.
   * <p>
   * Thanks to the format of the "stars" command, I can unfortunately not be more specific about
   * parsing commands here because some file systems allow names which are all spaces, and names
   * which contain quotation marks.
   *
   * @param line a line containing a command to parse.
   * @param out  an output stream to print to
   */
  public void parse(String line, PrintStream out) {
    line = line.stripLeading();
    if (line.equals("")) {
      return;
    }

    int firstSpace = line.indexOf(' ');
    String command;
    String rest;
    if (firstSpace == -1) {
      command = line;
      rest = "";
    } else {
      command = line.substring(0, firstSpace);
      rest = line.substring(firstSpace + 1);
    }

    if (commands.containsKey(command)) {
      commands.get(command).execute(rest, out);
    } else {
      throw new CommandParseException("'" + command + "' is not a valid command.");
    }
  }
}
