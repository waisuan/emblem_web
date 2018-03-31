package app;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.BasicDBObject;
import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import exception.RecordAlreadyExistsException;
import exception.RecordDoesNotExistException;
import exception.RecordWasRecentlyUpdatedException;
import model.Machine;
import model.MaintenanceHistory;
import model.ResultWrapper;
import model.User;
import util.CustomDateTimeFormatter;
import util.MachineEnum;
import util.MaintenanceEnum;
import util.UserEnum;

public class Service {
  private final MongoDatabase db;

  public final static String MACHINE_COLLECTION = "machines";
  public final static String MAINTENANCE_COLLECTION = "maintenance";
  public final static String USERS_COLLECTION = "users";

  public Service(MongoDatabase mongoDatabase) {
    this.db = mongoDatabase;
  }

  public User login(String username, String password) throws Exception {
    Document record = db.getCollection(USERS_COLLECTION)
        .find(new Document("username", username).append("password", password)).first();

    if (record == null) {
      throw new RecordDoesNotExistException();
    }

    User user = new User(record.getBoolean(UserEnum.CAN_CREATE.getDBEnum(), false),
        record.getBoolean(UserEnum.CAN_EDIT.getDBEnum(), false),
        record.getBoolean(UserEnum.CAN_DELETE.getDBEnum(), false),
        record.getBoolean(UserEnum.CAN_SEARCH.getDBEnum(), false),
        record.getBoolean(UserEnum.IS_ADMIN.getDBEnum(), false),
        record.getBoolean(UserEnum.ALREADY_LOGGED_ON.getDBEnum(), false),
        record.getString(UserEnum.DATE_OF_CREATION.getDBEnum()), record.getString(UserEnum.LAST_UPDATED.getDBEnum()),
        record.getLong(UserEnum.LAST_UPDATED_IN_LONG.getDBEnum()));

    if (user.isAlreadyLoggedOn()) {
      throw new RecordAlreadyExistsException();
    }

    db.getCollection(USERS_COLLECTION).updateOne(new Document(UserEnum.USERNAME.getDBEnum(), username),
        new Document().append("$set", new Document().append(UserEnum.ALREADY_LOGGED_ON.getDBEnum(), true)));

    return user;
  }

  public void logout(String username) throws Exception {
    Document record = db.getCollection(USERS_COLLECTION).find(new Document("username", username)).first();

    if (record == null) {
      throw new RecordDoesNotExistException();
    }

    db.getCollection(USERS_COLLECTION).updateOne(new Document(UserEnum.USERNAME.getDBEnum(), username),
        new Document().append("$set", new Document().append(UserEnum.ALREADY_LOGGED_ON.getDBEnum(), false)));
  }

  public void createUser(String username, String password) throws Exception {
    db.getCollection(USERS_COLLECTION).insertOne(
        new Document(UserEnum.USERNAME.getDBEnum(), username).append(UserEnum.PASSWORD.getDBEnum(), password));
  }

  private Machine constructMachineForGet(Document document) {
    Machine machine = new Machine(document.getString(MachineEnum.SERIAL_NUM.getDBEnum()),
        document.getString(MachineEnum.CUSTOMER.getDBEnum()), document.getString(MachineEnum.STATE.getDBEnum()),
        document.getString(MachineEnum.ACCOUNT_TYPE.getDBEnum()), document.getString(MachineEnum.MODEL.getDBEnum()),
        document.getString(MachineEnum.TNC_DATE.getDBEnum()), document.getString(MachineEnum.STATUS.getDBEnum()),
        document.getString(MachineEnum.PERSON_IN_CHARGE.getDBEnum()),
        document.getString(MachineEnum.REPORTED_BY.getDBEnum()),
        document.getString(MachineEnum.ADDITIONAL_NOTES.getDBEnum()),
        document.getString(MachineEnum.PPM_DATE.getDBEnum()), document.getString(MachineEnum.BRAND.getDBEnum()),
        document.getString(MachineEnum.LAST_UPDATED.getDBEnum()),
        document.getString(MachineEnum.DATE_OF_CREATION.getDBEnum()),
        document.getLong(MachineEnum.HISTORY_COUNT.getDBEnum()),
        document.getLong(MachineEnum.LAST_UPDATED_IN_LONG.getDBEnum()),
        document.getDate(MachineEnum.ALMOST_DUE.getDBEnum()), document.getDate(MachineEnum.OVERDUE.getDBEnum()),
        document.getInteger(MachineEnum.DUE_FOR_PPM.getDBEnum()),
        document.getDate(MachineEnum.TNC_DATE_IN_DATE.getDBEnum()),
        document.getDate(MachineEnum.PPM_DATE_IN_DATE.getDBEnum()),
        document.getString(MachineEnum.DUE_STATUS.getDBEnum()),
        document.getString(MachineEnum.PPM_DATE_IN_STRING.getDBEnum()),
        document.getString(MachineEnum.TNC_DATE_IN_STRING.getDBEnum()),
        document.getLong(MachineEnum.DATE_OF_CREATION_IN_LONG.getDBEnum()));
    return machine;
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

  private Document constructMachineForUpdate(Machine machine) {
    Document document = new Document().append("$set",
        new Document().append(MachineEnum.CUSTOMER.getDBEnum(), machine.getCustomer())
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
            .append(MachineEnum.LAST_UPDATED.getDBEnum(), machine.getLastUpdated())
            .append(MachineEnum.LAST_UPDATED_IN_LONG.getDBEnum(), machine.getLastUpdatedInLong())
            .append(MachineEnum.ALMOST_DUE.getDBEnum(), machine.getAlmostDue())
            .append(MachineEnum.OVERDUE.getDBEnum(), machine.getOverdue())
            .append(MachineEnum.DUE_FOR_PPM.getDBEnum(), machine.getDueForPPM())
            .append(MachineEnum.PPM_DATE_IN_DATE.getDBEnum(), machine.getPpmDateInDate())
            .append(MachineEnum.TNC_DATE_IN_DATE.getDBEnum(), machine.getTncDateInDate())
            .append(MachineEnum.DUE_STATUS.getDBEnum(), machine.getDueStatus())
            .append(MachineEnum.PPM_DATE_IN_STRING.getDBEnum(), machine.getPpmDateInString())
            .append(MachineEnum.TNC_DATE_IN_STRING.getDBEnum(), machine.getTncDateInString()));
    return document;
  }

  private MaintenanceHistory constructHistoryForGet(Document document) {
    MaintenanceHistory history = new MaintenanceHistory(document.getString(MachineEnum.SERIAL_NUM.getDBEnum()),
        document.getString(MaintenanceEnum.WORK_ORDER.getDBEnum()),
        document.getString(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum()),
        document.getString(MaintenanceEnum.ACTION_TAKEN.getDBEnum()),
        document.getString(MaintenanceEnum.REPORTED_BY.getDBEnum()),
        document.getString(MaintenanceEnum.WO_TYPE.getDBEnum()),
        document.getString(MaintenanceEnum.LAST_UPDATED.getDBEnum()),
        document.getString(MaintenanceEnum.DATE_OF_CREATION.getDBEnum()),
        document.getString(MaintenanceEnum.WORK_ORDER_DATE_IN_STRING.getDBEnum()),
        document.getLong(MaintenanceEnum.LAST_UPDATED_IN_LONG.getDBEnum()),
        document.getLong(MaintenanceEnum.DATE_OF_CREATION_IN_LONG.getDBEnum()));

    return history;
  }

  private Document constructHistoryForInsert(MaintenanceHistory history) {
    Document document = new Document(MachineEnum.SERIAL_NUM.getDBEnum(), history.getSerialNumber())
        .append(MaintenanceEnum.WORK_ORDER.getDBEnum(), history.getWorkOrderNumber())
        .append(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum(), history.getWorkOrderDate())
        .append(MaintenanceEnum.ACTION_TAKEN.getDBEnum(), history.getActionTaken())
        .append(MaintenanceEnum.REPORTED_BY.getDBEnum(), history.getReportedBy())
        .append(MaintenanceEnum.WO_TYPE.getDBEnum(), history.getWorkOrderType())
        .append(MaintenanceEnum.LAST_UPDATED.getDBEnum(), history.getLastUpdated())
        .append(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(), history.getDateOfCreation())
        .append(MaintenanceEnum.WORK_ORDER_DATE_IN_STRING.getDBEnum(), history.getWorkOrderDateInString())
        .append(MaintenanceEnum.LAST_UPDATED_IN_LONG.getDBEnum(), history.getLastUpdatedInLong())
        .append(MaintenanceEnum.DATE_OF_CREATION_IN_LONG.getDBEnum(), history.getDateOfCreationInLong());
    return document;
  }

  private Document constructHistoryForUpdate(MaintenanceHistory history) {
    Document document = new Document().append("$set",
        new Document().append(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum(), history.getWorkOrderDate())
            .append(MaintenanceEnum.ACTION_TAKEN.getDBEnum(), history.getActionTaken())
            .append(MaintenanceEnum.REPORTED_BY.getDBEnum(), history.getReportedBy())
            .append(MaintenanceEnum.WO_TYPE.getDBEnum(), history.getWorkOrderType())
            .append(MaintenanceEnum.LAST_UPDATED.getDBEnum(), history.getLastUpdated())
            .append(MaintenanceEnum.WORK_ORDER_DATE_IN_STRING.getDBEnum(), history.getWorkOrderDateInString())
            .append(MaintenanceEnum.LAST_UPDATED_IN_LONG.getDBEnum(), history.getLastUpdatedInLong()));
    return document;
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
      machine.setDateOfCreation(CustomDateTimeFormatter.convertLongToDateString(currTimestamp));
      machine.setDateOfCreationInLong(currTimestamp);
    }

    if (isInsert || isUpdate) {
      machine.setLastUpdated(CustomDateTimeFormatter.convertLongToDateString(currTimestamp));
      machine.setLastUpdatedInLong(currTimestamp);
    }

    // if (isGet) {
    // machine.setHistoryCount(new Long(getHistory(machine.getSerialNumber()).size()));
    // }
  }

  private void prepHistory(MaintenanceHistory history, boolean isGet, boolean isInsert, boolean isUpdate) {
    long currTimestamp = CustomDateTimeFormatter.generateTimestamp();

    if (history.getWorkOrderDate() != null) {
      if (history.getWorkOrderDateInString() != null || isInsert || isUpdate) {
        history.setWorkOrderDateInString(CustomDateTimeFormatter.convertStringToYYYYMMDD(history.getWorkOrderDate()));
      }
    }

    if (isInsert) {
      history.setDateOfCreation(CustomDateTimeFormatter.convertLongToDateString(currTimestamp));
      history.setDateOfCreationInLong(currTimestamp);
    }

    if (isInsert || isUpdate) {
      history.setLastUpdated(CustomDateTimeFormatter.convertLongToDateString(currTimestamp));
      history.setLastUpdatedInLong(currTimestamp);
    }
  }

  public ResultWrapper getAllMachines() throws Exception {
    final ArrayList<Machine> listOfMachines = new ArrayList<Machine>();
    final ArrayList<Machine> listOfMachinesDue = new ArrayList<Machine>();
    ResultWrapper wrapper = new ResultWrapper();

    FindIterable<Document> iterable = db.getCollection(MACHINE_COLLECTION).find()
        .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        Machine machine = constructMachineForGet(document);
        prepMachine(machine, true, false, false);
        if (machine.getDueForPPM() != 0) {
          listOfMachinesDue.add(machine);
        }
        listOfMachines.add(machine);
      }
    });

    wrapper.setMachines(listOfMachines);
    wrapper.setMachinesDue(listOfMachinesDue);

    return wrapper;
  }

  public List<Machine> getAllMachinesDue() throws Exception {
    final ArrayList<Machine> listOfMachinesDue = new ArrayList<Machine>();

    FindIterable<Document> iterable = db.getCollection(MACHINE_COLLECTION).find()
        .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        Machine machine = constructMachineForGet(document);
        prepMachine(machine, true, false, false);
        if (machine.getDueForPPM() != 0) {
          listOfMachinesDue.add(machine);
        }
      }
    });

    return listOfMachinesDue;
  }

  public ResultWrapper getOneMachine(String serialNumber, String lastUpdated, String hardfailure) throws Exception {
    ResultWrapper wrapper = new ResultWrapper();
    List<Machine> machines = getMachine(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber);
    if (machines.isEmpty()) {
      throw new RecordDoesNotExistException();
    }
    Machine currMachine = machines.get(0);
    if (lastUpdated != null && currMachine.getLastUpdatedInLong().compareTo(new Long(lastUpdated)) != 0) {
      if (hardfailure.equals("1")) {
        throw new RecordWasRecentlyUpdatedException();
      } else {
        wrapper.setMessage("RecordWasRecentlyUpdated");
      }
    }
    wrapper.setMachine(currMachine);
    return wrapper;
  }

  public List<Machine> getMachine(String searchKey, String searchValue) throws Exception {
    final ArrayList<Machine> listOfMachines = new ArrayList<Machine>();

    Document regQuery = new Document();
    regQuery.append("$regex", "^(?)" + Pattern.quote(searchValue));
    regQuery.append("$options", "i");
    FindIterable<Document> iterable = db.getCollection(MACHINE_COLLECTION).find(new Document(searchKey, regQuery))
        .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        Machine machine = constructMachineForGet(document);
        prepMachine(machine, true, false, false);

        listOfMachines.add(machine);
      }
    });

    return listOfMachines;
  }

  public ResultWrapper insertMachine(Machine machine) throws Exception {
    ResultWrapper wrapper = new ResultWrapper();

    if (!getMachine(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber()).isEmpty()) {
      throw new RecordAlreadyExistsException();
    }

    prepMachine(machine, false, true, false);
    db.getCollection(MACHINE_COLLECTION).insertOne(constructMachineForInsert(machine));

    wrapper.setMachinesDue(getAllMachinesDue());

    return wrapper;
  }

  public ResultWrapper updateMachine(Machine machine) throws Exception {
    ResultWrapper wrapper = new ResultWrapper();

    List<Machine> machines = getMachine(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber());
    if (machines.isEmpty()) {
      throw new RecordDoesNotExistException();
    }
    Machine currMachine = machines.get(0);
    if (currMachine.getLastUpdatedInLong().compareTo(machine.getLastUpdatedInLong()) != 0) {
      throw new RecordWasRecentlyUpdatedException();
    }

    prepMachine(machine, false, false, true);
    db.getCollection(MACHINE_COLLECTION).updateOne(
        new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber()),
        constructMachineForUpdate(machine));

    wrapper.setMachine(machine);
    wrapper.setMachinesDue(getAllMachinesDue());

    return wrapper;
  }

  public void deleteMachine(String serialNumber, String lastUpdated) throws Exception {
    List<Machine> machines = getMachine(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber);
    if (machines.isEmpty()) {
      throw new RecordDoesNotExistException();
    }
    Machine currMachine = machines.get(0);
    if (currMachine.getLastUpdatedInLong().compareTo(new Long(lastUpdated)) != 0) {
      throw new RecordWasRecentlyUpdatedException();
    }

    db.getCollection(MACHINE_COLLECTION).deleteMany(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber));
    db.getCollection(MAINTENANCE_COLLECTION).deleteMany(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber));
  }

  public List<MaintenanceHistory> getHistory(String serialNumber) throws Exception {
    List<Machine> machines = getMachine(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber);
    if (machines.isEmpty()) {
      throw new RecordDoesNotExistException();
    }

    final ArrayList<MaintenanceHistory> listOfHistory = new ArrayList<MaintenanceHistory>();

    Document regQuery = new Document();
    regQuery.append("$regex", "^(?)" + Pattern.quote(serialNumber));
    regQuery.append("$options", "i");
    FindIterable<Document> iterable = db.getCollection(MAINTENANCE_COLLECTION)
        .find(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), regQuery))
        .sort(new Document(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        MaintenanceHistory history = constructHistoryForGet(document);
        // prepHistory(history, true, false, false);
        listOfHistory.add(history);
      }
    });

    return listOfHistory;
  }

  public ResultWrapper getHistory(String serialNumber, String workOrderNumber, String lastUpdated,
      boolean checkIfUpdated, boolean checkIfExist) throws Exception {
    ResultWrapper wrapper = new ResultWrapper();

    final ArrayList<MaintenanceHistory> listOfHistory = new ArrayList<MaintenanceHistory>();

    Document serialNumberReg = new Document();
    serialNumberReg.append("$regex", "^(?)" + Pattern.quote(serialNumber));
    serialNumberReg.append("$options", "i");

    Document workOrderNumberReg = new Document();
    workOrderNumberReg.append("$regex", "^(?)" + Pattern.quote(workOrderNumber));
    workOrderNumberReg.append("$options", "i");

    FindIterable<Document> iterable = db.getCollection(MAINTENANCE_COLLECTION)
        .find(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumberReg)
            .append(MaintenanceEnum.WORK_ORDER.getDBEnum(), workOrderNumberReg))
        .sort(new Document(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        MaintenanceHistory history = constructHistoryForGet(document);
        // prepHistory(history, true, false, false);
        listOfHistory.add(history);
      }
    });

    if (listOfHistory.isEmpty()) {
      if (checkIfExist) {
        throw new RecordDoesNotExistException();
      } else {
        return null;
      }
    } else if (lastUpdated != null
        && listOfHistory.get(0).getLastUpdatedInLong().compareTo(new Long(lastUpdated)) != 0) {
      if (checkIfUpdated) {
        throw new RecordWasRecentlyUpdatedException();
      } else {
        wrapper.setMessage("RecordWasRecentlyUpdated");
      }
    }

    wrapper.setHistory(listOfHistory.get(0));

    return wrapper;
  }

  public void insertHistory(MaintenanceHistory history) throws Exception {
    ResultWrapper wrapper = getHistory(history.getSerialNumber(), history.getWorkOrderNumber(), null, false, false);
    if (wrapper != null) {
      throw new RecordAlreadyExistsException();
    }

    prepHistory(history, false, true, false);
    db.getCollection(MAINTENANCE_COLLECTION).insertOne(constructHistoryForInsert(history));

    getMachine(MachineEnum.SERIAL_NUM.getDBEnum(), history.getSerialNumber());

    BasicDBObject updateDocument = new BasicDBObject();
    updateDocument.append("$inc", new BasicDBObject().append("historyCount", 1));
    db.getCollection(MACHINE_COLLECTION).updateOne(
        new BasicDBObject().append(MachineEnum.SERIAL_NUM.getDBEnum(), history.getSerialNumber()), updateDocument);
  }

  public MaintenanceHistory updateHistory(MaintenanceHistory history) throws Exception {
    // TODO I don't like String.valueOf. Can we do better?
    getHistory(history.getSerialNumber(), history.getWorkOrderNumber(), String.valueOf(history.getLastUpdatedInLong()),
        true, true);

    prepHistory(history, false, false, true);
    db.getCollection(MAINTENANCE_COLLECTION)
        .updateOne(
            new Document(MachineEnum.SERIAL_NUM.getDBEnum(), history.getSerialNumber())
                .append(MaintenanceEnum.WORK_ORDER.getDBEnum(), history.getWorkOrderNumber()),
            constructHistoryForUpdate(history));

    return history;
  }

  public void deleteHistory(String serialNumber, String workOrderNumber, String lastUpdated) throws Exception {
    getHistory(serialNumber, workOrderNumber, lastUpdated, true, true);

    db.getCollection(MAINTENANCE_COLLECTION).deleteMany(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber)
        .append(MaintenanceEnum.WORK_ORDER.getDBEnum(), workOrderNumber));

    getMachine(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber);

    BasicDBObject updateDocument = new BasicDBObject();
    updateDocument.append("$inc", new BasicDBObject().append("historyCount", -1));
    db.getCollection(MACHINE_COLLECTION)
        .updateOne(new BasicDBObject().append(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber), updateDocument);
  }
}
