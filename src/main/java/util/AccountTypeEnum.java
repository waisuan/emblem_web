package util;

import java.util.HashMap;
import java.util.Map;

public enum AccountTypeEnum {
  HEALTH_CLINIC("Health Clinic"), HOSPITAL("Hospital"), PRIVATE_HOSPITAL("Private Hospital"), UNIVERSITY(
      "University"), RESEARCH("Research"), OTHER("Other");
  private String prop;
  private static final Map<String, AccountTypeEnum> lookup = new HashMap<String, AccountTypeEnum>();

  static {
    for (AccountTypeEnum e : AccountTypeEnum.values()) {
      lookup.put(e.getProp(), e);
    }
  }

  AccountTypeEnum(String prop) {
    this.prop = prop;
  }

  public String getProp() {
    return prop;
  }

  public static AccountTypeEnum getEnum(String prop) {
    return lookup.get(prop);
  }

  public static String getDBEnumFromProp(String prop) {
    return getEnum(prop).getDBEnum();
  }

  public String getDBEnum() {
    switch (this) {
      case HEALTH_CLINIC:
        return "healthClinic";
      case HOSPITAL:
        return "hospital";
      case PRIVATE_HOSPITAL:
        return "privateHospital";
      case UNIVERSITY:
        return "university";
      case RESEARCH:
        return "research";
      case OTHER:
        return "other";
      default:
        break;
    }

    return "";
  }
}
