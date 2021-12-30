package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.enums.Gender;
import com.ddkolesnik.userservice.model.bitrix.enums.TaxStatus;
import com.ddkolesnik.userservice.utils.PhoneUtils;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.*;
import lombok.experimental.FieldDefaults;

import java.util.Optional;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@JsonIgnoreProperties(ignoreUnknown = true)
@FieldDefaults(level = AccessLevel.PRIVATE)
@ToString(of = { "id", "name", "email", "individual", "selfEmployed"})
public class UserDTO {

  Integer id;
  String name;
  String secondName;
  String lastName;
  String email;
  String phone;
  String confirmCode;
  String inn;
  SnilsDTO snils;
  PassportDTO passport;
  BankRequisitesDTO bankRequisites;
  String address;
  Gender gender;
  String birthdate;
  String password;
  @Builder.Default
  boolean individual = false;
  @Builder.Default
  boolean selfEmployed = false;
  String placeOfBirth;
  Integer bitrixId;
  TaxStatus taxStatus;
  @Builder.Default
  boolean accredited = false;
  BalanceDTO balance;
  String companyFullName;
  String investorNumber;

  public String getPhone() {
    return PhoneUtils.cleanPhone(this.phone);
  }

  public String getFullName() {
    if (TaxStatus.LEGAL_ENTITY == this.taxStatus) {
      return companyFullName;
    }
    var fullName = this.lastName + " " + this.name + " ";
    return (fullName + Optional.ofNullable(this.secondName).orElse("")).trim();
  }

}
