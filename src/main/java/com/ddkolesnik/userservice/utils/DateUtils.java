package com.ddkolesnik.userservice.utils;

import lombok.experimental.UtilityClass;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@UtilityClass
public class DateUtils {

  public static String convertToDDMMYYYY(String issuedAt) {
    return convertTo(issuedAt, "yyyy-MM-dd", "dd.MM.yyyy");
  }

  public static String convertToYYYYMMDD(String issuedAt) {
    return convertTo(issuedAt, "dd.MM.yyyy", "yyyy-MM-dd");
  }

  private static String convertTo(String date, String patternFrom, String patternTo) {
    if (Objects.isNull(date) || date.isBlank()) {
      return null;
    }
    LocalDate localDate = LocalDate.parse(date, DateTimeFormatter.ofPattern(patternFrom));
    return localDate.format(DateTimeFormatter.ofPattern(patternTo));
  }

}
