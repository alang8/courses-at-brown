package edu.brown.cs.student.finproject.kdtree;

/**
 * Creates a KD Tree that can be used to search for data more efficiently than a normal list.
 *
 * @author theofernandez
 * @param <T> the type of data contained
 */

public class KDNode<T extends Locatable> {
  private final T data;
  private final KDNode<T> leftChild;
  private final KDNode<T> rightChild;

  /**
   * Takes in the a Locatable for the current node of the KD Tree, and assigns the
   * left and the right children of the KD Tree to other sub KD Trees.
   *
   * @param data       The current node in the KD Tree.
   * @param leftChild  The left sub tree of the KD Tree.
   * @param rightChild The right sub tree of the KD Tree.
   */

  public KDNode(T data, KDNode<T> leftChild, KDNode<T> rightChild) {
    assert data != null;
    this.data = data;
    this.leftChild = leftChild;
    this.rightChild = rightChild;
  }

  /**
   * Returns the Locatable value stored in the current KD Tree.
   *
   * @return The Locatable data stored in the current KD Tree.
   */
  public T getData() {
    return data;
  }

  /**
   * Returns the left child tree of the current KD Tree.
   *
   * @return The left child tree of the current KD Tree.
   */
  public KDNode<T> getLeftChild() {
    return leftChild;
  }

  /**
   * Returns the right child tree of the current KD Tree.
   *
   * @return The right child tree of the current KD Tree.
   */
  public KDNode<T> getRightChild() {
    return rightChild;
  }

  /**
   * Converts a KD tree to a string. Overrides the toString() method.
   *
   * @return The current KD tree as a String.
   */
  @Override
  public String toString() {
    return "[data = " + data + ", leftChild = " + leftChild + ", rightChild = " + rightChild + "]";
  }
}
