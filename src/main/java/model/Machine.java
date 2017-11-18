package model;

import java.io.Serializable;


public class Machine implements Serializable {

  private static final long serialVersionUID = -8335914162232634551L;

  private String serialNumber;
  private String customer;
  private String state;
  private String model;
  private String tncDate;
  private String status;
  private String personInCharge;
  private String lastUpdated;
  private String dateOfCreation;
  private String reportedBy;
  private String additionalNotes;
  private String accountType;
  private String ppmDate;
  private String brand;
  private long twoWeeksBeforePPMDate;
  private long twoWeeksAfterPPMDate;

  public Machine(String serialNumber, String customer, String state, String accountType,
      String model, String tncDate, String status, String personInCharge, String reportedBy,
      String additionalNotes, String ppmDate, String brand) {
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

  public Machine(String serialNumber, String customer, String state, String accountType,
      String model, String tncDate, String status, String personInCharge, String reportedBy,
      String additionalNotes, String ppmDate, String brand, String lastUpdated,
      String dateOfCreation, long twoWeeksBeforePPMDate, long twoWeeksAfterPPMDate) {
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
    this.twoWeeksBeforePPMDate = twoWeeksBeforePPMDate;
    this.twoWeeksAfterPPMDate = twoWeeksAfterPPMDate;
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

  public String getTncDate() {
    return tncDate;
  }

  public void setTncDate(String tncDate) {
    this.tncDate = tncDate;
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

  public void updateFields(String serialNumber, String customer, String state, String accountType,
      String model, String tncDate, String status, String personInCharge, String reportedBy,
      String additionalNotes, String ppmDate, String brand) {
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

  public String getPpmDate() {
    return ppmDate;
  }

  public void setPpmDate(String ppmDate) {
    this.ppmDate = ppmDate;
  }

  public String getBrand() {
    return brand;
  }

  public void setBrand(String brand) {
    this.brand = brand;
  }

  public long getTwoWeeksBeforePPMDate() {
    return twoWeeksBeforePPMDate;
  }

  public void setTwoWeeksBeforePPMDate(long twoWeeksBeforePPMDate) {
    this.twoWeeksBeforePPMDate = twoWeeksBeforePPMDate;
  }

  public long getTwoWeeksAfterPPMDate() {
    return twoWeeksAfterPPMDate;
  }

  public void setTwoWeeksAfterPPMDate(long twoWeeksAfterPPMDate) {
    this.twoWeeksAfterPPMDate = twoWeeksAfterPPMDate;
  }
}
