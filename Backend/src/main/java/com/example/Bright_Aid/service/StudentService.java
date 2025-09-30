package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.*;
import com.example.Bright_Aid.Dto.StudentDto;
import com.example.Bright_Aid.repository.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    public StudentService(StudentRepository studentRepository,
                          SchoolRepository schoolRepository) {
        this.studentRepository = studentRepository;
        this.schoolRepository = schoolRepository;
    }

    // Create or update Student
    public StudentDto saveStudent(StudentDto studentDto) {
        School school = schoolRepository.findById(studentDto.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found"));

        Student student = Student.builder()
                .studentId(studentDto.getStudentId())
                .school(school)
                .studentName(studentDto.getStudentName())
                .studentIdNumber(studentDto.getStudentIdNumber())
                .gender(studentDto.getGender() != null ?
                        Student.Gender.valueOf(studentDto.getGender()) : null)
                .dateOfBirth(studentDto.getDateOfBirth())
                .fatherName(studentDto.getFatherName())
                .fatherAlive(studentDto.getFatherAlive() != null ?
                        studentDto.getFatherAlive() : true)
                .fatherOccupation(studentDto.getFatherOccupation())
                .motherName(studentDto.getMotherName())
                .motherAlive(studentDto.getMotherAlive() != null ?
                        studentDto.getMotherAlive() : true)
                .motherOccupation(studentDto.getMotherOccupation())
                .guardianPhone(studentDto.getGuardianPhone())
                .address(studentDto.getAddress())
                .classLevel(Student.ClassLevel.valueOf(studentDto.getClassLevel()))
                .familyMonthlyIncome(studentDto.getFamilyMonthlyIncome())
                .hasScholarship(studentDto.getHasScholarship() != null ?
                        studentDto.getHasScholarship() : false)
                .build();

        Student saved = studentRepository.save(student);
        return mapToDto(saved);
    }

    // Get all students
    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    // Get student by ID
    public StudentDto getStudentById(Integer studentId) {
        return studentRepository.findById(studentId)
                .map(this::mapToDto)
                .orElseThrow(() -> new RuntimeException("Student not found"));
    }

    // Delete student
    public void deleteStudent(Integer studentId) {
        if (!studentRepository.existsById(studentId)) {
            throw new RuntimeException("Student not found");
        }
        studentRepository.deleteById(studentId);
    }

    // Update scholarship status
    public StudentDto updateScholarshipStatus(Integer studentId, Boolean hasScholarship) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        student.setHasScholarship(hasScholarship);
        Student saved = studentRepository.save(student);
        return mapToDto(saved);
    }

    // Grant scholarship
    public StudentDto grantScholarship(Integer studentId) {
        return updateScholarshipStatus(studentId, true);
    }

    // Remove scholarship
    public StudentDto removeScholarship(Integer studentId) {
        return updateScholarshipStatus(studentId, false);
    }

    // Map Student entity to DTO
    private StudentDto mapToDto(Student student) {
        return StudentDto.builder()
                .studentId(student.getStudentId())
                .schoolId(student.getSchool().getSchoolId())
                .studentName(student.getStudentName())
                .studentIdNumber(student.getStudentIdNumber())
                .gender(student.getGender() != null ? student.getGender().name() : null)
                .dateOfBirth(student.getDateOfBirth())
                .fatherName(student.getFatherName())
                .fatherAlive(student.getFatherAlive())
                .fatherOccupation(student.getFatherOccupation())
                .motherName(student.getMotherName())
                .motherAlive(student.getMotherAlive())
                .motherOccupation(student.getMotherOccupation())
                .guardianPhone(student.getGuardianPhone())
                .address(student.getAddress())
                .classLevel(student.getClassLevel().name())
                .familyMonthlyIncome(student.getFamilyMonthlyIncome())
                .hasScholarship(student.getHasScholarship())
                .createdAt(student.getCreatedAt())
                .updatedAt(student.getUpdatedAt())
                .build();
    }
}