package com.ddkolesnik.userservice.model.domain;

import com.ddkolesnik.userservice.enums.OwnerType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;

import javax.persistence.*;

/**
 * @author Alexandr Stegnin
 */

@Data
@Entity
@Table(name = "account")
@ToString(of = {"id", "accountNumber"})
@FieldDefaults(level = AccessLevel.PRIVATE)
@EqualsAndHashCode(of = {"id", "accountNumber"})
public class Account {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
  @SequenceGenerator(name="account_generator", sequenceName = "account_id_seq")
  @Column(name = "id")
  Long id;

  @Column(name = "account_number")
  String accountNumber;

  @Column(name = "owner_id")
  Long ownerId;

  @Enumerated(EnumType.STRING)
  @Column(name = "owner_type")
  OwnerType ownerType;

  @Column(name = "owner_name")
  String ownerName;
}
