package com.example.BrightAid;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/abc")
public class Controller {
    @GetMapping("/hello")
    public String show(){
        return "Hello World";
    }
}
