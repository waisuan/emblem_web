package util;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Calendar;
import java.util.Date;

public class CustomDateTimeFormatter {
  private final static DateFormat inputFormat = new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
  private final static DateFormat formatter = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
  private final static DateFormat formatter2 = new SimpleDateFormat("MM/dd/yyyy");
  private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("MM/dd/yyyy");

  public synchronized static String formatDate(String input) {
    if (null == input || input.equals(""))
      return input;

    try {
      return formatter.format(inputFormat.parse(input));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return input;
  }

  public synchronized static String formatDate2(String input) {
    if (null == input || input.equals(""))
      return input;

    try {
      return formatter2.format(inputFormat.parse(input));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return input;
  }

  public synchronized static String convertLocalDateToString(LocalDate localDate) {
    if (null == localDate)
      return "";

    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString();
  }

  public synchronized static LocalDate convertStringToLocalDate(String input) {
    if (null == input || input.equals(""))
      return null;

    return LocalDate.parse(input, dtf);
  }

  public synchronized static Date convertLocalDateToDate(LocalDate localDate) {
    if (null == localDate)
      return null;

    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    return date;
  }

  public synchronized static Date convertStringToDate(String input) {
    if (null == input || input.equals(""))
      return null;

    try {
      Date date = formatter.parse(input);
      return date;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public synchronized static Date convertStringToDate2(String input) {
    if (null == input || input.equals(""))
      return null;

    try {
      Date date = formatter2.parse(input);
      return date;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public synchronized static long generateTimestamp() {
    java.util.Date today = new java.util.Date();
    java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());

    return ts.getTime();
  }

  public synchronized static String convertLongToDateString(Long milliseconds) {
    if (null == milliseconds) {
      return "";
    }

    Date date = new Date(milliseconds);

    return formatDate(date.toString());
  }

  public synchronized static String convertLongToDateString2(Long milliseconds) {
    if (null == milliseconds) {
      return "";
    }

    Date date = new Date(milliseconds);

    return formatDate2(date.toString());
  }

  public synchronized static long convertDateStringToLong(String input) {
    if (input == null) {
      return generateTimestamp();
    }

    Date date = convertStringToDate(input);
    java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
    return ts.getTime();
  }

  public synchronized static String convertDateToString(Date date) {
    return date.toString();
  }

  public synchronized static long convertDateToLong(Date date) {
    java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
    return ts.getTime();
  }

  public synchronized static String convertStringToYYYYMMDD(String date) {
    String[] tokens = date.split("/");
    return tokens[2] + tokens[0] + tokens[1];
  }

  public synchronized static long getNow() {
    Calendar calendar = Calendar.getInstance(); // this would default to now
    java.sql.Timestamp ts = new java.sql.Timestamp(calendar.getTime().getTime());
    return ts.getTime();
  }

  public synchronized static long getXDaysBeforeDate(Date date, int substractDaysBy) {
    Calendar calendar = Calendar.getInstance(); // this would default to now
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, -substractDaysBy);
    java.sql.Timestamp ts = new java.sql.Timestamp(calendar.getTime().getTime());
    return ts.getTime();
  }

  public synchronized static long getXDaysAfterDate(Date date, int addDaysBy) {
    Calendar calendar = Calendar.getInstance(); // this would default to now
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, addDaysBy);
    java.sql.Timestamp ts = new java.sql.Timestamp(calendar.getTime().getTime());
    return ts.getTime();
  }
}
