package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.model.Scanned;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BankRequisitesDTO implements Scanned {

  String bankName;
  String bik;
  String correspondentAccount;
  String currentAccount;
  MultipartFile[] scans;

}
