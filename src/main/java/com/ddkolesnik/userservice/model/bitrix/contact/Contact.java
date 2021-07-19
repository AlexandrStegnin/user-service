package com.ddkolesnik.userservice.model.bitrix.contact;

import com.ddkolesnik.userservice.model.bitrix.file.Scan;
import com.ddkolesnik.userservice.model.bitrix.utils.Email;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;
import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
@JsonIgnoreProperties(ignoreUnknown = true)
public class Contact {

  @JsonProperty("ID")
  Integer id;

  @JsonProperty("NAME")
  String name;

  @JsonProperty("SECOND_NAME")
  String secondName;

  @JsonProperty("LAST_NAME")
  String lastName;

  @JsonProperty("EMAIL")
  List<Email> emails;

  @JsonProperty("UF_CRM_1554359872664")
  String gender;

  @JsonProperty("BIRTHDATE")
  String birthdate;

  @JsonProperty("UF_CRM_1625469293802")
  List<Scan> scans;

  @JsonCreator
  public Contact(@JsonProperty("ID") Integer id,
                 @JsonProperty("NAME") String name,
                 @JsonProperty("SECOND_NAME") String secondName,
                 @JsonProperty("LAST_NAME") String lastName,
                 @JsonProperty("EMAIL") List<Email> email,
                 @JsonProperty("UF_CRM_1554359872664") String gender,
                 @JsonProperty("BIRTHDATE") String birthdate,
                 @JsonProperty("UF_CRM_1625469293802") List<Scan> scans) {
    this.id = id;
    this.name = name;
    this.secondName = secondName;
    this.lastName = lastName;
    this.emails = email;
    this.gender = gender;
    this.birthdate = birthdate;
    this.scans = scans;
  }

}
