package com.ddkolesnik.userservice.utils;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

/**
 * @author Aleksandr Stegnin on 13.07.2021
 */
@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SecurityUtils {

  public static String getCurrentUserPhone() {
    Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
    return authentication.getName();
  }

}
