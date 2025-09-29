package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.StudentDto;
import com.example.Bright_Aid.service.StudentService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/students")
public class StudentController {

    private final StudentService studentService;

    public StudentController(StudentService studentService) {
        this.studentService = studentService;
    }

    // Create new student
    @PostMapping
    public ResponseEntity<StudentDto> createStudent(@Valid @RequestBody StudentDto studentDto) {
        StudentDto createdStudent = studentService.saveStudent(studentDto);
        return new ResponseEntity<>(createdStudent, HttpStatus.CREATED);
    }

    // Get all students
    @GetMapping
    public ResponseEntity<List<StudentDto>> getAllStudents() {
        List<StudentDto> students = studentService.getAllStudents();
        return new ResponseEntity<>(students, HttpStatus.OK);
    }

    // Get student by ID
    @GetMapping("/{studentId}")
    public ResponseEntity<StudentDto> getStudentById(@PathVariable Integer studentId) {
        StudentDto student = studentService.getStudentById(studentId);
        return new ResponseEntity<>(student, HttpStatus.OK);
    }

    // Update student
    @PutMapping("/{studentId}")
    public ResponseEntity<StudentDto> updateStudent(@PathVariable Integer studentId,
                                                    @Valid @RequestBody StudentDto studentDto) {
        studentDto.setStudentId(studentId);
        StudentDto updatedStudent = studentService.saveStudent(studentDto);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    // Delete student
    @DeleteMapping("/{studentId}")
    public ResponseEntity<Void> deleteStudent(@PathVariable Integer studentId) {
        studentService.deleteStudent(studentId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }

    // Update scholarship status
    @PatchMapping("/{studentId}/scholarship-status")
    public ResponseEntity<StudentDto> updateScholarshipStatus(@PathVariable Integer studentId,
                                                              @RequestParam Boolean hasScholarship) {
        StudentDto updatedStudent = studentService.updateScholarshipStatus(studentId, hasScholarship);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    // Grant scholarship
    @PatchMapping("/{studentId}/grant-scholarship")
    public ResponseEntity<StudentDto> grantScholarship(@PathVariable Integer studentId) {
        StudentDto updatedStudent = studentService.grantScholarship(studentId);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }

    // Remove scholarship
    @PatchMapping("/{studentId}/remove-scholarship")
    public ResponseEntity<StudentDto> removeScholarship(@PathVariable Integer studentId) {
        StudentDto updatedStudent = studentService.removeScholarship(studentId);
        return new ResponseEntity<>(updatedStudent, HttpStatus.OK);
    }
}