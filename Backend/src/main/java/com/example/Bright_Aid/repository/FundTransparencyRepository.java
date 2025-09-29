package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.FundTransparency;
import com.example.Bright_Aid.Entity.FundUtilization;
import com.example.Bright_Aid.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Repository
public interface FundTransparencyRepository extends JpaRepository<FundTransparency, Integer> {


}