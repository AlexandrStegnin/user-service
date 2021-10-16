package com.ddkolesnik.userservice.configuration.exception;

import com.ddkolesnik.userservice.response.ApiResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Instant;

/**
 * @author Alexandr Stegnin
 */
@Slf4j
@RestControllerAdvice
public class ApiExceptionHandler {

  @ExceptionHandler({BitrixException.class})
  protected ResponseEntity<ApiResponse> handleApiException(BitrixException ex) {
    ApiResponse apiResponse = ApiResponse.builder()
        .message(ex.getMessage())
        .status(ex.getStatus())
        .timestamp(Instant.now())
        .build();
    log.error("Произошла ошибка {}", apiResponse);
    return new ResponseEntity<>(apiResponse, ex.getStatus());
  }

}
