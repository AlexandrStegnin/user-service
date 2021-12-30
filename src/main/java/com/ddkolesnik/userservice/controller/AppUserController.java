package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.model.view.InvestorProfit;
import com.ddkolesnik.userservice.model.view.KindOnProject;
import com.ddkolesnik.userservice.service.AppUserService;
import com.ddkolesnik.userservice.service.view.InvestorProfitService;
import com.ddkolesnik.userservice.service.view.KindOnProjectService;
import com.ddkolesnik.userservice.utils.PhoneUtils;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.math.BigDecimal;

import static com.ddkolesnik.userservice.configuration.Location.*;

/**
 * @author Aleksandr Stegnin on 12.07.2021
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppUserController {

  private static final String LOGIN = "login";
  private static final String REDIRECT = "redirect:";
  private static final String USER = "user";

  AppUserService appUserService;
  KindOnProjectService kindOnProjectService;
  InvestorProfitService investorProfitService;

  @GetMapping(path = PROFILE)
  public String profile(Model model) {
    UserDTO dto = appUserService.findUser(SecurityUtils.getCurrentUserPhone());
    if (dto.isAccredited()) {
      return REDIRECT + PROFILE_ACCREDITED;
    }
    addAttributes(model, dto);
    return "profile";
  }

  @GetMapping(path = PROFILE_ACCREDITED)
  public String profileAccredited(Model model) {
    UserDTO dto = appUserService.findUser(SecurityUtils.getCurrentUserPhone());
    if (!dto.isAccredited()) {
      return REDIRECT + PROFILE;
    }
    addAttributes(model, dto);
    return "accredited";
  }

  @GetMapping(path = BANK_REQUISITES)
  public String bankRequisites(Model model) {
    UserDTO dto = appUserService.findUser(SecurityUtils.getCurrentUserPhone());
    addAttributes(model, dto);
    return "bank-requisites";
  }

  @GetMapping(path = LOGIN_URL)
  public String login() {
    return LOGIN;
  }

  private void addAttributes(Model model, UserDTO dto) {
    var phone = PhoneUtils.getFormattedPhone(dto.getPhone());
    model.addAttribute(USER, dto);
    model.addAttribute(LOGIN, phone);
    model.addAttribute("invested", fetchInvestedCash(phone));
    model.addAttribute("profit", fetchInvestorProfit(phone));
  }

  private BigDecimal fetchInvestedCash(String phone) {
    return kindOnProjectService.findByInvestorPhone(phone).stream()
        .distinct()
        .map(KindOnProject::getGivenCash)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

  private BigDecimal fetchInvestorProfit(String phone) {
    var investorProfit = investorProfitService.findByPhone(phone);
    return investorProfit.stream()
        .distinct()
        .map(InvestorProfit::getProfit)
        .reduce(BigDecimal.ZERO, BigDecimal::add);
  }

}
