package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.utils.PhoneUtils;
import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;

/**
 * @author Alexandr Stegnin
 */
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ChangePasswordDTO {

  String phone;
  String oldPassword;
  String newPassword;

  public String getPhone() {
    return PhoneUtils.cleanPhone(this.phone);
  }

}
