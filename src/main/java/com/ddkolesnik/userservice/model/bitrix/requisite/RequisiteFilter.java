package com.ddkolesnik.userservice.model.bitrix.requisite;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Map;

/**
 * @author Aleksandr Stegnin on 14.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class RequisiteFilter {

  Map<String, String> filter;

}
