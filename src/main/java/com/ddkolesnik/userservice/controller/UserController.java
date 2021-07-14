package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.dto.UserDTO;
import com.ddkolesnik.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Aleksandr Stegnin on 14.07.2021
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class UserController {

  UserService userService;

  @PostMapping(path = "update")
  public String update(@ModelAttribute UserDTO dto) {
    userService.updateAdditionalFields(dto);
    return "redirect:/profile";
  }

}
