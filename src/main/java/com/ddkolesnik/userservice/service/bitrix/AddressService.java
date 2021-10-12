package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.mapper.AddressMapper;
import com.ddkolesnik.userservice.model.bitrix.address.Address;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AddressService extends BitrixService {

  RequisiteService requisiteService;
  AddressMapper addressMapper;

  public AddressService(BitrixProperty bitrixProperty, RestTemplate restTemplate,
                        RequisiteService requisiteService, AddressMapper addressMapper) {
    super(bitrixProperty, restTemplate);
    this.requisiteService = requisiteService;
    this.addressMapper = addressMapper;
  }

  public void createAddress(UserDTO dto) {
    var requisite = requisiteService.findRequisite(dto);
    var addressCreate = Address.builder()
        .fields(addressMapper.convertFields(dto, requisite.getId()))
        .build();
    var create = restTemplate.exchange(bitrixProperty.getAddressAdd(),
        HttpMethod.POST, new HttpEntity<>(addressCreate), Object.class);
    var created = create.getBody();
    log.info("Результат создания адреса {}", created);
  }

  public void updateAddress(UserDTO dto) {
    var requisite = requisiteService.findRequisite(dto);
    var addressUpdate = Address.builder()
        .fields(addressMapper.convertFields(dto, requisite.getId()))
        .build();
    var update = restTemplate.exchange(bitrixProperty.getAddressUpdate(),
        HttpMethod.POST, new HttpEntity<>(addressUpdate), Object.class);
    var updated = update.getBody();
    log.info("Результат обновления адреса {}", updated);
  }

}
