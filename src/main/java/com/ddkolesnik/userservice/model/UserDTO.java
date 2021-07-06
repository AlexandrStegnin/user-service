package com.ddkolesnik.userservice.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserDTO {

  String name;
  String surname;
  String patronymic;
  String email;
  String phone;

}
