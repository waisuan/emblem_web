package model;

import java.io.Serializable;


public class MaintenanceHistory implements Serializable {

  private static final long serialVersionUID = -2250561711852519405L;

  private String serialNumber;
  private String workOrderNumber;
  private String workOrderDate;
  private String actionTaken;
  private long lastUpdated;
  private long dateOfCreation;
  private String reportedBy;
  private String workOrderType;

  public MaintenanceHistory(String serialNumber, String workOrderNumber, String workOrderDate,
      String actionTaken, String reportedBy, String workOrderType) {
    this.serialNumber = serialNumber;
    this.workOrderNumber = workOrderNumber;
    this.workOrderDate = workOrderDate;
    this.actionTaken = actionTaken;
    this.reportedBy = reportedBy;
    this.workOrderType = workOrderType;
  }

  public MaintenanceHistory(String serialNumber, String workOrderNumber, String workOrderDate,
      String actionTaken, String reportedBy, String workOrderType, long lastUpdated,
      long dateOfCreation) {
    this.serialNumber = serialNumber;
    this.workOrderNumber = workOrderNumber;
    this.workOrderDate = workOrderDate;
    this.actionTaken = actionTaken;
    this.reportedBy = reportedBy;
    this.workOrderType = workOrderType;
    this.lastUpdated = lastUpdated;
    this.dateOfCreation = dateOfCreation;
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

  public long getLastUpdated() {
    return lastUpdated;
  }

  public void setLastUpdated(long lastUpdated) {
    this.lastUpdated = lastUpdated;
  }

  public void updateFields(String serialNumber, String workOrderNumber, String workOrderDate,
      String actionTaken, String reportedBy, String workOrderType) {
    this.serialNumber = serialNumber;
    this.workOrderNumber = workOrderNumber;
    this.workOrderDate = workOrderDate;
    this.actionTaken = actionTaken;
    this.reportedBy = reportedBy;
    this.workOrderType = workOrderType;
  }

  public long getDateOfCreation() {
    return dateOfCreation;
  }

  public void setDateOfCreation(long dateOfCreation) {
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
}
