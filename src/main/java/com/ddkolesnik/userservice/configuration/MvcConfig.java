package com.ddkolesnik.userservice.configuration;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

/**
 * @author Aleksandr Stegnin on 08.07.2021
 */
@Configuration
public class MvcConfig implements WebMvcConfigurer {

  public void addViewControllers(ViewControllerRegistry registry) {
    registry.addViewController("/profile").setViewName("profile");
    registry.addViewController("/").setViewName("profile");
    registry.addViewController("/registration").setViewName("registration");
  }

}
