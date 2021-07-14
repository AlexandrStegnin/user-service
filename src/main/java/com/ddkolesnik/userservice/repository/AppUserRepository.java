package com.ddkolesnik.userservice.repository;

import com.ddkolesnik.userservice.model.domain.AppUser;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;

/**
 * @author Aleksandr Stegnin on 12.07.2021
 */
public interface AppUserRepository extends JpaRepository<AppUser, Long> {

  Optional<AppUser> findByPhone(String phone);

}
