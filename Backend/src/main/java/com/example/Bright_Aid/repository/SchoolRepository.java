package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.School;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface SchoolRepository extends JpaRepository<School, Integer> {

    Optional<School> findByRegistrationNumber(String registrationNumber);

    List<School> findBySchoolNameContainingIgnoreCase(String name);

    List<School> findByStatus(School.SchoolStatus status);

    List<School> findByVerificationStatus(School.VerificationStatus verificationStatus);

    List<School> findBySchoolType(School.SchoolType schoolType);

    @Query("SELECT s FROM School s WHERE s.division.divisionId = :divisionId")
    List<School> findByDivisionId(@Param("divisionId") Integer divisionId);

    @Query("SELECT s FROM School s WHERE s.district.districtId = :districtId")
    List<School> findByDistrictId(@Param("districtId") Integer districtId);

    @Query("SELECT s FROM School s WHERE s.upazila.upazilaId = :upazilaId")
    List<School> findByUpazilaId(@Param("upazilaId") Integer upazilaId);

    @Query("SELECT s FROM School s WHERE s.verificationStatus = :status AND s.status = :schoolStatus")
    List<School> findByVerificationStatusAndStatus(
            @Param("status") School.VerificationStatus verificationStatus,
            @Param("schoolStatus") School.SchoolStatus schoolStatus
    );

    Optional<School> findByUser_UserId(Integer userId);

    boolean existsByRegistrationNumber(String registrationNumber);
}