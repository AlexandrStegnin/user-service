package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateResult;
import com.ddkolesnik.userservice.response.ApiResponse;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

/**
 * @author Aleksandr Stegnin on 11.07.2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

  static String MESSAGE_TEMPLATE = "Инвестор успешно %s %s";

  BitrixContactService bitrixContactService;

  public boolean confirmPhone(UserDTO dto) {
    log.info("Функционал в разработке. {}", dto);
    //TODO сделать подтверждение номера телефона
    return true;
  }

  public ApiResponse update(UserDTO dto) {
    if (!confirmPhone(dto)) {
      return ApiResponse.builder()
          .message("При подтверждении номера телефона произошла ошибка")
          .build();
    }
    DuplicateResult duplicate = bitrixContactService.findDuplicates(dto);
    if (responseEmpty(duplicate.getResult())) {
      return createContact(dto);
    } else {
      return updateContact(dto);
    }
  }

  private ApiResponse createContact(UserDTO dto) {
    Object create = bitrixContactService.createContact(dto);
    boolean created = false;
    if (create instanceof LinkedHashMap) {
      Integer id = (Integer) (((LinkedHashMap<?, ?>) create).get("result"));
      created = Objects.nonNull(id);
    }
    if (created) {
      return ApiResponse.builder()
          .message(String.format(MESSAGE_TEMPLATE, "создан", dto))
          .build();
    }
    return ApiResponse.builder()
        .message("Произошла ошибка. Контакт не обновлён.")
        .build();
  }

  private ApiResponse updateContact(UserDTO dto) {
    Contact contact = bitrixContactService.findFirstContact(dto);
    if (Objects.isNull(contact)) {
      return ApiResponse.builder()
          .message(String.format("Произошла ошибка. Контакт не найден %s", dto))
          .build();
    }
    dto.setId(contact.getId());
    Object update = bitrixContactService.updateContact(dto);
    boolean updated = extractResult(update);
    if (updated) {
      return ApiResponse.builder()
          .message(String.format(MESSAGE_TEMPLATE, "обновлён", dto))
          .build();
    }
    return ApiResponse.builder()
        .message("Произошла ошибка. Контакт не обновлён.")
        .build();
  }

  public ApiResponse updateAdditionalFields(UserDTO dto) {
    Contact contact = bitrixContactService.findFirstContact(dto);
    if (Objects.isNull(contact)) {
      String message = String.format("Контакт не найден %s", dto.getPhone());
      log.error(message);
      return ApiResponse.builder()
          .message(message)
          .build();
    }
    return null;
  }

  private boolean extractResult(Object update) {
    if (update instanceof LinkedHashMap) {
      return (Boolean) (((LinkedHashMap<?, ?>) update).get("result"));
    }
    return false;
  }

  private boolean responseEmpty(Object result) {
    boolean isEmpty = false;
    if (result instanceof ArrayList<?>) {
      isEmpty = ((ArrayList<?>) result).isEmpty();
    } else if (result instanceof LinkedHashMap<?, ?>) {
      isEmpty = ((LinkedHashMap<?, ?>) result).isEmpty();
    }
    return isEmpty;
  }

}
