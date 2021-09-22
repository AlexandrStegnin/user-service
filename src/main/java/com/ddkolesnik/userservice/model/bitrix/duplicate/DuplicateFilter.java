package com.ddkolesnik.userservice.model.bitrix.duplicate;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DuplicateFilter {

  @JsonProperty("entity_type")
  String entityType = "CONTACT";

  @JsonProperty("type")
  String type = "PHONE";

  @JsonProperty("values")
  List<String> values = new ArrayList<>();

  public DuplicateFilter(String phone) {
    this.values.add(phone);
  }

}
