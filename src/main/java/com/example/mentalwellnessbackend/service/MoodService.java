
package com.example.mentalwellnessbackend.service;

import com.example.mentalwellnessbackend.entity.Mood;
import com.example.mentalwellnessbackend.entity.User;
import com.example.mentalwellnessbackend.repository.MoodRepository;

import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class MoodService {

    private final MoodRepository moodRepository;

    public MoodService(MoodRepository moodRepository) {
        this.moodRepository = moodRepository;
    }

    // Save a new mood
    public Mood saveMood(
            User user,
            String mood,
            Integer moodScore,
            String note) {

        if (mood == null || mood.isBlank()) {
            throw new IllegalArgumentException("Mood is required.");
        }

        if (moodScore == null || moodScore < 1 || moodScore > 5) {
            throw new IllegalArgumentException(
                    "Mood score must be between 1 and 5."
            );
        }

        Mood moodEntry = new Mood();

        moodEntry.setUser(user);
        moodEntry.setMood(mood);
        moodEntry.setMoodScore(moodScore);
        moodEntry.setNote(note);

        return moodRepository.save(moodEntry);
    }

    // Get all moods for one user
    public List<Mood> getMoodHistory(User user) {
        return moodRepository.findByUserOrderByCreatedAtDesc(user);
    }

    // Get latest 7 moods for one user
    public List<Mood> getWeeklyMoods(User user) {
        return moodRepository.findTop7ByUserOrderByCreatedAtDesc(user);
    }
}

