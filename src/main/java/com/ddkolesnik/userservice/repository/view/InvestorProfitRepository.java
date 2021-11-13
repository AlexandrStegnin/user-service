package com.ddkolesnik.userservice.repository.view;

import com.ddkolesnik.userservice.model.view.InvestorProfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface InvestorProfitRepository extends JpaRepository<InvestorProfit, Long> {
    List<InvestorProfit> findByInvestorIdOrderByYearSale(Long investorId);
}
