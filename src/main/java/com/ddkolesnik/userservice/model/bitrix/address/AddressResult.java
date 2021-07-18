package com.ddkolesnik.userservice.model.bitrix.address;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 18.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AddressResult {

  ArrayList<Address> result;
  Integer total;
  LinkedHashMap<?, ?> time;

}
