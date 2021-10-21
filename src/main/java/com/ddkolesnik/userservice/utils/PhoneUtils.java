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

  public boolean isWrongFormat(String phone) {
    return Objects.isNull(phone) || phone.isBlank() || phone.trim().length() != 12;
  }

}
