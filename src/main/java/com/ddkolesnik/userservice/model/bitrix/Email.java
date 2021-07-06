package com.ddkolesnik.userservice.model.bitrix;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Email {

  @JsonProperty("VALUE_TYPE")
  String valueType;

  @JsonProperty("VALUE")
  String value;

  @JsonProperty("TYPE_ID")
  String typeId;

  @JsonCreator
  public Email(@JsonProperty("VALUE_TYPE") String valueType,
               @JsonProperty("VALUE") String value,
               @JsonProperty("TYPE_ID") String typeId) {
    this.valueType = valueType;
    this.value = value;
    this.typeId = typeId;
  }

}
