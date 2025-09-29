package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.SchoolProject;
import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Entity.ProjectType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SchoolProjectRepository extends JpaRepository<SchoolProject, Integer> {


}