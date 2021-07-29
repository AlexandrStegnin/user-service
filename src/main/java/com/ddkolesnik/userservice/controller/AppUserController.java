package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.service.BitrixContactService;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

/**
 * @author Aleksandr Stegnin on 12.07.2021
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppUserController {

  BitrixContactService bitrixContactService;

  @GetMapping(path = "profile")
  public String profile(Model model) {
    UserDTO userDTO = bitrixContactService.getBitrixContact(SecurityUtils.getCurrentUserPhone());
    if (Objects.isNull(userDTO)) {
      return "redirect:/login";
    }
    model.addAttribute("userDTO", userDTO);
    model.addAttribute("login", userDTO.getPhone());
    return "profile";
  }

  @GetMapping(path = "login")
  public String login() {
    return "login";
  }

}
