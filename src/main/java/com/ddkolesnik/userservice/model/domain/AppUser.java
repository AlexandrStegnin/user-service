package com.ddkolesnik.userservice.model.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import javax.persistence.*;
import java.util.Collection;
import java.util.List;

/**
 * @author Aleksandr Stegnin on 12.07.2021
 */
@Data
@Entity
@Builder
@Table(name = "app_user")
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class AppUser implements UserDetails {

  @GenericGenerator(
      name = "app_user_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "app_user_id_seq"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
          @org.hibernate.annotations.Parameter(name = "optimizer", value = "hilo")
      }
  )
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_user_generator")
  Long id;

  String phone;

  String login;

  String password;

  @OneToOne
  @JoinColumn(name = "role_id")
  AppRole role;

  Integer bitrixId;

  public String getAccountNumber() {
    return this.phone.replace("+", "");
  }

  @Override
  public Collection<? extends GrantedAuthority> getAuthorities() {
    return List.of(role::getName);
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
