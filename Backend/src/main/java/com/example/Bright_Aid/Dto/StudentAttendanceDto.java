package com.example.Bright_Aid.Dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class StudentAttendanceDto {

    private Integer attendanceId;

    @NotNull(message = "Student ID is required")
    private Integer studentId;

    @NotNull(message = "Attendance date is required")
    private LocalDate attendanceDate;

    @NotNull(message = "Present status is required")
    private Boolean present;

    private String absenceReason;

    private LocalDateTime recordedAt;

    @NotNull(message = "Recorded by user ID is required")
    private Integer recordedBy;
}