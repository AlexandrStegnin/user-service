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
import java.util.UUID;

/**
 * Сущность для отображения представления заработок компании в разрезе инвесторов
 *
 * @author Alexandr Stegnin
 */
@Data
@Entity
@Immutable
@Table(name = "investor_profit")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class InvestorProfit {

  @Id
  @Column(name = "uuid", insertable = false, updatable = false)
  UUID id;

  @Column(name = "year_sale")
  int yearSale;

  @Column(name = "profit")
  BigDecimal profit;

  @Column(name = "login")
  String login;

}
