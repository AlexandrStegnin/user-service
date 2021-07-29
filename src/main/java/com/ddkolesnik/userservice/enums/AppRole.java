package com.ddkolesnik.userservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum AppRole {

  INVESTOR(5);

  long id;

}
