package com.ddkolesnik.userservice.model.bitrix.address;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BitrixAddress {

  @Builder.Default
  @JsonProperty("TYPE_ID")
  String typeId = "1";

  @Builder.Default
  @JsonProperty("ENTITY_TYPE_ID")
  String entityTypeId = "8";

  @JsonProperty("ENTITY_ID")
  String entityId;

  @JsonProperty("ADDRESS_1")
  String address1;

  @JsonProperty("ADDRESS_2")
  String address2;

  @JsonProperty("CITY")
  String city;

  @JsonProperty("POSTAL_CODE")
  String postalCode;

  @JsonProperty("REGION")
  String region;

  @JsonProperty("PROVINCE")
  String province;

  @JsonProperty("COUNTRY")
  String country;

  @JsonProperty("COUNTRY_CODE")
  String countryCode;

  @JsonProperty("LOC_ADDR_ID")
  String locAddrId;

  @JsonProperty("ANCHOR_TYPE_ID")
  String anchorTypeId;

  @JsonProperty("ANCHOR_ID")
  String anchorId;

  @JsonCreator
  public BitrixAddress(@JsonProperty("TYPE_ID") String typeId,
                 @JsonProperty("ENTITY_TYPE_ID") String entityTypeId,
                 @JsonProperty("ENTITY_ID") String entityId,
                 @JsonProperty("ADDRESS_1") String address1,
                 @JsonProperty("ADDRESS_2") String address2,
                 @JsonProperty("CITY") String city,
                 @JsonProperty("POSTAL_CODE") String postalCode,
                 @JsonProperty("REGION") String region,
                 @JsonProperty("PROVINCE") String province,
                 @JsonProperty("COUNTRY") String country,
                 @JsonProperty("COUNTRY_CODE") String countryCode,
                 @JsonProperty("LOC_ADDR_ID") String locAddrId,
                 @JsonProperty("ANCHOR_TYPE_ID") String anchorTypeId,
                 @JsonProperty("ANCHOR_ID") String anchorId) {
    this.typeId = typeId;
    this.entityTypeId = entityTypeId;
    this.entityId = entityId;
    this.address1 = address1;
    this.address2 = address2;
    this.city = city;
    this.postalCode = postalCode;
    this.region = region;
    this.province = province;
    this.country = country;
    this.countryCode = countryCode;
    this.locAddrId = locAddrId;
    this.anchorTypeId = anchorTypeId;
    this.anchorId = anchorId;
  }

}
