package com.ddkolesnik.userservice.model.view;

import lombok.AccessLevel;
import lombok.Data;
import lombok.experimental.FieldDefaults;
import org.hibernate.annotations.Immutable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;
import java.util.Date;

/**
 *
 * Класс для отображения представления "доля вложений инвестора в каждом проекте"
 *
 * @author Alexandr Stegnin
 */
@Data
@Entity
@Immutable
@Table(name = "kind_on_project")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class KindOnProject {

  @Id
  @Column(name = "ID")
  Long id;

  @Column(name = "facility")
  String facility;

  @Column(name = "login")
  String login;

  @Column(name = "given_cash")
  BigDecimal givenCash;

  @Column(name = "project_coast")
  BigDecimal projectCoast;

  @Column(name = "buy_date")
  Date buyDate;

  @Column(name = "investorid")
  Long investorId;

}
