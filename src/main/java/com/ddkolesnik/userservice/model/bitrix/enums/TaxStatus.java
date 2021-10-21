package com.ddkolesnik.userservice.model.bitrix.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.Objects;
import java.util.stream.Stream;

/**
 * @author Alexandr Stegnin
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum TaxStatus {

  INDIVIDUAL("ФИЗ ЛИЦО", 2303),
  LEGAL_ENTITY("ЮР ЛИЦО", 2304),
  BUSINESSMAN("ИП", 2305),
  SELF_EMPLOYED("САМОЗАНЯТЫЙ", 2306);

  String title;
  int code;

  public static TaxStatus fromCode(Integer code) {
    if (Objects.isNull(code)) {
      return null;
    }
    return Stream.of(values())
        .filter(value -> value.getCode() == code)
        .findAny()
        .orElse(null);
  }

}
