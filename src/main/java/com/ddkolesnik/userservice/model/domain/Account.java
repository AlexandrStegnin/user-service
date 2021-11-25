package com.ddkolesnik.userservice.model.domain;

import com.ddkolesnik.userservice.enums.OwnerType;
import lombok.AccessLevel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.ToString;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

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

  @GenericGenerator(
      name = "account_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "account_id_seq"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
          @org.hibernate.annotations.Parameter(name = "optimizer", value = "hilo")
      }
  )
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "account_generator")
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
