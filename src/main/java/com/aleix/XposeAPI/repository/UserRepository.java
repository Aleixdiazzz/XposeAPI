package com.aleix.XposeAPI.repository;

import com.aleix.XposeAPI.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);

    Optional<User> findByEmailAndPasswordHash(String email, String password);
}

