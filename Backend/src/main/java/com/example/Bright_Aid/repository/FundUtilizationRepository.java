package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.FundUtilization;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FundUtilizationRepository extends JpaRepository<FundUtilization, Integer> {

    // Native query to get fund utilization by donor ID
    @Query(value = """
        SELECT fu.* FROM fund_utilization fu
        INNER JOIN donations d ON fu.donation_id = d.donation_id
        WHERE d.donor_id = :donorId
        ORDER BY fu.utilization_date DESC
        """, nativeQuery = true)
    List<FundUtilization> findByDonorId(@Param("donorId") Integer donorId);

    // Native query to get fund utilization by NGO ID (through projects)
    @Query(value = """
        SELECT fu.* FROM fund_utilization fu
        INNER JOIN school_projects sp ON fu.project_id = sp.project_id
        WHERE sp.ngo_id = :ngoId
        ORDER BY fu.utilization_date DESC
        """, nativeQuery = true)
    List<FundUtilization> findByNgoId(@Param("ngoId") Integer ngoId);

    // Find by project ID
    List<FundUtilization> findByProjectProjectId(Integer projectId);

    // Native query to get fund utilization by school ID
    @Query(value = """
        SELECT fu.* FROM fund_utilization fu
        INNER JOIN school_projects sp ON fu.project_id = sp.project_id
        WHERE sp.school_id = :schoolId
        ORDER BY fu.utilization_date DESC
        """, nativeQuery = true)
    List<FundUtilization> findBySchoolId(@Param("schoolId") Integer schoolId);

    // Native query to get total amount used by donor
    @Query(value = """
        SELECT COALESCE(SUM(fu.amount_used), 0) FROM fund_utilization fu
        INNER JOIN donations d ON fu.donation_id = d.donation_id
        WHERE d.donor_id = :donorId
        """, nativeQuery = true)
    Double getTotalAmountUsedByDonor(@Param("donorId") Integer donorId);

    // Native query to get total amount used by NGO
    @Query(value = """
        SELECT COALESCE(SUM(fu.amount_used), 0) FROM fund_utilization fu
        INNER JOIN school_projects sp ON fu.project_id = sp.project_id
        WHERE sp.ngo_id = :ngoId
        """, nativeQuery = true)
    Double getTotalAmountUsedByNgo(@Param("ngoId") Integer ngoId);
}