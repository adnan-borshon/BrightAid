package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.StudentDto;
import com.example.Bright_Aid.service.StudentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/students")
@RequiredArgsConstructor
public class StudentController {

    private final StudentService studentService;

    @GetMapping
    public List<StudentDto> getAllStudents() {
        return studentService.getAllStudents();
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Integer id) {
        return studentService.getStudentById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@RequestBody StudentDto dto) {
        return ResponseEntity.ok(studentService.createStudent(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Integer id, @RequestBody StudentDto dto) {
        return ResponseEntity.ok(studentService.updateStudent(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer id) {
        studentService.deleteStudent(id);
        return ResponseEntity.noContent().build();
    }



    @PostMapping("/{id}/image")
    public ResponseEntity<Map<String, String>> uploadStudentImage(
            @PathVariable Integer id, 
            @RequestParam("image") MultipartFile file) {
        try {
            System.out.println("Received image upload request for student ID: " + id);
            System.out.println("File name: " + file.getOriginalFilename());
            System.out.println("File size: " + file.getSize());
            System.out.println("File empty: " + file.isEmpty());
            
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("error", "File is empty"));
            }
            
            String imagePath = studentService.updateStudentImage(id, file);
            return ResponseEntity.ok(Map.of("imagePath", imagePath));
        } catch (Exception e) {
            System.err.println("Error uploading image: " + e.getMessage());
            e.printStackTrace();
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }
    
    @GetMapping("/count/school/{schoolId}")
    public ResponseEntity<Map<String, Long>> getStudentCountBySchool(@PathVariable Integer schoolId) {
        Long count = studentService.getStudentCountBySchoolId(schoolId);
        return ResponseEntity.ok(Map.of("count", count));
    }


}
