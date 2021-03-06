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
  RESTORE_PASSWORD("586"),
  RETRY_SEND_MESSAGE("593"),
  UPDATE_FIELDS_NOTIFIER("594"),
  CREATE_CONTACT_NOTIFIER("595");

  String id;

}
