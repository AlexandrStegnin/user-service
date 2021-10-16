package com.ddkolesnik.userservice.configuration;

import com.ddkolesnik.userservice.configuration.property.BitrixProperty;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Configuration
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class AppConfiguration {

  BitrixProperty bitrixProperty;

  @Bean
  public RestTemplate getRestTemplate() {
    RestTemplateBuilder builder = new RestTemplateBuilder();
    DefaultUriBuilderFactory builderFactory = new DefaultUriBuilderFactory(bitrixProperty.getApiUrl());
    builder.defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE);
    RestTemplate restTemplate = builder.build();
    restTemplate.setUriTemplateHandler(builderFactory);
    return restTemplate;
  }

  @Bean
  public ObjectMapper getObjectMapper() {
    ObjectMapper mapper = new ObjectMapper();
    mapper.configure(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES, false);
    mapper.registerModule(new JavaTimeModule());
    return mapper;
  }

}
