package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.bitrix.contact.Contact;
import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.service.AppUserService;
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

  AppUserService appUserService;
  BitrixContactService bitrixContactService;

  @GetMapping(path = "profile")
  public String profile(Model model) {
    String phone = SecurityUtils.getCurrentUserPhone();
    AppUser user = appUserService.findByPhone(phone);
    Contact contact = bitrixContactService.findFirstContact(phone);
    model.addAttribute("userDTO", convert(user, contact));
    model.addAttribute("login", user.getLogin());
    return "profile";
  }

  @GetMapping(path = "login")
  public String login() {
    return "login";
  }

  private UserDTO convert(AppUser entity, Contact contact) {
    if (Objects.isNull(contact)) {
      return UserDTO.builder()
          .name(entity.getProfile().getName())
          .lastName(entity.getProfile().getLastName())
          .secondName(entity.getProfile().getSecondName())
          .email(entity.getProfile().getEmail())
          .phone(entity.getPhone())
          .build();
    }
    return UserDTO.builder()
        .name(contact.getName())
        .lastName(contact.getLastName())
        .secondName(contact.getSecondName())
        .email(entity.getProfile().getEmail())
        .phone(entity.getPhone())
        .build();
  }

}
