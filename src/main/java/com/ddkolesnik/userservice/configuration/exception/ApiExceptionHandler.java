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

  private static final String AN_EXCEPTION_OCCURRED = "Произошла ошибка {}";

  @ExceptionHandler(BitrixException.class)
  protected ResponseEntity<ApiResponse> handleApiException(BitrixException ex) {
    ApiResponse apiResponse = ApiResponse.builder()
        .message(ex.getMessage())
        .status(ex.getStatus())
        .timestamp(Instant.now())
        .build();
    log.error(AN_EXCEPTION_OCCURRED, apiResponse);
    return new ResponseEntity<>(apiResponse, ex.getStatus());
  }

  @ExceptionHandler(UserNotFoundException.class)
  protected ResponseEntity<ApiResponse> handleUserNotFoundException(UserNotFoundException ex) {
    ApiResponse apiResponse = ApiResponse.builder()
        .message(ex.getMessage())
        .status(ex.getStatus())
        .timestamp(Instant.now())
        .build();
    log.error(AN_EXCEPTION_OCCURRED, apiResponse);
    return new ResponseEntity<>(apiResponse, ex.getStatus());
  }

  @ExceptionHandler(AttachmentNotFoundException.class)
  protected ResponseEntity<ApiResponse> handleUserNotFoundException(AttachmentNotFoundException ex) {
    ApiResponse apiResponse = ApiResponse.builder()
        .message(ex.getMessage())
        .status(ex.getStatus())
        .timestamp(Instant.now())
        .build();
    log.error(AN_EXCEPTION_OCCURRED, apiResponse);
    return new ResponseEntity<>(apiResponse, ex.getStatus());
  }


}
