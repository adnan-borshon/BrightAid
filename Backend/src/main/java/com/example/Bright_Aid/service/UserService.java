package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.Dto.UserDTO;
import com.example.Bright_Aid.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    @Transactional(readOnly = true)
    public List<UserDTO> getAllUsers() {
        return userRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserById(Integer userId) {
        return userRepository.findById(userId)
                .map(this::convertToDTO);
    }

    @Transactional(readOnly = true)
    public Optional<UserDTO> getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .map(this::convertToDTO);
    }

    @Transactional
    public UserDTO createUser(UserDTO userDTO) {
        User user = convertToEntity(userDTO);
        User savedUser = userRepository.save(user);
        return convertToDTO(savedUser);
    }

    @Transactional
    public Optional<UserDTO> updateUser(Integer userId, UserDTO userDTO) {
        return userRepository.findById(userId)
                .map(existingUser -> {
                    existingUser.setEmail(userDTO.getEmail());
                    existingUser.setUsername(userDTO.getUsername());
                    existingUser.setPasswordHash(userDTO.getPasswordHash());
                    existingUser.setIsActive(userDTO.getIsActive());
                   if (userDTO.getUserType() != null) {
    existingUser.setUserType(User.UserType.valueOf(userDTO.getUserType()));
}
                    User updatedUser = userRepository.save(existingUser);
                    return convertToDTO(updatedUser);
                });
    }

    @Transactional
    public boolean deleteUser(Integer userId) {
        if (userRepository.existsById(userId)) {
            userRepository.deleteById(userId);
            return true;
        }
        return false;
    }

    @Transactional(readOnly = true)
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }
private UserDTO convertToDTO(User user) {
    return UserDTO.builder()
            .userId(user.getUserId())
            .email(user.getEmail())
            .username(user.getUsername())
            .passwordHash(user.getPasswordHash())
            .isActive(user.getIsActive())
            .userType(user.getUserType() != null ? user.getUserType().toString().toUpperCase() : null)
            .build();
}


    private User convertToEntity(UserDTO userDTO) {
            User.UserType userType = null;
    if (userDTO.getUserType() != null) {
        try {
            userType = User.UserType.valueOf(userDTO.getUserType().toUpperCase());
        } catch (IllegalArgumentException e) {
            // Optional: handle invalid value, e.g., throw exception or default to SCHOOL
            userType = User.UserType.SCHOOL;
        }
    }
  
        return User.builder()
            .email(userDTO.getEmail())
            .username(userDTO.getUsername())
            .passwordHash(userDTO.getPasswordHash())
            .isActive(userDTO.getIsActive() != null ? userDTO.getIsActive() : true)
            .userType(userType)
            .build();
    }
}