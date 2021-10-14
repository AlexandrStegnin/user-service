package com.ddkolesnik.userservice.controller;

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
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

import static com.ddkolesnik.userservice.configuration.Location.*;

/**
 * @author Aleksandr Stegnin on 08.07.2021
 */
@Slf4j
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegistrationController {

  private static final String SUCCESSFUL_SEND_SMS = "КОД УСПЕШНО ОТПРАВЛЕН";

  UserService userService;
  SendMessageService sendMessageService;

  @GetMapping(path = HOME_URL)
  public String redirect() {
    return "redirect:" + LOGIN_URL;
  }

  @GetMapping(path = REGISTRATION_URL)
  public String registrationPage(Model model) {
    model.addAttribute("userDTO", UserDTO.builder().build());
    return "registration";
  }

  @ResponseBody
  @PostMapping(path = CONFIRM_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse confirmUser(@RequestBody UserDTO dto) {
    dto = userService.confirm(dto);
    log.info("Пользователь успешно подтверждён {}", dto);
    userService.authenticateUser(dto.getPhone(), dto.getConfirmCode());
    sendMessageService.sendMessage(dto.getPhone());
    return ApiResponse.build200Response("OK");
  }

  @ResponseBody
  @PostMapping(path = CREATE_USER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse create(@RequestBody UserDTO dto) {
    dto = userService.create(dto);
    log.info("Пользователь успешно зарегистрирован {}", dto);
    return ApiResponse.build200Response(dto.getBitrixId().toString());
  }

  @ResponseBody
  @PostMapping(path = CHANGE_PASSWORD)
  public ApiResponse changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
    userService.changePassword(changePasswordDTO);
    return ApiResponse.build200Response("Пароль успешно изменён");
  }

  @ResponseBody
  @PostMapping(path = RESTORE_PASSWORD)
  public ApiResponse restorePassword(@RequestBody UserDTO dto) {
    userService.sendRestoreMessage(dto);
    return ApiResponse.build200Response("Сообщение успешно отправлено");
  }

  @ResponseBody
  @PostMapping(path = CONFIRM_BY_EMAIL)
  public ApiResponse sendConfirmEmailMessage() {
    userService.sendConfirmEmailMessage();
    return ApiResponse.build200Response(SUCCESSFUL_SEND_SMS);
  }

  @ResponseBody
  @PostMapping(path = CONFIRM_OLD_PHONE)
  public ApiResponse confirmOldPhone(@RequestBody ChangePhoneDTO dto) {
    userService.sendConfirmOldPhoneMessage(dto);
    return ApiResponse.build200Response(SUCCESSFUL_SEND_SMS);
  }

  @ResponseBody
  @PostMapping(path = CHECK_CONFIRM_CODE)
  public ApiResponse checkConfirmCode(@RequestBody ChangePhoneDTO dto) {
    userService.checkConfirmCode(dto);
    return ApiResponse.build200Response("Код подтверждён");
  }

  @ResponseBody
  @PostMapping(path = CONFIRM_NEW_PHONE)
  public ApiResponse confirmNewPhone(@RequestBody ChangePhoneDTO dto) {
    userService.sendConfirmNewPhoneMessage(dto);
    return ApiResponse.build200Response(SUCCESSFUL_SEND_SMS);
  }

  @ResponseBody
  @PostMapping(path = CHANGE_PHONE)
  public ApiResponse changePhone(@RequestBody ChangePhoneDTO dto) {
    userService.changePhone(dto);
    return ApiResponse.build200Response("Необходимо войти используя новый номер телефона.");
  }

}
