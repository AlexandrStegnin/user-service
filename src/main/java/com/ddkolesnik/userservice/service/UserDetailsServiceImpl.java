package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.repository.AppUserRepository;
import com.ddkolesnik.userservice.utils.PhoneUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * @author Aleksandr Stegnin on 12.07.2021
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserDetailsServiceImpl implements UserDetailsService {

  AppUserRepository appUserRepository;

  @Override
  public UserDetails loadUserByUsername(String phone) throws UsernameNotFoundException {
    phone = PhoneUtils.clearPhone(phone);
    Optional<AppUser> optionalAppUser = appUserRepository.findByPhone(phone);
    return optionalAppUser
        .orElseThrow(() -> new UsernameNotFoundException("Пользователь не найден"));
  }

}
