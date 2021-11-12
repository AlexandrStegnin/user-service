package com.ddkolesnik.userservice.repository;

import com.ddkolesnik.userservice.model.domain.AppRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * @author Alexandr Stegnin
 */
@Repository
public interface AppRoleRepository extends JpaRepository<AppRole, Long> {
  AppRole findByName(String name);
}
