package com.ddkolesnik.userservice.response;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.HttpStatus;

import java.time.Instant;

/**
 * @author Aleksandr Stegnin on 11.07.2021
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ApiResponse {

  String message;
  HttpStatus status;
  Instant timestamp;

  public static ApiResponse build200Response(String message) {
    return new ApiResponse(message, HttpStatus.OK, Instant.now());
  }

  public static ApiResponse build201Response(String message) {
    return new ApiResponse(message, HttpStatus.CREATED, Instant.now());
  }

}
