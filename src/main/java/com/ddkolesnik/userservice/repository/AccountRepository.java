package com.ddkolesnik.userservice.repository;

import com.ddkolesnik.userservice.enums.OwnerType;
import com.ddkolesnik.userservice.model.domain.Account;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {

    boolean existsByAccountNumber(String accountNumber);

    Account findByOwnerIdAndOwnerType(Long ownerId, OwnerType ownerType);

}
