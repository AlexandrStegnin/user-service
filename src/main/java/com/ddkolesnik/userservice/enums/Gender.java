package com.ddkolesnik.userservice.enums;

import java.util.stream.Stream;
import lombok.Getter;

/**
 * @author Aleksandr Stegnin on 18.07.2021
 */
@Getter
public enum Gender {

  MALE(574, "МУЖСКОЙ"), FEMALE(576, "ЖЕНСКИЙ");

  private final int id;

  private final String title;

  Gender(int id, String title) {
    this.id = id;
    this.title = title;
  }

  public static Gender fromId(int id) {
    return Stream.of(values())
        .filter(gender -> gender.getId() == id)
        .findAny()
        .orElseThrow();
  }

  public static Gender fromTitle(String title) {
    return Stream.of(values())
        .filter(gender -> gender.getTitle().equalsIgnoreCase(title))
        .findAny()
        .orElseThrow();
  }

}
