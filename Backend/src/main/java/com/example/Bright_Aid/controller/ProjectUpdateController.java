package com.example.Bright_Aid.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/api/project-updates")
@CrossOrigin(origins = "*")
public class ProjectUpdateController {

    @PostMapping
    public ResponseEntity<String> createProjectUpdate(@RequestBody Map<String, Object> updateData) {
        // Simple response for now
        return new ResponseEntity<>("Project update created successfully", HttpStatus.CREATED);
    }
}