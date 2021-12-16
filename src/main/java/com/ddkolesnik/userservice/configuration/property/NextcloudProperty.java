package com.ddkolesnik.userservice.configuration.property;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;

/**
 * @author Alexandr Stegnin
 */
@Data
@Validated
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@PropertySource("classpath:application-prod.properties")
@ConfigurationProperties(prefix = "nextcloud")
public class NextcloudProperty {

  @NotBlank
  String serviceUrl;

  @NotBlank
  String login;

  @NotBlank
  String password;

  @NotBlank
  String folder;

}
