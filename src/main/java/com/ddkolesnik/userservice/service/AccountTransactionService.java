package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.enums.OwnerType;
import com.ddkolesnik.userservice.model.domain.AccountTransaction;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.AccountDTO;
import com.ddkolesnik.userservice.model.dto.AccountTransactionDTO;
import com.ddkolesnik.userservice.model.dto.BalanceDTO;
import com.ddkolesnik.userservice.repository.AccountTransactionRepository;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * @author Alexandr Stegnin
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AccountTransactionService {

  AccountTransactionRepository accountTransactionRepository;
  AppUserService appUserService;
  AccountService accountService;

  public BalanceDTO getBalanceByInvestorPhone(String investorLogin) {
    AppUser user = appUserService.findByPhone(investorLogin);
    AccountDTO accountDTO = accountTransactionRepository.fetchBalance(OwnerType.INVESTOR, user.getId());
    if (Objects.nonNull(accountDTO)) {
      return new BalanceDTO(accountDTO);
    }
    return new BalanceDTO();
  }

  public List<AccountTransactionDTO> getDetails() {
    var user = appUserService.findByPhone(SecurityUtils.getCurrentUserPhone());
    List<AccountTransaction> transactions = accountTransactionRepository.findByInvestorId(user.getId());

    return transactions
        .stream()
        .map(AccountTransactionDTO::new)
        .collect(Collectors.toList());
  }

}
