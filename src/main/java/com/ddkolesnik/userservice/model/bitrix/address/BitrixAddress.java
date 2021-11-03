package com.ddkolesnik.userservice.model.bitrix.address;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BitrixAddress {

  @Builder.Default
  @JsonProperty("TYPE_ID")
  String typeId = "1";

  @Builder.Default
  @JsonProperty("ENTITY_TYPE_ID")
  String entityTypeId = "8";

  @JsonProperty("ENTITY_ID")
  String entityId;

  @JsonProperty("ADDRESS_2")
  String address;

}
