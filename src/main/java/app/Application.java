package app;

import static spark.Spark.port;
import static spark.Spark.staticFiles;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

public class Application {

  public static void main(String[] args) throws Exception {
    port(getHerokuAssignedPort());
    staticFiles.location("/public");
    new Resource(new Service(connect()));
  }

  public static MongoDatabase connect() {
    MongoClientURI mongodb_uri = new MongoClientURI("mongodb://localhost:27017/");
    String dbName = "emblem"; // TODO this should prolly be in a properties file
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("MONGODB_URI") != null) {
      mongodb_uri = new MongoClientURI(processBuilder.environment().get("MONGODB_URI"));
    }
    if (processBuilder.environment().get("MONGODB_DB") != null) {
      dbName = processBuilder.environment().get("MONGODB_DB");
    }
    MongoClient mongoClient = new MongoClient(mongodb_uri);
    MongoDatabase db = mongoClient.getDatabase(dbName);
    return db;
  }

  public static int getHerokuAssignedPort() {
    ProcessBuilder processBuilder = new ProcessBuilder();
    if (processBuilder.environment().get("PORT") != null) {
      return Integer.parseInt(processBuilder.environment().get("PORT"));
    }
    return 4567; // return default port if heroku-port isn't set (i.e. on localhost)
  }

}
