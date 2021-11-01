package com.ddkolesnik.userservice.enums;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.util.stream.Stream;

/**
 * Вид денег для транзакций
 *
 * @author Alexandr Stegnin
 */
@Getter
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public enum CashType {

  UNDEFINED(0, "Не определено"),
  NEW(1, "Новая сумма"),
  OLD(2, "Старая сумма"),
  INVESTOR_CASH(3, "Деньги инвесторов"),
  SALE_CASH(4, "Деньги с продажи"),
  RENT_CASH(5, "Деньги с аренды"),
  CASH_1C(6, "Проводка из 1С"),
  INVESTMENT_BODY(7, "Тело инвестиций"),
  CASH_1C_COMMISSION(8, "Проводка из 1С. Комиссия"),
  CASH_1C_CASHING(9, "Проводка из 1С. Вывод"),
  RESALE_SHARE(10, "Перепродажа доли"),
  RE_BUY_SHARE(11, "Перепокупка доли"),
  CORRECTION(12, "Корректировка");

  int id;
  String title;

  public static CashType fromTitle(String title) {
    return Stream.of(values())
        .filter(type -> type.getTitle().equalsIgnoreCase(title))
        .findFirst()
        .orElse(UNDEFINED);
  }

  public static CashType fromId(Long id) {
    return Stream.of(values())
        .filter(type -> type.getId() == id)
        .findFirst()
        .orElse(UNDEFINED);
  }

}
