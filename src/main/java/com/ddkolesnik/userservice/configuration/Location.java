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

}
