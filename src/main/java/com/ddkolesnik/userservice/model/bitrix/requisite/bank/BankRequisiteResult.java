package com.ddkolesnik.userservice.model.bitrix.requisite.bank;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankRequisiteResult {

  ArrayList<BankRequisite> result;
  Integer total;
  LinkedHashMap<?, ?> time;

}
