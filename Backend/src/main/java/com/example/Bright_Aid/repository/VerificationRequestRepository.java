package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.VerificationRequest;

import org.springframework.data.jpa.repository.JpaRepository;

import org.springframework.stereotype.Repository;

@Repository
public interface VerificationRequestRepository extends JpaRepository<VerificationRequest, Integer> {


}