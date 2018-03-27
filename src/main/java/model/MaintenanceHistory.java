package model;

import java.io.Serializable;

public class MaintenanceHistory implements Serializable {

  private static final long serialVersionUID = -2250561711852519405L;

  private String serialNumber;
  private String workOrderNumber;
  private String workOrderDate;
  private String actionTaken;
  private String lastUpdated;
  private String dateOfCreation;
  private String reportedBy;
  private String workOrderType;
  private String workOrderDateInString;
  private Long lastUpdatedInLong;

  public MaintenanceHistory(String serialNumber, String workOrderNumber, String workOrderDate, String actionTaken,
      String reportedBy, String workOrderType) {
    this.serialNumber = serialNumber;
    this.workOrderNumber = workOrderNumber;
    this.workOrderDate = workOrderDate;
    this.actionTaken = actionTaken;
    this.reportedBy = reportedBy;
    this.workOrderType = workOrderType;
  }

  public MaintenanceHistory(String serialNumber, String workOrderNumber, String workOrderDate, String actionTaken,
      String reportedBy, String workOrderType, String lastUpdated, String dateOfCreation, String workOrderDateInString,
      Long lastUpdatedInLong) {
    this.serialNumber = serialNumber;
    this.workOrderNumber = workOrderNumber;
    this.workOrderDate = workOrderDate;
    this.actionTaken = actionTaken;
    this.reportedBy = reportedBy;
    this.workOrderType = workOrderType;
    this.lastUpdated = lastUpdated;
    this.dateOfCreation = dateOfCreation;
    this.workOrderDateInString = workOrderDateInString;
    this.lastUpdatedInLong = lastUpdatedInLong;
  }

  public String getSerialNumber() {
    return serialNumber;
  }

  public void setSerialNumber(String serialNumber) {
    this.serialNumber = serialNumber;
  }

  public String getWorkOrderNumber() {
    return workOrderNumber;
  }

  public void setWorkOrderNumber(String workOrderNumber) {
    this.workOrderNumber = workOrderNumber;
  }

  public String getWorkOrderDate() {
    return workOrderDate;
  }

  public void setWorkOrderDate(String workOrderDate) {
    this.workOrderDate = workOrderDate;
  }

  public String getActionTaken() {
    return actionTaken;
  }

  public void setActionTaken(String actionTaken) {
    this.actionTaken = actionTaken;
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

  public String getWorkOrderType() {
    return workOrderType;
  }

  public void setWorkOrderType(String workOrderType) {
    this.workOrderType = workOrderType;
  }

  public String getWorkOrderDateInString() {
    return workOrderDateInString;
  }

  public void setWorkOrderDateInString(String workOrderDateInString) {
    this.workOrderDateInString = workOrderDateInString;
  }

  public Long getLastUpdatedInLong() {
    return lastUpdatedInLong;
  }

  public void setLastUpdatedInLong(Long lastUpdatedInLong) {
    this.lastUpdatedInLong = lastUpdatedInLong;
  }

}
