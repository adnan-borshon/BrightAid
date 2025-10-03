package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.NgoProjectRequests;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface NgoProjectRequestsRepository extends JpaRepository<NgoProjectRequests, Integer> {

    // Find all requests for a specific NGO project
    @Query(value = "SELECT * FROM ngo_project_requests WHERE ngo_project_id = :ngoProjectId", nativeQuery = true)
    List<NgoProjectRequests> findByNgoProjectId(@Param("ngoProjectId") Integer ngoProjectId);

    // Find all requests for a specific school
    @Query(value = "SELECT * FROM ngo_project_requests WHERE school_id = :schoolId", nativeQuery = true)
    List<NgoProjectRequests> findBySchoolId(@Param("schoolId") Integer schoolId);

    // Find all requests by status
    @Query(value = "SELECT * FROM ngo_project_requests WHERE status = :status", nativeQuery = true)
    List<NgoProjectRequests> findByStatus(@Param("status") String status);

    // Find requests within a date range
    @Query(value = "SELECT * FROM ngo_project_requests WHERE requested_at BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<NgoProjectRequests> findByRequestedAtBetween(@Param("startDate") LocalDateTime startDate,
                                                      @Param("endDate") LocalDateTime endDate);

    // Find all requests responded by a specific user
    @Query(value = "SELECT * FROM ngo_project_requests WHERE responded_by_user_id = :userId", nativeQuery = true)
    List<NgoProjectRequests> findByRespondedByUserId(@Param("userId") Integer userId);
}
