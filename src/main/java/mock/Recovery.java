package mock;

import java.util.Date;

import org.bson.Document;

import com.mongodb.MongoClient;
import com.mongodb.MongoClientURI;
import com.mongodb.client.MongoDatabase;

import model.Machine;
import util.CustomDateTimeFormatter;
import util.MachineEnum;
import util.UserEnum;

public class Recovery {

  private MongoDatabase getMlabDB() {
    MongoClientURI mongodb_uri = new MongoClientURI(
        "mongodb://heroku_pfmld8dw:984qi6gcbd27p8f8ulcsdq69dm@ds123929.mlab.com:23929/heroku_pfmld8dw");
    String dbName = "heroku_pfmld8dw"; // TODO this should prolly be in a properties file
    MongoClient mongoClient = new MongoClient(mongodb_uri);
    MongoDatabase db = mongoClient.getDatabase(dbName);
    return db;
  }

  private void prepMachine(Machine machine, boolean isGet, boolean isInsert, boolean isUpdate) {
    long currTimestamp = CustomDateTimeFormatter.generateTimestamp();

    if (machine.getHistoryCount() == null) {
      machine.setHistoryCount(new Long(0));
    }

    machine.setDueForPPM(new Integer(0));
    machine.setDueStatus("");

    if (machine.getPpmDate() != null && !machine.getPpmDate().isEmpty()) {
      if (machine.getPpmDateInString() == null || isInsert || isUpdate) {
        System.out.println(machine.getPpmDate());
        machine.setPpmDateInString(CustomDateTimeFormatter.convertStringToYYYYMMDD(machine.getPpmDate()));
      }

      if (machine.getPpmDateInDate() == null || isInsert || isUpdate) {
        machine.setPpmDateInDate(CustomDateTimeFormatter.convertStringToDate2(machine.getPpmDate()));
      }

      if (machine.getOverdue() == null || machine.getAlmostDue() == null || isInsert || isUpdate) {
        machine.setAlmostDue(CustomDateTimeFormatter.convertStringToDate2(CustomDateTimeFormatter
            .convertLongToDateString2(CustomDateTimeFormatter.getXDaysBeforeDate(machine.getPpmDateInDate(), 15))));
        machine.setOverdue(CustomDateTimeFormatter.convertStringToDate2(CustomDateTimeFormatter
            .convertLongToDateString2(CustomDateTimeFormatter.getXDaysAfterDate(machine.getPpmDateInDate(), 15))));
      }

      if (isGet) {
        String todayInString = CustomDateTimeFormatter.convertLongToDateString2(CustomDateTimeFormatter.getNow());
        Date today = CustomDateTimeFormatter.convertStringToDate2(todayInString);

        if (today.after(machine.getAlmostDue()) && today.before(machine.getOverdue())) {
          if (todayInString.equals(machine.getPpmDate())) {
            machine.setDueStatus("DUE NOW");
            machine.setDueForPPM(new Integer(1));
          } else if (today.before(machine.getPpmDateInDate())) {
            machine.setDueStatus("ALMOST DUE");
            machine.setDueForPPM(new Integer(2));
          } else if (today.after(machine.getPpmDateInDate())) {
            machine.setDueStatus("OVERDUE");
            machine.setDueForPPM(new Integer(3));
          }
        }
      }
    }

    if (machine.getTncDate() != null && !machine.getTncDate().isEmpty()) {
      if (machine.getTncDateInString() == null || isInsert || isUpdate) {
        machine.setTncDateInString(CustomDateTimeFormatter.convertStringToYYYYMMDD(machine.getTncDate()));
      }
      if (machine.getTncDateInDate() == null || isInsert || isUpdate) {
        machine.setTncDateInDate(CustomDateTimeFormatter.convertStringToDate2(machine.getTncDate()));
      }
    }

    if (isInsert) {
      // machine.setDateOfCreation(CustomDateTimeFormatter.convertLongToDateString(currTimestamp));
    }

    if (isInsert || isUpdate) {
      // machine.setLastUpdated(CustomDateTimeFormatter.convertLongToDateString(currTimestamp));
      // machine.setLastUpdatedInLong(currTimestamp);
    }
  }

  private Document constructMachineForInsert(Machine machine) {
    Document document = new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber())
        .append(MachineEnum.CUSTOMER.getDBEnum(), machine.getCustomer())
        .append(MachineEnum.STATE.getDBEnum(), machine.getState())
        .append(MachineEnum.ACCOUNT_TYPE.getDBEnum(), machine.getAccountType())
        .append(MachineEnum.MODEL.getDBEnum(), machine.getModel())
        .append(MachineEnum.TNC_DATE.getDBEnum(), machine.getTncDate())
        .append(MachineEnum.STATUS.getDBEnum(), machine.getStatus())
        .append(MachineEnum.PERSON_IN_CHARGE.getDBEnum(), machine.getPersonInCharge())
        .append(MachineEnum.REPORTED_BY.getDBEnum(), machine.getReportedBy())
        .append(MachineEnum.ADDITIONAL_NOTES.getDBEnum(), machine.getAdditionalNotes())
        .append(MachineEnum.PPM_DATE.getDBEnum(), machine.getPpmDate())
        .append(MachineEnum.BRAND.getDBEnum(), machine.getBrand())
        .append(MachineEnum.DATE_OF_CREATION.getDBEnum(), machine.getDateOfCreation())
        .append(MachineEnum.LAST_UPDATED.getDBEnum(), machine.getLastUpdated())
        .append(MachineEnum.LAST_UPDATED_IN_LONG.getDBEnum(), machine.getLastUpdatedInLong())
        .append(MachineEnum.HISTORY_COUNT.getDBEnum(), machine.getHistoryCount())
        .append(MachineEnum.ALMOST_DUE.getDBEnum(), machine.getAlmostDue())
        .append(MachineEnum.OVERDUE.getDBEnum(), machine.getOverdue())
        .append(MachineEnum.DUE_FOR_PPM.getDBEnum(), machine.getDueForPPM())
        .append(MachineEnum.PPM_DATE_IN_DATE.getDBEnum(), machine.getPpmDateInDate())
        .append(MachineEnum.TNC_DATE_IN_DATE.getDBEnum(), machine.getTncDateInDate())
        .append(MachineEnum.DUE_STATUS.getDBEnum(), machine.getDueStatus())
        .append(MachineEnum.PPM_DATE_IN_STRING.getDBEnum(), machine.getPpmDateInString())
        .append(MachineEnum.TNC_DATE_IN_STRING.getDBEnum(), machine.getTncDateInString())
        .append(MachineEnum.DATE_OF_CREATION_IN_LONG.getDBEnum(), machine.getDateOfCreationInLong());
    return document;
  }

  public void run() {
    MongoDatabase mlabDb = getMlabDB();
    // MongoClient mongoClient = new MongoClient();
    // MongoDatabase db = mongoClient.getDatabase("emblem");
    //
    // FindIterable<Document> iterable = db.getCollection("outdated_machines").find()
    // .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));
    //
    // List<Machine> machines = new ArrayList<Machine>();
    //
    // iterable.forEach(new Block<Document>() {
    // @Override
    // public void apply(final Document document) {
    //
    // Machine machine = new Machine(document.getString(MachineEnum.SERIAL_NUM.getDBEnum()),
    // document.getString(MachineEnum.CUSTOMER.getDBEnum()), document.getString(MachineEnum.STATE.getDBEnum()),
    // document.getString(MachineEnum.ACCOUNT_TYPE.getDBEnum()), document.getString(MachineEnum.MODEL.getDBEnum()),
    // CustomDateTimeFormatter.formatDate2(document.getString(MachineEnum.TNC_DATE.getDBEnum())),
    // document.getString(MachineEnum.STATUS.getDBEnum()),
    // document.getString(MachineEnum.PERSON_IN_CHARGE.getDBEnum()),
    // document.getString(MachineEnum.REPORTED_BY.getDBEnum()),
    // document.getString(MachineEnum.ADDITIONAL_NOTES.getDBEnum()),
    // CustomDateTimeFormatter.formatDate2(document.getString(MachineEnum.PPM_DATE.getDBEnum())),
    // document.getString(MachineEnum.BRAND.getDBEnum()),
    // CustomDateTimeFormatter.convertLongToDateString2(document.getLong(MachineEnum.LAST_UPDATED.getDBEnum())),
    // CustomDateTimeFormatter
    // .convertLongToDateString2(document.getLong(MachineEnum.DATE_OF_CREATION.getDBEnum())),
    // document.getLong(MachineEnum.HISTORY_COUNT.getDBEnum()),
    // document.getLong(MachineEnum.LAST_UPDATED_IN_LONG.getDBEnum()),
    // document.getDate(MachineEnum.ALMOST_DUE.getDBEnum()), document.getDate(MachineEnum.OVERDUE.getDBEnum()),
    // document.getInteger(MachineEnum.DUE_FOR_PPM.getDBEnum()),
    // document.getDate(MachineEnum.TNC_DATE_IN_DATE.getDBEnum()),
    // document.getDate(MachineEnum.PPM_DATE_IN_DATE.getDBEnum()),
    // document.getString(MachineEnum.DUE_STATUS.getDBEnum()),
    // document.getString(MachineEnum.PPM_DATE_IN_STRING.getDBEnum()),
    // document.getString(MachineEnum.TNC_DATE_IN_STRING.getDBEnum()),
    // document.getLong(MachineEnum.DATE_OF_CREATION_IN_LONG.getDBEnum()));
    //
    // machine.setDateOfCreation(CustomDateTimeFormatter.convertLongToDateString(CustomDateTimeFormatter
    // .convertDateToLong(CustomDateTimeFormatter.convertStringToDate2((machine.getDateOfCreation())))));
    // machine.setDateOfCreationInLong(CustomDateTimeFormatter
    // .convertDateToLong(CustomDateTimeFormatter.convertStringToDate2((machine.getDateOfCreation()))));
    // machine.setLastUpdated(CustomDateTimeFormatter.convertLongToDateString(CustomDateTimeFormatter
    // .convertDateToLong(CustomDateTimeFormatter.convertStringToDate2((machine.getLastUpdated())))));
    // machine.setLastUpdatedInLong(CustomDateTimeFormatter
    // .convertDateToLong(CustomDateTimeFormatter.convertStringToDate2((machine.getLastUpdated()))));
    // prepMachine(machine, true, false, false);
    //
    // // db.getCollection("prod_machines").insertOne(constructMachineForInsert(machine));
    // mlabDb.getCollection("machines").insertOne(constructMachineForInsert(machine));
    // }
    // });

    mlabDb.getCollection("users")
        .insertOne(new Document(UserEnum.USERNAME.getDBEnum(), "admin").append(UserEnum.PASSWORD.getDBEnum(), "9999"));
    mlabDb.getCollection("users")
        .insertOne(new Document(UserEnum.USERNAME.getDBEnum(), "user1").append(UserEnum.PASSWORD.getDBEnum(), "1234"));
    mlabDb.getCollection("users")
        .insertOne(new Document(UserEnum.USERNAME.getDBEnum(), "user2").append(UserEnum.PASSWORD.getDBEnum(), "1234"));
    mlabDb.getCollection("users")
        .insertOne(new Document(UserEnum.USERNAME.getDBEnum(), "user3").append(UserEnum.PASSWORD.getDBEnum(), "1234"));
    mlabDb.getCollection("users")
        .insertOne(new Document(UserEnum.USERNAME.getDBEnum(), "user4").append(UserEnum.PASSWORD.getDBEnum(), "1234"));
    mlabDb.getCollection("users")
        .insertOne(new Document(UserEnum.USERNAME.getDBEnum(), "user5").append(UserEnum.PASSWORD.getDBEnum(), "1234"));

    // ClassLoader classLoader = getClass().getClassLoader();
    // File file = new File(classLoader.getResource("database_bak.json").getFile());
    // Gson gson = new Gson();
    //
    // try (BufferedReader br = new BufferedReader(new FileReader(file))) {
    // String line;
    // while ((line = br.readLine()) != null) {
    // // Read line/string as JSON and ultimately converts string into a <Customer> Object.
    // // customers.add(gson.fromJson(line, Customer.class));
    // // System.out.println(line);
    // }
    // } catch (IOException e) {
    // e.printStackTrace();
    // }
  }

  public static void main(String[] args) {
    Recovery r = new Recovery();
    r.run();
  }
}
