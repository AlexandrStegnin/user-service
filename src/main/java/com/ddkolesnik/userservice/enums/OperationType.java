package com.ddkolesnik.userservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.stream.Stream;

/**
 * @author Alexandr Stegnin
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum OperationType {

  UNDEFINED(0, "Не определено"),
  DEBIT(1, "Приход"),
  CREDIT(2, "Расход");

  int id;
  String title;

  public static OperationType fromTitle(String title) {
    return Stream.of(values())
        .filter(type -> type.getTitle().equalsIgnoreCase(title))
        .findFirst()
        .orElse(UNDEFINED);
  }

  public static OperationType fromId(int id) {
    return Stream.of(values())
        .filter(type -> type.getId() == id)
        .findFirst()
        .orElse(UNDEFINED);
  }

}
