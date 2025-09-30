package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.StudentAttendance;
import com.example.Bright_Aid.Entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface StudentAttendanceRepository extends JpaRepository<StudentAttendance, Integer> {

    // Get all attendance records for a student
    List<StudentAttendance> findByStudent(Student student);

    // Get attendance records for a student on a specific date
    List<StudentAttendance> findByStudentAndAttendanceDate(Student student, LocalDate attendanceDate);

    // Custom query: count of present days for a student
    @Query("SELECT COUNT(sa) FROM StudentAttendance sa WHERE sa.student = :student AND sa.present = true")
    Long countPresentDays(@Param("student") Student student);

    // Custom query: count of absent days for a student
    @Query("SELECT COUNT(sa) FROM StudentAttendance sa WHERE sa.student = :student AND sa.present = false")
    Long countAbsentDays(@Param("student") Student student);
}
