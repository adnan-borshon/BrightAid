package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.School.SchoolType;
import com.example.Bright_Aid.Entity.School.VerificationStatus;
import com.example.Bright_Aid.Entity.School.SchoolStatus;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Entity.Division;
import com.example.Bright_Aid.Entity.District;
import com.example.Bright_Aid.Entity.Upazila;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {


}