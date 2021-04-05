package edu.brown.cs.student.finproject.util;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Deque;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A FruitTree is a tree where some leaves carry extra data.
 *
 * @param <N> the type of a node on the tree
 * @param <F> the type of a fruit on the tree
 */
public class FruitTree<N, F> {
  private final N nodeValue;
  private F fruit;
  private final Set<FruitTree<N, F>> children = new HashSet<>();

  /**
   * Constructs a FruitTree node with a no value and no fruit.
   * <p>
   * This node can only be used as the root node.
   */
  public FruitTree() {
    this(null, null);
  }

  /**
   * Constructs a FruitTree node with a certain value and no fruit.
   *
   * @param nodeValue the value of this node
   */
  public FruitTree(N nodeValue) {
    this(nodeValue, null);
  }

  /**
   * Constructs a FruitTree node with a certain value and fruit.
   *
   * @param nodeValue the value of this node
   * @param fruit     the fruit at this node
   */
  public FruitTree(N nodeValue, F fruit) {
    this.nodeValue = nodeValue;
    this.fruit = fruit;
  }

  /**
   * @return the value of this node
   */
  public N getValue() {
    return nodeValue;
  }

  /**
   * Checks whether a fruit exists at this node.
   * <p>
   * Equivalent to this.getFruit() == null
   *
   * @return whether this node has a fruit
   */
  public boolean hasFruit() {
    return fruit != null;
  }

  /**
   * Returns the fruit at this node, or null if there is none.
   *
   * @return the fruit at this node, or null
   */
  public F getFruit() {
    return fruit;
  }

  /**
   * Sets the fruit at this node.
   *
   * @param newFruit the new fruit at this node
   */
  public void setFruit(F newFruit) {
    fruit = newFruit;
  }

  /**
   * Returns a shallow copy of this FruitTree's children.
   *
   * @return the children of this node
   */
  public Set<FruitTree<N, F>> getChildren() {
    return new HashSet<>(children);
  }

  /**
   * Returns the values of this node's children.
   *
   * @return the values of the children of this node
   */
  public Set<N> getChildValues() {
    return children.stream().map(c -> c.nodeValue)
        .collect(Collectors.toSet());
  }

  /**
   * Returns the first child with the given value.
   * <p>
   * Comparisons done with the .equals() method.
   *
   * @param value a value to find
   * @return a child whose value is equal to the one given
   */
  public FruitTree<N, F> getChildWithValue(N value) {
    if (value == null) {
      throw new IllegalArgumentException("Children cannot have null as a value");
    }
    for (FruitTree<N, F> child : children) {
      N childValue = child.getValue();
      if (value.equals(childValue)) {
        return child;
      }
    }
    return null;
  }

  /**
   * Adds a child with a node value and no fruit.
   * <p>
   * Will not add a child if a child with the given value already exists.
   *
   * @param value the value of the node to add
   */
  public void addChild(N value) {
    if (value == null) {
      throw new IllegalArgumentException("Cannot add a node with a null value as a child");
    }
    FruitTree<N, F> child = new FruitTree<>(value, null);
    if (getChildWithValue(value) == null) {
      children.add(child);
    }
  }

  /**
   * Adds a child with a node value and no fruit.
   * <p>
   * Will not add a child if a child with the given value already exists.
   *
   * @param values the path of values to add
   * @return the leaf at the end of the path
   */
  @SafeVarargs
  public final FruitTree<N, F> buildPath(N... values) {
    FruitTree<N, F> parent = this;
    for (N value : values) {
      parent.addChild(value);
      parent = parent.getChildWithValue(value);
    }
    return parent;
  }

  /**
   * Finds a node by traversing down the tree according to the given values.
   *
   * @param values the node values to successively search for
   * @return the node at the found path
   */
  @SafeVarargs
  public final FruitTree<N, F> getAtPath(N... values) {
    if (values == null) {
      throw new IllegalArgumentException("Path must be defined");
    }
    FruitTree<N, F> found = this;
    for (N value : values) {
      if (found == null) {
        throw new PathNotFoundException("Path " + Arrays.toString(values) + " not found");
      }
      found = found.getChildWithValue(value);
    }
    return found;
  }

  /**
   * Returns whether another object is equal to this FruitTree.
   * <p>
   * An object is equal to this FruitTree if it is another FruitTree with an equal value, an equal
   * fruit, and equal children.
   *
   * @param other another object to compare
   * @return whether the other object is equal to this FruitTree
   */
  @Override
  public boolean equals(Object other) {
    if (!(other instanceof FruitTree)) {
      return false;
    }
    try {
      FruitTree<N, F> otherFt = (FruitTree<N, F>) other;
      return Objects.equals(nodeValue, otherFt.nodeValue)
          && Objects.equals(fruit, otherFt.fruit)
          && Objects.equals(getChildren(), otherFt.getChildren());
    } catch (ClassCastException e) {
      return false;
    }
  }

  /**
   * Returns a hash for this FruitTree.
   *
   * @return a number categorizing this FruitTree
   */
  @Override
  public int hashCode() {
    return Objects.hash(nodeValue, fruit, children);
  }

  /**
   * Returns an iterator which traverses all paths of this FruitTree, visiting each node once.
   *
   * @return an iterator of all paths in this FruitTree
   */
  private Iterator<List<FruitTree<N, F>>> allPathsIterator() {
    List<List<FruitTree<N, F>>> thisList = List.of(List.of(this));
    return new Iterator<>() {
      private final Deque<List<FruitTree<N, F>>> pathsToVisit =
          new ArrayDeque<>(thisList);

      @Override
      public boolean hasNext() {
        return !pathsToVisit.isEmpty();
      }

      @Override
      public List<FruitTree<N, F>> next() {
        if (!hasNext()) {
          throw new NoSuchElementException();
        }
        List<FruitTree<N, F>> path = pathsToVisit.pop();

        FruitTree<N, F> last = path.get(path.size() - 1);
        for (FruitTree<N, F> child : last.getChildren()) {
          List<FruitTree<N, F>> extendedPath = new ArrayList<>(path);
          extendedPath.add(child);
          pathsToVisit.push(extendedPath);
        }

        return path;
      }
    };
  }

  /**
   * Returns an iterable which can iterate over every path in this FruitTree.
   * <p>
   * All nodes will be visited once.
   *
   * @return an iterable through paths
   */
  private Iterable<List<FruitTree<N, F>>> allPaths() {
    return this::allPathsIterator;
  }

  /**
   * Converts this FruitTree into a readable form.
   *
   * @return a readable form for this FruitTree.
   */
  @Override
  public String toString() {
    List<String> strings = new LinkedList<>();
    for (List<FruitTree<N, F>> path : allPaths()) {
      List<N> pathElements = path.stream().map(FruitTree::getValue).collect(Collectors.toList());
      FruitTree<N, F> last = path.get(path.size() - 1);
      if (!last.hasFruit() && !last.getChildren().isEmpty()) {
        continue;
      }
      strings.add(String.format("%s: %s", pathElements, last.getFruit()));
    }
    String elements = String.join(", ", strings);
    return String.format("{%s}", elements);
  }
}
