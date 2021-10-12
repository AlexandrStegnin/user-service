package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.address.*;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AddressService extends BitrixService {

  RequisiteService requisiteService;

  public AddressService(BitrixProperty bitrixProperty, RestTemplate restTemplate,
                        RequisiteService requisiteService) {
    super(bitrixProperty, restTemplate);
    this.requisiteService = requisiteService;
  }

  public Address findAddress(Requisite requisite) {
    var filter = new LinkedHashMap<String, String>();
    filter.put(ENTITY_TYPE_ID, "8");
    filter.put(TYPE_ID, "1");
    filter.put(ENTITY_ID, requisite.getId());

    var addressFilter = new AddressFilter(filter);
    var address = restTemplate.exchange(bitrixProperty.getAddressList(),
        HttpMethod.POST, new HttpEntity<>(addressFilter), AddressResult.class);
    log.info("Результат поиска адресов {}", address);
    var response = address.getBody();
    if (Objects.isNull(response) || response.getTotal() == 0) {
      return null;
    }
    var addresses = response.getResult();
    if (addresses.isEmpty()) {
      return null;
    }
    return addresses.get(0);
  }

  public void createAddress(UserDTO dto) {
    var addressCreate = AddressCreate.builder()
        .fields(prepareAddressFields(dto))
        .build();
    var create = restTemplate.exchange(bitrixProperty.getAddressAdd(),
        HttpMethod.POST, new HttpEntity<>(addressCreate), Object.class);
    var created = create.getBody();
    log.info("Результат создания адреса {}", created);
  }

  public void updateAddress(UserDTO dto) {
    var addressUpdate = AddressUpdate.builder()
        .fields(prepareAddressFields(dto))
        .build();
    var update = restTemplate.exchange(bitrixProperty.getAddressUpdate(),
        HttpMethod.POST, new HttpEntity<>(addressUpdate), Object.class);
    var updated = update.getBody();
    log.info("Результат обновления адреса {}", updated);
  }

  private Map<String, Object> prepareAddressFields(UserDTO userDTO) {
    var requisite = requisiteService.findRequisite(userDTO);
    var fields = new LinkedHashMap<String, Object>();
    if (Objects.isNull(requisite)) {
      log.error("Реквизит не найден: {}", userDTO);
    } else {
      fields.put(ENTITY_ID, requisite.getId());
    }
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
