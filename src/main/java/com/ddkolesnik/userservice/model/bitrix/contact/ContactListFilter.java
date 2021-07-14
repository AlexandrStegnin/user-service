package com.ddkolesnik.userservice.model.bitrix.contact;

import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

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
  }

}
