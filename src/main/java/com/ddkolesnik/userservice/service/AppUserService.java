package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.configuration.exception.UserNotFoundException;
import com.ddkolesnik.userservice.enums.OwnerType;
import com.ddkolesnik.userservice.mapper.BalanceMapper;
import com.ddkolesnik.userservice.mapper.UserMapper;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.AccountDTO;
import com.ddkolesnik.userservice.model.dto.BalanceDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.repository.AccountTransactionRepository;
import com.ddkolesnik.userservice.repository.AppUserRepository;
import com.ddkolesnik.userservice.service.bitrix.BitrixContactService;
import com.ddkolesnik.userservice.service.bitrix.RequisiteService;
import com.ddkolesnik.userservice.utils.PhoneUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Aleksandr Stegnin on 13.07.2021
 */
@Slf4j
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppUserService {

  AccountTransactionRepository accountTransactionRepository;
  BitrixContactService bitrixContactService;
  AppUserRepository appUserRepository;
  RequisiteService requisiteService;
  AccountService accountService;
  BalanceMapper balanceMapper;
  PasswordEncoder encoder;
  UserMapper userMapper;

  public AppUser findByPhone(String phone) {
    var cleanPhone = PhoneUtils.cleanPhone(phone);
    return appUserRepository.findByPhone(cleanPhone)
        .orElseThrow(() -> new UserNotFoundException("Пользователь не найден"));
  }

  public void create(AppUser appUser) {
    var user = appUserRepository.save(appUser);
    var account = accountService.findByOwnerId(user.getId(), OwnerType.INVESTOR);
    if (Objects.isNull(account)) {
      accountService.createAccount(user);
    }
  }

  public void updatePassword(UserDTO dto) {
    var user = findByPhone(dto.getPhone());
    user.setPassword(dto.getPassword());
    appUserRepository.save(user);
  }

  public void updatePassword(String phone, String rawPassword) {
    var user = findByPhone(phone);
    user.setPassword(encoder.encode(rawPassword));
    appUserRepository.save(user);
  }

  public UserDTO findUser(String phone) {
    var contact = bitrixContactService.getContact(phone);
    var dto = userMapper.toDTO(contact);
    var requisite = requisiteService.findRequisite(contact.getId().toString());
    userMapper.updatePassport(requisite, dto);
    fetchBalance(dto);
    return dto;
  }

  public void update(AppUser user) {
    appUserRepository.save(user);
    updateAccount(user);
  }

  private void updateAccount(AppUser user) {
    var account = accountService.findByOwnerId(user.getId(), OwnerType.INVESTOR);
    if (Objects.nonNull(account)) {
      account.setAccountNumber(user.getPhone());
      accountService.update(account);
    }
  }

  private void fetchBalance(UserDTO dto) {
    AccountDTO accountDTO = accountTransactionRepository.fetchBalance(OwnerType.INVESTOR, Long.valueOf(dto.getId()));
    BalanceDTO balance = balanceMapper.toBalance(accountDTO);
    if (Objects.isNull(balance)) {
      balance = BalanceDTO.builder().build();
    }
    dto.setBalance(balance);
  }

}
