package com.ddkolesnik.userservice.controller.view;


import com.ddkolesnik.userservice.model.dto.BalanceDTO;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.model.view.KindOnProject;
import com.ddkolesnik.userservice.service.AccountTransactionService;
import com.ddkolesnik.userservice.service.view.KindOnProjectService;
import com.ddkolesnik.userservice.utils.PhoneUtils;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;

import java.math.BigDecimal;

import static com.ddkolesnik.userservice.configuration.Location.INVESTMENTS;

/**
 * @author Alexandr Stegnin
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvestmentsController {

  AccountTransactionService accountTransactionService;
  KindOnProjectService kindOnProjectService;

  @GetMapping(path = INVESTMENTS)
  public String showInvestments(@ModelAttribute UserDTO user, ModelMap model) {
    String login = SecurityUtils.getCurrentUserPhone();
    model.addAttribute("investorLogin", login);
    model.addAttribute("login", PhoneUtils.getFormattedPhone(login));
    BalanceDTO balanceDTO = accountTransactionService.getBalanceByInvestorPhone(login);
    model.addAttribute("account-number", balanceDTO.getAccountNumber());
    model.addAttribute("balance", balanceDTO.getSum());
    model.addAttribute("invested", fetchInvestedCash(login));
    return "investments";
  }

  private BigDecimal fetchInvestedCash(String phone) {
    var kindOnProjects = kindOnProjectService.findByInvestorPhone(phone);
    return kindOnProjects.stream()
        .distinct()
        .map(KindOnProject::getGivenCash)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
