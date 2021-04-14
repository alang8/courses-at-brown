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
import java.util.Arrays;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UserDataHandlers {

  /**
   * Class which ensures that a username isn't yet taken for our site.
   */
  public static class CheckUsernameHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    public CheckUsernameHandler(Connection c) {
      this.conn = c;
    }

    @Override
    public Object handle(Request request, Response response) {
      Map<String, Object> variables;
      boolean isTaken = false;
      try {
        JSONObject data = new JSONObject(request.body());
        String inputtedUsername = data.getString("username");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(inputtedUsername.getBytes());

        String query = "SELECT * FROM user_data WHERE username = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        ResultSet rs = prep.executeQuery();
        if (rs.next()) {
          //found a match
          isTaken = true;
        }
        variables = ImmutableMap.of("isTaken", isTaken);
      } catch (JSONException | SQLException e) {
        variables = ImmutableMap.of("message", "ERROR: SignUpHandler", "isTaken", true);
        System.out.println("ERROR: in UserDataHandlers.java - error w/ json/SQL in CheckUsernameHandler");
      }
      return GSON.toJson(variables);
    }
  }

  /**
   * Class which handles signing up for our site.
   */
  public static class SignUpHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    public SignUpHandler(Connection c) {
      this.conn = c;
    }

    @Override
    public Object handle(Request request, Response response) {
      Map<String, Object> variables;
      String msg;
      Map<String, Double> presets = new HashMap<>();
      try {
        JSONObject data = new JSONObject(request.body());

        String inputtedUsername = data.getString("username");
        String inputtedPassword = data.getString("password");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(inputtedUsername.getBytes());
        String hashedPassword = coder.encodeToString(inputtedPassword.getBytes());

        double crsRatingPref = 5.0;
        double avgHoursPref = 5.0;
        double maxHoursPref = 5.0;
        double crsSizePref = 5.0;
        double profRatingPref = 5.0;
        presets.put("crsRatingPref", crsRatingPref);
        presets.put("avgHoursPref", avgHoursPref);
        presets.put("maxHoursPref", maxHoursPref);
        presets.put("crsSizePref", crsSizePref);
        presets.put("profRatingPref", profRatingPref);
        msg = "Account Added!";
        String query = "INSERT INTO user_data VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        prep.setString(2, hashedPassword);
        prep.setDouble(3, crsRatingPref);
        prep.setDouble(4, avgHoursPref);
        prep.setDouble(5, maxHoursPref);
        prep.setDouble(6, crsSizePref);
        prep.setDouble(7, profRatingPref);
        prep.setString(8, null);
        prep.setString(9, null);
        prep.executeUpdate();
        variables = ImmutableMap.of("message", msg, "presets", presets);
        System.out.println("Inserted user!");
      } catch (JSONException | SQLException e) {
        variables = ImmutableMap.of("message", "ERROR: SignUpHandler", "presets", new HashMap<>());
        e.printStackTrace();
        System.out.println("ERROR: in UserDataHandlers.java - error w/ json/SQL in SignUpHandler");
      }
      return GSON.toJson(variables);
    }
  }

  /**
   * Class which handles logging into our site.
   */
  public static class LoginHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    public LoginHandler(Connection c) {
      this.conn = c;
    }

    @Override
    public Object handle(Request request, Response response) {
      String msg = "";
      boolean isValid = false;
      JSONObject data = null;
      Map<String, Double> presets = new HashMap<>();
      try {
        data = new JSONObject(request.body());
        String inputtedUsername = data.getString("username");
        String inputtedPassword = data.getString("password");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(inputtedUsername.getBytes());
        String hashedPassword = coder.encodeToString(inputtedPassword.getBytes());

        String query = "SELECT * FROM user_data WHERE username = ? AND password = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        prep.setString(2, hashedPassword);
        ResultSet rs = prep.executeQuery();
        msg = "Username/Password Combination Not Found";
        presets.put("crsRatingPref", 2.5);
        presets.put("avgHoursPref", 10.0);
        presets.put("maxHoursPref", 12.0);
        presets.put("crsSizePref", 20.0);
        presets.put("profRatingPref", 2.5);
        if (rs.next()) {
          //found a match
          msg = "Success!";
          isValid = true;
          //username, password, crs, avg, max, size, prof
          double crsRatPref = rs.getDouble(3);
          double avgHrPref = rs.getDouble(4);
          double maxHrPref = rs.getDouble(5);
          double sizePref = rs.getDouble(6);
          double profRatPref = rs.getDouble(7);
          presets.put("crsRatingPref", crsRatPref);
          presets.put("avgHoursPref", avgHrPref);
          presets.put("maxHoursPref", maxHrPref);
          presets.put("crsSizePref", sizePref);
          presets.put("profRatingPref", profRatPref);
        }
      } catch (JSONException | SQLException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.of("isValid", isValid, "message", msg, "presets", presets);
      return GSON.toJson(variables);
    }
  }

  /**
   * Class which handles writing course codes to our database for a given user.
   */
  public static class WriteCourseHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    public WriteCourseHandler(Connection c) {
      this.conn = c;
    }

    @Override
    public Object handle(Request request, Response response) {
      String msg = "";
      JSONObject data = null;
      try {
        data = new JSONObject(request.body());
        String curUser = data.getString("username");
        String colToAppend = data.getString("column");
        String codeToAdd = data.getString("course");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(curUser.getBytes());

        String query = "SELECT "+ colToAppend + " FROM user_data WHERE username = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        ResultSet rs = prep.executeQuery();

        if (rs.next()) {
          String curCourses = rs.getString(1);
          if (curCourses == null) {
            curCourses = "";
          }
          curCourses = curCourses + codeToAdd + ",";
          query = "UPDATE user_data SET " + colToAppend + " = ? WHERE username = ?;";
          prep = conn.prepareStatement(query);
          prep.setString(1, curCourses);
          prep.setString(2, hashedUsername);
          prep.executeUpdate();
          msg = "updated added course";
        } else {
          System.out.println("couldnt find user!!");
          msg = "failed";
        }
      } catch (JSONException | SQLException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.of("msg", msg);
      return GSON.toJson(variables);
    }
  }

  /**
   * Class which handles deleting course codes from our database for a given user.
   */
  public static class RemoveCourseHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    public RemoveCourseHandler(Connection c) {
      this.conn = c;
    }

    @Override
    public Object handle(Request request, Response response) {
      String msg = "";
      JSONObject data = null;
      try {
        data = new JSONObject(request.body());
        String curUser = data.getString("username");
        String colToEdit = data.getString("column");
        String codeToRemove = data.getString("course");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(curUser.getBytes());

        String query = "SELECT " + colToEdit + " FROM user_data WHERE username = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        ResultSet rs = prep.executeQuery();

        if (rs.next()) {
          String curCourses = rs.getString(1);
          if (curCourses == null) {
            curCourses = "";
          }
          curCourses = curCourses.replace(codeToRemove + ",", "");
          query = "UPDATE user_data SET " + colToEdit + " = ? WHERE username = ?;";
          prep = conn.prepareStatement(query);
          prep.setString(1, curCourses);
          prep.setString(2, hashedUsername);
          prep.executeUpdate();
          msg = "updated removed course";
        } else {
          System.out.println("couldnt find user!!");
          msg = "failed";
        }
      } catch (JSONException | SQLException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.of("msg", msg);
      return GSON.toJson(variables);
    }
  }

  /**
   * Class which handles writing preferences for a given user to the user database.
   */
  public static class SetPreferenceHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    public SetPreferenceHandler(Connection c) {
      this.conn = c;
    }
    @Override
    public Object handle(Request request, Response response) {
      String msg = "";
      try {
        JSONObject data = new JSONObject(request.body());
        System.out.println(data);
        String curUser = data.getString("username");
        JSONObject pref = data.getJSONObject("pref");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(curUser.getBytes());
        String query = "UPDATE user_data SET course_rating_pref=?, avg_hrs_pref=?, max_hrs_pref=?, class_size_pref=?, prof_rating_pref=? WHERE username = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setDouble(1, pref.getDouble("crsRatingPref"));
        prep.setDouble(2, pref.getDouble("avgHoursPref"));
        prep.setDouble(3, pref.getDouble("maxHoursPref"));
        prep.setDouble(4, pref.getDouble("crsSizePref"));
        prep.setDouble(5, pref.getDouble("profRatingPref"));
        prep.setString(6, hashedUsername);
        prep.executeUpdate();
        msg = "Success!";
      } catch (JSONException | SQLException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.of("msg", msg);
      return GSON.toJson(variables);
    }
  }

  /**
   * Class which handles loading saved user data.
   */
  public static class LoadUserHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection userConn;
    private Connection courseConn;

    public LoadUserHandler(Connection uDB, Connection cDB) {
      this.userConn = uDB;
      this.courseConn = cDB;
    }

    /*
    idea:
    - get user preferences from udb
    - get user taken classes from udb, split
    - sql select for id in list
    - for each return, map for all data needed for course object
          - name: string;
    dept: string;
    code: string;
    prereqs?: string[];
    description?: string;
    rating?: number;
    latestProf?: string;
    latestProfRating?: number;
    maxHours?: number;
    avgHours?: number;

    - get user saved classes, split
    - for each return, map same business.
     */

    @Override
    public Object handle(Request request, Response response) {
      JSONObject data = null;
      try {
        data = new JSONObject(request.body());
        String curUser = data.getString("username");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(curUser.getBytes());

        String query = "SELECT * FROM user_data WHERE username = ?";
        PreparedStatement prep = userConn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        ResultSet rs = prep.executeQuery();

        if (rs.next()) {
          Map<String, Double> userData = new HashMap<>();
          userData.put("crsRatingPref", rs.getDouble(3));
          userData.put("avgHoursPref", rs.getDouble(4));
          userData.put("maxHoursPref", rs.getDouble(5));
          userData.put("crsSizePref", rs.getDouble(6));
          userData.put("profRatingPref", rs.getDouble(7));
          String svdCourses = rs.getString(8);
          String tknCourses = rs.getString(9);
          Map<String, Object>[] savedCourseInfo = new Map[0];
          Map<String, Object>[] takenCourseInfo = new Map[0];
          if (svdCourses != null) {
            String[] savedCourses = svdCourses.split(",");
            savedCourseInfo = getCourseInfo(savedCourses);
          }
          if (tknCourses != null) {
            String[] takenCourses = tknCourses.split(",");
            takenCourseInfo = getCourseInfo(takenCourses);
          }
          Map<String, Object> variables = ImmutableMap.of("user", userData, "taken", takenCourseInfo, "saved", savedCourseInfo);
          return GSON.toJson(variables);
        } else {
          System.out.println("ERROR: error in LoadUserHandler");
          return null;
        }
      } catch (JSONException | SQLException e) {
        e.printStackTrace();
        return null;
      }
    }

    private Map<String, Object>[] getCourseInfo(String[] courses) {
      Map<String, Map<String, Object>> courseInfo = new HashMap<>();
      try {
        String query = "SELECT * FROM courseData INNER JOIN courseCR ON courseData.id=courseCR.id WHERE (courseData.id = ?";
        for (int i = 1; i < courses.length; i++) {
          query = query + " OR courseData.id = ?";
        }
        query = query + ");";
        PreparedStatement prep = courseConn.prepareStatement(query);
        for (int k = 1; k <= courses.length; k++) {
          prep.setString(k, courses[k - 1]);
        }
        ResultSet rs = prep.executeQuery();
        while (rs.next()) {
          //cols are: id,name,instr,sem,rawprereq,prereq,desc,id,crsrat,profrat,avghr,maxhr,classsz
          Map<String, Object> curCourseInfo = new HashMap<>();
          curCourseInfo.put("name", rs.getString(2));
          curCourseInfo.put("dept", rs.getString(1).substring(0, 4));
          curCourseInfo.put("code", rs.getString(1).substring(5));
          String prereqString = rs.getString(6);
          if (prereqString == null) {
            prereqString = "";
          }
          String[] prereqInfo = prereqString.replaceAll("[&|()]+", ",").split(",");
          prereqInfo = Arrays.stream(prereqInfo).filter(s -> !s.isEmpty()).toArray(String[]::new);
          curCourseInfo.put("prereqs", prereqInfo);
          curCourseInfo.put("description", rs.getString(7));
          curCourseInfo.put("rating", rs.getString(9));
          curCourseInfo.put("latestProf", rs.getString(3));
          curCourseInfo.put("latestProfRating", rs.getString(10));
          curCourseInfo.put("maxHours", rs.getString(12));
          curCourseInfo.put("avgHours", rs.getString(11));
          courseInfo.put(rs.getString(1), curCourseInfo);
        }
        return courseInfo.values().toArray(Map[]::new);
      } catch (SQLException e) {
        e.printStackTrace();
      }
      return courseInfo.values().toArray(Map[]::new);
    }
  }

  /**
   * Class which handles deleting user data.
   */
  public static class DeleteUserHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection userConn;

    public DeleteUserHandler(Connection uDB) {
      this.userConn = uDB;
    }

    @Override
    public Object handle(Request request, Response response) {
      JSONObject data = null;
      try {
        data = new JSONObject(request.body());
        String curUser = data.getString("username");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(curUser.getBytes());

        String query = "DELETE FROM user_data WHERE username = ?";
        PreparedStatement prep = userConn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        prep.executeUpdate();
        Map<String, Object> variables = ImmutableMap.of("msg", "user deleted");
        return GSON.toJson(variables);
      } catch (JSONException | SQLException e) {
        e.printStackTrace();
        return null;
      }
    }
  }
}
