package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.DropoutPredictionDto;
import com.example.Bright_Aid.Entity.DropoutPrediction;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.repository.DropoutPredictionRepository;
import com.example.Bright_Aid.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DropoutPredictionService {

    private final DropoutPredictionRepository repository;
    private final StudentRepository studentRepository;

    // Create prediction with automatic risk calculation using native query
    public DropoutPredictionDto createPrediction(Integer studentId, Double attendanceRate) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        
        // Calculate risk status using native query
        String riskStatusStr = repository.calculateRiskStatus(studentId, attendanceRate);
        DropoutPrediction.RiskStatus riskStatus = DropoutPrediction.RiskStatus.valueOf(riskStatusStr);
        
        DropoutPrediction prediction = DropoutPrediction.builder()
                .student(student)
                .attendanceRate(attendanceRate)
                .riskStatus(riskStatus)
                .lastCalculated(LocalDateTime.now())
                .build();
        
        return DropoutPredictionDto.fromEntity(repository.save(prediction));
    }

    // Get all predictions
    public List<DropoutPredictionDto> getAllPredictions() {
        return repository.findAll().stream()
                .map(DropoutPredictionDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Get prediction by ID
    public DropoutPredictionDto getPredictionById(Integer id) {
        DropoutPrediction prediction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prediction not found with id: " + id));
        return DropoutPredictionDto.fromEntity(prediction);
    }

    // Get predictions by student ID
    public List<DropoutPredictionDto> getPredictionsByStudentId(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + studentId));
        return repository.findByStudent(student).stream()
                .map(DropoutPredictionDto::fromEntity)
                .collect(Collectors.toList());
    }

    // Update prediction
    public DropoutPredictionDto updatePrediction(Integer id, Double attendanceRate) {
        DropoutPrediction prediction = repository.findById(id)
                .orElseThrow(() -> new RuntimeException("Prediction not found with id: " + id));
        
        // Recalculate risk status with new attendance rate
        String riskStatusStr = repository.calculateRiskStatus(prediction.getStudent().getStudentId(), attendanceRate);
        DropoutPrediction.RiskStatus riskStatus = DropoutPrediction.RiskStatus.valueOf(riskStatusStr);
        
        prediction.setAttendanceRate(attendanceRate);
        prediction.setRiskStatus(riskStatus);
        prediction.setLastCalculated(LocalDateTime.now());
        
        return DropoutPredictionDto.fromEntity(repository.save(prediction));
    }

    // Delete prediction
    public void deletePrediction(Integer id) {
        if (!repository.existsById(id)) {
            throw new RuntimeException("Prediction not found with id: " + id);
        }
        repository.deleteById(id);
    }
}