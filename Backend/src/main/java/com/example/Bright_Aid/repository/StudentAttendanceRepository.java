package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.StudentAttendance;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Integer> {


}