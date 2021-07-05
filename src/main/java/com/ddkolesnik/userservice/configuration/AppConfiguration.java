package com.ddkolesnik.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author Aleksandr Stegnin on 05.07.2021
 */
@Configuration
public class AppConfiguration {

  @Bean
  public RestTemplate getRestTemplate() {
    return new RestTemplate();
  }

}
