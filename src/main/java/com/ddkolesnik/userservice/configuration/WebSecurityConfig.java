package com.ddkolesnik.userservice.configuration;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import static com.ddkolesnik.userservice.configuration.Location.*;

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
        .antMatchers("/img/**", "/fonts/**", "/js/**", "/css/**", "/favicon.ico",
            HOME_URL, LOGIN_URL, LOGOUT_URL, REGISTRATION_URL, CONFIRM_URL, CREATE_USER,
            RESTORE_PASSWORD, CHANGE_PASSWORD, CONFIRM_OLD_PHONE, CHECK_CONFIRM_CODE,
            CONFIRM_NEW_PHONE, CHANGE_PHONE, CONFIRM_BY_EMAIL, RETRY_SEND_CONFIRM_MESSAGE).permitAll()
        .antMatchers(UNION_PROFIT, KIND_ON_PROJECT, INVESTMENTS).hasRole("INVESTOR")
        .anyRequest().authenticated()
        .and()
        .formLogin()
        .loginPage(LOGIN_URL)
        .usernameParameter("username").passwordParameter("password")
        .defaultSuccessUrl(PROFILE)
        .permitAll()
        .and()
        .logout().clearAuthentication(true)
        .logoutRequestMatcher(new AntPathRequestMatcher(LOGOUT_URL))
        .logoutSuccessUrl("/")
        .invalidateHttpSession(true)
        .deleteCookies("JSESSIONID")
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
