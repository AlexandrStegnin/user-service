package com.ddkolesnik.userservice.configuration;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

/**
 * @author Aleksandr Stegnin on 11.07.2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Location {

  public static final String HOME_URL = "/";
  public static final String LOGIN_URL = "/login";
  public static final String LOGOUT_URL = "/logout";
  public static final String REGISTRATION_URL = "/registration";
  public static final String CONFIRM_URL = "/confirm";
  public static final String CREATE_USER = "/create";
  public static final String UPDATE_USER = "/update";
  public static final String RESTORE_PASSWORD = "/restore-password";
  public static final String CHANGE_PASSWORD = "/change-password";
  public static final String CONFIRM_OLD_PHONE = "/confirm-old-phone";
  public static final String CHECK_CONFIRM_CODE = "/check-confirm-code";
  public static final String CONFIRM_NEW_PHONE = "/confirm-new-phone";
  public static final String CHANGE_PHONE = "/change-phone";
  public static final String CONFIRM_BY_EMAIL = "/confirm-by-email";
  public static final String RETRY_SEND_CONFIRM_MESSAGE = "/retry-send";
  public static final String PROFILE = "/profile";
  public static final String PROFILE_ACCREDITED = PROFILE + "/accredited";
  public static final String BANK_REQUISITES = "/bank-requisites";
  public static final String UNION_PROFIT = "/union-profit";
  public static final String KIND_ON_PROJECT = "/kind-on-project";
  public static final String INVESTMENTS = "/investments";

}
