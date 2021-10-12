package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;
import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.ADDRESS_2;

/**
 * @author Alexandr Stegnin
 */
@Component
public class AddressMapper {

  public Map<String, Object> convertFields(UserDTO userDTO, String requisiteId) {
    var fields = new LinkedHashMap<String, Object>();
    fields.put(ENTITY_ID, requisiteId);
    fields.put(TYPE_ID, 1);
    fields.put(ENTITY_TYPE_ID, 8);
    fields.put(CITY, userDTO.getAddress().getCity());
    fields.put(ADDRESS_1, getAddress1(userDTO));
    fields.put(ADDRESS_2, getAddress2(userDTO));
    return fields;
  }

  private String getAddress1(UserDTO dto) {
    var address = "";
    if (Objects.nonNull(dto.getAddress().getStreetAndHouse())) {
      address += dto.getAddress().getStreetAndHouse();
    }
    return address;
  }

  private String getAddress2(UserDTO dto) {
    if (Objects.nonNull(dto.getAddress().getOffice())) {
      return dto.getAddress().getOffice();
    }
    return "";
  }


}
