package mock;

import java.util.Random;
import java.util.concurrent.ThreadLocalRandom;

import com.mongodb.MongoClient;
import com.mongodb.client.MongoDatabase;

import app.Service;

public class QuickModelCreator {

  public static void main(String[] args) throws Exception {
    MongoClient mongoClient = new MongoClient();
    MongoDatabase db = mongoClient.getDatabase("emblem");
    Service s = new Service(db);

    int serialNumber = 801;
    int numberA = 30;
    int numberB = 20;
    // int year = 2001;
    int numberC = 200;

    Random rand = new Random();
    int number = ThreadLocalRandom.current().nextInt(150, 5000 + 1);
    int day = ThreadLocalRandom.current().nextInt(10, 29 + 1);
    int month = ThreadLocalRandom.current().nextInt(10, 12 + 1);
    int year = ThreadLocalRandom.current().nextInt(1980, 2020 + 1);

    // s.createUser("dummy", "1234");
    s.logout("dummy");
    // for (int i = 0; i < 50; ++i) {
    // Machine m = new Machine("S" + serialNumber, "CUSTOMER" + number, "STATE" + number, "TYPE" + number,
    // "MODEL" + number, month + "/" + day + "/" + year, "STATUS" + number, "PERSON" + number, "PERSON" + number,
    // "NOTE_" + number, month + "/" + day + "/" + year, "BRAND" + number);
    // s.insertMachine(m);
    // serialNumber++;
    // // numberA++;
    // // numberB++;
    // // numberC++;
    // // year++;
    //
    // number = ThreadLocalRandom.current().nextInt(150, 1000 + 1);
    // day = ThreadLocalRandom.current().nextInt(10, 29 + 1);
    // month = ThreadLocalRandom.current().nextInt(10, 12 + 1);
    // year = ThreadLocalRandom.current().nextInt(1980, 2020 + 1);
    // }

    // for (int i = 0; i < 50; ++i) {
    // Machine m = new Machine("S" + serialNumber, "CUSTOMER" + number, "STATE" + number, "TYPE" + number,
    // "MODEL" + number, month + "/" + day + "/" + year, "STATUS" + number, "PERSON" + number, "PERSON" + number,
    // "NOTE_" + number, "03/28/2018", "BRAND" + number);
    // s.insertMachine(m);
    // serialNumber++;
    // // numberA++;
    // // numberB++;
    // // numberC++;
    // // year++;
    //
    // number = ThreadLocalRandom.current().nextInt(150, 1000 + 1);
    // day = ThreadLocalRandom.current().nextInt(10, 29 + 1);
    // month = ThreadLocalRandom.current().nextInt(10, 12 + 1);
    // year = ThreadLocalRandom.current().nextInt(1980, 2020 + 1);
    // }

    // for (int j = 410; j <= 420; j++) {
    // for (int i = 0; i < 51; ++i) {
    // MaintenanceHistory mh = new MaintenanceHistory("S" + j, "W" + serialNumber, month + "/" + day + "/" + year,
    // "ACTION_TAKEN_" + number, "PERSON" + number, "TYPE" + number);
    // s.insertHistory(mh);
    // serialNumber++;
    // number = ThreadLocalRandom.current().nextInt(150, 1000 + 1);
    // day = ThreadLocalRandom.current().nextInt(10, 29 + 1);
    // month = ThreadLocalRandom.current().nextInt(10, 12 + 1);
    // year = ThreadLocalRandom.current().nextInt(1980, 2020 + 1);
    //
    // System.out.println("W" + serialNumber + " DONE");
    // }
    // System.out.println("# S" + j + " DONE");
    // serialNumber = 801;
    // }

    // List<Machine> m = s.getMachine(MachineEnum.SERIAL_NUM.getProp(), "S200");
    // m.get(0).setHistoryCount(new Long(124));
    // try {
    // s.updateMachine(m.get(0));
    // } catch (Exception e) {
    // // TODO Auto-generated catch block
    // e.printStackTrace();
    // }

    // System.out.println(CustomDateTimeFormatter.getNow());
    // System.out.println((CustomDateTimeFormatter.convertLongToDateString2(CustomDateTimeFormatter.getNow())));
    //
    // String checkDate = "03/20/2018";
    // Date checkDate2 = CustomDateTimeFormatter.convertStringToDate2(checkDate);
    // String date1 = "04/04/2018";
    // Date date2 = CustomDateTimeFormatter.convertStringToDate2(date1);
    // long twoWeeksBefore = CustomDateTimeFormatter.getXDaysBeforeDate(date2, 15);
    // String twoWeeksBefore2 = CustomDateTimeFormatter.convertLongToDateString2(twoWeeksBefore);
    // Date twoWeeksBefore3 = CustomDateTimeFormatter.convertStringToDate2(twoWeeksBefore2);
    // long twoWeeksAfter = CustomDateTimeFormatter.getXDaysAfterDate(date2, 15);
    // String twoWeeksAfter2 = CustomDateTimeFormatter.convertLongToDateString2(twoWeeksAfter);
    // Date twoWeeksAfter3 = CustomDateTimeFormatter.convertStringToDate2(twoWeeksAfter2);
    //
    // System.out.println(twoWeeksBefore2);
    // System.out.println(date1);
    // System.out.println(twoWeeksAfter2);
    //
    // System.out.println(checkDate2.after(twoWeeksBefore3) && checkDate2.before(twoWeeksAfter3));

    // Machine m2 = new Machine();
    // m2.setSerialNumber("SWS005");
    // m2.setPpmDate("03/20/2018");
    // s.insertMachine(m2);
    //
    // ResultWrapper ms = s.getAllMachines();
    //
    // for (Machine m : ms.getMachines()) {
    // // m.setDateOfCreation(CustomDateTimeFormatter.convertLongToDateString(new Long(m.getDateOfCreation())));
    // // m.setLastUpdated(CustomDateTimeFormatter.convertLongToDateString(new Long(m.getLastUpdated())));
    // // s.updateMachine(m);
    // // System.out.println(m.getPpmDate());
    // // System.out.println(m.getPpmDateInDate());
    // // System.out.println(m.getTncDate());
    // // System.out.println(m.getTncDateInDate());
    // // System.out.println(CustomDateTimeFormatter.formatDate2(m.getPpmDate()));
    // // m.setPpmDate(CustomDateTimeFormatter.formatDate2(m.getPpmDate()));
    // // m.setTncDate(CustomDateTimeFormatter.formatDate2(m.getTncDate()));
    // //
    //
    // // String[] tokens = m.getPpmDate().split("/");
    // // System.out.println(tokens[2] + tokens[0] + tokens[1]);
    //
    // // System.out.println(m);
    //
    // // System.out
    // // .println("# " + m.getPpmDate() + " vs. " + CustomDateTimeFormatter.convertStringToDate2(m.getPpmDate()));
    //
    // m.setHistoryCount(new Long(0));
    // }
  }

}
