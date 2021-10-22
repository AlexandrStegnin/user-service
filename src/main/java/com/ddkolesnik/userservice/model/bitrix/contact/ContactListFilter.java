package com.ddkolesnik.userservice.model.bitrix.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactListFilter {

  @JsonProperty("select")
  List<String> select = new ArrayList<>();

  String[] params;

  Map<String, String[]> filter = new HashMap<>(1);

  public ContactListFilter() {
    select.add(CONTACT_NAME);
    select.add(CONTACT_SECOND_NAME);
    select.add(CONTACT_LAST_NAME);
    select.add(CONTACT_EMAIL);
    select.add(CONTACT_GENDER);
    select.add(CONTACT_IS_INDIVIDUAL);
    select.add(CONTACT_IS_SELF_EMPLOYED);
    select.add(CONTACT_BIRTHDATE);
    select.add(CONTACT_PLACE_OF_BIRTH);
    select.add(IDENT_DOC_NAME);
    select.add(PASSPORT_ISSUED_AT);
    select.add(CONTACT_CONFIRM_CODE);
    select.add(CONTACT_RAW_PASSWORD);
    select.add(CONTACT_PHONE);
    select.add(CONTACT_TAX_STATUS);
    select.add(CONTACT_NEW_PHONE);
    select.add(CONTACT_ADDRESS);
    select.add(IS_CONTACT_ACCREDITED);
  }

  public ContactListFilter(Map<String, String[]> filter) {
    this();
    this.filter = filter;
  }

}
