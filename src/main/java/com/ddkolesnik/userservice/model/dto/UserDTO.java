package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.enums.Gender;
import com.ddkolesnik.userservice.model.bitrix.enums.TaxStatus;
import com.ddkolesnik.userservice.utils.PhoneUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Locale;
import java.util.Objects;
import java.util.Optional;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = { "id", "name", "email", "individual", "selfEmployed"})
public class UserDTO {

  Integer id;
  String name;
  String secondName;
  String lastName;
  String email;
  String phone;
  String confirmCode;
  String inn;
  String snils;
  PassportDTO passport;
  String address;
  Gender gender;
  String birthdate;
  String password;
  @Builder.Default
  boolean individual = false;
  @Builder.Default
  boolean selfEmployed = false;
  String placeOfBirth;
  Integer bitrixId;
  TaxStatus taxStatus;
  @Builder.Default
  boolean accredited = false;

  public String getPhone() {
    return PhoneUtils.cleanPhone(this.phone);
  }

  public String getFullName() {
    var fullName = this.lastName + " " + this.name + " ";
    return (fullName + Optional.ofNullable(this.secondName).orElse("")).trim().toUpperCase(Locale.ROOT);
  }

  public String getFormattedPhone() {
    if (Objects.nonNull(this.phone)) {
      var cleanPhone = PhoneUtils.cleanPhone(this.phone);
      var countryCode = cleanPhone.substring(0, 2);
      var operatorPart = cleanPhone.substring(2, 5);
      return String.format("%s (%s) %s-%s-%s",
          countryCode, operatorPart, cleanPhone.substring(5, 8),
          cleanPhone.substring(8, 10), cleanPhone.substring(10, 12));
    }
    return null;
  }

}
