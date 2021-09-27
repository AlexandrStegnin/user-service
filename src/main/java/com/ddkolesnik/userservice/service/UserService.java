package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.mapper.UserMapper;
import com.ddkolesnik.userservice.model.bitrix.address.Address;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateResult;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.ChangePasswordDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import java.util.UUID;

/**
 * @author Aleksandr Stegnin on 11.07.2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserService {

  BitrixContactService bitrixContactService;
  AuthenticationManager authenticationManager;
  AppUserService appUserService;
  PasswordEncoder passwordEncoder;
  UserMapper userMapper;

  public UserDTO confirm(UserDTO dto) {
    Contact contact = bitrixContactService.getById(dto.getBitrixId().toString());
    if (Objects.isNull(contact)) {
      throw new RuntimeException("Ошибка. Контакт не найден");
    }
    if (!dto.getConfirmCode().equalsIgnoreCase(contact.getConfirmCode())) {
      throw new RuntimeException("Ошибка. Контакт не подтверждён");
    }
    appUserService.updatePassword(dto.getPhone(), dto.getConfirmCode());
    return dto;
  }

  public UserDTO create(UserDTO dto) {
    DuplicateResult duplicate = bitrixContactService.findDuplicates(dto);
    if (responseEmpty(duplicate.getResult())) {
      Integer contactId = createContact(dto);
      if (Objects.isNull(contactId)) {
        throw new RuntimeException("Пользователь не создан");
      }
      dto.setBitrixId(contactId);
      createAppUser(dto);
    } else {
      updateContact(dto);
    }
    sendConfirmMessage(dto);
    return dto;
  }

  private void createAppUser(UserDTO dto) {
    generatePassword(dto);
    AppUser appUser = userMapper.toEntity(dto);
    userMapper.updateProfile(dto, appUser);
    appUserService.create(appUser);
  }

  private Integer createContact(UserDTO dto) {
    Object create = bitrixContactService.createContact(dto);
    if (create instanceof LinkedHashMap) {
      return (Integer) (((LinkedHashMap<?, ?>) create).get("result"));
    }
    return null;
  }

  private void updateContact(UserDTO dto) {
    Contact contact = bitrixContactService.findFirstContact(dto);
    if (Objects.isNull(contact)) {
      throw new RuntimeException(String.format("Произошла ошибка. Контакт не найден %s", dto));
    }
    dto.setId(contact.getId());
    dto.setBitrixId(contact.getId());
    bitrixContactService.updateContact(dto);
  }

  public void updateAdditionalFields(UserDTO dto) {
    Contact contact = bitrixContactService.findFirstContact(dto);
    if (Objects.isNull(contact)) {
      String message = String.format("Контакт не найден %s", dto.getPhone());
      log.error(message);
      throw new EntityNotFoundException(message);
    }
    dto.setId(contact.getId());
    Requisite requisite = bitrixContactService.findRequisite(dto);
    if (Objects.isNull(requisite)) {
      bitrixContactService.createRequisite(dto);
      requisite = bitrixContactService.findRequisite(dto);
    } else {
      bitrixContactService.updateRequisite(requisite, dto);
    }
    Address address = bitrixContactService.findAddress(requisite);
    if (Objects.isNull(address)) {
      bitrixContactService.createAddress(dto);
    } else {
      bitrixContactService.updateAddress(dto);
    }
    bitrixContactService.updateContact(dto);
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

  public void authenticateUser(String phone, String password) {
    UsernamePasswordAuthenticationToken authReq
        = new UsernamePasswordAuthenticationToken(phone, password);
    Authentication auth = authenticationManager.authenticate(authReq);
    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication(auth);
  }

  public void sendConfirmMessage(UserDTO dto) {
    bitrixContactService.sendConfirmMessage(dto);
  }

  public void sendRestoreMessage(UserDTO dto) {
    dto = findUserByPhone(dto);
    bitrixContactService.sendRestoreMessage(dto);
    Contact contact = bitrixContactService.findFirstContact(dto);
    if (Objects.isNull(contact.getRawPassword()) || contact.getRawPassword().isBlank()) {
      throw new RuntimeException("Не удалось обновить контакт. Код из Б24 не получен");
    }
    dto.setPassword(passwordEncoder.encode(contact.getRawPassword()));
    appUserService.updatePassword(dto);
    bitrixContactService.clearContactPassword(dto);
  }

  private UserDTO findUserByPhone(UserDTO dto) {
    AppUser user = appUserService.findByPhone(dto.getPhone());
    return userMapper.toDTO(user);
  }

  private void generatePassword(UserDTO dto) {
    dto.setPassword(UUID.randomUUID().toString().substring(0, 8));
  }

  public void changePassword(ChangePasswordDTO changePasswordDTO) {
    AppUser user = appUserService.findByPhone(changePasswordDTO.getPhone());
    if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
      throw new RuntimeException("Имя пользователя или пароль указан не верно");
    }
    user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
    appUserService.update(user);
  }
}
