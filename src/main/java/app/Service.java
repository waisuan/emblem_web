package app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import org.bson.Document;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

import model.Machine;
import model.MaintenanceHistory;
import util.CustomDateTimeFormatter;
import util.MachineEnum;
import util.MaintenanceEnum;

public class Service {
  private final MongoDatabase db;

  public final static String MACHINE_COLLECTION = "machines";
  public final static String MAINTENANCE_COLLECTION = "maintenance";

  public Service(MongoDatabase mongoDatabase) {
    this.db = mongoDatabase;
  }

  public List<Machine> getAllMachines() {
    final ArrayList<Machine> listOfMachines = new ArrayList<Machine>();

    FindIterable<Document> iterable = db.getCollection(MACHINE_COLLECTION).find()
        .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        Machine machine = new Machine((String) document.get(MachineEnum.SERIAL_NUM.getDBEnum()),
            (String) document.get(MachineEnum.CUSTOMER.getDBEnum()),
            (String) document.get(MachineEnum.STATE.getDBEnum()),
            (String) document.get(MachineEnum.ACCOUNT_TYPE.getDBEnum()),
            (String) document.get(MachineEnum.MODEL.getDBEnum()),
            (String) document.get(MachineEnum.TNC_DATE.getDBEnum()),
            (String) document.get(MachineEnum.STATUS.getDBEnum()),
            (String) document.get(MachineEnum.PERSON_IN_CHARGE.getDBEnum()),
            (String) document.get(MachineEnum.REPORTED_BY.getDBEnum()),
            (String) document.get(MachineEnum.ADDITIONAL_NOTES.getDBEnum()),
            (String) document.get(MachineEnum.PPM_DATE.getDBEnum()),
            (String) document.get(MachineEnum.BRAND.getDBEnum()),
            CustomDateTimeFormatter.convertLongToDateString(document.getLong(MachineEnum.LAST_UPDATED.getDBEnum())),
            CustomDateTimeFormatter.convertLongToDateString(document.getLong(MachineEnum.DATE_OF_CREATION.getDBEnum())),
            0, 0);
        // document.getLong(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum()),
        // document.getLong(MachineEnum.TWO_WEEKS_AFTER.getDBEnum()));
        listOfMachines.add(machine);
      }
    });

    return listOfMachines;
  }

  public List<Machine> getMachine(String searchKey, String searchValue) {
    searchKey = MachineEnum.getDBEnumFromProp(searchKey);

    if (null == searchKey)
      searchKey = MachineEnum.SERIAL_NUM.getDBEnum();

    if (MachineEnum.ALL.getDBEnum().equals(searchKey))
      return getAllMachines();

    final ArrayList<Machine> listOfMachines = new ArrayList<Machine>();

    Document regQuery = new Document();
    regQuery.append("$regex", "^(?)" + Pattern.quote(searchValue));
    regQuery.append("$options", "i");
    FindIterable<Document> iterable = db.getCollection(MACHINE_COLLECTION).find(new Document(searchKey, regQuery))
        .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        Machine machine = new Machine((String) document.get(MachineEnum.SERIAL_NUM.getDBEnum()),
            (String) document.get(MachineEnum.CUSTOMER.getDBEnum()),
            (String) document.get(MachineEnum.STATE.getDBEnum()),
            (String) document.get(MachineEnum.ACCOUNT_TYPE.getDBEnum()),
            (String) document.get(MachineEnum.MODEL.getDBEnum()),
            (String) document.get(MachineEnum.TNC_DATE.getDBEnum()),
            (String) document.get(MachineEnum.STATUS.getDBEnum()),
            (String) document.get(MachineEnum.PERSON_IN_CHARGE.getDBEnum()),
            (String) document.get(MachineEnum.REPORTED_BY.getDBEnum()),
            (String) document.get(MachineEnum.ADDITIONAL_NOTES.getDBEnum()),
            (String) document.get(MachineEnum.PPM_DATE.getDBEnum()),
            (String) document.get(MachineEnum.BRAND.getDBEnum()),
            CustomDateTimeFormatter.convertLongToDateString(document.getLong(MachineEnum.LAST_UPDATED.getDBEnum())),
            CustomDateTimeFormatter.convertLongToDateString(document.getLong(MachineEnum.DATE_OF_CREATION.getDBEnum())),
            0, 0);
        // document.getLong(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum()),
        // document.getLong(MachineEnum.TWO_WEEKS_AFTER.getDBEnum()));
        listOfMachines.add(machine);
      }
    });

    return listOfMachines;
  }

  public boolean insertMachine(Machine machine) {
    long currTimestamp = CustomDateTimeFormatter.generateTimestamp();
    long twoWeeksBefore = 0;
    long twoWeeksAfter = 0;
    // if (!machine.getPpmDate().isEmpty()) {
    // Date date = CustomDateTimeFormatter.convertStringToDate(machine.getPpmDate());
    // twoWeeksBefore = CustomDateTimeFormatter.getTwoWeeksBeforeDate(date);
    // twoWeeksAfter = CustomDateTimeFormatter.getTwoWeeksAfterDate(date);
    // }

    db.getCollection(MACHINE_COLLECTION)
        .insertOne(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber())
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
            .append(MachineEnum.DATE_OF_CREATION.getDBEnum(), currTimestamp)
            .append(MachineEnum.LAST_UPDATED.getDBEnum(), currTimestamp));
    // .append(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum(), twoWeeksBefore)
    // .append(MachineEnum.TWO_WEEKS_AFTER.getDBEnum(), twoWeeksAfter));

    return true;
  }

  public boolean updateMachine(Machine machine) {
    long currTimestamp = CustomDateTimeFormatter.generateTimestamp();
    long twoWeeksBefore = 0;
    long twoWeeksAfter = 0;
    // if (!machine.getPpmDate().isEmpty()) {
    // Date date = CustomDateTimeFormatter.convertStringToDate(machine.getPpmDate());
    // twoWeeksBefore = CustomDateTimeFormatter.getTwoWeeksBeforeDate(date);
    // twoWeeksAfter = CustomDateTimeFormatter.getTwoWeeksAfterDate(date);
    // }

    db.getCollection(MACHINE_COLLECTION).replaceOne(
        new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber()),
        new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber())
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
            .append(MachineEnum.DATE_OF_CREATION.getDBEnum(),
                CustomDateTimeFormatter.convertDateStringToLong(machine.getDateOfCreation()))
            .append(MachineEnum.LAST_UPDATED.getDBEnum(), currTimestamp));
    // .append(MachineEnum.LAST_UPDATED.getDBEnum(), CustomDateTimeFormatter.generateTimestamp())
    // .append(MachineEnum.DATE_OF_CREATION.getDBEnum(), machine.getDateOfCreation())
    // .append(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum(), twoWeeksBefore)
    // .append(MachineEnum.TWO_WEEKS_AFTER.getDBEnum(), twoWeeksAfter));
    return true;
  }

  public boolean deleteMachine(String serialNumber) {
    db.getCollection(MACHINE_COLLECTION).deleteMany(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber));
    db.getCollection(MAINTENANCE_COLLECTION).deleteMany(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber));
    return true;
  }

  public List<MaintenanceHistory> getHistory(String serialNumber) {
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
        MaintenanceHistory maintenance;
        maintenance = new MaintenanceHistory(document.getString(MachineEnum.SERIAL_NUM.getDBEnum()),
            document.getString(MaintenanceEnum.WORK_ORDER.getDBEnum()),
            document.getString(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum()),
            document.getString(MaintenanceEnum.ACTION_TAKEN.getDBEnum()),
            document.getString(MaintenanceEnum.REPORTED_BY.getDBEnum()),
            document.getString(MaintenanceEnum.WO_TYPE.getDBEnum()),
            CustomDateTimeFormatter.convertLongToDateString(document.getLong(MaintenanceEnum.LAST_UPDATED.getDBEnum())),
            CustomDateTimeFormatter
                .convertLongToDateString(document.getLong(MaintenanceEnum.DATE_OF_CREATION.getDBEnum())));
        listOfHistory.add(maintenance);
      }
    });

    return listOfHistory;
  }

  public MaintenanceHistory getHistory(String serialNumber, String workOrderNumber) {
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
        MaintenanceHistory maintenance;
        maintenance = new MaintenanceHistory(document.getString(MachineEnum.SERIAL_NUM.getDBEnum()),
            document.getString(MaintenanceEnum.WORK_ORDER.getDBEnum()),
            document.getString(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum()),
            document.getString(MaintenanceEnum.ACTION_TAKEN.getDBEnum()),
            document.getString(MaintenanceEnum.REPORTED_BY.getDBEnum()),
            document.getString(MaintenanceEnum.WO_TYPE.getDBEnum()),
            CustomDateTimeFormatter.convertLongToDateString(document.getLong(MaintenanceEnum.LAST_UPDATED.getDBEnum())),
            CustomDateTimeFormatter
                .convertLongToDateString(document.getLong(MaintenanceEnum.DATE_OF_CREATION.getDBEnum())));
        listOfHistory.add(maintenance);
      }
    });

    return listOfHistory.isEmpty() ? null : listOfHistory.get(0);
  }

  public void insertHistory(MaintenanceHistory maintenanceHistory) {
    long currTimestamp = CustomDateTimeFormatter.generateTimestamp();

    db.getCollection(MAINTENANCE_COLLECTION)
        .insertOne(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), maintenanceHistory.getSerialNumber())
            .append(MaintenanceEnum.WORK_ORDER.getDBEnum(), maintenanceHistory.getWorkOrderNumber())
            .append(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum(), maintenanceHistory.getWorkOrderDate())
            .append(MaintenanceEnum.ACTION_TAKEN.getDBEnum(), maintenanceHistory.getActionTaken())
            .append(MaintenanceEnum.REPORTED_BY.getDBEnum(), maintenanceHistory.getReportedBy())
            .append(MaintenanceEnum.WO_TYPE.getDBEnum(), maintenanceHistory.getWorkOrderType())
            .append(MaintenanceEnum.LAST_UPDATED.getDBEnum(), currTimestamp)
            .append(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(), currTimestamp));
  }

  public void updateHistory(MaintenanceHistory maintenanceHistory) {
    long currTimestamp = CustomDateTimeFormatter.generateTimestamp();

    db.getCollection(MAINTENANCE_COLLECTION).replaceOne(
        new Document(MachineEnum.SERIAL_NUM.getDBEnum(), maintenanceHistory.getSerialNumber()).append("workOrderNumber",
            maintenanceHistory.getWorkOrderNumber()),
        new Document(MachineEnum.SERIAL_NUM.getDBEnum(), maintenanceHistory.getSerialNumber())
            .append(MaintenanceEnum.WORK_ORDER.getDBEnum(), maintenanceHistory.getWorkOrderNumber())
            .append(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum(), maintenanceHistory.getWorkOrderDate())
            .append(MaintenanceEnum.ACTION_TAKEN.getDBEnum(), maintenanceHistory.getActionTaken())
            .append(MaintenanceEnum.REPORTED_BY.getDBEnum(), maintenanceHistory.getReportedBy())
            .append(MaintenanceEnum.WO_TYPE.getDBEnum(), maintenanceHistory.getWorkOrderType())
            .append(MaintenanceEnum.LAST_UPDATED.getDBEnum(), currTimestamp)
            .append(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(),
                CustomDateTimeFormatter.convertDateStringToLong(maintenanceHistory.getDateOfCreation())));
  }

  public void deleteHistory(String serialNumber, String workOrderNumber) {
    db.getCollection(MAINTENANCE_COLLECTION).deleteMany(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumber)
        .append(MaintenanceEnum.WORK_ORDER.getDBEnum(), workOrderNumber));
  }
}
