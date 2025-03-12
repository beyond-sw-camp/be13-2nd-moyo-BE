package com.beyond.backend.domain.user.repository;

import com.beyond.backend.domain.user.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long>, UserRepositoryCustom {

    Optional<User> findByUsername(String username);

    Boolean existsByUsername(String username);
}
