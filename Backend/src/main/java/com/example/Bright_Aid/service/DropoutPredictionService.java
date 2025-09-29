package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.DropoutPredictionDto;
import com.example.Bright_Aid.Entity.DropoutPrediction;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.repository.DropoutPredictionRepository;
import com.example.Bright_Aid.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional(readOnly = true)
public class DropoutPredictionService {

    private final DropoutPredictionRepository dropoutPredictionRepository;
    private final StudentRepository studentRepository;

    @Transactional
    public DropoutPredictionDto createDropoutPrediction(DropoutPredictionDto dropoutPredictionDto) {
        log.info("Creating dropout prediction for student ID: {}", dropoutPredictionDto.getStudentId());

        Student student = studentRepository.findById(dropoutPredictionDto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with ID: " + dropoutPredictionDto.getStudentId()));

        DropoutPrediction prediction = DropoutPrediction.builder()
                .student(student)
                .attendanceRate(dropoutPredictionDto.getAttendanceRate())
                .familyIncomeScore(dropoutPredictionDto.getFamilyIncomeScore())
                .parentStatusScore(dropoutPredictionDto.getParentStatusScore())
                .overallRiskScore(dropoutPredictionDto.getOverallRiskScore())
                .riskLevel(dropoutPredictionDto.getRiskLevel())
                .calculatedRiskFactors(dropoutPredictionDto.getCalculatedRiskFactors())
                .predictionDate(dropoutPredictionDto.getPredictionDate())
                .interventionTaken(dropoutPredictionDto.getInterventionTaken())
                .interventionNotes(dropoutPredictionDto.getInterventionNotes())
                .lastCalculated(dropoutPredictionDto.getLastCalculated() != null ?
                        dropoutPredictionDto.getLastCalculated() : LocalDateTime.now())
                .build();

        DropoutPrediction savedPrediction = dropoutPredictionRepository.save(prediction);
        log.info("Successfully created dropout prediction with ID: {}", savedPrediction.getPredictionId());

        return convertToDto(savedPrediction);
    }

    public DropoutPredictionDto getDropoutPredictionById(Integer predictionId) {
        log.info("Fetching dropout prediction with ID: {}", predictionId);

        DropoutPrediction prediction = dropoutPredictionRepository.findById(predictionId)
                .orElseThrow(() -> new RuntimeException("Dropout prediction not found with ID: " + predictionId));

        return convertToDto(prediction);
    }

    public List<DropoutPredictionDto> getAllDropoutPredictions() {
        log.info("Fetching all dropout predictions");

        List<DropoutPrediction> predictions = dropoutPredictionRepository.findAll();
        return predictions.stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    public Page<DropoutPredictionDto> getAllDropoutPredictions(Pageable pageable) {
        log.info("Fetching dropout predictions with pagination: {}", pageable);

        Page<DropoutPrediction> predictions = dropoutPredictionRepository.findAll(pageable);
        return predictions.map(this::convertToDto);
    }

    @Transactional
    public DropoutPredictionDto updateDropoutPrediction(Integer predictionId, DropoutPredictionDto dropoutPredictionDto) {
        log.info("Updating dropout prediction with ID: {}", predictionId);

        DropoutPrediction existingPrediction = dropoutPredictionRepository.findById(predictionId)
                .orElseThrow(() -> new RuntimeException("Dropout prediction not found with ID: " + predictionId));

        if (dropoutPredictionDto.getStudentId() != null &&
                !dropoutPredictionDto.getStudentId().equals(existingPrediction.getStudent().getStudentId())) {
            Student student = studentRepository.findById(dropoutPredictionDto.getStudentId())
                    .orElseThrow(() -> new RuntimeException("Student not found with ID: " + dropoutPredictionDto.getStudentId()));
            existingPrediction.setStudent(student);
        }

        if (dropoutPredictionDto.getAttendanceRate() != null) {
            existingPrediction.setAttendanceRate(dropoutPredictionDto.getAttendanceRate());
        }
        if (dropoutPredictionDto.getFamilyIncomeScore() != null) {
            existingPrediction.setFamilyIncomeScore(dropoutPredictionDto.getFamilyIncomeScore());
        }
        if (dropoutPredictionDto.getParentStatusScore() != null) {
            existingPrediction.setParentStatusScore(dropoutPredictionDto.getParentStatusScore());
        }
        if (dropoutPredictionDto.getOverallRiskScore() != null) {
            existingPrediction.setOverallRiskScore(dropoutPredictionDto.getOverallRiskScore());
        }
        if (dropoutPredictionDto.getRiskLevel() != null) {
            existingPrediction.setRiskLevel(dropoutPredictionDto.getRiskLevel());
        }
        if (dropoutPredictionDto.getCalculatedRiskFactors() != null) {
            existingPrediction.setCalculatedRiskFactors(dropoutPredictionDto.getCalculatedRiskFactors());
        }
        if (dropoutPredictionDto.getPredictionDate() != null) {
            existingPrediction.setPredictionDate(dropoutPredictionDto.getPredictionDate());
        }
        if (dropoutPredictionDto.getInterventionTaken() != null) {
            existingPrediction.setInterventionTaken(dropoutPredictionDto.getInterventionTaken());
        }
        if (dropoutPredictionDto.getInterventionNotes() != null) {
            existingPrediction.setInterventionNotes(dropoutPredictionDto.getInterventionNotes());
        }
        if (dropoutPredictionDto.getLastCalculated() != null) {
            existingPrediction.setLastCalculated(dropoutPredictionDto.getLastCalculated());
        }

        DropoutPrediction updatedPrediction = dropoutPredictionRepository.save(existingPrediction);
        log.info("Successfully updated dropout prediction with ID: {}", predictionId);

        return convertToDto(updatedPrediction);
    }

    @Transactional
    public void deleteDropoutPrediction(Integer predictionId) {
        log.info("Deleting dropout prediction with ID: {}", predictionId);

        if (!dropoutPredictionRepository.existsById(predictionId)) {
            throw new RuntimeException("Dropout prediction not found with ID: " + predictionId);
        }

        dropoutPredictionRepository.deleteById(predictionId);
        log.info("Successfully deleted dropout prediction with ID: {}", predictionId);
    }

    private DropoutPredictionDto convertToDto(DropoutPrediction prediction) {
        return DropoutPredictionDto.builder()
                .predictionId(prediction.getPredictionId())
                .studentId(prediction.getStudent().getStudentId())
                .attendanceRate(prediction.getAttendanceRate())
                .familyIncomeScore(prediction.getFamilyIncomeScore())
                .parentStatusScore(prediction.getParentStatusScore())
                .overallRiskScore(prediction.getOverallRiskScore())
                .riskLevel(prediction.getRiskLevel())
                .calculatedRiskFactors(prediction.getCalculatedRiskFactors())
                .predictionDate(prediction.getPredictionDate())
                .interventionTaken(prediction.getInterventionTaken())
                .interventionNotes(prediction.getInterventionNotes())
                .lastCalculated(prediction.getLastCalculated())
                .createdAt(prediction.getCreatedAt())
                .updatedAt(prediction.getUpdatedAt())
                .build();
    }
}