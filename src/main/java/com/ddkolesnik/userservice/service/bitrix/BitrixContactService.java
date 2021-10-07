package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.exception.BitrixException;
import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.mapper.ContactMapper;
import com.ddkolesnik.userservice.model.bitrix.bp.BusinessProcess;
import com.ddkolesnik.userservice.model.bitrix.bp.BusinessProcessTemplate;
import com.ddkolesnik.userservice.model.bitrix.contact.*;
import com.ddkolesnik.userservice.model.bitrix.enums.ValueType;
import com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.ddkolesnik.userservice.model.dto.ChangePhoneDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.response.ApiResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PropertySource(value = "classpath:application.properties")
public class BitrixContactService {

  BitrixProperty bitrixProperty;
  RestTemplate restTemplate;
  ObjectMapper objectMapper;
  ContactMapper contactMapper;
  DuplicateService duplicateService;

  public ApiResponse createOrUpdateContact(UserDTO dto) {
    var duplicate = duplicateService.findDuplicates(dto);
    if (Objects.isNull(duplicate)) {
      throw BitrixException.builder()
          .message("Поиск дубликатов по номеру телефона вернул неверный результат")
          .status(HttpStatus.BAD_REQUEST)
          .build();
    }
    if (responseEmpty(duplicate.getResult())) {
      Integer contactId = createContact(dto);
      if (Objects.isNull(contactId)) {
        throw BitrixException.builder()
            .message("Пользователь не создан")
            .status(HttpStatus.BAD_REQUEST)
            .build();
      }
      dto.setBitrixId(contactId);
      return ApiResponse.builder()
          .status(HttpStatus.CREATED)
          .message("Пользователь успешно создан в Б24")
          .build();
    } else {
      updateContact(dto);
      return ApiResponse.builder()
          .status(HttpStatus.OK)
          .message("Контакт Б24 успешно обновлён")
          .build();
    }
  }

  public Contact findFirstContact(UserDTO userDTO) {
    var contact = findFirstContact(userDTO.getPhone());
    if (Objects.nonNull(contact)) {
      userDTO.setBitrixId(contact.getId());
      userDTO.setId(contact.getId());
    }
    return contact;
  }

  public Contact findFirstContact(String phone) {
    Map<String, String[]> filter = new HashMap<>();
    filter.put(BitrixFields.CONTACT_PHONE, Collections.singleton(phone).toArray(new String[0]));
    var contactListFilter = new ContactListFilter(filter);
    var contactList = restTemplate.exchange(bitrixProperty.getContactList(),
        HttpMethod.POST, new HttpEntity<>(contactListFilter), ContactList.class);
    var contacts = contactList.getBody();
    log.info("Результат поиска списка контактов по телефону {}", contacts);
    if (Objects.nonNull(contacts)) {
      return contacts.getResult()
          .stream().min(Comparator.comparing(Contact::getId))
          .orElse(null);
    }
    return null;
  }

  public void updateContact(UserDTO userDTO) {
    var contactUpdate = contactMapper.convertDtoToContactUpdate(userDTO);
    var update = restTemplate.exchange(bitrixProperty.getContactUpdate(),
        HttpMethod.POST, new HttpEntity<>(contactUpdate), Object.class);
    var updated = update.getBody();
    log.info("Результат обновления контакта {}", updated);
  }

  public void updateContactPhone(Contact contact, ChangePhoneDTO changePhoneDTO, UserDTO userDTO) {
    var fields = new HashMap<String, Object>();
    var phones = new ArrayList<Phone>();
    var oldPhone = contact.getPhones().stream()
        .findAny()
        .orElseThrow(() -> BitrixException.builder()
            .message("Не удалось получить телефон из контакта")
            .status(HttpStatus.BAD_REQUEST)
            .build());
    oldPhone.setValue("");
    phones.add(oldPhone);
    var newPhone = Phone.builder()
        .value(changePhoneDTO.getNewPhone())
        .valueType(ValueType.WORK.name())
        .build();
    phones.add(newPhone);

    fields.put(BitrixFields.CONTACT_PHONE, phones);

    var contactUpdate = ContactUpdate.builder()
        .id(userDTO.getBitrixId())
        .fields(fields)
        .build();
    var update = restTemplate.exchange(bitrixProperty.getContactUpdate(),
        HttpMethod.POST, new HttpEntity<>(contactUpdate), Object.class);
    var updated = update.getBody();
    log.info("Результат обновления телефона контакта {}", updated);
  }

  public Contact getById(String id) {
    var contactGet = ContactGet.builder()
        .id(id)
        .build();
    Contact contact = null;
    var contactResponseEntity = restTemplate.exchange(bitrixProperty.getContactGet(),
        HttpMethod.POST, new HttpEntity<>(contactGet), Object.class);
    var contactObject = contactResponseEntity.getBody();
    if (contactObject instanceof LinkedHashMap) {
      var result = ((LinkedHashMap<?, ?>) contactObject).get("result");
      contact = objectMapper.convertValue(result, Contact.class);
    }
    log.info("Результат получения контакта {}", contact);
    return contact;
  }

  public Integer createContact(UserDTO userDTO) {
    var contactCreate = contactMapper.convertDtoToContactCreate(userDTO);
    var create = restTemplate.exchange(bitrixProperty.getContactAdd(),
        HttpMethod.POST, new HttpEntity<>(contactCreate), Object.class);
    var created = create.getBody();
    log.info("Результат создания контакта {}", created);
    return getContactId(created);
  }

  public Object deleteContact(UserDTO userDTO) {
    ContactDelete contactDelete = new ContactDelete(userDTO.getBitrixId());
    ResponseEntity<Object> delete = restTemplate.exchange(bitrixProperty.getContactDelete(),
        HttpMethod.POST, new HttpEntity<>(contactDelete), Object.class);
    Object deleted = delete.getBody();
    log.info("Результат удаления контакта {}", delete);
    return deleted;
  }

  public void sendConfirmMessage(UserDTO dto) {
    var businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.CONFIRM_PHONE.getId())
        .build();
    businessProcess.addDocumentId(BitrixFields.CONTACT_PREFIX.concat(dto.getBitrixId().toString()));
    var start = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    var started = start.getBody();
    log.info("Результат отправки смс для подтверждения {}", started);
  }

  public void sendRestoreMessage(UserDTO dto) {
    var businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.RESTORE_PASSWORD.getId())
        .build();
    businessProcess.addDocumentId(BitrixFields.CONTACT_PREFIX.concat(dto.getBitrixId().toString()));
    var restore = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    var restored = restore.getBody();
    log.info("Результат отправки смс для восстановления пароля {}", restored);
  }

  public void sendConfirmOldPhoneMessage(UserDTO dto) {
    var businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.CONFIRM_OLD_PHONE.getId())
        .build();
    businessProcess.addDocumentId(BitrixFields.CONTACT_PREFIX.concat(dto.getBitrixId().toString()));
    var confirmChangePhone = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    var confirmedChangePhone = confirmChangePhone.getBody();
    log.info("Результат отправки смс для подтверждения старого телефона {}", confirmedChangePhone);
  }

  public void sendConfirmNewPhoneMessage(UserDTO dto) {
    var businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.CONFIRM_NEW_PHONE.getId())
        .build();
    businessProcess.addDocumentId(BitrixFields.CONTACT_PREFIX.concat(dto.getBitrixId().toString()));
    var confirmChangePhone = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    var confirmedChangePhone = confirmChangePhone.getBody();
    log.info("Результат отправки смс для подтверждения нового телефона {}", confirmedChangePhone);
  }

  private Integer getContactId(Object contact) {
    if (contact instanceof LinkedHashMap) {
      return (Integer) (((LinkedHashMap<?, ?>) contact).get("result"));
    }
    return null;
  }

  private boolean responseEmpty(Object result) {
    if (Objects.isNull(result)) {
      return false;
    }
    boolean isEmpty = false;
    if (result instanceof ArrayList<?>) {
      isEmpty = ((ArrayList<?>) result).isEmpty();
    } else if (result instanceof LinkedHashMap<?, ?>) {
      isEmpty = ((LinkedHashMap<?, ?>) result).isEmpty();
    }
    return isEmpty;
  }

}
