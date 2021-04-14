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
import edu.brown.cs.futureatbrown.termproject.repl.CommandParser;
import edu.brown.cs.futureatbrown.termproject.repl.REPL;

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

//    CommandParser commandParser = new CommandParser();
//    REPL repl = new REPL(commandParser);
//    repl.read(System.in, System.out, System.err);
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
      Spark.post("/getcourses", new CourseDataHandlers.ClassDataHandler(courseDataConn));
      Spark.post("/courseinfo", new CourseDataHandlers.GetCourseHandler(courseDataConn));
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
}
