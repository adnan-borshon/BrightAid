package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdminRepository extends JpaRepository<Admin, Integer> {

    // Optional: Find all active admins
    List<Admin> findByIsActiveTrue();

    // Optional: Find admin by User ID
    Admin findByUserUserId(Integer userId);
}
