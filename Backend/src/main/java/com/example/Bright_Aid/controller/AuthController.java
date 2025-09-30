package com.example.Bright_Aid.controller;

import com.example.Bright_Aid.Dto.AuthDTO;
import com.example.Bright_Aid.Dto.AuthResponse;
import com.example.Bright_Aid.Dto.UserDTO;
import com.example.Bright_Aid.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Slf4j
public class AuthController {

   private final AuthService authService;

   @PostMapping("/login")
   public ResponseEntity<AuthResponse> login(@Valid @RequestBody AuthDTO authDTO) {
       log.info("Login request received for email: {}", authDTO.getEmail());
       AuthResponse authResponse = authService.login(authDTO);
       return ResponseEntity.ok(authResponse);
   }

   @PostMapping("/register")
   public ResponseEntity<UserDTO> register(@Valid @RequestBody UserDTO userDTO) {
       log.info("Registration request received for email: {}", userDTO.getEmail());
       UserDTO registeredUser = authService.register(userDTO);
       return ResponseEntity.status(HttpStatus.CREATED).body(registeredUser);
   }

   @PostMapping("/logout")
   public ResponseEntity<String> logout() {
       authService.logout();
       log.info("User logged out successfully");
       return ResponseEntity.ok("Logged out successfully");
   }
}
