package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.AppUserDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

/**
 * @author Alexandr Stegnin
 */

@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class SendMessageService {

  @Value("${spring.mail.app.token}")
  String MAIL_APP_TOKEN;

  @Value("${spring.mail.app.base.url}")
  String MAIL_APP_BASE_URL;

  @Value("${spring.mail.app.welcome.path}")
  String MAIL_APP_WELCOME_PATH;

  AppUserService appUserService;

  public void sendMessage(String login) {
    if (MAIL_APP_TOKEN == null || MAIL_APP_TOKEN.isEmpty()) {
      return;
    }
    WebClient webClient = WebClient.create(MAIL_APP_BASE_URL);
    AppUser user = appUserService.findByLogin(login);
    if (user.getProfile().getEmail() == null) {
      return;
    }
    AppUserDTO userDTO = new AppUserDTO();
    userDTO.setEmail(user.getProfile().getEmail());
    webClient.post()
        .uri(MAIL_APP_TOKEN + MAIL_APP_WELCOME_PATH)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(userDTO), AppUserDTO.class)
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

}
