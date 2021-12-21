package com.ddkolesnik.userservice.model.bitrix.requisite;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

import static com.ddkolesnik.userservice.model.bitrix.utils.BitrixFields.*;

/**
 * @author Aleksandr Stegnin on 14.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@ToString(of = {"id", "contactId"})
@FieldDefaults(level = AccessLevel.PRIVATE)
public class Requisite {

  @JsonProperty(REQUISITE_ID)
  String id;

  @JsonProperty(CONTACT_INN)
  String inn;

  @JsonProperty(REQUISITE_SNILS)
  String snils;

  @JsonProperty(PASSPORT_SERIAL)
  String serial;

  @JsonProperty(PASSPORT_NUMBER)
  String number;

  @JsonProperty(PASSPORT_DEP_CODE)
  String departmentCode;

  @JsonProperty(PASSPORT_ISSUED_BY)
  String issuedBy;

  @Builder.Default
  @JsonProperty(ENTITY_TYPE_ID)
  String entityTypeId = "3";

  @JsonProperty(ENTITY_ID)
  String contactId;

  @Builder.Default
  @JsonProperty(PRESET_ID)
  String presetId = "5";

  @JsonProperty(PASSPORT_ISSUED_AT)
  String issuedAt;

  @JsonProperty(CONTACT_BIRTHDATE)
  String birthdate;

  @JsonProperty(BUSINESSMAN_BIRTHDATE)
  String businessmanBirthdate;

  @JsonProperty(LEGAL_ENTITY_BIRTHDATE)
  String legalEntityBirthdate;

  @JsonProperty(CONTACT_PLACE_OF_BIRTH)
  String placeOfBirth;

  @JsonProperty(BUSINESSMAN_PLACE_OF_BIRTH)
  String businessmanPlaceOfBirth;

  @JsonProperty(LEGAL_ENTITY_PLACE_OF_BIRTH)
  String legalEntityPlaceOfBirth;

}
