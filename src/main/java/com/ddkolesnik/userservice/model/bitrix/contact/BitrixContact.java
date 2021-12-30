package com.ddkolesnik.userservice.model.bitrix.contact;

import com.ddkolesnik.userservice.model.bitrix.file.Scan;
import com.ddkolesnik.userservice.model.bitrix.requisite.bank.BankRequisite;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import java.util.List;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@NoArgsConstructor
@ToString(of = { "id", "name", "emails" })
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class BitrixContact {

  @JsonProperty(CONTACT_ID)
  Integer id;

  @JsonProperty(CONTACT_NAME)
  String name;

  @JsonProperty(CONTACT_SECOND_NAME)
  String secondName;

  @JsonProperty(CONTACT_LAST_NAME)
  String lastName;

  @JsonProperty(CONTACT_EMAIL)
  List<Email> emails;

  @JsonProperty(CONTACT_GENDER)
  String gender;

  @JsonProperty(CONTACT_BIRTHDATE)
  String birthdate;

  @JsonProperty(BUSINESSMAN_BIRTHDATE)
  String businessmanBirthdate;

  @JsonProperty(LEGAL_ENTITY_BIRTHDATE)
  String legalEntityBirthdate;

  @JsonProperty(CONTACT_SCANS)
  List<Scan> scans;

  @JsonProperty(CONTACT_PLACE_OF_BIRTH)
  String placeOfBirth;

  @JsonProperty(BUSINESSMAN_PLACE_OF_BIRTH)
  String businessmanPlaceOfBirth;

  @JsonProperty(LEGAL_ENTITY_PLACE_OF_BIRTH)
  String legalEntityPlaceOfBirth;

  @JsonProperty(CONTACT_CONFIRM_CODE)
  String confirmCode;

  @JsonProperty(value = CONTACT_RAW_PASSWORD, access = JsonProperty.Access.WRITE_ONLY)
  String rawPassword;

  @JsonProperty(CONTACT_PHONE)
  List<Phone> phones;

  @JsonProperty(CONTACT_TAX_STATUS)
  Integer taxStatus;

  @JsonProperty(CONTACT_NEW_PHONE)
  String newPhone;

  @JsonProperty(CONTACT_ADDRESS)
  String address;

  @JsonProperty(IS_CONTACT_ACCREDITED)
  Integer contactAccredited;

  BankRequisite bankRequisites;

  @JsonProperty(RETRY_CONFIRM_CODE)
  String retryConfirmCode;

  @JsonProperty(INVESTOR_NUMBER)
  String investorNumber;

}
