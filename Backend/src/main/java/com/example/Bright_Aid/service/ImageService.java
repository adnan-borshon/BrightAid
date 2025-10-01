package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.Entity.UserProfile;
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

    private static final String UPLOAD_DIR = "src/main/resources/static/images/";

    public String saveStudentImage(Integer studentId, MultipartFile file) throws IOException {
        // Validate file
        if (file.isEmpty()) {
            throw new IllegalArgumentException("File is empty");
        }

        // Get file extension
        String originalFilename = file.getOriginalFilename();
        String extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        
        // Create filename: student_1.jpg
        String filename = "student_" + studentId + extension;
        
        // Create directories if they don't exist
        Path studentDir = Paths.get(UPLOAD_DIR + "students/");
        Files.createDirectories(studentDir);
        
        // Save file
        Path filePath = studentDir.resolve(filename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
        
        // Update student profile_image in database
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new IllegalArgumentException("Student not found"));
        
        String imagePath = "/images/students/" + filename;
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
        
        // Update user profile image in database
        UserProfile userProfile = userProfileRepository.findByUser_UserId(userId)
                .orElseThrow(() -> new IllegalArgumentException("User profile not found"));
        
        String imagePath = "/images/users/" + filename;
        userProfile.setProfileImageUrl(imagePath);
        userProfileRepository.save(userProfile);
        
        return imagePath;
    }
}