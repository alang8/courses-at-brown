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
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UserDataHandlers {

  /**
   * Class which ensures that a username isnt yet taken for our site.
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
        System.out.println("ERROR: in UserDataHandlers.java - error w/ json/SQL in SignUpHandler");
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


        double crsRatingPref = 2.5;
        double avgHoursPref = 10.0;
        double maxHoursPref = 12.0;
        double crsSizePref = 20.0;
        double profRatingPref = 2.5;
        presets.put("crsRatingPref", crsRatingPref);
        presets.put("avgHoursPref", avgHoursPref);
        presets.put("maxHoursPref", maxHoursPref);
        presets.put("crsSizePref", crsSizePref);
        presets.put("profRatingPref", profRatingPref);
        msg = "Account Added!";
        String query = "INSERT INTO user_data VALUES (?, ?, ?, ?, ?, ?, ?);";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        prep.setString(2, hashedPassword);
        prep.setDouble(3, crsRatingPref);
        prep.setDouble(4, avgHoursPref);
        prep.setDouble(5, maxHoursPref);
        prep.setDouble(6, crsSizePref);
        prep.setDouble(7, profRatingPref);
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

        String query = "SELECT ? FROM user_data WHERE username = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, colToAppend);
        prep.setString(2, hashedUsername);
        ResultSet rs = prep.executeQuery();

        if (rs.next()) {
          String curCourses = rs.getString(1);
          if (curCourses == null) {
            curCourses = "";
          }
          curCourses = curCourses + codeToAdd + ",";
          query = "UPDATE user_data SET ? = ? WHERE username = ?;";
          prep = conn.prepareStatement(query);
          prep.setString(1, colToAppend);
          prep.setString(2, curCourses);
          prep.setString(3, hashedUsername);
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

        String query = "SELECT ? FROM user_data WHERE username = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, colToEdit);
        prep.setString(2, hashedUsername);
        ResultSet rs = prep.executeQuery();

        if (rs.next()) {
          String curCourses = rs.getString(1);
          if (curCourses == null) {
            curCourses = "";
          }
          curCourses = curCourses.replace(codeToRemove + ",", "");
          query = "UPDATE user_data SET ? = ? WHERE username = ?;";
          prep = conn.prepareStatement(query);
          prep.setString(1, colToEdit);
          prep.setString(2, curCourses);
          prep.setString(3, hashedUsername);
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
   * Class which handles writing course codes to our database for a given user.
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
      JSONObject data = null;
      try {
        data = new JSONObject(request.body());
        String curUser = data.getString("username");
        String preference = data.getString("pref");
        double newVal = data.getDouble("value");
        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(curUser.getBytes());

        String colToEdit = "";
        switch (preference) {
          case "avgHoursPref":
            colToEdit = "course_rating_pref";
            break;
          case "crsRatingPref":
            colToEdit = "avg_hrs_pref";
            break;
          case "maxHoursPref":
            colToEdit = "max_hrs_pref";
            break;
          case "crsSizePref":
            colToEdit = "class_size_pref";
            break;
          case "profRatingPref":
            colToEdit = "prof_rating_pref";
            break;
          default:
            System.out.println("ERROR: Error in SetPreferenceHandler, col was " + preference);
        }

        String query = "UPDATE user_data SET ?=? WHERE username = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, colToEdit);
        prep.setDouble(2, newVal);
        prep.setString(3, hashedUsername);
        prep.executeUpdate();
        msg = "Success!";
      } catch (JSONException | SQLException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.of("msg", msg);
      return GSON.toJson(variables);
    }
  }
}
