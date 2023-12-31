package com.capgemini.hackyeah.domain.repository;

import com.capgemini.hackyeah.domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findFirstByEmail(String email);
    Optional<User> findByEmail(String email);
    List<User> findAllByOrderByTotalPointsDesc();
}
