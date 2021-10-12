package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.utils.DateUtils;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;
import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.IDENT_DOC_NAME;

/**
 * @author Alexandr Stegnin
 */
@Component
public class RequisiteMapper {

  public Map<String, Object> convertFields(UserDTO userDTO) {
    var fields = new LinkedHashMap<String, Object>();
    fields.put(CONTACT_INN, userDTO.getInn());
    fields.put(CONTACT_SNILS, userDTO.getSnils());
    fields.put(PASSPORT_SERIAL, userDTO.getPassport().getSerial());
    fields.put(PASSPORT_NUMBER, userDTO.getPassport().getNumber());
    fields.put(PASSPORT_DEP_CODE, userDTO.getPassport().getDepartmentCode());
    fields.put(PASSPORT_ISSUED_BY, userDTO.getPassport().getIssuedBy());
    fields.put(ENTITY_TYPE_ID, "3");
    fields.put(PRESET_ID, "5");
    fields.put(ENTITY_ID, userDTO.getId().toString());
    fields.put(REQUISITE_NAME, "Реквизит");
    fields.put(PASSPORT_ISSUED_AT, DateUtils.convertToDDMMYYYY(userDTO.getPassport().getIssuedAt()));
    fields.put(IDENT_DOC_NAME, "Паспорт");
    return fields;
  }

}
