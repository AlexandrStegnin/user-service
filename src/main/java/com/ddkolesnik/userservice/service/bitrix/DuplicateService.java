package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateFilter;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateResult;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PropertySource(value = "classpath:application.properties")
public class DuplicateService {

  BitrixProperty bitrixProperty;
  RestTemplate restTemplate;

  public DuplicateResult findDuplicates(UserDTO userDTO) {
    var duplicateFilter = new DuplicateFilter(userDTO.getPhone());
    var bitrixResult = restTemplate.exchange(bitrixProperty.getDuplicateFindByComm(),
        HttpMethod.POST, new HttpEntity<>(duplicateFilter), DuplicateResult.class);
    var duplicate = bitrixResult.getBody();
    log.info("Результат поиска дубликатов {}", duplicate);
    return duplicate;
  }

}
