package com.ddkolesnik.userservice.model;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 11.07.2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ConfirmPhoneDTO {

  int confirmCode;

}
