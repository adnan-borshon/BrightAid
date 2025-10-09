package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Donor;
import com.example.Bright_Aid.Entity.School;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.Entity.UserProfile;
import com.example.Bright_Aid.repository.DonorRepository;
import com.example.Bright_Aid.repository.SchoolRepository;
import com.example.Bright_Aid.repository.StudentRepository;
import com.example.Bright_Aid.repository.UserProfileRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@RequiredArgsConstructor
public class ImageService {

    private final StudentRepository studentRepository;
    private final UserProfileRepository userProfileRepository;
    private final SchoolRepository schoolRepository;
    private final DonorRepository donorRepository;

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    public String saveStudentImage(Integer studentId, MultipartFile file) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Get student to find school ID for unique naming
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        // Get file extension
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // Create unique filename: school_1_student_5.jpg
        String filename = "school_" + student.getSchool().getSchoolId() + "_student_" + studentId + extension;
        
        // Create directories if they don't exist
        Path studentDir = Paths.get(UPLOAD_DIR + "students/");
        Files.createDirectories(studentDir);
        
        // Save file
        Path filePath = studentDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Update student profile_image in database with correct static path
        String imagePath = "/static/images/students/" + filename;
        student.setProfileImage(imagePath);
        studentRepository.save(student);
        
        return imagePath;
    }

    public String saveUserImage(Integer userId, MultipartFile file) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Get file extension
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // Create filename: user_1.jpg
        String filename = "user_" + userId + extension;
        
        // Create directories if they don't exist
        Path userDir = Paths.get(UPLOAD_DIR + "users/");
        Files.createDirectories(userDir);
        
        // Save file
        Path filePath = userDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Update user profile image in database with correct static path
        UserProfile userProfile = userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
        
        String imagePath = "/static/images/users/" + filename;
        userProfile.setProfileImageUrl(imagePath);
        userProfileRepository.save(userProfile);
        
        return imagePath;
    }

    // Save school image with proper naming: school_1.jpg
    public String saveSchoolImage(Integer schoolId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = "school_" + schoolId + extension;
        
        // Create schools directory
        Path schoolDir = Paths.get(UPLOAD_DIR + "schools/");
        Files.createDirectories(schoolDir);
        
        // Save file
        Path filePath = schoolDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Update school image in database with correct static path
        School school = schoolRepository.findById(schoolId)
                .orElseThrow(() -> new IllegalArgumentException("School not found"));
        
        String imagePath = "/static/images/schools/" + filename;
        school.setSchoolImage(imagePath);
        schoolRepository.save(school);
        
        return imagePath;
    }

    // Save donor image with proper naming: donor_1.jpg
    public String saveDonorImage(Integer donorId, MultipartFile file) throws IOException {
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        String filename = "donor_" + donorId + extension;
        
        // Create donors directory
        Path donorDir = Paths.get(UPLOAD_DIR + "donors/");
        Files.createDirectories(donorDir);
        
        // Save file
        Path filePath = donorDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Update donor image in database with correct static path
        Donor donor = donorRepository.findById(donorId)
                .orElseThrow(() -> new IllegalArgumentException("Donor not found"));
        
        String imagePath = "/static/images/donors/" + filename;
        donor.setDonorImage(imagePath);
        donorRepository.save(donor);
        
        return imagePath;
    }
}