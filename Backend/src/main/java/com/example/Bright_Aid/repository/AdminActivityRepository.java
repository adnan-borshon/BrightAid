package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.AdminActivity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface AdminActivityRepository extends JpaRepository<AdminActivity, Integer> {


}