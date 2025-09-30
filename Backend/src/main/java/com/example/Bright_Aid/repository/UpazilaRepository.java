package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Upazila;
import com.example.Bright_Aid.Entity.District;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UpazilaRepository extends JpaRepository<Upazila, Integer> {


    Optional<Upazila> findByUpazilaName(String upazilaName);
}