package com.ddkolesnik.userservice.model.domain;

import lombok.*;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author Aleksandr Stegnin on 13.07.2021
 */
@Getter
@Setter
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_profile")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class UserProfile implements Serializable {

  @Id
  Long id;

  @OneToOne(fetch = FetchType.LAZY)
  @JoinColumn(name = "id")
  @MapsId
  AppUser user;

  @Column(name = "last_name")
  String lastName;

  @Column(name = "first_name")
  String name;

  @Column(name = "patronymic")
  String secondName;

  String email;

  @Builder.Default
  boolean locked = false;

}
