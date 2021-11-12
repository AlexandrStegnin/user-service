package com.ddkolesnik.userservice.service.view;

import com.ddkolesnik.userservice.model.view.InvestorProfit;
import com.ddkolesnik.userservice.repository.view.InvestorProfitRepository;
import com.ddkolesnik.userservice.utils.SecurityUtils;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

/**
 * @author Alexandr Stegnin
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class InvestorProfitService {

  InvestorProfitRepository investorProfitRepository;

  public List<InvestorProfit> findByLogin(String login) {
    if (Objects.isNull(login)) {
      login = SecurityUtils.getCurrentUserPhone();
    }
    return investorProfitRepository.findByLoginOrderByYearSale(login);
  }

}
