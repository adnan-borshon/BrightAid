package com.example.Bright_Aid.repository;
import com.example.Bright_Aid.Entity.DropoutPrediction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DropoutPredictionRepository extends JpaRepository<DropoutPrediction, Integer> {


}