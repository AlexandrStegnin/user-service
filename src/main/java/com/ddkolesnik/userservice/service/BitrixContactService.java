package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.enums.Gender;
import com.ddkolesnik.userservice.model.bitrix.address.Address;
import com.ddkolesnik.userservice.model.bitrix.address.AddressCreate;
import com.ddkolesnik.userservice.model.bitrix.address.AddressFilter;
import com.ddkolesnik.userservice.model.bitrix.address.AddressResult;
import com.ddkolesnik.userservice.model.bitrix.address.AddressUpdate;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactCreate;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactDelete;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactGet;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactList;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactListFilter;
import com.ddkolesnik.userservice.model.bitrix.contact.ContactUpdate;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateFilter;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateResult;
import com.ddkolesnik.userservice.model.bitrix.file.FileData;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.bitrix.requisite.RequisiteCreate;
import com.ddkolesnik.userservice.model.bitrix.requisite.RequisiteFilter;
import com.ddkolesnik.userservice.model.bitrix.requisite.RequisiteResult;
import com.ddkolesnik.userservice.model.bitrix.requisite.RequisiteUpdate;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.ddkolesnik.userservice.model.bitrix.utils.ValueType;
import com.ddkolesnik.userservice.model.dto.AddressDTO;
import com.ddkolesnik.userservice.model.dto.PassportDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.io.IOException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
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
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

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

  @Value("${bitrix.crm.contact.get}")
  String BITRIX_CRM_CONTACT_GET;

  @Value("${bitrix.crm.requisite.list}")
  String BITRIX_CRM_REQUISITE_LIST;

  @Value("${bitrix.crm.requisite.update}")
  String BITRIX_CRM_REQUISITE_UPDATE;

  @Value("${bitrix.crm.requisite.add}")
  String BITRIX_CRM_REQUISITE_ADD;

  @Value("${bitrix.crm.address.list}")
  String BITRIX_CRM_ADDRESS_LIST;

  @Value("${bitrix.crm.address.update}")
  String BITRIX_CRM_ADDRESS_UPDATE;

  @Value("${bitrix.crm.address.add}")
  String BITRIX_CRM_ADDRESS_ADD;

  final RestTemplate restTemplate;

  final HttpEntity<DuplicateFilter> httpEntity;

  final DuplicateFilter duplicateFilter;

  final ContactListFilter contactListFilter;

  final HttpEntity<ContactListFilter> contactListEntity;

  final HttpEntity<ContactUpdate> updateContactHttpEntity;

  final ContactUpdate contactUpdate;

  final ContactCreate contactCreate;

  final HttpEntity<ContactCreate> createContactHttpEntity;

  final ContactDelete contactDelete;

  final HttpEntity<ContactDelete> deleteContactHttpEntity;

  final RequisiteFilter requisiteFilter;

  final HttpEntity<RequisiteFilter> requisiteHttpEntity;

  final RequisiteUpdate requisiteUpdate;

  final HttpEntity<RequisiteUpdate> requisiteUpdateHttpEntity;

  final RequisiteCreate requisiteCreate;

  final HttpEntity<RequisiteCreate> requisiteCreateHttpEntity;

  final AddressFilter addressFilter;

  final HttpEntity<AddressFilter> addressFilterHttpEntity;

  final AddressCreate addressCreate;

  final HttpEntity<AddressCreate> addressCreateHttpEntity;

  final AddressUpdate addressUpdate;

  final HttpEntity<AddressUpdate> addressUpdateHttpEntity;

  final ContactGet contactGet;

  final HttpEntity<ContactGet> contactGetHttpEntity;

  final ObjectMapper objectMapper;

  @Autowired
  public BitrixContactService(RestTemplate restTemplate) {
    this.restTemplate = restTemplate;
    this.duplicateFilter = new DuplicateFilter();
    this.contactListFilter = new ContactListFilter();
    this.contactUpdate = new ContactUpdate();
    this.contactCreate = new ContactCreate();
    this.contactDelete = new ContactDelete();
    this.requisiteFilter = new RequisiteFilter();
    this.requisiteUpdate = new RequisiteUpdate();
    this.requisiteCreate = new RequisiteCreate();
    this.addressFilter = new AddressFilter();
    this.addressCreate = new AddressCreate();
    this.addressUpdate = new AddressUpdate();
    this.contactGet = new ContactGet();
    this.httpEntity = new HttpEntity<>(duplicateFilter);
    this.contactListEntity = new HttpEntity<>(contactListFilter);
    this.updateContactHttpEntity = new HttpEntity<>(contactUpdate);
    this.createContactHttpEntity = new HttpEntity<>(contactCreate);
    this.deleteContactHttpEntity = new HttpEntity<>(contactDelete);
    this.requisiteHttpEntity = new HttpEntity<>(requisiteFilter);
    this.requisiteUpdateHttpEntity = new HttpEntity<>(requisiteUpdate);
    this.requisiteCreateHttpEntity = new HttpEntity<>(requisiteCreate);
    this.addressFilterHttpEntity = new HttpEntity<>(addressFilter);
    this.addressCreateHttpEntity = new HttpEntity<>(addressCreate);
    this.addressUpdateHttpEntity = new HttpEntity<>(addressUpdate);
    this.contactGetHttpEntity = new HttpEntity<>(contactGet);
    this.objectMapper = new ObjectMapper();
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
    return findFirstContact(userDTO.getPhone());
  }

  public Contact findFirstContact(String phone) {
    Map<String, String[]> filter = new HashMap<>();
    filter.put("PHONE", Collections.singleton(phone).toArray(new String[0]));
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
    ContactUpdate contact = convertToUpdateContact(userDTO);
    this.contactUpdate.setId(contact.getId());
    this.contactUpdate.setFields(contact.getFields());
    ResponseEntity<Object> update = restTemplate.exchange(BITRIX_CRM_CONTACT_UPDATE,
        HttpMethod.POST, updateContactHttpEntity, Object.class);
    Object updated = update.getBody();
    log.info("Результат обновления контакта {}", updated);
    return updated;
  }

  public Contact getById(String id) {
    this.contactGet.setId(id);
    Contact contact = null;
    ResponseEntity<Object> contactResponseEntity = restTemplate.exchange(BITRIX_CRM_CONTACT_GET,
        HttpMethod.POST, contactGetHttpEntity, Object.class);
    Object contactObject = contactResponseEntity.getBody();
    if (contactObject instanceof LinkedHashMap) {
      Object result = ((LinkedHashMap<?, ?>) contactObject).get("result");
      contact = objectMapper.convertValue(result, Contact.class);
    }
    log.info("Результат получения контакта {}", contact);
    return contact;
  }

  public Object createContact(UserDTO userDTO) {
    ContactCreate contact = convertToCreateContact(userDTO);
    this.contactCreate.setFields(contact.getFields());
    ResponseEntity<Object> create = restTemplate.exchange(BITRIX_CRM_CONTACT_ADD,
        HttpMethod.POST, createContactHttpEntity, Object.class);
    Object created = create.getBody();
    log.info("Результат создания контакта {}", created);
    return created;
  }

  public Object deleteContact(UserDTO userDTO) {
    this.contactDelete.setId(userDTO.getId());
    ResponseEntity<Object> delete = restTemplate.exchange(BITRIX_CRM_CONTACT_DELETE,
        HttpMethod.POST, deleteContactHttpEntity, Object.class);
    Object deleted = delete.getBody();
    log.info("Результат удаления контакта {}", delete);
    return deleted;
  }

  public Requisite findRequisite(String entityId) {
    LinkedHashMap<String, String> filter = new LinkedHashMap<>();
    filter.put("ENTITY_TYPE_ID", "3");
    filter.put("ENTITY_ID", entityId);
    this.requisiteFilter.setFilter(filter);
    ResponseEntity<RequisiteResult> requisite = restTemplate.exchange(BITRIX_CRM_REQUISITE_LIST,
        HttpMethod.POST, requisiteHttpEntity, RequisiteResult.class);
    log.info("Результат поиска реквизита {}", requisite);
    RequisiteResult response = requisite.getBody();
    if (Objects.isNull(response) || response.getTotal() == 0) {
      return null;
    }
    List<Requisite> requisites = response.getResult();
    if (requisites.isEmpty()) {
      return null;
    }
    return requisites.get(0);
  }

  public Requisite findRequisite(UserDTO dto) {
    return findRequisite(dto.getId().toString());
  }

  public Object createRequisite(UserDTO dto) {
    RequisiteCreate requisiteCreate = RequisiteCreate.builder()
        .fields(prepareRequisiteFields(dto))
        .build();
    this.requisiteCreate.setFields(requisiteCreate.getFields());
    ResponseEntity<Object> create = restTemplate.exchange(BITRIX_CRM_REQUISITE_ADD,
        HttpMethod.POST, requisiteCreateHttpEntity, Object.class);
    Object created = create.getBody();
    log.info("Результат создания реквизита {}", created);
    return created;
  }

  public Object updateRequisite(Requisite requisite, UserDTO dto) {
    RequisiteUpdate requisiteUpdate = RequisiteUpdate.builder()
        .id(Integer.parseInt(requisite.getId()))
        .fields(prepareRequisiteFields(dto))
        .build();
    this.requisiteUpdate.setId(requisiteUpdate.getId());
    this.requisiteUpdate.setFields(requisiteUpdate.getFields());
    ResponseEntity<Object> update = restTemplate.exchange(BITRIX_CRM_REQUISITE_UPDATE,
        HttpMethod.POST, requisiteUpdateHttpEntity, Object.class);
    Object updated = update.getBody();
    log.info("Результат обновления реквизита {}", updated);
    return updated;
  }

  public Address findAddress(UserDTO dto) {
    LinkedHashMap<String, String> filter = new LinkedHashMap<>();
    filter.put("ENTITY_TYPE_ID", "8");
    filter.put("TYPE_ID", "1");
    filter.put("ENTITY_ID", dto.getId().toString());
    this.addressFilter.setFilter(filter);
    ResponseEntity<AddressResult> address = restTemplate.exchange(BITRIX_CRM_ADDRESS_LIST,
        HttpMethod.POST, addressFilterHttpEntity, AddressResult.class);
    log.info("Результат поиска адресов {}", address);
    AddressResult response = address.getBody();
    if (Objects.isNull(response) || response.getTotal() == 0) {
      return null;
    }
    List<Address> addresses = response.getResult();
    if (addresses.isEmpty()) {
      return null;
    }
    return addresses.get(0);
  }

  public Object createAddress(UserDTO dto) {
    AddressCreate addressCreate = AddressCreate.builder()
        .fields(prepareAddressFields(dto))
        .build();
    this.addressCreate.setFields(addressCreate.getFields());
    ResponseEntity<Object> create = restTemplate.exchange(BITRIX_CRM_ADDRESS_ADD,
        HttpMethod.POST, addressCreateHttpEntity, Object.class);
    Object created = create.getBody();
    log.info("Результат создания адреса {}", created);
    return created;
  }

  public Object updateAddress(UserDTO dto) {
    AddressUpdate addressUpdate = AddressUpdate.builder()
        .fields(prepareAddressFields(dto))
        .build();
    this.addressUpdate.setFields(addressUpdate.getFields());
    ResponseEntity<Object> update = restTemplate.exchange(BITRIX_CRM_ADDRESS_UPDATE,
        HttpMethod.POST, addressUpdateHttpEntity, Object.class);
    Object updated = update.getBody();
    log.info("Результат обновления адреса {}", updated);
    return updated;
  }

  public UserDTO getBitrixContact(String phone) {

    Contact contact = findFirstContact(phone);
    if (Objects.nonNull(contact)) {
      UserDTO dto = new UserDTO();
      dto.setId(contact.getId());
      dto.setPhone(phone);
      if (!contact.getEmails().isEmpty()) {
        contact.getEmails().stream().findAny().ifPresent(email -> dto.setEmail(email.getValue()));
      }
      dto.setName(contact.getName());
      dto.setSecondName(contact.getSecondName());
      dto.setLastName(contact.getLastName());
      dto.setBirthdate(parseBirthdate(contact.getBirthdate()));
      dto.setPlaceOfBirth(contact.getPlaceOfBirth());
      contact = getById(contact.getId().toString());
      if (isGenderAvailable(contact)) {
        dto.setGender(Gender.fromId(Integer.parseInt(contact.getGender())));
      }

      Requisite requisite = findRequisite(contact.getId().toString());
      if (Objects.nonNull(requisite)) {
        dto.setInn(requisite.getInn());
        dto.setSnils(requisite.getSnils());
        PassportDTO passportDTO = PassportDTO.builder()
            .serial(requisite.getSerial())
            .issuedBy(requisite.getIssuedBy())
            .departmentCode(requisite.getDepartmentCode())
            .number(requisite.getNumber())
            .issuedAt(requisite.getIssuedAt())
            .build();
        dto.setPassport(passportDTO);
      }
      Address address = findAddress(dto);
      if (Objects.nonNull(address)) {
        AddressDTO addressDTO = AddressDTO.builder()
            .city(address.getCity())
            .streetAndHouse(address.getAddress1())
            .office(address.getAddress2())
            .build();
        dto.setAddress(addressDTO);
      }
      return dto;
    }
    return null;
  }

  private ContactCreate convertToCreateContact(UserDTO userDTO) {
    return ContactCreate.builder()
        .fields(prepareFields(userDTO))
        .build();
  }

  private ContactUpdate convertToUpdateContact(UserDTO userDTO) {
    return ContactUpdate.builder()
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
    if (Objects.nonNull(userDTO.getBirthdate())) {
      fields.put("BIRTHDATE", userDTO.getBirthdate());
    }
    if (Objects.nonNull(userDTO.getGender())) {
      fields.put("UF_CRM_1554359872664", userDTO.getGender().getId());
    }
    if (isScansAvailable(userDTO.getPassport())) {
      fields.put("UF_CRM_1625469293802", convertScans(userDTO));
    }
    if (Objects.nonNull(userDTO.getPlaceOfBirth())) {
      fields.put("UF_CRM_1623241366", userDTO.getPlaceOfBirth());
    }
    fields.put("UF_CRM_1623241031", userDTO.isIndividual() ? "Y" : "N");
    fields.put("UF_CRM_1623241054", userDTO.isSelfEmployed() ? "Y" : "N");
    return fields;
  }

  private Map<String, Object> prepareRequisiteFields(UserDTO userDTO) {
    Map<String, Object> fields = new LinkedHashMap<>();
    fields.put("RQ_INN", userDTO.getInn());
    fields.put("UF_CRM_1569583111", userDTO.getSnils());
    fields.put("RQ_IDENT_DOC_SER", userDTO.getPassport().getSerial());
    fields.put("RQ_IDENT_DOC_NUM", userDTO.getPassport().getNumber());
    fields.put("RQ_IDENT_DOC_DEP_CODE", userDTO.getPassport().getDepartmentCode());
    fields.put("RQ_IDENT_DOC_ISSUED_BY", userDTO.getPassport().getIssuedBy());
    fields.put("ENTITY_TYPE_ID", "3");
    fields.put("PRESET_ID", "5");
    fields.put("ENTITY_ID", userDTO.getId().toString());
    fields.put("NAME", "Реквизит");
    fields.put("RQ_IDENT_DOC_DATE", userDTO.getPassport().getIssuedAt());
    fields.put("RQ_IDENT_DOC", "Паспорт");
    return fields;
  }

  private Map<String, Object> prepareAddressFields(UserDTO userDTO) {
    Map<String, Object> fields = new LinkedHashMap<>();
    fields.put("TYPE_ID", 1);
    fields.put("ENTITY_TYPE_ID", 8);
    fields.put("ENTITY_ID", userDTO.getId().toString());
    fields.put("CITY", userDTO.getAddress().getCity());
    fields.put("ADDRESS_1", getAddress1(userDTO));
    fields.put("ADDRESS_2", getAddress2(userDTO));
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

  private String getAddress1(UserDTO dto) {
    String address = "";
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

  private List<FileData> convertScans(UserDTO dto) {
    MultipartFile[] files = dto.getPassport().getScans();
    List<FileData> fileDataList = new ArrayList<>();
    for (MultipartFile file : files) {
      try {
        byte[] content = file.getBytes();
        String imageString = Base64Utils.encodeToString(content);
        FileData fileData = new FileData();
        fileData.setFileData(new String[] {file.getOriginalFilename(), imageString});
        fileDataList.add(fileData);
      } catch (IOException e) {
        log.error("Произошла ошибка {}", e.getMessage());
      }
    }
    return fileDataList;
  }

  private boolean isScansAvailable(PassportDTO dto) {
    return Objects.nonNull(dto)
        && Objects.nonNull(dto.getScans())
        && (dto.getScans().length > 0);
  }

  private boolean isGenderAvailable(Contact contact) {
    return Objects.nonNull(contact) && Objects.nonNull(contact.getGender()) &&
        !contact.getGender().isEmpty();
  }

  private String parseBirthdate(String birthdateString) {
    if (Objects.isNull(birthdateString) || birthdateString.isEmpty()) {
      return null;
    }
    String datePart = birthdateString.split("T")[0];
    LocalDate localDate = LocalDate.parse(datePart, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
    return localDate.toString();
  }

}
