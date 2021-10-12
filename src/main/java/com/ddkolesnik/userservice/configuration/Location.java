package com.ddkolesnik.userservice.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Aleksandr Stegnin on 11.07.2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Location {

  public static final String HOME_URL = "/";

  public static final String REGISTRATION_URL = "registration";

  public static final String CONFIRM_URL = "confirm";

  public static final String CREATE_USER = "create";

  public static final String RESTORE_PASSWORD = "restore-password";

  public static final String CHANGE_PASSWORD = "change-password";

  public static final String CONFIRM_OLD_PHONE = "confirm-old-phone";

  public static final String CHECK_CONFIRM_CODE = "check-confirm-code";

  public static final String CONFIRM_NEW_PHONE = "confirm-new-phone";

  public static final String CHANGE_PHONE = "change-phone";

  public static final String CONFIRM_BY_EMAIL = "confirm-by-email";

}
