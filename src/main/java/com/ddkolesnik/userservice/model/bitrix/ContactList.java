package com.ddkolesnik.userservice.model.bitrix;

import java.util.ArrayList;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ContactList {

  List<Contact> result = new ArrayList<>();
  String error;
  String total;
  String next;

}
