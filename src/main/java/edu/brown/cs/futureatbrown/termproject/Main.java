package edu.brown.cs.futureatbrown.termproject;

import java.io.File;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.collect.ImmutableMap;
import com.google.gson.Gson;
import joptsimple.OptionParser;
import joptsimple.OptionSet;
import org.json.JSONException;
import org.json.JSONObject;
import spark.Request;
import spark.Response;
import spark.Route;
import spark.Spark;
import spark.template.freemarker.FreeMarkerEngine;
import freemarker.template.Configuration;

/**
 * The Main class of our project. This is where execution begins.
 */
public final class Main {

  /**
   * The initial method called when execution begins.
   *
   * @param args An array of command line arguments
   */
  public static void main(String[] args) {
    new Main(args).run();
  }

  private String[] args;
  private static final int DEFAULT_PORT = 4567;
  private static final String USER_DATA_DB = "data/empty.sqlite3";

  private Main(String[] args) {
    this.args = args;
  }

  private void run() {
    // Parse command line arguments
    OptionParser parser = new OptionParser();
    parser.accepts("gui");
    parser.accepts("port").withRequiredArg().ofType(Integer.class)
      .defaultsTo(DEFAULT_PORT);
    OptionSet options = parser.parse(args);

    if (options.has("gui")) {
      runSparkServer((int) options.valueOf("port"));
    }
  }

  private static FreeMarkerEngine createEngine() {
    Configuration config = new Configuration();
    File templates = new File("src/main/resources/spark/template/freemarker");
    try {
      config.setDirectoryForTemplateLoading(templates);
    } catch (IOException ioe) {
      System.out.printf("ERROR: Unable use %s for template loading.%n", templates);
      System.exit(1);
    }
    return new FreeMarkerEngine(config);
  }

  private void runSparkServer(int port) {
    Spark.port(port);
    Spark.externalStaticFileLocation("frontend/build");

    Spark.options("/*", (request, response) -> {
      String accessControlRequestHeaders = request.headers("Access-Control-Request-Headers");
      if (accessControlRequestHeaders != null) {
        response.header("Access-Control-Allow-Headers", accessControlRequestHeaders);
      }

      String accessControlRequestMethod = request.headers("Access-Control-Request-Method");

      if (accessControlRequestMethod != null) {
        response.header("Access-Control-Allow-Methods", accessControlRequestMethod);
      }

      return "OK";
    });

    String loginDBPath = "data/userDatabase.sqlite3";
    String courseDBPath = "data/courseDatabase.sqlite3";
    try {
      Class.forName("org.sqlite.JDBC");
      String loginDBUrl = "jdbc:sqlite:" + loginDBPath;
      Connection userDataConn = DriverManager.getConnection(loginDBUrl);
      String courseDBUrl = "jdbc:sqlite:" + courseDBPath;
      Connection courseDataConn = DriverManager.getConnection(courseDBUrl);
      Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

      Spark.post("/login", new UserDataHandlers.LoginHandler(userDataConn));
      Spark.post("/signup", new UserDataHandlers.SignUpHandler(userDataConn));
      Spark.post("/checkname", new UserDataHandlers.CheckUsernameHandler(userDataConn));
      Spark.post("/getcourses", new ClassDataHandler(courseDataConn));
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println("Couldnt connect to SQL user data!");
    }
  }


  private static class ClassDataHandler implements Route {
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
//        String query = "SELECT * FROM courseData INNER JOIN courseCR ON courseData.id=courseCR.id WHERE courseData.id LIKE ?;";
        PreparedStatement prep = conn.prepareStatement(query);
//        prep.setString(1, "CSCI%");
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
}



