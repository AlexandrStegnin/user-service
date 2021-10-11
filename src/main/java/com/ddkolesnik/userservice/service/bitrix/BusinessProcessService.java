package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.bp.BusinessProcess;
import com.ddkolesnik.userservice.model.bitrix.bp.BusinessProcessTemplate;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.CONTACT_PREFIX;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Service
public class BusinessProcessService extends BitrixService {

  public BusinessProcessService(BitrixProperty bitrixProperty, RestTemplate restTemplate) {
    super(bitrixProperty, restTemplate);
  }

  public void sendConfirmMessage(UserDTO dto) {
    var businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.CONFIRM_PHONE.getId())
        .build();
    businessProcess.addDocumentId(CONTACT_PREFIX.concat(dto.getBitrixId().toString()));
    var start = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    var started = start.getBody();
    log.info("Результат отправки смс для подтверждения {}", started);
  }

  public void sendRestoreMessage(UserDTO dto) {
    var businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.RESTORE_PASSWORD.getId())
        .build();
    businessProcess.addDocumentId(CONTACT_PREFIX.concat(dto.getBitrixId().toString()));
    var restore = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    var restored = restore.getBody();
    log.info("Результат отправки смс для восстановления пароля {}", restored);
  }

  public void sendConfirmOldPhoneMessage(UserDTO dto) {
    var businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.CONFIRM_OLD_PHONE.getId())
        .build();
    businessProcess.addDocumentId(CONTACT_PREFIX.concat(dto.getBitrixId().toString()));
    var confirmChangePhone = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    var confirmedChangePhone = confirmChangePhone.getBody();
    log.info("Результат отправки смс для подтверждения старого телефона {}", confirmedChangePhone);
  }

  public void sendConfirmNewPhoneMessage(UserDTO dto) {
    var businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.CONFIRM_NEW_PHONE.getId())
        .build();
    businessProcess.addDocumentId(CONTACT_PREFIX.concat(dto.getBitrixId().toString()));
    var confirmChangePhone = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    var confirmedChangePhone = confirmChangePhone.getBody();
    log.info("Результат отправки смс для подтверждения нового телефона {}", confirmedChangePhone);
  }

}