package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.ContactList;
import com.ddkolesnik.userservice.model.ContactListFilter;
import com.ddkolesnik.userservice.model.DuplicateFilter;
import com.ddkolesnik.userservice.model.DuplicateResult;
import com.ddkolesnik.userservice.model.UpdateContact;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
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
@PropertySource(value = {"classpath:private.properties", "classpath:application.properties"})
public class BitrixContactService {

  @Value("${bitrix.default.url}")
  String BITRIX_API_BASE_URL;

  @Value("${bitrix.webhook.user.id}")
  String BITRIX_WEBHOOK_USER_ID;

  @Value("${bitrix.access.key}")
  String BITRIX_ACCESS_KEY;

  @Value("${bitrix.crm.duplicate.findbycomm}")
  String BITRIX_CRM_DUPLICATE_FIND_BY_COMM;

  @Value("${bitrix.crm.contact.list}")
  String BITRIX_CRM_CONTACT_LIST;

  @Value("${bitrix.crm.contact.update}")
  String BITRIX_CRM_CONTACT_UPDATE;

  final RestTemplate restTemplate;

  final HttpEntity<DuplicateFilter> httpEntity;

  final DuplicateFilter duplicateFilter;

  final ContactListFilter contactListFilter;

  final HttpEntity<ContactListFilter> contactListEntity;

  HttpEntity<UpdateContact> contactHttpEntity;

  UpdateContact updateContact;

  @Autowired
  public BitrixContactService(RestTemplate restTemplate) {
    HttpHeaders httpHeaders = new HttpHeaders();
    httpHeaders.add(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    this.restTemplate = restTemplate;
    this.duplicateFilter = new DuplicateFilter();
    this.contactListFilter = new ContactListFilter();
    this.updateContact = new UpdateContact();
    this.httpEntity = new HttpEntity<>(duplicateFilter, httpHeaders);
    this.contactListEntity = new HttpEntity<>(contactListFilter, httpHeaders);
    this.contactHttpEntity = new HttpEntity<>(updateContact, httpHeaders);
  }

  public DuplicateResult findDuplicatesByPhones(List<String> phones) {
    duplicateFilter.setValues(phones);
    ResponseEntity<DuplicateResult> bitrixResult = restTemplate.exchange(
        BITRIX_API_BASE_URL + BITRIX_WEBHOOK_USER_ID + BITRIX_ACCESS_KEY + BITRIX_CRM_DUPLICATE_FIND_BY_COMM,
        HttpMethod.POST, httpEntity, DuplicateResult.class);
    return bitrixResult.getBody();
  }

  public ContactList getContactByPhones(List<String> phones) {
    Map<String, String[]> filter = new HashMap<>();
    filter.put("PHONE", phones.toArray(new String[0]));
    contactListFilter.setFilter(filter);
    ResponseEntity<ContactList> contactList = restTemplate.exchange(
        BITRIX_API_BASE_URL + BITRIX_WEBHOOK_USER_ID + BITRIX_ACCESS_KEY + BITRIX_CRM_CONTACT_LIST,
        HttpMethod.POST, contactListEntity, ContactList.class);
    return contactList.getBody();
  }

  public Object updateContact(UpdateContact contact) {
    this.updateContact.setId(contact.getId());
    this.updateContact.setFields(contact.getFields());
    ResponseEntity<Object> updated = restTemplate.exchange(
        BITRIX_API_BASE_URL + BITRIX_WEBHOOK_USER_ID + BITRIX_ACCESS_KEY + BITRIX_CRM_CONTACT_UPDATE,
        HttpMethod.POST, contactHttpEntity, Object.class);
    return updated.getBody();
  }

}
