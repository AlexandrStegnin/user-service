package com.ddkolesnik.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

/**
 * @author Aleksandr Stegnin on 08.07.2021
 */
@Configuration
@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

  @Override
  protected void configure(HttpSecurity http) throws Exception {
    http
        .authorizeRequests()
        .antMatchers("/favicon.ico", "/js/**", "/css/**",
            "/", "/registration", "/confirm", "/create", "/restore-password",
            "/change-password", "/confirm-old-phone", "/check-confirm-code",
            "/confirm-new-phone", "/change-phone").permitAll()
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage("/login")
        .usernameParameter("username").passwordParameter("password")
        .defaultSuccessUrl("/profile")
        .permitAll()
        .and()
        .logout()
        .permitAll();
  }

  @Bean
  public PasswordEncoder passwordEncoder() {
    return new BCryptPasswordEncoder();
  }

  @Bean
  public AuthenticationManager authenticationProvider() throws Exception {
    return super.authenticationManagerBean();
  }

}
