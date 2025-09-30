package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private Integer studentId;



    @NotNull(message = "School ID is required")
    private Integer schoolId;

    @NotBlank(message = "Student name is required")
    private String studentName;

    @NotBlank(message = "Student ID number is required")
    private String studentIdNumber;

    private String gender;

    private LocalDate dateOfBirth;

    private String fatherName;

    @Builder.Default
    private Boolean fatherAlive = true;

    private String fatherOccupation;

    private String motherName;

    @Builder.Default
    private Boolean motherAlive = true;

    private String motherOccupation;

    private String guardianPhone;

    private String address;

    @NotNull(message = "Class level is required")
    private String classLevel;

    private BigDecimal familyMonthlyIncome;

    @Builder.Default
    private Boolean hasScholarship = false;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;
}