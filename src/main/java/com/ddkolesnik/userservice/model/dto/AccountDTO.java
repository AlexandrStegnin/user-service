package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.model.domain.Account;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * @author Alexandr Stegnin
 */

@Data
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountDTO {

  Account owner;
  BigDecimal summary;
  String ownerName;

  public AccountDTO(Account owner, Double summary, String ownerName) {
    this(owner, new BigDecimal(String.valueOf(summary)), ownerName);
  }

  public AccountDTO(Account owner, Integer summary, String ownerName) {
    this(owner, new BigDecimal(String.valueOf(summary)), ownerName);
  }

}
