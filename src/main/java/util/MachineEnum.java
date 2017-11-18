package util;

import java.util.HashMap;
import java.util.Map;

public enum MachineEnum {
  SERIAL_NUM("Serial No."), STATE("State"), CUSTOMER("Customer"), ACCOUNT_TYPE("Acc. Type"), MODEL(
      "Model"), TNC_DATE("T&C Date"), STATUS("Status"), PERSON_IN_CHARGE("Person In Charge"), LAST_UPDATED(
          "Last Updated"), DATE_OF_CREATION("Date of Creation"), ALL("All"), REPORTED_BY("Reported By"), ADDITIONAL_NOTES(
              "Additional Notes"), PPM_DATE("PPM Date"), BRAND("Brand"), TWO_WEEKS_BEFORE(
                  "Two Weeks Before PPM Date"), TWO_WEEKS_AFTER("Two Weeks After PPM Date"), DUE_FOR_PPM(
                      "Due for PPM");

  private String prop;
  private static final Map<String, MachineEnum> lookup = new HashMap<String, MachineEnum>();

  static {
    for (MachineEnum e : MachineEnum.values()) {
      lookup.put(e.getProp(), e);
    }
  }

  MachineEnum(String prop) {
    this.prop = prop;
  }

  public String getProp() {
    return prop;
  }

  public static MachineEnum getEnum(String prop) {
    return lookup.get(prop);
  }

  public static String getDBEnumFromProp(String prop) {
    return getEnum(prop).getDBEnum();
  }

  public String getDBEnum() {
    switch (this) {
      case SERIAL_NUM:
        return "serialNumber";
      case STATE:
        return "state";
      case CUSTOMER:
        return "customer";
      case ACCOUNT_TYPE:
        return "accountType";
      case MODEL:
        return "model";
      case TNC_DATE:
        return "tncDate";
      case STATUS:
        return "status";
      case PERSON_IN_CHARGE:
        return "personInCharge";
      case LAST_UPDATED:
        return "lastUpdated";
      case DATE_OF_CREATION:
        return "dateOfCreation";
      case ALL:
        return "all";
      case REPORTED_BY:
        return "reportedBy";
      case ADDITIONAL_NOTES:
        return "additionalNotes";
      case PPM_DATE:
        return "ppmDate";
      case BRAND:
        return "brand";
      case TWO_WEEKS_BEFORE:
        return "twoWeeksBeforePPMDate";
      case TWO_WEEKS_AFTER:
        return "twoWeeksAfterPPMDate";
      case DUE_FOR_PPM:
        return "dueForPPM";
      default:
        break;
    }

    return "";
  }
}
