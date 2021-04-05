package edu.brown.cs.futureatbrown.termproject;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;

public class UserDataHandlers {

  public static class SignUpHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection conn;

    public SignUpHandler(String loginDBPath) {
      try {
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + loginDBPath;
        conn = DriverManager.getConnection(urlToDB);
      } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
      }
    }

    @Override
    public Object handle(Request request, Response response) {
      Map<String, Object> variables;
      try {
        JSONObject data = new JSONObject(request.body());

        String inputtedUsername = data.getString("username");
        String inputtedPassword = data.getString("password");

        String alphanumRegex = "^[a-zA-Z0-9_]+$";

        Base64.Encoder coder = Base64.getEncoder();
        String hashedUsername = coder.encodeToString(inputtedUsername.getBytes());
        String hashedPassword = coder.encodeToString(inputtedPassword.getBytes());

        String query = "SELECT * FROM user_data WHERE username = ?;";
        PreparedStatement prep = conn.prepareStatement(query);
        prep.setString(1, hashedUsername);
        ResultSet rs = prep.executeQuery();
        String msg;
        Map<String, Double> presets = new HashMap<>();
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

        if (rs.next()) {
          //found a match
          msg = "Username already taken!";
        } else {
          msg = "Account Added!";
          query = "INSERT INTO user_data VALUES (?, ?, ?, ?, ?, ?, ?);";
          prep = conn.prepareStatement(query);
          prep.setString(1, hashedUsername);
          prep.setString(2, hashedPassword);
          prep.setDouble(3, crsRatingPref);
          prep.setDouble(4, avgHoursPref);
          prep.setDouble(5, maxHoursPref);
          prep.setDouble(6, crsSizePref);
          prep.setDouble(7, profRatingPref);
          prep.executeQuery();
        }

        variables = ImmutableMap.of("message", msg, "presets", presets);
      } catch (JSONException | SQLException e) {
        variables = ImmutableMap.of("message", "ERROR: SignUpHandler", "presets", null);
        System.out.println("ERROR: in UserDataHandlers.java - error w/ json/SQL in SignUpHandler");
      }
      return GSON.toJson(variables);
    }
  }

  public static class LoginHandler implements Route {
    private static final Gson GSON = new Gson();
    private final String userDataPath;
    private Connection conn;

    public LoginHandler(String loginDBPath) {
      userDataPath = loginDBPath;
      try {
        Class.forName("org.sqlite.JDBC");
        String urlToDB = "jdbc:sqlite:" + loginDBPath;
        conn = DriverManager.getConnection(urlToDB);
      } catch (ClassNotFoundException | SQLException e) {
        e.printStackTrace();
      }
    }

    @Override
    public Object handle(Request request, Response response) throws Exception {
      JSONObject data = new JSONObject(request.body());
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
      String msg = "Username/Password Combination Not Found";
      Map<String, Double> presets = new HashMap<>();
      presets.put("crsRatingPref", 2.5);
      presets.put("avgHoursPref", 10.0);
      presets.put("maxHoursPref", 12.0);
      presets.put("crsSizePref", 20.0);
      presets.put("profRatingPref", 2.5);

      if (rs.next()) {
        //found a match
        msg = "Success!";
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

      Map<String, Object> variables = ImmutableMap.of("message", msg, "presets", presets);
      return GSON.toJson(variables);
    }
  }
}
