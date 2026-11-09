
package com.example.mentalwellnessbackend.repository;

import com.example.mentalwellnessbackend.entity.Mood;
import com.example.mentalwellnessbackend.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MoodRepository extends JpaRepository<Mood, Long> {

    // Get all moods belonging to one user
    List<Mood> findByUserOrderByCreatedAtDesc(User user);

    // Get the latest 7 mood entries for one user
    List<Mood> findTop7ByUserOrderByCreatedAtDesc(User user);
}

