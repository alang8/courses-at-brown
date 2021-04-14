package edu.brown.cs.futureatbrown.termproject;

import edu.brown.cs.futureatbrown.termproject.course.CourseEdge;
import edu.brown.cs.futureatbrown.termproject.course.CourseNode;
import edu.brown.cs.futureatbrown.termproject.course.Database;
import edu.brown.cs.futureatbrown.termproject.exception.SQLRuntimeException;
import org.checkerframework.checker.units.qual.A;
import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;

/**
 * Makes sure that all of the Course Node information preparing functions are implemented correctly
 */
public class CourseNodeTest {
  private static final double TOLERANCE = 0.001;

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
    try {
      Database.closeDB();
    } catch (SQLRuntimeException e) {
      e.printStackTrace();
    }
  }

  @Test
  public void getterFunctions() {
    setup();
    CourseNode math190 = Database.getCourseNode("MATH 0190");
    Assert.assertEquals(math190.getID(), "MATH 0190");
    Assert.assertEquals(math190.getName(), "Advanced Placement Calculus (Physics/Engineering)");
    Assert.assertEquals(math190.getInstr(), "J. Kostiuk");
    Assert.assertEquals(math190.getSem(), Integer.valueOf(1));
    Assert.assertEquals(math190.getRawprereq(), "Covers roughly the same material and has the same prerequisites as MATH 0170, but is intended for students with a special interest in physics or engineering");
    Assert.assertNull(math190.getPrereq());
    Assert.assertEquals(math190.getDescription(), "Covers roughly the same material and has the same prerequisites as MATH 0170, but is intended for students with a special interest in physics or engineering. The main topics are: calculus of vectors and paths in two and three dimensions; differential equations of the first and second order; and infinite series, including power series and Fourier series. The extra hour is a weekly problem session.");
    Assert.assertEquals(math190.getCourse_rating(), 4.31, TOLERANCE);
    Assert.assertEquals(math190.getProf_rating(), 4.6, TOLERANCE);
    Assert.assertEquals(math190.getAvg_hours(), 7.65, TOLERANCE);
    Assert.assertEquals(math190.getMax_hours(), 12.11, TOLERANCE);
    Assert.assertEquals(math190.getClass_size(), 44, TOLERANCE);
    Assert.assertEquals(math190.getNumOfDimensions(), 2);
    Assert.assertArrayEquals(math190.getCoordinates(), new double[] {4.31, 4.6}, TOLERANCE);
    Assert.assertEquals(math190.getCoordinate(0), 4.31, TOLERANCE);
    Assert.assertEquals(math190.getCoordinate(1), 4.6, TOLERANCE);
    Assert.assertEquals(math190.getWeight(), 0, TOLERANCE);
    Assert.assertFalse(math190.visited());
    Assert.assertEquals(math190.getPreviousPath(), new ArrayList<>());
    Assert.assertEquals(math190, math190.copy());
    teardown();
  }

  @Test
  public void setterFunctions() {
    setup();
    CourseNode math190 = Database.getCourseNode("MATH 0190");
    math190.setWeight(10.1);
    Assert.assertEquals(math190.getWeight(), 10.1, TOLERANCE);
    math190.setVisited(true);
    Assert.assertTrue(math190.visited());
    math190.setPreviousPath(List.of(new CourseEdge("Self Edge", math190, math190)));
    Assert.assertEquals(
      math190.getPreviousPath(),
      List.of(new CourseEdge("Self Edge", math190, math190))
    );
    teardown();
  }

  @Test
  public void informationProcessingFunctions() {
    setup();
    CourseNode cs1951D = Database.getCourseNode("CSCI 1951D");
    Assert.assertNotNull(cs1951D);
    Assert.assertNotNull(cs1951D.getPrereqSet());
    Assert.assertEquals(
      cs1951D.getPrereqSet(),
      new HashSet<>(List.of(
        List.of("MATH 0520", "MATH 0540", "MATH 1530"),
        List.of("CSCI 0160", "CSCI 0180", "CSCI 0190", "CSCI 1570")))
    );
    teardown();
  }
}
