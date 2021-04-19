package edu.brown.cs.futureatbrown.termproject.kdtree;

import java.util.Comparator;

/**
 * LocatableSort implements the Comparable interface and allows Locatable objects to be sorted by
 * one of their dimensions.
 *
 * @param <T> the type of Locatable to compare
 */
public class LocatableSort<T extends Locatable> implements Comparator<T> {
  private final int dimension;

  /**
   * Creates a sort method for Locatables by individual dimensions.
   *
   * @param dimension takes a valid dimension and sorts by that dimension
   */
  public LocatableSort(int dimension) {
    this.dimension = dimension;
  }

  /**
   * Compares two Locatables to see which is greater that the other one by comparing the given
   * dimension to see which value is greater.
   *
   * @param locatable1 the first Locatable value to compare
   * @param locatable2 the second Locatable value to compare
   * @return a positive integer if the first Locatable is larger, 0 if they are equal, and a
   * negative integer if it is smaller
   */
  public int compare(T locatable1, T locatable2) {
    return Double.compare(locatable2.getCoordinate(dimension), locatable1.getCoordinate(dimension));
  }
}
