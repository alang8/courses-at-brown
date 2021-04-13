package edu.brown.cs.futureatbrown.termproject.course;

import java.util.Set;

/**
 * Contains information about how a Course satisfies a Pathway
 */
public class CourseWay {
  private final String id;
  private Set<String> sequence;
  private int group_id;

  /**
   * Constructs a new CourseWay with the given parameters.
   *
   * @param id the unique id
   * @param sequence the sequence of courses
   * @param group_id the group id
   */
  public CourseWay(String id, Set<String> sequence, int group_id) {
    this.id = id;
    this.sequence = sequence;
    this.group_id = group_id;
  }

  /**
   * Returns the ID of this CourseWay. This value should be unique.
   *
   * @return the ID
   */
  public String getID() {
    return id;
  }

  /**
   * Returns the sequence of this CourseWay.
   *
   * @return the sequence
   */
  public Set<String> getSequence() {
    return sequence;
  }

  /**
   * Returns the group ID of this CourseWay.
   *
   * @return the group ID
   */
  public int getGroupID() {
    return group_id;
  }

  /**
   * Compares this CourseWay to another object for equality.
   * <p>
   * Another object is equal to this node if it is also a CourseWay and shares the same id.
   *
   * @param other another object
   * @return whether the other object is equal to this CourseWay
   */
  @Override
  public boolean equals(Object other) {
    return other instanceof CourseWay && id.equals(((CourseWay) other).id);
  }

  /**
   * Returns a unique hash for this CourseWay.
   *
   * @return the hash code
   */
  @Override
  public int hashCode() {
    return id.hashCode();
  }

  /**
   * Returns a string representing a CourseWay.
   *
   * @return the string
   */
  @Override
  public String toString() {
    String sequenceStr = "";
    for (String courseID : sequence) {
      sequenceStr = sequenceStr + courseID + ", ";
    }
    return id + ": " + sequenceStr + group_id;
  }
}
