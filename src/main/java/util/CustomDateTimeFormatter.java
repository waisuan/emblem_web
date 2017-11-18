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
  private final static DateFormat inputFormat =
      new SimpleDateFormat("EEE MMM dd HH:mm:ss zzz yyyy");
  private final static DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
  private final static DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd/MM/yyyy");

  public static String formatDate(String input) {
    if (null == input || input.equals(""))
      return input;

    try {
      return formatter.format(inputFormat.parse(input));
    } catch (ParseException e) {
      e.printStackTrace();
    }

    return input;
  }

  public static String convertLocalDateToString(LocalDate localDate) {
    if (null == localDate)
      return "";

    return Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant()).toString();
  }

  public static LocalDate convertStringToLocalDate(String input) {
    if (null == input || input.equals(""))
      return null;

    return LocalDate.parse(input, dtf);
  }

  public static Date convertLocalDateToDate(LocalDate localDate) {
    if (null == localDate)
      return null;

    Date date = Date.from(localDate.atStartOfDay(ZoneId.systemDefault()).toInstant());

    return date;
  }

  public static Date convertStringToDate(String input) {
    if (null == input || input.equals(""))
      return null;

    try {
      Date date = inputFormat.parse(input);
      return date;
    } catch (ParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static long generateTimestamp() {
    java.util.Date today = new java.util.Date();
    java.sql.Timestamp ts = new java.sql.Timestamp(today.getTime());

    return ts.getTime();
  }

  public static String convertLongToDateString(long milliseconds) {
    Date date = new Date(milliseconds);

    return formatDate(date.toString());
  }

  public static long convertDateStringToLong(String input) {
    Date date = convertStringToDate(input);
    java.sql.Timestamp ts = new java.sql.Timestamp(date.getTime());
    return ts.getTime();
  }

  public static long getTwoWeeksBeforeDate(Date date) {
    Calendar calendar = Calendar.getInstance(); // this would default to now
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, -14);
    java.sql.Timestamp ts = new java.sql.Timestamp(calendar.getTime().getTime());
    return ts.getTime();
  }

  public static long getTwoWeeksAfterDate(Date date) {
    Calendar calendar = Calendar.getInstance(); // this would default to now
    calendar.setTime(date);
    calendar.add(Calendar.DAY_OF_MONTH, 14);
    java.sql.Timestamp ts = new java.sql.Timestamp(calendar.getTime().getTime());
    return ts.getTime();
  }
}