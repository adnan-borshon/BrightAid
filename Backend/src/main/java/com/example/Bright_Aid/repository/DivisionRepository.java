package com.example.Bright_Aid.repository;

import com.example.Bright_Aid.Entity.Division;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import jakarta.transaction.Transactional;

import java.util.Optional;

@Repository
public interface DivisionRepository extends JpaRepository<Division, Integer> {

    @Modifying
    @Transactional
    @Query(value = """
        INSERT INTO divisions (division_id, division_name, division_code) VALUES
        (1, 'Dhaka', 'DHK'),
        (2, 'Chattogram', 'CTG'),
        (3, 'Rajshahi', 'RAJ'),
        (4, 'Khulna', 'KHL'),
        (5, 'Barishal', 'BAR'),
        (6, 'Sylhet', 'SYL'),
        (7, 'Rangpur', 'RNG'),
        (8, 'Mymensingh', 'MYM')
        """, nativeQuery = true)
    void insertAllBangladeshDivisions();

    Optional<Division> findByDivisionName(String divisionName);
}
