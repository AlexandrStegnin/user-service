package com.ddkolesnik.userservice.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@UtilityClass
public class PhoneUtils {

  public String cleanPhone(String phone) {
    if (Objects.nonNull(phone)) {
      return phone.replaceAll("(\\+)|\\D", "$1");
    }
    return null;
  }

  public String getFormattedPhone(String phone) {
    var cleanPhone = PhoneUtils.cleanPhone(phone);
    var countryCode = cleanPhone.substring(0, 2);
    var operatorPart = cleanPhone.substring(2, 5);
    return String.format("%s (%s) %s-%s-%s",
        countryCode, operatorPart, cleanPhone.substring(5, 8),
        cleanPhone.substring(8, 10), cleanPhone.substring(10, 12));
  }

  public boolean isWrongFormat(String phone) {
    return Objects.isNull(phone) || phone.isBlank() || phone.trim().length() != 12;
  }

}
