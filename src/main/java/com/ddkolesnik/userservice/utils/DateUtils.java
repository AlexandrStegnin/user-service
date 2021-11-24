package com.ddkolesnik.userservice.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@UtilityClass
public class DateUtils {

  private static final String DDMMYYYY = "dd.MM.yyyy";
  private static final String YYYYMMDD = "yyyy-MM-dd";

  public static String convert(String issuedAt) {
    try {
      return convertToDDMMYYYY(issuedAt);
    } catch (DateTimeParseException e) {
      e.printStackTrace();
    }
    try {
      return convertToYYYYMMDD(issuedAt);
    } catch (DateTimeParseException e) {
      e.printStackTrace();
    }
    return null;
  }

  public static String convertToDDMMYYYY(String issuedAt) {
    return convertTo(issuedAt, YYYYMMDD, DDMMYYYY);
  }

  public static String convertToYYYYMMDD(String issuedAt) {
    return convertTo(issuedAt, DDMMYYYY, YYYYMMDD);
  }

  private static String convertTo(String date, String patternFrom, String patternTo) {
    if (Objects.isNull(date) || date.isBlank()) {
      return null;
    }
    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(patternFrom));
    return localDate.format(DateTimeFormatter.ofPattern(patternTo));
  }

}
