package com.ddkolesnik.userservice.model.bitrix.utils;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 06.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Phone {

  @JsonProperty("ID")
  String id;

  @JsonProperty("VALUE_TYPE")
  String valueType;

  @JsonProperty("VALUE")
  String value;

  @JsonCreator
  public Phone(@JsonProperty("ID") String id,
               @JsonProperty("VALUE_TYPE") String valueType,
               @JsonProperty("VALUE") String value) {
    this.id = id;
    this.valueType = valueType;
    this.value = value;
  }

}
