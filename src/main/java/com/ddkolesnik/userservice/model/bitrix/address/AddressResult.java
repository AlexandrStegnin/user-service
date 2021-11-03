package com.ddkolesnik.userservice.model.bitrix.address;

import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.ArrayList;
import java.util.LinkedHashMap;

/**
 * @author Aleksandr Stegnin on 18.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResult {

  ArrayList<BitrixAddress> result;
  Integer total;
  LinkedHashMap<?, ?> time;

}
