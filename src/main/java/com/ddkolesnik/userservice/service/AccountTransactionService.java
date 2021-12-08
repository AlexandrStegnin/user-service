package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.enums.OwnerType;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.AccountDTO;
import com.ddkolesnik.userservice.model.dto.BalanceDTO;
import com.ddkolesnik.userservice.repository.AccountTransactionRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountTransactionService {

  AccountTransactionRepository accountTransactionRepository;
  AppUserService appUserService;

  public BalanceDTO getBalanceByInvestorPhone(String investorLogin) {
    AppUser user = appUserService.findByPhone(investorLogin);
    AccountDTO accountDTO = accountTransactionRepository.fetchBalance(OwnerType.INVESTOR, user.getId());
    if (Objects.nonNull(accountDTO)) {
      return new BalanceDTO(accountDTO);
    }
    return new BalanceDTO();
  }

}
