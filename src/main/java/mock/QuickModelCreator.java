package mock;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import app.Service;
import model.Machine;

public class QuickModelCreator {

  public static void main(String[] args) {
    MongoClient mongoClient = new MongoClient();
    MongoDatabase db = mongoClient.getDatabase("emblem");
    Service s = new Service(db);

    int serialNumber = 350;
    int numberA = 30;
    int numberB = 20;
    // int year = 2001;
    int numberC = 200;

    Random rand = new Random();
    int number = ThreadLocalRandom.current().nextInt(50, 200 + 1);
    int year = ThreadLocalRandom.current().nextInt(1980, 2000 + 1);

    for (int i = 0; i < 50; ++i) {
      Machine m = new Machine("S" + serialNumber, "CUSTOMER" + number, "STATE" + number, "TYPE" + number,
          "MODEL" + number, "03/10/" + year, "STATUS" + number, "PERSON" + number, "PERSON" + number, "NOTE_" + number,
          "05/07/" + year, "BRAND" + number);
      s.insertMachine(m);
      serialNumber++;
      // numberA++;
      // numberB++;
      // numberC++;
      // year++;

      number = ThreadLocalRandom.current().nextInt(50, 200 + 1);
      year = ThreadLocalRandom.current().nextInt(1980, 2000 + 1);
    }

    // for (int i = 0; i < 20; ++i) {
    // MaintenanceHistory mh = new MaintenanceHistory("S200", "W" + serialNumber, "02/08/" + year,
    // "ACTION_TAKEN_" + numberA, "PERSON" + numberA, "TYPE" + numberA);
    // s.insertHistory(mh);
    // serialNumber++;
    // year++;
    // numberA++;
    // }
  }

}
