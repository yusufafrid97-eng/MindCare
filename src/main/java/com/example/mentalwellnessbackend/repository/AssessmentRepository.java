package com.example.mentalwellnessbackend.repository;

import com.example.mentalwellnessbackend.entity.Assessment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AssessmentRepository extends JpaRepository<Assessment, Long> {
}
