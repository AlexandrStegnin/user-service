package com.ddkolesnik.userservice.configuration.exception;

import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;

/**
 * @author Alexandr Stegnin
 */
@Data
@Slf4j
@Builder
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class BitrixException extends RuntimeException {

  String message;
  HttpStatus status;

  public static BitrixException build400Exception(String message) {
    logMessage(message);
    return new BitrixException(message, HttpStatus.BAD_REQUEST);
  }

  public static BitrixException build404Exception(String message) {
    logMessage(message);
    return new BitrixException(message, HttpStatus.NOT_FOUND);
  }

  public static BitrixException build500Exception(String message) {
    logMessage(message);
    return new BitrixException(message, HttpStatus.INTERNAL_SERVER_ERROR);
  }

  public static BitrixException build422Exception(String message) {
    logMessage(message);
    return new BitrixException(message, HttpStatus.PRECONDITION_FAILED);
  }

  private static void logMessage(String message) {
    log.error("BitrixException: {}", message);
  }

}
