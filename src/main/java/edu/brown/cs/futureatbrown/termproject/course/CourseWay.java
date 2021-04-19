package edu.brown.cs.futureatbrown.termproject.course;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Contains information about how a Course satisfies a Pathway.
 */
public class CourseWay {
  private final String id;
  private final Set<String> sequence;
  private final int group_id;
  private Set<CourseWay> prerequisites;

  /**
   * Constructs a new CourseWay with the given parameters.
   *
   * @param id the unique id
   * @param sequence the sequence of courses
   * @param groupId the group id
   */
  public CourseWay(String id, Set<String> sequence, int groupId) {
    this.id = id;
    this.sequence = sequence;
    this.group_id = groupId;
  }

  /**
   * Constructs a new CourseWay with the given parameters.
   *
   * @param id the unique id
   * @param sequence the sequence of courses
   * @param group_id the group id
   */
  public CourseWay(String id, Set<String> sequence, int group_id, CourseGraph graph) {
    this.id = id;
    this.sequence = sequence;
    this.group_id = group_id;
    setPrerequisites(graph);
  }

  /**
   * Returns the ID of this CourseWay. This value should be unique.
   *
   * @return the id
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
   * Returns the group id of this CourseWay.
   *
   * @return the group id
   */
  public int getGroupID() {
    return group_id;
  }

  /**
   * Returns the prerequisites of the CourseWay
   *
   * @return Set of Set of CourseWays that contain the prerequisite sequence groups to each sequence
   */
  public Set<CourseWay> getPrerequisites() {
    return prerequisites;
  }

  /**
   * Sets the prerequisites of the CourseWay
   * @param graph - Graph containing the prerequisites
   */
  public void setPrerequisites(CourseGraph graph) {
    CourseNode currNode = graph.getNodeSet().get(this.id);
    Set<List<String>> prereqSetRaw;
    if (null == currNode) {
      this.prerequisites = null;
    } else {
      prereqSetRaw = currNode.getPrereqSet();
      for (String currID : this.sequence) {
        currNode = graph.getNodeSet().get(currID);
        prereqSetRaw.addAll(currNode.getPrereqSet());
      }
      if (prereqSetRaw.size() > 0) {
        this.prerequisites =
          prereqSetRaw.stream()
            .map(group ->
              new CourseWay(group.get(0), new HashSet<>(group), this.group_id))
             .collect(Collectors.toSet());
      }
    }
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
    StringBuilder sequenceStr = new StringBuilder();
    for (String courseID : sequence) {
      sequenceStr.append(courseID).append(", ");
    }
    return id + ": " + sequenceStr + group_id;
  }
}
