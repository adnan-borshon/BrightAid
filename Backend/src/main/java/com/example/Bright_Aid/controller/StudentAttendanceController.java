package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.StudentAttendanceDto;
import com.example.Bright_Aid.service.StudentAttendanceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/attendances")
@RequiredArgsConstructor
public class StudentAttendanceController {

    private final StudentAttendanceService attendanceService;

    // Create attendance
    @PostMapping
    public ResponseEntity<StudentAttendanceDto> createAttendance(@RequestBody StudentAttendanceDto dto) {
        StudentAttendanceDto savedDto = attendanceService.saveAttendance(dto);
        return ResponseEntity.ok(savedDto);
    }

    // Get all attendance for a student
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<StudentAttendanceDto>> getAttendanceByStudent(@PathVariable Integer studentId) {
        List<StudentAttendanceDto> list = attendanceService.getAttendanceByStudent(studentId);
        return ResponseEntity.ok(list);
    }

    // Get count of present days
    @GetMapping("/student/{studentId}/present-count")
    public ResponseEntity<Long> getPresentCount(@PathVariable Integer studentId) {
        Long count = attendanceService.countPresentDays(studentId);
        return ResponseEntity.ok(count);
    }

    // Get count of absent days
    @GetMapping("/student/{studentId}/absent-count")
    public ResponseEntity<Long> getAbsentCount(@PathVariable Integer studentId) {
        Long count = attendanceService.countAbsentDays(studentId);
        return ResponseEntity.ok(count);
    }
}
