package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.requisite.bank.BankRequisite;
import com.ddkolesnik.userservice.model.bitrix.requisite.bank.BankRequisiteFilter;
import com.ddkolesnik.userservice.model.bitrix.requisite.bank.BankRequisiteResult;
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

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BankRequisiteService extends BitrixService {

  RequisiteService requisiteService;

  public BankRequisiteService(BitrixProperty bitrixProperty, RestTemplate restTemplate,
                              RequisiteService requisiteService) {
    super(bitrixProperty, restTemplate);
    this.requisiteService = requisiteService;
  }

  public BankRequisite findRequisites(String entityId) {
    var requisite = requisiteService.findRequisite(entityId);
    if (Objects.isNull(requisite)) {
      log.error("Реквизит не найден");
      return null;
    }
    var filter = new LinkedHashMap<String, String>();
    filter.put(ENTITY_ID, requisite.getId());

    var bankRequisiteFilter = new BankRequisiteFilter(filter);
    var bankRequisite = restTemplate.exchange(bitrixProperty.getRequisiteBankDetailList(),
        HttpMethod.POST, new HttpEntity<>(bankRequisiteFilter), BankRequisiteResult.class);
    var response = bankRequisite.getBody();
    log.info("Результат поиска банковских реквизитов {}", response);
    if (Objects.isNull(response) || response.getTotal() == 0) {
      return null;
    }
    var requisites = response.getResult();
    if (requisites.isEmpty()) {
      return null;
    }
    return requisites.get(0);
  }

}
