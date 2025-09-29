package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.FundUtilization;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;



@Repository
public interface FundUtilizationRepository extends JpaRepository<FundUtilization, Integer> {


}