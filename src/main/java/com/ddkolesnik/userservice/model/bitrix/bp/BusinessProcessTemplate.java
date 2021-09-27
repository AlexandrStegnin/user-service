package com.ddkolesnik.userservice.model.bitrix.bp;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum BusinessProcessTemplate {

  CONFIRM_PHONE("574"),
  CONFIRM_OLD_PHONE("579"),
  CONFIRM_NEW_PHONE("580"),
  CHANGE_PHONE("581"),
  CHANGE_PASSWORD("586");

  String id;

}
