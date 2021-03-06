package com.ddkolesnik.userservice.service.bitrix;

import com.ddkolesnik.userservice.configuration.exception.BitrixException;
import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.ddkolesnik.userservice.mapper.ContactMapper;
import com.ddkolesnik.userservice.model.bitrix.contact.BitrixContact;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.enums.TaxStatus;
import com.ddkolesnik.userservice.model.bitrix.enums.ValueType;
import com.ddkolesnik.userservice.model.bitrix.requisite.bank.BankRequisite;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.ddkolesnik.userservice.model.dto.ChangePhoneDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.response.ApiResponse;
import com.ddkolesnik.userservice.web.BitrixWebClient;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Slf4j
@Service
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BitrixContactService extends BitrixService {

  ContactMapper contactMapper;
  BitrixWebClient bitrixWebClient;
  BankRequisiteService bankRequisiteService;

  public BitrixContactService(BitrixProperty bitrixProperty, RestTemplate restTemplate, ContactMapper contactMapper,
                              BitrixWebClient bitrixWebClient, BankRequisiteService bankRequisiteService) {
    super(bitrixProperty, restTemplate);
    this.contactMapper = contactMapper;
    this.bitrixWebClient = bitrixWebClient;
    this.bankRequisiteService = bankRequisiteService;
  }

  public ApiResponse createOrUpdateContact(UserDTO dto) {
     if (bitrixWebClient.isContactExists(dto.getPhone())) {
      updateContact(dto);
      return ApiResponse.build200Response("?????????????? ??24 ?????????????? ????????????????");
    }
    Integer contactId = createContact(dto);
    dto.setBitrixId(contactId);
    return ApiResponse.build201Response("???????????????????????? ?????????????? ???????????? ?? ??24");
  }

  public BitrixContact getContact(UserDTO userDTO) {
    var contact = getContact(userDTO.getPhone());
    userDTO.setBitrixId(contact.getId());
    userDTO.setId(contact.getId());
    return contact;
  }

  public BitrixContact getContact(String phone) {
    var contact = bitrixWebClient.getContact(phone);
    var taxStatus = TaxStatus.fromCode(contact.getTaxStatus());
    var bankRequisites = bankRequisiteService.findRequisites(contact.getId().toString(), taxStatus.getPresetId());
    if (Objects.isNull(bankRequisites)) {
      bankRequisites = new BankRequisite();
    }
    contact.setBankRequisites(bankRequisites);
    return contact;
  }

  public void updateContact(UserDTO userDTO) {
    var contactUpdate = contactMapper.toContactUpdate(userDTO);
    var updated = bitrixWebClient.updateContact(contactUpdate);
    userDTO.setBitrixId(contactUpdate.getId());
    log.info("?????????????????? ???????????????????? ???????????????? {}", updated);
  }

  public void updateContactPhone(BitrixContact bitrixContact, ChangePhoneDTO changePhoneDTO, UserDTO userDTO) {
    var fields = new HashMap<String, Object>();
    var phones = new ArrayList<Phone>();
    var oldPhone = bitrixContact.getPhones().stream()
        .findAny()
        .orElseThrow(() ->
            BitrixException.build400Exception("???? ?????????????? ???????????????? ?????????????? ???? ????????????????")
        );
    oldPhone.setValue("");
    phones.add(oldPhone);
    var newPhone = Phone.builder()
        .value(changePhoneDTO.getNewPhone())
        .valueType(ValueType.WORK.name())
        .build();
    phones.add(newPhone);

    fields.put(CONTACT_PHONE, phones);

    var contact = buildContact(userDTO, fields);
    var updated = bitrixWebClient.updateContact(contact);
    log.info("?????????????????? ???????????????????? ???????????????? ???????????????? {}", updated);
  }

  public void updateContactConfirmCode(UserDTO userDTO) {
    var fields = new HashMap<String, Object>();
    fields.put(CONTACT_CONFIRM_CODE, userDTO.getConfirmCode());
    var contact = buildContact(userDTO, fields);
    var updated = bitrixWebClient.updateContact(contact);
    log.info("?????????????????? ???????????????????? ???????? ?????????????????????????? ???????????????? {}", updated);
  }

  public void clearContactConfirmCodes(UserDTO userDTO) {
    var fields = new HashMap<String, Object>();
    fields.put(CONTACT_CONFIRM_CODE, "");
    fields.put(RETRY_CONFIRM_CODE, "");
    var contact = buildContact(userDTO, fields);
    var updated = bitrixWebClient.updateContact(contact);
    log.info("?????????????????? ???????????????????? ?????????? ?????????????????????????? ???????????????? {}", updated);
  }

  public void addNewContactPhone(UserDTO userDTO, String newPhone) {
    var fields = new HashMap<String, Object>();
    fields.put(CONTACT_NEW_PHONE, newPhone);
    var contact = buildContact(userDTO, fields);
    var updated = bitrixWebClient.updateContact(contact);
    log.info("?????????????????? ???????????????????? ???????????? ???????????????? ???????????????? {}", updated);
  }

  private Integer createContact(UserDTO userDTO) {
    var contact = contactMapper.toContactCreate(userDTO);
    return bitrixWebClient.createContact(contact);
  }

  private Contact buildContact(UserDTO userDTO, Map<String, Object> fields) {
    return Contact.builder()
        .id(userDTO.getBitrixId())
        .fields(fields)
        .build();
  }

}
