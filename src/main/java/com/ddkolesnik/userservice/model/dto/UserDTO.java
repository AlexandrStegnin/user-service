package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.enums.Gender;
import com.ddkolesnik.userservice.model.bitrix.enums.TaxStatus;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

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
  AddressDTO address;
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

}
