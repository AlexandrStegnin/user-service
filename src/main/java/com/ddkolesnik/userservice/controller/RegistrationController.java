package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.configuration.Location;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.response.ApiResponse;
import com.ddkolesnik.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Aleksandr Stegnin on 08.07.2021
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegistrationController {

  UserService userService;

  @GetMapping(path = Location.HOME_URL)
  public String registrationPage(Model model) {
    model.addAttribute("userDTO", UserDTO.builder().build());
    return Location.REGISTRATION_URL;
  }

  @ResponseBody
  @PostMapping(path = Location.CONFIRM_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse updateUser(@RequestBody UserDTO dto) {
    dto = userService.update(dto);
    log.info("Пользователь успешно зарегистрирован {}", dto);
    userService.authenticateUser(dto.getPhone(), dto.getPassword());
    return ApiResponse.builder()
        .message("OK")
        .build();
  }

}
