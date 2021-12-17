package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.model.domain.Account;
import com.ddkolesnik.userservice.model.domain.AccountTransaction;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;
import java.util.Date;
import java.util.Optional;

/**
 * @author Alexandr Stegnin
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AccountTransactionDTO {

  Long id;
  Date txDate;
  String operationType;
  String payer;
  String owner;
  String recipient;
  BigDecimal cash;
  String cashType;
  boolean blocked;

  public AccountTransactionDTO(AccountTransaction transaction) {
    this.id = transaction.getId();
    this.txDate = transaction.getTxDate();
    this.operationType = transaction.getOperationType().getTitle();
    this.payer = getName(transaction.getPayer());
    this.owner = getName(transaction.getOwner());
    this.recipient = getName(transaction.getRecipient());
    this.cashType = transaction.getCashType().getTitle();
    this.cash = transaction.getCash();
    this.blocked = transaction.isBlocked();
  }

  private String getName(Account account) {
    return Optional.ofNullable(account.getOwnerName())
        .orElse("");
  }

}
