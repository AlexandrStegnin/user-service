package com.ddkolesnik.userservice.repository;

import com.ddkolesnik.userservice.model.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */
@Repository
public interface UserAnnexRepository extends JpaRepository<Attachment, Long> {

  Boolean existsByUserIdAndAnnexReadIs(Long userId, Integer read);

  Boolean existsByUserIdAndDateReadIsNull(Long userId);

}
