package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import static com.ddkolesnik.userservice.configuration.Location.*;

/**
 * @author Alexandr Stegnin
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppController {

  UserService userService;

  @PostMapping(path = UPDATE_USER)
  public String update(@ModelAttribute UserDTO dto) {
    userService.updateAdditionalFields(dto);
    return "redirect:" + PROFILE;
  }

  @GetMapping(path = HOME_URL)
  public String redirect() {
    return "redirect:" + LOGIN_URL;
  }

  @GetMapping(path = REGISTRATION_URL)
  public String registrationPage(Model model) {
    model.addAttribute("userDTO", UserDTO.builder().build());
    return "registration";
  }

}
