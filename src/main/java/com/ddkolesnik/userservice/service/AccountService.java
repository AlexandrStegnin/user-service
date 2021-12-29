package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.enums.OwnerType;
import com.ddkolesnik.userservice.model.domain.Account;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.repository.AccountRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * @author Alexandr Stegnin
 */

@Service
@Transactional
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountService {

  AccountRepository accountRepository;

  public void createAccount(AppUser user) {
    Account account = new Account();
    account.setAccountNumber(generateAccountNumber(user));
    account.setOwnerId(user.getId());
    account.setOwnerType(OwnerType.INVESTOR);
    account.setOwnerName(user.getLogin());
    accountRepository.save(account);
  }

  private String generateAccountNumber(AppUser user) {
    return user.getAccountNumber();
  }

  public void update(Account account) {
    accountRepository.save(account);
  }

  public Account findByInvestorId(Long ownerId) {
    return accountRepository.findByOwnerIdAndOwnerType(ownerId, OwnerType.INVESTOR);
  }

}
