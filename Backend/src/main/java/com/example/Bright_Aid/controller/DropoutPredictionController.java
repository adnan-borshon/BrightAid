package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DropoutPredictionDto;
import com.example.Bright_Aid.Dto.DropoutPredictionRequestDto;
import com.example.Bright_Aid.service.DropoutPredictionService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dropout-predictions")
@RequiredArgsConstructor
public class DropoutPredictionController {

    private final DropoutPredictionService dropoutPredictionService;

    // GET all dropout predictions
    @GetMapping
    public ResponseEntity<List<DropoutPredictionDto>> getAllPredictions() {
        return ResponseEntity.ok(dropoutPredictionService.getAllPredictions());
    }

    // GET by ID
    @GetMapping("/{id}")
    public ResponseEntity<DropoutPredictionDto> getById(@PathVariable Integer id) {
        return ResponseEntity.ok(dropoutPredictionService.getPredictionById(id));
    }

    // GET by student ID
    @GetMapping("/student/{studentId}")
    public ResponseEntity<List<DropoutPredictionDto>> getByStudentId(@PathVariable Integer studentId) {
        return ResponseEntity.ok(dropoutPredictionService.getPredictionsByStudentId(studentId));
    }

    // POST new prediction
    @PostMapping
    public ResponseEntity<DropoutPredictionDto> create(@RequestBody DropoutPredictionRequestDto request) {
        return ResponseEntity.ok(dropoutPredictionService.createPrediction(
            request.getStudentId(),
            request.getAttendanceRate()
        ));
    }

    // PUT update prediction
    @PutMapping("/{id}")
    public ResponseEntity<DropoutPredictionDto> update(@PathVariable Integer id,
                                                       @RequestBody DropoutPredictionRequestDto request) {
        return ResponseEntity.ok(dropoutPredictionService.updatePrediction(id, request.getAttendanceRate()));
    }

    // DELETE prediction
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        dropoutPredictionService.deletePrediction(id);
        return ResponseEntity.noContent().build();
    }
}