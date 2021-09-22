package com.ddkolesnik.userservice.configuration;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.AccessLevel;
import lombok.experimental.FieldDefaults;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Configuration
@FieldDefaults(level = AccessLevel.PRIVATE)
@PropertySource(value = "classpath:private.properties")
public class AppConfiguration {

  @Value("${bitrix.default.url}")
  String BITRIX_API_BASE_URL;

  @Value("${bitrix.webhook.user.id}")
  String BITRIX_WEBHOOK_USER_ID;

  @Value("${bitrix.access.key}")
  String BITRIX_ACCESS_KEY;

  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplateBuilder builder = new RestTemplateBuilder();
    DefaultUriBuilderFactory builderFactory = new DefaultUriBuilderFactory(getBaseUrl());
    builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    RestTemplate restTemplate = builder.build();
    restTemplate.setUriTemplateHandler(builderFactory);
    return restTemplate;
  }

  @Bean
  public ObjectMapper getObjectMapper() {
    return new ObjectMapper();
  }

  private String getBaseUrl() {
    return BITRIX_API_BASE_URL + BITRIX_WEBHOOK_USER_ID + BITRIX_ACCESS_KEY;
  }

}
