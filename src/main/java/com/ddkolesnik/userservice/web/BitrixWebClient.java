package com.ddkolesnik.userservice.web;

import com.ddkolesnik.userservice.configuration.exception.BitrixException;
import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.contact.BitrixContact;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactList;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactListFilter;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateFilter;
import com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.*;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@Component
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
@PropertySource(value = "classpath:application.properties")
public class BitrixWebClient {

  private static final String ERROR_CREATE_CONTACT_IN_B_24 = "Не удалось создать контакт в Б24";
  private static final String BITRIX_SUCCESS_RESPONSE_KEY = "result";

  BitrixProperty bitrixProperty;
  RestTemplate restTemplate;

  public BitrixContact getContact(String phone) {
    BitrixContact contact = null;
    try {
      Map<String, String[]> filter = new HashMap<>();
      filter.put(BitrixFields.CONTACT_PHONE, Collections.singleton(phone).toArray(new String[0]));
      var contactListFilter = new ContactListFilter(filter);
      var contactList = restTemplate.exchange(bitrixProperty.getContactList(),
          HttpMethod.POST, new HttpEntity<>(contactListFilter), ContactList.class);
      var contacts = contactList.getBody();
      if (Objects.nonNull(contacts)) {
          contact = contacts.getResult()
              .stream().min(Comparator.comparing(BitrixContact::getId))
              .orElse(null);
      }
    } catch (Exception e) {
      log.error("При получении контакта по телефону {} произошла ошибка {}", phone, e.toString());
      throw BitrixException.build500Exception(
          String.format("При получении контакта по телефону %s произошла ошибка %s", phone, e)
      );
    }
    if (Objects.isNull(contact)) {
      throw BitrixException.build404Exception("Контакт не найден по телефону " + phone);
    }
    return contact;
  }

  public boolean updateContact(Contact contact) {
    try {
      restTemplate.exchange(bitrixProperty.getContactUpdate(),
          HttpMethod.POST, new HttpEntity<>(contact), LinkedHashMap.class);
      return true;
    } catch (Exception e) {
      log.error("При обновлении контакта произошла ошибка {}", e.toString());
      return false;
    }
  }

  public Integer createContact(Contact contact) {
    try {
      ParameterizedTypeReference<LinkedHashMap<String, Object>> responseType =
          new ParameterizedTypeReference<>() {
          };
      var create = restTemplate.exchange(bitrixProperty.getContactAdd(),
          HttpMethod.POST, new HttpEntity<>(contact), responseType);
      var created = create.getBody();
      if (Objects.isNull(created)) {
        throw BitrixException.build400Exception(ERROR_CREATE_CONTACT_IN_B_24);
      }
      Integer contactId = (Integer) created.getOrDefault(BITRIX_SUCCESS_RESPONSE_KEY, null);
      if (Objects.isNull(contactId)) {
        throw BitrixException.build400Exception(ERROR_CREATE_CONTACT_IN_B_24);
      }
      return contactId;
    } catch (Exception e) {
      log.error("При создании контакта произошла ошибка {}", e.toString());
      throw BitrixException.build400Exception(ERROR_CREATE_CONTACT_IN_B_24);
    }
  }

  public boolean isContactExists(String phone) {
    try {
      ParameterizedTypeReference<LinkedHashMap<String, Object>> responseType =
          new ParameterizedTypeReference<>() {
          };
      var duplicateFilter = new DuplicateFilter(phone);
      var bitrixResult = restTemplate.exchange(bitrixProperty.getDuplicateFindByComm(),
          HttpMethod.POST, new HttpEntity<>(duplicateFilter), responseType);
      var duplicate = bitrixResult.getBody();
      if (Objects.isNull(duplicate)) {
        return false;
      }
      var contactIdList = duplicate.get(BITRIX_SUCCESS_RESPONSE_KEY);
      if (contactIdList instanceof ArrayList) {
        ArrayList<?> contactList = (ArrayList<?>) contactIdList;
        return !contactList.isEmpty();
      }
      return true;
    } catch (Exception e) {
      log.error("При поиске дубликатов по номеру телефона {} произошла ошибка {}", phone, e.toString());
      return true;
    }
  }

}
