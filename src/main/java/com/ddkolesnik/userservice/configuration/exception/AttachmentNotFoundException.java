package com.ddkolesnik.userservice.configuration.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

/**
 * @author Alexandr Stegnin
 */
@Data
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = false)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AttachmentNotFoundException extends RuntimeException {

  String message;
  HttpStatus status;

  public AttachmentNotFoundException(String message) {
    this.message = message;
    this.status = HttpStatus.NOT_FOUND;
  }

}
