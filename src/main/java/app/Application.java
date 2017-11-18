package app;

import static spark.Spark.staticFiles;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

public class Application {

  public static void main(String[] args) throws Exception {
    staticFiles.location("/public");
    new Resource(new Service(connect()));
  }

  public static MongoDatabase connect() {
    MongoClient mongoClient = new MongoClient();
    MongoDatabase db = mongoClient.getDatabase("emblem");
    return db;
  }

}
