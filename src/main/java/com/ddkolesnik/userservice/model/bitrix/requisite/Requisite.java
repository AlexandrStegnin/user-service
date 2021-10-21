package com.ddkolesnik.userservice.model.bitrix.requisite;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;
import lombok.experimental.FieldDefaults;

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

  @JsonProperty("ID")
  String id;

  @JsonProperty("RQ_INN")
  String inn;

  @JsonProperty("UF_CRM_1569583111")
  String snils;

  @JsonProperty("RQ_IDENT_DOC_SER")
  String serial;

  @JsonProperty("RQ_IDENT_DOC_NUM")
  String number;

  @JsonProperty("RQ_IDENT_DOC_DEP_CODE")
  String departmentCode;

  @JsonProperty("RQ_IDENT_DOC_ISSUED_BY")
  String issuedBy;

  @Builder.Default
  @JsonProperty("ENTITY_TYPE_ID")
  String entityTypeId = "3";

  @JsonProperty("ENTITY_ID")
  String contactId;

  @Builder.Default
  @JsonProperty("PRESET_ID")
  String presetId = "5";

  @JsonProperty("RQ_IDENT_DOC_DATE")
  String issuedAt;

  @JsonCreator
  public Requisite(@JsonProperty("ID") String id,
                   @JsonProperty("UF_CRM_1569583111") String snils,
                   @JsonProperty("RQ_IDENT_DOC_SER") String serial,
                   @JsonProperty("RQ_IDENT_DOC_NUM") String number,
                   @JsonProperty("RQ_IDENT_DOC_DEP_CODE") String departmentCode,
                   @JsonProperty("RQ_IDENT_DOC_ISSUED_BY") String issuedBy,
                   @JsonProperty("ENTITY_TYPE_ID") String entityTypeId,
                   @JsonProperty("ENTITY_ID") String contactId,
                   @JsonProperty("PRESET_ID") String presetId,
                   @JsonProperty("RQ_IDENT_DOC_DATE") String issuedAt) {
    this.id = id;
    this.snils = snils;
    this.serial = serial;
    this.number = number;
    this.departmentCode = departmentCode;
    this.issuedBy = issuedBy;
    this.entityTypeId = entityTypeId;
    this.contactId = contactId;
    this.presetId = presetId;
    this.issuedAt = issuedAt;
  }

}
