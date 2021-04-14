package edu.brown.cs.futureatbrown.termproject;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

public class CourseDataHandlers {

  /**
   * Handler for getting all the course data from the database.
   */
  public static class ClassDataHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    ClassDataHandler(Connection c) {
      this.conn = c;
    }

    @Override
    public Object handle(Request request, Response response) {
      JSONObject data = null;
      Map<String, Double> presets = new HashMap<>();
      Collection<Map<String, String>> courses = new ArrayList<>();
      try {
        String query = "SELECT * FROM courseData INNER JOIN courseCR ON courseData.id=courseCR.id;";
        PreparedStatement prep = conn.prepareStatement(query);
        ResultSet rs = prep.executeQuery();
        //cols are: id,name,instr,sem,rawprereq,prereq,desc,id,crsrat,profrat,avghr,maxhr,classsz
        while (rs.next()) {
          Map<String, String> thisCourseData = new HashMap<>();
          thisCourseData.put("id", rs.getString(1));
          thisCourseData.put("name", rs.getString(2));
          thisCourseData.put("instr", rs.getString(3));
          thisCourseData.put("sem", "" + rs.getInt(4));
          thisCourseData.put("desc", rs.getString(7));
          thisCourseData.put("crsrat", rs.getString(9));
          thisCourseData.put("profrat", rs.getString(10));
          thisCourseData.put("avghr", rs.getString(11));
          thisCourseData.put("maxhr", rs.getString(12));
          thisCourseData.put("size", rs.getString(13));
          if (rs.getString(6) == null) {
            thisCourseData.put("prereqs", "");
          } else {
            thisCourseData.put("prereqs", rs.getString(6));
          }
          courses.add(thisCourseData);
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.of("courses", courses);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handler For getting a specific course object from the database.
   */
  public static class GetCourseHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    GetCourseHandler(Connection c) {
      this.conn = c;
    }

    @Override
    public Object handle(Request request, Response response) {
      Collection<Map<String, String>> courses = new ArrayList<>();
      Map<String, String> thisCourseData = new HashMap<>();
      String dept = "";
      String code = "";
      try {
        JSONObject data = new JSONObject(request.body());
        dept = data.getString("dept");
        code = data.getString("code");
        String courseCode = dept + " " + code;
        System.out.println("coursecode: " + courseCode);
        String query = "SELECT * FROM courseData INNER JOIN courseCR ON courseData.id=courseCR.id WHERE courseData.id = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, courseCode);
        ResultSet rs = prep.executeQuery();

        //cols are: id,name,instr,sem,rawprereq,prereq,desc,id,crsrat,profrat,avghr,maxhr,classsz
        if (rs.next()) {
          thisCourseData.put("code", rs.getString(1).substring(5));
          thisCourseData.put("dept", rs.getString(1).substring(0, 4));
          thisCourseData.put("name", rs.getString(2));
          thisCourseData.put("latestProf", rs.getString(3));
          thisCourseData.put("description", rs.getString(7));
          thisCourseData.put("rating", rs.getString(9));
          thisCourseData.put("latestProfRating", rs.getString(10));
          thisCourseData.put("avgHours", rs.getString(11));
          thisCourseData.put("maxHours", rs.getString(12));
          if (rs.getString(6) == null) {
            thisCourseData.put("prereqs", "");
          } else {
            String encodedPrereq = rs.getString(6);
            String prereqText = encodedPrereq.replaceAll("&", " and ");
            prereqText = prereqText.replaceAll("\\|", " or ");
            thisCourseData.put("prereqs", prereqText);
          }
          courses.add(thisCourseData);
        } else {
          System.out.println("ERROR: GetCourseHandler, couldnt find course " + courseCode);
        }
      } catch (SQLException | JSONException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.copyOf(thisCourseData);
      return GSON.toJson(variables);
    }
  }
}
