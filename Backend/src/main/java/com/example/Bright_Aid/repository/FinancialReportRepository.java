package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.FinancialReport;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface FinancialReportRepository extends JpaRepository<FinancialReport, Integer> {


}