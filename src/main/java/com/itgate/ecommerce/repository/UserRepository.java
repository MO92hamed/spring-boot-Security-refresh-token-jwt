package com.itgate.ecommerce.repository;

import com.itgate.ecommerce.models.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
  Optional<User> findByUsername(String username);
  User findByEmail(String email);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
  User findByPasswordResetToken(String passwordResetToken);
}
