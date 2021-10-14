package com.ddkolesnik.userservice.configuration.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author Alexandr Stegnin
 */
@Data
@Validated
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@ConfigurationProperties(prefix = "bitrix.crm")
public class BitrixProperty {

  @NotBlank
  String defaultUrl;

  @NotBlank
  String webhookUserId;

  @NotBlank
  String accessKey;

  @NotBlank
  String duplicateFindByComm;

  @NotBlank
  String contactList;

  @NotBlank
  String contactUpdate;

  @NotBlank
  String contactAdd;

  @NotBlank
  String contactDelete;

  @NotBlank
  String contactGet;

  @NotBlank
  String requisiteList;

  @NotBlank
  String requisiteUpdate;

  @NotBlank
  String requisiteAdd;

  @NotBlank
  String addressList;

  @NotBlank
  String addressUpdate;

  @NotBlank
  String addressAdd;

  @NotBlank
  String businessProcessStart;

  public String getApiUrl() {
    return defaultUrl + webhookUserId + accessKey;
  }

}
