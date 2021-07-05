package com.ddkolesnik.userservice.model;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DuplicateResult {

  @JsonProperty("result")
  Duplicate result;

}
