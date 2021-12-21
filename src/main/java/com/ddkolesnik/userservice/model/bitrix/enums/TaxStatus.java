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

  INDIVIDUAL("ФИЗ ЛИЦО", 2303, 3),
  LEGAL_ENTITY("ЮР ЛИЦО", 2304, 1),
  BUSINESSMAN("ИП", 2305, 5),
  SELF_EMPLOYED("САМОЗАНЯТЫЙ", 2306, 3);

  String title;
  int code;
  int presetId;

  public static TaxStatus fromCode(Integer code) {
    return Stream.of(values())
        .filter(Objects::nonNull)
        .filter(value -> value.getCode() == code)
        .findAny()
        .orElseThrow(IllegalArgumentException::new);
  }

}
