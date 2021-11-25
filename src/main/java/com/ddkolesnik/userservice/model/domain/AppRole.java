package com.ddkolesnik.userservice.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_role")
public class AppRole {

  @GenericGenerator(
      name = "app_role_generator",
      strategy = "org.hibernate.id.enhanced.SequenceStyleGenerator",
      parameters = {
          @org.hibernate.annotations.Parameter(name = "sequence_name", value = "app_role_id_seq"),
          @org.hibernate.annotations.Parameter(name = "increment_size", value = "1"),
          @org.hibernate.annotations.Parameter(name = "optimizer", value = "hilo")
      }
  )
  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_generator")
  private Long id;

  @Column(name = "name", unique = true, nullable = false, length = 30)
  private String name;

  @Column(name = "humanized")
  private String humanized;

  @PrePersist
  public void prePersist() {
    this.name = this.name.toUpperCase();
    if (!this.name.startsWith("ROLE_")) {
      this.name = "ROLE_".concat(this.name);
    }
  }

}
