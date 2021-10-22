package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RequisiteMapper {

  public Map<String, Object> convertFields(UserDTO dto) {
    var fields = new LinkedHashMap<String, Object>();
    fields.put(CONTACT_INN, dto.getInn());
    var snils = dto.getSnils();
    if (Objects.nonNull(snils)) {
      fields.put(CONTACT_SNILS, snils.getNumber());
    }
    convertPassport(fields, dto);
    fields.put(ENTITY_TYPE_ID, "3");
    fields.put(PRESET_ID, "5");
    fields.put(ENTITY_ID, dto.getId().toString());
    fields.put(REQUISITE_NAME, "Реквизит");
    return fields;
  }

  private void convertPassport(Map<String, Object> fields, UserDTO userDTO) {
    var passport = userDTO.getPassport();
    if (Objects.nonNull(passport)) {
      fields.put(PASSPORT_SERIAL, passport.getSerial());
      fields.put(PASSPORT_NUMBER, passport.getNumber());
      fields.put(PASSPORT_DEP_CODE, passport.getDepartmentCode());
      fields.put(PASSPORT_ISSUED_BY, passport.getIssuedBy());
      fields.put(PASSPORT_ISSUED_AT, passport.getIssuedAt());
      fields.put(IDENT_DOC_NAME, "Паспорт");
    }
  }

}
