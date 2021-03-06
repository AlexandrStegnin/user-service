package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.model.Scanned;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Aleksandr Stegnin on 13.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class PassportDTO implements Scanned {

  String serial;
  String number;
  String departmentCode;
  String issuedBy;
  MultipartFile[] scans;
  String issuedAt;

}
