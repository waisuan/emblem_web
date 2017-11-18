package app;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

import model.Machine;

import org.bson.Document;

import util.CustomDateTimeFormatter;
import util.MachineEnum;

import com.mongodb.Block;
import com.mongodb.client.FindIterable;
import com.mongodb.client.MongoDatabase;

public class Service {
  private final MongoDatabase db;

  public final static String MACHINE_COLLECTION = "machines";

  public Service(MongoDatabase mongoDatabase) {
    this.db = mongoDatabase;
  }

  public List<Machine> getAllMachines() {
    final ArrayList<Machine> listOfMachines = new ArrayList<Machine>();

    FindIterable<Document> iterable =
        db.getCollection(MACHINE_COLLECTION).find()
        .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        Machine machine =
            new Machine((String) document.get(MachineEnum.SERIAL_NUM.getDBEnum()),
                (String) document.get(MachineEnum.CUSTOMER.getDBEnum()), (String) document
                .get(MachineEnum.STATE.getDBEnum()), (String) document
                    .get(MachineEnum.ACCOUNT_TYPE.getDBEnum()), (String) document
                    .get(MachineEnum.MODEL.getDBEnum()), CustomDateTimeFormatter
                    .formatDate((String) document.get(MachineEnum.TNC_DATE.getDBEnum())),
                (String) document.get(MachineEnum.STATUS.getDBEnum()), (String) document
                    .get(MachineEnum.PERSON_IN_CHARGE.getDBEnum()), (String) document
                .get(MachineEnum.REPORTED_BY.getDBEnum()), (String) document
                .get(MachineEnum.ADDITIONAL_NOTES.getDBEnum()), CustomDateTimeFormatter
                .formatDate((String) document.get(MachineEnum.PPM_DATE.getDBEnum())),
                (String) document.get(MachineEnum.BRAND.getDBEnum()),
                CustomDateTimeFormatter.convertLongToDateString(document
                    .getLong(MachineEnum.LAST_UPDATED.getDBEnum())), CustomDateTimeFormatter
                    .convertLongToDateString(document.getLong(MachineEnum.DATE_OF_CREATION
                        .getDBEnum())), document.getLong(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum()),
                document.getLong(MachineEnum.TWO_WEEKS_AFTER.getDBEnum()));
        listOfMachines.add(machine);
      }
    });

    return listOfMachines;
  }

  public List<Machine> getMachine(String searchKey, String searchValue) {
    System.out.println(searchKey + " " + searchValue);
    searchKey = MachineEnum.getDBEnumFromProp(searchKey);
    System.out.println(searchKey);

    if (null == searchKey)
      searchKey = MachineEnum.SERIAL_NUM.getDBEnum();

    if (MachineEnum.ALL.getDBEnum().equals(searchKey))
      return getAllMachines();

    final ArrayList<Machine> listOfMachines = new ArrayList<Machine>();

    Document regQuery = new Document();
    regQuery.append("$regex", "^(?)" + Pattern.quote(searchValue));
    regQuery.append("$options", "i");
    FindIterable<Document> iterable =
        db.getCollection(MACHINE_COLLECTION).find(new Document(searchKey, regQuery))
        .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));

    iterable.forEach(new Block<Document>() {
      @Override
      public void apply(final Document document) {
        Machine machine =
            new Machine((String) document.get(MachineEnum.SERIAL_NUM.getDBEnum()),
                (String) document.get(MachineEnum.CUSTOMER.getDBEnum()), (String) document
                .get(MachineEnum.STATE.getDBEnum()), (String) document
                    .get(MachineEnum.ACCOUNT_TYPE.getDBEnum()), (String) document
                    .get(MachineEnum.MODEL.getDBEnum()), CustomDateTimeFormatter
                    .formatDate((String) document.get(MachineEnum.TNC_DATE.getDBEnum())),
                (String) document.get(MachineEnum.STATUS.getDBEnum()), (String) document
                    .get(MachineEnum.PERSON_IN_CHARGE.getDBEnum()), (String) document
                .get(MachineEnum.REPORTED_BY.getDBEnum()), (String) document
                .get(MachineEnum.ADDITIONAL_NOTES.getDBEnum()), CustomDateTimeFormatter
                .formatDate((String) document.get(MachineEnum.PPM_DATE.getDBEnum())),
                (String) document.get(MachineEnum.BRAND.getDBEnum()),
                CustomDateTimeFormatter.convertLongToDateString(document
                    .getLong(MachineEnum.LAST_UPDATED.getDBEnum())), CustomDateTimeFormatter
                    .convertLongToDateString(document.getLong(MachineEnum.DATE_OF_CREATION
                        .getDBEnum())), document.getLong(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum()),
                document.getLong(MachineEnum.TWO_WEEKS_AFTER.getDBEnum()));
        listOfMachines.add(machine);
      }
    });

    return listOfMachines;
  }
}