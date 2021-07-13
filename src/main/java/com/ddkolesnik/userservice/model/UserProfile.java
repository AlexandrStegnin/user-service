package com.ddkolesnik.userservice.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.FieldDefaults;

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
public class UserProfile {

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

  boolean locked = false;

}
