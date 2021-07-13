package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.AppUser;
import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.service.AppUserService;
import java.util.Objects;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
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

  @GetMapping(path = "profile")
  public String profile(Model model) {
    String phone = getAuthenticatedUserPhone();
    AppUser user = appUserService.findByPhone(phone);
    model.addAttribute("userDTO", convert(user));
    return "profile";
  }

  @GetMapping(path = "login")
  public String login() {
    return "login";
  }

  private String getAuthenticatedUserPhone() {
    final Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    if (Objects.nonNull(authentication)) {
      return authentication.getName();
    }
    return null;
  }

  private UserDTO convert(AppUser entity) {
    return UserDTO.builder()
        .name(entity.getProfile().getName())
        .lastName(entity.getProfile().getLastName())
        .secondName(entity.getProfile().getSecondName())
        .email(entity.getProfile().getEmail())
        .phone(entity.getPhone())
        .build();
  }

}
