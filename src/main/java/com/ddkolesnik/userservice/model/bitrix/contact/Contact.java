package com.ddkolesnik.userservice.model.bitrix.contact;

import com.ddkolesnik.userservice.model.bitrix.file.Scan;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.ddkolesnik.userservice.model.bitrix.utils.Phone;
import com.fasterxml.jackson.annotation.JsonCreator;
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
public class Contact {

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

  @JsonProperty(CONTACT_SCANS)
  List<Scan> scans;

  @JsonProperty(CONTACT_PLACE_OF_BIRTH)
  String placeOfBirth;

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

  @JsonCreator
  public Contact(@JsonProperty(CONTACT_ID) Integer id,
                 @JsonProperty(CONTACT_NAME) String name,
                 @JsonProperty(CONTACT_SECOND_NAME) String secondName,
                 @JsonProperty(CONTACT_LAST_NAME) String lastName,
                 @JsonProperty(CONTACT_EMAIL) List<Email> email,
                 @JsonProperty(CONTACT_GENDER) String gender,
                 @JsonProperty(CONTACT_BIRTHDATE) String birthdate,
                 @JsonProperty(CONTACT_SCANS) List<Scan> scans,
                 @JsonProperty(CONTACT_PLACE_OF_BIRTH) String placeOfBirth,
                 @JsonProperty(CONTACT_CONFIRM_CODE) String confirmCode,
                 @JsonProperty(CONTACT_RAW_PASSWORD) String rawPassword,
                 @JsonProperty(CONTACT_TAX_STATUS) Integer taxStatus,
                 @JsonProperty(CONTACT_NEW_PHONE) String newPhone) {
    this.id = id;
    this.name = name;
    this.secondName = secondName;
    this.lastName = lastName;
    this.emails = email;
    this.gender = gender;
    this.birthdate = birthdate;
    this.scans = scans;
    this.placeOfBirth = placeOfBirth;
    this.confirmCode = confirmCode;
    this.rawPassword = rawPassword;
    this.taxStatus = taxStatus;
    this.newPhone = newPhone;
  }

}
