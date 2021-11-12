package com.ddkolesnik.userservice.model.dto;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.math.RoundingMode;

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

  public BigDecimal getSum() {
    return sum.setScale(2, RoundingMode.CEILING);
  }

  public BalanceDTO(AccountDTO accountDTO) {
    this.accountNumber = accountDTO.getOwner().getAccountNumber();
    this.sum= accountDTO.getSummary();
  }

}
