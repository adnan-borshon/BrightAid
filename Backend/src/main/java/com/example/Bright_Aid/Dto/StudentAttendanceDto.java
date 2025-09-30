package com.example.Bright_Aid.Dto;

import lombok.*;

import java.time.LocalDate;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class StudentAttendanceDto {

    private Integer attendanceId;
    private Integer studentId;      // reference to student
    private LocalDate attendanceDate;
    private Boolean present;
    private String absenceReason;
}
