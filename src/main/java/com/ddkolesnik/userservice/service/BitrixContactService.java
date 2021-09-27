package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.model.bitrix.address.*;
import com.ddkolesnik.userservice.model.bitrix.bp.BusinessProcessTemplate;
import com.ddkolesnik.userservice.model.bitrix.bp.BusinessProcess;
import com.ddkolesnik.userservice.model.bitrix.contact.*;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateFilter;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateResult;
import com.ddkolesnik.userservice.model.bitrix.file.FileData;
import com.ddkolesnik.userservice.model.bitrix.requisite.*;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.ddkolesnik.userservice.model.bitrix.utils.ValueType;
import com.ddkolesnik.userservice.model.dto.PassportDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.utils.DateUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
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

  public DuplicateResult findDuplicates(UserDTO userDTO) {
    DuplicateFilter duplicateFilter = new DuplicateFilter(userDTO.getPhone());
    ResponseEntity<DuplicateResult> bitrixResult = restTemplate.exchange(bitrixProperty.getDuplicateFindByComm(),
        HttpMethod.POST, new HttpEntity<>(duplicateFilter), DuplicateResult.class);
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
    ContactListFilter contactListFilter = new ContactListFilter(filter);
    ResponseEntity<ContactList> contactList = restTemplate.exchange(bitrixProperty.getContactList(),
        HttpMethod.POST, new HttpEntity<>(contactListFilter), ContactList.class);
    ContactList contacts = contactList.getBody();
    log.info("Результат поиска списка контактов по телефону {}", contacts);
    if (Objects.nonNull(contacts)) {
      Contact contact = contacts.getResult()
          .stream().min(Comparator.comparing(Contact::getId))
          .orElse(null);
      if (Objects.nonNull(contact)) {
        contact.setPhone(phone);
      }
      return contact;
    }
    return null;
  }

  public void updateContact(UserDTO userDTO) {
    ContactUpdate contact = convertToUpdateContact(userDTO);
    ContactUpdate contactUpdate = new ContactUpdate(contact.getId(), contact.getFields());
    ResponseEntity<Object> update = restTemplate.exchange(bitrixProperty.getContactUpdate(),
        HttpMethod.POST, new HttpEntity<>(contactUpdate), Object.class);
    Object updated = update.getBody();
    log.info("Результат обновления контакта {}", updated);
  }

  public void clearContactPassword(UserDTO dto) {
    Map<String, Object> fields = new HashMap<>();
    fields.put("UF_CRM_1632722824", null);
    ContactUpdate contactUpdate = ContactUpdate.builder()
        .id(dto.getBitrixId())
        .fields(fields)
        .build();
    ResponseEntity<Object> update = restTemplate.exchange(bitrixProperty.getContactUpdate(),
        HttpMethod.POST, new HttpEntity<>(contactUpdate), Object.class);
    Object updated = update.getBody();
    log.info("Результат обновления контакта {}", updated);
  }

  public Contact getById(String id) {
    ContactGet contactGet = ContactGet.builder()
        .id(id)
        .build();
    Contact contact = null;
    ResponseEntity<Object> contactResponseEntity = restTemplate.exchange(bitrixProperty.getContactGet(),
        HttpMethod.POST, new HttpEntity<>(contactGet), Object.class);
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
    ContactCreate contactCreate = new ContactCreate(contact.getFields());
    ResponseEntity<Object> create = restTemplate.exchange(bitrixProperty.getContactAdd(),
        HttpMethod.POST, new HttpEntity<>(contactCreate), Object.class);
    Object created = create.getBody();
    log.info("Результат создания контакта {}", created);
    return created;
  }

  public Object deleteContact(UserDTO userDTO) {
    ContactDelete contactDelete = new ContactDelete(userDTO.getBitrixId());
    ResponseEntity<Object> delete = restTemplate.exchange(bitrixProperty.getContactDelete(),
        HttpMethod.POST, new HttpEntity<>(contactDelete), Object.class);
    Object deleted = delete.getBody();
    log.info("Результат удаления контакта {}", delete);
    return deleted;
  }

  public Requisite findRequisite(String entityId) {
    LinkedHashMap<String, String> filter = new LinkedHashMap<>();
    filter.put("ENTITY_TYPE_ID", "3");
    filter.put("ENTITY_ID", entityId);

    RequisiteFilter requisiteFilter = new RequisiteFilter(filter);
    ResponseEntity<RequisiteResult> requisite = restTemplate.exchange(bitrixProperty.getRequisiteList(),
        HttpMethod.POST, new HttpEntity<>(requisiteFilter), RequisiteResult.class);
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

  public void createRequisite(UserDTO dto) {
    RequisiteCreate requisiteCreate = RequisiteCreate.builder()
        .fields(prepareRequisiteFields(dto))
        .build();
    ResponseEntity<Object> create = restTemplate.exchange(bitrixProperty.getRequisiteAdd(),
        HttpMethod.POST, new HttpEntity<>(requisiteCreate), Object.class);
    Object created = create.getBody();
    log.info("Результат создания реквизита {}", created);
  }

  public void updateRequisite(Requisite requisite, UserDTO dto) {
    RequisiteUpdate requisiteUpdate = RequisiteUpdate.builder()
        .id(Integer.parseInt(requisite.getId()))
        .fields(prepareRequisiteFields(dto))
        .build();
    ResponseEntity<Object> update = restTemplate.exchange(bitrixProperty.getRequisiteUpdate(),
        HttpMethod.POST, new HttpEntity<>(requisiteUpdate), Object.class);
    Object updated = update.getBody();
    log.info("Результат обновления реквизита {}", updated);
  }

  public Address findAddress(Requisite requisite) {
    LinkedHashMap<String, String> filter = new LinkedHashMap<>();
    filter.put("ENTITY_TYPE_ID", "8");
    filter.put("TYPE_ID", "1");
    filter.put("ENTITY_ID", requisite.getId());

    AddressFilter addressFilter = new AddressFilter(filter);
    ResponseEntity<AddressResult> address = restTemplate.exchange(bitrixProperty.getAddressList(),
        HttpMethod.POST, new HttpEntity<>(addressFilter), AddressResult.class);
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

  public void createAddress(UserDTO dto) {
    AddressCreate addressCreate = AddressCreate.builder()
        .fields(prepareAddressFields(dto))
        .build();
    ResponseEntity<Object> create = restTemplate.exchange(bitrixProperty.getAddressAdd(),
        HttpMethod.POST, new HttpEntity<>(addressCreate), Object.class);
    Object created = create.getBody();
    log.info("Результат создания адреса {}", created);
  }

  public void updateAddress(UserDTO dto) {
    AddressUpdate addressUpdate = AddressUpdate.builder()
        .fields(prepareAddressFields(dto))
        .build();
    ResponseEntity<Object> update = restTemplate.exchange(bitrixProperty.getAddressUpdate(),
        HttpMethod.POST, new HttpEntity<>(addressUpdate), Object.class);
    Object updated = update.getBody();
    log.info("Результат обновления адреса {}", updated);
  }

  public void sendConfirmMessage(UserDTO dto) {
    BusinessProcess businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.CONFIRM_PHONE.getId())
        .build();
    businessProcess.addDocumentId("CONTACT_".concat(dto.getBitrixId().toString()));
    ResponseEntity<Object> start = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    Object started = start.getBody();
    log.info("Результат отправки смс для подтверждения {}", started);
  }

  public void sendRestoreMessage(UserDTO dto) {
    BusinessProcess businessProcess = BusinessProcess.builder()
        .templateId(BusinessProcessTemplate.CHANGE_PASSWORD.getId())
        .build();
    businessProcess.addDocumentId("CONTACT_".concat(dto.getBitrixId().toString()));
    ResponseEntity<Object> restore = restTemplate.exchange(bitrixProperty.getBusinessProcessStart(),
        HttpMethod.POST, new HttpEntity<>(businessProcess), Object.class);
    Object restored = restore.getBody();
    log.info("Результат отправки смс для восстановления пароля {}", restored);
  }

  private ContactCreate convertToCreateContact(UserDTO userDTO) {
    return ContactCreate.builder()
        .fields(prepareFields(userDTO, true))
        .build();
  }

  private ContactUpdate convertToUpdateContact(UserDTO userDTO) {
    return ContactUpdate.builder()
        .id(userDTO.getId())
        .fields(prepareFields(userDTO, false))
        .build();
  }

  private Map<String, Object> prepareFields(UserDTO userDTO, boolean isCreate) {
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
    if (isCreate) {
      fields.put("UF_CRM_1623241031", userDTO.isIndividual() ? "Y" : "N");
      fields.put("UF_CRM_1623241054", userDTO.isSelfEmployed() ? "Y" : "N");
    }
    fields.put("UF_CRM_1628253424", "1");
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
    fields.put("RQ_IDENT_DOC_DATE", DateUtils.convertToDDMMYYYY(userDTO.getPassport().getIssuedAt()));
    fields.put("RQ_IDENT_DOC", "Паспорт");
    return fields;
  }

  private Map<String, Object> prepareAddressFields(UserDTO userDTO) {
    Requisite requisite = findRequisite(userDTO);
    Map<String, Object> fields = new LinkedHashMap<>();
    if (Objects.isNull(requisite)) {
      log.error("Реквизит не найден: {}", userDTO);
    } else {
      fields.put("ENTITY_ID", requisite.getId());
    }
    fields.put("TYPE_ID", 1);
    fields.put("ENTITY_TYPE_ID", 8);
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

}
