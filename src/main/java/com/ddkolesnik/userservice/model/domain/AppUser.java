package com.ddkolesnik.userservice.model.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * @author Aleksandr Stegnin on 12.07.2021
 */
@Getter
@Setter
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppUser implements UserDetails {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_generator")
  @SequenceGenerator(name="app_user_generator", sequenceName = "app_user_id_seq")
  Long id;

  String phone;

  String login;

  String password;

  Long roleId;

  Integer bitrixId;

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(() -> "USER");
  }

  @OneToOne(mappedBy = "user", cascade = CascadeType.ALL)
  private UserProfile profile;

  @Override
  public String getPassword() {
    return this.password;
  }

  @Override
  public String getUsername() {
    return this.phone;
  }

  @Override
  public boolean isAccountNonExpired() {
    return true;
  }

  @Override
  public boolean isAccountNonLocked() {
    return true;
  }

  @Override
  public boolean isCredentialsNonExpired() {
    return true;
  }

  @Override
  public boolean isEnabled() {
    return true;
  }
}
