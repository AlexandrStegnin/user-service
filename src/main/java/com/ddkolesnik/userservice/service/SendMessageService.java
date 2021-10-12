package com.ddkolesnik.userservice.service;

import com.ddkolesnik.userservice.model.domain.AppUser;
import com.ddkolesnik.userservice.model.dto.AppUserDTO;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */

@Service
@RequiredArgsConstructor
@PropertySource(value = "classpath:private.properties")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class SendMessageService {

  @Value("${spring.mail.app.token}")
  String mailAppToken;

  @Value("${spring.mail.app.base.url}")
  String mailAppBaseUrl;

  @Value("${spring.mail.app.welcome.path}")
  String mailAppWelcomePath;

  @Value("${spring.mail.app.send.confirm.path}")
  String mailAppSendConfirmPath;

  final AppUserService appUserService;

  public void sendMessage(String login) {
    WebClient webClient = getWebClient();
    if (Objects.isNull(webClient)) {
      return;
    }
    AppUser user = appUserService.findByPhone(login);
    if (user.getProfile().getEmail() == null) {
      return;
    }
    AppUserDTO userDTO = new AppUserDTO();
    userDTO.setEmail(user.getProfile().getEmail());
    webClient.post()
        .uri(mailAppToken + mailAppWelcomePath)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(userDTO), AppUserDTO.class)
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

  public void sendConfirmEmailMessage(AppUserDTO dto) {
    WebClient webClient = getWebClient();
    if (Objects.isNull(webClient)) {
      return;
    }
    webClient.post()
        .uri(mailAppToken + mailAppSendConfirmPath)
        .contentType(MediaType.APPLICATION_JSON)
        .body(Mono.just(dto), AppUserDTO.class)
        .retrieve()
        .bodyToMono(Void.class)
        .subscribe();
  }

  private WebClient getWebClient() {
    if (mailAppToken == null || mailAppToken.isEmpty()) {
      return null;
    }
    return WebClient.create(mailAppBaseUrl);
  }

}
