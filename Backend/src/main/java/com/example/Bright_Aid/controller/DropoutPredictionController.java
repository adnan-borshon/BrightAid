package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.DropoutPredictionDto;
import com.example.Bright_Aid.service.DropoutPredictionService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/dropout-predictions")
@RequiredArgsConstructor
@Slf4j
@CrossOrigin(origins = "*")
public class DropoutPredictionController {

    private final DropoutPredictionService dropoutPredictionService;

    @PostMapping
    public ResponseEntity<DropoutPredictionDto> createDropoutPrediction(
            @Valid @RequestBody DropoutPredictionDto dropoutPredictionDto) {
        log.info("REST request to create dropout prediction for student ID: {}", dropoutPredictionDto.getStudentId());

        DropoutPredictionDto createdPrediction = dropoutPredictionService.createDropoutPrediction(dropoutPredictionDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(createdPrediction);
    }

    @GetMapping("/{predictionId}")
    public ResponseEntity<DropoutPredictionDto> getDropoutPredictionById(@PathVariable Integer predictionId) {
        log.info("REST request to get dropout prediction with ID: {}", predictionId);

        DropoutPredictionDto prediction = dropoutPredictionService.getDropoutPredictionById(predictionId);
        return ResponseEntity.ok(prediction);
    }

    @GetMapping
    public ResponseEntity<List<DropoutPredictionDto>> getAllDropoutPredictions() {
        log.info("REST request to get all dropout predictions");

        List<DropoutPredictionDto> predictions = dropoutPredictionService.getAllDropoutPredictions();
        return ResponseEntity.ok(predictions);
    }

    @GetMapping("/paginated")
    public ResponseEntity<Page<DropoutPredictionDto>> getAllDropoutPredictions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "predictionId") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {

        log.info("REST request to get dropout predictions with pagination - page: {}, size: {}, sortBy: {}, sortDirection: {}",
                page, size, sortBy, sortDirection);

        Sort sort = sortDirection.equalsIgnoreCase("DESC") ?
                Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();

        Pageable pageable = PageRequest.of(page, size, sort);
        Page<DropoutPredictionDto> predictions = dropoutPredictionService.getAllDropoutPredictions(pageable);

        return ResponseEntity.ok(predictions);
    }

    @PutMapping("/{predictionId}")
    public ResponseEntity<DropoutPredictionDto> updateDropoutPrediction(
            @PathVariable Integer predictionId,
            @Valid @RequestBody DropoutPredictionDto dropoutPredictionDto) {
        log.info("REST request to update dropout prediction with ID: {}", predictionId);

        DropoutPredictionDto updatedPrediction = dropoutPredictionService.updateDropoutPrediction(predictionId, dropoutPredictionDto);
        return ResponseEntity.ok(updatedPrediction);
    }

    @DeleteMapping("/{predictionId}")
    public ResponseEntity<Void> deleteDropoutPrediction(@PathVariable Integer predictionId) {
        log.info("REST request to delete dropout prediction with ID: {}", predictionId);

        dropoutPredictionService.deleteDropoutPrediction(predictionId);
        return ResponseEntity.noContent().build();
    }
}