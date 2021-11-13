package com.ddkolesnik.userservice.controller.view;

import com.ddkolesnik.userservice.model.view.CompanyInvestorProfit;
import com.ddkolesnik.userservice.model.view.CompanyProfit;
import com.ddkolesnik.userservice.model.view.InvestorProfit;
import com.ddkolesnik.userservice.service.view.CompanyProfitService;
import com.ddkolesnik.userservice.service.view.InvestorProfitService;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

import static com.ddkolesnik.userservice.configuration.Location.UNION_PROFIT;

/**
 * @author Alexandr Stegnin
 */
@RestController
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class ProfitController {

  InvestorProfitService investorProfitService;
  CompanyProfitService companyProfitService;

  @PostMapping(path = UNION_PROFIT)
  public List<CompanyInvestorProfit> findByPhone(@RequestBody(required = false) String phone) {
    List<CompanyProfit> companyProfits = companyProfitService.findAll();
    List<InvestorProfit> investorProfits = investorProfitService.findByPhone(phone);

    List<CompanyInvestorProfit> profitUnions = companyProfits.stream()
        .map(CompanyInvestorProfit::new)
        .collect(Collectors.toList());

    profitUnions.forEach(profitUnion -> investorProfits.stream()
        .filter(investorProfit -> investorProfit.getYearSale() == profitUnion.getYearSale())
        .findFirst()
        .ifPresent(profit -> {
          profitUnion.setLogin(profit.getLogin());
          profitUnion.setInvestorProfit(profit.getProfit());
        }));
    return profitUnions;
  }

}
