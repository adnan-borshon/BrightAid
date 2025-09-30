package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.Entity.Student.ClassLevel;
import com.example.Bright_Aid.Entity.Student.Gender;
import com.example.Bright_Aid.Entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    // Find all students by school
    List<Student> findBySchool(School school);

    // Find students by class level
    List<Student> findByClassLevel(ClassLevel classLevel);

    // Find students by gender
    List<Student> findByGender(Gender gender);

    // Find students born after a specific date
    List<Student> findByDateOfBirthAfter(LocalDate date);

    // Find students with a specific father alive status
    List<Student> findByFatherAlive(Boolean fatherAlive);

    // Find by unique student ID number
    Optional<Student> findByStudentIdNumber(String studentIdNumber);

    // Example custom query: search by partial name
    @Query("SELECT s FROM Student s WHERE s.studentName LIKE %:name%")
    List<Student> searchByName(@Param("name") String name);

    // ✅ New: Find students by profile image URL
    Optional<Student> findByProfileImage(String profileImage);

    // ✅ New: Find students where profile image is not null (students with images)
    List<Student> findByProfileImageIsNotNull();
}
