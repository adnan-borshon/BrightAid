package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.Dto.StudentDto;
import com.example.Bright_Aid.repository.SchoolRepository;
import com.example.Bright_Aid.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentService {

    private final StudentRepository studentRepository;
    private final SchoolRepository schoolRepository;

    // Convert Entity -> DTO
    private StudentDto convertToDTO(Student student) {
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
                .profileImage(student.getProfileImage())
                .build();
    }

    // Convert DTO -> Entity
    private Student convertToEntity(StudentDto dto) {
        School school = schoolRepository.findById(dto.getSchoolId())
                .orElseThrow(() -> new RuntimeException("School not found"));

        return Student.builder()
                .studentId(dto.getStudentId())
                .school(school)
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
                .profileImage(dto.getProfileImage())
                .build();
    }

    public List<StudentDto> getAllStudents() {
        return studentRepository.findAll()
                .stream().map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    public Optional<StudentDto> getStudentById(Integer id) {
        return studentRepository.findById(id).map(this::convertToDTO);
    }

    public StudentDto createStudent(StudentDto dto) {
        Student student = convertToEntity(dto);
        return convertToDTO(studentRepository.save(student));
    }

    public StudentDto updateStudent(Integer id, StudentDto dto) {
        return studentRepository.findById(id).map(existing -> {
            Student updated = convertToEntity(dto);
            updated.setStudentId(existing.getStudentId());
            return convertToDTO(studentRepository.save(updated));
        }).orElseThrow(() -> new RuntimeException("Student not found"));
    }

    public void deleteStudent(Integer id) {
        studentRepository.deleteById(id);
    }

    public String saveImage(MultipartFile file) {
        try {
            String uploadDir = "src/main/resources/static/images/students/";
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String fileName = UUID.randomUUID().toString() + "_" + file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            return "/images/students/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }
}
