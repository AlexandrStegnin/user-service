package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.model.bitrix.Contact;
import com.ddkolesnik.userservice.model.bitrix.ContactList;
import com.ddkolesnik.userservice.model.bitrix.ContactListFilter;
import com.ddkolesnik.userservice.model.bitrix.CreateContact;
import com.ddkolesnik.userservice.model.bitrix.DeleteContact;
import com.ddkolesnik.userservice.model.bitrix.DuplicateFilter;
import com.ddkolesnik.userservice.model.bitrix.DuplicateResult;
import com.ddkolesnik.userservice.model.bitrix.Email;
import com.ddkolesnik.userservice.model.bitrix.Phone;
import com.ddkolesnik.userservice.model.bitrix.UpdateContact;
import com.ddkolesnik.userservice.model.bitrix.ValueType;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@PropertySource(value = "classpath:application.properties")
public class BitrixContactService {

  @Value("${bitrix.crm.duplicate.findbycomm}")
  String BITRIX_CRM_DUPLICATE_FIND_BY_COMM;

  @Value("${bitrix.crm.contact.list}")
  String BITRIX_CRM_CONTACT_LIST;

  @Value("${bitrix.crm.contact.update}")
  String BITRIX_CRM_CONTACT_UPDATE;

  @Value("${bitrix.crm.contact.add}")
  String BITRIX_CRM_CONTACT_ADD;

  @Value("${bitrix.crm.contact.delete}")
  String BITRIX_CRM_CONTACT_DELETE;

  final RestTemplate restTemplate;

  final HttpEntity<DuplicateFilter> httpEntity;

  final DuplicateFilter duplicateFilter;

  final ContactListFilter contactListFilter;

  final HttpEntity<ContactListFilter> contactListEntity;

  final HttpEntity<UpdateContact> updateContactHttpEntity;

  final UpdateContact updateContact;

  final CreateContact createContact;

  final HttpEntity<CreateContact> createContactHttpEntity;

  final DeleteContact deleteContact;

  final HttpEntity<DeleteContact> deleteContactHttpEntity;

  @Autowired
  public BitrixContactService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.duplicateFilter = new DuplicateFilter();
    this.contactListFilter = new ContactListFilter();
    this.updateContact = new UpdateContact();
    this.createContact = new CreateContact();
    this.deleteContact = new DeleteContact();
    this.httpEntity = new HttpEntity<>(duplicateFilter);
    this.contactListEntity = new HttpEntity<>(contactListFilter);
    this.updateContactHttpEntity = new HttpEntity<>(updateContact);
    this.createContactHttpEntity = new HttpEntity<>(createContact);
    this.deleteContactHttpEntity = new HttpEntity<>(deleteContact);
  }

  public DuplicateResult findDuplicates(UserDTO userDTO) {
    duplicateFilter.setValues(Collections.singletonList(userDTO.getPhone()));
    ResponseEntity<DuplicateResult> bitrixResult = restTemplate.exchange(BITRIX_CRM_DUPLICATE_FIND_BY_COMM,
        HttpMethod.POST, httpEntity, DuplicateResult.class);
    DuplicateResult duplicate = bitrixResult.getBody();
    log.info("Результат поиска дубликатов {}", duplicate);
    return duplicate;
  }

  public Contact findFirstContact(UserDTO userDTO) {
    Map<String, String[]> filter = new HashMap<>();
    filter.put("PHONE", Collections.singleton(userDTO.getPhone()).toArray(new String[0]));
    contactListFilter.setFilter(filter);
    ResponseEntity<ContactList> contactList = restTemplate.exchange(BITRIX_CRM_CONTACT_LIST,
        HttpMethod.POST, contactListEntity, ContactList.class);
    ContactList contacts = contactList.getBody();
    log.info("Результат поиска списка контактов по телефону {}", contacts);
    if (Objects.nonNull(contacts)) {
      return contacts.getResult()
          .stream().min(Comparator.comparing(Contact::getId))
          .orElse(null);
    }
    return null;
  }

  public Object updateContact(UserDTO userDTO) {
    UpdateContact contact = convertToUpdateContact(userDTO);
    this.updateContact.setId(contact.getId());
    this.updateContact.setFields(contact.getFields());
    ResponseEntity<Object> update = restTemplate.exchange(BITRIX_CRM_CONTACT_UPDATE,
        HttpMethod.POST, updateContactHttpEntity, Object.class);
    Object updated = update.getBody();
    log.info("Результат обновления контакта {}", updated);
    return updated;
  }

  public Object createContact(UserDTO userDTO) {
    CreateContact contact = convertToCreateContact(userDTO);
    this.createContact.setFields(contact.getFields());
    ResponseEntity<Object> create = restTemplate.exchange(BITRIX_CRM_CONTACT_ADD,
        HttpMethod.POST, createContactHttpEntity, Object.class);
    Object created = create.getBody();
    log.info("Результат создания контакта {}", created);
    return created;
  }

  public Object deleteContact(UserDTO userDTO) {
    this.deleteContact.setId(userDTO.getId());
    ResponseEntity<Object> delete = restTemplate.exchange(BITRIX_CRM_CONTACT_DELETE,
        HttpMethod.POST, deleteContactHttpEntity, Object.class);
    Object deleted = delete.getBody();
    log.info("Результат удаления контакта {}", delete);
    return deleted;
  }

  private CreateContact convertToCreateContact(UserDTO userDTO) {
    return CreateContact.builder()
        .fields(prepareFields(userDTO))
        .build();
  }

  private UpdateContact convertToUpdateContact(UserDTO userDTO) {
    return UpdateContact.builder()
        .id(userDTO.getId())
        .fields(prepareFields(userDTO))
        .build();
  }

  private Map<String, Object> prepareFields(UserDTO userDTO) {
    Map<String, Object> fields = new LinkedHashMap<>();
    fields.put("NAME", userDTO.getName());
    fields.put("SECOND_NAME", userDTO.getSecondName());
    fields.put("LAST_NAME", userDTO.getLastName());
    fields.put("EMAIL", Collections.singletonList(convertEmail(userDTO.getEmail())));
    fields.put("PHONE", Collections.singletonList(convertPhone(userDTO.getPhone())));
    fields.put("UF_CRM_1625221385", "1");
    return fields;
  }

  private Email convertEmail(String email) {
    return Email.builder()
        .value(email)
        .valueType(ValueType.WORK.name())
        .typeId("EMAIL")
        .build();
  }

  private Phone convertPhone(String phone) {
    return Phone.builder()
        .value(phone)
        .valueType(ValueType.WORK.name())
        .build();
  }

}
