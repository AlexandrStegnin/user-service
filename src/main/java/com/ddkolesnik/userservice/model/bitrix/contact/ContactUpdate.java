package com.ddkolesnik.userservice.model.bitrix.contact;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * @author Aleksandr Stegnin on 06.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactUpdate {

  Integer id;
  @Builder.Default
  Map<String, Object> fields = new LinkedHashMap<>();

}
