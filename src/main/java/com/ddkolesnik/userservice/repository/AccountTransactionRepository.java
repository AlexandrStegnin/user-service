package com.ddkolesnik.userservice.repository;

import com.ddkolesnik.userservice.enums.OwnerType;
import com.ddkolesnik.userservice.model.domain.AccountTransaction;
import com.ddkolesnik.userservice.model.dto.AccountDTO;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AccountTransactionRepository extends JpaRepository<AccountTransaction, Long> {

  @Query("SELECT DISTINCT new com.ddkolesnik.userservice.model.dto.AccountDTO(atx.owner, SUM(atx.cash), atx.owner.ownerName) " +
      "FROM AccountTransaction atx " +
      "WHERE atx.owner.ownerType = :ownerType AND atx.owner.ownerId = :investorId " +
      "GROUP BY atx.owner, atx.owner.ownerName " +
      "ORDER BY atx.owner.ownerName")
  AccountDTO fetchBalance(@Param("ownerType") OwnerType ownerType, @Param("investorId") Long investorId);

  @Query("SELECT atx FROM AccountTransaction atx WHERE atx.owner.ownerId = :investorId " +
      "AND atx.owner.ownerType = com.ddkolesnik.userservice.enums.OwnerType.INVESTOR " +
      "ORDER BY atx.txDate DESC ")
  List<AccountTransaction> findByInvestorId(@Param("investorId") Long investorId);
}
