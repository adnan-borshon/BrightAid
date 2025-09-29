package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.StudentAttendanceDto;
import com.example.Bright_Aid.service.StudentAttendanceService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/student-attendances")
@RequiredArgsConstructor
public class StudentAttendanceController {

    private final StudentAttendanceService studentAttendanceService;

    @PostMapping
    public ResponseEntity<StudentAttendanceDto> createAttendance(@Valid @RequestBody StudentAttendanceDto dto) {
        StudentAttendanceDto created = studentAttendanceService.createAttendance(dto);
        return new ResponseEntity<>(created, HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    public ResponseEntity<StudentAttendanceDto> getAttendanceById(@PathVariable Integer id) {
        StudentAttendanceDto dto = studentAttendanceService.getAttendanceById(id);
        return ResponseEntity.ok(dto);
    }

    @GetMapping
    public ResponseEntity<List<StudentAttendanceDto>> getAllAttendances() {
        List<StudentAttendanceDto> attendances = studentAttendanceService.getAllAttendances();
        return ResponseEntity.ok(attendances);
    }

    @PutMapping("/{id}")
    public ResponseEntity<StudentAttendanceDto> updateAttendance(
            @PathVariable Integer id,
            @Valid @RequestBody StudentAttendanceDto dto) {
        StudentAttendanceDto updated = studentAttendanceService.updateAttendance(id, dto);
        return ResponseEntity.ok(updated);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAttendance(@PathVariable Integer id) {
        studentAttendanceService.deleteAttendance(id);
        return ResponseEntity.noContent().build();
    }
}