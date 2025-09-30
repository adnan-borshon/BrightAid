package com.example.Bright_Aid.Dto;

import com.example.Bright_Aid.Entity.Student.ClassLevel;
import com.example.Bright_Aid.Entity.Student.Gender;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentDto {

    private Integer studentId;
    private Integer schoolId; // to map School entity
    private String studentName;
    private String studentIdNumber;
    private Gender gender;
    private LocalDate dateOfBirth;
    private String fatherName;
    private Boolean fatherAlive;
    private String fatherOccupation;
    private String motherName;
    private Boolean motherAlive;
    private String motherOccupation;
    private String guardianPhone;
    private String address;
    private ClassLevel classLevel;
    private BigDecimal familyMonthlyIncome;
    private Boolean hasScholarship;
}
