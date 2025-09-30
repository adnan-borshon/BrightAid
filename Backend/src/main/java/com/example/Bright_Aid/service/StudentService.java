package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.Dto.StudentDto;
import com.example.Bright_Aid.repository.SchoolRepository;
import com.example.Bright_Aid.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    // Convert DTO → Entity
    private Student mapToEntity(StudentDto dto) {
        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new EntityNotFoundException("School not found"));

        return Student.builder()
                .studentId(dto.getStudentId())
                .studentName(dto.getStudentName())
                .studentIdNumber(dto.getStudentIdNumber())
                .gender(dto.getGender())
                .dateOfBirth(dto.getDateOfBirth())
                .fatherName(dto.getFatherName())
                .fatherAlive(dto.getFatherAlive())
                .fatherOccupation(dto.getFatherOccupation())
                .motherName(dto.getMotherName())
                .motherAlive(dto.getMotherAlive())
                .motherOccupation(dto.getMotherOccupation())
                .guardianPhone(dto.getGuardianPhone())
                .address(dto.getAddress())
                .classLevel(dto.getClassLevel())
                .familyMonthlyIncome(dto.getFamilyMonthlyIncome())
                .hasScholarship(dto.getHasScholarship())
                .school(school)
                .build();
    }

    // Convert Entity → DTO
    private StudentDto mapToDTO(Student student) {
        return StudentDto.builder()
                .studentId(student.getStudentId())
                .schoolId(student.getSchool().getSchoolId())
                .studentName(student.getStudentName())
                .studentIdNumber(student.getStudentIdNumber())
                .gender(student.getGender())
                .dateOfBirth(student.getDateOfBirth())
                .fatherName(student.getFatherName())
                .fatherAlive(student.getFatherAlive())
                .fatherOccupation(student.getFatherOccupation())
                .motherName(student.getMotherName())
                .motherAlive(student.getMotherAlive())
                .motherOccupation(student.getMotherOccupation())
                .guardianPhone(student.getGuardianPhone())
                .address(student.getAddress())
                .classLevel(student.getClassLevel())
                .familyMonthlyIncome(student.getFamilyMonthlyIncome())
                .hasScholarship(student.getHasScholarship())
                .build();
    }

    // CRUD operations

    public StudentDto createStudent(StudentDto dto) {
        Student student = mapToEntity(dto);
        return mapToDTO(studentRepository.save(student));
    }

    public StudentDto updateStudent(Integer id, StudentDto dto) {
        Student existing = studentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
        Student updated = mapToEntity(dto);
        updated.setStudentId(existing.getStudentId()); // preserve ID
        return mapToDTO(studentRepository.save(updated));
    }

    public StudentDto getStudentById(Integer id) {
        return studentRepository.findById(id)
                .map(this::mapToDTO)
                .orElseThrow(() -> new EntityNotFoundException("Student not found"));
    }

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll().stream()
                .map(this::mapToDTO)
                .collect(Collectors.toList());
    }

    public void deleteStudent(Integer id) {
        if (!studentRepository.existsById(id))
            throw new EntityNotFoundException("Student not found");
        studentRepository.deleteById(id);
    }
}
