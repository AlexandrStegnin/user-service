package com.ddkolesnik.userservice.repository.view;

import com.ddkolesnik.userservice.model.view.KindOnProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */
@Repository
public interface KindOnProjectRepository extends JpaRepository<KindOnProject, Long> {
    List<KindOnProject> findByInvestorIdOrderByBuyDate(Long investorId);
}
