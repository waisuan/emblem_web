package util;

import java.util.HashMap;
import java.util.Map;

public enum UserEnum {
  USERNAME("Username"), PASSWORD("Password"), CAN_CREATE("Add"), CAN_EDIT("Edit"), CAN_DELETE("Delete"), CAN_SEARCH(
      "Search"), SALT("Salt"), ALREADY_LOGGED_ON("Logged On"), DATE_OF_CREATION("Date of Creation"), IS_ADMIN(
          "Admin"), LAST_UPDATED("Last Updated"), LAST_UPDATED_IN_LONG("Last Updated In Long");

  private String prop;
  private static final Map<String, UserEnum> lookup = new HashMap<String, UserEnum>();

  static {
    for (UserEnum e : UserEnum.values()) {
      lookup.put(e.getProp(), e);
    }
  }

  UserEnum(String prop) {
    this.prop = prop;
  }

  public String getProp() {
    return prop;
  }

  public static UserEnum getEnum(String prop) {
    return lookup.get(prop);
  }

  public static String getDBEnumFromProp(String prop) {
    return getEnum(prop).getDBEnum();
  }

  public String getDBEnum() {
    switch (this) {
    case USERNAME:
      return "username";
    case PASSWORD:
      return "password";
    case CAN_CREATE:
      return "canCreate";
    case CAN_EDIT:
      return "canEdit";
    case CAN_DELETE:
      return "canDelete";
    case CAN_SEARCH:
      return "canSearch";
    case SALT:
      return "salt";
    case ALREADY_LOGGED_ON:
      return "alreadyLoggedOn";
    case DATE_OF_CREATION:
      return "dateOfCreation";
    case IS_ADMIN:
      return "isAdmin";
    case LAST_UPDATED:
      return "lastUpdated";
    case LAST_UPDATED_IN_LONG:
      return "lastUpdatedInLong";
    default:
      break;
    }

    return "";
  }
}
