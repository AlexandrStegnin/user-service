package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.configuration.Location;
import com.ddkolesnik.userservice.model.UserDTO;
import com.ddkolesnik.userservice.response.ApiResponse;
import com.ddkolesnik.userservice.service.UserService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;

/**
 * @author Aleksandr Stegnin on 08.07.2021
 */
@Controller
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class RegistrationController {

  UserService userService;

  @GetMapping(path = "/")
  public String home() {
    return "redirect:" + Location.REGISTRATION_URL;
  }

  @GetMapping(path = Location.REGISTRATION_URL)
  public String registrationPage(Model model) {
    model.addAttribute("userDTO", new UserDTO());
    return Location.REGISTRATION_URL;
  }

  @PostMapping(path = Location.REGISTRATION_URL)
  public String register(@ModelAttribute UserDTO userDTO, Model model) {
    model.addAttribute("userDTO", userDTO);
    model.addAttribute("confirm", "НА ВАШ ТЕЛЕФОН ОТПРАВЛЕН КОД ПРОВЕРКИ. ВВЕДИТЕ ЕГО В ПОЛЕ НИЖЕ");
    return Location.REGISTRATION_URL;
  }

  @ResponseBody
  @PostMapping(path = Location.CONFIRM_URL, consumes = MediaType.APPLICATION_JSON_VALUE, produces = MediaType.APPLICATION_JSON_VALUE)
  public ApiResponse updateUser(@RequestBody UserDTO dto) {
    return userService.update(dto);
  }

}
