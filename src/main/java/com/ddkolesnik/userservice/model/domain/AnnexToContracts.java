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
@Table(name = "annextocontracts")
public class AnnexToContracts {

  @GenericGenerator(
      name = "annex_to_contracts_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "annextocontracts_id_seq"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
          @org.hibernate.annotations.Parameter(name = "optimizer", value = "hilo")
      }
  )
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "annex_to_contracts_generator")
  @Column(name = "Id")
  BigInteger id;

  @Column(name = "annexname")
  String annexName;

  @Column(name = "filepath")
  String filePath;

  @Column(name = "dateload")
  Date dateLoad;

  @Column(name = "loadedby")
  Long loadedBy;

}
