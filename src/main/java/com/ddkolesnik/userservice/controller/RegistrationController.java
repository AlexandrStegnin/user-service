package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.configuration.Location;
import com.ddkolesnik.userservice.model.dto.ChangePasswordDTO;
import com.ddkolesnik.userservice.model.dto.ChangePhoneDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.response.ApiResponse;
import com.ddkolesnik.userservice.service.SendMessageService;
import com.ddkolesnik.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
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
  SendMessageService sendMessageService;

  @GetMapping(path = Location.HOME_URL)
  public String redirect() {
    return "redirect:/login";
  }

  @GetMapping(path = Location.REGISTRATION_URL)
  public String registrationPage(Model model) {
    model.addAttribute("userDTO", UserDTO.builder().build());
    return "registration";
  }

  @ResponseBody
  @PostMapping(path = Location.CONFIRM_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse confirmUser(@RequestBody UserDTO dto) {
    dto = userService.confirm(dto);
    log.info("Пользователь успешно подтверждён {}", dto);
    userService.authenticateUser(dto.getPhone(), dto.getConfirmCode());
    sendMessageService.sendMessage(dto.getPhone());
    return ApiResponse.builder()
        .message("OK")
        .build();
  }

  @ResponseBody
  @PostMapping(path = Location.CREATE_USER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse create(@RequestBody UserDTO dto) {
    dto = userService.create(dto);
    log.info("Пользователь успешно зарегистрирован {}", dto);
    return ApiResponse.builder()
        .message(dto.getBitrixId().toString())
        .build();
  }

  @ResponseBody
  @PostMapping(path = Location.CHANGE_PASSWORD)
  public ApiResponse changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
    userService.changePassword(changePasswordDTO);
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .message("Пароль успешно изменён")
        .build();
  }

  @ResponseBody
  @PostMapping(path = Location.RESTORE_PASSWORD)
  public ApiResponse restorePassword(@RequestBody UserDTO dto) {
    userService.sendRestoreMessage(dto);
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .message("Сообщение успешно отправлено")
        .build();
  }

  @ResponseBody
  @PostMapping(path = Location.CONFIRM_OLD_PHONE)
  public ApiResponse confirmOldPhone(@RequestBody ChangePhoneDTO dto) {
    userService.sendConfirmOldPhoneMessage(dto);
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .message("Код успешно отправлен")
        .build();
  }

  @ResponseBody
  @PostMapping(path = Location.CHECK_CONFIRM_CODE)
  public ApiResponse checkConfirmCode(@RequestBody ChangePhoneDTO dto) {
    userService.checkConfirmCode(dto);
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .message("Код подтверждён")
        .build();
  }

  @ResponseBody
  @PostMapping(path = Location.CONFIRM_NEW_PHONE)
  public ApiResponse confirmNewPhone(@RequestBody ChangePhoneDTO dto) {
    userService.sendConfirmNewPhoneMessage(dto);
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .message("Код успешно отправлен")
        .build();
  }

  @ResponseBody
  @PostMapping(path = Location.CHANGE_PHONE)
  public ApiResponse changePhone(@RequestBody ChangePhoneDTO dto) {
    userService.changePhone(dto);
    return ApiResponse.builder()
        .status(HttpStatus.OK)
        .message("Телефон успешно изменён")
        .build();
  }

}
