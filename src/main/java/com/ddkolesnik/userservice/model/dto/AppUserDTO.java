package com.ddkolesnik.userservice.model.dto;

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
public class AppUserDTO {

  String email;
  String confirmCode;

}
