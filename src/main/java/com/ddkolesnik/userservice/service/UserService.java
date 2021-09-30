package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.mapper.UserMapper;
import com.ddkolesnik.userservice.model.bitrix.address.Address;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.ChangePasswordDTO;
import com.ddkolesnik.userservice.model.dto.ChangePhoneDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.response.ApiResponse;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
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
    ApiResponse response = bitrixContactService.createOrUpdateContact(dto);
    if (response.getStatus() == HttpStatus.CREATED) {
      createAppUser(dto);
    }
    bitrixContactService.sendConfirmMessage(dto);
    return dto;
  }

  private void createAppUser(UserDTO dto) {
    generatePassword(dto);
    AppUser appUser = userMapper.toEntity(dto);
    appUser.setPassword(passwordEncoder.encode(dto.getPassword()));
    userMapper.updateProfile(dto, appUser);
    appUserService.create(appUser);
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

  public void authenticateUser(String phone, String password) {
    UsernamePasswordAuthenticationToken authReq
        = new UsernamePasswordAuthenticationToken(phone, password);
    Authentication auth = authenticationManager.authenticate(authReq);
    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication(auth);
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

  public void sendConfirmOldPhoneMessage(ChangePhoneDTO dto) {
    UserDTO userDTO = getUserDTO(dto.getOldPhone());
    bitrixContactService.sendConfirmOldPhoneMessage(userDTO);
  }

  public void checkConfirmCode(ChangePhoneDTO dto) {
    Contact contact = bitrixContactService.findFirstContact(dto.getOldPhone());
    if (Objects.isNull(contact)) {
      throw new RuntimeException("Не найден контакт Б24");
    }
    if (Objects.isNull(contact.getConfirmCode()) || !Objects.equals(contact.getConfirmCode(), dto.getConfirmCode())) {
      throw new RuntimeException("Не указан или неверно указан код подтверждения");
    }
  }

  public void sendConfirmNewPhoneMessage(ChangePhoneDTO dto) {
    UserDTO userDTO = getUserDTO(dto.getOldPhone());
    bitrixContactService.sendConfirmNewPhoneMessage(userDTO);
  }

  public void changePhone(ChangePhoneDTO changePhoneDTO) {
    UserDTO userDTO = getUserDTO(changePhoneDTO.getOldPhone());
    Integer id = userDTO.getId();
    Contact contact = bitrixContactService.findFirstContact(userDTO);
    checkConfirmCode(contact, changePhoneDTO);
    bitrixContactService.updateContact(userDTO);
    AppUser user = userMapper.toEntity(userDTO);
    user.setId(Long.valueOf(id));
    user.setLogin(changePhoneDTO.getNewPhone());
    user.setPhone(changePhoneDTO.getNewPhone());
    appUserService.update(user);
    bitrixContactService.updateContactPhone(contact, changePhoneDTO, userDTO);
  }

  private UserDTO getUserDTO(String phone) {
    return userMapper.toDTO(appUserService.findByPhone(phone));
  }

  private void checkConfirmCode(Contact contact, ChangePhoneDTO dto) {
    if (Objects.isNull(contact.getConfirmCode()) || Objects.isNull(dto.getConfirmCode())) {
      throw new RuntimeException("Не указан код подтверждения");
    }
    if (!contact.getConfirmCode().equals(dto.getConfirmCode())) {
      throw new RuntimeException("Код подтверждения не верный");
    }
  }

}
