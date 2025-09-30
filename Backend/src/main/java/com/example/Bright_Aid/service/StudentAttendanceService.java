package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.StudentAttendanceDto;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.Entity.StudentAttendance;
import com.example.Bright_Aid.repository.StudentAttendanceRepository;
import com.example.Bright_Aid.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentAttendanceService {

    private final StudentAttendanceRepository attendanceRepository;
    private final StudentRepository studentRepository;

    // Save attendance
    public StudentAttendanceDto saveAttendance(StudentAttendanceDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found"));

        StudentAttendance attendance = StudentAttendance.builder()
                .student(student)
                .attendanceDate(dto.getAttendanceDate())
                .present(dto.getPresent())
                .absenceReason(dto.getAbsenceReason())
                .build();

        attendance = attendanceRepository.save(attendance);

        dto.setAttendanceId(attendance.getAttendanceId());
        return dto;
    }

    // Get all attendance for a student
    public List<StudentAttendanceDto> getAttendanceByStudent(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));

        return attendanceRepository.findByStudent(student)
                .stream()
                .map(sa -> new StudentAttendanceDto(
                        sa.getAttendanceId(),
                        sa.getStudent().getStudentId(),
                        sa.getAttendanceDate(),
                        sa.getPresent(),
                        sa.getAbsenceReason()
                ))
                .collect(Collectors.toList());
    }

    // Count present days
    public Long countPresentDays(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return attendanceRepository.countPresentDays(student);
    }

    // Count absent days
    public Long countAbsentDays(Integer studentId) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(() -> new RuntimeException("Student not found"));
        return attendanceRepository.countAbsentDays(student);
    }
}
