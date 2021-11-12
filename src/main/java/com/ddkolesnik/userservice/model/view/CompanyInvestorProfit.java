package com.ddkolesnik.userservice.model.view;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

/**
 * Сущность для отображения представления заработок компании по годам и по всем инвесторам
 *
 * @author Alexandr Stegnin
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class CompanyInvestorProfit {

  int yearSale;
  BigDecimal profit;
  String login;
  BigDecimal investorProfit;

  public CompanyInvestorProfit(CompanyProfit companyProfit) {
    this.yearSale = companyProfit.getYearSale();
    this.profit = companyProfit.getProfit();
  }

}
