package com.example.Bright_Aid.Dto;

import com.fasterxml.jackson.annotation.JsonProperty;

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
       @JsonProperty("userType")
    private String userType;


}