package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.enums.OwnerType;
import com.ddkolesnik.userservice.model.domain.Account;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.repository.AppUserRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

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

  public AppUser findByPhone(String phone) {
    return appUserRepository.findByPhone(phone)
        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
  }

  public AppUser create(AppUser appUser) {
    AppUser user = appUserRepository.save(appUser);
    Account account = accountService.findByOwnerId(user.getId(), OwnerType.INVESTOR);
    if (account == null) {
      accountService.createAccount(user);
    }
    return user;
  }

}
