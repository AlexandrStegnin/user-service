package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.mapper.RequisiteMapper;
import com.ddkolesnik.userservice.model.bitrix.requisite.*;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Objects;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.ENTITY_ID;
import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.ENTITY_TYPE_ID;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RequisiteService extends BitrixService {

  RequisiteMapper requisiteMapper;

  public RequisiteService(BitrixProperty bitrixProperty, RestTemplate restTemplate,
                          RequisiteMapper requisiteMapper) {
    super(bitrixProperty, restTemplate);
    this.requisiteMapper = requisiteMapper;
  }

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
        .fields(requisiteMapper.convertFields(dto))
        .build();
    var create = restTemplate.exchange(bitrixProperty.getRequisiteAdd(),
        HttpMethod.POST, new HttpEntity<>(requisiteCreate), Object.class);
    var created = create.getBody();
    log.info("Результат создания реквизита {}", created);
  }

  public void updateRequisite(Requisite requisite, UserDTO dto) {
    var requisiteUpdate = RequisiteUpdate.builder()
        .id(Integer.parseInt(requisite.getId()))
        .fields(requisiteMapper.convertFields(dto))
        .build();
    var update = restTemplate.exchange(bitrixProperty.getRequisiteUpdate(),
        HttpMethod.POST, new HttpEntity<>(requisiteUpdate), Object.class);
    var updated = update.getBody();
    log.info("Результат обновления реквизита {}", updated);
  }

}
