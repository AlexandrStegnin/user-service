package com.ddkolesnik.userservice.model.bitrix.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
    select.add("NAME");
    select.add("SECOND_NAME");
    select.add("LAST_NAME");
    select.add("EMAIL");
    select.add("UF_CRM_1554359872664");
    select.add("UF_CRM_1623241031");
    select.add("UF_CRM_1623241054");
    select.add("BIRTHDATE");
    select.add("UF_CRM_1623241366");
    select.add("RQ_IDENT_DOC");
    select.add("RQ_IDENT_DOC_DATE");
    select.add("CITY");
    select.add("ADDRESS_1");
    select.add("ADDRESS_2");
    select.add("UF_CRM_1627978615");
  }

  public ContactListFilter(Map<String, String[]> filter) {
    this();
    this.filter = filter;
  }

}
