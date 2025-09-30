package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DropoutPredictionDto;
import com.example.Bright_Aid.Entity.DropoutPrediction;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.repository.StudentRepository;
import com.example.Bright_Aid.service.DropoutPredictionService;
import lombok.Data;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dropout-predictions")
@RequiredArgsConstructor
public class DropoutPredictionController {

    private final DropoutPredictionService predictionService;
    private final StudentRepository studentRepository;

    // Single student calculation
    @PostMapping("/calculate/{studentId}")
    public ResponseEntity<DropoutPredictionDto> calculateRiskForStudent(
            @PathVariable Integer studentId,
            @RequestParam double attendanceRate,
            @RequestParam double familyMonthlyIncome,
            @RequestParam boolean fatherAlive,
            @RequestParam boolean motherAlive,
            @RequestParam(required = false) String interventionNotes) {

        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        DropoutPrediction prediction = predictionService.calculateAndSavePrediction(
                student, attendanceRate, familyMonthlyIncome, fatherAlive, motherAlive, interventionNotes
        );

        return ResponseEntity.ok(DropoutPredictionDto.fromEntity(prediction));
    }

    // Batch calculation
    @PostMapping("/calculate/batch")
    public ResponseEntity<List<DropoutPredictionDto>> calculateRiskForAllStudents(
            @RequestBody List<StudentRiskDTO> studentRiskDTOs) {

        List<DropoutPredictionDto> predictions = studentRiskDTOs.stream()
                .map(dto -> {
                    Student student = studentRepository.findById(dto.getStudentId())
                            .orElseThrow(() -> new RuntimeException("Student not found: " + dto.getStudentId()));

                    DropoutPrediction prediction = predictionService.calculateAndSavePrediction(
                            student,
                            dto.getAttendanceRate(),
                            dto.getFamilyMonthlyIncome(),
                            dto.isFatherAlive(),
                            dto.isMotherAlive(),
                            dto.getInterventionNotes()
                    );
                    return DropoutPredictionDto.fromEntity(prediction);
                }).toList();

        return ResponseEntity.ok(predictions);
    }

    // Batch DTO
    @Data
    public static class StudentRiskDTO {
        private Integer studentId;
        private double attendanceRate;
        private double familyMonthlyIncome;
        private boolean fatherAlive;
        private boolean motherAlive;
        private String interventionNotes;
    }
}
