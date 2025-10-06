package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Ngo;
import com.example.Bright_Aid.Entity.Ngo.VerificationStatus;
import com.example.Bright_Aid.Entity.User;
// import com.example.Bright_Aid.Entity.Admin;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface NgoRepository extends JpaRepository<Ngo, Integer> {


}