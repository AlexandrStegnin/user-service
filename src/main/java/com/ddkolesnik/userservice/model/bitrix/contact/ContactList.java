package com.ddkolesnik.userservice.model.bitrix.contact;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactList {

  List<BitrixContact> result = new ArrayList<>();
  String error;
  String total;
  String next;

}
