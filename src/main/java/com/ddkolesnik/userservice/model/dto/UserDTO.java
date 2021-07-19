package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.enums.Gender;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
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

}
