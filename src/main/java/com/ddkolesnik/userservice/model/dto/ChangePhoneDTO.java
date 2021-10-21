package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.utils.PhoneUtils;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePhoneDTO {

  String oldPhone;
  String newPhone;
  String confirmCode;
  Integer bitrixId;
  String email;

  public String getNewPhone() {
    return PhoneUtils.cleanPhone(this.newPhone);
  }

  public String getOldPhone() {
    return PhoneUtils.cleanPhone(this.oldPhone);
  }

}
