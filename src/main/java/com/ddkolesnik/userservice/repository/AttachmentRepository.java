package com.ddkolesnik.userservice.repository;

import com.ddkolesnik.userservice.model.domain.Attachment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

/**
 * @author Alexandr Stegnin
 */
@Repository
public interface AttachmentRepository extends JpaRepository<Attachment, BigInteger> {
  List<Attachment> findByUserIdAndAnnex_AnnexName(Long userId, String annexName);

  List<Attachment> findByUserIdOrderByDateReadDesc(Long userId);
}
