package util;

import java.util.HashMap;
import java.util.Map;

public enum MaintenanceEnum {
  WORK_ORDER("Work Order"), WORK_ORDER_DATE("W.O. Date"), ACTION_TAKEN("Action Taken"), LAST_UPDATED(
      "Last Updated"), DATE_OF_CREATION("dateOfCreation"), REPORTED_BY("Reported By"), WO_TYPE(
          "W.O. Type"), WORK_ORDER_DATE_IN_STRING("W.O. Date in String"), LAST_UPDATED_IN_LONG("Last Updated in Long");

  String prop;
  private static final Map<String, MaintenanceEnum> lookup = new HashMap<String, MaintenanceEnum>();

  static {
    for (MaintenanceEnum e : MaintenanceEnum.values()) {
      lookup.put(e.getProp(), e);
    }
  }

  MaintenanceEnum(String prop) {
    this.prop = prop;
  }

  public String getProp() {
    return prop;
  }

  public static MaintenanceEnum getEnum(String prop) {
    return lookup.get(prop);
  }

  public static String getDBEnumFromProp(String prop) {
    return getEnum(prop).getDBEnum();
  }

  public String getDBEnum() {
    switch (this) {
    case WORK_ORDER:
      return "workOrderNumber";
    case WORK_ORDER_DATE:
      return "workOrderDate";
    case ACTION_TAKEN:
      return "actionTaken";
    case LAST_UPDATED:
      return "lastUpdated";
    case DATE_OF_CREATION:
      return "dateOfCreation";
    case REPORTED_BY:
      return "reportedBy";
    case WO_TYPE:
      return "workOrderType";
    case WORK_ORDER_DATE_IN_STRING:
      return "workOrderDateInString";
    case LAST_UPDATED_IN_LONG:
      return "lastUpdatedInLong";
    default:
      break;
    }

    return "";
  }
}
