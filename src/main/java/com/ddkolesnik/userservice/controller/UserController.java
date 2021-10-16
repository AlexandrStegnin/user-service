package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.dto.ChangePasswordDTO;
import com.ddkolesnik.userservice.model.dto.ChangePhoneDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.response.ApiResponse;
import com.ddkolesnik.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import static com.ddkolesnik.userservice.configuration.Location.*;

/**
 * @author Aleksandr Stegnin on 08.07.2021
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {

  private static final String SUCCESSFUL_SEND_SMS = "КОД УСПЕШНО ОТПРАВЛЕН";

  UserService userService;

  @PostMapping(path = CONFIRM_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse confirmUser(@RequestBody UserDTO dto) {
    userService.confirm(dto);
    return ApiResponse.build200Response("OK");
  }

  @PostMapping(path = CREATE_USER, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse create(@RequestBody UserDTO dto) {
    dto = userService.create(dto);
    return ApiResponse.build200Response(dto.getBitrixId().toString());
  }

  @PostMapping(path = CHANGE_PASSWORD)
  public ApiResponse changePassword(@RequestBody ChangePasswordDTO changePasswordDTO) {
    userService.changePassword(changePasswordDTO);
    return ApiResponse.build200Response("Пароль успешно изменён");
  }

  @PostMapping(path = RESTORE_PASSWORD)
  public ApiResponse restorePassword(@RequestBody UserDTO dto) {
    userService.sendRestoreMessage(dto);
    return ApiResponse.build200Response("Сообщение успешно отправлено");
  }

  @PostMapping(path = CONFIRM_BY_EMAIL)
  public ApiResponse sendConfirmEmailMessage() {
    userService.sendConfirmEmailMessage();
    return ApiResponse.build200Response(SUCCESSFUL_SEND_SMS);
  }

  @PostMapping(path = CONFIRM_OLD_PHONE)
  public ApiResponse confirmOldPhone(@RequestBody ChangePhoneDTO dto) {
    userService.sendConfirmOldPhoneMessage(dto);
    return ApiResponse.build200Response(SUCCESSFUL_SEND_SMS);
  }

  @PostMapping(path = CHECK_CONFIRM_CODE)
  public ApiResponse checkConfirmCode(@RequestBody ChangePhoneDTO dto) {
    userService.checkConfirmCode(dto);
    return ApiResponse.build200Response("Код подтверждён");
  }

  @PostMapping(path = CONFIRM_NEW_PHONE)
  public ApiResponse confirmNewPhone(@RequestBody ChangePhoneDTO dto) {
    userService.sendConfirmNewPhoneMessage(dto);
    return ApiResponse.build200Response(SUCCESSFUL_SEND_SMS);
  }

  @PostMapping(path = CHANGE_PHONE)
  public ApiResponse changePhone(@RequestBody ChangePhoneDTO dto) {
    userService.changePhone(dto);
    return ApiResponse.build200Response("Необходимо войти используя новый номер телефона.");
  }

  @PostMapping(path = RETRY_SEND_CONFIRM_MESSAGE)
  public ApiResponse retrySendConfirmMessage(@RequestBody UserDTO userDTO) {
    userService.retrySendConfirmMessage(userDTO);
    return ApiResponse.build200Response("Сообщение успешно отправлено");
  }

}
