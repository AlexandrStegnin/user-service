package com.ddkolesnik.userservice.controller;

import com.ddkolesnik.userservice.model.UserDTO;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

/**
 * @author Aleksandr Stegnin on 08.07.2021
 */
@Controller
public class RegistrationController {

  @GetMapping(path = "registration")
  public String registrationPage(Model model) {
    model.addAttribute("userDTO", new UserDTO());
    return "registration";
  }

  @PostMapping(path = "registration")
  public String register(@ModelAttribute UserDTO userDTO, Model model) {
    model.addAttribute("userDTO", userDTO);
    model.addAttribute("confirm", "НА ВАШ ТЕЛЕФОН ОТПРАВЛЕН КОД ПРОВЕРКИ. ВВЕДИТЕ ЕГО В ПОЛЕ НИЖЕ");
    return "registration";
  }

}
