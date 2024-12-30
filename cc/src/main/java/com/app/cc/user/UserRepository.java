package com.app.cc.user;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface UserRepository extends JpaRepository<User,Long> {

  Optional<User> findByEmail(String email);
  List<User> findByActiveFalse();
  List<User> findByActiveTrue();
  Optional<User> findById(Long id);
  long countByRole(Role role);
  @Query("SELECT COUNT(u) FROM User u WHERE u.active = true")
  Long countActiveUsers();

  // Query to count inactive users
  @Query("SELECT COUNT(u) FROM User u WHERE u.active = false")
  Long countInactiveUsers();
}
