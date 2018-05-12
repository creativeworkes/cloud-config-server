package com.creativeworkes.configserver.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.creativeworkes.configserver.entity.PUser;

/**
 * 用户DAO
 */
@Repository
public interface UserRepository extends JpaRepository<PUser, String> {
  PUser findById(String id);

  PUser findByUsername(String username);
}
