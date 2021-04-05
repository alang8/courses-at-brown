package edu.brown.cs.student.finproject.repl;

import edu.brown.cs.student.finproject.util.FruitTree;
import edu.brown.cs.student.finproject.util.PathNotFoundException;

import java.io.PrintStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Set;

/**
 * A StarTokenParser is able to parse numbers (as decimals) and strings (enclosed within quotes).
 * <p>
 * To use a StarTokenParser, one must register different functions for use, according to their order
 * of numbers and strings. Those functions are called when an appropriate input is passed to
 * runArguments.
 */
public class TokenParser {
  /**
   * Enumerates different types of tokens allowed.
   */
  public enum TokenType {
    NUMBER,
    STRING,
  }

  private static final String STRING_REGEX = "\"[^\"]+\"";

  /**
   * Contains different functions to execute commands of differing token types.
   */
  private final FruitTree<TokenType, ArgsCommand> tokenPaths
      = new FruitTree<>();

  /**
   * Checks whether a certain list of tokens corresponds to a valid invocation.
   *
   * @param types an array of token types
   * @return whether the given order of types has been registered
   */
  private boolean typeOrderExists(TokenType[] types) {
    try {
      FruitTree<TokenType, ArgsCommand> atEnd =
          tokenPaths.getAtPath(types);
      return atEnd != null && atEnd.getFruit() != null;
    } catch (PathNotFoundException e) {
      return false;
    }
  }

  /**
   * Registers a command which runs using the given types.
   *
   * @param types  an array of types which a user can input
   * @param action a function to run when the corresponding token types have been encountered
   */
  public void register(TokenType[] types, ArgsCommand action) {
    if (typeOrderExists(types)) {
      throw new CommandAlreadyExistsException();
    }
    tokenPaths.buildPath(types).setFruit(action);
  }

  /**
   * Parses the arguments and runs the correct commend.
   * <p>
   * This method does not use a Scanner, so errors may be present.
   *
   * @param arguments a line of arguments to parse
   * @param out       an output stream to print to
   */
  public void runArguments(String arguments, PrintStream out) {
    Scanner scanner = new Scanner(arguments);
    FruitTree<TokenType, ArgsCommand> currentNode = tokenPaths;
    List<String> tokens = new ArrayList<>();

    // Read all remaining tokens
    while (scanner.hasNext()) {
      Set<TokenType> possibleNextTypes = currentNode.getChildValues();
      String nextToken; // will be added to the token list
      // TODO Generalize this
      // TODO also make this more readable
      // Determine the type of the next token
      if (possibleNextTypes.contains(TokenType.NUMBER) && scanner.hasNextBigDecimal()) {
        // Read a BigDecimal, but as a String
        currentNode = currentNode.getChildWithValue(TokenType.NUMBER);
        nextToken = scanner.next();
      } else if (possibleNextTypes.contains(TokenType.STRING)) {
        // Read a quoted string based on STRING_REGEX
        nextToken = scanner.findInLine(STRING_REGEX);
        if (nextToken == null) {
          throw new CommandParseException(
              "Expected a quoted string at " + scanner.nextLine());
        }
        currentNode = currentNode.getChildWithValue(TokenType.STRING);
        nextToken = nextToken.substring(1, nextToken.length() - 1); // trim quotes
      } else {
        // Throw a CommandParseException detailing the issue.
        if (possibleNextTypes.size() == 0) {
          throw new CommandParseException("Too many arguments");
        } else {
          throw new CommandParseException(
              String.format("Incompatible tokens: expecting a token of type %s in %s",
                  possibleNextTypes, scanner.nextLine()));
        }
      }
      tokens.add(nextToken);
    }
    if (currentNode != null && currentNode.hasFruit()) {
      currentNode.getFruit().execute(tokens.toArray(String[]::new), out);
    } else {
      throw new CommandParseException("Invalid command arguments");
    }
  }
}
