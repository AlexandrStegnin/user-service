package com.ddkolesnik.userservice.model.domain;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;
import java.math.BigInteger;
import java.util.Date;

@Data
@Entity
@FieldDefaults(level = AccessLevel.PRIVATE)
@Table(name = "usersannextocontracts")
public class Attachment {

  @GenericGenerator(
      name = "users_annex_to_contracts_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "usersannextocontracts_id_seq"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
          @org.hibernate.annotations.Parameter(name = "optimizer", value = "hilo")
      }
  )
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "users_annex_to_contracts_generator")
  BigInteger id;

  @Column(name = "userid")
  Long userId;

  @Column(name = "annexread")
  int annexRead;

  @Column(name = "dateread")
  Date dateRead;

  @OneToOne(cascade =
      {
          CascadeType.DETACH,
          CascadeType.MERGE,
          CascadeType.REFRESH,
          CascadeType.PERSIST
      },
      fetch = FetchType.EAGER)
  @JoinColumn(name = "annextocontractsid", referencedColumnName = "id")
  AnnexToContracts annex;
}
