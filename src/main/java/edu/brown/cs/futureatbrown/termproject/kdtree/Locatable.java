package edu.brown.cs.futureatbrown.termproject.kdtree;

/**
 * The locatable interface allows objects to have positions with common methods. The positions
 * can be in any number of dimensions and allow the user to get the number of dimensions the
 * object has, the current location, or the coordinate in a specific dimension.
 *
 * @author theofernandez
 */
public interface Locatable {

  /**
   * Returns the number of dimensions that the locatable is in.
   *
   * @return Return an integer representing the number of dimensions the locatable has.
   */
  int getNumOfDimensions();

  /**
   * Returns the position of the locatable.
   *
   * @return An array of doubles representing the locatable's location.
   */
  double[] getCoordinates();

  /**
   * Returns the nth dimension of a locatable.
   *
   * @param dim Which dimension to get from the locatable.
   * @return The specified dimension from the locatable.
   */
  double getCoordinate(int dim);
}
