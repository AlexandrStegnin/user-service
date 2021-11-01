package com.ddkolesnik.userservice.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BalanceDTO {

  @Builder.Default
  String accountNumber = "";
  @Builder.Default
  BigDecimal sum = BigDecimal.ZERO;

}
