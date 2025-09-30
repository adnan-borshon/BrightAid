package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {

    // Add these three methods:
    List<School> findBySchoolNameContainingIgnoreCase(String name);

    List<School> findByStatus(School.SchoolStatus status);

    List<School> findByVerificationStatus(School.VerificationStatus verificationStatus);
}