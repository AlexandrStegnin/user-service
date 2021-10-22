package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.configuration.MapStructConfig;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.enums.ValueType;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.utils.ScanConverter;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Component
@Mapper(config = MapStructConfig.class)
public abstract class ContactMapper {

  @Mapping(target = "fields", expression = "java(extractFields(dto, true))")
  public abstract Contact toContactCreate(UserDTO dto);

  @Mapping(target = "fields", expression = "java(extractFields(dto, false))")
  public abstract Contact toContactUpdate(UserDTO dto);
  
  protected Map<String, Object> extractFields(UserDTO dto, boolean isCreate) {
    var fields = new LinkedHashMap<String, Object>();
    fields.put(CONTACT_NAME, dto.getName());
    fields.put(CONTACT_SECOND_NAME, dto.getSecondName());
    fields.put(CONTACT_LAST_NAME, dto.getLastName());
    fields.put(CONTACT_EMAIL, Collections.singletonList(convertEmail(dto.getEmail())));
    fields.put(CONTACT_PHONE, Collections.singletonList(convertPhone(dto.getPhone())));
    fields.put("UF_CRM_1625221385", "1");
    if (Objects.nonNull(dto.getBirthdate())) {
      fields.put(CONTACT_BIRTHDATE, dto.getBirthdate());
    }
    if (Objects.nonNull(dto.getGender())) {
      fields.put(CONTACT_GENDER, dto.getGender().getId());
    }
    if (ScanConverter.isScansAvailable(dto.getPassport())) {
      fields.put(CONTACT_SCANS, ScanConverter.convertScans(dto.getPassport()));
    }
    if (ScanConverter.isScansAvailable(dto.getSnils())) {
      fields.put(SNILS_SCANS, ScanConverter.convertScans(dto.getSnils()));
    }
    if (ScanConverter.isScansAvailable(dto.getBankRequisites())) {
      fields.put(BANK_REQUISITES_SCANS, ScanConverter.convertScans(dto.getBankRequisites()));
    }
    if (Objects.nonNull(dto.getPlaceOfBirth())) {
      fields.put(CONTACT_PLACE_OF_BIRTH, dto.getPlaceOfBirth());
    }
    if (isCreate) {
      fields.put(CONTACT_IS_INDIVIDUAL, dto.isIndividual() ? "Y" : "N");
      fields.put(CONTACT_IS_SELF_EMPLOYED, dto.isSelfEmployed() ? "Y" : "N");
    }
    fields.put("UF_CRM_1628253424", "1");
    if (Objects.nonNull(dto.getTaxStatus())) {
      fields.put(CONTACT_TAX_STATUS, dto.getTaxStatus().getCode());
    }
    if (Objects.nonNull(dto.getAddress())) {
      fields.put(CONTACT_ADDRESS, dto.getAddress());
    }
    return fields;
  }

  private Email convertEmail(String email) {
    return Email.builder()
        .value(email)
        .valueType(ValueType.WORK.name())
        .typeId(CONTACT_EMAIL)
        .build();
  }

  private Phone convertPhone(String phone) {
    return Phone.builder()
        .value(phone)
        .valueType(ValueType.WORK.name())
        .build();
  }
  
}
