package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.ConfirmPhoneDTO;
import com.ddkolesnik.userservice.model.UserDTO;
import com.ddkolesnik.userservice.model.bitrix.Contact;
import com.ddkolesnik.userservice.model.bitrix.DuplicateResult;
import com.ddkolesnik.userservice.response.ApiResponse;
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

  public void confirmPhone(ConfirmPhoneDTO dto) {
    log.info("Функционал в разработке. {}", dto);
  }

  public ApiResponse update(UserDTO dto) {
    DuplicateResult duplicate = bitrixContactService.findDuplicates(dto);
    if (Objects.isNull(duplicate)) {
      return createContact(dto);
    } else {
      return updateContact(dto);
    }
  }

  private ApiResponse createContact(UserDTO dto) {
    Object create = bitrixContactService.createContact(dto);
    boolean created = extractResult(create);
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
    Contact contact = bitrixContactService.findContacts(dto);
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

  private boolean extractResult(Object update) {
    if (update instanceof LinkedHashMap) {
      return (Boolean) (((LinkedHashMap<?, ?>) update).get("result"));
    }
    return false;
  }

}
