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

public String saveStudentImage(MultipartFile file, Integer studentId) {
    try {
        System.out.println("Saving image for student ID: " + studentId);
        String uploadDir = "src/main/resources/static/images/students/";
        Path uploadPath = Paths.get(uploadDir);
        
        System.out.println("Upload directory: " + uploadPath.toAbsolutePath());
        
        if (!Files.exists(uploadPath)) {
            System.out.println("Creating directory: " + uploadPath);
            Files.createDirectories(uploadPath);
        }
        
        String fileExtension = getFileExtension(file.getOriginalFilename());
        String fileName = "student_" + studentId + fileExtension;
        Path filePath = uploadPath.resolve(fileName);
        
        System.out.println("Saving file to: " + filePath.toAbsolutePath());
        Files.copy(file.getInputStream(), filePath);
        System.out.println("File saved successfully");
        
        // ✅ CHANGED: Return without /static prefix
        return "/images/students/" + fileName;
    } catch (IOException e) {
        System.err.println("IOException while saving image: " + e.getMessage());
        e.printStackTrace();
        throw new RuntimeException("Failed to save image: " + e.getMessage());
    }
}
    public String saveUserImage(MultipartFile file, Integer userId) {
        try {
            String uploadDir = "src/main/resources/static/images/users/";
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
            
            String fileExtension = getFileExtension(file.getOriginalFilename());
            String fileName = "user_" + userId + fileExtension;
            Path filePath = uploadPath.resolve(fileName);
            Files.copy(file.getInputStream(), filePath);
            
            return "/static/images/users/" + fileName;
        } catch (IOException e) {
            throw new RuntimeException("Failed to save image: " + e.getMessage());
        }
    }
    
    private String getFileExtension(String filename) {
        if (filename == null || filename.lastIndexOf('.') == -1) {
            return ".png";
        }
        return filename.substring(filename.lastIndexOf('.'));
    }

    public String updateStudentImage(Integer studentId, MultipartFile file) {
        System.out.println("Updating image for student ID: " + studentId);
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        
        String imagePath = saveStudentImage(file, studentId);
        System.out.println("Image saved at path: " + imagePath);
        student.setProfileImage(imagePath);
        studentRepository.save(student);
        System.out.println("Student profile image updated successfully");
        
        return imagePath;
    }
    
    public Long getStudentCountBySchoolId(Integer schoolId) {
        return studentRepository.countStudentsBySchoolId(schoolId);
    }


}
