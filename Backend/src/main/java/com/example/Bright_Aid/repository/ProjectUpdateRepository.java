package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.ProjectUpdate;
import com.example.Bright_Aid.Entity.SchoolProject;
import com.example.Bright_Aid.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;

@Repository
public interface ProjectUpdateRepository extends JpaRepository<ProjectUpdate, Integer> {

}