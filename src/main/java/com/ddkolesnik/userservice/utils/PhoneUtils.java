package com.ddkolesnik.userservice.utils;

import lombok.experimental.UtilityClass;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@UtilityClass
public class PhoneUtils {

  public String clearPhone(String phone) {
    return phone.replaceAll("(\\+)|\\D", "$1");
  }

  public boolean isWrongFormat(String phone) {
    return Objects.isNull(phone) || phone.isBlank() || phone.trim().length() != 12;
  }

}
