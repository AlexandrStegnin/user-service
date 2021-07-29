package com.ddkolesnik.userservice.enums;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */
@Getter
@AllArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum OwnerType {

    UNDEFINED(0, "Не определён"),
    INVESTOR(1, "Инвестор"),
    FACILITY(2, "Объект"),
    UNDER_FACILITY(3, "Подобъект"),
    ROOM(4, "Помещение");

    int id;

    String title;

}
