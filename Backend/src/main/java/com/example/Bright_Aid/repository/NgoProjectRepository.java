package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.NgoProject;
import com.example.Bright_Aid.Entity.NgoProject.ProjectStatus;
import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.Entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Repository
public interface NgoProjectRepository extends JpaRepository<NgoProject, Integer> {


}