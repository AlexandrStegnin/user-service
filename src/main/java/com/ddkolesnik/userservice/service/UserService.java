package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.configuration.exception.BitrixException;
import com.ddkolesnik.userservice.mapper.UserMapper;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.dto.ChangePasswordDTO;
import com.ddkolesnik.userservice.model.dto.ChangePhoneDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.service.bitrix.AddressService;
import com.ddkolesnik.userservice.service.bitrix.BitrixContactService;
import com.ddkolesnik.userservice.service.bitrix.BusinessProcessService;
import com.ddkolesnik.userservice.service.bitrix.RequisiteService;
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

  BusinessProcessService businessProcessService;
  AuthenticationManager authenticationManager;
  BitrixContactService bitrixContactService;
  RequisiteService requisiteService;
  PasswordEncoder passwordEncoder;
  AddressService addressService;
  AppUserService appUserService;
  UserMapper userMapper;

  public UserDTO confirm(UserDTO dto) {
    var contact = bitrixContactService.getById(dto.getBitrixId().toString());
    if (Objects.isNull(contact)) {
      throw BitrixException.builder()
          .status(HttpStatus.NOT_FOUND)
          .message("Ошибка. Контакт не найден")
          .build();
    }
    if (!dto.getConfirmCode().equalsIgnoreCase(contact.getConfirmCode())) {
      throw BitrixException.builder()
          .status(HttpStatus.PRECONDITION_FAILED)
          .message("Ошибка. Контакт не подтверждён")
          .build();
    }
    appUserService.updatePassword(dto.getPhone(), dto.getConfirmCode());
    return dto;
  }

  public UserDTO create(UserDTO dto) {
    var response = bitrixContactService.createOrUpdateContact(dto);
    if (response.getStatus() == HttpStatus.CREATED) {
      createAppUser(dto);
    }
    businessProcessService.sendConfirmMessage(dto);
    return dto;
  }

  private void createAppUser(UserDTO dto) {
    generatePassword(dto);
    var appUser = userMapper.toEntity(dto);
    appUser.setPassword(passwordEncoder.encode(dto.getPassword()));
    userMapper.updateProfile(dto, appUser);
    appUserService.create(appUser);
  }

  public void updateAdditionalFields(UserDTO dto) {
    var contact = bitrixContactService.findFirstContact(dto);
    if (Objects.isNull(contact)) {
      String message = String.format("Контакт не найден %s", dto.getPhone());
      log.error(message);
      throw new EntityNotFoundException(message);
    }
    dto.setId(contact.getId());
    var requisite = requisiteService.findRequisite(dto);
    if (Objects.isNull(requisite)) {
      requisiteService.createRequisite(dto);
      requisite = requisiteService.findRequisite(dto);
    } else {
      requisiteService.updateRequisite(requisite, dto);
    }
    var address = addressService.findAddress(requisite);
    if (Objects.isNull(address)) {
      addressService.createAddress(dto);
    } else {
      addressService.updateAddress(dto);
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
    businessProcessService.sendRestoreMessage(dto);
    var contact = bitrixContactService.findFirstContact(dto);
    if (Objects.isNull(contact.getRawPassword()) || contact.getRawPassword().isBlank()) {
      throw BitrixException.builder()
          .status(HttpStatus.PRECONDITION_FAILED)
          .message("Не удалось обновить контакт. Код из Б24 не получен")
          .build();
    }
    dto.setPassword(passwordEncoder.encode(contact.getRawPassword()));
    appUserService.updatePassword(dto);
  }

  private UserDTO findUserByPhone(UserDTO dto) {
    var user = appUserService.findByPhone(dto.getPhone());
    return userMapper.toDTO(user);
  }

  private void generatePassword(UserDTO dto) {
    dto.setPassword(UUID.randomUUID().toString().substring(0, 8));
  }

  public void changePassword(ChangePasswordDTO changePasswordDTO) {
    var user = appUserService.findByPhone(changePasswordDTO.getPhone());
    if (!passwordEncoder.matches(changePasswordDTO.getOldPassword(), user.getPassword())) {
      throw BitrixException.builder()
          .status(HttpStatus.BAD_REQUEST)
          .message("Имя пользователя или пароль указан не верно")
          .build();
    }
    user.setPassword(passwordEncoder.encode(changePasswordDTO.getNewPassword()));
    appUserService.update(user);
  }

  public void sendConfirmOldPhoneMessage(ChangePhoneDTO dto) {
    var userDTO = getUserDTO(dto.getOldPhone());
    businessProcessService.sendConfirmOldPhoneMessage(userDTO);
  }

  public void checkConfirmCode(ChangePhoneDTO dto) {
    var contact = bitrixContactService.findFirstContact(dto.getOldPhone());
    if (Objects.isNull(contact)) {
      throw BitrixException.builder()
          .status(HttpStatus.NOT_FOUND)
          .message("Не найден контакт Б24")
          .build();
    }
    if (Objects.isNull(contact.getConfirmCode()) || !Objects.equals(contact.getConfirmCode(), dto.getConfirmCode())) {
      throw BitrixException.builder()
          .status(HttpStatus.PRECONDITION_FAILED)
          .message("Не указан или неверно указан код подтверждения")
          .build();
    }
  }

  public void sendConfirmNewPhoneMessage(ChangePhoneDTO dto) {
    var userDTO = getUserDTO(dto.getOldPhone());
    businessProcessService.sendConfirmNewPhoneMessage(userDTO);
  }

  public void changePhone(ChangePhoneDTO changePhoneDTO) {
    var userDTO = getUserDTO(changePhoneDTO.getOldPhone());
    var id = userDTO.getId();
    var contact = bitrixContactService.findFirstContact(userDTO);
    checkConfirmCode(contact, changePhoneDTO);
    bitrixContactService.updateContact(userDTO);
    var user = userMapper.toEntity(userDTO);
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
      throw BitrixException.builder()
          .status(HttpStatus.BAD_REQUEST)
          .message("Не указан код подтверждения")
          .build();
    }
    if (!contact.getConfirmCode().equals(dto.getConfirmCode())) {
      throw BitrixException.builder()
          .status(HttpStatus.PRECONDITION_FAILED)
          .message("Код подтверждения не верный")
          .build();
    }
  }

}
