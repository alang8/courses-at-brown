package edu.brown.cs.futureatbrown.termproject.kdtree;

/**
 * The locatable interface allows objects to have positions with common methods. The positions
 * can be in any number of dimensions and allow the user to get the number of dimensions the
 * object has, the current location, or the coordinate in a specific dimension.
 */
public interface Locatable {

  /**
   * Returns the number of dimensions that the locatable is in.
   *
   * @return return an integer representing the number of dimensions the locatable has.
   */
  int getNumOfDimensions();

  /**
   * Returns the position of the locatable.
   *
   * @return an array of doubles representing the locatable's location.
   */
  double[] getCoordinates();

  /**
   * Returns the nth dimension of a locatable.
   *
   * @param dim which dimension to get from the locatable.
   * @return the specified dimension from the locatable.
   */
  double getCoordinate(int dim);
}
