// package throwaway;
//
// import java.security.NoSuchAlgorithmException;
// import java.security.spec.InvalidKeySpecException;
// import java.text.ParseException;
// import java.util.ArrayList;
// import java.util.Date;
// import java.util.regex.Pattern;
//
// import main.java.com.wssia.exception.InvalidUserException;
// import main.java.com.wssia.exception.ObjectAlreadyExistsException;
// import main.java.com.wssia.exception.ObjectDoesNotExistException;
// import main.java.com.wssia.exception.ObjectHasAlreadyBeenUpdatedException;
// import main.java.com.wssia.exception.UserAlreadyExistsException;
// import main.java.com.wssia.exception.UserDoesNotExistException;
// import main.java.com.wssia.exception.UserIsAlreadyLoggedInException;
// import main.java.com.wssia.model.Machine;
// import main.java.com.wssia.model.MaintenanceHistory;
// import main.java.com.wssia.model.User;
// import main.java.com.wssia.service.PasswordEncryptionService;
// import main.java.com.wssia.util.CustomDateTimeFormatter;
// import main.java.com.wssia.util.MachineEnum;
// import main.java.com.wssia.util.MaintenanceEnum;
// import main.java.com.wssia.util.UserEnum;
//
// import org.bson.Document;
//
// import com.mongodb.Block;
// import com.mongodb.MongoClient;
// import com.mongodb.client.FindIterable;
// import com.mongodb.client.MongoDatabase;
// import com.sun.org.apache.xml.internal.security.exceptions.Base64DecodingException;
// import com.sun.org.apache.xml.internal.security.utils.Base64;
//
// public class MongoDBDriver {
// private static MongoClient mongoClient;
// private static MongoDatabase db;
// public static String DB_NAME = "emblem";
// public final static String MACHINE_COLLECTION = "machines";
// public final static String MAINTENANCE_COLLECTION = "maintenance";
// public final static String ACCOUNT_COLLECTION = "accounts";
//
// public static void connect() throws ParseException {
// mongoClient = new MongoClient();
// db = mongoClient.getDatabase(DB_NAME);
// }
//
// public static ArrayList<Machine> queryForMachines() {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// final ArrayList<Machine> listOfMachines = new ArrayList<Machine>();
//
// FindIterable<Document> iterable =
// db.getCollection(MACHINE_COLLECTION).find()
// .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));
//
// iterable.forEach(new Block<Document>() {
// @Override
// public void apply(final Document document) {
// Machine machine =
// new Machine((String) document.get(MachineEnum.SERIAL_NUM.getDBEnum()),
// (String) document.get(MachineEnum.CUSTOMER.getDBEnum()), (String) document
// .get(MachineEnum.STATE.getDBEnum()), (String) document
// .get(MachineEnum.ACCOUNT_TYPE.getDBEnum()), (String) document
// .get(MachineEnum.MODEL.getDBEnum()), CustomDateTimeFormatter
// .formatDate((String) document.get(MachineEnum.TNC_DATE.getDBEnum())),
// (String) document.get(MachineEnum.STATUS.getDBEnum()), (String) document
// .get(MachineEnum.PERSON_IN_CHARGE.getDBEnum()), (String) document
// .get(MachineEnum.REPORTED_BY.getDBEnum()), (String) document
// .get(MachineEnum.ADDITIONAL_NOTES.getDBEnum()), CustomDateTimeFormatter
// .formatDate((String) document.get(MachineEnum.PPM_DATE.getDBEnum())),
// (String) document.get(MachineEnum.BRAND.getDBEnum()), document
// .getLong(MachineEnum.LAST_UPDATED.getDBEnum()), document
// .getLong(MachineEnum.DATE_OF_CREATION.getDBEnum()), document
// .getLong(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum()), document
// .getLong(MachineEnum.TWO_WEEKS_AFTER.getDBEnum()));
// listOfMachines.add(machine);
// }
// });
//
// return listOfMachines;
// }
//
// public static ArrayList<Machine> queryForMachine(String searchKey, String searchValue) {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// if (null == searchKey)
// searchKey = MachineEnum.SERIAL_NUM.getDBEnum();
//
// if (MachineEnum.ALL.getDBEnum().equals(searchKey))
// return queryForMachines();
//
// final ArrayList<Machine> listOfMachines = new ArrayList<Machine>();
//
// Document regQuery = new Document();
// regQuery.append("$regex", "^(?)" + Pattern.quote(searchValue));
// regQuery.append("$options", "i");
// FindIterable<Document> iterable =
// db.getCollection(MACHINE_COLLECTION).find(new Document(searchKey, regQuery))
// .sort(new Document(MachineEnum.DATE_OF_CREATION.getDBEnum(), 1));
//
// iterable.forEach(new Block<Document>() {
// @Override
// public void apply(final Document document) {
// Machine machine =
// new Machine((String) document.get(MachineEnum.SERIAL_NUM.getDBEnum()),
// (String) document.get(MachineEnum.CUSTOMER.getDBEnum()), (String) document
// .get(MachineEnum.STATE.getDBEnum()), (String) document
// .get(MachineEnum.ACCOUNT_TYPE.getDBEnum()), (String) document
// .get(MachineEnum.MODEL.getDBEnum()), CustomDateTimeFormatter
// .formatDate((String) document.get(MachineEnum.TNC_DATE.getDBEnum())),
// (String) document.get(MachineEnum.STATUS.getDBEnum()), (String) document
// .get(MachineEnum.PERSON_IN_CHARGE.getDBEnum()), (String) document
// .get(MachineEnum.REPORTED_BY.getDBEnum()), (String) document
// .get(MachineEnum.ADDITIONAL_NOTES.getDBEnum()), CustomDateTimeFormatter
// .formatDate((String) document.get(MachineEnum.PPM_DATE.getDBEnum())),
// (String) document.get(MachineEnum.BRAND.getDBEnum()), document
// .getLong(MachineEnum.LAST_UPDATED.getDBEnum()), document
// .getLong(MachineEnum.DATE_OF_CREATION.getDBEnum()), document
// .getLong(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum()), document
// .getLong(MachineEnum.TWO_WEEKS_AFTER.getDBEnum()));
// listOfMachines.add(machine);
// }
// });
//
// return listOfMachines;
// }
//
// public static ArrayList<MaintenanceHistory> queryForMaintenanceHistoryBySerialNumber(
// String serialNumber) {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// final ArrayList<MaintenanceHistory> listOfMaintenance = new ArrayList<MaintenanceHistory>();
//
// Document regQuery = new Document();
// regQuery.append("$regex", "^(?)" + Pattern.quote(serialNumber));
// regQuery.append("$options", "i");
// FindIterable<Document> iterable =
// db.getCollection(MAINTENANCE_COLLECTION)
// .find(new Document(MachineEnum.SERIAL_NUM.getDBEnum(), regQuery))
// .sort(new Document(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(), 1));
//
// iterable.forEach(new Block<Document>() {
// @Override
// public void apply(final Document document) {
// MaintenanceHistory maintenance;
// maintenance =
// new MaintenanceHistory(document.getString(MachineEnum.SERIAL_NUM.getDBEnum()), document
// .getString(MaintenanceEnum.WORK_ORDER.getDBEnum()), CustomDateTimeFormatter
// .formatDate((String) document.get(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum())),
// document.getString(MaintenanceEnum.ACTION_TAKEN.getDBEnum()), document
// .getString(MaintenanceEnum.REPORTED_BY.getDBEnum()), document
// .getString(MaintenanceEnum.WO_TYPE.getDBEnum()), document
// .getLong(MaintenanceEnum.LAST_UPDATED.getDBEnum()), document
// .getLong(MaintenanceEnum.DATE_OF_CREATION.getDBEnum()));
// listOfMaintenance.add(maintenance);
// }
// });
//
// return listOfMaintenance;
// }
//
// public static ArrayList<MaintenanceHistory> queryForMaintenanceHistoryBySerialNumberAndWorkOrder(
// String serialNumber, String workOrderNumber) {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// final ArrayList<MaintenanceHistory> listOfMaintenance = new ArrayList<MaintenanceHistory>();
//
// Document serialNumberReg = new Document();
// serialNumberReg.append("$regex", "^(?)" + Pattern.quote(serialNumber));
// serialNumberReg.append("$options", "i");
//
// Document workOrderNumberReg = new Document();
// workOrderNumberReg.append("$regex", "^(?)" + Pattern.quote(workOrderNumber));
// workOrderNumberReg.append("$options", "i");
//
// FindIterable<Document> iterable =
// db.getCollection(MAINTENANCE_COLLECTION)
// .find(
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), serialNumberReg).append(
// MaintenanceEnum.WORK_ORDER.getDBEnum(), workOrderNumberReg))
// .sort(new Document(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(), 1));
//
// iterable.forEach(new Block<Document>() {
// @Override
// public void apply(final Document document) {
// MaintenanceHistory maintenance;
// maintenance =
// new MaintenanceHistory(document.getString(MachineEnum.SERIAL_NUM.getDBEnum()), document
// .getString(MaintenanceEnum.WORK_ORDER.getDBEnum()), CustomDateTimeFormatter
// .formatDate((String) document.get(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum())),
// document.getString(MaintenanceEnum.ACTION_TAKEN.getDBEnum()), document
// .getString(MaintenanceEnum.REPORTED_BY.getDBEnum()), document
// .getString(MaintenanceEnum.WO_TYPE.getDBEnum()), document
// .getLong(MaintenanceEnum.LAST_UPDATED.getDBEnum()), document
// .getLong(MaintenanceEnum.DATE_OF_CREATION.getDBEnum()));
// listOfMaintenance.add(maintenance);
// }
// });
//
// return listOfMaintenance;
// }
//
// public static void insertNewMachine(Machine machine) throws ObjectAlreadyExistsException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// if (!queryForMachine(null, machine.getSerialNumber()).isEmpty()) {
// throw new ObjectAlreadyExistsException();
// }
//
// long currTimestamp = CustomDateTimeFormatter.generateTimestamp();
// long twoWeeksBefore = -1;
// long twoWeeksAfter = -1;
// if (!machine.getPpmDate().isEmpty()) {
// Date date = CustomDateTimeFormatter.convertStringToDate(machine.getPpmDate());
// twoWeeksBefore = CustomDateTimeFormatter.getTwoWeeksBeforeDate(date);
// twoWeeksAfter = CustomDateTimeFormatter.getTwoWeeksAfterDate(date);
// }
//
// db.getCollection(MACHINE_COLLECTION).insertOne(
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber())
// .append(MachineEnum.CUSTOMER.getDBEnum(), machine.getCustomer())
// .append(MachineEnum.STATE.getDBEnum(), machine.getState())
// .append(MachineEnum.ACCOUNT_TYPE.getDBEnum(), machine.getAccountType())
// .append(MachineEnum.MODEL.getDBEnum(), machine.getModel())
// .append(MachineEnum.TNC_DATE.getDBEnum(), machine.getTncDate())
// .append(MachineEnum.STATUS.getDBEnum(), machine.getStatus())
// .append(MachineEnum.PERSON_IN_CHARGE.getDBEnum(), machine.getPersonInCharge())
// .append(MachineEnum.REPORTED_BY.getDBEnum(), machine.getReportedBy())
// .append(MachineEnum.ADDITIONAL_NOTES.getDBEnum(), machine.getAdditionalNotes())
// .append(MachineEnum.PPM_DATE.getDBEnum(), machine.getPpmDate())
// .append(MachineEnum.BRAND.getDBEnum(), machine.getBrand())
// .append(MachineEnum.LAST_UPDATED.getDBEnum(), currTimestamp)
// .append(MachineEnum.DATE_OF_CREATION.getDBEnum(), currTimestamp)
// .append(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum(), twoWeeksBefore)
// .append(MachineEnum.TWO_WEEKS_AFTER.getDBEnum(), twoWeeksAfter));
// }
//
// public static void insertNewMaintenance(MaintenanceHistory maintenanceHistory)
// throws ObjectAlreadyExistsException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// if (!queryForMaintenanceHistoryBySerialNumberAndWorkOrder(maintenanceHistory.getSerialNumber(),
// maintenanceHistory.getWorkOrderNumber()).isEmpty()) {
// throw new ObjectAlreadyExistsException();
// }
//
// long currTimestamp = CustomDateTimeFormatter.generateTimestamp();
//
// db.getCollection(MAINTENANCE_COLLECTION)
// .insertOne(
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), maintenanceHistory.getSerialNumber())
// .append(MaintenanceEnum.WORK_ORDER.getDBEnum(),
// maintenanceHistory.getWorkOrderNumber())
// .append(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum(),
// maintenanceHistory.getWorkOrderDate())
// .append(MaintenanceEnum.ACTION_TAKEN.getDBEnum(),
// maintenanceHistory.getActionTaken())
// .append(MaintenanceEnum.REPORTED_BY.getDBEnum(), maintenanceHistory.getReportedBy())
// .append(MaintenanceEnum.WO_TYPE.getDBEnum(), maintenanceHistory.getWorkOrderType())
// .append(MaintenanceEnum.LAST_UPDATED.getDBEnum(), currTimestamp)
// .append(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(), currTimestamp));
// }
//
// public static void updateMachine(Machine machine) throws ObjectDoesNotExistException,
// ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// ArrayList<Machine> machines = queryForMachine(null, machine.getSerialNumber());
//
// if (machines.isEmpty()) {
// throw new ObjectDoesNotExistException();
// } else if (machines.get(0).getLastUpdated() != machine.getLastUpdated()) {
// throw new ObjectHasAlreadyBeenUpdatedException();
// }
//
// long twoWeeksBefore = -1;
// long twoWeeksAfter = -1;
// if (!machine.getPpmDate().isEmpty()) {
// if (machines.get(0).getPpmDate().equals(machine.getPpmDate())) {
// twoWeeksBefore = machine.getTwoWeeksBeforePPMDate();
// twoWeeksAfter = machine.getTwoWeeksAfterPPMDate();
// } else {
// Date date = CustomDateTimeFormatter.convertStringToDate(machine.getPpmDate());
// twoWeeksBefore = CustomDateTimeFormatter.getTwoWeeksBeforeDate(date);
// twoWeeksAfter = CustomDateTimeFormatter.getTwoWeeksAfterDate(date);
// }
// }
//
// db.getCollection(MACHINE_COLLECTION).replaceOne(
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber()),
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber())
// .append(MachineEnum.CUSTOMER.getDBEnum(), machine.getCustomer())
// .append(MachineEnum.STATE.getDBEnum(), machine.getState())
// .append(MachineEnum.ACCOUNT_TYPE.getDBEnum(), machine.getAccountType())
// .append(MachineEnum.MODEL.getDBEnum(), machine.getModel())
// .append(MachineEnum.TNC_DATE.getDBEnum(), machine.getTncDate())
// .append(MachineEnum.STATUS.getDBEnum(), machine.getStatus())
// .append(MachineEnum.PERSON_IN_CHARGE.getDBEnum(), machine.getPersonInCharge())
// .append(MachineEnum.REPORTED_BY.getDBEnum(), machine.getReportedBy())
// .append(MachineEnum.ADDITIONAL_NOTES.getDBEnum(), machine.getAdditionalNotes())
// .append(MachineEnum.PPM_DATE.getDBEnum(), machine.getPpmDate())
// .append(MachineEnum.BRAND.getDBEnum(), machine.getBrand())
// .append(MachineEnum.LAST_UPDATED.getDBEnum(),
// CustomDateTimeFormatter.generateTimestamp())
// .append(MachineEnum.DATE_OF_CREATION.getDBEnum(), machine.getDateOfCreation())
// .append(MachineEnum.TWO_WEEKS_BEFORE.getDBEnum(), twoWeeksBefore)
// .append(MachineEnum.TWO_WEEKS_AFTER.getDBEnum(), twoWeeksAfter));
// }
//
// public static void updateMaintenanceHistory(MaintenanceHistory maintenanceHistory)
// throws ObjectDoesNotExistException, ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// ArrayList<MaintenanceHistory> maintenances =
// queryForMaintenanceHistoryBySerialNumberAndWorkOrder(maintenanceHistory.getSerialNumber(),
// maintenanceHistory.getWorkOrderNumber());
//
// if (maintenances.isEmpty()) {
// throw new ObjectDoesNotExistException();
// } else if (maintenances.get(0).getLastUpdated() != maintenanceHistory.getLastUpdated()) {
// throw new ObjectHasAlreadyBeenUpdatedException();
// }
//
// db.getCollection(MAINTENANCE_COLLECTION)
// .replaceOne(
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), maintenanceHistory.getSerialNumber()).append(
// "workOrderNumber", maintenanceHistory.getWorkOrderNumber()),
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), maintenanceHistory.getSerialNumber())
// .append(MaintenanceEnum.WORK_ORDER.getDBEnum(),
// maintenanceHistory.getWorkOrderNumber())
// .append(MaintenanceEnum.WORK_ORDER_DATE.getDBEnum(),
// maintenanceHistory.getWorkOrderDate())
// .append(MaintenanceEnum.ACTION_TAKEN.getDBEnum(),
// maintenanceHistory.getActionTaken())
// .append(MaintenanceEnum.REPORTED_BY.getDBEnum(), maintenanceHistory.getReportedBy())
// .append(MaintenanceEnum.WO_TYPE.getDBEnum(), maintenanceHistory.getWorkOrderType())
// .append(MaintenanceEnum.LAST_UPDATED.getDBEnum(),
// CustomDateTimeFormatter.generateTimestamp())
// .append(MaintenanceEnum.DATE_OF_CREATION.getDBEnum(),
// maintenanceHistory.getDateOfCreation()));
// }
//
// public static void deleteMachine(Machine machine) throws ObjectDoesNotExistException,
// ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// ArrayList<Machine> machines = queryForMachine(null, machine.getSerialNumber());
//
// if (machines.isEmpty()) {
// throw new ObjectDoesNotExistException();
// } else if (machines.get(0).getLastUpdated() != machine.getLastUpdated()) {
// throw new ObjectHasAlreadyBeenUpdatedException();
// }
//
// db.getCollection(MACHINE_COLLECTION).deleteMany(
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber()));
// db.getCollection(MAINTENANCE_COLLECTION).deleteMany(
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), machine.getSerialNumber()));
// }
//
// public static void deleteMaintenanceHistory(MaintenanceHistory maintenanceHistory)
// throws ObjectDoesNotExistException, ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// ArrayList<MaintenanceHistory> maintenances =
// queryForMaintenanceHistoryBySerialNumberAndWorkOrder(maintenanceHistory.getSerialNumber(),
// maintenanceHistory.getWorkOrderNumber());
//
// if (maintenances.isEmpty()) {
// throw new ObjectDoesNotExistException();
// } else if (maintenances.get(0).getLastUpdated() != maintenanceHistory.getLastUpdated()) {
// throw new ObjectHasAlreadyBeenUpdatedException();
// }
//
// db.getCollection(MAINTENANCE_COLLECTION)
// .deleteMany(
// new Document(MachineEnum.SERIAL_NUM.getDBEnum(), maintenanceHistory.getSerialNumber())
// .append(MaintenanceEnum.WORK_ORDER.getDBEnum(),
// maintenanceHistory.getWorkOrderNumber()));
// }
//
// public static ArrayList<User> queryForUsers() {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// final ArrayList<User> users = new ArrayList<User>();
//
// FindIterable<Document> iterable =
// db.getCollection(ACCOUNT_COLLECTION).find()
// .sort(new Document(UserEnum.DATE_OF_CREATION.getDBEnum(), 1));
//
// iterable.forEach(new Block<Document>() {
// @Override
// public void apply(final Document document) {
// User user =
// new User(document.getString(UserEnum.USERNAME.getDBEnum()), document
// .getString(UserEnum.PASSWORD.getDBEnum()), document.getBoolean(UserEnum.CAN_CREATE
// .getDBEnum()), document.getBoolean(UserEnum.CAN_EDIT.getDBEnum()), document
// .getBoolean(UserEnum.CAN_DELETE.getDBEnum()), document
// .getBoolean(UserEnum.CAN_SEARCH.getDBEnum()), document.getBoolean(UserEnum.IS_ADMIN
// .getDBEnum()), document.getString(UserEnum.SALT.getDBEnum()), document
// .getBoolean(UserEnum.ALREADY_LOGGED_ON.getDBEnum()), document
// .getLong(UserEnum.DATE_OF_CREATION.getDBEnum()), document
// .getLong(UserEnum.LAST_UPDATED.getDBEnum()));
// users.add(user);
// }
// });
//
// return users;
// }
//
// public static User queryForUserByUsername(String username) {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// if (1 == db.getCollection(ACCOUNT_COLLECTION).count(new Document("username", username))) {
// FindIterable<Document> iterable =
// db.getCollection(ACCOUNT_COLLECTION).find(new Document("username", username));
// Document document = iterable.first();
// User user =
// new User(username, document.getString(UserEnum.PASSWORD.getDBEnum()),
// document.getBoolean(UserEnum.CAN_CREATE.getDBEnum()),
// document.getBoolean(UserEnum.CAN_EDIT.getDBEnum()),
// document.getBoolean(UserEnum.CAN_DELETE.getDBEnum()),
// document.getBoolean(UserEnum.CAN_SEARCH.getDBEnum()),
// document.getBoolean(UserEnum.IS_ADMIN.getDBEnum()), document.getString(UserEnum.SALT
// .getDBEnum()), document.getBoolean(UserEnum.ALREADY_LOGGED_ON.getDBEnum()),
// document.getLong(UserEnum.DATE_OF_CREATION.getDBEnum()),
// document.getLong(UserEnum.LAST_UPDATED.getDBEnum()));
// return user;
// }
//
// return null;
// }
//
// public static void createUser(User newUser) throws UserAlreadyExistsException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// if (null != queryForUserByUsername(newUser.getUsername())) {
// throw new UserAlreadyExistsException();
// }
//
// try {
// byte[] salt = PasswordEncryptionService.generateSalt();
// byte[] password = PasswordEncryptionService.getEncryptedPassword(newUser.getPassword(), salt);
// long currTimestamp = CustomDateTimeFormatter.generateTimestamp();
//
// db.getCollection(ACCOUNT_COLLECTION).insertOne(
// new Document(UserEnum.USERNAME.getDBEnum(), newUser.getUsername())
// .append(UserEnum.PASSWORD.getDBEnum(), Base64.encode(password))
// .append(UserEnum.CAN_CREATE.getDBEnum(), newUser.isCanCreate())
// .append(UserEnum.CAN_EDIT.getDBEnum(), newUser.isCanEdit())
// .append(UserEnum.CAN_DELETE.getDBEnum(), newUser.isCanDelete())
// .append(UserEnum.CAN_SEARCH.getDBEnum(), newUser.isCanSearch())
// .append(UserEnum.IS_ADMIN.getDBEnum(), newUser.isAdmin())
// .append(UserEnum.SALT.getDBEnum(), Base64.encode(salt))
// .append(UserEnum.ALREADY_LOGGED_ON.getDBEnum(), false)
// .append(UserEnum.DATE_OF_CREATION.getDBEnum(), currTimestamp)
// .append(UserEnum.LAST_UPDATED.getDBEnum(), currTimestamp));
// } catch (NoSuchAlgorithmException e) {
// e.printStackTrace();
// } catch (InvalidKeySpecException e) {
// e.printStackTrace();
// }
// }
//
// public static void updateUser(User updatedUser, boolean isLoggingOut)
// throws UserDoesNotExistException, UserIsAlreadyLoggedInException,
// ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// User user = queryForUserByUsername(updatedUser.getUsername());
//
// if (null == user) {
// throw new UserDoesNotExistException();
// } else if (!isLoggingOut && user.isAlreadyLoggedOn()) {
// throw new UserIsAlreadyLoggedInException();
// } else if (user.getLastUpdated() != updatedUser.getLastUpdated()) {
// throw new ObjectHasAlreadyBeenUpdatedException();
// }
//
// db.getCollection(ACCOUNT_COLLECTION)
// .updateOne(
// new Document(UserEnum.USERNAME.getDBEnum(), updatedUser.getUsername()),
// new Document("$set", new Document(UserEnum.CAN_CREATE.getDBEnum(), updatedUser
// .isCanCreate())
// .append(UserEnum.CAN_EDIT.getDBEnum(), updatedUser.isCanEdit())
// .append(UserEnum.CAN_DELETE.getDBEnum(), updatedUser.isCanDelete())
// .append(UserEnum.CAN_SEARCH.getDBEnum(), updatedUser.isCanSearch())
// .append(UserEnum.IS_ADMIN.getDBEnum(), updatedUser.isAdmin())
// .append(UserEnum.ALREADY_LOGGED_ON.getDBEnum(), updatedUser.isAlreadyLoggedOn())
// .append(UserEnum.LAST_UPDATED.getDBEnum(),
// CustomDateTimeFormatter.generateTimestamp())));
// }
//
// public static void deleteUser(User deletedUser) throws UserDoesNotExistException,
// UserIsAlreadyLoggedInException, ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// User user = queryForUserByUsername(deletedUser.getUsername());
//
// if (null == user) {
// throw new UserDoesNotExistException();
// } else if (user.isAlreadyLoggedOn()) {
// throw new UserIsAlreadyLoggedInException();
// } else if (user.getLastUpdated() != deletedUser.getLastUpdated()) {
// throw new ObjectHasAlreadyBeenUpdatedException();
// }
//
// db.getCollection(ACCOUNT_COLLECTION).deleteMany(
// new Document(UserEnum.USERNAME.getDBEnum(), deletedUser.getUsername()));
// }
//
// public static User loginAndAuthenticateUser(String username, String password)
// throws UserDoesNotExistException, UserIsAlreadyLoggedInException, InvalidUserException,
// ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// User user = queryForUserByUsername(username);
//
// if (null == user) {
// throw new UserDoesNotExistException();
// } else if (user.isAlreadyLoggedOn()) {
// throw new UserIsAlreadyLoggedInException();
// }
//
// try {
// if (!PasswordEncryptionService.authenticate(password, Base64.decode(user.getPassword()),
// Base64.decode(user.getSalt()))) {
// throw new InvalidUserException();
// }
// } catch (NoSuchAlgorithmException | InvalidKeySpecException e) {
// e.printStackTrace();
// } catch (Base64DecodingException e) {
// e.printStackTrace();
// }
//
// user.setAlreadyLoggedOn(true);
// updateUser(user, false);
//
// return user;
// }
//
// public static void logout(String username) throws UserDoesNotExistException,
// UserIsAlreadyLoggedInException, ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// User user = queryForUserByUsername(username);
//
// if (null == user) {
// return;
// }
//
// user.setAlreadyLoggedOn(false);
// updateUser(user, true);
// }
//
//
// public static void changeUserPassword(User updatedUser, String newPassword)
// throws UserDoesNotExistException, UserIsAlreadyLoggedInException,
// ObjectHasAlreadyBeenUpdatedException {
// if (null == db) {
// try {
// connect();
// } catch (ParseException e) {
// e.printStackTrace();
// }
// }
//
// User user = queryForUserByUsername(updatedUser.getUsername());
//
// if (null == user) {
// throw new UserDoesNotExistException();
// } else if (user.isAlreadyLoggedOn()) {
// throw new UserIsAlreadyLoggedInException();
// } else if (user.getLastUpdated() != updatedUser.getLastUpdated()) {
// throw new ObjectHasAlreadyBeenUpdatedException();
// }
//
// try {
// byte[] salt = PasswordEncryptionService.generateSalt();
// byte[] password = PasswordEncryptionService.getEncryptedPassword(newPassword, salt);
//
// db.getCollection(ACCOUNT_COLLECTION).updateOne(
// new Document(UserEnum.USERNAME.getDBEnum(), updatedUser.getUsername()),
// new Document("$set", new Document(UserEnum.PASSWORD.getDBEnum(), Base64.encode(password))
// .append(UserEnum.SALT.getDBEnum(), Base64.encode(salt)).append(
// UserEnum.LAST_UPDATED.getDBEnum(), CustomDateTimeFormatter.generateTimestamp())));
// } catch (NoSuchAlgorithmException e) {
// e.printStackTrace();
// } catch (InvalidKeySpecException e) {
// e.printStackTrace();
// }
// }
// }
