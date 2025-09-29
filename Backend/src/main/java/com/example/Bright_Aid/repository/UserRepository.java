package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Integer> {

    // Find user by email
    Optional<User> findByEmail(String email);

    // Optional: check if email exists
    boolean existsByEmail(String email);
}
