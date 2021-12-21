package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.exception.BitrixException;
import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.address.Address;
import com.ddkolesnik.userservice.model.bitrix.address.AddressFilter;
import com.ddkolesnik.userservice.model.bitrix.address.AddressResult;
import com.ddkolesnik.userservice.model.bitrix.address.BitrixAddress;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.ParameterizedTypeReference;
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

  public BitrixAddress findAddress(Requisite requisite) {
    try {
      var filter = new LinkedHashMap<String, String>();
      filter.put(ENTITY_TYPE_ID, "8");
      filter.put(TYPE_ID, REGISTRATION_ADDRESS);
      filter.put(ENTITY_ID, requisite.getId());
      var addressFilter = new AddressFilter(filter);
      var address = restTemplate.exchange(bitrixProperty.getAddressList(),
          HttpMethod.POST, new HttpEntity<>(addressFilter), AddressResult.class);
      var addressResult = address.getBody();
      if (Objects.isNull(addressResult) || addressResult.getTotal() == 0) {
        return null;
      }
      var addresses = addressResult.getResult();
      if (addresses.isEmpty()) {
        return null;
      }
      return addresses.get(0);
    } catch (Exception e) {
      log.error("При получении адреса произошла ошибка {}", e.toString());
      throw BitrixException.build500Exception("При получении адреса произошла ошибка");
    }
  }

  public void createAddress(UserDTO dto, int presetId) {
    try {
      var address = Address.builder()
          .fields(prepareAddressFields(dto, presetId))
          .build();
      ParameterizedTypeReference<LinkedHashMap<String, Object>> responseType =
          new ParameterizedTypeReference<>() {
          };
      var create = restTemplate.exchange(bitrixProperty.getAddressAdd(),
          HttpMethod.POST, new HttpEntity<>(address), responseType);
      var created = create.getBody();
      if (Objects.isNull(created)) {
        throw BitrixException.build400Exception("При создании адреса произошла ошибка");
      }
    } catch (Exception e) {
      log.error("При создании адреса произошла ошибка {}", e.toString());
      throw BitrixException.build500Exception("При создании адреса произошла ошибка");
    }
  }

  public void updateAddress(UserDTO dto, int presetId) {
    var addressUpdate = Address.builder()
        .fields(prepareAddressFields(dto, presetId))
        .build();
    var update = restTemplate.exchange(bitrixProperty.getAddressUpdate(),
        HttpMethod.POST, new HttpEntity<>(addressUpdate), Object.class);
    var updated = update.getBody();
    log.info("Результат обновления адреса {}", updated);
  }

  private Map<String, Object> prepareAddressFields(UserDTO userDTO, Integer presetId) {
    var requisite = requisiteService.findRequisite(userDTO, presetId);
    var fields = new LinkedHashMap<String, Object>();
    if (Objects.isNull(requisite)) {
      log.error("Реквизит не найден: {}", userDTO);
    } else {
      fields.put(ENTITY_ID, requisite.getId());
    }
    fields.put(TYPE_ID, 4);
    fields.put(ENTITY_TYPE_ID, 8);
    prepareAddress(fields, userDTO);
    return fields;
  }

  private void prepareAddress(Map<String, Object> fields, UserDTO userDTO) {
    var address = userDTO.getAddress();
    if (Objects.nonNull(address)) {
      fields.put(ADDRESS_2, address);
    }
  }

}
