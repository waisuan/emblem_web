package mock;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import app.Service;
import model.Machine;

public class QuickModelCreator {

  public static void main(String[] args) {
    MongoClient mongoClient = new MongoClient();
    MongoDatabase db = mongoClient.getDatabase("emblem");
    Service s = new Service(db);

    int serialNumber = 300;
    int numberA = 30;
    int numberB = 20;
    int year = 2001;
    int numberC = 200;

    for (int i = 0; i < 10; ++i) {
      Machine m = new Machine("S" + serialNumber, "CUSTOMER" + numberA, "STATE" + numberA, "TYPE" + numberA,
          "MODEL" + numberB, "03/10/" + year, "STATUS" + numberA, "PERSON" + numberB, "PERSON" + numberA,
          "NOTE_" + numberC, "05/07/" + year, "BRAND" + numberB);
      s.insertMachine(m);
      serialNumber++;
      numberA++;
      numberB++;
      numberC++;
      year++;
    }
  }

}
