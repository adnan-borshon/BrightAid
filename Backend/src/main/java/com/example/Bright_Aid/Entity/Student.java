package com.example.Bright_Aid.Entity;

import lombok.*;
import lombok.experimental.SuperBuilder;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;

@Entity
@Table(name = "students")
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true, onlyExplicitlyIncluded = true)
public class Student extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "student_id")
    @EqualsAndHashCode.Include
    private Integer studentId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "school_id", nullable = false)
    @NotNull
    @ToString.Exclude
    private School school;

    @NotBlank
    @Column(name = "student_name", nullable = false)
    private String studentName;

    @NotBlank
    @Column(name = "student_id_number", unique = true, nullable = false)
    private String studentIdNumber;

    @Enumerated(EnumType.STRING)
    @Column(name = "gender")
    private Gender gender;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "father_name")
    private String fatherName;

    @Column(name = "father_alive", nullable = false)
    @Builder.Default
    private Boolean fatherAlive = true;

    @Column(name = "father_occupation")
    private String fatherOccupation;

    @Column(name = "mother_name")
    private String motherName;

    @Column(name = "mother_alive", nullable = false)
    @Builder.Default
    private Boolean motherAlive = true;

    @Column(name = "mother_occupation")
    private String motherOccupation;

    @Column(name = "guardian_phone")
    private String guardianPhone;

    @Lob
    @Column(name = "address")
    private String address;

    @Enumerated(EnumType.STRING)
    @Column(name = "class_level", nullable = false)
    private ClassLevel classLevel;

    @Column(name = "family_monthly_income", precision = 10, scale = 2)
    private BigDecimal familyMonthlyIncome;

    @Column(name = "has_scholarship", nullable = false)
    @Builder.Default
    private Boolean hasScholarship = false;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<StudentAttendance> attendanceRecords;

    @OneToMany(mappedBy = "student", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @ToString.Exclude
    private List<DropoutPrediction> dropoutPredictions;

    public enum Gender {
        MALE, FEMALE, OTHER
    }

    public enum ClassLevel {
        ONE("1"), TWO("2"), THREE("3"), FOUR("4"), FIVE("5"),
        SIX("6"), SEVEN("7"), EIGHT("8"), NINE("9"), TEN("10");

        private final String value;

        ClassLevel(String value) {
            this.value = value;
        }

        public String getValue() {
            return value;
        }
    }
}
