package com.ddkolesnik.userservice.service.view;

import com.ddkolesnik.userservice.model.view.CompanyProfit;
import com.ddkolesnik.userservice.repository.view.CompanyProfitRepository;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import lombok.experimental.FieldDefaults;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */
@Service
@RequiredArgsConstructor
@FieldDefaults(makeFinal = true, level = AccessLevel.PRIVATE)
public class CompanyProfitService {

  CompanyProfitRepository companyProfitRepository;

  public List<CompanyProfit> findAll() {
    return companyProfitRepository.findAll();
  }

}
