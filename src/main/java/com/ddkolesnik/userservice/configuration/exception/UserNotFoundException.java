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
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserNotFoundException extends RuntimeException {

  String message;
  HttpStatus status;

  public UserNotFoundException(String message) {
    this.message = message;
    this.status = HttpStatus.NOT_FOUND;
  }

  public static UserNotFoundException buildNotFoundException() {
    return new UserNotFoundException("User not found");
  }

}
