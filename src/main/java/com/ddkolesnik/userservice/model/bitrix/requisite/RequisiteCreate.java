package com.ddkolesnik.userservice.model.bitrix.requisite;

import java.util.LinkedHashMap;
import java.util.Map;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 14.07.2021
 */
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequisiteCreate {

  @Builder.Default
  Map<String, Object> fields = new LinkedHashMap<>();

}
