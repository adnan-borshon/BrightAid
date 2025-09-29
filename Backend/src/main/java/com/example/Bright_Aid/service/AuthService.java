package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Dto.AuthDTO;
import com.example.Bright_Aid.Dto.AuthResponse;
import com.example.Bright_Aid.Dto.UserDTO;
import com.example.Bright_Aid.Security.JwtUtil;
import com.example.Bright_Aid.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
@Transactional
public class AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;

    public AuthResponse login(AuthDTO authDTO) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(authDTO.getEmail(), authDTO.getPassword())
            );
            
            UserDetails userDetails = (UserDetails) authentication.getPrincipal();
            String jwtToken = jwtUtil.generateToken(userDetails);

            User user = userRepository.findByEmail(authDTO.getEmail())
                    .orElseThrow(() -> new RuntimeException("User not found"));

            return AuthResponse.builder()
                    .token(jwtToken)
                    .user(mapToDTO(user))
                    .build();

        } catch (BadCredentialsException e) {
            throw new RuntimeException("Invalid email or password");
        }
    }

    public UserDTO register(UserDTO userDTO) {
        if (userRepository.existsByEmail(userDTO.getEmail())) {
            throw new RuntimeException("Email already exists: " + userDTO.getEmail());
        }

        // Encode password before creating user
        userDTO.setPasswordHash(passwordEncoder.encode(userDTO.getPasswordHash()));
        
        // Use UserService which has proper userType conversion
        return userService.createUser(userDTO);
    }

    public void logout() {
        SecurityContextHolder.clearContext();
    }

    private UserDTO mapToDTO(User user) {
        return UserDTO.builder()
                .userId(user.getUserId())
                .email(user.getEmail())
                .username(user.getUsername())
                .isActive(user.getIsActive())
                .userType(user.getUserType() != null ? user.getUserType().name() : null)
                .build();
    }
}
