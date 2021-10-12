package com.ddkolesnik.userservice.model.bitrix.contact;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Contact {

  Integer id;

  @Builder.Default
  Map<String, Object> fields = new LinkedHashMap<>();

}
