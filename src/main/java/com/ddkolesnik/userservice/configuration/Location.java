package com.ddkolesnik.userservice.configuration;

import lombok.experimental.UtilityClass;

/**
 * @author Aleksandr Stegnin on 11.07.2021
 */
@UtilityClass
public class Location {

  public final String HOME_URL = "/";
  public final String LOGIN_URL = "/login";
  public final String LOGOUT_URL = "/logout";
  public final String REGISTRATION_URL = "/registration";
  public final String CONFIRM_URL = "/confirm";
  public final String CREATE_USER = "/create";
  public final String UPDATE_USER = "/update";
  public final String RESTORE_PASSWORD = "/restore-password";
  public final String CHANGE_PASSWORD = "/change-password";
  public final String CONFIRM_OLD_PHONE = "/confirm-old-phone";
  public final String CHECK_CONFIRM_CODE = "/check-confirm-code";
  public final String CONFIRM_NEW_PHONE = "/confirm-new-phone";
  public final String CHANGE_PHONE = "/change-phone";
  public final String CONFIRM_BY_EMAIL = "/confirm-by-email";
  public final String PROFILE = "/profile";

}
