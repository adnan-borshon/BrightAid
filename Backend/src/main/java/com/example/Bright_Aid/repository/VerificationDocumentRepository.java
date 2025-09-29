package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.VerificationDocument;
import com.example.Bright_Aid.Entity.VerificationRequest;
import com.example.Bright_Aid.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;



@Repository
public interface VerificationDocumentRepository extends JpaRepository<VerificationDocument, Integer> {


}