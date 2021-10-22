package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.service.AppUserService;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

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

  AppUserService appUserService;

  @GetMapping(path = PROFILE)
  public String profile(Model model) {
    UserDTO userDTO = appUserService.findUser(SecurityUtils.getCurrentUserPhone());
    if (userDTO.isAccredited()) {
      return REDIRECT + PROFILE_ACCREDITED;
    }
    model.addAttribute("user", userDTO);
    model.addAttribute(LOGIN, userDTO.getFormattedPhone());
    model.addAttribute("fullName", userDTO.getFullName());
    return "profile";
  }

  @GetMapping(path = PROFILE_ACCREDITED)
  public String profileAccredited(Model model) {
    UserDTO userDTO = appUserService.findUser(SecurityUtils.getCurrentUserPhone());
    model.addAttribute("user", userDTO);
    model.addAttribute(LOGIN, userDTO.getFormattedPhone());
    return "accredited";
  }

  @GetMapping(path = BANK_REQUISITES)
  public String bankRequisites(Model model) {
    UserDTO userDTO = appUserService.findUser(SecurityUtils.getCurrentUserPhone());
    model.addAttribute("user", userDTO);
    model.addAttribute(LOGIN, userDTO.getFormattedPhone());
    return "bank-requisites";
  }

  @GetMapping(path = LOGIN_URL)
  public String login() {
    return LOGIN;
  }

  @GetMapping(path = LOGOUT_URL)
  public String logout() {
    SecurityContextHolder.clearContext();
    return LOGIN;
  }
}
