package edu.brown.cs.futureatbrown.termproject.kdtree;

import java.util.Comparator;

/**
 * LocatableDistanceSort implements the comparator interface and allows for locatables to be sorted
 * by their distance from a point. Only works in 3 dimensions.
 *
 * @param <T> the type of Locatable to compare
 */
public class LocatableDistanceSort<T extends Locatable> implements Comparator<T> {
  private final double xCoordinate, yCoordinate, zCoordinate;

  /**
   * Creates an instance of the distance sort. The distance sort will sort locatables by distance
   * from the given point.
   *
   * @param xCoordinate The xCoordinate to calculate the distance from.
   * @param yCoordinate The yCoordinate to calculate the distance from.
   * @param zCoordinate The zCoordinate to calculate the distance from.
   */
  public LocatableDistanceSort(double xCoordinate, double yCoordinate, double zCoordinate) {
    this.xCoordinate = xCoordinate;
    this.yCoordinate = yCoordinate;
    this.zCoordinate = zCoordinate;
  }

  /**
   * Implements compare from comparator. Takes in two locatables as input and sorts them based on
   * their distance from the given point.
   *
   * @param locatable1 A locatable with three dimensions.
   * @param locatable2 A second locatable with three dimensions.
   * @return An integer that is greater than one if the first locatable is farther, less than one if
   * the second locatable is farther, and 0 if they are the same distance from the target point.
   */
  public int compare(T locatable1, T locatable2) {
    double l1Distance = (locatable1.getCoordinate(0) - xCoordinate)
        * (locatable1.getCoordinate(0) - xCoordinate)
        + (locatable1.getCoordinate(1) - yCoordinate)
        * (locatable1.getCoordinate(1) - yCoordinate)
        + (locatable1.getCoordinate(2) - zCoordinate)
        * (locatable1.getCoordinate(2) - zCoordinate);
    double l2Distance = (locatable2.getCoordinate(0) - xCoordinate)
        * (locatable2.getCoordinate(0) - xCoordinate)
        + (locatable2.getCoordinate(1) - yCoordinate)
        * (locatable2.getCoordinate(1) - yCoordinate)
        + (locatable2.getCoordinate(2) - zCoordinate)
        * (locatable2.getCoordinate(2) - zCoordinate);
    return Double.compare(l1Distance, l2Distance);
  }
}
