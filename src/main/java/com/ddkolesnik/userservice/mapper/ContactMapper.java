package com.ddkolesnik.userservice.mapper;

import com.ddkolesnik.userservice.configuration.MapStructConfig;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactCreate;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactUpdate;
import com.ddkolesnik.userservice.model.bitrix.enums.ValueType;
import com.ddkolesnik.userservice.model.bitrix.file.FileData;
import com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.ddkolesnik.userservice.model.dto.PassportDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.springframework.stereotype.Component;
import org.springframework.util.Base64Utils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.*;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Component
@Mapper(config = MapStructConfig.class)
public abstract class ContactMapper {

  @Mapping(target = "fields", expression = "java(extractFields(dto, true))")
  public abstract ContactCreate convertDtoToContactCreate(UserDTO dto);

  @Mapping(target = "fields", expression = "java(extractFields(dto, false))")
  public abstract ContactUpdate convertDtoToContactUpdate(UserDTO dto);
  
  protected Map<String, Object> extractFields(UserDTO dto, boolean isCreate) {
    var fields = new LinkedHashMap<String, Object>();
    fields.put(CONTACT_NAME, dto.getName());
    fields.put(CONTACT_SECOND_NAME, dto.getSecondName());
    fields.put(CONTACT_LAST_NAME, dto.getLastName());
    fields.put(CONTACT_EMAIL, Collections.singletonList(convertEmail(dto.getEmail())));
    fields.put(BitrixFields.CONTACT_PHONE, Collections.singletonList(convertPhone(dto.getPhone())));
    fields.put("UF_CRM_1625221385", "1");
    if (Objects.nonNull(dto.getBirthdate())) {
      fields.put(CONTACT_BIRTHDATE, dto.getBirthdate());
    }
    if (Objects.nonNull(dto.getGender())) {
      fields.put(CONTACT_GENDER, dto.getGender().getId());
    }
    if (isScansAvailable(dto.getPassport())) {
      fields.put(CONTACT_SCANS, convertScans(dto));
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

  private boolean isScansAvailable(PassportDTO dto) {
    return Objects.nonNull(dto)
        && Objects.nonNull(dto.getScans())
        && (dto.getScans().length > 0);
  }

  private List<FileData> convertScans(UserDTO dto) {
    var files = dto.getPassport().getScans();
    var fileDataList = new ArrayList<FileData>();
    for (MultipartFile file : files) {
      try {
        byte[] content = file.getBytes();
        var imageString = Base64Utils.encodeToString(content);
        var fileData = new FileData();
        fileData.setFileData(new String[]{file.getOriginalFilename(), imageString});
        fileDataList.add(fileData);
      } catch (IOException e) {
        log.error("Произошла ошибка {}", e.getMessage());
      }
    }
    return fileDataList;
  }
  
}
