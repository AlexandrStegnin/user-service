package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.requisite.*;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.utils.DateUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;
import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.IDENT_DOC_NAME;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PropertySource(value = "classpath:application.properties")
public class RequisiteService {

  BitrixProperty bitrixProperty;
  RestTemplate restTemplate;

  public Requisite findRequisite(String entityId) {
    var filter = new LinkedHashMap<String, String>();
    filter.put(ENTITY_TYPE_ID, "3");
    filter.put(ENTITY_ID, entityId);

    var requisiteFilter = new RequisiteFilter(filter);
    var requisite = restTemplate.exchange(bitrixProperty.getRequisiteList(),
        HttpMethod.POST, new HttpEntity<>(requisiteFilter), RequisiteResult.class);
    log.info("Результат поиска реквизита {}", requisite);
    var response = requisite.getBody();
    if (Objects.isNull(response) || response.getTotal() == 0) {
      return null;
    }
    var requisites = response.getResult();
    if (requisites.isEmpty()) {
      return null;
    }
    return requisites.get(0);
  }

  public Requisite findRequisite(UserDTO dto) {
    return findRequisite(dto.getId().toString());
  }

  public void createRequisite(UserDTO dto) {
    var requisiteCreate = RequisiteCreate.builder()
        .fields(prepareRequisiteFields(dto))
        .build();
    var create = restTemplate.exchange(bitrixProperty.getRequisiteAdd(),
        HttpMethod.POST, new HttpEntity<>(requisiteCreate), Object.class);
    var created = create.getBody();
    log.info("Результат создания реквизита {}", created);
  }

  public void updateRequisite(Requisite requisite, UserDTO dto) {
    var requisiteUpdate = RequisiteUpdate.builder()
        .id(Integer.parseInt(requisite.getId()))
        .fields(prepareRequisiteFields(dto))
        .build();
    var update = restTemplate.exchange(bitrixProperty.getRequisiteUpdate(),
        HttpMethod.POST, new HttpEntity<>(requisiteUpdate), Object.class);
    var updated = update.getBody();
    log.info("Результат обновления реквизита {}", updated);
  }

  private Map<String, Object> prepareRequisiteFields(UserDTO userDTO) {
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
