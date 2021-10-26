package com.ddkolesnik.userservice.model.bitrix.requisite.bank;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankRequisite {

  @JsonProperty(BANK_NAME)
  String bankName;

  @JsonProperty(BANK_BIK)
  String bik;

  @JsonProperty(BANK_ACCOUNT_NUMBER)
  String accountNumber;

  @JsonProperty(BANK_CORR_ACCOUNT_NUMBER)
  String correspondentAccountNumber;

}
