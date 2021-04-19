package edu.brown.cs.futureatbrown.termproject.kdtree;

import java.util.ArrayList;
import java.util.List;

/**
 * Creates a KD Tree that can be used to search for data more efficiently than a normal list.
 *
 * @param <T> the type of item contained
 */

public class KDTree<T extends Locatable> {
  private KDNode<T> root;

  /**
   * Takes in an ArrayList of Locatables called inputList, and creates a balanced KD tree using
   * those Locatables as nodes.
   *
   * @param inputList a list of Locatables to be turned into a balanced KD Tree
   */
  public KDTree(List<T> inputList) {
    if (inputList.size() != 0) {
      int numOfDimensions = inputList.get(0).getNumOfDimensions();
      root = createKDTree(inputList, 0, numOfDimensions);
    }
  }

  /**
   * Creates a balanced KD Tree based on the input list.
   *
   * @param inputList the data to be input into the balanced KD Tree
   * @param layer the depth of the current node in the KD Tree
   * @param numOfDimensions the number of dimensions K in the KD Tree
   * @return a balanced KD Tree using the data from the input list
   */
  private KDNode<T> createKDTree(List<T> inputList, int layer, int numOfDimensions) {
    if (inputList.size() == 0) {
      return null;
    } else {
      int sortingDimension = layer % numOfDimensions;
      inputList.sort(new LocatableSort<>(sortingDimension));
      int inputSize = inputList.size();
      int medianLocation = inputSize / 2;
      double medianRelevantNumber = inputList.get(medianLocation).getCoordinate(sortingDimension);
      T median = inputList.get(medianLocation);
      inputList.remove(medianLocation);
      ArrayList<T> leftHalf = new ArrayList<>();
      ArrayList<T> rightHalf = new ArrayList<>();
      for (T locatable : inputList) {
        if (locatable.getCoordinate(sortingDimension) < medianRelevantNumber) {
          leftHalf.add(locatable);
        } else {
          rightHalf.add(locatable);
        }
      }
      KDNode<T> leftSubTree = null;
      KDNode<T> rightSubTree = null;
      if (leftHalf.size() != 0) {
        leftSubTree = createKDTree(new ArrayList<>(leftHalf), layer + 1, numOfDimensions);
      }
      if (rightHalf.size() != 0) {
        rightSubTree = createKDTree(new ArrayList<>(rightHalf), layer + 1, numOfDimensions);
      }
      return new KDNode<>(median, leftSubTree, rightSubTree);
    }
  }

  /**
   * Returns the root of this KD tree.
   *
   * @return the root of this KD tree; it may be null
   */
  public KDNode<T> getRoot() {
    return root;
  }

  /**
   * Converts a KD tree to a string. Overrides the toString() method.
   *
   * @return the current KD tree as a String
   */
  @Override
  public String toString() {
    return root.toString();
  }

  /**
   * Resets the KD tree to be empty.
   */
  public void clear() {
    root = null;
  }
}
