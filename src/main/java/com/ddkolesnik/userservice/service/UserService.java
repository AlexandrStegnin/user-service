package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.enums.AppRole;
import com.ddkolesnik.userservice.model.bitrix.address.Address;
import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.bitrix.duplicate.DuplicateResult;
import com.ddkolesnik.userservice.model.bitrix.requisite.Requisite;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.domain.UserProfile;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Objects;
import javax.persistence.EntityNotFoundException;
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
  AuthenticationManager authenticationManager;
  AppUserService appUserService;
  PasswordEncoder passwordEncoder;

  public boolean confirmPhone(UserDTO dto) {
    log.info("Функционал в разработке. {}", dto);
    //TODO сделать подтверждение номера телефона
    return true;
  }

  public UserDTO update(UserDTO dto) {
    if (!confirmPhone(dto)) {
      throw new RuntimeException("При подтверждении номера телефона произошла ошибка");
    }
    DuplicateResult duplicate = bitrixContactService.findDuplicates(dto);
    if (responseEmpty(duplicate.getResult())) {
      if (createContact(dto)) {
        createAppUser(dto);
        return dto;
      } else {
        throw new RuntimeException("Пользователь не создан");
      }
    } else {
      updateContact(dto);
      return dto;
    }
  }

  private void createAppUser(UserDTO dto) {
    UserProfile profile = UserProfile.builder()
        .email(dto.getEmail())
        .build();
    AppUser appUser = AppUser.builder()
        .phone(dto.getPhone())
        .login(dto.getPhone())
        .password(passwordEncoder.encode(dto.getPassword()))
        .roleId(AppRole.INVESTOR.getId())
        .profile(profile)
        .build();
    profile.setUser(appUser);
    appUserService.create(appUser);
  }

  private boolean createContact(UserDTO dto) {
    Object create = bitrixContactService.createContact(dto);
    boolean created = false;
    if (create instanceof LinkedHashMap) {
      Integer id = (Integer) (((LinkedHashMap<?, ?>) create).get("result"));
      created = Objects.nonNull(id);
    }
    return created;
  }

  private boolean updateContact(UserDTO dto) {
    Contact contact = bitrixContactService.findFirstContact(dto);
    if (Objects.isNull(contact)) {
      throw new RuntimeException(String.format("Произошла ошибка. Контакт не найден %s", dto));
    }
    dto.setId(contact.getId());
    Object update = bitrixContactService.updateContact(dto);
    return extractResult(update);
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

  public void authenticateUser(String phone, String password) {
    UsernamePasswordAuthenticationToken authReq
        = new UsernamePasswordAuthenticationToken(phone, password);
    Authentication auth = authenticationManager.authenticate(authReq);
    SecurityContext sc = SecurityContextHolder.getContext();
    sc.setAuthentication(auth);
  }

}
