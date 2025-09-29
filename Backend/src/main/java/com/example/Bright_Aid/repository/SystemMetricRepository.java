package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.SystemMetric;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface SystemMetricRepository extends JpaRepository<SystemMetric, Integer> {

    Optional<SystemMetric> findByMetricDate(LocalDate metricDate);

    List<SystemMetric> findByMetricDateBetween(LocalDate startDate, LocalDate endDate);

    List<SystemMetric> findByCalculatedAtBetween(LocalDateTime startDateTime, LocalDateTime endDateTime);

    List<SystemMetric> findByTotalActiveSchoolsGreaterThanEqual(Integer totalActiveSchools);

    List<SystemMetric> findByTotalFundsRaisedGreaterThanEqual(BigDecimal totalFundsRaised);

    List<SystemMetric> findByUtilizationRateGreaterThanEqual(BigDecimal utilizationRate);

    List<SystemMetric> findByHighRiskStudentsGreaterThanEqual(Integer highRiskStudents);

    List<SystemMetric> findByAverageAttendanceRateLessThan(BigDecimal averageAttendanceRate);

    @Query("SELECT sm FROM SystemMetric sm ORDER BY sm.metricDate DESC")
    List<SystemMetric> findAllOrderByMetricDateDesc();

    @Query("SELECT sm FROM SystemMetric sm WHERE sm.metricDate >= :startDate ORDER BY sm.metricDate DESC")
    List<SystemMetric> findRecentMetrics(@Param("startDate") LocalDate startDate);

    @Query("SELECT sm FROM SystemMetric sm WHERE sm.metricDate = (SELECT MAX(sm2.metricDate) FROM SystemMetric sm2)")
    Optional<SystemMetric> findLatestMetric();

    @Query("SELECT AVG(sm.utilizationRate) FROM SystemMetric sm WHERE sm.metricDate BETWEEN :startDate AND :endDate")
    BigDecimal getAverageUtilizationRate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT AVG(sm.averageAttendanceRate) FROM SystemMetric sm WHERE sm.metricDate BETWEEN :startDate AND :endDate")
    BigDecimal getAverageAttendanceRate(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT SUM(sm.totalFundsRaised) FROM SystemMetric sm WHERE sm.metricDate BETWEEN :startDate AND :endDate")
    BigDecimal getTotalFundsRaisedInPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);

    @Query("SELECT COUNT(sm) FROM SystemMetric sm WHERE sm.metricDate BETWEEN :startDate AND :endDate")
    Long countMetricsInPeriod(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}