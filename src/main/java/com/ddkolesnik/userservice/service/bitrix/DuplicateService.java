package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateFilter;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateResult;
import com.ddkolesnik.userservice.model.dto.UserDTO;
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
public class DuplicateService extends BitrixService {

  public DuplicateService(BitrixProperty bitrixProperty, RestTemplate restTemplate) {
    super(bitrixProperty, restTemplate);
  }

  public DuplicateResult findDuplicates(UserDTO userDTO) {
    var duplicateFilter = new DuplicateFilter(userDTO.getPhone());
    var bitrixResult = restTemplate.exchange(bitrixProperty.getDuplicateFindByComm(),
        HttpMethod.POST, new HttpEntity<>(duplicateFilter), DuplicateResult.class);
    var duplicate = bitrixResult.getBody();
    log.info("Результат поиска дубликатов {}", duplicate);
    return duplicate;
  }

}
