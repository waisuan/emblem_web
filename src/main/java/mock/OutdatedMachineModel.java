package mock;

import java.util.Date;

public class OutdatedMachineModel {

  private static final long serialVersionUID = -8335914162232634551L;

  private String serialNumber;
  private String customer;
  private String state;
  private String model;
  private String tncDate;
  private Date tncDateInDate;
  private String status;
  private String personInCharge;
  private String lastUpdated;
  private String dateOfCreation;
  private String reportedBy;
  private String additionalNotes;
  private String accountType;
  private String ppmDate;
  private Date ppmDateInDate;
  private String brand;
  private Long historyCount;
  private Long lastUpdatedInLong;
  private Date almostDue;
  private Date overdue;
  private Integer dueForPPM;
  private String dueStatus;
  private String ppmDateInString;
  private String tncDateInString;

  public OutdatedMachineModel() {
    // TODO Auto-generated constructor stub
  }

  public OutdatedMachineModel(String serialNumber, String customer, String state, String accountType, String model,
      String tncDate, String status, String personInCharge, String reportedBy, String additionalNotes, String ppmDate,
      String brand) {
    this.serialNumber = serialNumber;
    this.customer = customer;
    this.state = state;
    this.accountType = accountType;
    this.model = model;
    this.tncDate = tncDate;
    this.status = status;
    this.personInCharge = personInCharge;
    this.reportedBy = reportedBy;
    this.additionalNotes = additionalNotes;
    this.ppmDate = ppmDate;
    this.brand = brand;
  }

  public OutdatedMachineModel(String serialNumber, String customer, String state, String accountType, String model,
      String tncDate, String status, String personInCharge, String reportedBy, String additionalNotes, String ppmDate,
      String brand, String lastUpdated, String dateOfCreation, Long historyCount, Long lastUpdatedInLong,
      Date almostDue, Date overdue, Integer dueForPPM, Date tncDateInDate, Date ppmDateInDate, String dueStatus,
      String ppmDateInString, String tncDateInString) {
    this.serialNumber = serialNumber;
    this.customer = customer;
    this.state = state;
    this.accountType = accountType;
    this.model = model;
    this.tncDate = tncDate;
    this.status = status;
    this.personInCharge = personInCharge;
    this.reportedBy = reportedBy;
    this.additionalNotes = additionalNotes;
    this.ppmDate = ppmDate;
    this.brand = brand;
    this.lastUpdated = lastUpdated;
    this.dateOfCreation = dateOfCreation;
    this.historyCount = historyCount;
    this.lastUpdatedInLong = lastUpdatedInLong;
    this.almostDue = almostDue;
    this.overdue = overdue;
    this.dueForPPM = dueForPPM;
    this.tncDateInDate = tncDateInDate;
    this.ppmDateInDate = ppmDateInDate;
    this.dueStatus = dueStatus;
    this.ppmDateInString = ppmDateInString;
    this.tncDateInString = tncDateInString;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getCustomer() {
    return customer;
  }

  public void setCustomer(String customer) {
    this.customer = customer;
  }

  public String getState() {
    return state;
  }

  public void setState(String state) {
    this.state = state;
  }

  public String getAccountType() {
    return accountType;
  }

  public void setAccountType(String accountType) {
    this.accountType = accountType;
  }

  public String getModel() {
    return model;
  }

  public void setModel(String model) {
    this.model = model;
  }

  public String getStatus() {
    return status;
  }

  public void setStatus(String status) {
    this.status = status;
  }

  public String getPersonInCharge() {
    return personInCharge;
  }

  public void setPersonInCharge(String personInCharge) {
    this.personInCharge = personInCharge;
  }

  public String getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(String lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public String getDateOfCreation() {
    return dateOfCreation;
  }

  public void setDateOfCreation(String dateOfCreation) {
    this.dateOfCreation = dateOfCreation;
  }

  public String getReportedBy() {
    return reportedBy;
  }

  public void setReportedBy(String reportedBy) {
    this.reportedBy = reportedBy;
  }

  public String getAdditionalNotes() {
    return additionalNotes;
  }

  public void setAdditionalNotes(String additionalNotes) {
    this.additionalNotes = additionalNotes;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public Long getHistoryCount() {
    return historyCount;
  }

  public void setHistoryCount(Long historyCount) {
    this.historyCount = historyCount;
  }

  public Long getLastUpdatedInLong() {
    return lastUpdatedInLong;
  }

  public void setLastUpdatedInLong(Long lastUpdatedInLong) {
    this.lastUpdatedInLong = lastUpdatedInLong;
  }

  public Date getAlmostDue() {
    return almostDue;
  }

  public void setAlmostDue(Date almostDue) {
    this.almostDue = almostDue;
  }

  public Date getOverdue() {
    return overdue;
  }

  public void setOverdue(Date overdue) {
    this.overdue = overdue;
  }

  public Integer getDueForPPM() {
    return dueForPPM;
  }

  public void setDueForPPM(Integer dueForPPM) {
    this.dueForPPM = dueForPPM;
  }

  public Date getTncDateInDate() {
    return tncDateInDate;
  }

  public void setTncDateInDate(Date tncDateInDate) {
    this.tncDateInDate = tncDateInDate;
  }

  public Date getPpmDateInDate() {
    return ppmDateInDate;
  }

  public void setPpmDateInDate(Date ppmDateInDate) {
    this.ppmDateInDate = ppmDateInDate;
  }

  public void setTncDate(String tncDate) {
    this.tncDate = tncDate;
  }

  public void setPpmDate(String ppmDate) {
    this.ppmDate = ppmDate;
  }

  public String getTncDate() {
    return tncDate;
  }

  public String getPpmDate() {
    return ppmDate;
  }

  public String getDueStatus() {
    return dueStatus;
  }

  public void setDueStatus(String dueStatus) {
    this.dueStatus = dueStatus;
  }

  public String getPpmDateInString() {
    return ppmDateInString;
  }

  public void setPpmDateInString(String ppmDateInString) {
    this.ppmDateInString = ppmDateInString;
  }

  public String getTncDateInString() {
    return tncDateInString;
  }

  public void setTncDateInString(String tncDateInString) {
    this.tncDateInString = tncDateInString;
  }

  @Override
  public String toString() {
    return this.serialNumber + ", " + this.tncDate + ", " + this.ppmDate + ", " + this.historyCount + ", "
        + this.lastUpdatedInLong + ", " + this.dueForPPM + ", " + this.almostDue + ", " + this.overdue;
  }

}
