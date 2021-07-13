package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.AppUser;
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

  public AppUser findByPhone(String phone) {
    return appUserRepository.findByPhone(phone)
        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
  }

}
