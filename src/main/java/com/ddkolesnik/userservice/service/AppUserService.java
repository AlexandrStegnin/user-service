package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.enums.OwnerType;
import com.ddkolesnik.userservice.model.domain.Account;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.repository.AppUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Objects;

/**
 * @author Aleksandr Stegnin on 13.07.2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppUserService {

  AppUserRepository appUserRepository;
  AccountService accountService;

  public AppUser findByLogin(String login) {
    return appUserRepository.findByPhone(login)
        .orElseThrow(() -> new EntityNotFoundException("Пользователь не найден"));
  }

  public void create(AppUser appUser) {
    AppUser user = appUserRepository.save(appUser);
    Account account = accountService.findByOwnerId(user.getId(), OwnerType.INVESTOR);
    if (Objects.isNull(account)) {
      accountService.createAccount(user);
    }
  }

  public void updatePassword(UserDTO dto) {
    AppUser user = findByLogin(dto.getPhone());
    user.setPassword(dto.getPassword());
    appUserRepository.save(user);
  }

}
