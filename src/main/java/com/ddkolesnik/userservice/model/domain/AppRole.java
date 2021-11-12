package com.ddkolesnik.userservice.model.domain;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "app_role")
public class AppRole {

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "app_role_generator")
  @SequenceGenerator(name = "app_role_generator", sequenceName = "app_role_id_seq")
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
