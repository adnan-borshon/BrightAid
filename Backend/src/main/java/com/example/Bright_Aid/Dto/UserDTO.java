package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.User.UserType;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {

    private Integer userId;
    private String email;
    private String username;
    private String passwordHash;
    private Boolean isActive;
    private UserType userType;


}