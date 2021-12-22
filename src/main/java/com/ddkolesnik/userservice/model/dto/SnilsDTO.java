package com.ddkolesnik.userservice.model.dto;

import com.ddkolesnik.userservice.model.Scanned;
import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SnilsDTO implements Scanned {

  String number;
  MultipartFile[] scans;

  public String getNumber() {
    return Optional.ofNullable(this.number)
        .map(n -> n.replaceAll("[^0-9]+", ""))
        .orElse("");
  }

}
