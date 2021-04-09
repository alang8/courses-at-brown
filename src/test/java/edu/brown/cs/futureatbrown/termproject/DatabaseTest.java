package edu.brown.cs.futureatbrown.termproject;

import edu.brown.cs.futureatbrown.termproject.course.CourseConversions;
import edu.brown.cs.futureatbrown.termproject.course.CourseNode;
import edu.brown.cs.futureatbrown.termproject.course.Database;
import edu.brown.cs.futureatbrown.termproject.exception.SQLRuntimeException;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;

/**
 * Class that tests the querying of course data from the sql table.
 */
public class DatabaseTest {

  @Before
  public void setup() {
    try{
      Database.init("data/courseDatabase.sqlite3");
    } catch (SQLException throwables) {
    throwables.printStackTrace();
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

  @After
  public void teardown() {
    try{
      Database.closeDB();
    } catch (SQLRuntimeException throwables) {
      throwables.printStackTrace();
    }
  }

  @Test
  public void isReadyTest() {
    setup();
    Assert.assertTrue(Database.isReady());
    teardown();
  }

  @Test
  public void getCourseNode() {
    Assert.assertEquals(
      Database.getCourseNode("CSCI 0020"),
      new CourseNode("CSCI 0020", "The Digital World", "D. Stanford",
                    1, null, null, null,
                    3.58, 3.86, 2.61, 4.98, 302)
      );
    Assert.assertEquals(
      Database.getCourseNode("APMA 1655"),
      new CourseNode("APMA 1655", "Statistical Inference I", "Wang/Khoshnevis",
                    0, "Students may opt to enroll in 1655 for more in depth coverage of APMA 1650 \n" +
        "\n" + "Prerequisite (for either version): MATH 0100, 0170, 0180, 0190, 0200, or 0350",
        "MATH 0100|MATH 0180|MATH 0190|MATH 0200|MATH 0350", "Students may opt to enroll in 1655 for more in depth coverage of APMA 1650.   Enrollment in 1655 will include an optional recitation section and required additional individual work. Applied Math concentrators are encouraged to take 1655.\n" +
        "\n" + "Prerequisite (for either version): MATH 0100, 0170, 0180, 0190, 0200, or 0350.",
        4.5, 4.83, 5.15, 9.26, 80)
    );
    Assert.assertEquals(
      Database.getCourseNode("CSCI 1410"),
      new CourseNode("CSCI 1410", "Artificial Intelligence", "G. Konidaris", 1,
      " Prerequisites: CSCI 0160, CSCI 0180 or CSCI 0190; and one of CSCI0220 or CSCI1450 or APMA1650 or APMA1655",
      "(CSCI 0160|CSCI 0180|CSCI 0190)&(CSCI 0220|CSCI 1450|APMA 1650| APMA 1655) ",
      "Practical approaches to designing intelligent systems. Topics include search and optimization, uncertainty, learning, and decision making. Application areas include natural language processing, machine vision, machine learning, and robotics. Prerequisites: CSCI 0160, CSCI 0180 or CSCI 0190; and one of CSCI0220 or CSCI1450 or APMA1650 or APMA1655.",
        3.97, 4.47, 6.3, 12.3, 142)
    );
    Assert.assertNull(Database.getCourseNode("CSCI 1951D"));
//    Assert.assertEquals(
//      Database.getCourseNode("CSCI 1951D"),
//      new CourseNode("CSCI 1951D", "Projective Geometry via Interactive Proof Assistants", "J. Hughes", 2,
//        null, "(MATH 0520|MATH 0540|MATH 1530)&(CSCI 0160|CSCI 0180|CSCI 0190|CSCI 1570)",
//        "We will study both real and synthetic projective geometry, leading up to showing every Desarguian projective plane admits coordinates in some division ring. We'll do all of this using an automated proof assistant, Isabelle, and learn something about logic and automated theorem proving at the same time.\n" +
//          "\n" +
//          "Does not fit any pathways, but certainly fits the \"Math/CS three additional courses in Math, CS, or related areas\" item.",
//        null, null, null, null, null)
//    );
    teardown();
  }




}
