package com.ddkolesnik.userservice.repository.view;

import com.ddkolesnik.userservice.model.view.CompanyProfit;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */
@Repository
public interface CompanyProfitRepository extends JpaRepository<CompanyProfit, Long> {
}
