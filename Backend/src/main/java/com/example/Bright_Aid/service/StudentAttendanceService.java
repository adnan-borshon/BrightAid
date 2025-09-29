package com.example.Bright_Aid.service;

import com.example.Bright_Aid.Dto.StudentAttendanceDto;
import com.example.Bright_Aid.Entity.Student;
import com.example.Bright_Aid.Entity.StudentAttendance;
import com.example.Bright_Aid.Entity.User;
import com.example.Bright_Aid.repository.StudentAttendanceRepository;
import com.example.Bright_Aid.repository.StudentRepository;
import com.example.Bright_Aid.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudentAttendanceService {

    private final StudentAttendanceRepository studentAttendanceRepository;
    private final StudentRepository studentRepository;
    private final UserRepository userRepository;

    @Transactional
    public StudentAttendanceDto createAttendance(StudentAttendanceDto dto) {
        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getStudentId()));

        User user = userRepository.findById(dto.getRecordedBy())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getRecordedBy()));

        StudentAttendance attendance = StudentAttendance.builder()
                .student(student)
                .attendanceDate(dto.getAttendanceDate())
                .present(dto.getPresent())
                .absenceReason(dto.getAbsenceReason())
                .recordedAt(LocalDateTime.now())
                .recordedBy(user)
                .build();

        StudentAttendance saved = studentAttendanceRepository.save(attendance);
        return convertToDto(saved);
    }

    @Transactional(readOnly = true)
    public StudentAttendanceDto getAttendanceById(Integer id) {
        StudentAttendance attendance = studentAttendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));
        return convertToDto(attendance);
    }

    @Transactional(readOnly = true)
    public List<StudentAttendanceDto> getAllAttendances() {
        return studentAttendanceRepository.findAll().stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public StudentAttendanceDto updateAttendance(Integer id, StudentAttendanceDto dto) {
        StudentAttendance attendance = studentAttendanceRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Attendance not found with id: " + id));

        Student student = studentRepository.findById(dto.getStudentId())
                .orElseThrow(() -> new RuntimeException("Student not found with id: " + dto.getStudentId()));

        User user = userRepository.findById(dto.getRecordedBy())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + dto.getRecordedBy()));

        attendance.setStudent(student);
        attendance.setAttendanceDate(dto.getAttendanceDate());
        attendance.setPresent(dto.getPresent());
        attendance.setAbsenceReason(dto.getAbsenceReason());
        attendance.setRecordedBy(user);

        StudentAttendance updated = studentAttendanceRepository.save(attendance);
        return convertToDto(updated);
    }

    @Transactional
    public void deleteAttendance(Integer id) {
        if (!studentAttendanceRepository.existsById(id)) {
            throw new RuntimeException("Attendance not found with id: " + id);
        }
        studentAttendanceRepository.deleteById(id);
    }

    private StudentAttendanceDto convertToDto(StudentAttendance attendance) {
        return StudentAttendanceDto.builder()
                .attendanceId(attendance.getAttendanceId())
                .studentId(attendance.getStudent().getStudentId())
                .attendanceDate(attendance.getAttendanceDate())
                .present(attendance.getPresent())
                .absenceReason(attendance.getAbsenceReason())
                .recordedAt(attendance.getRecordedAt())
                .recordedBy(attendance.getRecordedBy().getUserId())
                .build();
    }
}