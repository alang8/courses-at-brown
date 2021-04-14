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
      //Setting up database connections.
      Class.forName("org.sqlite.JDBC");
      String loginDBUrl = "jdbc:sqlite:" + loginDBPath;
      Connection userDataConn = DriverManager.getConnection(loginDBUrl);
      String courseDBUrl = "jdbc:sqlite:" + courseDBPath;
      Connection courseDataConn = DriverManager.getConnection(courseDBUrl);
      Spark.before((request, response) -> response.header("Access-Control-Allow-Origin", "*"));

      /*
      Database d = new Database();
      d.setupGraph();
      CourseGraph g = d.getGraph();
      GraphAlgorithms graphAlg = new GraphAlgorithms();
       */

      //Setting up spark routes.
      Spark.post("/login", new UserDataHandlers.LoginHandler(userDataConn));
      Spark.post("/signup", new UserDataHandlers.SignUpHandler(userDataConn));
      Spark.post("/checkname", new UserDataHandlers.CheckUsernameHandler(userDataConn));
      Spark.post("/getcourses", new ClassDataHandler(courseDataConn));
      Spark.post("/courseinfo", new GetCourseHandler(courseDataConn));
      Spark.post("/writecourse", new UserDataHandlers.WriteCourseHandler(userDataConn));
      Spark.post("/removecourse", new UserDataHandlers.RemoveCourseHandler(userDataConn));
      Spark.post("/setpreference", new UserDataHandlers.SetPreferenceHandler(userDataConn));
      Spark.post("/loaduser", new UserDataHandlers.LoadUserHandler(userDataConn, courseDataConn));
      Spark.post("/deleteuser", new UserDataHandlers.DeleteUserHandler(userDataConn));
      Spark.post("/path", new GetPathHandler(courseDataConn));
    } catch (ClassNotFoundException | SQLException e) {
      System.out.println("Couldnt connect to SQL user data!");
    }
  }

  /**
   * Handler for getting the Path for a user.
   */
  private static class GetPathHandler implements Route {
    private static final Gson GSON = new Gson();
    private Connection cConn;

    GetPathHandler(Connection c) {
      this.cConn = c;
    }

    @Override
    public Object handle(Request request, Response response) {
      Map<String, Integer> thePath = new HashMap<>();
      try {
        JSONObject data = new JSONObject(request.body());
        JSONObject prefJSON = data.getJSONObject("prefs");
        System.out.println("in getpath handler");
        System.out.println(prefJSON);
        System.out.println(prefJSON.getDouble("avgHoursPref"));
        double aHP = prefJSON.getDouble("avgHoursPref");
        double mHP = prefJSON.getDouble("maxHoursPref");
        double cRP = prefJSON.getDouble("crsRatingPref");
        double pRP = prefJSON.getDouble("profRatingPref");
        double cSP = prefJSON.getDouble("crsSizePref");
        double aHI = aHP / 5.0 * 7.5;
        double mHI = mHP / 5.0 * 13.8;
        double cSI = cSP / 5.0 * 72.4;
        int minNumCourses = 0;
        int maxNumCourses = 26;

        String conc = data.getString("concentration");
        String query = "SELECT SUM(num_courses) FROM " + conc + "Groups;";
        PreparedStatement prep = cConn.prepareStatement(query);
        ResultSet rs = prep.executeQuery();

        double bFP = 5;
        int cSM = 400;

        if (rs.next()) {
          minNumCourses = rs.getInt(1);
          maxNumCourses = Math.min(2 * minNumCourses, 32);
        } else {
          System.out.println("ERROR: GetPathHandler, couldn't find table for " + conc);
        }

        /*
          averages
          avg hours : 7.53125
          max hours : 13.755
          class size: 72.3703703703704
           */
        /*

          Map<String, Integer> thePath = new HashMap<>();
          g.setupGlobalParams(cRP, pRP, aHI, minNumCourses, maxNumCourses, bFP, mHP, mHI, cSP, cSI, cSM, conc);
          List<List<CourseEdge>> paths = graphAlg.pathway(introCourses, g)
          List<CourseEdge> bestPath = paths.get(0);
          int currentOverallSem = 0;
          int prevCourseSem = 0;
          CourseEdge curEdge = bestPath.get(0);

          //TODO will the list of edges be in "order"? if so...
          CourseNode curNode;
          for(CourseEdge c : bestPath) {
            CourseNode curNode = c.getStart();
            if(curNode.getSem() != prevCourseSem && curNode.getSem() != 0) {
              currentOverallSem += 1;
            }
            thePath.put(curNode.getID(), currentOverallSem);
            //TODO assumes that consecutive edges share start / end
          }

          //once at end, need to include final node. TODO if thats how its formatted
          curNode = c.getEnd();
          if(curNode.getSem() != prevCourseSem && curNode.getSem() != 0) {
              currentOverallSem += 1;
          }
          thePath.put(curNode.getID(), currentOverallSem);
         */
        thePath.put("CSCI 0170", 0);
        thePath.put("CSCI 0180", 1);
        thePath.put("MATH 0540", 1);
        thePath.put("CSCI 0220", 2);
        thePath.put("APMA 0350", 2);
      } catch (JSONException | SQLException e) {
        e.printStackTrace();
      }
      Map<String, Object> variables = ImmutableMap.of("path", thePath);
      return GSON.toJson(variables);
    }
  }

  /**
   * Handler for getting all the course data from the database.
   */
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
  private static class GetCourseHandler implements Route {
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
