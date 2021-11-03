package com.ddkolesnik.userservice.model.bitrix.address;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Aleksandr Stegnin on 14.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Address {

  @Builder.Default
  Map<String, Object> fields = new LinkedHashMap<>();

}
