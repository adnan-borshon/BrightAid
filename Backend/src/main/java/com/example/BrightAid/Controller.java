package com.example.BrightAid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:5173")
public class Controller {
    
    @Autowired
    private StudentRepository studentRepository;
    
    @GetMapping("/test")
    public String test(){
        return "Backend is working!";
    }
    
    @GetMapping("/students")
    public List<Student> getStudents(){
        return studentRepository.findAll();
    }
}
